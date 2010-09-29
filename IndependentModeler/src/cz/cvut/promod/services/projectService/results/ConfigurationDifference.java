package cz.cvut.promod.services.projectService.results;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 23:43:20, 8.2.2010
 */

/**
 * Dialog showing ProMod configuration changes between actual configuration and the one loaded from a project file.
 */
public class ConfigurationDifference {

    public static enum ChangeType{
        /**
         * There is referred a notation that does NOT exist in actual configuration of ProMod
         * Message contains the identifier of the non existing notation.
         */
        MISSING_NOTATION,

        /**
         * There is referred a module that does NOT exist in actual configuration of ProMod.
         * Message contains the identifier of the non existing module.
         */
        MISSING_MODULE,

        /**
         * There is referred a extension that does NOT exist in actual configuration of ProMod.
         * Message contains the identifier of the non existing extension.
         */
        MISSING_EXTENSION,

        /**
         * There is an existing notation but has a different full name.
         * Message contains the full name acquired from the project file.
         */
        DIFFERENT_FULL_NAME,

        /**
         * There is an existing notation but has a different abbreviation.
         * Message contains the abbreviation acquired from the project file.
         */
        DIFFERENT_ABBREVIATION,

        /**
         * There is an existing notation but has a different extension.
         * Message contains the extension acquired from the project file. 
         */
        DIFFERENT_EXTENSION,
    }

    private final ChangeType changeType;

    private final String message;

    private final String identifier;


    public ConfigurationDifference(final ChangeType changeType, final String message) {
        this.changeType = changeType;
        this.message = message;

        this.identifier = null;
    }

    public ConfigurationDifference(final ChangeType changeType, final String message, final String identifier) {
        this.changeType = changeType;
        this.message = message;

        this.identifier = identifier;
    }

    /**
     * @return the configuration change type
     */
    public ChangeType getChangeType() {
        return changeType;
    }

    /**
     * @return the configuration change message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @return the plugin identifier
     */
    public String getIdentifier() {
        return identifier;
    }
}
