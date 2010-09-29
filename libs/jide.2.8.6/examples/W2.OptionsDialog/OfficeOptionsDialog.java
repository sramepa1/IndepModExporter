/*
 * @(#)OfficeOptionsDialog.java 2/15/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.dialog.*;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.plaf.UIDefaultsLookup;
import com.jidesoft.swing.JideSwingUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Demoed Component: {@link com.jidesoft.dialog.MultiplePageDialog} <br> Required jar files:
 * jide-common.jar, jide-dialogs.jar <br> Required L&F: Jide L&F extension required
 */
public class OfficeOptionsDialog extends MultiplePageDialog {

    public OfficeOptionsDialog(Frame owner, String title) throws HeadlessException {
        super(owner, title);
    }

    @Override
    protected void initComponents() {
        super.initComponents();
        getContentPanel().setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        getButtonPanel().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
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
        return new Dimension(400, 500);
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showOptionsDialog(true);

    }

    public static void showOptionsDialog(final boolean exit) {
        final MultiplePageDialog dialog = new OfficeOptionsDialog(null, "Microsoft Office Option Dialog");
        dialog.setStyle(MultiplePageDialog.TAB_STYLE);
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

        AbstractDialogPage panel1 = new OfficeOptionPage("View");
        AbstractDialogPage panel2 = new OfficeOptionPage("General");
        AbstractDialogPage panel3 = new OfficeOptionPage("Edit");
        AbstractDialogPage panel4 = new OfficeOptionPage("Print");
        AbstractDialogPage panel5 = new OfficeOptionPage("Save");
        AbstractDialogPage panel6 = new OfficeOptionPage("Security");
        AbstractDialogPage panel7 = new OfficeOptionPage("Asian Typography");
        AbstractDialogPage panel8 = new OfficeOptionPage("File Locations");
        AbstractDialogPage panel9 = new OfficeOptionPage("Japanese Find");
        AbstractDialogPage panel10 = new OfficeOptionPage("Spelling & Grammar");
        AbstractDialogPage panel11 = new OfficeOptionPage("Track Changes");
        AbstractDialogPage panel12 = new OfficeOptionPage("User Information");
        AbstractDialogPage panel13 = new OfficeOptionPage("Compatibility");

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

        dialog.setPageList(model);

        dialog.pack();
        JideSwingUtilities.globalCenterWindow(dialog);
        dialog.setVisible(true);
    }

    public static class OfficeOptionPage extends AbstractDialogPage {
        public OfficeOptionPage(String name) {
            super(name);
        }

        public OfficeOptionPage(String name, Icon icon) {
            super(name, icon);
        }

        public void lazyInitialize() {
            initComponents();
        }

        public void initComponents() {
            setLayout(new BorderLayout());
            add(new JLabel("This is just a demo. \"" + getFullTitle() + "\" page is not implemented yet.", JLabel.CENTER), BorderLayout.CENTER);
        }
    }

}
