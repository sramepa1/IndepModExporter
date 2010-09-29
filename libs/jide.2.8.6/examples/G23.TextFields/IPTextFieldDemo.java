/*
 * @(#)IPTextFieldDemo.java 4/6/2006
 *
 * Copyright 2002 - 2006 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.field.IPTextField;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.JideBoxLayout;
import com.jidesoft.swing.JideSwingUtilities;

import javax.swing.*;
import java.awt.*;

/**
 * Demoed Component: {@link com.jidesoft.field.IPTextField} <br> Required jar files:
 * jide-common.jar, jide-grids.jar <br> Required L&F: any L&F
 */
public class IPTextFieldDemo extends AbstractDemo {
    public IPTextFieldDemo() {
    }

    public String getName() {
        return "IPTextField Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_GRIDS;
    }

    @Override
    public int getAttributes() {
        return ATTRIBUTE_BETA;
    }

    @Override
    public String getDescription() {
        return "This is a demo of IPTextField. It can be used to edit IP address.\n" +
                "Demoed classes:\n" +
                "com.jidesoft.field.IPTextField";
    }

    private static int GAP = 4;

//    public Component getOptionsPanel() {
//        return new JButton(new AbstractAction("Get"){
//            public void actionPerformed(ActionEvent e) {
//                System.out.println(_ipTextField.isValueValid());
//                System.out.println(_ipTextField.getRawText()[0] + "." + _ipTextField.getRawText()[1] + "." + _ipTextField.getRawText()[1] + "." + _ipTextField.getRawText()[3]);
//                System.out.println(_ipTextField.getValue());
//                System.out.println(_ipTextField.getText());
//                _ipTextField.setRawText(new String[]{"1232", "-123", "AAA"});
//                System.out.println(_ipTextField.getRawText()[0] + "." + _ipTextField.getRawText()[1] + "." + _ipTextField.getRawText()[1] + "." + _ipTextField.getRawText()[3]);
//            }
//        });
//    }

    public Component getDemoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new JideBoxLayout(panel, JideBoxLayout.Y_AXIS));

        IPTextField ipTextField = new IPTextField();
        panel.add(new JLabel("IP Address: "));
        panel.add(Box.createVerticalStrut(GAP));
        panel.add(ipTextField);
        panel.add(Box.createVerticalStrut(GAP * 2));

        IPTextField maskTextField = new IPTextField();
        panel.add(new JLabel("Class B Mask: "));
        panel.add(Box.createVerticalStrut(GAP));
        maskTextField.setSubnetMask("255.255.0.0");
        panel.add(maskTextField);
        panel.add(Box.createVerticalStrut(GAP * 2));

        panel.add(new JLabel("Disabled: "));
        panel.add(Box.createVerticalStrut(GAP));
        IPTextField disabledIPTextField = new IPTextField("192.168.0.1");
        disabledIPTextField.setEnabled(false);
        panel.add(disabledIPTextField);
        panel.add(Box.createVerticalStrut(GAP * 2));

        panel.add(new JLabel("Not editable: "));
        panel.add(Box.createVerticalStrut(GAP));
        IPTextField notEditableIPTextField = new IPTextField(new int[]{192, 168, 0, 1});
        notEditableIPTextField.setEditable(false);
        panel.add(notEditableIPTextField);
        panel.add(Box.createVerticalStrut(GAP * 2));

//        JTextField textField = new JTextField("ABCDE", 20);
//        textField.setEditable(false);
//        panel.add(textField);
//
//        panel.add(Box.createVerticalStrut(GAP * 2));
//
//        JTextField textField2 = new JTextField("ABCDE", 20);
//        textField2.setEditable(false);
//        textField2.setEnabled(false);
//        panel.add(textField2);

        panel.add(Box.createGlue(), JideBoxLayout.VARY);

        return JideSwingUtilities.createLeftPanel(panel);
    }

    @Override
    public String getDemoFolder() {
        return "G23.TextFields";
    }

    static public void main(String[] s) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new IPTextFieldDemo());
    }
}
