package cz.cvut.promod.services.projectService.localIO;

import cz.cvut.promod.gui.ModelerModel;
import cz.cvut.promod.services.pluginLoaderService.utils.PluginLoadErrors;
import cz.cvut.promod.services.projectService.treeProjectNode.ProjectRoot;
import cz.cvut.promod.services.projectService.ProjectService;
import cz.cvut.promod.services.projectService.results.LoadProjectResult;
import cz.cvut.promod.services.projectService.results.ConfigurationDifference;
import cz.cvut.promod.services.ModelerSession;
import cz.cvut.promod.services.pluginLoaderService.utils.NotationSpecificPlugins;
import cz.cvut.promod.plugin.notationSpecificPlugIn.notation.Notation;
import cz.cvut.promod.plugin.notationSpecificPlugIn.module.Module;
import cz.cvut.promod.plugin.extension.Extension;
import cz.cvut.promod.resources.Resources;

import java.io.*;
import java.util.List;
import java.util.LinkedList;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 0:44:44, 11.11.2009
 */

/**
 * Project file loading functionality holder.
 */
public class ProjectFileLoader {

    private static final Logger LOG = Logger.getLogger(ProjectFileLoader.class);

    /**
     * Loads the project file.
     *
     * @param projectFile is the FS project file location
     * @return an instance of LoadProjectResult class holding operation results
     */
    public static LoadProjectResult loadProjectFile(final File projectFile) {
        if(!isValidFile(projectFile)){
            return null;
        }

        final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

        final InputStream schemaInputStream = ClassLoader.getSystemResourceAsStream(
                Resources.CONFIG + Resources.PROJECT_XSD_FILE
        );


        final Schema schema;
        final SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        final Source source = new StreamSource(schemaInputStream);
        try {
            schema = schemaFactory.newSchema(source);

        } catch (SAXException e) {
            LOG.info("Skipping plugin definition file validation. Schema not found.");
            return new LoadProjectResult(null, null);
        }

        LOG.info("Validating project file against xsd file.");

        documentBuilderFactory.setNamespaceAware(true);
        documentBuilderFactory.setValidating(false);

        final Document document;

        try {
            final DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            documentBuilder.setErrorHandler(new ProjectFileXmlErrorHandler());

            document = documentBuilder.parse(projectFile);

            final Validator validator = schema.newValidator();

            validator.validate(new DOMSource(document));

        } catch (ParserConfigurationException e) {
            LOG.error("XML Parser configuration has failed.", e);
            return new LoadProjectResult(null, null);

        } catch (IOException e) {
            LOG.error("An IO error has occurred when reading the project file.", e);
            return new LoadProjectResult(null, null);

        } catch (SAXException e) {
            LOG.error("An SAX error has occurred when reading the project file.", e);
            return new LoadProjectResult(null, null);
        }

        final List<ConfigurationDifference> messages = getConfigurationChanges(document);
        final ProjectRoot projectRoot = new ProjectRoot(getProjectName(projectFile.getName()), projectFile.getParent());

        return new LoadProjectResult(projectRoot, messages);
    }

    /**
     * Finds differences in actual ProMod configuration and the configuration loaded from the project file
     * and stores this changes in list.
     *
     * @param document is the loaded DOM document of project file
     *
     * @return list containing all differences in configuration 
     */
    private static List<ConfigurationDifference> getConfigurationChanges(final Document document) {
        final List<ConfigurationDifference> messages = new LinkedList<ConfigurationDifference>();

        final NodeList rootNodeList = document.getElementsByTagName(XmlConsts.ROOT_ELEMENT);
        if(rootNodeList.getLength() != 1){
            LOG.error("More than one (or missing) root node in project xml file.");
            return null;
        }

        final Node rootNode = rootNodeList.item(0);

        final NodeList nodeList = rootNode.getChildNodes();

        for(int i = 0; i < nodeList.getLength(); i++){
            final Node node = nodeList.item(i);

            if(XmlConsts.BASIC_INFO_ELEMENT.equals(node.getNodeName())){
              processBasicInfo(node, messages);

            } else if(XmlConsts.CONFIGURATION_ELEMENT.equals(node.getNodeName())){
                final NodeList congigurationNodes = node.getChildNodes();

                for(int j = 0; j < congigurationNodes.getLength(); j++){
                    final Node configurationNode = congigurationNodes.item(j);

                    if(XmlConsts.PLUGINS_ELEMENT.equals(configurationNode.getNodeName())){
                        final NodeList pluginNodes = configurationNode.getChildNodes();

                        for(int k = 0; k < pluginNodes.getLength(); k++){
                            final Node pluginNode = pluginNodes.item(k);

                            if(XmlConsts.NOTATION_ELEMENT.equals(pluginNode.getNodeName())){
                                processNotationSpecificInfo(pluginNode, messages);

                            } else if(XmlConsts.EXTENSION_ELEMENT.equals(pluginNode.getNodeName())){
                                processExtensionSpecificInfo(pluginNode, messages);
                            }
                        }
                    }
                }

                break;
            }
        }

        return messages;                
    }

