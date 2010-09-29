/*
 * @(#)ResizingTableDemo.java 9/8/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.*;
import com.jidesoft.plaf.LookAndFeelFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Demoed Component: {@link com.jidesoft.grid.JideTable} <br> Required jar files: jide-common.jar, jide-grids.jar <br>
 * Required L&F: any L&F
 */
public class ResizableTableDemo extends AbstractDemo {
    public JideTable _table;
    boolean[] _rowFont;

    public ResizableTableDemo() {
    }

    public String getName() {
        return "Resizable Table Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_GRIDS;
    }

    @Override
    public String getDescription() {
        return "This is a demo of JideTable with several resizable options.\n" +
                "\nWhen a table is column resizable or row resizable, you drag the grid line " +
                "in the table to resize the column or row respectively." +
                "If a table is column auto resizable, you can double click on the grid line of the table header " +
                "and make the table column automatically resize to the widest size of all cells in that column.\n\n" +
                "The text in all the cells could be wrapped. You can see how auto resize rows feature works by shrinking one of the columns (such as the Name column).\n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.grid.TableColumnAutoResizer\n" +
                "com.jidesoft.grid.TableRowAutoResizer\n" +
                "com.jidesoft.grid.TableColumnResizer\n" +
                "com.jidesoft.grid.TableRowResizer\n" +
                "com.jidesoft.grid.JideTable";
    }

