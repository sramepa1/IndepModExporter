/*
 * @(#)CheckBoxTreeDemo.java 9/10/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.icons.IconsFactory;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.*;
import com.jidesoft.tree.TreeUtils;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;

/**
 * Demoed Component: {@link com.jidesoft.swing.CheckBoxTree} <br> Required jar files: jide-common.jar, jide-grids.jar
 * <br> Required L&F: any L&F
 */
public class CheckBoxTreeDemo extends AbstractDemo {
    private CheckBoxTree _tree;

    private static final String SINGLE_SELECTION = "Single Selection";
    private static final String CONTIGUOUS_SELECTION = "Contiguous Selection";
    private static final String DISCONTIGUOUS_SELECTION = "Discontiguous Selection";

    public CheckBoxTreeDemo() {
    }

    public String getName() {
        return "CheckBoxTree Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_COMMON;
    }

    @Override
    public String getDescription() {
        return "This is a demo of CheckBoxTree. \n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.swing.CheckBoxTree";
    }

    private TreeModel createTreeModel() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("All Albums");
        DefaultTreeModel treeModel = new DefaultTreeModel(root);
        HashMap<String, DefaultMutableTreeNode> albums = new HashMap<String, DefaultMutableTreeNode>();

        try {
            InputStream resource = this.getClass().getClassLoader().getResourceAsStream("Library.txt.gz");
            if (resource == null) {
                return null;
            }
            InputStream in = new GZIPInputStream(resource);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            reader.readLine(); // skip first line
            do {
                String line = reader.readLine();
                if (line == null || line.length() == 0) {
                    break;
                }
                String[] values = line.split("\t");
                String songName = "";
                String albumName = "";
                if (values.length >= 1) {
                    songName = values[0];
                }
                if (values.length >= 2) {
                    songName += " - " + values[1];
                }
                if (values.length >= 3) {
                    albumName = values[2]; // artist
                }

                DefaultMutableTreeNode treeNode = albums.get(albumName);
                if (treeNode == null) {
                    treeNode = new DefaultMutableTreeNode(albumName);
                    albums.put(albumName, treeNode);
                    root.add(treeNode);
                }
                treeNode.add(new DefaultMutableTreeNode(songName));
            }
            while (true);
            return treeModel;
        }
        catch (IOException e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Component getOptionsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new JideBoxLayout(panel, BoxLayout.Y_AXIS, 3));

        final JButton selectAll = new JButton(new AbstractAction("Select All") {
            public void actionPerformed(ActionEvent e) {
                if (_tree.getCheckBoxTreeSelectionModel().isDigIn()) {
                    _tree.getCheckBoxTreeSelectionModel().setSelectionPath(new TreePath(_tree.getModel().getRoot()));
                }
            }
        });
        JButton clearAll = new JButton(new AbstractAction("Clear All") {
            public void actionPerformed(ActionEvent e) {
                _tree.getCheckBoxTreeSelectionModel().clearSelection();
            }
        });

