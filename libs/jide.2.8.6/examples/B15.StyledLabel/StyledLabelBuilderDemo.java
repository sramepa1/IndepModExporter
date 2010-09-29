/*
 * @(#)StyledLabelBuilderDemo.java 1/3/2006
 *
 * Copyright 2002 - 2006 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.JideBoxLayout;
import com.jidesoft.swing.PartialLineBorder;
import com.jidesoft.swing.StyledLabel;
import com.jidesoft.swing.StyledLabelBuilder;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Demoed Component: {@link com.jidesoft.swing.StyledLabel} <br> Required jar files: jide-common.jar <br> Required L&F:
 * any L&F
 */
public class StyledLabelBuilderDemo extends AbstractDemo {
    protected StyledLabel _label;
    private final String INITIAL_TEXT = "{Preview:bold} of {StyledLabel:f:red}";

    public StyledLabelBuilderDemo() {
    }

    public String getName() {
        return "StyledLabelBuilder Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_COMMON;
    }

    @Override
    public String getDescription() {
        return "StyledLabelBuilder is a helper class for StyledLabel.\n" +
                "In this demo, we will show you how to use one annotated string to create StyledLabel with the help of StyledLabelBuilder.\n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.swing.StyledLabelBuilder\n" +
                "com.jidesoft.swing.StyledLabel";
    }

    public Component getDemoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new JideBoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(createLabelsPanel());
        panel.add(Box.createVerticalStrut(6));
        panel.add(createInputPanel());
        panel.add(Box.createVerticalStrut(6), JideBoxLayout.VARY);
        panel.add(createHelpPanel());
        panel.add(Box.createGlue(), JideBoxLayout.VARY);
        return panel;
    }

    private JComponent createLabelsPanel() {
        _label = new StyledLabel();
        StyledLabelBuilder.setStyledText(_label, INITIAL_TEXT);
        _label.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1, 10, 10));
        panel.add(_label);
        panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(
                new PartialLineBorder(Color.gray, 1, true), " Preview ",
                TitledBorder.CENTER, TitledBorder.CENTER), BorderFactory.createEmptyBorder(2, 4, 8, 4)));

        panel.setPreferredSize(new Dimension(200, 100));
        return panel;
    }

    private JComponent createHelpPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 1, 6, 6));
        panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(
                new PartialLineBorder(Color.gray, 1, true), " Help ",
                TitledBorder.CENTER, TitledBorder.CENTER), BorderFactory.createEmptyBorder(2, 4, 8, 4)));

        StyledLabel titleLabel = StyledLabelBuilder.createStyledLabel("{Font styles\\::b, f:blue}");
        titleLabel.setFont(titleLabel.getFont().deriveFont(12.0f));
        panel.add(titleLabel);
        panel.add(StyledLabelBuilder.createStyledLabel("   - {plain:b} or {p:b}, i.e. \\{plain text:p} => {plain text:p}"));
        panel.add(StyledLabelBuilder.createStyledLabel("   - {bold:b} or {b:b}, i.e. \\{bold text:b} => {bold text:b}"));
        panel.add(StyledLabelBuilder.createStyledLabel("   - {italic:b} or {i:b}, i.e. \\{italic text:i} => {italic text:i}"));
        panel.add(StyledLabelBuilder.createStyledLabel("   - {bolditalic:b} or {bi:b}, i.e. \\{bold and italic text:bi} => {bold and italic:bi}"));

        titleLabel = StyledLabelBuilder.createStyledLabel("{Line styles\\::b,f:blue}");
        titleLabel.setFont(titleLabel.getFont().deriveFont(12.0f));
        panel.add(titleLabel);
        panel.add(StyledLabelBuilder.createStyledLabel("   - {strike:b} or {s:b}, i.e. \\{strikethrough:s} => {strikethrough:s}"));
        panel.add(StyledLabelBuilder.createStyledLabel("   - {doublestrike:b} or {ds:b}, i.e. \\{double strikethrough:ds} => {double strikethrough:ds}"));
        panel.add(StyledLabelBuilder.createStyledLabel("   - {waved:b} or {w:b}, i.e. \\{waved:w} => {waved:w}"));
        panel.add(StyledLabelBuilder.createStyledLabel("   - {underlined:b} or {u:b}, i.e. \\{underlined:u} => {underlined:u}"));
        panel.add(StyledLabelBuilder.createStyledLabel("   - {dotted:b} or {d:b}, i.e. \\{dotted:d} => {dotted:d}"));
        panel.add(StyledLabelBuilder.createStyledLabel("   - {superscript:b} or {sp:b}, i.e. Java\\{TM:sp} => Java{TM:sp}"));
        panel.add(StyledLabelBuilder.createStyledLabel("   - {subscipt:b} or {sb:b}, i.e. CO\\{2:sb} => CO{2:sb}"));

        titleLabel = StyledLabelBuilder.createStyledLabel("{Using Colors\\::b,f:blue} using {f:b} for font color, {l:b} for line color and {b:b} for backgroundcolor");
        titleLabel.setFont(titleLabel.getFont().deriveFont(12.0f));
        panel.add(titleLabel);
        panel.add(StyledLabelBuilder.createStyledLabel("   - {f\\::b} plus color name defined in class Color, i.e. \\{red text:f:red} => {red text:f:red}"));
        panel.add(StyledLabelBuilder.createStyledLabel("   - {l\\::b}: plus color name defined in class Color, i.e. \\{red underline:u, l:red} => {red underline:u, l:red}"));
        panel.add(StyledLabelBuilder.createStyledLabel("   - {b\\::b}: plus color name defined in class Color, i.e. \\{red background:b:red} => {red background:b:red}"));
        panel.add(StyledLabelBuilder.createStyledLabel("   - {f\\::b} or {l\\::b} or {b\\::b}: plus #RRGGBB, i.e. \\{any color:f:#00AA55} => {any color:f:#00AA55}"));
        panel.add(StyledLabelBuilder.createStyledLabel("   - {f\\::b} or {l\\::b} or {b\\::b}: plus #RGB as in CSS, i.e. \\{any color:f:#0A5} => {any color:f:#0A5}"));
        panel.add(StyledLabelBuilder.createStyledLabel("   - {f\\::b} or {l\\::b}or {b\\::b}: plus (R, G, B), i.e. \\{any line color:s, l:(0, 220, 128)} or \\{any background color:b:(0, 120, 128)} => {any line color:s, l:(0, 220, 128)} or {any background color:b:(0, 120, 128)}"));

        titleLabel = StyledLabelBuilder.createStyledLabel("{Special characters\\::b, f:blue}");
        titleLabel.setFont(titleLabel.getFont().deriveFont(12.0f));
        panel.add(titleLabel);
        panel.add(StyledLabelBuilder.createStyledLabel("   - Special annotation characters {\\{:b} {\\}:b} {\\(:b} {\\):b} {\\#:b} {\\::b} {\\,:b} should be escaped by \"\\\\\" when they are used as regular text"));
        panel.add(StyledLabelBuilder.createStyledLabel("   - i.e. \\{\\\\\\{brace\\\\\\}:b} => {\\{brace\\}:b}"));
        panel.add(StyledLabelBuilder.createStyledLabel("   - If you need multiple styles connect them with {,:b} (comma), i.e. \\{bold red text:f:red, b} => {bold red text:f:red, b}"));

