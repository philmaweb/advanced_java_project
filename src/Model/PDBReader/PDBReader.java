package Model.PDBReader;

import Model.AtomRecord;
import javafx.geometry.Point3D;
import jdk.nashorn.internal.objects.NativeRegExp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Philipp on 2015-12-14.
 * reads in a specified pdb file
 */
public class PDBReader {

    private String pdbHeader = "";
    private ArrayList<AtomRecord> records;
    private ArrayList<ArrayList<AtomRecord>> chains;


    public void read(BufferedReader reader) throws IOException{
        String line;// = null;
        records = new ArrayList<AtomRecord>();

        System.out.println("Trying to read in file");

        while ((line = reader.readLine().trim()) != null){
            if (line.startsWith("ATOM")){
                AtomRecord aR = new AtomRecord(line);
                records.add(aR);
                //System.out.println(aR.getName()+ "x " + aR.getPoint3D().getX() + " y " + aR.getPoint3D().getY() + " z " + aR.getPoint3D().getZ());
            }
            else {
                if (line.startsWith("END")){
                    System.out.println("Finished parsing");
                    break;
                }
                else{
                    pdbHeader += line + "\n";
                    //System.out.println("Cant parse line: " + line);
                }
            }
        }
        reader.close();
        groupIntoChains();
        centerAtoms();
    }

    private void centerAtoms() {
        ArrayList<Point3D> toCenter = new ArrayList<>();
        for (ArrayList<AtomRecord> chain : chains){
            for (AtomRecord atomRecord : chain){
                toCenter.add(atomRecord.getPoint3D());
            }
        }

        ArrayList<Point3D> centered = center(toCenter);

        int i = 0;
        for (ArrayList<AtomRecord> chain : chains){
            for (AtomRecord atomRecord : chain){
                atomRecord.setPoint3D(centered.get(i));
                i++;
            }
        }
    }

    /**
     * center around 0 0 0
     * @param points
     */
    public static ArrayList<Point3D> center(ArrayList<Point3D> points) {
        ArrayList<Point3D> result=new ArrayList<>(points.size());
        if (points.size() > 0) {
            double[] center = {0, 0, 0};

            for (Point3D point : points) {
                center[0] += point.getX();
                center[1] += point.getY();
                center[2] += point.getZ();
            }
            center[0] /= points.size();
            center[1] /= points.size();
            center[2] /= points.size();

            for (Point3D point : points) {
                result.add(point.subtract(new Point3D(center[0], center[1], center[2])));
            }
        }
        return result;
    }

    /**
     * extract Atoms records for each residue and put the into a list of lists
     */
    private void groupIntoChains() {
        chains = new ArrayList<ArrayList<AtomRecord>>();

        ArrayList<AtomRecord> currentChain = new ArrayList<AtomRecord>();
        int currentChainIndex = getRecords().get(0).getIndexOfResidium();
        for (AtomRecord record: getRecords()
             ) {
            if(currentChainIndex != record.getIndexOfResidium()){
                chains.add(currentChain);
                currentChainIndex = record.getIndexOfResidium();
                currentChain = new ArrayList<AtomRecord>();
            }
            currentChain.add(record);
        }
        //add last chain
        chains.add(currentChain);
    }

    public void clear(){
        records.clear();
    }

    public ArrayList<AtomRecord> getRecords() {
        return records;
    }

    public ArrayList<ArrayList<AtomRecord>> getChains() {
        return chains;
    }


}
