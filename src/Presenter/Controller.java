package Presenter;

import Model.NucleotideRepresentation;
import Model.PDBReader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Philipp on 2016-01-27.
 */
public class Controller {

    private Presenter presenter;

    @FXML
    private MenuBar menuBar;

    @FXML
    private MenuItem loadFileItem;

    @FXML
    private MenuItem clearFileItem;

    @FXML
    private MenuItem coloringAGCUViewItem;

    @FXML
    private MenuItem coloringPurinePyrimidineViewItem;

    @FXML
    private MenuItem coloringPairedViewItem;

    @FXML
    private TextField sequenceTextField;

    @FXML
    private TextField bracketsTextfield;

    @FXML
    private TextArea logger;

    @FXML
    private Text selectedFileText;

    @FXML
    private Group world2d;

    @FXML
    private TitledPane rootPane;

    @FXML
    private Pane stackPane;

    @FXML
    private Pane pane2d;

    @FXML
    void initialize(){
        this.presenter = new Presenter(
                stackPane,pane2d,logger,selectedFileText,sequenceTextField,bracketsTextfield
        );
    }

    @FXML
    void loadFile(ActionEvent ev){
            FileChooser fc = new FileChooser();
            // we only want .pdb files
            ArrayList<String> allowedFilendings = new ArrayList<String>();
            allowedFilendings.add("*.pdb");
            FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("PDB files (*.pdb)",allowedFilendings);
            fc.getExtensionFilters().add(filter);

            File f = fc.showOpenDialog(rootPane.getScene().getWindow());
            presenter.loadFile(f);
        }



    @FXML
    void clearFile(ActionEvent ev){
        presenter.clear();
    }

    @FXML
    void changeToAGCUColoring(ActionEvent ev){
        changeColoring(NucleotideRepresentation.AGCU);
    }

    @FXML
    void changeToPuPyColoring(ActionEvent ev){
        changeColoring(NucleotideRepresentation.PURINE_PYRIMIDINE);
    }

    @FXML
    void changeToPairedColoring(ActionEvent ev){
        changeColoring(NucleotideRepresentation.PAIRED);
    }

    private void changeColoring(NucleotideRepresentation nR){
        presenter.changeColoringTo(nR);
    }

}
