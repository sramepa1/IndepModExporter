/*
 * @(#)SampleVsnetDemo.java 2/18/2005
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
public class SampleVsnetDemo extends AbstractDemo {
    public String getName() {
        return "Action Framework Demo - Mimicing Visual Studio .NET";
    }

    public String getProduct() {
        return PRODUCT_NAME_ACTION;
    }

    @Override
    public String getDescription() {
        return "This is a demo to mimic Microsoft Visual Studio .NET. It not only has dockable frames, several command bars as well as several files opened in the middle. It is actually a demo of JIDE Action Framework, JIDE Docking Framework and DocumentPane\n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.action.DockableBar\n" +
                "com.jidesoft.action.CommandBarBar\n" +
                "com.jidesoft.action.DefaultDockableBarManager\n" +
                "com.jidesoft.dock.DockableFrame\n" +
                "com.jidesoft.document.DocumentPane\n" +
                "and a lot of more.\n";
    }

    public Component getDemoPanel() {
        ButtonPanel buttonPanel = new ButtonPanel(SwingConstants.TOP);
        buttonPanel.addButton(new JButton(new AbstractAction("Launch Visual Studio .NET Demo") {
            public void actionPerformed(ActionEvent e) {
                SampleVsnet.showDemo(false);
            }
        }));

        return buttonPanel;
    }

    @Override
    public Component getOptionsPanel() {
        return null;
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new SampleVsnetDemo());
    }

    @Override
    public String getDemoFolder() {
        return "A2.SampleVsnet";
    }

    @Override
    public String[] getDemoSource() {
        return new String[]{"SampleVsnet.java", "VsnetCommandBarFactory.java", "VsnetIconsFactory.java"};
    }
}
