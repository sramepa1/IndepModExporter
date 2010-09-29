/*
 * @(#)JideTabbedPaneDemo.java
 *
 * Copyright 2002 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.dialog.JideOptionPane;
import com.jidesoft.icons.IconsFactory;
import com.jidesoft.icons.JideIconsFactory;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.plaf.UIDefaultsLookup;
import com.jidesoft.swing.JideBoxLayout;
import com.jidesoft.swing.JideTabbedPane;
import com.jidesoft.swing.TabEditingValidator;

import javax.swing.*;
import javax.swing.plaf.UIResource;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Demoed Component: JideTabbedPane <br> Required jar files: jide-common.jar <br> Required L&F: Jide L&F extension
 * required
 */
public class JideTabbedPaneDemo extends AbstractDemo {
    private static final long serialVersionUID = 9174217322756338307L;

    private static JideTabbedPane _tabbedPane;
    private static boolean _allowDuplicateTabNames = false;
    static JideTabbedPaneDemo demo = new JideTabbedPaneDemo();
    private static JPanel _panel;

    private final String WIN2K_COLOR_THEME = "Win2K";
    private final String OFFICE2003_COLOR_THEME = "Office2003";
    private final String VSNET_COLOR_THEME = "Vsnet";
    private final String WINXP_COLOR_THEME = "WinXP";

    public JideTabbedPaneDemo() {
    }

    public String getName() {
        return "JideTabbedPane Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_COMMON;
    }

    @Override
    public String getDescription() {
        return "JideTabbedPane is an extended version JTabbedPane. The additional features are \n" +
                "1. Has an option to hide the tab area if there is only one component in a tabbed pane\n" +
                "2. Has an option to shrink the tab width so that all tabs can always fit in one row\n" +
                "3. Has an option to show scroll left and scroll right buttons and a close button in the right corner of the tab area\n" +
                "4. Has an option to show a close button on tab\n" +
                "5. Supports in-placing tab editing. Mouse double clicks on tab to start editing\n" +
                "6. Supports various LookAndFeels\n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.swing.JideTabbedPane\n";
    }

    public Component getDemoPanel() {
        _panel = new JPanel(new BorderLayout());
        _panel.setOpaque(true);
        _panel.setBackground(UIDefaultsLookup.getColor("control"));
        _panel.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

        _tabbedPane = createTabbedPane();

        _panel.add(_tabbedPane, BorderLayout.CENTER);
        return _panel;
    }

