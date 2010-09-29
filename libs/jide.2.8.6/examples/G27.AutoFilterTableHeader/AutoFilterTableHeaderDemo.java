/*
 * @(#)AutoFilterTableHeader.java 2/5/2007
 *
 * Copyright 2002 - 2007 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.comparator.ObjectComparatorManager;
import com.jidesoft.converter.*;
import com.jidesoft.filter.FilterFactoryManager;
import com.jidesoft.grid.*;
import com.jidesoft.grouper.DefaultObjectGrouper;
import com.jidesoft.grouper.GrouperContext;
import com.jidesoft.grouper.ObjectGrouperManager;
import com.jidesoft.grouper.date.DateYearGrouper;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.JideTitledBorder;
import com.jidesoft.swing.PartialEtchedBorder;
import com.jidesoft.swing.PartialSide;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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
 * Demoed Component: {@link com.jidesoft.grid.AutoFilterTableHeader} <br> Required jar files: jide-common.jar,
 * jide-grids.jar <br> Required L&F: any L&F
 */
public class AutoFilterTableHeaderDemo extends AbstractDemo {
    protected AutoFilterTableHeader _header;
    protected FilterFactoryManager _filterManager;
    private SortableTable _sortableTable;
    private static final long serialVersionUID = -8262453726101747955L;

    public AutoFilterTableHeaderDemo() {
        CellEditorManager.initDefaultEditor();
        CellRendererManager.initDefaultRenderer();
        ObjectConverterManager.initDefaultConverter();
        ObjectComparatorManager.initDefaultComparator();
        DoubleConverter converter = new DoubleConverter();
        NumberFormat numberInstance = NumberFormat.getNumberInstance();
        numberInstance.setMinimumFractionDigits(2);
        converter.setNumberFormat(numberInstance);
        ObjectConverterManager.registerConverter(Double.class, converter);
        ObjectConverterManager.registerConverter(Integer.class, new SalesConverter(), SalesConverter.CONTEXT);

        ObjectGrouperManager.initDefaultGrouper();
        ObjectGrouperManager.registerGrouper(Float.class, new SalesObjectGrouper(), SalesObjectGrouper.CONTEXT);

        _filterManager = new FilterFactoryManager();
        _filterManager.registerDefaultFilterFactories();
    }

    public String getName() {
        return "AutoFilterTableHeader (Table) Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_GRIDS;
    }

    @Override
    public int getAttributes() {
        return ATTRIBUTE_NONE;
    }

    @Override
    public String getDescription() {
        return "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.grid.AutoFilterTableHeader";
    }

