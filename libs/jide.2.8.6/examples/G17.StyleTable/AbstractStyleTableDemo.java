/*
 * @(#)StyleTableDemo.java 6/17/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.grid.CellStyle;
import com.jidesoft.grid.CellStyleTable;
import com.jidesoft.grid.SortableTable;
import com.jidesoft.grid.StyleModel;
import com.jidesoft.icons.JideIconsFactory;
import com.jidesoft.plaf.LookAndFeelFactory;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Demoed Component: {@link com.jidesoft.grid.CellStyleTable} <br> Required jar files: jide-common.jar, jide-grids.jar
 * <br> Required L&F: any L&F
 */
public class AbstractStyleTableDemo extends AbstractDemo {
    public CellStyleTable _table;
    protected AbstractTableModel _tableModel;
    protected int _pattern;

    public AbstractStyleTableDemo() {
    }

    public String getName() {
        return "CellStyleTable Demo (Predefined Patterns)";
    }

    public String getProduct() {
        return PRODUCT_NAME_GRIDS;
    }

    @Override
    public String getDescription() {
        return "This is a demo of CellStyleTable.\n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.grid.AbstractStyleTableModel\n" +
                "com.jidesoft.grid.CellStyleTable";
    }


    protected static final Color UP = new Color(0, 136, 0);
    protected static final Color DOWN = new Color(204, 0, 0);

    class StyleTableTableModel extends DefaultTableModel implements StyleModel {

        public StyleTableTableModel(Object[][] data, Object[] columnNames) {
            super(data, columnNames);
        }

        CellStyle _cellStyle = new CellStyle();

        public CellStyle getCellStyleAt(int row, int column) {
            _cellStyle.setText(null);
            _cellStyle.setToolTipText(null);
            _cellStyle.setBorder(null);
            _cellStyle.setIcon(null);
            _cellStyle.setBackground(null);
            _cellStyle.setForeground(null);
            _cellStyle.setFontStyle(-1);
            Number value = (Number) getValueAt(row, column);
            if (_pattern == 0) {
                if (value.doubleValue() > 0) {
                    _cellStyle.setForeground(UP);
                    _cellStyle.setIcon(JideIconsFactory.getImageIcon(JideIconsFactory.Arrow.UP));
                }
                else {
                    _cellStyle.setForeground(DOWN);
                    _cellStyle.setIcon(JideIconsFactory.getImageIcon(JideIconsFactory.Arrow.DOWN));
                }
            }
            else if (_pattern == 1) {
                if (value.doubleValue() < 0) {
                    _cellStyle.setForeground(Color.WHITE);
                    _cellStyle.setBackground(DOWN);
                }
                else return null;
            }
            else if (_pattern == 2) {
                if (value.doubleValue() < 0) {
                    _cellStyle.setFontStyle(Font.BOLD);
                    _cellStyle.setForeground(DOWN);
                    _cellStyle.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                }
                else return null;
            }
            else if (_pattern == 3) {
                if (value.doubleValue() < 0) {
                    _cellStyle.setText("####");
                    _cellStyle.setToolTipText("Actual value is " + value);
                    _cellStyle.setForeground(DOWN);
                }
                else {
                    _cellStyle.setForeground(UP);
                }
            }
            return _cellStyle;
        }


        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return Double.class;
        }

        public boolean isCellStyleOn() {
            return _pattern != 4;
        }
    }

    public Component getDemoPanel() {
        JPanel panel = new JPanel(new BorderLayout(6, 6));
        Object[][] data = new Object[100][6];
        String[] columNames = new String[6];
        for (int i = 0; i < columNames.length; i++) {
            columNames[i] = "" + i;
        }
        for (int i = 0; i < data.length; i++) {
            data[i] = new Object[6];
            for (int j = 0; j < data[i].length; j++) {
                data[i][j] = Math.round((Math.random() - 0.2) * 10000) / 100.0;
            }
        }
        _tableModel = new StyleTableTableModel(data, columNames);
        _table = new SortableTable(_tableModel);
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
        buttons[0] = new JRadioButton(new AbstractAction("Stock Style (green for up and red for down") {
            public void actionPerformed(ActionEvent e) {
                _pattern = 0;
                _table.repaint();
            }
        });
        buttons[1] = new JRadioButton(new AbstractAction("Alert Style (for negative value)") {
            public void actionPerformed(ActionEvent e) {
                _pattern = 1;
                _table.repaint();
            }
        });
        buttons[2] = new JRadioButton(new AbstractAction("Emphasize Style (for negative value)") {
            public void actionPerformed(ActionEvent e) {
                _pattern = 2;
                _table.repaint();
            }
        });
        buttons[3] = new JRadioButton(new AbstractAction("Hidden text (for negative value)") {
            public void actionPerformed(ActionEvent e) {
                _pattern = 3;
                _table.repaint();
            }
        });
        buttons[4] = new JRadioButton(new AbstractAction("None") {
            public void actionPerformed(ActionEvent e) {
                _pattern = 4;
                _table.repaint();
            }
        });

        ButtonGroup buttonGroup = new ButtonGroup();
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setSelected(_pattern == i);
            panel.add(buttons[i]);
            buttonGroup.add(buttons[i]);
        }
        return panel;
    }

    @Override
    public String getDemoFolder() {
        return "G17.StyleTable";
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new AbstractStyleTableDemo());
    }
}

