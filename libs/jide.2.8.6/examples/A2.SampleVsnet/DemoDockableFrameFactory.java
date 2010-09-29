/*
 * @(#)DockableFrameFactory.java 2/18/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.docking.DockContext;
import com.jidesoft.docking.DockableFrame;
import com.jidesoft.swing.JideScrollPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 */
public class DemoDockableFrameFactory {
    public static DockableFrame createSampleProjectViewFrame() {
        DockableFrame frame = new DockableFrame("Project View", VsnetIconsFactory.getImageIcon(VsnetIconsFactory.Standard.SOLUTION));
        frame.getContext().setInitMode(DockContext.STATE_AUTOHIDE);
        frame.getContext().setInitSide(DockContext.DOCK_SIDE_EAST);
        frame.add(createScrollPane(new JTextArea()));
        frame.setPreferredSize(new Dimension(200, 200));
        return frame;
    }

    public static DockableFrame createSampleClassViewFrame() {
        DockableFrame frame = new DockableFrame("Class View", VsnetIconsFactory.getImageIcon(VsnetIconsFactory.Standard.CLASSVIEW));
        frame.getContext().setInitMode(DockContext.STATE_FRAMEDOCKED);
        frame.getContext().setInitSide(DockContext.DOCK_SIDE_EAST);
        frame.getContext().setInitIndex(1);
        frame.add(createScrollPane(new JTextArea()));
        frame.setPreferredSize(new Dimension(400, 200));
        frame.setTitle("Class View - SampleVsnet");
        frame.setTabTitle("Class View");
        return frame;
    }

    public static DockableFrame createSampleServerFrame() {
        DockableFrame frame = new DockableFrame("Server Explorer", VsnetIconsFactory.getImageIcon(VsnetIconsFactory.Standard.SERVER));
        frame.getContext().setInitMode(DockContext.STATE_AUTOHIDE);
        frame.getContext().setInitSide(DockContext.DOCK_SIDE_WEST);
        frame.getContext().setInitIndex(0);
        frame.add(createScrollPane(new JTextArea()));
        frame.setPreferredSize(new Dimension(200, 200));
        return frame;
    }

    public static DockableFrame createSampleResourceViewFrame() {
        DockableFrame frame = new DockableFrame("Resource View", VsnetIconsFactory.getImageIcon(VsnetIconsFactory.Standard.RESOURCEVIEW));
        frame.getContext().setInitMode(DockContext.STATE_FRAMEDOCKED);
        frame.getContext().setInitSide(DockContext.DOCK_SIDE_EAST);
        frame.getContext().setInitIndex(1);
        frame.add(createScrollPane(new JTextArea()));
        frame.setPreferredSize(new Dimension(200, 200));
        frame.setTitle("Resource View");
        return frame;
    }

    public static DockableFrame createSamplePropertyFrame() {
        DockableFrame frame = new DockableFrame("Property", VsnetIconsFactory.getImageIcon(VsnetIconsFactory.Standard.PROPERTY));
        frame.getContext().setInitMode(DockContext.STATE_FRAMEDOCKED);
        frame.getContext().setInitSide(DockContext.DOCK_SIDE_WEST);
        frame.getContext().setInitIndex(0);
        frame.add(createScrollPane(new JTextArea()));
        frame.setPreferredSize(new Dimension(200, 200));
        return frame;
    }

