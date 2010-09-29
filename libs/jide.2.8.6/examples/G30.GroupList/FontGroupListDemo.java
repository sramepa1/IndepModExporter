import com.jidesoft.combobox.FontListCellRenderer;
import com.jidesoft.list.GroupList;
import com.jidesoft.plaf.LookAndFeelFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FontGroupListDemo extends AbstractDemo {
    public FontGroupListDemo() {
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
        final FontModel model = new FontModel();
        model.putFont((Font) model.getElementAt(0));
        model.putFont((Font) model.getElementAt(10));

        final GroupList list = new GroupList(model);
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == 1 && e.getClickCount() >= 2) {
                    int selectedIndex = list.getSelectedIndex();
                    model.putFont((Font) list.getModel().getElementAt(selectedIndex));
                }
            }
        });
//        list.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
//            public void valueChanged(ListSelectionEvent e) {
//                System.out.println(e);
//                System.out.println(list.getModel().getElementAt(e.getLastIndex()));
//            }
//        });
        configureList(list);

        JPanel panel = new JPanel(new BorderLayout(4, 4));
        panel.add(new JLabel("Double click to put the font into the recently used font group"), BorderLayout.BEFORE_FIRST_LINE);
        panel.add(new JScrollPane(list));

        return panel;
    }

    private void configureList(GroupList list) {
        list.setCellRenderer(new FontListCellRenderer());
        list.setGroupCellRenderer(new GroupCellRenderer());
        list.setVisibleRowCount(20);
    }


    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new FontGroupListDemo());
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
