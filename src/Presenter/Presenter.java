package Presenter;

import GUI.DefaultPhongMaterials;
import GUI.MeshAnd3DObjectBuilder;
import Model.*;
import Model.BondInferenceAnd2D.Graph;
import Model.BondInferenceAnd2D.SpringEmbedder;
import Model.Nucleotides.INucleotide;
import javafx.collections.ObservableList;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
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

    private double mouseDownX;
    private double mouseDownY;


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


        //initial good coords
        cameraTranslate.setZ(-3900);
        cameraRotateY.setAngle(-100);
        cameraRotateX.setAngle(28);

        addMouseHanderToPane(stackPane,cameraRotateX,cameraRotateY,cameraTranslate);
    }

    /**
     * handle mouse events
     *
     * @param pane
     * @param cameraTranslate
     */
    private void addMouseHanderToPane(Pane pane, Rotate cameraRotateX, Rotate cameraRotateY, Translate cameraTranslate) {
        pane.setOnMousePressed((me) -> {
            mouseDownX = me.getSceneX();
            mouseDownY = me.getSceneY();
        });
        pane.setOnMouseDragged((me) -> {
            double mouseDeltaX = me.getSceneX() - mouseDownX;
            double mouseDeltaY = me.getSceneY() - mouseDownY;

            if (me.isShiftDown()) {
                cameraTranslate.setZ(cameraTranslate.getZ() + 5*mouseDeltaY);
            }
            else if (me.isAltDown()){
                cameraTranslate.setX(cameraTranslate.getX() + 5*mouseDeltaX);
                cameraTranslate.setY(cameraTranslate.getY() + 5*mouseDeltaY);

            }
            else // rotate
            {
                cameraRotateY.setAngle(cameraRotateY.getAngle() + mouseDeltaX);
                cameraRotateX.setAngle(cameraRotateX.getAngle() - mouseDeltaY);
                //System.out.println(cameraRotateY.getAngle() + mouseDeltaX);
                //System.out.println(cameraRotateX.getAngle() + mouseDeltaY);
            }
            mouseDownX = me.getSceneX();
            mouseDownY = me.getSceneY();
        });
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
        //fileDisplay.setText(model.getPdbFileName());
        pane2d.getChildren().clear();
        System.out.println("Cleared");
    }

    /**
     * create 3D and 2D View, model should already be setup
     */
    private void createWorld(){
        construct3DView();
        construct2DView();
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
        recolor3dNucleotides();
        //recolor2dNucleotides();
    }

    private void recolor3dNucleotides(){
        //TODO color ribose
        PhongMaterial adenineMat = DefaultPhongMaterials.ADENINE_MATERIAL;
        PhongMaterial guanineMat = DefaultPhongMaterials.GUANINE_MATERIAL;
        PhongMaterial cytosinMat = DefaultPhongMaterials.CYTOSIN_MATERIAL;
        PhongMaterial uracilMat = DefaultPhongMaterials.URACIL_MATERIAL;
//        PhongMaterial riboseMat = DefaultPhongMaterials.RIBOSE_MATERIAL;
//        PhongMaterial phosphateMat = DefaultPhongMaterials.PHOSPHATE_MATERIAL;
        switch (model.getCurrentNucleotideRepresentation()){
            case AGCU:
                break;
            case PURINE_PYRIMIDINE:
                adenineMat = DefaultPhongMaterials.PURINE_MATERIAL;
                guanineMat = DefaultPhongMaterials.PURINE_MATERIAL;
                cytosinMat = DefaultPhongMaterials.PYRIMIDINE_MATERIAL;
                uracilMat = DefaultPhongMaterials.PYRIMIDINE_MATERIAL;
                break;
            case PAIRED:
                break;
        }
        for (INucleotide n : model.getNucleotideList()){
            for (Node node : n.getGroup3d().getChildren()){
                Group g = (Group) node;
                for (Node node2 : g.getChildren()){
                    if(n.getIsPaired() && (model.getCurrentNucleotideRepresentation().equals(NucleotideRepresentation.PAIRED))){
                        ((Shape3D) node2).setMaterial(DefaultPhongMaterials.PAIR_MATERIAL);
                    }
                    else {
                        switch (n.getNucleotideClass()) {
                            case ADENINE:
                                ((Shape3D) node2).setMaterial(adenineMat);
                                break;
                            case URACIL:
                                ((Shape3D) node2).setMaterial(uracilMat);
                                break;
                            case GUANINE:
                                ((Shape3D) node2).setMaterial(guanineMat);
                                break;
                            case CYTOSIN:
                                ((Shape3D) node2).setMaterial(cytosinMat);
                                break;
                        }
                    }
                }
            }
        }
    }

    //2D Stuff

    private void construct2DView() {

        Graph g = model.getGraph2d();
        int[][] edges = g.getEdges();
        int numberOfNodes = g.getNumberOfNodes();
        double[][] circleCoords = SpringEmbedder.computeSpringEmbedding(1,numberOfNodes,edges,null);
        double[][] finalCoords = SpringEmbedder.computeSpringEmbedding(50,numberOfNodes,edges,null);

        SpringEmbedder.centerCoordinates(circleCoords,10,500,10,500);
        SpringEmbedder.centerCoordinates(finalCoords,10,500,10,500);

        //TODO for later Animation
        model.setWorld2dStart(circleCoords);
        model.setWorld2dEnd(finalCoords);

        showFinalGraph();

    }



    private void draw2dWorld(double[][] coords) {
        drawEdges(coords);
        drawNonConvalentBonds(coords);
        drawNodes(coords);
    }

    private void showCircleGraph() {
        draw2dWorld(model.getWorld2dStart());

    }
    private void showFinalGraph() {
        draw2dWorld(model.getWorld2dEnd());
    }

    private void drawEdges(double[][] coords) {
        pane2d.getChildren().clear();
        ArrayList<Node> cLines = generateCovalentEdges(coords);
        for (int i = 0; i < cLines.size(); i++) {
            pane2d.getChildren().add(cLines.get(i));
        }
    }

    private ArrayList<Node> generateCovalentEdges(double[][] coords) {
        ArrayList<Node> cLines = new ArrayList<>();

        //int[][] edges = this.getGraph().getEdges();
        int numberOfNodes = model.getGraph2d().getNumberOfNodes();
        for (int i = 0; i < numberOfNodes -1 ; i++) {
            Line l = new Line(coords[i][0],coords[i][1],coords[i+1][0],coords[i+1][1]);
            cLines.add(l);
        }
        return cLines;
    }

    private void drawNonConvalentBonds(double[][] coords) {
        ArrayList<Node> lines = generateNonCovalentEdges(coords);
        for (int i = 0; i < lines.size(); i++) {
            pane2d.getChildren().add(lines.get(i));
        }
    }

    private ArrayList<Node> generateNonCovalentEdges(double[][] coords){
        ArrayList<Node> nodes = new ArrayList<>();
        int[][] edges = model.getGraph2d().getEdges();
        int numberOfNodes = model.getGraph2d().getNumberOfNodes();
        for (int i = numberOfNodes-1; i < edges.length ; i++) {
            //index of point in coords
            int e1 = edges[i][0];
            int e2 = edges[i][1];
            //get coords with index
            double[] p1 = new double[]{coords[e1][0],coords[e1][1]};
            double[] p2 = new double[]{coords[e2][0],coords[e2][1]};

            Line l = new Line(p1[0],p1[1],p2[0],p2[1]);
            l.setStroke(Color.BLUEVIOLET);
            nodes.add(l);
        }
        return nodes;
    }

    private void drawNodes(double[][] coords) {
        Group g = new Group();
        ArrayList<Node> nodes = generateNodes(coords);
        for (int i = 0; i < nodes.size(); i++) {
            g.getChildren().addAll(nodes.get(i));
        }
        //p.getChildren().removeAll();
        pane2d.getChildren().add(g);
//        this.setgNodes(g);
    }

    private ArrayList<Node> generateNodes(double[][] coords){
        ArrayList<Node> nodes = new ArrayList<>();
        for (int i = 0; i < coords.length ; i++) {
            double x = coords[i][0];
            double y = coords[i][1];
            //TODO need to improve on that, no accessing of textfields for this
            //instead use nucleotideList
            String s = String.valueOf(sequenceTextField.getText().charAt(i)).toUpperCase();
            Circle cSurround =  new Circle(x,y,9,Color.BLACK);
            Circle cGround =    new Circle(x,y,8,getColorByText(s));
            Circle cTop =       new Circle(x,y,6,Color.WHITE);
            Text t =            new Text(x-4,y+5,s);
            Tooltip tooltip = new Tooltip("Nucleotide: " + s + "\n Position: " + (i+1));
            Tooltip.install(cTop,tooltip);
            Tooltip.install(cGround,tooltip);
            Tooltip.install(t,tooltip);

            Group nucleotideGroup = new Group(cSurround,cGround,cTop,t);
            nodes.add(nucleotideGroup);
        }
        return nodes;
    }

    private Color getColorByText(String s) {
        switch (s) {
            case "A":
                return Color.RED;
            case "U":
                return Color.GREEN;
            case "G":
                return Color.BLUE;
            case "C":
                return Color.YELLOW;
            default:
                return Color.BLACK;
        }
    }



    //ALLERT to show Exception
    private void showException(Exception e) {
        System.err.println(e.getCause());
        Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
        alert.showAndWait();
    }

}
