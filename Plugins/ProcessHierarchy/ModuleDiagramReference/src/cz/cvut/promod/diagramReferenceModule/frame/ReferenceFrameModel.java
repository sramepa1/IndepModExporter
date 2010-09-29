package cz.cvut.promod.diagramReferenceModule.frame;

import cz.cvut.promod.gui.support.utils.NotationGuiHolder;

import java.util.Set;
import java.util.HashSet;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 16:08:52, 16.4.2010
 *
 * The model component of the Diagram Reference dockable frame.
 */
public class ReferenceFrameModel {

    private final Set<NotationGuiHolder.Position> positions;

    public ReferenceFrameModel() {
        positions = new HashSet<NotationGuiHolder.Position>();
        positions.add(NotationGuiHolder.Position.BOTTOM);
        positions.add(NotationGuiHolder.Position.TOP);
    }


    public Set<NotationGuiHolder.Position> getPositions() {
        return positions;
    }
}
