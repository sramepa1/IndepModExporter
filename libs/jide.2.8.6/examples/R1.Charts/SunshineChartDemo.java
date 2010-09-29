/*
 * @(#)SunshineChartDemo.java 8/22/2009
 *
 * 2002 - 2010 JIDE Software Inc. All rights reserved.
 * Copyright 2005 - 2010 Catalysoft Ltd. All rights reserved.
 */

import com.jidesoft.chart.Chart;
import com.jidesoft.chart.Legend;
import com.jidesoft.chart.annotation.AutoPositionedLabel;
import com.jidesoft.chart.axis.Axis;
import com.jidesoft.chart.axis.TimeAxis;
import com.jidesoft.chart.fit.Polynomial;
import com.jidesoft.chart.fit.QuadraticFitter;
import com.jidesoft.chart.model.AnnotatedChartModel;
import com.jidesoft.chart.model.Chartable;
import com.jidesoft.chart.model.DefaultChartModel;
import com.jidesoft.chart.model.SelectableChartModel;
import com.jidesoft.chart.render.CylinderBarRenderer;
import com.jidesoft.chart.style.ChartStyle;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.range.NumericRange;
import com.jidesoft.range.TimeRange;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import static java.awt.GridBagConstraints.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

/**
 * An example that shows some historical data for the temperatures in Cambridge, UK
 */
@SuppressWarnings("serial")
public class SunshineChartDemo extends AbstractDemo {
    private static final Logger logger = Logger.getLogger(SunshineChartDemo.class.getName());
    private JPanel demoPanel;
    private static final DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
    private static QuadraticFitter fitter = QuadraticFitter.getInstance();
    private SelectableChartModel[] selectableModels = new SelectableChartModel[12];
    private static WeatherData weatherData;
    private JToggleButton button = new JToggleButton("Show Trends");
    // See www.metoffice.gov.uk
    private JLabel acknowledgement = new JLabel("<html><small>Data courtesy of The UK Meteorological Office</small></html>", JLabel.RIGHT);
    private Chart chart;
    private TimeRange xRange;

    public SunshineChartDemo() {

    }

    public JPanel createDemo() {
        weatherData = WeatherData.getInstance();
        demoPanel = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        demoPanel.setLayout(layout);
        chart = new Chart("Cambridge Temperatures");
        chart.setAnimateOnShow(false);
        Legend legend = new Legend(chart, 4);
        legend.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        JLabel legendLabel = new JLabel("Legend", JLabel.CENTER);
        legendLabel.setForeground(Color.blue);
        legend.setTitleLabel(legendLabel);
        try {
            Date from = dateFormat.parse("01-Jan-1958");
            Date to = dateFormat.parse("31-Dec-2006");
            xRange = new TimeRange(from, to);
            Axis xAxis = new TimeAxis(xRange);
            xAxis.setLabel("Year");
            Axis yAxis = new Axis(new NumericRange(0, 2000));
            yAxis.setLabel("Sunshine (hrs)");
            chart.setXAxis(xAxis);
            chart.setYAxis(yAxis);
            chart.setTitle(new AutoPositionedLabel("Sunshine in Cambridge", Color.blue));

            demoPanel.add(chart, new GridBagConstraints(1, 1, 2, 1, 1.0, 1.0, CENTER, BOTH, new Insets(0, 0, 0, 0), 0, 0));
            demoPanel.add(button, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, WEST, NONE, new Insets(0, 0, 0, 0), 0, 0));
            demoPanel.add(acknowledgement, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, EAST, NONE, new Insets(0, 0, 0, 10), 0, 0));
            demoPanel.add(legend, new GridBagConstraints(1, 3, 2, 1, 1.0, 0.0, CENTER, HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

            int m = 0;
            AnnotatedChartModel[] trends = new AnnotatedChartModel[12];
            for (Month month : Month.values()) {
                DefaultChartModel model = weatherData.getSunshineModel(month);
                Polynomial curve = fitter.performRegression(model);
                List<Double> xPoints = new ArrayList<Double>();
                for (Chartable p : model) {
                    xPoints.add(p.getX().position());
                }
                trends[m] = fitter.createModel(model.getName(), curve, xPoints.toArray(new Double[]{}));
                SelectableChartModel selectableModel = new SelectableChartModel(model, trends[m]);
                selectableModels[m] = selectableModel;
                ChartStyle style = new ChartStyle(month.getColor(), false, false);
                style.setBarsVisible(true);
                style.setBarColor(month.getColor());
                style.setBarWidth(null);
                chart.addModel(selectableModels[m], style);
                m++;
            }
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int modelIndex = button.isSelected() ? 1 : 0;
                    if (selectableModels != null) {
                        for (SelectableChartModel pm : selectableModels) {
                            pm.setModelIndex(modelIndex);
                        }
                    }
                }
            });

            // Add zooming and panning. Here, zooming is on the horizontal axis only
            chart.addMouseZoomer().addMousePanner();
            chart.setBarRenderer(new CylinderBarRenderer());
            chart.setBarGap(1);
            demoPanel.setPreferredSize(new Dimension(600, 500));
        }
        catch (Exception e) {
            logger.severe("Exception while creating chart: " + e.getMessage());
            e.printStackTrace();
        }
        return demoPanel;
    }

    @Override
    public String getDescription() {
        return "This is a chart of some historical weather data collected in Cambridge UK. " +
                "It shows the sunshine recorded in each of the calendar months for years ranging from 1959 to 2006." +
                "You can zoom in and out of this graph by using the mouse wheel, and pan by clicking and dragging.";
    }

    public Component getDemoPanel() {
        if (demoPanel == null) {
            demoPanel = createDemo();
        }
        return demoPanel;
    }

    public String getName() {
        return "Cambridge Sunshine Chart";
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
        showAsFrame(new SunshineChartDemo());
    }

}
