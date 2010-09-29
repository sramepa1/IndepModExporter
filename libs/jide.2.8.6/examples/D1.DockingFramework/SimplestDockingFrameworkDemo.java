/*
 * @(#)SimplestDockingFrameworkDemo.java 10/11/2007
 *
 * Copyright 2002 - 2007 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.docking.DefaultDockableHolder;
import com.jidesoft.docking.DockableFrame;
import com.jidesoft.icons.JideIconsFactory;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.JideScrollPane;

import javax.swing.*;
import java.awt.*;

/**
 * This is a sample program for JIDE Docking Framework. It will create a JFrame with about just one dockable frames to
 * show the how easy it is to create an application using JIDE Docking Framework. <br> Required jar files:
 * jide-common.jar, jide-dock.jar <br> Required L&F: Jide L&F extension required
 */
public class SimplestDockingFrameworkDemo extends DefaultDockableHolder {

    private static final String PROFILE_NAME = "jidesoft-simplest";

    public SimplestDockingFrameworkDemo(String title) throws HeadlessException {
        super(title);
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showDemo();
    }

    public static void showDemo() {
        SimplestDockingFrameworkDemo frame = new SimplestDockingFrameworkDemo("Demo of JIDE Docking Framework");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getDockingManager().setProfileKey(PROFILE_NAME);
        frame.getDockingManager().addFrame(createDockableFrame("Example", JideIconsFactory.getImageIcon(JideIconsFactory.DockableFrame.BLANK)));
        frame.getDockingManager().loadLayoutData();
        frame.toFront();
    }

    protected static DockableFrame createDockableFrame(String key, Icon icon) {
        DockableFrame frame = new DockableFrame(key, icon);
        frame.setPreferredSize(new Dimension(200, 200));
        frame.add(new JideScrollPane(new JTextArea()));
        return frame;
    }
}
