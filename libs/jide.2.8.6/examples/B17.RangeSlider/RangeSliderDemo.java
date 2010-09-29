/*
 * @(#)RangeSliderDemo.java 11/24/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.JideButton;
import com.jidesoft.swing.JideSwingUtilities;
import com.jidesoft.swing.RangeSlider;
import com.jidesoft.swing.SelectAllUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Demoed Component: {@link com.jidesoft.swing.JideButton} <br> Required jar files: jide-common.jar,
 * jide-components.jar <br> Required L&F: Jide L&F extension required
 */
public class RangeSliderDemo extends AbstractDemo {
    protected RangeSlider _rangeSlider;

    public RangeSliderDemo() {
    }

    public String getName() {
        return "RangeSlider Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_COMMON;
    }

    @Override
    public String getDescription() {
        return "RangeSlider is a slider to select a range.\n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.swing.RangeSlider\n";
    }

    public Component getDemoPanel() {
        return JideSwingUtilities.createTopPanel(createRangePanel());
    }

    JPanel createRangePanel() {
        final JTextField minField = new JTextField();
        final JTextField maxField = new JTextField();
        SelectAllUtils.install(minField);
        SelectAllUtils.install(maxField);

        _rangeSlider = new RangeSlider(0, 100, 10, 90);
        _rangeSlider.setPaintTicks(true);
        _rangeSlider.setMajorTickSpacing(10);
        _rangeSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                minField.setText("" + _rangeSlider.getLowValue());
                maxField.setText("" + _rangeSlider.getHighValue());
            }
        });

        minField.setText("" + _rangeSlider.getLowValue());
        maxField.setText("" + _rangeSlider.getHighValue());

        JPanel minPanel = new JPanel(new BorderLayout());
        minPanel.add(new JLabel("Min"), BorderLayout.BEFORE_FIRST_LINE);
        minField.setEditable(false);
        minPanel.add(minField);

        JPanel maxPanel = new JPanel(new BorderLayout());
        maxPanel.add(new JLabel("Max", SwingConstants.TRAILING), BorderLayout.BEFORE_FIRST_LINE);
        maxField.setEditable(false);
        maxPanel.add(maxField);

        JPanel textFieldPanel = new JPanel(new GridLayout(1, 3));
        textFieldPanel.add(minPanel);
        textFieldPanel.add(new JPanel());
        textFieldPanel.add(maxPanel);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(_rangeSlider, BorderLayout.CENTER);
        panel.add(textFieldPanel, BorderLayout.AFTER_LAST_LINE);

        return panel;
    }

    @Override
    public String getDemoFolder() {
        return "B17.RangeSlider";
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new RangeSliderDemo());
    }

    @Override
    public Component getOptionsPanel() {
        final JCheckBox option1 = new JCheckBox("Show Ticks");
        final JCheckBox option2 = new JCheckBox("Show Labels");
        final JCheckBox option3 = new JCheckBox("Show Track");
        final JCheckBox option4 = new JCheckBox("Horizontal/Vertical");

        JPanel switchPanel = new JPanel(new GridLayout(4, 1, 3, 3));
        switchPanel.add(option1);
        switchPanel.add(option2);
        switchPanel.add(option3);
        switchPanel.add(option4);

        option1.setSelected(_rangeSlider.getPaintTicks());
        option1.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _rangeSlider.setPaintTicks(option1.isSelected());
            }
        });
        option2.setSelected(_rangeSlider.getPaintLabels());
        option2.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _rangeSlider.setPaintLabels(option2.isSelected());
            }
        });
        option3.setSelected(_rangeSlider.getPaintTrack());
        option3.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _rangeSlider.setPaintTrack(option3.isSelected());
            }
        });
        option4.setSelected(_rangeSlider.getOrientation() == SwingConstants.HORIZONTAL);
        option4.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _rangeSlider.setOrientation(option4.isSelected() ? SwingConstants.HORIZONTAL : SwingConstants.VERTICAL);
            }
        });

        return switchPanel;
    }

    static JideButton createJideButton(String name, Icon icon) {
        final JideButton button = new JideButton(name, icon);
        button.setHorizontalAlignment(SwingConstants.CENTER);
        return button;
    }
}
