/*
 * @(#)DualYAxisBarChartDemo.java 8/22/2009
 *
 * 2002 - 2010 JIDE Software Inc. All rights reserved.
 * Copyright 2005 - 2010 Catalysoft Ltd. All rights reserved.
 */

import com.jidesoft.chart.Chart;
import com.jidesoft.chart.PointShape;
import com.jidesoft.chart.annotation.AutoPositionedLabel;
import com.jidesoft.chart.axis.Axis;
import com.jidesoft.chart.axis.AxisPlacement;
import com.jidesoft.chart.event.*;
import com.jidesoft.chart.model.ChartModel;
import com.jidesoft.chart.model.ChartPoint;
import com.jidesoft.chart.model.DefaultChartModel;
import com.jidesoft.chart.model.Highlight;
import com.jidesoft.chart.render.Bar3DRenderer;
import com.jidesoft.chart.style.ChartStyle;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.range.NumericRange;
import com.jidesoft.range.Range;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Stack;
import java.util.logging.Logger;

@SuppressWarnings("serial")
public class DualYAxisBarChartDemo extends AbstractDemo {
    private static final Logger logger = Logger.getLogger(DualYAxisBarChartDemo.class.getName());
    private JPanel demoPanel;
    private Chart chart;
    private Stack<ZoomFrame> zoomStack;

    public DualYAxisBarChartDemo() {

    }

    private JPanel createDemo() {
        zoomStack = new Stack<ZoomFrame>();
        chart = new Chart();
        demoPanel = new JPanel();
        demoPanel.setPreferredSize(new Dimension(500, 500));
        NumericRange yRange = new NumericRange(0, 40);
        NumericRange yRightRange = new NumericRange(-2, 2);
        Range<?> xRange = new NumericRange(0, 360);
        final Axis xAxis = new Axis(xRange);
        xAxis.setLabel(new AutoPositionedLabel("Angle (Degrees)"));
        final Axis yAxis = new Axis(yRange);
        yAxis.setLabel(new AutoPositionedLabel("Modified Sine Wave"));
        final Axis yRightAxis = new Axis(yRightRange);
        yRightAxis.setPlacement(AxisPlacement.TRAILING);
        yRightAxis.setLabel(new AutoPositionedLabel("Cosine Wave"));

        ChartModel sineModel = createModel();
        ChartModel cosModel = createModel2();

        chart.setXAxis(xAxis);
        chart.setYAxis(yAxis);
        chart.addYAxis(yRightAxis);
        chart.setTitle(new AutoPositionedLabel("Sine and Cosine drawn against Different Axes", Color.darkGray));

        ChartStyle sineStyle = new ChartStyle();
        sineStyle.setBarColor(Color.magenta);
        sineStyle.setLinesVisible(false);
        sineStyle.setPointsVisible(false);
        sineStyle.setBarsVisible(true);
        chart.addModel(sineModel, sineStyle);
        chart.addModel(cosModel, yRightAxis);
        chart.setStyle(cosModel, new ChartStyle(Color.red, PointShape.CIRCLE, Color.green));
        chart.setShadowVisible(true);
        chart.setBarRenderer(new Bar3DRenderer());
        chart.setChartBackground(new GradientPaint(0, 0, Color.white, 800, 800, Color.gray));

        chart.setPanelBackground(new GradientPaint(0, 0, Color.white, 800, 800, Color.gray));
        demoPanel.setLayout(new BorderLayout());
        demoPanel.add(chart);

        // highlight every 10th point
        Highlight redHighlight = new Highlight("red");
        chart.setHighlightStyle(redHighlight, new ChartStyle(Color.red, PointShape.DISC, 7));
        for (int i = 0; i < cosModel.getPointCount(); i += 10) {
            ChartPoint point = (ChartPoint) cosModel.getPoint(i);
            point.setHighlight(redHighlight);
        }

        // TODO : For the rubber band zoomer we need to have a zoom stack
        RubberBandZoomer rubberBand = new RubberBandZoomer(chart);
        chart.addDrawable(rubberBand);
        chart.addMouseListener(rubberBand);
        chart.addMouseMotionListener(rubberBand);

        rubberBand.addZoomListener(new ZoomListener() {
            public void zoomChanged(ChartSelectionEvent event) {
                if (event instanceof RectangleSelectionEvent) {
                    Range<?> currentXRange = chart.getXAxis().getOutputRange();
                    Range<?> currentYRange = chart.getYAxis().getOutputRange();
                    ZoomFrame frame = new ZoomFrame(currentXRange, currentYRange);
                    zoomStack.push(frame);
                    Rectangle selection = (Rectangle) event.getLocation();
                    Point topLeft = selection.getLocation();
                    Point bottomRight = new Point(topLeft.x + selection.width, topLeft.y + selection.height);
                    assert bottomRight.x >= topLeft.x;
                    Point2D rp1 = chart.calculateUserPoint(topLeft);
                    Point2D rp2 = chart.calculateUserPoint(bottomRight);
                    if (rp1 != null && rp2 != null) {
                        // Catch the problem case when division has led to NaN
                        if (Double.isNaN(rp1.getX()) || Double.isNaN(rp2.getX()) ||
                                Double.isNaN(rp1.getY()) || Double.isNaN(rp2.getY())) {
                            logger.warning("Cannot zoom as zoomed position is out of range");
                            return;
                        }
                        assert rp2.getX() >= rp1.getX() : rp2.getX() + " must be greater than or equal to " + rp1.getX();
                        Range<?> xRange = new NumericRange(rp1.getX(), rp2.getX());
                        assert rp1.getY() >= rp2.getY() : rp1.getY() + " must be greater than or equal to " + rp2.getY();
                        Range<?> yRange = new NumericRange(rp2.getY(), rp1.getY());
                        xAxis.setRange(xRange);
                        yAxis.setRange(yRange);
                    }
                }
                else if (event instanceof PointSelectionEvent) {
                    if (zoomStack.size() > 0) {
                        ZoomFrame frame = zoomStack.pop();
                        Range<?> xRange = frame.getXRange();
                        Range<?> yRange = frame.getYRange();
                        xAxis.setRange(xRange);
                        yAxis.setRange(yRange);
                    }
                }
            }
        });
        return demoPanel;
    }

    private ChartModel createModel() {
        DefaultChartModel model = new DefaultChartModel();
        int numIntervals = 20;
        for (int i = 0; i <= numIntervals; i++) {
            double x = (360.0 * i) / numIntervals;
            double y = 10 + 8 * Math.sin(x * 2 * Math.PI / 360.0);
            if (y < 0) {
                y = 0;
            }
            ChartPoint p = new ChartPoint(x, y);
            model.addPoint(p);
        }
        return model;
    }

    private ChartModel createModel2() {
        DefaultChartModel model = new DefaultChartModel();
        int numIntervals = 100;
        for (int i = 0; i <= numIntervals; i++) {
            double x = (360.0 * i) / numIntervals;
            double y = 0.5 + Math.cos(x * 2 * Math.PI / 360.0);
            ChartPoint p = new ChartPoint(x, y);
            model.addPoint(p);
        }
        return model;
    }

    @Override
    public String getDescription() {
        return "This example demonstrates that you can mix trace types on the same chart. " +
                "It also shows the use of two y axes, and a highlight effect on every tenth point of the line.";
    }

    public String getName() {
        return "Dual Y Axis Bar Chart";
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
        showAsFrame(new DualYAxisBarChartDemo());
    }

}
