package cz.cvut.promod.services.pluginLoaderService;

import cz.cvut.promod.plugin.notationSpecificPlugIn.notation.Notation;
import cz.cvut.promod.plugin.notationSpecificPlugIn.notation.localIOController.NotationLocalIOController;
import cz.cvut.promod.plugin.notationSpecificPlugIn.module.Module;
import cz.cvut.promod.plugin.Plugin;
import cz.cvut.promod.plugin.extension.Extension;
import cz.cvut.promod.services.pluginLoaderService.utils.NotationSpecificPlugins;
import cz.cvut.promod.services.pluginLoaderService.utils.PluginLoadErrors;
import cz.cvut.promod.services.pluginLoaderService.utils.PluginDetails;
import cz.cvut.promod.services.pluginLoaderService.utils.ModifiableNotationSpecificPlugins;
import cz.cvut.promod.services.pluginLoaderService.errorHandling.PluginLoaderErrorHandler;
import cz.cvut.promod.services.projectService.ProjectService;
import cz.cvut.promod.gui.ModelerModel;
import cz.cvut.promod.resources.Resources;

import java.util.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;


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
 * Date: 22:09:56, 12.10.2009
 */

/**
 * Implementation of PluginLoaderService.
 */
public class PluginLoaderServiceImpl implements PluginLoaderService {

    private final Logger LOG = Logger.getLogger(PluginLoaderServiceImpl.class);

    /**
     * Specifies what kind of plugins is being loaded.
     * Only for inner services purposes.
     */
    private static enum LoadPlugInType {
        NOTATIONS_AND_MODULES,
        EXTENSION
    }

    private final static String ROOT_TAG = "promod-plugins";
    private final static String NOTATION_TAG = "notation";
    private final static String MODULE_TAG = "module";
    private final static String EXTENSION_TAG = "extension";

    private final static String CLASS_ATTRIBUTE = "class";
    private final static String CONFIG_ATTRIBUTE = "config";
    private final static String ALIAS_ATTRIBUTE = "alias";

    private final static String NOTATION_INTERFACE_QUALIFIED_NAME= "cz.cvut.promod.plugin.notationSpecificPlugIn.notation.Notation";
    private final static String MODULE_INTERFACE_QUALIFIED_NAME= "cz.cvut.promod.plugin.notationSpecificPlugIn.module.Module";
    private final static String IOCONTROLPLUGIN_INTERFACE_QUALIFIED_NAME= "cz.cvut.promod.plugin.extension.Extension";

    private final List<PluginDetails> notationDetails;
    private final List<PluginDetails> extensionDetails;

    private final List<ModifiableNotationSpecificPlugins> notationSpecificPluginsList;
    private final List<Extension> extensionList;

    /**
     * Holds all errors that occur during plugin loading, instantiating or preparing to use.
     */
    private final List<PluginLoadErrors> errorList;

    private final Class notationClass;
    private final Class moduleClass;
    private final Class extenderClass;


    public PluginLoaderServiceImpl() throws ClassNotFoundException{
        errorList = new LinkedList<PluginLoadErrors>();

        notationDetails = new LinkedList<PluginDetails>();
        extensionDetails = new LinkedList<PluginDetails>();

        notationSpecificPluginsList = new LinkedList<ModifiableNotationSpecificPlugins>();
        extensionList = new LinkedList<Extension>();

        notationClass = Class.forName(NOTATION_INTERFACE_QUALIFIED_NAME);
        moduleClass = Class.forName(MODULE_INTERFACE_QUALIFIED_NAME);
        extenderClass = Class.forName(IOCONTROLPLUGIN_INTERFACE_QUALIFIED_NAME);
    }

