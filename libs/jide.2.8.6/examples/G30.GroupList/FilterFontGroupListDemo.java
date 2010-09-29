/*
 * @(#)FilterFontGroupListDemo.java 10/14/2007
 *
 * Copyright 2002 - 2007 JIDE Software Inc. All rights reserved.
 */

/*
 * FilterFontGroupListDemo.java
 *
 * Created on 2007-10-14, 8:44:26
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import com.jidesoft.combobox.FontListCellRenderer;
import com.jidesoft.list.*;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.JideTitledBorder;
import com.jidesoft.swing.PartialEtchedBorder;
import com.jidesoft.swing.PartialSide;
import com.jidesoft.swing.SearchableUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.EventObject;

public class FilterFontGroupListDemo extends AbstractDemo {
    private static final long serialVersionUID = -638987915478113355L;

    public FilterFontGroupListDemo() {
    }

    public String getName() {
        return "GroupList (Font Demo)";
    }

    public String getProduct() {
        return PRODUCT_NAME_GRIDS;
    }

    @Override
    public String getDemoFolder() {
        return "G30.GroupList";
    }

    @Override
    public String getDescription() {
        return "This is a demo of GroupList. GroupList is a JList supporting grouping. In this demo, we add \"Recently Used Fonts\" group to a regular JList. You can double click on a cell to put that cell into the \"Recently Used Fonts\" group.\n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.list.GroupList\n" +
                "com.jidesoft.list.GroupableListModel";
    }

    public JComponent getDemoPanel() {
        JPanel panel = new JPanel(new BorderLayout(6, 6));

        final FontModel model = new FontModel();
        model.putFont((Font) model.getElementAt(1));
        model.putFont((Font) model.getElementAt(10));

        JPanel quickSearchPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        final QuickGroupableListFilterField field = new FontFilterField(model);
        quickSearchPanel.add(field);
        quickSearchPanel.setBorder(new JideTitledBorder(new PartialEtchedBorder(PartialEtchedBorder.LOWERED, PartialSide.NORTH), "QuickListFilterField", JideTitledBorder.LEADING, JideTitledBorder.ABOVE_TOP));

        final FilterableGroupableListModel filteredListModel =
                (FilterableGroupableListModel) field.getDisplayListModel();
        final GroupList list = new GroupList(filteredListModel);
        field.setList(list);
        SearchableUtils.installSearchable(list);
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == 1 && e.getClickCount() >= 2) {
                    int selectedIndex = list.getSelectedIndex();
                    Object element = list.getModel().getElementAt(selectedIndex);
                    if (element instanceof Font) {
                        Font font = (Font) element;
                        model.putFont(font);
                        selectedIndex++;
                        list.setSelectedIndex(selectedIndex);
                    }
                }
            }
        });
        configureList(list);

        JPanel listPanel = new JPanel(new BorderLayout(2, 2));
        listPanel.setBorder(BorderFactory.createCompoundBorder(new JideTitledBorder(new PartialEtchedBorder(PartialEtchedBorder.LOWERED, PartialSide.NORTH), "Filtered Font List", JideTitledBorder.LEADING, JideTitledBorder.ABOVE_TOP),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)));
        int size = model.getSize() - model.getRecentlyUsedFont().size();
        final JLabel label = new JLabel(size + " out of " + size + " fonts");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
        list.registerKeyboardAction(new AbstractAction() {
            private static final long serialVersionUID = -6716726163445185240L;

            public void actionPerformed(ActionEvent e) {
                ListModel model = list.getModel();
                int[] selection = list.getSelectedIndices();
                int[] temp;

                while (model instanceof ListModelWrapper && !(model instanceof FontModel)) {
                    temp = new int[selection.length];
                    for (int i = 0; i < selection.length; i++) {
                        temp[i] = ((ListModelWrapper) model).getActualIndexAt(selection[i]);
                    }
                    selection = temp;
                    model = ((ListModelWrapper) model).getActualModel();
                }

                if (model instanceof FontModel) {
                    Arrays.sort(selection);

                    for (int i = selection.length - 1; i >= 0; i--) {
                        int index = selection[i];
                        if (0 <= index && index < model.getSize()) {
                            ((FontModel) model).remove(index);
                        }
                    }
                }
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), JComponent.WHEN_FOCUSED);

        filteredListModel.addListGroupChangeListener(new ListGroupChangeListener() {

            public void groupChanged(ListGroupChangeEvent e) {
                updateLabel(e);
            }

            protected void updateLabel(EventObject e) {
                if (e.getSource() instanceof ListModel) {
                    ListModel listModel = (ListModel) e.getSource();
                    int count = listModel.getSize();
                    int offset = 0;
                    for (int i = 0; i < count; i++) {
                        int indexInOriginalModel = ListModelWrapperUtils.getActualIndexAt(listModel, i);
                        if ("Recently Used Fonts".equals(model.getGroupAt(indexInOriginalModel))) {
                            offset++;
                        }
                        else {
                            break;
                        }
                    }
                    count -= offset;
                    label.setText(count + " out of " + (model.getSize() - model.getRecentlyUsedFont().size()) + " fonts");
                }
            }
        });
        listPanel.add(new JScrollPane(list));
        listPanel.add(new JLabel("Double click to put the font into the recently used font group"), BorderLayout.BEFORE_FIRST_LINE);
        listPanel.add(label, BorderLayout.BEFORE_FIRST_LINE);

        panel.add(quickSearchPanel, BorderLayout.BEFORE_FIRST_LINE);
        panel.add(listPanel, BorderLayout.CENTER);

        return panel;
    }

    private void configureList(GroupList list) {
        list.setCellRenderer(new FontListCellRenderer());
        list.setGroupCellRenderer(new GroupCellRenderer());
        list.setVisibleRowCount(20);
    }


    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new FilterFontGroupListDemo());
    }

    @Override
    public String[] getDemoSource() {
        return new String[]{
                getClass().getName() + ".java",
                "FontFilterField.java",
                "FontModel.java",
                "GroupCellRenderer.java",
        };
    }
}
