/*
 * @(#)LineChartDemo.java 8/22/2009
 *
 * 2002 - 2010 JIDE Software Inc. All rights reserved.
 * Copyright 2005 - 2010 Catalysoft Ltd. All rights reserved.
 */

import com.jidesoft.chart.Chart;
import com.jidesoft.chart.annotation.AutoPositionedLabel;
import com.jidesoft.chart.axis.Axis;
import com.jidesoft.chart.axis.AxisPlacement;
import com.jidesoft.chart.event.*;
import com.jidesoft.chart.model.ChartModel;
import com.jidesoft.chart.model.ChartPoint;
import com.jidesoft.chart.model.DefaultChartModel;
import com.jidesoft.chart.style.ChartStyle;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.range.NumericRange;
import com.jidesoft.range.Range;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;


@SuppressWarnings("serial")
public class LineChartDemo extends AbstractDemo {
    static final Logger logger = Logger.getLogger(LineChartDemo.class.getName());
    private JPanel demoPanel;
    private Chart chart;
    private ChartModel sineModel, cosineModel;

    public LineChartDemo() {

    }

    private JPanel createDemo() {
        chart = new Chart();
        sineModel = createModel(true);
        cosineModel = createModel(false);
        demoPanel = new JPanel(new BorderLayout());
        demoPanel.setPreferredSize(new Dimension(500, 500));

        NumericRange yRange = new NumericRange(-1.2, 1.2);
        Range<?> xRange = new NumericRange(-180, 180);
        final Axis xAxis = new Axis(xRange);
        final Axis yAxis = new Axis(yRange);
        xAxis.setLabel(new AutoPositionedLabel("Angle (degrees)"));
        yAxis.setLabel(new AutoPositionedLabel("y = sin(x)"));
        yAxis.setPlacement(AxisPlacement.FLOATING);
        xAxis.setPlacement(AxisPlacement.FLOATING);
        ChartStyle sineStyle = new ChartStyle(Color.blue);
        ChartStyle cosineStyle = new ChartStyle(Color.red);
        sineStyle.setLineWidth(2);
        cosineStyle.setLineWidth(2);
        chart.addModel(sineModel, sineStyle);
        chart.addModel(cosineModel, cosineStyle);
        chart.setShadowVisible(true);
        chart.setXAxis(xAxis);
        chart.setYAxis(yAxis);
        JLabel title = new JLabel("Line Chart", JLabel.CENTER);
        title.setForeground(Color.blue);
        demoPanel.add(title, BorderLayout.NORTH);
        chart.setChartBackground(new GradientPaint(0, 0, Color.white, 800, 800, new Color(150, 150, 230)));

        demoPanel.add(chart, BorderLayout.CENTER);


        final ChartCrossHair valueReader = new ChartCrossHair(chart);
        chart.addMouseListener(valueReader);
        chart.addMouseMotionListener(valueReader);
        chart.addDrawable(valueReader);

        final ChartValueReporter valueReporter = new ChartValueReporter(chart);
        chart.addMouseListener(valueReporter);
        chart.addMouseMotionListener(valueReporter);

        demoPanel.add(valueReporter, BorderLayout.SOUTH);

        chart.addMousePanner();
        // We have to do a little more work than usual with the mouse zooming because when the mouse wheel moves
        // we zoom in and therefore we have to update the ChartValueFocus and the ChartValueReader
        // Normally we can just call chart.addMouseZoomer()
        MouseWheelZoomer zoomer = new MouseWheelZoomer(chart);
        zoomer.addZoomListener(new ZoomListener() {
            public void zoomChanged(ChartSelectionEvent event) {
                valueReader.update();
                valueReporter.update();
            }
        });
        chart.addMouseWheelListener(zoomer);
        chart.addMouseMotionListener(zoomer);
        return demoPanel;
    }

    private ChartModel createModel(boolean sine) {
        DefaultChartModel model = new DefaultChartModel();
        int numIntervals = 100;
        for (int i = 0; i <= numIntervals; i++) {
            double x = -180 + (360.0 * i) / numIntervals;
            double y;
            if (sine) {
                y = Math.sin(x * 2 * Math.PI / 360);
            }
            else {
                y = Math.cos(x * 2 * Math.PI / 360);
            }
            if (x > 50) {
                y += 0.2 * (Math.random() - 0.5);
            }
            ChartPoint p = new ChartPoint(x, y);
            model.addPoint(p);
        }
        return model;
    }

    public String getName() {
        return "Simple Curve Chart";
    }

    @Override
    public String getDemoFolder() {
        return "R1.Charts";
    }

    @Override
    public String getDescription() {
        return "As you move the mouse over the chart, the cross-hairs indicate a point on the curve" +
                " whose coordinates you can read off from the message at the bottom of the panel." +
                " I added a randomized section to the right so you can see that the cross-hairs really do " +
                "follow the generated curve and are not pre-computed.";
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
        showAsFrame(new LineChartDemo());
    }

}
