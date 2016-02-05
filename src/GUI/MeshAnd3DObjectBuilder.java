package GUI;

import Model.AtomRecord;
import Selection.Utilities;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Sphere;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

import java.util.Collection;
import java.util.HashMap;

/**
 * Created by Philipp on 2016-01-30.
 * Builds meshes for 3drepresentation
 */
public class MeshAnd3DObjectBuilder {
    public static final int SCALE_FACTOR = 100;
    public static final int BOND_DIAMETER = 8;
    public static final int SPHERE_RADIUS = 15;

    public static Group makeFrontBackHexagon(AtomRecord[] hexlis, PhongMaterial mat){
        Group hexagons = makeHexagon(hexlis, mat);
        Utilities.reverse(hexlis);
        MeshView hexagon2 = (MeshView) makeHexagon(hexlis, mat).getChildren().get(0);
        hexagons.getChildren().add(hexagon2);
        return hexagons;
    }

    public static Group makeFrontBackPentagon(AtomRecord[] pentlis, PhongMaterial mat){
        Group pentagons = makePentagon(pentlis, mat);
        Utilities.reverse(pentlis);
        MeshView pentagon2 = (MeshView) makePentagon(pentlis, mat).getChildren().get(0);
        pentagons.getChildren().add(pentagon2);
        return pentagons;
    }

    private static Group makeHexagon(AtomRecord[] input, PhongMaterial mat){
        final Group group = new Group();
        if (input.length != 6)
            throw new RuntimeException("Need 6 points for hexagon");

        float[] points = new float[3 * input.length];
        for (int i = 0; i < input.length; i++) {
            points[3 * i] = (float) (input[i].getPoint3D().getX()) * 100;
            points[3 * i + 1] = (float) (input[i].getPoint3D().getY()) * 100;
            points[3 * i + 2] = (float) (input[i].getPoint3D().getZ()) * 100;
        }

        float[] texCoords = {0, 0, 0, 1, 1, 1};

        int[] faces = new int[]{
                0, 0, 1, 1, 2, 2,
                0, 0, 2, 1, 3, 2,
                0, 0, 3, 1, 4, 2,
                0, 0, 4, 1, 5, 2};

        int[] smoothing = {1, 1, 1, 1};

        TriangleMesh mesh = new TriangleMesh();
        mesh.getPoints().addAll(points);
        mesh.getTexCoords().addAll(texCoords);
        mesh.getFaces().addAll(faces);
        mesh.getFaceSmoothingGroups().addAll(smoothing);
        MeshView meshView = new MeshView(mesh);
        meshView.setMaterial(mat);
        //meshView.setDrawMode(DrawMode.LINE);
        group.getChildren().add(meshView);
        return group;
    }

    private static Group makePentagon(AtomRecord[] input, PhongMaterial mat) {
        final Group group = new Group();
        if (input.length != 5)
            throw new RuntimeException("Need 5 points for pentagon");

        float[] points = new float[3 * input.length];
        for (int i = 0; i < input.length; i++) {
            points[3 * i] = (float) (input[i].getPoint3D().getX()) * SCALE_FACTOR;
            points[3 * i + 1] = (float) (input[i].getPoint3D().getY()) * SCALE_FACTOR;
            points[3 * i + 2] = (float) (input[i].getPoint3D().getZ()) * SCALE_FACTOR;
        }
        float[] texCoords = {0, 0, 0, 1, 1, 1};
        int[] faces = new int[]{
                0, 0, 1, 1, 2, 2,
                0, 0, 2, 1, 3, 2,
                0, 0, 3, 1, 4, 2};

        int[] smoothing = {1, 1, 1};

        TriangleMesh mesh = new TriangleMesh();
        mesh.getPoints().addAll(points);
        mesh.getTexCoords().addAll(texCoords);
        mesh.getFaces().addAll(faces);
        mesh.getFaceSmoothingGroups().addAll(smoothing);
        MeshView meshView = new MeshView(mesh);
        meshView.setMaterial(mat);
        //meshView.setDrawMode(DrawMode.LINE);

        group.getChildren().add(meshView);
        return group;
    }


    /**
     * from http://netzwerg.ch/blog/2015/03/22/javafx-3d-line/
     * @param origin
     * @param target
     * @return
     */
    public static Cylinder createConnection(AtomRecord origin, AtomRecord target) {
        //scaling factor
        if ((origin == null)||(target == null)){
            System.out.println("Sorry one of our points is null");
            return new Cylinder(0,0);
        }
        //System.out.println(origin.getId() + " " + target.getId());
        //System.out.println(origin.getPoint3D() + " " + target.getPoint3D());
        Point3D scaledOrigin = new Point3D(origin.getPoint3D().getX()*SCALE_FACTOR,
                origin.getPoint3D().getY()*SCALE_FACTOR,
                origin.getPoint3D().getZ()*SCALE_FACTOR);

        Point3D scaledTarget = new Point3D(target.getPoint3D().getX()*SCALE_FACTOR,
                target.getPoint3D().getY()*SCALE_FACTOR,
                target.getPoint3D().getZ()*SCALE_FACTOR);

        Point3D yAxis = new Point3D(0, 1, 0);
        Point3D diff = scaledTarget.subtract(scaledOrigin);
        double height = diff.magnitude();

        Point3D mid = scaledTarget.midpoint(scaledOrigin);
        Translate moveToMidpoint = new Translate(mid.getX(), mid.getY(), mid.getZ());

        Point3D axisOfRotation = diff.crossProduct(yAxis);
        double angle = Math.acos(diff.normalize().dotProduct(yAxis));
        Rotate rotateAroundCenter = new Rotate(-Math.toDegrees(angle), axisOfRotation);

        Cylinder line = new Cylinder(BOND_DIAMETER, height);

        line.getTransforms().addAll(moveToMidpoint, rotateAroundCenter);

        line.setMaterial(getMaterialByAtomType(target.getAtomType()));

        return line;
    }

    public static Group createAtomsSpheres(Collection<AtomRecord> values) {
        Group rv = new Group();
        for (AtomRecord a: values) {
            Sphere sphere = new Sphere(SPHERE_RADIUS);
            sphere.setTranslateX(a.getPoint3D().getX()*SCALE_FACTOR);
            sphere.setTranslateY(a.getPoint3D().getY()*SCALE_FACTOR);
            sphere.setTranslateZ(a.getPoint3D().getZ()*SCALE_FACTOR);
            sphere.setMaterial(getMaterialByAtomType(a.getAtomType()));
            rv.getChildren().add(sphere);
        }
        return rv;
    }

    private static PhongMaterial getMaterialByAtomType(String s){
        switch (s) {
            case "C": {
                return DefaultPhongMaterials.C_MATERIAL;
            }
            case "N": {
                return DefaultPhongMaterials.N_MATERIAL;
            }
            case "O": {
                return DefaultPhongMaterials.O_MATERIAL;
            }
            case "P": {
                return DefaultPhongMaterials.PHOSPHATE_MATERIAL;
            }
        }
        return new PhongMaterial();//White Phong Material for H
    }
}
