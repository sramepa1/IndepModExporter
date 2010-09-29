/*
 * @(#)TwoFramesDockingFrameworkDemo.java 9/20/2006
 *
 * Copyright 2002 - 2006 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.action.CommandBarFactory;
import com.jidesoft.docking.DefaultDockingManager;
import com.jidesoft.docking.DockContext;
import com.jidesoft.docking.DockableFrame;
import com.jidesoft.docking.DockableHolderPanel;
import com.jidesoft.icons.JideIconsFactory;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.JideMenu;
import com.jidesoft.swing.JideScrollPane;
import com.jidesoft.utils.Lm;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * This is a sample program for JIDE Docking Framework showing how to use two DockingManager in the
 * same JFrame. It will create a JFrame with about a tabbed pane with two tabs. Each tab has its own
 * DockingManager. <br> Required jar files: jide-common.jar, jide-dock.jar <br> Required L&F: Jide
 * L&F extension required
 */
public class TwoFramesDockingFrameworkDemo {

    private static final String PROFILE_NAME = "jidesoft-two-frames";

    private static WindowAdapter _windowListener;

    static JFrame _firstFrame;
    static JFrame _secondFrame;
    static DockableHolderPanel _firstPanel;
    static DockableHolderPanel _secondPanel;

    public TwoFramesDockingFrameworkDemo() {
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        TwoFramesDockingFrameworkDemo.showDemo(true);
    }

