/*
 * @(#)TableScrollPaneDemo.java
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.grid.*;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.JideTabbedPane;
import com.jidesoft.utils.Lm;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Demoed Component: {@link com.jidesoft.grid.TableScrollPane} <br> Required jar files: jide-common.jar, jide-grids.jar
 * <br> Required L&F: any L&F
 */
public class TableScrollPaneDemo extends AbstractDemo {

    protected MultiTableModel _totalModel;
    protected MultiTableModel _model;
    public TableScrollPane _pane;
    private String _tablePref;
    private static final long serialVersionUID = -5850105228695796397L;

    public TableScrollPaneDemo() {
    }

    public String getName() {
        return "TableScrollPane";
    }

    @Override
    public String getDescription() {
        return "This is a demo of TableScrollPane. TableScrollPane is a special component which supports table row header, row footer and column footer.\n" +
                "\nIt also shows how to archive multiple line cell renderer. Try to resize \"Task\" column to make it smaller and see what happens.\n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.grid.TableScrollPane\n" +
                "com.jidesoft.grid.JideTable";
    }

    public String getProduct() {
        return PRODUCT_NAME_GRIDS;
    }

    @Override
    public Component getOptionsPanel() {
        JPanel checkBoxPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        JCheckBox allowMultiSelection = new JCheckBox("Allow Multi Selection in Different Tables");
        allowMultiSelection.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _pane.setAllowMultiSelectionInDifferentTable(e.getStateChange() == ItemEvent.SELECTED);
            }
        });
        allowMultiSelection.setSelected(_pane.isAllowMultiSelectionInDifferentTable());
        checkBoxPanel.add(allowMultiSelection);

        JButton savePrefButton = new JButton(new AbstractAction("Save Preference") {
            private static final long serialVersionUID = -1383609484861319779L;

            public void actionPerformed(ActionEvent e) {
                _tablePref = TableUtils.getTablePreferenceByName(_pane);
                Lm.showPopupMessageBox("<HTML>The table column width and column order information has been saved. You can change width and order in the table now. " +
                        "<BR>After you are done with it, press \"Load Preference\" to restore the saved column width and order.</HTML>");
            }
        });
        JButton loadPrefButton = new JButton(new AbstractAction("Load Preference") {
            private static final long serialVersionUID = 8541271321784337549L;

            public void actionPerformed(ActionEvent e) {
                TableUtils.setTablePreferenceByName(_pane, _tablePref);
            }
        });
        JButton loadWidthButton = new JButton(new AbstractAction("Load Width only") {
            private static final long serialVersionUID = -6178675632788894514L;

            public void actionPerformed(ActionEvent e) {
                TableUtils.setTableColumnWidthByName(_pane, _tablePref);
            }
        });

        checkBoxPanel.add(savePrefButton);
        checkBoxPanel.add(loadPrefButton);
        checkBoxPanel.add(loadWidthButton);
        return checkBoxPanel;
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
        tabbedPane.addTab("Timesheet Example", createTablePane());
        SortableTable sortableTable = new SortableTable(new TimeSheetTableModelEx());
        sortableTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tabbedPane.addTab("Table in JScrollPane (for comparison)", new JScrollPane(sortableTable));
        return tabbedPane;

    }

    private TableScrollPane createTablePane() {
        _model = new TimeSheetTableModelEx();
        _totalModel = new TimeSheetTotalTableModel(_model);
        _pane = new TableScrollPane(_model, _totalModel, true) {

            @Override
            protected JTable createTable(TableModel model, boolean sortable) {
                return new SortableTable(model) {
                    @Override
                    public boolean getScrollableTracksViewportWidth() {
                        if (autoResizeMode != AUTO_RESIZE_OFF) {
                            if (getParent() instanceof JViewport) {
                                return getParent().getWidth() > getPreferredSize().width;
                            }
                        }
                        return false;
                    }
                };
            }

            @Override
            public TableCustomizer getTableCustomizer() {
                return new TableCustomizer() {
                    public void customize(JTable table) {
// uncomment the following line to allow multiline line renderer and make row height adjust automatically
//                        ((JideTable) table).setRowAutoResizes(true);
// uncomment the following line to have a fixed row height
                        ((JideTable) table).setRowAutoResizes(true);
                        ((JideTable) table).setRowResizable(true);
                    }
                };
            }
        };
        _pane.getRowHeaderTable().getColumnModel().getColumn(0).setPreferredWidth(25);
        _pane.getRowHeaderTable().getColumnModel().getColumn(1).setPreferredWidth(100);
        _pane.getRowHeaderTable().getColumnModel().getColumn(2).setPreferredWidth(60);
        _pane.getRowHeaderTable().getColumnModel().getColumn(3).setPreferredWidth(60);
        _pane.getRowHeaderTable().getColumnModel().getColumn(4).setPreferredWidth(40);
        _pane.getRowHeaderTable().getColumnModel().getColumn(0).setCellRenderer(new SortTableHeaderRenderer());
        _pane.getRowHeaderTable().setBackground(new Color(255, 254, 203));
        _pane.getMainTable().setBackground(new Color(255, 254, 203));
        _pane.getRowFooterTable().setBackground(new Color(178, 178, 142));

        ((SortableTableModel) _pane.getRowHeaderTable().getModel()).setColumnSortable(0, false);

        TableHeaderPopupMenuInstaller installer = new TableHeaderPopupMenuInstaller(_pane.getMainTable());
        installer.addTableHeaderPopupMenuCustomizer(new AutoResizePopupMenuCustomizer());
        installer.addTableHeaderPopupMenuCustomizer(new TableColumnChooserPopupMenuCustomizer());

        TableHeaderPopupMenuInstaller installer2 = new TableHeaderPopupMenuInstaller(_pane.getRowFooterTable());
        installer2.addTableHeaderPopupMenuCustomizer(new AutoResizePopupMenuCustomizer());
        installer2.addTableHeaderPopupMenuCustomizer(new TableColumnChooserPopupMenuCustomizer());

        TableHeaderPopupMenuInstaller installer3 = new TableHeaderPopupMenuInstaller(_pane.getRowHeaderTable());
        installer3.addTableHeaderPopupMenuCustomizer(new AutoResizePopupMenuCustomizer());
        installer3.addTableHeaderPopupMenuCustomizer(new TableColumnChooserPopupMenuCustomizer());

        JLabel label = new JLabel("Total: ");
        label.setHorizontalAlignment(SwingConstants.TRAILING);
        label.setVerticalAlignment(SwingConstants.TOP);
        _pane.setCorner(JScrollPane.LOWER_LEFT_CORNER, label);

        _pane.getColumnFooterTable().setBackground(new Color(178, 178, 142));

        return _pane;
    }

    @Override
    public String getDemoFolder() {
        return "G10.TableScrollPane";
    }

    static public void main(String[] s) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new TableScrollPaneDemo());
    }

    static String[] HEADER = new String[]{
            "#", "Task", "Billing", "Project", "Location", "Mon 8-2", "Tue 8-3", "Wed 8-4", "Thu 8-5", "Fri 8-6", "Sat 8-7", "Sun 8-8", "Task Total"
    };

    static String[] FOOTER = new String[]{
            "Mon 8-2", "Tue 8-3", "Wed 8-4", "Thu 8-5", "Fri 8-6", "Sat 8-7", "Sun 8-8", "", "", "", "", "", "", ""
    };

    static Object[][] ENTRIES = new Object[][]{
            new Object[]{null, "Administration", "Non-Billable", "Project 1", "NY", "", "2", "", "2", "", "", "", null},
            new Object[]{null, "Sick", "Time-off", "", "NY", "", "", "", "", "4", "", "", null},
            new Object[]{null, "Vacation", "Time-off", "", "NY", "", "", "", "", "8", "", "", null},
            new Object[]{null, "Development", "Billable", "Project 2", "NJ", "", "", "", "8", "", "", "", null},
            new Object[]{null, "Website design", "Billable", "Project 2", "NJ", "4", "", "", "", "", "", "", null},
            new Object[]{null, "Customer Support", "Billable", "Project 2", "NJ", "4", "", "", "", "", "", "", null}
    };

    // Sick, Vacation,

    class TimeSheetTableModelEx extends AbstractMultiTableModel {
        private static final long serialVersionUID = 7142342324546147914L;

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
            if (rowIndex >= ENTRIES.length) {
                // skip for now
            }
            else if (columnIndex == 0) {
                // no editable
            }
            else if (columnIndex == HEADER.length - 1) { //last column
                // no editable
            }
            else {
                ENTRIES[rowIndex][columnIndex] = aValue;
                if (columnIndex >= 5 && columnIndex <= 11) {
                    // update column total
                    ((AbstractTableModel) _totalModel).fireTableCellUpdated(0, columnIndex - 5);
                    // update row total
                    fireTableCellUpdated(rowIndex, getColumnCount() - 1);
                }
            }
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            if (columnIndex == 0) {
                return "" + (TableModelWrapperUtils.getRowAt(_pane.getRowHeaderTable().getModel(), rowIndex) + 1);
            }
            else if (rowIndex >= ENTRIES.length) {
                if (columnIndex >= 5 && columnIndex <= 11) {
                    return "";
                }
                else if (columnIndex == HEADER.length - 1) {
                    return "";
                }
                else {
                    return "";
                }
            }
            else if (columnIndex == HEADER.length - 1) { //last column
                double total = 0.0;
                for (int i = 5; i <= 11; i++) {
                    try {
                        try {
                            total += Double.parseDouble((String) ENTRIES[rowIndex][i]);
                        }
                        catch (NumberFormatException e) {
                            //null
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return "" + total;
            }
            else {
                return ENTRIES[rowIndex][columnIndex];
            }
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex >= 5 && columnIndex != HEADER.length - 1; // only cells in the main table are editable.
        }

        public int getTableIndex(int columnIndex) {
            return 0;
        }

        public int getColumnType(int column) {
            if (column <= 4) {
                return HEADER_COLUMN;
            }
            else if (column == HEADER.length - 1) {
                return FOOTER_COLUMN;
            }
            else {
                return REGULAR_COLUMN;
            }
        }
    }

    class TimeSheetTotalTableModel extends AbstractMultiTableModel {
        TableModel _model;
        private static final long serialVersionUID = -9132647394140127017L;

        public TimeSheetTotalTableModel(TableModel model) {
            _model = model;
        }

        @Override
        public String getColumnName(int column) {
            return FOOTER[column];
        }

        public int getColumnCount() {
            return 7;
        }

        public int getRowCount() {
            return 1;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return String.class;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            double total = 0.0;
            for (int i = 0; i < _model.getRowCount(); i++) {
                try {
                    total += Double.parseDouble((String) _model.getValueAt(i, columnIndex + 5));
                }
                catch (NumberFormatException e) {
                    //null
                }
            }
            return "" + total;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }

        public int getColumnType(int column) {
            return REGULAR_COLUMN;
        }

        public int getTableIndex(int columnIndex) {
            return 0;
        }
    }
}
