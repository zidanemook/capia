package com.capia.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.capia.game.Capia;


public class MainMenuScreen implements Screen {

    final Capia game;
    Stage stage;
    TextButton singlePlayButton, multiPlayButton, exitButton;
    Skin skin;

    Table table;

    public MainMenuScreen(final Capia game) {
        this.game = game;
        SetUpUI(game);
    }

    private void SetUpUI(final Capia game) {
        skin = new Skin(Gdx.files.internal("skin/uiskin.json")); // Point this to your skin file

        stage = new Stage();
        Gdx.input.setInputProcessor(stage); // Make the stage consume events

        table = new Table();
        table.setFillParent(true); // Makes the table fill the whole stage
        table.pad(100); // Apply padding of 10 units to all sides
        table.align(Align.left | Align.center); // Align the table's contents to the left and center vertically
        table.defaults().width(120).height(50); // Default button size
        stage.addActor(table);
        table.setDebug(true); // This is optional, but enables debug lines for tables.


        singlePlayButton = new TextButton("Single Play", skin);
        //singlePlayButton.setPosition(300, 350);
        singlePlayButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                final Dialog dialog = new Dialog("Single Play Options", skin);
                dialog.text("Select an option:");

                // Add "Start new game" button
                dialog.button("Start new game", true); // Passing 'true' here means that this button is the default button.
                dialog.getButtonTable().getCells().peek().getActor().addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        //game.setScreen(new GameScreen(game));
                        dialog.hide(); // Close the dialog
                    }
                });

                dialog.button("Load game", "loadGame");
                // Add "Close" button
                dialog.button("Close", false); // Passing 'false' here means that this button is not the default button.
                dialog.getButtonTable().getCells().peek().getActor().addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        dialog.hide(); // Close the dialog
                    }
                });

                dialog.show(stage);
            }
        });
        table.add(singlePlayButton).padBottom(50); // Add padding below the button
        table.row(); // Add a new row to the table, placing the next element below the current one

        multiPlayButton = new TextButton("Multi Play", skin);
        multiPlayButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MultiMenuScreen(game));

            }
        });
        table.add(multiPlayButton).padBottom(50);;
        table.row();

        exitButton = new TextButton("Exit", skin);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        table.add(exitButton);

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
        //game.font.draw(game.batch, "Welcome to Capia!!! ", 300, 350);
        //game.font.draw(game.batch, "Tap anywhere to begin!", 300, 300);
        game.batch.end();

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f)); // Update the stage
        stage.draw(); // Draw the stage

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
        //stage.getViewport().update(width, height, true);
    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }


    //...Rest of class omitted for succinctness.

}