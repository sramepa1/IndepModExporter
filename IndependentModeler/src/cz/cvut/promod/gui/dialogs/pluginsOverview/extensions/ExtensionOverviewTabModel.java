package cz.cvut.promod.gui.dialogs.pluginsOverview.extensions;

import cz.cvut.promod.services.ModelerSession;
import cz.cvut.promod.plugin.extension.Extension;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.util.List;
import java.util.LinkedList;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 17:20:11, 11.2.2010
 *
 * A Model component of ExtensionOverviewTab.
 */
public class ExtensionOverviewTabModel implements ListModel {

    private final List<ExtensionLabelWrapper> extensionsList;


    public ExtensionOverviewTabModel() {
        extensionsList = new LinkedList<ExtensionLabelWrapper>();

        initList();
    }

    private void initList() {
        for(final Extension extension : ModelerSession.getExtensionService().getExtensions()){
            extensionsList.add(new ExtensionLabelWrapper(extension.getIdentifier(), extension.getName()));
        }
    }

    public int getSize() {
        return extensionsList.size();
    }

    public Object getElementAt(int index) {
        return extensionsList.get(index);
    }

    public void addListDataListener(ListDataListener l) {

    }

    public void removeListDataListener(ListDataListener l) {

    }

    /**
     * Represents a list in list of extensions.
     */
    protected static class ExtensionLabelWrapper {

        private final String identifier;
        private final String displayName;

        private ExtensionLabelWrapper(final String identifier, final String displayName) {
            this.identifier = identifier;
            this.displayName = displayName;
        }

        public String getIdentifier() {
            return identifier;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }
}
