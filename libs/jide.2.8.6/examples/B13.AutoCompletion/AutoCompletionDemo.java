/*
 * @(#)AutoCompletionDemo.java 6/24/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.*;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

/**
 * Demoed Component: {@link com.jidesoft.swing.AutoCompletion}, {@link com.jidesoft.swing.AutoCompletionComboBox}. <br>
 * Required jar files: jide-common.jar, jide-grids.jar <br> Required L&F: Jide L&F extension required
 */
public class AutoCompletionDemo extends AbstractDemo {
    private static final long serialVersionUID = -3703831246278663243L;

    protected String[] _fontNames;
    protected List<String> _fontList;

    public AutoCompletionDemo() {
    }

    public String getName() {
        return "AutoCompletion Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_COMMON;
    }

    @Override
    public String getDescription() {
        return "This is a demo of several AutoCompletion components. \n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.swing.AutoCompletion\n" +
                "com.jidesoft.swing.AutoCompletionComboBox";
    }

    public Component getDemoPanel() {
        _fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        _fontList = Arrays.asList(_fontNames);

        JPanel panel1 = createPanel1();
        JPanel panel2 = createPanel2();

        JPanel panel = new JPanel(new BorderLayout(6, 6));
        panel.add(panel1, BorderLayout.BEFORE_FIRST_LINE);
        panel.add(panel2);
        return panel;
    }

    private JPanel createPanel1() {
        JPanel panel = new JPanel();
        panel.setLayout(new JideBoxLayout(panel, JideBoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(new JideTitledBorder(new PartialEtchedBorder(PartialEtchedBorder.LOWERED, PartialSide.NORTH), "AutoCompletion combo box and text field", JideTitledBorder.LEADING, JideTitledBorder.ABOVE_TOP),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)));

        JComboBox autoCompletionComboBox = new AutoCompletionComboBox(_fontNames);
        autoCompletionComboBox.setName("AutoCompletion JComboBox (Strict)");
        autoCompletionComboBox.setToolTipText("AutoCompletion JComboBox (Strict)");
        panel.add(new JLabel("AutoCompletion JComboBox (Strict)"));
        panel.add(Box.createVerticalStrut(3), JideBoxLayout.FIX);
        panel.add(autoCompletionComboBox);
        panel.add(Box.createVerticalStrut(12), JideBoxLayout.FIX);

        AutoCompletionComboBox autoCompletionComboBoxNotStrict = new AutoCompletionComboBox(_fontNames);
        autoCompletionComboBoxNotStrict.setStrict(false);
        autoCompletionComboBoxNotStrict.setName("AutoCompletion JComboBox (Not strict)");
        autoCompletionComboBoxNotStrict.setToolTipText("AutoCompletion JComboBox (Not strict)");
        panel.add(new JLabel("AutoCompletion JComboBox (Not strict)"));
        panel.add(Box.createVerticalStrut(3), JideBoxLayout.FIX);
        panel.add(autoCompletionComboBoxNotStrict);
        panel.add(Box.createVerticalStrut(12), JideBoxLayout.FIX);

        // create tree combobox
        final JTextField textField = new JTextField();
        textField.setName("AutoCompletion JTextField with a hidden data");
        SelectAllUtils.install(textField);
        new AutoCompletion(textField, _fontList);
        panel.add(new JLabel("AutoCompletion JTextField with a hidden data"));
        panel.add(Box.createVerticalStrut(3), JideBoxLayout.FIX);
        panel.add(textField);
        panel.add(Box.createVerticalStrut(12), JideBoxLayout.FIX);

//        panel.add(Box.createVerticalStrut(24), JideBoxLayout.FIX);
//        panel.add(new JLabel("As comparisons:"));
//        panel.add(Box.createVerticalStrut(6), JideBoxLayout.FIX);
//
//        JComboBox searchableComboBox = new JComboBox(_fontNames);
//        searchableComboBox.setEditable(false);
//        SearchableUtils.installSearchable(searchableComboBox);
//        searchableComboBox.setToolTipText("Searchable JComboBox");
//        panel.add(new JLabel("Searchable JComboBox"));
//        panel.add(Box.createVerticalStrut(3), JideBoxLayout.FIX);
//        panel.add(searchableComboBox);
//        panel.add(Box.createVerticalStrut(12), JideBoxLayout.FIX);
//
//        JTextField completionTextField = new JTextField();
//        new ListCompletion(completionTextField, _fontNames);
//        completionTextField.setToolTipText("Completion JTextField (not auto-complete)");
//        panel.add(new JLabel("Completion JTextField (not auto-complete)"));
//        panel.add(Box.createVerticalStrut(3), JideBoxLayout.FIX);
//        panel.add(completionTextField);
//        panel.add(Box.createVerticalStrut(12), JideBoxLayout.FIX);

        return panel;
    }

    private JPanel createPanel2() {
        JPanel panel = new JPanel();
        panel.setLayout(new JideBoxLayout(panel, JideBoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(new JideTitledBorder(new PartialEtchedBorder(PartialEtchedBorder.LOWERED, PartialSide.NORTH), "AutoCompletion with list and tree", JideTitledBorder.LEADING, JideTitledBorder.ABOVE_TOP),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)));

        // create tree combobox
        final JTextField treeTextField = new JTextField();
        treeTextField.setName("AutoCompletion JTextField with JTree");
        SelectAllUtils.install(treeTextField);
        final JTree tree = new JTree();
        tree.setVisibleRowCount(10);
        final TreeSearchable searchable = new TreeSearchable(tree);
        searchable.setRecursive(true);
        new AutoCompletion(treeTextField, searchable);
        panel.add(new JLabel("AutoCompletion JTextField with JTree"));
        panel.add(Box.createVerticalStrut(3), JideBoxLayout.FIX);
        panel.add(treeTextField);
        panel.add(Box.createVerticalStrut(2), JideBoxLayout.FIX);
        panel.add(new JScrollPane(tree));
        panel.add(Box.createVerticalStrut(12), JideBoxLayout.FIX);

        // create font name combobox
        final JTextField fontNameTextField = new JTextField();
        fontNameTextField.setName("AutoCompletion JTextField with JList");
        SelectAllUtils.install(fontNameTextField);
        final JList fontNameList = new JList(_fontNames);
        fontNameList.setVisibleRowCount(10);
        new AutoCompletion(fontNameTextField, new ListSearchable(fontNameList));
        panel.add(new JLabel("AutoCompletion JTextField with JList"));
        panel.add(Box.createVerticalStrut(3), JideBoxLayout.FIX);
        panel.add(fontNameTextField);
        panel.add(Box.createVerticalStrut(2), JideBoxLayout.FIX);
        panel.add(new JScrollPane(fontNameList));
        panel.add(Box.createVerticalStrut(12), JideBoxLayout.FIX);

        return panel;
    }

    @Override
    public String getDemoFolder() {
        return "B13.AutoCompletion";
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new AutoCompletionDemo());
    }
}
