/*
 * @(#)TableAdapterDemo.java
 * 
 * 2002 - 2010 JIDE Software Incorporated. All rights reserved.
 * Copyright (c) 2005 - 2010 Catalysoft Limited. All rights reserved.
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import com.jidesoft.chart.Chart;
import com.jidesoft.chart.axis.Axis;
import com.jidesoft.chart.axis.CategoryAxis;
import com.jidesoft.chart.axis.NumericAxis;
import com.jidesoft.chart.model.TableToChartAdapter;
import com.jidesoft.chart.render.SphericalPointRenderer;
import com.jidesoft.chart.style.ChartStyle;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.range.CategoryRange;
import com.jidesoft.range.NumericRange;
import com.jidesoft.range.Range;

import static java.awt.GridBagConstraints.*;

/**
 * A demo that shows how a chart can be created dynamically based on selections from a table
 */
@SuppressWarnings("serial")
public class TableAdapterDemo extends AbstractDemo {
    private JPanel demoPanel, optionsPanel;
    private Chart chart;
    private JScrollPane scrollPane;
    private JTable table;
    private DefaultTableModel tableModel;
    private DefaultComboBoxModel xColumns, yColumns;
    private TableToChartAdapter adapter;
    private TableCellRenderer headerRenderer;
    private Axis xAxis, yAxis;
    private boolean useRowSelection = false;

