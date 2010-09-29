/*
 * @(#)GradientLineFillDemo.java 8/22/2009
 *
 * 2002 - 2010 JIDE Software Inc. All rights reserved.
 * Copyright 2005 - 2010 Catalysoft Ltd. All rights reserved.
 */

import com.jidesoft.chart.Chart;
import com.jidesoft.chart.annotation.AutoPositionedLabel;
import com.jidesoft.chart.axis.Axis;
import com.jidesoft.chart.model.ChartModel;
import com.jidesoft.chart.model.DefaultChartModel;
import com.jidesoft.chart.style.ChartStyle;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.range.NumericRange;
import com.jidesoft.range.Range;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

@SuppressWarnings("serial")
public class GradientLineFillDemo extends AbstractDemo {
    private ChartStyle style1;
    private ChartStyle style2;
    private JPanel demoPanel;
    private Chart chart;
    private Range<Double> xRange = new NumericRange(0, 110);
    private Range<Double> yRange = new NumericRange(0, 275);
    private Axis xAxis, yAxis;
    private JPanel controlPanel;
    private JButton button = new JButton("Recreate");

    public GradientLineFillDemo() {

    }

    public JPanel createDemo() {
        demoPanel = new JPanel();
        demoPanel.setPreferredSize(new Dimension(500, 500));
        chart = new Chart();
        controlPanel = new JPanel();
        controlPanel.setBackground(Color.black);
        button.setBackground(Color.black);
        controlPanel.add(button);
        demoPanel.setLayout(new BorderLayout());
        demoPanel.add(chart, BorderLayout.CENTER);
        demoPanel.add(controlPanel, BorderLayout.SOUTH);
        style1 = new ChartStyle(Color.cyan);
        style2 = new ChartStyle(Color.red);
        xAxis = new Axis(xRange, "x");
        yAxis = new Axis(yRange, "y");
        chart.setXAxis(xAxis);
        chart.setYAxis(yAxis);
        chart.setPanelBackground(Color.black);
        chart.setAxisColor(Color.white);
        chart.setLabelColor(Color.lightGray);
        chart.setChartBackground(Color.black);
        chart.setTickColor(Color.white);
        chart.setGridColor(new Color(92, 92, 92));
        chart.setTitle(new AutoPositionedLabel("Gradient Line Fill Chart", Color.white));
        createAndAddModels();

        demoPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Dimension size = demoPanel.getSize();
                style1.setLineFill(new GradientPaint(0f, 0f, new Color(0, 255, 255, 150), 0, (float) size.getHeight(), new Color(0, 0, 0, 128)));
                style2.setLineFill(new GradientPaint(0f, 0f, new Color(255, 0, 0, 150), 0, (float) size.getHeight(), new Color(0, 0, 0, 128)));
            }
        });

        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                chart.removeModels();
                createAndAddModels();
                chart.startAnimation();
            }
        });
        return demoPanel;
    }

    private void createAndAddModels() {
        // Models are constrained so that model2 is always less than or equal to model 1
        ChartModel model1 = createModel("Model 1", null);
        ChartModel model2 = createModel("Model 2", model1);

        chart.addModel(model1, style1);
        chart.addModel(model2, style2);
    }

    private ChartModel createModel(String name, ChartModel other) {
        DefaultChartModel model = new DefaultChartModel(name);
        final int maxChange = 15;
        double upperLimit = other == null ? 250 : other.getPoint(0).getY().position();
        double y = upperLimit * Math.random();
        double x = 0;
        int numPoints = 200;
        double step = 100.0 / numPoints;
        for (int i = 0; i <= numPoints; i++) {
            model.addPoint(x, y);
            y += 2 * maxChange * Math.random() - maxChange;
            if (y < 0) {
                y = 0;
            }
            else if (y > 250) {
                y = 250;
            }
            if (other != null) {
                double otherY = i >= other.getPointCount() ? Double.MAX_VALUE : other.getPoint(i).getY().position();
                if (y > otherY) {
                    y = otherY;
                }
            }
            x += step;
        }
        return model;
    }

    public String description() {
        return "This demonstrates that you can set a gradient fill between the drawn line and the axis. " +
                "We think this looks pretty cool!";
    }

    public String getName() {
        return "Gradient Line Fill";
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
        showAsFrame(new GradientLineFillDemo());
    }
}
