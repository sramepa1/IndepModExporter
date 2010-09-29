/*
 * @(#)QuickFilterComboBoxDemo.java 9/30/2006
 *
 * Copyright 2002 - 2006 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.combobox.QuickComboBoxFilterField;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.*;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.awt.*;

/**
 * Demoed Component: {@link com.jidesoft.grid.SortableTable} <br> Required jar files:
 * jide-common.jar, jide-grids.jar <br> Required L&F: any L&F
 */
public class QuickFilterComboBoxDemo extends AbstractDemo {

    public QuickFilterComboBoxDemo() {
    }

    public String getName() {
        return "QuickFilter (ComboBox) Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_GRIDS;
    }

    @Override
    public String getDescription() {
        return "This is a demo of QuickComboBoxFilterField and FilterableComboBoxModel. \n" +
                "\nFilterableComboBoxModel is a ComboBoxModel which can support Filters. FilterableComboBoxModel can wrap any existing comboBox model and apply filters onto the comboBox model.\n" +
                "\nQuickComboBoxFilterField is an instance of QuickFilterField. It provides a quick way to allow filter away items from a ComboBox model. If you have a huge combobox, this QuickComboBoxFilterField will allow you to narrow the comboBox easily.\n" +
                "Demoed classes:\n" +
                "com.jidesoft.combobox.FilterableComboBoxModel\n" +
                "com.jidesoft.combobox.QuickComboBoxFilterField";
    }

    public Component getDemoPanel() {
        final DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel();
        String[] fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        for (String fontName : fontNames) {
            comboBoxModel.addElement(fontName);
        }

        JPanel quickSearchPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        final QuickComboBoxFilterField field = new QuickComboBoxFilterField(comboBoxModel);
        field.setHintText("Type here to filter fonts");
        quickSearchPanel.add(field);
        quickSearchPanel.setBorder(new JideTitledBorder(new PartialEtchedBorder(PartialEtchedBorder.LOWERED, PartialSide.NORTH), "QuickComboBoxFilterField - it will filter the list in the ComboBox below", JideTitledBorder.LEADING, JideTitledBorder.ABOVE_TOP));

        final JComboBox comboBox = new JComboBox(field.getDisplayComboBoxModel());
        comboBox.setEditable(false);
        field.setComboBox(comboBox);
        SearchableUtils.installSearchable(comboBox);

        JPanel comboBoxPanel = new JPanel(new BorderLayout(2, 2));
        comboBoxPanel.setBorder(BorderFactory.createCompoundBorder(new JideTitledBorder(new PartialEtchedBorder(PartialEtchedBorder.LOWERED, PartialSide.NORTH), "Filtered Font ComboBox", JideTitledBorder.LEADING, JideTitledBorder.ABOVE_TOP),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)));
        final JLabel label = new JLabel(field.getDisplayComboBoxModel().getSize() + " out of " + comboBoxModel.getSize() + " fonts");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));

        field.getDisplayComboBoxModel().addListDataListener(new ListDataListener() {
            public void intervalAdded(ListDataEvent e) {
                updateLabel(e);
            }

            public void intervalRemoved(ListDataEvent e) {
                updateLabel(e);
            }

            public void contentsChanged(ListDataEvent e) {
                updateLabel(e);
            }

            protected void updateLabel(ListDataEvent e) {
                if (e.getSource() instanceof ListModel) {
                    int count = ((ListModel) e.getSource()).getSize();
                    label.setText(count + " out of " + comboBoxModel.getSize() + " fonts");
                }
            }
        });
        comboBoxPanel.add(comboBox);
        comboBoxPanel.add(label, BorderLayout.BEFORE_FIRST_LINE);

        JPanel panel = new JPanel();
        panel.setLayout(new JideBoxLayout(panel, JideBoxLayout.Y_AXIS, 6));
        panel.add(quickSearchPanel);
        panel.add(comboBoxPanel);
        panel.add(Box.createGlue(), JideBoxLayout.VARY);

        return panel;
    }

    @Override
    public String getDemoFolder() {
        return "G15.QuickFilter";
    }

    static public void main(String[] s) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new QuickFilterComboBoxDemo());
    }
}
