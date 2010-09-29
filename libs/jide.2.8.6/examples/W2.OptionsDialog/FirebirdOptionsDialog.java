/*
 * @(#)FirebirdOptionsDialog.java 2/15/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.dialog.*;
import com.jidesoft.pane.CollapsiblePane;
import com.jidesoft.plaf.CollapsiblePaneUI;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.plaf.UIDefaultsLookup;
import com.jidesoft.swing.JideSwingUtilities;
import com.jidesoft.swing.MultilineLabel;
import com.jidesoft.swing.PartialLineBorder;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.event.*;

/**
 * Demoed Component: {@link MultiplePageDialog} <br> Required jar files: jide-common.jar,
 * jide-dialogs.jar, jide-components.jar <br> Required L&F: Jide L&F extension required
 */
public class FirebirdOptionsDialog extends MultiplePageDialog {
    public FirebirdOptionsDialog(Frame owner, String title) throws HeadlessException {
        super(owner, title);
    }

    @Override
    protected void initComponents() {
        super.initComponents();
        getContentPanel().setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        getIndexPanel().setBackground(Color.white);
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

// the following commented code to show you how to put the icon panel on the top instead of on the left.
//    protected ButtonPanel createIconButtonPanel() {
//        return new ButtonPanel(SwingConstants.LEFT, ButtonPanel.SAME_SIZE);
//    }
//
//    protected JComponent setupContentPanel(JComponent indexPanel, JComponent pagesPanel) {
//        JPanel middlePanel = new JPanel(new BorderLayout(10, 10));
//        if (indexPanel != null) {
//            middlePanel.add(indexPanel, BorderLayout.BEFORE_FIRST_LINE);
//        }
//        if (pagesPanel != null) {
//            middlePanel.add(pagesPanel, BorderLayout.CENTER);
//        }
//        return middlePanel;
//    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(600, 520);
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showOptionsDialog(true);
    }

    public static void showOptionsDialog(final boolean exit) {
        final FirebirdOptionsDialog dialog = new FirebirdOptionsDialog(null, "Mozilla Firebird-like Option Dialog");
        dialog.setStyle(MultiplePageDialog.ICON_STYLE);
        PageList model = new PageList();
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

        // setup model
        AbstractDialogPage panel1 = new FirebirdOptionPage1("General", FirebirdIconsFactory.getImageIcon(FirebirdIconsFactory.Options.GENERAL));
        AbstractDialogPage panel2 = new FirebirdOptionPage2("Privacy", FirebirdIconsFactory.getImageIcon(FirebirdIconsFactory.Options.PRIVACY));
        AbstractDialogPage panel3 = new FirebirdOptionPage("Web Features", FirebirdIconsFactory.getImageIcon(FirebirdIconsFactory.Options.WEB));
        AbstractDialogPage panel4 = new FirebirdOptionPage("Themes", FirebirdIconsFactory.getImageIcon(FirebirdIconsFactory.Options.THEMES));
        AbstractDialogPage panel5 = new FirebirdOptionPage("Extensions", FirebirdIconsFactory.getImageIcon(FirebirdIconsFactory.Options.EXTENSIONS));
        AbstractDialogPage panel6 = new FirebirdOptionPage("Fonts & Color", FirebirdIconsFactory.getImageIcon(FirebirdIconsFactory.Options.FONTSCOLOR));
        AbstractDialogPage panel7 = new FirebirdOptionPage("Connections", FirebirdIconsFactory.getImageIcon(FirebirdIconsFactory.Options.CONNECTION));

        model.append(panel1);
        model.append(panel2);
        model.append(panel3);
        model.append(panel4);
        model.append(panel5);
        model.append(panel6);
        model.append(panel7);

        dialog.setPageList(model);

        dialog.pack();
        JideSwingUtilities.globalCenterWindow(dialog);
        dialog.setVisible(true);
    }

    public static class FirebirdOptionPage extends AbstractDialogPage {
        public FirebirdOptionPage(String name) {
            super(name);
        }

