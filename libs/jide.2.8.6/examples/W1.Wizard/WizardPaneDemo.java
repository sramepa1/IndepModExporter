/*
 * @(#)WizardPaneDemo.java 3/23/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.dialog.ButtonEvent;
import com.jidesoft.dialog.ButtonNames;
import com.jidesoft.dialog.PageList;
import com.jidesoft.icons.JideIconsFactory;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.JideSwingUtilities;
import com.jidesoft.swing.MultilineLabel;
import com.jidesoft.wizard.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Demoed Component: {@link com.jidesoft.wizard.WizardDialog} <br> Required jar files:
 * jide-common.jar, jide-components.jar, jide-dialogs.jar <br> Required L&F: Any L&F
 */
public class WizardPaneDemo extends AbstractDemo {
    public String getName() {
        return "WizardDialogPane Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_DIALOGS;
    }

    @Override
    public String getDescription() {
        return "Wizard is a well-known user interface that is ideal to guide user through for complex and unfamiliar tasks. A typical usage of it is project wizard - which asks user a couple questions and generate source code of a project automatically for user.\n" +
                "There are several wizard standards. The most famous two are Microsoft Wizard 97 standard and Java L&F Wizard standard. Please see references for details. I strongly suggest you read those standards before designing any wizards. Those documents are very well written. They are also the specs for our wizard component. You can find links to them at the reference section of JIDE Dialogs Developer Guide. Our wizard implementation are based on these two standards.\n" +
                "Different from Wizard Demo, this demo shows you how to embed a Wizard as a panel into another panel without using dialog.\n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.wizard.WizardDialogPane\n" +
                "com.jidesoft.wizard.WelcomeWizardPage\n" +
                "com.jidesoft.wizard.CompletionWizardPage\n" +
                "com.jidesoft.wizard.AbstractWizardPage\n";
    }

    public Component getDemoPanel() {
        return createWizardPane(WizardStyle.WIZARD97_STYLE);
    }

    @Override
    public Component getOptionsPanel() {
        return null;
    }

