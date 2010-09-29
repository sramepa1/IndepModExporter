/*
 * @(#)ListStyleOptionsDialog.java
 *
 * Copyright 2002 - 2003 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.dialog.*;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.plaf.UIDefaultsLookup;
import com.jidesoft.swing.JideSwingUtilities;
import com.jidesoft.swing.PartialEtchedBorder;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Demoed Component: {@link MultiplePageDialog} <br> Required jar files: jide-common.jar,
 * jide-dialogs.jar <br> Required L&F: Jide L&F extension required
 */
public class ListStyleOptionsDialog extends MultiplePageDialog {
    public ListStyleOptionsDialog(Frame owner, String title) throws HeadlessException {
        super(owner, title);
    }

    private static Border createSeparatorBorder() {
        return new PartialEtchedBorder(EtchedBorder.LOWERED, PartialEtchedBorder.NORTH);
    }

    @Override
    protected void initComponents() {
        super.initComponents();
        getContentPanel().setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        getIndexPanel().setOpaque(true);
        JLabel label = new JLabel("Category");
        getIndexPanel().add(label, BorderLayout.BEFORE_FIRST_LINE);
        getButtonPanel().setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        getPagesPanel().setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
    }

    @Override
    public ButtonPanel createButtonPanel() {
        ButtonPanel buttonPanel = super.createButtonPanel();
        AbstractAction okAction = new AbstractAction(UIDefaultsLookup.getString("OptionPane.okButtonText")) {
            public void actionPerformed(ActionEvent e) {
                setDialogResult(RESULT_AFFIRMED);
                setVisible(false);
                dispose();
            }
        };
        AbstractAction cancelAction = new AbstractAction(UIDefaultsLookup.getString("OptionPane.cancelButtonText")) {
            public void actionPerformed(ActionEvent e) {
                setDialogResult(RESULT_CANCELLED);
                setVisible(false);
                dispose();
            }
        };
        ((JButton) buttonPanel.getButtonByName(ButtonNames.OK)).setAction(okAction);
        ((JButton) buttonPanel.getButtonByName(ButtonNames.CANCEL)).setAction(cancelAction);
        setDefaultCancelAction(cancelAction);
        setDefaultAction(okAction);
        return buttonPanel;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(600, 500);
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showOptionsDialog(true);
    }

    public static void showOptionsDialog(final boolean exit) {
        final MultiplePageDialog dialog = new ListStyleOptionsDialog(null, "List Style Option Dialog");
        dialog.setStyle(MultiplePageDialog.LIST_STYLE);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                if (exit) {
                    System.exit(0);
                }
                else {
                    dialog.dispose();
                }
            }
        });
        PageList model = new PageList();

        // setup model
        AbstractDialogPage panel1 = new ListOptionPage("General");
        AbstractDialogPage panel2 = new ListOptionPage("Accessibility");
        AbstractDialogPage panel3 = new ListOptionPage("Code Coloring");
        AbstractDialogPage panel4 = new ListOptionPage("Code Format");
        AbstractDialogPage panel5 = new ListOptionPage("Code Hints");
        AbstractDialogPage panel6 = new ListOptionPage("Code Rewriting");
        AbstractDialogPage panel7 = new ListOptionPage("CSS Styles");
        AbstractDialogPage panel8 = new ListOptionPage("File Type / Editors");
        AbstractDialogPage panel9 = new ListOptionPage("Fonts");
        AbstractDialogPage panel10 = new ListOptionPage("Highlighting");
        AbstractDialogPage panel11 = new ListOptionPage("Invisible Elements");
        AbstractDialogPage panel12 = new ListOptionPage("Layers");
        AbstractDialogPage panel13 = new ListOptionPage("Layout Mode");
        AbstractDialogPage panel14 = new ListOptionPage("New Document");
        AbstractDialogPage panel15 = new ListOptionPage("Office Copy/Paste");
        AbstractDialogPage panel16 = new ListOptionPage("Panels");
        AbstractDialogPage panel17 = new ListOptionPage("Preview in Browser");
        AbstractDialogPage panel18 = new ListOptionPage("Site");
        AbstractDialogPage panel19 = new ListOptionPage("Status Bar");
        AbstractDialogPage panel20 = new ListOptionPage("Validator");
        model.append(panel1);
        model.append(panel2);
        model.append(panel3);
        model.append(panel4);
        model.append(panel5);
        model.append(panel6);
        model.append(panel7);
        model.append(panel8);
        model.append(panel9);
        model.append(panel10);
        model.append(panel11);
        model.append(panel12);
        model.append(panel13);
        model.append(panel14);
        model.append(panel15);
        model.append(panel16);
        model.append(panel17);
        model.append(panel18);
        model.append(panel19);
        model.append(panel20);

        dialog.setPageList(model);

        dialog.pack();
        JideSwingUtilities.globalCenterWindow(dialog);
        dialog.setVisible(true);
    }

    public static class ListOptionPage extends AbstractDialogPage {
        public ListOptionPage(String name) {
            super(name);
        }

        public ListOptionPage(String name, Icon icon) {
            super(name, icon);
        }

        public void lazyInitialize() {
            initComponents();
        }

        public void initComponents() {
            JPanel headerPanel = new JPanel(new BorderLayout(4, 4));
            JLabel label = new JLabel(getTitle());
            headerPanel.add(label, BorderLayout.BEFORE_FIRST_LINE);
            JPanel panel = new JPanel();
            panel.setBorder(createSeparatorBorder());
            headerPanel.add(panel, BorderLayout.CENTER);

            setLayout(new BorderLayout());
            add(headerPanel, BorderLayout.BEFORE_FIRST_LINE);
            add(new JLabel("This is just a demo. \"" + getFullTitle() + "\" page is not implemented yet.", JLabel.CENTER), BorderLayout.CENTER);
        }

    }
}