    @Override
    public Component getOptionsPanel() {
        JPanel switchPanel = new JPanel(new GridLayout(0, 1));
        final JCheckBox option1 = new JCheckBox("Enable Column Chooser");
        final JCheckBox option2 = new JCheckBox("Column Auto-resizable when Double Clicking on the Header");
        final JCheckBox option3 = new JCheckBox("Column Resizable when Dragging the Vertical Grid Line");
        final JCheckBox option4 = new JCheckBox("Row Resizable when Dragging the Horizontal Grid Line");
        final JCheckBox option5 = new JCheckBox("Keep Rows at the Same Height");

        switchPanel.add(option1);
        switchPanel.add(option2);
        switchPanel.add(option3);
        switchPanel.add(option4);
        switchPanel.add(option5);

        option1.setSelected(true);
        TableHeaderPopupMenuInstaller installer = new TableHeaderPopupMenuInstaller(_table);
        installer.addTableHeaderPopupMenuCustomizer(new AutoResizePopupMenuCustomizer());
        installer.addTableHeaderPopupMenuCustomizer(new TableColumnChooserPopupMenuCustomizer());
        option1.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (option1.isSelected()) {
                    TableHeaderPopupMenuInstaller installer = new TableHeaderPopupMenuInstaller(_table);
                    installer.addTableHeaderPopupMenuCustomizer(new AutoResizePopupMenuCustomizer());
                    installer.addTableHeaderPopupMenuCustomizer(new TableColumnChooserPopupMenuCustomizer());
                }
                else {
                    TableHeaderPopupMenuInstaller.getTableHeaderPopupMenuInstaller(_table).uninstallListeners();
                }
            }
        });

        option2.setSelected(_table.isColumnAutoResizable());
        option2.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _table.setColumnAutoResizable(option2.isSelected());
            }
        });

        option3.setSelected(_table.isColumnResizable());
        option3.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _table.setColumnResizable(option3.isSelected());
            }
        });

        option4.setSelected(_table.isRowResizable());
        option4.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _table.setRowResizable(option4.isSelected());
            }
        });

        option5.setSelected(!_table.isVariousRowHeights());
        option5.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _table.setVariousRowHeights(!option5.isSelected());
            }
        });

        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 2, 2));
        buttonPanel.add((new JButton(new AbstractAction("Auto resize all the columns") {
            public void actionPerformed(ActionEvent e) {
                TableUtils.autoResizeAllColumns(_table);
            }
        })));

        buttonPanel.add((new JButton(new AbstractAction("Auto resize all the rows") {
            public void actionPerformed(ActionEvent e) {
                TableUtils.autoResizeAllRows(_table);
            }
        })));

        buttonPanel.add((new JButton(new AbstractAction("Auto resize the selected rows") {
            public void actionPerformed(ActionEvent e) {
                int fromRow = _table.getSelectionModel().getMinSelectionIndex();
                int toRow = _table.getSelectionModel().getMaxSelectionIndex();
                if (fromRow != -1 && toRow != -1) {
                    TableUtils.autoResizeRows(_table, fromRow, toRow);
                }
            }
        })));

        JPanel optionPanel = new JPanel(new BorderLayout(6, 6));
        optionPanel.add(new JLabel("Options: "), BorderLayout.BEFORE_FIRST_LINE);
        optionPanel.add(switchPanel, BorderLayout.CENTER);
        optionPanel.add(buttonPanel, BorderLayout.AFTER_LAST_LINE);
        return optionPanel;
    }

    static String[] QUOTE_COLUMNS = new String[]{"Symbol", "Name", "Last", "Change", "Volume"};

    static Object[][] QUOTES = new Object[][]{
            new Object[]{"AA", "ALCOA INC", "32.88", "+0.53 (1.64%)", "4156200"},
            new Object[]{"AIG", "AMER INTL GROUP", "69.53", "-0.58 (0.83%)", "4369200"},
            new Object[]{"AXP", "AMER EXPRESS CO", "48.90", "-0.35 (0.71%)", "4103600"},
            new Object[]{"BA", "BOEING CO", "49.14", "-0.18 (0.36%)", "3573700"},
            new Object[]{"C", "CITIGROUP", "44.21", "-0.89 (1.97%)", "28594900"},
            new Object[]{"CAT", "CATERPILLAR INC", "79.40", "+0.62 (0.79%)", "1458200"},
            new Object[]{"DD", "DU PONT CO", "42.62", "-0.14 (0.33%)", "1832700"},
            new Object[]{"DIS", "WALT DISNEY CO", "23.87", "-0.32 (1.32%)", "4443600"},
            new Object[]{"GE", "GENERAL ELEC CO", "33.37", "+0.24 (0.72%)", "31429500"},
            new Object[]{"GM", "GENERAL MOTORS", "43.94", "-0.20 (0.45%)", "3722100"},
            new Object[]{"HD", "HOME DEPOT INC", "34.33", "-0.18 (0.52%)", "5367900"},
            new Object[]{"HON", "HONEYWELL INTL", "35.70", "+0.23 (0.65%)", "4092100"},
            new Object[]{"HPQ", "HEWLETT-PACKARD", "19.65", "-0.25 (1.26%)", "11003000"},
            new Object[]{"IBM", "INTL BUS MACHINE", "84.02", "-0.11 (0.13%)", "6880500"},
            new Object[]{"INTC", "INTEL CORP", "23.15", "-0.23 (0.98%)", "95177008"},
            new Object[]{"JNJ", "JOHNSON&JOHNSON", "55.35", "-0.57 (1.02%)", "5428000"},
            new Object[]{"JPM", "JP MORGAN CHASE", "36.00", "-0.45 (1.23%)", "12135300"},
            new Object[]{"KO", "COCA COLA CO", "50.84", "-0.32 (0.63%)", "4143600"},
            new Object[]{"MCD", "MCDONALDS CORP", "27.91", "+0.12 (0.43%)", "6110800"},
            new Object[]{"MMM", "3M COMPANY", "88.62", "+0.43 (0.49%)", "2073800"},
            new Object[]{"MO", "ALTRIA GROUP", "48.20", "-0.80 (1.63%)", "6005500"},
            new Object[]{"MRK", "MERCK & CO", "44.71", "-0.97 (2.12%)", "5472100"},
            new Object[]{"MSFT", "MICROSOFT CP", "27.87", "-0.26 (0.92%)", "46717716"},
            new Object[]{"PFE", "PFIZER INC", "32.58", "-1.43 (4.20%)", "28783200"},
            new Object[]{"PG", "PROCTER & GAMBLE", "55.01", "-0.07 (0.13%)", "5538400"},
            new Object[]{"SBC", "SBC COMMS", "23.00", "-0.54 (2.29%)", "6423400"},
            new Object[]{"UTX", "UNITED TECH CP", "91.00", "+1.16 (1.29%)", "1868600"},
            new Object[]{"VZ", "VERIZON COMMS", "34.81", "-0.35 (1.00%)", "4182600"},
            new Object[]{"WMT", "WAL-MART STORES", "52.33", "-0.25 (0.48%)", "6776700"},
            new Object[]{"XOM", "EXXON MOBIL", "45.32", "-0.14 (0.31%)", "7838100"}
    };

    class QuoteTableModel extends DefaultTableModel implements ContextSensitiveTableModel, StyleModel {
        CellStyle _BiggerFont;

        public QuoteTableModel() {
            super(QUOTES, QUOTE_COLUMNS);
            ResizableTableDemo.this._rowFont = new boolean[getRowCount()];
            for (int i = 0; i < getRowCount(); i++) {
                ResizableTableDemo.this._rowFont[i] = false;
            }
            _BiggerFont = new CellStyle();
            _BiggerFont.setFont(new Font("Monospaced", Font.BOLD, 17));
        }

        public ConverterContext getConverterContextAt(int rowIndex, int columnIndex) {
            return null;
        }

        public EditorContext getEditorContextAt(int rowIndex, int columnIndex) {
            if (columnIndex == 1) {
                return MultilineTableCellRenderer.CONTEXT;
            }
            return null;
        }

        public Class<?> getCellClassAt(int rowIndex, int columnIndex) {
            return String.class;
        }

        public CellStyle getCellStyleAt(int rowIndex, int columnIndex) {
            if (ResizableTableDemo.this._rowFont[rowIndex]) {
                return _BiggerFont;
            }
            return null;
        }

        public boolean isCellStyleOn() {
            return true;
        }

        public boolean isCellEditable(int row, int column) {
            return false;
        }
    }

    public Component getDemoPanel() {
        JPanel panel = new JPanel(new BorderLayout(6, 6));
        _table = new SortableTable(new QuoteTableModel());
        _table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        _table.setColumnResizable(true);
        _table.setRowResizable(true);
        _table.setVariousRowHeights(true);

        _table.setColumnAutoResizable(true);

        _table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        _table.getColumnModel().getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        panel.add(new JScrollPane(_table), BorderLayout.CENTER);
        return panel;
    }

    @Override
    public String getDemoFolder() {
        return "G12.JideTable";
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new ResizableTableDemo());
    }
}

