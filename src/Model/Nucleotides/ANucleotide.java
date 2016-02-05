package Model.Nucleotides;

import GUI.Circleand2DBuilder;
import GUI.DefaultPhongMaterials;
import GUI.MeshAnd3DObjectBuilder;
import Model.AtomRecord;
import Model.BondInferenceAnd2D.Pos2d;
import Model.PDBModel;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape3D;
import javafx.scene.shape.Sphere;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by Philipp on 2016-01-30.
 * abstract class for Nucleotides
 */
public abstract class ANucleotide implements INucleotide{

    //holds the information about an RNA Nucleotide, nucleotides are A,G,C and U
    private String name;
    private int residueNumber;

    private Pos2d pos2DStart;//Start position in 2d representation
    private Pos2d pos2DEnd;//End position after animation

    private INucleotide matedNucleotide;//Mate paired Nucleotide

    private HashMap<String, AtomRecord> residueMap;//holds all Atomrecords with residue definition from pdb file
    private Ribose ribose;
    private Phosphate phosphate;

    private Group group3d;//3Groups: Phosphate, Ribose, Nucleobase, AtomsAndCovalentBonds, HBonds//TODO HBonds
    private Group nucleobase;
    private Group atomsAndCovalentBonds;
    private Group group2d;//Node2d representation 3 circles with a Text on Top and installed Tooltip

    private boolean hasHBondDonorsAndAcceptors;
    private ArrayList<String> acceptorKeys;
    private ArrayList<String> donorKeys;

    private BooleanProperty isSelected;

    private BooleanProperty isPaired;

    private PDBModel model;//needs reference to Model to get current NucleotideRepresentation

    //private ArrayList<HashMap>
    //private AtomRecord atomRecords; //All Atoms belonging to Nucleotide

    public ANucleotide(HashMap<String, AtomRecord> residueMap, PDBModel model){
        String validKey = residueMap.keySet().iterator().next();
        this.residueMap = residueMap;
        this.residueNumber = residueMap.get(validKey).getIndexOfResidium();
        this.name = residueMap.get(validKey).getResidium() + " " + residueNumber;
        this.isPaired = new SimpleBooleanProperty(false);
        this.isSelected = new SimpleBooleanProperty(false);
        this.model = model;
        createStructureAndGroups();
    }


    @Override
    public int getPositionInSequence() {
        return residueNumber;
    }

    @Override
    public void setPair(INucleotide nucleotide){
        matedNucleotide = nucleotide;
        //isPaired = true;
        setIsPaired(true);
    }

    @Override
    public INucleotide getPairMate(){
        return matedNucleotide;
    };

    private boolean checkRiboseCoords(){
        String[] checkList = new String[]{"C4'","C3'","C2'","C1'","O4'"};
        for (String s: checkList
                ) {
            if (! residueMap.containsKey(s)){
                return false;
            }
        }
        return true;
    }

    private boolean checkPhosphateCoords(){
        return residueMap.containsKey("P");
    }

    @Override
    public boolean[] checkBackboneGiven(){
        return new boolean[]{checkRiboseCoords(), checkPhosphateCoords()};
    }

    //will be implemented in the NUcleotide classes
    abstract Group createNucleobase();


    private void createStructureAndGroups() {
        nucleobase = createNucleobase();
        boolean[] riboseAndphosphateComplete = checkBackboneGiven();
        if(riboseAndphosphateComplete[0]){
            this.ribose = new Ribose(residueMap);
        }
        if (riboseAndphosphateComplete[1]){
            this.phosphate = new Phosphate(residueMap);
        }
        this.atomsAndCovalentBonds = new Group(createAtomSpheres());
        createImportantConnections();
        this.group3d = new Group(getPhosphateSphere(),ribose.getPresentation3d(),nucleobase,atomsAndCovalentBonds);
    }

    private Group createAtomSpheres() {
        return MeshAnd3DObjectBuilder.createAtomsSpheres(residueMap.values());
    }

    /**
     * Ribose to Nucleobase and Atoms of Nucleobase that are not a Mesh
     */
    abstract void createImportantConnections();


    @Override
    public Group getPhosphateSphere() {
        if (null == getPhosphate()){
            return new Group();
        }
        return getPhosphate().getPresentation3d();
    }

    @Override
    public Group getPhosphateBonds() {
        if (null == getPhosphate()){
            return new Group();
        }
        return getPhosphate().getBonds();
    }

    @Override
    public Group getRiboseRepresentation(){
        if (null == getRibose()){
            return new Group();
        }
        return getRibose().getPresentation3d();
    }

    public String getName() {
        return name;
    }

    @Override
    public String getRisidue() {return name.substring(0,1);}
    public int getResidueNumber() {
        return residueNumber;
    }

    public boolean getIsPaired() {
        return isPaired.get();
    }

    public BooleanProperty isPairedProperty() {
        return isPaired;
    }

    public void setIsPaired(boolean isPaired) {
        this.isPaired.set(isPaired);
    }

    public HashMap<String, AtomRecord> getResidueMap() {
        return residueMap;
    }

    public Ribose getRibose() {
        return ribose;
    }

    public Phosphate getPhosphate() {
        return phosphate;
    }

    public Group getGroup3d() {
        return group3d;
    }

    public Group getGroup2d() {
        return group2d;
    }

    @Override
    public boolean hasHBondDonorsAndAcceptors() {
        return hasHBondDonorsAndAcceptors;
    }

    public void setHasHBondDonorsAndAcceptors(boolean b){
        this.hasHBondDonorsAndAcceptors = b;
    }


