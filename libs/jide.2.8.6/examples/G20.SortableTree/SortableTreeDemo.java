/*
 * @(#)SortableTreeDemo.java 7/19/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.icons.IconsFactory;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.tree.SortableTreeModel;
import com.jidesoft.tree.TreeUtils;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;

/**
 * Demoed Component: {@link com.jidesoft.grid.SortableTable} <br> Required jar files: jide-common.jar, jide-grids.jar
 * <br> Required L&F: any L&F
 */
public class SortableTreeDemo extends AbstractDemo {

    private static final Icon ICON_ALBUM = IconsFactory.getImageIcon(QuickFilterTreeDemo.class, "/icons/album.png");
    private static final Icon ICON_SONG = IconsFactory.getImageIcon(QuickFilterTreeDemo.class, "/icons/song.png");

    public SortableTreeDemo() {
    }

    public String getName() {
        return "SortableTree Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_GRIDS;
    }

    @Override
    public String getDescription() {
        return "This is a demo of JTree with SortableTreeModel to get sortable feature. You can click on the \"Sort\" to sort" +
                "or unsort the tree nodes. Please note, SortableTreeModel is a model wrapper, just like TableModelWrapper or ListModelWrapper. " +
                "It uses index mapping to sort the tree model. The actual tree model is not touched at all. Not only that, the tree node expansion state and selected tree path are kept" +
                "after sorting.\n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.tree.SortableTreeModel";
    }

    public Component getDemoPanel() {
        JPanel panel = new JPanel(new BorderLayout(6, 6));

        final SortableTreeModel newModel = new SortableTreeModel(createTreeModel());
        final JTree tree = new JTree(newModel) {
            @Override
            public Dimension getPreferredScrollableViewportSize() {
                return new Dimension(400, 400);
            }
        };
        tree.setRootVisible(false);
        tree.setShowsRootHandles(true);
        TreeCellRenderer renderer = tree.getCellRenderer();
        if (renderer instanceof DefaultTreeCellRenderer) {
            ((DefaultTreeCellRenderer) renderer).setLeafIcon(ICON_SONG);
            ((DefaultTreeCellRenderer) renderer).setClosedIcon(ICON_ALBUM);
            ((DefaultTreeCellRenderer) renderer).setOpenIcon(ICON_ALBUM);
        }

        TreeUtils.expandAll(tree, true);

        panel.add(new JScrollPane(tree));

        tree.registerKeyboardAction(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                TreePath[] selections = tree.getSelectionPaths();
                for (TreePath selection : selections) {
                    ((DefaultTreeModel) newModel.getActualModel()).removeNodeFromParent((DefaultMutableTreeNode) selection.getLastPathComponent());
                }
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), JComponent.WHEN_FOCUSED);


        JCheckBox checkBox = new JCheckBox("Sort");
        checkBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                Enumeration<TreePath> enumeration = TreeUtils.saveExpansionStateByTreePath(tree);
                TreePath[] selected = TreeUtils.saveSelection(tree);
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    newModel.sort();
                }
                else {
                    newModel.unsort();
                }
                TreeUtils.loadExpansionStateByTreePath(tree, enumeration);
                TreeUtils.loadSelection(tree, selected);
            }
        });
        panel.add(checkBox, BorderLayout.BEFORE_FIRST_LINE);

//        final JTextField textField = new JTextField();
//        textField.addActionListener(new AbstractAction() {
//            public void actionPerformed(ActionEvent e) {
//                String text = textField.getText();
//                if (text != null && text.length() != 0) {
//                    DefaultTreeModel defaultTreeModel = ((DefaultTreeModel) newModel.getActualModel());
//                    if (tree.getSelectionPath() != null) {
//                        TreePath treePath = tree.getSelectionPath();
//                        DefaultMutableTreeNode parent = (DefaultMutableTreeNode) ((DefaultMutableTreeNode) treePath.getLastPathComponent()).getParent();
//                        int index = defaultTreeModel.getIndexOfChild(parent, treePath.getLastPathComponent());
//                        defaultTreeModel.insertNodeInto(new DefaultMutableTreeNode(text), parent, index + 1);
//                    }
//                    else {
//                        defaultTreeModel.insertNodeInto(new DefaultMutableTreeNode(text), (DefaultMutableTreeNode) defaultTreeModel.getRoot(), 0);
//                    }
//                    textField.setText("");
//                }
//            }
//        });
//        panel.add(textField, BorderLayout.AFTER_LAST_LINE);
        return panel;
    }

    private TreeModel createTreeModel() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Songs");
        DefaultTreeModel treeModel = new DefaultTreeModel(root);
        HashMap<String, DefaultMutableTreeNode> albums = new HashMap();

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
    public String getDemoFolder() {
        return "G20.SortableTree";
    }

    static public void main(String[] s) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new SortableTreeDemo());
    }
}
