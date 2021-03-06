package Model.BondInferenceAnd2D;


import Model.AtomRecord;
import Model.Nucleotides.INucleotide;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Philipp on 2016-01-31.
 * Infers HBonds according to distance and angle
 */
public class HBondInferer {


    double maxDistance;
    double minDistance;
    double minAngle;

    public HBondInferer(ArrayList<INucleotide> nucleotideList){
        //pessimistic distance and angles, probably structures wont be perfect
        this.maxDistance = 3.5;
        this.minDistance = 1.0;
        this.minAngle = 130.0;
        inferPairs(nucleotideList);
    }

    /**
     * inefficient implementation, if too slow implement more efficient one
     * @param lis
     */
    public void inferPairs(ArrayList<INucleotide> lis){
        for (int i = 0; i < lis.size(); i++) {
            INucleotide n = lis.get(i);
            //possible partner list, from these the best is selected if applicable
            ArrayList<INucleotide> possiblePartnerList = new ArrayList<>();
            for (int j = 0; j < lis.size(); j++) {
                INucleotide m = lis.get(j);
                //has to be in every Nucleotide, get rough estimate
                double approxDistance = n.getResidueMap().get("C2").getDistanceTo(m.getResidueMap().get("C2"));
                if ((approxDistance < 10.0) &&
                        n.hasHBondDonorsAndAcceptors() &&
                        m.hasHBondDonorsAndAcceptors() &&
                        n.isWCPair(m)){
                    //calc more accurate distance and look for Hbonds
                    //print angle and distance
                    HBondCalculationHandler hBondCalculationHandler = new HBondCalculationHandler(n,m);
                    ArrayList<Double> distances = hBondCalculationHandler.getDistances();
                    ArrayList<Double> angles = hBondCalculationHandler.getAngles();
//                    System.out.println(n.getName() + "angles" +  angles + "distances" + distances);
                    if (validateDistances(distances) && validateAngles(angles)){
                        n.setPair(m);
                        m.setPair(n);
//                        System.out.println("Pair found!: " + n.getName() + m.getName() );
                    }
                }
            }
        }
    }

    private boolean validateDistances(ArrayList<Double> distances){
        boolean rv = true;
        for (Double d: distances) {
            rv &= (d>minDistance && d<maxDistance);
        }
        return rv;
    }

    private boolean validateAngles(ArrayList<Double> angles){
        boolean rv = true;
        for (Double a: angles) {
            rv &= (a>minAngle);
        }
        return rv;
    }

    /**
     * Should make it easier to calculate and not lose track of all the variables
     */
    public class HBondCalculationHandler{
        private INucleotide a;
        private INucleotide b;
        private HashMap<String, AtomRecord> residueMapA;
        private HashMap<String, AtomRecord> residueMapB;
        private ArrayList<Double> distances;
        private ArrayList<Double> angles;
        private ArrayList<String[]> aDonorsKeyPair;
        private ArrayList<String[]> bDonorsKeyPair;

        public HBondCalculationHandler(INucleotide a, INucleotide b){
            this.a = a;
            this.b = b;
            this.distances = new ArrayList<>();
            this.angles= new ArrayList<>();
            this.residueMapA = a.getResidueMap();
            this.residueMapB = b.getResidueMap();
            this.aDonorsKeyPair = a.getKeyPairsToCheckForHBonds();
            this.bDonorsKeyPair = b.getKeyPairsToCheckForHBonds();
            calculate();
        }


        private void calculate() {
            //for Donors in A and B
            doCalculation(aDonorsKeyPair, a, residueMapA, residueMapB);
            doCalculation(bDonorsKeyPair, b, residueMapB, residueMapA);
            }

        private void doCalculation(ArrayList<String[]> pairList, INucleotide donorOwner, HashMap<String, AtomRecord> residueMap0, HashMap<String, AtomRecord> residueMap1){
            for (String[] pair: pairList) {
                String donorKey = pair[0];
                String acceptorKey = pair[1];
                AtomRecord donor = residueMap0.get(donorKey);
                AtomRecord currentHAtom = donorOwner.getHFromDonor(donorKey);
                AtomRecord acceptor = residueMap1.get(acceptorKey);
                double distance = currentHAtom.getDistanceTo(acceptor);
                double angle = donor.getAngle(acceptor,currentHAtom);
                distances.add(distance);
                angles.add(angle);
            }
        }

        public ArrayList<Double> getDistances() {
            return distances;
        }

        public ArrayList<Double> getAngles() {
            return angles;
        }
    }

}
