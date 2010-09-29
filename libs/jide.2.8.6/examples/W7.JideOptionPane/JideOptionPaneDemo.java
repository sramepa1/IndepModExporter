/*
 * @(#)JideOptionPaneDemo.java 3/27/2006
 *
 * Copyright 2002 - 2006 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.dialog.JideOptionPane;
import com.jidesoft.icons.JideIconsFactory;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.plaf.UIDefaultsLookup;
import com.jidesoft.plaf.basic.ThemePainter;
import com.jidesoft.swing.JideBoxLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Demoed Component: {@link com.jidesoft.dialog.JideOptionPane} <br> Required jar files: jide-common.jar,
 * jide-dialog.jar <br> Required L&F: Jide L&F extension required
 */
public class JideOptionPaneDemo extends AbstractDemo {
    public JPanel _demoPanel;

    public JideOptionPaneDemo() {
    }

    public String getName() {
        return "JideOptionPaneDemo";
    }

    public String getProduct() {
        return PRODUCT_NAME_DIALOGS;
    }

    @Override
    public int getAttributes() {
        return ATTRIBUTE_BETA;
    }

    @Override
    public String getDescription() {
        return "JideOptionPane is an enhanced version of Swing's JOptionPane. \n" +
                "\n1. Supports banner panel to get a better looking message box. " +
                "\n2. Supports details button." +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.dialog.JideOptionPane";
    }


