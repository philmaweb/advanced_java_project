package GUI;

import Model.Nucleotides.INucleotide;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * Created by Philipp on 2016-02-07.
 * Pie Chart to show on click
 */
public class FrequencyPieChart {

    private ObservableList<PieChart.Data> data;
    private PieChart pieChart;
    private Stage chartStage;

    public FrequencyPieChart(ArrayList<INucleotide> nucleotides) {
        initChart(nucleotides);
        setStage();
    }


    private void initChart(ArrayList<INucleotide> nucleotides) {
        int countA = 0;
        int countG = 0;
        int countC = 0;
        int countU = 0;
        for (INucleotide n : nucleotides) {
            switch (n.getNucleotideClass()) {
                case ADENINE:
                    countA++;
                    break;
                case URACIL:
                    countU++;
                    break;
                case GUANINE:
                    countG++;
                    break;
                case CYTOSIN:
                    countC++;
                    break;
            }
        }
        data = FXCollections.observableArrayList(
                new PieChart.Data("A: " + countA,countA),
                new PieChart.Data("G: " + countG,countG),
                new PieChart.Data("C: " + countC,countC),
                new PieChart.Data("U: " + countU,countU));
        this.pieChart = new PieChart(data);
        this.pieChart.setTitle("AGCU content of sequence");

    }


    private void setStage() {
        Scene pieChartScene = new Scene(pieChart,400,400);
        chartStage = new Stage();
        chartStage.setScene(pieChartScene);
    }

    public Stage getChartStage() {
        return chartStage;
    }
}