    @Override
    public String getDemoFolder() {
        return "W1.Wizard";
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new WizardPaneDemo());
    }

    public static WizardDialogPane createWizardPane(int style) {
        WizardStyle.setStyle(style);

        final WizardDialogPane wizard = new WizardDialogPane();
        if (style == WizardStyle.WIZARD97_STYLE) {
            wizard.setDefaultGraphic(WizardDemoIconsFactory.getImageIcon(WizardDemoIconsFactory.Wizard97.SAMPLE_IMAGE_SMALL).getImage());
        }
        else {
            wizard.setDefaultGraphic(WizardDemoIconsFactory.getImageIcon(WizardDemoIconsFactory.Metal.SAMPLE_IMAGE_SMALL).getImage());
        }

        // setup model
        PageList model = new PageList();

        AbstractWizardPage page1 = new WelcomePage("Welcome to the JIDE Wizard Feature Demo",
                "This wizard will guide you through the features of Wizard Component from JIDE Software, Inc.");

        AbstractWizardPage page2 = new LicensePage("License Agreement",
                "This page shows you how to enable/disable a button based on user selection.");

        AbstractWizardPage page3 = new InfoWarningPage("Infos and Warnings",
                "This page shows you how to show info message and warning message.");

        AbstractWizardPage page4 = new ChangeNextPagePage("Change Next Page",
                "This page shows you how to change next page based on user selection.");

        AbstractWizardPage page5 = new CompletionPage("Completing the JIDE Wizard Feature Demo",
                "You have successfully run through the important features of Wizard Component from JIDE Software, Inc.");

        model.append(page1);
        model.append(page2);
        model.append(page4);
        model.append(page3);
        model.append(page5);

        wizard.setPageList(model);

        // You have to write your own finish and cancel because we have no idea where you put this wizard pane this
        // we don't know how to dispose wizard when Finish or Cancel button is pressed.
        wizard.setFinishAction(new AbstractAction("Finish") {
            public void actionPerformed(ActionEvent e) {
                if (wizard.setCurrentPage(null)) {
                    JOptionPane.showMessageDialog(wizard, "Wizard finished");
                }
            }
        });
        wizard.setCancelAction(new AbstractAction("Cancel") {
            public void actionPerformed(ActionEvent e) {
                if (wizard.setCurrentPage(null)) {
                    JOptionPane.showMessageDialog(wizard, "Wizard is cancelled");
                }
            }
        });

        wizard.initComponents();

        return wizard;
    }

    public static class WelcomePage extends WelcomeWizardPage {
        public WelcomePage(String title, String description) {
            super(title, description/*, SampleWizardIconsFactory.getImageIcon(SampleWizardIconsFactory.Wizard97.SAMPLE_IMAGE_SMALL)*/);
        }

        @Override
        protected void initContentPane() {
            super.initContentPane();
            addText("JIDE Software, Inc. is a leading-edge provider of Swing components for Java developers. JIDE means Java IDE (Integrated Development Environment). As the name indicated, JIDE Software's development components focus on IDE or IDE-like applications for software developers. All our products are in pure Java and Swing, to allow the most compatibilities with industrial standards.");
            addSpace();
            addText("To continue, click Next.");
        }
    }

    public static class CompletionPage extends CompletionWizardPage {
        public CompletionPage(String title, String description) {
            super(title, description);
        }

        @Override
        protected void initContentPane() {
            super.initContentPane();
            addSpace();
            addText("To close this wizard, click Finish.");
        }
    }

    public static class LicensePage extends AbstractWizardPage {
        private JRadioButton _button1;
        private JRadioButton _button2;

        public LicensePage(String title, String description) {
            super(title, description, JideIconsFactory.getImageIcon(JideIconsFactory.JIDE50));
        }

        @Override
        public int getLeftPaneItems() {
            return (WizardStyle.getStyle() == WizardStyle.JAVA_STYLE) ? LEFTPANE_STEPS | LEFTPANE_HELP : super.getLeftPaneItems();
        }

        @Override
        public JComponent createWizardContent() {
            JPanel panel = createLicensePanel();
            panel.setBorder(getContentThinBorder());
            return panel;
        }

        @Override
        public void setupWizardButtons() {
            fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.BACK);
            fireButtonEvent(ButtonEvent.HIDE_BUTTON, ButtonNames.FINISH);
            updateNextButton();
        }

        public JPanel createLicensePanel() {
            JPanel panel = new JPanel(new BorderLayout(4, 4));
            MultilineLabel label = new MultilineLabel("Please read the following license agreement. You must accept the terms of this agreement before continuing the wizard.");
            panel.add(label, BorderLayout.BEFORE_FIRST_LINE);
            panel.add(new JScrollPane(new JTextArea()), BorderLayout.CENTER);
            panel.add(createRadioButtons(), BorderLayout.AFTER_LAST_LINE);
            return panel;
        }

        public JPanel createRadioButtons() {
            _button1 = new JRadioButton("I accept the agreement");
            _button2 = new JRadioButton("I do not accept the agreement");
            _button2.setSelected(true);
            _button1.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        updateNextButton();
                    }
                }
            });
            _button2.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        updateNextButton();
                    }
                }
            });
            JPanel panel = new JPanel(new GridLayout(2, 1));
            panel.add(_button1);
            panel.add(_button2);
            ButtonGroup group = new ButtonGroup();
            group.add(_button1);
            group.add(_button2);
            JideSwingUtilities.setOpaqueRecursively(panel, false);
            return panel;
        }

        private void updateNextButton() {
            if (_button1 != null && _button2 != null) {
                if (_button1.isSelected()) {
                    fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.NEXT);
                }
                else if (_button2.isSelected()) {
                    fireButtonEvent(ButtonEvent.DISABLE_BUTTON, ButtonNames.NEXT);
                }
            }
        }

    }

    public static class InfoWarningPage extends DefaultWizardPage {
        public InfoWarningPage(String title, String description) {
            super(title, description, JideIconsFactory.getImageIcon(JideIconsFactory.JIDE50));
        }

        @Override
        protected void initContentPane() {
            super.initContentPane();
            addInfo("The information bubble icon can emphasize important yet not crucial information. " +
                    "Call addInfo(String text) in DefaultWizardPage to add this type of message.");
            addWarning("The yellow warning icon can emphasize information vital to either the success of the task or the consequences of the actions performed on the wizard page. " +
                    "Call addWarning(String text) in DefaultWizardPage to add this type of message.");
        }

        @Override
        public void setupWizardButtons() {
            fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.BACK);
            fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.NEXT);
            fireButtonEvent(ButtonEvent.HIDE_BUTTON, ButtonNames.FINISH);
        }
    }

    public static class ChangeNextPagePage extends DefaultWizardPage {
        private JRadioButton _button1;
        private JRadioButton _button2;

        public ChangeNextPagePage(String title, String description) {
            super(title, description, JideIconsFactory.getImageIcon(JideIconsFactory.JIDE50));
        }

        @Override
        public List getSteps() {
            List list = new ArrayList();
            list.add("Welcome to the JIDE Wizard Feature Demo");
            list.add("License Agreement");
            list.add("Change Next Page");
            list.add("...");
            return list;
        }

        @Override
        public int getSelectedStepIndex() {
            return 2;
        }

        @Override
        protected void initContentPane() {
            super.initContentPane();
            _button1 = new JRadioButton("If this radio button is selected, press Next button will continue to Infos and Warning page");
            _button2 = new JRadioButton("If this radio button is selected, press Next button will continue to Completion page");
            _button1.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        updateNextPage();
                    }
                }
            });
            _button2.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        updateNextPage();
                    }
                }
            });
            JPanel panel = new JPanel(new GridLayout(2, 1));
            panel.add(_button1);
            _button1.setSelected(true);
            panel.add(_button2);
            ButtonGroup group = new ButtonGroup();
            group.add(_button1);
            group.add(_button2);
            addComponent(panel);
            JideSwingUtilities.setOpaqueRecursively(panel, false);
        }

        @Override
        public void setupWizardButtons() {
            fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.BACK);
            fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.NEXT);
            fireButtonEvent(ButtonEvent.HIDE_BUTTON, ButtonNames.FINISH);
            updateNextPage();
        }

        private void updateNextPage() {
            if (_button1.isSelected() && getOwner() != null) {
                getOwner().setNextPage(getOwner().getPageByTitle("Infos and Warnings"));
            }
            else if (_button2.isSelected() && getOwner() != null) {
                getOwner().setNextPage(getOwner().getPageByTitle("Completing the JIDE Wizard Feature Demo"));
            }
        }
    }

}
