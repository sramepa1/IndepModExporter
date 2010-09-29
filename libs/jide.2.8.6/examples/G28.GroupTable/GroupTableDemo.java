/*
 * @(#)GroupableTableModelDemo.java 4/13/2007
 *
 * Copyright 2002 - 2007 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.converter.*;
import com.jidesoft.grid.*;
import com.jidesoft.grouper.date.DateMonthGrouper;
import com.jidesoft.grouper.date.DateQuarterGrouper;
import com.jidesoft.grouper.date.DateWeekOfYearGrouper;
import com.jidesoft.grouper.date.DateYearGrouper;
import com.jidesoft.hssf.HssfTableUtils;
import com.jidesoft.icons.IconsFactory;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.JideSplitPane;
import com.jidesoft.swing.JideSwingUtilities;
import com.jidesoft.swing.SearchableBar;
import com.jidesoft.swing.TableSearchable;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Vector;
import java.util.zip.GZIPInputStream;

/**
 * Demoed Component: {@link com.jidesoft.grid.JideTable} <br> Required jar files: jide-common.jar, jide-grids.jar <br>
 * Required L&F: any L&F
 */
public class GroupTableDemo extends AbstractDemo {
    public TreeTable _table;
    public JLabel _message;
    protected TableModel _tableModel;
    private DefaultGroupTableModel _groupTableModel;
    private boolean _useStyle;
    private String _lastDirectory = ".";
    private static final long serialVersionUID = 256315903870338341L;

    public GroupTableDemo() {
        ObjectConverterManager.initDefaultConverter();
        // instead of using the default toString method provided by DefaultGroupRow, we add our own
        // converter so that we can display the number of items.
        ObjectConverterManager.registerConverter(DefaultGroupRow.class, new ObjectConverter() {
            public String toString(Object object, ConverterContext context) {
                if (object instanceof DefaultGroupRow) {
                    DefaultGroupRow row = (DefaultGroupRow) object;
                    StringBuffer buf = new StringBuffer(row.toString());
                    int allVisibleChildrenCount = TableModelWrapperUtils.getVisibleChildrenCount(_table.getModel(), row);
                    buf.append(" (").append(allVisibleChildrenCount).append(" items)");
                    return buf.toString();
                }
                return null;
            }

            public boolean supportToString(Object object, ConverterContext context) {
                return true;
            }

            public Object fromString(String string, ConverterContext context) {
                return null;
            }

            public boolean supportFromString(String string, ConverterContext context) {
                return false;
            }
        });
    }

    public String getName() {
        return "GroupTable Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_GRIDS;
    }


    @Override
    public int getAttributes() {
        return ATTRIBUTE_UPDATED;
    }

    protected static final Color BACKGROUND1 = new Color(159, 155, 217);
    protected static final Color BACKGROUND2 = new Color(197, 194, 232);

    public static final CellStyle style1 = new CellStyle();
    public static final CellStyle style2 = new CellStyle();
    public static final CellStyle styleGroup1 = new CellStyle();
    public static final CellStyle styleGroup2 = new CellStyle();

    static {
        style1.setBackground(BACKGROUND1);
        style2.setBackground(BACKGROUND2);
        style1.setHorizontalAlignment(SwingConstants.CENTER);
        style2.setHorizontalAlignment(SwingConstants.CENTER);
        styleGroup1.setBackground(BACKGROUND1);
        styleGroup2.setBackground(BACKGROUND2);
        styleGroup1.setFontStyle(Font.BOLD);
        styleGroup2.setFontStyle(Font.BOLD);
    }

