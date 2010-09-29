package cz.cvut.promod.diagramReferenceModule;

import cz.cvut.promod.plugin.notationSpecificPlugIn.DockableFrameData;
import cz.cvut.promod.diagramReferenceModule.frame.ReferenceFrame;

import java.util.Set;
import java.util.HashSet;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 15:56:02, 16.4.2010
 *
 * The model component of the DiagramReferenceModule plugin.
 */
public class DiagramReferenceModuleModel {

    // translations
    public static final String DIALOG_TITLE = "Select diagram ...";
    public static final String ERROR_TITLE = "Associated diagram error";
    public static final String ERROR_MESSAGE = "Associated diagram has NOT been found.";
    public static final String FRAME_TITLE = "Diagram Reference";
    public static final String NO_DIAGRAM = "No associated diagram.";
    public static final String GO_TO_DIAGRAM = "Go to the diagram ...";
    public static final String CONNECT_DIAGRAM = "Connect Diagram";
    public static final String REMOVE_CONNECTION_DIAGRAM = "Remove Diagram Connection";
    public static final String NAME = "DiagramReferenceModule";
    public static final String DESCRIPTION = "Module allowing a user defines references from elements of the Process Hierarchy Notation to any diagram.";

    public static final String FRAME_NAME = "ReferenceFrame_1";

    private final String notationIdentifier;

    private final String identifier;

    private final Set<DockableFrameData> dockableFrames;

    private final ReferenceFrame referenceFrame;


    public DiagramReferenceModuleModel(final String notationIdentifier,
                                       final String identifier) {

        this.notationIdentifier = notationIdentifier;
        this.identifier = identifier;

        referenceFrame = new ReferenceFrame();

        dockableFrames = new HashSet<DockableFrameData>();
        dockableFrames.add(referenceFrame);
    }

    public String getNotationIdentifier() {
        return notationIdentifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    public Set<DockableFrameData> getDockableFrames() {
        return dockableFrames;
    }

    public ReferenceFrame getReferenceFrame() {
        return referenceFrame;
    }

    public String getName() {
        return NAME;
    }

    public String getDescription() {
        return DESCRIPTION;
    }
}
