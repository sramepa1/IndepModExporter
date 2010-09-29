/*
 * @(#)MeterDemo.java 8/22/2009
 *
 * 2002 - 2010 JIDE Software Inc. All rights reserved.
 * Copyright 2005 - 2010 Catalysoft Ltd. All rights reserved.
 */

import com.jidesoft.chart.Chart;
import com.jidesoft.chart.Drawable;
import com.jidesoft.chart.annotation.AutoPositionedLabel;
import com.jidesoft.chart.axis.Axis;
import com.jidesoft.chart.axis.TimeAxis;
import com.jidesoft.chart.model.DefaultChartModel;
import com.jidesoft.chart.style.ChartStyle;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.range.NumericRange;
import com.jidesoft.range.Range;
import com.jidesoft.range.TimeRange;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.logging.Logger;

public class MeterDemo extends AbstractDemo {
    private static final long serialVersionUID = 3477660378504625992L;
    private static final Logger logger = Logger.getLogger(MeterDemo.class.getName());
    private JPanel demoPanel;
    private RandomWalkMeter wm1, wm2, wm3;
    private Chart chart;
    private DefaultChartModel model1, model2, model3;
    private Axis xAxis, yAxis;
    private GridBagLayout layout;
    private int tp1 = 0, tp2 = 0, tp3 = 0;

    public MeterDemo() {

    }

    private JPanel createDemo() {
        demoPanel = new JPanel();
        demoPanel.setPreferredSize(new Dimension(600, 400));
        wm1 = new RandomWalkMeter("Red", new Color(120, 30, 30), Color.red);
        wm2 = new RandomWalkMeter("Green", new Color(30, 120, 30), Color.green);
        wm3 = new RandomWalkMeter("Blue", new Color(30, 30, 120), Color.cyan);
        chart = new Chart();
        model1 = new DefaultChartModel("Red");
        model2 = new DefaultChartModel("Green");
        model3 = new DefaultChartModel("Blue");
        ChartStyle redStyle = new ChartStyle(Color.red);
        redStyle.setLineWidth(2);
        ChartStyle greenStyle = new ChartStyle(Color.green);
        greenStyle.setLineWidth(2);
        ChartStyle blueStyle = new ChartStyle(Color.blue);
        blueStyle.setLineWidth(2);
        chart.addModel(model1, redStyle);
        chart.addModel(model2, greenStyle);
        chart.addModel(model3, blueStyle);
        chart.setChartBackground(new Color(128, 128, 128, 64));
        NumericRange yRange = new NumericRange(0, 110);
        long now = System.currentTimeMillis();
        TimeRange xRange = new TimeRange(now, now);
        xAxis = new TimeAxis(xRange);
        yAxis = new Axis(yRange);
        xAxis.setLabel(new AutoPositionedLabel("Time"));
        yAxis.setLabel(new AutoPositionedLabel("Value"));
        chart.setYAxis(yAxis);
        chart.setXAxis(xAxis);
        layout = new GridBagLayout();
        demoPanel.setLayout(layout);
        GridBagConstraints c1 = new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
        demoPanel.add(wm1, c1);
        GridBagConstraints c2 = new GridBagConstraints(2, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
        demoPanel.add(wm2, c2);
        GridBagConstraints c3 = new GridBagConstraints(3, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
        demoPanel.add(wm3, c3);
        GridBagConstraints c4 = new GridBagConstraints(1, 2, 3, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
        demoPanel.add(chart, c4);
        PropertyChangeListener valueListener = new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                RandomWalkMeter source = (RandomWalkMeter) evt.getSource();
                Double value = (Double) evt.getNewValue();
                addToModel(source, value);
            }
        };
        wm1.addPropertyChangeListener("value", valueListener);
        wm2.addPropertyChangeListener("value", valueListener);
        wm3.addPropertyChangeListener("value", valueListener);
        return demoPanel;
    }

    private void addToModel(RandomWalkMeter source, Double value) {
        if (source == wm1) {
            model1.addPoint(System.currentTimeMillis(), value);
            if (tp1++ > 60) {
                model1.removePoint(0);
                List<Drawable> drawables = (List<Drawable>) chart.getDrawables();
                drawables.remove(0);
            }
            Color c = new Color((float) (wm1.getValue() / 100.0), (float) (wm2.getValue() / 100.0), (float) (wm3.getValue() / 100.0), 0.25f);
            logger.fine("Color is " + c);
            chart.addDrawable(new ColorDrawable(System.currentTimeMillis(), c));
        }
        else if (source == wm2) {
            model2.addPoint(System.currentTimeMillis(), value);
            if (tp2++ > 60) {
                model2.removePoint(0);
            }
        }
        else if (source == wm3) {
            model3.addPoint(System.currentTimeMillis(), value);
            if (tp3++ > 60) {
                model3.removePoint(0);
            }
        }

        Range<?> timeRange = model1.getXRange();
        long maxX = (long) (timeRange.minimum() + 60 * 1000.0); // A minute's worth of data
        TimeRange xRange = new TimeRange((long) timeRange.minimum(), maxX);
        xAxis.setRange(xRange, true);
    }

    @Override
    public String getDescription() {
        return "This shows three Meter components, each displaying one dimension of the red-green-blue parts of a colour. " +
                "The values change once per second as a 'random walk'. Notice how the meters do not switch abruptly to the new value, " +
                " but the dial pointer travels across the intermediate values to reach the new value. The chart underneath shows the historical values of each of the " +
                "three meters, and gives an indication of the combined colour as the background to the chart. ";
    }

    public String getName() {
        return "Meter Demo";
    }

    @Override
    public String getDemoFolder() {
        return "R1.Charts";
    }

    /**
     * Used to give an indication of the randomly chosen color in the background
     */
    class ColorDrawable implements Drawable {
        private double x;
        private Color color;

        public ColorDrawable(double x, Color color) {
            this.x = x;
            this.color = color;
        }

        public void draw(Graphics g) {
            final int halfASecond = 500;
            Point topLeft = chart.calculatePixelPoint(new Point2D.Double(x - halfASecond, 110));
            Point bottomRight = chart.calculatePixelPoint(new Point2D.Double(x + halfASecond, 0));
            g.setColor(color);
            g.fillRect(topLeft.x, topLeft.y, bottomRight.x - topLeft.x, bottomRight.y - topLeft.y);
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
        showAsFrame(new MeterDemo());
    }

}
