/*
 * @(#)CalculatorDemo.java 7/11/2006
 *
 * Copyright 2002 - 2006 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.combobox.CalculatorComboBox;
import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.CalculatorCellEditor;
import com.jidesoft.grid.ContextSensitiveTableModel;
import com.jidesoft.grid.EditorContext;
import com.jidesoft.grid.SortableTable;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Demoed Component: {@link com.jidesoft.swing.Calculator} <br> Required jar files: jide-common.jar,
 * jide-grids.jar <br> Required L&F: any L&F
 */
public class CalculatorDemo extends AbstractDemo {

    public CalculatorDemo() {
    }

    public String getName() {
        return "Calculator";
    }

    public String getProduct() {
        return PRODUCT_NAME_COMMON;
    }

    @Override
    public String getDescription() {
        return "This is a demo of Calculator. There are two ways to use Calculator. The first way is to use it " +
                "as a regular panel and you can put it anywhere is your application." +
                "The second way is to use it as a combobox.\n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.swing.Calculator\n" +
                "com.jidesoft.combobox.CalculatorComboBox";
    }

    public Component getDemoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new JideBoxLayout(panel, JideBoxLayout.Y_AXIS, 12));
        panel.add(createCalculatorPanel());
        panel.add(createCalculatorComboBox());
        panel.add(createCalculatorTable(), JideBoxLayout.VARY);
        return panel;
    }

    private JComponent createCalculatorPanel() {
        JPanel panel = new JPanel(new BorderLayout(6, 6));
        panel.setBorder(BorderFactory.createCompoundBorder(new JideTitledBorder(new PartialEtchedBorder(PartialEtchedBorder.LOWERED, PartialSide.NORTH),
                "TextField with Calculator", JideTitledBorder.LEADING, JideTitledBorder.ABOVE_TOP),
                BorderFactory.createEmptyBorder(6, 0, 6, 0)));
        final JTextField textField = new JTextField();
        SelectAllUtils.install(textField);
        textField.setColumns(20);
        textField.setHorizontalAlignment(JTextField.TRAILING);
        panel.add(JideSwingUtilities.createTopPanel(textField), BorderLayout.CENTER);
        Calculator calculator = new Calculator();
        calculator.registerKeyboardActions(textField, JComponent.WHEN_FOCUSED);
        calculator.addPropertyChangeListener(Calculator.PROPERTY_DISPLAY_TEXT, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                textField.setText("" + evt.getNewValue());
            }
        });
        calculator.clear();
        panel.add(calculator, BorderLayout.AFTER_LINE_ENDS);
        return panel;
    }

    private JComponent createCalculatorComboBox() {
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBorder(BorderFactory.createCompoundBorder(new JideTitledBorder(new PartialEtchedBorder(PartialEtchedBorder.LOWERED, PartialSide.NORTH), "CalculatorComboBox", JideTitledBorder.LEADING, JideTitledBorder.ABOVE_TOP),
                BorderFactory.createEmptyBorder(6, 0, 6, 0)));
        CalculatorComboBox comboBox = new CalculatorComboBox();
        comboBox.getCalculator().clear();
        panel.add(comboBox);
        return panel;
    }

    private JComponent createCalculatorTable() {
        SortableTable table = new SortableTable(createTableModel());
        JPanel panel = new JPanel(new BorderLayout(6, 6));
        panel.setBorder(BorderFactory.createCompoundBorder(new JideTitledBorder(new PartialEtchedBorder(PartialEtchedBorder.LOWERED, PartialSide.NORTH),
                "Using CalculatorComboBox in JTable", JideTitledBorder.LEADING, JideTitledBorder.ABOVE_TOP),
                BorderFactory.createEmptyBorder(6, 0, 6, 0)));
        table.setPreferredScrollableViewportSize(new Dimension(200, 200));
        panel.add(new JScrollPane(table));
        return panel;
    }

    private TableModel createTableModel() {
        return new SampleTableModel();
    }


    @Override
    public String getDemoFolder() {
        return "G24.Calculator";
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new CalculatorDemo());
    }

    private static class SampleTableModel extends DefaultTableModel implements ContextSensitiveTableModel {
        public SampleTableModel() {
            setColumnCount(2);
            addRow(new Object[]{"Computer", 799.99});
            addRow(new Object[]{"Movie", 10.8});
            addRow(new Object[]{"Dining", 112.8});
            addRow(new Object[]{"Cloth", 102.4});
        }

        public ConverterContext getConverterContextAt(int row, int column) {
            return null;
        }

        public EditorContext getEditorContextAt(int row, int column) {
            if (column == 1) {
                return CalculatorCellEditor.CONTEXT;
            }
            else {
                return null;
            }
        }

        public Class<?> getCellClassAt(int row, int column) {
            return getColumnClass(column);
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 0) {
                return String.class;
            }
            else /*if(columnIndex == 1)*/ {
                return Double.class;
            }
        }

        @Override
        public String getColumnName(int column) {
            if (column == 0) {
                return "Category";
            }
            else /*if(columnIndex == 1)*/ {
                return "Price";
            }
        }
    }
}