    @Override
    public String getDemoFolder() {
        return "B6.JideTabbedPane";
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new JideTabbedPaneDemo());
    }

    @Override
    public Component getOptionsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new JideBoxLayout(panel, BoxLayout.Y_AXIS, 2));

        final JCheckBox show = new JCheckBox("Always Show Buttons");
        show.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _tabbedPane.setShowTabButtons(show.isSelected());
            }
        });

        final JCheckBox hide = new JCheckBox("Hidden Tab Area if Only One Tab");
        hide.setSelected(_tabbedPane.isHideOneTab());
        hide.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _tabbedPane.setHideOneTab(hide.isSelected());
            }
        });

        final JCheckBox showTab = new JCheckBox("Show Tab Area");
        showTab.setSelected(_tabbedPane.isShowTabArea());
        showTab.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _tabbedPane.setShowTabArea(showTab.isSelected());
            }
        });

        final JCheckBox showTabContent = new JCheckBox("Show Tab Content");
        showTabContent.setSelected(_tabbedPane.isShowTabContent());
        showTabContent.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                boolean content = showTabContent.isSelected();
                _tabbedPane.setShowTabContent(content);
                if (content) {
                    _panel.add(_tabbedPane, BorderLayout.CENTER);
                }
                else {
                    _panel.add(_tabbedPane, BorderLayout.BEFORE_FIRST_LINE);
                }

            }
        });

        final JCheckBox useDefaultIcon = new JCheckBox("Use Default Value of Show Icons");

        final JCheckBox icon = new JCheckBox("Show Icons");
        icon.setSelected(_tabbedPane.isShowIconsOnTab());
        icon.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _tabbedPane.setShowIconsOnTab(icon.isSelected());
            }
        });

        useDefaultIcon.setSelected(_tabbedPane.isUseDefaultShowIconsOnTab());
        icon.setEnabled(!useDefaultIcon.isSelected());
        useDefaultIcon.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _tabbedPane.setUseDefaultShowIconsOnTab(useDefaultIcon.isSelected());
                icon.setEnabled(!useDefaultIcon.isSelected());
            }
        });

        final JCheckBox showCloseButton = new JCheckBox("Show Close Button");
        showCloseButton.setSelected(_tabbedPane.isShowCloseButton());
        showCloseButton.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _tabbedPane.setShowCloseButton(showCloseButton.isSelected());
            }
        });

        final JCheckBox useDefaultClose = new JCheckBox("Use Default Value of Show Close Button On Tab");

        final JCheckBox close = new JCheckBox("Show Close Button on Tab");
        close.setSelected(_tabbedPane.isShowCloseButtonOnTab());

        useDefaultClose.setSelected(_tabbedPane.isUseDefaultShowCloseButtonOnTab());
        close.setEnabled(!useDefaultClose.isSelected());
        useDefaultClose.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _tabbedPane.setUseDefaultShowCloseButtonOnTab(useDefaultClose.isSelected());
                close.setEnabled(!useDefaultClose.isSelected());
            }
        });

        final JCheckBox closeOnSelectedTab = new JCheckBox("Show Close Button On Selected Tab");
        closeOnSelectedTab.setSelected(_tabbedPane.isShowCloseButtonOnSelectedTab());
        closeOnSelectedTab.setEnabled(close.isSelected());
        closeOnSelectedTab.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _tabbedPane.setShowCloseButtonOnSelectedTab(closeOnSelectedTab.isSelected());
                SwingUtilities.updateComponentTreeUI(_tabbedPane);
            }
        });

        close.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _tabbedPane.setShowCloseButtonOnTab(close.isSelected());
                closeOnSelectedTab.setEnabled(close.isSelected());
            }
        });


        final JCheckBox toggleFirstTabClosable = new JCheckBox("The First Tab is Closable");
        toggleFirstTabClosable.setSelected(_tabbedPane.isTabClosableAt(0));
        toggleFirstTabClosable.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _tabbedPane.setTabClosableAt(0, toggleFirstTabClosable.isSelected());
            }
        });

        final JCheckBox toggleMiddleMouseButtonCloseTab = new JCheckBox("Middle Mouse Button to Close the Tab");
        toggleMiddleMouseButtonCloseTab.setSelected(_tabbedPane.isCloseTabOnMouseMiddleButton());
        toggleMiddleMouseButtonCloseTab.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _tabbedPane.setCloseTabOnMouseMiddleButton(toggleMiddleMouseButtonCloseTab.isSelected());
            }
        });

        final JCheckBox editing = new JCheckBox("Allow Editing Tab Title");
        editing.setSelected(_tabbedPane.isTabEditingAllowed());
        editing.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _tabbedPane.setTabEditingAllowed(editing.isSelected());
            }
        });

        final JCheckBox allowDuplicateTabNames = new JCheckBox("Allow Duplicate Tab Names");
        allowDuplicateTabNames.setSelected(_tabbedPane.isTabEditingAllowed());
        allowDuplicateTabNames.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _allowDuplicateTabNames = allowDuplicateTabNames.isSelected();
            }
        });

        final JCheckBox bold = new JCheckBox("Bold Active Tab");
        bold.setSelected(_tabbedPane.isBoldActiveTab());
        bold.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _tabbedPane.setBoldActiveTab(bold.isSelected());
            }
        });

        final JCheckBox scroll = new JCheckBox("Scroll Selected Tab on Wheel");
        scroll.setSelected(_tabbedPane.isScrollSelectedTabOnWheel());
        scroll.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _tabbedPane.setScrollSelectedTabOnWheel(scroll.isSelected());
            }
        });

        final JCheckBox leading = new JCheckBox("Set a Tab Leading Component");
        leading.setSelected(false);
        leading.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (leading.isSelected()) {
                    Component leadingComponent = new LabelUIResource(JideIconsFactory.getImageIcon(JideIconsFactory.JIDELOGO_SMALL2));
                    _tabbedPane.setTabLeadingComponent(leadingComponent);
                }
                else {
                    _tabbedPane.setTabLeadingComponent(null);
                }
            }
        });

        final JCheckBox trailing = new JCheckBox("Set a Tab Trailing Component");
        trailing.setSelected(false);
        trailing.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (trailing.isSelected()) {
                    Component trailingComponent = new LabelUIResource(JideIconsFactory.getImageIcon(JideIconsFactory.JIDELOGO_SMALL));
                    _tabbedPane.setTabTrailingComponent(trailingComponent);
                }
                else {
                    _tabbedPane.setTabTrailingComponent(null);
                }
            }
        });

        JLabel shape = new JLabel("Tab Shape:");
        shape.setDisplayedMnemonic('S');

        // the comboBox of the shapes
        String[] tabShapes = {
                "Office2003",
                "Windows",
                "Vsnet",
                "Vsnet (Rounded Corner)",
                "Eclipse",
                "Eclipse3x",
                "Excel",
                "Box",
                "Flat",
                "Flat (Rounded Corner)",
                "Windows (Selected Only)"
        };

        final JComboBox shapeComboBox = new JComboBox(tabShapes);
        shapeComboBox.setSelectedIndex(0);

        shape.setLabelFor(shapeComboBox);

        JLabel color = new JLabel("Color Theme:");
        color.setDisplayedMnemonic('C');

        //the comboBox of the color choose
        String[] colorPhases = {WIN2K_COLOR_THEME, OFFICE2003_COLOR_THEME, WINXP_COLOR_THEME};

        final JComboBox colorComboBox = new JComboBox(colorPhases);
        colorComboBox.setSelectedIndex(1);

        color.setLabelFor(colorComboBox);

        shapeComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                Runnable runnable = new Runnable() {
                    public void run() {
                        int defaultStyle = LookAndFeelFactory.OFFICE2003_STYLE;
                        if (shapeComboBox.getSelectedIndex() == 0) {
                            LookAndFeelFactory.installJideExtension(defaultStyle);
                            _tabbedPane.setColorTheme(JideTabbedPane.COLOR_THEME_OFFICE2003);
                            _tabbedPane.setTabShape(JideTabbedPane.SHAPE_OFFICE2003);
                            colorComboBox.removeAllItems();
                            colorComboBox.addItem(WIN2K_COLOR_THEME);
                            colorComboBox.addItem(OFFICE2003_COLOR_THEME);
                            colorComboBox.addItem(WINXP_COLOR_THEME);
                            colorComboBox.setSelectedIndex(1);
                            _panel.setBackground(UIDefaultsLookup.getColor("control"));
                        }
                        else if (shapeComboBox.getSelectedIndex() == 1) {
                            LookAndFeelFactory.installJideExtension(defaultStyle);
                            _tabbedPane.setColorTheme(JideTabbedPane.COLOR_THEME_WIN2K);
                            _tabbedPane.setTabShape(JideTabbedPane.SHAPE_WINDOWS);
                            colorComboBox.removeAllItems();
                            colorComboBox.addItem(WIN2K_COLOR_THEME);
                            colorComboBox.addItem(OFFICE2003_COLOR_THEME);
                            colorComboBox.addItem(VSNET_COLOR_THEME);
                            colorComboBox.addItem(WINXP_COLOR_THEME);
                            colorComboBox.setSelectedIndex(3);
                            _panel.setBackground(UIDefaultsLookup.getColor("control"));
                        }
                        else if (shapeComboBox.getSelectedIndex() == 2) {
                            LookAndFeelFactory.installJideExtension(defaultStyle);
                            _tabbedPane.setColorTheme(JideTabbedPane.COLOR_THEME_VSNET);
                            _tabbedPane.setTabShape(JideTabbedPane.SHAPE_VSNET);
                            colorComboBox.removeAllItems();
                            colorComboBox.addItem(WIN2K_COLOR_THEME);
                            colorComboBox.addItem(OFFICE2003_COLOR_THEME);
                            colorComboBox.addItem(VSNET_COLOR_THEME);
                            colorComboBox.setSelectedIndex(2);
                            _panel.setBackground(UIDefaultsLookup.getColor("control"));
                        }
                        else if (shapeComboBox.getSelectedIndex() == 3) {
                            LookAndFeelFactory.installJideExtension(defaultStyle);
                            _tabbedPane.setColorTheme(JideTabbedPane.COLOR_THEME_VSNET);
                            _tabbedPane.setTabShape(JideTabbedPane.SHAPE_ROUNDED_VSNET);
                            colorComboBox.removeAllItems();
                            colorComboBox.addItem(WIN2K_COLOR_THEME);
                            colorComboBox.addItem(OFFICE2003_COLOR_THEME);
                            colorComboBox.addItem(VSNET_COLOR_THEME);
                            colorComboBox.setSelectedIndex(2);
                            _panel.setBackground(UIDefaultsLookup.getColor("control"));
                        }
                        else if (shapeComboBox.getSelectedIndex() == 4) {
                            LookAndFeelFactory.installJideExtension(LookAndFeelFactory.ECLIPSE_STYLE);
                            _tabbedPane.setColorTheme(JideTabbedPane.COLOR_THEME_WIN2K);
                            _tabbedPane.setTabShape(JideTabbedPane.SHAPE_ECLIPSE);
                            colorComboBox.removeAllItems();
                            colorComboBox.addItem(WIN2K_COLOR_THEME);
                            colorComboBox.setSelectedIndex(0);
                            _panel.setBackground(UIDefaultsLookup.getColor("control"));
                        }
                        else if (shapeComboBox.getSelectedIndex() == 5) {
                            LookAndFeelFactory.installJideExtension(LookAndFeelFactory.ECLIPSE3X_STYLE);
                            _tabbedPane.setTabShape(JideTabbedPane.SHAPE_ECLIPSE3X);
                            _tabbedPane.setColorTheme(JideTabbedPane.COLOR_THEME_WIN2K);
                            colorComboBox.removeAllItems();
                            colorComboBox.addItem(WIN2K_COLOR_THEME);
                            colorComboBox.setSelectedIndex(0);
                            _panel.setBackground(UIDefaultsLookup.getColor("control"));
                        }
                        else if (shapeComboBox.getSelectedIndex() == 6) {
                            LookAndFeelFactory.installJideExtension(defaultStyle);
                            _tabbedPane.setColorTheme(JideTabbedPane.COLOR_THEME_VSNET);
                            _tabbedPane.setTabShape(JideTabbedPane.SHAPE_EXCEL);
                            colorComboBox.removeAllItems();
                            colorComboBox.addItem(WIN2K_COLOR_THEME);
                            colorComboBox.addItem(OFFICE2003_COLOR_THEME);
                            colorComboBox.addItem(VSNET_COLOR_THEME);
                            colorComboBox.addItem(WINXP_COLOR_THEME);
                            colorComboBox.setSelectedIndex(2);
                            _panel.setBackground(UIDefaultsLookup.getColor("control"));
                        }
                        else if (shapeComboBox.getSelectedIndex() == 7) {
                            LookAndFeelFactory.installJideExtension(defaultStyle);
                            _tabbedPane.setColorTheme(JideTabbedPane.COLOR_THEME_VSNET);
                            _tabbedPane.setTabShape(JideTabbedPane.SHAPE_BOX);
                            colorComboBox.removeAllItems();
                            colorComboBox.addItem(WIN2K_COLOR_THEME);
                            colorComboBox.addItem(OFFICE2003_COLOR_THEME);
                            colorComboBox.addItem(VSNET_COLOR_THEME);
                            colorComboBox.addItem(WINXP_COLOR_THEME);
                            colorComboBox.setSelectedIndex(2);
                            _panel.setBackground(UIDefaultsLookup.getColor("control"));
                        }
                        else if (shapeComboBox.getSelectedIndex() == 8) {
                            LookAndFeelFactory.installJideExtension(defaultStyle);
                            _tabbedPane.setColorTheme(JideTabbedPane.COLOR_THEME_VSNET);
                            _tabbedPane.setTabShape(JideTabbedPane.SHAPE_FLAT);
                            colorComboBox.removeAllItems();
                            colorComboBox.addItem(WIN2K_COLOR_THEME);
                            colorComboBox.addItem(OFFICE2003_COLOR_THEME);
                            colorComboBox.addItem(VSNET_COLOR_THEME);
                            colorComboBox.setSelectedIndex(2);
                            _panel.setBackground(UIDefaultsLookup.getColor("control"));
                        }
                        else if (shapeComboBox.getSelectedIndex() == 9) {
                            LookAndFeelFactory.installJideExtension(defaultStyle);
                            _tabbedPane.setColorTheme(JideTabbedPane.COLOR_THEME_VSNET);
                            _tabbedPane.setTabShape(JideTabbedPane.SHAPE_ROUNDED_FLAT);
                            colorComboBox.removeAllItems();
                            colorComboBox.addItem(WIN2K_COLOR_THEME);
                            colorComboBox.addItem(OFFICE2003_COLOR_THEME);
                            colorComboBox.addItem(VSNET_COLOR_THEME);
                            colorComboBox.setSelectedIndex(2);
                            _panel.setBackground(UIDefaultsLookup.getColor("control"));
                        }
                        else if (shapeComboBox.getSelectedIndex() == 10) {
                            LookAndFeelFactory.installJideExtension(defaultStyle);
                            _tabbedPane.setColorTheme(JideTabbedPane.COLOR_THEME_WIN2K);
                            _tabbedPane.setTabShape(JideTabbedPane.SHAPE_WINDOWS_SELECTED);
                            colorComboBox.removeAllItems();
                            colorComboBox.addItem(WIN2K_COLOR_THEME);
                            colorComboBox.addItem(OFFICE2003_COLOR_THEME);
                            colorComboBox.addItem(WINXP_COLOR_THEME);
                            colorComboBox.setSelectedIndex(2);
                            _panel.setBackground(UIDefaultsLookup.getColor("control"));
                        }

                        SwingUtilities.updateComponentTreeUI(_tabbedPane.getTopLevelAncestor());
                    }
                };
                SwingUtilities.invokeLater(runnable);
            }
        });

        final JCheckBox oneNoteColorCheckBox = new JCheckBox("OneNote Color");
        oneNoteColorCheckBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    _tabbedPane.setTabColorProvider(JideTabbedPane.ONENOTE_COLOR_PROVIDER);
                }
                else {
                    _tabbedPane.setTabColorProvider(null);
                }
            }
        });

        colorComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (WIN2K_COLOR_THEME.equals(colorComboBox.getSelectedItem())) {//color set is default
                    _tabbedPane.setColorTheme(JideTabbedPane.COLOR_THEME_WIN2K);
                }
                else if (OFFICE2003_COLOR_THEME.equals(colorComboBox.getSelectedItem())) {//color set is office2003
                    _tabbedPane.setColorTheme(JideTabbedPane.COLOR_THEME_OFFICE2003);
                }
                else if (VSNET_COLOR_THEME.equals(colorComboBox.getSelectedItem())) {//color set is visual studio
                    _tabbedPane.setColorTheme(JideTabbedPane.COLOR_THEME_VSNET);
                }
                else if (WINXP_COLOR_THEME.equals(colorComboBox.getSelectedItem())) {//color set is windows xp
                    _tabbedPane.setColorTheme(JideTabbedPane.COLOR_THEME_WINXP);
                }

