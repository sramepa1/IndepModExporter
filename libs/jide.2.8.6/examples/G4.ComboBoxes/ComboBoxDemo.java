/*
 * @(#)ComboBoxDemo.java 2/14/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.combobox.*;
import com.jidesoft.grid.SortableTable;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.JideBoxLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Demoed Component: {@link ListComboBox}, {@link FileChooserComboBox}, {@link ColorComboBox}, {@link DateComboBox} <br>
 * Required jar files: jide-common.jar, jide-grids.jar <br> Required L&F: Jide L&F extension required
 */
public class ComboBoxDemo extends AbstractDemo {
    private static final long serialVersionUID = -2758050378982771174L;

    public ComboBoxDemo() {
    }

    public String getName() {
        return "ComboBox Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_GRIDS;
    }

    @Override
    public int getAttributes() {
        return ATTRIBUTE_UPDATED;
    }

    @Override
    public String getDescription() {
        return "This is a demo of various comboboxes. \n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.combobox.DateComboBox\n" +
                "com.jidesoft.combobox.ColorComboBox\n" +
                "com.jidesoft.combobox.TreeComboBox\n" +
                "com.jidesoft.combobox.ListComboBox\n" +
                "com.jidesoft.combobox.FileChooserComboBox";
    }

    private JPanel createTitledComponent(String label, String toolTip, JComponent component) {
        JPanel panel = new JPanel();
        panel.setLayout(new JideBoxLayout(panel, JideBoxLayout.Y_AXIS, 3));
        component.setToolTipText(toolTip);
        component.setName(label);
        panel.add(new JLabel(label));
        panel.add(component);
        panel.add(Box.createGlue(), JideBoxLayout.VARY);
        return panel;
    }

    public Component getDemoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2, 20, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JComboBox comboBox = new JComboBox(new String[]{"True", "False"});
        comboBox.setEditable(true);
        panel.add(createTitledComponent("Regular Editable JComboBox (For Comparison)", "Regular Editable JComboBox (For Comparison)", comboBox));

        JComboBox nonEditableComboBox = new JComboBox(new String[]{"True", "False"});
        nonEditableComboBox.setEditable(false);
        panel.add(createTitledComponent("Regular Non-editable JComboBox (For Comparison)", "Regular Non-editable JComboBox (For Comparison)", nonEditableComboBox));

        panel.add(new JPanel());
        panel.add(new JPanel());

        AbstractComboBox booleanComboBox = createBooleanComboBox();
        booleanComboBox.setSelectedItem(Boolean.FALSE);
        panel.add(createTitledComponent("Editable ListComboBox (Boolean)", "ComboBox to choose boolean", booleanComboBox));

        AbstractComboBox booleanComboBox2 = createBooleanComboBox();
        booleanComboBox2.setEditable(false);
        booleanComboBox2.setSelectedItem(Boolean.FALSE);
        panel.add(createTitledComponent("Non-editable ListComboBox (Boolean)", "ComboBox to choose boolean", booleanComboBox2));

        // create font name combobox
        ListComboBox fontNameComboBox = createListComboBox();
        fontNameComboBox.setSelectedItem("Arial");
        panel.add(createTitledComponent("Editable ListComboBox (Font Name)", "ComboBox to choose font name or type in font name directly", fontNameComboBox));

        ListComboBox fontNameComboBox2 = createListComboBox();
        fontNameComboBox2.setEditable(false);
        new ListComboBoxSearchable(fontNameComboBox2);
        fontNameComboBox2.setSelectedItem("Arial");
        panel.add(createTitledComponent("Non-editable ListComboBox (Font Name)", "ComboBox to choose font name from a list", fontNameComboBox2));

        CheckBoxListComboBox checkBoxListComboBox = createCheckBoxListComboBox();
        checkBoxListComboBox.setSelectedItem(new String[]{"Arial", "Verdana"});
        panel.add(createTitledComponent("CheckBoxListComboBox (Font Name) (Editable)", "ComboBox to choose multiple font names or type in font names directly", checkBoxListComboBox));

