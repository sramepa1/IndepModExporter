package cz.cvut.promod.gui.projectNavigation.events;

import javax.swing.tree.TreePath;
import java.awt.event.ActionEvent;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 12:54:20, 24.1.2010
 */

/**
 * Represent an extension of ActionEvent that hold tree path and depth for expanding/collapsing
 * project tree navigation.
 */
public class ProjectTreeExpandEvent extends ActionEvent{

    private final TreePath treePath;

    private final int depth; // if depth < 1 -> expand/collapse all

    private final boolean expand;

    /**
     * Constructs a new event for expand/collapse project tree action.
     *
     * @param source is the source of action
     * @param treePath is the root of subtree to be expanded/collapsed
     * @param depth is the depth for expanding, -1 stands for expand all
     * @param expand true for expanding, false for collapsing
     */
    public ProjectTreeExpandEvent(final Object source, 
                                  final TreePath treePath,
                                  final int depth, final boolean expand) {
        super(source, 0, null);

        this.treePath = treePath;
        this.depth = depth;
        this.expand = expand;
    }

    public TreePath getTreePath() {
        return treePath;
    }

    public int getDepth() {
        return depth;
    }

    public boolean isExpand() {
        return expand;
    }
}
