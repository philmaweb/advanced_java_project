package Model.Nucleotides;

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
 * Holds all Info of a Nucleotide
 *
 */
public interface INucleotide {
    //holds the information about an RNA Nucleotide, nucleotides are A,G,C and U

    int getPositionInSequence();
    void setPair(INucleotide nucleotide);
    INucleotide getPairMate();
    boolean[] checkBackboneGiven();
    boolean checkCoordsGiven();
    String getName();
    int getResidueNumber();
    boolean getIsPaired();
    String getRisidue();
    HashMap<String, AtomRecord> getResidueMap();
    Ribose getRibose();
    Phosphate getPhosphate();
    Group getPhosphateSphere();
    Group getPhosphateBonds();
    Group getRiboseRepresentation();
    Group getGroup3d();
    Group getGroup2d();
    NucleotideClasses getNucleotideClass();
    boolean isWCPair(INucleotide nucleotide);//return true if we have AU or GC
    boolean hasHBondDonorsAndAcceptors();
    void checkHasHBondDoAc();
    ArrayList<AtomRecord> getHbondAcceptors();
    ArrayList<AtomRecord> getHbondDonors();
    AtomRecord getHFromDonor(String donorKey);//get the H Atom from the Donor, needed for distance and Angle calculation
    ArrayList<String[]> getKeyPairsToCheckForHBonds();

    void setUp2dCoords(Pos2d pos1, Pos2d pos2);
    Pos2d getPosition2DStart();
    Pos2d getPosition2DEnd();
    void setPosition2DStart(Pos2d pos);
    void setPosition2DEnd(Pos2d pos);
    boolean getIsSelected();
    void setIsSelected(boolean b);

    void updateColoring(NucleotideRepresentation representation);
    Color get2DColorByClass();// A red, U green, C Yellow, G Blue

    enum NucleotideClasses{ADENINE,URACIL,GUANINE,CYTOSIN};

}
