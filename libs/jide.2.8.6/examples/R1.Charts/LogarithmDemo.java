/*
 * @(#)LogarithmDemo.java 8/22/2009
 *
 * 2002 - 2010 JIDE Software Inc. All rights reserved.
 * Copyright 2005 - 2010 Catalysoft Ltd. All rights reserved.
 */

import com.jidesoft.chart.Chart;
import com.jidesoft.chart.Legend;
import com.jidesoft.chart.PointShape;
import com.jidesoft.chart.axis.Axis;
import com.jidesoft.chart.model.DefaultChartModel;
import com.jidesoft.chart.model.InvertibleTransform;
import com.jidesoft.chart.render.SphericalPointRenderer;
import com.jidesoft.chart.style.ChartStyle;
import com.jidesoft.plaf.LookAndFeelFactory;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

@SuppressWarnings("serial")
public class LogarithmDemo extends AbstractDemo {
    private static final Color red = new Color(192, 50, 50);
    private static final Color green = new Color(50, 192, 50);
    private static final Color blue = new Color(50, 50, 192);
    private JPanel demoPanel;
    private JPanel legendPanel;
    private DefaultChartModel exponentialModel, linearModel, logarithmicModel;
    private Axis xAxis, yAxis;
    private final ButtonGroup xAxisButtonGroup = new ButtonGroup();
    private final ButtonGroup yAxisButtonGroup = new ButtonGroup();
    private Legend legend;

    public LogarithmDemo() {

    }

    private JPanel createDemo() {
        demoPanel = new JPanel();
        demoPanel.setLayout(new BorderLayout());
        demoPanel.setPreferredSize(new Dimension(500, 500));
        final Chart chart = new Chart();
        xAxis = new Axis(0.5, 10.5);
        yAxis = new Axis(0.5, 10.5);
        chart.setXAxis(xAxis);
        chart.setYAxis(yAxis);
        demoPanel.add(chart, BorderLayout.CENTER);
        legendPanel = new JPanel();
        demoPanel.add(legendPanel, BorderLayout.NORTH);

        exponentialModel = new DefaultChartModel("<html>y=e<sup>x</sup></html>");
        linearModel = new DefaultChartModel("y=x");
        logarithmicModel = new DefaultChartModel("y=ln(x)");

        JPanel panel = new JPanel();
        demoPanel.add(panel, BorderLayout.SOUTH);
        createModels();
        chart.setPointRenderer(new SphericalPointRenderer());
        chart.addModel(exponentialModel, createStyle(red));
        chart.addModel(linearModel, createStyle(green));
        chart.addModel(logarithmicModel, createStyle(blue));

        panel.setLayout(new GridLayout(1, 2, 5, 5));

        legend = new Legend(chart, 3);
        legendPanel.add(legend);

        JPanel xAxisPanel = new JPanel();
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[]{96, 29, 0};
        gridBagLayout.rowHeights = new int[]{14, 0};
        gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0};
        gridBagLayout.rowWeights = new double[]{0.0, 0.0};
        xAxisPanel.setLayout(gridBagLayout);
        panel.add(xAxisPanel);

        JLabel lblXAxis = new JLabel("X Axis", JLabel.RIGHT);
        GridBagConstraints gbc1 = new GridBagConstraints();
        gbc1.anchor = GridBagConstraints.EAST;
        gbc1.gridheight = 2;
        gbc1.weighty = 1.0;
        gbc1.weightx = 1.0;
        gbc1.fill = GridBagConstraints.VERTICAL;
        gbc1.insets = new Insets(0, 0, 0, 5);
        gbc1.gridx = 0;
        gbc1.gridy = 0;
        xAxisPanel.add(lblXAxis, gbc1);

        JRadioButton xLogarithmicButton = new JRadioButton("Logarithmic");
        xAxisButtonGroup.add(xLogarithmicButton);
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.fill = GridBagConstraints.HORIZONTAL;
        gbc2.weightx = 1.0;
        gbc2.gridx = 1;
        gbc2.gridy = 1;
        xAxisPanel.add(xLogarithmicButton, gbc2);

        final JRadioButton xLinearButton = new JRadioButton("Linear");
        xLinearButton.setSelected(true);
        xAxisButtonGroup.add(xLinearButton);
        GridBagConstraints gbc3 = new GridBagConstraints();
        gbc3.weightx = 1.0;
        gbc3.fill = GridBagConstraints.HORIZONTAL;
        gbc3.gridx = 1;
        gbc3.gridy = 0;
        xAxisPanel.add(xLinearButton, gbc3);


