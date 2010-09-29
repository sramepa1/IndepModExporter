/*
 * @(#)QuickFilterDemo.java 6/20/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.grid.*;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.JideSplitPane;
import com.jidesoft.swing.JideTitledBorder;
import com.jidesoft.swing.PartialEtchedBorder;
import com.jidesoft.swing.PartialSide;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Vector;
import java.util.zip.GZIPInputStream;

/**
 * Demoed Component: {@link com.jidesoft.grid.SortableTable} <br> Required jar files: jide-common.jar, jide-grids.jar
 * <br> Required L&F: any L&F
 */
public class QuickFilterTableDemo extends AbstractDemo {
    private static final long serialVersionUID = 6209431674990004185L;

    private QuickFilterPane _quickFilterPane;
    private SortableTable _sortableTable;

    public QuickFilterTableDemo() {
    }

    public String getName() {
        return "QuickFilter (Table) Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_GRIDS;
    }

    @Override
    public String getDescription() {
        return "This is a demo of QuickFilterPane and QuickTableFilterField. Both are implemented on top of FilterableTableModel. You can use one of them or both and make the huge data browsing a very easy task for your users.\n" +
                "\nIn this demo, we created a large song list with over 10000 songs. If you try to use it, you will find how fast and easy it is to find the song you want.\n" +
                "\nType in any text in the QuickTableFilterField. You will see the table will only show the rows that matches. If you click on the icon of QuickTableFilterField, you can choose which columns to search.\n" +
                "\nOr you can click on the lists of QuickFilterPane. Each list will filter away the rows that don't match so that you can easily find the exact data you are looking for. You can type in directly the lists to quickly navigate " +
                "to the item you are looking for (the Searchable feature in JIDE). You can also do multiple selection on those lists.\n" +
                "\nAll the cells in the table is editable. So you can change the data in the table randomly to see the lists in QuickFilterPane updated automatically.\n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.grid.FilterableTableModel\n" +
                "com.jidesoft.grid.QuickTableFilterField\n" +
                "com.jidesoft.grid.QuickFilterPane";
    }

