/*
 * @(#)PageNavigationBarDemo.java 9/10/2008
 *
 * Copyright 2002 - 2008 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.grid.SortableTable;
import com.jidesoft.grid.TableModelWrapperUtils;
import com.jidesoft.list.ListModelWrapperUtils;
import com.jidesoft.paging.PageNavigationBar;
import com.jidesoft.paging.PageNavigationSupport;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.JideSplitPane;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Vector;
import java.util.zip.GZIPInputStream;

public class PageNavigationBarDemo extends AbstractDemo {
    private TableModel _tableModel;
    private ListModel _listModel;

    public PageNavigationBarDemo() {
    }

    public String getName() {
        return "PageNavigationBar Demo";
    }

    @Override
    public String getDemoFolder() {
        return "T1.NavigationBar";
    }

    public String getProduct() {
        return PRODUCT_NAME_DATAGRIDS;
    }

    public Component getDemoPanel() {
        readData();
        JideSplitPane pane = new JideSplitPane();
        pane.addPane(createListPanel());
        pane.addPane(createTablePanel());
        return pane;
    }

    protected Component createListPanel() {
        final JList list = new JList(_listModel != null ? _listModel : new DefaultListModel());
        list.setVisibleRowCount(20);
        final JScrollPane scroller = new JScrollPane(list);
        scroller.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int rowCount = scroller.getViewport().getHeight() / list.getCellBounds(0, 0).height;
                PageNavigationSupport pageNavigationSupport = (PageNavigationSupport) ListModelWrapperUtils.getActualListModel(list.getModel(), PageNavigationSupport.class);
                if (pageNavigationSupport != null) {
                    pageNavigationSupport.setPageSize(rowCount);
                }
            }
        });

        JPanel panel = new JPanel(new BorderLayout(2, 2));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Navigation Bar for JList"),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        panel.add(scroller);
        panel.add(new PageNavigationBar(list), BorderLayout.AFTER_LAST_LINE);
        return panel;
    }

    protected Component createTablePanel() {
        final SortableTable table = new SortableTable(_tableModel != null ? _tableModel : new DefaultTableModel());
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        final JScrollPane scroller = new JScrollPane(table);
        scroller.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            public void adjustmentValueChanged(AdjustmentEvent e) {
                table.setCellContentVisible(!e.getValueIsAdjusting());
            }
        });
        scroller.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int rowCount = scroller.getViewport().getHeight() / table.getRowHeight();
                PageNavigationSupport pageNavigationSupport = (PageNavigationSupport) TableModelWrapperUtils.getActualTableModel(table.getModel(), PageNavigationSupport.class);
                if (pageNavigationSupport != null) {
                    pageNavigationSupport.setPageSize(rowCount);
                }
            }
        });

        JPanel panel = new JPanel(new BorderLayout(2, 2));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Navigation Bar for JTable"),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        panel.add(scroller);
        PageNavigationBar pageNavigationBar = new PageNavigationBar(table);
        pageNavigationBar.setName("tablePageNavigationBar");
        panel.add(pageNavigationBar, BorderLayout.AFTER_LAST_LINE);
        return panel;
    }

    protected void readData() {
        try {
            InputStream resource = PageNavigationBarDemo.class.getClassLoader().getResourceAsStream("Library.txt.gz");
            if (resource == null) {
                return;
            }
            InputStream in = new GZIPInputStream(resource);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            Vector<Vector<Object>> tableData = new Vector();
            Vector<String> columnNames = new Vector();
            final Vector<Object> listData = new Vector();

            String columnsLine = reader.readLine(); // skip first line
            String[] columnValues = columnsLine.split("\t");
            columnNames.addAll(Arrays.asList(columnValues));
            do {
                String line = reader.readLine();
                if (line == null || line.length() == 0) {
                    break;
                }
                String[] values = line.split("\t");
                Vector<Object> lineData = new Vector();
                if (values.length < 1) {
                    lineData.add(null); // song name
                    listData.add(null); // song name
                }
                else {
                    lineData.add(values[0]); // song name
                    listData.add(values[0]); // song name
                }
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
                tableData.add(lineData);
            }
            while (true);
            _tableModel = new DefaultTableModel(tableData, columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return true;
                }
            };
            _listModel = new AbstractListModel() {
                public int getSize() {
                    return listData.size();
                }

                public Object getElementAt(int index) {
                    return listData.get(index);
                }
            };
        }
        catch (IOException e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
    }

    static public void main(String[] s) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new PageNavigationBarDemo());
    }
}