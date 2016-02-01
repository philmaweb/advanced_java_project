package Model.Nucleotides;

import GUI.DefaultPhongMaterials;
import GUI.MeshAnd3DObjectBuilder;
import Model.AtomRecord;
import javafx.scene.Group;

import java.util.HashMap;

/**
 * Created by Philipp on 2016-01-30.
 * Ribose, 3
 */
public class Ribose {
    //should have a 3d representation
    Group presentation3d;

    public Ribose(HashMap<String, AtomRecord> residueMap){
        init(residueMap);
    }


    private void init(HashMap<String, AtomRecord> map) {
        AtomRecord[] riboseList = new AtomRecord[5];
        riboseList[0]=map.get("C4'");
        riboseList[1]=map.get("C3'");
        riboseList[2]=map.get("C2'");
        riboseList[3]=map.get("C1'");
        riboseList[4]=map.get("O4'");
        //TODO Add to ribose group and a/u/g/c groups
        //System.out.println("riboses size" + riboses.getChildren().size());
        Group pentagon = MeshAnd3DObjectBuilder.makeFrontBackPentagon(riboseList, DefaultPhongMaterials.RIBOSE_MATERIAL);
        //TODO add to RiboseGroup
        //riboses3d.add(pentagon);
        //System.out.println("riboses size" + riboses.getChildren().size());
        this.presentation3d = pentagon;
    }

    public Group getPresentation3d() {
        return presentation3d;
    }
}
