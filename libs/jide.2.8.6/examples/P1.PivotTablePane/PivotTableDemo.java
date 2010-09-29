/*
 * @(#)PivotTableDemo.java 10/1/2008
 *
 * Copyright 2002 - 2008 JIDE Software Inc. All rights reserved.
 *
 */

import com.jidesoft.converter.*;
import com.jidesoft.grid.*;
import com.jidesoft.grouper.AbstractObjectGrouper;
import com.jidesoft.grouper.date.*;
import com.jidesoft.hssf.HssfPivotTableUtils;
import com.jidesoft.hssf.HssfTableUtils;
import com.jidesoft.pivot.*;
import com.jidesoft.plaf.LookAndFeelFactory;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.GZIPInputStream;

/**
 * Demoed Component: {@link com.jidesoft.pivot.PivotTablePane} <br> Required jar files: jide-common.jar, jide-grids.jar
 * <br> Required L&F: any L&F
 */
public class PivotTableDemo extends AbstractDemo {
    protected PivotTablePane _pivotTablePane;
    protected String _lastDirectory = ".";
    private Map<String, Boolean> _rowExpansionState;
    private Map<String, Boolean> _columnExpansionState;
    private static final long serialVersionUID = -5087720822571689883L;

    public PivotTableDemo() {
    }

    public String getName() {
        return "Pivot Grid Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_PIVOT;
    }

    @Override
    public String getDescription() {
        return "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.pivot.PivotTablePane";
    }

    @Override
    public int getAttributes() {
        return ATTRIBUTE_UPDATED;
    }

    static CellStyle HIGH_STYLE = new CellStyle();
    static CellStyle LOW_STYLE = new CellStyle();
    static CellStyle SUMMARY_STYLE = new CellStyle();
    static CellStyle DEFAULT_STYLE = new CellStyle();
    static CellStyle HEADER_STYLE = new CellStyle();

    static {
        HIGH_STYLE.setForeground(Color.WHITE);
        HIGH_STYLE.setBackground(Color.RED);

        LOW_STYLE.setBackground(Color.YELLOW);

        SUMMARY_STYLE.setBackground(new Color(255, 255, 192));

        HEADER_STYLE.setFontStyle(Font.BOLD);
    }

