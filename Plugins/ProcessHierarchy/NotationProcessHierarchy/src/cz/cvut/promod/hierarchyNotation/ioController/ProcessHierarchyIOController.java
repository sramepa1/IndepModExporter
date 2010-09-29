package cz.cvut.promod.hierarchyNotation.ioController;

import cz.cvut.promod.plugin.notationSpecificPlugIn.notation.localIOController.NotationLocalIOController;
import cz.cvut.promod.services.projectService.treeProjectNode.ProjectDiagram;
import cz.cvut.promod.services.projectService.ProjectService;
import cz.cvut.promod.hierarchyNotation.modelFactory.diagramModel.ProcessHierarchyDiagramModel;
import cz.cvut.promod.hierarchyNotation.modelFactory.graphModel.ProcessHierarchyGraphModel;
import cz.cvut.promod.hierarchyNotation.modelFactory.vertexes.ProcessVertex;
import cz.cvut.promod.hierarchyNotation.modelFactory.vertexes.PHVertexView;
import org.apache.log4j.Logger;
import org.jgraph.graph.*;

import java.beans.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 18:40:57, 26.11.2009
 *
 * An implementation of a NotationLocalIOController interface for the ProcessHierarchyNotation plugin.
 */
public class ProcessHierarchyIOController implements NotationLocalIOController {

    private static final Logger LOG = Logger.getLogger(ProcessHierarchyIOController.class);

    public static final String UUID_PROPERTY =  "uuid";
    public static final String DISPLAY_NAME_PROPERTY = "displayName";
    public static final String NOTATION_IDENTIFIER_PROPERTY = "notationIdentifier";
    public static final String DIAGRAM_MODEL_PROPERTY = "diagramModel";
    public static final String LAYOUT_CACHE_PROPERTY = "graphLayoutCache";
    public static final String CELL_PROPERTY = "cell";
    public static final String ATTRIBUTES_PROPERTY = "attributes";
    public static final String USER_OBJECT_PROPERTY = "userObject";
    public static final String ROOTS_PROPERTY = "roots";
    public static final String ROUNTING_SIMPLE_PROPERTY = "getROUTING_SIMPLE";
    public static final String ROUNTING_DEFAULT_PROPERTY = "getROUTING_DEFAULT";
    public static final String MODEl_PROPERTY = "model";
    public static final String FACTORY_PROPERTY = "factory";
    public static final String CELL_VIEWS_PROPERTY = "cellViews";
    public static final String HIDD_CELLS_PROPERTY = "hiddenCellViews";
    public static final String PARTIAL_PROPERTY = "partial";
    public static final String MSB_PROPERTY = "mostSignificantBits";
    public static final String LSB_PROPERTY = "leastSignificantBits";
    public static final String TRANSIENT_PROPERTY = "transient";

    private final String extension;

    private final String identifier;

    // Most of properties stored in xyzViews are not necessary to save into the file system.
    static{
		initTransiency(PHVertexView.class);
        initTransiency(EdgeView.class);
    }

    /**
     * Constructs a new ProcessHierarchyIOController.
     * @param extension is the notation's file extension
     * @param identifier is the notation identifier
     */
    public ProcessHierarchyIOController(final String extension, final String identifier){
        this.extension = extension;
        this.identifier = identifier;
    }

    /**
     *  {@inheritDoc}
     */
    public SaveResult saveProjectDiagram(final ProjectDiagram projectDiagram,
                                         final String parentLocation,
                                         final boolean makeDirs) {

        if(!isValidDiagramModel(projectDiagram)){
            return SaveResult.ERROR;
        }

        final XMLEncoder encoder;
        final String filePath = parentLocation + System.getProperty("file.separator") +
                                projectDiagram.getDisplayName() + ProjectService.FILE_EXTENSION_DELIMITER + extension;

        try {
            final File file = new File(filePath);

            encoder = new XMLEncoder(new FileOutputStream(file));

        } catch (FileNotFoundException e) {
            LOG.error("Unable to find the file to save the project diagram: " + projectDiagram.getDisplayName() + ", " + filePath, e);
            return SaveResult.ERROR;
        }

        boolean error = false;

       initEncoder(encoder);

        try{
            encoder.writeObject(projectDiagram);

        } catch (Exception exception){
            LOG.error("An error during EPC diagram xml encoding has occurred.", exception);
            error = true;
        } finally {
            encoder.close();
        }

        return error ? SaveResult.ERROR : SaveResult.SUCCESS;
    }

    /**
     * Tests whether the project diagram that's going to be saved is valid.
     *
     * @param projectDiagram is the project diagram that is supposed to be tested for validity
     * @return true if the project diagram is valid, false otherwise
     */
    private boolean isValidDiagramModel(final ProjectDiagram projectDiagram) {
        if(projectDiagram == null){
            LOG.error("Nullary project diagram.");
            return false;
        }

        if(!(projectDiagram.getDiagramModel() instanceof ProcessHierarchyDiagramModel)){
            LOG.error("Not valid diagram model.");
            return false;
        }

        return true;
    }

