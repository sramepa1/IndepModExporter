/*
 * @(#)MultiColumnPropertyPaneDemo.java 3/15/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.combobox.FileChooserComboBox;
import com.jidesoft.combobox.FileChooserPanel;
import com.jidesoft.combobox.PopupPanel;
import com.jidesoft.converter.ColorConverter;
import com.jidesoft.converter.ConverterContext;
import com.jidesoft.converter.MonthConverter;
import com.jidesoft.grid.*;
import com.jidesoft.plaf.LookAndFeelFactory;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Demoed Component: {@link com.jidesoft.grid.PropertyPane}, {@link com.jidesoft.grid.PropertyTable}
 * <br> Required jar files: jide-common.jar, jide-grids.jar <br> Required L&F: Jide L&F extension
 * required
 */
public class MultiColumnPropertyPaneDemo extends AbstractDemo {

    private static PropertyTable _table;

    public MultiColumnPropertyPaneDemo() {
        CellEditorManager.registerEditor(String.class, new CellEditorFactory() {
            public CellEditor create() {
                return new FileNameCellEditor() {
                    protected FileChooserComboBox createFileChooserComboBox() {
                        return new FileChooserComboBox() {
                            @Override
                            public PopupPanel createPopupComponent() {
                                FileChooserPanel panel = new FileChooserPanel() {
                                    @Override
                                    protected JFileChooser createFileChooser() {
                                        JFileChooser fileChooser = new JFileChooser();
                                        fileChooser.setFileFilter(new FileFilter() {
                                            @Override
                                            public boolean accept(File f) {
                                                return f.getName().endsWith(".java");
                                            }

                                            @Override
                                            public String getDescription() {
                                                return null;
                                            }
                                        });
                                        return fileChooser;
                                    }
                                };
                                return panel;
                            }
                        };
                    }
                };
            }
        }, FileNameCellEditor.CONTEXT);
    }

