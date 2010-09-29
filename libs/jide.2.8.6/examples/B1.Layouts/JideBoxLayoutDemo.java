/*
 * @(#)JideBoxLayoutDemo.java
 *
 * Copyright 2002 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.JideBoxLayout;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Demoed Component: {@link JideBoxLayout} <br> Required jar files: jide-common.jar <br> Required L&F: any L&F
 */
public class JideBoxLayoutDemo extends AbstractDemo {
    public String getName() {
        return "JideBoxLayout Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_COMMON;
    }

    public Component getDemoPanel() {
        // create a panel with JideBoxLayout
        JPanel panel = new JPanel();
        panel.setLayout(new JideBoxLayout(panel, 0, 6));

        JButton button = new JButton("FIX (w:120)");
        button.setPreferredSize(new Dimension(120, 200));
        button.setMinimumSize(new Dimension(0, 0));
        panel.add(button, JideBoxLayout.FIX);

        button = new JButton("FLEX (w:120)");
        button.setPreferredSize(new Dimension(120, 200));
        button.setMinimumSize(new Dimension(0, 0));
        panel.add(button, JideBoxLayout.FLEXIBLE);

        button = new JButton("FLEX (w:240)");
        button.setPreferredSize(new Dimension(240, 200));
        button.setMinimumSize(new Dimension(0, 0));
        panel.add(button, JideBoxLayout.FLEXIBLE);

        button = new JButton("VARY (w:120)");
        button.setPreferredSize(new Dimension(120, 200));
        button.setMinimumSize(new Dimension(0, 0));
        panel.add(button, JideBoxLayout.VARY);

        panel.setBorder(new TitledBorder("Resize the panel to see the size changing behavior"));

        return panel;
    }

    @Override
    public String getDescription() {
        return "Similar to BoxLayout, JideBoxLayout lays components out either vertically or horizontally.  Unlike BoxLayout however, there is a constraint associated with each component, set to either FIX, FLEXIBLE, or VARY. If the constraint is set to FIX then the component's width (or height if the JideBoxLayout is vertical) will always be the preferred width. By contrast, although FLEXIBLE components try to keep the preferred width, they will shrink proportionally if there isn't enough space.  Finally, VARY components will expand in size to fill whatever width is left. Although you can add multiple FIX or FLEXIBLE components, only one VARY component is allowed.\n" +
                "\nYou can resize the frame to see how each components are resized.\n" +
                "\n(Notes: it will throw exception when you resize smaller than 60 pixels)\n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.swing.JideBorderLayout";
    }

    @Override
    public String getDemoFolder() {
        return "B1.Layouts";
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new JideBoxLayoutDemo());
    }
}
