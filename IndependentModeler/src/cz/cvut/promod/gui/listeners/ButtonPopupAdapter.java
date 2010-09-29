package cz.cvut.promod.gui.listeners;

import com.jidesoft.swing.JideButton;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 18:10:20, 19.10.2009
 *
 * Is the listeners showing the popup menu on the buttons controlling the dockable frame visibility.
 */
public class ButtonPopupAdapter extends MouseAdapter {

    private final JideButton owner;
    private final JPopupMenu menu;

    public ButtonPopupAdapter( final JideButton owner, final JPopupMenu menu ){
        this.owner = owner;
        this.menu = menu;
    }

    public void mousePressed( final MouseEvent e ){
        maybepopup(e);
    }

    public void mouseReleased( final MouseEvent e ){
        maybepopup(e);
    }

    private void maybepopup( final MouseEvent e ){
        if( e.isPopupTrigger() && owner.isEnabled() ){
          menu.show(owner, e.getX(), e.getY());
        }
    }
}
