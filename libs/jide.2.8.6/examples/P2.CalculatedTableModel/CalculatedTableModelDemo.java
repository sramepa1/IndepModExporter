/*
 * @(#)CalculatedTableModelDemo.java 7/14/2006
 *
 * Copyright 2002 - 2006 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.comparator.ObjectComparatorManager;
import com.jidesoft.converter.ConverterContext;
import com.jidesoft.converter.DoubleConverter;
import com.jidesoft.converter.ObjectConverterManager;
import com.jidesoft.grid.*;
import com.jidesoft.grouper.AbstractObjectGrouper;
import com.jidesoft.grouper.date.*;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Vector;
import java.util.zip.GZIPInputStream;

/**
 * Demoed Component: {@link com.jidesoft.grid.SortableTable} <br> Required jar files: jide-common.jar, jide-grids.jar
 * <br> Required L&F: any L&F
 */
public class CalculatedTableModelDemo extends AbstractDemo {
    public CachedTableModel _cachedTableModel;
    private static final long serialVersionUID = 1025588992093208356L;

    public CalculatedTableModelDemo() {
    }

    public String getName() {
        return "CalculatedTableModel Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_PIVOT;
    }

    @Override
    public String getDescription() {
        return "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.grid.CalculatedTableModel";
    }

    public Component getDemoPanel() {
        final TableModel tableModel = createProductReportsTableModel();
        if (tableModel == null) {
            return new JLabel("Failed to read data file");
        }

        JPanel panel1 = createSortableTable(tableModel);
        JPanel panel2 = createCalculatedTable(tableModel);

        JideSplitPane pane = new JideSplitPane();
        pane.add(panel1);
        pane.add(panel2, JideBoxLayout.VARY);

        return pane;
    }

    private JPanel createCalculatedTable(TableModel tableModel) {
        JPanel panel = new JPanel(new BorderLayout(2, 2));
        panel.setName("CalculatedTable");
        panel.setBorder(BorderFactory.createCompoundBorder(new JideTitledBorder(new PartialEtchedBorder(PartialEtchedBorder.LOWERED, PartialSide.NORTH), "CalculatedTableModel", JideTitledBorder.LEADING, JideTitledBorder.ABOVE_TOP),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)));
        CalculatedTableModel calculatedTableModel = new CalculatedTableModel(tableModel);
        calculatedTableModel.addColumn(new SingleColumn(tableModel, 0));
        calculatedTableModel.addColumn(new SingleColumn(tableModel, 1));
        calculatedTableModel.addColumn(new SingleColumn(tableModel, 2));
        SingleColumn column = new SingleColumn(tableModel, 2, "Tag");
        column.setObjectGrouper(new AbstractObjectGrouper() {
            public String getName() {
                return "Tag";
            }

            public Class getType() {
                return String.class;
            }

            public Object getValue(Object value) {
                if (value instanceof Float) {
                    float sales = (Float) value;
                    if (sales < 100) {
                        return "S";
                    }
                    else if (sales < 1000) {
                        return "M";
                    }
                    else if (sales < 10000) {
                        return "L";
                    }
                    else if (sales < 100000) {
                        return "Super";
                    }
                }
                return null;
            }
        });
        calculatedTableModel.addColumn(column);
        calculatedTableModel.addColumn(new SingleColumn(tableModel, 3));
        calculatedTableModel.addColumn(new SingleColumn(tableModel, 3, "Year", new DateYearGrouper()));
        calculatedTableModel.addColumn(new SingleColumn(tableModel, 3, "Qtr", new DateQuarterGrouper()));
        calculatedTableModel.addColumn(new SingleColumn(tableModel, 3, "Month", new DateMonthGrouper()));
        calculatedTableModel.addColumn(new SingleColumn(tableModel, 3, "Week", new DateWeekOfYearGrouper()));
        calculatedTableModel.addColumn(new SingleColumn(tableModel, 3, "Day of Year", new DateDayOfYearGrouper()));
        calculatedTableModel.addColumn(new SingleColumn(tableModel, 3, "Day of Month", new DateDayOfMonthGrouper()));
        try {
            ExpressionCalculatedColumn longName = new ExpressionCalculatedColumn(tableModel, "Long Name", "LENGTH([ProductName]) > 4");
            longName.setDependingColumns(new int[]{1});
            calculatedTableModel.addColumn(longName);
        }
        catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        calculatedTableModel.addColumn(new AbstractCalculatedColumn(tableModel, "Amount", Float.class) {
            private static final long serialVersionUID = 2262942915907503179L;

            public Object getValueAt(int rowIndex) {
                Object valueAt = getActualModel().getValueAt(rowIndex, 2);
                if (valueAt instanceof Float) {
                    return (Float) valueAt * 2;
                }
                return "--";
            }

            public int[] getDependingColumns() {
                return new int[]{2};
            }
        });

        _cachedTableModel = new CachedTableModel(calculatedTableModel);
        _cachedTableModel.setCacheEnabled(true);
        SortableTable derivedTable = new SortableTable(_cachedTableModel);
        JScrollPane scrollPane = new JScrollPane(derivedTable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        panel.add(scrollPane);
        return panel;
    }

/*
    private void timingIt(CachedTableModel model) {
        long start = System.currentTimeMillis();
        for (int row = 0; row < model.getRowCount(); row++) {
            for (int col = 0; col < model.getColumnCount(); col++) {
                model.getValueAt(row, col);
            }
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }
*/

    private JPanel createSortableTable(TableModel tableModel) {
        JPanel panel = new JPanel(new BorderLayout(2, 2));
        panel.setName("Original TableModel");
        panel.setBorder(BorderFactory.createCompoundBorder(new JideTitledBorder(new PartialEtchedBorder(PartialEtchedBorder.LOWERED, PartialSide.NORTH), "SortableTableModel", JideTitledBorder.LEADING, JideTitledBorder.ABOVE_TOP),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)));
        SortableTable sortableTable = new SortableTable(tableModel);
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

            Vector<Vector<Object>> data = new Vector<Vector<Object>>();
            Vector<String> columnNames = new Vector<String>();

            String columnsLine = reader.readLine(); // skip first line
            String[] columnValues = columnsLine.split("\t");
            columnNames.addAll(Arrays.asList(columnValues));
            do {
                String line = reader.readLine();
                if (line == null || line.length() == 0) {
                    break;
                }
                String[] values = line.split("\t");
                Vector<Object> lineData = new Vector<Object>();
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
            return new ContextDefaultTableModel(data, columnNames) {
                private static final long serialVersionUID = 7616491558372401902L;

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
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    static class ContextDefaultTableModel extends DefaultTableModel implements ContextSensitiveTableModel {
        private static final long serialVersionUID = 6149838651825255860L;

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

    }

    @Override
    public String getDemoFolder() {
        return "P2.CalculatedTableModel";
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
        showAsFrame(new CalculatedTableModelDemo());
    }
}