// the following code builder to registered new styles and use it as annotation.
//        StyledLabelBuilder styledLabelBuilder = new
//                StyledLabelBuilder().register("NORMAL_STYLE", Font.PLAIN, Color.RED).
//                register("ERROR_STYLE", Font.PLAIN, Color.RED).
//                register("UNDERLINE_STYLE", Font.PLAIN, Color.BLUE,
//                        StyleRange.STYLE_UNDERLINED, Color.BLUE).
//                register("UNDERLINE_STYLE2", "UNDERLINE_STYLE,f:yellow").
//                register("STRIKE_THROUGH_STYLE", Font.PLAIN, Color.GRAY,
//                        StyleRange.STYLE_STRIKE_THROUGH, Color.RED);
//        StyledLabel label = new StyledLabel();
//        styledLabelBuilder.configure(label, "{text:UNDERLINE_STYLE} {text:UNDERLINE_STYLE2}");
//        panel.add(label);
        return panel;
    }

    private JComponent createInputPanel() {
        JPanel panel = new JPanel(new BorderLayout(4, 4));
        panel.add(new JLabel("Annotated String: "), BorderLayout.BEFORE_LINE_BEGINS);
        final JTextField textField = new JTextField(INITIAL_TEXT);
        textField.setColumns(50);
        panel.add(textField, BorderLayout.CENTER);
        AbstractAction action = new AbstractAction("Update") {
            public void actionPerformed(ActionEvent e) {
                _label.clearStyleRanges();
                StyledLabelBuilder.setStyledText(_label, textField.getText());
            }
        };

        final JButton button = new JButton(action);
        button.setMnemonic('U');

        textField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                button.doClick();
            }
        });

        panel.add(button, BorderLayout.AFTER_LINE_ENDS);

        panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(
                new PartialLineBorder(Color.gray, 1, true), " Type in an annotated string and press enter to see the result ",
                TitledBorder.CENTER, TitledBorder.CENTER), BorderFactory.createEmptyBorder(2, 4, 8, 4)));
        return panel;
    }

    @Override
    public String getDemoFolder() {
        return "B15.StyledLabel";
    }

    static public void main(String[] s) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new StyledLabelBuilderDemo());
    }
}
