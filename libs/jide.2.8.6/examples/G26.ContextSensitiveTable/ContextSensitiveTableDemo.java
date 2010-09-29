/*
 * @(#)ContextSensitiveTableDemo.java 1/7/2007
 *
 * Copyright 2002 - 2007 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.combobox.ColorComboBox;
import com.jidesoft.converter.ColorConverter;
import com.jidesoft.converter.ConverterContext;
import com.jidesoft.converter.ObjectConverterManager;
import com.jidesoft.grid.*;
import com.jidesoft.plaf.LookAndFeelFactory;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;

/**
 * Demoed Component: {@link com.jidesoft.grid.ContextSensitiveTable} <br> Required jar files:
 * jide-common.jar, jide-grids.jar <br> Required L&F: any L&F
 */
public class ContextSensitiveTableDemo extends AbstractDemo {
    public ContextSensitiveTable _table;
    public JLabel _message;

    public ContextSensitiveTableDemo() {
    }

    public String getName() {
        return "ContextSensitiveTable Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_GRIDS;
    }

    @Override
    public String getDescription() {
        return "This is a demo of ContextSensitiveTable." +
                "In this demo, we create a simple ContextSensitiveTable which only filled in the value in the first column. " +
                "In this demo, you will see how we use different converter, cell renderer and cell editor for the same data type." +
                "Please read DemoTableModel class in the source code to learn how it works." +
                "\n\n" +
                "Demoed classes:\n" +
                "com.jidesoft.grid.ContextSensitiveTable";
    }

    class DemoTableModel extends AbstractTableModel implements ContextSensitiveTableModel {
        private Color color = new Color(0, 128, 255);
        private boolean bool = false;

        @Override
        public String getColumnName(int column) {
            if (column == 0) {
                return "Color";
            }
            else if (column == 1) {
                return "Boolean";
            }
            return super.getColumnName(column);
        }

        public int getRowCount() {
            return 5;
        }

        public int getColumnCount() {
            return 2;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            if (columnIndex == 0) {
                return color;
            }
            else if (columnIndex == 1) {
                return bool ? Boolean.TRUE : Boolean.FALSE;
            }
            return null;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return true;
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            if (columnIndex == 0) {
                color = (Color) aValue;
            }
            else if (columnIndex == 1) {
                bool = Boolean.TRUE.equals(aValue);
            }
            for (int i = 0; i < getRowCount(); i++) {
                fireTableCellUpdated(i, columnIndex);
            }
        }

        //              ConverterContext           EditorContext                   Result
        // For color column
        // the 1st row      null                    null                Default renderer and editor for Color is used
        // the 2nd row      null                    "StringOnly"        Only string value is displayed
        // the 3rd row      null                    "IconOnly"          Only color icon is displayed
        // the 4th row      CONTEXT_HEX             "StringOnly"        Only string value is displayed, and it's in hex color format.
        // the 5th row      CONTEXT_HEX             null                Default renderer and editor is used except the string valie is in hex color format.
        //
        // For boolean column
        // the 1st row      null                    null                Default renderer and editor for Boolean is used
        // the 2nd row      YesNo                   null                Yes/No is used instead of True/False
        // the 3rd row      null                    "CheckBox"          CheckBox is used instead of JComboBox
        // the 4th row      same as the 1st row
        // the 5th row      same as the 1st row

        public ConverterContext getConverterContextAt(int row, int column) {
            if (column == 0) {
                switch (row) {
                    case 3:
                    case 4:
                        return ColorConverter.CONTEXT_HEX;
                }
            }
            else if (column == 1) {
                switch (row) {
                    case 1:
                        return YesNoConverter.CONTEXT;
                }
            }
            return null;
        }

        public EditorContext getEditorContextAt(int row, int column) {
            if (column == 0) {
                switch (row) {
                    case 0:
                    case 4:
                        return null;
                    case 1:
                        return new EditorContext("StringOnly");
                    case 2:
                        return new EditorContext("IconOnly");
                    case 3:
                        return new EditorContext("StringOnly");
                }
            }
            else if (column == 1) {
                switch (row) {
                    case 2:
                        return BooleanCheckBoxCellEditor.CONTEXT;
                }
            }
            return null;
        }

        @Override
        public Class<?> getColumnClass(int column) {
            if (column == 0) {
                return Color.class;
            }
            else if (column == 1) {
                return Boolean.class;
            }
            return null;
        }

        public Class<?> getCellClassAt(int row, int column) {
            if (column == 0) {
                return Color.class;
            }
            else if (column == 1) {
                return Boolean.class;
            }
            return null;
        }
    }

    public Component getDemoPanel() {

        // how is how we setup ObjectConverterManager/CellRendererManager/CellEditorManager
        ObjectConverterManager.initDefaultConverter();
        CellRendererManager.initDefaultRenderer();
        CellEditorManager.initDefaultEditor();

        // for color

        // There are two converters - RgbColorConverter and HexColorConverter. They are registered already in ObjectConverterManager.initDefaultConverter().

        CellRendererManager.registerRenderer(Color.class, new ContextSensitiveCellRenderer(), new EditorContext("StringOnly"));
        CellEditorManager.registerEditor(Color.class, new CellEditorFactory() {
            public CellEditor create() {
                ColorCellEditor editor = new ColorCellEditor() {
                    @Override
                    protected ColorComboBox createColorComboBox() {
                        ColorComboBox colorComboBox = super.createColorComboBox();
                        colorComboBox.setColorIconVisible(false);
                        return colorComboBox;
                    }
                };
                return editor;
            }
        }, new EditorContext("StringOnly"));


        CellRendererManager.registerRenderer(Color.class, new ColorCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (component instanceof ColorComboBox) {
                    ((ColorComboBox) component).setColorValueVisible(false);
                }
                return component;
            }
        }, new EditorContext("IconOnly"));

        CellEditorManager.registerEditor(Color.class, new CellEditorFactory() {
            public CellEditor create() {
                ColorCellEditor editor = new ColorCellEditor() {
                    @Override
                    protected ColorComboBox createColorComboBox() {
                        ColorComboBox colorComboBox = super.createColorComboBox();
                        colorComboBox.setColorValueVisible(false);
                        return colorComboBox;
                    }
                };
                return editor;
            }
        }, new EditorContext("IconOnly"));

        // for boolean
        ObjectConverterManager.registerConverter(Boolean.class, new YesNoConverter(), YesNoConverter.CONTEXT);

        // There are two cell renderers and cell editors for Boolean. One uses JComboBox. The other uses JCheckBox.
        // They are registered already in CellRendererManager.initDefaultRenderer() and CellEditorManager.initDefaultEditor()

        JPanel panel = new JPanel(new BorderLayout(6, 6));
        _table = new ContextSensitiveTable(new DemoTableModel());

        panel.add(new JScrollPane(_table), BorderLayout.CENTER);
        return panel;
    }

    @Override
    public String getDemoFolder() {
        return "G26.ContextSensitiveTable";
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new ContextSensitiveTableDemo());
    }
}