    /** {@inheritDoc}
     *
     * Not valid XML file won't be loaded.
     *
     * If there will be any need to load not certainly valid files in the future, it could be enough to skip the
     * XML validation. There are even error messages already prepared.
     */
    public boolean loadPlugInsDefinition(final String pluginDefinitionFile){
        final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

        final InputStream schemaInputStream = ClassLoader.getSystemResourceAsStream(
                Resources.CONFIG + Resources.PLUGINS_XSD_FILE
        );

        LOG.info("Validating plugin definition file against xsd file.");

        documentBuilderFactory.setNamespaceAware(true);
        documentBuilderFactory.setValidating(false);

        final Schema schema;
        final SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        final Source source = new StreamSource(schemaInputStream);
        try {
            schema = schemaFactory.newSchema(source);

        } catch (SAXException e) {
            LOG.info("Skipping plugin definition file validation.");
            final PluginLoadErrors error = new PluginLoadErrors(ModelerModel.MODELER_IDENTIFIER);
            error.reportError(PluginLoadErrors.ERRORS.xmlParsing,  errorList);
            return  false;
        }

        final Document document;

        try {
            final DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            documentBuilder.setErrorHandler(new PluginLoaderErrorHandler());

            document = documentBuilder.parse(new File(pluginDefinitionFile));

            final Validator validator = schema.newValidator();

            validator.validate(new DOMSource(document));

        } catch (ParserConfigurationException e) {
            LOG.error("XML Parser configuration has failed.", e);
            final PluginLoadErrors error = new PluginLoadErrors(ModelerModel.MODELER_IDENTIFIER);
            error.reportError(PluginLoadErrors.ERRORS.xmlParsing, e.getMessage(), errorList);
            return false;

        } catch (IOException e) {
            LOG.error("An IO error has occurred when reading the plugin info xml file.", e);
            final PluginLoadErrors error = new PluginLoadErrors(ModelerModel.MODELER_IDENTIFIER);
            error.reportError(PluginLoadErrors.ERRORS.xmlParsing, e.getMessage(), errorList);
            return false;

        } catch (SAXException e) {
            LOG.error("An SAX error has occurred when reading the plugin info xml file.", e);
            final PluginLoadErrors error = new PluginLoadErrors(ModelerModel.MODELER_IDENTIFIER);
            error.reportError(PluginLoadErrors.ERRORS.xmlParsing, e.getMessage(), errorList);
            return false;
            
        } catch (NullPointerException exception){
            LOG.error("Nullary info about the plugin specification.", exception);
            final PluginLoadErrors error = new PluginLoadErrors(ModelerModel.MODELER_IDENTIFIER);
            error.reportError(PluginLoadErrors.ERRORS.xmlParsing, exception.getMessage(), errorList);
            return false;
        }

        return document != null && loadPluginDetails(document);
    }

    /**
     * Loads plugin details = aliases, classes, config files and save this information for future use,
     *
     * @param document is the DOM document of plugin definition file containing plugin details
     *
     * @return true if no error occurs, false otherwise
     */
    private boolean loadPluginDetails(final Document document){
        final NodeList rootNodeList = document.getElementsByTagName(ROOT_TAG);
        if(rootNodeList.getLength() != 1){
            LOG.error("More than one (or missing) root node in plugin xml file.");
            final PluginLoadErrors error = new PluginLoadErrors(ModelerModel.MODELER_IDENTIFIER);
            error.reportError(PluginLoadErrors.ERRORS.xmlParsing, errorList);
            return false;
        }

        final Node rootNode = rootNodeList.item(0);

        final NodeList nodeList = rootNode.getChildNodes();

        for(int i = 0; i < nodeList.getLength(); i++){
            final Node notationNode = nodeList.item(i);

            if(notationNode.getNodeName().equals(NOTATION_TAG)){
                NamedNodeMap attributes = notationNode.getAttributes();

                final PluginDetails notationDetail = new PluginDetails(
                        readAttribute(attributes.getNamedItem(CLASS_ATTRIBUTE)),
                        readAttribute(attributes.getNamedItem(CONFIG_ATTRIBUTE)),
                        readAttribute(attributes.getNamedItem(ALIAS_ATTRIBUTE))
                );

                final NodeList moduleList = notationNode.getChildNodes();

                for(int j = 0; j < moduleList.getLength(); j++){
                    final Node moduleNode = moduleList.item(j);

                    if(moduleNode.getNodeName().equals(MODULE_TAG)){
                        attributes = moduleNode.getAttributes();

                        final PluginDetails moduleDetail = new PluginDetails(
                                readAttribute(attributes.getNamedItem(CLASS_ATTRIBUTE)),
                                readAttribute(attributes.getNamedItem(CONFIG_ATTRIBUTE)),
                                readAttribute(attributes.getNamedItem(ALIAS_ATTRIBUTE))
                        );

                        notationDetail.addPluginDetails(moduleDetail);
                    }
                }

                notationDetails.add(notationDetail);

            } else if(notationNode.getNodeName().equals(EXTENSION_TAG)){
                final NamedNodeMap attributes = notationNode.getAttributes();

                final PluginDetails extensionDetail = new PluginDetails(
                        readAttribute(attributes.getNamedItem(CLASS_ATTRIBUTE)),
                        readAttribute(attributes.getNamedItem(CONFIG_ATTRIBUTE)),
                        readAttribute(attributes.getNamedItem(ALIAS_ATTRIBUTE))
                );

                extensionDetails.add(extensionDetail);
            }
        }

        return true;
    }

