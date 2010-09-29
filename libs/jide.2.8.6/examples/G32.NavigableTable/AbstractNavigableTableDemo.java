/*
 * @(#)AbstractNavigableTableDemo.java 9/12/2007
 *
 * Copyright 2002 - 2007 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.dialog.ButtonPanel;
import com.jidesoft.grid.*;
import com.jidesoft.plaf.LookAndFeelFactory;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Demoed Component: {@link com.jidesoft.grid.CellStyleTable} <br> Required jar files: jide-common.jar, jide-grids.jar
 * <br> Required L&F: any L&F
 */
public class AbstractNavigableTableDemo extends AbstractDemo {
    public JideTable _table;
    protected AbstractTableModel _tableModel;
    protected int _pattern;

    public AbstractNavigableTableDemo() {
    }

    public String getName() {
        return "NavigableTable Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_GRIDS;
    }

    @Override
    public String getDescription() {
        return "This is a demo of NavigableTable. You can press the TAB, SHIFT-TAB or ENTER key in the table to see the cell navigation behavior." +
                "To make it easy to tell, for non-navigable cells, we paint them with a gray background.\n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.grid.AbstractStyleTableModel\n" +
                "com.jidesoft.grid.CellStyleTable";
    }

    class StyleTableTableModel extends AbstractNavigableTableModel implements StyleModel {
        final CellStyle NOT_NAVIGABLE = new CellStyle();


        public StyleTableTableModel() {
            NOT_NAVIGABLE.setBackground(new Color(240, 240, 240));
        }

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

        @Override
        public boolean isNavigableAt(int rowIndex, int columnIndex) {
            switch (_pattern) {
                case 0:
                    return rowIndex % 2 == columnIndex % 2;
                case 1:
                    return rowIndex % 2 == 1;
                case 2:
                    return columnIndex % 2 == 1;
                case 3:
                    return rowIndex == 1 && columnIndex == 1;
                case 4:
                    return false;
            }
            return true;
        }

        @Override
        public boolean isNavigationOn() {
            return true;
        }

        public CellStyle getCellStyleAt(int rowIndex, int columnIndex) {
            return isNavigableAt(rowIndex, columnIndex) ? null : NOT_NAVIGABLE;
        }

        public boolean isCellStyleOn() {
            return true;
        }
    }

    public Component getDemoPanel() {
        JPanel panel = new JPanel(new BorderLayout(6, 6));
        _tableModel = new StyleTableTableModel();
        _table = new CellStyleTable(_tableModel) {
            @Override
            protected boolean isNavigationKey(KeyStroke ks) {
                if (ks == null) {
                    return false;
                }

                switch (ks.getKeyCode()) {
                    case KeyEvent.VK_RIGHT:
                    case KeyEvent.VK_LEFT:
                    case KeyEvent.VK_UP:
                    case KeyEvent.VK_DOWN:
                        return true;
                    default:
                        return super.isNavigationKey(ks);
                }
            }
        };
        _table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        _table.getColumnModel().getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        panel.add(new JScrollPane(_table), BorderLayout.CENTER);
        return panel;
    }

    @Override
    public Component getOptionsPanel() {
        ButtonPanel panel = new ButtonPanel(SwingConstants.TOP);
        JRadioButton grids = new JRadioButton(new AbstractAction("Grids") {
            public void actionPerformed(ActionEvent e) {
                _table.clearSelection();
                _pattern = 0;
                _table.repaint();
            }
        });
        panel.addButton(grids);
        JRadioButton rowStripe = new JRadioButton(new AbstractAction("Row Stripe") {
            public void actionPerformed(ActionEvent e) {
                _table.clearSelection();
                _pattern = 1;
                _table.repaint();
            }
        });
        panel.addButton(rowStripe);
        JRadioButton columnStripe = new JRadioButton(new AbstractAction("Column Stripe") {
            public void actionPerformed(ActionEvent e) {
                _table.clearSelection();
                _pattern = 2;
                _table.repaint();
            }
        });
        panel.addButton(columnStripe);
        JRadioButton one = new JRadioButton(new AbstractAction("Single One") {
            public void actionPerformed(ActionEvent e) {
                _table.clearSelection();
                _pattern = 3;
                _table.repaint();
            }
        });
        panel.addButton(one);
        JRadioButton none = new JRadioButton(new AbstractAction("None") {
            public void actionPerformed(ActionEvent e) {
                _table.clearSelection();
                _pattern = 4;
                _table.repaint();
            }
        });
        panel.addButton(none);

        grids.setSelected(_pattern == 0);
        rowStripe.setSelected(_pattern == 1);
        columnStripe.setSelected(_pattern == 2);
        one.setSelected(_pattern == 3);
        none.setSelected(_pattern == 4);

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(grids);
        buttonGroup.add(rowStripe);
        buttonGroup.add(columnStripe);
        buttonGroup.add(one);
        buttonGroup.add(none);
        return panel;
    }

    @Override
    public String getDemoFolder() {
        return "G32.NavigableTable";
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new AbstractNavigableTableDemo());
    }
}
