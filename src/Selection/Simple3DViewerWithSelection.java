package Selection;

import javafx.application.Application;
import javafx.beans.binding.BooleanBinding;
import javafx.geometry.Point3D;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape3D;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

/**
 * simple 3D viewer
 * Created by huson on 12/1/15.
 */
public class Simple3DViewerWithSelection extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // setup camera:
        final PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(10000.0);

        final Rotate cameraRotateX = new Rotate(0, new Point3D(1, 0, 0));
        final Rotate cameraRotateY = new Rotate(0, new Point3D(0, 1, 0));
        final Translate cameraTranslate = new Translate(0, 0, -1000);
        camera.getTransforms().addAll(cameraRotateX, cameraRotateY, cameraTranslate);

        // setup world and subscene
        final Group world3D = new Group();
        final SubScene subScene=new SubScene(world3D,1000,1000,true,SceneAntialiasing.BALANCED);
        subScene.setCamera(camera);

        // setup top pane and stacked pane
        final Pane topPane=new Pane();
        topPane.setPickOnBounds(false);
        final Group world2D=new Group();
        topPane.getChildren().addAll(world2D);

        final StackPane stackPane=new StackPane(subScene,topPane);
        StackPane.setAlignment(topPane, Pos.CENTER);
        StackPane.setAlignment(subScene, Pos.CENTER);

        // setup scene and stage:
        final Scene scene=new Scene(stackPane,1000,1000);

        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.show();

        // add mouse handling
        MouseHandler.addMouseHanderToScene(scene, cameraRotateX, cameraRotateY, cameraTranslate);

        // make some objects

        final Box box1 = Utilities.makeBox(Color.ROSYBROWN);
        box1.setTranslateY(50);
        Tooltip.install(box1,new Tooltip("BOX1"));

        final Box box2 = Utilities.makeBox(Color.BLUEVIOLET);
        box2.setTranslateY(-50);
        Tooltip.install(box2,new Tooltip("BOX2"));

        final Cylinder cylinder = Utilities.makeCylinder(Color.ORANGE);
        Tooltip.install(cylinder,new Tooltip("cylinder"));

        // put shapes and bounding boxes into two arrays:
        final Shape3D[] shapes={box1,box2,cylinder};
        world3D.getChildren().addAll(shapes);

        final Rectangle[] boundingBoxes={
                Utilities.createBoundingBoxWithBinding(box1, cameraRotateX, cameraRotateY, cameraTranslate, false),
                Utilities.createBoundingBoxWithBinding(box2, cameraRotateX, cameraRotateY, cameraTranslate, false),
                Utilities.createBoundingBoxWithBinding(cylinder, cameraRotateX, cameraRotateY, cameraTranslate, false)
        };
        world2D.getChildren().addAll(boundingBoxes);

        // set up selection model:
        MySelectionModel<Shape3D> mySelectionModel=new MySelectionModel<>(shapes);

        // setup selection capture in view:
        for (int i = 0; i < shapes.length; i++) {
            final int index=i;
            shapes[i].setOnMouseClicked((e) -> {
                if(!e.isShiftDown())
                    mySelectionModel.clearSelection();
                if(mySelectionModel.isSelected(index)) {
                    mySelectionModel.clearSelection(index);
                }
                else {
                    mySelectionModel.select(index);
                }
            });
        }

        // bind visibility of bounding boxes to selection state
        for(int i=0;i<shapes.length;i++) {
            final int index=i;

            boundingBoxes[index].visibleProperty().bind(new BooleanBinding(){
                {
                    bind(mySelectionModel.getSelectedItems());
                }

                @Override
                protected boolean computeValue() {
                    return mySelectionModel.getSelectedIndices().contains(index);
                }
            });

        }
    }


public static void main(String[] args) {
        launch(args);
    }
}


