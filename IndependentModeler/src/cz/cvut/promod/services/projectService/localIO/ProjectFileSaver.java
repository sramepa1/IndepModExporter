package cz.cvut.promod.services.projectService.localIO;

import cz.cvut.promod.services.ModelerSession;
import cz.cvut.promod.services.pluginLoaderService.utils.NotationSpecificPlugins;
import cz.cvut.promod.services.projectService.treeProjectNode.ProjectRoot;
import cz.cvut.promod.services.projectService.utils.ProjectServiceUtils;
import cz.cvut.promod.plugin.notationSpecificPlugIn.notation.Notation;
import cz.cvut.promod.plugin.notationSpecificPlugIn.module.Module;
import cz.cvut.promod.plugin.extension.Extension;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.xml.stream.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.Date;

import javax.xml.transform.stax.StAXSource;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 17:49:07, 19.11.2009
 */
public class ProjectFileSaver {

    /**
     * Saves the project to the xml project file. The location is defined by the
     * location given by the project node and it's user object.
     *
     * There is saved some basic information about the project like project name,
     * project location, date and user, but attributes project name and location
     * are not important at all. This attributes are not use when the project is
     * being loaded.
     *
     * There are then saved some information about the ProMod current configuration.
     * Some basic information about currently loaded notations, modules and
     * extension is saved. This info can be used when the project is being
     * loaded to publish some errors or at least warn the user that there are
     * some changes in ProMod configuration and some functionality can be missing
     * or changed.
     *
     * @param projectTreePath is the tree path of the project navigation tree to the project root that is supposed
     * to be saved
     *
     * @param useFormater if true, then then the xml output will be formatted,
     * decreases performance
     *
     * @throws javax.xml.stream.XMLStreamException when an xml streaming error occurs
     *
     * @throws IOException an IO error has occurred
     *
     * @throws TransformerException specifies an exceptional condition that occurred during the transformation process
     *
     * @throws NullPointerException when the project location is null
     *
     * @throws IllegalArgumentException when the tree path to the project root is not valid
     */
    public static void saveProject(final TreePath projectTreePath, final boolean useFormater)
            throws XMLStreamException, IOException, TransformerException, NullPointerException, IllegalArgumentException {

        if(!ProjectServiceUtils.isValidTreePath(projectTreePath)){
            throw new IllegalArgumentException("Invalid tree path");
        }

        if(!ProjectServiceUtils.isProjectRoot((DefaultMutableTreeNode) projectTreePath.getLastPathComponent())){
            throw new IllegalArgumentException("Not a project root to save.");                        
        }

        final DefaultMutableTreeNode projectRootNode = (DefaultMutableTreeNode) projectTreePath.getLastPathComponent();

        final ProjectRoot projectRoot = (ProjectRoot) projectRootNode.getUserObject();

        final File projectFile = projectRoot.getProjectFile();

        if(projectFile == null){
            // should never happened
            throw new IOException("Nullary project location");
        }

        final File parentFile = projectFile.getParentFile();

        if(parentFile == null){
            throw new IOException("Nullary project location");
        }

        if(!parentFile.exists()){
            if(!parentFile.mkdirs()){
                throw new IOException("Unable to create necessary directories.");
            }
        }

        if(!projectFile.exists()){
            if(!projectFile.createNewFile()){
                throw new IOException("Impossible to create the project file");
            }
        }

        final XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();
        final XMLStreamWriter xmlStreamWriter;
        ByteArrayOutputStream byteArrayOutputStream = null;

        if(useFormater){
            byteArrayOutputStream = new ByteArrayOutputStream();
            xmlStreamWriter = xmlOutputFactory.createXMLStreamWriter(byteArrayOutputStream, XmlConsts.ENCODING);
        } else {
            xmlStreamWriter = xmlOutputFactory.createXMLStreamWriter(new FileOutputStream(projectRoot.getProjectFile()), XmlConsts.ENCODING);
        }

        xmlStreamWriter.writeStartDocument(XmlConsts.ENCODING, XmlConsts.VERSION);
        xmlStreamWriter.writeComment(XmlConsts.PROMOD_PROJECT_COMMENT);
        xmlStreamWriter.writeStartElement(XmlConsts.ROOT_ELEMENT);

        writeBasicProjectInfo(xmlStreamWriter, projectRoot);

        writeConfigurationInfo(xmlStreamWriter);

        xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeEndDocument();
        xmlStreamWriter.close();

        if(useFormater){
            final TransformerFactory transformerFactory = TransformerFactory.newInstance();
            final Transformer transformer = transformerFactory.newTransformer();

            transformer.setOutputProperty(OutputKeys.METHOD, XmlConsts.METHOD);
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, XmlConsts.NO);
            transformer.setOutputProperty(OutputKeys.ENCODING, XmlConsts.ENCODING);
            transformer.setOutputProperty(OutputKeys.INDENT, XmlConsts.YES);
            transformer.setOutputProperty(XmlConsts.INDEND_AMOUNT_PROPERTY, XmlConsts.INDEND_AMOUNT);

            final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                    byteArrayOutputStream.toByteArray()
            );

            XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
            XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(
                    byteArrayInputStream
            );

