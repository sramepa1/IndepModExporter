/*
 * @(#)BubbleChartDemo.java 9/07/2009
 *
 * 2002 - 2010 JIDE Software Inc. All rights reserved.
 * Copyright 2005 - 2010 Catalysoft Ltd. All rights reserved.
 */

import com.jidesoft.chart.Chart;
import com.jidesoft.chart.Orientation;
import com.jidesoft.chart.PointShape;
import com.jidesoft.chart.axis.Axis;
import com.jidesoft.chart.axis.NumericAxis;
import com.jidesoft.chart.model.ChartModel;
import com.jidesoft.chart.model.ChartPoint3D;
import com.jidesoft.chart.model.Chartable;
import com.jidesoft.chart.model.DefaultChartModel;
import com.jidesoft.chart.model.SortedChartModel;
import com.jidesoft.chart.render.DefaultPointRenderer;
import com.jidesoft.chart.render.SphericalPointRenderer;
import com.jidesoft.chart.style.ChartStyle;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.JideSwingUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class BubbleChartDemo extends AbstractDemo {
    private Chart chart;
    private JPanel demoPanel;
    private static final Color r1 = new Color(255, 0, 0, 128);
    private static final Color g1 = new Color(0, 255, 0, 128);
    private static final Color b1 = new Color(0, 0, 255, 128);
    private static final Color r2 = new Color(200, 0, 0, 255);
    private static final Color g2 = new Color(0, 200, 0, 255);
    private static final Color b2 = new Color(0, 0, 200, 255);
    private ChartModel reds, greens, blues;
    private ChartStyle redStyle, greenStyle, blueStyle;

    public BubbleChartDemo() {
        super();
    }

    public Component getDemoPanel() {
        chart = new Chart();
        demoPanel = new JPanel(new BorderLayout());
        demoPanel.setPreferredSize(new Dimension(600, 500));
        demoPanel.add(chart, BorderLayout.CENTER);

        Axis xAxis = new NumericAxis(0, 5);
        Axis yAxis = new NumericAxis(0, 5);

        chart.setXAxis(xAxis);
        chart.setYAxis(yAxis);
        chart.setRolloverEnabled(true);
        chart.setSelectionEnabled(true);
        DefaultPointRenderer renderer = new DefaultPointRenderer();
        renderer.setOutlineWidth(4f);
        chart.setPointRenderer(renderer);

        redStyle = new ChartStyle(r1, PointShape.BOX);
        greenStyle = new ChartStyle(g1, PointShape.UP_TRIANGLE);
        blueStyle = new ChartStyle(b1, PointShape.DISC);

        reds = createModel("red", 30);
        greens = createModel("green", 30);
        blues = createModel("blue", 30);

        chart.addModel(reds, redStyle);
        chart.addModel(greens, greenStyle);
        chart.addModel(blues, blueStyle);
        return demoPanel;
    }

    public ChartModel createModel(String name, int count) {
        DefaultChartModel model = new DefaultChartModel(name);
        for (int i = 0; i < count; i++) {
            double x = Math.random() * 5;
            double y = Math.random() * 5;
            double z = 5 + Math.random() * 45;
            Chartable p = new ChartPoint3D(x, y, z);
            model.addPoint(p);
        }
        // We create a sorted model from the data so that when selecting multiple points
        // with a shift-select, it selects all those points from this model within a range of x values
        return new SortedChartModel(model, Orientation.vertical);
    }

    @Override
    public Component getOptionsPanel() {
        JRadioButton shapesButton = new JRadioButton("Shapes");
        JRadioButton spheresButton = new JRadioButton("Spheres");
        shapesButton.setSelected(true);
        ButtonGroup group = new ButtonGroup();
        group.add(shapesButton);
        group.add(spheresButton);
        JPanel optionsPanel = new JPanel();
        optionsPanel.add(shapesButton);
        optionsPanel.add(spheresButton);

        shapesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                chart.setChartBackground(Color.white);
                chart.setShadowVisible(false);
                redStyle.setPointColor(r1);
                greenStyle.setPointColor(g1);
                blueStyle.setPointColor(b1);
                DefaultPointRenderer renderer = new DefaultPointRenderer();
                renderer.setOutlineWidth(4f);
                chart.setPointRenderer(renderer);
                chart.startAnimation();
            }
        });
        spheresButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                chart.setShadowVisible(true);
                Paint paint = JideSwingUtilities.getRadialGradientPaint(300f, 250f, 250f, new float[]{0.0f, 1.0f}, new Color[]{Color.white, Color.lightGray});
                chart.setChartBackground(paint);
                redStyle.setPointColor(r2);
                greenStyle.setPointColor(g2);
                blueStyle.setPointColor(b2);
                SphericalPointRenderer renderer = new SphericalPointRenderer();
                renderer.setOutlineWidth(4f);
                chart.setPointRenderer(renderer);
                chart.startAnimation();
            }
        });
        return optionsPanel;
    }

    public String getName() {
        return "Bubble Chart";
    }
    
    @Override
    public int getAttributes() {
        return ATTRIBUTE_NONE;
    }

    @Override
    public String getDemoFolder() {
        return "R1.Charts";
    }

    @Override
    public String getDescription() {
        return "Bubble Charts are like point charts, except that each point also has a size. " +
                "Here, you can select a bubble chart that uses different shapes or view the same data using 3D spheres.";
    }

    public String getProduct() {
        return PRODUCT_NAME_CHARTS;
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new BubbleChartDemo());
    }

}
