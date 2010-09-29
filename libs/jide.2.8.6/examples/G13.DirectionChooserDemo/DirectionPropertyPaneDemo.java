/*
 * @(#)CustomizePropertyPaneDemo.java 4/6/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.*;
import com.jidesoft.plaf.LookAndFeelFactory;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Demoed Component: {@link com.jidesoft.grid.PropertyPane}, {@link com.jidesoft.grid.PropertyTable}
 * <br> Required jar files: jide-common.jar, jide-grids.jar <br> Required L&F: Jide L&F extension
 * required
 */
public class DirectionPropertyPaneDemo extends AbstractDemo {

    private static PropertyTable _table;

    public DirectionPropertyPaneDemo() {
        CellEditorManager.registerEditor(Integer.class, new CellEditorFactory() {
            public CellEditor create() {
                return new DirectionCellEditor();
            }
        }, DirectionCellEditor.CONTEXT);
        DirectionCellRenderer cellRenderer = new DirectionCellRenderer();
        CellRendererManager.registerRenderer(Integer.class, cellRenderer, DirectionCellEditor.CONTEXT);
    }

    @Override
    public String getDescription() {
        return "This is a demo of creating a customized cell editor in PropertyTable. \n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.grid.PropertyPane\n" +
                "com.jidesoft.grid.PropertyTable\n" +
                "com.jidesoft.grid.PropertyTableModel\n" +
                "com.jidesoft.grid.Property\n" +
                "com.jidesoft.grid.CellEditorManager\n" +
                "com.jidesoft.grid.CellRendererManager\n" +
                "com.jidesoft.converter.ObjectConverterManager";
    }

    @Override
    public String getDemoFolder() {
        return "G13.DirectionChooserDemo";
    }

    @Override
    public String[] getDemoSource() {
        return new String[]{"DirectionChooserDemo.java", "DirectionConverter.java", "DirectionChooserPanel.java", "DirectionComboBox.java", "DirectionSplitButton.java"};
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new DirectionPropertyPaneDemo());
    }

    public String getName() {
        return "PropertyPane Demo (Customized Cell Editor)";
    }

    public String getProduct() {
        return PRODUCT_NAME_GRIDS;
    }

    public Component getDemoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        _table = createTable();
        PropertyPane propertyPane = new PropertyPane(_table);
        panel.add(propertyPane, BorderLayout.CENTER);
        return panel;
    }

    // create property table
    private PropertyTable createTable() {
        ArrayList list = new ArrayList();

        SampleProperty property = null;

        property = new SampleProperty("Direction 1", "A direction cell editor.", Integer.class, "Direction");
        property.setConverterContext(DirectionConverter.CONTEXT);
        property.setEditorContext(DirectionCellEditor.CONTEXT);
        list.add(property);

        property = new SampleProperty("Direction 2", "A direction cell editor.", Integer.class, "Direction");
        property.setConverterContext(DirectionConverter.CONTEXT);
        property.setEditorContext(DirectionCellEditor.CONTEXT);
        list.add(property);

        PropertyTableModel model = new PropertyTableModel(list);
        PropertyTable table = new PropertyTable(model);
        table.expandFirstLevel();

        return table;
    }

    static HashMap map = new HashMap();

    static {
        map.put("Direction 1", SwingConstants.WEST);
        map.put("Direction 2", SwingConstants.EAST);
    }

    static class SampleProperty extends Property {
        public SampleProperty(String name, String description, Class type, String category, ConverterContext context, java.util.List childProperties) {
            super(name, description, type, category, context, childProperties);
        }

        public SampleProperty(String name, String description, Class type, String category, ConverterContext context) {
            super(name, description, type, category, context);
        }

        public SampleProperty(String name, String description, Class type, String category) {
            super(name, description, type, category);
        }

        public SampleProperty(String name, String description, Class type) {
            super(name, description, type);
        }

        public SampleProperty(String name, String description) {
            super(name, description);
        }

        public SampleProperty(String name) {
            super(name);
        }

        @Override
        public void setValue(Object value) {
            map.put(getFullName(), value);
        }

        @Override
        public Object getValue() {
            Object value = map.get(getFullName());
            return value;
        }

        @Override
        public boolean hasValue() {
            return map.get(getFullName()) != null;
        }
    }
}
