package cz.cvut.promod.gui.projectNavigation.listeners;

import cz.cvut.promod.services.ModelerSession;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.*;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 23:10:30, 22.10.2009
 */

/**
 * Project navigation mouse popup menu listener.
 */
public class MousePopup extends MouseAdapter {

    final JPopupMenu popup;
    final JTree tree;

    public MousePopup(final JPopupMenu popup, final JTree tree){
        this.popup = popup;
        this.tree = tree;
    }

    @Override
    public void mousePressed(final MouseEvent event) {
        showPopupMenu(event);
    }

    @Override
    public void mouseReleased(final MouseEvent event) {
        showPopupMenu(event);
    }

    /**
     * Shows the popup menu.
     *
     * @param event the mouse event
     */
    private void showPopupMenu(final MouseEvent event){
        if (event.isPopupTrigger()) {
            final TreePath treePath = tree.getPathForLocation(event.getX(), event.getY());

            if(treePath != null){
                ModelerSession.getProjectControlService().setSelectedItem(treePath);
                popup.show((Component)event.getSource(),event.getX(), event.getY());
            }
        }
    }
}
