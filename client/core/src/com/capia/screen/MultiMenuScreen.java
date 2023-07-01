package com.capia.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.capia.dto.RequestMessage;
import com.capia.dto.ResponseMessage;
import com.capia.dto.world.ConnectWorldRequest;
import com.capia.dto.world.DeleteWorldRequest;
import com.capia.dto.world.GenerateWorldRequest;
import com.capia.dto.world.WorldListRequest;
import com.capia.game.Capia;
import com.capia.ui.dialog.LoginDialog;
import com.capia.websocket.GameWebSocketClient;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MultiMenuScreen implements Screen {

    private final Capia game;
    //table
    private Stage stage;
    private Skin skin;
    private Table table;


    private SelectBox<Integer> worldSizeSelectBox;
    private TextField worldNameField;
    private TextButton generateWorldButton;
    private ScrollPane scrollPane;

    private TextField ipField;
    private TextButton connectToOuterButton;
    private TextButton backButton;

    //table1
    private Table table1;
    private TextButton connectButton;
    private TextButton deleteButton;

    //network
    private ObjectMapper objectMapper;
    private GameWebSocketClient gameWebSocketClient;
    private ConcurrentLinkedQueue<ResponseMessage> messageQueue;

    private LoginDialog loginDialog;

    MultiMenuScreen(final Capia game)
    {
        this.game = game;
        gameWebSocketClient = new GameWebSocketClient(URI.create("ws://localhost:8080")){
            @Override
            public void onOpen(ServerHandshake handshakeData) {
                super.onOpen(handshakeData);
                sendListMap();
            }
        };

        try{
            gameWebSocketClient.connect();
        }catch(Exception e)
        {
            System.out.println(e.toString());
            //game.setScreen(new MainMenuScreen(game));
        }
        gameWebSocketClient.setOnMessageCallback(this::MessageHandling);
        this.messageQueue = new ConcurrentLinkedQueue<>();
        objectMapper = new ObjectMapper();

        SetUpUI(game);
        game.camera = new OrthographicCamera();
        game.camera.setToOrtho(false, 1280, 720);

    }

    public void sendListMap() {
        //서버로부터 맵 목록 요청하기
        WorldListRequest request = new WorldListRequest();
        RequestMessage message = new RequestMessage();
        message.setType("WORLD");
        message.setSubType("LIST");
        message.setPayload(request);

        try {
            String requestJson = objectMapper.writeValueAsString(message);
            gameWebSocketClient.send(requestJson);  // "this" refers to the WebSocket client
        } catch (Exception e) {
            System.out.println(e.toString());
            game.setScreen(new MainMenuScreen(game));
        }
    }

    private void SetUpUI(final Capia game) {

        skin = new Skin(Gdx.files.internal("skin/uiskin.json")); // Point this to your skin file
        stage = new Stage();
        Gdx.input.setInputProcessor(stage); // Make the stage consume events

        table = new Table();
        table.padLeft(100);
        table.padTop(160);
        table.setFillParent(true);
        stage.addActor(table);
        table.setDebug(true);

        table.align(Align.left); // Align the table's contents to the left and center vertically
        table.defaults().width(200).height(50); // Default button size

        //월드입력 위에 월드크기, 도시밀도, AI개수, 초기자금

        Integer[] worldSizes = new Integer[]{200, 500, 1000, 2000, 4000, 8000, 16000};
        worldSizeSelectBox = new SelectBox<>(skin);
        worldSizeSelectBox.setItems(worldSizes);
        worldSizeSelectBox.setSelected(512);

        // Listener를 사용해 선택된 월드 크기 값 변경을 감지하고 필요한 액션 수행
        worldSizeSelectBox.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {

            }
        });

        worldNameField = new TextField("", skin);
        worldNameField.setMessageText("Enter World Name");

        // Add a filter to only allow alphanumeric input and limit to 30 characters
        worldNameField.setTextFieldFilter(new TextField.TextFieldFilter() {
            private final String regex = "^[a-zA-Z]*$";

            @Override
            public boolean acceptChar(TextField textField, char c) {
                // Allow only if the total characters would be 30 or less, and the character is alphanumeric
                return (textField.getText().length() < 30) && String.valueOf(c).matches(regex);
            }
        });

        // Add a listener to enable/disable the button depending on whether the input is valid
        worldNameField.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                int textLength = worldNameField.getText().length();
                if (textLength >= 5 && textLength <= 30) {
                    generateWorldButton.setDisabled(false);
                } else {
                    generateWorldButton.setDisabled(true);
                }
            }
        });

        generateWorldButton = new TextButton("Generate World", skin);
        generateWorldButton.setDisabled(true);
        generateWorldButton.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                GenerateWorldRequest request = new GenerateWorldRequest();
                request.setName(worldNameField.getText());
                Integer selected = worldSizeSelectBox.getSelected();
                request.setWidth(selected);
                request.setHeight(selected);
                generateWorldButton.setDisabled(true);

                RequestMessage message = new RequestMessage();
                message.setType("WORLD");
                message.setSubType("GENERATE");
                message.setPayload(request);

                try {
                    String requestJson = objectMapper.writeValueAsString(message);
                    gameWebSocketClient.send(requestJson);
                } catch (Exception e) {
                    System.out.println(e.toString());
                    game.setScreen(new MainMenuScreen(game));
                    generateWorldButton.setDisabled(false);
                }

            }
        });


        ipField = new TextField("", skin);
        ipField.setMessageText("Enter IP Address");

        connectToOuterButton = new TextButton("Connect to outer world", skin);
        connectToOuterButton.setDisabled(true);  // Initially disabled
        connectToOuterButton.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                //IP 연결이 완료되면 LoginDialog 띄우면 될듯
                // TODO: Connect to the server with the provided ID and password

            }
        });

        backButton = new TextButton("Back to main", skin);
        //backButton.setPosition(300, 250);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenuScreen(game));
            }
        });


        table.add(new Label("World Size:", skin));
        table.row();
        table.add(worldSizeSelectBox).row();
        table.row();
        table.add(new Label("", skin));
        table.row();
        table.add(worldNameField);
        table.row();
        table.add(generateWorldButton);
        table.row();
        table.add(new Label("", skin));
        table.row();
        table.add(ipField);
        table.row();
        table.add(connectToOuterButton);
        table.row();
        table.add(new Label("", skin));
        table.row();
        table.add(backButton);



        //table1  에 넣어야 할것들

        List<String> list = new List<>(skin);
        scrollPane = new ScrollPane(list, skin);
        // 접속과 삭제 버튼을 생성합니다.
        connectButton = new TextButton("Connect", skin);
        connectButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                loginDialog.show(stage);
            }
        });

        deleteButton = new TextButton("Delete", skin);
        deleteButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                DeleteWorldRequest request = new DeleteWorldRequest();
                List<String> list = ((List<String>) scrollPane.getActor());
                request.setName(list.getSelected());

                RequestMessage message = new RequestMessage();
                message.setType("WORLD");
                message.setSubType("DELETE");
                message.setPayload(request);

                try {
                    String requestJson = objectMapper.writeValueAsString(message);
                    gameWebSocketClient.send(requestJson);
                } catch (Exception e) {
                    System.out.println(e.toString());
                }
            }
        });

        // Table1에 위젯을 추가합니다.
        table1 = new Table();
        stage.addActor(table1);
        table1.setFillParent(true);
        table1.setDebug(true);
        table1.align(Align.right); // Align the table's contents to the left and center vertically
        table1.defaults().width(200).height(50);

        table1.add(scrollPane).width(400).height(400);
        table1.row();
        table1.add(new Label("", skin));
        table1.row();
        table1.add(connectButton); // 버튼을 각각의 열에 추가합니다.
        table1.add(deleteButton);


        //Dialog

        this.loginDialog = new LoginDialog("Menu", skin, (result, id, pasasword )-> {

            switch (result) {
                case CONNECT:

                    ConnectWorldRequest request = new ConnectWorldRequest();
                    List<String> templist = ((List<String>) scrollPane.getActor());
                    request.setWorldName(templist.getSelected());//값이 있는경우 호스트 클라
                    request.setUserName(id);
                    request.setPassword(pasasword);

                    RequestMessage message = new RequestMessage();
                    message.setType("WORLD");
                    message.setSubType("CONNECT");
                    message.setPayload(request);

                    try {
                        String requestJson = objectMapper.writeValueAsString(message);
                        gameWebSocketClient.send(requestJson);
                    } catch (Exception e) {
                        System.out.println(e.toString());
                    }

                    break;
                case CANCEL:
                    // 알맞은 동작을 취합니다...
                    break;
            }
        });
    }

    public void MessageHandling(ResponseMessage responseMessage) {

        //gameWebSocketClient 에서는 별개의 쓰레드에서 메시지를 받는다 여기에 콜백함수를 호출하도록 했었는데 opengl은 별개의 쓰레드에서
        //텍스쳐를 불러오지 못하는 문제가 발생해서 메시지를 메시지큐에 저장하고 Render에서 사용하는 방식으로 변환하였다.
        messageQueue.add(responseMessage);

//        if ("WORLD".equals(responseMessage.getType()) && "GENERATE".equals(responseMessage.getSubType())) {
//            game.setScreen(new GameScreen(game, responseMessage));
//        }
    }

    private void ProcessQueue() {

        while (!messageQueue.isEmpty()) {
            ResponseMessage responseMessage = messageQueue.poll();

            if ("WORLD".equals(responseMessage.getType()) ) {

                //GENERATE 하면 리스트를 보여주는것으로 변경
                //if("GENERATE".equals(responseMessage.getSubType()))
                //{
                    //game.setScreen(new GameScreen(game, responseMessage));
                //}
                if("LIST".equals(responseMessage.getSubType()))
                {
                    if(responseMessage.getStatus().equals("FAIL")){
                        String errorMessage = (String) responseMessage.getPayload();

                        Dialog dialog = new Dialog("Error", skin);
                        dialog.text(errorMessage);
                        dialog.button("OK");
                        dialog.show(stage);
                    }
                    else{
                        ArrayList<String> mapResponses = objectMapper.convertValue(responseMessage.getPayload(), new TypeReference<ArrayList<String>>() {});

                        List<String> list = (List<String>) scrollPane.getActor();
                        list.clearItems();
                        list.setItems(mapResponses.toArray(new String[0])); // List에 항목을 추가합니다.

                        scrollPane.setFadeScrollBars(false);

                    }

                    generateWorldButton.setDisabled(false);
                }
                else if("CONNECT".equals(responseMessage.getSubType()))
                {
                    game.setScreen(new GameScreen(game, responseMessage));
                }
                else if("DELETE".equals(responseMessage.getSubType()))
                {
                    sendListMap();
                }
            }

        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        ScreenUtils.clear(0, 0, 0.2f, 1);

        game.camera.update();
        game.batch.setProjectionMatrix(game.camera.combined);

        game.batch.begin();

        game.batch.end();

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f)); // Update the stage
        stage.draw(); // Draw the stage


        ProcessQueue();

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
        gameWebSocketClient.close();
        gameWebSocketClient = null;
        stage.dispose();
        skin.dispose();
    }
}
