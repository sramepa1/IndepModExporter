/*
 * @(#)FastGradientPaintDemo.java 2/14/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.combobox.ColorChooserPanel;
import com.jidesoft.combobox.ColorComboBox;
import com.jidesoft.pane.CollapsiblePane;
import com.jidesoft.pane.CollapsiblePanes;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.JideSwingUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;

/**
 * Demoed Component: {@link com.jidesoft.swing.JideSwingUtilities#fillGradient(java.awt.Graphics2D,java.awt.Shape,java.awt.Color,java.awt.Color,boolean)}
 * <br> Required jar files: jide-common.jar, jide-components.jar, jide-grids.jar <br> Required L&F:
 * Jide L&F extension required
 */
public class FastGradientPaintDemo extends AbstractDemo {
    private CollapsiblePanes _pane;

    public FastGradientPaintDemo() {
    }

    public String getName() {
        return "FastGradientPane Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_COMMON;
    }

    @Override
    public String getDescription() {
        return "FastGradientPaint is a faster gradient paint. The demo is designed to show you how fast it is by comparing it with regular GradientPaint.\n" +
                "\nMoving mouse over the gradient paint above to see the actual Java code being used.\n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.swing.JideSwingUtilities#fillGradient";
    }

    @Override
    public String getDemoFolder() {
        return "C5.FastGradientPaint";
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new FastGradientPaintDemo());
    }

    public Component getDemoPanel() {
        _pane = new CollapsiblePanes();
        PaintPanel panel1 = new PaintPanel("Fast Gradient Paint", 0);
        PaintPanel panel2 = new PaintPanel("Normal Gradient Paint", 1);
        PaintPanel panel3 = new PaintPanel("Single Color Paint (for comparison purpose)", 2);
        _pane.add(panel1);
        _pane.add(panel2);
        _pane.add(panel3);
        _pane.addExpansion();
        return _pane;
    }

    class PaintPanel extends CollapsiblePane {
        public PaintPanel(String title, int type) {
            super(title);
            GradientPanel gradientPanel = new GradientPanel(type);
            getContentPane().setLayout(new BorderLayout(6, 6));
            getContentPane().add(new ResultPanel(gradientPanel), BorderLayout.BEFORE_FIRST_LINE);
            getContentPane().add(gradientPanel, BorderLayout.CENTER);
            getContentPane().setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        }
    }

    class ResultPanel extends JPanel {
        protected JLabel _resultLabel;
        GradientPanel _gradientPanel;

        public ResultPanel(GradientPanel gradientPanel) {
            _gradientPanel = gradientPanel;
            setLayout(new BorderLayout());
            JButton button = new JButton("Repaint " + _repeat + " times");
            add(button, BorderLayout.AFTER_LINE_ENDS);
            button.addActionListener(new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    NumberFormat format = NumberFormat.getInstance();
                    format.setMaximumFractionDigits(2);
// uncomment this if-else statement if you are using JDK1.5 so that you can leverage the new nanoTime function to get a more acurate result.
//                    if (SystemInfo.isJdk15Above()) {
//                        ArrayList results = new ArrayList(_repeat);
//                        double gap = 0;
//                        for (int i = 0; i < _repeat; i++) {
//                            long start = System.nanoTime();
//                            _gradientPanel.paintImmediately(0, 0, _gradientPanel.getWidth(), _gradientPanel.getHeight());
//                            long end = System.nanoTime();
//                            long stepGap = end - start;
//                            results.add(new Long(stepGap));
//                            gap += stepGap;
//                        }
//                        if (gap > 1000000) {
//                            gap /= 1000000.0;
//                            _resultLabel.setText("<HTML>Repainting " + _repeat + " times took <FONT COLOR=RED>" + format.format(gap) + "</FONT> millisecond</HTML>");
//                        }
//                        else if (gap > 1000) {
//                            gap /= 1000.0;
//                            _resultLabel.setText("<HTML>Repainting " + _repeat + " times took <FONT COLOR=RED>" + format.format(gap) + "</FONT> microsecond</HTML>");
//                        }
//                        else {
//                            _resultLabel.setText("<HTML>Repainting " + _repeat + " times took <FONT COLOR=RED>" + format.format(gap) + "</FONT> nanosecond</HTML>");
//                        }
//                    }
//                    else {
                    long gap = 0;
                    long start = System.currentTimeMillis();
                    for (int i = 0; i < _repeat; i++) {
                        _gradientPanel.paintImmediately(0, 0, _gradientPanel.getWidth(), _gradientPanel.getHeight());
                    }
                    long end = System.currentTimeMillis();
                    gap = end - start;
                    _resultLabel.setText("<HTML>Repainting " + _repeat + " times took <FONT COLOR=RED>" + format.format(gap) + "</FONT> millisecond</HTML>");
//                    }
                }
            });
            _resultLabel = new JLabel("");
            add(_resultLabel, BorderLayout.CENTER);
            setOpaque(false);
        }
    }

    class GradientPanel extends JPanel {
        int type;

        public GradientPanel(int type) {
            this.type = type;
            if (type == 0) {
                setToolTipText("<HTML><B>Code used:</B> <BR>&nbsp;&nbsp;" +
                        "Rectangle rect = ...;<BR>&nbsp;&nbsp;" +
                        "JideSwingUtilities.fillGradient(g2d, rect, startColor, endColor, <FONT COLOR=BLUE>false</FONT>);" +
                        "</HTML>");
            }
            else if (type == 1) {
                setToolTipText("<HTML><B>Code used:</B> <BR>&nbsp;&nbsp;" +
                        "Rectangle rect = ...;<BR>&nbsp;&nbsp;" +
                        "GradientPaint paint = <FONT COLOR=BLUE>new</FONT> GradientPaint(rect.<FONT COLOR=PURPLE>x</FONT>, rect.<FONT COLOR=PURPLE>y</FONT>, startColor, rect.<FONT COLOR=PURPLE>width </FONT> + rect.<FONT COLOR=PURPLE>x</FONT>, rect.<FONT COLOR=PURPLE>y</FONT>, endColor, <FONT COLOR=BLUE>true</FONT>);<BR>&nbsp;&nbsp;" +
                        "g2d.setPaint(paint);<BR>&nbsp;&nbsp;" +
                        "g2d.fill(rect);" +
                        "</HTML>");
            }
            else if (type == 2) {
                setToolTipText("<HTML><B>Code used:</B> <BR>&nbsp;&nbsp;" +
                        "Rectangle rect = ...;<BR>&nbsp;&nbsp;" +
                        "g.setColor(startColor);<BR>&nbsp;&nbsp;" +
                        "g.fill(rect.<FONT COLOR=PURPLE>x</FONT>, rect.<FONT COLOR=PURPLE>y</FONT>, rect.<FONT COLOR=PURPLE>width</FONT>, rect.<FONT COLOR=PURPLE>height</FONT>);" +
                        "</HTML>");
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            Rectangle rect = new Rectangle(0, 0, getWidth(), getHeight());
            switch (type) {
                case 0:
                    JideSwingUtilities.fillGradient((Graphics2D) g, rect, _startColor, _endColor, _vertical);
                    break;
                case 1:
                    JideSwingUtilities.fillNormalGradient((Graphics2D) g, rect, _startColor, _endColor, _vertical);
                    break;
                case 2:
                    g.setColor(_startColor);
                    g.fillRect(rect.x, rect.y, rect.width, rect.height);
                    break;
            }
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(500, 200);
        }

        @Override
        public Point getToolTipLocation(MouseEvent event) {
            return new Point(4, 4);
        }
    }

    static Color _startColor = Color.BLUE;
    static Color _endColor = Color.YELLOW;
    static boolean _vertical = false;
    static int _repeat = 100;

    @Override
    public Component getOptionsPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 1));

        JCheckBox checkbox = new JCheckBox("Vertical");
        checkbox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    _vertical = true;
                }
                else {
                    _vertical = false;
                }
                _pane.repaint();
            }
        });
        checkbox.setOpaque(false);
        panel.add(checkbox);

        panel.add(new JLabel("Start Color: "));
        ColorComboBox startColorComboBox = new ColorComboBox(ColorChooserPanel.PALETTE_COLOR_40);
        startColorComboBox.setSelectedColor(_startColor);
        startColorComboBox.setAllowDefaultColor(false);
        startColorComboBox.setAllowMoreColors(false);
        startColorComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    _startColor = (Color) e.getItem();
                    if (_startColor == null) {
                        _startColor = Color.BLACK;
                    }
                    _pane.repaint();
                }
            }
        });
        panel.add(startColorComboBox);
        panel.add(new JLabel("End Color: "));
        ColorComboBox endColorComboBox = new ColorComboBox(ColorChooserPanel.PALETTE_COLOR_40);
        endColorComboBox.setAllowDefaultColor(false);
        endColorComboBox.setSelectedColor(_endColor);
        endColorComboBox.setAllowMoreColors(false);
        endColorComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    _endColor = (Color) e.getItem();
                    if (_endColor == null) {
                        _endColor = Color.WHITE;
                    }
                    _pane.repaint();
                }
            }
        });
        panel.add(endColorComboBox);

        panel.setOpaque(false);
        return panel;
    }
}
