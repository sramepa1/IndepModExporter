/*
 * @(#)PagedDatabaseTableModelDemo.java 9/11/2008
 *
 * Copyright 2002 - 2008 JIDE Software Inc. All rights reserved.
 *
 */

import com.jidesoft.filter.InFilter;
import com.jidesoft.grid.FilterableTableModel;
import com.jidesoft.grid.IFilterableTableModel;
import com.jidesoft.paging.PageNavigationBar;
import com.jidesoft.plaf.LookAndFeelFactory;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class DatabasePageTableModelDemo extends DatabaseTableModelDemo {
    public DatabasePageTableModelDemo() {
    }

    @Override
    public String getName() {
        return "DatabaseTableModel (Paged) Demo";
    }

    @Override
    public String[] getDemoSource() {
        return new String[]{"DatabasePageTableModelDemo.java", "AbstractDatabaseDemo.java", "InitializeDatabase.java",
                "SqlUtils.java", "DerbyUtils.java", "HsqlUtils.java", "MySqlUtils.java", "PostgreSqlUtils.java"};
    }

    @Override
    protected void customizeDemoPanel(final JTable table, final TableModel tableModel, final JPanel demoPanel) {
        final FilterableTableModel filterableTableModel = new FilterableTableModel(tableModel);
        int floatColumnIndex = -1;
        for (int i = 0; i < filterableTableModel.getColumnCount(); i++) {
            if (filterableTableModel.getColumnClass(i) == Float.class) {
                floatColumnIndex = i;
                break;
            }
        }
        final int filterColumnIndex = floatColumnIndex == -1 ? 3 : floatColumnIndex;
        JCheckBox addFilterCheckBox1 = new JCheckBox(getLocalFilterLabel());
        final JLabel filterMessage1 = new JLabel();
        addFilterCheckBox1.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
//                        filterableTableModel.addFilter(1, new LikeFilter("%Meat%"));
//                        filterableTableModel.addFilter(filterColumnIndex, new GreaterThanFilter(1000f));
//                        filterableTableModel.addFilter(filterColumnIndex, new BetweenFilter(20f, 60f));
                    filterableTableModel.addFilter(filterColumnIndex, new InFilter(new Object[]{45f, 46f, 77f}));
                }
                else {
                    filterableTableModel.clearFilters();
                }
                long start = System.nanoTime();
                filterableTableModel.setFiltersApplied(true);
                long end = System.nanoTime();
                filterMessage1.setText("Taking " + (end - start) / 1000000 + " ms");
            }
        });
        JCheckBox addFilterCheckBox2 = new JCheckBox("Using the database filtering feature");
        final JLabel filterMessage2 = new JLabel();
        addFilterCheckBox2.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (tableModel instanceof IFilterableTableModel) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
//                        tableModel.addFilter(1, new LikeFilter("Meat"));
//                        tableModel.addFilter(filterColumnIndex, new GreaterThanFilter(1000f));
//                        tableModel.addFilter(filterColumnIndex, new BetweenFilter(20f, 60f));
                        ((IFilterableTableModel) tableModel).addFilter(filterColumnIndex, new InFilter(new Object[]{45f, 46f, 77f}));
                    }
                    else {
                        ((IFilterableTableModel) tableModel).clearFilters();
                    }
                    long start = System.nanoTime();
                    ((IFilterableTableModel) tableModel).setFiltersApplied(true);
                    long end = System.nanoTime();
                    filterMessage2.setText("Taking " + (end - start) / 1000000 + " ms");
                }
            }
        });

        JPanel checkBoxPanel = new JPanel(new GridLayout(2, 2, 0, 0));
        checkBoxPanel.add(addFilterCheckBox1);
        checkBoxPanel.add(addFilterCheckBox2);
        checkBoxPanel.add(filterMessage1);
        checkBoxPanel.add(filterMessage2);

        demoPanel.add(checkBoxPanel, BorderLayout.BEFORE_FIRST_LINE);

        table.setModel(filterableTableModel);

        demoPanel.add(new PageNavigationBar(table), BorderLayout.AFTER_LAST_LINE);
        demoPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Demo for DatabaseTableModel with PageNavigationBar"),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
    }

    protected String getLocalFilterLabel() {
        return "Using local filtering";
    }

    static public void main(String[] s) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new DatabasePageTableModelDemo());
    }
}