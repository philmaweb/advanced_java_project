package Model.Nucleotides;

import GUI.DefaultPhongMaterials;
import GUI.MeshAnd3DObjectBuilder;
import Model.AtomRecord;
import Model.BondInferenceAnd2D.NucleotideDefaultValues;
import Model.PDBModel;
import javafx.scene.Group;
import javafx.scene.control.Tooltip;

import java.util.HashMap;

/**
 * Created by Philipp on 2016-01-30.
 *
 */
public class Guanine extends ANucleotide {

    public Guanine(HashMap<String, AtomRecord> residueMap, PDBModel model){
        super(residueMap,model);
        setAcceptorKeys(NucleotideDefaultValues.acceptorKeysG);
        setDonorKeys(NucleotideDefaultValues.donatorKeysG);
        checkHasHBondDoAc();
    }

    @Override
    public NucleotideClasses getNucleotideClass() {
        return NucleotideClasses.GUANINE;
    }

    @Override
    Group createNucleobase() {
        Group guanine = new Group();

        if(checkCoordsGiven()){
            HashMap<String, AtomRecord> map = getResidueMap();
            AtomRecord[] hexlis = new AtomRecord[6];
            AtomRecord[] pentLis = new AtomRecord[5];
            AtomRecord[] riboseConnection = new AtomRecord[2];

            hexlis[0] = map.get("N1");
            hexlis[1] = map.get("C2");
            hexlis[2] = map.get("N3");
            hexlis[3] = map.get("C4");
            hexlis[4] = map.get("C5");
            hexlis[5] = map.get("C6");

            pentLis[0]= map.get("C5");
            pentLis[1]= map.get("N7");
            pentLis[2]= map.get("C8");
            pentLis[3]= map.get("N9");
            pentLis[4]= map.get("C4");


            riboseConnection[0] = map.get("N9");
            riboseConnection[1] = map.get("C1'");
            guanine = MeshAnd3DObjectBuilder.makeFrontBackHexagon(hexlis, DefaultPhongMaterials.GUANINE_MATERIAL);
            Group pentagon = MeshAnd3DObjectBuilder.makeFrontBackPentagon(pentLis,DefaultPhongMaterials.GUANINE_MATERIAL);
            guanine.getChildren().addAll(pentagon.getChildren().get(0),pentagon.getChildren().get(1));
            Tooltip t = new Tooltip(hexlis[0].getResidium() + " " + hexlis[0].getIndexOfResidium());
            Tooltip.install(guanine, t);
            //TODO
//            guanines3d.getChildren().add(guanine);

            //Also draw line to ribbose
//            smallWorld3d.getChildren().add(createConnection(riboseConnection[0] ,riboseConnection[1]));

        }

        return guanine;
    }

    @Override //Guanine Verification
    public boolean checkCoordsGiven() {
            String[] checkList = new String[]{"N1","C2","N3","C4","C5","C6","C1'","N7","C8","N9"};
            for (String s: checkList
                    ) {
                if (! getResidueMap().containsKey(s)){
                    return false;
                }
            }
            return true;
        }
}