        final JCheckBox digIn = new JCheckBox("Dig In");
        digIn.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                _tree.getCheckBoxTreeSelectionModel().setDigIn(digIn.isSelected());
                selectAll.setEnabled(digIn.isSelected());
            }
        });
        digIn.setSelected(_tree.getCheckBoxTreeSelectionModel().isDigIn());

        final JCheckBox checkBoxEnabled = new JCheckBox("CheckBox Enabled");
        checkBoxEnabled.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                _tree.setCheckBoxEnabled(checkBoxEnabled.isSelected());
            }
        });
        checkBoxEnabled.setSelected(_tree.isCheckBoxEnabled());

        final JCheckBox clickInCheckBoxOnly = new JCheckBox("Click only valid in CheckBox");
        clickInCheckBoxOnly.addActionListener(new AbstractAction() {
            private static final long serialVersionUID = 5234198740430142668L;

            public void actionPerformed(ActionEvent e) {
                _tree.setClickInCheckBoxOnly(clickInCheckBoxOnly.isSelected());
            }
        });
        clickInCheckBoxOnly.setSelected(_tree.isClickInCheckBoxOnly());

        final JCheckBox treeEnabled = new JCheckBox("Tree Enabled ");
        treeEnabled.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                _tree.setEnabled(treeEnabled.isSelected());
            }
        });
        treeEnabled.setSelected(_tree.isEnabled());

        String[] selectionModes = new String[]{
                CheckBoxTreeDemo.SINGLE_SELECTION,
                CheckBoxTreeDemo.CONTIGUOUS_SELECTION,
                CheckBoxTreeDemo.DISCONTIGUOUS_SELECTION
        };

        JComboBox comboBox = new JComboBox(selectionModes);
        comboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED && e.getItem() instanceof String) {
                    if ((e.getItem()).equals(CheckBoxTreeDemo.SINGLE_SELECTION)) {
                        _tree.getCheckBoxTreeSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
                    }
                    else if ((e.getItem()).equals(CheckBoxTreeDemo.CONTIGUOUS_SELECTION)) {
                        _tree.getCheckBoxTreeSelectionModel().setSelectionMode(TreeSelectionModel.CONTIGUOUS_TREE_SELECTION);
                    }
                    else {
                        _tree.getCheckBoxTreeSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
                    }
                }
            }
        });
        int mode = _tree.getCheckBoxTreeSelectionModel().getSelectionMode();
        switch (mode) {
            case TreeSelectionModel.SINGLE_TREE_SELECTION:
                comboBox.setSelectedIndex(0);
                break;
            case TreeSelectionModel.CONTIGUOUS_TREE_SELECTION:
                comboBox.setSelectedIndex(1);
                break;
            case TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION:
                comboBox.setSelectedIndex(2);
                break;
        }

        final JCheckBox singleEventMode = new JCheckBox("Single Event Mode");
        singleEventMode.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                _tree.getCheckBoxTreeSelectionModel().setSingleEventMode(singleEventMode.isSelected());
            }
        });
        singleEventMode.setSelected(_tree.getCheckBoxTreeSelectionModel().isSingleEventMode());

        panel.add(new JLabel("Set Selection Mode:"));
        panel.add(comboBox);
        panel.add(Box.createVerticalStrut(3));
        panel.add(digIn);
        panel.add(singleEventMode);
        panel.add(checkBoxEnabled);
        panel.add(clickInCheckBoxOnly);
        panel.add(treeEnabled);
        panel.add(Box.createVerticalStrut(3));
        JPanel buttonPanel = new JPanel(new GridLayout(1, 0, 6, 6));
        buttonPanel.add(selectAll);
        buttonPanel.add(clearAll);
        panel.add(buttonPanel);
        panel.add(Box.createGlue());
        return panel;
    }

    public Component getDemoPanel() {
        JPanel panel = new JPanel(new BorderLayout(6, 6));

        final TreeModel treeModel = createTreeModel();

        JPanel treePanel = new JPanel(new BorderLayout(2, 2));
        treePanel.setBorder(BorderFactory.createCompoundBorder(new JideTitledBorder(new PartialEtchedBorder(PartialEtchedBorder.LOWERED, PartialSide.NORTH), "Albums", JideTitledBorder.LEADING, JideTitledBorder.ABOVE_TOP),
                BorderFactory.createEmptyBorder(6, 0, 0, 0)));
        _tree = new CheckBoxTree(treeModel) {
            @Override
            public Dimension getPreferredScrollableViewportSize() {
                return new Dimension(400, 400);
            }
        };
        _tree.setRootVisible(false);
        _tree.setShowsRootHandles(true);
        DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) _tree.getActualCellRenderer();
        renderer.setLeafIcon(IconsFactory.getImageIcon(QuickFilterTreeDemo.class, "/icons/song.png"));
        renderer.setClosedIcon(IconsFactory.getImageIcon(QuickFilterTreeDemo.class, "/icons/album.png"));
        renderer.setOpenIcon(IconsFactory.getImageIcon(QuickFilterTreeDemo.class, "/icons/album.png"));

        SearchableUtils.installSearchable(_tree);

        TreeUtils.expandAll(_tree, true);

        JPanel listsPanel = new JPanel(new GridLayout(1, 2));

        final JList selectedList = new JList();
        final JList eventsList = new JList();
        final DefaultListModel eventsModel = new DefaultListModel();
        _tree.getCheckBoxTreeSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                TreePath[] paths = e.getPaths();
                for (TreePath path : paths) {
                    eventsModel.addElement((e.isAddedPath(path) ? "Added - " : "Removed - ") + path);
                }
                eventsModel.addElement("---------------");
                eventsList.ensureIndexIsVisible(eventsModel.size() - 1);

                TreePath[] treePaths = _tree.getCheckBoxTreeSelectionModel().getSelectionPaths();
                DefaultListModel selectedModel = new DefaultListModel();
                if (treePaths != null) {
                    for (TreePath path : treePaths) {
                        selectedModel.addElement(path);
                    }
                }
                selectedList.setModel(selectedModel);
            }
        });
        eventsList.setModel(eventsModel);

        selectedList.setVisibleRowCount(8);
        eventsList.setVisibleRowCount(8);
        JPanel selectedPanel = new JPanel(new BorderLayout());
        selectedPanel.setBorder(BorderFactory.createCompoundBorder(new JideTitledBorder(new PartialEtchedBorder(PartialEtchedBorder.LOWERED, PartialSide.NORTH), "Selected Songs", JideTitledBorder.LEADING, JideTitledBorder.ABOVE_TOP),
                BorderFactory.createEmptyBorder(6, 0, 0, 0)));
        selectedPanel.add(new JScrollPane(selectedList));

        JPanel eventsPanel = new JPanel(new BorderLayout());
        eventsPanel.setBorder(BorderFactory.createCompoundBorder(new JideTitledBorder(new PartialEtchedBorder(PartialEtchedBorder.LOWERED, PartialSide.NORTH), "Event Fired", JideTitledBorder.LEADING, JideTitledBorder.ABOVE_TOP),
                BorderFactory.createEmptyBorder(6, 0, 0, 0)));
        eventsPanel.add(new JScrollPane(eventsList));

        listsPanel.add(selectedPanel);
        listsPanel.add(eventsPanel);

        treePanel.add(listsPanel, BorderLayout.AFTER_LAST_LINE);
        treePanel.add(new JScrollPane(_tree));
        panel.add(treePanel);

        return panel;
    }

    @Override
    public String getDemoFolder() {
        return "B16.CheckBoxTree";
    }

    static public void main(String[] s) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new CheckBoxTreeDemo());
    }
}
