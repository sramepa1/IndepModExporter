package cz.cvut.promod.services.pluginLoaderService.utils;

import cz.cvut.promod.services.ModelerSession;

import java.util.List;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 13:59:06, 12.12.2009
 */

/**
 * PluginLoadErrors class make possible to collect error that occur during plugin loading and instantiating
 * and publish them later.
 */
public class PluginLoadErrors {
       
    public static final String MISSING_XSD_ERROR  =
            ModelerSession.getCommonResourceBundle().getString("pluginLoaderService.error.missing.xsd");
    public static final String NO_ERROR  =
            ModelerSession.getCommonResourceBundle().getString("pluginLoaderService.error.noError");
    public static final String XML_PARSING_ERROR =
            ModelerSession.getCommonResourceBundle().getString("pluginLoaderService.error.xmlParsing");
    public static final String CLASS_NAME_ERROR =
            ModelerSession.getCommonResourceBundle().getString("pluginLoaderService.error.className");
    public static final String FIND_CLASS_ERROR =
            ModelerSession.getCommonResourceBundle().getString("pluginLoaderService.error.findClass");
    public static final String CLASS_HIERARCHY_ERROR =
            ModelerSession.getCommonResourceBundle().getString("pluginLoaderService.error.classHierarchy");
    public static final String INSTANTIATION_ERROR =
            ModelerSession.getCommonResourceBundle().getString("pluginLoaderService.error.instantiation");
    public static final String NULLARY_NOTATION_NAME_ERROR =
            ModelerSession.getCommonResourceBundle().getString("pluginLoaderService.error.nullaryNotationName");
    public static final String EMPTY_NOTATION_NAME =
            ModelerSession.getCommonResourceBundle().getString("pluginLoaderService.error.emptyNotationName");
    public static final String NO_MODEL_FACTORY_ERROR =
            ModelerSession.getCommonResourceBundle().getString("pluginLoaderService.error.noModelFactory");
    public static final String INVALID_MODEL_FACTORY_ERROR =
            ModelerSession.getCommonResourceBundle().getString("pluginLoaderService.error.invalidModelFactory");
    public static final String NULLARY_ABBREVIATION_ERROR =
            ModelerSession.getCommonResourceBundle().getString("pluginLoaderService.error.nullaryAbbreviation");
    public static final String EMPTY_ABREVIATION_ERROR =
            ModelerSession.getCommonResourceBundle().getString("pluginLoaderService.error.emptyAbbreviation");
    public static final String IO_CONTROLLER_ERROR =
            ModelerSession.getCommonResourceBundle().getString("pluginLoaderService.error.ioController");
    public static final String NULLARY_FILE_EXTENSION_ERROR =
            ModelerSession.getCommonResourceBundle().getString("pluginLoaderService.error.nullaryFileExtension");
    public static final String EMPTY_FILE_EXTENSION_ERROR =
            ModelerSession.getCommonResourceBundle().getString("pluginLoaderService.error.emptyFileExtension");
    public static final String NOTATION_IDENTIFIER_DUPLICITY =
            ModelerSession.getCommonResourceBundle().getString("pluginLoaderService.error.notationIdentifierDuplicity");
    public static final String NULLARY_PLUGIN_IDENTIFIER =
            ModelerSession.getCommonResourceBundle().getString("pluginLoaderService.error.nullaryPluginIdentifier");
    public static final String LIKE_MODELER_PLUGIN_IDENTIFIER_ERROR =
            ModelerSession.getCommonResourceBundle().getString("pluginLoaderService.error.likeModelerpluginIdentifier");
    public static final String EMPTY_PLUGIN_IDENTIFIER_ERROR =
            ModelerSession.getCommonResourceBundle().getString("pluginLoaderService.error.emptyPluginIdentifier");
    public static final String NO_RELATED_NOTATION_IDENTIFIER =
            ModelerSession.getCommonResourceBundle().getString("pluginLoaderService.error.noRelatedNotationIdentifier");
    public static final String NULLARY_RELATED_NOTATION_IDENTIFIER_ERROR =
            ModelerSession.getCommonResourceBundle().getString("pluginLoaderService.error.nullaryRelatedNotationIdentifier");
    public static final String EMPTY_RELATED_NOTATION_IDENTIFIER_ERROR =
            ModelerSession.getCommonResourceBundle().getString("pluginLoaderService.error.emptyRelatedNotationIdentifier");
    public static final String MODULE_IDENTIFIER_DUPLICITY_ERROR =
            ModelerSession.getCommonResourceBundle().getString("pluginLoaderService.error.moduleIdentifierDuplicity");
    public static final String EXTENSION_IDENIFIER_DUPLICITY_ERROR =
            ModelerSession.getCommonResourceBundle().getString("pluginLoaderService.error.extensionIdentifierDuplicity");
    public static final String PROJECT_FILE_LIKE_EXTENSION =
            ModelerSession.getCommonResourceBundle().getString("pluginLoaderService.error.projectFileLikeNotation");
    public static final String DUPLICITY_EXTENSION =
            ModelerSession.getCommonResourceBundle().getString("pluginLoaderService.error.notationExtensionDuplicity");
    public static final String TOO_LONG_IDENTIFIER =
            ModelerSession.getCommonResourceBundle().getString("pluginLoaderService.error.too.long.identifier");
    public static final String NULLARY_NAME_ERROR =
            ModelerSession.getCommonResourceBundle().getString("pluginLoaderService.error.nullaryPluginName");
    public static final String EMPTY_NAME_ERROR =
            ModelerSession.getCommonResourceBundle().getString("pluginLoaderService.error.emptyPluginName");

