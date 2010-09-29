/*
 * @(#)ShrinkSearchableDemo.java 5/14/2009
 *
 * Copyright 2002 - 2009 JIDE Software Inc. All rights reserved.
 *
 */

import com.jidesoft.combobox.*;
import com.jidesoft.grid.*;
import com.jidesoft.list.AbstractGroupableListModel;
import com.jidesoft.list.GroupList;
import com.jidesoft.list.GroupListShrinkSearchableSupport;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.JideBoxLayout;
import com.jidesoft.swing.SearchableUtils;
import com.jidesoft.swing.TableSearchable;
import com.jidesoft.swing.TreeSearchable;
import com.jidesoft.tree.TreeShrinkSearchableSupport;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.Position;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

/**
 * Demoed Component: {@link com.jidesoft.grid.ShrinkSearchableSupport} <br> Required jar files: jide-common.jar,
 * jide-grids.jar</br>
 */
public class ShrinkSearchableDemo extends AbstractDemo {
    private static final long serialVersionUID = -1487209204059326031L;

    public ShrinkSearchableDemo() {
    }

    public String getName() {
        return "Shrinkable Searchable Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_GRIDS;
    }

    @Override
    public String getDescription() {
        return "ShrinkSearchableSupport is a collection of several classes that enable shrinkable quick search feature on JList, JComboBox or JTable.\n" +
                "1. Press any letter key to start the search and you will see the list will be shrunk based on what you typed\n" +
                "2. Press up/down arrow key to navigation to next or previous matching occurrence\n" +
                "3. Hold CTRL key while pressing up/down arrow key to to multiple selection\n" +
                "4. Press CTRL+A to select all matching occurrences\n" +
                "5. Use '?' to match any character or '*' to match several characters (except in JTextComponent) \n" +
                "6. A lot of customization options using the API\n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.swing.Searchable\n" +
                "com.jidesoft.swing.ListSearchable\n" +
                "com.jidesoft.swing.TreeSearchable\n" +
                "com.jidesoft.swing.ComboBoxSearchable\n" +
                "com.jidesoft.swing.TableSearchable\n" +
                "com.jidesoft.swing.SearchableUtils\n" +
                "com.jidesoft.swing.Searchable\n" +
                "com.jidesoft.grid.ShrinkSearchableSupport\n" +
                "com.jidesoft.grid.ListShrinkSearchableSupport\n" +
                "com.jidesoft.list.GroupListShrinkSearchableSupport\n" +
                "com.jidesoft.tree.TreeShrinkSearchableSupport\n" +
                "com.jidesoft.grid.TableShrinkSearchableSupport\n" +
                "com.jidesoft.grid.ComboBoxShrinkSearchableSupport\n" +
                "com.jidesoft.grid.ListComboBoxShrinkSearchableSupport\n" +
                "com.jidesoft.grid.TableComboBoxShrinkSearchableSupport\n";
    }

