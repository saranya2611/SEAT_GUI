package gui;

import javax.swing.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.RefineryUtilities;
// Class to generate and display Chart
class PieChart extends JFrame {

    Slice[] slices;
    JFreeChart chart;

    public PieChart(String title, Slice[] slices) {
        super(title);
        this.slices = slices;
        setContentPane(createDemoPanel());
    }

    private static JFreeChart createChart(PieDataset dataset) {
        JFreeChart chart = ChartFactory.createPieChart(
                "",   // chart title
                dataset,          // data
                false,             // include legend
                true,
                false);

        return chart;
    }

    private PieDataset createDataset() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (int i = 0; i < slices.length; i++)
            dataset.setValue(slices[i].name, slices[i].value);
        return dataset;
    }

    public JPanel createDemoPanel() {
        chart = createChart(createDataset());
        return new ChartPanel(chart);
    }
}