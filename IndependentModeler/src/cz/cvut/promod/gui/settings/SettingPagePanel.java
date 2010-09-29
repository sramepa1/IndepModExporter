package cz.cvut.promod.gui.settings;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by IntelliJ IDEA.
 *
 * This Class represents Setting Page Panel. In this panel there will be individual components. There shouldn't be no
 * Ok/Cancel/Apply buttons. These buttons will be controlled by Independent Modeler. This class provides only Actions
 * which will be called when the OK/Cancel/Apply button is clicked.
 *
 * User: lucky
 * Date: 20.8.2010
 * Time: 18:49:04
 */
public abstract class SettingPagePanel extends JPanel {
    private List<ActionListener> applyActionEnabledListeners = new LinkedList<ActionListener>();

    /**
     * This panel doesn't need to be initialized in the constructor. Some Setting Pages won't event be printed, because
     * user won't select them.
     */
    public abstract void lazyInitialize();

    /**
     * Returns action which will be called when user clicks on the Apply button
     * @return Apply Action
     */
    public abstract AbstractAction getApplyAction();

    /**
     * Returns action which will be called when user clicks on the Cancel button
     * @return
     */
    public abstract AbstractAction getCancelAction();

    /**
     * Returns action which will be called when user clicks on the Ok button
     * @return
     */
    public abstract AbstractAction getOkAction();

    /**
     * Adds ActionListener which will be called when applyActionEnable is fired. This should be fired when user changes
     * something in the settings panel and thus Apply button should be enabled
     * @param lsnr Action Listener
     */
    public void addApplyActionEnabledListener(ActionListener lsnr) {
        if (! this.applyActionEnabledListeners.contains(lsnr)) {
            this.applyActionEnabledListeners.add(lsnr);
        }
    }

    /**
     * This method should be called when user changes something in the Setting Panel. This will tell Independent Modeler
     * that Apply button should be enabled
     */
    public void fireApplyActionEnable() {
        for (ActionListener action : this.applyActionEnabledListeners) {
            action.actionPerformed(null);
        }
    }
}
