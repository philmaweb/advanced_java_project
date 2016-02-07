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
 * Cytosin Nucleotide class
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
    public void updateColoring(NucleotideRepresentation representation) {
        PhongMaterial updateMaterial = DefaultPhongMaterials.CYTOSIN_MATERIAL;
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

                //Add Atomsspheres
                for (int i = 0; i < hexlis.length; i++) {
                    AtomRecord ar = hexlis[i];
                    getAtomsAndCovalentBonds().getChildren().add(MeshAnd3DObjectBuilder.createAtomSphere(ar));
                }

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
    protected NucleotideTextRepresentation createNucleotideTextRepresentation() {
        return new NucleotideTextRepresentation(this.getPositionInSequence(), NucleotideClasses.CYTOSIN);

    }

    @Override
    void createImportantConnections() {
        Group connections = new Group();
        connections.getChildren().add(MeshAnd3DObjectBuilder.createConnection(getResidueMap().get("C2"),getResidueMap().get("O2")));
        connections.getChildren().add(MeshAnd3DObjectBuilder.createConnection(getResidueMap().get("C4"),getResidueMap().get("N4")));
        connections.getChildren().add(MeshAnd3DObjectBuilder.createConnection(getResidueMap().get("N4"),getResidueMap().get("H41")));
        connections.getChildren().add(MeshAnd3DObjectBuilder.createConnection(getResidueMap().get("N4"),getResidueMap().get("H42")));
        connections.getChildren().add(MeshAnd3DObjectBuilder.createConnection(getResidueMap().get("C5"),getResidueMap().get("H5")));
        connections.getChildren().add(MeshAnd3DObjectBuilder.createConnection(getResidueMap().get("C6"),getResidueMap().get("H6")));

        connections.getChildren().add(MeshAnd3DObjectBuilder.createConnection(getResidueMap().get("N1"),getResidueMap().get("C1'")));

        connections.getChildren().add(MeshAnd3DObjectBuilder.createAtomSphere(getResidueMap().get("C2")));
        connections.getChildren().add(MeshAnd3DObjectBuilder.createAtomSphere(getResidueMap().get("O2")));
        connections.getChildren().add(MeshAnd3DObjectBuilder.createAtomSphere(getResidueMap().get("C4")));
        connections.getChildren().add(MeshAnd3DObjectBuilder.createAtomSphere(getResidueMap().get("N4")));
        connections.getChildren().add(MeshAnd3DObjectBuilder.createAtomSphere(getResidueMap().get("H41")));
        connections.getChildren().add(MeshAnd3DObjectBuilder.createAtomSphere(getResidueMap().get("H42")));
        connections.getChildren().add(MeshAnd3DObjectBuilder.createAtomSphere(getResidueMap().get("C5")));
        connections.getChildren().add(MeshAnd3DObjectBuilder.createAtomSphere(getResidueMap().get("H5")));
        connections.getChildren().add(MeshAnd3DObjectBuilder.createAtomSphere(getResidueMap().get("C6")));
        connections.getChildren().add(MeshAnd3DObjectBuilder.createAtomSphere(getResidueMap().get("H6")));
        connections.getChildren().add(MeshAnd3DObjectBuilder.createAtomSphere(getResidueMap().get("N1")));
        connections.getChildren().add(MeshAnd3DObjectBuilder.createAtomSphere(getResidueMap().get("C1")));

        getAtomsAndCovalentBonds().getChildren().add(connections);
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
