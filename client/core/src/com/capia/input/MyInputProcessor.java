package com.capia.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class MyInputProcessor implements InputProcessor {

    private final int edgeTolerance = 20;
    private Vector3 mousePosition = new Vector3();

    private Vector3 lastMousePosition = new Vector3();

    private int scrollAmount = 0;

    private boolean isEscPressed = false;

    //Drag
    private Vector2 dragAmount = new Vector2();
    private boolean isRightMouseButtonPressed = false;
    private int lastScreenX, lastScreenY;

    public MyInputProcessor() {
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.RIGHT) {
            isRightMouseButtonPressed = true;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        if (button == Input.Buttons.RIGHT) {
            isRightMouseButtonPressed = false;
            dragAmount.setZero();
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (isRightMouseButtonPressed) {
            // 마우스가 움직였는지 확인
            if (lastMousePosition.x != screenX || lastMousePosition.y != screenY) {
                dragAmount.x = screenX - mousePosition.x;
                dragAmount.y = screenY - mousePosition.y;
            }
        }
        lastMousePosition.set(screenX, screenY, 0);
        mousePosition.set(screenX, screenY, 0);
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        mousePosition.set(screenX, screenY, 0);
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        scrollAmount += amountY;  // Scroll amount is typically 1 or -1
        return true;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE) {
            isEscPressed = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {

        if (keycode == Input.Keys.ESCAPE) {
            isEscPressed = false;
            return true;
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    public Vector3 getMousePosition() {
        return mousePosition;
    }

    public boolean isMouseNearEdge() {
        int screenWidth = com.badlogic.gdx.Gdx.graphics.getWidth();
        int screenHeight = com.badlogic.gdx.Gdx.graphics.getHeight();

        return mousePosition.x <= edgeTolerance ||
                mousePosition.y <= edgeTolerance ||
                mousePosition.x >= screenWidth - edgeTolerance ||
                mousePosition.y >= screenHeight - edgeTolerance;
    }

    public int getScrollAmount() {
        return scrollAmount;
    }

    public void resetScrollAmount() {
        scrollAmount = 0;
    }

    public boolean isEscPressed() {
        return isEscPressed;
    }

    public boolean isRightMouseButtonPressed() {
        return isRightMouseButtonPressed;
    }

    public Vector2 getDragAmount() {
        return new Vector2(dragAmount);
    }

    public void update(){

        if (!mousePosition.equals(lastMousePosition)) {

        } else {
            // 마우스 위치가 이전 프레임과 같은 경우

            if (isRightMouseButtonPressed) {
                dragAmount.setZero();
            }
        }

    }
}
