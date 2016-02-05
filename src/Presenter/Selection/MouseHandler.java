package Presenter.Selection;

import javafx.scene.Group;
import javafx.scene.layout.Pane;
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
     * @param pane
     * @param cameraRotateX
     * @param cameraRotateY
     * @param cameraTranslate
     */
    public static void addMouseHanderToPane(Pane pane, Rotate cameraRotateX, Rotate cameraRotateY, Translate cameraTranslate) {
        new MouseHandler(pane, cameraRotateX, cameraRotateY, cameraTranslate);
    }

    /**
     * handle mouse events
     * @param pane
     * @param cameraTranslate
     */
    private MouseHandler(Pane pane, Rotate cameraRotateX, Rotate cameraRotateY, Translate cameraTranslate) {
        pane.setOnMousePressed((me) -> {
            mouseDownX = me.getSceneX();
            mouseDownY = me.getSceneY();
        });
        pane.setOnMouseDragged((me) -> {
            double mouseDeltaX = me.getSceneX() - mouseDownX;
            double mouseDeltaY = me.getSceneY() - mouseDownY;

            if (me.isShiftDown()) {
                cameraTranslate.setZ(cameraTranslate.getZ() + 5*mouseDeltaY);
            }
            else if (me.isAltDown()){
                cameraTranslate.setX(cameraTranslate.getX() + 5*mouseDeltaX);
                cameraTranslate.setY(cameraTranslate.getY() + 5*mouseDeltaY);

            }
            else // rotate
            {
                cameraRotateY.setAngle(cameraRotateY.getAngle() + mouseDeltaX);
                cameraRotateX.setAngle(cameraRotateX.getAngle() - mouseDeltaY);
                //System.out.println(cameraRotateY.getAngle() + mouseDeltaX);
                //System.out.println(cameraRotateX.getAngle() + mouseDeltaY);
            }
            mouseDownX = me.getSceneX();
            mouseDownY = me.getSceneY();
        });
    }

    private MouseHandler(Pane pane, Group group){
        pane.setOnMousePressed((me) -> {
            mouseDownX = me.getSceneX();
            mouseDownY = me.getSceneY();
        });
        pane.setOnMouseDragged((me) -> {
            double mouseDeltaX = me.getSceneX() - mouseDownX;
            double mouseDeltaY = me.getSceneY() - mouseDownY;

            group.setTranslateX(mouseDeltaX);
            group.setTranslateY(mouseDeltaY);
        });
    }


    /**
     * handle mouse events in 2d
     */
    public static void addMouseHandlerTo2dPane(Pane pane, Group group){
        new MouseHandler(pane,group);
    }



}
