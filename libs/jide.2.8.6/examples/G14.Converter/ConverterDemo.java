/*
 * @(#)ConverterDemo.java 5/12/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.combobox.AbstractComboBox;
import com.jidesoft.combobox.ListComboBox;
import com.jidesoft.converter.ObjectConverterManager;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.JideBoxLayout;

import javax.swing.*;
import java.awt.*;

/**
 * Demoed Component: {@link com.jidesoft.combobox.AbstractComboBox}, {@link
 * com.jidesoft.combobox.PopupPanel} <br> Required jar files: jide-common.jar, jide-grids.jar <br>
 * Required L&F: Jide L&F extension required
 */
public class ConverterDemo extends AbstractDemo {
    public ConverterDemo() {
    }

    public String getName() {
        return "Converter Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_COMMON;
    }

    @Override
    public String getDescription() {
        return "This is a demo of how to use ObjectConverter and use it in ListComboBox.\n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.combobox.ObjectConverter\n" +
                "com.jidesoft.combobox.ListComboBox";
    }

    public Component getDemoPanel() {
        // register two converters. Both for CustomType but the first one is the default, the second one is for a special case.
        ObjectConverterManager.registerConverter(CustomType.class, new DefaultCustomTypeConverter());
        ObjectConverterManager.registerConverter(CustomType.class, new SpecialCustomTypeConverter(), SpecialCustomTypeConverter.CONTEXT);

        JPanel panel = new JPanel();
        panel.setLayout(new JideBoxLayout(panel, BoxLayout.Y_AXIS));

        CustomType[] customTypes = new CustomType[]{
                new CustomType(1, "JIDE Docking Framework"),
                new CustomType(2, "JIDE Action Framework"),
                new CustomType(3, "JIDE Components"),
                new CustomType(4, "JIDE Grids"),
                new CustomType(5, "JIDE Dialogs"),
        };

        panel.add(new JLabel("<HTML>We defined a CustomType class which has two fields. See below." +
                "<PRE><BR> <FONT COLOR=\"BLUE\">public class</FONT> CustomType {" +
                "<BR>    <FONT COLOR=\"BLUE\">int</FONT> <FONT COLOR=\"PURPLE\">_intValue</FONT>;" +
                "<BR>    String <FONT COLOR=\"PURPLE\">_stringValue</FONT>;" +
                "<BR> }" +
                "<BR></PRE>" +
                "</HTML>"));

        panel.add(Box.createVerticalStrut(24), JideBoxLayout.FIX);

        panel.add(new JLabel("This ListComboBox uses default converter for this custom type"));
        panel.add(Box.createVerticalStrut(6), JideBoxLayout.FIX);

        AbstractComboBox comboBox1 = createListComboBox(customTypes);
        panel.add(comboBox1);

        panel.add(Box.createVerticalStrut(12), JideBoxLayout.FIX);

        panel.add(new JLabel("This one uses a special converter for this custom type"));
        panel.add(Box.createVerticalStrut(6), JideBoxLayout.FIX);
        AbstractComboBox comboBox2 = createListComboBox(customTypes);
//        comboBox2.setEditable(false);
        // set the context to use the special converter.
        comboBox2.setConverterContext(SpecialCustomTypeConverter.CONTEXT);
        panel.add(comboBox2);

        panel.add(Box.createVerticalStrut(12), JideBoxLayout.FIX);

        panel.add(Box.createGlue(), JideBoxLayout.VARY);
        return panel;
    }

    @Override
    public String getDemoFolder() {
        return "G14.Converter";
    }

    @Override
    public String[] getDemoSource() {
        return new String[]{"ConverterDemo.java", "CustomType.java", "DefaultCustomTypeConverter.java", "SpecialCustomTypeConverter.java"};
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new ConverterDemo());
    }

    private ListComboBox createListComboBox(Object[] objects) {
        ListComboBox comboBox = new ListComboBox(objects, CustomType.class);
        return comboBox;
    }
}
