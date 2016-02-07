package GUI;

import Model.Nucleotides.INucleotide;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * Created by Philipp on 2016-02-06.
 * Represents a single selectable Nucleotide in 1D
 * For Nucleotide representation can be AUGC or -
 * For DotBracket representation can be .-()
 */
public class NucleotideTextRepresentation extends Text {

    private int positionInSequence;
    private INucleotide.NucleotideClasses myClass;
    private String letter;
    private String dotBracket;
    BooleanProperty isSelected;
    BooleanProperty isPaired;
    BooleanProperty isNucleotideRepresentation;
    BooleanProperty isLeftBracket;


    public NucleotideTextRepresentation(int positionInSequence, INucleotide.NucleotideClasses whichNucleotide) {
        this.positionInSequence = positionInSequence;
        this.myClass = whichNucleotide;

        this.isSelected = new SimpleBooleanProperty(false);
        this.isPaired = new SimpleBooleanProperty(false);
        //Will be bound in a Nucleotide class, gaps dont get a change
        this.isNucleotideRepresentation= new SimpleBooleanProperty(true);
        this.isLeftBracket= new SimpleBooleanProperty(false);
        inferLetterAndColor();
        this.setFont(Font.font("Monospaced", FontWeight.BOLD,25));
        this.underlineProperty().bind(isSelected);
        Tooltip tooltip = new Tooltip(letter + " " + positionInSequence);
        tooltip.setFont(Font.font("Monospaced",FontWeight.NORMAL,12));
        Tooltip.install(this,tooltip);
        this.setText(letter);

        isNucleotideRepresentation.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                switchNucleotideDotBracket();
            }
        });
    }

    /**
     * setup letter representation
     */
    private void inferLetterAndColor() {
        switch (myClass) {
            case ADENINE:
                this.letter = "A";
                this.setFill(DefaultPhongMaterials.ADENINE_MATERIAL.getDiffuseColor());
                break;
            case URACIL:
                this.setFill(DefaultPhongMaterials.URACIL_MATERIAL.getDiffuseColor());
                this.letter = "U";
                break;
            case GUANINE:
                this.setFill(DefaultPhongMaterials.GUANINE_MATERIAL.getDiffuseColor());
                this.letter = "G";
                break;
            case CYTOSIN:
                this.setFill(DefaultPhongMaterials.CYTOSIN_MATERIAL.getDiffuseColor());
                this.letter = "C";
                break;
            case GAP:
                this.setFill(Color.BLACK);
                this.letter = "-";
                break;
        }
    }

    public BooleanProperty isSelectedProperty() {
        return isSelected;
    }

    private void switchNucleotideDotBracket(){
        if (isNucleotideRepresentation.get()){
            this.setText(letter);
        }
        else {
            if (isPaired.get()){
                if (isLeftBracket.get()){
                    dotBracket = "(";
                }
                else {
                    dotBracket = ")";
                }
            }
            //not paired
            else {
                dotBracket = ".";
            }
            setText(dotBracket);
        }
    }

    public BooleanProperty isPairedProperty() {
        return isPaired;
    }

    public BooleanProperty isNucleotideRepresentationProperty() {
        return isNucleotideRepresentation;
    }

    public BooleanProperty isLeftBracketProperty() {
        return isLeftBracket;
    }

    /**
     * get Gap representation
     * @param pos
     * @return
     */
    public static NucleotideTextRepresentation getGapRepresentation(int pos){
        return new NucleotideTextRepresentation(pos,INucleotide.NucleotideClasses.GAP);
    }
}