    private final String alias; // alias as is in the properties file where all plugin are

    private String fullClassName = null;

    public enum ERRORS {
        /** initial value, no error has occurred */
        noError,

        /** not possible to parse plugin definition file, file doesn't fulfil the xsd schema is one of possible reasons */
        missingXSD,

        /** not possible to parse plugin definition file, file doesn't fulfil the xsd schema is one of possible reasons */
        xmlParsing,

        /** invalid class name, null or empty */
        className,

        /** ClassLoader couldn't find the class */
        findClass,

        /** class does NOT implement necessary interface (Notation, Module, ...) */
        classHierarchy,

        /** and error during class instantiation has occurred */
        instantiation,

        /** notation returns nullary notation name */
        nullaryNotationName,

        /** notation returns an empty string as it's notation name */
        emptyNotationName,

        /** notation doesn't provide ModelFactory definition */
        noModelFactory,

        /** notation doesn't provide valid ModelFactory definition */
        invalidModelFactory,

        /** notation returns nullary abbreviation */
        nullaryAbbreviation,

        /** notation returns an empty String as it's abbreviation */
        emptyAbbreviation,

         /** notation doesn't provide IOController definition */
        ioController,

        /** notation's ioController provides nullary file extension */
        nullaryFileExtension,

        /** notation's ioController provides an empty string as it's file extension */
        emptyFileExtension,

        /** notation has the same file extension like the ProMod project file */
        projectFileLikeExtension,

        /** notation has the same file extension like the ProMod project file */
        notationExtensionDuplicity,

        /** notation has the same identifier as another notation that has been already loaded */
        notationIdentifierDuplicity,

        /** nullary plugin name */
        nullaryName,

        /** an empty string for plugin name */
        emptyName,

        /** plugin returns nullary identifier */
        nullaryPluginIdentifier,

        /** plugin returns identifier that is same like modeler identifier */
        likeModelerpluginIdentifier,

        /** plugin returns an empty string as it's identifier */
        emptyPluginIdentifier,

        /** plugin returns an too long identifier */
        tooLongPluginIdentifier,

        /** module provides no existing notation identifier */
        noRelatedNotationIdentifier,

        /** module provides nullary related notation identifier */
        nullaryRelatedNotationIdentifier,

        /** module provides an empty string as related notation identifier */
        emptyRelatedNotationIdentifier,

        /** more than one module for the same notation have the same identifier */
        moduleIdentifierDuplicity,

        /** extension has the same identifier as another extension that has been already loaded */
        extensionIdentifierDuplicity,
    }

    private ERRORS error = ERRORS.noError;

    private String message = null;

    /**
     * Constructs new PluginLoadErrors for plugin with alias.
     *
     * @param alias is the plugin's alias
     */
    public PluginLoadErrors(final String alias) {
        this.alias = alias;
    }

