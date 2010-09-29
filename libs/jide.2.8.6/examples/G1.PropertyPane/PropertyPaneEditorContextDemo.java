/*
 * @(#)${NAME}
 *
 * Copyright 2002 - 2004 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.action.CommandBar;
import com.jidesoft.combobox.ListComboBox;
import com.jidesoft.converter.ConverterContext;
import com.jidesoft.grid.*;
import com.jidesoft.icons.JideIconsFactory;
import com.jidesoft.plaf.LookAndFeelFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Demoed Component: {@link com.jidesoft.grid.PropertyPane}, {@link com.jidesoft.grid.PropertyTable} <br> This demo
 * shows you how to use EditorContext to pass in additional data so that cell editor can use it. <br> Required jar
 * files: jide-common.jar, jide-grids.jar <br> Required L&F: Jide L&F extension required
 */
public class PropertyPaneEditorContextDemo extends JFrame {

    private static PropertyPaneEditorContextDemo _frame;
    private static PropertyTable _table;

    public PropertyPaneEditorContextDemo(String title) throws HeadlessException {
        super(title);
    }

    public PropertyPaneEditorContextDemo() throws HeadlessException {
        this("");
    }

    public String getDemoFolder() {
        return "G1.PropertyPane";
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();

        _frame = new PropertyPaneEditorContextDemo("PropertyPane Sample");
        _frame.setIconImage(JideIconsFactory.getImageIcon(JideIconsFactory.JIDE32).getImage());
        _frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                _frame.dispose();
                System.exit(0);
            }
        });

        _table = createTable();

        PropertyPane propertyPane = new PropertyPane(_table) {
            @Override
            protected JComponent createToolBarComponent() {
                CommandBar toolBar = new CommandBar();
                toolBar.setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
                toolBar.setFloatable(false);
                toolBar.setStretch(true);
                toolBar.setPaintBackground(false);
                toolBar.setChevronAlwaysVisible(false);
                return toolBar;
            }
        };

        _frame.getContentPane().setLayout(new BorderLayout());
        _frame.getContentPane().add(propertyPane, BorderLayout.CENTER);

        _frame.setBounds(10, 10, 400, 500);

        _frame.setVisible(true);

    }

    // create property table
    private static PropertyTable createTable() {
        EditorContext genericEditorContext = new EditorContext("Generic");
        CellEditorManager.registerEditor(String.class,
                new CellEditorFactory() {
                    public CellEditor create() {
                        return new FormattedTextFieldCellEditor(String.class);
                    }
                }, genericEditorContext);

        ArrayList list = new ArrayList();

        SampleProperty property = null;

        property = new SampleProperty("two choices", "This row has two possible values", String.class, "Choices");
        genericEditorContext = new EditorContext("Generic", new String[]{"1", "2"});
        property.setEditorContext(genericEditorContext);
        list.add(property);

        property = new SampleProperty("three choices", "This row has three possible values", String.class, "Choices");
        genericEditorContext = new EditorContext("Generic", new String[]{"1", "2", "3"});
        property.setEditorContext(genericEditorContext);
        list.add(property);

        property = new SampleProperty("four choices", "This row has four possible values", String.class, "Choices");
        genericEditorContext = new EditorContext("Generic", new String[]{"1", "2", "3", "4"});
        property.setEditorContext(genericEditorContext);
        list.add(property);

        PropertyTableModel model = new PropertyTableModel(list);
        PropertyTable table = new PropertyTable(model);
        table.expandFirstLevel();
        return table;
    }

    static HashMap map = new HashMap();

    static {
        map.put("two choices", "2");
        map.put("three choices", "3");
        map.put("four choices", "4");
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
            return map.get(getFullName());
        }

        @Override
        public boolean hasValue() {
            return map.get(getFullName()) != null;
        }

        static GenericCellEditor _cellEditor = new GenericCellEditor();

        @Override
        public CellEditor getCellEditor(int column) {
            if (column == 1) {
                return _cellEditor;
            }
            return super.getCellEditor(column);
        }
    }

    static class GenericCellEditor extends ListComboBoxCellEditor {
        public GenericCellEditor() {
            super(new Object[0]);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            Component component = super.getTableCellEditorComponent(table, value, isSelected, row, column);
            if (component instanceof ListComboBox && table instanceof PropertyTable && table.getModel() instanceof PropertyTableModel) {
                PropertyTableModel model = (PropertyTableModel) table.getModel();
                Property property = model.getPropertyAt(row);
                EditorContext context = property.getEditorContext();
                if (context != null && context.getUserObject() instanceof String[]) {
                    DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel((String[]) context.getUserObject());
                    comboBoxModel.setSelectedItem(value);
                    ((ListComboBox) component).setModel(comboBoxModel);
                    ((ListComboBox) component).setPopupVolatile(true);
                }
            }
            return component;
        }
    }
}
