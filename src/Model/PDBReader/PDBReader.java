package Model.PDBReader;

import Model.AtomRecord;
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
    }

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
