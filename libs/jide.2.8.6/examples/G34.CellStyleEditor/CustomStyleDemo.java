/*
 * @(#)CustomStyleDemo.java 9/10/2008
 *
 * Copyright 2002 - 2008 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.grid.ColumnCellStyleProvider;
import com.jidesoft.grid.SortableTable;
import com.jidesoft.grid.TableCellStyleEditor;
import com.jidesoft.plaf.LookAndFeelFactory;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Demoed Component: {@link com.jidesoft.pivot.PivotTablePane} <br> Required jar files: jide-common.jar, jide-grids.jar
 * <br> Required L&F: any L&F
 */
public class CustomStyleDemo extends CustomTableDemo {
    private TableCellStyleEditor _styleEditorPanel;
    private ColumnCellStyleProvider _columnCellStyleProvider;

    public CustomStyleDemo() {
    }

    public String getName() {
        return "Custom Style Demo";
    }

    @Override
    public int getAttributes() {
        return ATTRIBUTE_NONE;
    }

    public String getProduct() {
        return PRODUCT_NAME_GRIDS;
    }

    @Override
    public String getDemoFolder() {
        return "G34.CellStyleEditor";
    }

    @Override
    public SortableTable createTable(DefaultTableModel tableModel) {
        _tableModel = setupProductDetailsCalculatedTableModel(tableModel);
        _columnCellStyleProvider = new ColumnCellStyleProvider(_tableModel);
        final SortableTable table = new SortableTable(_tableModel);
        table.setCellStyleProvider(_columnCellStyleProvider);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        return table;
    }

    @Override
    public JButton createUpdateButton() {
        return new JButton(new AbstractAction("Update the Cell Styles") {
            public void actionPerformed(ActionEvent e) {
                _columnCellStyleProvider.setCellStyles(_styleEditorPanel.getCellStyles());
                ((AbstractTableModel) _tableModel).fireTableDataChanged();
            }
        });
    }

    @Override
    public Component createEditorPanel() {
        _styleEditorPanel = new TableCellStyleEditor(_tableModel);
        _styleEditorPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("TableCellStyleEditor"),
                BorderFactory.createEmptyBorder(5, 10, 10, 10)));
        return _styleEditorPanel;
    }

    static public void main(String[] s) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new CustomStyleDemo());
    }
}