package com.capia.ui.dialog;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class LoginDialog extends Dialog {

    private TextField idField;
    private TextField passwordField;

    public interface ResultListener {
        void onResult(LoginDialog.Result result, String id, String password);
    }
    private LoginDialog.ResultListener listener;

    public enum Result {
        CANCEL,
        CONNECT,
    }

    public LoginDialog(String title, Skin skin, LoginDialog.ResultListener listener) {
        super(title, skin);

        idField = new TextField("", skin);

        passwordField = new TextField("", skin);
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');

        // add text fields to dialog
        getContentTable().row();
        getContentTable().add("ID:").pad(10);
        getContentTable().add(idField).width(200).pad(10);
        getContentTable().row();
        getContentTable().add("Password:").pad(10);
        getContentTable().add(passwordField).width(200).pad(10);

        // add "Connect" button
        TextButton connectButton = new TextButton("Connect", skin);
        connectButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String id = idField.getText();
                String password = passwordField.getText();

                listener.onResult(LoginDialog.Result.CONNECT, idField.getText(), passwordField.getText());

                hide(); // Close the dialog
            }
        });

        // add "Close" button
        TextButton closeButton = new TextButton("Cancel", skin);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                listener.onResult(LoginDialog.Result.CANCEL, "", "");
                hide(); // Close the dialog
            }
        });

        // add buttons to dialog
        getButtonTable().add(connectButton);
        getButtonTable().add(closeButton);
    }

    // other necessary methods and overrides can go here
}
