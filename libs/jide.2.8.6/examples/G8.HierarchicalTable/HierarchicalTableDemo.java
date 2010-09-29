/*
 * @(#)HierarchicalTableDemo.java 2/14/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.dialog.ButtonPanel;
import com.jidesoft.grid.*;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.JideSwingUtilities;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseWheelListener;
import java.util.Arrays;

/**
 * Demoed Component: {@link com.jidesoft.grid.HierarchicalTable} <br> Required jar files: jide-common.jar,
 * jide-grids.jar <br> Required L&F: Jide L&F extension required
 */
public class HierarchicalTableDemo extends AbstractDemo {

    protected static final Color BG1 = new Color(232, 237, 230);
    protected static final Color BG2 = new Color(243, 234, 217);
    protected static final Color BG3 = new Color(214, 231, 247);

    private HierarchicalTable _table;
    private DefaultTableModel _productsTableModel;

    private ListSelectionModelGroup _group = new ListSelectionModelGroup();
    private static final long serialVersionUID = 436895008521368975L;

    public HierarchicalTableDemo() {
    }

    public String getName() {
        return "HierarchicalTable (Nested Table) Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_GRIDS;
    }

    @Override
    public String getDescription() {
        return "This is a demo of HierarchicalTable. HierarchicalTable is a JTable which supports nested components for each row.\n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.grid.HierarchicalTable\n" +
                "com.jidesoft.grid.HierarchicalTableModel\n" +
                "com.jidesoft.grid.ListSelectionModelGroup";
    }

    @Override
    public Component getOptionsPanel() {
        ButtonPanel buttonPanel = new ButtonPanel(SwingConstants.TOP);
        JButton button = new JButton(new AbstractAction("Insert a row") {
            private static final long serialVersionUID = 7679251172444946388L;

            public void actionPerformed(ActionEvent e) {
                _productsTableModel.insertRow(0, new Object[]{"JIDE Action Framework", "Feature drag-n-dropable toolbar/menu bar components"});
            }
        });
        button.setRequestFocusEnabled(false);
        buttonPanel.addButton(button);

        button = new JButton(new AbstractAction("Delete selected rows") {
            private static final long serialVersionUID = -2674554471570939607L;

            public void actionPerformed(ActionEvent e) {
                int[] rows = _table.getSelectedRows();
                for (int i = 0; i < rows.length; i++) {
                    int row = rows[i];
                    rows[i] = TableModelWrapperUtils.getActualRowAt(_table.getModel(), row, HierarchicalTableModel.class);
                }
                Arrays.sort(rows);
                for (int i = rows.length - 1; i >= 0; i--) {
                    int row = rows[i];
                    _productsTableModel.removeRow(row);
                }
            }
        });
        button.setRequestFocusEnabled(false);
        buttonPanel.addButton(button);

        button = new JButton(new AbstractAction("Update a cell") {
            private static final long serialVersionUID = 6964633308175358313L;

            public void actionPerformed(ActionEvent e) {
                _productsTableModel.setValueAt("Collection of useful components include tabbed document interface, status bar, floor tabbed pane and collapsible pane.", 1, 1);
            }
        });
        button.setRequestFocusEnabled(false);
        buttonPanel.addButton(button);

        button = new JButton(new AbstractAction("Remove all rows") {
            private static final long serialVersionUID = -5697917003911973530L;

            public void actionPerformed(ActionEvent e) {
                for (int i = _productsTableModel.getRowCount() - 1; i >= 0; i--) {
                    _productsTableModel.removeRow(i);
                }
            }
        });
        button.setRequestFocusEnabled(false);
        buttonPanel.addButton(button);
        return buttonPanel;
    }

    public Component getDemoPanel() {
        _table = createTable();
        JScrollPane scrollPane = new JScrollPane(_table);
        scrollPane.getViewport().putClientProperty("HierarchicalTable.mainViewport", Boolean.TRUE);
        return scrollPane;
    }