    @Override
    public boolean isWCPair(INucleotide nucleotide) {
        switch (getNucleotideClass()){
            case ADENINE:
                return nucleotide.getNucleotideClass().equals(NucleotideClasses.URACIL);
            case URACIL:
                return nucleotide.getNucleotideClass().equals(NucleotideClasses.ADENINE);
            case GUANINE:
                return nucleotide.getNucleotideClass().equals(NucleotideClasses.CYTOSIN);
            case CYTOSIN:
                return nucleotide.getNucleotideClass().equals(NucleotideClasses.GUANINE);
        }
        return false;
    }

    public ArrayList<String> getAcceptorKeys() {
        return acceptorKeys;
    }

    public void setAcceptorKeys(ArrayList<String> acceptorKeys) {
        this.acceptorKeys = acceptorKeys;
    }

    public ArrayList<String> getDonorKeys() {
        return donorKeys;
    }

    public void setDonorKeys(ArrayList<String> donorKeys) {
        this.donorKeys = donorKeys;
    }

    @Override
    public ArrayList<AtomRecord> getHbondAcceptors() {
        ArrayList<AtomRecord> lis = new ArrayList<>();
        for (String k : getAcceptorKeys()) {
        lis.add(residueMap.get(k));
        }
        return lis;
    }

    @Override
    public ArrayList<AtomRecord> getHbondDonors() {
        ArrayList<AtomRecord> lis = new ArrayList<>();
        for (String k : getDonorKeys()) {
            lis.add(residueMap.get(k));
        }
        return lis;
    }

    /**
     * You shall call this only if the keyLists are instatiated!
     * checks for HBondAcceptors and DOnors in the AtomRecords
     */
    public void checkHasHBondDoAc() {
        boolean doesContainAll = true;
        for (String s : getDonorKeys()) {
            doesContainAll &= getResidueMap().containsKey(s);
        }
        for (String s : getAcceptorKeys()) {
            doesContainAll &= getResidueMap().containsKey(s);
        }
        setHasHBondDonorsAndAcceptors(doesContainAll);
    }

    @Override
    public AtomRecord getHFromDonor(String donorKey) {
        switch (getNucleotideClass()){
            case ADENINE:
                return residueMap.get("H61");
            case URACIL:
                return residueMap.get("H3");
            case GUANINE:
                if (donorKey.equals("N1")){
                    return residueMap.get("H1");
                }
                else {
                    return residueMap.get("H21");
                }
            case CYTOSIN:
                return residueMap.get("H41");
        }
        System.out.println("got C2, bad sign");
        return residueMap.get("C2");
    }

    /**
     * Only returns Pair which is composed of
     * this ones donor and the other ones acceptor
     * even positions is the DonorKey, odd acceptor Key of complementary nucleotide
     * @return
     */
    @Override
    public ArrayList<String[]> getKeyPairsToCheckForHBonds() {
        ArrayList<String[]> rv = new ArrayList<>();
        String[] onePair = new String[2];
        String[] twoPair = new String[2];
        switch (getNucleotideClass()) {
            case ADENINE:
                onePair[0]="N6";//donor
                onePair[1]="O4";//acceptor
                rv.add(onePair);
                break;
            case URACIL:
                onePair[0]="N3";
                onePair[1]="N1";
                rv.add(onePair);
                break;
            case GUANINE:
                onePair[0]="N2";
                onePair[1]="O2";
                twoPair[0]="N1";
                twoPair[1]="N3";
                rv.add(onePair);
                rv.add(twoPair);
                break;
            case CYTOSIN:
                onePair[0]="N4";
                onePair[1]="O6";
                rv.add(onePair);
                break;
        }
        return rv;
    }

    @Override
    public Pos2d getPosition2DStart() {
        return pos2DStart;
    }

    @Override
    public Pos2d getPosition2DEnd() {
        return pos2DEnd;
    }

    @Override
    public void setPosition2DStart(Pos2d pos) {
        this.pos2DStart = pos;
    }

    @Override
    public void setPosition2DEnd(Pos2d pos) {
        this.pos2DEnd = pos;
    }

    @Override
    public void setUp2dCoords(Pos2d pos1, Pos2d pos2){
        setPosition2DStart(pos1);
        setPosition2DEnd(pos2);
        this.group2d = new Group(Circleand2DBuilder.generateNodeRepresentation(this));
    }

    public boolean getIsSelected() {
        return isSelected.get();
    }

    public BooleanProperty isSelectedProperty() {
        return isSelected;
    }

    @Override
    public void setIsSelected(boolean isSelected) {
        this.isSelected.set(isSelected);
    }


    /**
     * called from inside each Nucleotide class with correct Material
     * @param mat
     */
    void recolor(PhongMaterial mat){
        //Selected overwrites all other colors
        if (getIsSelected()){
            mat = DefaultPhongMaterials.SELECTED_MATERIAL;
        }
        for (Node nodes: nucleobase.getChildren()) {
                ((Shape3D) nodes).setMaterial(mat);
        }
        Group group2d = getGroup2d();
        for (Node node: group2d.getChildren()) {
            Group node2Group = (Group) node;
            ((Circle) node2Group.getChildren().get(1)).setFill(mat.getDiffuseColor());
            ((Circle) node2Group.getChildren().get(2)).setFill(Color.WHITE);
        }
    }

    public Group getAtomsAndCovalentBonds() {
        return atomsAndCovalentBonds;
    }

    @Override
    public Group getNucleobase() {
        return nucleobase;
    }
}
