/*
 * @(#)PopupDemo.java 2/11/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.docking.DockableFrame;
import com.jidesoft.docking.DockableFramePopup;
import com.jidesoft.docking.DockableHolder;
import com.jidesoft.docking.DockingManager;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.plaf.UIDefaultsLookup;
import com.jidesoft.popup.JidePopup;
import com.jidesoft.swing.JideBoxLayout;
import com.jidesoft.swing.JideMenu;
import com.jidesoft.swing.SearchableUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Demoed Component: {@link com.jidesoft.popup.JidePopup} <br> Required jar files: jide-common.jar <br> Required L&F:
 * Jide L&F extension required
 */
public class PopupDemo extends AbstractDemo {
    public LocationCustomizationPanel _locationPanel;
    private static final long serialVersionUID = -8155526609074691406L;
    private JidePopup _popup;

    public PopupDemo() {
    }

    public String getName() {
        return "PopupDemo";
    }

    public String getProduct() {
        return PRODUCT_NAME_COMMON;
    }

    @Override
    public String[] getDemoSource() {
        return new String[]{"PopupDemo.java", "AnimationCustomizationPanel.java", "LocationCustomizationPanel.java"};
    }

    @Override
    public String getDescription() {
        return "JidePopup is a popup that is detachable, dragable, resizable, autohide when timeout or clicks outside.\n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.popup.JidePopup";
    }


