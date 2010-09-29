/*
 * @(#)CubicSplinesChartDemo.java 8/22/2009
 *
 * 2002 - 2010 JIDE Software Inc. All rights reserved.
 * Copyright 2005 - 2010 Catalysoft Ltd. All rights reserved.
 */

import com.jidesoft.chart.Chart;
import com.jidesoft.chart.PointShape;
import com.jidesoft.chart.axis.Axis;
import com.jidesoft.chart.event.PointSelection;
import com.jidesoft.chart.fit.SplineEngine;
import com.jidesoft.chart.model.*;
import com.jidesoft.chart.style.ChartStyle;
import com.jidesoft.chart.style.LineStyle;
import com.jidesoft.chart.style.PointStyle;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.range.CombinedNumericRange;
import com.jidesoft.range.Range;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;

@SuppressWarnings("serial")
public class CubicSplinesChartDemo extends AbstractDemo {
    private JPanel demoPanel;
    private DefaultChartModel upper, lower, splineChartModel;
    private ChartModel averageModel;
    private Chart chart;

    private double[] xPoints = new double[]{2.0, 3.0, 4.0, 5.0};
    private double[] upperPoints = new double[]{2.05, 2.55, 4.55, 4.95};
    private double[] lowerPoints = new double[]{1.95, 2.40, 3.95, 4.75};

    private Chartable _nearestPoint = null;
    private ChartModel nearestModel = null;

    public CubicSplinesChartDemo() {
    }

    private JPanel createDemo() {
        upper = new DefaultChartModel();
        lower = new DefaultChartModel();
        splineChartModel = new DefaultChartModel();
        chart = new Chart();
        demoPanel = new JPanel();
        demoPanel.setPreferredSize(new Dimension(500, 500));
        demoPanel.setLayout(new BorderLayout());

        assert xPoints.length == lowerPoints.length && lowerPoints.length == upperPoints.length;

        for (int i = 0; i < xPoints.length; i++) {
            lower.addPoint(xPoints[i], lowerPoints[i]);
            upper.addPoint(xPoints[i], upperPoints[i]);
        }

        Range<?> xRange = lower.getXRange();
        Range<Double> yRange = new CombinedNumericRange().add(lower.getYRange()).add(upper.getYRange());

        Axis xAxis = new Axis(xRange);
        Axis yAxis = new Axis(yRange);

        chart.setXAxis(xAxis);
        chart.setYAxis(yAxis);

        chart.setShadowVisible(false);

        PointStyle pointStyle = new PointStyle(Color.green.darker(), PointShape.SQUARE);
        ChartStyle chartStyle = new ChartStyle();
        chartStyle.setPointStyle(pointStyle);
        chartStyle.setLinesVisible(false);
        chartStyle.setPointsVisible(true);

        chart.addModel(lower, chartStyle);
        chart.addModel(upper, chartStyle);

        //ChartStyle averageChartStyle = new ChartStyle(Color.blue, false, true);
        ChartStyle splineChartStyle = new ChartStyle();
        splineChartStyle.setLinesVisible(true);
        LineStyle lineStyle = new LineStyle();
        lineStyle.setColor(Color.blue);
        lineStyle.setWidth(1);
        splineChartStyle.setLineStyle(lineStyle);

        final AnnotatedChartModel[] baseModels = new AnnotatedChartModel[]{lower, upper};

        averageModel = new AverageChartModel(baseModels);
        //chart.addModel(averageModel, averageChartStyle);

        chart.addModel(splineChartModel, splineChartStyle);
        computeSpline(averageModel, splineChartModel);

        demoPanel.add(chart, BorderLayout.CENTER);

        chart.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {

                if (e.getClickCount() == 2 && nearestModel != null && _nearestPoint != null) {
                    ((DefaultChartModel) nearestModel).removePoint(_nearestPoint);
                    ((DefaultChartModel) nearestModel).update();
                    computeSpline(averageModel, splineChartModel);
                }

            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseExited(MouseEvent e) {
            }

            public void mousePressed(MouseEvent e) {
                Point p = e.getPoint();
                Double minDistance = null;
                for (ChartModel model : baseModels) {
                    PointSelection nearest = chart.nearestPoint(p, model);
                    Chartable chartable = nearest.getSelected();
                    double distance = nearest.getDistance();
                    if (minDistance == null || distance < minDistance) {
                        _nearestPoint = chartable;
                        nearestModel = model;
                        minDistance = distance;
                    }
                }
            }

            public void mouseReleased(MouseEvent e) {
            }
        });
        chart.addMouseMotionListener(new MouseMotionListener() {
            public void mouseDragged(MouseEvent e) {
                if (nearestModel != null) {
                    Point p = e.getPoint();
                    Point2D realPoint = chart.calculateUserPoint(p);
                    if (realPoint != null) {
                        ((ChartPoint) _nearestPoint).setY(new RealPosition(realPoint.getY()));
                    }
                    // TODO: Should not have to call an update() method!
                    ((DefaultChartModel) nearestModel).update();
                    computeSpline(averageModel, splineChartModel);
                }
            }

            public void mouseMoved(MouseEvent e) {
            }
        });
        return demoPanel;
    }


    private void computeSpline(final ChartModel inputModel, DefaultChartModel splineModel) {
        splineModel.clearPoints();
        // Create some double arrays and populate them from the chart model
        // TODO: xs and ys are assumed to be sorted in the call to computeSplineSlopes, which is not guaranteed
        final int pointCount = inputModel.getPointCount();
        double xs[] = new double[pointCount];
        double ys[] = new double[pointCount];
        for (int i = 0; i < pointCount; i++) {
            Chartable p = inputModel.getPoint(i);
            xs[i] = p.getX().position();
            ys[i] = p.getY().position();
        }

        double[] slopes = SplineEngine.computeSplineSlopes(pointCount - 1, xs, ys);

        // Number of divisions in each segment
        int nDivs = 20;
        double rx, ry;

        for (int seg = 0; seg < pointCount - 1; seg++) {
            double xstep = (xs[seg + 1] - xs[seg]) / nDivs;
            for (int i = 0; i <= nDivs; i++) {
                rx = xs[seg] + i * xstep;
                ry = SplineEngine.splineEval(rx, xs[seg], xs[seg + 1], ys[seg], ys[seg + 1], slopes[seg], slopes[seg + 1]);
                splineModel.addPoint(rx, ry);
            }
        }
    }

    @Override
    public String getDescription() {
        return "This graph shows some interpolation using cubic splines. " +
                "The points are shown in pairs - the midpoint of each pair is used for interpolation. " +
                "You can click on any of the points and move it. Notice how the interpolation reacts to the new positions of the points. " +
                "You can also dismiss a point by double-clicking on it (but I have not yet implemented a method of bringing them back once they have been dismissed!).";
    }

    public Component getDemoPanel() {
        if (demoPanel == null) {
            demoPanel = createDemo();
        }
        return demoPanel;
    }

    public String getName() {
        return "Curve Fitting Chart";
    }

    @Override
    public String getDemoFolder() {
        return "R1.Charts";
    }

    public String getProduct() {
        return PRODUCT_NAME_CHARTS;
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new CubicSplinesChartDemo());
    }

}
