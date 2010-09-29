/*
 * @(#)TemperaturesChartDemo.java 8/22/2009
 *
 * 2002 - 2010 JIDE Software Inc. All rights reserved.
 * Copyright 2005 - 2010 Catalysoft Ltd. All rights reserved.
 */

import com.jidesoft.chart.Chart;
import com.jidesoft.chart.Legend;
import com.jidesoft.chart.annotation.AutoPositionedLabel;
import com.jidesoft.chart.axis.Axis;
import com.jidesoft.chart.axis.TimeAxis;
import com.jidesoft.chart.fit.LineFitter;
import com.jidesoft.chart.fit.Polynomial;
import com.jidesoft.chart.model.AnnotatedChartModel;
import com.jidesoft.chart.model.DefaultChartModel;
import com.jidesoft.chart.model.SelectableChartModel;
import com.jidesoft.chart.style.ChartStyle;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.range.NumericRange;
import com.jidesoft.range.TimeRange;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import static java.awt.GridBagConstraints.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Logger;

/**
 * An example that shows some historical data for the temperatures in Cambridge, UK
 */
@SuppressWarnings("serial")
public class TemperaturesChartDemo extends AbstractDemo {
    private static final Logger logger = Logger.getLogger(TemperaturesChartDemo.class.getName());
    private JPanel demoPanel;
    private static final DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
    private static LineFitter fitter = LineFitter.getInstance();
    private SelectableChartModel[] selectableModels = new SelectableChartModel[12];
    private static WeatherData weatherData;
    private JToggleButton button = new JToggleButton("Show Trends");
    // See www.metoffice.gov.uk
    private JLabel acknowledgement = new JLabel("<html><small>Data courtesy of The UK Meteorological Office</small></html>", JLabel.RIGHT);
    private Chart chart;
    private TimeRange xRange;

    public TemperaturesChartDemo() {

    }

    public JPanel createDemo() {
        weatherData = WeatherData.getInstance();
        demoPanel = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        demoPanel.setLayout(layout);
        chart = new Chart("Cambridge Temperatures");
        Legend legend = new Legend(chart, 4);
        legend.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.green), "Key"));
        try {
            Date from = dateFormat.parse("01-Jan-1959");
            Date to = dateFormat.parse("31-Dec-2006");
            xRange = new TimeRange(from, to);
            Axis xAxis = new TimeAxis(xRange);
            xAxis.setLabel("Year");
            Axis yAxis = new Axis(new NumericRange(0, 30));
            yAxis.setLabel("Max Temp (\u2103)");
            chart.setXAxis(xAxis);
            chart.setYAxis(yAxis);
            chart.setTitle(new AutoPositionedLabel("Temperatures in Cambridge", Color.blue));

            demoPanel.add(chart, new GridBagConstraints(1, 1, 2, 1, 1.0, 1.0, CENTER, BOTH, new Insets(0, 0, 0, 0), 0, 0));
            demoPanel.add(button, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, WEST, NONE, new Insets(0, 0, 0, 0), 0, 0));
            demoPanel.add(acknowledgement, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, EAST, NONE, new Insets(0, 0, 0, 10), 0, 0));
            demoPanel.add(legend, new GridBagConstraints(1, 3, 2, 1, 1.0, 0.0, CENTER, HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
            int m = 0;
            AnnotatedChartModel[] trends = new AnnotatedChartModel[12];
            for (Month month : Month.values()) {
                DefaultChartModel model = weatherData.getTempMaxModel(month);
                Polynomial curve = fitter.performRegression(model);
                trends[m] = fitter.createModel(model.getName(), curve, model.getXRange(), 10);
                SelectableChartModel selectableModel = new SelectableChartModel(model, trends[m]);
                selectableModels[m] = selectableModel;
                chart.addModel(selectableModels[m], new ChartStyle(month.getColor(), false, true));
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
            chart.addMouseZoomer(true, false).addMousePanner();
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
        return "This is a chart of some historical temperature data collected in Cambridge UK. " +
                "It shows the maximum temperature in each of the calendar months for years ranging from 1959 to 2006." +
                " If we are looking for evidence of global warming we would look for an upwards trend from left to right. " +
                "See the trends by clicking on the 'Show Trends' button. " +
                "You can zoom in and out of the graph by using the mouse wheel, and pan by clicking and dragging. ";
    }

    public String getName() {
        return "Cambridge Temperatures Chart";
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
        showAsFrame(new TemperaturesChartDemo());
    }

}