//                oneNoteColorCheckBox.setEnabled(OFFICE2003_COLOR_THEME.equals(colorComboBox.getSelectedItem()));
            }
        });

        JLabel lay = new JLabel("Layout:");
        lay.setDisplayedMnemonic('L');

        //the comboBox of the layout
        String[] layPhases = {
                "None",
                "Fit",
                "Fixed",
                "Compressed"};

        final JComboBox layoutComboBox = new JComboBox(layPhases);
        layoutComboBox.setSelectedIndex(0);

        lay.setLabelFor(layoutComboBox);

        layoutComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (layoutComboBox.getSelectedIndex() == 0) {//LayOut Style is Auto Size
                    _tabbedPane.setTabResizeMode(JideTabbedPane.RESIZE_MODE_NONE);
                }
                else if (layoutComboBox.getSelectedIndex() == 1) {//LayOut Style is Size to Fix
                    _tabbedPane.setTabResizeMode(JideTabbedPane.RESIZE_MODE_FIT);
                }
                else if (layoutComboBox.getSelectedIndex() == 2) {//LayOut Style is Fix
                    _tabbedPane.setTabResizeMode(JideTabbedPane.RESIZE_MODE_FIXED);
                }
                else if (layoutComboBox.getSelectedIndex() == 3) {//LayOut Style is Compressed
                    _tabbedPane.setTabResizeMode(JideTabbedPane.RESIZE_MODE_COMPRESSED);
                }
            }
        });

        JLabel position = new JLabel("Tab Placement:");
        position.setDisplayedMnemonic('P');

        //the tabPlacement of the tabbedPane
        String[] positionPhases = {
                "Top",
                "Bottom",
                "Left",
                "Right"};

        final JComboBox positionComboBox = new JComboBox(positionPhases);
        positionComboBox.setSelectedIndex(0);

        position.setLabelFor(positionComboBox);

        positionComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (positionComboBox.getSelectedIndex() == 0) {
                    _tabbedPane.setTabPlacement(JideTabbedPane.TOP);
                }
                else if (positionComboBox.getSelectedIndex() == 1) {
                    _tabbedPane.setTabPlacement(JideTabbedPane.BOTTOM);
                }
                else if (positionComboBox.getSelectedIndex() == 2) {
                    _tabbedPane.setTabPlacement(JideTabbedPane.LEFT);
                }
                else if (positionComboBox.getSelectedIndex() == 3) {
                    _tabbedPane.setTabPlacement(JideTabbedPane.RIGHT);
                }
            }
        });

        panel.add(shape);
        panel.add(shapeComboBox);
        panel.add(Box.createVerticalStrut(4), JideBoxLayout.FIX);

        panel.add(color);
        panel.add(colorComboBox);
        panel.add(oneNoteColorCheckBox);
        panel.add(Box.createVerticalStrut(4), JideBoxLayout.FIX);

        panel.add(lay);
        panel.add(layoutComboBox);
        panel.add(Box.createVerticalStrut(4), JideBoxLayout.FIX);

        panel.add(position);
        panel.add(positionComboBox);
        panel.add(Box.createVerticalStrut(8), JideBoxLayout.FIX);

        panel.add(new JLabel("More Options:"));
        panel.add(Box.createVerticalStrut(4), JideBoxLayout.FIX);
        panel.add(show);
        panel.add(hide);
        panel.add(showTab);
        panel.add(showTabContent);
        panel.add(useDefaultIcon);
        panel.add(icon);
        panel.add(bold);
        panel.add(scroll);
        panel.add(showCloseButton);
        panel.add(useDefaultClose);
        panel.add(close);
        panel.add(closeOnSelectedTab);
        panel.add(toggleFirstTabClosable);
        panel.add(toggleMiddleMouseButtonCloseTab);
        panel.add(editing);
        panel.add(allowDuplicateTabNames);
        panel.add(leading);
        panel.add(trailing);

        return panel;
    }


    private static JideTabbedPane createTabbedPane() {
        final JideTabbedPane tabbedPane = new JideTabbedPane(JideTabbedPane.TOP);
        tabbedPane.setOpaque(true);

        final String[] titles = new String[]{
                "Mail",
                "Calendar",
                "Contacts",
                "Tasks",
                "Notes",
                "Folder List",
                "Shortcuts",
                "Journal"
        };

        final int[] mnemonics = new int[]{
                KeyEvent.VK_M,
                KeyEvent.VK_C,
                KeyEvent.VK_O,
                KeyEvent.VK_T,
                KeyEvent.VK_N,
                KeyEvent.VK_F,
                KeyEvent.VK_S,
                KeyEvent.VK_J
        };

        final ImageIcon[] icons = new ImageIcon[]{
                IconsFactory.getImageIcon(JideTabbedPaneDemo.class, "icons/email.gif"),
                IconsFactory.getImageIcon(JideTabbedPaneDemo.class, "icons/calendar.gif"),
                IconsFactory.getImageIcon(JideTabbedPaneDemo.class, "icons/contacts.gif"),
                IconsFactory.getImageIcon(JideTabbedPaneDemo.class, "icons/tasks.gif"),
                IconsFactory.getImageIcon(JideTabbedPaneDemo.class, "icons/notes.gif"),
                IconsFactory.getImageIcon(JideTabbedPaneDemo.class, "icons/folder.gif"),
                IconsFactory.getImageIcon(JideTabbedPaneDemo.class, "icons/shortcut.gif"),
                IconsFactory.getImageIcon(JideTabbedPaneDemo.class, "icons/journal.gif")
        };

        for (int i = 0; i < titles.length; i++) {
            JScrollPane scrollPane = new JScrollPane(new JTextArea());
            scrollPane.setPreferredSize(new Dimension(530, 530));
            tabbedPane.addTab(titles[i], icons[i], scrollPane);
            tabbedPane.setMnemonicAt(i, mnemonics[i]);
        }

        tabbedPane.setEnabledAt(2, false);

        tabbedPane.setTabEditingValidator(new TabEditingValidator() {
            public boolean alertIfInvalid(int tabIndex, String tabText) {
                if (tabText.trim().length() == 0) {
                    JideOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(tabbedPane), "'" + tabText + "' is an invalid name for a tab title.", "Invalid Tab Title", JideOptionPane.ERROR_MESSAGE, null);
                    return false;
                }

                if (_allowDuplicateTabNames)
                    return true;

                for (int i = 0; i < tabbedPane.getTabCount(); i++) {
                    if (tabText.trim().equalsIgnoreCase(tabbedPane.getDisplayTitleAt(i)) && i != tabbedPane.getSelectedIndex()) {
                        JideOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(tabbedPane), "There is already a tab with the title of '" + tabText + "'.", "Invalid Tab Title", JideOptionPane.ERROR_MESSAGE, null);
                        return false;
                    }
                }
                return true;
            }

            public boolean isValid(int tabIndex, String tabText) {
                if (tabText.trim().length() == 0)
                    return false;

                if (_allowDuplicateTabNames)
                    return true;
                for (int i = 0; i < tabbedPane.getTabCount(); i++) {
                    if (tabText.trim().equalsIgnoreCase(tabbedPane.getDisplayTitleAt(i)) && i != tabbedPane.getSelectedIndex()) {
                        return false;
                    }
                }
                return true;
            }

            public boolean shouldStartEdit(int tabIndex, MouseEvent event) {
                return true;
            }
        });
        return tabbedPane;
    }


    class LabelUIResource extends JLabel implements UIResource {
        public LabelUIResource(String text) {
            super(text);
        }

        public LabelUIResource(Icon image) {
            super(image);
        }

    }
}
