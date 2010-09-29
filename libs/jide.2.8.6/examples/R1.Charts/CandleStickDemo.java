/*
 * CandleStickTrial.java 2002 - 2010 JIDE Software Incorporated. All rights
 * reserved. Copyright (c) 2005 - 2010 Catalysoft Limited. All rights reserved.
 */

import com.jidesoft.chart.Chart;
import com.jidesoft.chart.axis.NumericAxis;
import com.jidesoft.chart.axis.TimeAxis;
import com.jidesoft.chart.model.*;
import com.jidesoft.chart.render.CandlestickChartType;
import com.jidesoft.chart.render.CandlestickPointRenderer;
import com.jidesoft.chart.style.ChartStyle;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.range.NumericRange;
import com.jidesoft.range.TimeRange;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@SuppressWarnings("serial")
public class CandleStickDemo extends AbstractDemo {
    private static DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.US);
    private Color gray = Color.gray;
    private ChartStyle underlyingStyle = new ChartStyle(gray, false, true);
    private Color green = new Color(70, 200, 70, 150);
    private ChartStyle greenStyle = new ChartStyle(green);
    private Color red = new Color(200, 80, 80, 150);
    private ChartStyle redStyle = new ChartStyle(red);
    private JCheckBox underlyingModelCheckbox;
    private CandlestickPointRenderer candlestickRenderer = new CandlestickPointRenderer();
    private JPanel panel;
    private JPanel buttonPanel;
    private JPanel optionsPanel;
    private Chart chart;
    private NumericAxis yAxis;
    private Date from, to;
    private TimeRange timeRange;
    private TimeAxis xAxis;
    private Highlight upHighlight = new Highlight("up");
    private Highlight downHighlight = new Highlight("down");
    private ChartModel underlyingModel;

    public JPanel getDemoPanel() {
        chart = new Chart();
        yAxis = new NumericAxis(new NumericRange(900, 1010), "Price ($)");
        chart.setYAxis(yAxis);
        
        try {
            from = createTime("09-Sep-2009 06:00:00");
            to = createTime("09-Sep-2009 10:30:00");
        }
        catch (ParseException pe) {
            pe.printStackTrace();
        }
        timeRange = new TimeRange(from, to);
        xAxis = new TimeAxis(timeRange, "Time");
        chart.setXAxis(xAxis);
        panel = new JPanel(new BorderLayout());
        panel.add(chart, BorderLayout.CENTER);
        JButton button = new JButton("Generate");
        buttonPanel = new JPanel();
        panel.add(buttonPanel, BorderLayout.NORTH);
        buttonPanel.add(button);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addModels();
                chart.startAnimation();
            }
        });
        underlyingModelCheckbox = new JCheckBox("Show Underlying Trace", false);
       
        chart.setHighlightStyle(upHighlight, greenStyle);
        chart.setHighlightStyle(downHighlight, redStyle);
        chart.setPointRenderer(candlestickRenderer);
        chart.setRolloverEnabled(true);
        chart.addMouseMotionListener(new MouseMotionListener() {
            public void mouseMoved(MouseEvent e) {
                checkTooltip();
            }

            public void mouseDragged(MouseEvent e) {
                checkTooltip();
            }

            private void checkTooltip() {
                ChartPointOHLC cp = (ChartPointOHLC) chart.getCurrentChartPoint();
                if (cp != null) {
                    StringBuilder builder = new StringBuilder("<html><table>");
                    builder.append(String.format("<tr><td><b>Open</b></td><td>$%.2f</td></tr>", cp.getOpen()));
                    builder.append(String.format("<tr><td><b>High</b></td><td>$%.2f</td></tr>", cp.getHigh()));
                    builder.append(String.format("<tr><td><b>Low</b></td><td>$%.2f</td></tr>", cp.getLow()));
                    builder.append(String.format("<tr><td><b>Close</b></td><td>$%.2f</td></tr>", cp.getClose()));
                    builder.append("</table></html>");
                    chart.setToolTipText(builder.toString());
                }
                else {
                    chart.setToolTipText(null);
                }
            }
        });
        panel.setPreferredSize(new Dimension(600, 500));
        addModels();
        chart.startAnimation();
        return panel;
    }
    

    @Override
    public Component getOptionsPanel() {
        if (optionsPanel == null) {
            optionsPanel = new JPanel();
            BoxLayout optionsPanelLayout = new BoxLayout(optionsPanel, BoxLayout.Y_AXIS);
            optionsPanel.setLayout(optionsPanelLayout);
            JPanel stylePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            ButtonGroup styleGroup = new ButtonGroup();
            final JRadioButton candleStickButton = new JRadioButton("Candlestick");
            final JRadioButton lineButton = new JRadioButton("Line");
            styleGroup.add(candleStickButton);
            styleGroup.add(lineButton);
            stylePanel.add(candleStickButton);
            stylePanel.add(lineButton);
            optionsPanel.add(stylePanel);
            ActionListener rendererListener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (lineButton.isSelected()) {
                        candlestickRenderer.setCandlestickType(CandlestickChartType.LINE);
                    } else {
                        candlestickRenderer.setCandlestickType(CandlestickChartType.BAR);
                    }
                    chart.repaint();
                }
            };
            candleStickButton.addActionListener(rendererListener);
            lineButton.addActionListener(rendererListener);
            candleStickButton.setSelected(true);
            JPanel underlyingModelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            underlyingModelPanel.add(underlyingModelCheckbox);
            optionsPanel.add(underlyingModelPanel);
            underlyingModelCheckbox.setHorizontalAlignment(JCheckBox.LEFT);
            underlyingModelCheckbox.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (underlyingModelCheckbox.isSelected()) {
                        chart.addModel(underlyingModel, underlyingStyle);
                    } else {
                        chart.removeModel(underlyingModel);
                    }
                }
            });
            GridBagLayout layout = new GridBagLayout();
            JPanel opacityPanel = new JPanel(layout);
            final JSlider slider1 = new JSlider(0, 100, 50);
            slider1.setPreferredSize(new Dimension(50, 20));
            final JSlider slider2 = new JSlider(0, 100, 50);
            slider2.setPreferredSize(new Dimension(50, 20));
            slider1.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    int opacity = (int) (slider1.getValue() * 255.0 / 100);
                    Color g = new Color(green.getRed(), green.getGreen(), green.getBlue(), opacity);
                    greenStyle.setPointColor(g);
                    Color r = new Color(red.getRed(), red.getGreen(), red.getBlue(), opacity);
                    redStyle.setPointColor(r);
                    Color b = new Color(Color.black.getRed(), Color.black.getGreen(), Color.black.getBlue(), opacity);
                    candlestickRenderer.setOutlineColor(b);
                    chart.repaint();
                }
            });
            slider2.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    int opacity = (int) (slider2.getValue() * 255.0 / 100);
                    Color g = new Color(gray.getRed(), gray.getGreen(), gray.getBlue(), opacity);
                    underlyingStyle.setLineColor(g);
                    chart.repaint();
                }
            });
            GridBagConstraints c1 = new GridBagConstraints(0, 0, 1, 1, 0.0, 1.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0,0,0,0), 0, 0);
            opacityPanel.add(new JLabel("Candlestick:"), c1);
            GridBagConstraints c2 = new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0), 0, 0);
            opacityPanel.add(slider1, c2);
            GridBagConstraints c3 = new GridBagConstraints(0, 1, 1, 1, 0.0, 1.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0,0,0,0), 0, 0);
            opacityPanel.add(new JLabel("Trace:"), c3);
            GridBagConstraints c4 = new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0), 0, 0);
            opacityPanel.add(slider2, c4);
            opacityPanel.setBorder(new TitledBorder("Opacity"));
            optionsPanel.add(opacityPanel);
        }
        return optionsPanel;
    }



    private void addModels() {
        chart.removeModels();
        ChartStyle style = new ChartStyle(Color.black, true, false);
        underlyingModel = createModel(from, to);
        if (underlyingModelCheckbox.isSelected()) {
            chart.addModel(underlyingModel, underlyingStyle);
        }
        chart.addModel(createSummaryModel(from, to, underlyingModel), style);
    }

    private ChartModel createSummaryModel(Date from, Date to, ChartModel detailModel) {
        long minute = 60000;
        DefaultChartModel model = new DefaultChartModel("Summary");
        Long periodStart = null;
        double openingY = 0;
        double closingY = 0;
        double minY = Double.MAX_VALUE;
        double maxY = Double.MIN_VALUE;
        // for (long t = from.getTime(); t < to.getTime(); t += 10 * minute) {
        for (int i = 0; i < detailModel.getPointCount(); i++) {
            Chartable u = detailModel.getPoint(i);
            long t = (long) u.getX().position();
            double y = u.getY().position();
            if (periodStart == null) {
                periodStart = t;
                openingY = y;
            }
            if (y > maxY) {
                maxY = y;
            }
            if (y < minY) {
                minY = y;
            }
            if (t - periodStart >= 10 * minute || i == detailModel.getPointCount() - 1) {
                closingY = y;
                // logger.info(String.format("Opening = %.2f Closing = %.2f Min = %.2f Max = %.2f",
                // openingY, closingY, minY, maxY));
                double periodX = (periodStart + t) / 2.0; // the mid point on the x axis
                ChartPointOHLC ohlc = new ChartPointOHLC((long) periodX, 300000.0, openingY, maxY, minY, closingY);
                ohlc.setHighlight(closingY < openingY ? downHighlight : upHighlight);
                model.addPoint(ohlc);
                // Reset for a new period
                periodStart = t;
                openingY = y;
                minY = y;
                maxY = y;
            }
        }
        return model;
    }

    private ChartModel createModel(Date from, Date to) {
        long second = 1000;
        double y = 950;
        double walkStepSize = 7.5;
        DefaultChartModel model = new DefaultChartModel();
        for (long t = from.getTime(); t < to.getTime(); t += 10 * second) {
            ChartPoint p = new ChartPoint(t, y);
            model.addPoint(p);
            y += Math.random() * walkStepSize - walkStepSize / 2.0;
            if (y < 900)
                y = 900;
            if (y > 1000)
                y = 1000;
        }
        return model;
    }

    public String getName() {
        return "Candlestick Chart";
    }

    @Override
    public String getDemoFolder() {
        return "R1.Charts";
    }

    @Override
    public int getAttributes() {
        return ATTRIBUTE_UPDATED;
    }

    public String getProduct() {
        return PRODUCT_NAME_CHARTS;
    }

    public static Date createTime(String timeString) throws ParseException {
        return dateFormat.parse(timeString);
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new CandleStickDemo());
    }

}
