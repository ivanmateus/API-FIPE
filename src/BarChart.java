//------------------------------------------------------------
//Ivan Mateus de Lima Azevedo
//10525602
//Gabriel de Andrade Dezan
//10525706
//------------------------------------------------------------

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import javax.swing.*;
import java.awt.*;

public class BarChart extends JFrame {

    public BarChart(String applicationTitle, String chartTitle, CategoryDataset dataSet){
        super(applicationTitle);
        JFreeChart barChart = ChartFactory.createBarChart3D(
                chartTitle,
                "Marcas",
                "Quantidade de carros",
                dataSet,
                PlotOrientation.HORIZONTAL,
                false, true, false);
        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new Dimension(800, 600));
        setContentPane(chartPanel);
    }
}
