/*
 * @(#)SelectableCollapsiblePaneDemo.java 9/13/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.pane.CollapsiblePane;
import com.jidesoft.pane.CollapsiblePanes;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.JideButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Demoed Component: {@link com.jidesoft.pane.CollapsiblePane}. This is a demo to show you how to
 * write your own UI class to make CollapsiblePane selectable. See forum post at <a
 * href="http://www.jidesoft.com/forum/viewtopic.php?p=5618#5618">http://www.jidesoft.com/forum/viewtopic.php?p=5618#5618</a>
 * for related information. <br> Required jar files: jide-common.jar, jide-components.jar <br>
 * Required L&F: Jide L&F extension required
 */
public class SelectableCollapsiblePaneDemo extends AbstractDemo {

    private CollapsiblePane _fileFolderTaskPane;
    private CollapsiblePane _otherPlacesPane;

    public SelectableCollapsiblePaneDemo() {
    }

    public String getName() {
        return "CollapsiblePane Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_COMPONENTS;
    }

    public Component getDemoPanel() {
        CollapsiblePanes pane = new CollapsiblePanes();
        _fileFolderTaskPane = createFileFolderTaskPane();
        _otherPlacesPane = createOtherPlacesPane();
        pane.add(_fileFolderTaskPane);
        pane.add(_otherPlacesPane);
        CollapsiblePaneGroup group = new CollapsiblePaneGroup();
        group.add(_fileFolderTaskPane);
        group.add(_otherPlacesPane);
        pane.addExpansion();
        return new JScrollPane(pane);
    }

    @Override
    public Component getOptionsPanel() {
        final JRadioButton style1 = new JRadioButton("Tree Style");
        final JRadioButton style2 = new JRadioButton("Dropdown Style");
        final JRadioButton style3 = new JRadioButton("Plain Style");
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(style1);
        buttonGroup.add(style2);
        buttonGroup.add(style3);
        JPanel switchPanel = new JPanel(new GridLayout(0, 1, 3, 3));
        switchPanel.add(style1);
        switchPanel.add(style2);
        switchPanel.add(style3);
        style2.setSelected(true);

        style1.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (style1.isSelected()) {
                    _fileFolderTaskPane.setStyle(CollapsiblePane.TREE_STYLE);
                    _otherPlacesPane.setStyle(CollapsiblePane.TREE_STYLE);
                }
            }
        });
        style2.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (style2.isSelected()) {
                    _fileFolderTaskPane.setStyle(CollapsiblePane.DROPDOWN_STYLE);
                    _otherPlacesPane.setStyle(CollapsiblePane.DROPDOWN_STYLE);
                }
            }
        });
        style3.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (style3.isSelected()) {
                    _fileFolderTaskPane.setStyle(CollapsiblePane.PLAIN_STYLE);
                    _otherPlacesPane.setStyle(CollapsiblePane.PLAIN_STYLE);
                }
            }
        });

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
        UIManager.put("CollapsiblePaneUI", "Office2003SelectableCollapsiblePaneUI");
        showAsFrame(new SelectableCollapsiblePaneDemo());
    }

    // CollapsiblePane
    private CollapsiblePane createFileFolderTaskPane() {
        CollapsiblePane pane = new CollapsiblePane("File and Folder Tasks");
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
        pane.setContentPane(labelPanel);
//        pane.setEmphasized(true);
        return pane;
    }

    private CollapsiblePane createOtherPlacesPane() {
        CollapsiblePane pane = new CollapsiblePane("Other Places");
        JPanel labelPanel = new JPanel();
        labelPanel.setOpaque(false);
        labelPanel.setLayout(new GridLayout(4, 1, 1, 0));
        labelPanel.add(createHyperlinkButton("Local Disk (C:)", IconsFactoryDemo.getImageIcon(IconsFactoryDemo.CollapsiblePane.LOCALDISK)));
        labelPanel.add(createHyperlinkButton("My Pictures", IconsFactoryDemo.getImageIcon(IconsFactoryDemo.CollapsiblePane.PICTURES)));
        labelPanel.add(createHyperlinkButton("My Computer", IconsFactoryDemo.getImageIcon(IconsFactoryDemo.CollapsiblePane.COMPUTER)));
        labelPanel.add(createHyperlinkButton("My Network Places", IconsFactoryDemo.getImageIcon(IconsFactoryDemo.CollapsiblePane.NETWORK)));
        labelPanel.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        pane.setContentPane(labelPanel);
        return pane;
    }

    static JComponent createHyperlinkButton(String name, Icon icon) {
        final JideButton button = new JideButton(name, icon);
        button.setButtonStyle(JideButton.HYPERLINK_STYLE);

        button.setOpaque(false);
        button.setPreferredSize(new Dimension(0, 20));
        button.setHorizontalAlignment(SwingConstants.LEADING);

        button.setRequestFocusEnabled(true);
        button.setFocusable(true);

        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }
}
