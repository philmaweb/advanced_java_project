package GUI;

import Model.BondInferenceAnd2D.Graph;
import Model.Nucleotides.INucleotide;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

import java.util.ArrayList;

/**
 * Created by Philipp on 2016-02-03.
 * Creates 2d Objects
 */
public class Circleand2DBuilder {

    /**
     * non-covalent edge, represented as black line
     * @param coords
     * @param graph
     * @return
     */
    public static ArrayList<Node> generateCovalentEdges(double[][] coords, Graph graph) {
        ArrayList<Node> cLines = new ArrayList<>();
        int numberOfNodes = graph.getNumberOfNodes();
        for (int i = 0; i < numberOfNodes -1 ; i++) {
            //x,y,x2,y2
            Line l = new Line(coords[i][0],coords[i][1],coords[i+1][0],coords[i+1][1]);
            cLines.add(l);
        }
        return cLines;
    }

    /**
     * non-covalent edge, represented as line of color blueViolet
     * @param coords
     * @param graph
     * @return
     */
    public static ArrayList<Node> generateNonCovalentEdges(double[][] coords, Graph graph){
        ArrayList<Node> nodes = new ArrayList<>();
        int[][] edges = graph.getEdges();
        int numberOfNodes = graph.getNumberOfNodes();
        for (int i = numberOfNodes-1; i < edges.length ; i++) {
            //index of point in coords
            int e1 = edges[i][0];
            int e2 = edges[i][1];
            //get coords with index
            double[] p1 = new double[]{coords[e1][0],coords[e1][1]};
            double[] p2 = new double[]{coords[e2][0],coords[e2][1]};

            Line l = new Line(p1[0],p1[1],p2[0],p2[1]);
            l.setStroke(Color.BLUEVIOLET);
            nodes.add(l);
        }
        return nodes;
    }

    /**
     * generates end Position for nodes
     * @param nucleotide
     * @return
     */
    public static Group generateNodeRepresentation(INucleotide nucleotide){
        double x = nucleotide.getPosition2DEnd().getX();
        double y = nucleotide.getPosition2DEnd().getY();
        String s = nucleotide.getRisidue().toUpperCase();
        Circle cSurround =  new Circle(x,y,9,Color.BLACK);
        Circle cGround =    new Circle(x,y,8,nucleotide.get2DColorByClass());
        Circle cTop =       new Circle(x,y,6,Color.WHITE);
        Text t =            new Text(x-4,y+5,s);
        Tooltip tooltip = new Tooltip(nucleotide.getName());
        Tooltip.install(cTop,tooltip);
        Tooltip.install(cGround,tooltip);
        Tooltip.install(t,tooltip);
        return new Group(cSurround,cGround,cTop,t);
    }

}
