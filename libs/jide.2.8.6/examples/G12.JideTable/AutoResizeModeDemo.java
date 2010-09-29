/*
 * @(#)AutoResizeModeDemo.java 9/14/2009
 *
 * Copyright 2002 - 2009 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.grid.JideTable;
import com.jidesoft.grid.RowStripeTableStyleProvider;
import com.jidesoft.grid.SortableTable;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.utils.ProductNames;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Demoed Component: {@link JideTable} <br> Required jar files: jide-common.jar, jide-grids.jar <br> Required L&F: any
 * L&F
 */
public class AutoResizeModeDemo extends AbstractDemo {
    private static final long serialVersionUID = -8856470365128446666L;
    public SortableTable _table;
    protected final Color BACKGROUND1 = new Color(253, 253, 220);
    protected final Color BACKGROUND2 = new Color(255, 255, 255);

    public AutoResizeModeDemo() {
    }

    public String getName() {
        return "AutoResizeMode Table Demo";
    }

    public String getProduct() {
        return ProductNames.PRODUCT_NAME_GRIDS;
    }

    @Override
    public String getDescription() {
        return "This is a demo of JideTable with different auto resize mode.\n" +
                "\nJIDE added a new auto resize mode: AUTO_RESIZE_FILL.\n" +
                "\nIn that mode, the right blank area to the table contents in a JViewport will be filled automatically.\n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.grid.JideTable";
    }

    @Override
    public Component getOptionsPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 1, 4, 4));

        JComboBox autoResizeMode = new JComboBox(new String[]{"Auto Resize Fill", "Auto Resize Off", "Auto Resize Next Column", "Auto Resize SubsequentColumns", "Auto Resize Last Column", "Auto Resize All Columns"});
        autoResizeMode.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getItem() == null || e.getStateChange() != ItemEvent.SELECTED) {
                    return;
                }
                if (e.getItem().equals("Auto Resize Off")) {
                    _table.setAutoResizeMode(JideTable.AUTO_RESIZE_OFF);
                }
                else if (e.getItem().equals("Auto Resize Next Column")) {
                    _table.setAutoResizeMode(JideTable.AUTO_RESIZE_NEXT_COLUMN);
                }
                else if (e.getItem().equals("Auto Resize SubsequentColumns")) {
                    _table.setAutoResizeMode(JideTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
                }
                else if (e.getItem().equals("Auto Resize Last Column")) {
                    _table.setAutoResizeMode(JideTable.AUTO_RESIZE_LAST_COLUMN);
                }
                else if (e.getItem().equals("Auto Resize All Columns")) {
                    _table.setAutoResizeMode(JideTable.AUTO_RESIZE_ALL_COLUMNS);
                }
                else if (e.getItem().equals("Auto Resize Fill")) {
                    _table.setAutoResizeMode(JideTable.AUTO_RESIZE_FILL);
                }
            }
        });

        panel.add(autoResizeMode);
        return panel;
    }

    public Component getDemoPanel() {
        JPanel panel = new JPanel(new BorderLayout(6, 6));
        DefaultTableModel model = new DefaultTableModel(10, 4) {
            private static final long serialVersionUID = -930081547133724313L;

            @Override
            public Object getValueAt(int row, int column) {
                return "(" + row + " , " + column + ")";
            }
        };
        _table = new SortableTable(model);
        _table.setNestedTableHeader(true);
        _table.setShowGrid(false);
        _table.setAutoResizeMode(JideTable.AUTO_RESIZE_FILL);
        _table.setTableStyleProvider(new RowStripeTableStyleProvider(new Color[]{BACKGROUND1, BACKGROUND2}));

        panel.add(new JScrollPane(_table), BorderLayout.CENTER);
        return panel;
    }

    @Override
    public String getDemoFolder() {
        return "G12.JideTable";
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        AbstractDemo.showAsFrame(new AutoResizeModeDemo());
    }
}