    public Component getDemoPanel() {
        if (demoPanel == null) {
            demoPanel = new JPanel(new BorderLayout());
            chart = new Chart();
            chart.setPointRenderer(new SphericalPointRenderer());
            chart.setShadowVisible(true);
            chart.addMousePanner().addMouseZoomer();
            chart.setChartBackground(new GradientPaint(0f, 0f, Color.white, 0f, 500f, new Color(180, 180, 250)));
            chart.setPreferredSize(new Dimension(400, 300));
            demoPanel.add(chart, BorderLayout.CENTER);
            String[] columns = {"Column 1", "Column 2", "Column 3", "Column 4", "Column 5"};
            Object[][] data = new Object[][] {
                    {1,2,3.0,9, "eggs"}, {2,4,6.5,7, "bacon"}, {3,6,8.0,5, "sausage"}, 
                    {4,8,6.5,3,"mushrooms"}, {5,10,3.0, 1, "toast"}};
            tableModel = new DefaultTableModel(data, columns);
            table = new JTable(tableModel);
            table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
            headerRenderer = table.getTableHeader().getDefaultRenderer();
            table.getTableHeader().setDefaultRenderer(new XYTableHeaderRenderer());
            ListSelectionModel rowSelection = table.getSelectionModel();
            rowSelection.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    updateFromRowSelection();
                }
            });
            scrollPane = new JScrollPane(table);
            scrollPane.setPreferredSize(new Dimension(400, 120));
            demoPanel.add(scrollPane, BorderLayout.SOUTH);
            adapter = new TableToChartAdapter("Adapter", tableModel);
            xAxis = new NumericAxis(0, 16);
            yAxis = new NumericAxis(0, 16);
            chart.setXAxis(xAxis);
            chart.setYAxis(yAxis);
            ChartStyle style = new ChartStyle(new Color(200, 50, 220, 200));
            style.setPointsVisible(true);
            style.setLinesVisible(true);
            style.setPointSize(15);
            style.setLineWidth(3);
            chart.addModel(adapter, style);
        }
        return demoPanel;
    }
    
    /**
     * Depending on the selection mode, update the row interval in the adapter and update
     * the axis ranges
     */
    private void updateFromRowSelection() {
        Integer minRow = null;
        Integer maxRow = null;
        if (useRowSelection) {
            int[] selectedRows = table.getSelectedRows();
            if (selectedRows.length > 0) {
                Arrays.sort(selectedRows);
                minRow = selectedRows[0];
                maxRow = selectedRows[selectedRows.length-1];
            } else {
                // Select no entries by making one of the indices -1
                minRow = 0;
                maxRow = -1;
            }
        }
        adapter.setRowInterval(minRow, maxRow);
        // The selection has changed, which means the points have changed so we
        // need to update the x and y axis too
        updateXRange();
        updateYRange();
    }

    @Override
    public Component getOptionsPanel() {
        if (optionsPanel == null) {
            GridBagLayout layout = new GridBagLayout();
            optionsPanel = new JPanel(layout);
            JLabel xLabel = new JLabel("X", JLabel.RIGHT);
            JLabel yLabel = new JLabel("Y", JLabel.RIGHT);
            xColumns = new DefaultComboBoxModel();
            yColumns = new DefaultComboBoxModel();
            getDemoPanel(); // Make sure the demoPanel has been created
            for (int i=0; i<tableModel.getColumnCount(); i++) {
                xColumns.addElement(tableModel.getColumnName(i));
                yColumns.addElement(tableModel.getColumnName(i));
            }
            DefaultListCellRenderer renderer = new DefaultListCellRenderer();
            renderer.setHorizontalAlignment(JLabel.CENTER);
            final JComboBox xChooser = new JComboBox(xColumns);
            xChooser.setRenderer(renderer);
            final JComboBox yChooser = new JComboBox(yColumns);
            yChooser.setRenderer(renderer);
            GridBagConstraints ll = new GridBagConstraints(0, 0, 2, 1, 1.0, 0, WEST, HORIZONTAL, new Insets(5, 1, 5, 10), 0, 0);
            GridBagConstraints xl = new GridBagConstraints(0, 1, 1, 1, 0, 0, EAST, NONE, new Insets(5,10,5,10), 0, 0);
            GridBagConstraints yl = new GridBagConstraints(0, 2, 1, 1, 0, 0, EAST, NONE, new Insets(5,10,5,10), 0, 0);
            GridBagConstraints xc = new GridBagConstraints(1, 1, 1, 1, 0, 0, WEST, NONE, new Insets(5,0,5,0), 0, 0);
            GridBagConstraints yc = new GridBagConstraints(1, 2, 1, 1, 0, 0, WEST, NONE, new Insets(5,0,5,0), 0, 0);
            GridBagConstraints sl = new GridBagConstraints(0, 3, 1, 2, 0, 0, WEST, NONE, new Insets(5,1,5,10), 0, 0); 
            GridBagConstraints yb = new GridBagConstraints(1, 3, 1, 1, 0, 0, WEST, NONE, new Insets(5,10,1,10), 0, 0);
            GridBagConstraints nb = new GridBagConstraints(1, 4, 1, 1, 0, 0, WEST, NONE, new Insets(1,10,5,10), 0, 0);
            optionsPanel.add(new JLabel("Choose values to use for x and y:", JLabel.LEFT), ll);
            optionsPanel.add(xLabel, xl);
            optionsPanel.add(yLabel, yl);
            optionsPanel.add(xChooser, xc);
            optionsPanel.add(yChooser, yc);
            optionsPanel.add(new JLabel("Show Points From:", JLabel.LEFT), sl);
            JRadioButton selectedRowsButton = new JRadioButton("Selected Rows Only");
            JRadioButton allRowsButton = new JRadioButton("All Rows");
            ButtonGroup buttonGroup = new ButtonGroup();
            buttonGroup.add(selectedRowsButton);
            buttonGroup.add(allRowsButton);
            allRowsButton.setSelected(true);
            optionsPanel.add(selectedRowsButton, yb);
            optionsPanel.add(allRowsButton, nb);
            selectedRowsButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                   useRowSelection = true;
                   updateFromRowSelection();
                }
            });
            allRowsButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                   useRowSelection = false;
                   updateFromRowSelection();
                }
            });
            xChooser.setSelectedIndex(adapter.getXColumn());
            yChooser.setSelectedIndex(adapter.getYColumn());
            xChooser.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    adapter.setXColumn(xChooser.getSelectedIndex());
                    updateXRange();
                    xAxis.setLabel(xChooser.getSelectedItem().toString());
                    chart.startAnimation();
                }
            });
            yChooser.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    adapter.setYColumn(yChooser.getSelectedIndex());
                    updateYRange();
                    yAxis.setLabel(yChooser.getSelectedItem().toString());
                    chart.startAnimation();
                }
            });
            xChooser.setSelectedIndex(0);
            yChooser.setSelectedIndex(1);
        }
        return optionsPanel;
    }
    
    /**
     * Update the x axis with the range from the table model adapter
     */
    @SuppressWarnings("unchecked")
    private void updateXRange() {
        Range<?> xRange = adapter.getXRange();
        if (xRange instanceof NumericRange) {
            NumericRange nRange = (NumericRange) xRange;
            if (nRange.getMin() == nRange.getMax()) {
                // Deal with the special case of only one point
                xAxis = new NumericAxis(nRange.getMin()-1, nRange.getMax()+1);
            } else {
                xAxis = new NumericAxis(nRange.stretch(1.2));
            }
            chart.setXAxis(xAxis);
        } else {
            xAxis = new CategoryAxis((CategoryRange)xRange);
            chart.setXAxis(xAxis);
        }
    }
    
    /**
     * Update the y axis with the range from the table model adapter
     */
    @SuppressWarnings("unchecked")
    private void updateYRange() {
        Range<?> yRange = adapter.getYRange();
        if (yRange instanceof NumericRange) {
            NumericRange nRange = (NumericRange) yRange;
            if (nRange.getMin() == nRange.getMax()) {
                // Deal with the special case of only one point
                yAxis = new NumericAxis(nRange.getMin()-1, nRange.getMax()+1);
            } else {
                yAxis = new NumericAxis(((NumericRange) yRange).stretch(1.2));
            }
            chart.setYAxis(yAxis);
        } else {
            yAxis = new CategoryAxis((CategoryRange)yRange);
            chart.setYAxis(yAxis);
        }
    }

    /**
     * A header renderer that shows which of the columns in the table are being shown in the x and y axes
     */
    private class XYTableHeaderRenderer extends DefaultTableCellRenderer {
        public XYTableHeaderRenderer() {
            setHorizontalAlignment(JLabel.CENTER);
        }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            JLabel label = (JLabel) headerRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            int xColumn = adapter.getXColumn();
            int yColumn = adapter.getYColumn();
            String text;
            if (column == xColumn && column == yColumn) {
                text = value + " [x,y]";
            } else if (column == xColumn) {
                text = value + " [x]";
            } else if (column == yColumn) {
                text = value + " [y]";
            } else {
                text = value.toString();
            }
            label.setText(text);
            return label;
        }
        
    }
    

    @Override
    public int getAttributes() {
        return ATTRIBUTE_NEW;
    }

    public String getName() {
        return "Table Adapter Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_CHARTS;
    }
    
    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new TableAdapterDemo());
    }

}
