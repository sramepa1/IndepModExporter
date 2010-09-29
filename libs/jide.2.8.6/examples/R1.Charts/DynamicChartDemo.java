/*
 * @(#)DynamicChartDemo.java 8/22/2009
 *
 * 2002 - 2010 JIDE Software Inc. All rights reserved.
 * Copyright 2005 - 2010 Catalysoft Ltd. All rights reserved.
 */

import com.jidesoft.chart.Chart;
import com.jidesoft.chart.annotation.AutoPositionedLabel;
import com.jidesoft.chart.axis.Axis;
import com.jidesoft.chart.axis.TimeAxis;
import com.jidesoft.chart.model.ChartPoint;
import com.jidesoft.chart.model.DefaultChartModel;
import com.jidesoft.chart.style.ChartStyle;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.range.NumericRange;
import com.jidesoft.range.Range;
import com.jidesoft.range.TimeRange;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DynamicChartDemo extends AbstractDemo {
    private static final long serialVersionUID = 3477660378504625992L;
    private JPanel demoPanel;
    private Chart chart;
    private DefaultChartModel chartModel;
    private int timePoint = 0;
    private ComponentListener resizeListener;
    private JPanel controlPanel;
    private ButtonGroup buttonGroup = new ButtonGroup();

    public DynamicChartDemo() {

    }

    private JPanel createDemo() {
        chart = new Chart();
        chartModel = new DefaultChartModel();
        demoPanel = new JPanel();
        demoPanel.setPreferredSize(new Dimension(500, 500));
        NumericRange yRange = new NumericRange(0, 5000);
        long now = System.currentTimeMillis();
        TimeRange xRange = new TimeRange(now, now);
        final Axis xAxis = new TimeAxis(xRange);
        final Axis yAxis = new Axis(yRange);
        xAxis.setLabel(new AutoPositionedLabel("Time"));
        yAxis.setLabel(new AutoPositionedLabel("Free Memory"));

        chart.addModel(chartModel);
        //chart.setStyle(model1, new ChartStyle(Color.red, false, true));
        chart.setXAxis(xAxis);
        chart.setYAxis(yAxis);
        chart.setChartBackground(new GradientPaint(0.0f, 0.0f, Color.gray, 0.0f, 200.0f, Color.black, false));
        chart.setTickColor(Color.white);
        chart.setTitle(new AutoPositionedLabel("Dynamic Chart", Color.yellow.brighter()));

        controlPanel = new JPanel();
        final JRadioButton lineRadioButton = new JRadioButton("Line");
        final JRadioButton barsRadioButton = new JRadioButton("Bars");
        controlPanel.add(barsRadioButton);
        controlPanel.add(lineRadioButton);
        buttonGroup.add(lineRadioButton);
        buttonGroup.add(barsRadioButton);
        lineRadioButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (lineRadioButton.isSelected()) {
                    useLineStyle();
                }
            }
        });
        barsRadioButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (barsRadioButton.isSelected()) {
                    useBarStyle();
                }
            }
        });
        useBarStyle();
        barsRadioButton.setSelected(true);

        demoPanel.setLayout(new BorderLayout());
        demoPanel.add(chart, BorderLayout.CENTER);
        demoPanel.add(controlPanel, BorderLayout.SOUTH);

        // The action listener to be used by a swing timer
        ActionListener addPointListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addPointToModel(chartModel);
                if (timePoint > 120) {
                    chartModel.removePoint(0);
                }
                //chart.repaint();
                Range<Double> timeRange = chartModel.getXRange();
                if (timeRange != null) {
                    long maxX = (long) (timeRange.minimum() + 60 * 1000.0); // A minute's worth of data
                    TimeRange xRange = (TimeRange) xAxis.getRange();
                    xRange.setMin((long) timeRange.minimum());
                    xRange.setMax(maxX);
                    NumericRange modelYRange = (NumericRange) chartModel.getYRange();
                    double range = modelYRange.maximum() - modelYRange.minimum();
                    final double extraSpace = 0.15; // = 15%
                    //modelYRange.setMin(0);
                    //modelYRange.setMax(modelYRange.getMax() + extraSpace * range);
                    NumericRange yRange = (NumericRange) yAxis.getRange();
                    yRange.setMin(0);
                    yRange.setMax(modelYRange.getMax() + extraSpace * range);
                    //yAxis.setRange(modelYRange, true);
                }
            }
        };
        Timer timer = new Timer(500, addPointListener);
        timer.setInitialDelay(500);
        timer.start();
        return demoPanel;
    }

    private void useBarStyle() {
        if (resizeListener != null) {
            demoPanel.removeComponentListener(resizeListener);
        }
        ChartStyle style = new ChartStyle(Color.green, false, false);
        style.setBarsVisible(true);
        style.setBarColor(Color.green);
        style.setBarWidth(2);
        style.setLineStroke(new BasicStroke(3));
        chart.setStyle(chartModel, style);
    }

    private void useLineStyle() {
        final ChartStyle style = new ChartStyle(Color.green);
        style.setLineWidth(2);
        if (resizeListener != null) {
            demoPanel.removeComponentListener(resizeListener);
        }
        resizeListener = new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizeGradientFill(style);
            }
        };
        resizeGradientFill(style);
        chart.setStyle(chartModel, style);
        demoPanel.addComponentListener(resizeListener);
    }

    private void resizeGradientFill(ChartStyle style) {
        Dimension size = demoPanel.getSize();
        style.setLineFill(new GradientPaint(0f, 0f, new Color(0, 255, 128, 180), 0, (float) size.getHeight(), new Color(0, 0, 0, 64)));
    }

    private void addPointToModel(DefaultChartModel model) {
        assert SwingUtilities.isEventDispatchThread();
        long now = System.currentTimeMillis();
        double y = Runtime.getRuntime().freeMemory() / 1000000.0;
        ChartPoint p = new ChartPoint(now, y);
        model.addPoint(p);
        timePoint++;
    }

    @Override
    public String getDescription() {
        return "This shows the amount of free memory in the JVM. " +
                "The example demonstrates that a chart can be modified and updated as new data becomes available.";
    }

    public String getName() {
        return "Dynamic Chart";
    }

    @Override
    public String getDemoFolder() {
        return "R1.Charts";
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
        showAsFrame(new DynamicChartDemo());
    }
}
