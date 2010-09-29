/*
 * @(#)PointSpinnerDemo.java 4/8/2007
 *
 * Copyright 2002 - 2007 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.spinner.PointSpinner;
import com.jidesoft.swing.JideSwingUtilities;

import javax.swing.*;
import java.awt.*;

/**
 * Demoed Component: {@link com.jidesoft.spinner.DateSpinner} <br> Required jar files:
 * jide-common.jar, jide-grids.jar <br> Required L&F: any L&F
 */
public class PointSpinnerDemo extends AbstractDemo {
    public PointSpinnerDemo() {
    }

    public String getName() {
        return "PointSpinner Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_COMMON;
    }

    @Override
    public String getDescription() {
        return "This is a demo of PointSpinner. It can be used to display and edit Point type." +
                "a date or time.\n" +
                "Demoed classes:\n" +
                "com.jidesoft.spinner.PointSpinner";
    }

    public Component getDemoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1, 10, 10));

        final PointSpinner pointSpinner = new PointSpinner();
        panel.add(pointSpinner);

        return JideSwingUtilities.createTopPanel(panel);
    }

    @Override
    public String getDemoFolder() {
        return "G21.Spinners";
    }

    static public void main(String[] s) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new PointSpinnerDemo());
    }
}
