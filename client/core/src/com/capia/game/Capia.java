package com.capia.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.capia.screen.MainMenuScreen;

public class Capia extends Game {

    public SpriteBatch batch;
    public BitmapFont font;

    public OrthographicCamera camera;

    private Screen currentScreen;

    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont(); // use libGDX's default Arial font

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1280, 720);

        this.setScreen(new MainMenuScreen(this));
    }

    public void render() {
        super.render(); // important!
    }

    //x 버튼 누르면 여기로와서 Screen에서 사용한 리소스 해제함.
    public void dispose() {
        batch.dispose();
        font.dispose();
        if (currentScreen != null) {
            currentScreen.dispose();
        }
        super.dispose();
    }

    @Override
    public void setScreen(Screen screen) {
        if (currentScreen != null) {
            currentScreen.dispose();
        }
        currentScreen = screen;
        super.setScreen(screen);
    }
}