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
 */
public class Circleand2DBuilder {

    public static ArrayList<Node> generateCovalentEdges(double[][] coords, Graph graph) {
        ArrayList<Node> cLines = new ArrayList<>();

        //int[][] edges = this.getGraph().getEdges();
        int numberOfNodes = graph.getNumberOfNodes();
        for (int i = 0; i < numberOfNodes -1 ; i++) {
            Line l = new Line(coords[i][0],coords[i][1],coords[i+1][0],coords[i+1][1]);
            cLines.add(l);
        }
        return cLines;
    }

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
    /*
    public static ArrayList<Node> generateNodes(double[][] coords, String sequence){
        ArrayList<Node> nodes = new ArrayList<>();
        for (int i = 0; i < coords.length ; i++) {
            double x = coords[i][0];
            double y = coords[i][1];
            //instead use nucleotideList
            String s = String.valueOf(sequence.charAt(i)).toUpperCase();
            Circle cSurround =  new Circle(x,y,9,Color.BLACK);
            Circle cGround =    new Circle(x,y,8,getColorByText(s));
            Circle cTop =       new Circle(x,y,6,Color.WHITE);
            Text t =            new Text(x-4,y+5,s);
            Tooltip tooltip = new Tooltip("Nucleotide: " + s + "\n Position: " + (i+1));
            Tooltip.install(cTop,tooltip);
            Tooltip.install(cGround,tooltip);
            Tooltip.install(t,tooltip);

            Group nucleotideGroup = new Group(cSurround,cGround,cTop,t);
            nodes.add(nucleotideGroup);
        }
        return nodes;
    }*/

}
