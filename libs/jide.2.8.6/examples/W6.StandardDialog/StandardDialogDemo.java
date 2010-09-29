/*
 * @(#)StandardDialogDemo.java 5/25/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.dialog.StandardDialog;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.JideBoxLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Demoed Component: {@link com.jidesoft.dialog.StandardDialog} <br> Required jar files:
 * jide-common.jar, jide-dialog.jar <br> Required L&F: Jide L&F extension required
 */
public class StandardDialogDemo extends AbstractDemo {
    public StandardDialogDemo() {
    }

    public String getName() {
        return "StandardDialogDemo";
    }

    public String getProduct() {
        return PRODUCT_NAME_COMMON;
    }

    @Override
    public String getDescription() {
        return "StandardDialog is a step to standardize the creation of JDialog so that all dialogs in your application look consistent. \n" +
                "\nThere are a list of features we put in StandardDialog which are usually need by any dialogs." +
                "\n1. ButtonPanel where you can put several buttons. " +
                "\n2. Handle ENTER and ESC key automatically" +
                "\n3. Allow you to set initial focused component" +
                "\n4. Standard layouts to arrange banner, content and buttons" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.dialog.StandardDialog";
    }


    public Component getDemoPanel() {
        final JPanel panel = new JPanel();
        panel.setLayout(new JideBoxLayout(panel, JideBoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton button = new JButton();
        AbstractAction action = new AbstractAction("Show StandardDialog 1") {
            public void actionPerformed(ActionEvent e) {
                StandardDialog example = new StandardDialogExample1();
                example.pack();
                example.setLocationRelativeTo(null);
                example.setVisible(true);
            }
        };
        button.setAction(action);
        panel.add(button);
        panel.add(Box.createVerticalStrut(12), JideBoxLayout.FIX);

        button = new JButton();
        action = new AbstractAction("Show StandardDialog 2") {
            public void actionPerformed(ActionEvent e) {
                StandardDialog example = new StandardDialogExample2();
                example.pack();
                example.setLocationRelativeTo(null);
                example.setVisible(true);
            }
        };
        button.setAction(action);
        panel.add(button);
        panel.add(Box.createVerticalStrut(12), JideBoxLayout.FIX);

        button = new JButton();
        action = new AbstractAction("Show StandardDialog 3") {
            public void actionPerformed(ActionEvent e) {
                StandardDialog example = new StandardDialogExample3();
                example.pack();
                example.setLocationRelativeTo(null);
                example.setVisible(true);
            }
        };
        button.setAction(action);
        panel.add(button);
        panel.add(Box.createVerticalStrut(12), JideBoxLayout.FIX);

        button = new JButton();
        action = new AbstractAction("Show StandardDialog 4") {
            public void actionPerformed(ActionEvent e) {
                StandardDialog example = new StandardDialogExample4();
                example.pack();
                example.setLocationRelativeTo(null);
                example.setVisible(true);
            }
        };
        button.setAction(action);
        panel.add(button);
        panel.add(Box.createVerticalStrut(12), JideBoxLayout.FIX);

        button = new JButton();
        action = new AbstractAction("Show \"Expandable\" Dialog") {
            public void actionPerformed(ActionEvent e) {
                StandardDialog example = new StandardDialogExample5();
                example.pack();
                example.setLocationRelativeTo(null);
                example.setVisible(true);
            }
        };
        button.setAction(action);
        panel.add(button);
        panel.add(Box.createVerticalStrut(12), JideBoxLayout.FIX);

        panel.add(Box.createGlue(), JideBoxLayout.VARY);
        return panel;
    }

    @Override
    public String getDemoFolder() {
        return "W6.StandardDialog";
    }

    @Override
    public String[] getDemoSource() {
        return new String[]{"StandardDialogDemo.java", "StandardDialogExample1.java", "StandardDialogExample2.java", "StandardDialogExample3.java", "StandardDialogExample4.java", "StandardDialogExample5.java",};
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new StandardDialogDemo());
    }
}

