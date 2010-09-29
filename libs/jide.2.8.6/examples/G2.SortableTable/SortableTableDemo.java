/*
 * @(#)SortableTableDemo.java
 *
 * Copyright 2002 - 2003 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.converter.DoubleConverter;
import com.jidesoft.converter.ObjectConverterManager;
import com.jidesoft.grid.*;
import com.jidesoft.hssf.HssfTableUtils;
import com.jidesoft.icons.JideIconsFactory;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.JideBoxLayout;
import com.jidesoft.swing.JideTabbedPane;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Demoed Component: {@link SortableTable} <br> Required jar files: jide-common.jar, jide-grids.jar <br> Required L&F:
 * any L&F
 */
public class SortableTableDemo extends AbstractDemo {
    protected SortableTable _sortableTable;
    protected String _lastDirectory = ".";
    private static final long serialVersionUID = -5373007282200581748L;

    public SortableTableDemo() {
        ObjectConverterManager.initDefaultConverter();
        DecimalFormat format = new DecimalFormat();
        format.setMinimumFractionDigits(2);
        format.setMaximumFractionDigits(10);
        DoubleConverter converter = new DoubleConverter(format);
        ObjectConverterManager.registerConverter(Double.class, converter);
    }

    public String getName() {
        return "SortableTable Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_GRIDS;
    }

    @Override
    public String getDescription() {
        return "This is a demo of SortableTable. \n" +
                "\nClick once on the header to sort ascending, click twice to sort descending, a third time to unsort. Hold CTRL key (or Command key on Mac OS X) then click on several headers to see mulitple columns sorting.\n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.grid.SortableTable";
    }

    @Override
    public int getAttributes() {
        return ATTRIBUTE_UPDATED;
    }

