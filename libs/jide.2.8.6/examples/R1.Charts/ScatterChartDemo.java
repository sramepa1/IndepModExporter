/*
 * @(#)ScatterChartDemo.java 8/22/2009
 *
 * 2002 - 2010 JIDE Software Inc. All rights reserved.
 * Copyright 2005 - 2010 Catalysoft Ltd. All rights reserved.
 */

import com.jidesoft.chart.Chart;
import com.jidesoft.chart.PointShape;
import com.jidesoft.chart.annotation.AutoPositionedLabel;
import com.jidesoft.chart.axis.Axis;
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
import java.awt.geom.Point2D;
import java.util.Stack;
import java.util.logging.Logger;

@SuppressWarnings("serial")
public class ScatterChartDemo extends AbstractDemo {
    private static final Logger logger = Logger.getLogger(ScatterChartDemo.class.getName());
    private JPanel demoPanel;
    private Chart chart;
    private Stack<ZoomFrame> zoomStack;

    public ScatterChartDemo() {

    }

    private JPanel createDemo() {
        zoomStack = new Stack<ZoomFrame>();
        chart = new Chart();
        demoPanel = new JPanel();
        demoPanel.setPreferredSize(new Dimension(500, 500));
        NumericRange yRange = new NumericRange(0, 10);
        Range<?> xRange = new NumericRange(0, 10);
        final Axis xAxis = new Axis(xRange);
        final Axis yAxis = new Axis(yRange);
        xAxis.setLabel(new AutoPositionedLabel("X"));
        yAxis.setLabel(new AutoPositionedLabel("Y"));

        ChartModel model1 = createCloudModel(6, 4, 5, 4);
        ChartModel model2 = createCloudModel(4, 4, 4, 5);
        ChartModel model3 = createCloudModel(5, 6, 3, 3);

        Color translucentBlue = new Color(0, 0, 255, 128);
        ChartStyle style1 = new ChartStyle(translucentBlue, PointShape.BOX);
        style1.setPointSize(20);
        chart.addModel(model1, style1);

        Color translucentRed = new Color(255, 0, 0, 128);
        ChartStyle style2 = new ChartStyle(translucentRed, PointShape.UP_TRIANGLE);
        style2.setPointSize(20);
        chart.addModel(model2, style2);

        Color translucentGreen = new Color(0, 255, 0, 128);
        ChartStyle style3 = new ChartStyle(translucentGreen, PointShape.DISC);
        style3.setPointSize(20);
        chart.addModel(model3, style3);

        chart.setXAxis(xAxis);
        chart.setYAxis(yAxis);
        chart.setTitle(new AutoPositionedLabel("Simple Scatter Chart", Color.blue));

        demoPanel.setLayout(new BorderLayout());
        demoPanel.add(chart, BorderLayout.CENTER);

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
                        logger.fine("Top left position was " + rp1);
                        logger.fine("Bottom right position was " + rp2);
                        assert rp2.getX() >= rp1.getX();
                        Range<?> xRange = new NumericRange(rp1.getX(), rp2.getX());
                        assert rp1.getY() >= rp2.getY();
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

    /**
     * Creates a cloud of random points contained in an ellipse defined by the input coordinates.
     *
     * @param centreX
     * @param centreY
     * @return a ChartModel
     */
    private ChartModel createCloudModel(double centreX, double centreY, double xSize, double ySize) {
        DefaultChartModel model = new DefaultChartModel();
        int numPoints = 30;
        for (int i = 0; i < numPoints; i++) {
            double direction = 2 * Math.PI * Math.random();
            double x = centreX + xSize * Math.random() * Math.cos(direction);
            double y = centreY + ySize * Math.random() * Math.sin(direction);
            ChartPoint p = new ChartPoint(x, y);
            model.addPoint(p);
        }
        return model;
    }

    @Override
    public String getDescription() {
        return "A chart doesn't have to be a set of connected lines. Sometimes correlations show up best with a scatter chart. " +
                "This chart also shows use of transparent colours, as the points may sometimes overlap. " +
                "To zoom in to this chart, click and drag out a 'rubber band' selection area using the mouse. Use a right click to " +
                "zoom out again.";
    }

    public String getName() {
        return "Scatter Chart";
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
        showAsFrame(new ScatterChartDemo());
    }


}