        CheckBoxListComboBox checkBoxListComboBox2 = createCheckBoxListComboBox();
        checkBoxListComboBox2.setEditable(false);
//        new ListComboBoxSearchable(checkBoxListComboBox2);
        checkBoxListComboBox2.setSelectedItem(new String[]{"Arial", "Verdana"});
        panel.add(createTitledComponent("Non-editable CheckBoxListComboBox (Font Name)", "ComboBox to choose multiple font names", checkBoxListComboBox2));

        MultiSelectListComboBox multiSelectListComboBox = createMultiSelectListComboBox();
        multiSelectListComboBox.setSelectedItem(new String[]{"Arial", "Verdana"});
        panel.add(createTitledComponent("MultiSelectListComboBox (Font Name) (Editable)", "ComboBox to choose multiple font names or type in font names directly", multiSelectListComboBox));

        MultiSelectListComboBox multiSelectListComboBox2 = createMultiSelectListComboBox();
        multiSelectListComboBox2.setEditable(false);
//        new ListComboBoxSearchable(multiSelectListComboBox2);
        multiSelectListComboBox2.setSelectedItem(new String[]{"Arial", "Verdana"});
        panel.add(createTitledComponent("Non-editable MultiSelectListComboBox (Font Name)", "ComboBox to choose multiple font names", multiSelectListComboBox2));

        AbstractComboBox treeComboBox = new TreeComboBox();
        panel.add(createTitledComponent("Editable TreeComboBox", "ComboBox which has a tree as the selection list", treeComboBox));

        TreeComboBox treeComboBox2 = new TreeComboBox() {
            @Override
            protected boolean isValidSelection(TreePath path) {
                TreeNode treeNode = (TreeNode) path.getLastPathComponent();
                return treeNode.isLeaf();
            }
        };
        treeComboBox2.setEditable(false);
        TreeComboBoxSearchable treeComboBoxSearchable = new TreeComboBoxSearchable(treeComboBox2);
        treeComboBoxSearchable.setRecursive(true);
        panel.add(createTitledComponent("Non-editable TreeComboBox (only leafs are selectable)", "ComboBox which has a tree as the selection list", treeComboBox2));

        TableComboBox tableComboBox = new TableComboBox(new QuoteTableModel()) {
            @Override
            protected JTable createTable(TableModel model) {
                return new SortableTable(model);
            }
        };
        tableComboBox.setSelectedItem("ALCOA INC");
        tableComboBox.setMaximumRowCount(12);
        tableComboBox.setValueColumnIndex(1); // display the second column value in the combobox.
        panel.add(createTitledComponent("Editable TableComboBox", "ComboBox which has a table as the selection list", tableComboBox));

        TableComboBox tableComboBox2 = new TableComboBox(new QuoteTableModel()) {
            @Override
            protected JTable createTable(TableModel model) {
                return new SortableTable(model);
            }
        };
        tableComboBox2.setSelectedItem("ALCOA INC");
        tableComboBox2.setEditable(false);
        new TableComboBoxSearchable(tableComboBox2);
        tableComboBox2.setMaximumRowCount(12);
        tableComboBox2.setValueColumnIndex(1); // display the second column value in the combobox.
        panel.add(createTitledComponent("Non-editable TableComboBox", "ComboBox which has a table as the selection list", tableComboBox2));

        // create date combobox
        DateComboBox dateComboBox = new DateComboBox();
        dateComboBox.setDate(null);
        panel.add(createTitledComponent("DateComboBox", "ComboBox to choose date", dateComboBox));

        // create date combobox
        DateComboBox datetimeComboBox = new DateComboBox();
        datetimeComboBox.setTimeDisplayed(true);
        datetimeComboBox.setFormat(DateFormat.getDateTimeInstance());
        datetimeComboBox.setTimeFormat("hh:mm:ss a");
        datetimeComboBox.setDate(Calendar.getInstance().getTime());
        panel.add(createTitledComponent("DateComboBox (Time Displayed)", "ComboBox to choose date and time", datetimeComboBox));

        // create month combobox
        MonthComboBox monthComboBox = new MonthComboBox();
        monthComboBox.setFormat(new SimpleDateFormat("MM/yyyy"));
        monthComboBox.setDate(null);
        panel.add(createTitledComponent("MonthComboBox", "ComboBox to choose a month", monthComboBox));

//        monthComboBox.addItemListener(new ItemListener() {
//            public void itemStateChanged(ItemEvent e) {
//                if (e.getStateChange() == ItemEvent.SELECTED) {
//                    System.out.println("Date selected: " + ObjectConverterManager.toString(e.getItem()));
//                }
//            }
//        });

