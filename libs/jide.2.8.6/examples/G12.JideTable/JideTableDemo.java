/*
 * @(#)JideTableDemo.java 2/14/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.grid.*;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.JideSwingUtilities;
import com.jidesoft.validation.ValidationObject;
import com.jidesoft.validation.ValidationResult;
import com.jidesoft.validation.Validator;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;

/**
 * Demoed Component: {@link com.jidesoft.grid.JideTable} <br> Required jar files: jide-common.jar, jide-grids.jar <br>
 * Required L&F: any L&F
 */
public class JideTableDemo extends AbstractDemo {
    public JideTable _table;
    public JLabel _message;
    private static final long serialVersionUID = 2637320232281479121L;

    public JideTableDemo() {
    }

    public String getName() {
        return "JideTable Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_GRIDS;
    }

    @Override
    public String getDescription() {
        return "This is a demo of JideTable. JideTable is an extended version of JTable with the addition of several features.\n" +
                "\nAddition cell editing events: supports editingStarting, editingStarted and editingStopping events.\n" +
                "\nValidation support: Validator can be added to JideTable.\n" +
                "\nVarious row heights: supports row auto resize and row height change event.\n" +
                "\nNested column header: supports multiple-level nested column header.\n" +
                "\nTyping \"invalid\" in a cell, when you click out the cell, validation error will be shown.\n" +
                "\nTyping \"no stop\" in a cell, when you click out the cell, cell won't stop editing.\n" +
                "\nTyping \"no editing\" in a cell, when you click out the cell, you can never edit it again.\n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.grid.JideTable";
    }

