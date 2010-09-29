package cz.cvut.promod.epcImageExport;

import cz.cvut.promod.epc.workspace.EPCWorkspaceData;
import cz.cvut.promod.epcImageExport.frames.imageExport.ImageExport;
import cz.cvut.promod.epcImageExport.resources.Resources;
import cz.cvut.promod.epcImageExport.settings.ImageExportSettings;
import cz.cvut.promod.gui.settings.SettingPageData;
import cz.cvut.promod.plugin.notationSpecificPlugIn.DockableFrameData;
import cz.cvut.promod.plugin.notationSpecificPlugIn.notation.NotationWorkspaceData;
import cz.cvut.promod.services.ModelerSession;
import cz.cvut.promod.services.actionService.actionUtils.ProModAction;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 23:41:00, 13.12.2009
 *
 * The model component of the EPCImageExportModule plugin. 
 */
public class EPCImageExportModuleModel {

    private static final Logger LOG = Logger.getLogger(EPCImageExportModuleModel.class);

    public static final String NOTATION_IDENTIFIER = "epc.notation.identifier";
    public static final String IDENTIFIER = "epc.imageexport.png.identifier";
    public static final String NAME = "epc.imageexport.png.name";
    public static final String DESCRIPTION = "epc.imageexport.png.description";

    public static final String PNG_EXPORT_ACTION = "epc.imageexport.png.export.action";

    private static final String INSET_LABEL = Resources.getResources().getString("epc.imageexport.settings.inset");

    private Set<DockableFrameData> dockableFrames;

    private final Properties properties = new Properties();

    private final Map<String, ProModAction> actions;

    private List<SettingPageData> settingPages;


    public EPCImageExportModuleModel(final File propertiesFile) throws InstantiationException {
        try {
            properties.load(new FileReader(propertiesFile));                        

        } catch (IOException e) {
            LOG.error("Properties for the EPC Image Export module couldn't be read.", e);
            throw new InstantiationException("Mandatory properties couldn't be read.");
        }

        if(Resources.getResources() == null){
            throw new InstantiationException("Resource bundle not available");            
        }

        actions = new HashMap<String, ProModAction>();

        //init dockable frames
        dockableFrames = new HashSet<DockableFrameData>();

        settingPages = new LinkedList<SettingPageData>();
    }

    /**
     * Initialize dockable frames.
     */
    public void initFrames() {
        final NotationWorkspaceData workspaceData = ModelerSession.getNotationService().
                                    getNotation(getRelatedNotationIdentifier()).getNotationWorkspaceData();

        if(!(workspaceData instanceof EPCWorkspaceData)){
            // should never happened
            LOG.error("Related notation doesn't provide appropriate notation workspace.");
            return;
        }

        final EPCWorkspaceData epcWorkspaceData = (EPCWorkspaceData) workspaceData;

        final ImageExport imageExport = new ImageExport(epcWorkspaceData.getGraph(), actions);

        dockableFrames.add(imageExport);

        // init setting pages
        ImageExportSettings imageExportSettings = new ImageExportSettings(imageExport.getPresentationModel());
        SettingPageData settingPageData = new SettingPageData(INSET_LABEL, null, imageExportSettings);
        settingPages.add(settingPageData);
    }

    /**
     * @return the related notation identifier
     */
    public String getRelatedNotationIdentifier() {
        return properties.getProperty(NOTATION_IDENTIFIER);
    }

    /**
     * @return the plugin's identifier
     */
    public String getIdentifier() {
        return properties.getProperty(IDENTIFIER);
    }

    /**
     * @return dockable frames
     */
    public Set<DockableFrameData> getDockableFrames() {
        return dockableFrames;
    }

    /**
     * @return the action map
     */
    public Map<String, ProModAction> getActions() {
        return actions;
    }

    /**
     * @return common settings dialog pages
     */
    public List<SettingPageData> getSettingPages() {
        return settingPages;
    }

    /**
     * @return the plugin's name
     */
    public String getName() {
        return properties.getProperty(NAME);
    }

    /**
     * @return the plugin's description
     */
    public String getDescription() {
        return properties.getProperty(DESCRIPTION);
    }
}
