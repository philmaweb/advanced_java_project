package Presenter;

import GUI.*;
import Model.AtomRecord;
import Model.BondInferenceAnd2D.Graph;
import Model.BondInferenceAnd2D.SpringEmbedder;
import Model.NucleotideRepresentation;
import Model.Nucleotides.INucleotide;
import Model.PDBReader.PDBReader;
import Model.ProjectModel;
import Presenter.Selection.MouseHandler;
import Presenter.Selection.MySelectionModel;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Philipp on 2016-01-25.
 * Handles all Views and Groups, gets access to view from controler due to fxml
 */
public class Presenter {

    private ProjectModel model;
    SubScene subScene3d;
    Rotate cameraRotateX;
    Rotate cameraRotateY;
    Translate cameraTranslate;

    private Group world3d;
    private Group world2d;
    private Group smallWorld3d;

    private ArrayList<Group> riboses3d;
    private ArrayList<Group> phosphates3d;

    private Group bonds3d;

    Pane stackPane;
    Pane pane2d;
    Logger logger;
    Text selectedFileText;
    TextFlow sequenceTextFlow;
    MySelectionModel mySelectionModel;


    /**
     *
     * @param stackPane
     * @param pane2d
     * @param logger
     * @param selectedFileText
     * @param sequenceTextFlow
     */
    public Presenter(
            Pane stackPane,
            Pane pane2d,
            Logger logger,
            Text selectedFileText,
            TextFlow sequenceTextFlow
            ) {

        this.model = new ProjectModel();
        this.stackPane = stackPane;
        this.pane2d = pane2d;
        this.logger = logger;
        this.selectedFileText = selectedFileText;
        this.sequenceTextFlow= sequenceTextFlow;


        this.initVariables();
        this.addBindings();
        this.addListeners();
    }

    /**
     * initialize Variables for parsing
     */
    private void initVariables(){

        riboses3d = new ArrayList<>();
        phosphates3d = new ArrayList<>();

        bonds3d =  new Group();

        smallWorld3d = new Group(bonds3d);
        world3d = new Group(smallWorld3d);

        world2d = new Group();

        //set font to monospaced
        Font font = Font.font("Monospaced",12);

        logger.setFont(font);
        logger.setEditable(false);
        logger.append("Welcome to RNA-Viewer 3000");
    }


    /**
     * add listeners to Subscene and setup Camera
     */
    private void addListeners(){

        final PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setNearClip(100);
        camera.setFarClip(100000.0);

        cameraRotateX = new Rotate(0, new Point3D(1, 0, 0));
        cameraRotateY = new Rotate(0, new Point3D(0, 1, 0));
        cameraTranslate = new Translate(0, 0, -1000);

        camera.getTransforms().addAll(cameraRotateX, cameraRotateY, cameraTranslate);

        subScene3d = new SubScene(world3d,stackPane.getWidth(),stackPane.getHeight()-200,true, SceneAntialiasing.BALANCED);
        subScene3d.setCamera(camera);
        stackPane.getChildren().add(subScene3d);

        //bind size of subscene to stackPane
        subScene3d.heightProperty().bind(stackPane.heightProperty());
        subScene3d.widthProperty().bind(stackPane.widthProperty());

        pane2d.getChildren().add(world2d);

        //initial Zoom
        cameraTranslate.setZ(-10000);

        MouseHandler.addMouseHanderToPane(stackPane,cameraRotateX,cameraRotateY,cameraTranslate);
        MouseHandler.addMouseHandlerTo2dPane(pane2d,world2d);

    }

    /**
        Add binding to text for file display in subsecene
     */
    private void addBindings(){
        selectedFileText.textProperty().bind(model.pdbFileNamePropertyProperty());
    }


    /**
     * load file with absolute path
     * @param f
     */
    public void loadFile(File f){
        if (f != null){
            this.clear();
            model.setPdbFileNameProperty(f.getName());
            PDBReader pdbReader = model.getPdbReader();
            try {
                BufferedReader br = new BufferedReader(new FileReader(f));
                pdbReader.read(br);
                //pdbReader.read(new BufferedReader(new FileReader("src/AUGC.pdb")));
            } catch (IOException e) {
                System.err.println("Could not read in the PDB file");
                e.printStackTrace();
                System.err.println(e.getCause() + "\n"+ e.getMessage());
                System.exit(1);
            }
//            logger.append("Opened File " + f.getName());
            model.setPdbRepresentationFromList(pdbReader.getChains());
            createWorld();
        }
    }

