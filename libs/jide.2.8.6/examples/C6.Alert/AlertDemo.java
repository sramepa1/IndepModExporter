/*
 * @(#)AlertDemo.java 2/16/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.alert.Alert;
import com.jidesoft.alert.AlertGroup;
import com.jidesoft.animation.CustomAnimation;
import com.jidesoft.icons.IconsFactory;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.*;
import com.jidesoft.utils.PortingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Demoed Component: {@link com.jidesoft.alert.Alert} <br> Required jar files: jide-common.jar, jide-components.jar <br>
 * Required L&F: Jide L&F extension required
 */
public class AlertDemo extends AbstractDemo {

    public AnimationCustomizationPanel _entrancePanel;
    public AnimationCustomizationPanel _exitPanel;
    public LocationCustomizationPanel _locationPanel;
    private static final long serialVersionUID = -2231287006032485437L;

    public AlertDemo() {
    }

    public String getName() {
        return "AlertDemo";
    }

    public String getProduct() {
        return PRODUCT_NAME_COMPONENTS;
    }

    @Override
    public String[] getDemoSource() {
        return new String[]{"AlertDemo.java", "AnimationCustomizationPanel.java", "LocationCustomizationPanel.java"};
    }

    @Override
    public String getDescription() {
        return "Alert is a special popup that supports animation. It's an ideal component to display warning or error messages such as alerts, notificatons.\n" +
                "\nTo see the demo, press \"Show Alert\" button. You can adjust some options in options panel to see different animation options.\n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.alert.Alert\n" +
                "com.jidesoft.animaton.CustomAnimation";
    }

    private AlertGroup _alertGroup = new AlertGroup();

