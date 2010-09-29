/*
 * @(#)JideButtonDemo.java
 *
 * Copyright 2002 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.AutoRepeatButtonUtils;
import com.jidesoft.swing.JideButton;
import com.jidesoft.swing.JideSwingUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Demoed Component: {@link JideButton} <br> Required jar files: jide-common.jar, jide-components.jar <br> Required L&F:
 * Jide L&F extension required
 */
public class JideButtonDemo extends AbstractDemo {

    private AbstractButton[] _buttons;

    public JideButtonDemo() {
    }

    public String getName() {
        return "JideButton Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_COMMON;
    }

    @Override
    public String getDescription() {
        return "JideButton is a special JButton ideal for toolbar buttons. It has different styles and can adjust to different LookAndFeels.\n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.swing.JideButton\n";
    }

    public Component getDemoPanel() {
        JPanel panel = new JPanel(new GridLayout(6, 1));
        _buttons = new AbstractButton[6];
        _buttons[0] = createJideButton("Rename this file", ButtonsIconsFactory.getImageIcon(ButtonsIconsFactory.Buttons.RENAME));
        _buttons[1] = createJideButton("Move this file", ButtonsIconsFactory.getImageIcon(ButtonsIconsFactory.Buttons.MOVE));
        _buttons[2] = createJideButton("Copy this file", ButtonsIconsFactory.getImageIcon(ButtonsIconsFactory.Buttons.COPY));
        _buttons[3] = createJideButton("Publish this file", ButtonsIconsFactory.getImageIcon(ButtonsIconsFactory.Buttons.PUBLISH));
        _buttons[4] = createJideButton("Email this file", ButtonsIconsFactory.getImageIcon(ButtonsIconsFactory.Buttons.EMAIL));
        _buttons[5] = createJideButton("Delete this file", ButtonsIconsFactory.getImageIcon(ButtonsIconsFactory.Buttons.DELET));
        for (AbstractButton button : _buttons) {
            panel.add(button);
        }
        return JideSwingUtilities.createTopPanel(panel);
    }

    @Override
    public String getDemoFolder() {
        return "B4.JideButton";
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new JideButtonDemo());
    }

    @Override
    public Component getOptionsPanel() {
        final JRadioButton style1 = new JRadioButton("Toolbar Style");
        final JRadioButton style2 = new JRadioButton("Toolbox Style");
        final JRadioButton style3 = new JRadioButton("Flat Style");
        final JRadioButton style4 = new JRadioButton("Hyperlink Style");

        JCheckBox autoRepeat = new JCheckBox("Keep triggering actions while button is pressed");
        autoRepeat.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    for (AbstractButton _button : _buttons) {
                        JideButton button = (JideButton) _button;
                        AutoRepeatButtonUtils.install(button);
                    }
                }
                else {
                    for (AbstractButton _button : _buttons) {
                        JideButton button = (JideButton) _button;
                        AutoRepeatButtonUtils.uninstall(button);
                    }
                }
            }
        });
        autoRepeat.setSelected(false);

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(style1);
        buttonGroup.add(style2);
        buttonGroup.add(style3);
        buttonGroup.add(style4);
        JPanel switchPanel = new JPanel(new GridLayout(5, 1, 2, 2));
        switchPanel.add(style1);
        switchPanel.add(style2);
        switchPanel.add(style3);
        switchPanel.add(style4);
        switchPanel.add(autoRepeat);

        style1.setSelected(true);
        style1.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (style1.isSelected()) {
                    for (AbstractButton _button : _buttons) {
                        JideButton button = (JideButton) _button;
                        button.setButtonStyle(JideButton.TOOLBAR_STYLE);
                    }
                }
            }
        });
        style2.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (style2.isSelected()) {
                    for (AbstractButton _button : _buttons) {
                        JideButton button = (JideButton) _button;
                        button.setButtonStyle(JideButton.TOOLBOX_STYLE);
                    }
                }
            }
        });
        style3.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (style3.isSelected()) {
                    for (AbstractButton _button : _buttons) {
                        JideButton button = (JideButton) _button;
                        button.setButtonStyle(JideButton.FLAT_STYLE);
                    }
                }
            }
        });
        style4.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (style4.isSelected()) {
                    for (AbstractButton _button : _buttons) {
                        JideButton button = (JideButton) _button;
                        button.setButtonStyle(JideButton.HYPERLINK_STYLE);
                    }
                }
            }
        });

        return switchPanel;
    }

    static JideButton createJideButton(String name, Icon icon) {
        final JideButton button = new JideButton(new AbstractAction(name, icon) {
            public void actionPerformed(ActionEvent e) {
                System.out.println("print");
            }
        });
        button.setHorizontalAlignment(SwingConstants.CENTER);
        return button;
    }
}