        // create date combobox
        MultilineStringComboBox multilineStringComboBox = new MultilineStringComboBox();
        panel.add(createTitledComponent("MultilineStringComboBox", "ComboBox to choose multiple line text", multilineStringComboBox));

        // create color combobox
        ColorComboBox colorComboBox = new ColorComboBox();
        colorComboBox.setSelectedColor(Color.GREEN);
        colorComboBox.setInvalidValueAllowed(true);
        panel.add(createTitledComponent("ColorComboBox", "ComboBox to choose color", colorComboBox));

        // create font combobox
        FontComboBox fontComboBox = new FontComboBox();
        fontComboBox.setToolTipText("ComboBox to choose a font");
        panel.add(createTitledComponent("FontComboBox", "ComboBox to choose a font", fontComboBox));

        // create file chooser combobox
        FileChooserComboBox fileComboBox = new FileChooserComboBox();
        panel.add(createTitledComponent("FileChooserComboBox", "ComboBox to choose a file", fileComboBox));

        // create insets combobox
        InsetsComboBox insetsComboBox = new InsetsComboBox();
        panel.add(createTitledComponent("InsetsComboBox", "ComboBox to choose an insets", insetsComboBox));

        // create DateSpinnerComboBox
        DateSpinnerComboBox dateSpinnerComboBox = new DateSpinnerComboBox();
        panel.add(createTitledComponent("DateSpinnerComboBox", "DateSpinnerComboBox", dateSpinnerComboBox));

        // set it to empty mainly for Windows L&F as the extra gap doesn't look when spinner is inside the combobox.
        // UIManager.put("Spinner.arrowButtonInsets", new InsetsUIResource(0, 0, 0, 0));

        // create NumberSpinnerComboBox
        SpinnerNumberModel numberModel = new SpinnerNumberModel(10.00, 0.00, 99.99, 0.01);
        NumberSpinnerComboBox numberSpinnerComboBox = new NumberSpinnerComboBox(numberModel) {
            private JPopupMenu _menu;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (e != null) { // when event is not null, it means the combobox button is pressed.
                    if (_menu == null || !_menu.isVisible()) {
                        _menu = new JPopupMenu();
                        for (int i = 0; i < 9; i++) {
                            _menu.add(createMenuItem(10.0 + i * 10.0, this));
                        }
                        JComponent c = (JComponent) e.getSource();
                        _menu.show(c, -_menu.getPreferredSize().width + c.getWidth(), c.getHeight());
                    }
                    else {
                        _menu.setVisible(false);
                        _menu = null;
                    }
                }
            }

            @Override
            public PopupPanel createPopupComponent() {
                return null;
            }
        };
        numberSpinnerComboBox.setDecimalFormatPattern("0.00");
        panel.add(createTitledComponent("NumberSpinnerComboBox", "NumberSpinnerComboBox", numberSpinnerComboBox));

        return panel;
    }

    private JMenuItem createMenuItem(final double value, final NumberSpinnerComboBox comboBox) {
        JMenuItem item = new JMenuItem(new DecimalFormat("0.00").format(value));
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                comboBox.setSelectedItem(value);
            }
        });
        return item;
    }

    @Override
    public String getDemoFolder() {
        return "G4.ComboBoxes";
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new ComboBoxDemo());
    }

    private static ListComboBox createBooleanComboBox() {
        return new ListComboBox(ListComboBox.BOOLEAN_ARRAY, Boolean.class);
    }

    private static ListComboBox createListComboBox() {
        String[] fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        return new ListComboBox(fontNames, String.class);
    }

    private static CheckBoxListComboBox createCheckBoxListComboBox() {
        String[] fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        return new CheckBoxListComboBox(fontNames, String[].class);
    }

    private static MultiSelectListComboBox createMultiSelectListComboBox() {
        String[] fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        return new MultiSelectListComboBox(fontNames, String[].class);
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
        private static final long serialVersionUID = -1127902635797157517L;

        public QuoteTableModel() {
            super(QUOTES, QUOTE_COLUMNS);
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    }
}