    public static DockableFrame createSampleTaskListFrame() {
        final DockableFrame frame = new DockableFrame("Task List", VsnetIconsFactory.getImageIcon(VsnetIconsFactory.Standard.TASKLIST));
        frame.getContext().setInitMode(DockContext.STATE_FRAMEDOCKED);
        frame.getContext().setInitSide(DockContext.DOCK_SIDE_SOUTH);
        JList list = new JList(new String[]{"Task1", "Task2", "Task3"});
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    frame.getDockingManager().notifyFrame("Property");
                }
            }
        });
        list.setToolTipText("This is a tooltip");
        frame.add(createScrollPane(list));
        frame.add(new JTextField(), BorderLayout.BEFORE_FIRST_LINE);
        frame.add(new JTextField(), BorderLayout.AFTER_LAST_LINE);
        frame.setPreferredSize(new Dimension(200, 200));
        frame.setMinimumSize(new Dimension(100, 100));
        return frame;
    }

    public static DockableFrame createSampleCommandFrame() {
        DockableFrame frame = new DockableFrame("Command", VsnetIconsFactory.getImageIcon(VsnetIconsFactory.Standard.COMMAND));
        frame.getContext().setInitMode(DockContext.STATE_FRAMEDOCKED);
        frame.getContext().setInitSide(DockContext.DOCK_SIDE_SOUTH);
        frame.getContext().setInitIndex(1);
        JTextArea textArea = new JTextArea();
        frame.add(createScrollPane(textArea));
        textArea.setText(">");
        frame.setPreferredSize(new Dimension(200, 200));
        return frame;
    }

    public static DockableFrame createSampleOutputFrame() {
        DockableFrame frame = new DockableFrame("Output", VsnetIconsFactory.getImageIcon(VsnetIconsFactory.Standard.OUTPUT));
        frame.getContext().setInitMode(DockContext.STATE_FRAMEDOCKED);
        frame.getContext().setInitSide(DockContext.DOCK_SIDE_SOUTH);
        frame.getContext().setInitIndex(0);
        frame.add(createScrollPane(new JTextArea()));
        frame.setPreferredSize(new Dimension(200, 200));
        return frame;
    }

    public static DockableFrame createSampleFindResult1Frame() {
        DockableFrame frame = new DockableFrame("Find Results 1", VsnetIconsFactory.getImageIcon(VsnetIconsFactory.Standard.FINDRESULT1));
        frame.getContext().setInitMode(DockContext.STATE_FRAMEDOCKED);
        frame.getContext().setInitSide(DockContext.DOCK_SIDE_SOUTH);
        frame.getContext().setInitIndex(0);
        JTextArea textArea = new JTextArea();
        frame.add(createScrollPane(textArea));
        textArea.setText("Find all \"TestDock\", Match case, Whole word, Find Results 1, All Open Documents\n" +
                "C:\\Projects\\src\\com\\jidesoft\\test\\TestDock.java(1):// TestDock.java : implementation of the TestDock class\n" +
                "C:\\Projects\\src\\jidesoft\\test\\TestDock.java(8):#import com.jidesoft.test.TestDock;\n" +
                "C:\\Projects\\src\\com\\jidesoft\\Test.java(10):#import com.jidesoft.test.TestDock;\n" +
                "Total found: 3    Matching files: 5    Total files searched: 5");
        frame.setPreferredSize(new Dimension(200, 200));
        return frame;
    }

    public static DockableFrame createSampleFindResult2Frame() {
        DockableFrame frame = new DockableFrame("Find Results 2", VsnetIconsFactory.getImageIcon(VsnetIconsFactory.Standard.FINDRESULT2));
        frame.getContext().setInitMode(DockContext.STATE_FRAMEDOCKED);
        frame.getContext().setInitSide(DockContext.DOCK_SIDE_SOUTH);
        frame.getContext().setInitIndex(1);
        JTextArea textArea = new JTextArea();
        frame.add(createScrollPane(textArea));
        textArea.setText("Find all \"TestDock\", Match case, Whole word, Find Results 2, All Open Documents\n" +
                "C:\\Projects\\src\\com\\jidesoft\\test\\TestDock.java(1):// TestDock.java : implementation of the TestDock class\n" +
                "C:\\Projects\\src\\jidesoft\\test\\TestDock.java(8):#import com.jidesoft.test.TestDock;\n" +
                "C:\\Projects\\src\\com\\jidesoft\\Test.java(10):#import com.jidesoft.test.TestDock;\n" +
                "Total found: 3    Matching files: 5    Total files searched: 5");
        frame.setPreferredSize(new Dimension(200, 200));
        return frame;
    }

    public static JScrollPane createScrollPane(Component component) {
        JScrollPane pane = new JideScrollPane(component);
        pane.setFocusable(false);
        return pane;
    }
}
