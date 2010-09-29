/*
 * @(#)ConfigurableBarChartDemo.java 8/22/2009
 *
 * 2002 - 2010 JIDE Software Inc. All rights reserved.
 * Copyright 2005 - 2010 Catalysoft Ltd. All rights reserved.
 */

import com.jidesoft.chart.Chart;
import com.jidesoft.chart.ChartColor;
import static com.jidesoft.chart.ChartColor.*;
import com.jidesoft.chart.annotation.AutoPositionedLabel;
import com.jidesoft.chart.axis.Axis;
import com.jidesoft.chart.axis.CategoryAxis;
import com.jidesoft.chart.model.ChartCategory;
import com.jidesoft.chart.model.ChartPoint;
import com.jidesoft.chart.model.Chartable;
import com.jidesoft.chart.model.DefaultChartModel;
import com.jidesoft.chart.render.*;
import com.jidesoft.chart.style.ChartStyle;
import com.jidesoft.chart.util.ChartUtils;
import com.jidesoft.icons.JideIconsFactory;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.range.CategoryRange;
import com.jidesoft.range.NumericRange;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class ConfigurableBarChartDemo extends AbstractDemo {
    private JPanel demoPanel;
    private JPanel controlPanel;
    private Chart chart;
    private JLabel titleLabel = new JLabel(getName(), SwingConstants.CENTER);

    public ConfigurableBarChartDemo() {
    }

    private JPanel createDemo() {
        chart = new Chart();
        demoPanel = new JPanel();
        demoPanel.setPreferredSize(new Dimension(500, 500));
        demoPanel.setLayout(new BorderLayout());
        DefaultChartModel model = new DefaultChartModel();
        // An alternative is to create categories from strings
        ChartCategory<ChartColor> cRed = new ChartCategory<ChartColor>(red);
        ChartCategory<ChartColor> cOrange = new ChartCategory<ChartColor>(orange);
        ChartCategory<ChartColor> cYellow = new ChartCategory<ChartColor>(yellow);
        ChartCategory<ChartColor> cGreen = new ChartCategory<ChartColor>(green);
        // This shows how you can customize the display string of a category
        ChartCategory<ChartColor> cCyan = new ChartCategory<ChartColor>("blue", cyan);
        ChartCategory<ChartColor> cIndigo = new ChartCategory<ChartColor>(indigo);
        ChartCategory<ChartColor> cViolet = new ChartCategory<ChartColor>(violet);
        CategoryRange<ChartColor> colors = new CategoryRange<ChartColor>().add(cRed).add(cOrange).add(cYellow).add(cGreen).add(cCyan).add(cIndigo).add(cViolet);

        ChartPoint p1 = new ChartPoint(cRed, 3.0, red.getHighlight());
        ChartPoint p2 = new ChartPoint(cOrange, 5.0, orange.getHighlight());
        ChartPoint p3 = new ChartPoint(cYellow, 7.0, yellow.getHighlight());
        ChartPoint p4 = new ChartPoint(cGreen, 10.0, green.getHighlight());
        ChartPoint p5 = new ChartPoint(cCyan, 7.0, cyan.getHighlight());
        ChartPoint p6 = new ChartPoint(cIndigo, 5.0, indigo.getHighlight());
        ChartPoint p7 = new ChartPoint(cViolet, 3.0, violet.getHighlight());
        model.addPoint(p1).addPoint(p2).addPoint(p3).addPoint(p4).addPoint(p5).addPoint(p6).addPoint(p7);

        titleLabel.setForeground(Color.blue);
        demoPanel.add(titleLabel, BorderLayout.NORTH);
        chart.addModel(model);
        //chart.setStyle(model, new ChartStyle(Color.cyan, PointShape.SQUARE));
        chart.setVerticalGridLinesVisible(false);
        chart.setShadowVisible(true);
        chart.setChartBackground(new GradientPaint(0f, 0f, Color.lightGray, 0f, 300f, Color.gray));

        Axis xAxis = new CategoryAxis<ChartColor>(colors);
        xAxis.setLabel(new AutoPositionedLabel("Colors"));
        chart.setXAxis(xAxis);
        Axis yAxis = new Axis(new NumericRange(0, 11));
        yAxis.setLabel(new AutoPositionedLabel("Frequency"));
        chart.setYAxis(yAxis);
        ChartStyle style = new ChartStyle(Color.cyan, false, false);
        style.setBarsVisible(true);
        style.setPointsVisible(false);
        chart.setStyle(model, style);

        useColorHighlights();
        demoPanel.add(chart, BorderLayout.CENTER);
        return demoPanel;
    }

    private void useColorHighlights() {
        // Highlights are semantic tags. Here we specify what they actually mean
        chart.setHighlightStyle(red.getHighlight(), new ChartStyle(red.getColor()));
        chart.setHighlightStyle(orange.getHighlight(), new ChartStyle(orange.getColor()));
        chart.setHighlightStyle(yellow.getHighlight(), new ChartStyle(yellow.getColor()));
        chart.setHighlightStyle(green.getHighlight(), new ChartStyle(green.getColor()));
        chart.setHighlightStyle(cyan.getHighlight(), new ChartStyle(cyan.getColor()));
        chart.setHighlightStyle(indigo.getHighlight(), new ChartStyle(indigo.getColor()));
        chart.setHighlightStyle(violet.getHighlight(), new ChartStyle(violet.getColor()));
    }

    private void useTextureHighlights() {
        // Highlights are semantic tags. Here we specify what they actually mean
        chart.setHighlightStyle(red.getHighlight(), createTextureStyle("TextureRed.png"));
        chart.setHighlightStyle(orange.getHighlight(), createTextureStyle("TextureOrange.png"));
        chart.setHighlightStyle(yellow.getHighlight(), createTextureStyle("TextureYellow.png"));
        chart.setHighlightStyle(green.getHighlight(), createTextureStyle("TextureGreen.png"));
        chart.setHighlightStyle(cyan.getHighlight(), createTextureStyle("TextureBlue.png"));
        chart.setHighlightStyle(indigo.getHighlight(), createTextureStyle("TextureIndigo.png"));
        chart.setHighlightStyle(violet.getHighlight(), createTextureStyle("TextureViolet.png"));
    }

    private ChartStyle createTextureStyle(String fileName) {
        Paint p = ChartUtils.createTexture(chart, fileName);
        ChartStyle style = new ChartStyle();
        style.setBarPaint(p);
        return style;
    }

    public String description() {
        return "This shows how a <code>ChartModel</code> can be displayed as a bar chart. " +
                "You can change the renderer used for the bars " +
                "Notice also that in this example we have a categorical x axis. ";
    }

    public String getName() {
        return "Bar Chart";
    }

    @Override
    public String getDemoFolder() {
        return "R1.Charts";
    }

    class ControlPanel extends JPanel {
        private JRadioButton flat = new JRadioButton("Flat");
        private JRadioButton raised = new JRadioButton("Raised");
        private JRadioButton cylinder = new JRadioButton("Cylinder");
        private JRadioButton bar3d = new JRadioButton("3D");
        private JRadioButton textured = new JRadioButton("Textured");
        private JCheckBox rolloverCheckbox = new JCheckBox("Rollover");
        private JCheckBox selectionCheckbox = new JCheckBox("Selection");
        private JCheckBox doubleClickCheckbox = new JCheckBox("Double Click");
        private int gapBetweenBars = 20;
        private BarRenderer flatRenderer = new DefaultBarRenderer();
        private BarRenderer raisedRenderer = new RaisedBarRenderer();
        private BarRenderer cylinderRenderer = new CylinderBarRenderer();
        private BarRenderer bar3dRenderer = new Bar3DRenderer();
        private AxisRenderer noAxisrenderer = new NoAxisRenderer();
        private AxisRenderer axis3dRenderer = new Axis3DRenderer();

        public ControlPanel() {
            setLayout(new GridLayout(0, 1));
            add(new JLabel("Display options:"));
            add(flat);
            add(raised);
            add(bar3d);
            add(cylinder);
            add(textured);
            add(new JLabel("Interaction Options:"));
            add(rolloverCheckbox);
            rolloverCheckbox.setToolTipText("Highlights a bar when the cursor is over it");
            add(selectionCheckbox);
            selectionCheckbox.setToolTipText("Enables you to select bars of interest");
            add(doubleClickCheckbox);
            doubleClickCheckbox.setToolTipText("Do something in response to a double-click on a bar");
            add(Box.createVerticalStrut(10));
            JLabel acknowledgement = new JLabel("<html><small>Sample Textures by www.spiralgraphics.biz</small></html>");
            add(acknowledgement);
            flat.setSelected(true);
            ButtonGroup group = new ButtonGroup();
            group.add(flat);
            group.add(raised);
            group.add(cylinder);
            group.add(bar3d);
            group.add(textured);
            chart.setBarRenderer(flatRenderer);
            chart.setBarGap(gapBetweenBars);
            ActionListener listener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Object source = e.getSource();
                    BarRenderer barRenderer;
                    AxisRenderer axisRenderer;
                    if (source == flat) {
                        barRenderer = flatRenderer;
                        axisRenderer = noAxisrenderer;
                        useColorHighlights();
                    }
                    else if (source == raised) {
                        barRenderer = raisedRenderer;
                        axisRenderer = noAxisrenderer;
                        useColorHighlights();
                    }
                    else if (source == cylinder) {
                        barRenderer = cylinderRenderer;
                        axisRenderer = axis3dRenderer;
                        useColorHighlights();
                    }
                    else if (source == bar3d) {
                        axisRenderer = axis3dRenderer;
                        barRenderer = bar3dRenderer;
                        useColorHighlights();
                    }
                    else {
                        barRenderer = flatRenderer;
                        axisRenderer = noAxisrenderer;
                        useTextureHighlights();
                    }
                    chart.setBarRenderer(barRenderer);
                    chart.getXAxis().setAxisRenderer(axisRenderer);
                    chart.startAnimation();
                    chart.update();
                    demoPanel.revalidate();
                    demoPanel.repaint();
                }
            };
            flat.addActionListener(listener);
            raised.addActionListener(listener);
            cylinder.addActionListener(listener);
            bar3d.addActionListener(listener);
            textured.addActionListener(listener);

            rolloverCheckbox.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    chart.setRolloverEnabled(rolloverCheckbox.isSelected());
                }
            });

            selectionCheckbox.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    chart.setSelectionEnabled(selectionCheckbox.isSelected());
                }
            });
            final ActionListener doubleClickListener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Chartable cp = chart.getCurrentChartPoint();
                    if (cp != null) {
                        ChartCategory<?> category = (ChartCategory<?>) cp.getX();
                        String message = String.format("You double-clicked on %s", category.getName());
                        JOptionPane.showMessageDialog(chart, message, "Double Click", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            };
            doubleClickCheckbox.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (doubleClickCheckbox.isSelected()) {
                        chart.addDoubleClickListener(doubleClickListener);
                    }
                    else {
                        chart.removeDoubleClickListener(doubleClickListener);
                    }
                }
            });
        }
    }


    @Override
    public Component getOptionsPanel() {
        if (controlPanel == null) {
            controlPanel = new ControlPanel();
        }
        return controlPanel;
    }

    public Component getDemoPanel() {
        if (demoPanel == null) {
            // You only see the effect of this on the Mac - it replaces the default Java Icon on the popup
            UIManager.put("OptionPane.informationIcon", JideIconsFactory.getImageIcon(JideIconsFactory.JIDE50));
            demoPanel = createDemo();
        }
        return demoPanel;
    }

    public String getProduct() {
        return PRODUCT_NAME_CHARTS;
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new ConfigurableBarChartDemo());
    }

}