    /** {@inheritDoc} */
    public List<PluginDetails> getNotationModulesInstInfo() {
        return Collections.unmodifiableList(notationDetails);
    }

    /** {@inheritDoc} */
    public List<PluginDetails> getExtensionInstInfo() {
        return Collections.unmodifiableList(extensionDetails);
    }

    /**
     * Reads node's attribute.
     *
     * @param node is the attribute node
     *
     * @return value of the attribute it exists, null otherwise
     */
    private String readAttribute(final Node node){
        if(node != null){
            return node.getNodeValue();
        }

        return null;
    }

    /** {@inheritDoc} */
    public void instantiatePlugins() {
        instantiatePlugins(LoadPlugInType.NOTATIONS_AND_MODULES);
        instantiatePlugins(LoadPlugInType.EXTENSION);
    }

    /**
     * Instantiate plugins accordingly to the information loaded from plugin configuration file and store this instances
     * for future use.
     *
     * @param loadPlugInType is the type of plugins that are supposed to be instantiated
     *
     * @see cz.cvut.promod.services.pluginLoaderService.PluginLoaderServiceImpl.LoadPlugInType
     */
    private void instantiatePlugins(LoadPlugInType loadPlugInType){
        final List<PluginDetails> pluginDetailsList;
        Class plugInTypeInterface = null;

        pluginDetailsList = getRequiredList(loadPlugInType);

        if(pluginDetailsList == null){
            return;
        }

        for(final PluginDetails details : pluginDetailsList){
            String notationIdentifier = null; // when modules are being loaded, this holds the related notation id.

            PluginDetails pluginDetail = details;

            switch (loadPlugInType){
                case NOTATIONS_AND_MODULES:
                    plugInTypeInterface = notationClass;
                    break;
                case EXTENSION:
                    plugInTypeInterface = extenderClass;
                    break;
            }

            do{
                final String alias = pluginDetail.getAlias();

                if(alias == null || alias.isEmpty()) {
                    LOG.error("Skipping loading of an plugin - not provided alias.");
                    break;
                }

                final PluginLoadErrors error = new PluginLoadErrors(alias);

                final String fullClassName = pluginDetail.getClazz();

                if(!isValidClassName(fullClassName)){
                    LOG.error("Due to error(s) skipping loading of plugin defined by '" + pluginDetail + "' property.");
                    error.reportError(PluginLoadErrors.ERRORS.className, errorList);
                    break;
                }

                error.setFullClassName(fullClassName); // set the non empty and non nullary class name

                final Class clazz = getPluginClass(fullClassName);
                if(clazz == null) {
                    LOG.error("Due to error(s) skipping loading of " + alias + ".");
                    error.reportError(PluginLoadErrors.ERRORS.findClass, errorList);
                    break;
                }

                if(!implementsInterfaces(clazz, plugInTypeInterface)){
                    LOG.error("Due to error(s) skipping loading of " + clazz.getName() + ".");
                    error.reportError(PluginLoadErrors.ERRORS.classHierarchy, errorList);
                    break;
                }

                final Plugin plugin = instantiatePlugin(
                        clazz, pluginDetail, error, notationIdentifier, plugInTypeInterface == moduleClass);
                if(plugin == null) {
                    LOG.error("Due to error(s) skipping loading of " + clazz.getName() + ".");
                    break;
                }

                //save the loaded plugin
                if(!storePluginObject(plugInTypeInterface, plugin, error)){
                    LOG.error("Due to error(s) skipping loading of " + clazz.getName() + ".");
                }

                // instantiate modules
                pluginDetail = details.pop();
                plugInTypeInterface = moduleClass;

                // store temporarily the notation identifier for instantiating of it's related modules
                if(plugin instanceof Notation && notationIdentifier == null){
                    notationIdentifier = plugin.getIdentifier();
                }

            } while(pluginDetail != null);
        }
    }

