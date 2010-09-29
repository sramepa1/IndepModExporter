/*
 * @(#)BorderDemo.java
 *
 * Copyright 2002 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.plaf.UIDefaultsLookup;
import com.jidesoft.swing.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Demoed Component: {@link PartialEtchedBorder}, {@link PartialLineBorder}, {@link JideTitledBorder}. <br> Required jar
 * files: jide-common.jar <br> Required L&F: any L&F
 */
public class BorderDemo extends AbstractDemo {
    private static final long serialVersionUID = -7135804588529154849L;

    public String getName() {
        return "Borders Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_COMMON;
    }

    @Override
    public String getDescription() {
        return "This demo shows you several Border classes we added.\n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.swing.PartialEtchedBorder\n" +
                "com.jidesoft.swing.PartialLineBorder\n" +
                "com.jidesoft.swing.JideTitledBorder";
    }

    public Component getDemoPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setLayout(new JideBoxLayout(panel, JideBoxLayout.Y_AXIS, 10));

        JTextArea textField = new JTextArea();
        JPanel border = new JPanel(new BorderLayout());
        border.setPreferredSize(new Dimension(300, 100));
        border.add(new JScrollPane(textField), BorderLayout.CENTER);
        border.setBorder(new JideTitledBorder(new PartialEtchedBorder(PartialEtchedBorder.LOWERED, PartialSide.NORTH), "PartialEtchedBorder"));

        JTextArea textField2 = new JTextArea();
        JPanel border2 = new JPanel(new BorderLayout());
        border2.setPreferredSize(new Dimension(300, 100));
        border2.add(new JScrollPane(textField2), BorderLayout.CENTER);
        border2.setBorder(new JideTitledBorder(new PartialLineBorder(Color.darkGray, 1, PartialSide.NORTH), "PartialLineBorder"));

        JTextArea textField3 = new JTextArea();
        JPanel border3 = new JPanel(new BorderLayout());
        border3.setPreferredSize(new Dimension(300, 100));
        border3.add(new JScrollPane(textField3), BorderLayout.CENTER);
        border3.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(
                new PartialLineBorder(Color.gray, 1, true), "Rounded Corners Border",
                TitledBorder.TRAILING, TitledBorder.ABOVE_TOP), BorderFactory.createEmptyBorder(6, 4, 4, 4)));

        JTextArea textField4 = new JTextArea();
        JPanel border4 = new JPanel(new BorderLayout());
        border4.setPreferredSize(new Dimension(400, 100));
        border4.add(new JScrollPane(textField4), BorderLayout.CENTER);
        border4.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(
                new PartialGradientLineBorder(new Color[]{new Color(0, 0, 128), UIDefaultsLookup.getColor("control")}, 2, PartialSide.NORTH), "Gradient Border",
                TitledBorder.CENTER, TitledBorder.ABOVE_TOP), BorderFactory.createEmptyBorder(6, 4, 4, 4)));

        panel.add(border, JideBoxLayout.FLEXIBLE);
        panel.add(Box.createVerticalStrut(12));
        panel.add(border2, JideBoxLayout.FLEXIBLE);
        panel.add(Box.createVerticalStrut(12));
        panel.add(border3, JideBoxLayout.FLEXIBLE);
        panel.add(Box.createVerticalStrut(12));
        panel.add(border4, JideBoxLayout.FLEXIBLE);
        panel.add(Box.createGlue(), JideBoxLayout.VARY);
        return panel;
    }

    @Override
    public String getDemoFolder() {
        return "B3.Borders";
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new BorderDemo());
    }
}
