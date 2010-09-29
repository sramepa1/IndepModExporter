/*
 * @(#)SimplestActionFrameworkDemo.java 7/6/2008
 *
 * Copyright 2002 - 2008 JIDE Software Inc. All rights reserved.
 *
 */

import com.jidesoft.action.CommandBar;
import com.jidesoft.action.DefaultDockableBarHolder;
import com.jidesoft.action.DockableBar;
import com.jidesoft.icons.JideIconsFactory;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.JideButton;

import javax.swing.*;
import java.awt.*;

/**
 * This is a sample program for JIDE Docking Framework. It will create a JFrame with about just one dockable frames to
 * show the how easy it is to create an application using JIDE Docking Framework. <br> Required jar files:
 * jide-common.jar, jide-dock.jar <br> Required L&F: Jide L&F extension required
 */
public class SimplestActionFrameworkDemo extends DefaultDockableBarHolder {

    private static final String PROFILE_NAME = "jidesoft-simplest-action";

    private SimplestActionFrameworkDemo(String title) throws HeadlessException {
        super(title);
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showDemo();
    }

    private static void showDemo() {
        SimplestActionFrameworkDemo frame = new SimplestActionFrameworkDemo("Demo of JIDE Action Framework");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getDockableBarManager().setProfileKey(PROFILE_NAME);
        frame.getDockableBarManager().addDockableBar(createDockableBar("Example"));
        frame.getDockableBarManager().loadLayoutData();
        frame.toFront();
    }

    private static DockableBar createDockableBar(String key) {
        DockableBar bar = new CommandBar(key);
        bar.add(createButton(JideIconsFactory.getImageIcon(JideIconsFactory.DockableFrame.FRAME1)));
        bar.add(createButton(JideIconsFactory.getImageIcon(JideIconsFactory.DockableFrame.FRAME2)));
        bar.add(createButton(JideIconsFactory.getImageIcon(JideIconsFactory.DockableFrame.FRAME3)));
        bar.add(createButton(JideIconsFactory.getImageIcon(JideIconsFactory.DockableFrame.FRAME4)));
        return bar;
    }

    private static JideButton createButton(Icon icon) {
        JideButton button = new JideButton(icon);
        button.setRequestFocusEnabled(false);
        button.setFocusable(false);
        return button;
    }
}