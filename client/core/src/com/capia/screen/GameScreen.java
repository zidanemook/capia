package com.capia.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import com.capia.dto.ResponseMessage;
import com.capia.dto.map.CityInfoDTO;
import com.capia.dto.map.RoadDataDTO;
import com.capia.dto.world.GenerateWorldResponse;
import com.capia.game.Capia;
import com.capia.input.MyInputProcessor;
import com.capia.model.map.CityInfo;
import com.capia.model.map.QuadTree;
import com.capia.model.map.RoadData;
import com.capia.model.map.TileMap;
import com.capia.model.world.World;
import com.capia.ui.dialog.InGameDialog;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GameScreen implements Screen {

    private final Capia game;
    private World world;

    private int selectCity;

    private final MyInputProcessor inputProcessor;

    private final ObjectMapper objectMapper;

    private Stage stage; // For displaying the dialog
    private InGameDialog inGameDialog;

    private InputMultiplexer multiplexer;

    private final float zoomoutLimit = 8.0f;

    private final float zoominLimit = 0.5f;

    private Skin skin;
    public GameScreen(final Capia game, ResponseMessage responseMessage)
    {
        this.game = game;
        selectCity = 0;

        objectMapper = new ObjectMapper();
        // The payload should be a list of MapResponse objects

        SetUpWorld(responseMessage);

        SetUpUI(game);

        this.inputProcessor = new MyInputProcessor();
        this.multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage); // Make sure stage is added before MyInputProcessor
        multiplexer.addProcessor(inputProcessor);
        Gdx.input.setInputProcessor(multiplexer);
    }

    private void SetUpWorld(ResponseMessage responseMessage) {
        GenerateWorldResponse generateWorldResponse = objectMapper.convertValue(responseMessage.getPayload(), new TypeReference<GenerateWorldResponse>() {});
        world = new World();
        world.setName(generateWorldResponse.getName());

        TileMap tileMap = new TileMap(generateWorldResponse.getHeight(), generateWorldResponse.getWidth());

        world.setTileMap(tileMap);

        List<CityInfoDTO> cities = generateWorldResponse.getCities();
        for(CityInfoDTO cityInfoDTO : cities)
        {
            List<RoadDataDTO> roadDataDTOList = cityInfoDTO.getRoadDataList();


            List<RoadData> roadDataList = roadDataDTOList.stream().map(dto -> {
                RoadData roadData = RoadData.builder()
                        .x(dto.getX())
                        .y(dto.getY())
                        .width(dto.getWidth())
                        .height(dto.getHeight())
                        .build();
                return roadData;
            }).collect(Collectors.toList());

            tileMap.UpdateRoadTileTexture(new ArrayList<>(roadDataList));

            CityInfo cityInfo = CityInfo.builder()
                    .name(cityInfoDTO.getName())
                    .x(cityInfoDTO.getX())
                    .y(cityInfoDTO.getY())
                    .radius(cityInfoDTO.getRadius())
                    .population(cityInfoDTO.getPopulation())
                    .spendingLevel(cityInfoDTO.getSpendingLevel())
                    .salaryLevel(cityInfoDTO.getSalaryLevel())
                    .economicClimate(cityInfoDTO.getEconomicClimate())
                    .world(world)
                    .roadDataList(roadDataList)
                    .build();

            world.getCityInfoList().add(cityInfo);
        }
    }


    private QuadTree convertToQuadTree(RoadDataDTO roadDataDTO) {
        QuadTree quadTree = new QuadTree();
        quadTree.setX(roadDataDTO.getX());
        quadTree.setY(roadDataDTO.getY());
        quadTree.setWidth(roadDataDTO.getWidth());
        quadTree.setHeight(roadDataDTO.getHeight());
        return quadTree;
    }

    private void SetUpUI(final Capia game) {
        this.skin = new Skin(Gdx.files.internal("skin/uiskin.json")); // Point this to your skin file
        this.stage = new Stage();
        this.inGameDialog = new InGameDialog("Menu", skin, result -> {
            switch (result) {
                case RETURN_TO_MAIN:
                    game.setScreen(new MainMenuScreen(game));
                    break;
                case EXIT_GAME:
                    Gdx.app.exit();
                    break;
                case CANCEL:
                    // 알맞은 동작을 취합니다...
                    break;
            }
        });
    }



    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        //GAME
        ScreenUtils.clear(0, 0, 0.2f, 1);
        game.camera.update();

        game.batch.setProjectionMatrix(game.camera.combined);
        game.batch.begin();
        world.render(game, selectCity);
        game.batch.end();

        //UI
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f)); // Update the stage
        stage.draw(); // Draw the stage


        //INPUT
        inputProcess();
        updateProcess();

    }

    private void updateProcess() {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            //stage.addActor(inGameDialog);
             // Show the dialog

            inGameDialog.show(stage);


            // Handle the dialog result
//            switch (inGameDialog.getResult()) {
//                case RETURN_TO_MAIN:
//                    game.setScreen(new MainMenuScreen(game));
//                    break;
//                case EXIT_GAME:
//                    Gdx.app.exit();
//                    break;
//                case CANCEL:
//                    inGameDialog.hide();
//                    break;
//            }
        }
    }

    private void inputProcess() {

        CameraMoveByMouse();
        CameraZoomByScroll();
        inputProcessor.update();

    }

    private void CameraMoveByMouse() {
        final int speed = 400;
        final int edgeTolerance = 20;

        // Check if mouse is touching the edge of the screen
//        if (inputProcessor.isMouseNearEdge()) {
//            int screenWidth = Gdx.graphics.getWidth();
//            int screenHeight = Gdx.graphics.getHeight();
//            float directionX = 0;
//            float directionY = 0;
//
//            if (inputProcessor.getMousePosition().x <= edgeTolerance) {
//                directionX = -1;
//            } else if (inputProcessor.getMousePosition().x >= screenWidth - edgeTolerance) {
//                directionX = 1;
//            }
//
//            if (inputProcessor.getMousePosition().y <= edgeTolerance) {
//                directionY = 1;
//            } else if (inputProcessor.getMousePosition().y >= screenHeight - edgeTolerance) {
//                directionY = -1;
//            }
//
//            game.camera.translate(speed * Gdx.graphics.getDeltaTime() * directionX,
//                    speed * Gdx.graphics.getDeltaTime() * directionY,
//                    0);
//        }

        // Check if right mouse button is pressed
        if (inputProcessor.isRightMouseButtonPressed()) {
            Vector2 dragAmount = inputProcessor.getDragAmount();

            // Invert the direction to make camera move with the drag
            float directionX = -dragAmount.x;
            float directionY = dragAmount.y;

            // Modify the direction with the zoom level
            directionX *= game.camera.zoom;
            directionY *= game.camera.zoom;

            game.camera.translate(directionX, directionY, 0);
        }
    }


    private void CameraZoomByScroll() {
        float newZoom = game.camera.zoom + inputProcessor.getScrollAmount() * 0.5f;
        game.camera.zoom = Math.max(zoominLimit, Math.min(newZoom, zoomoutLimit));
        inputProcessor.resetScrollAmount();
    }


    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

        // Dispose the tile map
        world.dispose();

        stage.dispose();
        skin.dispose();
    }

}
