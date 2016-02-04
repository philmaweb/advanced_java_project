package Model.Nucleotides;

import GUI.DefaultPhongMaterials;
import GUI.MeshAnd3DObjectBuilder;
import Model.AtomRecord;
import Model.BondInferenceAnd2D.NucleotideDefaultValues;
import Model.NucleotideRepresentation;
import Model.PDBModel;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape3D;

import java.util.HashMap;

/**
 * Created by Philipp on 2016-01-30.
 */
public class Uracil extends ANucleotide{

    public Uracil(HashMap<String, AtomRecord> residueMap, PDBModel model){
        super(residueMap,model);
        setAcceptorKeys(NucleotideDefaultValues.acceptorKeysU);
        setDonorKeys(NucleotideDefaultValues.donatorKeysU);
        checkHasHBondDoAc();
    }




    @Override
    public NucleotideClasses getNucleotideClass() {
        return NucleotideClasses.URACIL;
    }

    @Override
    public Color get2DColorByClass() {
        return Color.GREEN;
    }

    @Override
    Group createNucleobase() {
        Group uracil = new Group();

        if(checkCoordsGiven()){
            HashMap<String, AtomRecord> map = getResidueMap();
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
            //System.out.println(hexlis + " " +  hexlis.size());
            uracil = MeshAnd3DObjectBuilder.makeFrontBackHexagon(hexlis, DefaultPhongMaterials.URACIL_MATERIAL);
//            uracils3d.getChildren().add(uracil);
            Tooltip t = new Tooltip(hexlis[0].getResidium() + " " + hexlis[0].getIndexOfResidium());
            Tooltip.install(uracil, t);
            //Also draw line to ribbose
//            smallWorld3d.getChildren().add(createConnection(riboseConnection[0], riboseConnection[1]));
        }
        return uracil;
    }

    /**
     * check if coords for drawing are included
     * @return
     */
    @Override
    public boolean checkCoordsGiven() {
        final String[] checkList = new String[]{"N1","C2","N3","C4","C5","C6","C1'"};
        for (String s: checkList
                ) {
            if (! getResidueMap().containsKey(s)){
                return false;
            }
        }
        return true;
    }

    @Override
    public void updateColoring(NucleotideRepresentation representation) {
        PhongMaterial updateMaterial = DefaultPhongMaterials.URACIL_MATERIAL;
        switch (representation) {
            case AGCU:
                break;
            case PURINE_PYRIMIDINE:
                updateMaterial = DefaultPhongMaterials.PYRIMIDINE_MATERIAL;
                break;
            case PAIRED:
                if (getIsPaired()){
                    updateMaterial = DefaultPhongMaterials.PAIR_MATERIAL;
                }
                break;
            case SELECTED:
                if (getIsSelected()){
                    updateMaterial = DefaultPhongMaterials.SELECTED_MATERIAL;
                }
                break;
        }
        recolor(updateMaterial);
    }



}
