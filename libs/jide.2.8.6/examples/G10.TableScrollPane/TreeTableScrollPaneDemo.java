/*
 * @(#)TreeTableScrollPaneDemo.java 1/8/2006
 *
 * Copyright 2002 - 2006 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.grid.*;
import com.jidesoft.hssf.HssfTableScrollPaneUtils;
import com.jidesoft.hssf.HssfTableUtils;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.JideTabbedPane;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Demoed Component: {@link com.jidesoft.grid.TableScrollPane} <br> Required jar files: jide-common.jar, jide-grids.jar
 * <br> Required L&F: any L&F
 */
public class TreeTableScrollPaneDemo extends AbstractDemo {

    protected MultiTableModel _model;
    private String _lastDirectory = ".";
    private TableScrollPane _scrollPane;
    private static final long serialVersionUID = 633721222142039212L;

    public TreeTableScrollPaneDemo() {
    }

    public String getName() {
        return "TableScrollPane (TreeTable)";
    }

    @Override
    public String getDescription() {
        return "This is a demo of TableScrollPane. TableScrollPane is a special component which supports table row header, row footer and column footer.\n" +
                "\nIn this particular demo, we show you how to use TreeTable in row header in a TableScrollPane.\n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.grid.TableScrollPane\n" +
                "com.jidesoft.grid.JideTable";
    }

    public String getProduct() {
        return PRODUCT_NAME_GRIDS;
    }

    public Component getDemoPanel() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(800, 400));
        panel.setLayout(new BorderLayout());
        panel.add(createTabbedPane(), BorderLayout.CENTER);
        return panel;
    }

    private Component createTabbedPane() {
        JideTabbedPane tabbedPane = new JideTabbedPane();
        tabbedPane.setTabShape(JideTabbedPane.SHAPE_BOX);
        tabbedPane.addTab("TreeTable in TableScrollPane", createTablePane());
        return tabbedPane;

    }

    private TableScrollPane createTablePane() {
        List<DummyRow> list = new ArrayList<DummyRow>();
        DummyRow w1 = new DummyRow("Week 1");
        for (int i = 0; i < 7; i++) {
            w1.addChild(new DummyRow("Day " + (i + 1)));
        }
        list.add(w1);
        DummyRow w2 = new DummyRow("Week 2");
        for (int i = 0; i < 7; i++) {
            w2.addChild(new DummyRow("Day " + (i + 1)));
        }
        list.add(w2);
        DummyRow w3 = new DummyRow("Week 3");
        for (int i = 0; i < 7; i++) {
            w3.addChild(new DummyRow("Day " + (i + 1)));
        }
        list.add(w3);

        _model = new TreeTableModel<DummyRow>(list) {
            private static final long serialVersionUID = 8706721423900949194L;

            @Override
            public int getColumnType(int columnIndex) {
                return columnIndex < 1 ? HEADER_COLUMN : REGULAR_COLUMN;
            }

            @Override
            public String getColumnName(int column) {
                if (column == 0) {
                    return "Day";
                }
                else {
                    return "Product " + column;
                }
            }

            public int getColumnCount() {
                return 21;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? String.class : Integer.class;
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return true;
            }
        };
        _scrollPane = new TableScrollPane(_model, true) {
            @Override
            protected JTable createTable(TableModel model, boolean sortable, int type) {
                if (type == MultiTableModel.HEADER_COLUMN) {
                    TreeTable treeTable = new TreeTable(model);
                    treeTable.setExportCollapsedRowsToExcel(true);
                    return treeTable;
                }
                return super.createTable(model, sortable, type);
            }

            @Override
            public TableCustomizer getTableCustomizer() {
                return new TableCustomizer() {
                    public void customize(JTable table) {
                        table.setRowHeight(18);
                        if (table instanceof TreeTable) {
                            ((TreeTable) table).setShowTreeLines(true);
                        }
                    }
                };
            }
        };
        new TableHeaderPopupMenuInstaller(_scrollPane.getRowHeaderTable()) {
            @Override
            protected void customizeMenuItems(final JTableHeader header, final JPopupMenu popup, final int clickingColumn) {
                super.customizeMenuItems(header, popup, clickingColumn);

                addSeparatorIfNecessary(popup);

                final JMenuItem export = new JMenuItem(new AbstractAction("Export to Excel 2003 format") {
                    private static final long serialVersionUID = 2581042425782595535L;

                    public void actionPerformed(ActionEvent e) {
                        _scrollPane.putClientProperty(HssfTableUtils.CLIENT_PROPERTY_EXCEL_OUTPUT_FORMAT, HssfTableUtils.EXCEL_OUTPUT_FORMAT_2003);
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
                        _scrollPane.putClientProperty(HssfTableUtils.CLIENT_PROPERTY_EXCEL_OUTPUT_FORMAT, HssfTableUtils.EXCEL_OUTPUT_FORMAT_2007);
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
        return _scrollPane;
    }

    @Override
    public String getDemoFolder() {
        return "G10.TableScrollPane";
    }

    @Override
    public int getAttributes() {
        return ATTRIBUTE_UPDATED;
    }

    static public void main(String[] s) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new TreeTableScrollPaneDemo());
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
                HssfTableScrollPaneUtils.export(_scrollPane, chooser.getSelectedFile().getAbsolutePath(), "TreeTableScrollPane", false);
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    static class DummyRow extends DefaultExpandableRow {

        private String _name;
        private List<Integer> _values;

        public DummyRow(String name) {
            _name = name;
            _values = new ArrayList<Integer>();
            for (int i = 0; i < 20; i++) {
                _values.add((int) (Math.random() * 40));
            }
        }

        public Object getValueAt(int columnIndex) {
            if (columnIndex == 0) {
                return _name;
            }
            return _values.get(columnIndex - 1);
        }
    }
}
