package cz.cvut.promod.epc.workspace.cell;

import org.jgraph.graph.PortRenderer;

import javax.swing.*;
import java.awt.*;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 19:51:23, 7.12.2009
 *
 * Special implementation of the EPCPortRenderer for the EPCNotation plugin.
 */
public class EPCPortRenderer extends PortRenderer {

    private ImageIcon portIcon = null;

    public EPCPortRenderer(final ImageIcon portIcon){
        this.portIcon = portIcon;
    }

    public void paint(Graphics g) {
        if(portIcon != null){
            portIcon.paintIcon(this, g, 0, 0);

        } else {
            // if the icon is not available, then use default appearance
            super.paint(g);
        }
    }
    
}
