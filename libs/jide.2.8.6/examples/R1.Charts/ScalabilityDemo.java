/*
 * @(#)ScalabilityDemo.java 8/22/2009
 *
 * 2002 - 2010 JIDE Software Inc. All rights reserved.
 * Copyright 2005 - 2010 Catalysoft Ltd. All rights reserved.
 */

import com.jidesoft.chart.Chart;
import com.jidesoft.chart.model.ChartModel;
import com.jidesoft.chart.model.DefaultChartModel;
import com.jidesoft.chart.style.ChartStyle;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.utils.SwingWorker;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.logging.Logger;

@SuppressWarnings("serial")
public class ScalabilityDemo extends AbstractDemo {
    private static final Logger logger = Logger.getLogger(ScalabilityDemo.class.getName());
    private Chart chart;
    private JPanel controlPanel = new JPanel();
    private JLabel pointCountLabel = new JLabel();
    // The slider will be used as the log (to base 10) of the number of points in the ChartModel
    private JSlider slider;
    private ChartStyle style = new ChartStyle(Color.red, true, false);

    public ScalabilityDemo() {
        this(5);
    }

    public ScalabilityDemo(int logOfMax) {
        controlPanel.setPreferredSize(new Dimension(200, 120));
        slider = new JSlider(1, logOfMax);
        controlPanel.add(new JLabel("Data set size: "));
        controlPanel.add(slider);
        controlPanel.add(pointCountLabel);
        slider.setMajorTickSpacing(1);
        slider.setPaintTrack(true);
        slider.setSnapToTicks(true);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setPaintTicks(true);
        slider.setValue(3);
        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                if (!slider.getValueIsAdjusting()) {
                    updateChartModel();
                }
            }
        });
    }

    private Chart createDemo() {
        chart = new Chart();
        // Don't want animation with a large number of points
        chart.setAnimateOnShow(false);
        chart.setPreferredSize(new Dimension(600, 500));
        updateChartModel();

        Paint background = new GradientPaint(0.0f, 0.0f, new Color(0, 0, 150), 0.0f, 800.0f, new Color(0, 0, 64), true);
        chart.setChartBackground(background);

        // Always do lazy rendering
        chart.setLazyRenderingThreshold(0);
        chart.addMouseZoomer().addMousePanner();
        return chart;
    }

    private void updateChartModel() {
        new ModelCreator().execute();
    }

    class ModelCreator extends SwingWorker<ChartModel, Void> {

        @Override
        protected ChartModel doInBackground() throws Exception {
            long before = System.currentTimeMillis();
            DefaultChartModel model = new DefaultChartModel();
            int sliderValue = slider.getValue();
            int steps = (int) Math.pow(10, sliderValue);
            pointCountLabel.setText("(" + steps + " points)");
            double increment = 1.0 / steps;
            double spread = 0.5;
            double i = 0;
            for (int n = 0; n < steps; n++) {
                double y = i + (spread * Math.random()) - spread / 2.0;
                if (y < 0) y = 0;
                if (y > 1) y = 1;
                model.addPoint(i, y);
                i += increment;
            }
            long after = System.currentTimeMillis();
            logger.fine(String.format("Model created in %.1fs", (after - before) / 1000.0));
            return model;
        }


        @Override
        protected void done() {
            try {
                chart.removeModels();
                ChartModel m = get();
                chart.addModel(m);
                style.setPointSize(3);
                chart.setStyle(m, style);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getDescription() {
        return "This example shows efficient handling of large datasets. " +
                "Rendering large datasets can take some time, but with JIDE Charts' advanced rendering approach, the" +
                " interactivity of the user interface does not suffer. The code generates a random, but bounded, data set. " +
                " Try resizing the window, or zooming in and out using the mouse wheel. " +
                "A traditional approach to rendering does not allow to display more than about 20000 points without the " +
                "user-interface suffering lock-ups while the painting takes place. With this approach it is possible to display " +
                "over a *million* points. ";
    }

    public String getName() {
        return "Scalability Demo";
    }

    @Override
    public String getDemoFolder() {
        return "R1.Charts";
    }

    @Override
    public Component getOptionsPanel() {
        return controlPanel;
    }

    public Component getDemoPanel() {
        if (chart == null) {
            chart = createDemo();
        }
        return chart;
    }

    public String getProduct() {
        return PRODUCT_NAME_CHARTS;
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new ScalabilityDemo());
    }
}
