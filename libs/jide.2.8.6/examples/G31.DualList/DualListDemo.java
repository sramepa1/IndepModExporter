import com.jidesoft.list.DualList;
import com.jidesoft.list.DualListModel;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.JideBoxLayout;
import com.jidesoft.swing.JideTitledBorder;
import com.jidesoft.swing.PartialEtchedBorder;
import com.jidesoft.swing.PartialSide;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashSet;
import java.util.Set;

public class DualListDemo extends AbstractDemo {

    private DualList _list;

    public DualListDemo() {
    }

    public String getName() {
        return "DualList (Country Demo)";
    }

    public String getProduct() {
        return PRODUCT_NAME_GRIDS;
    }

    @Override
    public String getDemoFolder() {
        return "G31.DualList";
    }

    @Override
    public String getDescription() {
        return "DualList is a pane that contains two lists. " +
                "The list on the left contains the original items. " +
                "The list on the right are selected items. " +
                "There are controls to move items back and forth. \n" +
                "We have full keyboard support in this component" +
                "\n1. UP and DOWN key to move change the selected items in the two lists;" +
                "\n2. LEFT, RIGHT, or ENTER keys to move the selected items between the two lists;" +
                "\n3. CTRL-UP and CTRL-DOWN keys will move the selected items up and down in the right list;" +
                "\n4. CTRL-HOME and CTRL-END keys will move the selected items to the top or the bottom in the right list;" +
                "\n5. CTRL-LEFT and CTRL-RIGHT keys will move focus between the two lists.";
    }

    @Override
    public Component getOptionsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new JideBoxLayout(panel, JideBoxLayout.Y_AXIS, 2));
        JRadioButton v = new JRadioButton("Remove Chosen Items", true);
        v.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    _list.setSelectionMode(DualListModel.REMOVE_SELECTION);
                }
            }
        });

        JRadioButton vw = new JRadioButton("Disabled Chosen Items");
        vw.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    _list.setSelectionMode(DualListModel.DISABLE_SELECTION);
                }
            }
        });

        JRadioButton h = new JRadioButton("Keep Chosen Items");
        h.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    _list.setSelectionMode(DualListModel.KEEP_SELECTION);
                }
            }
        });

        ButtonGroup group = new ButtonGroup();
        group.add(h);
        group.add(v);
        group.add(vw);

        panel.add(h);
        panel.add(v);
        panel.add(vw);

        String[] commands = new String[]{
                DualList.COMMAND_MOVE_RIGHT,
                DualList.COMMAND_MOVE_LEFT,
                DualList.COMMAND_MOVE_ALL_RIGHT,
                DualList.COMMAND_MOVE_ALL_LEFT,
                DualList.COMMAND_MOVE_UP,
                DualList.COMMAND_MOVE_DOWN,
                DualList.COMMAND_MOVE_TO_TOP,
                DualList.COMMAND_MOVE_TO_BOTTOM,
        };
        for (final String command : commands) {
            final JCheckBox b = new JCheckBox(String.format("Show \"%s\"", command), true);
            b.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    _list.setButtonVisible(command, b.isSelected());
                }
            });
            panel.add(b);
        }
        return panel;
    }

    public JComponent getDemoPanel() {
        DualListModel model = new CountryDualListModel();
        _list = new DualList(model);
        configureList(_list);

//        _list.getModel().addListSelectionListener(new ListSelectionListener(){
//            public void valueChanged(ListSelectionEvent e) {
//                System.out.println(e.getFirstIndex() + " " + e.getLastIndex());
//            }
//        });

        JPanel panel = new JPanel(new BorderLayout(4, 4));
        panel.add(_list, BorderLayout.CENTER);

        return panel;
    }

    private void configureList(DualList list) {
        list.getOriginalListPane().setBorder(new JideTitledBorder(new PartialEtchedBorder(PartialEtchedBorder.LOWERED, PartialSide.NORTH), "All Countries:"));
        list.getSelectedListPane().setBorder(new JideTitledBorder(new PartialEtchedBorder(PartialEtchedBorder.LOWERED, PartialSide.NORTH), "Selected Countries:"));

        ListCellRenderer renderer = new CountryRenderer();
        list.setCellRenderer(renderer);

        DualListModel model = list.getModel();
        int size = model.getSize();
        int count = Math.min(3, size);

        Set<Integer> selection = new HashSet();
        while (selection.size() < count) {
            int value;
            do {
                value = (int) (Math.random() * size);
            }
            while (selection.contains(value));
            selection.add(value);
        }
        for (int value : selection) {
            model.addSelectionInterval(value, value);
        }
    }

    @Override
    public String[] getDemoSource() {
        return new String[]{
                getClass().getName() + ".java",
                "CountryDualListModel.java",
                "CountryRenderer.java",
        };
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new DualListDemo());
    }

}
