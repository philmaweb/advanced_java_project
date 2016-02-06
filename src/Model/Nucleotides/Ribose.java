package Model.Nucleotides;

import GUI.DefaultPhongMaterials;
import GUI.MeshAnd3DObjectBuilder;
import Model.AtomRecord;
import javafx.scene.Group;
import javafx.scene.Node;

import java.util.ArrayList;
import java.util.Arrays;
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
        Group pentagon = MeshAnd3DObjectBuilder.makeFrontBackPentagon(riboseList, DefaultPhongMaterials.RIBOSE_MATERIAL);

        this.presentation3d = pentagon;

        for (int i = 0; i < riboseList.length; i++) {
            AtomRecord ar = riboseList[i];
            presentation3d.getChildren().add(MeshAnd3DObjectBuilder.createAtomSphere(ar));
        }
    }

    public Group getPresentation3d() {
        return presentation3d;
    }

}
