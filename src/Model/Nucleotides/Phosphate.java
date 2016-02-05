package Model.Nucleotides;

import GUI.DefaultPhongMaterials;
import GUI.MeshAnd3DObjectBuilder;
import Model.AtomRecord;
import javafx.scene.Group;
import javafx.scene.shape.Sphere;

import java.util.HashMap;

/**
 * Created by Philipp on 2016-01-30.
 * Representation of a phosphate in 3d Space
 * will only be created if Phosphor Check passes
 */
public class Phosphate {
    //should have a 3d representation
    Group presentation3d = new Group();
    Group bonds = new Group();//can only be 3d
    AtomRecord atomRecord;//position of center phosphorous

    public Phosphate(HashMap<String, AtomRecord> residueMap){
        init(residueMap);
    }


    private void init(HashMap<String, AtomRecord> map) {
        AtomRecord[] connectionList = new AtomRecord[4];
        connectionList[0]=map.get("P");
        connectionList[1]=map.get("O5'");
        connectionList[2]=map.get("C5'");
        connectionList[3]=map.get("C4'");
        Group phosphate = makePhosphateSphere(map.get("P"));
        //System.out.println("phosphates size" + phosphates.getChildren().size());
        Group bonds = new Group();

        //phosphates3d.add(phosphate);
        //create Phosphate connections
        bonds.getChildren().add(MeshAnd3DObjectBuilder.createConnection(connectionList[0],connectionList[1]));
        bonds.getChildren().add(MeshAnd3DObjectBuilder.createConnection(connectionList[1],connectionList[2]));
        bonds.getChildren().add(MeshAnd3DObjectBuilder.createConnection(connectionList[2],connectionList[3]));
        //System.out.println("phosphates size" + phosphates.getChildren().size());
        this.atomRecord = connectionList[0];
        this.presentation3d = phosphate;
        this.bonds = bonds;
    }


    private Group makePhosphateSphere(AtomRecord input){
        final Group group =  new Group();
        float[] point = new float[3];
        point[0] = (float) (input.getPoint3D().getX()) * MeshAnd3DObjectBuilder.SCALE_FACTOR;
        point[1] = (float) (input.getPoint3D().getY()) * MeshAnd3DObjectBuilder.SCALE_FACTOR;
        point[2] = (float) (input.getPoint3D().getZ()) * MeshAnd3DObjectBuilder.SCALE_FACTOR;
        Sphere sphere = new Sphere(50);
        sphere.setTranslateX(point[0]);
        sphere.setTranslateY(point[1]);
        sphere.setTranslateZ(point[2]);
        sphere.setMaterial(DefaultPhongMaterials.PHOSPHATE_MATERIAL);
        group.getChildren().add(sphere);
        return group;
    }

    public Group getPresentation3d() {
        return presentation3d;
    }

    public Group getBonds() {
        return bonds;
    }

    public AtomRecord getAtomRecord() {
        return atomRecord;
    }
}
