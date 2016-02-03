package Model.Nucleotides;

import GUI.DefaultPhongMaterials;
import GUI.MeshAnd3DObjectBuilder;
import Model.AtomRecord;
import Model.BondInferenceAnd2D.NucleotideDefaultValues;
import Model.PDBModel;
import javafx.scene.Group;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;

import java.util.HashMap;

/**
 * Created by Philipp on 2016-01-30.
 */
public class Cytosin extends ANucleotide{

    public Cytosin(HashMap<String, AtomRecord> residueMap, PDBModel model){
        super(residueMap,model);
        setAcceptorKeys(NucleotideDefaultValues.acceptorKeysC);
        setDonorKeys(NucleotideDefaultValues.donatorKeysC);
        checkHasHBondDoAc();
    }


    @Override
    public NucleotideClasses getNucleotideClass() {
        return NucleotideClasses.CYTOSIN;
    }

    @Override
    public Color get2DColorByClass() {
        return Color.YELLOW;
    }

    @Override
    Group createNucleobase(){
        Group cytosin = new Group();
        if (checkCoordsGiven()){
            HashMap<String, AtomRecord> map = this.getResidueMap();
                AtomRecord[] riboseConnection = new AtomRecord[2];
                AtomRecord[] hexlis = new AtomRecord[6];
                hexlis[0] = map.get("N1");
                hexlis[1] = map.get("C2");
                hexlis[2] = map.get("N3");
                hexlis[3] = map.get("C4");
                hexlis[4] = map.get("C5");
                hexlis[5] = map.get("C6");

                riboseConnection[0] = map.get("C1'");
                riboseConnection[1] = map.get("N1");
                cytosin = MeshAnd3DObjectBuilder.makeFrontBackHexagon(hexlis, DefaultPhongMaterials.CYTOSIN_MATERIAL);
                Tooltip t = new Tooltip(hexlis[0].getResidium() + " " + hexlis[0].getIndexOfResidium());
                Tooltip.install(cytosin, t);
//            cytosins3d.getChildren().add(cytosin);
                //world.getChildren().add(cytosin);
                //Also draw line to ribbose
//            smallWorld3d.getChildren().add(createConnection(riboseConnection[0],riboseConnection[1]));
            }
        return cytosin;
    }


    @Override
    public boolean checkCoordsGiven() {
        String[] checkList = new String[]{"N1","C2","N3","C4","C5","C6","C1'"};
        for (String s: checkList
                ) {
            if (! getResidueMap().containsKey(s)){
                return false;
            }
        }
        return true;
    }


}
