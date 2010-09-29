/*
 * @(#)AbstractDatabaseDemo.java 9/11/2008
 *
 * Copyright 2002 - 2008 JIDE Software Inc. All rights reserved.
 *
 */

import com.jidesoft.swing.JideBoxLayout;
import com.jidesoft.swing.JideSwingUtilities;
import com.jidesoft.swing.MultilineLabel;
import com.jidesoft.swing.ResizablePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Demoed Component: {@link com.jidesoft.pivot.PivotTablePane} <br> Required jar files: jide-common.jar, jide-grids.jar
 * <br> Required L&F: any L&F
 */
abstract public class AbstractDatabaseDemo extends AbstractDemo {
    private JComboBox _jdbcComboBox;
    private JLabel _jdbcMessage;
    private JLabel _tableMessage;
    private JButton _createTableButton;
    private JTextField _multiplyField;
    private JTextArea _helpMessage;
    private JButton _reloadDemoButton;

    protected Connection _connection;
    protected SqlUtils _sqlUtils;
    private JPanel _demoPanel;
    private Component _databaseDemoPanel;

    public AbstractDatabaseDemo() {
    }

    public Component createDatabasePanel() {
        Object[] utils = new Object[]{
                new HsqlUtils(),
                new DerbyUtils(),
                new PostgreSqlUtils(),
                new MySqlUtils(),
        };

        _jdbcComboBox = new JComboBox(utils);
        _jdbcComboBox.setSelectedItem(null);
        _jdbcComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                if (value instanceof SqlUtils) {
                    return super.getListCellRendererComponent(list, ((SqlUtils) value).getName(), index, isSelected, cellHasFocus);
                }
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });

        _jdbcMessage = new JLabel(" ");
        _tableMessage = new JLabel(" ");
        _multiplyField = new JTextField("10", 5);
        _multiplyField.setEnabled(false);
        _createTableButton = new JButton(new AbstractAction("Create Sample Table") {
            public void actionPerformed(ActionEvent e) {
                int multiply = 1;
                try {
                    multiply = Integer.parseInt(_multiplyField.getText());
                }
                catch (NumberFormatException e1) {
                    // ignore
                }
                if (_sqlUtils != null && _connection != null) {
                    _sqlUtils.initializeDB(true);

                    _jdbcComboBox.setEnabled(false);
                    _createTableButton.setEnabled(false);
                    _reloadDemoButton.setEnabled(false);
                    _multiplyField.setEnabled(false);
                    _tableMessage.setText("Creating a sample table ...");
                    _tableMessage.setForeground(Color.BLUE);
                    final int m = multiply;

                    Thread thread = new Thread() {
                        @Override
                        public void run() {
                            try {
                                InitializeDatabase.populateSalesTable(_connection, m);
                                _connection.close();
                                _connection = _sqlUtils.getConnection();
                            }
                            catch (final SQLException ex) {
                                Runnable runnable = new Runnable() {
                                    public void run() {
                                        _helpMessage.setText(ex.getLocalizedMessage());
                                        updateTableMessage();
                                        _jdbcComboBox.setEnabled(true);
                                        _createTableButton.setEnabled(true);
                                        _reloadDemoButton.setEnabled(true);
                                        _multiplyField.setEnabled(true);
                                    }
                                };
                                SwingUtilities.invokeLater(runnable);
                                return;
                            }
                            catch (OutOfMemoryError e) {

                            }
                            Runnable runnable = new Runnable() {
                                public void run() {
                                    updateTableMessage();
                                    _jdbcComboBox.setEnabled(true);
                                    _createTableButton.setEnabled(true);
                                    _reloadDemoButton.setEnabled(true);
                                    _multiplyField.setEnabled(true);
                                }
                            };
                            SwingUtilities.invokeLater(runnable);
                        }
                    };
                    thread.start();

                }
            }
        });
        _createTableButton.setEnabled(false);
        _helpMessage = new MultilineLabel("In order to run this demo, you need to have a database table on your system. " +
                "We prepared a sample data with 2082 records. You can fill them into a table called \"sales\" by clicking on the \"Create Sample table\" button above." +
                "You can adjust the int field above if you want to fill more records. For example, if you input 10, it will create 2080 x 10 records by filling the same record 10 times." +
                "\nNote that if you are using HSQLDB or Derby, a database 'jidedb' will be created automatically but if you are using PostgreSQL or MySQL, please create a database (or schema) called 'jidedb' manually.");
        _helpMessage.setRows(8);
        _helpMessage.setWrapStyleWord(true);
        _helpMessage.setLineWrap(true);
        _helpMessage.setEditable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new JideBoxLayout(panel, JideBoxLayout.Y_AXIS, 2));
        JLabel label = new JLabel("Select a built-in JDBC driver: (You can add more by modifying the AbstractDatabaseDemo.java)");
        label.setLabelFor(_jdbcComboBox);
        panel.add(label, JideBoxLayout.FIX);
        JPanel jdbcPanel = new JPanel(new BorderLayout(12, 12));
        jdbcPanel.add(_jdbcComboBox, BorderLayout.BEFORE_LINE_BEGINS);
        jdbcPanel.add(_jdbcMessage, BorderLayout.CENTER);
        panel.add(jdbcPanel, JideBoxLayout.FIX);
        panel.add(Box.createVerticalStrut(3));
        panel.add(_tableMessage);
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new JideBoxLayout(tablePanel, JideBoxLayout.X_AXIS, 2));
        tablePanel.add(_createTableButton);
        tablePanel.add(new JLabel(" x "));
        tablePanel.add(_multiplyField);
        tablePanel.add(Box.createGlue(), JideBoxLayout.VARY);
        panel.add(_tableMessage, JideBoxLayout.FIX);
        panel.add(tablePanel, JideBoxLayout.FIX);
        panel.add(Box.createVerticalStrut(3));
        panel.add(_helpMessage, JideBoxLayout.VARY);

        _jdbcComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _sqlUtils = (SqlUtils) _jdbcComboBox.getSelectedItem();
                try {
                    _sqlUtils.registerDriver();
                    _connection = null;
                    connectionFailed();
                    _connection = _sqlUtils.getConnection();
                    if (_connection == null) {
                        _jdbcMessage.setText("Failed to connect to the " + _sqlUtils.getName() + ". Please check the values in the corresponding subclass of SqlUtils");
                        _jdbcMessage.setForeground(Color.RED);
                    }
                    else {
                        _jdbcMessage.setText("Connect to the " + _sqlUtils.getName() + " successfully");
                        _jdbcMessage.setForeground(Color.BLUE);
                        connectionEstablish();
                    }
                    updateTableMessage();
                }
                catch (ClassNotFoundException ex) {
                    _jdbcMessage.setText("Please make sure the JDBC driver \"" + _sqlUtils.getDriver() + "\" is in the classpath");
                    _jdbcMessage.setForeground(Color.RED);
                    updateTableMessage();
                    _createTableButton.setEnabled(false);
                    _multiplyField.setEnabled(false);
                }
                catch (SQLException ex) {
                    _jdbcMessage.setText(ex.getLocalizedMessage());
                    _jdbcMessage.setForeground(Color.RED);
                    updateTableMessage();
                    _createTableButton.setEnabled(false);
                    _multiplyField.setEnabled(false);
                }
            }
        });


        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Prepare the Database"),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        return panel;

    }

    protected void reloadDemo() {
        try {
            if (_databaseDemoPanel != null) _demoPanel.remove(_databaseDemoPanel);
            _databaseDemoPanel = createDatabaseDemoPanel();
            _demoPanel.add(_databaseDemoPanel, JideBoxLayout.VARY);
        }
        catch (SQLException e) {
            JLabel label = new JLabel(e.getLocalizedMessage());
            label.setForeground(Color.RED);
            _demoPanel.add(label);
        }

        packWindow();
    }

    private void packWindow() {
        _demoPanel.revalidate();
        _demoPanel.doLayout();
        Container ancestor = SwingUtilities.getAncestorOfClass(ResizablePanel.class, _demoPanel);
        if (ancestor == null) {
            ((Window) _demoPanel.getTopLevelAncestor()).pack();
        }
    }

    protected void clearDemo() {
        if (_databaseDemoPanel != null) _demoPanel.remove(_databaseDemoPanel);
        _databaseDemoPanel = null;

        packWindow();
    }


    protected void connectionEstablish() {
    }

    protected void connectionFailed() {
        clearDemo();
    }

    private void updateTableMessage() {
        if (_connection == null || _sqlUtils == null) {
            _tableMessage.setText(" ");
            _createTableButton.setEnabled(false);
            _multiplyField.setEnabled(false);
        }
        else {
            int recordCount = _sqlUtils.isSampleTableAvailable(_connection);
            if (recordCount == -1) {
                _tableMessage.setText("There is no table available. Click on the button below to create a sample table.");
                _tableMessage.setForeground(Color.RED);
            }
            else {
                _tableMessage.setText("There is a sample table with " + recordCount + " records");
                _tableMessage.setForeground(Color.BLUE);
            }
            _createTableButton.setEnabled(true);
            _multiplyField.setEnabled(true);
        }
    }

    public Component getDemoPanel() {
        _demoPanel = new JPanel();
        _demoPanel.setLayout(new JideBoxLayout(_demoPanel, JideBoxLayout.Y_AXIS, 6));
        _demoPanel.add(createDatabasePanel(), JideBoxLayout.FLEXIBLE);
        _reloadDemoButton = new JButton(new AbstractAction("Reload the demo") {
            public void actionPerformed(ActionEvent e) {
                reloadDemo();
            }
        });
        _demoPanel.add(JideSwingUtilities.createCenterPanel(_reloadDemoButton), JideBoxLayout.FIX);
        return _demoPanel;
    }

    abstract Component createDatabaseDemoPanel() throws SQLException;
}