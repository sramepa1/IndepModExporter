/*
 * @(#)GradientLineFillDemo.java 8/22/2009
 *
 * 2002 - 2010 JIDE Software Inc. All rights reserved.
 * Copyright 2005 - 2010 Catalysoft Ltd. All rights reserved.
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Toolkit;
import java.net.URL;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jidesoft.chart.Chart;
import com.jidesoft.chart.Drawable;
import com.jidesoft.chart.IntervalMarker;
import com.jidesoft.chart.LineMarker;
import com.jidesoft.chart.annotation.ChartImage;
import com.jidesoft.chart.annotation.ChartLabel;
import com.jidesoft.chart.axis.Axis;
import com.jidesoft.chart.axis.AxisPlacement;
import com.jidesoft.chart.axis.NumericAxis;
import com.jidesoft.chart.model.ChartModel;
import com.jidesoft.chart.model.Chartable;
import com.jidesoft.chart.model.DefaultChartModel;
import com.jidesoft.chart.render.PointRenderer;
import com.jidesoft.chart.render.SphericalPointRenderer;
import com.jidesoft.chart.style.ChartStyle;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.range.NumericRange;

import static com.jidesoft.chart.Orientation.*;

@SuppressWarnings("serial")
public class MarkerDemo extends AbstractDemo {
    private JPanel demoPanel;
    private Chart chart;
    private DefaultChartModel smallBirdsModel, largeBirdsModel;
    
    @Override
    public String getDescription() {
        return "This demonstrates the use of line and interval markers";
    }

    public String getName() {
        return "Marker Demo";
    }

    @Override
    public String getDemoFolder() {
        return "R1.Charts";
    }

    public Component getDemoPanel() {
        if (demoPanel == null) {
            demoPanel = new JPanel(new BorderLayout());
            demoPanel.add(new JLabel("Turkey Roasting Times", JLabel.CENTER), BorderLayout.NORTH);
            smallBirdsModel = new DefaultChartModel("Small Birds");
            smallBirdsModel.addPoint(2, 110);
            smallBirdsModel.addPoint(2.5, 120);
            smallBirdsModel.addPoint(3, 130);
            smallBirdsModel.addPoint(3.5, 140);
            
            largeBirdsModel = new DefaultChartModel("Large Birds");
            largeBirdsModel.addPoint(4, 170);
            largeBirdsModel.addPoint(5, 190);
            largeBirdsModel.addPoint(6, 210);
            largeBirdsModel.addPoint(7, 230);
            largeBirdsModel.addPoint(8, 250);
            largeBirdsModel.addPoint(9, 270);
            largeBirdsModel.addPoint(10, 290);
            chart = new Chart();
            demoPanel.add(chart, BorderLayout.CENTER);
            ChartStyle style = new ChartStyle(new Color(200, 100, 0)).withPointsAndLines();
            style.setPointSize(10);
            chart.setPointRenderer(new SphericalPointRenderer());
            chart.addModel(smallBirdsModel, style);
            chart.addModel(largeBirdsModel, style);
            chart.setXAxis(new NumericAxis(new NumericRange(1.5, 10.5), "Weight (kg)"));
            chart.setYAxis(new NumericAxis(new NumericRange(100, 310), "Cooking Time (mins)"));
            Axis hoursAxis = new NumericAxis(new NumericRange(1.667, 5.167), "Cooking Time (hrs)");
            hoursAxis.setPlacement(AxisPlacement.TRAILING);
            chart.addYAxis(hoursAxis);
            chart.addDrawable(new LineMarker(chart, horizontal, 120, Color.darkGray));
            chart.addDrawable(new LineMarker(chart, horizontal, 180, Color.darkGray));
            chart.addDrawable(new LineMarker(chart, horizontal, 240, Color.darkGray));
            chart.addDrawable(new LineMarker(chart, horizontal, 300, Color.darkGray));
            IntervalMarker smallBirdsMarker = new IntervalMarker(chart);
            smallBirdsMarker.setOrientation(vertical);
            smallBirdsMarker.setInterval(2, 3.5);
            Paint smallBirdsPaint = new GradientPaint(0f, 250f, new Color(255, 0, 0, 50), 0f, 500f, new Color(50, 50, 255, 50));
            smallBirdsMarker.setPaint(smallBirdsPaint);
            chart.addDrawable(smallBirdsMarker);
            IntervalMarker largeBirdsMarker = new IntervalMarker(chart);
            largeBirdsMarker.setOrientation(vertical);
            largeBirdsMarker.setInterval(4, 10);
            Paint largeBirdsPaint = new GradientPaint(0f, 0f, new Color(255, 0, 0, 50), 0f, 500f, new Color(50, 50, 255, 50));
            largeBirdsMarker.setPaint(largeBirdsPaint);
            chart.addDrawable(largeBirdsMarker);
            demoPanel.setPreferredSize(new Dimension(600, 500));
            ChartLabel smallBirdsLabel = new ChartLabel(2.75, 135, "Small Birds");
            smallBirdsLabel.setRotation(-Math.PI/5);
            smallBirdsModel.addAnnotation(smallBirdsLabel);
            ChartLabel largeBirdsLabel = new ChartLabel(6.5, 240, "Large Birds");
            largeBirdsLabel.setRotation(-Math.PI/5);
            largeBirdsModel.addAnnotation(largeBirdsLabel);
            
            final Image smallTurkeyImage = createImage("roasted_turkey_24.png");
            final Image largeTurkeyImage = createImage("roasted_turkey_48.png");
            chart.setPointRenderer(new PointRenderer() {
                public Shape renderPoint(Graphics g, Chart chart, ChartModel m, Chartable point, boolean isSelected,
                        boolean hasRollover, boolean hasFocus, int x, int y) {
                    if (m.equals(smallBirdsModel)) {
                        g.drawImage(smallTurkeyImage, x-12, y-12, chart);
                    } else {
                        g.drawImage(largeTurkeyImage, x-24, y-24, chart);
                    }
                    return null;
                }
            });
            
        }
        return demoPanel;
    }
    
    private Image createImage(String path) {
        ClassLoader loader = getClass().getClassLoader();
        if (loader != null) {
            URL url = loader.getResource(path);
            if (url == null) {
                url = loader.getResource("/" + path);
            }
            Image image = Toolkit.getDefaultToolkit().createImage(url);
            return image;
        }
        return null;
    }

    @Override
    public int getAttributes() {
        return ATTRIBUTE_UPDATED;
    }

    public String getProduct() {
        return PRODUCT_NAME_CHARTS;
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new MarkerDemo());
    }
}
