package Presenter;

import GUI.Circleand2DBuilder;
import GUI.MeshAnd3DObjectBuilder;
import Model.*;
import Model.BondInferenceAnd2D.Graph;
import Model.BondInferenceAnd2D.SpringEmbedder;
import Model.Nucleotides.INucleotide;
import Model.PDBReader.PDBReader;
import Presenter.Selection.MouseHandler;
import Presenter.Selection.MySelectionModel;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
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
 */
public class Presenter {

    private ProjectModel model;
    SubScene subScene3d;

    private Group world3d;
    private Group world2d;
    private Group smallWorld3d;

    private ArrayList<Group> riboses3d;
    private ArrayList<Group> phosphates3d;

    private Group adenines3d;
    private Group uracils3d;
    private Group cytosins3d;
    private Group guanines3d;
    private Group bonds3d;

    Pane stackPane;
    Pane pane2d;
    TextArea logger;
    Text selectedFileText;
    TextField sequenceTextField;
    TextField bracketTextField;
    MySelectionModel mySelectionModel;


    /**
     *
     * @param stackPane
     * @param pane2d
     * @param logger
     * @param selectedFileText
     * @param sequenceTextField
     * @param bracketTextField
     */
    public Presenter(
            Pane stackPane,
            Pane pane2d,
            TextArea logger,
            Text selectedFileText,
            TextField sequenceTextField,
            TextField bracketTextField
            ) {

        this.model = new ProjectModel();
        this.stackPane = stackPane;
        this.pane2d = pane2d;
        this.logger = logger;
        this.selectedFileText = selectedFileText;
        this.bracketTextField = bracketTextField;
        this.sequenceTextField = sequenceTextField;


        this.initVariables();
        this.addBindings();
        this.addListeners();
    }

    private void initVariables(){

        riboses3d = new ArrayList<>();
        phosphates3d = new ArrayList<>();

        adenines3d = new Group();
        uracils3d = new Group();
        cytosins3d = new Group();
        guanines3d =  new Group();
        bonds3d =  new Group();

        smallWorld3d = new Group(bonds3d,adenines3d,uracils3d,cytosins3d,guanines3d);
        world3d = new Group(smallWorld3d);

        world2d = new Group();

        //set font to beeing monospaced
        Font font = Font.font("Monospaced",12);
        bracketTextField.setFont(font);
        sequenceTextField.setFont(font);

    }


    private void addListeners(){

        final PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(100000.0);

        final Rotate cameraRotateX = new Rotate(0, new Point3D(1, 0, 0));
        final Rotate cameraRotateY = new Rotate(0, new Point3D(0, 1, 0));
        final Translate cameraTranslate = new Translate(0, 0, -1000);
        camera.getTransforms().addAll(cameraRotateX, cameraRotateY, cameraTranslate);

        subScene3d = new SubScene(world3d,stackPane.getWidth(),stackPane.getHeight()-200,true, SceneAntialiasing.BALANCED);
        subScene3d.setCamera(camera);
        stackPane.getChildren().add(subScene3d);

        //bind size of subscene to stackPane
        subScene3d.heightProperty().bind(stackPane.heightProperty());
        subScene3d.widthProperty().bind(stackPane.widthProperty());

        pane2d.getChildren().add(world2d);


        //initial good coords
        cameraTranslate.setZ(-3900);
        cameraRotateY.setAngle(-100);
        cameraRotateX.setAngle(28);

        MouseHandler.addMouseHanderToPane(stackPane,cameraRotateX,cameraRotateY,cameraTranslate);
        MouseHandler.addMouseHandlerTo2dPane(pane2d,world2d);


    }

    private void addBindings(){
        selectedFileText.textProperty().bind(model.pdbFileNamePropertyProperty());
        sequenceTextField.textProperty().bind(model.rNASequenceProperty());
        bracketTextField.textProperty().bind(model.bracketsProperty());
    }


    /**
     * load file with absolute path
     * @param f
     */
    public void loadFile(File f){
        this.clear();
        if (f != null){
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
            model.setPdbRepresentationFromList(pdbReader.getChains());
            createWorld();
        }
    }

    public void clear(){
        adenines3d.getChildren().clear();
        uracils3d.getChildren().clear();
        cytosins3d.getChildren().clear();
        guanines3d.getChildren().clear();
        phosphates3d.clear();
        riboses3d.clear();
        smallWorld3d.getChildren().clear();
        smallWorld3d.getChildren().addAll(bonds3d,adenines3d,uracils3d,cytosins3d,guanines3d);
        model.setPdbFileName("No PDB-File selected");
        model.setrNASequence("");
        model.setBrackets("");
        //fileDisplay.setText(model.getPdbFileName());
        pane2d.getChildren().clear();
        world2d.getChildren().clear();
        pane2d.getChildren().add(world2d);

        System.out.println("Cleared");
    }

