/*
 * @(#)OptionsDialogDemo.java 2/15/2005
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
public class OptionsDialogDemo extends AbstractDemo {
    public String getName() {
        return "Options Dialog Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_DIALOGS;
    }

    @Override
    public String getDescription() {
        return "Multiple-Page Dialog is a dialog which allows you to divide content into several child panels. \n" +
                "\nEven though there are more cases to use this dialog, one major use case is for Options Dialog. Please click buttons in the demo panel to see several templates we provided.\n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.dialog.MultiplePageDialog\n";
    }

    public Component getDemoPanel() {
        ButtonPanel buttonPanel = new ButtonPanel(SwingConstants.TOP);
        buttonPanel.addButton(new JButton(new AbstractAction("Firebird Options Dialog (Icon style)") {
            public void actionPerformed(ActionEvent e) {
                FirebirdOptionsDialog.showOptionsDialog(false);
            }
        }));

        buttonPanel.addButton(new JButton(new AbstractAction("IntelliJ IDEA Options Dialog (Icon style)") {
            public void actionPerformed(ActionEvent e) {
                IntelliJOptionsDialog.showOptionsDialog(false);
            }
        }));

        buttonPanel.addButton(new JButton(new AbstractAction("Dreamweaver Options Dialog (List style)") {
            public void actionPerformed(ActionEvent e) {
                ListStyleOptionsDialog.showOptionsDialog(false);
            }
        }));

        buttonPanel.addButton(new JButton(new AbstractAction("VSNET Options Dialog (Tree style)") {
            public void actionPerformed(ActionEvent e) {
                VsnetOptionsDialog.showOptionsDialog(false);
            }
        }));

        buttonPanel.addButton(new JButton(new AbstractAction("Microsoft Office Options Dialog (Tab style)") {
            public void actionPerformed(ActionEvent e) {
                OfficeOptionsDialog.showOptionsDialog(false);
            }
        }));
        return buttonPanel;
    }

    @Override
    public String getDemoFolder() {
        return "W2.OptionsDialog";
    }

    @Override
    public String[] getDemoSource() {
        return new String[]{"OptionsDialogDemo.java", "FirebirdOptionsDialog.java", "ListStyleOptionsDialog.java", "IntelliJOptionsDialog.java", "OfficeOptionsDialog.java", "VsnetOptionsDialog.java"};
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new OptionsDialogDemo());
    }
}
