/*
 * @(#)ChartPreferencePanel.java 8/22/2009
 *
 * 2002 - 2010 JIDE Software Inc. All rights reserved.
 * Copyright 2005 - 2010 Catalysoft Ltd. All rights reserved.
 */


import com.jidesoft.chart.Chart;
import com.jidesoft.chart.PointShape;
import com.jidesoft.chart.model.AnnotatedChartModel;
import com.jidesoft.chart.preference.LineWidthChooser;
import com.jidesoft.chart.preference.PointShapeChooser;
import com.jidesoft.chart.preference.PointSizeChooser;
import com.jidesoft.chart.style.ChartStyle;
import com.jidesoft.combobox.ColorComboBox;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.logging.Logger;

public class ChartPreferencePanel extends JPanel {
    private static final long serialVersionUID = 9135707890202580896L;
    private static final Logger logger = Logger.getLogger(ChartPreferencePanel.class.getName());
    private static Dimension controlSize = new Dimension(40, 20);
    private JLabel pointSizeLabel = new JLabel("Points: ");
    private PointSizeChooser pointSizeChooser = new PointSizeChooser();
    private PointShapeChooser pointShapeChooser = new PointShapeChooser();
    private JLabel lineWidthLabel = new JLabel("Lines: ");
    private LineWidthChooser lineWidthChooser = new LineWidthChooser();
    private Chart chart;
    private AnnotatedChartModel model;
    private ColorComboBox lineColorsChooser = createColorComboBox();
    private ColorComboBox pointColorsChooser = createColorComboBox();
    private JCheckBox usePoints = new JCheckBox();
    private JCheckBox useLines = new JCheckBox();

    public ChartPreferencePanel(Chart newChart, AnnotatedChartModel newModel) {
        chart = newChart;
        model = newModel;
        final GridBagLayout gridBagLayout = new GridBagLayout();
        setLayout(gridBagLayout);

        final GridBagConstraints gridBagConstraints_0 = new GridBagConstraints();
        gridBagConstraints_0.gridx = 0;
        gridBagConstraints_0.gridy = 0;
        gridBagConstraints_0.insets = new Insets(5, 10, 5, 2);
        add(usePoints, gridBagConstraints_0);

        final GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(5, 10, 5, 2);

        add(pointSizeLabel, gridBagConstraints);
        final GridBagConstraints gridBagConstraints_1 = new GridBagConstraints();
        gridBagConstraints_1.gridx = 2;
        gridBagConstraints_1.gridy = 0;
        gridBagConstraints_1.insets = new Insets(5, 5, 5, 5);
        pointSizeChooser.setPreferredSize(controlSize);
        add(pointSizeChooser, gridBagConstraints_1);
        final GridBagConstraints gridBagConstraints_3 = new GridBagConstraints();
        gridBagConstraints_3.gridx = 3;
        gridBagConstraints_3.gridy = 0;
        gridBagConstraints_3.insets = new Insets(5, 5, 5, 5);
        add(pointShapeChooser, gridBagConstraints_3);

        final GridBagConstraints gridBagConstraints_8 = new GridBagConstraints();
        gridBagConstraints_8.gridx = 6;
        gridBagConstraints_8.gridy = 0;
        gridBagConstraints_8.insets = new Insets(5, 10, 5, 2);
        add(useLines, gridBagConstraints_8);

        final GridBagConstraints gridBagConstraints_4 = new GridBagConstraints();
        gridBagConstraints_4.gridx = 7;
        gridBagConstraints_4.gridy = 0;
        gridBagConstraints_4.insets = new Insets(5, 10, 5, 2);
        add(lineWidthLabel, gridBagConstraints_4);

        final GridBagConstraints gridBagConstraints_6 = new GridBagConstraints();
        gridBagConstraints_6.insets = new Insets(10, 10, 10, 10);
        gridBagConstraints_6.gridy = 0;
        gridBagConstraints_6.gridx = 5;
        add(pointColorsChooser, gridBagConstraints_6);

        final GridBagConstraints gridBagConstraints_5 = new GridBagConstraints();
        gridBagConstraints_5.gridx = 8;
        gridBagConstraints_5.gridy = 0;
        gridBagConstraints_5.insets = new Insets(5, 5, 5, 5);
        lineWidthChooser.setPreferredSize(controlSize);
        add(lineWidthChooser, gridBagConstraints_5);
        gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};

