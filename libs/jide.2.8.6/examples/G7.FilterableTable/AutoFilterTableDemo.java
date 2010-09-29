/*
 * @(#)AutoFilterTableDemo.java 11/3/2009
 *
 * Copyright 2002 - 2009 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.grid.*;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.JideScrollPane;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Vector;
import java.util.zip.GZIPInputStream;

/**
 * Demoed Component: {@link FilterableTableModel} <br> Required jar files: jide-common.jar, jide-grids.jar <br> Required
 * L&F: any L&F
 */
public class AutoFilterTableDemo extends AbstractDemo {
    public JCheckBox _booleanFilterCheckBox;
    public JCheckBox _intFilterCheckBox;
    private AutoFilterUtils _autoFilterUtils;
    private static final long serialVersionUID = -6896621999099053205L;

    public AutoFilterTableDemo() {
    }

    public String getName() {
        return "AutoFilter Table Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_GRIDS;
    }

    @Override
    public String getDescription() {
        return "This is a demo of FilterableTableModel. FilterableTableModel is a table model which supports filters on columns. You can add filters so that only data the satisfied the filters will be shown in the table.\n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.grid.FilterableTableModel\n" +
                "com.jidesoft.grid.Filter";
    }

    @Override
    public Component getOptionsPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 1));
        final JCheckBox allowColumn2Filter = new JCheckBox("Allow \"ProductSales\" to Filter");
        allowColumn2Filter.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (allowColumn2Filter.isSelected()) {
                    _autoFilterUtils.setFilterableColumnIdentifiers(null);
                }
                else {
                    _autoFilterUtils.setFilterableColumnIdentifiers(new Object[]{"CategoryName", "ProductName", "ShippedDate"});
                }
            }
        });
        allowColumn2Filter.setSelected(true);
        panel.add(allowColumn2Filter);

        final JCheckBox needAutoCompletion = new JCheckBox("Need Auto Completion");
        needAutoCompletion.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _autoFilterUtils.setIntelliHintsEnabled(needAutoCompletion.isSelected());
            }
        });
        needAutoCompletion.setSelected(_autoFilterUtils.isIntelliHintsEnabled());
        panel.add(needAutoCompletion);
        return panel;
    }

    public Component getDemoPanel() {
        TableModel model = createProductReportsTableModel();

        SortableTable table = new SortableTable(model);
        table.setClearSelectionOnTableDataChanges(false);
        table.setRowResizable(true);
        table.setVariousRowHeights(true);
        table.setSelectInsertedRows(false);

        _autoFilterUtils = new AutoFilterUtils();
        JideScrollPane pane = _autoFilterUtils.install(table);
        table.setNestedTableHeader(true);
        TableColumnGroup all = new TableColumnGroup("All Columns");
        TableColumnGroup first = new TableColumnGroup("Product Information");
        first.add(table.getColumnModel().getColumn(0));
        first.add(table.getColumnModel().getColumn(1));
        TableColumnGroup second = new TableColumnGroup("Sale Information");
        second.add(table.getColumnModel().getColumn(2));
        second.add(table.getColumnModel().getColumn(3));
        all.add(first);
        all.add(second);

        if (table.getTableHeader() instanceof NestedTableHeader) {
            NestedTableHeader header = (NestedTableHeader) table.getTableHeader();
            header.addColumnGroup(all);
        }

        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        return pane;
    }

    @Override
    public String getDemoFolder() {
        return "G7.FilterableTable";
    }

    static public void main(String[] s) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new AutoFilterTableDemo());
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
            columnNames.addAll(Arrays.asList(columnValues));

            do {
                String line = reader.readLine();
                if (line == null || line.length() == 0) {
                    break;
                }
                String[] values = line.split("\t");
                Vector lineData = new Vector();
                lineData.add(values[0]); // category  name
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
            return new ProductTableModel(data, columnNames);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class ProductTableModel extends DefaultContextSensitiveTableModel {
        private static final long serialVersionUID = -7774927734958822379L;

        public ProductTableModel(Vector data, Vector columnNames) {
            super(data, columnNames);
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            switch (columnIndex) {
                case 0:
                case 1:
                    return String.class;
                case 2:
                    return Float.class;
                case 3:
                    return Date.class;
            }
            return super.getColumnClass(columnIndex);
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    }
}