    public Component getOptionsPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 1, 6, 6));
        final JCheckBox checkBox = new JCheckBox("Use CheckBoxLists in QuickFilterPane");
        checkBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _quickFilterPane.setUseCheckBoxList(checkBox.isSelected());
            }
        });
        panel.add(checkBox);

        JCheckBox showSortOrder = new JCheckBox("Always show sort order on table");
        showSortOrder.setSelected(_sortableTable.isShowSortOrderNumber());
        showSortOrder.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _sortableTable.setShowSortOrderNumber(e.getStateChange() == ItemEvent.SELECTED);
            }
        });
        panel.add(showSortOrder);

        return panel;
    }

    public Component getDemoPanel() {
        final TableModel tableModel = createTableModel();
        if (tableModel == null) {
            return new JLabel("Failed to read Library.txt.gz");
        }

        _quickFilterPane = new QuickFilterPane(tableModel, getFilterPaneColumnIndices());

        JPanel quickSearchPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        QuickTableFilterField filterField = new QuickTableFilterField(_quickFilterPane.getDisplayTableModel(), new int[]{1, 2, 0, 3, 5});
        filterField.setHintText("Type here to filter songs");
// uncomment to disable auto filter when typing
//        filterField.setSearchingDelay(-1);
        quickSearchPanel.add(filterField);
        quickSearchPanel.setBorder(new JideTitledBorder(new PartialEtchedBorder(PartialEtchedBorder.LOWERED, PartialSide.NORTH), "QuickTableFilterField", JideTitledBorder.LEADING, JideTitledBorder.ABOVE_TOP));

        JideSplitPane pane = new JideSplitPane(JideSplitPane.VERTICAL_SPLIT);
        _quickFilterPane.setBorder(BorderFactory.createCompoundBorder(new JideTitledBorder(new PartialEtchedBorder(PartialEtchedBorder.LOWERED, PartialSide.NORTH), "QuickFilterPane", JideTitledBorder.LEADING, JideTitledBorder.ABOVE_TOP),
                BorderFactory.createEmptyBorder(6, 0, 0, 0)));
        pane.addPane(_quickFilterPane);

        JPanel tablePanel = new JPanel(new BorderLayout(2, 2));
        tablePanel.setBorder(BorderFactory.createCompoundBorder(new JideTitledBorder(new PartialEtchedBorder(PartialEtchedBorder.LOWERED, PartialSide.NORTH), "Filtered Song List", JideTitledBorder.LEADING, JideTitledBorder.ABOVE_TOP),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)));
        final JLabel label = new JLabel(filterField.getDisplayTableModel().getRowCount() + " out of " + tableModel.getRowCount() + " songs");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));

        _sortableTable = createTable(filterField.getDisplayTableModel());
        _sortableTable.setColumnAutoResizable(true);
        _sortableTable.setAutoResizeMode(JideTable.AUTO_RESIZE_FILL);
        _sortableTable.setTableStyleProvider(new RowStripeTableStyleProvider());

        setupTableColumnWidth(_sortableTable);

        filterField.setTable(_sortableTable); // this is not necessary but with this call, the selection will be preserved when filter changes.

        JScrollPane scrollPane = new JScrollPane(_sortableTable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setCorner(JScrollPane.UPPER_RIGHT_CORNER, TableColumnChooser.getTableColumnChooserButton(_sortableTable,
                new int[]{0}, new String[]{"Song Name", "Artist of the Song", "Album of the Song", "Genre of the Song", "Duration of the Song", "Year when the Song is published",}));
        filterField.getDisplayTableModel().addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                if (e.getSource() instanceof TableModel) {
                    int count = ((TableModel) e.getSource()).getRowCount();
                    label.setText(count + " out of " + tableModel.getRowCount() + " songs");
                }
            }
        });

        tablePanel.add(label, BorderLayout.BEFORE_FIRST_LINE);
        tablePanel.add(scrollPane);
        pane.addPane(tablePanel);

        JPanel panel = new JPanel(new BorderLayout(3, 3));
        panel.add(quickSearchPanel, BorderLayout.BEFORE_FIRST_LINE);
        panel.add(pane);

        return panel;
    }

    protected SortableTable createTable(TableModel tableModel) {
        return new SortableTable(tableModel);
    }

    protected int[] getFilterPaneColumnIndices() {
        return new int[]{3, 1, 2};
    }

    protected void setupTableColumnWidth(SortableTable sortableTable) {
        sortableTable.getColumnModel().getColumn(0).setPreferredWidth(200);
        sortableTable.getColumnModel().getColumn(4).setPreferredWidth(60);
        sortableTable.getColumnModel().getColumn(5).setPreferredWidth(60);
    }

    protected TableModel createTableModel() {
        try {
            InputStream resource = this.getClass().getClassLoader().getResourceAsStream("Library.txt.gz");
            if (resource == null) {
                return null;
            }
            InputStream in = new GZIPInputStream(resource);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            Vector<Vector<Object>> data = new Vector<Vector<Object>>();
            Vector<String> columnNames = new Vector<String>();

            String columnsLine = reader.readLine(); // skip first line
            String[] columnValues = columnsLine.split("\t");
            columnNames.addAll(Arrays.asList(columnValues));
            do {
                String line = reader.readLine();
                if (line == null || line.length() == 0) {
                    break;
                }
                String[] values = line.split("\t");
                Vector<Object> lineData = new Vector<Object>();
                if (values.length < 1)
                    lineData.add(null); // song name
                else
                    lineData.add(values[0]); // song name
                if (values.length < 2)
                    lineData.add(null); // artist
                else
                    lineData.add(values[1]); // artist
                if (values.length < 3)
                    lineData.add(null); // album
                else
                    lineData.add(values[2]); // album
                if (values.length < 4)
                    lineData.add(null); // genre
                else
                    lineData.add(values[3]); // genre
                if (values.length < 5)
                    lineData.add(null); // time
                else
                    lineData.add(values[4]); // time
                if (values.length < 6)
                    lineData.add(null); // year
                else
                    lineData.add(values[5]); // year
                data.add(lineData);
            }
            while (true);
            return new DefaultTableModel(data, columnNames) {
                private static final long serialVersionUID = -7688454714895438279L;

                @Override
                public boolean isCellEditable(int row, int column) {
                    return true;
                }
            };
        }
        catch (IOException e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getDemoFolder() {
        return "G15.QuickFilter";
    }

    static public void main(String[] s) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new QuickFilterTableDemo());
    }

}
