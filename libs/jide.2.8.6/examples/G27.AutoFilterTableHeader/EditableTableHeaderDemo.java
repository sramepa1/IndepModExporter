/*
 * @(#)EditableTableHeaderDemo.java 2/14/2007
 *
 * Copyright 2002 - 2007 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.comparator.ObjectComparatorManager;
import com.jidesoft.converter.ConverterContext;
import com.jidesoft.converter.DoubleConverter;
import com.jidesoft.converter.ObjectConverterManager;
import com.jidesoft.grid.*;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.JideTitledBorder;
import com.jidesoft.swing.PartialEtchedBorder;
import com.jidesoft.swing.PartialSide;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.util.zip.GZIPInputStream;

/**
 * Demoed Component: {@link com.jidesoft.grid.EditableTableHeader} <br> Required jar files:
 * jide-common.jar, jide-grids.jar <br> Required L&F: any L&F
 */
public class EditableTableHeaderDemo extends AbstractDemo {
    public EditableTableHeaderDemo() {
    }

    public String getName() {
        return "EditableTableHeader Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_GRIDS;
    }

    @Override
    public String getDescription() {
        return "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.grid.EditableTableHeader";
    }

    public Component getDemoPanel() {
        final TableModel tableModel = createProductReportsTableModel();
        if (tableModel == null) {
            return new JLabel("Failed to read data file");
        }

        return createSortableTable(tableModel);
    }

    private JPanel createSortableTable(TableModel tableModel) {
        JPanel panel = new JPanel(new BorderLayout(2, 2));
        panel.setBorder(BorderFactory.createCompoundBorder(new JideTitledBorder(new PartialEtchedBorder(PartialEtchedBorder.LOWERED, PartialSide.NORTH), "ProductName and ShippedDate columns are editable; single click to start editing", JideTitledBorder.LEADING, JideTitledBorder.ABOVE_TOP),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)));
        JTable sortableTable = new CellStyleTable(tableModel);
        EditableTableHeader header = new EditableTableHeader(sortableTable.getColumnModel());
        sortableTable.setTableHeader(header);
        JScrollPane scrollPane = new JScrollPane(sortableTable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        panel.add(scrollPane);
        return panel;
    }

    private TableModel createProductReportsTableModel() {
        try {
            InputStream resource = this.getClass().getClassLoader().getResourceAsStream("ProductReports.txt.gz");
            if (resource == null) {
                return null;
            }

            InputStream in = new GZIPInputStream(resource);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));

            Vector data = new Vector();
            Vector columnNames = new Vector();

            String columnsLine = reader.readLine(); // skip first line
            String[] columnValues = columnsLine.split("\t");
            for (String name : columnValues) {
                columnNames.add(name);
            }
            do {
                String line = reader.readLine();
                if (line == null || line.length() == 0) {
                    break;
                }
                String[] values = line.split("\t");
                Vector lineData = new Vector();
                if (values.length < 1)
                    lineData.add(null); // category name
                else
                    lineData.add(values[0]); // category  name
                if (values.length < 2)
                    lineData.add(null); // product name
                else
                    lineData.add(values[1]); // productname
                if (values.length < 3)
                    lineData.add(null); // product sale
                else {
                    String value = values[2];
                    if (value.startsWith("$")) {
                        float f = Float.parseFloat(value.substring(1));
                        lineData.add(f); // product amount
                    }
                }
                if (values.length < 3)
                    lineData.add(null); // ship date
                else {
                    String value = values[3];
                    try {
                        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
                        Date date = format.parse(value);
                        lineData.add(date); // order date
                    }
                    catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                for (int i = 0; i < 1; i++) {
                    data.add(lineData);
                }
            }
            while (true);
            DefaultTableModel tableModel = new EditableTableHeaderDemo.ContextDefaultTableModel(data, columnNames) {
                @Override
                public Class<?> getColumnClass(int columnIndex) {
                    switch (columnIndex) {
                        case 0:
                        case 1:
                            return String.class;
                        case 2:
                            return float.class;
                        case 3:
                            return Date.class;
                    }
                    return super.getColumnClass(columnIndex);
                }

                @Override
                public boolean isCellEditable(int row, int column) {
                    return true;
                }

                @Override
                public Class<?> getCellClassAt(int row, int column) {
                    return getColumnClass(column);
                }
            };
            return tableModel;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    static class ContextDefaultTableModel extends DefaultTableModel implements ContextSensitiveTableModel, EditableColumnTableModel {
        public ContextDefaultTableModel() {
        }

        public ContextDefaultTableModel(int rowCount, int columnCount) {
            super(rowCount, columnCount);
        }

        public ContextDefaultTableModel(Vector columnNames, int rowCount) {
            super(columnNames, rowCount);
        }

        public ContextDefaultTableModel(Object[] columnNames, int rowCount) {
            super(columnNames, rowCount);
        }

        public ContextDefaultTableModel(Vector data, Vector columnNames) {
            super(data, columnNames);
        }

        public ContextDefaultTableModel(Object[][] data, Object[] columnNames) {
            super(data, columnNames);
        }

        public ConverterContext getConverterContextAt(int row, int column) {
            return null;
        }

        public EditorContext getEditorContextAt(int row, int column) {
            return null;
        }

        public Class<?> getCellClassAt(int row, int column) {
            return getColumnClass(column);
        }


        public boolean isColumnHeaderEditable(int columnIndex) {
            return columnIndex == 1 || columnIndex == 3;
        }

        public TableCellEditor getColumnHeaderCellEditor(int columnIndex) {
            return null;
        }
    }

    @Override
    public String getDemoFolder() {
        return "G27.AutoFilterTableHeader";
    }

    static public void main(String[] s) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        CellEditorManager.initDefaultEditor();
        CellRendererManager.initDefaultRenderer();
        ObjectConverterManager.initDefaultConverter();
        ObjectComparatorManager.initDefaultComparator();
        DoubleConverter converter = new DoubleConverter();
        NumberFormat numberInstance = NumberFormat.getNumberInstance();
        numberInstance.setMinimumFractionDigits(2);
        converter.setNumberFormat(numberInstance);
        ObjectConverterManager.registerConverter(Double.class, converter);
        showAsFrame(new EditableTableHeaderDemo());
    }
}
