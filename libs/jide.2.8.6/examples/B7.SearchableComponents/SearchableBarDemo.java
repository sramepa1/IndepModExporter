/*
 * @(#)SearchableBarDemo.java 10/17/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.InputStream;

/**
 * Demoed Component: {@link com.jidesoft.swing.SearchableBar} <br> Required jar files: jide-common.jar,
 * jide-components.jar <br> Required L&F: Jide L&F extension required
 */
public class SearchableBarDemo extends AbstractDemo {
    private static final long serialVersionUID = 2129144432840257348L;

    public SearchableBar _textAreaSearchableBar;
    public SearchableBar _tableSearchableBar;

    public SearchableBarDemo() {
    }

    public String getName() {
        return "SearchableBar Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_COMMON;
    }

    @Override
    public String getDescription() {
        return "SearchableBar works with Searchable components to provide a full size panel to achieve the searching feature. \n" +
                "Comparing to default Searchable feature, SearchableBar is more appropriate for components such as " +
                "a large text area or table.\n" +
                "1. Press any a specified key stroke to start the search. In the demo, we use CTRL-F to start searching but it could be customized to any key stroke.\n" +
                "2. Press up/down arrow key to navigation to next or previous matching occurrence\n" +
                "3. Press Highlights button to select all matching occurrences\n" +
                "4. A lot of customization options using the API\n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.swing.SearchableBar";
    }

