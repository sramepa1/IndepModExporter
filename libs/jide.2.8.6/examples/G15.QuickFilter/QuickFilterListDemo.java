/*
 * @(#)FilterableListDemo.java 7/20/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.list.QuickListFilterField;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.JideTitledBorder;
import com.jidesoft.swing.PartialEtchedBorder;
import com.jidesoft.swing.PartialSide;
import com.jidesoft.swing.SearchableUtils;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.text.Position;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Arrays;

/**
 * Demoed Component: {@link com.jidesoft.grid.SortableTable} <br> Required jar files:
 * jide-common.jar, jide-grids.jar <br> Required L&F: any L&F
 */
public class QuickFilterListDemo extends AbstractDemo {

    public QuickFilterListDemo() {
    }

    public String getName() {
        return "QuickFilter (List) Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_GRIDS;
    }

    @Override
    public String getDescription() {
        return "This is a demo of QuickListFilterField and FilterableListModel. \n" +
                "\nFilterableListModel is a ListModel which can support Filters. FilterableListModel can wrap any existing list model and apply filters onto the list model.\n" +
                "\nQuickListFilterField is an instance of QuickFilterField. It provides a quick way to allow filter away items from a list model. If you have a huge list, this QuickListFilterField will allow you to narrow the list easily.\n" +
                "Demoed classes:\n" +
                "com.jidesoft.list.FilterableListModel\n" +
                "com.jidesoft.list.QuickListFilterField";
    }


    public Component getDemoPanel() {
        final DefaultListModel listModel = new DefaultListModel();
        String[] fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        for (String fontName : fontNames) {
            listModel.addElement(fontName);
        }
        JPanel panel = new JPanel(new BorderLayout(6, 6));

        JPanel quickSearchPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        final QuickListFilterField field = new QuickListFilterField(listModel);
        field.setHintText("Type here to filter fonts");
        quickSearchPanel.add(field);
        quickSearchPanel.setBorder(new JideTitledBorder(new PartialEtchedBorder(PartialEtchedBorder.LOWERED, PartialSide.NORTH), "QuickListFilterField", JideTitledBorder.LEADING, JideTitledBorder.ABOVE_TOP));

        final JList list = new JList(field.getDisplayListModel()) {
            @Override
            public int getNextMatch(String prefix, int startIndex, Position.Bias bias) {
                return -1;
            }
        };
        list.setVisibleRowCount(20);
        field.setList(list);
        SearchableUtils.installSearchable(list);

        JPanel listPanel = new JPanel(new BorderLayout(2, 2));
        listPanel.setBorder(BorderFactory.createCompoundBorder(new JideTitledBorder(new PartialEtchedBorder(PartialEtchedBorder.LOWERED, PartialSide.NORTH), "Filtered Font List", JideTitledBorder.LEADING, JideTitledBorder.ABOVE_TOP),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)));
        final JLabel label = new JLabel(field.getDisplayListModel().getSize() + " out of " + listModel.getSize() + " fonts");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
        list.registerKeyboardAction(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                int[] selection = list.getSelectedIndices();
                int[] actualSelection = new int[selection.length];
                for (int i = 0; i < selection.length; i++) {
                    actualSelection[i] = field.getDisplayListModel().getActualIndexAt(selection[i]);
                }

                Arrays.sort(actualSelection);

                for (int i = actualSelection.length - 1; i >= 0; i--) {
                    int index = actualSelection[i];
                    listModel.remove(index);
                }
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), JComponent.WHEN_FOCUSED);

        field.getDisplayListModel().addListDataListener(new ListDataListener() {
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
                    label.setText(count + " out of " + listModel.getSize() + " fonts");
                }
            }
        });
        listPanel.add(new JScrollPane(list));
        listPanel.add(label, BorderLayout.BEFORE_FIRST_LINE);

        panel.add(quickSearchPanel, BorderLayout.BEFORE_FIRST_LINE);
        panel.add(listPanel, BorderLayout.CENTER);

//        final JTextField textField = new JTextField();
//        textField.addActionListener(new AbstractAction() {
//            public void actionPerformed(ActionEvent e) {
//                String text = textField.getText();
//                if (text != null && text.length() != 0) {
//                    if (list.getSelectedIndex() != -1) {
//                        int actualIndex = field.getDisplayListModel().getActualIndexAt(list.getSelectedIndex());
//                        listModel.setElementAt(text, actualIndex);
//                    }
//                    else {
//                        listModel.insertElementAt(text, 10);
//                        list.setSelectedValue(text, true);
//                    }
//                    textField.setText("");
//                }
//            }
//        });
//        panel.add(textField, BorderLayout.AFTER_LAST_LINE);
        return panel;
    }

    @Override
    public String getDemoFolder() {
        return "G15.QuickFilter";
    }

    static public void main(String[] s) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new QuickFilterListDemo());
    }
}
