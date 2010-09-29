/*
 * @(#)NestedDockingFrameworkDemo.java 7/16/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.action.CommandBarFactory;
import com.jidesoft.docking.*;
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
 * This is a sample program for JIDE Docking Framework showing how to use two DockingManager in the same JFrame. It will
 * create a JFrame with about a tabbed pane with two tabs. Each tab has its own DockingManager. <br> Required jar files:
 * jide-common.jar, jide-dock.jar <br> Required L&F: Jide L&F extension required
 */
public class NestedDockingFrameworkDemo extends JFrame implements DockableHolder {

    private static NestedDockingFrameworkDemo _frame;

    private static final String PROFILE_NAME = "jidesoft-nested";

    private static WindowAdapter _windowListener;

    static DockableHolderPanel _firstPanel;

    public NestedDockingFrameworkDemo(String title) throws HeadlessException {
        super(title);
    }

    public DockingManager getDockingManager() {
        return _firstPanel.getDockingManager();
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showDemo(true);
    }

    public static DockableHolder showDemo(final boolean exit) {
        if (_frame != null) {
            _frame.toFront();
            return _frame;
        }
        _frame = new NestedDockingFrameworkDemo("Demo of JIDE Docking Framework - nested DockingManager within another DockableFrame");
        _frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        _frame.setIconImage(JideIconsFactory.getImageIcon(JideIconsFactory.JIDE32).getImage());

        // add a window listener so that timer can be stopped when exit
        _windowListener = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                clearUp();
                if (exit) {
                    System.exit(0);
                }
            }
        };
        _frame.addWindowListener(_windowListener);

        // add menu bar
        _frame.setJMenuBar(createMenuBar());

        _firstPanel = new DockableHolderPanel(_frame);
        _firstPanel.getDockingManager().setProfileKey(PROFILE_NAME + "-1");

        _frame.getContentPane().setLayout(new BorderLayout());
        _frame.getContentPane().add(_firstPanel, BorderLayout.CENTER);

        _firstPanel.getDockingManager().setInitSplitPriority(DefaultDockingManager.SPLIT_SOUTH_NORTH_EAST_WEST);

        // add all dockable frames
        _firstPanel.getDockingManager().addFrame(createNestedDockingFrameworkFrame());
        _firstPanel.getDockingManager().addFrame(createSampleClassViewFrame());
        _firstPanel.getDockingManager().addFrame(createSampleServerFrame());

        _firstPanel.getDockingManager().loadLayoutDataFrom("first_default");

        _frame.toFront();
        return _frame;
    }

    private static void clearUp() {
        _frame.removeWindowListener(_windowListener);
        _windowListener = null;

        if (_firstPanel.getDockingManager() != null) {
            _firstPanel.getDockingManager().saveLayoutDataAs("first_default");
        }

        _frame.dispose();
        _frame = null;
    }

    protected static DockableFrame createSampleProjectViewFrame() {
        DockableFrame frame = new DockableFrame("Project View", JideIconsFactory.getImageIcon(JideIconsFactory.DockableFrame.FRAME1));
        frame.getContext().setInitMode(DockContext.STATE_FRAMEDOCKED);
        frame.getContext().setInitSide(DockContext.DOCK_SIDE_EAST);
        frame.getContentPane().add(createScrollPane(new JTextArea()));
        frame.setPreferredSize(new Dimension(200, 200));
        return frame;
    }

    protected static DockableFrame createSampleClassViewFrame() {
        DockableFrame frame = new DockableFrame("Class View", JideIconsFactory.getImageIcon(JideIconsFactory.DockableFrame.FRAME2));
        frame.getContext().setInitMode(DockContext.STATE_FRAMEDOCKED);
        frame.getContext().setInitSide(DockContext.DOCK_SIDE_EAST);
        frame.getContext().setInitIndex(0);
        frame.getContentPane().add(createScrollPane(new JTextArea()));
        frame.setPreferredSize(new Dimension(400, 200));
        frame.setTitle("Class View - DockingFrameworkDemo");
        frame.setTabTitle("Class View");
        return frame;
    }

    protected static DockableFrame createSampleServerFrame() {
        DockableFrame frame = new DockableFrame("Server Explorer", JideIconsFactory.getImageIcon(JideIconsFactory.DockableFrame.FRAME3));
        frame.getContext().setInitMode(DockContext.STATE_FRAMEDOCKED);
        frame.getContext().setInitSide(DockContext.DOCK_SIDE_EAST);
        frame.getContext().setInitIndex(0);
        frame.getContentPane().add(createScrollPane(new JTextArea()));
        frame.setPreferredSize(new Dimension(200, 200));
        return frame;
    }

    protected static DockableFrame createNestedDockingFrameworkFrame() {
        DockableFrame frame = new DockableFrame("Nested Docking Framework", JideIconsFactory.getImageIcon(JideIconsFactory.DockableFrame.FRAME4));
        frame.getContext().setInitMode(DockContext.STATE_FRAMEDOCKED);
        frame.getContext().setInitSide(DockContext.DOCK_SIDE_EAST);
        frame.getContext().setInitIndex(0);
        DockableHolderPanel nestedDockableHolder = new DockableHolderPanel(_frame);
        nestedDockableHolder.getDockingManager().setProfileKey("nested-docking");
        nestedDockableHolder.getDockingManager().addFrame(createSampleFindResult1Frame());
        nestedDockableHolder.getDockingManager().addFrame(createSampleFindResult2Frame());
        nestedDockableHolder.getDockingManager().addFrame(createSampleFindResult3Frame());
        frame.getContentPane().add(nestedDockableHolder);
        nestedDockableHolder.getDockingManager().loadLayoutData();
        frame.setPreferredSize(new Dimension(200, 200));
        return frame;
    }

    protected static DockableFrame createSampleFindResult1Frame() {
        DockableFrame frame = new DockableFrame("Find Results 1", JideIconsFactory.getImageIcon(JideIconsFactory.DockableFrame.FRAME9));
        frame.getContext().setInitMode(DockContext.STATE_FRAMEDOCKED);
        frame.getContext().setInitSide(DockContext.DOCK_SIDE_SOUTH);
        frame.getContext().setInitIndex(0);
        frame.getContext().setCurrentDockSide(DockContext.DOCK_SIDE_SOUTH);
        JTextArea textArea = new JTextArea();
        frame.getContentPane().add(createScrollPane(textArea));
        textArea.setText("Find all \"TestDock\", Match case, Whole word, Find Results 1, All Open Documents\n" +
                "C:\\Projects\\src\\com\\jidesoft\\test\\TestDock.java(1):// TestDock.java : implementation of the TestDock class\n" +
                "C:\\Projects\\src\\jidesoft\\test\\TestDock.java(8):#import com.jidesoft.test.TestDock;\n" +
                "C:\\Projects\\src\\com\\jidesoft\\Test.java(10):#import com.jidesoft.test.TestDock;\n" +
                "Total found: 3    Matching files: 5    Total files searched: 5");
        frame.setPreferredSize(new Dimension(200, 200));
        return frame;
    }

    protected static DockableFrame createSampleFindResult2Frame() {
        DockableFrame frame = new DockableFrame("Find Results 2", JideIconsFactory.getImageIcon(JideIconsFactory.DockableFrame.FRAME10));
        frame.getContext().setInitMode(DockContext.STATE_FRAMEDOCKED);
        frame.getContext().setInitSide(DockContext.DOCK_SIDE_SOUTH);
        frame.getContext().setInitIndex(0);
        JTextArea textArea = new JTextArea();
        frame.getContentPane().add(createScrollPane(textArea));
        textArea.setText("Find all \"TestDock\", Match case, Whole word, Find Results 2, All Open Documents\n" +
                "C:\\Projects\\src\\com\\jidesoft\\test\\TestDock.java(1):// TestDock.java : implementation of the TestDock class\n" +
                "C:\\Projects\\src\\jidesoft\\test\\TestDock.java(8):#import com.jidesoft.test.TestDock;\n" +
                "C:\\Projects\\src\\com\\jidesoft\\Test.java(10):#import com.jidesoft.test.TestDock;\n" +
                "Total found: 3    Matching files: 5    Total files searched: 5");
        frame.setPreferredSize(new Dimension(200, 200));
        return frame;
    }

    protected static DockableFrame createSampleFindResult3Frame() {
        DockableFrame frame = new DockableFrame("Find Results 3", JideIconsFactory.getImageIcon(JideIconsFactory.DockableFrame.FRAME11));
        frame.getContext().setInitMode(DockContext.STATE_FRAMEDOCKED);
        frame.getContext().setInitSide(DockContext.DOCK_SIDE_SOUTH);
        frame.getContext().setInitIndex(0);
        frame.getContentPane().add(createScrollPane(new JTextArea()));
        frame.setPreferredSize(new Dimension(200, 200));
        return frame;
    }

    protected static JMenuBar createMenuBar() {
        JMenuBar menu = new JMenuBar();

        JMenu fileMenu = createFileMenu();
        //JMenu viewMenu = createViewMenu();
        JMenu lnfMenu = CommandBarFactory.createLookAndFeelMenu(_frame);
        JMenu helpMenu = createHelpMenu();

        menu.add(fileMenu);
        //menu.add(viewMenu);
        menu.add(lnfMenu);
        menu.add(helpMenu);


        return menu;
    }

    private static JScrollPane createScrollPane(Component component) {
        return new JideScrollPane(component);
    }

    private static JMenu createHelpMenu() {
        JMenu menu = new JideMenu("Help");
        menu.setMnemonic('H');

        JMenuItem item = new JMenuItem("About JIDE Products");
        item.setMnemonic('A');
        item.addActionListener(new AbstractAction() {
            private static final long serialVersionUID = -1336883963647526111L;

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
            private static final long serialVersionUID = 8112536352875271644L;

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
            InputStream in = NestedDockingFrameworkDemo.class.getResourceAsStream(fileName);
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
            URL url = NestedDockingFrameworkDemo.class.getResource(fileName);
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
