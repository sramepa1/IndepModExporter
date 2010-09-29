/*
 * @(#)CyclicalChartDemo.java 8/22/2009
 *
 * 2002 - 2010 JIDE Software Inc. All rights reserved.
 * Copyright 2005 - 2010 Catalysoft Ltd. All rights reserved.
 */

import com.jidesoft.chart.Chart;
import com.jidesoft.chart.axis.Axis;
import com.jidesoft.chart.axis.AxisPlacement;
import com.jidesoft.chart.model.DefaultChartModel;
import com.jidesoft.chart.render.SphericalPointRenderer;
import com.jidesoft.chart.style.ChartStyle;
import com.jidesoft.plaf.LookAndFeelFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

@SuppressWarnings("serial")
public class CyclicalChartDemo extends AbstractDemo implements AnimatedDemo {
    private static final Logger logger = Logger.getLogger(CyclicalChartDemo.class.getName());
    private Chart chart;
    private DefaultChartModel outerModel, innerModel;
    private double angle = 0;
    private Timer timer;

    public CyclicalChartDemo() {

    }

    public Chart createChart() {
        outerModel = new DefaultChartModel();
        outerModel.setCyclical(true);
        innerModel = new DefaultChartModel();
        innerModel.setCyclical(true);
        chart = new Chart("Cyclical Models");
        chart.setPreferredSize(new Dimension(500, 500));
        Axis xAxis = chart.getXAxis();
        Axis yAxis = chart.getYAxis();
        xAxis.setRange(-15, 15);
        xAxis.setPlacement(AxisPlacement.FLOATING);
        yAxis.setRange(-15, 15);
        yAxis.setPlacement(AxisPlacement.FLOATING);
        ChartStyle outerStyle = new ChartStyle(new Color(0, 0, 170), true, true);
        outerStyle.setLineWidth(6);
        outerStyle.setPointSize(20);
        updateModel(outerModel, 12, 12, angle);
        chart.addModel(outerModel, outerStyle);
        chart.setPointRenderer(new SphericalPointRenderer());
        chart.setShadowVisible(false);
        ChartStyle innerStyle = new ChartStyle(new Color(170, 0, 0), true, true);
        innerStyle.setLineWidth(6);
        innerStyle.setPointSize(20);
        updateModel(innerModel, 6, 6, angle);
        chart.addModel(innerModel, innerStyle).setPointRenderer(new SphericalPointRenderer());
        chart.setShadowVisible(false);
        ActionListener listener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                angle += Math.PI / 180;
                updateModel(outerModel, 12, 12, angle);
                updateModel(innerModel, 6, 6, -angle);
                outerModel.update();
            }
        };
        timer = new Timer(40, listener);
        timer.setRepeats(true);
        chart.setVerticalGridLinesVisible(false);
        chart.setHorizontalGridLinesVisible(false);
        chart.setAxisColor(Color.yellow);
        chart.setChartBackground(new GradientPaint(0f, 0f, Color.lightGray, 0f, 600f, new Color(100, 0, 100)));
        chart.setLabelColor(Color.yellow);
        chart.setTickColor(Color.yellow);
        return chart;
    }

    public Component getDemoPanel() {
        if (chart == null) {
            createChart();
        }
        return chart;
    }

    public void startAnimation() {
        logger.fine("Starting animation for " + getName());
        timer.restart();
    }

    public void stopAnimation() {
        logger.fine("Stopping animation for " + getName());
        timer.stop();
    }

    public void updateModel(DefaultChartModel model, int radius, int points, double angle) {
        model.clearPoints();
        double increment = Math.PI * 2 / points;
        for (double i = angle; i < angle + 2 * Math.PI; i += increment) {
            double x = radius * Math.cos(i);
            double y = radius * Math.sin(i);
            model.addPoint(x, y, false);
        }
    }

    @Override
    public String getDescription() {
        return "This chart shows a cyclical chart model. (The animation is just for fun!)";
    }

    public String getName() {
        return "Cyclical Chart";
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
        showAsFrame(new CyclicalChartDemo());
    }
}
