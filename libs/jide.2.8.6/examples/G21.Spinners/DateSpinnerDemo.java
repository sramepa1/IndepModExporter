/*
 * @(#)${NAME}
 *
 * Copyright 2002 - 2004 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.spinner.DateSpinner;
import com.jidesoft.swing.JideSwingUtilities;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;

/**
 * Demoed Component: {@link com.jidesoft.spinner.DateSpinner} <br> Required jar files:
 * jide-common.jar, jide-grids.jar <br> Required L&F: any L&F
 */
public class DateSpinnerDemo extends AbstractDemo {
    public DateSpinnerDemo() {
    }

    public String getName() {
        return "DateSpinner Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_COMMON;
    }

    @Override
    public String getDescription() {
        return "This is a demo of DateSpinner. It can be used to display and edit Date type using a DateFormat string that can represent" +
                "a date or time.\n" +
                "Demoed classes:\n" +
                "com.jidesoft.spinner.DateSpinner";
    }

    public Component getDemoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1, 10, 10));

        DateSpinner date = new DateSpinner("MM/dd/yyyy");
        panel.add(date);

        DateSpinner time = new DateSpinner("hh:mm:ssa", Calendar.getInstance().getTime());
        panel.add(time);

        return JideSwingUtilities.createTopPanel(panel);
    }

    @Override
    public String getDemoFolder() {
        return "G21.Spinners";
    }

    static public void main(String[] s) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new DateSpinnerDemo());
    }
}
