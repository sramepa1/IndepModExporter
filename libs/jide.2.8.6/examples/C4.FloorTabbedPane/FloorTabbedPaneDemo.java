/*
 * @(#)FloorTabbedPaneDemo.java
 *
 * Copyright 2002 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.dialog.ButtonPanel;
import com.jidesoft.icons.JideIconsFactory;
import com.jidesoft.pane.FloorTabbedPane;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.JideButton;

import javax.swing.*;
import javax.swing.plaf.UIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Demoed Component: {@link FloorTabbedPane} <br> Required jar files: jide-common.jar, jide-components.jar <br> Required
 * L&F: Jide L&F extension required
 */
public class FloorTabbedPaneDemo extends AbstractDemo {
    private FloorTabbedPane _tabbedPane;

    public FloorTabbedPaneDemo() {
    }

    public String getName() {
        return "FloorTabbedPane Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_COMPONENTS;
    }

    @Override
    public Component getOptionsPanel() {
        ButtonPanel buttonPanel = new ButtonPanel(SwingConstants.TOP);
        JButton button = new JButton(new AbstractAction("Add a tab") {
            public void actionPerformed(ActionEvent e) {
                TabPanel tabPanel = createTabPanel(_tabbedPane.getTabCount() + 1);
                addTabPanel(tabPanel);
            }
        });
        button.setRequestFocusEnabled(false);
        buttonPanel.addButton(button);

        button = new JButton(new AbstractAction("Insert a tab at index 2 (if exists)") {
            public void actionPerformed(ActionEvent e) {
                TabPanel tabPanel = createTabPanel(_tabbedPane.getTabCount() + 1);
                addTabPanel(tabPanel, 2);
            }
        });
        button.setRequestFocusEnabled(false);
        buttonPanel.addButton(button);

        button = new JButton(new AbstractAction("Delete selected tab") {
            public void actionPerformed(ActionEvent e) {
                _tabbedPane.removeTabAt(_tabbedPane.getSelectedIndex());
            }
        });
        button.setRequestFocusEnabled(false);
        buttonPanel.addButton(button);

        button = new JButton(new AbstractAction("Delete the second tab (if exsits)") {
            public void actionPerformed(ActionEvent e) {
                _tabbedPane.removeTabAt(1);
            }
        });
        button.setRequestFocusEnabled(false);
        buttonPanel.addButton(button);

        button = new JButton(new AbstractAction("Change selected tab title") {
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = _tabbedPane.getSelectedIndex();
                String titleAt = _tabbedPane.getTitleAt(selectedIndex);
                if (Character.isUpperCase(titleAt.charAt(0))) {
                    _tabbedPane.setTitleAt(selectedIndex, titleAt.toLowerCase());
                }
                else {
                    _tabbedPane.setTitleAt(selectedIndex, titleAt.toUpperCase());
                }
            }
        });
        button.setRequestFocusEnabled(false);
        buttonPanel.addButton(button);

        button = new JButton(new AbstractAction("Change selected tab icon") {
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = _tabbedPane.getSelectedIndex();
                _tabbedPane.setIconAt(selectedIndex, JideIconsFactory.getImageIcon("jide/dockableframe_blank.gif"));
            }
        });
        button.setRequestFocusEnabled(false);
        buttonPanel.addButton(button);

        button = new JButton(new AbstractAction("Change selected tab tooltip") {
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = _tabbedPane.getSelectedIndex();
                String toolTipAt = _tabbedPane.getToolTipTextAt(selectedIndex);
                if (Character.isUpperCase(toolTipAt.charAt(0))) {
                    _tabbedPane.setToolTipTextAt(selectedIndex, toolTipAt.toLowerCase());
                }
                else {
                    _tabbedPane.setToolTipTextAt(selectedIndex, toolTipAt.toUpperCase());
                }
            }
        });
        button.setRequestFocusEnabled(false);
        buttonPanel.addButton(button);

        final JCheckBox orientationCheckBox = new JCheckBox("Toggle Sliding Orientation");
        orientationCheckBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _tabbedPane.setOrientation(orientationCheckBox.isSelected() ? SwingConstants.VERTICAL : SwingConstants.HORIZONTAL);
            }
        });
        orientationCheckBox.setSelected(_tabbedPane.getOrientation() == SwingConstants.VERTICAL);
        buttonPanel.addButton(orientationCheckBox);

        return buttonPanel;
    }

    public Component getDemoPanel() {
        return createTabbedPane();
    }

    @Override
    public String getDescription() {
        return "FloorTabbedPane is another type of tabbed pane. A typical tabbed pane has many panels and corresponding tabs. The user can click on a tab to choose which panel to view. Although a FloorTabbedPane also has many panels, instead of using tabs, it just uses buttons to switch between panels. The _panes are organized vertically, as floors in a storied building (that's how it gets the name of FloorTabbedPane). One famous example of it is the Outlook Bar in the Microsoft Outlook product. \n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.pane.FloorTabbedPane";
    }

    @Override
    public String getDemoFolder() {
        return "C4.FloorTabbedPane";
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new FloorTabbedPaneDemo());
    }

    private class TabPanel extends JPanel {
        Icon _icon;
        String _title;
        JComponent _component;

        public TabPanel(String title, Icon icon, JComponent component) {
            _title = title;
            _icon = icon;
            _component = component;
        }

        public Icon getIcon() {
            return _icon;
        }

        public void setIcon(Icon icon) {
            _icon = icon;
        }

        public String getTitle() {
            return _title;
        }

        public void setTitle(String title) {
            _title = title;
        }

        public JComponent getComponent() {
            return _component;
        }

        public void setComponent(JComponent component) {
            _component = component;
        }
    }

    // an example of creating your own button for floor tabbed pane.
    private static class FloorButton extends JideButton implements UIResource {
        public FloorButton(Action a) {
            super(a);
            setButtonStyle(TOOLBOX_STYLE);
            setOpaque(true); // you can try to set to false to see the difference.
        }
    }

    private JTabbedPane createTabbedPane() {
        _tabbedPane = new FloorTabbedPane() {
            @Override
            protected AbstractButton createButton(Action action) {
                return new FloorButton(action);
            }
        };

        for (int i = 0; i < 8; i++) {
            TabPanel tabPanel = createTabPanel(i + 1);
            addTabPanel(tabPanel);
        }
        return _tabbedPane;
    }

    private void addTabPanel(TabPanel tabPanel) {
        _tabbedPane.addTab(tabPanel.getTitle(), tabPanel.getIcon(), tabPanel.getComponent(), "tooltip for " + tabPanel.getTitle());
        int index = _tabbedPane.getTabCount();
        _tabbedPane.setMnemonicAt(index - 1, ("" + index).charAt(0));
    }

    private void addTabPanel(TabPanel tabPanel, int index) {
        _tabbedPane.insertTab(tabPanel.getTitle(), tabPanel.getIcon(), tabPanel.getComponent(), "tooltip for " + tabPanel.getTitle(), index);
        _tabbedPane.setMnemonicAt(index, ("" + index).charAt(0));
    }

    protected TabPanel createTabPanel(int index) {
        JScrollPane pane = new JScrollPane(new JTextArea());
        pane.setPreferredSize(new Dimension(200, 400));
        TabPanel panel = new TabPanel("Tab " + index,
                JideIconsFactory.getImageIcon("jide/dockableframe_" + index + ".gif"),
                pane);
        return panel;
    }
}