    /**
     * clear the panes and groups, called whenever a file is successfully selected
     */
    public void clear(){
        phosphates3d.clear();
        riboses3d.clear();
        smallWorld3d.getChildren().clear();
        smallWorld3d.getChildren().addAll(bonds3d);
        model.setPdbFileName("No PDB-File selected");
        model.setrNASequence("");
        model.setBrackets("");
        pane2d.getChildren().clear();
        world2d.getChildren().clear();
        pane2d.getChildren().add(world2d);
        sequenceTextFlow.getChildren().clear();
        logger.append("Cleared");
    }

    /**
     * create 3D and 2D View, model should already be setup
     */
    private void createWorld(){
        logger.append("Creating World");
        construct1DView();
        construct2DView();
        construct3DView();
        ArrayList<INucleotide> nucleotides = model.getNucleotideList();
        //add selection model
        //cannot be done before the file is loaded
        mySelectionModel = new MySelectionModel(nucleotides);
        for (int i = 0; i < nucleotides.size(); i++) {
            INucleotide n = nucleotides.get(i);
            final int index = i;
            n.getNucleobase().setOnMouseClicked((e)->{
                addNucleotideHandler(n,index);
            });
            n.getHBondGroup().setOnMouseClicked((e)->{
                NucleotideRepresentation nRepr = model.getCurrentNucleotideRepresentation();
                mySelectionModel.handlePairClickEvent(index,n,nRepr);
                if(n.getIsSelected()){
                    logger.append("Selected Nucleotides " + n.getName() + " and " + n.getPairMate().getName());
                }
                else {
                    logger.append("Deselected Nucleotides " + n.getName() + " and " + n.getPairMate().getName());
                }
            });
            n.getGroup2d().setOnMouseClicked((e)->{
                addNucleotideHandler(n,index);
            });
            n.getNucleotideTextRepresentation().setOnMouseClicked((e)->{
                addNucleotideHandler(n,index);
            });
        }
    }

    private void addNucleotideHandler(INucleotide n, int index){
        NucleotideRepresentation nRepr = model.getCurrentNucleotideRepresentation();
        mySelectionModel.handleClickEvent(index,n,nRepr);
        if(n.getIsSelected()){
            logger.append("Selected Nucleotide " + n.getName());
        }
        else {
            logger.append("Deselected Nucleotide " + n.getName());
        }
    }

    /**
     * Fill TextFlow with nucleotides / DotBracket representaion
     */
    private void construct1DView() {
        int lastNucleotidePosition = 0;
        for (INucleotide n: model.getNucleotideList()) {
            int nucleotidePosition =n.getPositionInSequence();
            //detect gaps, insert as many as needed
            while (((nucleotidePosition - 1)!=lastNucleotidePosition) && (nucleotidePosition -1 > lastNucleotidePosition)){
                this.sequenceTextFlow.getChildren().add(NucleotideTextRepresentation.getGapRepresentation(lastNucleotidePosition+1));
                lastNucleotidePosition++;
                logger.append("Detected Gap at position " + lastNucleotidePosition);
            }
            this.sequenceTextFlow.getChildren().add(n.getNucleotideTextRepresentation());
            lastNucleotidePosition++;
        }
    }

    /**
     * Fill 3d World with nucleotides
     */
    public void construct3DView() {
        for (INucleotide n : model.getNucleotideList()) {
            smallWorld3d.getChildren().add(n.getGroup3d());
            riboses3d.add(n.getRiboseRepresentation());
            phosphates3d.add(n.getPhosphateSphere());
            smallWorld3d.getChildren().addAll(n.getPhosphateBonds(),n.getRiboseRepresentation(),n.getPhosphateSphere());
        }
        //Build Backbone
        connectPhosphates();
    }

