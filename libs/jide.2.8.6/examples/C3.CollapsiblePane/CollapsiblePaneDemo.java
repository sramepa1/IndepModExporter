/*
 * @(#)CollapsiblePaneDemo.java
 *
 * Copyright 2002 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.pane.CollapsiblePane;
import com.jidesoft.pane.CollapsiblePanes;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.plaf.UIDefaultsLookup;
import com.jidesoft.swing.JideBoxLayout;
import com.jidesoft.swing.JideButton;
import com.jidesoft.swing.JideSwingUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Demoed Component: {@link CollapsiblePane} <br> Required jar files: jide-common.jar, jide-components.jar <br> Required
 * L&F: Jide L&F extension required
 */
public class CollapsiblePaneDemo extends AbstractDemo {

    private CollapsiblePane _fileFolderTaskPane;
    private CollapsiblePane _otherPlacesPane;
    private CollapsiblePanes _container;

    public CollapsiblePaneDemo() {
    }

    public String getName() {
        return "CollapsiblePane Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_COMPONENTS;
    }

    public Component getDemoPanel() {
        _container = new CollapsiblePanes();
        _fileFolderTaskPane = createFileFolderTaskPane();
        _otherPlacesPane = createOtherPlacesPane();
        _container.add(_fileFolderTaskPane);
        _container.add(_otherPlacesPane);
        _container.addExpansion();
        return new JScrollPane(_container);
    }

