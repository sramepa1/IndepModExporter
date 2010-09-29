package cz.cvut.promod.plugin.notationSpecificPlugIn.notation.factory;

import cz.cvut.promod.plugin.notationSpecificPlugIn.notation.model.DiagramModel;


/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 22:16:11, 5.11.2009
 */

/**
 * Every notation has to provide an implementation of DiagramModelFactory. This factory is for obtaining a new
 * instance of DiagramModel. I.g. when a new diagram is required by the user. 
 */
public interface DiagramModelFactory {

    /**
     * Creates a new instance DiagramModel class. Usually every notations has it's own implementation of DiagramModel
     * class (Subclass of DiagramModel).  
     *
     * @return an subclass of DiagramModel ready for holding notation specific diagram information
     */
    public DiagramModel createEmptyDiagramModel();

}
