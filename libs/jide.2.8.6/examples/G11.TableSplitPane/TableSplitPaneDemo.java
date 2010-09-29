/*
 * @(#)TableSplitPaneDemo.java
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.grid.*;
import com.jidesoft.plaf.LookAndFeelFactory;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;

/**
 * Demoed Component: {@link com.jidesoft.grid.TableSplitPane} <br> Required jar files:
 * jide-common.jar, jide-grids.jar <br> Required L&F: any L&F
 */
public class TableSplitPaneDemo extends AbstractDemo {
    public TableSplitPaneDemo() {
    }

    public String getName() {
        return "TableSplitPane";
    }

    @Override
    public String getDescription() {
        return "This is a demo of TableSplitPane. TableSplitPane is a special component which supports spanning one large table model into multiple tables while still keeping seamless navigation, sorting, scrolling etc.\n" +
                "\nIt also shows you how to create a nested table column header which is the feature of JideTable (see the last table).\n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.grid.TableSplitPane\n" +
                "com.jidesoft.grid.JideTable";
    }

    public String getProduct() {
        return PRODUCT_NAME_GRIDS;
    }

    public Component getDemoPanel() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(800, 400));
        panel.setLayout(new BorderLayout());
        panel.add(createTablePane(), BorderLayout.CENTER);
        return panel;
    }

    private TableSplitPane createTablePane() {
        MultiTableModel model = new SchedulerTableModelEx();
        MultiTableModel totalModel = new SchedulerTotalTableModel();
        TableSplitPane pane = new TableSplitPane(model, totalModel, false) {
            @Override
            public TableCustomizer getTableCustomizer() {
                return new TableCustomizer() {
                    public void customize(JTable table) {
                        if (table instanceof JideTable) {
                            ((JideTable) table).setNestedTableHeader(true);
                        }
                    }
                };
            }
        };
        TableScrollPane[] panes = pane.getTableScrollPanes();

        TableScrollPane scrollPane = panes[2];
        JTable table = scrollPane.getMainTable();

        // create nested table columns
        TableColumnModel cm = table.getColumnModel();
        TableColumnGroup first = new TableColumnGroup("Sum 1 - 4");
        first.add(cm.getColumn(0));
        first.add(cm.getColumn(1));
        first.add(cm.getColumn(2));
        first.add(cm.getColumn(3));
        TableColumnGroup second = new TableColumnGroup("Sum 5 - 9");
        second.add(cm.getColumn(4));
        second.add(cm.getColumn(5));
        second.add(cm.getColumn(6));
        second.add(cm.getColumn(7));
        second.add(cm.getColumn(8));
        TableColumnGroup all = new TableColumnGroup("Sum 1 - 9");
        all.add(first);
        all.add(second);
        NestedTableHeader header = (NestedTableHeader) table.getTableHeader();
        header.addColumnGroup(all);

        return pane;
    }

    @Override
    public String getDemoFolder() {
        return "G11.TableSplitPane";
    }

    static public void main(String[] s) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new TableSplitPaneDemo());
    }

    // 0 header
    // 1 - 10
    // 11 - 20
    // 21 - 30
    static String[] HEADER = new String[]{
            " ",
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
            "Week 1", "Week 2", "Week 3", "Week 4", "Week 5", "Week 6", "Week 7", "Week 8", "Week 9", "Week 10",
            "Sum 1", "Sum 2", "Sum 3", "Sum 4", "Sum 5", "Sum 6", "Sum 7", "Sum 8", "Sum 9", "Sum 10"
    };

    static Object[][] ENTRIES = new Object[][]{
    };

    class SchedulerTableModelEx extends AbstractMultiTableModel {
        @Override
        public String getColumnName(int column) {
            return HEADER[column];
        }

        public int getColumnCount() {
            return HEADER.length;
        }

        public int getRowCount() {
            return 20;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return String.class;
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            return "(" + rowIndex + ", " + columnIndex + ")";
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex != 0 && columnIndex != HEADER.length - 1; // not last one and not first one
        }

        public int getTableIndex(int columnIndex) {
            if (columnIndex <= 10) {
                return 0;
            }
            else if (columnIndex <= 20) {
                return 1;
            }
            else {
                return 2;
            }
        }

        public int getColumnType(int column) {
            if (column == 0) {
                return HEADER_COLUMN;
            }
            else if (column == 30) {
                return FOOTER_COLUMN;
            }
            else {
                return REGULAR_COLUMN;
            }
        }
    }

    class SchedulerTotalTableModel extends AbstractMultiTableModel {
        public int getColumnCount() {
            return HEADER.length;
        }

        public int getRowCount() {
            return 1;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return String.class;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            return "Total (" + rowIndex + ", " + columnIndex + ")";
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return true;
        }

        public int getTableIndex(int columnIndex) {
            if (columnIndex <= 10) {
                return 0;
            }
            else if (columnIndex <= 20) {
                return 1;
            }
            else {
                return 2;
            }
        }

        public int getColumnType(int column) {
            if (column == 0) {
                return HEADER_COLUMN;
            }
            else if (column == 30) {
                return FOOTER_COLUMN;
            }
            else {
                return REGULAR_COLUMN;
            }
        }
    }
}