    /**
     * Return required list of plugin details.
     *
     * @param loadPlugInType is the type of plugins
     *
     * @return required list or null if any error occurs
     */
    private List<PluginDetails> getRequiredList(final LoadPlugInType loadPlugInType) {
        switch (loadPlugInType){
            case NOTATIONS_AND_MODULES:
                return notationDetails;
            case EXTENSION:
                return extensionDetails;
            default:
                LOG.error("Unknown plugIn type has been recognized.");
        }

        return null;
    }

    /**
     * Checks the qualified class name.
     *
     * @param fullClassName is the qualified class name
     * @return true if the fullClassName is not null or an empty string, false otherwise
     */
    private boolean isValidClassName(final String fullClassName) {
        if(fullClassName == null){
            LOG.error("Nullary plugin class name.");
            return false;
        }

        if(fullClassName.isEmpty()){
            LOG.error("Empty plugin class name.");
            return false;
        }
        
        return true;
    }

    /**
     * Returns an instance of Class class representing the plugin class.
     *
     * @param fullClassName is the qualified name of the class
     *
     * @return in instance of Class if the plugin class definition is accessible, false otherwise
     */
    private Class getPluginClass(final String fullClassName){
        final Class clazz;
        try {
            // get plugin class
            clazz = Class.forName(fullClassName);

        } catch (ClassNotFoundException exception) {
            LOG.error("Wrong pluIn class definition, " + fullClassName + ".", exception);
            return null;

        } catch (NoClassDefFoundError error){
            LOG.error("Wrong pluIn class definition, " + fullClassName + ".", error);
            return null;

        } catch(LinkageError error){
            LOG.error("Wrong pluIn class definition, " + fullClassName + ".", error);
            return null;
        }


        return clazz;
    }

    /**
     * Checks whether the plugin implements necessary interfaces (Notation, Module, etc.).
     *
     * @param clazz that is supposed to implement an interface specified by the second argument
     * @param plugInTypeInterface the plugin specific interface (Notation, Module, etc.)
     * 
     * @return true if necessary the clazz implements necessary interface, false otherwise
     */
    private boolean implementsInterfaces(final Class clazz, final Class plugInTypeInterface){
        boolean implementsRequiredInterface = false;
        for(final Class implementedInterface : clazz.getInterfaces()){
            if(plugInTypeInterface == implementedInterface){
                implementsRequiredInterface = true;
                break;
            }
        }

        if(!implementsRequiredInterface){
            LOG.error("Plugin ("+ clazz.getName() +") doesn't implement required notation + (" + plugInTypeInterface.getSimpleName() + ").");
        }

        return implementsRequiredInterface;
    }

