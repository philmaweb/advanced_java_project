package Model.Nucleotides;

import Model.AtomRecord;
import Model.BondInferenceAnd2D.Pos2d;
import Model.PDBModel;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Group;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Philipp on 2016-01-30.
 * abstract class for Nucleotides
 */
public abstract class ANucleotide implements INucleotide{

    //holds the information about an RNA Nucleotide, nucleotides are A,G,C and U
    private String name;
    private int residueNumber;
    private Pos2d pos2d;
    private INucleotide matedNucleotide;

    private HashMap<String, AtomRecord> residueMap;//holds all Atomrecords with residue definition from pdb file
    private Ribose ribose;
    private Phosphate phosphate;

    private Group group3d;//Phosphate, Ribose, Nucleobase, //TODO Bonds
    private Group group2d;

    private boolean hasHBondDonorsAndAcceptors;
    private ArrayList<String> acceptorKeys;
    private ArrayList<String> donorKeys;

    private BooleanProperty isPaired;
    private BooleanProperty isAdenine;
    private BooleanProperty isGuanine;
    private BooleanProperty isUracil;
    private BooleanProperty isCytosin;

    private PDBModel model;//needs reference to Model to get current NucleotideRepresentation bindings

    //private ArrayList<HashMap>
    //private AtomRecord atomRecords; //All Atoms belonging to Nucleotide

    public ANucleotide(HashMap<String, AtomRecord> residueMap, PDBModel model){
        String validKey = residueMap.keySet().iterator().next();
        this.residueMap = residueMap;
        this.residueNumber = residueMap.get(validKey).getIndexOfResidium();
        this.name = residueMap.get(validKey).getResidium() + " " + residueNumber;
        this.isPaired = new SimpleBooleanProperty(false);
        this.isAdenine = new SimpleBooleanProperty(getNucleotideClass().equals(NucleotideClasses.ADENINE));
        this.isGuanine = new SimpleBooleanProperty(getNucleotideClass().equals(NucleotideClasses.GUANINE));
        this.isCytosin = new SimpleBooleanProperty(getNucleotideClass().equals(NucleotideClasses.CYTOSIN));
        this.isUracil = new SimpleBooleanProperty(getNucleotideClass().equals(NucleotideClasses.URACIL));
        this.model = model;


        createStructureAndGroups();
    }


    @Override
    public int getPositionInSequence() {
        return residueNumber;
    }

    @Override
    public Pos2d getPosition2d(){
        return pos2d;
    };

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
        Group nucleobase3d = createNucleobase();
        boolean[] riboseAndphosphateComplete = checkBackboneGiven();
        if(riboseAndphosphateComplete[0]){
            this.ribose = new Ribose(residueMap);
        }
        if (riboseAndphosphateComplete[1]){
            this.phosphate = new Phosphate(residueMap);
        }
        /*Group phosphateSphereGroup = getPhosphateSphere();
        if (null != phosphateSphereGroup){
            for (Node node: phosphateSphereGroup.getChildren()) {
                Sphere sphere = (Sphere) node;
                sphere.materialProperty().//setValue(getCorrectPhosphateMaterial());
                bind(Bindings.when(model.isPairedViewPropertyProperty()
                                   .and(isPaired))
                                .then(DefaultPhongMaterials.PAIR_MATERIAL)
                                .otherwise(DefaultPhongMaterials.PHOSPHATE_MATERIAL));
            }
        }
        Group riboseMeshViewGroup = ribose.getPresentation3d();
        if (null != riboseMeshViewGroup){
            for (Node node: riboseMeshViewGroup.getChildren()) {
                MeshView meshView = (MeshView) node;
                meshView.materialProperty().
                        bind(Bindings.when(model.isPairedViewPropertyProperty().and(isPaired))
                        .then(DefaultPhongMaterials.PAIR_MATERIAL)
                        .otherwise(DefaultPhongMaterials.RIBOSE_MATERIAL));
            }
        }

        for (Node node : nucleobase3d.getChildren()) {
            MeshView meshView = (MeshView) node;
            meshView.materialProperty().bind(getNucleoBaseBinding());
        }*/
        this.group3d = new Group(getPhosphateSphere(),ribose.getPresentation3d(),nucleobase3d);
    }

    //return Nucleobase Binding
   /* private ObjectBinding getNucleoBaseBinding(){
        NucleotideClasses currentClass = getNucleotideClass();
        switch (currentClass){
            case ADENINE:
                return Bindings.when(model.isPairedViewPropertyProperty().and(isPaired))
                        .then(DefaultPhongMaterials.PAIR_MATERIAL)
                        .otherwise(DefaultPhongMaterials.ADENINE_MATERIAL);
            case URACIL:
                return Bindings.when(model.isPairedViewPropertyProperty().and(isPaired))
                        .then(DefaultPhongMaterials.PAIR_MATERIAL)
                        .otherwise(DefaultPhongMaterials.URACIL_MATERIAL);
            case GUANINE:
                return Bindings.when(model.isPairedViewPropertyProperty().and(isPaired))
                        .then(DefaultPhongMaterials.PAIR_MATERIAL)
                        .otherwise(DefaultPhongMaterials.GUANINE_MATERIAL);
            case CYTOSIN:
                return Bindings.when(model.isPairedViewPropertyProperty().and(isPaired))
                        .then(DefaultPhongMaterials.PAIR_MATERIAL)
                        .otherwise(DefaultPhongMaterials.CYTOSIN_MATERIAL);
        }//never reached
        return Bindings.when(model.isPairedViewPropertyProperty().and(isPaired))
                .then(DefaultPhongMaterials.PAIR_MATERIAL)
                .otherwise(DefaultPhongMaterials.CYTOSIN_MATERIAL);
    }*/


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

    public boolean getIsAdenine() {
        return isAdenine.get();
    }

    public BooleanProperty isAdenineProperty() {
        return isAdenine;
    }

    public void setIsAdenine(boolean isAdenine) {
        this.isAdenine.set(isAdenine);
    }

    public boolean getIsGuanine() {
        return isGuanine.get();
    }

    public BooleanProperty isGuanineProperty() {
        return isGuanine;
    }

    public void setIsGuanine(boolean isGuanine) {
        this.isGuanine.set(isGuanine);
    }

    public boolean getIsUracil() {
        return isUracil.get();
    }

    public BooleanProperty isUracilProperty() {
        return isUracil;
    }

    public void setIsUracil(boolean isUracil) {
        this.isUracil.set(isUracil);
    }

    public boolean getIsCytosin() {
        return isCytosin.get();
    }

    public BooleanProperty isCytosinProperty() {
        return isCytosin;
    }

    public void setIsCytosin(boolean isCytosin) {
        this.isCytosin.set(isCytosin);
    }

    public Pos2d getPos2d() {
        return pos2d;
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
}
