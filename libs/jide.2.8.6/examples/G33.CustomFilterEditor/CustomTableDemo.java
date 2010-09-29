/*
 * @(#)CustomTableDemo.java 9/10/2008
 *
 * Copyright 2002 - 2008 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.*;
import com.jidesoft.grouper.AbstractObjectGrouper;
import com.jidesoft.grouper.date.*;
import com.jidesoft.swing.JideSwingUtilities;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import java.util.zip.GZIPInputStream;

/**
 * Demoed Component: {@link com.jidesoft.pivot.PivotTablePane} <br> Required jar files: jide-common.jar, jide-grids.jar
 * <br> Required L&F: any L&F
 */
abstract public class CustomTableDemo extends AbstractDemo {
    protected TableModel _tableModel;

    public CustomTableDemo() {
    }

    public Component getDemoPanel() {
        final DefaultTableModel tableModel = (DefaultTableModel) createProductReportsTableModel();

        if (tableModel == null) {
            return new JLabel("Failed to read data file");
        }

        JTable table = createTable(tableModel);

        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.add(createEditorPanel(), BorderLayout.BEFORE_FIRST_LINE);
        JPanel tablePanel = new JPanel(new BorderLayout(6, 6));

        tablePanel.add(new JScrollPane(table));

        JButton updateButton = createUpdateButton();
        tablePanel.add(JideSwingUtilities.createCenterPanel(updateButton), BorderLayout.BEFORE_FIRST_LINE);
        panel.add(tablePanel);
        return panel;
    }

    abstract public JTable createTable(DefaultTableModel tableModel);

    abstract public JButton createUpdateButton();

    abstract public Component createEditorPanel();

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
            columnNames.addAll(Arrays.asList(columnValues));
            columnNames.add("WarrantyDate");
            columnNames.add("Shipped");

            int lineIndex = 0;
            do {
                String line = reader.readLine();
                if (line == null || line.length() == 0) {
                    break;
                }

                String[] values = line.split("\t");
                Vector lineData = new Vector();
                lineData.add(lineIndex == 100 ? null : values[0]); // category  name
                lineData.add(values[1]); // product name
                {
                    String value = values[2];
                    if (value.startsWith("$")) {
                        float f = Float.parseFloat(value.substring(1));
                        lineData.add(f); // product amount
                    }
                }
                {
                    String value = values[3];
                    try {
                        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
                        Date date = format.parse(value);
                        lineData.add(/*lineIndex == 400 ? null : */date); // order date
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(date);
                        cal.roll(Calendar.YEAR, 10);
                        lineData.add(cal); // calendar
                    }
                    catch (ParseException e) {
                        e.printStackTrace();
                    }

                    lineData.add(lineIndex % 2 == 0);
                }
                for (int i = 0; i < 1; i++) {
                    data.add(lineData);
                }

                lineIndex++;
            }
            while (true);
            DefaultTableModel tableModel = new ContextDefaultTableModel(data, columnNames) {
                @Override
                public Class getColumnClass(int columnIndex) {
                    switch (columnIndex) {
                        case 0:
                        case 1:
                            return String.class;
                        case 2:
                            return float.class;
                        case 3:
                            return Date.class;
                        case 4:
                            return Calendar.class;
                        case 5:
                            return Boolean.class;
                    }
                    return super.getColumnClass(columnIndex);
                }

                @Override
                public boolean isCellEditable(int row, int column) {
                    return true;
                }
            };
            return tableModel;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected CalculatedTableModel setupProductDetailsCalculatedTableModel(TableModel tableModel) {
        CalculatedTableModel calculatedTableModel = new CalculatedTableModel(tableModel);
        calculatedTableModel.addAllColumns();
        SingleColumn column = new SingleColumn(tableModel, "ProductName");
        column.setObjectGrouper(new AbstractObjectGrouper() {
            public Class getType() {
                return String.class;
            }

            public Object getValue(Object value) {
                if (value != null) {
                    String name = value.toString();
                    return name.substring(0, 1);
                }
                else {
                    return "";
                }
            }

            public String getName() {
                return "Alphabetical";
            }
        });
        calculatedTableModel.addColumn(column);
        calculatedTableModel.addColumn(new SingleColumn(tableModel, "ShippedDate", "Year", new DateYearGrouper()));
        calculatedTableModel.addColumn(new SingleColumn(tableModel, "ShippedDate", "Quarter", new DateQuarterGrouper()));
        calculatedTableModel.addColumn(new SingleColumn(tableModel, "ShippedDate", "Month", new DateMonthGrouper()));
        calculatedTableModel.addColumn(new SingleColumn(tableModel, "ShippedDate", "Week", new DateWeekOfYearGrouper()));
        calculatedTableModel.addColumn(new SingleColumn(tableModel, "ShippedDate", "Day of Year", new DateDayOfYearGrouper()));
        calculatedTableModel.addColumn(new SingleColumn(tableModel, "ShippedDate", "Day of Month", new DateDayOfMonthGrouper()));
        calculatedTableModel.addColumn(new AbstractCalculatedColumn(tableModel, "Sales2", Float.class) {
            public Object getValueAt(int rowIndex) {
                Object valueAt = getActualModel().getValueAt(rowIndex, 2);
                if (valueAt instanceof Float) {
                    return new Float(((Float) valueAt).floatValue() * 2);
                }
                return "--";
            }

            public int[] getDependingColumns() {
                return new int[]{2};
            }
        });

        return calculatedTableModel;
    }

    static class ContextDefaultTableModel extends DefaultTableModel implements ContextSensitiveTableModel {
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

        public Class getCellClassAt(int row, int column) {
            return getColumnClass(column);
        }

    }
}