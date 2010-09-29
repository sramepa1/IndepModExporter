/*
 * SortFontGroupListDemo.java
 *
 * Created on Oct 16, 2007, 11:48:16 AM
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import com.jidesoft.combobox.FontListCellRenderer;
import com.jidesoft.list.GroupList;
import com.jidesoft.list.ListModelWrapper;
import com.jidesoft.list.SortableGroupableListModel;
import com.jidesoft.list.SortableListModel;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.Comparator;

public class SortFontGroupListDemo extends AbstractDemo {

    private FontModel _model;
    private SortableGroupableListModel _sortableListModel;

    public SortFontGroupListDemo() {
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

    @Override
    public Component getOptionsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new JideBoxLayout(panel, JideBoxLayout.Y_AXIS, 2));

        JRadioButton ascending = new JRadioButton("Sort Ascending");
        ascending.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    _sortableListModel.setSortOrder(SortableListModel.SORT_ASCENDING);
                }
            }
        });

        JRadioButton descending = new JRadioButton("Sort Descending");
        descending.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    _sortableListModel.setSortOrder(SortableListModel.SORT_DESCENDING);
                }
            }
        });
        final JRadioButton unsorted = new JRadioButton("Reset", true);
        unsorted.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    _sortableListModel.setSortOrder(SortableListModel.UNSORTED);
                }
            }
        });

        ButtonGroup group = new ButtonGroup();
        group.add(unsorted);
        group.add(ascending);
        group.add(descending);

        JButton shuffle = new JButton("Shuffle");
        shuffle.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                unsorted.setSelected(true);
                _model.shuffle();
            }
        });

        panel.add(unsorted);
        panel.add(ascending);
        panel.add(descending);
        panel.add(shuffle);
        return panel;
    }

    public JComponent getDemoPanel() {
        JPanel panel = new JPanel(new BorderLayout(6, 6));

        _model = new FontModel();
        _model.shuffle();
        _model.putFont((Font) _model.getElementAt(2));
        _model.putFont((Font) _model.getElementAt(_model.getSize() - 1));

        _sortableListModel = new SortableGroupableListModel(_model);
        _sortableListModel.setComparator(new Comparator<Font>() {
            public int compare(Font f1, Font f2) {
                return f1.getName().compareToIgnoreCase(f2.getName());
            }
        });
        final GroupList list = new GroupList(_sortableListModel);
        SearchableUtils.installSearchable(list);
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == 1 && e.getClickCount() >= 2) {
                    int selectedIndex = list.getSelectedIndex();
                    Object element = list.getModel().getElementAt(selectedIndex);
                    if (element instanceof Font) {
                        Font font = (Font) element;
                        _model.putFont(font);
                    }
                }
            }
        });
        configureList(list);

        JPanel listPanel = new JPanel(new BorderLayout(2, 2));
        listPanel.setBorder(BorderFactory.createCompoundBorder(new JideTitledBorder(new PartialEtchedBorder(PartialEtchedBorder.LOWERED, PartialSide.NORTH), "Filtered Font List", JideTitledBorder.LEADING, JideTitledBorder.ABOVE_TOP),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)));
        list.registerKeyboardAction(new AbstractAction() {
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
        listPanel.add(new JScrollPane(list));
        listPanel.add(new JLabel("Double click to put the font into the recently used font group"), BorderLayout.BEFORE_FIRST_LINE);

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
        showAsFrame(new SortFontGroupListDemo());
    }

    @Override
    public String[] getDemoSource() {
        return new String[]{
                getClass().getName() + ".java",
                "FontModel.java",
                "GroupCellRenderer.java",
        };
    }
}
