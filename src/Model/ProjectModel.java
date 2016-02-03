package Model;

import Model.BondInferenceAnd2D.Pos2d;
import Model.Nucleotides.INucleotide;
import Model.PDBReader.PDBReader;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;

/**
 * Created by Philipp on 2016-01-21.
 * holds PDB representation and creates 2d model
 */
public class ProjectModel extends PDBModel {

    private PDBReader pdbReader;
    private StringProperty pdbFileNameProperty;


    public ProjectModel(){
        super();
        this.pdbFileNameProperty = new SimpleStringProperty("No PDB-File selected");
        this.pdbReader = new PDBReader();
    }

    //GETTER AND SETTER
    public PDBReader getPdbReader() {
        return pdbReader;
    }

    public void setPdbReader(PDBReader pdbReader) {
        this.pdbReader = pdbReader;
    }

    public String getPdbFileNameProperty() {
        return pdbFileNameProperty.get();
    }

    public StringProperty pdbFileNamePropertyProperty() {
        return pdbFileNameProperty;
    }

    public void setPdbFileNameProperty(String pdbFileNameProperty) {
        this.pdbFileNameProperty.set(pdbFileNameProperty);
    }

    public void setUp2DCoords(double[][] circleCoords, double[][] finalCoords){
        this.setWorld2dStart(circleCoords);
        this.setWorld2dEnd(finalCoords);

        ArrayList<INucleotide> nucleotides = getNucleotideList();
        for (int i = 0; i < nucleotides.size(); i++) {
            INucleotide currentN = nucleotides.get(i);
            Pos2d start = new Pos2d(circleCoords[i][0],circleCoords[i][1]);
            Pos2d end = new Pos2d(finalCoords[i][0],finalCoords[i][1]);
            currentN.setUp2dCoords(start,end);//automatically creates 2d representation
        }
    }


}
