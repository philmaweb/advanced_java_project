package Model.BondInferenceAnd2D;


import Model.AtomRecord;
import Model.Nucleotides.INucleotide;

import java.util.ArrayList;

/**
 * Created by Philipp on 2016-01-31.
 * Infers HBonds according to distance and angle
 */
public class HBondInferer {


    double maxDistance;
    double minDistance;
    double minAngle;
    double maxAngle;

    public HBondInferer(ArrayList<INucleotide> nucleotideList){
        inferPairs(nucleotideList);
        this.maxDistance = 4;
        this.minDistance = 1;
        this.minAngle = 0;
        this.maxAngle = 1;
    }

    /**
     * inefficient implementation, if too slow implement more efficient one
     * @param lis
     */
    public void inferPairs(ArrayList<INucleotide> lis){
        //double[][] distances = new double[lis.size()][lis.size()];
        //double[][] angles = new double[lis.size()][lis.size()];
        //populate arrays
        for (int i = 0; i < lis.size(); i++) {
            INucleotide n = lis.get(i);
            //possible partner list, from these the best is selected if applicable
            ArrayList<INucleotide> possiblePartnerList = new ArrayList<>();
            for (int j = 0; j < lis.size(); j++) {
                INucleotide m = lis.get(j);
                //has to be in every Nucleotide
                double approxDistance = n.getResidueMap().get("C2").getDistanceTo(m.getResidueMap().get("C2"));
                if ((approxDistance < 10.0) &&
                        n.hasHBondDonorsAndAcceptors() &&
                        m.hasHBondDonorsAndAcceptors() &&
                        n.isWCPair(m)){
                    //calc more accurate distance and look for Hbonds
                    //print angle and distance
                    ArrayList<Double> distances = calculateDistanceHBonds(n,m);
                    ArrayList<Double> angles = calculateAngleHBonds(n,m);
                    System.out.println(distances);
                    System.out.println(angles);
                }
            }
        }
    }

    private ArrayList<Double> calculateAngleHBonds(INucleotide a, INucleotide b) {
        ArrayList<Double> angles = new ArrayList<>();
        //calc for all acceptors in a
        for (AtomRecord ha : a.getHbondAcceptors()) {
            for (AtomRecord hd : b.getHbondDonors()) {
                angles.add(ha.getAngle(hd));
            } }
        //calc for all donors in a
        for (AtomRecord hd : a.getHbondDonors()) {
            for (AtomRecord ha : b.getHbondAcceptors()) {
                angles.add(hd.getAngle(ha));
            } }
        return angles;
    }

    private ArrayList<Double> calculateDistanceHBonds(INucleotide a, INucleotide b) {
        ArrayList<Double> distances = new ArrayList<>();
        //calc for all acceptors in a
        for (AtomRecord ha : a.getHbondAcceptors()) {
            for (AtomRecord hd : b.getHbondDonors()) {
                distances.add(ha.getAngle(hd));
            } }
        //calc for all donors in a
        for (AtomRecord hd : a.getHbondDonors()) {
            for (AtomRecord ha : b.getHbondAcceptors()) {
                distances.add(hd.getAngle(ha));
            } }

        return distances;

    }

}