    public Component getDemoPanel() {
        final JPanel panel = new JPanel();
        panel.setLayout(new JideBoxLayout(panel, JideBoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        final JButton attachedButton = new JButton();
        _popup = new JidePopup();
        _popup.setMovable(true);
        _popup.getContentPane().setLayout(new BorderLayout());
        JTextArea view = new JTextArea();
        view.setRows(10);
        view.setColumns(40);
        _popup.getContentPane().add(new JScrollPane(view));
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = menuBar.add(new JideMenu("File"));
        menu.add("<< Example >>");
        menuBar.add(new JideMenu("Edit"));
        menuBar.add(new JideMenu("Help"));
        _popup.setJMenuBar(menuBar);
        _popup.setDefaultFocusComponent(view);
        AbstractAction action = new AbstractAction("Show Attached Popup (regular popup)") {
            private static final long serialVersionUID = -3390684439104516428L;

            public void actionPerformed(ActionEvent e) {
                _popup.updateUI();
                _popup.setOwner(attachedButton);
                _popup.setResizable(true);
                _popup.setMovable(true);
                if (_popup.isPopupVisible()) {
                    _popup.hidePopup();
                }
                else {
                    _popup.showPopup();
                }
            }
        };
        attachedButton.setAction(action);
        attachedButton.setName("" + action.getValue(Action.NAME));
        panel.add(attachedButton);
        panel.add(Box.createVerticalStrut(12), JideBoxLayout.FIX);

        if ("window".equalsIgnoreCase(System.getProperty("docking.floatingContainerType"))) {
            final JButton attachedDockableFrameButton = new JButton();
            action = new AbstractAction("Show Attached Popup (dockable frame type popup)") {
                private static final long serialVersionUID = 394684988168614629L;

                public void actionPerformed(ActionEvent e) {
                    Container topLevelMenuContainer = attachedDockableFrameButton.getTopLevelAncestor();
                    DockingManager dockingManager;
                    JidePopup popup;
                    if (topLevelMenuContainer instanceof DockableHolder) {
                        dockingManager = ((DockableHolder) topLevelMenuContainer).getDockingManager();
                        popup = new DockableFramePopup(dockingManager) {
                            @Override
                            public DockableFrame createDockableFrame() {
                                return new DockableFrame("Popup", UIDefaultsLookup.getIcon("DockableFrame.defaultIcon"));
                            }
                        };
                    }
                    else {
                        popup = new JidePopup();
                    }

                    popup.setMovable(true);
                    popup.getContentPane().setLayout(new BorderLayout());
                    JTextArea view = new JTextArea();
                    view.setRows(10);
                    view.setColumns(40);
                    popup.getContentPane().add(new JScrollPane(view));
                    JMenuBar menuBar = new JMenuBar();
                    JMenu menu = menuBar.add(new JideMenu("File"));
                    menu.add("<< Example >>");
                    menuBar.add(new JideMenu("Edit"));
                    menuBar.add(new JideMenu("Help"));
                    popup.setJMenuBar(menuBar);
                    popup.setOwner(attachedDockableFrameButton);
                    popup.setResizable(true);
                    popup.setMovable(true);
                    popup.setDefaultFocusComponent(view);
                    popup.showPopup();
                }
            };
            attachedDockableFrameButton.setAction(action);
            panel.add(attachedDockableFrameButton);
            panel.add(Box.createVerticalStrut(12), JideBoxLayout.FIX);
        }

        final JButton detachedButton = new JButton();
        action = new AbstractAction("Show Detached Popup") {
            private static final long serialVersionUID = -3131778065777162209L;

            public void actionPerformed(ActionEvent e) {
                _popup.updateUI();
                _popup.setOwner(detachedButton);
                _popup.setResizable(true);
                _popup.setMovable(true);
                _popup.setAttachable(false);
                if (_popup.isPopupVisible()) {
                    _popup.hidePopup();
                }
                else {
                    _popup.showPopup(_locationPanel.getDisplayLocation());
                }
            }
        };
        detachedButton.setAction(action);
        detachedButton.setName("" + action.getValue(Action.NAME));
        panel.add(detachedButton);
        panel.add(Box.createVerticalStrut(12), JideBoxLayout.FIX);

        final JButton ideaButton = new JButton();
        final JidePopup ideaPopup = new JidePopup();
        ideaPopup.setMovable(false);
        JPanel ideaPanel = new JPanel(new BorderLayout());
        ideaPanel.setBorder(BorderFactory.createEmptyBorder(4, 2, 2, 2));
        final JComboBox comboBox = new JComboBox(getCountryNames());
        SearchableUtils.installSearchable(comboBox);
        ideaPanel.add(new JLabel("<HTML><B>Enter country name:</B></HTML>"), BorderLayout.BEFORE_FIRST_LINE);
        ideaPanel.add(comboBox);
        ideaPopup.getContentPane().add(ideaPanel);
        action = new AbstractAction("Show IntelliJ IDEA Ctrl-N Popup") {
            private static final long serialVersionUID = -4566205355290811944L;

            public void actionPerformed(ActionEvent e) {
                ideaPopup.setOwner(ideaButton.getTopLevelAncestor());
                ideaPopup.setResizable(false);
                ideaPopup.setMovable(false);
                ideaPopup.setDefaultFocusComponent(comboBox);
                ideaPopup.setOwner(ideaButton);
                comboBox.registerKeyboardAction(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        _popup.hidePopupImmediately();
                    }
                }, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
                if (ideaPopup.isPopupVisible()) {
                    ideaPopup.hidePopup();
                }
                else {
                    ideaPopup.showPopup(_locationPanel.getDisplayLocation());
                }
            }
        };
        ideaButton.setAction(action);
        ideaButton.setName("" + action.getValue(Action.NAME));
        panel.add(ideaButton);
        panel.add(Box.createVerticalStrut(12), JideBoxLayout.FIX);

        panel.add(Box.createGlue(), JideBoxLayout.VARY);
        return panel;
    }

    @Override
    public Component getOptionsPanel() {
        JPanel panel = new JPanel(new BorderLayout(6, 6));
        final JCheckBox jumpFunction = new JCheckBox("Enable \"Jump\" Function");
        jumpFunction.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (jumpFunction.isSelected()) {
                    _popup.setBackToOriginalInsets(new Insets(10, 10, 10, 10));
                }
                else {
                    _popup.setBackToOriginalInsets(new Insets(0, 0, 0, 0));
                }
            }
        });
        jumpFunction.setSelected(true);
        panel.add(jumpFunction);
        _locationPanel = new LocationCustomizationPanel();
        panel.add(_locationPanel, BorderLayout.AFTER_LAST_LINE);
        return panel;
    }

    @Override
    public String getDemoFolder() {
        return "B9.Popup";
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new PopupDemo());
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
}

