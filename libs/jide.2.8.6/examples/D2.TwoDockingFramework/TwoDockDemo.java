/*
 * @(#)TwoDockDemo.java 2/18/2005
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
public class TwoDockDemo extends AbstractDemo {
    public String getName() {
        return "Docking Framework Demo - Two Docking Managers";
    }

    public String getProduct() {
        return PRODUCT_NAME_DOCK;
    }

    @Override
    public String getDescription() {
        return "This is a demo of JIDE Docking Framework which has two Docking Managers. Each Docking Manager has its own set of dockable frames.\n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.dock.DockableFrame\n" +
                "com.jidesoft.dock.DefaultDockingManager\n" +
                "and a lot of more.\n";
    }

    public Component getDemoPanel() {
        ButtonPanel buttonPanel = new ButtonPanel(SwingConstants.TOP);
        buttonPanel.addButton(new JButton(new AbstractAction("Launch Two Docking Frameworks As a TabbedPane") {
            public void actionPerformed(ActionEvent e) {
                TwoDockingFrameworkDemo.showDemo(false);
            }
        }));

        buttonPanel.addButton(new JButton(new AbstractAction("Launch Two Side-by-Side Docking Frameworks in one JFrame (allow drag-n-drop across them)") {
            public void actionPerformed(ActionEvent e) {
                SideBySideDockingFrameworkDemo.showDemo(false);
            }
        }));

        buttonPanel.addButton(new JButton(new AbstractAction("Launch Two Docking Frameworks in two JFrames (allow drag-n-drop across them)") {
            public void actionPerformed(ActionEvent e) {
                TwoFramesDockingFrameworkDemo.showDemo(false);
            }
        }));

        buttonPanel.addButton(new JButton(new AbstractAction("Launch Nested Docking Frameworks") {
            public void actionPerformed(ActionEvent e) {
                NestedDockingFrameworkDemo.showDemo(false);
            }
        }));

        return buttonPanel;
    }

    @Override
    public Component getOptionsPanel() {
        return null;
    }

    @Override
    public String getDemoFolder() {
        return "D2.TwoDockingFramework";
    }

    @Override
    public String[] getDemoSource() {
        return new String[]{"TwoDockingFrameworkDemo.java"};
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new TwoDockDemo());
    }
}
