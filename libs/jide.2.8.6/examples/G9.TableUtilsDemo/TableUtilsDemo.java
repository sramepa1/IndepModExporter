/*
 * @(#)${NAME}
 *
 * Copyright 2002 - 2004 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.grid.*;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.utils.Lm;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Demoed Component: {@link com.jidesoft.grid.TableUtils} <br> Required jar files: jide-common.jar, jide-grids.jar <br>
 * Required L&F: any L&F
 */
public class TableUtilsDemo extends AbstractDemo {
    private SortableTable _table;

    private int[] _selections;

    private String _tablePref;

    private String _sortingOrders;


    public TableUtilsDemo() {
    }

    public String getName() {
        return "TableUtils Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_GRIDS;
    }

    @Override
    public String getDescription() {
        return "This is a demo of TableUtils. TableUtils is a utility class containing several useful methods related to JTable.\n" +
                "\nSave/load selections: saveSelection() method will save the current selection into an int array. You can load the selection back later. This is useful when selection is lost due to table update." +
                "To see a demo, Select several rows in the table, press \"Save Selection\" button, clear the selections by a mouse click on the table, then click on \"Load Selection\" button\n" +
                "\nSave/load table preference: table preference includes table column width and order. After user resized or reordred the table columns, user won't like the information to be persisted so then when table is shown again, it is the same as before. You can use setTablePreference() and getTablePreference() to archive this. " +
                "To see a demo, resize and reorder table columns, press \"Save Preference\" button, resize and reorder more columns, then click on \"Load Preference\" button.\n" +
                "\nThere are several additional methods beginning with \"synchronize...\" which are not covered in this demo. Those methods are for synchronization of several tables such as scrolling, selection, row heights etc. You can find usage of those methods in the demos of TableScrollPane and TableSplitPane.\n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.grid.TableUtils";
    }

    @Override
    public Component getOptionsPanel() {
        JButton saveSelectionButton = new JButton(new AbstractAction("Save Selection") {
            public void actionPerformed(ActionEvent e) {
                _selections = TableUtils.saveSelection(_table);
                Lm.showPopupMessageBox("<HTML>The selection has been saved. You can clear the selection in the table now. " +
                        "<BR>After you clear it, press \"Load Selection\" to restore the saved selection.</HTML>");
            }
        });
        JButton loadSelectionButton = new JButton(new AbstractAction("Load Selection") {
            public void actionPerformed(ActionEvent e) {
                TableUtils.loadSelection(_table, _selections);
            }
        });
        JButton savePrefButton = new JButton(new AbstractAction("Save Preference") {
            public void actionPerformed(ActionEvent e) {
                _tablePref = TableUtils.getTablePreferenceByName(_table);
                Lm.showPopupMessageBox("<HTML>The table column width and column order information has been saved. You can change width and order in the table now. " +
                        "<BR>After you are done with it, press \"Load Preference\" to restore the saved column width and order.</HTML>");
            }
        });
        JButton loadPrefButton = new JButton(new AbstractAction("Load Preference") {
            public void actionPerformed(ActionEvent e) {
                TableUtils.setTablePreferenceByName(_table, _tablePref);
            }
        });
        JButton saveSortingPrefButton = new JButton(new AbstractAction("Save Sorting Orders") {
            public void actionPerformed(ActionEvent e) {
                _sortingOrders = TableUtils.getSortableTablePreference(_table, true);
                Lm.showPopupMessageBox("<HTML>The table column width and column order information has been saved. You can change width and order in the table now. " +
                        "<BR>After you are done with it, press \"Load Preference\" to restore the saved column width and order.</HTML>");
            }
        });
        JButton loadSortingPrefButton = new JButton(new AbstractAction("Load Sorting Orders") {
            public void actionPerformed(ActionEvent e) {
                TableUtils.setSortableTablePreference(_table, _sortingOrders, true);
            }
        });
        JButton loadABCDEFModelButton = new JButton(new AbstractAction("ABCDEF") {
            public void actionPerformed(ActionEvent e) {
                _table.setModel(new SampleTableModel2());
            }
        });
        JButton loadABCEFModelButton = new JButton(new AbstractAction("ABCEF") {
            public void actionPerformed(ActionEvent e) {
                _table.setModel(new SampleTableModel3());
            }
        });

        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(saveSelectionButton);
        panel.add(loadSelectionButton);
        panel.add(savePrefButton);
        panel.add(loadPrefButton);
        panel.add(saveSortingPrefButton);
        panel.add(loadSortingPrefButton);
        panel.add(loadABCDEFModelButton);
        panel.add(loadABCEFModelButton);
        return panel;
    }

    public Component getDemoPanel() {
        _table = new SortableTable(new SampleTableModel1());
        TableHeaderPopupMenuInstaller installer = new TableHeaderPopupMenuInstaller(_table);
        installer.addTableHeaderPopupMenuCustomizer(new AutoResizePopupMenuCustomizer());
        installer.addTableHeaderPopupMenuCustomizer(new TableColumnChooserPopupMenuCustomizer());
        _table.setRowSelectionAllowed(true);
        _table.setColumnSelectionAllowed(true);
        _table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        return new JScrollPane(_table);
    }

    public static class SampleTableModel1 extends AbstractTableModel {

        public int getColumnCount() {
            return 3;
        }

        public int getRowCount() {
            return 8;
        }

        @Override
        public String getColumnName(int column) {
            switch (column) {
                case 0:
                    return "A";
                case 1:
                    return "B";
                case 2:
                    return "C";
            }
            return "";
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            return rowIndex + "," + columnIndex;
        }
    }

    public static class SampleTableModel2 extends AbstractTableModel {

        public int getColumnCount() {
            return 6;
        }

        public int getRowCount() {
            return 8;
        }

        @Override
        public String getColumnName(int column) {
            switch (column) {
                case 0:
                    return "A";
                case 1:
                    return "B";
                case 2:
                    return "C";
                case 3:
                    return "D";
                case 4:
                    return "E";
                case 5:
                    return "F";
            }
            return "";
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            return rowIndex + "," + columnIndex;
        }
    }

    public static class SampleTableModel3 extends AbstractTableModel {

        public int getColumnCount() {
            return 5;
        }

        public int getRowCount() {
            return 8;
        }

        @Override
        public String getColumnName(int column) {
            switch (column) {
                case 0:
                    return "A";
                case 1:
                    return "B";
                case 2:
                    return "C";
                case 3:
                    return "E";
                case 4:
                    return "F";
            }
            return "";
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            return rowIndex + "," + columnIndex;
        }
    }

    @Override
    public String getDemoFolder() {
        return "G9.TableUtilsDemo";
    }

    static public void main(String[] s) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new TableUtilsDemo());
    }
}