    public static void showDemo(final boolean exit) {
        if (_firstFrame != null && _secondFrame != null) {
            _firstFrame.toFront();
            _secondFrame.toFront();
            return;
        }
        _firstFrame = new JFrame("Frame1");
        _secondFrame = new JFrame("Frame2");
        _firstFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        _firstFrame.setIconImage(JideIconsFactory.getImageIcon(JideIconsFactory.JIDE32).getImage());
        _secondFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        _secondFrame.setIconImage(JideIconsFactory.getImageIcon(JideIconsFactory.JIDE32).getImage());

        // add a widnow listener so that timer can be stopped when exit
        _windowListener = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                TwoFramesDockingFrameworkDemo.clearUp();
                if (exit) {
                    System.exit(0);
                }
            }
        };
        _firstFrame.addWindowListener(_windowListener);
        _secondFrame.addWindowListener(_windowListener);

        // add menu bar
        _firstFrame.setJMenuBar(TwoFramesDockingFrameworkDemo.createMenuBar(_firstFrame));
        _secondFrame.setJMenuBar(TwoFramesDockingFrameworkDemo.createMenuBar(_secondFrame));

        _firstPanel = new DockableHolderPanel(_firstFrame);
        _firstPanel.getDockingManager().setProfileKey(PROFILE_NAME + "-1");
        _secondPanel = new DockableHolderPanel(_secondFrame);
        _secondPanel.getDockingManager().setProfileKey(PROFILE_NAME + "-2");

        _firstFrame.getContentPane().setLayout(new BorderLayout());
        _firstFrame.getContentPane().add(_firstPanel, BorderLayout.CENTER);

        _secondFrame.getContentPane().setLayout(new BorderLayout());
        _secondFrame.getContentPane().add(_secondPanel, BorderLayout.CENTER);

        _firstPanel.getDockingManager().setInitSplitPriority(DefaultDockingManager.SPLIT_SOUTH_NORTH_EAST_WEST);

        // add all dockable frames
        _firstPanel.getDockingManager().addFrame(createSampleResourceViewFrame());
        _firstPanel.getDockingManager().addFrame(createSampleClassViewFrame());
        _firstPanel.getDockingManager().addFrame(createSampleProjectViewFrame());

        _secondPanel.getDockingManager().beginLoadLayoutData();

        _secondPanel.getDockingManager().setInitSplitPriority(DefaultDockingManager.SPLIT_SOUTH_NORTH_EAST_WEST);

        _secondPanel.getDockingManager().addFrame(createSampleTaskListFrame());
        _secondPanel.getDockingManager().addFrame(createSampleCommandFrame());
        _secondPanel.getDockingManager().addFrame(createSampleOutputFrame());

        _firstPanel.getDockingManager().setInitBounds(new Rectangle(0, 0, 800, 600));
        _secondPanel.getDockingManager().setInitBounds(new Rectangle(50, 50, 800, 600));

        _firstPanel.getDockingManager().loadLayoutDataFrom("first_default");
        _secondPanel.getDockingManager().loadLayoutDataFrom("second_default");

        _firstPanel.getDockingManager().getWorkspace().setAcceptDockableFrame(true);
        _firstPanel.getDockingManager().setEasyTabDock(true);
        _secondPanel.getDockingManager().getWorkspace().setAcceptDockableFrame(true);
        _secondPanel.getDockingManager().setEasyTabDock(true);

        _firstPanel.getDockingManager().setCrossDraggingAllowed(true);
        _firstPanel.getDockingManager().setCrossDroppingAllowed(true);
        _secondPanel.getDockingManager().setCrossDroppingAllowed(true);
        _secondPanel.getDockingManager().setCrossDraggingAllowed(true);

        _firstFrame.toFront();
        _secondFrame.toFront();
    }

    private static void clearUp() {
        _firstFrame.removeWindowListener(_windowListener);
        _secondFrame.removeWindowListener(_windowListener);
        _windowListener = null;

        if (_firstPanel.getDockingManager() != null) {
            _firstPanel.getDockingManager().saveLayoutDataAs("first_default");
        }
        if (_secondPanel.getDockingManager() != null) {
            _secondPanel.getDockingManager().saveLayoutDataAs("second_default");
        }

        _firstFrame.dispose();
        _firstFrame = null;
        _secondFrame.dispose();
        _secondFrame = null;
    }

    protected static DockableFrame createSampleProjectViewFrame() {
        DockableFrame frame = new DockableFrame("Project View", JideIconsFactory.getImageIcon(JideIconsFactory.DockableFrame.FRAME1));
        frame.getContext().setInitMode(DockContext.STATE_FRAMEDOCKED);
        frame.getContext().setInitSide(DockContext.DOCK_SIDE_WEST);
        frame.getContentPane().add(createScrollPane(new JTextArea()));
        frame.setPreferredSize(new Dimension(200, 200));
        return frame;
    }

    protected static DockableFrame createSampleClassViewFrame() {
        DockableFrame frame = new DockableFrame("Class View", JideIconsFactory.getImageIcon(JideIconsFactory.DockableFrame.FRAME2));
        frame.getContext().setInitMode(DockContext.STATE_FRAMEDOCKED);
        frame.getContext().setInitSide(DockContext.DOCK_SIDE_EAST);
        frame.getContext().setInitIndex(1);
        frame.getContentPane().add(createScrollPane(new JTextArea()));
        frame.setPreferredSize(new Dimension(400, 200));
        frame.setTitle("Class View - DockingFrameworkDemo");
        frame.setTabTitle("Class View");
        return frame;
    }

    protected static DockableFrame createSampleResourceViewFrame() {
        DockableFrame frame = new DockableFrame("Resource View", JideIconsFactory.getImageIcon(JideIconsFactory.DockableFrame.FRAME4));
        frame.getContext().setInitMode(DockContext.STATE_FRAMEDOCKED);
        frame.getContext().setInitSide(DockContext.DOCK_SIDE_EAST);
        frame.getContext().setInitIndex(1);
        frame.getContentPane().add(createScrollPane(new JTextArea()));
        frame.setPreferredSize(new Dimension(200, 200));
        frame.setTitle("Resource View");
        return frame;
    }

    protected static DockableFrame createSampleTaskListFrame() {
        DockableFrame frame = new DockableFrame("Task List", JideIconsFactory.getImageIcon(JideIconsFactory.DockableFrame.FRAME6));
        frame.getContext().setInitMode(DockContext.STATE_FRAMEDOCKED);
        frame.getContext().setInitSide(DockContext.DOCK_SIDE_SOUTH);
        JList list = new JList(new String[]{"Task1", "Task2", "Task3"});
        list.setToolTipText("This is a tooltip");
        frame.getContentPane().add(createScrollPane(list));
        frame.setPreferredSize(new Dimension(200, 200));
        frame.setMinimumSize(new Dimension(100, 100));
        return frame;
    }

    protected static DockableFrame createSampleCommandFrame() {
        DockableFrame frame = new DockableFrame("Command", JideIconsFactory.getImageIcon(JideIconsFactory.DockableFrame.FRAME7));
        frame.getContext().setInitMode(DockContext.STATE_FRAMEDOCKED);
        frame.getContext().setInitSide(DockContext.DOCK_SIDE_SOUTH);
        frame.getContext().setInitIndex(1);
        JTextArea textArea = new JTextArea();
        frame.getContentPane().add(createScrollPane(textArea));
        textArea.setText(">");
        frame.setPreferredSize(new Dimension(200, 200));
        return frame;
    }

    protected static DockableFrame createSampleOutputFrame() {
        DockableFrame frame = new DockableFrame("Output", JideIconsFactory.getImageIcon(JideIconsFactory.DockableFrame.FRAME8));
        frame.getContext().setInitMode(DockContext.STATE_FRAMEDOCKED);
        frame.getContext().setInitSide(DockContext.DOCK_SIDE_SOUTH);
        frame.getContext().setInitIndex(0);
        frame.getContentPane().add(createScrollPane(new JTextArea()));
        frame.setPreferredSize(new Dimension(200, 200));
        return frame;
    }

    protected static JMenuBar createMenuBar(JFrame frame) {
        JMenuBar menu = new JMenuBar();

        JMenu fileMenu = createFileMenu();
        JMenu lnfMenu = CommandBarFactory.createLookAndFeelMenu(frame);
        JMenu helpMenu = createHelpMenu();

        menu.add(fileMenu);
        menu.add(lnfMenu);
        menu.add(helpMenu);


        return menu;
    }

    private static JScrollPane createScrollPane(Component component) {
        JScrollPane pane = new JideScrollPane(component);
        return pane;
    }

    private static JMenu createHelpMenu() {
        JMenu menu = new JideMenu("Help");
        menu.setMnemonic('H');

        JMenuItem item = new JMenuItem("About JIDE Products");
        item.setMnemonic('A');
        item.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                Lm.showAboutMessageBox();
            }
        });
        menu.add(item);

        return menu;
    }

    private static JMenu createFileMenu() {
        JMenuItem item;

        JMenu menu = new JideMenu("File");
        menu.setMnemonic('F');

        item = new JMenuItem("Exit");
        item.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                clearUp();
            }
        });
        menu.add(item);
        return menu;
    }

    public static JComponent createTextArea(String fileName) {
        JTextArea area = new JTextArea();
        Document doc = new PlainDocument();
        try {
            InputStream in = TwoFramesDockingFrameworkDemo.class.getResourceAsStream(fileName);
            if (in == null) {
                in = new FileInputStream(fileName);
            }
            byte[] buff = new byte[4096];
            int nch;
            while ((nch = in.read(buff, 0, buff.length)) != -1) {
                doc.insertString(doc.getLength(), new String(buff, 0, nch), null);
            }
            area.setDocument(doc);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (BadLocationException e) {
            e.printStackTrace();
        }

        return createScrollPane(area);
    }

    public static Component createHtmlArea(String fileName) {
        JEditorPane area = new JEditorPane();
        try {
            URL url = TwoFramesDockingFrameworkDemo.class.getResource(fileName);
            if (url != null) {
                area.setPage(url);
            }
            else {
                area.setPage("file://" + fileName);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return createScrollPane(area);
    }
}
