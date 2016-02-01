package Selection;

import javafx.scene.Scene;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

/**
 * addes a mouse handler
 * Created by huson on 12/8/15.
 */
public class MouseHandler {
    private double mouseDownX;
    private double mouseDownY;

    /**
     * add a mouse handler to a scene
     *
     * @param scene
     * @param cameraRotateX
     * @param cameraRotateY
     * @param cameraTranslate
     */
    public static void addMouseHanderToScene(Scene scene, Rotate cameraRotateX, Rotate cameraRotateY, Translate cameraTranslate) {
        new MouseHandler(scene, cameraRotateX, cameraRotateY, cameraTranslate);
    }

    /**
     * handle mouse events
     *
     * @param scene
     * @param cameraTranslate
     */
    private MouseHandler(Scene scene, Rotate cameraRotateX, Rotate cameraRotateY, Translate cameraTranslate) {
        scene.setOnMousePressed((me) -> {
            mouseDownX = me.getSceneX();
            mouseDownY = me.getSceneY();
        });
        scene.setOnMouseDragged((me) -> {
            double mouseDeltaX = me.getSceneX() - mouseDownX;
            double mouseDeltaY = me.getSceneY() - mouseDownY;

            if (me.isShiftDown()) { // move in or out
                cameraTranslate.setZ(cameraTranslate.getZ() + mouseDeltaY);
            } else // rotate
            {
                cameraRotateY.setAngle(cameraRotateY.getAngle() + mouseDeltaX);
                cameraRotateX.setAngle(cameraRotateX.getAngle() - mouseDeltaY);
            }
            mouseDownX = me.getSceneX();
            mouseDownY = me.getSceneY();
        });
    }

}
