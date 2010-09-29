/*
 * @(#)JideBorderLayoutDemo.java
 *
 * Copyright 2002 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.JideBorderLayout;
import com.jidesoft.swing.JideTabbedPane;

import javax.swing.*;
import java.awt.*;

/**
 * Demoed Component: {@link JideBorderLayout} <br> Required jar files: jide-common.jar <br> Required
 * L&F: any L&F
 */
public class JideBorderLayoutDemo extends AbstractDemo {
    public String getName() {
        return "JideBorderLayout Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_COMMON;
    }

    @Override
    public String getDescription() {
        return "JideBorderLayout is almost the same as the standard Swing BorderLayout except that the NORTH and SOUTH component's width is the same as the CENTER component, as shown overleaf. Please note the different between BorderLayout and JideBorderLayout.\n" +
                "\nIn AWT BorderLayout, the north and south components take all of the horizontal space that is available.\n" +
                "\nBy contrast, in JideBorderLayout the north and south components only take the same horizontal space as the center component.\n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.swing.JideBorderLayout";
    }

    public Component getDemoPanel() {
        JPanel panel1 = createJideBorderLayoutPanel();
        JPanel panel2 = createBorderLayoutPanel();

        JideTabbedPane tabbedPane = new JideTabbedPane();
        tabbedPane.setTabShape(JideTabbedPane.SHAPE_BOX);
        tabbedPane.add(panel1);
        tabbedPane.add(panel2);

        return tabbedPane;
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new JideBorderLayoutDemo());
    }

    private JPanel createJideBorderLayoutPanel() {
        JPanel panel = new JPanel(new JideBorderLayout(6, 10));
        panel.setName("JideBorderLayout");
        JButton button = new JButton("NORTH");
        panel.add(button, JideBorderLayout.BEFORE_FIRST_LINE);
        button = new JButton("SOUTH");
        panel.add(button, JideBorderLayout.AFTER_LAST_LINE);
        button = new JButton("WEST");
        panel.add(button, JideBorderLayout.BEFORE_LINE_BEGINS);
        button = new JButton("EAST");
        panel.add(button, JideBorderLayout.AFTER_LINE_ENDS);
        button = new JButton("CENTER");
        button.setPreferredSize(new Dimension(200, 200));
        panel.add(button, JideBorderLayout.CENTER);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return panel;
    }

    private JPanel createBorderLayoutPanel() {
        JPanel panel = new JPanel(new BorderLayout(6, 10));
        panel.setName("BorderLayout");
        JButton button = new JButton("NORTH");
        panel.add(button, JideBorderLayout.BEFORE_FIRST_LINE);
        button = new JButton("SOUTH");
        panel.add(button, JideBorderLayout.AFTER_LAST_LINE);
        button = new JButton("WEST");
        panel.add(button, JideBorderLayout.BEFORE_LINE_BEGINS);
        button = new JButton("EAST");
        panel.add(button, JideBorderLayout.AFTER_LINE_ENDS);
        button = new JButton("CENTER");
        button.setPreferredSize(new Dimension(200, 200));
        panel.add(button, JideBorderLayout.CENTER);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return panel;
    }

    @Override
    public String getDemoFolder() {
        return "examples/B1.Layouts/";
    }
}
