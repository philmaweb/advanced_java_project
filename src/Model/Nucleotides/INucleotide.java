package Model.Nucleotides;

import GUI.NucleotideTextRepresentation;
import Model.AtomRecord;
import Model.BondInferenceAnd2D.Pos2d;
import Model.NucleotideRepresentation;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Philipp on 2015-10-17.
 * Interface to interact with Nucleotides
 */
public interface INucleotide {
    //holds the information about an RNA Nucleotide, nucleotides are A,G,C and U

    int getPositionInSequence();
    //If a pair is detected during HBond inference, is is set for both mates
    void setPair(INucleotide nucleotide);
    INucleotide getPairMate();
    //Checks if backbone coords are given by looking for the correct keys in the hashmap
    boolean[] checkBackboneGiven();
    boolean checkCoordsGiven();

    //Get Groups of representations
    Group getPhosphateSphere();
    Group getPhosphateBonds();
    Group getRiboseRepresentation();
    Group getGroup3d();
    Group getNucleobase();
    Group getHBondGroup();

    //2d representation of one Nucleotide
    Group getGroup2d();
    //coords in the 2dpane
    void setUp2dCoords(Pos2d pos2);
    Pos2d getPosition2DEnd();
    void setPosition2DEnd(Pos2d pos);


    //1d representation
    NucleotideTextRepresentation getNucleotideTextRepresentation();

    //is watson crick pair?
    boolean isWCPair(INucleotide nucleotide);//return true if we have AU or GC
    void setIsLeftBracket(boolean isLeftBracket);//is true if the Nucleotide is a Left bracket
    boolean getIsLeftBracket();//is true if the Nucleotide is a Left bracket
    //check if Donors and acceptors are available
    boolean hasHBondDonorsAndAcceptors();
    void checkHasHBondDoAc();
    AtomRecord getHFromDonor(String donorKey);//get the H Atom from the Donor, needed for distance and Angle calculation
    ArrayList<String[]> getKeyPairsToCheckForHBonds();

    //check if a Nucleotide is selected
    boolean getIsSelected();
    void setIsSelected(boolean b);

    //update Nucleobase Coloring
    void updateColoring(NucleotideRepresentation representation);
    Color get2DColorByClass();// A red, U green, C Yellow, G Blue

    //accessor fields
    String getName();
    int getResidueNumber();
    boolean getIsPaired();
    String getRisidue();
    HashMap<String, AtomRecord> getResidueMap();
    Ribose getRibose();
    Phosphate getPhosphate();

    //makes switching easy
    NucleotideClasses getNucleotideClass();
    enum NucleotideClasses{ADENINE,URACIL,GUANINE,CYTOSIN,GAP};

}
