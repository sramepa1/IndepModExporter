/*
 * @(#)DifferentViewsDemo.java 8/22/2009
 *
 * 2002 - 2010 JIDE Software Inc. All rights reserved.
 * Copyright 2005 - 2010 Catalysoft Ltd. All rights reserved.
 */

import com.jidesoft.chart.Chart;
import com.jidesoft.chart.PointShape;
import com.jidesoft.chart.annotation.AutoPositionedLabel;
import com.jidesoft.chart.axis.Axis;
import com.jidesoft.chart.axis.AxisPlacement;
import com.jidesoft.chart.model.ChartModel;
import com.jidesoft.chart.model.ChartPoint;
import com.jidesoft.chart.model.DefaultChartModel;
import com.jidesoft.chart.style.ChartStyle;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.range.NumericRange;
import com.jidesoft.range.Range;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;


@SuppressWarnings("serial")
public class DifferentViewsDemo extends AbstractDemo {
    private static final int b = 20;
    private JPanel demoPanel;
    private ChartModel model;
    private Chart chart1, chart2, chart3, chart4;

    public DifferentViewsDemo() {

    }

    private JPanel createDemo() {
        demoPanel = new JPanel();
        model = createModel();
        demoPanel.setLayout(new GridLayout(2, 2));
        demoPanel.setPreferredSize(new Dimension(500, 400));
        chart1 = new SampleChart("Line", Color.blue, model);
        chart2 = new SampleChart("Points", Color.yellow, model);
        chart3 = new SampleChart("Lines and Points", Color.black, model);
        chart4 = new SampleChart("Bar Chart", Color.white, model);
        demoPanel.add(chart1);
        demoPanel.add(chart2);
        demoPanel.add(chart3);
        demoPanel.add(chart4);

        chart1.setBorder(new EmptyBorder(b, b, b, b));
        ChartStyle style1 = new ChartStyle(Color.blue);
        style1.setLineWidth(2);
        chart1.addModel(model, style1);
        chart1.setChartBackground(new Color(200, 200, 255));
        chart1.setPanelBackground(Color.gray);
        ChartStyle style2 = new ChartStyle(new Color(255, 0, 0, 128), PointShape.UPRIGHT_CROSS);
        style2.setPointSize(7);
        chart2.addModel(model, style2);
        chart2.setPanelBackground(new GradientPaint(0.0f, 0.0f, Color.cyan, 100.0f, 100.0f, Color.blue, true));
        chart2.setBorder(new EmptyBorder(b, b, b, b));
        ChartStyle style3 = new ChartStyle(Color.pink, PointShape.BOX, Color.green);
        chart3.addModel(model, style3);
        chart3.setBorder(new EmptyBorder(b, b, b, b));
        chart3.setPanelBackground(new GradientPaint(0.0f, 0.0f, Color.white, 100.0f, 100.0f, Color.green, true));
        chart3.setChartBackground(Color.black); //new Color(238, 221, 130));
        chart3.setForeground(Color.white);
        chart3.setAxisColor(Color.white);
        chart3.getXAxis().getLabel().setColor(Color.white);
        chart3.getYAxis().getLabel().setColor(Color.white);
        chart3.setTickColor(Color.white);
        chart3.setLabelColor(Color.white);
        ChartStyle style4 = new ChartStyle(Color.orange);
        style4.setLinesVisible(false);
        style4.setBarsVisible(true);
        style4.setBarColor(new Color(128, 128, 128, 128));
        chart4.addModel(model, style4);
        chart4.setBorder(new EmptyBorder(b, b, b, b));
        chart4.setChartBackground(new GradientPaint(0.0f, 0.0f, Color.yellow, 200.0f, 200.0f, Color.orange, true));
        chart4.setPanelBackground(Color.magenta);
        return demoPanel;
    }

    /**
     * An custom extension of JChart that sets up axes and title
     */
    class SampleChart extends Chart {
        public SampleChart(String title, Color titleColor, ChartModel model) {
            setName(title);
            NumericRange yRange = new NumericRange(-1, 1);
            Range<?> xRange = new NumericRange(-180, 180);
            final Axis xAxis = new Axis(xRange);
            final Axis yAxis = new Axis(yRange);
            xAxis.setLabel(new AutoPositionedLabel("Angle (degrees)"));
            xAxis.setPlacement(AxisPlacement.CENTER);
            yAxis.setLabel(new AutoPositionedLabel("y = sin(x)"));
            yAxis.setPlacement(AxisPlacement.CENTER);

            setXAxis(xAxis);
            setYAxis(yAxis);
            setShadowVisible(false);
            setTitle(new AutoPositionedLabel(title, titleColor));
            //chart.setPanelBackground(Color.blue);
            setChartBackground(new GradientPaint(0, 0, Color.white, 800, 800, new Color(150, 150, 230)));
        }
    }

    private ChartModel createModel() {
        DefaultChartModel model = new DefaultChartModel();
        int numIntervals = 30;
        for (int i = 0; i <= numIntervals; i++) {
            double x = -180 + (360.0 * i) / numIntervals;
            double y = Math.sin(x * 2 * Math.PI / 360);
            ChartPoint p = new ChartPoint(x, y);
            model.addPoint(p);
        }
        return model;
    }

    public String getName() {
        return "Same Model. Different Views";
    }

    @Override
    public String getDemoFolder() {
        return "R1.Charts";
    }

    @Override
    public String getDescription() {
        return "Here are some different views of the same data.";
    }

    public static void centralize(Window w) {
        final double xProportion = 0.5;
        final double yProportion = 0.5;
        Dimension winSize = w.getSize();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) (xProportion * (screenSize.width - winSize.width));
        int y = (int) (yProportion * (screenSize.height - winSize.height));
        w.setLocation(x, y);
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
        showAsFrame(new DifferentViewsDemo());
    }

}
