/*
 * @(#)FileSystemTreeTableDemo.java 6/28/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.grid.SortableTreeTableModel;
import com.jidesoft.grid.TreeTable;
import com.jidesoft.grid.TreeTableModel;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.TableSearchable;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Demoed Component: {@link com.jidesoft.grid.TreeTable} <br> Required jar files: jide-common.jar, jide-grids.jar <br>
 * Required L&F: any L&F
 */
public class FileSystemTreeTableDemo extends AbstractDemo {
    public TreeTable _table;
    protected AbstractTableModel _tableModel;
    protected int _pattern;

    public FileSystemTreeTableDemo() {
    }

    public String getName() {
        return "TreeTable (File System)";
    }

    public String getProduct() {
        return PRODUCT_NAME_GRIDS;
    }

    @Override
    public String getDescription() {
        return "This is a tree view file system using TreeTable component.\n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.grid.TreeTableModel\n" +
                "com.jidesoft.grid.TreeTable";
    }

    static final TableCellRenderer FILE_RENDERER = new FileRowCellRenderer();
    static final TableCellRenderer FILE_SIZE_RENDERER = new FileSizeCellRenderer();
    static final TableCellRenderer FILE_DATE_RENDERER = new FileDateCellRenderer();

    public Component getDemoPanel() {
        _tableModel = createTableModel();
        _table = new TreeTable(_tableModel);
        _table.setSortable(true);
// you can also set different sortable option on each column.        
//        ((SortableTreeTableModel) _table.getModel()).setSortableOption(1, SortableTreeTableModel.SORTABLE_NONE);
//        ((SortableTreeTableModel) _table.getModel()).setSortableOption(2, SortableTreeTableModel.SORTABLE_LEAF_LEVEL);

        // configure the TreeTable
        _table.setExpandAllAllowed(false);
        _table.setRowHeight(18);
        _table.setShowTreeLines(true);
        _table.setShowGrid(false);
        _table.setIntercellSpacing(new Dimension(0, 0));

        // do not select row when expanding a row.
        _table.setSelectRowWhenToggling(false);

        _table.getColumnModel().getColumn(0).setPreferredWidth(300);
        _table.getColumnModel().getColumn(1).setPreferredWidth(100);
        _table.getColumnModel().getColumn(2).setPreferredWidth(100);
        _table.getColumnModel().getColumn(3).setPreferredWidth(100);

        _table.getColumnModel().getColumn(0).setCellRenderer(FILE_RENDERER);
        _table.getColumnModel().getColumn(1).setCellRenderer(FILE_SIZE_RENDERER);
        _table.getColumnModel().getColumn(3).setCellRenderer(FILE_DATE_RENDERER);

        // add searchable feature
        TableSearchable searchable = new TableSearchable(_table) {
            @Override
            protected String convertElementToString(Object item) {
                if (item instanceof FileRow) {
                    return ((FileRow) item).getName();
                }
                return super.convertElementToString(item);
            }
        };
        searchable.setMainIndex(0); // only search for name column

        JScrollPane scrollPane = new JScrollPane(_table);
        scrollPane.getViewport().setBackground(Color.WHITE);

        JPanel panel = new JPanel(new BorderLayout(6, 6));
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(new JLabel("Local File System: "), BorderLayout.BEFORE_FIRST_LINE);
        panel.setPreferredSize(new Dimension(700, 500));
        return panel;
    }

    private TreeTableModel createTableModel() {
        File[] roots = FileSystemView.getFileSystemView().getRoots();
        List<FileRow> rootList = new ArrayList();
        for (File root : roots) {
            rootList.add(new FileRow(root));
        }
        return new FileSystemTableModel(rootList);
    }

    @Override
    public String getDemoFolder() {
        return "G18.TreeTable";
    }

    @Override
    public String[] getDemoSource() {
        return new String[]{"FileSystemTreeTableDemo.java", "FileSystemTableModel.java", "FileRow.java", "FileRowCellRenderer.java", "FileSizeCellRenderer.java"};
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new FileSystemTreeTableDemo());
    }

    @Override
    public Component getOptionsPanel() {
        final JRadioButton style1 = new JRadioButton("Sort leaf level only");
        final JRadioButton style2 = new JRadioButton("Sort root level only");
        final JRadioButton style3 = new JRadioButton("Sort non-root levels");
        final JRadioButton style4 = new JRadioButton("Sort all levels");
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(style1);
        buttonGroup.add(style2);
        buttonGroup.add(style3);
        buttonGroup.add(style4);
        JPanel switchPanel = new JPanel(new GridLayout(0, 1, 3, 3));
        switchPanel.add(style1);
        switchPanel.add(style2);
        switchPanel.add(style3);
        switchPanel.add(style4);
        style4.setSelected(true);

        style1.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (style1.isSelected()) {
                    ((SortableTreeTableModel) _table.getModel()).setDefaultSortableOption(SortableTreeTableModel.SORTABLE_LEAF_LEVEL);
                }
            }
        });
        style2.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (style2.isSelected()) {
                    ((SortableTreeTableModel) _table.getModel()).setDefaultSortableOption(SortableTreeTableModel.SORTABLE_ROOT_LEVEL);
                }
            }
        });
        style3.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (style3.isSelected()) {
                    ((SortableTreeTableModel) _table.getModel()).setDefaultSortableOption(SortableTreeTableModel.SORTABLE_NON_ROOT_LEVEL);
                }
            }
        });
        style4.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (style4.isSelected()) {
                    ((SortableTreeTableModel) _table.getModel()).setDefaultSortableOption(SortableTreeTableModel.SORTABLE_ALL_LEVELS);
                }
            }
        });

        final JCheckBox showTreeLine = new JCheckBox("Show tree line");
        showTreeLine.setSelected(_table.isShowTreeLines());
        showTreeLine.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _table.setShowTreeLines(showTreeLine.isSelected());
            }
        });
        switchPanel.add(showTreeLine);

        final JCheckBox showLeafNodeTreeLines = new JCheckBox("Show leaf node tree lines");
        showLeafNodeTreeLines.setSelected(_table.isShowLeafNodeTreeLines());
        showLeafNodeTreeLines.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _table.setShowLeafNodeTreeLines(showLeafNodeTreeLines.isSelected());
            }
        });
        switchPanel.add(showLeafNodeTreeLines);

        final JCheckBox expandIconVisible = new JCheckBox("Show tree icons");
        expandIconVisible.setSelected(_table.isExpandIconVisible());
        expandIconVisible.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _table.setExpandIconVisible(expandIconVisible.isSelected());
            }
        });
        switchPanel.add(expandIconVisible);

        final JCheckBox expandable = new JCheckBox("Row expandable/collapsible");
        expandable.setSelected(_table.isExpandable());
        expandable.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _table.setExpandable(expandable.isSelected());
            }
        });
        switchPanel.add(expandable);

        final JButton expand1Button = new JButton("Add new row");
        expand1Button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (_tableModel instanceof TreeTableModel) {
                    ((TreeTableModel) _tableModel).addRow(new FileRow(new File("C:\\Program Files")));
                }
            }
        });
        switchPanel.add(expand1Button);

        final JButton expand2Button = new JButton("Delete selected row");
        expand2Button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selected = _table.getSelectedRow();
                if (selected != -1) {
                    ((TreeTableModel) _tableModel).removeRow(selected);
                }
            }
        });
        switchPanel.add(expand2Button);

        switchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        return switchPanel;
    }
}

