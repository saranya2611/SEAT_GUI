package gui;

import javax.swing.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.RefineryUtilities;

//// Supporting data class for Chart

public class Slice {
    double value;
    String name;

    public Slice(double value, String name) {
        this.value = value;
        this.name = name;
    }
}