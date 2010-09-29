/*
 * @(#)StandardDialogExample1.java 5/25/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.dialog.BannerPanel;
import com.jidesoft.dialog.ButtonPanel;
import com.jidesoft.dialog.ButtonResources;
import com.jidesoft.dialog.StandardDialog;
import com.jidesoft.icons.JideIconsFactory;
import com.jidesoft.plaf.UIDefaultsLookup;
import com.jidesoft.swing.MultilineLabel;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 */
public class StandardDialogExample1 extends StandardDialog {
    public StandardDialogExample1() throws HeadlessException {
        super((Frame) null, "Standard Dialog Example");
    }

    @Override
    public JComponent createBannerPanel() {
        BannerPanel headerPanel1 = new BannerPanel("This is a BannerPanel", "BannerPanel is very useful to display a title, a description and an icon. It can be used in dialog to show some help information or display a product logo in a nice way.",
                JideIconsFactory.getImageIcon(JideIconsFactory.JIDE32));
        headerPanel1.setFont(new Font("Tahoma", Font.PLAIN, 11));
        headerPanel1.setBackground(Color.WHITE);
        headerPanel1.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        return headerPanel1;
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
                "\n\nIn this example, we create a dialog with banner, some example contents in content area, and a button panel with three buttons at the bottom of the dialog.");
        panel.add(new JScrollPane(textArea), BorderLayout.CENTER);
        setInitFocusedComponent(textField);
        return panel;
    }

    @Override
    public ButtonPanel createButtonPanel() {
        ButtonPanel buttonPanel = new ButtonPanel();
        JButton okButton = new JButton();
        JButton cancelButton = new JButton();
        JButton helpButton = new JButton();
        okButton.setName(OK);
        cancelButton.setName(CANCEL);
        helpButton.setName(HELP);
        buttonPanel.addButton(okButton, ButtonPanel.AFFIRMATIVE_BUTTON);
        buttonPanel.addButton(cancelButton, ButtonPanel.CANCEL_BUTTON);
        buttonPanel.addButton(helpButton, ButtonPanel.HELP_BUTTON);

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
        final ResourceBundle resourceBundle = ButtonResources.getResourceBundle(Locale.getDefault());
        helpButton.setAction(new AbstractAction(resourceBundle.getString("Button.help")) {
            public void actionPerformed(ActionEvent e) {
                // do something
            }
        });
        helpButton.setMnemonic(resourceBundle.getString("Button.help.mnemonic").charAt(0));

        setDefaultCancelAction(cancelButton.getAction());
        setDefaultAction(okButton.getAction());
        getRootPane().setDefaultButton(okButton);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return buttonPanel;
    }
}