        public FirebirdOptionPage(String name, Icon icon) {
            super(name, icon);
        }

        public void lazyInitialize() {
            initComponents();
        }

        public void initComponents() {
            BannerPanel headerPanel = new BannerPanel(getTitle(), null);
            headerPanel.setForeground(Color.WHITE);
            headerPanel.setBackground(new Color(10, 36, 106));
            headerPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.white, Color.darkGray, Color.darkGray, Color.gray));

            setLayout(new BorderLayout());
            add(headerPanel, BorderLayout.BEFORE_FIRST_LINE);
            add(new JLabel("This is just a demo. \"" + getFullTitle() + "\" page is not implemented yet.", JLabel.CENTER), BorderLayout.CENTER);
        }

    }

    public static class FirebirdOptionPage1 extends FirebirdOptionPage {
        public FirebirdOptionPage1(String name, Icon icon) {
            super(name, icon);
        }

        @Override
        public void initComponents() {
            super.initComponents();
            add(createGeneralPage(this), BorderLayout.CENTER);
        }
    }

    public static class FirebirdOptionPage2 extends FirebirdOptionPage {
        public FirebirdOptionPage2(String name, Icon icon) {
            super(name, icon);
        }

        @Override
        public void initComponents() {
            super.initComponents();
            add(createPrivacyPage(), BorderLayout.CENTER);
        }
    }

    private static CompoundBorder createRoundCornerBorder(String title) {
        return BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(
                new PartialLineBorder(Color.gray, 1, true), title), BorderFactory.createEmptyBorder(0, 6, 4, 6));
    }

    private static JPanel createPrivacyPage() {
        MultilineLabel label = new MultilineLabel("As you browse the web, information about where you have been, " +
                "what you have done, etc is kept in the following areas");

        JComponent middlePanel = createPrivacyMiddlePanel();

        JPanel bottomPanel = new JPanel(new BorderLayout(6, 6));
        bottomPanel.add(new JLabel("Clear all information stored while browsing:", JLabel.RIGHT), BorderLayout.CENTER);
        bottomPanel.add(new JButton("Clear All"), BorderLayout.AFTER_LINE_ENDS);

        JPanel panel = new JPanel(new BorderLayout(6, 6));
        panel.add(label, BorderLayout.BEFORE_FIRST_LINE);
        panel.add(middlePanel, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.AFTER_LAST_LINE);
        panel.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        return panel;
    }

    private static JComponent createPrivacyMiddlePanel() {
        CollapsiblePane pane1 = createCollapsiblePane("History");
        pane1.getContentPane().setLayout(new FlowLayout(FlowLayout.LEADING));
        JLabel label1 = new JLabel("Remember visited pages for the last ");
        JTextField text1 = new JTextField("9 ");
        text1.setColumns(4);
        JLabel label2 = new JLabel(" days");
        pane1.getContentPane().add(label1);
        pane1.getContentPane().add(text1);
        pane1.getContentPane().add(label2);

        CollapsiblePane pane2 = createCollapsiblePane("Saved Form Information");
        pane2.getContentPane().setLayout(new BorderLayout(4, 4));
        MultilineLabel label3 = new MultilineLabel("Information entered in web  page forms and the Search Bar is saved to make filling out forms and searching faster.");
        JCheckBox check1 = new JCheckBox("Save information I enter in web page forms and the Search Bar");
        check1.setBackground(Color.white);
        pane2.getContentPane().add(label3, BorderLayout.CENTER);
        pane2.getContentPane().add(check1, BorderLayout.AFTER_LAST_LINE);

        CollapsiblePane pane3 = createCollapsiblePane("Saved Passwords");
        pane3.getContentPane().setLayout(new BorderLayout(4, 4));
        MultilineLabel label4 = new MultilineLabel("Login information for web pages can be kept in the Password Manager so that you do not need to re-enter your login details every time you visit.");
        JCheckBox check2 = new JCheckBox("Remember Passwords");
        check2.setBackground(Color.white);
        pane3.getContentPane().add(label4, BorderLayout.CENTER);
        pane3.getContentPane().add(check2, BorderLayout.AFTER_LAST_LINE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.add(pane1);
        panel.add(pane2);
        panel.add(pane3);
        panel.add(Box.createVerticalStrut(200));
        panel.setBackground(Color.white);
        return new JScrollPane(panel);
    }

    private static CollapsiblePane createCollapsiblePane(String title) {
        CollapsiblePane pane1 = new CollapsiblePane(title);
        pane1.setBackground(Color.white);
        pane1.setStyle(CollapsiblePane.PLAIN_STYLE);
        ((CollapsiblePaneUI) pane1.getUI()).getTitlePane().setFocusable(false);
        pane1.getContentPane().setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 0));
        pane1.getContentPane().setOpaque(false);
        pane1.getActualComponent().setBackground(Color.white);
        JComponent actualComponent = pane1.getActualComponent();
        JideSwingUtilities.setOpaqueRecursively(actualComponent, false);
        return pane1;
    }

    private static JPanel createGeneralPage(final AbstractDialogPage page) {
        // home page panel
        JPanel homePagePanel = new JPanel(new GridLayout(2, 1, 6, 6));
        JLabel label = new JLabel("Location(s):");
        label.setDisplayedMnemonic('a');
        final JTextField textField = new JTextField("http://www.mozilla.org/start");
        label.setLabelFor(textField);
        JPanel textPanel = new JPanel(new BorderLayout(6, 6));
        textPanel.add(label, BorderLayout.BEFORE_LINE_BEGINS);
        textPanel.add(textField, BorderLayout.CENTER);
        ButtonPanel buttonPanel = new ButtonPanel();
        buttonPanel.addButton(new JButton("Use Current Page"));
        buttonPanel.addButton(new JButton("Use Bookmark..."));
        buttonPanel.addButton(new JButton("Use Blank Page"));
        homePagePanel.add(textPanel);
        homePagePanel.add(buttonPanel);
        homePagePanel.setBorder(createRoundCornerBorder(" Home Page "));

        // windows and tabs panel
        JPanel windowsAndTabsPanel = new JPanel(new BorderLayout(6, 6));
        JCheckBox checkBox = new JCheckBox("Open Link in the background");
        checkBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);
            }
        });
        windowsAndTabsPanel.add(checkBox, BorderLayout.CENTER);
        windowsAndTabsPanel.setBorder(createRoundCornerBorder(" Windows and Tabs "));

        // download folder panel
        JPanel downloadPanel = new JPanel(new GridLayout(2, 1));
        JRadioButton radio1 = new JRadioButton("Ask me where to save every file");
        JRadioButton radio2 = new JRadioButton("Save all files to this folder:");
        downloadPanel.add(radio1);
        downloadPanel.add(radio2);
        ButtonGroup group = new ButtonGroup();
        radio1.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);
            }
        });
        radio2.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                page.fireButtonEvent(ButtonEvent.ENABLE_BUTTON, ButtonNames.APPLY);
            }
        });
        group.add(radio1);
        group.add(radio2);
        downloadPanel.setBorder(createRoundCornerBorder(" Download Folder "));

        // default browser panel
        JPanel defaultPanel = new JPanel(new BorderLayout());
        JLabel label1 = new JLabel("Set Mozilla Firebird as your default browser");
        defaultPanel.add(label1, BorderLayout.CENTER);
        defaultPanel.add(new JButton("Set Default Browser"), BorderLayout.AFTER_LINE_ENDS);
        defaultPanel.setBorder(createRoundCornerBorder(" Default Browser"));


        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

        panel.add(Box.createVerticalStrut(6));
        panel.add(homePagePanel);
        panel.add(Box.createVerticalStrut(6));
        panel.add(windowsAndTabsPanel);
        panel.add(Box.createVerticalStrut(6));
        panel.add(downloadPanel);
        panel.add(Box.createVerticalStrut(6));
        panel.add(defaultPanel);
        panel.add(Box.createVerticalStrut(600));
        return panel;
    }
}
