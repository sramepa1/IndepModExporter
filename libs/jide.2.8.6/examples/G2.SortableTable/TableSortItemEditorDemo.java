/*
 * @(#)TableSortItemEditorDemo.java 9/23/2008
 *
 * Copyright 2002 - 2008 JIDE Software Inc. All rights reserved.
 *
 */

import com.jidesoft.grid.ISortableTableModel;
import com.jidesoft.grid.SortableTable;
import com.jidesoft.grid.SortableTableModel;
import com.jidesoft.grid.TableSortItemEditor;
import com.jidesoft.plaf.LookAndFeelFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Demoed Component: {@link com.jidesoft.grid.TableSortItemEditor} <br> Required jar files: jide-common.jar,
 * jide-grids.jar <br> Required L&F: any L&F
 */
public class TableSortItemEditorDemo extends CustomTableDemo {
    private TableSortItemEditor _sortItemEditor;
    private SortableTableModel _sortableTableModel;

    public TableSortItemEditorDemo() {
    }

    public String getName() {
        return "TableSortItemEditor Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_GRIDS;
    }

    @Override
    public int getAttributes() {
        return ATTRIBUTE_NONE;
    }

    @Override
    public String getDemoFolder() {
        return "G2.SortableTabledemo";
    }

    @Override
    public JTable createTable(DefaultTableModel tableModel) {
        _tableModel = setupProductDetailsCalculatedTableModel(tableModel);
        _sortableTableModel = new SortableTableModel(_tableModel);
        SortableTable table = new SortableTable(_sortableTableModel);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        return table;
    }

    @Override
    public JButton createUpdateButton() {
        return new JButton(new AbstractAction("Update the SortableTableModel") {
            public void actionPerformed(ActionEvent e) {
                final SortableTableModel.ColumnComparatorContextProvider provider = _sortItemEditor.getColumnComparatorContextProvider();
                final ISortableTableModel.SortItem[] sortItems = _sortItemEditor.getSortItems();
                _sortableTableModel.setColumnComparatorContextProvider(provider);
                for (int i = 0; i < sortItems.length; i++) {
                    ISortableTableModel.SortItem sortItem = sortItems[i];
                    _sortableTableModel.sortColumn(sortItem.column, i == 0, sortItem.ascending);
                }
            }
        });
    }

    @Override
    public Component createEditorPanel() {
        _sortItemEditor = new TableSortItemEditor(_tableModel);
        _sortItemEditor.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("TableSortItemEditor"),
                BorderFactory.createEmptyBorder(5, 10, 10, 10)));
        return _sortItemEditor;
    }

    static public void main(String[] s) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new TableSortItemEditorDemo());
    }
}