    /**
     * create 3D and 2D View, model should already be setup
     */
    private void createWorld(){
        construct3DView();
        construct2DView();
        ArrayList<INucleotide> nucleotides = model.getNucleotideList();
        //add selection model
        mySelectionModel = new MySelectionModel(nucleotides);
        for (int i = 0; i < nucleotides.size(); i++) {
            INucleotide n = nucleotides.get(i);
            final int index = i;
            n.getGroup3d().setOnMouseClicked((e)->{
                NucleotideRepresentation nRepr = model.getCurrentNucleotideRepresentation();
                mySelectionModel.handleClickEvent(e,index,n,nRepr);
            });
            n.getGroup2d().setOnMouseClicked((e)->{
                NucleotideRepresentation nRepr = model.getCurrentNucleotideRepresentation();
                mySelectionModel.handleClickEvent(e,index,n,nRepr);
            });
        }
    }

    public void construct3DView() {
        System.out.println("adding chains from model");
        for (INucleotide n : model.getNucleotideList()) {
//            AtomRecord firstEntry = map.entrySet().iterator().next().getValue();
//            String residium = firstEntry.getResidium();
            switch (n.getNucleotideClass()) {
                case CYTOSIN:
                    cytosins3d.getChildren().add(n.getGroup3d());
                    break;
                case ADENINE:
                    adenines3d.getChildren().add(n.getGroup3d());
                    break;
                case GUANINE:
                    guanines3d.getChildren().add(n.getGroup3d());
                    break;
                case URACIL:
                    uracils3d.getChildren().add(n.getGroup3d());
                    break;
            }
            riboses3d.add(n.getRiboseRepresentation());
            phosphates3d.add(n.getPhosphateSphere());
            smallWorld3d.getChildren().addAll(n.getPhosphateBonds(),n.getRiboseRepresentation(),n.getPhosphateSphere());
        }
        System.out.println("Creating Phosphate connections");
        connectPhosphates();
    }



    private void connectPhosphates(){
        //connect two ps only if their index differs by one
        HashMap<Integer, AtomRecord> phosphorMap = model.getPhosphorMap();
//        System.out.println("phosphorMap is " + phosphorMap);
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



    /**
     * center around original
     * //TODO
     * @param points
     */
    public static ArrayList<Point3D> center(ArrayList<Point3D> points) {
        ArrayList<Point3D> result=new ArrayList<>(points.size());
        if (points.size() > 0) {
            double[] center = {0, 0, 0};

            for (Point3D point : points) {
                center[0] += point.getX();
                center[1] += point.getY();
                center[2] += point.getZ();
            }
            center[0] /= points.size();
            center[1] /= points.size();
            center[2] /= points.size();

            for (Point3D point : points) {
                result.add(point.subtract(new Point3D(center[0], center[1], center[2])));
            }
        }
        return result;
    }


    public void changeColoringTo(NucleotideRepresentation nR) {
        model.setCurrentNucleotideRepresentation(nR);
        recolorNucleotides();
    }

    private void recolorNucleotides() {
        for (INucleotide n :
                model.getNucleotideList()) {
            n.updateColoring(model.getCurrentNucleotideRepresentation());
        }
    }

    //2D Stuff

    private void construct2DView() {
        Graph g = model.getGraph2d();
        int[][] edges = g.getEdges();
        int numberOfNodes = g.getNumberOfNodes();
        double[][] circleCoords = SpringEmbedder.computeSpringEmbedding(1,numberOfNodes,edges,null);
        double[][] finalCoords = SpringEmbedder.computeSpringEmbedding(50,numberOfNodes,edges,null);
        int maxWidth = (int) (pane2d.getMaxWidth()-10);
        int maxHeight = (int) (pane2d.getMaxHeight()-10);
        SpringEmbedder.centerCoordinates(circleCoords,10, maxWidth ,10, maxHeight);
        SpringEmbedder.centerCoordinates(finalCoords,10, maxWidth,10, maxHeight);

        //TODO for later Animation
        model.setWorld2dStart(circleCoords);
        model.setWorld2dEnd(finalCoords);
        //setup the 2d representation for all nucleotides
        model.setUp2DCoords(circleCoords,finalCoords);
//        showFinalGraph();
        showFinalGraph();
    }

    private void draw2dWorld(double[][] coords) {
        addEdges(coords);
        addNonConvalentBonds(coords);
        addNodes();
    }

    private void showCircleGraph() {
        draw2dWorld(model.getWorld2dStart());

    }
    private void showFinalGraph() {
        draw2dWorld(model.getWorld2dEnd());
    }

    private void addEdges(double[][] coords) {
//        world2d.getChildren().clear();
        ArrayList<Node> cLines = Circleand2DBuilder.generateCovalentEdges(coords, model.getGraph2d());
        for (Node cLine : cLines) {
            world2d.getChildren().add(cLine);
        }
    }

    private void addNonConvalentBonds(double[][] coords) {
        ArrayList<Node> lines = Circleand2DBuilder.generateNonCovalentEdges(coords,model.getGraph2d());
        for (Node line : lines){
            world2d.getChildren().add(line);
        }
    }

    private void addNodes() {
        for (INucleotide n : model.getNucleotideList()) {
            world2d.getChildren().add(n.getGroup2d());
        }
    }



    //ALLERT to show Exception
    private void showException(Exception e) {
        System.err.println(e.getCause());
        Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
        alert.showAndWait();
    }

}