        JPanel yAxisPanel = new JPanel();
        GridBagLayout gridBagLayout2 = new GridBagLayout();
        gridBagLayout2.columnWidths = new int[]{29, 0, 0};
        gridBagLayout2.rowHeights = new int[]{14, 0, 0};
        gridBagLayout2.columnWeights = new double[]{0.0, 0.0, 1.0E-4};
        gridBagLayout2.rowWeights = new double[]{0.0, 0.0, 1.0E-4};
        yAxisPanel.setLayout(gridBagLayout2);
        panel.add(yAxisPanel);

        JLabel lblYAxis = new JLabel("Y Axis", JLabel.RIGHT);
        GridBagConstraints gbc4 = new GridBagConstraints();
        gbc4.anchor = GridBagConstraints.EAST;
        gbc4.gridheight = 2;
        gbc4.fill = GridBagConstraints.BOTH;
        gbc4.weighty = 1.0;
        gbc4.weightx = 1.0;
        gbc4.insets = new Insets(0, 0, 5, 5);
        gbc4.gridx = 0;
        gbc4.gridy = 0;
        yAxisPanel.add(lblYAxis, gbc4);


        final JRadioButton yLinearButton = new JRadioButton("Linear");
        yLinearButton.setSelected(true);
        yAxisButtonGroup.add(yLinearButton);
        GridBagConstraints gbc5 = new GridBagConstraints();
        gbc5.fill = GridBagConstraints.HORIZONTAL;
        gbc5.weightx = 1.0;
        gbc5.insets = new Insets(0, 0, 5, 0);
        gbc5.gridx = 1;
        gbc5.gridy = 0;
        yAxisPanel.add(yLinearButton, gbc5);


        JRadioButton yLogarithmicButton = new JRadioButton("Logarithmic");
        yAxisButtonGroup.add(yLogarithmicButton);
        GridBagConstraints gbc6 = new GridBagConstraints();
        gbc6.weightx = 1.0;
        gbc6.fill = GridBagConstraints.HORIZONTAL;
        gbc6.gridx = 1;
        gbc6.gridy = 1;
        yAxisPanel.add(yLogarithmicButton, gbc6);


        ChangeListener listener = new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                if (yLinearButton.isSelected()) {
                    yAxis.setAxisTransform(null);
                }
                else {
                    yAxis.setAxisTransform(new LogTransform());
                }
                if (xLinearButton.isSelected()) {
                    xAxis.setAxisTransform(null);
                }
                else {
                    xAxis.setAxisTransform(new LogTransform());
                }
                chart.update();
            }
        };

        xLinearButton.addChangeListener(listener);
        xLogarithmicButton.addChangeListener(listener);
        yLinearButton.addChangeListener(listener);
        yLogarithmicButton.addChangeListener(listener);

        return demoPanel;
    }

    private ChartStyle createStyle(Color color) {
        ChartStyle style = new ChartStyle(color, PointShape.DISC, 8);
        style.setLinesVisible(true);
        style.setLineColor(color);
        style.setLineWidth(2);
        return style;
    }

    private void createModels() {
        double step = 0.25;
        for (double i = xAxis.minimum(); i < xAxis.maximum() + step; i += step) {
            exponentialModel.addPoint(i, Math.pow(Math.E, i));
            linearModel.addPoint(i, i);
            double log = Math.log(i);
            if (!Double.isInfinite(log) && log > 0) {
                logarithmicModel.addPoint(i, log);
            }
        }
    }

    @Override
    public String getDescription() {
        return "This demo shows that we can easily configure none, one or both axes to be logarithmic. " +
                "The red trace is an exponential curve that is displayed as a straight line when the y axis is logarithmic and the x axis is linear." +
                " The green trace is the function y = x so is displayed as a straight line in a linear-linear or log-log chart. The blue trace is a " +
                " logarithmic function so is displayed as a straight line when the x axis is logarithmic and the y axis is linear.";
    }

    public String getName() {
        return "Logarithmic Axes";
    }

    @Override
    public String getDemoFolder() {
        return "R1.Charts";
    }

    class LogTransform implements InvertibleTransform<Double> {

        public Double transform(Double pos) {
            return Math.log10(pos);
        }

        public Double inverseTransform(Double t) {
            return Math.pow(10, t);
        }

    }

    public Component getDemoPanel() {
        if (demoPanel == null) {
            demoPanel = createDemo();
        }
        return demoPanel;
    }

    public String getProduct() {
        return PRODUCT_NAME_CHARTS;
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new LogarithmDemo());
    }

}