    public Component getDemoPanel() {
        LookAndFeelFactory.UIDefaultsCustomizer uiDefaultsCustomizer = new LookAndFeelFactory.UIDefaultsCustomizer() {
            public void customize(UIDefaults defaults) {
                ThemePainter painter = (ThemePainter) UIDefaultsLookup.get("Theme.painter");
                defaults.put("OptionPaneUI", "com.jidesoft.plaf.basic.BasicJideOptionPaneUI");

                defaults.put("OptionPane.showBanner", Boolean.TRUE); // show banner or not. default is true
                defaults.put("OptionPane.bannerIcon", JideIconsFactory.getImageIcon(JideIconsFactory.JIDE50));
                defaults.put("OptionPane.bannerFontSize", 13);
                defaults.put("OptionPane.bannerFontStyle", Font.BOLD);
                defaults.put("OptionPane.bannerMaxCharsPerLine", 60);
                defaults.put("OptionPane.bannerForeground", painter != null ? painter.getOptionPaneBannerForeground() : null);  // you should adjust this if banner background is not the default gradient paint
                defaults.put("OptionPane.bannerBorder", null); // use default border

                // set both bannerBackgroundDk and // set both bannerBackgroundLt to null if you don't want gradient
                defaults.put("OptionPane.bannerBackgroundDk", painter != null ? painter.getOptionPaneBannerDk() : null);
                defaults.put("OptionPane.bannerBackgroundLt", painter != null ? painter.getOptionPaneBannerLt() : null);
                defaults.put("OptionPane.bannerBackgroundDirection", Boolean.TRUE); // default is true

                // optionally, you can set a Paint object for BannerPanel. If so, the three UIDefaults related to banner background above will be ignored.
                defaults.put("OptionPane.bannerBackgroundPaint", null);

                defaults.put("OptionPane.buttonAreaBorder", BorderFactory.createEmptyBorder(6, 6, 6, 6));
                defaults.put("OptionPane.buttonOrientation", SwingConstants.RIGHT);
            }
        };
        uiDefaultsCustomizer.customize(UIManager.getDefaults());

        _demoPanel = new JPanel() {
            @Override
            public Dimension getMaximumSize() {
                return new Dimension(getPreferredSize().width, super.getMaximumSize().height);
            }
        };
        _demoPanel.setLayout(new JideBoxLayout(_demoPanel, JideBoxLayout.Y_AXIS));

        JPanel panel1 = new JPanel(new GridLayout(0, 1, 10, 10));
        panel1.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Standard JOptionPane Features"),
                BorderFactory.createEmptyBorder(5, 10, 10, 10)));
        panel1.add(createInputDialogButton());
        panel1.add(createWarningDialogButton());
        panel1.add(createComponentDialogButton());
        panel1.add(createConfirmDialogButton());

        JPanel panel2 = new JPanel(new GridLayout(0, 1, 10, 10));
        panel2.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("JideOptionPane Features"),
                BorderFactory.createEmptyBorder(5, 10, 10, 10)));
        panel2.add(createDetailsDialogButton());
        panel2.add(createProgressDialogButton());

        _demoPanel.add(panel2);
        _demoPanel.add(Box.createRigidArea(new Dimension(1, 15)));
        _demoPanel.add(panel1);
        _demoPanel.add(Box.createVerticalGlue(), JideBoxLayout.VARY);

        return _demoPanel;
    }

    public JButton createWarningDialogButton() {
        Action a = new AbstractAction("Show Warning Dialog") {
            public void actionPerformed(ActionEvent e) {
                JideOptionPane.showMessageDialog(_demoPanel,
                        "<html><P><font color=black>This is a test of the <font color=red><b>Emergency Broadcast System</b></font>. <i><b>This is <br> only a test</b></i>.  The webmaster of your local intranet, in voluntary <br> cooperation with the <font color=blue><b>Federal</b></font> and <font color=blue><b>State</b></font> authorities, have <br> developed this system to keep you informed in the event of an <br> emergency. If this had been an actual emergency, the signal you <br> just heard would have been followed by official information, news <br> or instructions. This concludes this test of the <font color=red><b>Emergency <br> Broadcast System</b></font>.</font></P><P><br>Developer Note: This dialog demo used HTML for text formatting.</P></html>",
                        "Warning Dialog Example",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        };
        return createButton(a);
    }

    public JButton createConfirmDialogButton() {
        Action a = new AbstractAction("Show Confirmation Dialog") {
            public void actionPerformed(ActionEvent e) {
                int result = JideOptionPane.showConfirmDialog(getDemoPanel(), "Is the sun shining outside today?");
                if (result == JOptionPane.YES_OPTION) {
                    JideOptionPane.showMessageDialog(getDemoPanel(), "<html>Well what are you doing playing on the computer?<br> Get outside! Take a trip to the beach! Get a little sun!</html>");
                }
                else if (result == JOptionPane.NO_OPTION) {
                    JideOptionPane.showMessageDialog(getDemoPanel(), "Well good thing you're inside protected from the elements!");
                }
            }
        };
        return createButton(a);
    }

    public JButton createInputDialogButton() {
        Action a = new AbstractAction("Show Input Dialog") {
            public void actionPerformed(ActionEvent e) {
                String result = JideOptionPane.showInputDialog(getDemoPanel(), "What is your favorite movie?");
                if ((result != null) && (result.length() > 0)) {
                    JideOptionPane.showMessageDialog(getDemoPanel(),
                            result + ": " + "That was a pretty good movie!");
                }
            }
        };
        return createButton(a);
    }

    public JButton createComponentDialogButton() {
        Action a = new AbstractAction("Show Component Dialog") {
            public void actionPerformed(ActionEvent e) {
                // In a ComponentDialog, you can show as many message components and
                // as many options as you want:

                // Messages
                Object[] message = new Object[4];
                message[0] = "<html>JOptionPane can contain as many components <br> as you want, such as a text field:</html>";
                message[1] = new JTextField("or a combobox:");

                JComboBox cb = new JComboBox();
                cb.addItem("item 1");
                cb.addItem("item 2");
                cb.addItem("item 3");
                message[2] = cb;

                message[3] = "<html>JOptionPane can also show as many options <br> as you want:</html>";

                // Options
                String[] options = {
                        "Yes",
                        "No",
                        "Maybe",
                        "Probably",
                        "Cancel"
                };
                int result = JideOptionPane.showOptionDialog(
                        getDemoPanel(),                             // the parent that the dialog blocks
                        message,                                    // the dialog message array
                        "Component Dialog Example",                 // the title of the dialog window
                        JOptionPane.DEFAULT_OPTION,                 // option type
                        JOptionPane.INFORMATION_MESSAGE,            // message type
                        null,                                       // optional icon, use null to use the default icon
                        options,                                    // options string array, will be made into buttons
                        options[3]                                  // option that should be made into a default button
                );
                switch (result) {
                    case 0: // yes
                        JideOptionPane.showMessageDialog(getDemoPanel(), "Upbeat and positive! I like that! Good choice.");
                        break;
                    case 1: // no
                        JideOptionPane.showMessageDialog(getDemoPanel(), "Definitely not, I wouldn't do it either.");
                        break;
                    case 2: // maybe
                        JideOptionPane.showMessageDialog(getDemoPanel(), "<html><font color=black> Mmmm.. yes, the situation is unclear at this <br> time. Check back when you know for sure.</font></html>");
                        break;
                    case 3: // probably
                        JideOptionPane.showMessageDialog(getDemoPanel(), "<html><font color=black>You know you want to. I think you should <br> have gone for broke and pressed \"Yes\".</font></html>");
                        break;
                    default:
                        break;
                }

            }
        };
        return createButton(a);
    }

    public JButton createDetailsDialogButton() {
        Action a = new AbstractAction("Show Details Dialog") {
            public void actionPerformed(ActionEvent e) {
                String details = ("java.lang.Exception: Stack trace\n" +
                        "\tat java.awt.Component.processMouseEvent(Component.java:5957)\n" +
                        "\tat javax.swing.JComponent.processMouseEvent(JComponent.java:3284)\n" +
                        "\tat java.awt.Component.processEvent(Component.java:5722)\n" +
                        "\tat java.awt.Container.processEvent(Container.java:1966)\n" +
                        "\tat java.awt.Component.dispatchEventImpl(Component.java:4365)\n" +
                        "\tat java.awt.Container.dispatchEventImpl(Container.java:2024)\n" +
                        "\tat java.awt.Component.dispatchEvent(Component.java:4195)\n" +
                        "\tat java.awt.LightweightDispatcher.retargetMouseEvent(Container.java:4228)\n" +
                        "\tat java.awt.LightweightDispatcher.processMouseEvent(Container.java:3892)\n" +
                        "\tat java.awt.LightweightDispatcher.dispatchEvent(Container.java:3822)\n" +
                        "\tat java.awt.Container.dispatchEventImpl(Container.java:2010)\n" +
                        "\tat java.awt.Window.dispatchEventImpl(Window.java:2299)\n" +
                        "\tat java.awt.Component.dispatchEvent(Component.java:4195)\n" +
                        "\tat java.awt.EventQueue.dispatchEvent(EventQueue.java:599)\n" +
                        "\tat java.awt.EventDispatchThread.pumpOneEventForFilters(EventDispatchThread.java:273)\n" +
                        "\tat java.awt.EventDispatchThread.pumpEventsForFilter(EventDispatchThread.java:183)\n" +
                        "\tat java.awt.EventDispatchThread.pumpEventsForHierarchy(EventDispatchThread.java:173)\n" +
                        "\tat java.awt.EventDispatchThread.pumpEvents(EventDispatchThread.java:168)\n" +
                        "\tat java.awt.EventDispatchThread.pumpEvents(EventDispatchThread.java:160)\n" +
                        "\tat java.awt.EventDispatchThread.run(EventDispatchThread.java:121)");
                JideOptionPane optionPane = new JideOptionPane("Click \"Details\" button to see more information ... ", JOptionPane.ERROR_MESSAGE, JideOptionPane.CLOSE_OPTION);
                optionPane.setTitle("An exception happened during file transfers - if the title is very long, it will wrap automatically.");
                optionPane.setDetails(details);
                JDialog dialog = optionPane.createDialog(_demoPanel, "Warning");
                dialog.setResizable(true);
                dialog.pack();
                dialog.setVisible(true);
            }
        };
        return createButton(a);
    }

    public JButton createProgressDialogButton() {
        Action a = new AbstractAction("Show Progress Dialog") {
            public void actionPerformed(ActionEvent e) {
                JProgressBar progressBar = new JProgressBar(0, 100);
                progressBar.setValue(28);

                JPanel panel = new JPanel();
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                panel.add(new JLabel("Percent Complete: " + progressBar.getValue() + "%"));
                panel.add(Box.createVerticalStrut(3));
                panel.add(progressBar);
                panel.add(Box.createVerticalStrut(3));
                panel.add(new JLabel("Scranning C:\\Program Files\\...\\win.ini ..."));
                JideOptionPane optionPane = new JideOptionPane(panel, JOptionPane.INFORMATION_MESSAGE, 0, null, new Object[]{
                        new JButton("Pause")});
                optionPane.setTitle("Scanning computer for virus ...");

                String details = ("List scanned files here ... ");
                optionPane.setDetails(details);

                JDialog dialog = optionPane.createDialog(_demoPanel, "Please wait");
                dialog.setResizable(true);
                dialog.pack();
                dialog.setVisible(true);
            }
        };
        return createButton(a);
    }

    public JButton createButton(Action a) {
        JButton b = new JButton() {
            @Override
            public Dimension getMaximumSize() {
                int width = Short.MAX_VALUE;
                int height = super.getMaximumSize().height;
                return new Dimension(width, height);
            }
        };
        // setting the following client property informs the button to show
        // the action text as it's name. The default is to not show the
        // action text.
        b.putClientProperty("displayActionText", Boolean.TRUE);
        b.setAction(a);
        // b.setAlignmentX(JButton.CENTER_ALIGNMENT);
        return b;
    }

    @Override
    public String getDemoFolder() {
        return "W7.JideOptionPane";
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new JideOptionPaneDemo());
    }
}