    /**
     * Instantiate a class provided as a argument of this method.
     *
     * @param clazz definition of class to be instantiated
     * @param pluginDetails holds info about the plugin that is supposed to be instantiated
     * @param error to report errors
     * @param notationIdentifier is the identifier of notation, used when modules are being instantiated
     * @param isModule indicates if the module is going to be instantiated or not
     * @return an object of the class (plugin) if no error occurs, null otherwise
     */
    private Plugin instantiatePlugin(final Class clazz,
                                     final PluginDetails pluginDetails,
                                     final PluginLoadErrors error,
                                     final String notationIdentifier,
                                     final boolean isModule){
        try {
            Plugin plugin;

            final String configFilePath = pluginDetails.getConfig();
            final File configFile;

            if(configFilePath != null){
                // use constructor taking a file as argument 

                configFile = new File(configFilePath);

                if(!checkConfigFile(configFile, pluginDetails.getAlias())){
                    LOG.error("Configuration data provided is not a valid file. Skipping plugin instantiation, " + pluginDetails.getAlias() + ".");
                    error.reportError(PluginLoadErrors.ERRORS.instantiation, errorList);
                    return null;

                }
            } else {
                configFile = null;
            }

            Constructor moduleFileIdentifierConstructor = null;
            Constructor moduleIdentifierConstructor = null;
            Constructor fileConstructor = null;

            for(final Constructor constructor : clazz.getConstructors()){
                final Class<?>[] parameters = constructor.getParameterTypes();

                if(isModule){
                    // try to find special constructors for modules

                    if((parameters.length == 2) && parameters[0].equals(File.class) && parameters[1].equals(String.class)){
                        moduleFileIdentifierConstructor = constructor;
                    }
                    else if((parameters.length == 1) && parameters[0].equals(String.class)){
                        moduleIdentifierConstructor = constructor;
                    }
                }

                if((parameters.length == 1) && parameters[0].equals(File.class)){
                    fileConstructor = constructor;
                }
            }

            if(moduleFileIdentifierConstructor != null){
                plugin = (Plugin) moduleFileIdentifierConstructor.newInstance(configFile, notationIdentifier);

            } else if (moduleIdentifierConstructor != null){
                plugin = (Plugin) moduleIdentifierConstructor.newInstance(notationIdentifier);

            } else if(fileConstructor != null){
                // there is a required constructor
                plugin = (Plugin) fileConstructor.newInstance(configFile);

            } else {
                // configuration data not provided, use parameter-less constructor
                plugin = (Plugin) clazz.newInstance();
            }

            return plugin;

        } catch (InstantiationException e) {
            LOG.error("PluginLoaderService could not instantiate plugin object, full class name: " + clazz.getName() + ".");
            error.reportError(PluginLoadErrors.ERRORS.instantiation, e.getMessage(), errorList);

        } catch (IllegalAccessException e) {
            LOG.error("PluginLoaderService could not access parameter-less constructor of plugin, full class name: " + clazz.getName() + ".");
            error.reportError(PluginLoadErrors.ERRORS.instantiation, e.getMessage(), errorList);

        } catch (InvocationTargetException e) {
            LOG.error("Nullary constructor error.", e);
            error.reportError(PluginLoadErrors.ERRORS.instantiation, e.getMessage(), errorList);
        }

        return null;
    }

    /**
     * Stores instantiated plugin for future use.
     *
     * @param loadPlugInType is the plugin type
     * @param plugin is the object of plugin
     * @param error to report errors
     *
     * @return true if no error(s) occurred, false otherwise
     */
    private boolean storePluginObject(final Class loadPlugInType,
                                      final Plugin plugin,
                                      final PluginLoadErrors error) {

        if(plugin != null){

            if(loadPlugInType == notationClass){
                    final Notation notation = (Notation) plugin;
                    return  storeNotation(notation, error);
            }
            else if(loadPlugInType == moduleClass){
                    final Module module = (Module) plugin;
                    return storeModule(module, error);
            }
            else if(loadPlugInType == extenderClass){
                    final Extension extension = (Extension) plugin;
                    return storeExtension(extension, error);
            }
            else {
                LOG.error("Unknown plugin type.");
            }
        }

        return false;
    }

    /**
     * Stores loaded and instantiated extension for future use.
     *
     * @param extension is the newly loaded and instantiated extension
     * @param error for error publishing
     * @return true if no error has occurred and the extension has been saved, false otherwise
     */
    private boolean storeExtension(final Extension extension, final PluginLoadErrors error) {

        if(!hasExtensionMandatoryData(extension, error)){
            return false;
        }

        if(!existsExtension(extension.getIdentifier())){
            extensionList.add(extension);

            return true;

        } else {
            LOG.error("Extension with duplicate identifier has been loaded. Skipping notation: " + extension.getIdentifier() + ".");
            error.reportError(PluginLoadErrors.ERRORS.extensionIdentifierDuplicity, errorList);
        }

        return false;
    }

