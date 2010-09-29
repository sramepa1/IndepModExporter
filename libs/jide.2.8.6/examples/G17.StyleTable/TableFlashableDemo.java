/*
 * @(#)CellFlashTableDemo.java 3/11/2007
 *
 * Copyright 2002 - 2007 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.converter.DateConverter;
import com.jidesoft.dialog.ButtonPanel;
import com.jidesoft.grid.*;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.Flashable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

/**
 * Demoed Component: {@link com.jidesoft.grid.HierarchicalTable} <br> Required jar files: jide-common.jar,
 * jide-grids.jar <br> Required L&F: Jide L&F extension required
 */
public class TableFlashableDemo extends AbstractDemo {
    protected static final Color BG1 = new Color(255, 255, 255);

    protected DefaultTableModel _quotesTableModel;
    private Timer _timer;
    private CellSpanTable _table;

    public TableFlashableDemo() {
    }

    public String getName() {
        return "CellStyleTable Demo (Flashing)";
    }

    public String getProduct() {
        return PRODUCT_NAME_GRIDS;
    }

    public Component getDemoPanel() {
        _table = createTable();
        JScrollPane scrollPane = new JScrollPane(_table);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        return scrollPane;
    }

    @Override
    public String getDescription() {
        return "First, click on \"Start Updating\" to start a timer updating the stock price and time stamp columns. " +
                "Then click on \"Start Flashing\" to start or stop flashing of the cells. By default we will flash any stock prices above $80 for 5 seconds, " +
                "and will also flash any stock ticker cells where the stock ticker starts with 'A' (just for fun; no business reason)" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.swing.Flashable\n" +
                "com.jidesoft.grid.TableFlashable";
    }

