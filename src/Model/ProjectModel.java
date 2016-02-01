package Model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.HashMap;

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


}
