package cz.cvut.promod.diagramReferenceModule;

import cz.cvut.promod.gui.settings.SettingPageData;
import cz.cvut.promod.hierarchyNotation.workspace.ProcessHierarchyWorkspaceData;
import cz.cvut.promod.plugin.notationSpecificPlugIn.DockableFrameData;
import cz.cvut.promod.plugin.notationSpecificPlugIn.notation.Notation;
import cz.cvut.promod.plugin.notationSpecificPlugIn.notation.NotationWorkspaceData;
import cz.cvut.promod.services.ModelerSession;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 15:53:19, 16.4.2010
 *
 * Implementation of DiagramReferenceModule plugin.
 */
public class DiagramReferenceModule implements cz.cvut.promod.plugin.notationSpecificPlugIn.module.Module{

    private static String IDENTIFIER_KEY = "identifier";

    private final Logger LOG = Logger.getLogger(DiagramReferenceModule.class);

    private final DiagramReferenceModuleModel model;


    public DiagramReferenceModule(final File file, final String notationIdentifier) throws InstantiationException, IOException {
        final Properties properties = new Properties();
        properties.load(new FileInputStream(file));

        final String identifier = properties.getProperty(IDENTIFIER_KEY);

        if(identifier == null){
            throw new InstantiationException();
        }

        model = new DiagramReferenceModuleModel(notationIdentifier, identifier);
    }

    public String getName() {
        return model.getName();
    }

    public String getDescription() {
        return model.getDescription();
    }

    public String getRelatedNotationIdentifier() {
        return model.getNotationIdentifier();
    }

    public Set<DockableFrameData> getDockableFrames() {
        return model.getDockableFrames();
    }

    public String getIdentifier() {
        return model.getIdentifier();
    }

    public void init() {
        initSelectionListener();
    }

    private void initSelectionListener() {
        final Notation notation = ModelerSession.getNotationService().getNotation(getRelatedNotationIdentifier());

        final NotationWorkspaceData workspace = notation.getNotationWorkspaceData();

        if(workspace instanceof ProcessHierarchyWorkspaceData){
            final ProcessHierarchyWorkspaceData workspaceData = (ProcessHierarchyWorkspaceData) workspace;

            workspaceData.getGraph().addGraphSelectionListener(model.getReferenceFrame());

            model.getReferenceFrame().setGraph(workspaceData.getGraph());

        } else {
            LOG.fatal("Invalid workspace component.");
        }
    }

    public void finish() {
        LOG.info("Diagram Reference Module is performing the finish sequence.");
    }

    public List<SettingPageData> getSettingPages() {
        return null;
    }

}
