package cz.cvut.promod.gui.settings.utils;

import com.jidesoft.dialog.AbstractDialogPage;

import javax.swing.*;
import java.awt.*;

import cz.cvut.promod.services.ModelerSession;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 16:32:45, 24.1.2010
 *
 *
 * Basic settings of the host application for the settings dialog.
 */
public class BasicSettingPage extends AbstractDialogPage {

    private static final String SETTINGS_LABEL = ModelerSession.getCommonResourceBundle().getString("modeler.settings");

    private final JLabel label = ModelerSession.getComponentFactoryService().createLabel("");


    public BasicSettingPage(final String name) {
        super(name);
    }

    public BasicSettingPage(final String name, final Icon icon) {
        super(name, icon);
    }

    public void lazyInitialize() {
        label.setText("[" + getName() + "] " + SETTINGS_LABEL);
        
        initLayout();
    }

    public void initLayout() {
        setLayout(new BorderLayout());

        label.setHorizontalAlignment(JLabel.CENTER);

        add(label, BorderLayout.CENTER);
    }

}
