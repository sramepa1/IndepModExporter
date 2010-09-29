/*
 * @(#)HibernatePageTableModelDemo.java 9/11/2008
 *
 * Copyright 2002 - 2008 JIDE Software Inc. All rights reserved.
 *
 */

import com.jidesoft.hibernate.HibernatePageTableModel;
import com.jidesoft.plaf.LookAndFeelFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.sql.SQLException;

public class HibernatePageTableModelDemo extends DatabasePageTableModelDemo {
    public HibernatePageTableModelDemo() {
    }

    @Override
    public String getName() {
        return "HibernatePageTableModel Demo";
    }

    @Override
    public String[] getDemoSource() {
        return new String[]{"HibernatePageTableModelDemo.java", "Sales.java"};
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
        return new HibernatePageTableModel(session, tableClass);
    }

    @Override
    protected void customizeDemoPanel(JTable table, TableModel tableModel, JPanel demoPanel) {
        super.customizeDemoPanel(table, tableModel, demoPanel);
        demoPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Demo for HibernatePageTableModel"),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
    }

    @Override
    protected String getLocalFilterLabel() {
        return super.getLocalFilterLabel() + " (the current page only)";
    }

    static public void main(String[] s) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new HibernatePageTableModelDemo());
    }
}