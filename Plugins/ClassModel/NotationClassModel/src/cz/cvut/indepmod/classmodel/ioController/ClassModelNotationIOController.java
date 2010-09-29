package cz.cvut.indepmod.classmodel.ioController;

import cz.cvut.promod.plugin.notationSpecificPlugIn.notation.localIOController.NotationLocalIOController;
import cz.cvut.promod.services.projectService.treeProjectNode.ProjectDiagram;

public class ClassModelNotationIOController implements NotationLocalIOController {

    public SaveResult saveProjectDiagram(ProjectDiagram projectDiagram, String parentLocation, boolean makeDirs) {
        return null;
    }

    public ProjectDiagram loadProjectDiagram(String diagramLocation) throws Exception {
        return null;
    }

    public String getNotationFileExtension() {
        return "cls"; // TODO: This should be configurable (or part of the model)
    }
}
