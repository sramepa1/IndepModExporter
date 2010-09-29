package cz.cvut.promod.gui.settings.utils;

import com.jidesoft.dialog.AbstractDialogPage;
import com.jidesoft.dialog.ButtonEvent;
import com.jidesoft.dialog.ButtonListener;
import com.jidesoft.dialog.ButtonNames;
import cz.cvut.promod.gui.settings.SettingPageData;
import cz.cvut.promod.gui.settings.SettingPagePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by IntelliJ IDEA.
 *
 * This Class creates AbstractDialogPage instance based on the SettingPageData instance. This class is used in the
 * Settings Dialog.
 *
 * User: lucky
 * Date: 20.8.2010
 * Time: 18:55:40
 */
public class SettingPage extends AbstractDialogPage {

    private SettingPageData data;

    /**
     * Creates new SettingPage
     * @param data SettingPageData which provides needed information about a Setting Page
     */
    public SettingPage(SettingPageData data) {
        this.data = data;
        String name = this.data.getName();
        Icon icon = this.data.getIcon();

        if (name != null) this.setTitle(this.data.getName());
        if (icon != null) this.setIcon(this.data.getIcon());

        this.initEventHandling();
    }

    public void lazyInitialize() {
        SettingPagePanel panel = this.data.getSettingPage();

        this.setLayout(new GridLayout(1, 1));
        if (panel != null) {
            panel.lazyInitialize();
            this.add(panel);
        }
    }

    /**
     * Inits event handling.
     */
    public void initEventHandling() {
        final AbstractAction applyAction = this.data.getSettingPage().getApplyAction();
        final AbstractAction cancelAction = this.data.getSettingPage().getCancelAction();
        final AbstractAction okAction = this.data.getSettingPage().getOkAction();

        addButtonListener(new ButtonListener() {
            public void buttonEventFired(ButtonEvent e) {
                if(e.getID() == 0){ // the button was clicked
                    if(ButtonNames.APPLY.equals(e.getButtonName())){
                        applyAction.actionPerformed(null);
                    }
                }
            }
        });

        addButtonListener(new ButtonListener() {
            public void buttonEventFired(ButtonEvent e) {
                if(ButtonNames.CANCEL.equals(e.getButtonName())){
                    cancelAction.actionPerformed(null);
                }
            }
        });

        addButtonListener(new ButtonListener() {
            public void buttonEventFired(ButtonEvent e) {
                if(ButtonNames.OK.equals(e.getButtonName())){
                    okAction.actionPerformed(null);
                }
            }
        });



        this.data.getSettingPage().addApplyActionEnabledListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);
            }
        });
    }
}
