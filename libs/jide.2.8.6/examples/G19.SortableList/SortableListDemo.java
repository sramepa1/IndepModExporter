/*
 * @(#)SortableListDemo.java 7/14/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.list.ListUtils;
import com.jidesoft.list.SortableListModel;
import com.jidesoft.plaf.LookAndFeelFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.util.Arrays;

/**
 * Demoed Component: {@link com.jidesoft.list.SortableListModel} <br> Required jar files:
 * jide-common.jar, jide-grids.jar <br> Required L&F: any L&F
 */
public class SortableListDemo extends AbstractDemo {

    public SortableListDemo() {
    }

    public String getName() {
        return "Sortable List Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_GRIDS;
    }

    @Override
    public String getDescription() {
        return "This is a demo of JList with SortableListModel to get sortable feature. You can click on the \"Sort\" to sort\n" +
                "or unsort the list. Please note, SortableListModel is a model wrapper, just like TableModelWrapper or TreeModelWrapper. \n" +
                "It uses index mapping to sort the list model. The actual list model is not touched at all. Not only that, " +
                "selected items in the list are kept after sorting." +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.list.SortableListModel";
    }

    public Component getDemoPanel() {
        final DefaultListModel listModel = new DefaultListModel();
        String[] fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        for (String fontName : fontNames) {
            if (Math.random() > 0.5) {
                listModel.add(0, fontName);
            }
            else {
                listModel.addElement(fontName);
            }
        }

        final SortableListModel sortableListModel = new SortableListModel(listModel);
        final JList list = new JList(sortableListModel);
        list.setVisibleRowCount(20);
        JPanel panel = new JPanel(new BorderLayout(6, 6));
        panel.add(new JScrollPane(list));
        JCheckBox checkBox = new JCheckBox("Sort");
        checkBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                int[] selected = ListUtils.saveSelection(list);
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    sortableListModel.sort(SortableListModel.SORT_ASCENDING);
                }
                else {
                    sortableListModel.sort(SortableListModel.UNSORTED);
                }
                ListUtils.loadSelection(list, selected);
            }
        });
        list.registerKeyboardAction(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                int[] selection = list.getSelectedIndices();
                int[] actualSelection = new int[selection.length];
                for (int i = 0; i < selection.length; i++) {
                    actualSelection[i] = sortableListModel.getActualIndexAt(selection[i]);
                }

                Arrays.sort(actualSelection);

                for (int i = actualSelection.length - 1; i >= 0; i--) {
                    int index = actualSelection[i];
                    listModel.remove(index);
                }
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), JComponent.WHEN_FOCUSED);
        checkBox.setSelected(true);
        panel.add(checkBox, BorderLayout.BEFORE_FIRST_LINE);

//        final JTextField textField = new JTextField();
//        textField.addActionListener(new AbstractAction() {
//            public void actionPerformed(ActionEvent e) {
//                String text = textField.getText();
//                if (text != null && text.length() != 0) {
//                    if (list.getSelectedIndex() != -1) {
//                        int actualIndex = sortableListModel.getActualIndexAt(list.getSelectedIndex());
//                        listModel.setElementAt(text, actualIndex);
//                    }
//                    else {
//                        listModel.addElement(text);
//                    }
//                    list.setSelectedValue(text, true);
//                    textField.setText("");
//                }
//            }
//        });
//        panel.add(textField, BorderLayout.AFTER_LAST_LINE);

        return panel;
    }

    @Override
    public String getDemoFolder() {
        return "G19.SortableList";
    }

    static public void main(String[] s) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new SortableListDemo());
    }
}