    /**
     * Checks extension mandatory data.
     *
     * @param extension is the extension to be checked
     * @param error for error publishing
     * @return true if the extension fulfil requirements, false otherwise
     */
    private boolean hasExtensionMandatoryData(final Extension extension, final PluginLoadErrors error){
        if(!hasPluginMandatoryData(extension, error)){
            LOG.error("Extension do not have all it's mandatory data.");
            return false;
        }

        return true;
    }

    /**
     * Returns the loaded extension.
     *
     * @param extensionIdentifier is the identifier of required extension
     * @return loaded extension with given identifier, null if there is no such extension
     */
    private Extension getExtension(final String extensionIdentifier){
        for(final Extension extension : extensionList){
            if(extension.getIdentifier().equals(extensionIdentifier)){
                return extension;
            }
        }

        return null;
    }

    /**
     * @param extensionIdentifier is the identifier of an extension
     * @return true if such a extension with given identifier has been already loaded, false otherwise
     */
    private boolean existsExtension(final String extensionIdentifier){
        return getExtension(extensionIdentifier) != null;
    }

    /**
     * Checks and the stores instantiated notation for future use.
     *
     * @param notation is the newly instantiated notation
     * @param error for error publishing
     * @return true if the notation was successfully store for future use, false if any error occurs
     */
    private boolean storeNotation(final Notation notation, final PluginLoadErrors error) {

        if(!hasNotationMandatoryData(notation, error)){
            return false;
        }

        if(!existUniqueNotationIdentifier(notation.getIdentifier())){
            notationSpecificPluginsList.add(new ModifiableNotationSpecificPlugins(notation));

            return true;

        } else {
            LOG.error("Notation with duplicate identifier has been loaded. Skipping notation: " + notation.getIdentifier() + ".");
            error.reportError(PluginLoadErrors.ERRORS.notationIdentifierDuplicity, errorList);
        }

        return false;
    }

    /**
     * Checks whether given notation identifier is equal to any other loaded notation's identifier.
     *
     * @param identifier is the identifier to be checked
     * @return true if the identifier is unique, false otherwise
     */
    private boolean existUniqueNotationIdentifier(final String identifier){
        for(final ModifiableNotationSpecificPlugins notationSpecificPlugins : notationSpecificPluginsList){
            if(notationSpecificPlugins.getNotation().getIdentifier().equals(identifier)){
                return true;
            }
        }

        return false;
    }

    /**
     * Returns the required instance of NotationSpecificPlugins class.
     *
     * @param identifier is the identifier of notation which NotationSpecificPlugins instance should be found
     * @return required instance of NotationSpecificPlugins class, null if there is no notation with given identifier
     */
    private ModifiableNotationSpecificPlugins getNotationSpecificPlugins(final String identifier){
        for(final ModifiableNotationSpecificPlugins notationSpecificPlugins : notationSpecificPluginsList){
            if(notationSpecificPlugins.getNotation().getIdentifier().equals(identifier)){
                return notationSpecificPlugins;
            }
        }

        return null;
    }

    /**
     * Check all necessary condition that have to be accomplished to accept the module. If no problem occurs,
     * the module is stored and ready to use.
     *
     * @param module tested module
     * @param error to report errors
     * @return true if the module is acceptable and has been stored, false otherwise
     */
    private boolean storeModule(final Module module, final PluginLoadErrors error) {

        if(!hasModuleMandatoryData(module, error)){
            return false;
        }

        // check whether there is a notation to which is this module related
        if(!existUniqueNotationIdentifier(module.getRelatedNotationIdentifier())){
            LOG.error("Module without related notation has been loaded. Skipping module - "
                        + module.getIdentifier() + ", related notation identifier - " + module.getRelatedNotationIdentifier() + ".");

            error.reportError(PluginLoadErrors.ERRORS.noRelatedNotationIdentifier, errorList);

            return false;
        }

        // check whether the is already no module with the same module identifier for the same notation
        final ModifiableNotationSpecificPlugins notationSpecificPlugins = getNotationSpecificPlugins(module.getRelatedNotationIdentifier());

        if(notationSpecificPlugins.existModule(module.getIdentifier())){
            LOG.error("More than one module for the same notation have the same identifier");

            error.reportError(PluginLoadErrors.ERRORS.moduleIdentifierDuplicity, errorList);

            return false;
        }

        // if no error has not occurred so far, store the module
        notationSpecificPlugins.addModule(module);

        return true;
    }


