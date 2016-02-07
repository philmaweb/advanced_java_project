package Model.PDBReader;

/**
 * Created by Philipp on 2016-02-03.
 * numbers to parse a PDB file
 */
public class PDBMagicNumberDefaults {
    // see http://www.wwpdb.org/documentation/file-format-content/format33/sect9.html#ATOM
    // for reference
        /*
        Record Format
        COLUMNS        DATA  TYPE    FIELD        DEFINITION
        -------------------------------------------------------------------------------------
         1 -  6        Record name   "ATOM  "
         7 - 11        Integer       serial       Atom  serial number.
        13 - 16        Atom          name         Atom name.
        17             Character     altLoc       Alternate location indicator.
        18 - 20        Residue name  resName      Residue name.
        22             Character     chainID      Chain identifier.
        23 - 26        Integer       resSeq       Residue sequence number.
        27             AChar         iCode        Code for insertion of residues.
        31 - 38        Real(8.3)     x            Orthogonal coordinates for X in Angstroms.
        39 - 46        Real(8.3)     y            Orthogonal coordinates for Y in Angstroms.
        47 - 54        Real(8.3)     z            Orthogonal coordinates for Z in Angstroms.
        55 - 60        Real(6.2)     occupancy    Occupancy.
        61 - 66        Real(6.2)     tempFactor   Temperature  factor.
        77 - 78        LString(2)    element      Element symbol, right-justified.
        79 - 80        LString(2)    charge       Charge  on the atom.

        example:
        ATOM      1  P   C   A  13     -11.658 -10.243   4.892  1.00  1.68           P
         */
    public static final int idStart = 6;
    public static final int idEnd = 11;
    public static final int nameStart = 12;
    public static final int nameEnd = 16;
    public static final int residiumStart = 17;
    public static final int residiumEnd = 20;
    public static final int chainStart = 21;
    public static final int chainEnd = 22;
    public static final int indexOfRisidiumStart = 23;
    public static final int indexOfRisidiumEnd = 26;
    public static final int xStart = 30;
    public static final int xEnd = 38;
    public static final int yStart = 38;
    public static final int yEnd = 46;
    public static final int zStart = 46;
    public static final int zEnd = 54;
    public static final int atomTypeStart = 76;
    public static final int atomTypeEnd = 78;
}
