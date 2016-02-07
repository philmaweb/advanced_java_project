package Presenter;

import Model.NucleotideRepresentation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.ArrayList;
import GUI.Logger;
import javafx.stage.Stage;

/**
 * Created by Philipp on 2016-01-27.
 * passes the elements and events to the presenter
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
    private MenuItem centerViewItem;

    @FXML
    private TextFlow sequenceTextFlow;

    @FXML
    private Logger logger;

    @FXML
    private Text selectedFileText;


    @FXML
    private TitledPane rootPane;

    @FXML
    private Pane stackPane;

    @FXML
    private Pane pane2d;

    //Pass events to Presenter

    /**
     * Is called on initialization to create a Presenter
     */
    @FXML
    void initialize(){
        this.presenter = new Presenter(
                stackPane,pane2d,logger,selectedFileText,sequenceTextFlow
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

    @FXML
    void centerView(ActionEvent ev){presenter.centerView();}

    @FXML
    void switchBracketNucleotide(ActionEvent ev){
        presenter.changeNucleotideBracketView();
    }

    private void changeColoring(NucleotideRepresentation nR){
        presenter.changeColoringTo(nR);
    }

}
