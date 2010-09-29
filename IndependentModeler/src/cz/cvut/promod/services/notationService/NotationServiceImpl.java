package cz.cvut.promod.services.notationService;

import cz.cvut.promod.plugin.notationSpecificPlugIn.notation.Notation;
import cz.cvut.promod.plugin.notationSpecificPlugIn.notation.localIOController.NotationLocalIOController;
import cz.cvut.promod.plugin.notationSpecificPlugIn.notation.model.DiagramModel;
import cz.cvut.promod.services.pluginLoaderService.utils.NotationSpecificPlugins;
import cz.cvut.promod.services.pluginLoaderService.utils.PluginLoadErrors;

import java.util.*;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 17:49:53, 10.10.2009
 */

/**
 * NotationService implementation.
 */
public class NotationServiceImpl implements NotationService{

    private final List<NotationSpecificPlugins> notationSpecificPluginsList;
    final List<PluginLoadErrors> errors;

    public NotationServiceImpl(final List<NotationSpecificPlugins> notationSpecificPluginsList,
                               final List<PluginLoadErrors> errors) {

        this.notationSpecificPluginsList = notationSpecificPluginsList;
        this.errors = errors;
    }

    /** {@inheritDoc} */
    public boolean check() {
        return true;
    }

    /** {@inheritDoc} */
    public List<Notation> getNotations() {
        final List<Notation> notations = new LinkedList<Notation>();

        for(final NotationSpecificPlugins notationSpecificPlugins : notationSpecificPluginsList){
            notations.add(notationSpecificPlugins.getNotation());
        }

        return notations;
    }

    /** {@inheritDoc} */
    public Notation getNotation(final String notationIdentifier) {
        for(final NotationSpecificPlugins notationSpecificPlugins : notationSpecificPluginsList){
            final Notation notation = notationSpecificPlugins.getNotation();

            if(notation.getIdentifier().equals(notationIdentifier)){
                return notation;
            }
        }
        
        return null;
    }

    /** {@inheritDoc} */
    public List<String> getNotationsIdentifiers() {
        final List<String> identifiers = new LinkedList<String>();

        for(final NotationSpecificPlugins notationSpecificPlugins : notationSpecificPluginsList){
            identifiers.add(notationSpecificPlugins.getNotation().getIdentifier());
        }

        return identifiers;
    }

    /** {@inheritDoc} */
    public NotationSpecificPlugins getNotationSpecificPlugins(final String notationIdentifier) {
        for(final NotationSpecificPlugins notationSpecificPlugins : notationSpecificPluginsList){
            if(notationSpecificPlugins.getNotation().getIdentifier().equals(notationIdentifier)){
                return notationSpecificPlugins;
            }
        }

        return null;
    }

    /** {@inheritDoc} */
    public DiagramModel createEmptyNotationModel(final String notationIdentifier){
        if(existNotation(notationIdentifier)){
            return getNotation(notationIdentifier).getDiagramModelFactory().createEmptyDiagramModel();
        }

        return null;
    }

    /** {@inheritDoc} */
    public boolean existNotation(final String notationIdentifier) {
        return getNotation(notationIdentifier) != null;
    }

    public String getNotationFileExtension(final String notationIdentifier) {
        final Notation notation = getNotation(notationIdentifier);

        if(notation != null){
            final NotationLocalIOController notationLocalIOController = notation.getLocalIOController();

            if(notationLocalIOController != null){
                return notationLocalIOController.getNotationFileExtension();
            }
        }

        return null;
    }

    public String getNotationIdentifier(final String notationFileExtension) {
        if(notationFileExtension == null){
            return null;
        }

        for(final Notation notation : getNotations()){
            if(notation.getLocalIOController().getNotationFileExtension().equals(notationFileExtension)){
                return notation.getIdentifier();
            }
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    public List<PluginLoadErrors> getErrors() {
        return errors;
    }
}