    public Component getDemoPanel() {

        final JPanel panel = new JPanel();
        panel.setLayout(new JideBoxLayout(panel, JideBoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        final JButton button = new JButton();
        final AbstractAction action = new AbstractAction("Show Alert") {
            private static final long serialVersionUID = 7783644087905367174L;

            public void actionPerformed(ActionEvent e) {
//                button.setEnabled(false);
                final Alert alert = new Alert();
                alert.getContentPane().setLayout(new BorderLayout());
                alert.getContentPane().add(createSampleAlert(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        CustomAnimation hideAnimation = _exitPanel.getAnimation();
                        hideAnimation.setVisibleBounds(PortingUtils.getLocalScreenBounds());
                        alert.setHideAnimation(hideAnimation);
                        alert.hidePopup();
                    }
                }));
                alert.addPropertyChangeListener("visible", new PropertyChangeListener() {
                    public void propertyChange(PropertyChangeEvent evt) {
                        if (Boolean.FALSE.equals(evt.getNewValue())) {
                            button.setEnabled(true);
                        }
                    }
                });
                alert.setOwner(button);
                alert.setResizable(true);
                alert.setMovable(true);
                alert.setTimeout(2000);
                alert.setTransient(false);
                alert.setPopupBorder(BorderFactory.createLineBorder(new Color(10, 30, 106)));

                CustomAnimation showAnimation = _entrancePanel.getAnimation();
                showAnimation.setVisibleBounds(PortingUtils.getLocalScreenBounds());
                alert.setShowAnimation(showAnimation);

                CustomAnimation hideAnimation = _exitPanel.getAnimation();
                hideAnimation.setVisibleBounds(PortingUtils.getLocalScreenBounds());
                alert.setHideAnimation(hideAnimation);

                _alertGroup.add(alert);

                alert.showPopup(_locationPanel.getDisplayLocation());
            }
        };
        button.setAction(action);
        panel.add(button);
        panel.add(Box.createVerticalStrut(12), JideBoxLayout.FIX);

        final JTextField textField = new JTextField("Press ENTER to show alert", 20);
        AbstractAction textFieldAction = new AbstractAction("Show Alert") {
            private static final long serialVersionUID = 23533742449906383L;

            public void actionPerformed(ActionEvent e) {
                final Alert alert = new Alert();
                alert.getContentPane().setLayout(new BorderLayout());
                alert.getContentPane().add(createSampleAlert(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        alert.hidePopupImmediately();
                    }
                }));
                alert.setOwner(textField);
                alert.setResizable(true);
                alert.setMovable(true);
                alert.setTransient(false);
                alert.setTimeout(2000);
                alert.setPopupBorder(BorderFactory.createLineBorder(new Color(10, 30, 106)));

                CustomAnimation showAnimation = _entrancePanel.getAnimation();
                showAnimation.setVisibleBounds(PortingUtils.getLocalScreenBounds());
                alert.setShowAnimation(showAnimation);

                CustomAnimation hideAnimation = _exitPanel.getAnimation();
                hideAnimation.setVisibleBounds(PortingUtils.getLocalScreenBounds());
                alert.setHideAnimation(hideAnimation);

                alert.showPopup(_locationPanel.getDisplayLocation());
            }
        };
        textField.setAction(textFieldAction);
        panel.add(textField);
        panel.add(Box.createVerticalStrut(12), JideBoxLayout.FIX);

        panel.add(Box.createGlue(), JideBoxLayout.VARY);
        return panel;
    }

    @Override
    public Component getOptionsPanel() {
        _entrancePanel = new AnimationCustomizationPanel(CustomAnimation.TYPE_ENTRANCE);
        _exitPanel = new AnimationCustomizationPanel(CustomAnimation.TYPE_EXIT);
        JideTabbedPane pane = new JideTabbedPane();
        pane.setTabShape(JideTabbedPane.SHAPE_BOX);
        pane.add("Entrance Animation", _entrancePanel);
        pane.add("Exit Animation", _exitPanel);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(6, 6));
        panel.add(pane, BorderLayout.CENTER);
        _locationPanel = new LocationCustomizationPanel();
        panel.add(_locationPanel, BorderLayout.BEFORE_FIRST_LINE);
        return panel;
    }

    @Override
    public String getDemoFolder() {
        return "C6.Alert";
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new AlertDemo());
    }

    public static JComponent createSampleAlert(ActionListener closeAction) {
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 0, 0));
        bottomPanel.add(createButton(IconsFactory.getImageIcon(AlertDemo.class, "icons/flag.png")));
        JideButton deleteButton = createButton(IconsFactory.getImageIcon(AlertDemo.class, "icons/delete.png"));
        deleteButton.addActionListener(closeAction);
        bottomPanel.add(deleteButton);

        JPanel leftPanel = new JPanel(new BorderLayout(6, 6));
        leftPanel.add(new JLabel(IconsFactory.getImageIcon(AlertDemo.class, "icons/mail.png")));
        leftPanel.add(bottomPanel, BorderLayout.AFTER_LAST_LINE);

        JPanel rightPanel = new JPanel(new GridLayout(1, 2, 0, 0));
        rightPanel.add(createButton(IconsFactory.getImageIcon(AlertDemo.class, "icons/option.png")));
        JideButton closeButton = createButton(IconsFactory.getImageIcon(AlertDemo.class, "icons/close.png"));
        closeButton.addActionListener(closeAction);
        rightPanel.add(closeButton);

        final String text = "<HTML><B>support@jidesoft.com</B><BR>" +
                "RE: Alert Demo<BR>" +
                "<FONT COLOR=BLUE>Hello,<BR>" +
                "This is a sample alert demo. It will disappear after 2s.</FONT></HTML>";
        final String highlightText = "<HTML><U><FONT COLOR=BLUE><B>support@jidesoft.com</B><BR>" +
                "RE: Alert Demo<BR>" +
                "Hello,<BR>" +
                "This is a sample alert demo. It will disappear after 2s.</FONT><U></HTML>";
        final JLabel message = new JLabel(text);

        message.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                message.setText(highlightText);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                message.setText(text);
            }
        });

        PaintPanel panel = new PaintPanel(new BorderLayout(6, 6));
        panel.setBorder(BorderFactory.createEmptyBorder(6, 7, 7, 7));
        panel.add(message, BorderLayout.CENTER);
        JPanel topPanel = JideSwingUtilities.createTopPanel(rightPanel);
        panel.add(topPanel, BorderLayout.AFTER_LINE_ENDS);
        panel.add(leftPanel, BorderLayout.BEFORE_LINE_BEGINS);
        for (int i = 0; i < panel.getComponentCount(); i++) {
            JideSwingUtilities.setOpaqueRecursively(panel.getComponent(i), false);
        }
        panel.setOpaque(true);
        panel.setBackgroundPaint(new GradientPaint(0, 0, new Color(231, 229, 224), 0, panel.getPreferredSize().height, new Color(212, 208, 200)));
        return panel;
    }

    private static JideButton createButton(Icon icon) {
        return new JideButton(icon);
    }
}