    @Override
    public Component getOptionsPanel() {
        JButton saveButton = new JButton(new AbstractAction("Save Layout as XML") {
            private static final long serialVersionUID = -5107715775596570738L;

            public void actionPerformed(ActionEvent e) {
                try {
                    JFileChooser chooser = new JFileChooser() {
                        @Override
                        protected JDialog createDialog(Component parent) throws HeadlessException {
                            JDialog dialog = super.createDialog(parent);
                            dialog.setTitle("Save the layout as an \".xml\" file");
                            return dialog;
                        }
                    };
                    chooser.setCurrentDirectory(new File(_lastDirectory));
                    int result = chooser.showDialog(((JButton) e.getSource()).getTopLevelAncestor(), "Save");
                    if (result == JFileChooser.APPROVE_OPTION) {
                        _lastDirectory = chooser.getCurrentDirectory().getAbsolutePath();
                        PivotTablePersistenceUtils.save(_pivotTablePane, chooser.getSelectedFile().getAbsolutePath());
                    }
                }
                catch (ParserConfigurationException e1) {
                    //noinspection CallToPrintStackTrace
                    e1.printStackTrace();
                }
                catch (IOException e1) {
                    //noinspection CallToPrintStackTrace
                    e1.printStackTrace();
                }
            }
        });
        JButton loadButton = new JButton(new AbstractAction("Load Layout from XML") {
            private static final long serialVersionUID = 3732486289243549658L;

            public void actionPerformed(ActionEvent e) {
                try {
                    JFileChooser chooser = new JFileChooser() {
                        @Override
                        protected JDialog createDialog(Component parent) throws HeadlessException {
                            JDialog dialog = super.createDialog(parent);
                            dialog.setTitle("Load an \".xml\" file");
                            return dialog;
                        }
                    };
                    chooser.setCurrentDirectory(new File(_lastDirectory));
                    int result = chooser.showDialog(((JButton) e.getSource()).getTopLevelAncestor(), "Open");
                    if (result == JFileChooser.APPROVE_OPTION) {
                        _lastDirectory = chooser.getCurrentDirectory().getAbsolutePath();
                        PivotTablePersistenceUtils.load(_pivotTablePane, chooser.getSelectedFile().getAbsolutePath());
                        _pivotTablePane.fieldsUpdated();
                    }
                }
                catch (SAXException e1) {
                    //noinspection CallToPrintStackTrace
                    e1.printStackTrace();
                }
                catch (ParserConfigurationException e1) {
                    //noinspection CallToPrintStackTrace
                    e1.printStackTrace();
                }
                catch (IOException e1) {
                    //noinspection CallToPrintStackTrace
                    e1.printStackTrace();
                }
            }
        });
        final JButton exportButton = new JButton(new AbstractAction("Export to Excel 2003 Format") {
            private static final long serialVersionUID = 3094181974476823551L;

            public void actionPerformed(ActionEvent e) {
                _pivotTablePane.putClientProperty(HssfTableUtils.CLIENT_PROPERTY_EXCEL_OUTPUT_FORMAT, HssfTableUtils.EXCEL_OUTPUT_FORMAT_2003);
                if (!HssfTableUtils.isHssfInstalled()) {
                    JOptionPane.showMessageDialog((Component) e.getSource(), "Export to Excel feature is disabled because POI-HSSF jar is missing in the classpath.");
                    return;
                }
                outputToExcel(e);
            }
        });

        final JButton export2007Button = new JButton(new AbstractAction("Export to Excel 2007 Format") {
            private static final long serialVersionUID = -6770274222025672395L;

            public void actionPerformed(ActionEvent e) {
                _pivotTablePane.putClientProperty(HssfTableUtils.CLIENT_PROPERTY_EXCEL_OUTPUT_FORMAT, HssfTableUtils.EXCEL_OUTPUT_FORMAT_2007);
                if (!HssfTableUtils.isXssfInstalled()) {
                    JOptionPane.showMessageDialog((Component) e.getSource(), "Export to Excel 2007 feature is disabled because one or several POI-XSSF dependency jars are missing in the classpath. Please include all the jars from poi release in the classpath and try to run again.");
                    return;
                }
                outputToExcel(e);
            }
        });

        JButton autoResizeButton = new JButton(new AbstractAction("Auto Resize All Columns") {
            private static final long serialVersionUID = -6770274222025672395L;

            public void actionPerformed(ActionEvent e) {
                _pivotTablePane.autoResizeAllColumns();
            }
        });

        JButton verifyButton = new JButton(new AbstractAction("Verify") {
            private static final long serialVersionUID = 8735193034044602626L;

            public void actionPerformed(ActionEvent e) {
                IPivotDataModel pivotDataProvider = _pivotTablePane.getPivotDataModel();
                if (pivotDataProvider instanceof PivotDataModel) pivotDataProvider.verify();
            }
        });

        final JButton getRowButton = new JButton(new AbstractAction("Get Row Expansion State") {
            public void actionPerformed(ActionEvent e) {
                _rowExpansionState = _pivotTablePane.getPivotDataModel().getRowHeaderTableModel().getExpansionState();
            }
        });

        final JButton setRowButton = new JButton(new AbstractAction("Set Row Expansion State") {
            public void actionPerformed(ActionEvent e) {
                _pivotTablePane.getPivotDataModel().getRowHeaderTableModel().setExpansionState(_rowExpansionState);
            }
        });

        final JButton getColumnButton = new JButton(new AbstractAction("Get Column Expansion State") {
            public void actionPerformed(ActionEvent e) {
                _columnExpansionState = _pivotTablePane.getPivotDataModel().getColumnHeaderTableModel().getExpansionState();
            }
        });

        final JButton setColumnButton = new JButton(new AbstractAction("Set Column Expansion State") {
            public void actionPerformed(ActionEvent e) {
                _pivotTablePane.getPivotDataModel().getColumnHeaderTableModel().setExpansionState(_columnExpansionState);
            }
        });

        JCheckBox styleCheckBox = new JCheckBox("Show Cell Style");
        styleCheckBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    _pivotTablePane.getPivotDataModel().setCellStyleProvider(new PivotCellStyleProvider() {
                        public CellStyle getDataTableCellStyleAt(DataTableModel model, HeaderTableModel rowHeaderModel, HeaderTableModel columnHeaderModel, int rowIndex, int columnIndex) {
                            if (rowHeaderModel.isSubtotalRowOrColumn(rowIndex) || columnHeaderModel.isSubtotalRowOrColumn(columnIndex)
                                    || rowHeaderModel.isGrandTotalRowOrColumn(rowIndex) || columnHeaderModel.isGrandTotalRowOrColumn(columnIndex)) {
                                return SUMMARY_STYLE;
                            }
                            else {
                                Object value = model.getValueAt(rowIndex, columnIndex);
                                int summaryType = model.getSummaryTypeAt(rowIndex, columnIndex);
                                if (summaryType != PivotConstants.SUMMARY_COUNT) {
                                    if (value instanceof Double && (Double) value > 10000) {
                                        return HIGH_STYLE;
                                    }
                                    else if (value instanceof Double && (Double) value < 50) {
                                        return LOW_STYLE;
                                    }
                                }
                                return DEFAULT_STYLE;
                            }
                        }

                        public CellStyle getColumnHeaderCellStyleAt(HeaderTableModel model, int rowIndex, int columnIndex) {
                            return HEADER_STYLE;
                        }

                        public CellStyle getRowHeaderCellStyleAt(HeaderTableModel model, int rowIndex, int columnIndex) {
                            return HEADER_STYLE;
                        }
                    });

                }
                else {
                    _pivotTablePane.getPivotDataModel().setCellStyleProvider(null);
                }
            }
        });
        styleCheckBox.setSelected(_pivotTablePane.getPivotDataModel().getCellStyleProvider() != null);

        JCheckBox fieldChooserCheckBox = new JCheckBox("Show Field Chooser");
        fieldChooserCheckBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _pivotTablePane.setFieldChooserVisible(e.getStateChange() == ItemEvent.SELECTED);
            }
        });
        fieldChooserCheckBox.setSelected(_pivotTablePane.isFieldChooserVisible());

        JCheckBox filterChooserCheckBox = new JCheckBox("Filter Field Chooser");
        filterChooserCheckBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _pivotTablePane.setFieldChooserFilterFieldVisible(e.getStateChange() == ItemEvent.SELECTED);
            }
        });
        filterChooserCheckBox.setSelected(_pivotTablePane.isFieldChooserFilterFieldVisible());

        JCheckBox grandTotalCheckBox = new JCheckBox("Show Grand Total");
        grandTotalCheckBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _pivotTablePane.getPivotDataModel().setShowGrandTotalForColumn(e.getStateChange() == ItemEvent.SELECTED);
                _pivotTablePane.getPivotDataModel().setShowGrandTotalForRow(e.getStateChange() == ItemEvent.SELECTED);
                _pivotTablePane.bothHeadersUpdated();
            }
        });
        grandTotalCheckBox.setSelected(_pivotTablePane.getPivotDataModel().isShowGrandTotalForColumn() && _pivotTablePane.getPivotDataModel().isShowGrandTotalForRow());

        JCheckBox hideFieldOnDragging = new JCheckBox("Hide FieldBox if Dragging Out");
        hideFieldOnDragging.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _pivotTablePane.setHideFieldOnDraggingOut(e.getStateChange() == ItemEvent.SELECTED);
            }
        });
        hideFieldOnDragging.setSelected(_pivotTablePane.isHideFieldOnDraggingOut());

        JCheckBox rearrangable = new JCheckBox("Fields Rearrangable");
        rearrangable.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _pivotTablePane.setRearrangable(e.getStateChange() == ItemEvent.SELECTED);
            }
        });
        rearrangable.setSelected(_pivotTablePane.isRearrangable());

        JCheckBox shrinkDataFields = new JCheckBox("Shrink Data Fields");
        shrinkDataFields.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _pivotTablePane.setShrinkDataFieldArea(e.getStateChange() == ItemEvent.SELECTED);
            }
        });
        shrinkDataFields.setSelected(_pivotTablePane.isShrinkDataFieldArea());

        JCheckBox plainHeaderTables = new JCheckBox("Plain Table Headers");
        plainHeaderTables.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _pivotTablePane.setPlainHeaderTables(e.getStateChange() == ItemEvent.SELECTED);
            }
        });
        plainHeaderTables.setSelected(_pivotTablePane.isPlainHeaderTables());

        JCheckBox headerSelectionModel = new JCheckBox("Allow Header Selection");
        headerSelectionModel.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _pivotTablePane.setHeaderSelectionMode(e.getStateChange() == ItemEvent.SELECTED ? PivotConstants.HEADER_SELECTION_HEADER_TABLE_ONLY : PivotConstants.HEADER_SELECTION_DATA_TABLE_ONLY);
            }
        });
        headerSelectionModel.setSelected(_pivotTablePane.getHeaderSelectionMode() == PivotConstants.HEADER_SELECTION_HEADER_TABLE_ONLY);

        JCheckBox filterFieldAreaVisible = new JCheckBox("Filter Area Visible");
        filterFieldAreaVisible.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _pivotTablePane.setFilterFieldAreaVisible(e.getStateChange() == ItemEvent.SELECTED);
            }
        });
        filterFieldAreaVisible.setSelected(_pivotTablePane.isFilterFieldAreaVisible());

        JCheckBox dataFieldAreaVisible = new JCheckBox("Data Area Visible");
        dataFieldAreaVisible.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _pivotTablePane.setDataFieldAreaVisible(e.getStateChange() == ItemEvent.SELECTED);
            }
        });
        dataFieldAreaVisible.setSelected(_pivotTablePane.isDataFieldAreaVisible());

        JCheckBox rowFieldAreaVisible = new JCheckBox("Row Area Visible");
        rowFieldAreaVisible.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _pivotTablePane.setRowFieldAreaVisible(e.getStateChange() == ItemEvent.SELECTED);
            }
        });
        rowFieldAreaVisible.setSelected(_pivotTablePane.isRowFieldAreaVisible());

        JCheckBox columnFieldAreaVisible = new JCheckBox("Column Area Visible");
        columnFieldAreaVisible.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _pivotTablePane.setColumnFieldAreaVisible(e.getStateChange() == ItemEvent.SELECTED);
            }
        });
        columnFieldAreaVisible.setSelected(_pivotTablePane.isColumnFieldAreaVisible());

        JCheckBox rowFieldFilterable = new JCheckBox("Row Field Filterable");
        rowFieldFilterable.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _pivotTablePane.setRowFieldFilterable(e.getStateChange() == ItemEvent.SELECTED);
            }
        });
        rowFieldFilterable.setSelected(_pivotTablePane.isRowFieldFilterable());

        JCheckBox columnFieldFilterable = new JCheckBox("Column Field Filterable");
        columnFieldFilterable.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _pivotTablePane.setColumnFieldFilterable(e.getStateChange() == ItemEvent.SELECTED);
            }
        });
        columnFieldFilterable.setSelected(_pivotTablePane.isColumnFieldFilterable());

        JCheckBox dataFieldFilterable = new JCheckBox("Data Field Filterable");
        dataFieldFilterable.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _pivotTablePane.setDataFieldFilterable(e.getStateChange() == ItemEvent.SELECTED);
            }
        });
        dataFieldFilterable.setSelected(_pivotTablePane.isDataFieldFilterable());

        JCheckBox showFilterIcon = new JCheckBox("Show Filter Icon");
        showFilterIcon.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _pivotTablePane.setShowFilterIcon(e.getStateChange() == ItemEvent.SELECTED);
            }
        });
        showFilterIcon.setSelected(_pivotTablePane.isShowFilterIcon());

        JCheckBox columnAutoResizable = new JCheckBox("Column Auto Resizable");
        columnAutoResizable.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _pivotTablePane.setColumnAutoResizable(e.getStateChange() == ItemEvent.SELECTED);
            }
        });
        columnAutoResizable.setSelected(_pivotTablePane.isColumnAutoResizable());

        JCheckBox columnWidthRespectField = new JCheckBox("Column Width From Field");
        columnWidthRespectField.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _pivotTablePane.setRespectFieldPreferredWidth(e.getStateChange() == ItemEvent.SELECTED);
                _pivotTablePane.getPivotDataModel().getField("ProductSales").setPreferredWidth(30);
                _pivotTablePane.getPivotDataModel().getColumnHeaderTableModel().fireTableStructureChanged();
            }
        });
        columnWidthRespectField.setSelected(_pivotTablePane.isRespectFieldPreferredWidth());

        JCheckBox considerRowFieldLength = new JCheckBox("Auto Resize Counting Row Field");
        considerRowFieldLength.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _pivotTablePane.setConsiderRowFieldWidth(e.getStateChange() == ItemEvent.SELECTED);
            }
        });
        considerRowFieldLength.setSelected(_pivotTablePane.isConsiderRowFieldWidth());

        JCheckBox hideSummaryValues = new JCheckBox("Hide Summary Values");
        hideSummaryValues.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _pivotTablePane.getPivotDataModel().setHideSummaryValues(e.getStateChange() == ItemEvent.SELECTED);
            }
        });
        hideSummaryValues.setSelected(_pivotTablePane.getPivotDataModel().isHideSummaryValues());

        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.add(saveButton);
        panel.add(loadButton);
        panel.add(exportButton);
        panel.add(export2007Button);
        panel.add(autoResizeButton);
        panel.add(verifyButton);
        panel.add(getRowButton);
        panel.add(setRowButton);
        panel.add(getColumnButton);
        panel.add(setColumnButton);
        panel.add(styleCheckBox);
        panel.add(fieldChooserCheckBox);
        panel.add(filterChooserCheckBox);
        panel.add(grandTotalCheckBox);
        panel.add(rearrangable);
        panel.add(shrinkDataFields);
        panel.add(plainHeaderTables);
        panel.add(headerSelectionModel);
        panel.add(filterFieldAreaVisible);
        panel.add(dataFieldAreaVisible);
        panel.add(rowFieldAreaVisible);
        panel.add(columnFieldAreaVisible);
        panel.add(rowFieldFilterable);
        panel.add(columnFieldFilterable);
        panel.add(dataFieldFilterable);
        panel.add(hideFieldOnDragging);
        panel.add(showFilterIcon);
        panel.add(columnAutoResizable);
        panel.add(considerRowFieldLength);
        panel.add(columnWidthRespectField);
        panel.add(hideSummaryValues);