    @Override
    public Component getOptionsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new JideBoxLayout(panel, JideBoxLayout.Y_AXIS));

        panel.add(new JLabel("Maximum sorted columns: "));
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        model.addElement("No maximum (default)");
        for (int i = 0; i < _sortableTable.getColumnCount(); i++) {
            model.addElement(new Integer(i + 1));
        }
        panel.add(Box.createVerticalStrut(3));
        final JComboBox maxComboBox = new JComboBox(model);
        maxComboBox.addActionListener(new AbstractAction() {
            private static final long serialVersionUID = -6480174819379988040L;

            public void actionPerformed(ActionEvent e) {
                int maxCount = maxComboBox.getSelectedIndex();
                if (maxCount == 0) {
                    ((ISortableTableModel) _sortableTable.getModel()).setMaximumSortColumns(-1);
                }
                else {
                    ((ISortableTableModel) _sortableTable.getModel()).setMaximumSortColumns(maxCount);
                }
            }
        });
        panel.add(maxComboBox);

        panel.add(Box.createVerticalStrut(6));

        panel.add(new JLabel("Sort priority: "));
        model = new DefaultComboBoxModel();
        model.addElement("FILO (Default)");
        model.addElement("FIFO");
        panel.add(Box.createVerticalStrut(3));
        final JComboBox priorityComboBox = new JComboBox(model);
        priorityComboBox.addActionListener(new AbstractAction() {
            private static final long serialVersionUID = 3712678830602409607L;

            public void actionPerformed(ActionEvent e) {
                int priority = priorityComboBox.getSelectedIndex();
                ((ISortableTableModel) _sortableTable.getModel()).setSortPriority(priority);
            }
        });
        panel.add(priorityComboBox);

        panel.add(Box.createVerticalStrut(12));

        JCheckBox sortingEnabled = new JCheckBox("Allow user to sort table");
        sortingEnabled.setSelected(_sortableTable.isSortingEnabled());
        sortingEnabled.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _sortableTable.setSortingEnabled(e.getStateChange() == ItemEvent.SELECTED);
            }
        });
        panel.add(sortingEnabled);

        JCheckBox showSortOrder = new JCheckBox("Always show sort order");
        showSortOrder.setSelected(_sortableTable.isShowSortOrderNumber());
        showSortOrder.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _sortableTable.setShowSortOrderNumber(e.getStateChange() == ItemEvent.SELECTED);
            }
        });
        panel.add(showSortOrder);

        final JCheckBox sortIconCheckBox = new JCheckBox("Use Look&Feel Default Sort Icon");
        sortIconCheckBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _sortableTable.setUseLnfDefaultSortIcon(e.getStateChange() == ItemEvent.SELECTED);
            }
        });
        sortIconCheckBox.setSelected(_sortableTable.isUseLnfDefaultSortIcon());
        panel.add(sortIconCheckBox);

        panel.add(Box.createVerticalStrut(12));

        panel.add(new JButton(new AbstractAction("Export to Excel 2003 Format") {
            private static final long serialVersionUID = -3816637828684822007L;

            public void actionPerformed(ActionEvent e) {
                _sortableTable.putClientProperty(HssfTableUtils.CLIENT_PROPERTY_EXCEL_OUTPUT_FORMAT, HssfTableUtils.EXCEL_OUTPUT_FORMAT_2003);
                if (!HssfTableUtils.isHssfInstalled()) {
                    JOptionPane.showMessageDialog((Component) e.getSource(), "Export to Excel feature is disabled because POI-HSSF jar is missing in the classpath.");
                    return;
                }
                outputToExcel(e);
            }
        }));

        panel.add(new JButton(new AbstractAction("Export to Excel 2007 Format") {
            private static final long serialVersionUID = -9178768956978016068L;

            public void actionPerformed(ActionEvent e) {
                _sortableTable.putClientProperty(HssfTableUtils.CLIENT_PROPERTY_EXCEL_OUTPUT_FORMAT, HssfTableUtils.EXCEL_OUTPUT_FORMAT_2007);
                if (!HssfTableUtils.isXssfInstalled()) {
                    JOptionPane.showMessageDialog((Component) e.getSource(), "Export to Excel 2007 feature is disabled because one or several POI-XSSF dependency jars are missing in the classpath. Please include all the jars from poi release in the classpath and try to run again.");
                    return;
                }
                outputToExcel(e);
            }
        }));
        return panel;
    }

    public Component getDemoPanel() {
        TableModel model = new SampleTableModel();

        _sortableTable = new SortableTable(model);

        JTable normalTable = new JTable(model);

        JideTabbedPane tabbedPane = new JideTabbedPane();
        tabbedPane.setTabShape(JideTabbedPane.SHAPE_BOX);
        tabbedPane.addTab("SortableTable", new JScrollPane(_sortableTable));
        tabbedPane.addTab("JTable (for comparison)", new JScrollPane(normalTable));
        tabbedPane.setPreferredSize(new Dimension(550, 400));
        return tabbedPane;
    }

    static class SampleTableModel extends AbstractTableModel implements ContextSensitiveTableModel, ToolTipSupport {
        private static final long serialVersionUID = 8798261997256893224L;

        public int getColumnCount() {
            return 6;
        }

        public int getRowCount() {
            return 8;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }

        public ConverterContext getConverterContextAt(int row, int column) {
            return null;
        }

        public EditorContext getEditorContextAt(int row, int column) {
            return null;
        }

        public Class<?> getCellClassAt(int row, int column) {
            return getColumnClass(column);
        }

        @Override
        public String getColumnName(int column) {
            switch (column) {
                case 0:
                    return "int column";
                case 1:
                    return "double column";
                case 2:
                    return "boolean column";
                case 3:
                    return "string column";
                case 4:
                    return "icon column";
                case 5:
                    return "date column";
            }
            return "";
        }

        @Override
        public Class<?> getColumnClass(int column) {
            switch (column) {
                case 0:
                    return Integer.class;
                case 1:
                    return Double.class;
                case 2:
                    return Boolean.class;
                case 3:
                    return String.class;
                case 4:
                    return Icon.class;
                case 5:
                    return Date.class;
            }
            return Object.class;
        }

        public Object getValueAt(int row, int column) {
            switch (column) {
                case 0:
                    if (row > 4) {
                        return 2;
                    }
                    else {
                        return row;
                    }
                case 1:
                    return row * 2.333333;
                case 2:
                    if (row % 2 == 0)
                        return Boolean.TRUE;
                    return Boolean.FALSE;
                case 3:
                    return "row " + (getRowCount() - row);
                case 4:
                    if (row % 2 == 0)
                        return JideIconsFactory.getImageIcon(JideIconsFactory.FileType.HTML);
                    else
                        return JideIconsFactory.getImageIcon(JideIconsFactory.FileType.JAVA);
                case 5:
                    Calendar calendar = Calendar.getInstance();
                    calendar.roll(Calendar.DAY_OF_YEAR, row);
                    return calendar.getTime();
            }
            return null;
        }

        public String getToolTipText(int columnIndex) {
            return "Click to sort this " + getColumnName(columnIndex);
        }
    }

    @Override
    public String getDemoFolder() {
        return "G2.SortableTable";
    }

    static public void main(String[] s) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new SortableTableDemo());
    }

    private void outputToExcel(ActionEvent e) {
        JFileChooser chooser = new JFileChooser() {
            @Override
            protected JDialog createDialog(Component parent) throws HeadlessException {
                JDialog dialog = super.createDialog(parent);
                dialog.setTitle("Export the content to an Excel file");
                return dialog;
            }
        };
        chooser.setCurrentDirectory(new File(_lastDirectory));
        int result = chooser.showDialog(((JButton) e.getSource()).getTopLevelAncestor(), "Export");
        if (result == JFileChooser.APPROVE_OPTION) {
            _lastDirectory = chooser.getCurrentDirectory().getAbsolutePath();
            try {
                System.out.println("Exporting to " + chooser.getSelectedFile().getAbsolutePath());
                HssfTableUtils.export(_sortableTable, chooser.getSelectedFile().getAbsolutePath(), "SortableTable", false, true, new HssfTableUtils.DefaultCellValueConverter() {
                    @Override
                    public int getDataFormat(JTable table, Object value, int rowIndex, int columnIndex) {
                        if (value instanceof Double) {
                            return 2; // use 0.00 format
                        }
                        else if (value instanceof Date) {
                            return 0xe; // use "m/d/yy" format
                        }
                        return super.getDataFormat(table, value, rowIndex, columnIndex);
                    }
                });
                System.out.println("Exported");
            }
            catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    public void dispose() {
    }
}