    @Override
    public Component getOptionsPanel() {
        JPanel checkBoxPanel = new JPanel(new GridLayout(0, 1));
        JCheckBox autoFilterCheckBox = new JCheckBox("Auto Filter");
        autoFilterCheckBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _header.setAutoFilterEnabled(e.getStateChange() == ItemEvent.SELECTED);
            }
        });
        autoFilterCheckBox.setSelected(_header.isAutoFilterEnabled());

        JCheckBox showFilterNameCheckBox = new JCheckBox("Show Filter Name on Header");
        showFilterNameCheckBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _header.setShowFilterName(e.getStateChange() == ItemEvent.SELECTED);
            }
        });
        showFilterNameCheckBox.setSelected(_header.isShowFilterName());

        JCheckBox showFilterNameAsToolTipCheckBox = new JCheckBox("Show Filter Name as ToolTip");
        showFilterNameAsToolTipCheckBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _header.setShowFilterNameAsToolTip(e.getStateChange() == ItemEvent.SELECTED);
            }
        });
        showFilterNameAsToolTipCheckBox.setSelected(_header.isShowFilterNameAsToolTip());

        JCheckBox showFilterIconCheckBox = new JCheckBox("Show Filter Icon on Header");
        showFilterIconCheckBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _header.setShowFilterIcon(e.getStateChange() == ItemEvent.SELECTED);
            }
        });
        showFilterIconCheckBox.setSelected(_header.isShowFilterIcon());
        JCheckBox sortArrowVisibleCheckBox = new JCheckBox("Show Sort Arrow on Header");
        sortArrowVisibleCheckBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _header.setShowSortArrow(e.getStateChange() == ItemEvent.SELECTED);
            }
        });
        sortArrowVisibleCheckBox.setSelected(_header.isShowSortArrow());
        JCheckBox allowMultipleValuesCheckBox = new JCheckBox("Allow Multiple Values as Filter");
        allowMultipleValuesCheckBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _header.setAllowMultipleValues(e.getStateChange() == ItemEvent.SELECTED);
            }
        });
        allowMultipleValuesCheckBox.setSelected(_header.isAllowMultipleValues());

        JCheckBox pauseSorting = new JCheckBox("Pause Sorting");
        pauseSorting.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                TableModel tableModel = TableModelWrapperUtils.getActualTableModel(_sortableTable.getModel(), SortableTableModel.class);
                if (tableModel instanceof SortableTableModel) {
                    ((SortableTableModel) tableModel).setSortingPaused(e.getStateChange() == ItemEvent.SELECTED);
                }
            }
        });
        pauseSorting.setSelected(false);

        JCheckBox pauseFiltering = new JCheckBox("Pause Filtering");
        pauseFiltering.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                TableModel tableModel = TableModelWrapperUtils.getActualTableModel(_sortableTable.getModel(), FilterableTableModel.class);
                if (tableModel instanceof FilterableTableModel) {
                    ((FilterableTableModel) tableModel).setFilteringPaused(e.getStateChange() == ItemEvent.SELECTED);
                }
            }
        });
        pauseFiltering.setSelected(false);

        JButton addRowsSelection = new JButton(new AbstractAction("Add one row") {
            private static final long serialVersionUID = -4709145280400090007L;

            public void actionPerformed(ActionEvent e) {
                TableModel tableModel = TableModelWrapperUtils.getActualTableModel(_sortableTable.getModel(), DefaultContextSensitiveTableModel.class);
                if (tableModel instanceof DefaultTableModel) {
                    ((DefaultTableModel) tableModel).addRow((Vector) ((DefaultTableModel) tableModel).getDataVector().elementAt(0));
                }
            }
        });

        checkBoxPanel.add(autoFilterCheckBox);
        checkBoxPanel.add(showFilterNameCheckBox);
        checkBoxPanel.add(showFilterNameAsToolTipCheckBox);
        checkBoxPanel.add(showFilterIconCheckBox);
        checkBoxPanel.add(sortArrowVisibleCheckBox);
        checkBoxPanel.add(allowMultipleValuesCheckBox);
        checkBoxPanel.add(pauseSorting);
        checkBoxPanel.add(pauseFiltering);
        checkBoxPanel.add(addRowsSelection);
