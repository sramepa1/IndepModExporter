/*
 * @(#)ConfigurableChartDemo.java 8/22/2009 2002 - 2010 JIDE Software Inc. All
 * rights reserved. Copyright 2005 - 2010 Catalysoft Ltd. All rights reserved.
 */

import com.jidesoft.chart.Chart;
import com.jidesoft.chart.PointShape;
import com.jidesoft.chart.annotation.AutoPositionedLabel;
import com.jidesoft.chart.annotation.ChartLabel;
import com.jidesoft.chart.axis.Axis;
import com.jidesoft.chart.axis.AxisPlacement;
import com.jidesoft.chart.event.*;
import com.jidesoft.chart.model.AnnotatedChartModel;
import com.jidesoft.chart.model.ChartPoint;
import com.jidesoft.chart.model.DefaultChartModel;
import com.jidesoft.chart.model.RealPosition;
import com.jidesoft.chart.style.ChartStyle;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.range.NumericRange;
import com.jidesoft.range.Range;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Stack;

@SuppressWarnings("serial")
public class ConfigurableChartDemo extends AbstractDemo {
    private static final Color initialSineColor = Color.green;
    private static final Color initialCosineColor = Color.red;
    private Chart chart;
    private Stack<ZoomFrame> zoomStack = new Stack<ZoomFrame>();
    private AnnotatedChartModel sineModel;
    private AnnotatedChartModel cosModel;
    private ChartPreferencePanel sinePreferencePanel;
    private ChartPreferencePanel cosinePreferencePanel;
    private JPanel controlPanel;
    private JPanel demoPanel = null;

