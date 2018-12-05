//------------------------------------------------------------
//Ivan Mateus de Lima Azevedo
//10525602
//Gabriel de Andrade Dezan
//10525706
//------------------------------------------------------------

import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.PieDataset;
import org.jfree.util.Rotation;
import java.awt.*;

public class PieChart extends JFrame {
    private static final long serialVersionUID = 1L;

    public PieChart(String applicationTitle, String chartTitle, PieDataset result){
        super(applicationTitle);
        JFreeChart chart = createChart(result, chartTitle);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800,600));
        setContentPane(chartPanel);
    }

    private JFreeChart createChart(PieDataset dataSet, String title){
        JFreeChart chart = ChartFactory.createPieChart3D(title, dataSet, true, true, false);
        PiePlot3D plot = (PiePlot3D) chart.getPlot();
        plot.setStartAngle(290);
        plot.setDirection(Rotation.CLOCKWISE);
        plot.setForegroundAlpha(0.5f);
        return chart;
    }
}
