package Model.Nucleotides;

import Model.AtomRecord;
import Model.BondInferenceAnd2D.Pos2d;
import javafx.scene.Group;

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
    Pos2d getPosition2d();
    void setPair(INucleotide nucleotide);
    INucleotide getPairMate();
    boolean[] checkBackboneGiven();
    boolean checkCoordsGiven();
    String getName();
    int getResidueNumber();
    boolean getIsPaired();
    String getRisidue();
    Pos2d getPos2d();
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

    enum NucleotideClasses{ADENINE,URACIL,GUANINE,CYTOSIN};

}