    @Override
    public Component getOptionsPanel() {
        JPanel checkBoxPanel = new JPanel(new GridLayout(0, 1));
        JCheckBox singleLevel = new JCheckBox("Single Level");
        singleLevel.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _groupTableModel.setSingleLevelGrouping(e.getStateChange() == ItemEvent.SELECTED);
                _groupTableModel.groupAndRefresh();
                _groupTableModel.fireTableStructureChanged();
                _table.expandAll();
            }
        });
        singleLevel.setSelected(_groupTableModel.isSingleLevelGrouping());

        JCheckBox keepColumnOrder = new JCheckBox("Keep Column Order");
        keepColumnOrder.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                String preference = TableUtils.getTablePreferenceByName(_table);
                _groupTableModel.setKeepColumnOrder(e.getStateChange() == ItemEvent.SELECTED);
                _groupTableModel.groupAndRefresh();
                _groupTableModel.fireTableStructureChanged();
                TableUtils.setTablePreferenceByName(_table, preference);
                _table.expandAll();
            }
        });
        keepColumnOrder.setSelected(_groupTableModel.isKeepColumnOrder());

        JCheckBox showGroupColumns = new JCheckBox("Show Group Columns");
        showGroupColumns.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _groupTableModel.setDisplayGroupColumns(e.getStateChange() == ItemEvent.SELECTED);
                _groupTableModel.groupAndRefresh();
                _groupTableModel.fireTableStructureChanged();
                _table.expandAll();
            }
        });
        showGroupColumns.setSelected(_groupTableModel.isDisplayGroupColumns());

        JCheckBox showCountColumn = new JCheckBox("Show Count Columns");
        showCountColumn.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _groupTableModel.setDisplayCountColumn(e.getStateChange() == ItemEvent.SELECTED);
                _groupTableModel.groupAndRefresh();
                _groupTableModel.fireTableStructureChanged();
                TableUtils.saveColumnOrders(_table, false);
                _table.expandAll();
            }
        });
        showCountColumn.setSelected(_groupTableModel.isDisplayCountColumn());

        JCheckBox showStyle = new JCheckBox("Add Styles");
        showStyle.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _useStyle = e.getStateChange() == ItemEvent.SELECTED;
                _table.setShowGrid(!_useStyle);
                _table.setIntercellSpacing(_useStyle ? new Dimension(0, 0) : new Dimension(1, 1));
                _table.setShowTreeLines(!_useStyle);
                _table.repaint();
            }
        });
        showStyle.setSelected(false);

        checkBoxPanel.add(singleLevel);
        checkBoxPanel.add(keepColumnOrder);
        checkBoxPanel.add(showGroupColumns);
        checkBoxPanel.add(showCountColumn);
        checkBoxPanel.add(showStyle);
        return checkBoxPanel;
    }

    public Component getDemoPanel() {
        final JideSplitPane panel = new JideSplitPane(JideSplitPane.VERTICAL_SPLIT);

        _tableModel = createProductReportsTableModel();
        final CalculatedTableModel calculatedTableModel = setupProductDetailsCalculatedTableModel(_tableModel);

        _groupTableModel = new StyledGroupTableModel(calculatedTableModel);
        _groupTableModel.addGroupColumn(0);
        _groupTableModel.addGroupColumn(1);
        _groupTableModel.groupAndRefresh();

        QuickTableFilterField field = new QuickTableFilterField(_groupTableModel);

        SortableTreeTableModel sortableTTM = new SortableTreeTableModel(field.getDisplayTableModel());
        sortableTTM.setSortableOption(0, SortableTreeTableModel.SORTABLE_ROOT_LEVEL);
        _table = new GroupTable(sortableTTM);
        TableHeaderPopupMenuInstaller installer = new TableHeaderPopupMenuInstaller(_table) {
            @Override
            protected void customizeMenuItems(final JTableHeader header, final JPopupMenu popup, final int clickingColumn) {
                super.customizeMenuItems(header, popup, clickingColumn);

                addSeparatorIfNecessary(popup);

                final JMenuItem export = new JMenuItem(new AbstractAction("Export to Excel 2003 format") {
                    private static final long serialVersionUID = 2581042425782595535L;

                    public void actionPerformed(ActionEvent e) {
                        _table.putClientProperty(HssfTableUtils.CLIENT_PROPERTY_EXCEL_OUTPUT_FORMAT, HssfTableUtils.EXCEL_OUTPUT_FORMAT_2003);
                        if (!HssfTableUtils.isHssfInstalled()) {
                            JOptionPane.showMessageDialog((Component) e.getSource(), "Export to Excel feature is disabled because POI-HSSF jar is missing in the classpath.");
                            return;
                        }
                        outputToExcel(e);
                    }
                });

                final JMenuItem export2007 = new JMenuItem(new AbstractAction("Export to Excel 2007 format") {
                    private static final long serialVersionUID = 2581042425782595535L;

                    public void actionPerformed(ActionEvent e) {
                        _table.putClientProperty(HssfTableUtils.CLIENT_PROPERTY_EXCEL_OUTPUT_FORMAT, HssfTableUtils.EXCEL_OUTPUT_FORMAT_2007);
                        if (!HssfTableUtils.isXssfInstalled()) {
                            JOptionPane.showMessageDialog((Component) e.getSource(), "Export to Excel 2007 feature is disabled because one or several POI-XSSF dependency jars are missing in the classpath. Please include all the jars from poi release in the classpath and try to run again.");
                            return;
                        }
                        outputToExcel(e);
                    }
                });
                popup.add(export);
                popup.add(export2007);
            }
        };
        installer.addTableHeaderPopupMenuCustomizer(new AutoResizePopupMenuCustomizer());
        installer.addTableHeaderPopupMenuCustomizer(new GroupTablePopupMenuCustomizer());
        installer.addTableHeaderPopupMenuCustomizer(new TableColumnChooserPopupMenuCustomizer());
        installer.addTableHeaderPopupMenuCustomizer(new SelectTablePopupMenuCustomizer());
        _table.setExpandedIcon(IconsFactory.getImageIcon(GroupTableDemo.class, "icons/outlook_collapse.png"));
        _table.setCollapsedIcon(IconsFactory.getImageIcon(GroupTableDemo.class, "icons/outlook_expand.png"));
        _table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        _table.setOptimized(true);

        // hide the grid lines is good for performance
        _table.setShowLeafNodeTreeLines(false);
        _table.setShowTreeLines(false);
        _table.setExportCollapsedRowsToExcel(true);

        _table.expandAll();

        panel.add(JideSwingUtilities.createLabeledComponent(new JLabel("Right click on the table header to see more options"),
                new JScrollPane(_table), BorderLayout.BEFORE_FIRST_LINE));
        panel.add(field, BorderLayout.AFTER_LINE_ENDS);

        TableSearchable searchable = new GroupTableSearchable(_table);
        searchable.setSearchColumnIndices(new int[]{2, 3});
        searchable.setRepeats(true);
        SearchableBar searchableBar = SearchableBar.install(searchable, KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_DOWN_MASK), new SearchableBar.Installer() {
            public void openSearchBar(SearchableBar searchableBar) {
                panel.add(searchableBar, BorderLayout.AFTER_LAST_LINE);
                panel.invalidate();
                panel.revalidate();
            }

            public void closeSearchBar(SearchableBar searchableBar) {
                panel.remove(searchableBar);
                panel.invalidate();
                panel.revalidate();
            }
        });
        searchableBar.setName("TableSearchableBar");
        return panel;
    }

    @Override
    public String getDemoFolder() {
        return "G28.GroupableTableModel";
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new GroupTableDemo());
    }

    private void outputToExcel(ActionEvent e) {
        JFileChooser chooser = new JFileChooser() {
            @Override
            protected JDialog createDialog(Component parent) throws HeadlessException {
                JDialog dialog = super.createDialog(parent);
                dialog.setTitle("Export the content to an Excel file");
                return dialog;
            }
        };
        chooser.setCurrentDirectory(new File(_lastDirectory));
        int result = chooser.showDialog(((JMenuItem) e.getSource()).getTopLevelAncestor(), "Export");
        if (result == JFileChooser.APPROVE_OPTION) {
            _lastDirectory = chooser.getCurrentDirectory().getAbsolutePath();
            try {
                HssfTableUtils.export(_table, chooser.getSelectedFile().getAbsolutePath(), "GroupTable", false);
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private CalculatedTableModel setupProductDetailsCalculatedTableModel(TableModel tableModel) {
        CalculatedTableModel calculatedTableModel = new CalculatedTableModel(tableModel);
        calculatedTableModel.addAllColumns();
        SingleColumn year = new SingleColumn(tableModel, "ShippedDate", "Year", new DateYearGrouper());
        year.setConverterContext(YearNameConverter.CONTEXT);
        calculatedTableModel.addColumn(year);
        SingleColumn qtr = new SingleColumn(tableModel, "ShippedDate", "Quarter", new DateQuarterGrouper());
        qtr.setConverterContext(QuarterNameConverter.CONTEXT);
        calculatedTableModel.addColumn(qtr);
        SingleColumn month = new SingleColumn(tableModel, "ShippedDate", "Month", new DateMonthGrouper());
        month.setConverterContext(MonthNameConverter.CONTEXT);
        calculatedTableModel.addColumn(month);
        calculatedTableModel.addColumn(new SingleColumn(tableModel, "ShippedDate", "Week", new DateWeekOfYearGrouper()));

        return calculatedTableModel;
    }

    private class StyledGroupTableModel extends DefaultGroupTableModel implements StyleModel {
        private static final long serialVersionUID = 4936234855874300579L;

        public StyledGroupTableModel(TableModel tableModel) {
            super(tableModel);
        }

        public CellStyle getCellStyleAt(int rowIndex, int columnIndex) {
            if (_useStyle && hasGroupColumns()) {
                Row row = getRowAt(rowIndex);
                boolean topLevel = false;
                if (row instanceof DefaultGroupRow) {
                    topLevel = true;
                }
                while (!(row instanceof DefaultGroupRow)) {
                    row = (Row) row.getParent();
                }
                if (getOriginalRows().indexOf(row) % 2 == 0) {
                    return topLevel ? styleGroup1 : style1;
                }
                else {
                    return topLevel ? styleGroup2 : style2;
                }
            }
            else {
                return null;
            }
        }

        public boolean isCellStyleOn() {
            return _useStyle;
        }
    }

    private TableModel createProductReportsTableModel() {
        try {
            InputStream resource = this.getClass().getClassLoader().getResourceAsStream("ProductReports.txt.gz");
            if (resource == null) {
                return null;
            }
            InputStream in = new GZIPInputStream(resource);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            Vector<Object> data = new Vector<Object>();
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
        private static final long serialVersionUID = -752023689225531021L;

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
            return true;
        }
    }
}