        final GridBagConstraints gridBagConstraints_7 = new GridBagConstraints();
        gridBagConstraints_7.insets = new Insets(10, 10, 10, 10);
        gridBagConstraints_7.gridy = 0;
        gridBagConstraints_7.gridx = 9;
        add(lineColorsChooser, gridBagConstraints_7);

        pointSizeChooser.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                ChartStyle style = chart.getStyle(model);
                if (style != null) {
                    style.setPointSize(pointSizeChooser.getPointSize());
                    chart.repaint();
                }
            }
        });

        pointShapeChooser.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                ChartStyle style = chart.getStyle(model);
                if (style != null) {
                    style.setPointShape(pointShapeChooser.getPointShape());
                    chart.repaint();
                }
            }
        });

        pointColorsChooser.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                Color selectedColor = pointColorsChooser.getSelectedColor();
                ChartStyle style = chart.getStyle(model);
                if (style != null) {
                    style.setPointColor(selectedColor);
                    chart.repaint();
                }
            }
        });

        lineWidthChooser.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                ChartStyle style = chart.getStyle(model);
                if (style != null) {
                    style.setLineWidth(lineWidthChooser.getLineWidth());
                    chart.repaint();
                }
            }
        });

        lineColorsChooser.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                Color selectedColor = lineColorsChooser.getSelectedColor();
                ChartStyle style = chart.getStyle(model);
                if (style != null) {
                    style.setLineColor(selectedColor);
                    chart.repaint();
                }
            }
        });

        useLines.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                updateLineControls();
            }
        });

        usePoints.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                updatePointControls();
            }
        });
    }

    private void updateLineControls() {
        boolean selected = useLines.isSelected();
        lineWidthChooser.setEnabled(selected);
        lineWidthLabel.setEnabled(selected);
        lineColorsChooser.setEnabled(selected);
        // Annotations not shown if the trace is not visible
        boolean annotationsVisible = useLines.isSelected() || usePoints.isSelected();
        logger.fine("Annotations for " + model.getName() + " visible = " + annotationsVisible);
        model.setAnnotationsVisible(annotationsVisible);
        ChartStyle style = chart.getStyle(model);
        if (style != null) {
            style.setLinesVisible(selected);
        }
        chart.repaint();
    }

    private void updatePointControls() {
        boolean selected = usePoints.isSelected();
        pointColorsChooser.setEnabled(selected);
        pointShapeChooser.setEnabled(selected);
        pointSizeChooser.setEnabled(selected);
        pointSizeLabel.setEnabled(selected);
        // Annotations not shown if the trace is not visible
        boolean annotationsVisible = useLines.isSelected() || usePoints.isSelected();
        logger.fine("Annotations for " + model.getName() + " visible = " + annotationsVisible);
        model.setAnnotationsVisible(annotationsVisible);
        ChartStyle style = chart.getStyle(model);
        if (style != null) {
            style.setPointsVisible(selected);
        }
        chart.repaint();
    }

    private ColorComboBox createColorComboBox() {
        final ColorComboBox colorComboBox = new ColorComboBox();
        colorComboBox.setPreferredSize(controlSize);
        colorComboBox.setSelectedColor(Color.black);
        colorComboBox.setAllowMoreColors(false);
        colorComboBox.setColorValueVisible(false);
        colorComboBox.setAllowDefaultColor(false);
        return colorComboBox;
    }

    public Integer getPointSize() {
        return pointSizeChooser.getPointSize();
    }

    public void setPointSize(int pointSize) {
        pointSizeChooser.setPointSize(pointSize);
    }

    public PointShape getPointShape() {
        return pointShapeChooser.getPointShape();
    }

    public void setPointShape(PointShape shape) {
        pointShapeChooser.setPointShape(shape);
    }

    public void setPointColors(Color pointColor) {
        pointColorsChooser.setSelectedColor(pointColor);
    }

    public Integer getLineWidth() {
        return lineWidthChooser.getLineWidth();
    }

    public void setLineWidth(int lineWidth) {
        lineWidthChooser.setLineWidth(lineWidth);
    }

    public void setLineColors(Color lineColor) {
        lineColorsChooser.setSelectedColor(lineColor);
    }

    public void setUsingPoints(boolean usingPoints) {
        usePoints.setSelected(usingPoints);
        updatePointControls();
    }

    public void setUsingLines(boolean usingLines) {
        useLines.setSelected(usingLines);
        updateLineControls();
    }
}
