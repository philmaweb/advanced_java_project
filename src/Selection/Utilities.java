package Selection;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Window;

/**
 * some utilities
 * Created by huson on 12/1/15.
 */
public class Utilities {
    /**
     * make a box to draw
     *
     * @param color
     * @return box
     */
    public static Box makeBox(Color color) {
        final Box box = new Box(30, 40, 50);
        final PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(color.darker());
        material.setSpecularColor(color.brighter());
        box.setMaterial(material);

        //box.setDrawMode(DrawMode.LINE);
        return box;
    }

    /**
     * make a cylinder to draw
     *
     * @param color
     * @return cylinder
     */
    public static Cylinder makeCylinder(Color color) {
        final Cylinder cylinder = new Cylinder(10, 100);
        final PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(color.darker());
        material.setSpecularColor(color.brighter());

        cylinder.setMaterial(material);
        //cylinder.setDrawMode(DrawMode.LINE);

        return cylinder;
    }


    /**
     * create a bounding box that is bound to user determined transformations
     */
    public static Rectangle createBoundingBoxWithBinding(Node node, final Rotate worldRotateX, final Rotate worldRotateY, Translate cameraTranslate, boolean visible) {
        final Rectangle boundingBox = new Rectangle();
        boundingBox.setStroke(Color.DARKRED);
        boundingBox.setFill(Color.TRANSPARENT);
        boundingBox.setMouseTransparent(true);
        boundingBox.setVisible(visible);

        final ObjectBinding<Rectangle> binding = createBoundingBoxBinding(node, worldRotateX, worldRotateY, cameraTranslate);

        boundingBox.xProperty().bind(new DoubleBinding() {
            {
                bind(binding);
            }

            @Override
            protected double computeValue() {
                return binding.get().getX();
            }
        });
        boundingBox.yProperty().bind(new DoubleBinding() {
            {
                bind(binding);
            }

            @Override
            protected double computeValue() {
                return binding.get().getY();
            }
        });
        boundingBox.widthProperty().bind(new DoubleBinding() {
            {
                bind(binding);
            }

            protected double computeValue() {
                return binding.get().getWidth();
            }
        });
        boundingBox.heightProperty().bind(new DoubleBinding() {
            {
                bind(binding);
            }

            @Override
            protected double computeValue() {
                return binding.get().getHeight();
            }
        });

        return boundingBox;
    }

    /**
     * creates bounding box binding
     *
     * @param node
     * @param worldRotateX
     * @param worldRotateY
     * @param cameraTranslate
     * @return binding
     */
    private static ObjectBinding<Rectangle> createBoundingBoxBinding(Node node, final Rotate worldRotateX, final Rotate worldRotateY, Translate cameraTranslate) {
        return new ObjectBinding<Rectangle>() {
            {
                bind(worldRotateX.angleProperty(), worldRotateY.angleProperty(), cameraTranslate.zProperty());
            }

            @Override
            protected Rectangle computeValue() {
                try {
                    final Window window = node.getScene().getWindow();
                    final Bounds boundsInLocal = node.getBoundsInLocal();
                    final Bounds boundsOnScreen = node.localToScreen(boundsInLocal);
                    return new Rectangle(
                            boundsOnScreen.getMinX() - window.getX() - node.getScene().getX(),
                            boundsOnScreen.getMinY() - window.getY() - node.getScene().getY(),
                            boundsOnScreen.getWidth(),
                            boundsOnScreen.getHeight());
                } catch (NullPointerException e) {
                    return new Rectangle(0, 0, 0, 0);
                }
            }
        };
    }

    /**
     * Reverse an array in a efficient way
     * @param orig
     */
    public static void reverse(Object[] orig){
        for(int i = 0; i < orig.length / 2; i++){
            Object temp = orig[i];
            orig[i] = orig[orig.length - i - 1];
            orig[orig.length - i - 1] = temp;
        }
    }
}




