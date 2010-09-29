package cz.cvut.promod.plugin.notationSpecificPlugIn.notation.utils;

import cz.cvut.promod.services.ModelerSession;
import cz.cvut.promod.plugin.notationSpecificPlugIn.notation.NotationWorkspaceData;

import javax.swing.*;

import java.awt.*;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 22:25:38, 18.10.2009
 */

/**
 * Default implementation of NotationWorkspaceData interface. This class is used in case of
 * emergency and during testing to insert an instance of an empty JPanel containing just textual
 * form of notation identifier. This class is used when there is not own implementation of
 * NotationWorkspaceData available in loaded plugin notation.
 */
public class NotationWorkspaceDataDefault implements NotationWorkspaceData {

    private final String notationIdentifier;

    private final DefaultUpdatableWorkspaceComponent defaultUpdatableWorkspaceComponent;

    public NotationWorkspaceDataDefault(final String notationIdentifier){
        this.notationIdentifier = notationIdentifier;

        defaultUpdatableWorkspaceComponent = new DefaultUpdatableWorkspaceComponent(notationIdentifier);
    }

    public JComponent getWorkspaceComponentSingleton() {
        return defaultUpdatableWorkspaceComponent; 
    }
}