    @Override
    public String getDemoFolder() {
        return "G8.HierarchicalTable";
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new HierarchicalTableDemo());
    }

    // create property table
    private HierarchicalTable createTable() {
        _productsTableModel = new ProductTableModel();
        HierarchicalTable table = new HierarchicalTable();
        table.setAutoRefreshOnRowUpdate(false);
        table.setModel(_productsTableModel);
        table.setBackground(BG1);
        table.setName("Product Table");
        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setComponentFactory(new HierarchicalTableComponentFactory() {
            public Component createChildComponent(HierarchicalTable table, Object value, int row) {
                if (value == null) {
                    return new JPanel();
                }
                TableModel model = ((TableModel) value);
                if (model instanceof HierarchicalTableModel) {
                    HierarchicalTable childTable = new HierarchicalTable(model) {
                        @Override
                        public void scrollRectToVisible(Rectangle aRect) {
                            HierarchicalTableDemo.scrollRectToVisible(this, aRect);
                        }
                    };
                    childTable.setBackground(BG2);
                    childTable.setOpaque(true);
                    childTable.setName("Detail Table");
                    childTable.setComponentFactory(new HierarchicalTableComponentFactory() {
                        public Component createChildComponent(HierarchicalTable table, Object value, int row) {
                            if (value instanceof TableModel) {
                                TableModel model = ((TableModel) value);
                                SortableTable sortableTable = new SortableTable(model) {
                                    @Override
                                    public void scrollRectToVisible(Rectangle aRect) {
                                        HierarchicalTableDemo.scrollRectToVisible(this, aRect);
                                    }
                                };
                                FitScrollPane pane = new FitScrollPane(sortableTable);
                                sortableTable.setBackground(BG3);
                                _group.add(sortableTable.getSelectionModel());
                                TreeLikeHierarchicalPanel treeLikeHierarchicalPanel = new TreeLikeHierarchicalPanel(pane);
                                treeLikeHierarchicalPanel.setBackground(sortableTable.getMarginBackground());
                                return treeLikeHierarchicalPanel;
                            }
                            return null;
                        }

                        public void destroyChildComponent(HierarchicalTable table, Component component, int row) {
                            Component t = JideSwingUtilities.getFirstChildOf(JTable.class, component);
                            if (t instanceof JTable) {
                                _group.remove(((JTable) t).getSelectionModel());
                            }
                        }
                    });
                    _group.add(childTable.getSelectionModel());
                    TreeLikeHierarchicalPanel treeLikeHierarchicalPanel = new TreeLikeHierarchicalPanel(new FitScrollPane(childTable));
                    treeLikeHierarchicalPanel.setBackground(childTable.getMarginBackground());
                    return treeLikeHierarchicalPanel;
                }
                else {
                    SortableTable sortableTable = new SortableTable(model) {
                        @Override
                        public void scrollRectToVisible(Rectangle aRect) {
                            HierarchicalTableDemo.scrollRectToVisible(this, aRect);
                        }
                    };
                    sortableTable.setBackground(BG2);
                    _group.add(sortableTable.getSelectionModel());
                    TreeLikeHierarchicalPanel treeLikeHierarchicalPanel = new TreeLikeHierarchicalPanel(new FitScrollPane(sortableTable));
                    treeLikeHierarchicalPanel.setBackground(sortableTable.getMarginBackground());
                    return treeLikeHierarchicalPanel;
                }
            }

            public void destroyChildComponent(HierarchicalTable table, Component component, int row) {
                Component t = JideSwingUtilities.getFirstChildOf(JTable.class, component);
                if (t instanceof JTable) {
                    _group.remove(((JTable) t).getSelectionModel());
                }
            }
        });
        _group.add(table.getSelectionModel());
        return table;
    }


    static String[] DETAIL_COLUMNS = new String[]{"Feature", "Description"};
    static String[] PRODUCT_COLUMNS = new String[]{"Name", "Description"};

    static String[][] JIDE_PRODUCTS = new String[][]{
            new String[]{"JIDE Docking Framework", "Swing-based GUI Framework which enables drag-n-drop dockable windows"},
            new String[]{"JIDE Components", "Collection of useful components"},
            new String[]{"JIDE Grids", "Collection of JTable related components"},
            new String[]{"JIDE Dialogs", "Collection of JDialog related components",}
    };

    static String[][] JIDE_DOCK = new String[][]{
            new String[]{"Drag-n-drop", ""},
            new String[]{"Autohide Windows", ""},
            new String[]{"Nested Floating Windows", ""},
            new String[]{"Persist Layouts", ""}
    };

    static String[][] JIDE_ACTION = new String[][]{
            new String[]{"Drag-n-drop command bars", ""},
            new String[]{"Floating command bars", ""},
            new String[]{"Persist Layouts", ""}
    };

    static String[][] JIDE_COMP = new String[][]{
            new String[]{"DocumentPane", "Drag-n-drop tabbed document interface as you see in modern IDEs etc"},
            new String[]{"Status Bar", "Full customizable status bar that you can use to display status, progress, time and other information at the bottom of your application"},
            new String[]{"OutlookTabbedPane", "Shortcut Bar, just like in Outlook 2003"},
            new String[]{"CollapsiblePane", "Task Bar which you can find in Windows XP"}
    };

    static String[][] JIDE_GRIDS = new String[][]{
            new String[]{"PropertyTable", "A two-column JTable used to display properties of any object"},
            new String[]{"SortableTable", "A JTable which allows multiple-column sorting"},
            new String[]{"FilterTableModel", "A table model which allows filters on each column"},
            new String[]{"HierarchicalTable", "A JTable which allows hierarchical display of components on each row"},
            new String[]{"ComboBox", "A collection of ComboBoxes"},
            new String[]{"CellEditors", "A collection of CellEditor for many types of objects"},
            new String[]{"Converters", "A list of converters which can convert String to/from any other types"},
            new String[]{"TableUtils", "A utility class which contains several useful methods for JTable"}
    };

    static String[][] JIDE_COMBOBOXES = new String[][]{
            new String[]{"ColorComboBox", "A ComboBox to choose color"},
            new String[]{"DateComboBox", "A ComboBox to choose date"},
            new String[]{"MonthComboBox", "A ComboBox to choose month"},
            new String[]{"FileChooserComboBox", "A ComboBox to choose file"},
            new String[]{"FontComboBox", "A ComboBox to choose font"},
            new String[]{"ListComboBox", "A ComboBox to choose an item from a list"},
            new String[]{"BooleanComboBox", "A ComboBox to choose true or false"},
            new String[]{"StringArrayComboBox", "A ComboBox to choose String[]"}
    };

    static String[][] JIDE_CELLEDITORS = new String[][]{
            new String[]{"ColorCellEditor", "A cell editor to choose a color"},
            new String[]{"DateCellEditor", "A cell editor to choose a date"},
            new String[]{"MonthCellEditor", "A cell editor to choose a month"},
            new String[]{"FileNameCellEditor", "A cell editor to choose file name"},
            new String[]{"DoubleCellEditor", "A cell editor to edit double"},
            new String[]{"IntegerCellEditor", "A cell editor to edit int"},
            new String[]{"NumberCellEditor", "A cell editor to edit number"},
            new String[]{"ListComboBoxCellEditor", "A cell editor to choose an item from a list"},
            new String[]{"BooleanCellEditor", "A cell editor to choose true or false"},
            new String[]{"FontNameCellEditor", "A cell editor to choose a font name"},
            new String[]{"StringArrayCellEditor", "A cell editor to choose a String[]"}
    };

    static class GridsProductTableModel extends DefaultTableModel implements HierarchicalTableModel {
        private static final long serialVersionUID = 348567038622072738L;

        public GridsProductTableModel() {
            super(JIDE_GRIDS, DETAIL_COLUMNS);
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }

        public boolean hasChild(int row) {
            Object name = getValueAt(row, 0);
            return "CellEditors".equals(name) || "ComboBox".equals(name);
        }

        public boolean isHierarchical(int row) {
            return true;
        }

        public boolean isExpandable(int row) {
            return true;
        }

        public Object getChildValueAt(int row) {
            TableModel model = null;
            Object name = getValueAt(row, 0);
            if ("ComboBox".equals(name)) {
                model = new DefaultTableModel(JIDE_COMBOBOXES, DETAIL_COLUMNS) {
                    private static final long serialVersionUID = -1904217823204838470L;

                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };
            }
            else if ("CellEditors".equals(name)) {
                model = new DefaultTableModel(JIDE_CELLEDITORS, DETAIL_COLUMNS) {
                    private static final long serialVersionUID = -7946879244547513550L;

                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };
            }
            return model;
        }
    }

    static String[][] JIDE_DIALOGS = new String[][]{
            new String[]{"StandardDialog", "A common dialog class which implements some common features that all dialogs will need"},
            new String[]{"Lazy-loading Page", "Lazy loading panel with page event (open, closing, closed etc) support"},
            new String[]{"Wizard", "Wizard component which support both Microsoft Wizard 97 standard and Java L&F standard"},
            new String[]{"MultiplePageDialog", "A dialog contains many pages which can be used as options dialog or user preference dialog"},
            new String[]{"ButtonPanel", "Layout buttons in different order based OS convention"},
            new String[]{"BannerPanel", "A banner panel mainly for decoration purpose"},
            new String[]{"Tips of the Day Dialog", "A dialog shows tips of the day"}
    };

    static class ProductTableModel extends DefaultTableModel implements HierarchicalTableModel {
        private static final long serialVersionUID = -8165093837937149049L;

        public ProductTableModel() {
            super(JIDE_PRODUCTS, PRODUCT_COLUMNS);
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return row == 5;
        }

        public boolean hasChild(int row) {
            return true;
        }

        public boolean isExpandable(int row) {
            return row != 5;
        }

        public boolean isHierarchical(int row) {
            return true;
        }

        public Object getChildValueAt(int row) {
            TableModel model = null;
            Object name = getValueAt(row, 0);
            if ("JIDE Docking Framework".equals(name)) {
                model = new DefaultTableModel(JIDE_DOCK, DETAIL_COLUMNS) {
                    private static final long serialVersionUID = 3772392541085322455L;

                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };
            }
            else if ("JIDE Components".equals(name)) {
                model = new DefaultTableModel(JIDE_COMP, DETAIL_COLUMNS) {
                    private static final long serialVersionUID = 5945811427299234203L;

                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };
            }
            else if ("JIDE Grids".equals(name)) {
                model = new HierarchicalTableDemo.GridsProductTableModel();
            }
            else if ("JIDE Dialogs".equals(name)) {
                model = new DefaultTableModel(JIDE_DIALOGS, DETAIL_COLUMNS) {
                    private static final long serialVersionUID = -5324821889052002150L;

                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };
            }
            else if ("JIDE Action Framework".equals(name)) {
                model = new DefaultTableModel(JIDE_ACTION, DETAIL_COLUMNS) {
                    private static final long serialVersionUID = 1130973794297964752L;

                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };
            }
            return model;
        }
    }

    static class FitScrollPane extends JScrollPane implements ComponentListener {
        public FitScrollPane() {
            initScrollPane();
        }

        public FitScrollPane(Component view) {
            super(view);
            initScrollPane();
        }

        public FitScrollPane(Component view, int vsbPolicy, int hsbPolicy) {
            super(view, vsbPolicy, hsbPolicy);
            initScrollPane();
        }

        public FitScrollPane(int vsbPolicy, int hsbPolicy) {
            super(vsbPolicy, hsbPolicy);
            initScrollPane();
        }

        private void initScrollPane() {
            setBorder(BorderFactory.createLineBorder(Color.GRAY));
            setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
            getViewport().getView().addComponentListener(this);
            removeMouseWheelListeners();
        }

        // remove MouseWheelListener as there is no need for it in FitScrollPane.
        private void removeMouseWheelListeners() {
            MouseWheelListener[] listeners = getMouseWheelListeners();
            for (MouseWheelListener listener : listeners) {
                removeMouseWheelListener(listener);
            }
        }

        @Override
        public void updateUI() {
            super.updateUI();
            removeMouseWheelListeners();
        }

        public void componentResized(ComponentEvent e) {
            setSize(getSize().width, getPreferredSize().height);
        }

        public void componentMoved(ComponentEvent e) {
        }

        public void componentShown(ComponentEvent e) {
        }

        public void componentHidden(ComponentEvent e) {
        }

        @Override
        public Dimension getPreferredSize() {
            getViewport().setPreferredSize(getViewport().getView().getPreferredSize());
            return super.getPreferredSize();
        }
    }

    public static void scrollRectToVisible(Component component, Rectangle aRect) {
        Container parent;
        int dx = component.getX(), dy = component.getY();

        for (parent = component.getParent();
             parent != null && (!(parent instanceof JViewport) || (((JViewport) parent).getClientProperty("HierarchicalTable.mainViewport") == null));
             parent = parent.getParent()) {
            Rectangle bounds = parent.getBounds();

            dx += bounds.x;
            dy += bounds.y;
        }

        if (parent != null) {
            aRect.x += dx;
            aRect.y += dy;

            ((JComponent) parent).scrollRectToVisible(aRect);
            aRect.x -= dx;
            aRect.y -= dy;
        }
    }

}
