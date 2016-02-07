package Model;

import Model.BondInferenceAnd2D.Graph;
import Model.BondInferenceAnd2D.HBondInferer;
import Model.Nucleotides.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by Philipp on 2016-01-13.
 * stores all the info from the PDB-file
 */
public class PDBModel {

    private ArrayList<INucleotide> nucleotideList;
    private StringProperty rNASequence;
    private StringProperty brackets;
    private HashMap<Integer, AtomRecord> phosphorMap;
    private String pdbFileName;
    private NucleotideRepresentation currentNucleotideRepresentation;

    private Graph graph2d;

    private double[][] world2dStart;
    private double[][] world2dEnd;

    private BooleanProperty isNucleotideOrBracketRepresentation;


    public PDBModel() {
        this.nucleotideList = new ArrayList<>();
        currentNucleotideRepresentation = NucleotideRepresentation.AGCU;
        this.graph2d = new Graph();
        isNucleotideOrBracketRepresentation = new SimpleBooleanProperty(true);

        rNASequence = new SimpleStringProperty("");
        brackets = new SimpleStringProperty("");
    }

    private INucleotide extractRecordByNames(ArrayList<AtomRecord> residue) throws Exception {
        HashMap<String, AtomRecord> map = new HashMap<>();
        for (AtomRecord r : residue) {
            map.put(r.getName(),r);
        }
        String validKey = map.keySet().iterator().next();
        String nucleotideIdentifier = map.get(validKey).getResidium();
//        System.out.println(nucleotideIdentifier + map.get(validKey).getIndexOfResidium());
        switch (nucleotideIdentifier){
            case "A":
            case "ADE":
                return new Adenine(map,this);
            case "G":
            case "GUA":
                return new Guanine(map,this);
            case "C":
            case "CYT":
                return new Cytosin(map,this);
            case "U":
            case "URA":
                return new Uracil(map,this);
        }
//        return null;
        throw new Exception("Cannot Identify Residue to a RNA Base");
    }

    private void extractPhosphorMap(){
        HashMap<Integer, AtomRecord> map = new HashMap<>();
        for (INucleotide n : nucleotideList){
            Phosphate p = n.getPhosphate();
            if (null != p){
                map.put(n.getResidueNumber(),n.getPhosphate().getAtomRecord());
            }
        }
        this.setPhosphorMap(map);
    }

    public ArrayList<INucleotide> getNucleotideList() {
        return nucleotideList;
    }

    public void setNucleotideList(ArrayList<INucleotide> nucleotideList) {
        this.nucleotideList = nucleotideList;
        sortNucleotides();
        extractPhosphorMap();//extracts PhosphorMap for Backbone
        extractSequence();//extracts sequence from Nucleotide List
        HBondInferer hBondInferer = new HBondInferer(nucleotideList);//sets the Pairs if they have a mate
        extractDotBracketNotation();//extracts dotBracketNotation
        try {
            graph2d.parseNotation(brackets.getValue());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * extract dot bracket notation, does not check for pseudoKnots
     */
    private void extractDotBracketNotation() {
        String rv = "";
        for (INucleotide n : nucleotideList){
            if (n.getIsPaired()){
                //check if pair has smaller or bigger index
                if(n.getPositionInSequence() < n.getPairMate().getPositionInSequence()){
                    rv += "(";
                    n.setIsLeftBracket(true);
                }
                else{
                    rv += ")";
                }
            }
            else {
                rv += ".";
            }
        }
        brackets.setValue(rv);

    }

    /**
     * expects sorted nucleotide List
     * creates primary sequence and sets rNASequence
     */
    private void extractSequence() {
        String s = "";
        for (INucleotide n : nucleotideList) {
            s+=n.getRisidue();
        }
        rNASequence.setValue(s);
    }

    /**
     * sorts nucleotideList by residue number
     */
    private void sortNucleotides() {
        Collections.sort(nucleotideList, (o1, o2) -> {
            //a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the second
            int n1 = o1.getResidueNumber();
            int n2 = o2.getResidueNumber();
            if (n1 < n2){
                return -1;
            }
            if(n2 < n1){
                return 1;
            }
            return 0;
        });
    }

    public void setPdbRepresentationFromList(ArrayList<ArrayList<AtomRecord>> lis) {
        ArrayList<INucleotide> pdbRepr = new ArrayList<>();
        for (ArrayList<AtomRecord> chain: lis) {
            try{
                pdbRepr.add(extractRecordByNames(chain));
            }
            catch (Exception e){
                System.err.println("Could not extract residue");
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
        }
        setNucleotideList(pdbRepr);
    }

    public void setPdbFileName(String pdbFileName) {
        this.pdbFileName = pdbFileName;
    }

    public NucleotideRepresentation getCurrentNucleotideRepresentation() {
        return currentNucleotideRepresentation;
    }

    public void setCurrentNucleotideRepresentation(NucleotideRepresentation currentNucleotideRepresentation) {
        this.currentNucleotideRepresentation = currentNucleotideRepresentation;
    }

    public HashMap<Integer, AtomRecord> getPhosphorMap() {
        return this.phosphorMap;
    }

    public void setPhosphorMap(HashMap<Integer, AtomRecord> phosphorMap) {
        this.phosphorMap = phosphorMap;
    }

    public void setrNASequence(String rNASequence) {
        this.rNASequence.set(rNASequence);
    }

    public void setBrackets(String brackets) {
        this.brackets.set(brackets);
    }

    public Graph getGraph2d() {
        return graph2d;
    }

    public void setWorld2dStart(double[][] world2dStart) {
        this.world2dStart = world2dStart;
    }

    public double[][] getWorld2dEnd() {
        return world2dEnd;
    }

    public void setWorld2dEnd(double[][] world2dEnd) {
        this.world2dEnd = world2dEnd;
    }

    public boolean getIsNucleotideOrBracketRepresentation() {
        return isNucleotideOrBracketRepresentation.get();
    }

    public BooleanProperty isNucleotideOrBracketRepresentationProperty() {
        return isNucleotideOrBracketRepresentation;
    }

    public void setIsNucleotideOrBracketRepresentation(boolean isNucleotideOrBracketRepresentation) {
        this.isNucleotideOrBracketRepresentation.set(isNucleotideOrBracketRepresentation);
    }
}