    /**
     * Initializes the xml encoder. Sets all necessary Persistence delegates for all classes involved in the persistence
     * process.
     *
     * @param encoder is the encoder that is supposed to be initialized
     */
    private void initEncoder(final XMLEncoder encoder) {
        // abstract view
        encoder.setPersistenceDelegate(AbstractCellView.class, new DefaultPersistenceDelegate(new String[] {CELL_PROPERTY, ATTRIBUTES_PROPERTY}));

        // project diagram
        encoder.setPersistenceDelegate(ProjectDiagram.class, new DefaultPersistenceDelegate(
                new String[] {DISPLAY_NAME_PROPERTY, NOTATION_IDENTIFIER_PROPERTY, UUID_PROPERTY, DIAGRAM_MODEL_PROPERTY})
        );

        // diagram model
        encoder.setPersistenceDelegate(ProcessHierarchyDiagramModel.class, new DefaultPersistenceDelegate(
                new String[] {LAYOUT_CACHE_PROPERTY}));

        // vertex
        encoder.setPersistenceDelegate(PHVertexView.class, new DefaultPersistenceDelegate(new String[] {CELL_PROPERTY}));
        encoder.setPersistenceDelegate(DefaultGraphCell.class, new DefaultPersistenceDelegate( new String[] {USER_OBJECT_PROPERTY}));

        // vertex models
        encoder.setPersistenceDelegate(ProcessVertex.class, new DefaultPersistenceDelegate(new String[] {}));        

        // edges
        encoder.setPersistenceDelegate(DefaultEdge.class, new DefaultPersistenceDelegate(new String[] {USER_OBJECT_PROPERTY}));
        encoder.setPersistenceDelegate(DefaultEdge.DefaultRouting.class, new PersistenceDelegate() {
            protected Expression instantiate(Object oldInstance, Encoder out) {
                return new Expression(oldInstance, GraphConstants.class, ROUNTING_SIMPLE_PROPERTY, null);}
        });
        encoder.setPersistenceDelegate(DefaultEdge.LoopRouting.class, new PersistenceDelegate() {
            protected Expression instantiate(Object oldInstance, Encoder out) {
                return new Expression(oldInstance, GraphConstants.class, ROUNTING_DEFAULT_PROPERTY, null);
            }
        });

        // graph models
        encoder.setPersistenceDelegate(DefaultGraphModel.class, new DefaultPersistenceDelegate(new String[] {ROOTS_PROPERTY, ATTRIBUTES_PROPERTY}));
        encoder.setPersistenceDelegate(ProcessHierarchyGraphModel.class, new DefaultPersistenceDelegate(new String[] {ROOTS_PROPERTY, ATTRIBUTES_PROPERTY}));

        // graph layout cache
        encoder.setPersistenceDelegate(GraphLayoutCache.class, new DefaultPersistenceDelegate(
                        new String[] {MODEl_PROPERTY, FACTORY_PROPERTY, CELL_VIEWS_PROPERTY, HIDD_CELLS_PROPERTY, PARTIAL_PROPERTY}));

        // utils
        encoder.setPersistenceDelegate(UUID.class, new DefaultPersistenceDelegate( new String[] {MSB_PROPERTY, LSB_PROPERTY}));
        encoder.setPersistenceDelegate(ArrayList.class, encoder.getPersistenceDelegate(List.class));
    }

    /**
     * {@inheritDoc}
     */
    public ProjectDiagram loadProjectDiagram(final String diagramLocation) throws Exception{
        if(diagramLocation == null || diagramLocation.isEmpty()){
            return null;
        }

        final File file = new File(diagramLocation);

        if(!file.exists()){
            LOG.error("Unable to load file (file does NOT exist), " + diagramLocation);
            return null;
        }

        final ProjectDiagram projectDiagram;
        try {
            final XMLDecoder decoder = new XMLDecoder(new FileInputStream(file));

            projectDiagram = (ProjectDiagram) decoder.readObject();
        } catch (ClassCastException e){
            LOG.error("Unable to cast the loaded object as ProjectDiagram class.", e);
            throw e;

        } catch (FileNotFoundException e) {
            LOG.error("Unable to find the file.", e);
            throw e;
        }

        return createNewProjectDiagram(file, projectDiagram);
    }

    /**
     * Creates new instance of ProjectDiagram. Sets the display name as the file name, and sets the notation identifier
     * to the current epc notation identifier.
     *
     * If the DiagramModel is not cast-able to the EPCDiagramModel then returns null.
     *
     * @param file is the file from which the diagram has been loaded
     * @param loadedProjectDiagram the project diagram that has been loaded
     * @return new project diagram that is supposed to be add to project tree if no error occurs, false otherwise
     */
    private ProjectDiagram createNewProjectDiagram(final File file, final ProjectDiagram loadedProjectDiagram) {
        if(!(loadedProjectDiagram.getDiagramModel() instanceof ProcessHierarchyDiagramModel)){
            LOG.error("Diagram model of loaded project diagram is not an instance of ProcessHierarchyDiagramModel class, " + file.getAbsolutePath());
            return null;
        }

        final String diagramName = file.getName().trim().substring(0, file.getName().trim().lastIndexOf(ProjectService.FILE_EXTENSION_DELIMITER));

        return new ProjectDiagram(diagramName, identifier, loadedProjectDiagram.getUuid(), loadedProjectDiagram.getDiagramModel());
    }

    public String getNotationFileExtension() {
        return extension;
    }

    /**
     * Sets all unnecessary properties of the class specified as the method argument as transient. These properties
     * will be omitted during serialization process.
     *
     * In case of cell views, only 'cell' and 'attributes' properties are supposed to be serialized.
     *
     * @param clazz is the class to omit all necessary properties.
     */
	public static void initTransiency(final Class clazz) {
		try {
			final BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
			final PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

            for (final PropertyDescriptor descriptor : propertyDescriptors) {
                if (!descriptor.getName().equals(CELL_PROPERTY) && !descriptor.getName().equals(ATTRIBUTES_PROPERTY)) {
                    descriptor.setValue(TRANSIENT_PROPERTY, true);
                }
            }

		} catch (IntrospectionException e) {
			LOG.error("Unable to set some field(s) of " + clazz.getName() + " class transient.");
		}
	}

}
