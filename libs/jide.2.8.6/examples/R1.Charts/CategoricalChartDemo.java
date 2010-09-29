/*
 * @(#)CategoricalChartDemo.java 8/22/2009
 *
 * 2002 - 2010 JIDE Software Inc. All rights reserved.
 * Copyright 2005 - 2010 Catalysoft Ltd. All rights reserved.
 */

import com.jidesoft.chart.Chart;
import com.jidesoft.chart.PointShape;
import com.jidesoft.chart.annotation.AutoPositionedLabel;
import com.jidesoft.chart.axis.Axis;
import com.jidesoft.chart.axis.CategoryAxis;
import com.jidesoft.chart.event.PointSelection;
import com.jidesoft.chart.model.*;
import com.jidesoft.chart.render.SphericalPointRenderer;
import com.jidesoft.chart.style.ChartStyle;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.range.CategoryRange;
import com.jidesoft.range.NumericRange;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;

@SuppressWarnings("serial")
public class CategoricalChartDemo extends AbstractDemo {
    private JPanel demoPanel;
    private ChartCategory<Country> usa = new ChartCategory<Country>(new Country("USA"));
    private ChartCategory<Country> uk = new ChartCategory<Country>(new Country("UK"));
    private ChartCategory<Country> france = new ChartCategory<Country>(new Country("France"));
    private ChartCategory<Country> germany = new ChartCategory<Country>(new Country("Germany"));
    private ChartCategory<Country> russia = new ChartCategory<Country>(new Country("Russia"));
    private ChartCategory<Country> china = new ChartCategory<Country>(new Country("China"));
    private CategoryRange<Country> countries = new CategoryRange<Country>().add(usa).add(uk).add(france).add(germany).add(russia).add(china);
    private DefaultChartModel model;
    private Chart chart;
    private Highlight redHighlight = new Highlight("red");
    private ChartPoint highlighted = null;

    public CategoricalChartDemo() {

    }

    public JPanel createDemo() {
        model = new DefaultChartModel();
        demoPanel = new JPanel();
        Axis xAxis = new CategoryAxis<Country>(countries);
        xAxis.setLabel("Countries");
        Axis yAxis = new Axis(new NumericRange(0, 25));
        yAxis.setLabel("Numbers");
        chart = new Chart();
        chart.setXAxis(xAxis);
        chart.setYAxis(yAxis);
        chart.setTitle(new AutoPositionedLabel("Chart with Categorical X Values", Color.blue));
        chart.setVerticalGridLinesVisible(false);
        chart.setShadowVisible(true);
        model.addPoint(usa, 22).addPoint(uk, 18).addPoint(france, 13.5).addPoint(germany, 12).addPoint(russia, 8).addPoint(china, 7);
        ChartStyle style = new ChartStyle(new Color(0, 170, 0), true, true);
        style.setLineWidth(6);
        style.setPointSize(20);
        chart.addModel(model, style).setPointRenderer(new SphericalPointRenderer());
        chart.setHighlightStyle(redHighlight, new ChartStyle(new Color(200, 0, 0), PointShape.DISC, 20));
        demoPanel.setLayout(new BorderLayout());
        demoPanel.add(chart, BorderLayout.CENTER);
        chart.addMouseMotionListener(new MouseMotionListener() {
            // Allow the user to drag _points in the vertical direction
            public void mouseDragged(MouseEvent e) {
                rollover(e);
                if (highlighted != null) {
                    Point p = e.getPoint();
                    Point2D userPoint = chart.calculateUserPoint(p);
                    if (userPoint != null) {
                        highlighted.setY(new RealPosition(userPoint.getY()));
                    }
                    chart.repaint();
                }
            }

            // Add a rollover effect
            public void mouseMoved(MouseEvent e) {
                rollover(e);
            }

            private void rollover(MouseEvent e) {
                Point p = e.getPoint();
                PointSelection selection = chart.nearestPoint(p, model);
                if (highlighted != null) {
                    highlighted.setHighlight(null);
                }
                Chartable selected = selection.getSelected();
                Point2D selectedCoords = new Point2D.Double(selected.getX().position(), selected.getY().position());
                Point dp = chart.calculatePixelPoint(selectedCoords);
                // Only activate the rollover effect when within 50 pixels of a point
                if (p.distance(dp) < 50) {
                    highlighted = (ChartPoint) selection.getSelected();
                    highlighted.setHighlight(redHighlight);
                    ChartCategory<?> x = (ChartCategory<?>) selected.getX();
                    chart.setToolTipText(String.format("%s : %.1f", x.getName(), selected.getY().position()));
                }
                else {
                    chart.setToolTipText(null);
                }
                chart.repaint();
            }
        });
        chart.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                if (highlighted != null) {
                    highlighted.setHighlight(null);
                    chart.repaint();
                }
            }
        });
        demoPanel.setPreferredSize(new Dimension(500, 500));
        return demoPanel;
    }


    @Override
    public String getDescription() {
        return "This example shows that values do not have to be numbers for us to be able to draw a chart. " +
                "Here the x axis value can be only one of a set of possible values, which we call a 'category'. " +
                "There is also a rollover effect on the points - a point turns red when you mouse over it. It also shows a tool tip. When you " +
                "drag the mouse you move the position of the highlighted point in the y direction.";
    }


    public String getName() {
        return "Categorical Chart";
    }

    @Override
    public String getDemoFolder() {
        return "R1.Charts";
    }

    class Country {
        private String name;

        public Country(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
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
        showAsFrame(new CategoricalChartDemo());
    }
}
