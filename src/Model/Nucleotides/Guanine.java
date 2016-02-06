package Model.Nucleotides;

import GUI.DefaultPhongMaterials;
import GUI.MeshAnd3DObjectBuilder;
import GUI.NucleotideTextRepresentation;
import Model.AtomRecord;
import Model.BondInferenceAnd2D.NucleotideDefaultValues;
import Model.NucleotideRepresentation;
import Model.PDBModel;
import javafx.scene.Group;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;

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
    public void updateColoring(NucleotideRepresentation representation) {
        PhongMaterial updateMaterial = DefaultPhongMaterials.GUANINE_MATERIAL;
        switch (representation) {
            case AGCU:
                break;
            case PURINE_PYRIMIDINE:
                updateMaterial = DefaultPhongMaterials.PURINE_MATERIAL;
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

    @Override
    public Color get2DColorByClass() {
        return Color.BLUE;
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

            //Add Atomsspheres
            for (int i = 0; i < hexlis.length; i++) {
                AtomRecord ar = hexlis[i];
                getAtomsAndCovalentBonds().getChildren().add(MeshAnd3DObjectBuilder.createAtomSphere(ar));
            }


            for (int i = 0; i < pentLis.length; i++) {
                AtomRecord ar = pentLis[i];
                getAtomsAndCovalentBonds().getChildren().add(MeshAnd3DObjectBuilder.createAtomSphere(ar));
            }

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

    @Override
    protected NucleotideTextRepresentation createNucleotideTextRepresentation() {
        return new NucleotideTextRepresentation(this.getPositionInSequence(), NucleotideClasses.GUANINE);

    }

    @Override
    void createImportantConnections() {
        Group connections = new Group();
        connections.getChildren().add(MeshAnd3DObjectBuilder.createConnection(getResidueMap().get("N1"),getResidueMap().get("H1")));
        connections.getChildren().add(MeshAnd3DObjectBuilder.createConnection(getResidueMap().get("C2"),getResidueMap().get("N2")));
        connections.getChildren().add(MeshAnd3DObjectBuilder.createConnection(getResidueMap().get("N2"),getResidueMap().get("H21")));
        connections.getChildren().add(MeshAnd3DObjectBuilder.createConnection(getResidueMap().get("N2"),getResidueMap().get("H22")));
        connections.getChildren().add(MeshAnd3DObjectBuilder.createConnection(getResidueMap().get("C6"),getResidueMap().get("O6")));
        connections.getChildren().add(MeshAnd3DObjectBuilder.createConnection(getResidueMap().get("C8"),getResidueMap().get("H8")));

        connections.getChildren().add(MeshAnd3DObjectBuilder.createConnection(getResidueMap().get("N9"),getResidueMap().get("C1'")));

        connections.getChildren().add(MeshAnd3DObjectBuilder.createAtomSphere(getResidueMap().get("N1")));
        connections.getChildren().add(MeshAnd3DObjectBuilder.createAtomSphere(getResidueMap().get("H1")));
        connections.getChildren().add(MeshAnd3DObjectBuilder.createAtomSphere(getResidueMap().get("C2")));
        connections.getChildren().add(MeshAnd3DObjectBuilder.createAtomSphere(getResidueMap().get("N2")));
        connections.getChildren().add(MeshAnd3DObjectBuilder.createAtomSphere(getResidueMap().get("H21")));
        connections.getChildren().add(MeshAnd3DObjectBuilder.createAtomSphere(getResidueMap().get("H22")));
        connections.getChildren().add(MeshAnd3DObjectBuilder.createAtomSphere(getResidueMap().get("C6")));
        connections.getChildren().add(MeshAnd3DObjectBuilder.createAtomSphere(getResidueMap().get("O6")));
        connections.getChildren().add(MeshAnd3DObjectBuilder.createAtomSphere(getResidueMap().get("C8")));
        connections.getChildren().add(MeshAnd3DObjectBuilder.createAtomSphere(getResidueMap().get("H8")));
        connections.getChildren().add(MeshAnd3DObjectBuilder.createAtomSphere(getResidueMap().get("N9")));
        connections.getChildren().add(MeshAnd3DObjectBuilder.createAtomSphere(getResidueMap().get("C1")));

        getAtomsAndCovalentBonds().getChildren().add(connections);
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
