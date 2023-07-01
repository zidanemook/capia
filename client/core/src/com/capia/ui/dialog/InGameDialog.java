package com.capia.ui.dialog;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import lombok.Getter;

@Getter
public class InGameDialog extends Dialog {

    public interface ResultListener {
        void onResult(Result result);
    }
    private ResultListener listener;

    public enum Result {
        NONE,
        RETURN_TO_MAIN,
        EXIT_GAME,
        CANCEL
    }

    public InGameDialog(String title, Skin skin, ResultListener listener) {
        super(title, skin);
        this.listener = listener;

        // add "Connect" button
        TextButton ReturnButton = new TextButton("Return to MainMenu", skin);
        ReturnButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                listener.onResult(Result.RETURN_TO_MAIN);
                hide(); // Close the dialog
            }
        });

        TextButton exitButton = new TextButton("Exit Game", skin);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                listener.onResult(Result.EXIT_GAME);
                hide(); // Close the dialog
            }
        });

        // add "Close" button
        TextButton cancelButton = new TextButton("Cancel", skin);
        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                listener.onResult(Result.CANCEL);
                hide(); // Close the dialog
            }
        });

        // add buttons to dialog
        getButtonTable().add(ReturnButton).width(200).pad(10);
        getButtonTable().row();
        getButtonTable().add(exitButton).width(200).pad(10);
        getButtonTable().row();
        getButtonTable().add(cancelButton).width(200).pad(10);
    }

    // other necessary methods and overrides can go here
}
