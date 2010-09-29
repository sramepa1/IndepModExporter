/*
 * @(#)LuceneQuickFilterTableDemo.java 4/13/2009
 *
 * Copyright 2002 - 2009 JIDE Software Inc. All rights reserved.
 *
 */

import com.jidesoft.grid.*;
import com.jidesoft.lucene.LuceneFilterableTableModel;
import com.jidesoft.lucene.LuceneQuickTableFilterField;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.JideTitledBorder;
import com.jidesoft.swing.PartialEtchedBorder;
import com.jidesoft.swing.PartialSide;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
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
 * Demoed Component: {@link com.jidesoft.lucene.LuceneFilterableTableModel} <br> Required jar files: jide-common.jar,
 * jide-grids.jar <br> Required L&F: any L&F
 */
public class LuceneQuickFilterTableDemo extends AbstractDemo {
    private SortableTable _table;

    public LuceneQuickFilterTableDemo() {
    }

    public String getName() {
        return "LuceneFilterableTableModel Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_GRIDS;
    }

    @Override
    public String getDescription() {
        return "This is a demo of LuceneFilterableTableModel and LuceneQuickTableFilterField.\n" +
                "LuceneFilterableTableModel is a special FilterableTableModel which supports lucene query so it has better performance at high volume data scenario.\n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.lucene.LuceneFilterableTableModel\n" +
                "com.jidesoft.lucene.LuceneQuickTableFilterField";
    }