    /**
     * Stores error type that has occurred and adds the error to the list of errors.
     *
     * @param error is the error that has occurred
     * @param errorList is the list of errors
     */
    public void reportError(final ERRORS error, final List<PluginLoadErrors> errorList){
        this.error = error;
        errorList.add(this);
    }

    /**
     * Stores error type that has occurred with additional message specification and
     * adds the error to the list of errors.
     *
     * @param error is the error that has occurred
     * @param message is detail info about the error
     * @param errorList is the list of errors
     */
    public void reportError(final ERRORS error, final String message, final List<PluginLoadErrors> errorList){
        this.error = error;
        this.message = message;
        errorList.add(this);
    }

    /**
     * Returns the error type.
     *
     * @return error type
     */
    public ERRORS getError() {
        return error;
    }

    /**
     * Returns translation for the error.
     *
     * @return translated error information
     */
    public String getTranslatedError(){
        switch (error){
        case missingXSD:
            return MISSING_XSD_ERROR;
        case noError:
            return NO_ERROR;
        case xmlParsing:
            return XML_PARSING_ERROR;
        case className:
            return CLASS_NAME_ERROR;
        case findClass:
            return FIND_CLASS_ERROR;
        case classHierarchy:
            return CLASS_HIERARCHY_ERROR;
        case instantiation:
            return INSTANTIATION_ERROR;
        case nullaryNotationName:
            return NULLARY_NOTATION_NAME_ERROR;
        case emptyNotationName:
            return EMPTY_NOTATION_NAME;
        case noModelFactory:
            return NO_MODEL_FACTORY_ERROR;
        case invalidModelFactory:
            return INVALID_MODEL_FACTORY_ERROR;
        case nullaryAbbreviation:
            return NULLARY_ABBREVIATION_ERROR;
        case emptyAbbreviation:
            return EMPTY_ABREVIATION_ERROR;
        case ioController:
            return IO_CONTROLLER_ERROR;
        case nullaryFileExtension:
            return NULLARY_FILE_EXTENSION_ERROR;
        case emptyFileExtension:
            return EMPTY_FILE_EXTENSION_ERROR;
        case notationIdentifierDuplicity:
            return NOTATION_IDENTIFIER_DUPLICITY;
        case nullaryPluginIdentifier:
            return NULLARY_PLUGIN_IDENTIFIER;
        case likeModelerpluginIdentifier:
            return LIKE_MODELER_PLUGIN_IDENTIFIER_ERROR;
        case emptyPluginIdentifier:
            return EMPTY_PLUGIN_IDENTIFIER_ERROR;
        case noRelatedNotationIdentifier:
            return NO_RELATED_NOTATION_IDENTIFIER;
        case nullaryRelatedNotationIdentifier:
            return NULLARY_RELATED_NOTATION_IDENTIFIER_ERROR;
        case emptyRelatedNotationIdentifier:
            return EMPTY_RELATED_NOTATION_IDENTIFIER_ERROR;
        case moduleIdentifierDuplicity:
            return MODULE_IDENTIFIER_DUPLICITY_ERROR;
        case extensionIdentifierDuplicity:
            return EXTENSION_IDENIFIER_DUPLICITY_ERROR;
        case nullaryName:
            return NULLARY_NAME_ERROR;
        case emptyName:
            return EMPTY_NAME_ERROR;
        case projectFileLikeExtension:
            return PROJECT_FILE_LIKE_EXTENSION;
        case notationExtensionDuplicity:
            return DUPLICITY_EXTENSION;
        case tooLongPluginIdentifier:
            return TOO_LONG_IDENTIFIER;
        default:
            return error.toString();
        }
    }

    /**
     * Returns the detail message about the error
     *
     * @return detail info about the error
     */
    public String getMessage() {
        return message;
    }

    /**
     * Returns the plugin's alias
     *
     * @return plugin's alias
     */
    public String getAlias() {
        return alias;
    }

    /**
     * Sets the plugin's class qualified name for better detail about the pertinent error
     *
     * @param fullClassName is the plugin's class qualified name
     */
    public void setFullClassName(final String fullClassName) {
        this.fullClassName = fullClassName;
    }

    /**
     * Returns the plugin's class qualified name for better detail about the pertinent error
     *
     * @return plugin's class qualified name for better detail about the pertinent error
     */
    public String getFullClassName() {
        return fullClassName;
    }
    
}