    @Override
    public String getDemoFolder() {
        return "G17.StyleTable";
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new TableFlashableDemo());
    }

    @Override
    public Component getOptionsPanel() {
        ButtonPanel buttonPanel = new ButtonPanel(SwingConstants.TOP);
        buttonPanel.addButton(new JButton(new AbstractAction("Start Updating") {
            public void actionPerformed(ActionEvent e) {
                if (_timer == null) {
                    _timer = new Timer(100, new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            int row = (int) (_quotesTableModel.getRowCount() * Math.random());
                            double price = (double) 100 * Math.random();
                            _quotesTableModel.setValueAt(new Double(price), row, 2);
                            _quotesTableModel.setValueAt(Calendar.getInstance(), row, 3);
                        }
                    });
                }

                if (_timer.isRunning()) {
                    _timer.stop();
                    putValue(Action.NAME, "Start Updating");
                }
                else {
                    _timer.start();
                    putValue(Action.NAME, "Stop Updating");
                }
            }
        }));
        buttonPanel.addButton(new JButton(new AbstractAction("Start Flashing") {
            public void actionPerformed(ActionEvent e) {
                Flashable flashable;
                if (!Flashable.isFlashableInstalled(_table)) {
                    flashable = new TableFlashable(_table, new FlashCellStyle[]{_stockPriceFlashStyle, _stockNameFlashStyle}, new int[]{0, 2});
                }
                else {
                    flashable = Flashable.getFlashable(_table);
                }

                if (flashable.isFlashing()) {
                    flashable.stopFlashing();
                    putValue(Action.NAME, "Start Flashing");
                }
                else {
                    flashable.startFlashing();
                    putValue(Action.NAME, "Stop Flashing");
                }
            }
        }));
        return buttonPanel;
    }

    private CellSpanTable createTable() {
        _quotesTableModel = new QuoteTableModel();
        final SortableTable table = new SortableTable(_quotesTableModel);
        table.setTableStyleProvider(new RowStripeTableStyleProvider());
        table.setName("Quote Table");
        table.setPreferredScrollableViewportSize(new Dimension(600, 400));
        table.getSelectionModel().setAnchorSelectionIndex(0);
        table.getSelectionModel().setLeadSelectionIndex(0);
        TableHeaderPopupMenuInstaller installer = new TableHeaderPopupMenuInstaller(table);
        installer.addTableHeaderPopupMenuCustomizer(new AutoResizePopupMenuCustomizer());
        installer.addTableHeaderPopupMenuCustomizer(new TableColumnChooserPopupMenuCustomizer());
        return table;
    }

    static String[] QUOTE_COLUMNS = new String[]{"Symbol", "Name"};

    static Object[][] QUOTES = new Object[][]{
            new Object[]{"AA", "ALCOA INC"},
            new Object[]{"AIG", "AMER INTL GROUP"},
            new Object[]{"AXP", "AMER EXPRESS CO"},
            new Object[]{"BA", "BOEING CO"},
            new Object[]{"C", "CITIGROUP"},
            new Object[]{"CAT", "CATERPILLAR INC"},
            new Object[]{"DD", "DU PONT CO"},
            new Object[]{"DIS", "WALT DISNEY CO"},
            new Object[]{"GE", "GENERAL ELEC CO"},
            new Object[]{"GM", "GENERAL MOTORS"},
            new Object[]{"HD", "HOME DEPOT INC"},
            new Object[]{"HON", "HONEYWELL INTL"},
            new Object[]{"HPQ", "HEWLETT-PACKARD"},
            new Object[]{"IBM", "INTL BUS MACHINE"},
            new Object[]{"INTC", "INTEL CORP"},
            new Object[]{"JNJ", "JOHNSON&JOHNSON"},
            new Object[]{"JPM", "JP MORGAN CHASE"},
            new Object[]{"KO", "COCA COLA CO"},
            new Object[]{"MCD", "MCDONALDS CORP"},
            new Object[]{"MMM", "3M COMPANY"},
            new Object[]{"MO", "ALTRIA GROUP"},
            new Object[]{"MRK", "MERCK & CO"},
            new Object[]{"MSFT", "MICROSOFT CP"},
            new Object[]{"PFE", "PFIZER INC"},
            new Object[]{"PG", "PROCTER & GAMBLE"},
            new Object[]{"SBC", "SBC COMMS"},
            new Object[]{"UTX", "UNITED TECH CP"},
            new Object[]{"VZ", "VERIZON COMMS"},
            new Object[]{"WMT", "WAL-MART STORES"},
            new Object[]{"XOM", "EXXON MOBIL"},
    };

    static FlashCellStyle _stockPriceFlashStyle = new FlashCellStyle();
    static FlashCellStyle _stockNameFlashStyle = new FlashCellStyle();

    static {
        CellStyle style = new CellStyle();
        style.setBackground(Color.RED);
        style.setForeground(Color.WHITE);
        style.setFontStyle(Font.BOLD);
        _stockPriceFlashStyle.setFlashCellStyle(style);

        CellStyle style2 = new CellStyle();
        style2.setBorder(BorderFactory.createLineBorder(Color.RED));
        _stockNameFlashStyle.setFlashCellStyle(style2);
    }

    static class QuoteTableModel extends DefaultTableModel implements StyleModel, ContextSensitiveTableModel {

        public QuoteTableModel() {
            super(QUOTES, QUOTE_COLUMNS);

// add more data to test performance            
//            for (int i = 0; i < 100000; i++) {
//                addRow(QUOTES[i % QUOTES.length]);
//            }

            addColumn("Last");
            addColumn("Last Time");
        }

        public ConverterContext getConverterContextAt(int row, int column) {
            return column == 3 ? DateConverter.DATETIME_CONTEXT : null;
        }

        public EditorContext getEditorContextAt(int row, int column) {
            return null;
        }

        public Class<?> getCellClassAt(int row, int column) {
            return getColumnClass(column);
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            switch (columnIndex) {
                case 2:
                    return double.class;
                case 3:
                    return Calendar.class;
            }
            return String.class;
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }

        public CellStyle getCellStyleAt(int rowIndex, int columnIndex) {
            if (columnIndex == 2) {
                Object o = getValueAt(rowIndex, columnIndex);
                Object date = getValueAt(rowIndex, 3);
                if (o instanceof Double && (Double) o > 80 &&
                        date instanceof Calendar && Calendar.getInstance().getTimeInMillis() - ((Calendar) date).getTimeInMillis() < 5000) {
                    return _stockPriceFlashStyle;
                }
            }
            else if (columnIndex == 0) {
                Object o = getValueAt(rowIndex, columnIndex);
                if (o.toString().startsWith("A")) {
                    return _stockNameFlashStyle;
                }
            }
            return null;
        }

        public boolean isCellStyleOn() {
            return true;
        }
    }
}

