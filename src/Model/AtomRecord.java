package Model;

import Model.PDBReader.PDBMagicNumberDefaults;
import javafx.geometry.Point3D;

/**
 * Created by Philipp on 2015-12-14.
 * represents one entry into the pdb file
 */
public class AtomRecord {

    private String atom;
    private int id;
    private String name;
    private String residium;
    private String chain;
    private int indexOfResidium;
    private Point3D point3D;
    private String betaColumn;
    private String atomType;

//ATOM      1  P   C   A  13     -11.658 -10.243   4.892  1.00  1.68           P
    public AtomRecord(String line) {

        String[] lis = line.split("\\s+");
        parseWithMagicNumbers(line);
//        System.out.println(this);
    }

    @Override
    public String toString(){
        return "" + id + "\t " + name + "\t " + residium + "\t " + chain + "\t " + indexOfResidium + "\t " + point3D  + "\t " +atomType;
    }

    /**
     * Use PDB magic numbers to parse without relying on space between columns
     */
    private void parseWithMagicNumbers(String line) {
        this.id = Integer.valueOf(line.substring(PDBMagicNumberDefaults.idStart,PDBMagicNumberDefaults.idEnd).trim());
        this.name = line.substring(PDBMagicNumberDefaults.nameStart,PDBMagicNumberDefaults.nameEnd).trim();
        this.residium = line.substring(PDBMagicNumberDefaults.residiumStart,PDBMagicNumberDefaults.residiumEnd).trim().substring(0,1);
        this.chain = line.substring(PDBMagicNumberDefaults.chainStart,PDBMagicNumberDefaults.chainEnd).trim();
        this.indexOfResidium = Integer.valueOf(line.substring(PDBMagicNumberDefaults.indexOfRisidiumStart,PDBMagicNumberDefaults.indexOfRisidiumEnd).trim());
        Double x = Double.valueOf(line.substring(PDBMagicNumberDefaults.xStart,PDBMagicNumberDefaults.xEnd).trim());
        Double y = Double.valueOf(line.substring(PDBMagicNumberDefaults.yStart,PDBMagicNumberDefaults.yEnd).trim());
        Double z = Double.valueOf(line.substring(PDBMagicNumberDefaults.zStart,PDBMagicNumberDefaults.zEnd).trim());
        this.point3D = new Point3D(x,y,z);
        this.atomType = line.substring(PDBMagicNumberDefaults.atomTypeStart,PDBMagicNumberDefaults.atomTypeEnd).trim();
    }

    /**
     * get Distance from other Atom
     * @param target
     * @return
     */
    public double getDistanceTo(AtomRecord target){
        Point3D origin = this.getPoint3D();
        Point3D pointTarget = target.getPoint3D();
        Point3D diff = pointTarget.subtract(origin);
        double height = diff.magnitude();
        return height;
    }

    public double getAngle(AtomRecord target, AtomRecord vertex){
        Point3D pointOrigin = this.getPoint3D();
        Point3D pointTarget = target.getPoint3D();
        Point3D pointVertex = vertex.getPoint3D();
        double angle = pointVertex.angle(pointOrigin,pointTarget);
        return angle;
    }

    //GETTER AND SETTER
    public String getName() {
        return name;
    }

    public String getResidium() {
        return residium;
    }

    public int getIndexOfResidium() {
        return indexOfResidium;
    }

    public Point3D getPoint3D() {
        return point3D;
    }

    public void setPoint3D(Point3D point3D) {
        this.point3D = point3D;
    }

    public String getAtomType() {
        return atomType;
    }

}
