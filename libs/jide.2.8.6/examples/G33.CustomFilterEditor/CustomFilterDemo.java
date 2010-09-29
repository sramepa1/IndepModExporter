/*
 * @(#)CustomFilterDemo.java 9/10/2008
 *
 * Copyright 2002 - 2008 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.grid.FilterableTableModel;
import com.jidesoft.grid.IFilterableTableModel;
import com.jidesoft.grid.SortableTable;
import com.jidesoft.grid.TableCustomFilterEditor;
import com.jidesoft.plaf.LookAndFeelFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Demoed Component: {@link com.jidesoft.pivot.PivotTablePane} <br> Required jar files: jide-common.jar, jide-grids.jar
 * <br> Required L&F: any L&F
 */
public class CustomFilterDemo extends CustomTableDemo {
    private FilterableTableModel _filterableTableModel;
    private TableCustomFilterEditor _tableCustomFilterEditor;

    public CustomFilterDemo() {
    }

    public String getName() {
        return "Custom Filter Demo";
    }

    @Override
    public String getDemoFolder() {
        return "G33.CustomFilterEditor";
    }

    public String getProduct() {
        return PRODUCT_NAME_GRIDS;
    }

    @Override
    public int getAttributes() {
        return ATTRIBUTE_NONE;
    }

    @Override
    public JTable createTable(DefaultTableModel tableModel) {
        _tableModel = setupProductDetailsCalculatedTableModel(tableModel);
        _filterableTableModel = new FilterableTableModel(_tableModel);
        final SortableTable table = new SortableTable(_filterableTableModel);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        return table;
    }

    @Override
    public JButton createUpdateButton() {
        return new JButton(new AbstractAction("Update the FilterableTableModel") {
            public void actionPerformed(ActionEvent e) {
                _filterableTableModel.clearFilters();
                IFilterableTableModel.FilterItem[] items = _tableCustomFilterEditor.getFilterItems();
                for (IFilterableTableModel.FilterItem item : items) {
                    _filterableTableModel.addFilter(item);
                }
                _filterableTableModel.setFiltersApplied(true);
            }
        });
    }

    @Override
    public Component createEditorPanel() {
        _tableCustomFilterEditor = new TableCustomFilterEditor(_tableModel);
        _tableCustomFilterEditor.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("TableCustomFilterEditor"),
                BorderFactory.createEmptyBorder(5, 10, 10, 10)));
        return _tableCustomFilterEditor;
    }

    static public void main(String[] s) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new CustomFilterDemo());
    }
}