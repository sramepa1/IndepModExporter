package cz.cvut.indepmod.classmodel.modelFactory;

import cz.cvut.indepmod.classmodel.modelFactory.diagramModel.ClassModelDiagramModel;
import cz.cvut.promod.plugin.notationSpecificPlugIn.notation.factory.DiagramModelFactory;
import cz.cvut.promod.plugin.notationSpecificPlugIn.notation.model.DiagramModel;

/**
 * Created by IntelliJ IDEA.
 * User: martin
 * Date: 9.8.2010
 * Time: 13:24:06
 */
public class ClassModelNotationModelFactory implements DiagramModelFactory {

    public DiagramModel createEmptyDiagramModel() {
        return new ClassModelDiagramModel();
    }
}