//        checkBoxPanel.add(new JButton(new AbstractAction("Add Produce") {
//            public void actionPerformed(ActionEvent e) {
//                _header.getFilterableTableModel().addFilter(0, new SingleValueFilter("Produce"));
//                _header.getFilterableTableModel().setFiltersApplied(true);
//            }
//        }));
//        checkBoxPanel.add(new JButton(new AbstractAction("Add Seafood") {
//            public void actionPerformed(ActionEvent e) {
//                _header.getFilterableTableModel().addFilter(0, new MultipleValuesFilter(new Object[]{"Seafood", "Produce"}));
//                _header.getFilterableTableModel().setFiltersApplied(true);
//            }
//        }));
        return checkBoxPanel;
    }

    public Component getDemoPanel() {
        final TableModel tableModel = createProductReportsTableModel();
        if (tableModel == null) {
            return new JLabel("Failed to read data file");
        }

        return createSortableTable(tableModel);
    }

    private JPanel createSortableTable(TableModel tableModel) {
        final JPanel panel = new JPanel(new BorderLayout(2, 2));
        panel.setBorder(BorderFactory.createCompoundBorder(new JideTitledBorder(new PartialEtchedBorder(PartialEtchedBorder.LOWERED, PartialSide.NORTH), "AutoFilterTableHeader", JideTitledBorder.LEADING, JideTitledBorder.ABOVE_TOP),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)));
        _sortableTable = new SortableTable(tableModel);
        _header = new AutoFilterTableHeader(_sortableTable) {
            @Override
            protected IFilterableTableModel createFilterableTableModel(TableModel model) {
                return new FilterableTableModel(model) {
                    private static final long serialVersionUID = 7072186511643823323L;

                    @Override
                    public boolean isColumnAutoFilterable(int column) {
                        return true;
                    }
                };
            }
        };
        _header.setAutoFilterEnabled(true);
        _sortableTable.setTableHeader(_header);
        _sortableTable.setPreferredScrollableViewportSize(new Dimension(600, 500));
        TableHeaderPopupMenuInstaller installer = new TableHeaderPopupMenuInstaller(_sortableTable);
        installer.addTableHeaderPopupMenuCustomizer(new AutoResizePopupMenuCustomizer());
        installer.addTableHeaderPopupMenuCustomizer(new TableColumnChooserPopupMenuCustomizer());
        panel.add(new JScrollPane(_sortableTable));
        return panel;
    }

    public TableModel createProductReportsTableModel() {
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
                        //noinspection CallToPrintStackTrace
                        e.printStackTrace();
                    }
                }
                for (int i = 0; i < 1; i++) {
                    data.add(lineData);
                }
            }
            while (true);
            return new DefaultContextSensitiveTableModel(data, columnNames) {
                private static final long serialVersionUID = -1938173557449999961L;

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
                    return true;
                }

                @Override
                public Class<?> getCellClassAt(int row, int column) {
                    return getColumnClass(column);
                }

                @Override
                public ConverterContext getConverterContextAt(int row, int column) {
                    if (column == 2) {
                        return CurrencyConverter.CONTEXT;
                    }
                    return super.getConverterContextAt(row, column);
                }
            };
        }
        catch (IOException e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
        return null;
    }

    static class DefaultContextSensitiveTableModel extends DefaultTableModel implements ContextSensitiveTableModel, GroupableTableModel {
        private static final long serialVersionUID = 6219782090215635528L;

        public DefaultContextSensitiveTableModel() {
        }

        public DefaultContextSensitiveTableModel(int rowCount, int columnCount) {
            super(rowCount, columnCount);
        }

        public DefaultContextSensitiveTableModel(Vector columnNames, int rowCount) {
            super(columnNames, rowCount);
        }

        public DefaultContextSensitiveTableModel(Object[] columnNames, int rowCount) {
            super(columnNames, rowCount);
        }

        public DefaultContextSensitiveTableModel(Vector data, Vector columnNames) {
            super(data, columnNames);
        }

        public DefaultContextSensitiveTableModel(Object[][] data, Object[] columnNames) {
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

        public GrouperContext getGrouperContext(int columnIndex) {
            switch (columnIndex) {
                case 2:
                    return SalesObjectGrouper.CONTEXT;
                case 3:
                    return DateYearGrouper.CONTEXT;
            }
            return null;
        }
    }

    private static class SalesConverter extends DefaultObjectConverter {
        public static ConverterContext CONTEXT = new ConverterContext("Sales");

        @Override
        public String toString(Object object, ConverterContext context) {
            if (object instanceof Integer) {
                int value = (Integer) object;
                switch (value) {
                    case 0:
                        return "From 0 to 100";
                    case 1:
                        return "From 100 to 1000";
                    case 2:
                        return "From 1000 to 10000";
                    case 3:
                        return "Greater than 10000";
                }
            }
            return null;
        }

        @Override
        public boolean supportFromString(String string, ConverterContext context) {
            return false;
        }
    }

    private static class SalesObjectGrouper extends DefaultObjectGrouper {
        public static final GrouperContext CONTEXT = new GrouperContext("Sales");

        @Override
        public Object getValue(Object value) {
            if (value instanceof Number) {
                double v = ((Number) value).doubleValue();
                if (v < 100) {
                    return 0;
                }
                else if (v < 1000) {
                    return 1;
                }
                else if (v < 10000) {
                    return 2;
                }
                else {
                    return 3;
                }
            }
            return null;
        }

        @Override
        public Class<?> getType() {
            return Integer.class;
        }

        @Override
        public ConverterContext getConverterContext() {
            return SalesConverter.CONTEXT;
        }
    }

    @Override
    public String getDemoFolder() {
        return "G27.AutoFilterTableHeader";
    }

    static public void main(String[] s) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new AutoFilterTableHeaderDemo());
    }
}
