/*
 * @(#)StyledLabelPerformanceDemo.java 8/11/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.JideSplitPane;
import com.jidesoft.swing.PartialLineBorder;
import com.jidesoft.swing.StyleRange;
import com.jidesoft.swing.StyledLabel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Demoed Component: {@link com.jidesoft.swing.StyledLabel} <br> Required jar files: jide-common.jar
 * <br> Required L&F: any L&F
 */
public class StyledLabelPerformanceDemo extends AbstractDemo {
    private final int COUNT = 100;

    public StyledLabelPerformanceDemo() {
    }

    public String getName() {
        return "StyledLabel Demo (Performance Test)";
    }

    public String getProduct() {
        return PRODUCT_NAME_COMMON;
    }

    @Override
    public String getDescription() {
        return "StyledLabel is a special JLabel that can display the text in different colors and mixed with all kinds of line decorations.\n" +
                "\nSome features provided by StyledLabel can be achieved using html code in JLabel. However there are many advantages of using StyledLabel than using html JLabel. " +
                "One of the most important advantages is the performance. " +
                "StyledLabel is very simple and almost as light as the plain JLabel, " +
                "thus the performance of StyledLabel is 10 to 20 times better than html JLabel based on our test. " +
                "This demo is to show you the difference. It will create " + COUNT + " plain text JLabel, " + COUNT + " StyledLabel, " + COUNT + " html JLabel. " +
                "The time taken by each case is printed out in the console. On our testing machine, the time used by plain JLabel and StyledLabel are almost the same " +
                "and both are 20 times faster than html label. After seeing this result, I guess you will be more careful when using html JLabel in your code.\n" +
                "\nDemoed classes:\n" +
                "com.jidesoft.swing.StyledLabel";
    }

    public Component getDemoPanel() {
        JideSplitPane panel = new JideSplitPane(JideSplitPane.HORIZONTAL_SPLIT);
        new JLabel("Bold Italic Underlined"); // warm up
        panel.add(createLabelsPanel());
        panel.add(createStyledLabelsPanel());
        panel.add(createHtmlLabelsPanel());
        return panel;
    }

    private JComponent createStyledLabelsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2, 10, 1));

        // Creates a StyledLabel to warn up so that we don't include class loading time into the performance test.
        // This is the same for all three cases.
        StyledLabel label = new StyledLabel("Bold Italic Underlined");
        // we could pub the creation of StyleRange[] outside the loop.
        // But to make the comparison fair, we kept it inside.
        label.setStyleRanges(new StyleRange[]{
                new StyleRange(0, 4, Font.BOLD),
                new StyleRange(5, 6, Font.ITALIC),
                new StyleRange(12, 10, Font.PLAIN, StyleRange.STYLE_UNDERLINED)
        });

        long start = System.currentTimeMillis();
        for (int i = 0; i < COUNT; i++) {
            label = new StyledLabel("Bold Italic Underlined");
            // we could pub the creation of StyleRange[] outside the loop.
            // But to make the comparison fair, we kept it inside.
            label.setStyleRanges(new StyleRange[]{
                    new StyleRange(0, 4, Font.BOLD),
                    new StyleRange(5, 6, Font.ITALIC),
                    new StyleRange(12, 10, Font.PLAIN, StyleRange.STYLE_UNDERLINED)
            });
            panel.add(label);
        }
        panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(
                new PartialLineBorder(Color.gray, 1, true), " StyledLabel Examples - use " + (System.currentTimeMillis() - start) + " ms ",
                TitledBorder.CENTER, TitledBorder.CENTER, null, Color.RED), BorderFactory.createEmptyBorder(6, 4, 4, 4)));
        return panel;
    }

    private JComponent createHtmlLabelsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2, 10, 1));

        // Creates a StyledLabel to warn up so that we don't include class loading time into the performance test.
        // This is the same for all three cases.
        new JLabel("<HTML><B>Bold</B> <I>Italic</I> <U>Underlined</U></HTML>");

        long start = System.currentTimeMillis();
        for (int i = 0; i < COUNT; i++) {
            JLabel label = new JLabel("<HTML><B>Bold</B> <I>Italic</I> <U>Underlined</U></HTML>");
            panel.add(label);
        }
        panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(
                new PartialLineBorder(Color.gray, 1, true), " JLabel (HTML) Examples - use " + (System.currentTimeMillis() - start) + " ms ",
                TitledBorder.CENTER, TitledBorder.CENTER, null, Color.RED), BorderFactory.createEmptyBorder(6, 4, 4, 4)));
        return panel;
    }

    private JComponent createLabelsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2, 10, 1));

        // Creates a StyledLabel to warn up so that we don't include class loading time into the performance test.
        // This is the same for all three cases.
        new JLabel("Bold Italic Underlined");

        long start = System.currentTimeMillis();
        for (int i = 0; i < COUNT; i++) {
            JLabel label = new JLabel("Bold Italic Underlined");
            panel.add(label);
        }
        panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(
                new PartialLineBorder(Color.gray, 1, true), " JLabel (Plain) Examples - use " + (System.currentTimeMillis() - start) + " ms ",
                TitledBorder.CENTER, TitledBorder.CENTER, null, Color.RED), BorderFactory.createEmptyBorder(6, 4, 4, 4)));
        return panel;
    }

    @Override
    public String getDemoFolder() {
        return "B15.StyledLabel";
    }

    static public void main(String[] s) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new StyledLabelPerformanceDemo());
    }
}
