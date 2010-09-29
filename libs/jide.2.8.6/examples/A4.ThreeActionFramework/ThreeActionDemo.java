/*
 * @(#)ThreeActionDemo.java 2/18/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.dialog.ButtonPanel;
import com.jidesoft.plaf.LookAndFeelFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Demoed Component: {@link com.jidesoft.wizard.WizardDialog} <br> Required jar files:
 * jide-common.jar, jide-components.jar, jide-dialogs.jar <br> Required L&F: Any L&F
 */
public class ThreeActionDemo extends AbstractDemo {
    public String getName() {
        return "Action Framework Demo - Three DockableBar Managers";
    }

    public String getProduct() {
        return PRODUCT_NAME_ACTION;
    }

    @Override
    public String getDescription() {
        return "This is a demo of mixed usage of JIDE Action Framework and JIDE Docking Framework. In this particular demo, there are three Acton Framework and two Docking Framework. From this demo, you can see how flexible are Action Framework and Docking Framework. In fact, you can use them at any child panel of your application.\n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.action.DockableBar\n" +
                "com.jidesoft.action.CommandBarBar\n" +
                "com.jidesoft.action.DefaultDockableBarManager\n" +
                "com.jidesoft.dock.DockableFrame\n" +
                "and a lot of more.\n";
    }

    public Component getDemoPanel() {
        ButtonPanel buttonPanel = new ButtonPanel(SwingConstants.TOP);
        buttonPanel.addButton(new JButton(new AbstractAction("Launch Action Framework Demo") {
            public void actionPerformed(ActionEvent e) {
                ThreeActionFrameworkDemo.showDemo(false);
            }
        }));

        return buttonPanel;
    }

    @Override
    public Component getOptionsPanel() {
        return null;
    }

    @Override
    public String[] getDemoSource() {
        return new String[]{"ThreeActionFrameworkDemo.java"};
    }

    @Override
    public String getDemoFolder() {
        return "A4.ThreeActionFramework";
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new ThreeActionDemo());
    }
}