//        panel.add(new JButton(new AbstractAction("Repaint") {
//            public void actionPerformed(ActionEvent e) {
//                long start = System.currentTimeMillis();
//                for (int i = 0; i < 10; i++) {
//                    _pivotTablePane.paintImmediately(0, 0, _pivotTablePane.getWidth(), _pivotTablePane.getHeight());
//                }
//                long end = System.currentTimeMillis();
//                System.out.println(end - start);
//            }
//        }));

//        panel.add(new JButton(new AbstractAction("Update") {
//            public void actionPerformed(ActionEvent e) {
//                Thread runnable = new Thread() {
//                    public void run() {
//                        try {
//                            Thread.sleep(5000);
//                        }
//                        catch (InterruptedException e1) {
//                            e1.printStackTrace();
//                        }
//                        Runnable runnable = new Runnable() {
//                            public void run() {
//                                System.out.println("updated");
//                                _pivotTablePane.bothHeadersUpdated();
//                            }
//                        };
//                        SwingUtilities.invokeLater(runnable);
//                    }
//                };
//                runnable.start();
//            }
//        }));
        return panel;
    }

    public Component getDemoPanel() {
        final TableModel tableModel = createProductReportsTableModel();
//        final TableModel tableModel = createCustomerReportsTableModel();

        if (tableModel == null) {
            return new JLabel("Failed to read data file");
        }

        final CalculatedTableModel calculatedTableModel = setupProductDetailsCalculatedTableModel(tableModel);
//        final CalculatedTableModel calculatedTableModel = setupCustomerReportsCalculatedTableModel(tableModel);

        final PivotDataModel pivotDataModel = setupProductDetailsPivotDataModel(calculatedTableModel);
//        final PivotDataModel pivotDataModel = setupCustomerReportsPivotDataModel(calculatedTableModel);

        _pivotTablePane = new PivotTablePane(pivotDataModel);
//        _pivotTablePane.getDataTable().addMouseListener(new MouseAdapter() {
//            public void mouseClicked(MouseEvent e) {
//                int row = ((JTable) e.getSource()).rowAtPoint(e.getPoint());
//                int column = ((JTable) e.getSource()).columnAtPoint(e.getPoint());
//                java.util.List<Integer> list = _pivotTablePane.getPivotDataModel().getDataAt(row, column);
//                if (list != null) {
//                    for (Integer rowIndex : list) {
//                        System.out.println(pivotDataModel.getTableModel().getValueAt(rowIndex, pivotDataModel.getField("ProductSales").getModelIndex()));
//                    }
//                }
//            }
//        });
        _pivotTablePane.setColumnAutoResizable(true);
        return _pivotTablePane;
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
                        //noinspection CallToPrintStackTrace
                        e.printStackTrace();
                    }
                }
                for (int i = 0; i < 1; i++) {
                    data.add(lineData);
                }
            }
            while (true);