    /**
     * Finds and saves for future use all differences between actual ProMod configuration and the configuration loaded
     * from a project file related to the extensions.
     *
     * @param pluginNode is the plugin node from the project file DOM
     * @param messages is the list that collect all differences
     */
    private static void processExtensionSpecificInfo(final Node pluginNode, final List<ConfigurationDifference> messages) {
        final NamedNodeMap notationAttributes = pluginNode.getAttributes();
        final String extensionIdentifier = readAttribute(notationAttributes.getNamedItem(XmlConsts.IDENTIFIER_ATTRIBUTE));
        final String extensionFullName = readAttribute(notationAttributes.getNamedItem(XmlConsts.FULL_NAME_ATTRIBUTE));

        if(extensionIdentifier != null){
            final Extension extension = ModelerSession.getExtensionService().getExtension(extensionIdentifier);

            if(extension == null){
                messages.add(new ConfigurationDifference(ConfigurationDifference.ChangeType.MISSING_EXTENSION, extensionIdentifier));

            } else {
                if(!extension.getName().equals(extensionFullName)){
                    messages.add(new ConfigurationDifference(
                            ConfigurationDifference.ChangeType.DIFFERENT_FULL_NAME, extensionFullName, extensionIdentifier)
                    );
                }
            }

        } else {
            // should never happened
            LOG.error("Missing extension identifier info in the project file.");            
        }
        
    }

    /**
     * Finds and saves for future use all differences between actual ProMod configuration and the configuration loaded
     * from a project file related to the notations and their modules.
     *
     * @param pluginNode is the plugin node from the project file DOM
     * @param messages is the list that collect all differences
     */
    private static void processNotationSpecificInfo(final Node pluginNode, final List<ConfigurationDifference> messages) {
        final NamedNodeMap notationAttributes = pluginNode.getAttributes();
        final String notationIdentifier = readAttribute(notationAttributes.getNamedItem(XmlConsts.IDENTIFIER_ATTRIBUTE));
        final String notationFullName = readAttribute(notationAttributes.getNamedItem(XmlConsts.FULL_NAME_ATTRIBUTE));
        final String notationAbbreviation = readAttribute(notationAttributes.getNamedItem(XmlConsts.ABBREVIATION_ATTRIBUTE));
        final String notationExtension = readAttribute(notationAttributes.getNamedItem(XmlConsts.EXTENSION_ATTRIBUTE));

        if(notationIdentifier != null){
            final NotationSpecificPlugins notationSpecificPlugins = ModelerSession.getNotationService().getNotationSpecificPlugins(notationIdentifier);

            if(notationSpecificPlugins == null){
                messages.add(new ConfigurationDifference(ConfigurationDifference.ChangeType.MISSING_NOTATION, notationIdentifier));

            } else {
                final Notation notation = notationSpecificPlugins.getNotation();

                if(!notation.getFullName().equals(notationFullName)){ // the full name doesn't fit
                    messages.add(new ConfigurationDifference(
                            ConfigurationDifference.ChangeType.DIFFERENT_FULL_NAME, notationFullName, notationIdentifier)
                    );
                }
                if(!notation.getAbbreviation().equals(notationAbbreviation)){  // the abbreviation doesn't fit
                    messages.add(new ConfigurationDifference(
                            ConfigurationDifference.ChangeType.DIFFERENT_ABBREVIATION, notationAbbreviation, notationIdentifier)
                    );
                }
                if(!notation.getLocalIOController().getNotationFileExtension().equals(notationExtension)){  // the extension doesn't fit
                    messages.add(new ConfigurationDifference(
                            ConfigurationDifference.ChangeType.DIFFERENT_EXTENSION, notationExtension, notationIdentifier)
                    );
                }

                final NodeList nodes = pluginNode.getChildNodes();
                for(int i = 0; i < nodes.getLength(); i++){
                    final Node moduleNode = nodes.item(i);

                    if(XmlConsts.MODULE_ELEMENT.equals(moduleNode.getNodeName())){
                        final  NamedNodeMap moduleAttributes = moduleNode.getAttributes();
                        final String moduleIdentifier = readAttribute(moduleAttributes.getNamedItem(XmlConsts.IDENTIFIER_ATTRIBUTE));

                        if(moduleIdentifier != null){
                            final Module module = notationSpecificPlugins.getModule(moduleIdentifier);

                            if(module == null){
                                messages.add(new ConfigurationDifference(
                                        ConfigurationDifference.ChangeType.MISSING_MODULE, moduleIdentifier, notationIdentifier)
                                );
                            }
                        } else {
                            // should never happened
                            LOG.error("Missing module identifier info in the project file.");
                        }
                    }
                }
            }

        } else {
            // should never happened
            LOG.error("Missing notation identifier info in the project file.");
        }


    }

    private static void processBasicInfo(final Node node, final List<ConfigurationDifference> messages) {
        // not important for projects
        // possible usage for special purposes 
    }

    /**
     * Reads node's attribute.
     *
     * @param node is the attribute node
     *
     * @return value of the attribute it exists, null otherwise
     */
    private static String readAttribute(final Node node){
        if(node != null){
            return node.getNodeValue();
        }

        return null;
    }

    private static String getProjectName(final String fileName) {
        return fileName.substring(0, fileName.lastIndexOf(ProjectService.FILE_EXTENSION_DELIMITER));
    }

    /**
     * Checks the basic validity of the project file.
     *
     * @param projectFile is the project file
     *
     * @return true if the project file is valid, false otherwise
     */
    private static boolean isValidFile(final File projectFile){
        if(projectFile == null){
            LOG.error("Undefined project file.");
            return false;
        }

        if(!projectFile.exists()){
            LOG.error("Not existing project file.");
            return false;
        }

        if(!projectFile.canRead()){
            LOG.error("NOt possible to read the project file.");
            return false;
        }

        if(!projectFile.isFile()){
            LOG.error("Project file is not a file.");
            return false;
        }

        if(!projectFile.getName().endsWith(ProjectService.PROJECT_FILE_EXTENSION)){
            LOG.error("Not a correct project file extension.");
            return false;
        }

        return true;
    }

}
