/*
 * @(#)DirectionChooserDemo.java 3/26/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.combobox.AbstractComboBox;
import com.jidesoft.converter.ObjectConverterManager;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.JideBoxLayout;
import com.jidesoft.swing.JideSwingUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Demoed Component: {@link com.jidesoft.combobox.AbstractComboBox}, {@link
 * com.jidesoft.combobox.PopupPanel} <br> Required jar files: jide-common.jar, jide-grids.jar <br>
 * Required L&F: Jide L&F extension required
 */
public class DirectionChooserDemo extends AbstractDemo {
    private JLabel _message1;
    private JLabel _message2;

    public DirectionChooserDemo() {
    }

    public String getName() {
        return "Customized ComboBox and SplitButton Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_GRIDS;
    }

    @Override
    public String getDescription() {
        return "This is a demo of how to customize ComboBox, SplitButton and PopupPanel to choose a direction.\n" +
                "It demos how to define DirectionConverter, to create a DirectionChooserPanel, to DirectionComboBox.\n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.combobox.ObjectConverter\n" +
                "com.jidesoft.combobox.AbstractComboBox\n" +
                "com.jidesoft.swing.JideSplitButton\n" +
                "com.jidesoft.combobox.PopupPanel";
    }

    public Component getDemoPanel() {
        // register DirectionConverter
        DirectionConverter converter = new DirectionConverter();
        ObjectConverterManager.registerConverter(Integer.class, converter, DirectionConverter.CONTEXT);
        ObjectConverterManager.registerConverter(int.class, converter, DirectionConverter.CONTEXT);

        JPanel panel = new JPanel();
        panel.setLayout(new JideBoxLayout(panel, BoxLayout.Y_AXIS));

        _message1 = new JLabel("ComboBox: ");
        panel.add(_message1);
        panel.add(Box.createVerticalStrut(6), JideBoxLayout.FIX);

        AbstractComboBox _comboBox = createDirectionComboBox();
        panel.add(_comboBox);

        panel.add(Box.createVerticalStrut(12), JideBoxLayout.FIX);

        _message2 = new JLabel("SplitButton: ");
        panel.add(_message2);
        panel.add(Box.createVerticalStrut(6), JideBoxLayout.FIX);
        DirectionSplitButton _splitButton = createDirectionSplitButton();
        panel.add(JideSwingUtilities.createLeftPanel(_splitButton));

        panel.add(Box.createGlue(), JideBoxLayout.VARY);
        return panel;
    }

    @Override
    public String getDemoFolder() {
        return "G13.DirectionChooserDemo";
    }

    @Override
    public String[] getDemoSource() {
        return new String[]{"DirectionPropertyPaneDemo.java", "DirectionCellRenderer.java", "DirectionCellEditor.java"};
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new DirectionChooserDemo());
    }

    private DirectionComboBox createDirectionComboBox() {
        DirectionComboBox comboBox = new DirectionComboBox();
        comboBox.setSelectedDirection(SwingConstants.SOUTH);
        comboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if (e.getItem() instanceof Integer) {
                        _message1.setText("<HTML>ComboBox: <BR><FONT COLOR=RED>ItemListener fired => \"" + ObjectConverterManager.toString(e.getItem(), Integer.class, DirectionConverter.CONTEXT) + "\" is selected</FONT></HTML>");
                    }
                }
            }
        });
        return comboBox;
    }

    private DirectionSplitButton createDirectionSplitButton() {
        final DirectionSplitButton splitButton = new DirectionSplitButton();
        splitButton.setSelectedDirection(SwingConstants.SOUTH);
        splitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                _message2.setText("<HTML>SplitButton: <BR><FONT COLOR=RED>ActionListener fired => \"" + ObjectConverterManager.toString(splitButton.getSelectedDirection(), Integer.class, DirectionConverter.CONTEXT) + "\" is selected</FONT></HTML>");
            }
        });
        return splitButton;
    }
}
