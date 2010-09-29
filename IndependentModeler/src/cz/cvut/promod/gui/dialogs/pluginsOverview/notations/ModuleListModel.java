package cz.cvut.promod.gui.dialogs.pluginsOverview.notations;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.util.List;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 4:36:19, 17.4.2010
 *
 * Holder of loaded module's info for NotationOverviewTab.
 */
public class ModuleListModel implements ListModel {

    private final List<ModuleLabelWrapper> modulesList;

    public ModuleListModel(final List<ModuleLabelWrapper> modulesList){
        this.modulesList = modulesList;
    }

    public int getSize() {
        return modulesList.size();
    }

    public Object getElementAt(int index) {
        try{
        return modulesList.get(index);
        } catch (IndexOutOfBoundsException e){
            return null;
        }
    }

    public void addListDataListener(ListDataListener l) {}

    public void removeListDataListener(ListDataListener l) {}


    /**
     * Represents a list in list of extensions.
     */
    public static class ModuleLabelWrapper {

        private final String identifier;
        private final String displayName;

        public ModuleLabelWrapper(final String identifier, final String displayName) {
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