    @Override
    public Component getOptionsPanel() {
        JPanel switchPanel = new JPanel(new GridLayout(0, 1, 3, 3));

        final JRadioButton style1 = new JRadioButton("Full");
        final JRadioButton style2 = new JRadioButton("Compact");

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(style1);
        buttonGroup.add(style2);

        switchPanel.add(new JLabel("Styles:"));
        switchPanel.add(style1);
        switchPanel.add(style2);
        style1.setSelected(true);

        style1.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (style1.isSelected()) {
                    _tableSearchableBar.setCompact(false);
                    _textAreaSearchableBar.setCompact(false);
                }
            }
        });
        style2.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (style2.isSelected()) {
                    _tableSearchableBar.setCompact(true);
                    _textAreaSearchableBar.setCompact(true);
                }
            }
        });

        switchPanel.add(new JLabel("Options: "));

        final JCheckBox option1 = new JCheckBox("Show close button");
        final JCheckBox option2 = new JCheckBox("Show navigation buttons");
        final JCheckBox option3 = new JCheckBox("Show highlights button");
        final JCheckBox option4 = new JCheckBox("Show match case check box");
        final JCheckBox option5 = new JCheckBox("Show repeats check box");
        final JCheckBox option6 = new JCheckBox("Show status");

        switchPanel.add(option1);
        switchPanel.add(option2);
        switchPanel.add(option3);
        switchPanel.add(option4);
        switchPanel.add(option5);
        switchPanel.add(option6);

        option1.setSelected(true);
        option1.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                updateButtons(option1.isSelected(), SearchableBar.SHOW_CLOSE);
            }
        });

        option2.setSelected(true);
        option2.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                updateButtons(option2.isSelected(), SearchableBar.SHOW_NAVIGATION);
            }
        });

        option3.setSelected(true);
        option3.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                updateButtons(option3.isSelected(), SearchableBar.SHOW_HIGHLIGHTS);
            }
        });

        option4.setSelected(true);
        option4.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                updateButtons(option4.isSelected(), SearchableBar.SHOW_MATCHCASE);
            }
        });

        option5.setSelected(false);
        option5.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                updateButtons(option5.isSelected(), SearchableBar.SHOW_REPEATS);
            }
        });

        option6.setSelected(true);
        option6.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                updateButtons(option6.isSelected(), SearchableBar.SHOW_STATUS);
            }
        });

        return switchPanel;
    }

    private void updateButtons(boolean selected, int bit) {
        if (selected) {
            _textAreaSearchableBar.setVisibleButtons(_textAreaSearchableBar.getVisibleButtons() | bit);
            _tableSearchableBar.setVisibleButtons(_textAreaSearchableBar.getVisibleButtons() | bit);
        }
        else {
            _textAreaSearchableBar.setVisibleButtons(_textAreaSearchableBar.getVisibleButtons() & ~bit);
            _tableSearchableBar.setVisibleButtons(_textAreaSearchableBar.getVisibleButtons() & ~bit);
        }
    }

    public Component getDemoPanel() {
        JideTabbedPane tabbedPane = new JideTabbedPane();
        tabbedPane.addTab("JTextArea with SearchableBar", createSearchableTextArea());
        tabbedPane.addTab("JTable with SearchableBar", createSearchableTable());
        return tabbedPane;
    }

    private JPanel createSearchableTextArea() {
        final JTextComponent textArea = SearchableBarDemo.createEditor("Readme.txt");
        final JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(textArea), BorderLayout.CENTER);
        Searchable searchable = SearchableUtils.installSearchable(textArea);
        searchable.setRepeats(true);
        _textAreaSearchableBar = SearchableBar.install(searchable, KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_DOWN_MASK), new SearchableBar.Installer() {
            public void openSearchBar(SearchableBar searchableBar) {
                String selectedText = textArea.getSelectedText();
                if (selectedText != null && selectedText.length() > 0) {
                    searchableBar.setSearchingText(selectedText);
                }
                panel.add(searchableBar, BorderLayout.AFTER_LAST_LINE);
                panel.invalidate();
                panel.revalidate();
            }

            public void closeSearchBar(SearchableBar searchableBar) {
                panel.remove(searchableBar);
                panel.invalidate();
                panel.revalidate();
            }
        });
        _textAreaSearchableBar.getInstaller().openSearchBar(_textAreaSearchableBar);
        return panel;
    }

    private JPanel createSearchableTable() {
        JTable table = new JTable(new QuoteTableModel());
        table.setPreferredScrollableViewportSize(new Dimension(200, 100));
        table.setColumnSelectionAllowed(false);
        table.setRowSelectionAllowed(true);
        Searchable searchable = SearchableUtils.installSearchable(table);
        searchable.setRepeats(true);
        final JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        _tableSearchableBar = SearchableBar.install(searchable, KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_DOWN_MASK), new SearchableBar.Installer() {
            public void openSearchBar(SearchableBar searchableBar) {
                panel.add(searchableBar, BorderLayout.AFTER_LAST_LINE);
                panel.invalidate();
                panel.revalidate();
            }

            public void closeSearchBar(SearchableBar searchableBar) {
                panel.remove(searchableBar);
                panel.invalidate();
                panel.revalidate();
            }
        });
        _tableSearchableBar.setName("TableSearchableBar");
        return panel;
    }

    @Override
    public String getDemoFolder() {
        return "B7.SearchableComponents";
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        AbstractDemo.showAsFrame(new SearchableBarDemo());
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

    static class QuoteTableModel extends DefaultTableModel {
        private static final long serialVersionUID = -1590945383551825133L;

        public QuoteTableModel() {
            super(QUOTES, QUOTE_COLUMNS);
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    }

    public static JTextComponent createEditor(String fileName) {
        JTextComponent textComponent = new JTextArea() {
            @Override
            public Dimension getPreferredScrollableViewportSize() {
                return new Dimension(700, 400);
            }
        };
        Document doc = new DefaultStyledDocument();
        try {
            // try to start reading
            InputStream in = SearchableBarDemo.class.getResourceAsStream(fileName);
            if (in != null) {
                byte[] buff = new byte[4096];
                int nch;
                while ((nch = in.read(buff, 0, buff.length)) != -1) {
                    doc.insertString(doc.getLength(), new String(buff, 0, nch), null);
                }
                textComponent.setDocument(doc);
            }
            else {
                textComponent.setText("Copy readme.txt into the class output directory");
            }
        }
        catch (Exception e) {
            // ignore
        }
        return textComponent;
    }
}
