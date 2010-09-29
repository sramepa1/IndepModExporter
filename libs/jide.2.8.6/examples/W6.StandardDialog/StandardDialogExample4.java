/*
 * @(#)StandardDialogExample4.java 5/25/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.dialog.ButtonPanel;
import com.jidesoft.dialog.StandardDialog;
import com.jidesoft.plaf.UIDefaultsLookup;
import com.jidesoft.swing.MultilineLabel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 */
public class StandardDialogExample4 extends StandardDialog {
    public StandardDialogExample4() throws HeadlessException {
        super((Frame) null, "Standard Dialog Example");
    }

    @Override
    public JComponent createBannerPanel() {
        return null;
    }

    @Override
    public JComponent createContentPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

        JLabel label = new JLabel("Initial Focused Component: ");
        label.setDisplayedMnemonic('I');
        label.setHorizontalAlignment(SwingConstants.CENTER);

        JTextField textField = new JTextField();
        label.setLabelFor(textField);

        JPanel topPanel = new JPanel(new BorderLayout(6, 6));
        topPanel.add(label, BorderLayout.BEFORE_LINE_BEGINS);
        topPanel.add(textField, BorderLayout.CENTER);
        panel.add(topPanel, BorderLayout.BEFORE_FIRST_LINE);

        JTextArea textArea = new MultilineLabel();
        textArea.setColumns(50);
        textArea.setRows(20);
        textArea.setText("This is an example using StandardDialog." +
                "\n\nIn this example, we create a dialog with some example contents in content area, and a button panel with two buttons and a special check box on the same line as _panes. Please note, it's the ButtonPanel supporting this feature. You can see the source code to find out how we did it.");
        panel.add(new JScrollPane(textArea), BorderLayout.CENTER);
        setInitFocusedComponent(textField);
        return panel;
    }

    @Override
    public ButtonPanel createButtonPanel() {
        ButtonPanel buttonPanel = new ButtonPanel();
        JButton okButton = new JButton();
        JButton cancelButton = new JButton();
        JCheckBox checkbox = new JCheckBox("Don't show this dialog again.");
        checkbox.setMnemonic('D');
        okButton.setName(OK);
        cancelButton.setName(CANCEL);
        buttonPanel.addButton(okButton, ButtonPanel.AFFIRMATIVE_BUTTON);
        buttonPanel.addButton(cancelButton, ButtonPanel.CANCEL_BUTTON);
        buttonPanel.addButton(checkbox, ButtonPanel.HELP_BUTTON);

        okButton.setAction(new AbstractAction(UIDefaultsLookup.getString("OptionPane.okButtonText")) {
            public void actionPerformed(ActionEvent e) {
                setDialogResult(RESULT_AFFIRMED);
                setVisible(false);
                dispose();
            }
        });
        cancelButton.setAction(new AbstractAction(UIDefaultsLookup.getString("OptionPane.cancelButtonText")) {
            public void actionPerformed(ActionEvent e) {
                setDialogResult(RESULT_CANCELLED);
                setVisible(false);
                dispose();
            }
        });

        setDefaultCancelAction(cancelButton.getAction());
        setDefaultAction(okButton.getAction());
        getRootPane().setDefaultButton(okButton);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonPanel.setSizeConstraint(ButtonPanel.NO_LESS_THAN); // since the checkbox is quite wide, we don't want all of them have the same size.
        return buttonPanel;
    }
}