    public Component getDemoPanel() {
        if (demoPanel == null) {
            chart = new Chart();
            sineModel = createSineModel();
            cosModel = createCosineModel();
            sinePreferencePanel = new ChartPreferencePanel(chart, sineModel);
            cosinePreferencePanel = new ChartPreferencePanel(chart, cosModel);
            controlPanel = new ControlPanel();
            demoPanel = new JPanel();
            NumericRange yRange = new NumericRange(0, 50);
            NumericRange yRightRange = new NumericRange(-1, 1);
            Range<?> xRange = new NumericRange(0, 360);
            final Axis xAxis = new Axis(xRange);
            final Axis yAxis = new Axis(yRange);
            final Axis yRightAxis = new Axis(yRightRange);
            yAxis.setPlacement(AxisPlacement.LEADING);
            yRightAxis.setPlacement(AxisPlacement.TRAILING);
            yRightAxis.setLabel(new AutoPositionedLabel("Cosine Wave"));
            xAxis.setLabel(new AutoPositionedLabel("Angle (Degrees)"));
            yAxis.setLabel(new AutoPositionedLabel("Modified Sine Wave"));

            chart.setXAxis(xAxis);
            chart.setYAxis(yAxis);
            chart.addYAxis(yRightAxis);
            chart.setShadowVisible(true);

            ChartStyle sineStyle = new ChartStyle(Color.green, PointShape.BOX, Color.green);
            // sineStyle.setPointSize(1);
            chart.addModel(sineModel, sineStyle);
            sinePreferencePanel.setPointSize(5);
            sinePreferencePanel.setPointShape(PointShape.BOX);
            sinePreferencePanel.setPointColors(initialSineColor);
            sinePreferencePanel.setLineWidth(1);
            sinePreferencePanel.setLineColors(initialSineColor);
            sinePreferencePanel.setUsingLines(true);
            sinePreferencePanel.setUsingPoints(false);

            chart.addModel(cosModel, yRightAxis);
            ChartStyle cosStyle = new ChartStyle(Color.red, PointShape.DISC, Color.red);
            chart.setStyle(cosModel, cosStyle);
            cosinePreferencePanel.setPointSize(5);
            cosinePreferencePanel.setPointShape(PointShape.DISC);
            cosinePreferencePanel.setPointColors(initialCosineColor);
            cosinePreferencePanel.setLineWidth(1);
            cosinePreferencePanel.setLineColors(initialCosineColor);
            cosinePreferencePanel.setUsingLines(true);
            cosinePreferencePanel.setUsingPoints(false);

            // chart.setPanelBackground(new GradientPaint(0,0,Color.white, 800,
            // 800, Color.gray));

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
                            assert rp2.getX() >= rp1.getX();
                            Range<?> xRange = new NumericRange(rp1.getX(), rp2.getX());
                            assert rp1.getY() >= rp2.getY();
                            Range<?> yRange = new NumericRange(rp2.getY(), rp1.getY());
                            xAxis.setRange(xRange);
                            yAxis.setRange(yRange);
                        }
                    } else if (event instanceof PointSelectionEvent) {
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
            demoPanel.setPreferredSize(new Dimension(600, 500));
            demoPanel.setLayout(new BorderLayout());
            demoPanel.add(chart, BorderLayout.CENTER);
            demoPanel.add(controlPanel, BorderLayout.SOUTH);
        }
        return demoPanel;
    }

    private AnnotatedChartModel createSineModel() {
        DefaultChartModel model = new DefaultChartModel("Sine");
        int numIntervals = 100;
        for (int i = 0; i <= numIntervals; i++) {
            double x = (360.0 * i) / numIntervals;
            double y = 20 + 20 * Math.sin(x * 2 * Math.PI / 360.0);
            if (y < 0) {
                y = 0;
            }
            ChartPoint p = new ChartPoint(x, y);
            model.addPoint(p);
        }
        ChartLabel label = new ChartLabel(new RealPosition(150.0), new RealPosition(38.0), "Sine Wave");
        model.addAnnotation(label);
        return model;
    }

    private AnnotatedChartModel createCosineModel() {
        DefaultChartModel model = new DefaultChartModel("Cosine");
        int numIntervals = 100;
        for (int i = 0; i <= numIntervals; i++) {
            double x = (360.0 * i) / numIntervals;
            double y = Math.cos(x * 2 * Math.PI / 360.0);
            ChartPoint p = new ChartPoint(x, y);
            model.addPoint(p);
        }
        ChartLabel label = new ChartLabel(new RealPosition(100.0), new RealPosition(8.0), "Cosine Wave");
        model.addAnnotation(label);
        return model;
    }

    @Override
    public String getDescription() {
        return "This example shows two traces drawn against different Y axes. "
                + "The sine trace is drawn against the axis shown on the left, "
                + "and the cosine trace is drawn against the axis shown on the right. "
                + "In other words the y values of the (scaled) sine trace range from 0 to 40, while the y values of the scaled cosine trace range from -1 to 1. "
                + "Use the mouse to drag out a zoom area and right-click to zoom out again. ";
    }

    public String getName() {
        return "Configurable Chart";
    }

    @Override
    public String getDemoFolder() {
        return "R1.Charts";
    }

    class ControlPanel extends JPanel {
        private JLabel sineLabel = new JLabel(sineModel.getName(), JLabel.RIGHT);
        private JLabel cosineLabel = new JLabel(cosModel.getName(), JLabel.RIGHT);

        public ControlPanel() {
            setBorder(new TitledBorder("Configure Appearance"));
            GridBagLayout layout = new GridBagLayout();
            setLayout(layout);
            GridBagConstraints c1 = new GridBagConstraints(1, 1, 1, 1, 0, 0, GridBagConstraints.EAST,
                    GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0);
            add(sineLabel, c1);
            GridBagConstraints c2 = new GridBagConstraints(2, 1, 1, 1, 0, 0, GridBagConstraints.CENTER,
                    GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0);
            add(sinePreferencePanel, c2);
            GridBagConstraints c3 = new GridBagConstraints(1, 2, 1, 1, 0, 0, GridBagConstraints.EAST,
                    GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0);
            add(cosineLabel, c3);
            GridBagConstraints c4 = new GridBagConstraints(2, 2, 1, 1, 0, 0, GridBagConstraints.CENTER,
                    GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0);
            add(cosinePreferencePanel, c4);
        }
    }

    public String getProduct() {
        return PRODUCT_NAME_CHARTS;
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new ConfigurableChartDemo());
    }

}
