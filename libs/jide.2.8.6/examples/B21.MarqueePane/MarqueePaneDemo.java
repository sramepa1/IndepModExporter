/*
 * @(#)MarqueePaneDemo.java 6/9/2009
 *
 * Copyright 2002 - 2009 JIDE Software Inc. All rights reserved.
 *
 */

import com.jidesoft.grid.JideTable;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Demoed Component: {@link com.jidesoft.swing.MarqueePane} <br> Required jar files: jide-common.jar <br> Required L&F:
 * any L&F
 */
public class MarqueePaneDemo extends AbstractDemo {
    private static final long serialVersionUID = 5611828470716987509L;
    MarqueePane _horizonMarqueeLeft;
    MarqueePane _verticalMarqueeUp;
    MarqueePane _verticalMarqueeDown;

    public MarqueePaneDemo() {
    }

    public String getName() {
        return "MarqueePane Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_COMMON;
    }

    @Override
    public String getDescription() {
        return "MarqueePane is a subclass of JScrollPane to display components with a limited space by rolling it left and right, up and down.\n" +
                "Demoed classes:\n" +
                "com.jidesoft.swing.MarqueePane";
    }

    @Override
    public Component getOptionsPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        JCheckBox freezeCheckBox = new JCheckBox("Freeze Auto Scrolling");
        freezeCheckBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    _horizonMarqueeLeft.stopAutoScrolling();
                    _verticalMarqueeUp.stopAutoScrolling();
                    _verticalMarqueeDown.stopAutoScrolling();
                }
                else {
                    _horizonMarqueeLeft.startAutoScrolling();
                    _verticalMarqueeUp.startAutoScrolling();
                    _verticalMarqueeDown.startAutoScrolling();
                }
            }
        });
        panel.add(freezeCheckBox);
        return panel;
    }

    public Component getDemoPanel() {
        StyledLabel styledLabel = new StyledLabel();
        customizeStyledLabel(styledLabel);

        MarqueePane horizonMarqueeLeft = new MarqueePane(styledLabel);
        horizonMarqueeLeft.setPreferredSize(new Dimension(250, 40));
        horizonMarqueeLeft.setBorder(BorderFactory.createCompoundBorder(new JideTitledBorder(new PartialEtchedBorder(PartialEtchedBorder.LOWERED, PartialSide.NORTH), "Scroll Left", JideTitledBorder.LEADING, JideTitledBorder.ABOVE_TOP),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)));

        JPanel demoPanel = new JPanel(new BorderLayout(5, 5));
        demoPanel.add(horizonMarqueeLeft, BorderLayout.BEFORE_FIRST_LINE);

        JTable table = new JideTable(new QuoteTableModel());

        MultilineLabel textArea = new MultilineLabel();
        textArea.setText("Obama welcomes bill to regulate tobacco \n" +
                "Fake Rockefeller found guilty of kidnapping \n" +
                "Al Qaeda fighters relocating, officials say \n" +
                "Navarrette: Haters looking for scapegoats \n" +
                "Avlon: 'Wingnuts' spread hate of Obama, Jews \n" +
                "Ticker: Palin knocks 'perverted' Letterman \n" +
                "Spokesman: Chastity Bono changing gender \n" +
                "iReport.com: Share stories of gender change \n" +
                "Robin Meade: Packing for presidential skydive \n" +
                "WLUK: Girl gets excuse note from Obama \n" +
                "Woman gives up home, car to help kids \n" +
                "9-month-old snatched from home  \n" +
                "WPLG: Cat killings becoming more violent \n" +
                "Cargo containers become beautiful homes \n" +
                "Fortune: Dare you ask for a raise now? \n" +
                "Truck loses load of cash, causes car jam  \n" +
                "Flying fish smack boater in head   \n" +
                "Dog eats bag of pot, gets high");

        MarqueePane verticalMarqueeUp = new MarqueePane(textArea);
        verticalMarqueeUp.setScrollDirection(MarqueePane.SCROLL_DIRECTION_UP);
        verticalMarqueeUp.setPreferredSize(new Dimension((int) horizonMarqueeLeft.getPreferredSize().getWidth(), 38));
        verticalMarqueeUp.setScrollAmount(1);
        verticalMarqueeUp.setStayPosition(14);
        verticalMarqueeUp.setBorder(BorderFactory.createCompoundBorder(new JideTitledBorder(new PartialEtchedBorder(PartialEtchedBorder.LOWERED, PartialSide.NORTH), "Scroll Up", JideTitledBorder.LEADING, JideTitledBorder.ABOVE_TOP),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)));

        MarqueePane verticalMarqueeDown = new MarqueePane(table);
        verticalMarqueeDown.setScrollDirection(MarqueePane.SCROLL_DIRECTION_DOWN);
        verticalMarqueeDown.setScrollDelay(200);
        verticalMarqueeDown.setStayDelay(1000);
        verticalMarqueeDown.setPreferredSize(new Dimension((int) horizonMarqueeLeft.getPreferredSize().getWidth(), 320));
        verticalMarqueeDown.setBorder(BorderFactory.createCompoundBorder(new JideTitledBorder(new PartialEtchedBorder(PartialEtchedBorder.LOWERED, PartialSide.NORTH), "Scroll Down", JideTitledBorder.LEADING, JideTitledBorder.ABOVE_TOP),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)));

        demoPanel.add(verticalMarqueeUp, BorderLayout.CENTER);
        demoPanel.add(verticalMarqueeDown, BorderLayout.AFTER_LAST_LINE);
        _horizonMarqueeLeft = horizonMarqueeLeft;
        _verticalMarqueeUp = verticalMarqueeUp;
        _verticalMarqueeDown = verticalMarqueeDown;
        return demoPanel;
    }

    private void customizeStyledLabel(StyledLabel styledLabel) {
        styledLabel.setText("GOOG   429.11   -6.51          DIA   87.64   -0.1          FXI   39.19   +1.12          GLD   93.62   -0.21          USO   39   +0.81          MSFT   22.25   +0.17");
        styledLabel.setForeground(Color.WHITE);
        int[] steps = new int[]{16, 5, 24, 4, 24, 5, 24, 5, 21, 5, 25, 5};
        int index = 0;
        for (int i = 0; i < steps.length; i++) {
            if (i % 2 == 0) {
                styledLabel.addStyleRange(new StyleRange(index, steps[i], Font.PLAIN, Color.WHITE, Color.BLACK, 0, Color.WHITE));
            }
            else {
                if (styledLabel.getText().charAt(index) == '-') {
                    styledLabel.addStyleRange(new StyleRange(index, steps[i], Font.PLAIN, Color.RED, Color.BLACK, 0, Color.WHITE));
                }
                else {
                    styledLabel.addStyleRange(new StyleRange(index, steps[i], Font.PLAIN, Color.GREEN, Color.BLACK, 0, Color.WHITE));
                }
            }
            index += steps[i];
        }
    }

    @Override
    public String getDemoFolder() {
        return "B8.JideScrollPane";
    }

    static public void main(String[] s) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new MarqueePaneDemo());
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
        private static final long serialVersionUID = -9125734576564111643L;

        public QuoteTableModel() {
            super(QUOTES, QUOTE_COLUMNS);
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    }
}