            transformer.transform(
                    new StAXSource(xmlStreamReader), new StreamResult(projectRoot.getProjectFile()));
        }

    }

    /**
     * Appends current ProMod configuration data to the project xml file.
     * It is that possible to publish error or warning if the project is
     * loaded bt the ProMod having different configuration.
     *
     * It means, that there could be some missing notations, modules, extensions,
     * or the notation could be the same but it can use different file extension
     * and so on.
     *
     * @param xmlStreamWriter is a instance of XMLStreamWriter to that are the
     * elements supposed to be written
     *
     * @throws javax.xml.stream.XMLStreamException when an xml streaming error occurs
     */
    private static void writeConfigurationInfo(final XMLStreamWriter xmlStreamWriter) throws XMLStreamException {
        xmlStreamWriter.writeStartElement(XmlConsts.CONFIGURATION_ELEMENT);
            xmlStreamWriter.writeStartElement(XmlConsts.PLUGINS_ELEMENT);
                writeNotationsInfo(xmlStreamWriter);
                writeExtensionsInfo(xmlStreamWriter);
            xmlStreamWriter.writeEndElement();
        xmlStreamWriter.writeEndElement();
    }

    /**
     * Appends information about all notations and their related modules to the
     * project xml file.
     *
     * @param xmlStreamWriter is a instance of XMLStreamWriter to that are the
     * elements supposed to be written
     *
     * @throws javax.xml.stream.XMLStreamException when an xml streaming error occurs
     */
    private static void writeNotationsInfo(final XMLStreamWriter xmlStreamWriter) throws XMLStreamException {
        for(final String notationIdentifier : ModelerSession.getNotationService().getNotationsIdentifiers()){
            final NotationSpecificPlugins notationSpecificPlugins  =
                  ModelerSession.getNotationService().getNotationSpecificPlugins(notationIdentifier);
            final Notation notation = notationSpecificPlugins.getNotation();

            xmlStreamWriter.writeStartElement(XmlConsts.NOTATION_ELEMENT);
                xmlStreamWriter.writeAttribute(XmlConsts.IDENTIFIER_ATTRIBUTE, notation.getIdentifier());
                xmlStreamWriter.writeAttribute(XmlConsts.FULL_NAME_ATTRIBUTE, notation.getFullName());
                xmlStreamWriter.writeAttribute(XmlConsts.ABBREVIATION_ATTRIBUTE, notation.getAbbreviation());
                xmlStreamWriter.writeAttribute(XmlConsts.EXTENSION_ATTRIBUTE, notation.getLocalIOController().getNotationFileExtension());

                for(final Module module : notationSpecificPlugins.getModules()){
                    xmlStreamWriter.writeStartElement(XmlConsts.MODULE_ELEMENT);
                        xmlStreamWriter.writeAttribute(XmlConsts.IDENTIFIER_ATTRIBUTE, module.getIdentifier());
                    xmlStreamWriter.writeEndElement();                                  
                }
            xmlStreamWriter.writeEndElement();
        }
    }

    /**
     * Appends basic extensions info to the project xml file.
     *
     * @param xmlStreamWriter is a instance of XMLStreamWriter to that are the
     * extensions supposed to be written
     * 
     * @throws javax.xml.stream.XMLStreamException when an xml streaming error occurs
     */
    private static void writeExtensionsInfo(final XMLStreamWriter xmlStreamWriter) throws XMLStreamException {
        for(final Extension extension : ModelerSession.getExtensionService().getExtensions()){
            xmlStreamWriter.writeStartElement(XmlConsts.EXTENSION_ELEMENT);
                xmlStreamWriter.writeAttribute(XmlConsts.IDENTIFIER_ATTRIBUTE, extension.getIdentifier());
                xmlStreamWriter.writeAttribute(XmlConsts.FULL_NAME_ATTRIBUTE, extension.getName());
                xmlStreamWriter.writeAttribute(XmlConsts.DESCRIPTION_ATTRIBUTE, extension.getDescription());
            xmlStreamWriter.writeEndElement();
        }
    }

    /**
     * Appends basic project specific information to the project xml file.
     *
     * @param xmlOutputFactory is a instance of XMLStreamWriter to that are the
     * elements supposed to be written
     *
     * @param projectRoot is the project root that is supposed to be written to
     * the xml file
     *
     * @throws javax.xml.stream.XMLStreamException when an xml streaming error occurs
     */
    private static void writeBasicProjectInfo(final XMLStreamWriter xmlOutputFactory, final ProjectRoot projectRoot)
            throws XMLStreamException {

        xmlOutputFactory.writeStartElement(XmlConsts.BASIC_INFO_ELEMENT);

            xmlOutputFactory.writeStartElement(XmlConsts.LAST_NAME_ELEMENT);
                xmlOutputFactory.writeCharacters(projectRoot.getDisplayName());
            xmlOutputFactory.writeEndElement();

            xmlOutputFactory.writeStartElement(XmlConsts.LAST_LOCATION_ELEMENT);
                xmlOutputFactory.writeCharacters(projectRoot.getProjectFile().getAbsolutePath());
            xmlOutputFactory.writeEndElement();

            xmlOutputFactory.writeStartElement(XmlConsts.LAST_DATE_ELEMENT);
                xmlOutputFactory.writeCharacters(new Date().toString());
            xmlOutputFactory.writeEndElement();

            xmlOutputFactory.writeStartElement(XmlConsts.LAST_USER_ELEMENT);
                ModelerSession.getUserService().getUser();
            xmlOutputFactory.writeEndElement();

        xmlOutputFactory.writeEndElement();
    }
}
