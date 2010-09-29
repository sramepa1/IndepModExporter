/*
 * @(#)StatusBarDemo.java
 *
 * Copyright 2002 - 2003 JIDE Software. All rights reserved.
 */

import com.jidesoft.icons.JideIconsFactory;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.status.*;
import com.jidesoft.swing.JideBoxLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Demoed Component: {@link StatusBar} <br> Required jar files: jide-common.jar, jide-components.jar <br> Required L&F:
 * Jide L&F extension required
 */
public class StatusBarDemo extends AbstractDemo {

    private Timer _timer;

    public String getName() {
        return "StatusBar Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_COMPONENTS;
    }

    @Override
    public String getDescription() {
        return "Status bar is never a central part of an application but almost every application has it. " +
                "StatusBar is an very extensible implementation of status bar. " +
                "It supports several built-in status bar item such as ProgressStatusBarItem, LabelStatusBarItem, ButtonStatusBarItemTimeStatusBarItem, and MemoryStatusBarItem. You can also create your own StatusBarItem.\n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.status.StatusBar\n" +
                "com.jidesoft.status.StatusBarItem";
    }

    public Component getDemoPanel() {
        // add status bar
        StatusBar statusBar = createStatusBar();
        JPanel dummyPanel = new JPanel();
        dummyPanel.setPreferredSize(new Dimension(600, 300));
        JPanel panel = new JPanel(new BorderLayout(6, 6));
        panel.add(dummyPanel, BorderLayout.CENTER);
        panel.add(statusBar, BorderLayout.AFTER_LAST_LINE);
        return panel;
    }

    public StatusBarDemo() {
    }

    @Override
    public String getDemoFolder() {
        return "C2.StatusBar";
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new StatusBarDemo());
    }

    private StatusBar createStatusBar() {
        // setup status bar
        StatusBar statusBar = new StatusBar();
        final ProgressStatusBarItem progress = new ProgressStatusBarItem();
        progress.setCancelCallback(new ProgressStatusBarItem.CancelCallback() {
            public void cancelPerformed() {
                _timer.stop();
                _timer = null;
                progress.setStatus("Cancelled");
                progress.showStatus();
            }
        });
        statusBar.add(progress, JideBoxLayout.VARY);
        ButtonStatusBarItem button = new ButtonStatusBarItem("READ-ONLY");
        button.setIcon(JideIconsFactory.getImageIcon(JideIconsFactory.SAVE));
        button.setPreferredWidth(20);
        statusBar.add(button, JideBoxLayout.FLEXIBLE);

        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (_timer != null && _timer.isRunning())
                    return;
                _timer = new Timer(100, new ActionListener() {
                    int i = 0;

                    public void actionPerformed(ActionEvent e) {
                        if (i == 0)
                            progress.setProgressStatus("Initializing ......");
                        if (i == 10)
                            progress.setProgressStatus("Running ......");
                        if (i == 90)
                            progress.setProgressStatus("Completing ......");
                        progress.setProgress(i++);
                        if (i > 100)
                            _timer.stop();
                    }
                });
                _timer.start();
            }
        });

        final LabelStatusBarItem label = new LabelStatusBarItem("Line");
        label.setText("100:42");
        label.setAlignment(JLabel.CENTER);
        statusBar.add(label, JideBoxLayout.FLEXIBLE);

        final OvrInsStatusBarItem ovr = new OvrInsStatusBarItem();
        ovr.setPreferredWidth(100);
        ovr.setAlignment(JLabel.CENTER);
        statusBar.add(ovr, JideBoxLayout.FLEXIBLE);

        final TimeStatusBarItem time = new TimeStatusBarItem();
        statusBar.add(time, JideBoxLayout.FLEXIBLE);
        final MemoryStatusBarItem gc = new MemoryStatusBarItem();
        statusBar.add(gc, JideBoxLayout.FLEXIBLE);

        return statusBar;
    }
}
