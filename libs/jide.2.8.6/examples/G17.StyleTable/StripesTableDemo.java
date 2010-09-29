/*
 * @(#)RowStipesTableDemo.java 3/15/2008
 *
 * Copyright 2002 - 2008 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.grid.CellStyleTable;
import com.jidesoft.grid.ColumnStripeTableStyleProvider;
import com.jidesoft.grid.RowStripeTableStyleProvider;
import com.jidesoft.grid.SortableTable;
import com.jidesoft.plaf.LookAndFeelFactory;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Demoed Component: {@link com.jidesoft.grid.CellStyleTable} <br> Required jar files: jide-common.jar, jide-grids.jar
 * <br> Required L&F: any L&F
 */
public class StripesTableDemo extends AbstractDemo {
    protected static final Color BACKGROUND1 = new Color(253, 253, 244);
    protected static final Color BACKGROUND2 = new Color(230, 230, 255);
    protected static final Color BACKGROUND3 = new Color(210, 255, 210);

    protected static final Color FOREGROUND1 = new Color(0, 0, 10);

    protected static final Color BACKGROUND4 = new Color(0, 128, 0);
    protected static final Color FOREGROUND4 = new Color(255, 255, 255);

    public CellStyleTable _table;

    public StripesTableDemo() {
    }

    public String getName() {
        return "CellStyleTable Demo (Row Stipes)";
    }

    public String getProduct() {
        return PRODUCT_NAME_GRIDS;
    }

    @Override
    public String getDescription() {
        return "This is a demo of CellStyleTable using setTableStyleProvider. You can see the row stipes and column stripes. It won't change even you sort the table or rearrange the column order.\n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.grid.AbstractStyleTableModel\n" +
                "com.jidesoft.grid.CellStyleTable";
    }

    class DataTableModel extends AbstractTableModel {

        public int getRowCount() {
            return 10000;
        }

        public int getColumnCount() {
            return 9;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            return "" + rowIndex + "," + columnIndex;
        }
    }

    public Component getDemoPanel() {
        JPanel panel = new JPanel(new BorderLayout(6, 6));
        _table = new SortableTable(new StripesTableDemo.DataTableModel());
        _table.setRowSelectionAllowed(true);
        _table.setColumnSelectionAllowed(true);
        _table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        _table.getColumnModel().getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        panel.add(new JScrollPane(_table), BorderLayout.CENTER);
        return panel;
    }

    @Override
    public Component getOptionsPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 1));
        JRadioButton[] buttons = new JRadioButton[5];
        buttons[0] = new JRadioButton(new AbstractAction("Row Stripe (two colors)") {
            public void actionPerformed(ActionEvent e) {
                _table.setTableStyleProvider(new RowStripeTableStyleProvider(new Color[]{BACKGROUND2, BACKGROUND3}));
            }
        });
        buttons[1] = new JRadioButton(new AbstractAction("Row Stripe (three colors)") {
            public void actionPerformed(ActionEvent e) {
                _table.setTableStyleProvider(new RowStripeTableStyleProvider(new Color[]{BACKGROUND1, BACKGROUND2, BACKGROUND3}));
            }
        });
        buttons[2] = new JRadioButton(new AbstractAction("Row Stripe (background and foreground)") {
            public void actionPerformed(ActionEvent e) {
                _table.setTableStyleProvider(new RowStripeTableStyleProvider(new Color[]{BACKGROUND1, BACKGROUND4}, new Color[]{FOREGROUND1, FOREGROUND4}));
            }
        });
        buttons[3] = new JRadioButton(new AbstractAction("Column Stripe") {
            public void actionPerformed(ActionEvent e) {
                _table.setTableStyleProvider(new ColumnStripeTableStyleProvider(new Color[]{BACKGROUND2, BACKGROUND3}));
            }
        });
        buttons[4] = new JRadioButton(new AbstractAction("None") {
            public void actionPerformed(ActionEvent e) {
                _table.setTableStyleProvider(null);
            }
        });

        ButtonGroup buttonGroup = new ButtonGroup();
        for (JRadioButton button : buttons) {
            panel.add(button);
            buttonGroup.add(button);
        }
        buttons[0].doClick();
        return panel;
    }

    @Override
    public String getDemoFolder() {
        return "G17.StyleTable";
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new StripesTableDemo());
    }
}
