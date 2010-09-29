package cz.cvut.promod.gui;

import cz.cvut.promod.gui.support.utils.NotationGuiHolder;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

import com.jidesoft.dialog.AbstractDialogPage;
import com.jidesoft.dialog.PageList;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 16:09:51, 10.10.2009
 */

/**
 * Data holder for the Modeler class.
 */
public class ModelerModel {

    public static final String APPLICATION_NAME = "ProMod";

    public static final String MODELER_IDENTIFIER = "modeler";

    private String selectedNotation = null; //must be null due to initial view update invoked by Modeler class constructor

    private Map<String, NotationGuiHolder> notationGuiHolders = new HashMap<String, NotationGuiHolder>();

    private final PageList settingPagesModel = new PageList();

    /**
     * @return NotationGuiHolder collection
     */
    public Collection<NotationGuiHolder> getNotationGuiHolders(){
      return notationGuiHolders.values();
    }

    /**
     * @param notationIdentifier is the notation identifier
     * @return required NotationGuiHolder
     */
    public NotationGuiHolder getNotationGuiHolder( final String notationIdentifier ){
      return notationGuiHolders.get(notationIdentifier);
    }

    /**
     * Adds a new NotationGuiHolder.
     * @param notationGuiHolder is the new NotationGuiHolder
     */
    public void addNotationGuiHolder( final NotationGuiHolder notationGuiHolder){
      notationGuiHolders.put(notationGuiHolder.getNotationIdentifier(), notationGuiHolder);
    }

    /**
     * @return selected notation identifier
     */
    public String getSelectedNotation() {
        return selectedNotation;
    }

    /**
     * Sets selected notation identifier
     * @param selectedNotation newly selected identifier
     */
    public void setSelectedNotation(final String selectedNotation) {
        this.selectedNotation = selectedNotation;
    }

    /**
     * Adds settings pages to the common settings dialog.
     * @param pages are the pages to be added
     */
    public void addSettingPages(final List<AbstractDialogPage> pages) {
        for(final AbstractDialogPage page : pages){
            settingPagesModel.append(page);
        }
    }

    /**
     * @return returns the model holding all settings pages  
     */
    public PageList getSettingPagesModel() {
        return settingPagesModel;
    }
}
