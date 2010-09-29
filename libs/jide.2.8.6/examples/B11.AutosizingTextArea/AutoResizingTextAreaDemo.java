/*
 * @(#)AutoresizingDemo.java 4/9/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.AutoResizingTextArea;
import com.jidesoft.swing.JideBoxLayout;

import javax.swing.*;
import java.awt.*;

/**
 * Demoed Component: {@link com.jidesoft.swing.AutoResizingTextArea} <br> Required jar files:
 * jide-common.jar <br> Required L&F: any L&F
 */
public class AutoResizingTextAreaDemo extends AbstractDemo {

    public AutoResizingTextAreaDemo() {
    }

    public String getName() {
        return "Autoresizing TextArea Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_COMMON;
    }

    @Override
    public String getDescription() {
        return "AutoResizingTextArea is a special text area which automatically resizes itself vertically. This is an ideal candidiate when you want to show a smaller text area initially. When user starts to type in a lot of text, it automatically increases so that more text can be read.\n" +
                "\nYou can set minimum rows and maximum rows allowed. \n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.swing.AutoResizingTextArea";
    }

    public Component getDemoPanel() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(400, 400));
        panel.setLayout(new JideBoxLayout(panel, JideBoxLayout.Y_AXIS));
        panel.add(new JScrollPane(new AutoResizingTextArea("Typing in new line here to see the text area growing automatically. \nMinimum 2 rows and maximum 10 rows", 2, 10, 20)));
        panel.add(Box.createGlue(), JideBoxLayout.VARY);
        return panel;
    }

    @Override
    public String getDemoFolder() {
        return "B11.AutosizingTextArea";
    }

    static public void main(String[] s) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new AutoResizingTextAreaDemo());
    }
}