    @Override
    public Component getOptionsPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 1, 4, 4));
        final JCheckBox hideOriginalTableHeader = new JCheckBox("Hide the original table header");
        hideOriginalTableHeader.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                JTableHeader header = _table.getTableHeader();
                if (header instanceof NestedTableHeader) {
                    ((NestedTableHeader) header).setOriginalTableHeaderVisible(!hideOriginalTableHeader.isSelected());
                }
            }
        });
        panel.add(hideOriginalTableHeader);

        final JCheckBox autoStartCellEditing = new JCheckBox("Auto-start editing when TAB or ENTER");
        autoStartCellEditing.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _table.setAutoStartCellEditing(autoStartCellEditing.isSelected());
            }
        });
        autoStartCellEditing.setSelected(_table.isAutoStartCellEditing());
        panel.add(autoStartCellEditing);

        final JCheckBox autoSelectTextWhenStartsEditing = new JCheckBox("Auto-select the cell text when editing");
        autoSelectTextWhenStartsEditing.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _table.setAutoSelectTextWhenStartsEditing(autoSelectTextWhenStartsEditing.isSelected());
            }
        });
        autoSelectTextWhenStartsEditing.setSelected(_table.isAutoSelectTextWhenStartsEditing());
        panel.add(autoSelectTextWhenStartsEditing);

        final JCheckBox enable = new JCheckBox("Enable Table");
        enable.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _table.setEnabled(enable.isSelected());
            }
        });
        enable.setSelected(_table.isEnabled());
        panel.add(enable);

        JCheckBox columnAutoResizable = new JCheckBox("Column Auto Resizable");
        columnAutoResizable.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                boolean selected = e.getStateChange() == ItemEvent.SELECTED;
                _table.setColumnAutoResizable(selected);
                _table.setAutoResizeMode(selected ? JTable.AUTO_RESIZE_OFF : JTable.AUTO_RESIZE_ALL_COLUMNS);
            }
        });
        columnAutoResizable.setSelected(_table.isColumnAutoResizable());
        panel.add(columnAutoResizable);

        JComboBox autoCompletionModeComboBox = new JComboBox(new Object[]{"Disabled", "Cells in the same column", "Cells in the same row", "All cells"});
        autoCompletionModeComboBox.setSelectedIndex(_table.getEditorAutoCompletionMode());
        autoCompletionModeComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    _table.setEditorAutoCompletionMode(((JComboBox) e.getSource()).getSelectedIndex());
                }

            }
        });
        panel.add(JideSwingUtilities.createLabeledComponent(new JLabel("AutoCompletion Mode: "), autoCompletionModeComboBox, BorderLayout.BEFORE_LINE_BEGINS));

        return panel;
    }

    public Component getDemoPanel() {
        JPanel panel = new JPanel(new BorderLayout(6, 6));
        DefaultTableModel model = new CustomizedTableModel(8, 8);
        model.setValueAt("no editing", 2, 2);
        model.setValueAt("no stop", 2, 3);
        model.setValueAt("valid", 2, 4);
        _table = new JideTable(model);
        _table.setNestedTableHeader(true);
        _table.setAlwaysRequestFocusForEditor(true);
        _table.setClickCountToStart(2);
        _table.setEditorAutoCompletionMode(JideTable.EDITOR_AUTO_COMPLETION_MODE_TABLE);

        TableColumnGroup all = new TableColumnGroup("AH");
        TableColumnGroup first = new TableColumnGroup("AB");
        first.add(_table.getColumnModel().getColumn(0));
        first.add(_table.getColumnModel().getColumn(1));
        TableColumnGroup second = new TableColumnGroup("CD");
        second.add(_table.getColumnModel().getColumn(2));
        second.add(_table.getColumnModel().getColumn(3));
        TableColumnGroup third = new TableColumnGroup("EF");
        third.add(_table.getColumnModel().getColumn(4));
        third.add(_table.getColumnModel().getColumn(5));
        TableColumnGroup fourth = new TableColumnGroup("GH");
        fourth.add(_table.getColumnModel().getColumn(6));
        fourth.add(_table.getColumnModel().getColumn(7));
        all.add(first);
        all.add(second);
        all.add(third);
        all.add(fourth);

        if (_table.getTableHeader() instanceof NestedTableHeader) {
            NestedTableHeader header = (NestedTableHeader) _table.getTableHeader();
            header.addColumnGroup(all);
        }

        panel.add(new JScrollPane(_table), BorderLayout.CENTER);
        _message = new JLabel();
        panel.add(_message, BorderLayout.AFTER_LAST_LINE);
        _table.addCellEditorListener(new JideCellEditorListener() {
            public boolean editingStarting(ChangeEvent e) {
                int row = ((CellChangeEvent) e).getRow();
                int column = ((CellChangeEvent) e).getColumn();
                if ("no editing".equals(_table.getValueAt(row, column))) {
                    _message.setText("Editing is not started because editingStarting() returns false.");
                    return false;
                }
                else {
                    return true;
                }
            }

            public void editingStarted(ChangeEvent e) {
            }

            public boolean editingStopping(ChangeEvent e) {
                if (e.getSource() instanceof CellEditor) {
                    if ("no stop".equals(((CellEditor) e.getSource()).getCellEditorValue())) {
                        _message.setText("Editing is not stopped because editingStopping() returns false.");
                        return false;
                    }
                }
                return true;
            }

            public void editingCanceled(ChangeEvent e) {
            }

            public void editingStopped(ChangeEvent e) {
            }
        });

        _table.addValidator(new Validator() {
            public ValidationResult validating(ValidationObject e) {
                if ("invalid".equals(e.getNewValue())) {
                    ValidationResult validationResult = new ValidationResult(0, false, "The value is \"invalid\"");
                    _message.setText(validationResult.getMessage());
                    return validationResult;
                }
                else {
                    return ValidationResult.OK;
                }
            }
        });
        _message.setForeground(Color.RED);
        _message.setText("");
        return panel;
    }

    @Override
    public String getDemoFolder() {
        return "G12.JideTable";
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new JideTableDemo());
    }

    class CustomizedTableModel extends DefaultTableModel implements ColumnWidthTableModel {
        private static final long serialVersionUID = 1244030636855477737L;

        public CustomizedTableModel() {
            super();
        }

        public CustomizedTableModel(int rowCount, int columnCount) {
            super(rowCount, columnCount);
        }

        public CustomizedTableModel(Vector columnNames, int rowCount) {
            super(columnNames, rowCount);
        }

        public CustomizedTableModel(Object[] columnNames, int rowCount) {
            super(columnNames, rowCount);
        }

        public CustomizedTableModel(Vector data, Vector columnNames) {
            super(data, columnNames);
        }

        public CustomizedTableModel(Object[][] data, Object[] columnNames) {
            super(data, columnNames);
        }

        public int getMinimumWidth(int column) {
            return -1; // return -1 so that the minimum width will not be configured by this table model
        }

        public int getPreferredWidth(int column) {
            if (column == 1) {
                return 150;
            }
            return -1;
        }

        public int getMaximumWidth(int column) {
            return -1; // return -1 so that the maximum width will not be configured by this table model
        }
    }
}

