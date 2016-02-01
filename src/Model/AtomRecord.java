package Model;

import javafx.geometry.Point3D;
import javafx.scene.transform.Translate;

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
        this.atom = lis[0];
        this.id = Integer.valueOf(lis[1]);
        this.name = lis[2];
        this.residium = lis[3].substring(0,1);
        this.chain =  lis[4];
        this.indexOfResidium = Integer.valueOf(lis[5]);
        this.point3D = new Point3D(Double.valueOf(lis[6]),Double.valueOf(lis[7]),Double.valueOf(lis[8]));
        this.betaColumn = lis[9];
        this.atomType = lis[lis.length-1];
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
    public String getAtom() {
        return atom;
    }

    public void setAtom(String atom) {
        this.atom = atom;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResidium() {
        return residium;
    }

    public void setResidium(String residium) {
        this.residium = residium;
    }

    public String getChain() {
        return chain;
    }

    public void setChain(String chain) {
        this.chain = chain;
    }

    public int getIndexOfResidium() {
        return indexOfResidium;
    }

    public void setIndexOfResidium(int indexOfResidium) {
        this.indexOfResidium = indexOfResidium;
    }

    public Point3D getPoint3D() {
        return point3D;
    }

    public void setPoint3D(Point3D point3D) {
        this.point3D = point3D;
    }

    public String getBetaColumn() {
        return betaColumn;
    }

    public void setBetaColumn(String betaColumn) {
        this.betaColumn = betaColumn;
    }

    public String getAtomType() {
        return atomType;
    }

    public void setAtomType(String atomType) {
        this.atomType = atomType;
    }
}
