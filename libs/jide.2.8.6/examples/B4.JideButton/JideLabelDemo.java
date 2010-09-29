/*
 * @(#)JideLabelDemo.java
 *
 * Copyright 2002 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.JideLabel;
import com.jidesoft.swing.JideSwingUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Demoed Component: {@link com.jidesoft.swing.JideLabel} <br> Required jar files: jide-common.jar, jide-components.jar
 * <br> Required L&F: Jide L&F extension required
 */
public class JideLabelDemo extends AbstractDemo {

    private JideLabel[] _labels;
    protected JPanel _panel;

    public JideLabelDemo() {
    }

    public String getName() {
        return "JideLabel Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_COMMON;
    }

    @Override
    public String getDescription() {
        return "JideLabel is a special JLabel ideal for toolbar buttons. It supports vertical layout.\n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.swing.JideLabel\n";
    }

    public Component getDemoPanel() {
        _panel = new JPanel(new GridLayout(1, 3));
        _panel.setPreferredSize(new Dimension(300, 300));
        _labels = new JideLabel[3];
        _labels[0] = createJideLabel("Rename this file", ButtonsIconsFactory.getImageIcon(ButtonsIconsFactory.Buttons.RENAME));
        _labels[1] = createJideLabel("Move this file", ButtonsIconsFactory.getImageIcon(ButtonsIconsFactory.Buttons.MOVE));
        _labels[2] = createJideLabel("Copy this file", ButtonsIconsFactory.getImageIcon(ButtonsIconsFactory.Buttons.COPY));
        for (JideLabel button : _labels) {
            _panel.add(button);
        }
        return JideSwingUtilities.createCenterPanel(_panel);
    }

    @Override
    public String getDemoFolder() {
        return "B4.JideLabel";
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new JideLabelDemo());
    }

    @Override
    public Component getOptionsPanel() {
        final JRadioButton rotation1 = new JRadioButton("Normal");
        final JRadioButton rotation2 = new JRadioButton("Vertical (Clockwise)");
        final JRadioButton rotation3 = new JRadioButton("Vertical (Counter-clockwise");
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(rotation1);
        buttonGroup.add(rotation2);
        buttonGroup.add(rotation3);
        JPanel switchPanel = new JPanel(new GridLayout(0, 1, 2, 2));
        switchPanel.add(rotation1);
        switchPanel.add(rotation2);
        switchPanel.add(rotation3);
        rotation1.setSelected(true);

        rotation1.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (rotation1.isSelected()) {
                    for (JideLabel label : _labels) {
                        label.setOrientation(SwingConstants.HORIZONTAL);
                    }
                    _panel.setLayout(new GridLayout(1, 3));
                    for (JideLabel button : _labels) {
                        _panel.add(button);
                    }
                }
            }
        });
        rotation2.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (rotation2.isSelected()) {
                    for (JideLabel label : _labels) {
                        label.setOrientation(SwingConstants.VERTICAL);
                        label.setClockwise(true);
                    }
                    _panel.setLayout(new GridLayout(3, 1));
                    for (JideLabel button : _labels) {
                        _panel.add(button);
                    }
                }
            }
        });
        rotation3.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (rotation3.isSelected()) {
                    for (JideLabel label : _labels) {
                        label.setOrientation(SwingConstants.VERTICAL);
                        label.setClockwise(false);
                    }
                    _panel.setLayout(new GridLayout(3, 1));
                    for (JideLabel button : _labels) {
                        _panel.add(button);
                    }
                }
            }
        });

        return switchPanel;
    }

    static JideLabel createJideLabel(String name, Icon icon) {
        final JideLabel button = new JideLabel(name);
        button.setIcon(icon);
        button.setHorizontalAlignment(SwingConstants.CENTER);
        return button;
    }
}