//            System.out.println(data.size());
            return new DefaultContextSensitiveTableModel(data, columnNames) {
                private static final long serialVersionUID = -4217547219462901851L;

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
                public ConverterContext getConverterContextAt(int row, int column) {
                    return column == 2 ? CurrencyConverter.CONTEXT : null;
                }
            };
        }
        catch (IOException e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
        return null;
    }

    private CalculatedTableModel setupProductDetailsCalculatedTableModel(TableModel tableModel) {
        CalculatedTableModel calculatedTableModel = new CalculatedTableModel(tableModel);
        calculatedTableModel.addAllColumns();
        SingleColumn column = new SingleColumn(tableModel, "ProductName");
        column.setObjectGrouper(new AbstractObjectGrouper() {
            public Class<?> getType() {
                return String.class;
            }

            public Object getValue(Object value) {
                if (value != null) {
                    String name = value.toString();
                    return name.substring(0, 1);
                }
                else {
                    return null;
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
        calculatedTableModel.addColumn(new AbstractCalculatedColumn(tableModel, "Sales2", BigDecimal.class) {
            private static final long serialVersionUID = -6309145400597924345L;

            public Object getValueAt(int rowIndex) {
                Object valueAt = getActualModel().getValueAt(rowIndex, 2);
                if (valueAt instanceof Float) {
                    return new BigDecimal((Float) valueAt);
                }
                return "--";
            }

            public int[] getDependingColumns() {
                return new int[]{2};
            }
        });

        return calculatedTableModel;
    }

    private PivotDataModel setupProductDetailsPivotDataModel(TableModel derivedTableModel) {
// use CachedTableModel will use more memory but the speed will be faster.
//        final PivotDataModel pivotDataModel = new PivotDataModel(new CachedTableModel(derivedTableModel));
        final PivotDataModel pivotDataModel = new PivotDataModel(derivedTableModel);

        // here is an example to add your own summary type.
        pivotDataModel.setSummaryCalculator(new DefaultSummaryCalculator() {
            final int SUMMARY_LAST_VALUE = PivotConstants.SUMMARY_RESERVED_MAX + 1;
            private Object _lastValue;

            @Override
            public void addValue(IPivotDataModel dataModel, PivotField field, int rowIndex, int columnIndex, Object v) {
                super.addValue(dataModel, field, rowIndex, columnIndex, v);
                if (v instanceof Number) {
                    _lastValue = ((Number) v).doubleValue();
                }
            }

            @Override
            public void clear() {
                super.clear();
                _lastValue = 0;
            }

            @Override
            public int getNumberOfSummaries() {
                return super.getNumberOfSummaries() + 1;
            }

            @Override
            public String getSummaryName(Locale locale, int type) {
                if (type == SUMMARY_LAST_VALUE) {
                    return "Last Value";
                }
                return super.getSummaryName(locale, type);
            }

            @Override
            public Object getSummaryResult(int type) {
                if (type == SUMMARY_LAST_VALUE) {
                    return _lastValue;
                }
                return super.getSummaryResult(type);
            }

            @Override
            public int[] getAllowedSummaries(Class<?> type, ConverterContext context) {
                int[] summaries = super.getAllowedSummaries(type, context);
                int[] newSummaries = new int[summaries.length + 1];
                System.arraycopy(summaries, 0, newSummaries, 0, summaries.length);
                newSummaries[summaries.length] = SUMMARY_LAST_VALUE;
                return newSummaries;
            }
        });

        pivotDataModel.getField("CategoryName").setAreaType(PivotField.AREA_ROW);
        pivotDataModel.getField("CategoryName").setAreaIndex(1);
        pivotDataModel.getField("CategoryName").setTitle("Category Name");
        pivotDataModel.getField("CategoryName").setSubtotalType(PivotField.SUBTOTAL_AUTOMATIC);

// sample code to add a filter icon to the field box if there are filters.        
//        pivotDataModel.getField("CategoryName").addPropertyChangeListener(PivotField.PROPERTY_SELECTED_POSSIBLE_VALUES, new PropertyChangeListener() {
//            public void propertyChange(PropertyChangeEvent evt) {
//                Object[] newValue = (Object[]) evt.getNewValue();
//                PivotField field = (PivotField) evt.getSource();
//                if (newValue == null) {
//                    field.setIcon(null);
//                }
//                else {
//                    field.setIcon(GridIconsFactory.getImageIcon(GridIconsFactory.Table.FILTER));
//                }
//            }
//        });

        pivotDataModel.getField("ProductName").setAreaType(PivotField.AREA_ROW);
        pivotDataModel.getField("ProductName").setAreaIndex(2);
        pivotDataModel.getField("ProductName").setTitle("Product Name");
        pivotDataModel.getField("ProductName").setNullValueAllowed(true);

        pivotDataModel.getField("ProductName (Alphabetical)").setTitle("Product Name (Alphabetical)");

        pivotDataModel.getField("ProductSales").setAreaType(PivotField.AREA_DATA);
        pivotDataModel.getField("ProductSales").setAreaIndex(1);
        pivotDataModel.getField("ProductSales").setTitle("Sales");
        pivotDataModel.getField("ProductSales").setSummaryType(PivotField.SUMMARY_SUM);

        pivotDataModel.getField("Sales2").setTitle("Sales2");
        pivotDataModel.getField("Sales2").setSummaryType(PivotField.SUMMARY_SUM);
        pivotDataModel.getField("Sales2").setConverterContext(CurrencyConverter.CONTEXT);

        pivotDataModel.getField("Year").setAreaType(PivotField.AREA_COLUMN);
        pivotDataModel.getField("Year").setAreaIndex(1);
        pivotDataModel.getField("Year").setSummaryType(PivotField.SUMMARY_NONE);
        pivotDataModel.getField("Year").setGrandTotalSummaryType(PivotField.SUMMARY_NONE);
        pivotDataModel.getField("Year").setSubtotalType(PivotField.SUBTOTAL_CUSTOM);
        pivotDataModel.getField("Year").setCustomSubtotals(new int[]{PivotField.SUMMARY_STDDEV, PivotField.SUMMARY_SUM});
        pivotDataModel.getField("Quarter").setAreaType(PivotField.AREA_COLUMN);
        pivotDataModel.getField("Quarter").setSubtotalType(PivotField.SUBTOTAL_CUSTOM);
        pivotDataModel.getField("Quarter").setCustomSubtotals(new int[]{PivotField.SUMMARY_STDDEV, PivotField.SUMMARY_SUM, PivotField.SUMMARY_MAX});
        pivotDataModel.getField("Quarter").setAreaIndex(2);
        pivotDataModel.getField("Quarter").setSummaryType(PivotField.SUMMARY_NONE);
        pivotDataModel.getField("Quarter").setGrandTotalSummaryType(PivotField.SUMMARY_NONE);
        pivotDataModel.getField("Month").setAreaType(PivotField.AREA_COLUMN);
        pivotDataModel.getField("Month").setAreaIndex(3);
        pivotDataModel.getField("Month").setSummaryType(PivotField.SUMMARY_NONE);
        pivotDataModel.getField("Month").setGrandTotalSummaryType(PivotField.SUMMARY_NONE);
        pivotDataModel.getField("Week").setSummaryType(PivotField.SUMMARY_NONE);
        pivotDataModel.getField("Week").setGrandTotalSummaryType(PivotField.SUMMARY_NONE);
        pivotDataModel.getField("ShippedDate").setSummaryType(PivotField.SUMMARY_NONE);
        pivotDataModel.getField("ShippedDate").setGrandTotalSummaryType(PivotField.SUMMARY_NONE);

        pivotDataModel.setShowGrandTotalForColumn(true);
        pivotDataModel.setShowGrandTotalForRow(true);

// the comments below are for performance testing.
//        System.gc();
//        System.gc();
//        System.gc();
//        long startMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
//        long start = System.currentTimeMillis();

        pivotDataModel.calculate();

//        System.out.println(System.currentTimeMillis() - start);
//        System.gc();
//        System.gc();
//        System.gc();
//        System.out.println((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) - startMem);
//
// find out how long it takes to read the table model as comparision
//        PivotField[] rows = pivotDataModel.getRowFields();
//        PivotField[] columns = pivotDataModel.getColumnFields();
//        int[] indices = new int[rows.length + columns.length];
//        for (int i = 0; i < rows.length; i++) {
//            PivotField field = rows[i];
//            indices[i] = field.getModelIndex();
//        }
//        for (int i = 0; i < columns.length; i++) {
//            PivotField field = columns[i];
//            indices[i + rows.length] = field.getModelIndex();
//        }
//
//        start = System.currentTimeMillis();
//        PivotDataSource model = pivotDataModel.getDataSource();
//        int rowCount = model.getRowCount();
//        for (int row = 0; row < rowCount; row++) {
//            for (int index : indices) {
//                model.getValueAt(row, index);
//            }
//        }
//        System.out.println(System.currentTimeMillis() - start);
        return pivotDataModel;
    }

    @SuppressWarnings({"UnusedDeclaration"})
    private TableModel createCustomerReportsTableModel() {
        try {
            InputStream resource = this.getClass().getClassLoader().getResourceAsStream("CustomerReports.txt.gz");
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
                    lineData.add(null); // product name
                else {
                    lineData.add(values[0]); // product name
                }
                if (values.length < 2)
                    lineData.add(null); // company name
                else
                    lineData.add(values[1]); // company name
                if (values.length < 3)
                    lineData.add(null); // order date
                else {
                    String value = values[2];
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
                if (values.length < 4)
                    lineData.add(null); // product amount
                else {
                    String value = values[3];
                    if (value.startsWith("$")) {
                        float f = Float.parseFloat(value.substring(1));
                        lineData.add(f); // product amount
                    }
                }
                for (int i = 0; i < 1; i++) {
                    data.add(lineData);
                }
            }
            while (true);
            return new DefaultContextSensitiveTableModel(data, columnNames) {
                private static final long serialVersionUID = 8718992031537900737L;

                @Override
                public Class<?> getColumnClass(int columnIndex) {
                    switch (columnIndex) {
                        case 0:
                        case 1:
                            return String.class;
                        case 2:
                            return Date.class;
                        case 3:
                            return Float.class;
                    }
                    return super.getColumnClass(columnIndex);
                }

                @Override
                public ConverterContext getConverterContextAt(int row, int column) {
                    return column == 3 ? CurrencyConverter.CONTEXT : null;
                }

                @Override
                public boolean isCellEditable(int row, int column) {
                    return true;
                }
            };
        }
        catch (IOException e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings({"UnusedDeclaration"})
    private CalculatedTableModel setupCustomerReportsCalculatedTableModel(TableModel tableModel) {
        CalculatedTableModel calculatedTableModel = new CalculatedTableModel(tableModel);
        calculatedTableModel.addAllColumns();
        calculatedTableModel.addColumn(new SingleColumn(tableModel, "OrderDate", "Year", new DateYearGrouper()));
        calculatedTableModel.addColumn(new SingleColumn(tableModel, "OrderDate", "Quarter", new DateQuarterGrouper()));
        calculatedTableModel.addColumn(new SingleColumn(tableModel, "OrderDate", "Month", new DateMonthGrouper()));
        calculatedTableModel.addColumn(new SingleColumn(tableModel, "OrderDate", "Week", new DateWeekOfYearGrouper()));
        calculatedTableModel.addColumn(new SingleColumn(tableModel, "OrderDate", "Day of Year", new DateDayOfYearGrouper()));
        calculatedTableModel.addColumn(new SingleColumn(tableModel, "OrderDate", "Day of Month", new DateDayOfMonthGrouper()));
        return calculatedTableModel;
    }

    @SuppressWarnings({"UnusedDeclaration"})
    private IPivotDataModel setupCustomerReportsPivotDataModel(CalculatedTableModel calculatedTableModel) {
        final IPivotDataModel pivotDataModel = new PivotDataModel(calculatedTableModel);

        pivotDataModel.getField("ProductName").setAreaType(PivotField.AREA_ROW);
        pivotDataModel.getField("ProductName").setAreaIndex(0);
        pivotDataModel.getField("ProductName").setTitle("Product Name");
        pivotDataModel.getField("ProductName").setSubtotalType(PivotField.SUBTOTAL_AUTOMATIC);
        pivotDataModel.getField("CompanyName").setAreaType(PivotField.AREA_ROW);
        pivotDataModel.getField("CompanyName").setAreaIndex(1);
        pivotDataModel.getField("CompanyName").setTitle("Company Name");
        pivotDataModel.getField("CompanyName").setSubtotalType(PivotField.SUBTOTAL_AUTOMATIC);

        pivotDataModel.getField("ProductAmount").setAreaType(PivotField.AREA_DATA);
        pivotDataModel.getField("ProductAmount").setSummaryType(PivotField.SUMMARY_SUM);
        pivotDataModel.getField("ProductAmount").setTitle("Amount");

        pivotDataModel.getField("Year").setAreaType(PivotField.AREA_COLUMN);
        pivotDataModel.getField("Year").setConverterContext(YearNameConverter.CONTEXT);
        pivotDataModel.getField("Year").setEditorContext(ContextSensitiveCellRenderer.CONTEXT_SENSITIVE_CONTEXT);
        pivotDataModel.getField("Year").setAreaIndex(1);
        pivotDataModel.getField("Year").setSubtotalType(PivotField.SUBTOTAL_CUSTOM);
        pivotDataModel.getField("Year").setCustomSubtotals(new int[]{PivotField.SUMMARY_STDDEV, PivotField.SUMMARY_SUM});
        pivotDataModel.getField("Quarter").setAreaType(PivotField.AREA_COLUMN);
        pivotDataModel.getField("Quarter").setConverterContext(QuarterNameConverter.CONTEXT);
        pivotDataModel.getField("Quarter").setEditorContext(ContextSensitiveCellRenderer.CONTEXT_SENSITIVE_CONTEXT);
        pivotDataModel.getField("Quarter").setAreaIndex(2);
        pivotDataModel.getField("Quarter").setSubtotalType(PivotField.SUBTOTAL_CUSTOM);
        pivotDataModel.getField("Quarter").setCustomSubtotals(new int[]{PivotField.SUMMARY_STDDEV, PivotField.SUMMARY_SUM, PivotField.SUMMARY_MEAN});
        pivotDataModel.getField("Month").setAreaType(PivotField.AREA_COLUMN);
        pivotDataModel.getField("Month").setConverterContext(MonthNameConverter.CONTEXT);
        pivotDataModel.getField("Month").setEditorContext(ContextSensitiveCellRenderer.CONTEXT_SENSITIVE_CONTEXT);
        pivotDataModel.getField("Month").setAreaIndex(3);
        pivotDataModel.calculate();
        return pivotDataModel;
    }

    @Override
    public String getDemoFolder() {
        return "P1.PivotTablePane";
    }

    static public void main(String[] s) {
        PivotDataModel.setValueImmutable(true);
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new PivotTableDemo());
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
        int result = chooser.showDialog(((JButton) e.getSource()).getTopLevelAncestor(), "Export");
        if (result == JFileChooser.APPROVE_OPTION) {
            _lastDirectory = chooser.getCurrentDirectory().getAbsolutePath();
            try {
                System.out.println("Exporting to " + chooser.getSelectedFile().getAbsolutePath());
                HssfPivotTableUtils.export(_pivotTablePane, chooser.getSelectedFile().getAbsolutePath(), "Pivot", false, new HssfTableUtils.DefaultCellValueConverter() {
                    @Override
                    public int getDataFormat(JTable table, Object value, int rowIndex, int columnIndex) {
                        if (value instanceof Double) {
                            return 2; // use 0.00 format
                            // in case you want custom format, you can use this code below to do it.
                            // return ((HSSFWorkbook) table.getClientProperty("HssfTableUtils.HSSFWorkbook")).createDataFormat().getFormat("#,##0.000000");
                        }
                        else if (value instanceof Date) {
                            return 0xe; // use m/d/yy
                        }
                        return super.getDataFormat(table, value, rowIndex, columnIndex);
                    }
                });
                System.out.println("Exported");
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void dispose() {
        _pivotTablePane = null;
    }
}
