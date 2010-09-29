/*
 * @(#)TipsOfTheDayDemo.java
 *
 * Copyright 2002 - 2003 JIDE Software. All rights reserved.
 */

import com.jidesoft.dialog.ButtonPanel;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.JideSwingUtilities;
import com.jidesoft.tipoftheday.ResourceBundleTipOfTheDaySource;
import com.jidesoft.tipoftheday.TipOfTheDayDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

/**
 * Demoed Component: {@link TipOfTheDayDialog} <br> Required jar files: jide-common.jar,
 * jide-dialogs.jar <br> Required L&F: Jide L&F extension required
 */
public class TipsOfTheDayDemo extends AbstractDemo {

    public static final String PROFILE_NAME = "jiedsoft-tip";
    private static final long serialVersionUID = -8937175174621328524L;

    public String getName() {
        return "Tips of the Day Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_DIALOGS;
    }

    @Override
    public String getDescription() {
        return "A lot of applications use Tips of the Day dialog to get a new user up and running quickly.  The content of Tips of the Day is little different from the online help document, but most users prefer to read Tips of the Day because they are concise and they only take a short amount of time per day.  To allow more applications to easily create their own Tips of the Day, we created the TipOfTheDayDialog component. \n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.tipoftheday.TipOfTheDayDialog\n";
    }

    public Component getDemoPanel() {
        ButtonPanel buttonPanel = new ButtonPanel(SwingConstants.TOP);
        buttonPanel.addButton(new JButton(new AbstractAction("Show Tips of the Day Dialog") {
            private static final long serialVersionUID = -7078485316239651300L;

            public void actionPerformed(ActionEvent e) {
                showTipsOfTheDay();
            }
        }));
        return buttonPanel;
    }

    @Override
    public String getDemoFolder() {
        return "W3.TipsOfTheDay";
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new TipsOfTheDayDemo());
    }

    private static boolean getPrefBooleanValue(String key, boolean defaultValue) {
        Preferences prefs = Preferences.userRoot();
        return prefs.node(PROFILE_NAME).getBoolean(key, defaultValue);
    }

    private static void setPrefBooleanValue(String key, boolean value) {
        Preferences prefs = Preferences.userRoot();
        prefs.node(PROFILE_NAME).putBoolean(key, value);
    }

    private static void showTipsOfTheDay() {
        ResourceBundleTipOfTheDaySource tipOfTheDaySource = new ResourceBundleTipOfTheDaySource(ResourceBundle.getBundle("tips"));
        tipOfTheDaySource.setCurrentTipIndex(-1);
        URL styleSheet = TipOfTheDayDialog.class.getResource("/tips.css");
        TipOfTheDayDialog dialog = new TipOfTheDayDialog((Frame) null, tipOfTheDaySource, new AbstractAction("Show Tips on startup") {
            private static final long serialVersionUID = 3919739150082321631L;

            public void actionPerformed(ActionEvent e) {
                if (e.getSource() instanceof JCheckBox) {
                    JCheckBox checkBox = (JCheckBox) e.getSource();
                    setPrefBooleanValue("tip", checkBox.isSelected());
                }
                // change your user preference
            }
        }, styleSheet);

        dialog.setShowTooltip(getPrefBooleanValue("tip", true));
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setResizable(false);
        dialog.pack();
        JideSwingUtilities.globalCenterWindow(dialog);
        dialog.setVisible(true);
    }
}