    /**
     * Checks basic data of any instance of Plugin interface.
     *
     * @param plugin an instance of Plugin class to be checked.
     *
     * @param error to report errors
     * @return true if and only if all mandatory data is present and correct, false otherwise
     */
    private boolean hasPluginMandatoryData(final Plugin plugin, PluginLoadErrors error){
        if(plugin.getIdentifier() == null){
            LOG.error("Plugin identifier cannot be null.");
            error.reportError(PluginLoadErrors.ERRORS.nullaryPluginIdentifier, errorList);
            return false;
        }

        if(plugin.getIdentifier().trim().isEmpty()){
            LOG.error("Plugin identifier cannot be an empty string (white characters don't count).");
            error.reportError(PluginLoadErrors.ERRORS.emptyPluginIdentifier, errorList);
            return false;
        }

        if(plugin.getIdentifier().length() > PluginLoaderService.MAX_PLUGIN_IDENTIFIER_LENGTH){
            LOG.error("Plugin identifier length has exceeded the max number of symbols.");
            error.reportError(PluginLoadErrors.ERRORS.tooLongPluginIdentifier, errorList);
            return false;
        }

        if(plugin.getIdentifier().trim().equals(ModelerModel.MODELER_IDENTIFIER)){
            LOG.error("Plugin identifier cannot be a save as modeler identifier.");
            error.reportError(PluginLoadErrors.ERRORS.likeModelerpluginIdentifier, errorList);
            return false;
        }

        if(plugin.getName() == null){
            LOG.error("Extension name cannot be null.");
            error.reportError(PluginLoadErrors.ERRORS.nullaryName, errorList);
            return false;
        }

        if(plugin.getName().trim().isEmpty()){
            LOG.error("Extension name cannot be an empty string.");
            error.reportError(PluginLoadErrors.ERRORS.emptyName, errorList);
            return false;
        }

        return true;
    }

    /**
     * Checks basic data of any instance of Notation interface.
     *
     * @param notation that's data will be checked.
     * @param error to report errors 
     * @return true if and only if all mandatory data are available (don't has to be valid), false otherwise
     */
    private boolean hasNotationMandatoryData(final Notation notation, final PluginLoadErrors error){
        if(!hasPluginMandatoryData(notation, error)){
            LOG.error("Notation do not have all it's mandatory data.");
            return false;
        }

        if(notation.getFullName() == null){
            LOG.error("Notation full name cannot be null.");
            error.reportError(PluginLoadErrors.ERRORS.nullaryNotationName, errorList);
            return false;
        }

        if(notation.getFullName().trim().isEmpty()){
            LOG.error("Notation full name cannot be an empty string (white characters don't count).");
            error.reportError(PluginLoadErrors.ERRORS.emptyNotationName, errorList);
            return false;
        }

        if(notation.getDiagramModelFactory() == null){
            LOG.error("Notations has not provide an instance of DiagramModelFactory interface.");
            error.reportError(PluginLoadErrors.ERRORS.noModelFactory, errorList);
            return false;
        }

        if(notation.getDiagramModelFactory().createEmptyDiagramModel() == null){
            LOG.error("Notations has not provided an correct instance of DiagramModelFactory interface.");
            error.reportError(PluginLoadErrors.ERRORS.invalidModelFactory, errorList);
            return false;
        }

        if(notation.getAbbreviation() == null){
            LOG.error("Notation abbreviation cannot be null");
            error.reportError(PluginLoadErrors.ERRORS.nullaryAbbreviation, errorList);
            return false;
        }

        if(notation.getAbbreviation().trim().isEmpty()){
            LOG.error("Notation abbreviation cannot be an empty string (white characters don't count).");
            error.reportError(PluginLoadErrors.ERRORS.emptyAbbreviation, errorList);
            return false;
        }

        final NotationLocalIOController ioController = notation.getLocalIOController();

        if(ioController == null){
            LOG.error("No implementation of NotationLocalIOController provided.");
            error.reportError(PluginLoadErrors.ERRORS.ioController, errorList);
            return false;
        }

        if(ioController.getNotationFileExtension() == null){
            LOG.error("Notation file extension cannot be null");
            error.reportError(PluginLoadErrors.ERRORS.nullaryFileExtension, errorList);
            return false;
        }

        if(ioController.getNotationFileExtension().trim().isEmpty()){
            LOG.error("Notation file extension cannot be an empty string (white characters don't count).");
            error.reportError(PluginLoadErrors.ERRORS.emptyFileExtension, errorList);
            return false;
        }

        if(ioController.getNotationFileExtension().trim().equals(ProjectService.PROJECT_FILE_EXTENSION_NAME)){
            LOG.error("Notation file extension is equal with ProMod project file extension.");
            error.reportError(PluginLoadErrors.ERRORS.projectFileLikeExtension, errorList);
            return false;
        }

        for(final ModifiableNotationSpecificPlugins notationSpecificPlugins : notationSpecificPluginsList){
            if(notationSpecificPlugins.getNotation().getLocalIOController().getNotationFileExtension().equals(
                    ioController.getNotationFileExtension()
            )){
                LOG.error("Notation file extension has the same extension like any earlier loaded notation.");
                error.reportError(PluginLoadErrors.ERRORS.notationExtensionDuplicity, errorList);
                return false;
            }
        }

        return true;
    }

