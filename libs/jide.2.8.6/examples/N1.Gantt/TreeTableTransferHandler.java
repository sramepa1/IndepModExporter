import com.jidesoft.grid.*;

import javax.swing.JComponent;
import javax.swing.TransferHandler;
import javax.swing.table.TableModel;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Allows the user to drag nodes around in the TreeTable.
 * The code assumes all Expandables in your TreeTable support add/removeChild.
 * This code only allows DND to occur within the same TreeTableModel.
 * In Java 5 the drop to another table will be silently ignored,
 * in Java 6 the correct 'can't drop' cursor will appear.
 *
 * <p>Usage:
 * <pre>treeTable.setDragEnabled(true);
 *treeTable.setTransferHandler(new TreeTableTransferHandler());
 *treeTable.setDropMode(DropMode.ON_OR_INSERT_ROWS);</pre>
 * DropMode is only for Java 6 - you should also uncomment the canImport/importData(TransferSupport) methods
 * (and can remove the JComponent/Transferable ones as they will no longer be used).
 * Since Java 5 doesn't support insert drop, you probably want to have the root node visible,
 * or dropping nodes onto the root won't be possible.
 */
public class TreeTableTransferHandler extends TransferHandler {

    private static final long serialVersionUID = 782232536379799089L;

    @Override
    public int getSourceActions(JComponent component) {
        if(component instanceof TreeTable) {
            TreeTable treeTable = (TreeTable) component;
            if(treeTable.getModel() != null) {
                return MOVE;
            }
        }
        return NONE;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Transferable createTransferable(JComponent component) {
        if(component instanceof TreeTable) {
            TreeTable treeTable = (TreeTable) component;
            ITreeTableModel<Row> model = (ITreeTableModel<Row>) treeTable.getModel();
            int[] rows = treeTable.getSelectedRows();
            return new RowTransferable(getParentsOnly(model, rows));
        }
        else {
            return null;
        }
    }

    @Override
    protected void exportDone(JComponent source, Transferable data, int action) {
        // the remove is done in the importData method
    }

// Can be removed when using Java 6 DND support
    @Override
    public boolean canImport(JComponent comp, DataFlavor[] transferFlavors) {
        boolean tasty = false;
        for(DataFlavor flavor : transferFlavors) {
            if(RowTransferable.FLAVOR.equals(flavor)) {
                tasty = true;
                break;
            }
        }
        return tasty && comp instanceof TreeTable;
    }

// Uncomment for Java 6 DND support
//    @SuppressWarnings("unchecked")
//    @Override
//    public boolean canImport(TransferSupport support) {
//        Component component = support.getComponent();
//    if(!(component instanceof TreeTable)
//            || !canImport((JComponent) component, support.getDataFlavors())) {
//        return false;
//    }
//        if(support.isDrop() && support.getDropAction() == MOVE) {
//            javax.swing.JTable.DropLocation location = (javax.swing.JTable.DropLocation) support.getDropLocation();
//            int rowIndex = location.getRow();
//            boolean insert = location.isInsertRow();
//
//            try {
//                List<Row> rows = (List<Row>) support.getTransferable().getTransferData(RowTransferable.FLAVOR);
//                TreeTableModel<Row> model = (TreeTableModel<Row>) ((TreeTable) component).getModel();
//                Node parentNode = getParentNode(model, rowIndex, insert);
//                for(Row row : rows) {
//                    if(!canImport(model, parentNode, row)) {
//                        return false;
//                    }
//                }
//                return true;
//            }
//            catch (UnsupportedFlavorException e) {
//                // shouldn't happen;
//                return false;
//            }
//            catch (IOException e) {
//                // shouldn't happen;
//                return false;
//            }
//        }
//        return false;
//    }

    private boolean canImport(ITreeTableModel<Row> model, Node parent, Row row) {
        return row != parent && !isAnchestor(row, parent)
            && model.getRoot() instanceof Node && isAnchestor((Node) model.getRoot(), row);
    }

// Can be removed when using Java 6 DND support
    @SuppressWarnings("unchecked")
    @Override
    public boolean importData(JComponent component, Transferable transferable) {
        if(!canImport(component, transferable.getTransferDataFlavors())) {
            return false;
        }

        if(component instanceof TreeTable) {
            TreeTable treeTable = (TreeTable) component;
            int rowIndex = treeTable.getSelectedRow();
            boolean insert = false;
            if(rowIndex != -1) {
                insert = !(((ITreeTableModel<Row>) treeTable.getModel()).getRowAt(rowIndex) instanceof Expandable);
            }

            return importRows(treeTable, transferable, rowIndex, insert);
        }
        return false;
    }

// Uncomment for Java 6 DND support
//    @Override
//    public boolean importData(TransferSupport support) {
//    if(!canImport(support)) {
//        return false;
//    }

//        Component component = support.getComponent();
//        if(support.getDropAction() == MOVE && component instanceof TreeTable) {
//            javax.swing.JTable.DropLocation location = (javax.swing.JTable.DropLocation) support.getDropLocation();
//            int rowIndex = location.getRow();
//            boolean insert = location.isInsertRow();
//
//            return importRows((TreeTable) component, support.getTransferable(), rowIndex, insert);
//        }
//        return false;
//    }

    @SuppressWarnings("unchecked")
    private boolean importRows(TreeTable treeTable, Transferable transferable, int rowIndex,
            boolean insert) {

        try {
            List<Row> rows = new ArrayList<Row>((List<Row>)
                    transferable.getTransferData(RowTransferable.FLAVOR));

            ITreeTableModel<Row> model = (ITreeTableModel<Row>) treeTable.getModel();

            Node parentNode = getParentNode(model, rowIndex, insert);
            if(parentNode instanceof Expandable) {
                Expandable parent = (Expandable) parentNode;
                int insertIndex = parent.getChildrenCount();
                if(insert && rowIndex >= 0 && rowIndex < ((TableModel) model).getRowCount()) {
                    int childIndex = parent.getChildIndex(model.getRowAt(rowIndex));
                    if(childIndex != -1) {
                        insertIndex = childIndex;
                    }
                }

                for(Iterator<Row> iter = rows.iterator(); iter.hasNext();) {
                    // in Java 5 not able to check this in the canImport() method
                    Row row = iter.next();
                    if(!canImport(model, parent, row)) {
                        iter.remove();
                    }
                }

                for(Row row : rows) {
                    if(parent == row.getParent()
                            && parent.getChildIndex(row) < insertIndex) {
                        insertIndex--;
                    }
                    row.getParent().removeChild(row);
                }
                for(Row row : rows) {
                    parent.addChild(insertIndex++, row);
                }
                return true;
            }
            return false;
        }
        catch (UnsupportedFlavorException e) {
            // shouldn't happen;
            return false;
        }
        catch (IOException e) {
            // shouldn't happen;
            return false;
        }
    }

    private Node getParentNode(ITreeTableModel<Row> model, int rowIndex, boolean insert) {
        Node parentNode;
        if(rowIndex < 0 || rowIndex >= ((TableModel) model).getRowCount()) {
            parentNode = (Node) model.getRoot();
        }
        else {
            parentNode = model.getRowAt(rowIndex);
            if(insert) {
                parentNode = parentNode.getParent();
            }
        }
        return parentNode;
    }

    private List<Row> getParentsOnly(ITreeTableModel<? extends Row> model, int[] selectedRows) {
        List<Row> selectedParents = new ArrayList<Row>();
        for(int rowIndex : selectedRows) {
            Row row = model.getRowAt(rowIndex);

            boolean add = true;
            for(Iterator<Row> iter = selectedParents.iterator(); iter.hasNext();) {
                Row current = iter.next();
                if(isAnchestor(current, row)) {
                    add = false;
                    break;
                }
                else if(isAnchestor(row, current)) {
                    iter.remove();
                }
            }

            if(add) {
                selectedParents.add(row);
            }
        }

        return selectedParents;
    }

    private boolean isAnchestor(Node nodeA, Node nodeB) {
        if(nodeB != null) {
            Expandable parent = nodeB.getParent();
            return parent == nodeA || isAnchestor(nodeA, parent);
        }
        return false;
    }

    private static class RowTransferable implements Transferable {

        private static final DataFlavor FLAVOR =
            new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType + ";class=java.util.List", "List of TreeTable Rows");

        private final List<Row> rows;

        public RowTransferable(List<Row> rows) {
            this.rows = rows;
        }

        public List<Row> getRows() {
            return rows;
        }

        public List<Row> getTransferData(DataFlavor flavor) throws UnsupportedFlavorException,
                IOException {
            if(isDataFlavorSupported(flavor)) {
                return getRows();
            }
            else {
                throw new UnsupportedFlavorException(flavor);
            }
        }

        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[] { FLAVOR };
        }

        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return FLAVOR.equals(flavor);
        }

    }
}