    public Component getDemoPanel() {
        JTree tree = new JTree() {
            @Override
            public TreePath getNextMatch(String prefix, int startingRow, Position.Bias bias) {
                return null;
            }
        };
        tree.setVisibleRowCount(15);
        final TreeSearchable treeSearchable = SearchableUtils.installSearchable(tree);
        new TreeShrinkSearchableSupport(treeSearchable);

        JList list = new JList(getCountryNames()) {
            @Override
            public int getNextMatch(String prefix, int startIndex, Position.Bias bias) {
                return -1;
            }
        };
        list.setVisibleRowCount(10);
        new ListShrinkSearchableSupport(SearchableUtils.installSearchable(list));

        JComboBox comboBox = new JComboBox(getCountryNames());
        comboBox.setEditable(false); // combobox searchable only works when combobox is not editable.
        new ComboBoxShrinkSearchableSupport(SearchableUtils.installSearchable(comboBox));

        ListComboBox listComboBox = new ListComboBox(getCountryNames());
        listComboBox.setEditable(false); // combobox searchable only works when combobox is not editable.
        new ListComboBoxShrinkSearchableSupport(new ListComboBoxSearchable(listComboBox));

        TableComboBox tableComboBox = new TableComboBox(new QuoteTableModel());
        tableComboBox.setEditable(false); // combobox searchable only works when combobox is not editable.
        new TableComboBoxShrinkSearchableSupport(new TableComboBoxSearchable(tableComboBox));

        JTable table = new JTable(new QuoteTableModel());
        table.setPreferredScrollableViewportSize(new Dimension(200, 100));
        table.setColumnSelectionAllowed(false);
        table.setRowSelectionAllowed(true);
        TableSearchable tableSearchable = SearchableUtils.installSearchable(table);
        tableSearchable.setMainIndex(-1); // search for all columns
        new TableShrinkSearchableSupport(tableSearchable);

        FontModel model = new FontModel();
        model.shuffle();
        model.putFont((String) model.getElementAt(2));
        model.putFont((String) model.getElementAt(model.getSize() - 1));

        final GroupList groupList = new GroupList(model) {
            @Override
            public int getNextMatch(String prefix, int startIndex, Position.Bias bias) {
                return -1;
            }
        };
        groupList.setVisibleRowCount(10);
        groupList.setCellRenderer(new FontListCellRenderer());
        groupList.setGroupCellRenderer(new GroupCellRenderer());
        new GroupListShrinkSearchableSupport(SearchableUtils.installSearchable(groupList));

        JPanel treePanel = createTitledPanel(new JLabel("Searchable JTree"), 'E', new JScrollPane(tree));
        JCheckBox checkbox = new JCheckBox("Recursive");
        checkbox.setMnemonic('R');
        checkbox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    treeSearchable.setRecursive(true);
                }
                else {
                    treeSearchable.setRecursive(false);
                }
            }
        });
        checkbox.setSelected(treeSearchable.isRecursive());
        treePanel.add(checkbox, BorderLayout.AFTER_LAST_LINE);

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new JideBoxLayout(listPanel, JideBoxLayout.Y_AXIS, 6));
        listPanel.add(createTitledPanel(new JLabel("Searchable JList"), 'L', new JScrollPane(list)), JideBoxLayout.VARY);
        listPanel.add(createTitledPanel(new JLabel("Searchable GroupList"), 'L', new JScrollPane(groupList)), JideBoxLayout.FIX);
        listPanel.add(createTitledPanel(new JLabel("Searchable JComboBox"), 'C', comboBox), JideBoxLayout.FIX);
        listPanel.add(createTitledPanel(new JLabel("Searchable ListComboBox (in JIDE Grids)"), 'I', listComboBox), JideBoxLayout.FIX);
        listPanel.add(createTitledPanel(new JLabel("Searchable TableComboBox (in JIDE Grids)"), 'T', tableComboBox), JideBoxLayout.FIX);

        // add to the parent panel
        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 10));
        panel.add(listPanel);

        JPanel tableTreePanel = new JPanel(new BorderLayout(5, 5));
        tableTreePanel.add(createTitledPanel(new JLabel("Searchable JTable (Configured to search for \"symbol\" column only.)"), 'T', new JScrollPane(table)));
        tableTreePanel.add(treePanel, BorderLayout.AFTER_LAST_LINE);
        panel.add(tableTreePanel);

        return panel;
    }

    @Override
    public String getDemoFolder() {
        return "B7.SearchableComponents";
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new ShrinkSearchableDemo());
    }

    private static JPanel createTitledPanel(JLabel label, char mnemonic, JComponent component) {
        JPanel panel = new JPanel(new BorderLayout(2, 2));
        panel.add(label, BorderLayout.BEFORE_FIRST_LINE);
        label.setDisplayedMnemonic(mnemonic);
        if (component instanceof JScrollPane) {
            label.setLabelFor(((JScrollPane) component).getViewport().getView());
        }
        else {
            label.setLabelFor(component);
        }
        panel.add(component, BorderLayout.CENTER);
        return panel;
    }

    public static String[] getCountryNames() {
        return new String[]{
                "Andorra",
                "United Arab Emirates",
                "Afghanistan",
                "Antigua And Barbuda",
                "Anguilla",
                "Albania",
                "Armenia",
                "Netherlands Antilles",
                "Angola",
                "Antarctica",
                "Argentina",
                "American Samoa",
                "Austria",
                "Australia",
                "Aruba",
                "Azerbaijan",
                "Bosnia And Herzegovina",
                "Barbados",
                "Bangladesh",
                "Belgium",
                "Burkina Faso",
                "Bulgaria",
                "Bahrain",
                "Burundi",
                "Benin",
                "Bermuda",
                "Brunei Darussalam",
                "Bolivia",
                "Brazil",
                "Bahamas",
                "Bhutan",
                "Bouvet Island",
                "Botswana",
                "Belarus",
                "Belize",
                "Canada",
                "Cocos (Keeling) Islands",
                "Congo, The Democratic Republic Of The",
                "Central African Republic",
                "Congo",
                "Switzerland",
                "Côte D'Ivoire",
                "Cook Islands",
                "Chile",
                "Cameroon",
                "China",
                "Colombia",
                "Costa Rica",
                "Cuba",
                "Cape Verde",
                "Christmas Island",
                "Cyprus",
                "Czech Republic",
                "Germany",
                "Djibouti",
                "Denmark",
                "Dominica",
                "Dominican Republic",
                "Algeria",
                "Ecuador",
                "Estonia",
                "Egypt",
                "Western Sarara",
                "Eritrea",
                "Spain",
                "Ethiopia",
                "Finland",
                "Fiji",
                "Falkland Islands (Malvinas)",
                "Micronesia, Federated States Of",
                "Faroe Islands",
                "France",
                "Gabon",
                "United Kingdom",
                "Grenada",
                "Georgia",
                "French Guiana",
                "Ghana",
                "Gibraltar",
                "Greenland",
                "Gambia",
                "Guinea",
                "Guadeloupe",
                "Equatorial Guinea",
                "Greece",
                "South Georgia And The South Sandwich Islands",
                "Guatemala",
                "Guam",
                "Guinea-bissau",
                "Guyana",
                "Hong Kong",
                "Heard Island And Mcdonald Islands",
                "Honduras",
                "Croatia",
                "Haiti",
                "Hungary",
                "Indonesia",
                "Ireland",
                "Israel",
                "India",
                "British Indian Ocean Territory",
                "Iraq",
                "Iran, Islamic Republic Of",
                "Iceland",
                "Italy",
                "Jamaica",
                "Jordan",
                "Japan",
                "Kenya",
                "Kyrgyzstan",
                "Cambodia",
                "Kiribati",
                "Comoros",
                "Saint Kitts And Nevis",
                "Korea, Democratic People'S Republic Of",
                "Korea, Republic Of",
                "Kuwait",
                "Cayman Islands",
                "Kazakhstan",
                "Lao People'S Democratic Republic",
                "Lebanon",
                "Saint Lucia",
                "Liechtenstein",
                "Sri Lanka",
                "Liberia",
                "Lesotho",
                "Lithuania",
                "Luxembourg",
                "Latvia",
                "Libyan Arab Jamabiriya",
                "Morocco",
                "Monaco",
                "Moldova, Republic Of",
                "Madagascar",
                "Marshall Islands",
                "Macedonia, The Former Yugoslav Repu8lic Of",
                "Mali",
                "Myanmar",
                "Mongolia",
                "Macau",
                "Northern Mariana Islands",
                "Martinique",
                "Mauritania",
                "Montserrat",
                "Malta",
                "Mauritius",
                "Maldives",
                "Malawi",
                "Mexico",
                "Malaysia",
                "Mozambique",
                "Namibia",
                "New Caledonia",
                "Niger",
                "Norfolk Island",
                "Nigeria",
                "Nicaragua",
                "Netherlands",
                "Norway",
                "Nepal",
                "Niue",
                "New Zealand",
                "Oman",
                "Panama",
                "Peru",
                "French Polynesia",
                "Papua New Guinea",
                "Philippines",
                "Pakistan",
                "Poland",
                "Saint Pierre And Miquelon",
                "Pitcairn",
                "Puerto Rico",
                "Portugal",
                "Palau",
                "Paraguay",
                "Qatar",
                "Réunion",
                "Romania",
                "Russian Federation",
                "Rwanda",
                "Saudi Arabia",
                "Solomon Islands",
                "Seychelles",
                "Sudan",
                "Sweden",
                "Singapore",
                "Saint Helena",
                "Slovenia",
                "Svalbard And Jan Mayen",
                "Slovakia",
                "Sierra Leone",
                "San Marino",
                "Senegal",
                "Somalia",
                "Suriname",
                "Sao Tome And Principe",
                "El Salvador",
                "Syrian Arab Republic",
                "Swaziland",
                "Turks And Caicos Islands",
                "Chad",
                "French Southern Territories",
                "Togo",
                "Thailand",
                "Tajikistan",
                "Tokelau",
                "Turkmenistan",
                "Tunisia",
                "Tonga",
                "East Timor",
                "Turkey",
                "Trinidad And Tobago",
                "Tuvalu",
                "Taiwan, Province Of China",
                "Tanzania, United Republic Of",
                "Ukraine",
                "Uganda",
                "United States Minor Outlying Islands",
                "United States",
                "Uruguay",
                "Uzbekistan",
                "Venezuela",
                "Virgin Islands, British",
                "Virgin Islands, U.S.",
                "Viet Nam",
                "Vanuatu",
                "Wallis And Futuna",
                "Samoa",
                "Yemen",
                "Mayotte",
                "Yugoslavia",
                "South Africa",
                "Zambia",
                "Zimbabwe"
        };
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
        private static final long serialVersionUID = 7487309284053329041L;

        public QuoteTableModel() {
            super(QUOTES, QUOTE_COLUMNS);
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    }

    class FontModel extends AbstractGroupableListModel {
        private static final long serialVersionUID = 948780704659524032L;

        private final String[] FONT_NAMES =
                GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

        private String[] GROUP_NAMES = {"Recently Used Fonts", "All Fonts"};

        private java.util.List<String> _fonts;
        private int _limit;
        private java.util.List<String> _recentlyUsed;

        public FontModel() {
            this(8);
        }

        public FontModel(int limit) {
            _limit = limit;
            _recentlyUsed = new LinkedList<String>();
            _fonts = new ArrayList<String>();
            _fonts.addAll(Arrays.asList(FONT_NAMES));
        }

        @Override
        public Object getGroupAt(int index) {
            if (index < _recentlyUsed.size()) {
                return GROUP_NAMES[0];
            }
            else {
                return GROUP_NAMES[1];
            }
        }

        public int getSize() {
            return _recentlyUsed.size() + _fonts.size();
        }

        public Object getElementAt(int index) {
            int usedSize = _recentlyUsed.size();
            if (index < usedSize) {
                return _recentlyUsed.get(index);
            }
            return _fonts.get(index - usedSize);
        }

        public java.util.List<String> getRecentlyUsedFont() {
            return Collections.unmodifiableList(_recentlyUsed);
        }

        public void putFont(String font) {
            if (_recentlyUsed.contains(font)) {
                _recentlyUsed.remove(font);
            }
            _recentlyUsed.add(0, font);
            if (_recentlyUsed.size() > _limit) {
                _recentlyUsed.remove(_recentlyUsed.size() - 1);
            }
            super.fireGroupChanged(this);
        }

        @Override
        public Object[] getGroups() {
            return GROUP_NAMES;
        }

        public void remove(int index) {
            int usedSize = _recentlyUsed.size();
            if (index < usedSize) {
                _recentlyUsed.remove(index);
            }
            else {
                _fonts.remove(index - usedSize);
            }
            fireGroupChanged(this);
        }

        public void shuffle() {
            Collections.shuffle(_recentlyUsed);
            Collections.shuffle(_fonts);
            fireGroupChanged(this);
        }
    }
}