    /**
     * connect phosphate backbone in order
     * only creates connection if difference between indices is 1
     */
    private void connectPhosphates(){
        HashMap<Integer, AtomRecord> phosphorMap = model.getPhosphorMap();
        ArrayList<Integer> myList = new ArrayList<>(phosphorMap.keySet());
        Collections.sort(myList);
        Iterator iterator = myList.iterator();
        Integer a;
        if (myList.size() >1){
            a = (Integer) iterator.next();
        }
        else{
            return;
        }
        while (iterator.hasNext()){
            Integer b = (Integer) iterator.next();
            if (Math.abs(b-a)==1){
                int i = (int) a;
                int j = (int) b;
                smallWorld3d.getChildren().add(MeshAnd3DObjectBuilder.createConnection(phosphorMap.get(i),phosphorMap.get(j)));
                a = b;
            }
        }
    }


    //2D STUFF

    /**
     * Construct the 2d representation
     */
    private void construct2DView() {
        Graph g = model.getGraph2d();
        int[][] edges = g.getEdges();
        int numberOfNodes = g.getNumberOfNodes();
        double[][] circleCoords = SpringEmbedder.computeSpringEmbedding(1,numberOfNodes,edges,null);
        double[][] finalCoords = SpringEmbedder.computeSpringEmbedding(25,numberOfNodes,edges,null);
        int maxWidth = (int) (pane2d.getMaxWidth()-10);
        int maxHeight = (int) (pane2d.getMaxHeight()-10);
        SpringEmbedder.centerCoordinates(finalCoords,10, maxWidth,10, maxHeight);

        model.setWorld2dEnd(finalCoords);
        //setup the 2d representation for all nucleotides
        model.setUp2DCoords(circleCoords,finalCoords);
        showFinalGraph();
    }

    /**
     * draw the graph with constructed coords
     * @param coords
     */
    private void draw2dWorld(double[][] coords) {
        addEdges(coords);
        //add Nodes in the end to place them on Top
        addNodes();
    }

    private void showFinalGraph() {
        draw2dWorld(model.getWorld2dEnd());
    }

    /**
     * generate Covalent and noncovalent Edges and add them to the 2d pane
     * @param coords
     */
    private void addEdges(double[][] coords) {
        //Covalent bonds
        ArrayList<Node> cLines = Circleand2DBuilder.generateCovalentEdges(coords, model.getGraph2d());
        for (Node cLine : cLines) {
            world2d.getChildren().add(cLine);
        }

        //Non covalent bonds
        ArrayList<Node> lines = Circleand2DBuilder.generateNonCovalentEdges(coords,model.getGraph2d());
        for (Node line : lines){
            world2d.getChildren().add(line);
        }
    }

    /**
     * get Node representaion form Nucleotides and add them to the world2d
     */
    private void addNodes() {
        for (INucleotide n : model.getNucleotideList()) {
            world2d.getChildren().add(n.getGroup2d());
        }
    }

    //VIEW-Changes

    /**
     * Color Change handling
     * @param nR
     */
    public void changeColoringTo(NucleotideRepresentation nR) {
        model.setCurrentNucleotideRepresentation(nR);
        recolorNucleotides();
        logger.append("Changed View to " + nR);
    }

    /**
     * calls update function of Nucleotides
     */
    private void recolorNucleotides() {
        for (INucleotide n : model.getNucleotideList()) {
            n.updateColoring(model.getCurrentNucleotideRepresentation());
        }
    }

    /**
     * Center 3d back to Original coords
     */
    public void centerView() {
        cameraTranslate.setZ(-10000);
        cameraRotateX.setAngle(0);
        cameraRotateY.setAngle(0);
        logger.append("Centered View");
    }


    /**
     * Show a AGCU PieChart
     */
    public void showPieChart() {
        logger.append("PieChart shown");
        FrequencyPieChart frequencyPieChart = new FrequencyPieChart(model.getNucleotideList());
        frequencyPieChart.getChartStage().show();
    }

    /**
     * Change between Nucleotide and Bracket View of Sequence representation
     */
    public void changeNucleotideBracketView() {
        boolean current = model.getIsNucleotideOrBracketRepresentation();
        model.setIsNucleotideOrBracketRepresentation(!current);
        if(current){
            logger.append("Changed to Bracket Notation");
        }
        else {
            logger.append("Changed to Nucleotide Notation");
        }
    }

    //ALLERT to show Exception
    private void showException(Exception e) {
        System.err.println(e.getCause());
        Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
        alert.showAndWait();
    }

}
