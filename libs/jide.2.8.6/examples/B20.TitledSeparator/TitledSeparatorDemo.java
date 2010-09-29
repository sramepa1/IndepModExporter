/*
 * @(#)TitledSeparatorDemo.java 5/2/2008
 *
 * Copyright 2002 - 2008 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.PartialGradientLineBorder;
import com.jidesoft.swing.PartialLineBorder;
import com.jidesoft.swing.PartialSide;
import com.jidesoft.swing.TitledSeparator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Demoed Component: {@link com.jidesoft.swing.TitledSeparator} <br> Required jar files: jide-common.jar <br> Required
 * L&F: any L&F
 */
public class TitledSeparatorDemo extends AbstractDemo {
    public TitledSeparatorDemo() {
    }

    public String getName() {
        return "TitledSeparator Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_COMMON;
    }

    @Override
    public String getDescription() {
        return "TitledSeparator is a graphical widget, comprising of a " +
                "label and a separator. Its typical usage is to group " +
                "together/separate logical regions in a GUI/panel\n" +
                "Demoed classes:\n" +
                "com.jidesoft.swing.TitledSeparator";
    }

    public Component getDemoPanel() {
        TitledSeparator[] ts = new TitledSeparator[15];

        ts[0] = new TitledSeparator("Default Separator");
        ts[1] = new TitledSeparator("Solid Line", TitledSeparator.TYPE_PARTIAL_LINE, SwingConstants.LEFT);
        ts[2] = new TitledSeparator("Gradient Line", TitledSeparator.TYPE_PARTIAL_GRADIENT_LINE, SwingConstants.LEFT);
        ts[3] = new TitledSeparator("Etched Separator");
        ts[4] = new TitledSeparator("SwingConstants.LEFT Title");
        ts[5] = new TitledSeparator("Right Title", TitledSeparator.TYPE_PARTIAL_ETCHED, SwingConstants.RIGHT);

        JLabel label1 = new JLabel("Red Title");
        label1.setForeground(Color.RED);
        ts[6] = new TitledSeparator(label1, TitledSeparator.TYPE_PARTIAL_GRADIENT_LINE, SwingConstants.RIGHT);

        JLabel label2 = new JLabel("Blue Title");
        label2.setForeground(Color.BLUE);
        ts[7] = new TitledSeparator(label2, TitledSeparator.TYPE_PARTIAL_ETCHED, SwingConstants.CENTER);

        JLabel label3 = new JLabel("Magenta Title");
        label3.setForeground(Color.MAGENTA);
        ts[8] = new TitledSeparator(label3, TitledSeparator.TYPE_PARTIAL_ETCHED, SwingConstants.CENTER);

        ts[9] = new TitledSeparator(new JLabel("Top Aligned Separator"), TitledSeparator.TYPE_PARTIAL_ETCHED, SwingConstants.LEFT, SwingConstants.TOP);
        ts[10] = new TitledSeparator(new JLabel("Center Aligned Separator"), TitledSeparator.TYPE_PARTIAL_ETCHED, SwingConstants.LEFT, SwingConstants.CENTER);
        ts[11] = new TitledSeparator(new JLabel("Bottom Aligned Separator"), TitledSeparator.TYPE_PARTIAL_ETCHED, SwingConstants.LEFT, SwingConstants.BOTTOM);

        ts[12] = new TitledSeparator(new JLabel("Custom Separator"), new PartialLineBorder(Color.RED, 1, PartialSide.SOUTH), SwingConstants.LEFT);
        ts[13] = new TitledSeparator(new JLabel("Custom Separator"), new PartialGradientLineBorder(new Color[]{Color.YELLOW, Color.BLACK}, 2, PartialSide.SOUTH), SwingConstants.LEFT);

        ts[14] = new TitledSeparator();
        ts[14].setLabelComponent(new JLabel("Configured TitledSeparator"));
        ts[14].setTextAlignment(SwingConstants.RIGHT);
        ts[14].setBarAlignment(SwingConstants.CENTER);
        ts[14].setSeparatorBorder(new PartialGradientLineBorder(new Color[]{Color.BLUE, Color.YELLOW}, 2, PartialSide.SOUTH));

        JComponent panel = new JPanel(new GridLayout(0, 1, 0, 0));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        for (TitledSeparator separator : ts) {
            panel.add(separator);
        }

        return panel;
    }

    @Override
    public String getDemoFolder() {
        return "B20.TitledSeparator";
    }

    static public void main(String[] s) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new TitledSeparatorDemo());
    }
}