    @Override
    public Component getOptionsPanel() {
        JPanel switchPanel = new JPanel(new GridLayout(0, 1, 3, 3));

        final JRadioButton[] buttons = new JRadioButton[4];
        buttons[0] = new JRadioButton("Dropdown Style");
        buttons[1] = new JRadioButton("Tree Style");
        buttons[2] = new JRadioButton("Plain Style");
        buttons[3] = new JRadioButton("Separator Style");
        ButtonGroup buttonGroup = new ButtonGroup();
        switchPanel.add(new JLabel("CollapsiblePane Styles:"));
        for (JRadioButton button : buttons) {
            buttonGroup.add(button);
            switchPanel.add(button);
        }
        buttons[0].setSelected(true);

        final JRadioButton[] directions = new JRadioButton[4];
        directions[0] = new JRadioButton("Sliding South");
        directions[1] = new JRadioButton("Sliding West");
        directions[2] = new JRadioButton("Sliding North");
        directions[3] = new JRadioButton("Sliding East");
        ButtonGroup directionsGroup = new ButtonGroup();
        switchPanel.add(new JLabel("Sliding Directions:"));
        for (JRadioButton direction : directions) {
            directionsGroup.add(direction);
            switchPanel.add(direction);
        }
        directions[0].setSelected(true);

        final JCheckBox showTitleBar = new JCheckBox("Show Title Bar");
        showTitleBar.setSelected(true);
        showTitleBar.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _fileFolderTaskPane.setShowTitleBar(showTitleBar.isSelected());
                _otherPlacesPane.setShowTitleBar(showTitleBar.isSelected());
            }
        });
        switchPanel.add(showTitleBar);

        final JCheckBox showExpandButton = new JCheckBox("Show Expand/Collapse Button");
        showExpandButton.setSelected(true);
        showExpandButton.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _fileFolderTaskPane.setShowExpandButton(showExpandButton.isSelected());
                _otherPlacesPane.setShowExpandButton(showExpandButton.isSelected());
            }
        });
        switchPanel.add(showExpandButton);

        final JCheckBox showTitleIconButton = new JCheckBox("Show Title Icon");
        showTitleIconButton.setSelected(true);
        showTitleIconButton.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _fileFolderTaskPane.setTitleIcon(showTitleIconButton.isSelected() ? IconsFactoryDemo.getImageIcon(IconsFactoryDemo.CollapsiblePane.CONTROL_PANEL) : null);
                _otherPlacesPane.setTitleIcon(showTitleIconButton.isSelected() ? IconsFactoryDemo.getImageIcon(IconsFactoryDemo.CollapsiblePane.OTHER) : null);
            }
        });
        switchPanel.add(showTitleIconButton);

        final JCheckBox showTitleComponentButton = new JCheckBox("Show Extra Compoent on TitleBar");
        showTitleComponentButton.setSelected(false);
        showTitleComponentButton.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    JideButton button = new JideButton("Help");
                    button.setPreferredSize(new Dimension(40, 14));
                    button.setButtonStyle(JideButton.HYPERLINK_STYLE);
                    button.setForeground(Color.WHITE);
                    _fileFolderTaskPane.setTitleComponent(button);

                    JCheckBox checkBox = new JCheckBox("Check All");
                    checkBox.setPreferredSize(new Dimension(70, 14));
                    checkBox.setOpaque(false);
                    _otherPlacesPane.setTitleComponent(checkBox);
                }
                else {
                    _fileFolderTaskPane.setTitleComponent(null);
                    _otherPlacesPane.setTitleComponent(null);
                }
            }
        });
        switchPanel.add(showTitleComponentButton);

        final JCheckBox fillContextAreaButton = new JCheckBox("Fill Content Area");
        fillContextAreaButton.setSelected(true);
        fillContextAreaButton.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _fileFolderTaskPane.setContentAreaFilled(fillContextAreaButton.isSelected());
                _otherPlacesPane.setContentAreaFilled(fillContextAreaButton.isSelected());
            }
        });
        switchPanel.add(fillContextAreaButton);

        final JCheckBox collapsibleButton = new JCheckBox("Collapsible");
        collapsibleButton.setSelected(true);
        collapsibleButton.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _fileFolderTaskPane.setCollapsible(collapsibleButton.isSelected());
                _otherPlacesPane.setCollapsible(collapsibleButton.isSelected());
            }
        });
        switchPanel.add(collapsibleButton);

        final JCheckBox emphasizeButton = new JCheckBox("Emphasize \"File and Folder Tasks\" Pane");
        emphasizeButton.setSelected(true);
        emphasizeButton.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _fileFolderTaskPane.setEmphasized(emphasizeButton.isSelected());
            }
        });
        switchPanel.add(emphasizeButton);

        final JButton expand1Button = new JButton("Toggle \"File and Folder Tasks\" Pane");
        expand1Button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                _fileFolderTaskPane.collapse(!_fileFolderTaskPane.isCollapsed());
            }
        });
        switchPanel.add(expand1Button);

        final JButton expand2Button = new JButton("Toggle \"Other Places\" Pane");
        expand2Button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                _otherPlacesPane.collapse(!_otherPlacesPane.isCollapsed());
            }
        });
        switchPanel.add(expand2Button);

        buttons[0].addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (buttons[0].isSelected()) {
                    _fileFolderTaskPane.setStyle(CollapsiblePane.DROPDOWN_STYLE);
                    _otherPlacesPane.setStyle(CollapsiblePane.DROPDOWN_STYLE);
                    showTitleIconButton.setEnabled(true);
                    _container.setBackground(UIManager.getColor("Panel.background"));
                    _container.setGap(UIDefaultsLookup.getInt("CollapsiblePanes.gap"));
                    _container.setBorder(UIDefaultsLookup.getBorder("CollapsiblePanes.border"));
                }
            }
        });
        buttons[1].addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (buttons[1].isSelected()) {
                    _fileFolderTaskPane.setStyle(CollapsiblePane.TREE_STYLE);
                    _otherPlacesPane.setStyle(CollapsiblePane.TREE_STYLE);
                    showTitleIconButton.setEnabled(false);
                    _container.setBackground(UIManager.getColor("Panel.background"));
                    _container.setGap(UIDefaultsLookup.getInt("CollapsiblePanes.gap"));
                    _container.setBorder(UIDefaultsLookup.getBorder("CollapsiblePanes.border"));
                }
            }
        });
        buttons[2].addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (buttons[2].isSelected()) {
                    _fileFolderTaskPane.setStyle(CollapsiblePane.PLAIN_STYLE);
                    _otherPlacesPane.setStyle(CollapsiblePane.PLAIN_STYLE);
                    showTitleIconButton.setEnabled(false);
                    _container.setBackground(UIManager.getColor("Panel.background"));
                    _container.setGap(UIDefaultsLookup.getInt("CollapsiblePanes.gap"));
                    _container.setBorder(UIDefaultsLookup.getBorder("CollapsiblePanes.border"));
                }
            }
        });
        buttons[3].addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (buttons[3].isSelected()) {
                    _fileFolderTaskPane.setStyle(CollapsiblePane.SEPARATOR_STYLE);
                    _otherPlacesPane.setStyle(CollapsiblePane.SEPARATOR_STYLE);
                    showTitleIconButton.setEnabled(false);
                    _container.setBackground(Color.WHITE);
                    _container.setGap(0);
                    _container.setBorder(BorderFactory.createEmptyBorder());
                }
            }
        });

        directions[0].addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (directions[0].isSelected()) {
                    _fileFolderTaskPane.setSlidingDirection(SwingConstants.SOUTH);
                    _otherPlacesPane.setSlidingDirection(SwingConstants.SOUTH);
                    _container.setAxis(JideBoxLayout.Y_AXIS);
                }
            }
        });
        directions[1].addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (directions[1].isSelected()) {
                    _fileFolderTaskPane.setSlidingDirection(SwingConstants.WEST);
                    _otherPlacesPane.setSlidingDirection(SwingConstants.WEST);
                    _container.setAxis(JideBoxLayout.X_AXIS);
                }
            }
        });
        directions[2].addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (directions[2].isSelected()) {
                    _fileFolderTaskPane.setSlidingDirection(SwingConstants.NORTH);
                    _otherPlacesPane.setSlidingDirection(SwingConstants.NORTH);
                    _container.setAxis(JideBoxLayout.Y_AXIS);
                }
            }
        });
        directions[3].addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (directions[3].isSelected()) {
                    _fileFolderTaskPane.setSlidingDirection(SwingConstants.EAST);
                    _otherPlacesPane.setSlidingDirection(SwingConstants.EAST);
                    _container.setAxis(JideBoxLayout.X_AXIS);
                }
            }
        });


        switchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        return switchPanel;
    }

    @Override
    public String getDescription() {
        return "CollapsiblePane is a pane whose content pane can collapse and expand. It is actually the Java implementation of task panes as you can find in Windows XP.\n" +
                "Demoed classes:\n" +
                "com.jidesoft.pane.CollapsiblePane\n" +
                "com.jidesoft.pane.CollapsiblePanes";
    }

    @Override
    public String getDemoFolder() {
        return "C3.CollapsiblePane";
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new CollapsiblePaneDemo());
    }

    // CollapsiblePane
    private static CollapsiblePane createFileFolderTaskPane() {
        CollapsiblePane pane = new CollapsiblePane("File and Folder Tasks");
        pane.setName("File and Folder Tasks");
        pane.setTitleIcon(IconsFactoryDemo.getImageIcon(IconsFactoryDemo.CollapsiblePane.CONTROL_PANEL));
// uncomment following for a different style of collapsible pane
//        panel.setStyle(CollapsiblePane.TREE_STYLE);
        JPanel labelPanel = new JPanel();
        labelPanel.setOpaque(false);
        labelPanel.setLayout(new GridLayout(6, 1, 1, 0));
        labelPanel.add(createHyperlinkButton("Rename this file", IconsFactoryDemo.getImageIcon(IconsFactoryDemo.CollapsiblePane.RENAME)));
        labelPanel.add(createHyperlinkButton("Move this file", IconsFactoryDemo.getImageIcon(IconsFactoryDemo.CollapsiblePane.MOVE)));
        labelPanel.add(createHyperlinkButton("Copy this file", IconsFactoryDemo.getImageIcon(IconsFactoryDemo.CollapsiblePane.COPY)));
        labelPanel.add(createHyperlinkButton("Publish this file", IconsFactoryDemo.getImageIcon(IconsFactoryDemo.CollapsiblePane.PUBLISH)));
        labelPanel.add(createHyperlinkButton("Email this file", IconsFactoryDemo.getImageIcon(IconsFactoryDemo.CollapsiblePane.EMAIL)));
        labelPanel.add(createHyperlinkButton("Delete this file", IconsFactoryDemo.getImageIcon(IconsFactoryDemo.CollapsiblePane.DELET)));
        labelPanel.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        pane.setContentPane(JideSwingUtilities.createTopPanel(labelPanel));
        pane.setEmphasized(true);
        return pane;
    }

    private static CollapsiblePane createOtherPlacesPane() {
        CollapsiblePane pane = new CollapsiblePane("Other Places");
        pane.setName("Other Places");
        pane.setTitleIcon(IconsFactoryDemo.getImageIcon(IconsFactoryDemo.CollapsiblePane.OTHER));
        JPanel labelPanel = new JPanel();
        labelPanel.setOpaque(false);
        labelPanel.setLayout(new GridLayout(4, 1, 1, 0));
        labelPanel.add(createHyperlinkButton("Local Disk (C:)", IconsFactoryDemo.getImageIcon(IconsFactoryDemo.CollapsiblePane.LOCALDISK)));
        labelPanel.add(createHyperlinkButton("My Pictures", IconsFactoryDemo.getImageIcon(IconsFactoryDemo.CollapsiblePane.PICTURES)));
        labelPanel.add(createHyperlinkButton("My Computer", IconsFactoryDemo.getImageIcon(IconsFactoryDemo.CollapsiblePane.COMPUTER)));
        labelPanel.add(createHyperlinkButton("My Network Places", IconsFactoryDemo.getImageIcon(IconsFactoryDemo.CollapsiblePane.NETWORK)));
        labelPanel.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        pane.setContentPane(JideSwingUtilities.createTopPanel(labelPanel));
        return pane;
    }

    static JComponent createHyperlinkButton(String name, Icon icon) {
        final JideButton button = new JideButton(name, icon);
        button.setButtonStyle(JideButton.HYPERLINK_STYLE);

        button.setOpaque(false);
        button.setHorizontalAlignment(SwingConstants.LEADING);

        button.setRequestFocusEnabled(true);
        button.setFocusable(true);

        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }
}
