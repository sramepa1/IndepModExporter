/*
 * @(#)BeanTableDemo.java 6/6/2007
 *
 * Copyright 2002 - 2007 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.converter.DoubleConverter;
import com.jidesoft.converter.ObjectConverterManager;
import com.jidesoft.grid.*;
import com.jidesoft.icons.IconsFactory;
import com.jidesoft.introspector.BeanIntrospector;
import com.jidesoft.introspector.Introspector;
import com.jidesoft.introspector.IntrospectorFactory;
import com.jidesoft.introspector.IntrospectorManager;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.JideBoxLayout;
import com.jidesoft.swing.JideSplitPane;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.beans.IntrospectionException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Demoed Component: {@link com.jidesoft.grid.BeanTableModel} <br> Required jar files: jide-common.jar, jide-grids.jar
 * <br> Required L&F: any L&F
 */
public class BeanTableDemo extends AbstractDemo {
    protected SortableTable _sortableTable;
    private PropertyTable _propertyTable;
    private BeanTableModel<JLabel> _beanTableModel;

    final String[] titles = new String[]{
            "Mail",
            "Calendar",
            "Contacts",
            "Tasks",
            "Notes",
            "Folder List",
            "Shortcuts",
            "Journal"
    };

    final ImageIcon[] icons = new ImageIcon[]{
            IconsFactory.getImageIcon(BeanTableDemo.class, "icons/email.gif"),
            IconsFactory.getImageIcon(BeanTableDemo.class, "icons/calendar.gif"),
            IconsFactory.getImageIcon(BeanTableDemo.class, "icons/contacts.gif"),
            IconsFactory.getImageIcon(BeanTableDemo.class, "icons/tasks.gif"),
            IconsFactory.getImageIcon(BeanTableDemo.class, "icons/notes.gif"),
            IconsFactory.getImageIcon(BeanTableDemo.class, "icons/folder.gif"),
            IconsFactory.getImageIcon(BeanTableDemo.class, "icons/shortcut.gif"),
            IconsFactory.getImageIcon(BeanTableDemo.class, "icons/journal.gif")
    };

    public BeanTableDemo() {
        ObjectConverterManager.initDefaultConverter();
        DecimalFormat format = new DecimalFormat();
        format.setMinimumFractionDigits(2);
        format.setMaximumFractionDigits(10);
        DoubleConverter converter = new DoubleConverter(format);
        ObjectConverterManager.registerConverter(Double.class, converter);
    }

    public String getName() {
        return "BeanTableModel Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_GRIDS;
    }

    @Override
    public String getDescription() {
        return "This is a demo of BeanTableModel. \n" +
                "\nThe table on the left side is based on BeanTableModel. It shows properties from eight JLabels. Click on a row to select a JLabel as the object. The PropertyTableModel will show the properties of the JLabel in a vertical layout. The preview panel" +
                "will show the actual JLabel a as Swing component in a UI. When you change any propreties in the JLabels, it will be reflected in the UI immediately.\n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.grid.SortableTable";
    }

    private int _previouslySelectedRow = -1;

    public Component getDemoPanel() {
        IntrospectorManager.initDefaultIntrospector();
        IntrospectorManager.registerIntrospector(JLabel.class, new IntrospectorFactory() {
            public Introspector create() {
                try {
                    return new BeanIntrospector(JLabel.class, new String[]{"name", "Name", "text", "Text", "icon", "Icon", "opaque", "Opaque", "toolTipText", "ToolTip", "background", "Background", "foreground", "Foreground"}, 2);
                }
                catch (IntrospectionException e) {
                    return null;
                }
            }
        });

        List<JLabel> objects = new ArrayList();
        for (int i = 0; i < 8; i++) {
            JLabel label = new JLabel(titles[i], icons[i], JLabel.LEADING);
            label.setToolTipText(titles[i]);
            label.setName(titles[i]);
            objects.add(label);
        }

        Introspector introspector = IntrospectorManager.getIntrospector(JLabel.class);
        _beanTableModel = new BeanTableModel(objects, introspector);
        try {
            _beanTableModel.bind(objects);
        }
        catch (Exception e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
        _sortableTable = new SortableTable(_beanTableModel);
        TableUtils.autoResizeAllColumns(_sortableTable);
        _sortableTable.setRowHeight(20);

        _propertyTable = new PropertyTable();
        _propertyTable.setRowHeight(20);

        JideSplitPane tablePane = new JideSplitPane();
        tablePane.add(createTitledPanel("BeanTableModel", new JScrollPane(_sortableTable)), JideBoxLayout.VARY);
        tablePane.add(createTitledPanel("PropertyTable", new PropertyPane(_propertyTable)));

        JPanel preview = new JPanel(new FlowLayout(FlowLayout.LEADING, 6, 6));
        for (Object object : objects) {
            preview.add((JLabel) object);
        }

        _sortableTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int visualRow = _sortableTable.getSelectedRow();
                    if (visualRow == -1) {
                        _propertyTable.setModel(new PropertyTableModel());
                    }
                    else {
                        int actualRow = TableModelWrapperUtils.getActualRowAt(_sortableTable.getModel(), visualRow);
                        if (actualRow == _previouslySelectedRow) {
                            return;
                        }

                        if (_previouslySelectedRow != -1) {
                            Object obj = _beanTableModel.getObject(_previouslySelectedRow);
                            try {
                                ((PropertyTableModel<Property>) _propertyTable.getModel()).unbind(obj);
                            }
                            catch (Exception e1) {
                                //noinspection CallToPrintStackTrace
                                e1.printStackTrace();
                            }
                        }

                        Object obj = _beanTableModel.getObject(actualRow);
                        PropertyTableModel<Property> propertyTaleModel = IntrospectorManager.getBeanIntrospector(JLabel.class).createPropertyTableModel(obj);
                        try {
                            propertyTaleModel.bind(obj);
                        }
                        catch (Exception e1) {
                            //noinspection CallToPrintStackTrace
                            e1.printStackTrace();
                        }
                        propertyTaleModel.setOrder(PropertyTableModel.SORTED);
                        _propertyTable.setModel(propertyTaleModel);

                        _previouslySelectedRow = actualRow;
                    }
                }
            }
        });

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(tablePane);
        panel.add(createTitledPanel("Preview", preview), BorderLayout.AFTER_LAST_LINE);
        return panel;
    }

    private static JPanel createTitledPanel(String title, JComponent component) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(component);
        panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(title), BorderFactory.createEmptyBorder(0, 6, 6, 6)));
        return panel;
    }

    @Override
    public String getDemoFolder() {
        return "G2.BeanTableModel";
    }

    static public void main(String[] s) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new BeanTableDemo());
    }
}
