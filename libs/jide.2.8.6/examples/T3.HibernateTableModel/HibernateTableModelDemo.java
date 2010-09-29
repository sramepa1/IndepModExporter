/*
 * @(#)HibernateTableModelDemo.java 9/11/2008
 *
 * Copyright 2002 - 2008 JIDE Software Inc. All rights reserved.
 *
 */

import com.jidesoft.filter.InFilter;
import com.jidesoft.grid.IFilterableTableModel;
import com.jidesoft.grid.QuickTableFilterField;
import com.jidesoft.hibernate.HibernateTableModel;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.JideSwingUtilities;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;

public class HibernateTableModelDemo extends DatabaseTableModelDemo {
    public HibernateTableModelDemo() {
    }

    @Override
    public String getName() {
        return "HibernateTableModel Demo";
    }

    @Override
    public String[] getDemoSource() {
        return new String[]{"HibernateTableModelDemo.java", "Sales.java"};
    }

    @Override
    public String getDemoFolder() {
        return "T3.HibernateTableModel";
    }

    @Override
    protected TableModel createTableModel() throws SQLException {
        final Class tableClass = Sales.class;
        Configuration cfg = new Configuration()
                .addClass(tableClass)
                .setProperty("hibernate.connection.driver_class", _sqlUtils.getDriver())
                .setProperty("hibernate.connection.url", _sqlUtils.getConnectionURL())
                .setProperty("hibernate.connection.username", _sqlUtils.getUserName())
                .setProperty("hibernate.connection.password", _sqlUtils.getPassword())
                .setProperty("hibernate.dialect", "org.hibernate.dialect." + _sqlUtils.getName() + "Dialect")
                .setProperty("hibernate.jdbc.use_scrollable_resultset", "true");
        SessionFactory factory = cfg.buildSessionFactory();
        Session session = factory.openSession();
        session.beginTransaction();
        Criteria criteria = session.createCriteria(tableClass);
        java.util.List list = criteria.list();
        return new HibernateTableModel(session, list, tableClass);
    }

    @Override
    protected void customizeDemoPanel(final JTable table, final TableModel tableModel, final JPanel demoPanel) {
        final QuickTableFilterField field = new QuickTableFilterField(tableModel);
        final IFilterableTableModel filterableTableModel = field.getDisplayTableModel();
        final JCheckBox addFilterCheckBox1 = new JCheckBox("Using local filtering");
        addFilterCheckBox1.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
//                        filterableTableModel.addFilter(1, new LikeFilter("%Meat%"));
//                        filterableTableModel.addFilter(3, new GreaterThanFilter(1000f));
//                        filterableTableModel.addFilter(3, new BetweenFilter(20f, 60f));
                    filterableTableModel.addFilter(3, new InFilter(new Object[]{45f, 46f, 77f}));
                }
                else {
                    filterableTableModel.clearFilters();
                }
                long start = System.nanoTime();
                filterableTableModel.setFiltersApplied(true);
                long end = System.nanoTime();
                addFilterCheckBox1.setText("Using local filtering: taking " + (end - start) / 1000000 + " ms");
            }
        });

        JPanel checkBoxPanel = new JPanel(new GridLayout(1, 1, 0, 0));
        checkBoxPanel.add(addFilterCheckBox1);

        demoPanel.add(checkBoxPanel, BorderLayout.BEFORE_FIRST_LINE);

        table.setModel(filterableTableModel);
        demoPanel.add(JideSwingUtilities.createLabeledComponent(new JLabel("QuickTableFilterField: "), field, BorderLayout.BEFORE_LINE_BEGINS), BorderLayout.AFTER_LAST_LINE);
        demoPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Demo for HibernateTableModel"),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
    }

    static public void main(String[] s) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new HibernateTableModelDemo());
    }
}