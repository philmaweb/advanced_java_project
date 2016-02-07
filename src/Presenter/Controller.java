package Presenter;

import Model.NucleotideRepresentation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;

import java.awt.*;
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
    void loadFile(){
            FileChooser fc = new FileChooser();
            // we only want .pdb files
            ArrayList<String> allowedFilendings = new ArrayList<>();
            allowedFilendings.add("*.pdb");
            FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("PDB files (*.pdb)",allowedFilendings);
            fc.getExtensionFilters().add(filter);

            File f = fc.showOpenDialog(rootPane.getScene().getWindow());
            presenter.loadFile(f);
        }


    @FXML
    void clearFile(){
        presenter.clear();
    }

    @FXML
    void changeToAGCUColoring(){
        changeColoring(NucleotideRepresentation.AGCU);
    }

    @FXML
    void changeToPuPyColoring(){
        changeColoring(NucleotideRepresentation.PURINE_PYRIMIDINE);
    }

    @FXML
    void changeToPairedColoring(){
        changeColoring(NucleotideRepresentation.PAIRED);
    }

    @FXML
    void centerView(){presenter.centerView();}

    @FXML
    void switchBracketNucleotide(){
        presenter.changeNucleotideBracketView();
    }

    private void changeColoring(NucleotideRepresentation nR){
        presenter.changeColoringTo(nR);
    }

    @FXML
    void showPieChart(){
        presenter.showPieChart();
    }

    @FXML
    void showHelp(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("For your information");
        alert.setTitle("Help Information");
        alert.setContentText("This Program was written for the course Advanced Java for Bioinformatics +" +
                "in the WinterSemester 2015/16. \nWith RNA Viewer 3000 you can load and " +
                "view RNA molecules in the PDB file format. By extracting hydrogen bonds, " +
                "the secondary structure approximation is computed.\n" +
                "First Load a PDB-File. \n" +
                "In the tertiary structure window you can turn the view by mouse dragging," +
                " when pressing shift you can zoom in or out and with alt you can move the camera horizontally." +
                " When needed use 'View'-'Center View' to come back to the original 3D-position." +
                " Each Nucleobase can be selected by clicking on it. Clicking a second time with remove the" +
                " selection and highlighting. When clicking on a HBond the Watson-Crick Pair is selected." +
                " You can choose between different Views, that either highlight all Nucleobases with different colors," +
                " to highlight purine and pyrimidine bases or to highlight paired bases.\n" +
                "You can also switch between the Nucleotide and Dot-Bracket notation for the primary sequence" +
                " and move the 2D-representation around by dragging it.");
        presenter.logger.append("Help opened");
        alert.showAndWait();
    }
}
