/*
 * @(#)DatabaseTableModelDemo.java 9/11/2008
 *
 * Copyright 2002 - 2008 JIDE Software Inc. All rights reserved.
 *
 */

import com.jidesoft.database.DatabaseTableModel;
import com.jidesoft.filter.Filter;
import com.jidesoft.filter.InFilter;
import com.jidesoft.filter.LikeFilter;
import com.jidesoft.grid.*;
import com.jidesoft.paging.PageNavigationSupport;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.JideSwingUtilities;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

public class DatabaseTableModelDemo extends AbstractDatabaseDemo {
    public DatabaseTableModelDemo() {
    }

    public String getName() {
        return "DatabaseTableModel Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_DATAGRIDS;
    }

    @Override
    public String getDemoFolder() {
        return "T2.DatabaseTableModel";
    }

    @Override
    public String[] getDemoSource() {
        return new String[]{"DatabaseTableModelDemo.java", "AbstractDatabaseDemo.java", "InitializeDatabase.java",
                "SqlUtils.java", "DerbyUtils.java", "HsqlUtils.java", "MySqlUtils.java", "PostgreSqlUtils.java"};
    }

    @Override
    protected Component createDatabaseDemoPanel() throws SQLException {
        if (_connection == null) {
            return new JPanel();
        }
        final TableModel tableModel = createTableModel();
        final SortableTable table = new SortableTable(tableModel);
        final JScrollPane scroller = new JScrollPane(table);
        scroller.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            public void adjustmentValueChanged(AdjustmentEvent e) {
                table.setCellContentVisible(!e.getValueIsAdjusting());
            }
        });
        scroller.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int rowCount = scroller.getViewport().getHeight() / table.getRowHeight();
                PageNavigationSupport pageNavigationSupport = (PageNavigationSupport) TableModelWrapperUtils.getActualTableModel(table.getModel(), PageNavigationSupport.class);
                if (pageNavigationSupport != null) {
                    pageNavigationSupport.setPageSize(rowCount);
                }
            }
        });

        JPanel demoPanel = new JPanel(new BorderLayout(4, 4));
        demoPanel.add(scroller);
        customizeDemoPanel(table, tableModel, demoPanel);
        return demoPanel;
    }

    protected TableModel createTableModel() throws SQLException {
//        DatabaseTableModel tableModel = new DatabaseTableModel(_connection, "ID, CategoryName as Category, ProductName as Product, Amount, OrderDate as Date", "sales");
//        Map<String, String> mapping = new HashMap();
//        mapping.put("CATEGORY", "CATEGORYNAME");
//        mapping.put("PRODUCT", "PRODUCTNAME");
//        mapping.put("DATE", "ORDERDATE");
//        tableModel.setColumnMapping(mapping);
        return new DatabaseTableModel(_connection, "*", "sales");
    }

    protected void customizeDemoPanel(final JTable table, final TableModel tableModel, final JPanel demoPanel) {
        final QuickTableFilterField field = new QuickTableFilterField(tableModel) {
            @Override
            public Filter getFilter() {
                String s = getSearchingText();
                if (s != null && s.length() > 0) {
                    if (isWildcardEnabled()) {
                        s = s.replace("*", "%");
                        s = s.replace(".", "_");
                    }
                    StringBuffer pattern = new StringBuffer();
                    if (isFromStart()) {
                        pattern.append(s).append("%");
                    }
                    else {
                        pattern.append("%").append(s).append("%");
                    }
                    LikeFilter likeFilter = new LikeFilter(pattern.toString());
                    likeFilter.setCaseSensitive(isCaseSensitive());
                    return likeFilter;
                }
                else {
                    return null;
                }
            }

            @Override
            protected IFilterableTableModel createFilterableTableModel(TableModel tableModel) {
                if (tableModel instanceof IFilterableTableModel) {
                    return (IFilterableTableModel) tableModel;
                }
                else {
                    return super.createFilterableTableModel(tableModel);
                }
            }
        };

        final FilterableTableModel filterableTableModel = new FilterableTableModel(field.getDisplayTableModel());
        int floatColumnIndex = -1;
        for (int i = 0; i < filterableTableModel.getColumnCount(); i++) {
            if (filterableTableModel.getColumnClass(i) == Float.class) {
                floatColumnIndex = i;
                break;
            }
        }
        final int filterColumnIndex = floatColumnIndex == -1 ? 3 : floatColumnIndex;
        JCheckBox addFilterCheckBox1 = new JCheckBox("Using local filtering");
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
        demoPanel.add(JideSwingUtilities.createLabeledComponent(new JLabel("QuickTableFilterField (use SQL LIKE):"), field, BorderLayout.BEFORE_LINE_BEGINS), BorderLayout.AFTER_LAST_LINE);
        demoPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Demo for DatabaseTableModel"),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
    }

    static public void main(String[] s) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new DatabaseTableModelDemo());
    }
}
