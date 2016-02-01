package Model.BondInferenceAnd2D;

import java.util.ArrayList;

/**
 * Created by Philipp on 2016-02-01.
 * holds ArrayLists with HBond Acceptor And HBond Donors for all Nucleotides
 */
public class NucleotideDefaultValues {

    //create final ArrayLists
    public static final ArrayList<String> acceptorKeysA = new ArrayList<String>(){{add("N1");}};
    public static final ArrayList<String> acceptorKeysG = new ArrayList<String>(){{add("O6");}};
    public static final ArrayList<String> acceptorKeysC = new ArrayList<String>(){{add("O2");}{add("N3");}};
    public static final ArrayList<String> acceptorKeysU = new ArrayList<String>(){{add("O4");}};

    public static final ArrayList<String> donatorKeysA = new ArrayList<String>(){{add("N6");}};
    public static final ArrayList<String> donatorKeysG = new ArrayList<String>(){{add("N1");}{add("N2");}};
    public static final ArrayList<String> donatorKeysC = new ArrayList<String>(){{add("N4");}};
    public static final ArrayList<String> donatorKeysU = new ArrayList<String>(){{add("N3");}};

}
