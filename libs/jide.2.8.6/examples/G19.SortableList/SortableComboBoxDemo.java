/*
 * @(#)SortableComboBoxDemo.java 6/1/2007
 *
 * Copyright 2002 - 2007 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.combobox.SortableComboBoxModel;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.JideSwingUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Demoed Component: {@link com.jidesoft.combobox.SortableComboBoxModel} <br> Required jar files:
 * jide-common.jar, jide-grids.jar <br> Required L&F: any L&F
 */
public class SortableComboBoxDemo extends AbstractDemo {

    public SortableComboBoxDemo() {
    }

    public String getName() {
        return "Sortable Combobox Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_GRIDS;
    }

    @Override
    public String getDescription() {
        return "This is a demo of JCombobox with SortableComboboxModel to get sortable feature. You can click on the \"Sort\" to sort\n" +
                "or unsort the combobox. Please note, SortableComboBoxModel is a model wrapper, just like TableModelWrapper or TreeModelWrapper. \n" +
                "It uses index mapping to sort the combobox model. The actual combobox model is not touched at all. Not only that, " +
                "selected items in the combobox are kept after sorting." +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.combobox.SortableComboBoxModel";
    }

    public Component getDemoPanel() {
        final DefaultComboBoxModel comboboxModel = new DefaultComboBoxModel();
        String[] fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        for (String fontName : fontNames) {
            if (Math.random() > 0.5) {
                comboboxModel.insertElementAt(fontName, 0);
            }
            else {
                comboboxModel.addElement(fontName);
            }
        }

        final SortableComboBoxModel sortableComboBoxModel = new SortableComboBoxModel(comboboxModel);
        final JComboBox combobox = new JComboBox(sortableComboBoxModel);
        JPanel panel = new JPanel(new BorderLayout(6, 6));
        panel.add(combobox, BorderLayout.CENTER);
        JCheckBox checkBox = new JCheckBox("Sort");
        checkBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    sortableComboBoxModel.sort(SortableComboBoxModel.SORT_ASCENDING);
                }
                else {
                    sortableComboBoxModel.sort(SortableComboBoxModel.UNSORTED);
                }
            }
        });
        checkBox.setSelected(true);
        panel.add(checkBox, BorderLayout.AFTER_LINE_ENDS);

//        combobox.addItemListener(new ItemListener() {
//            public void itemStateChanged(ItemEvent e) {
//                if (e.getStateChange() == ItemEvent.SELECTED) {
//                    System.out.println(ComboBoxModelWrapperUtils.getActualIndexAt(combobox.getModel(), combobox.getSelectedIndex()));
//                }
//            }
//        });

        return JideSwingUtilities.createTopPanel(panel);
    }

    @Override
    public String getDemoFolder() {
        return "G19.SortableList";
    }

    static public void main(String[] s) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new SortableComboBoxDemo());
    }
}
