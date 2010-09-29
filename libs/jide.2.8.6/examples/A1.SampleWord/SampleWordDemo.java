/*
 * @(#)SampleWordDemo.java 2/18/2005
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
public class SampleWordDemo extends AbstractDemo {
    public String getName() {
        return "Action Framework Demo - Mimicing Microsoft Word";
    }

    public String getProduct() {
        return PRODUCT_NAME_ACTION;
    }

    @Override
    public String getDescription() {
        return "Almost all applications need toolbars and menu bar. These components are JIDE Action Framework all about. It provides a much more sophisticated component called CommandBar to replace both JToolBar and JMenuBar, the default Swing implantations. Most features you saw in Microsoft Office products regarding toolbars and menu bar, you will find them in JIDE Action Framework product. \n" +
                "\nYou also can choose different style in JIDE Action Framework. In currently release, we included Office 2003 style, Visual Studio .NET style, Office XP style, Eclipse style, even Aqua style on Mac OS X. The default Metal style is in there too.\n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.action.DockableBar\n" +
                "com.jidesoft.action.CommandBarBar\n" +
                "com.jidesoft.action.DefaultDockableBarManager\n" +
                "and a lot of more.\n";
    }

    public Component getDemoPanel() {
        ButtonPanel buttonPanel = new ButtonPanel(SwingConstants.TOP);
        buttonPanel.addButton(new JButton(new AbstractAction("Launch Microsoft Word Demo") {
            public void actionPerformed(ActionEvent e) {
                SampleWord.showDemo(false);
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
        return "A1.SampleWord";
    }

    @Override
    public String[] getDemoSource() {
        return new String[]{"SampleWord.java", "Office2003CommandBarFactory.java", "Office2003IconsFactory.java"};
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new SampleWordDemo());
    }
}