    /**
     * Check mandatory module data.
     *
     * @param module to be checked
     * @param error for error reporting
     * @return true if no error occurred, false otherwise
     */
    private boolean hasModuleMandatoryData(final Module module, final PluginLoadErrors error){
        if(!hasPluginMandatoryData(module, error)){
            LOG.error("Module do not have all it's mandatory data.");
            return false;
        }

        if(module.getRelatedNotationIdentifier() == null){
            LOG.error("Module's related notation identifier cannot be null");

            error.reportError(PluginLoadErrors.ERRORS.nullaryRelatedNotationIdentifier, errorList);
        }

        if(module.getRelatedNotationIdentifier().trim().isEmpty()){
            LOG.error("Module's related notation identifier cannot be an empty string (white characters don't count).");

            error.reportError(PluginLoadErrors.ERRORS.emptyRelatedNotationIdentifier, errorList);
        }

        return true;
    }

    /**
     * Check the configuration file of a plugin.
     *
     * @param configFile is the configuration file
     * @param alias is the plugin alias for logging purposes
     *
     * @return true if no error occurs, false otherwise
     */
    private boolean checkConfigFile(final File configFile, final String alias) {
        if(!configFile.exists()){
            LOG.error("Config file doesn't exist for plugin marked as '" + alias + "'");
            return false;
        }

        if(!configFile.isFile()){
            LOG.error("Config file is not a file for plugin marked as '" + alias + "'");
            return false;
        }

        if(!configFile.canRead()){
            LOG.error("Config file is not readable for plugin marked as '" + alias + "'");
            return false;
        }

        return true;
    }

    /** {@inheritDoc} */
    public List<NotationSpecificPlugins> getNotationSpecificPlugins() {
        final List<NotationSpecificPlugins> plugins = new LinkedList<NotationSpecificPlugins>();

        // ensures unmodifiable list of modules
        for(ModifiableNotationSpecificPlugins plugin : notationSpecificPluginsList){
            plugins.add(new NotationSpecificPlugins(plugin.getNotation(), Collections.unmodifiableList(plugin.getModules())));
        }

        // ensures unmodifiable list of notations and modules
        return Collections.unmodifiableList(plugins);
    }

    /** {@inheritDoc} */
    public List<Extension> getExtensions() {
        return Collections.unmodifiableList(extensionList);
    }

    /** {@inheritDoc} */
    public List<PluginLoadErrors> getErrors() {
        return Collections.unmodifiableList(errorList);
    }

    /** {@inheritDoc} */
    public boolean check() {       
        return true;
    }
}