    @Override
    public String getDescription() {
        return "This is a demo of PropertyTable and PropertyPane. PropertyTable is a special two-column table which is ideal for showing name/value pairs. PropertyPane is a wrapper around PropertyTable with toolbar buttons to support typical operations on PropertyTable\n" +
                "\nPropertyPane is backed by PropertyTableModel. You can define mutliple levels of nested categories and still be able to show them in PropertyTable.\n" +
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
        return "G1.PropertyPane";
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new MultiColumnPropertyPaneDemo());
    }

    public String getName() {
        return "PropertyPane Demo";
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
        // before use MaskFormatter, you must register converter context and editor context
        ConverterContext ssnConverterContext = null;
        try {
            ssnConverterContext = new ConverterContext("SSN", new MaskFormatter("###-##-####"));
        }
        catch (ParseException e) {
        }
        EditorContext ssnEditorContext = new EditorContext("SSN");
        CellEditorManager.registerEditor(String.class, new CellEditorFactory() {
            public CellEditor create() {
                return new FormattedTextFieldCellEditor(String.class);
            }
        }, ssnEditorContext);

        ArrayList list = new ArrayList();

        SampleProperty property = null;

        property = new SampleProperty("Background", "The row is intended to show how to create a cell to input color in RGB format.", Color.class, "Appearance");
        list.add(property);

        property = new SampleProperty("Foreground", "The row is intended to show how to create a cell to input color in HEX format.", Color.class, "Appearance", ColorConverter.CONTEXT_HEX);
        list.add(property);

        property = new SampleProperty("Opaque", "The row is intended to show how to create a cell to input a boolean value.", Boolean.class, "Appearance");
        list.add(property);

        property = new SampleProperty("Bounds", "The row is intended to show how to create a cell to input a Rectangle.", Rectangle.class, "Appearance");
        list.add(property);

        property = new SampleProperty("Dimension", "The row is intended to show how to create a cell to input a Dimension.", Dimension.class, "Appearance");
        list.add(property);

        property = new SampleProperty("Name", "Name of the component", String.class);
        property.setCategory("Appearance");
        list.add(property);

        property = new SampleProperty("Font Name", "The row is intended to show how to create a cell to choose a font name", String.class);
        property.setEditorContext(new EditorContext("FontName"));
        property.setConverterContext(new ConverterContext("FontName"));
        property.setCategory("Appearance");
        list.add(property);

        property = new SampleProperty("Text", "Text of the component", String.class);
        property.setCategory("Appearance");
        list.add(property);

        property = new SampleProperty("Visible", "Visibility", Boolean.class);
        property.setCategory("Appearance");
        list.add(property);

        property = new SampleProperty("File Name", "The row is intended to show how to create a cell to input a file using FileChooser.", String.class);
        property.setEditorContext(FileNameCellEditor.CONTEXT);
        list.add(property);

        property = new SampleProperty("CreationDate", "The row is intended to show how to create a cell to input Date using DateComboBox.", Date.class);
        list.add(property);

        property = new SampleProperty("ExpirationDate", "The row is intended to show how to create a cell to input month/year using MonthComboBox.", Calendar.class);
        property.setConverterContext(MonthConverter.CONTEXT_MONTH);
        property.setEditorContext(MonthCellEditor.CONTEXT);
        list.add(property);

        property = new SampleProperty("Not Editable", "The row is intended to show a readonly cell.");
        property.setEditable(false);
        list.add(property);

        property = new SampleProperty("Double", "The row is intended to show how to create a cell to input a double.", Double.class);
        list.add(property);

        property = new SampleProperty("Float", "The row is intended to show how to create a cell to input a float.", Float.class);
        list.add(property);

        property = new SampleProperty("Integer", "The row is intended to show how to create a cell to input an integer.", Integer.class);
        list.add(property);

        property = new SampleProperty("Long text to test tooltips. If you see the whole text, you are OK.", "The row is intended to show how to tooltip of the cell value is too long.");
        list.add(property);


        property = new SampleProperty("SSN", "This row is intended to show how to create a String cell editor which uses MaskFormatter",
                String.class);
        property.setConverterContext(ssnConverterContext);
        property.setEditorContext(ssnEditorContext);
        list.add(property);

        property = new SampleProperty("Level 1", "The row is intended to show how to create several levels.");

        SampleProperty property2 = new SampleProperty("Level 2");
        property.addChild(property2);

        SampleProperty property31 = new SampleProperty("Level 3.1");
        property2.addChild(property31);

        SampleProperty property32 = new SampleProperty("Level 3.2");
        property2.addChild(property32);

        SampleProperty property33 = new SampleProperty("You can have as many levels as you want.");
        property2.addChild(property33);

        list.add(property);

        property = new SampleProperty("String Array", "This row is intended to show how to create a String array cell editor",
                String[].class);
        list.add(property);


        PropertyTableModel model = new PropertyTableModel(list) {
            @Override
            public int getColumnCount() {
                return 3;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex <= 1) {
                    return super.getColumnClass(columnIndex);
                }
                else {
                    return super.getColumnClass(1);
                }
            }

            @Override
            public String getColumnName(int column) {
                if (column <= 1) {
                    return super.getColumnName(column);
                }
                else {
                    return super.getColumnName(1);
                }
            }

            @Override
            public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
                if (columnIndex <= 1) {
                    super.setValueAt(aValue, rowIndex, columnIndex);
                }
                else {
                    Property property = getPropertyAt(rowIndex);
                    if (property instanceof SampleProperty) {
                        property.setValueAt(aValue, columnIndex);
                    }
                }
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                if (columnIndex <= 1) {
                    return super.getValueAt(rowIndex, columnIndex);
                }
                else {
                    Property property = getPropertyAt(rowIndex);
                    if (property instanceof SampleProperty) {
                        return property.getValueAt(columnIndex);
                    }
                }
                return "";
            }

            @Override
            public Class<?> getCellClassAt(int row, int column) {
                if (column <= 1) {
                    return super.getCellClassAt(row, column);
                }
                else {
                    return super.getCellClassAt(row, 1);
                }
            }
        };
        PropertyTable table = new PropertyTable(model);
        table.expandFirstLevel();
        return table;
    }

    static HashMap map = new HashMap();

    static {
        map.put("Bounds", new Rectangle(0, 0, 100, 200));
        map.put("Dimension", new Dimension(800, 600));
        map.put("Background", new Color(255, 0, 0));
        map.put("Foreground", new Color(255, 255, 255));
        map.put("File Name", "C:\\Program Files\\JIDE\\src\\com\\jidesoft\\Demo.java");
        map.put("CreationDate", Calendar.getInstance());
        map.put("ExpirationDate", Calendar.getInstance());
        map.put("Name", "Label1");
        map.put("Font Name", "Arial");
        map.put("Text", "Data");
        map.put("Opaque", Boolean.FALSE);
        map.put("Visible", Boolean.TRUE);
        map.put("Not Editable", 10);
        map.put("Integer", 1234);
        map.put("Double", 1.0);
        map.put("Float", new Float(0.01));
        map.put("SSN", "000-00-0000");
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

        @Override
        public Object getValueAt(int column) {
            if (column == 0) {
                return getDisplayName();
            }
            else {
                return getValue(); // just use getValue() for demoing purpose.
            }
        }

        @Override
        public void setValueAt(Object value, int column) {
            if (column != 0) {
                setValue(value); // just use setValue() for demoing purpose.
            }
        }
    }
}