    @Override
    public Component getOptionsPanel() {
        JPanel buttonPanel = new JPanel(new GridLayout(0, 1));
        AbstractAction buttonActionAddRowsSelection = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                int rowIndex = _table.getSelectionModel().getMinSelectionIndex();
                if (rowIndex == -1) {
                    return;
                }
                int actualRowIndex = TableModelWrapperUtils.getActualRowAt(_table.getModel(), rowIndex, ProductTableModel.class);
                ProductTableModel innerModel = (ProductTableModel) TableModelWrapperUtils.getActualTableModel(_table.getModel(), ProductTableModel.class);
                innerModel.getDataVector().add(actualRowIndex, innerModel.getDataVector().get(0));
                innerModel.fireTableRowsInserted(actualRowIndex, actualRowIndex);
            }
        };
        buttonActionAddRowsSelection.putValue(Action.NAME, "Add one row before the selected row");
        JButton addRowsSelection = new JButton("Add one row before the selected row");
        addRowsSelection.setAction(buttonActionAddRowsSelection);

        AbstractAction buttonActionDeleteRow = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                int startIndex = _table.getSelectionModel().getMinSelectionIndex();
                int endIndex = _table.getSelectionModel().getMaxSelectionIndex();
                for (int rowIndex = endIndex; rowIndex >= startIndex; rowIndex--) {
                    if (rowIndex < 0 || rowIndex >= _table.getRowCount()) {
                        return;
                    }
                    int actualRowIndex = TableModelWrapperUtils.getActualRowAt(_table.getModel(), rowIndex, ProductTableModel.class);
                    ProductTableModel innerModel = (ProductTableModel) TableModelWrapperUtils.getActualTableModel(_table.getModel(), ProductTableModel.class);
                    innerModel.removeRow(actualRowIndex);
                }
            }
        };
        buttonActionDeleteRow.putValue(Action.NAME, "Delete the selected row(s)");
        JButton deleteRow = new JButton("Delete the selected row(s)");
        deleteRow.setAction(buttonActionDeleteRow);

        AbstractAction buttonActionTestDeleteRow = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                ProductTableModel innerModel = (ProductTableModel) TableModelWrapperUtils.getActualTableModel(_table.getModel(), ProductTableModel.class);
                if (innerModel.getRowCount() >= 3) {
                    int endRow = innerModel.getRowCount() - 3;
                    for (int i = endRow; i >= 0; i--) {
                        innerModel.getDataVector().remove(i);
                    }
                    innerModel.fireTableRowsDeleted(0, endRow);
                }
            }
        };
        buttonActionTestDeleteRow.putValue(Action.NAME, "Delete almost all rows");
        JButton testDeleteRow = new JButton("Delete almost all rows");
        testDeleteRow.setAction(buttonActionTestDeleteRow);

        AbstractAction buttonActionFireTableStructureChanged = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                ProductTableModel innerModel = (ProductTableModel) TableModelWrapperUtils.getActualTableModel(_table.getModel(), ProductTableModel.class);
                innerModel.fireTableStructureChanged();
            }
        };
        buttonActionFireTableStructureChanged.putValue(Action.NAME, "Fire Table Structure Changed Event");
        JButton fireTableStructureChangedEvent = new JButton("Fire Table Structure Changed Event");
        fireTableStructureChangedEvent.setAction(buttonActionFireTableStructureChanged);

        AbstractAction buttonActionFireTableChanged = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                ProductTableModel innerModel = (ProductTableModel) TableModelWrapperUtils.getActualTableModel(_table.getModel(), ProductTableModel.class);
                innerModel.fireTableDataChanged();
            }
        };
        buttonActionFireTableChanged.putValue(Action.NAME, "Fire Table Data Changed Event");
        JButton fireTableDataChangedEvent = new JButton("Fire Table Data Changed Event");
        fireTableDataChangedEvent.setAction(buttonActionFireTableChanged);

        AbstractAction buttonActionFireTableRowUpdated = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                ProductTableModel innerModel = (ProductTableModel) TableModelWrapperUtils.getActualTableModel(_table.getModel(), ProductTableModel.class);
                if (innerModel.getRowCount() >= 2) {
                    innerModel.fireTableRowsUpdated(0, innerModel.getRowCount() - 2);
                }
            }
        };
        buttonActionFireTableRowUpdated.putValue(Action.NAME, "Fire Table Row Updated Event");
        JButton fireTableRowUpdatedEvent = new JButton("Fire Table Row Updated Event");
        fireTableRowUpdatedEvent.setAction(buttonActionFireTableRowUpdated);

        AbstractAction buttonActionFireTableCellUpdated = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                ProductTableModel innerModel = (ProductTableModel) TableModelWrapperUtils.getActualTableModel(_table.getModel(), ProductTableModel.class);
                for (int row = innerModel.getRowCount() - 1; row >= Math.max(innerModel.getRowCount() - 11, 0); row--) {
                    innerModel.fireTableCellUpdated(row, 0);
                }
            }
        };
        buttonActionFireTableCellUpdated.putValue(Action.NAME, "Fire Table Cell Updated Event");
        JButton fireTableCellUpdatedEvent = new JButton("Fire Table Cell Updated Event");
        fireTableCellUpdatedEvent.setAction(buttonActionFireTableCellUpdated);

        buttonPanel.add(addRowsSelection);
        buttonPanel.add(deleteRow);
        buttonPanel.add(testDeleteRow);
        buttonPanel.add(fireTableStructureChangedEvent);
        buttonPanel.add(fireTableDataChangedEvent);
        buttonPanel.add(fireTableRowUpdatedEvent);
        buttonPanel.add(fireTableCellUpdatedEvent);
        return buttonPanel;
    }

    public Component getDemoPanel() {
        TableModel model = createProductReportsTableModel();
        QuickTableFilterField filterField = new LuceneQuickTableFilterField(model);
        filterField.setHintText("Type here to filter");
        filterField.setObjectConverterManagerEnabled(true);

        _table = new SortableTable(new LuceneFilterableTableModel(filterField.getDisplayTableModel()));
        _table.setClearSelectionOnTableDataChanges(false);
        _table.setRowResizable(true);
        _table.setVariousRowHeights(true);

        AutoFilterTableHeader _header = new AutoFilterTableHeader(_table);
        _header.setAutoFilterEnabled(true);
        _table.setTableHeader(_header);

        JPanel panel = new JPanel(new BorderLayout(3, 3));
        JPanel fieldPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        fieldPanel.setBorder(BorderFactory.createCompoundBorder(new JideTitledBorder(new PartialEtchedBorder(PartialEtchedBorder.LOWERED, PartialSide.NORTH), "LuceneQuickTableFilterField", JideTitledBorder.LEADING, JideTitledBorder.ABOVE_TOP),
                BorderFactory.createEmptyBorder(3, 3, 3, 3)));
        fieldPanel.add(filterField);

        JPanel tablePanel = new JPanel(new BorderLayout(3, 3));
        tablePanel.setBorder(BorderFactory.createCompoundBorder(new JideTitledBorder(new PartialEtchedBorder(PartialEtchedBorder.LOWERED, PartialSide.NORTH), "Filtered Product Sales Item", JideTitledBorder.LEADING, JideTitledBorder.ABOVE_TOP),
                BorderFactory.createEmptyBorder(3, 3, 3, 3)));
        tablePanel.add(new JScrollPane(_table));

        panel.add(fieldPanel, BorderLayout.BEFORE_FIRST_LINE);
        panel.add(tablePanel);

        return panel;
    }

    @Override
    public String getDemoFolder() {
        return "G15.QuickFilter";
    }

    static public void main(String[] s) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new LuceneQuickFilterTableDemo());
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