/*
 * @(#)TwoDockingFrameworkDemo.java
 *
 * Copyright 2002 - 2003 JIDE Software. All rights reserved.
 */

import com.jidesoft.action.CommandBarFactory;
import com.jidesoft.docking.*;
import com.jidesoft.icons.JideIconsFactory;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.JideMenu;
import com.jidesoft.swing.JideScrollPane;
import com.jidesoft.swing.JideTabbedPane;
import com.jidesoft.utils.Lm;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.*;
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
public class TwoDockingFrameworkDemo extends JFrame implements DockableHolder {

    private static TwoDockingFrameworkDemo _frame;

    private static final String PROFILE_NAME = "jidesoft";

    private static boolean _firstAutohideAll = false;
    private static boolean _secondAutohideAll = false;
    private static WindowAdapter _windowListener;

    static JideTabbedPane _tabbedPane;

    static DockableHolderPanel _firstPanel;
    static DockableHolderPanel _secondPanel;

    public TwoDockingFrameworkDemo(String title) throws HeadlessException {
        super(title);
    }

    public DockingManager getDockingManager() {
        if (_tabbedPane == null) {
            return null;
        }
        int selected = _tabbedPane.getSelectedIndex();
        if (selected == 0) {
            return _firstPanel.getDockingManager();
        }
        else {
            return _secondPanel.getDockingManager();
        }
    }

    static String getPrefix() {
        if (_tabbedPane.getSelectedIndex() == 0) {
            return "app1_";
        }
        else if (_tabbedPane.getSelectedIndex() == 1) {
            return "app2_";
        }
        return "";
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showDemo(true);
    }

    public static TwoDockingFrameworkDemo showDemo(final boolean exit) {
        if (_frame != null) {
            _frame.toFront();
            return _frame;
        }
        _frame = new TwoDockingFrameworkDemo("Demo of JIDE Docking Framework - two DockingManagers under the same JFrame");
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
        _frame.setJMenuBar(createMenuBar(0));

        _firstPanel = new DockableHolderPanel(_frame);
        _firstPanel.getDockingManager().setProfileKey(PROFILE_NAME + "-1");
        _secondPanel = new DockableHolderPanel(_frame);
        _secondPanel.getDockingManager().setProfileKey(PROFILE_NAME + "-2");

        _tabbedPane = new JideTabbedPane();
        _frame.getContentPane().setLayout(new BorderLayout());
        _frame.getContentPane().add(_tabbedPane, BorderLayout.CENTER);

        _tabbedPane.addTab("DockingManager 1", _firstPanel);
        _tabbedPane.addTab("DockingManager 2", _secondPanel);
        _tabbedPane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                switchDockingManager(_tabbedPane.getSelectedIndex());
                switchViewMenu(_tabbedPane.getSelectedIndex());
            }
        });

        _firstPanel.getDockingManager().setInitSplitPriority(DefaultDockingManager.SPLIT_SOUTH_NORTH_EAST_WEST);

        // add all dockable frames
        _firstPanel.getDockingManager().addFrame(createSampleResourceViewFrame());
        _firstPanel.getDockingManager().addFrame(createSampleClassViewFrame());
        _firstPanel.getDockingManager().addFrame(createSampleProjectViewFrame());
        _firstPanel.getDockingManager().addFrame(createSampleServerFrame());
        _firstPanel.getDockingManager().addFrame(createSamplePropertyFrame());

        _secondPanel.getDockingManager().beginLoadLayoutData();

        _secondPanel.getDockingManager().setInitSplitPriority(DefaultDockingManager.SPLIT_SOUTH_NORTH_EAST_WEST);

        _secondPanel.getDockingManager().addFrame(createSampleFindResult1Frame());
        _secondPanel.getDockingManager().addFrame(createSampleFindResult2Frame());
        _secondPanel.getDockingManager().addFrame(createSampleFindResult3Frame());

        _secondPanel.getDockingManager().addFrame(createSampleTaskListFrame());
        _secondPanel.getDockingManager().addFrame(createSampleCommandFrame());
        _secondPanel.getDockingManager().addFrame(createSampleOutputFrame());

        _secondPanel.getDockingManager().addFrame(createSampleVariablesFrame());
        _secondPanel.getDockingManager().addFrame(createSampleStackTraceFrame());
        _secondPanel.getDockingManager().addFrame(createSampleThreadsFrame());
        _secondPanel.getDockingManager().addFrame(createSampleConsoleFrame());
        _secondPanel.getDockingManager().addFrame(createSampleBreakpointsFrame());
        _secondPanel.getDockingManager().addFrame(createSampleWatchesFrame());

        _firstPanel.getDockingManager().loadLayoutDataFrom("first_default");

        _secondPanel.getDockingManager().loadLayoutDataFrom("second_default");

        switchDockingManager(_tabbedPane.getSelectedIndex());

        _frame.toFront();
        return _frame;
    }

    private static void switchViewMenu(int index) {
        JMenuBar menu = _frame.getJMenuBar();
        if (menu != null) {
            menu.removeAll();
        }
        _frame.setJMenuBar(createMenuBar(index));
        _frame.repaint();
    }

    private static void switchDockingManager(int index) {
        if (index == 0) {
            _firstPanel.getDockingManager().setFloatingFramesVisible(true);
        }
        else {
            _firstPanel.getDockingManager().stopShowingAutohideFrameImmediately();
            _firstPanel.getDockingManager().setFloatingFramesVisible(false);
        }

        if (index == 1) {
            _secondPanel.getDockingManager().setFloatingFramesVisible(true);
        }
        else {
            _secondPanel.getDockingManager().stopShowingAutohideFrameImmediately();
            _secondPanel.getDockingManager().setFloatingFramesVisible(false);
        }
    }

    private static void clearUp() {
        _frame.removeWindowListener(_windowListener);
        _windowListener = null;

        if (_firstPanel.getDockingManager() != null) {
            _firstPanel.getDockingManager().saveLayoutDataAs("first_default");
        }
        if (_secondPanel.getDockingManager() != null) {
            _secondPanel.getDockingManager().saveLayoutDataAs("second_default");
        }

        _frame.dispose();
        _frame = null;
    }

    protected static DockableFrame createSampleProjectViewFrame() {
        DockableFrame frame = new DockableFrame("Project View", JideIconsFactory.getImageIcon(JideIconsFactory.DockableFrame.FRAME1));
        frame.getContext().setInitMode(DockContext.STATE_AUTOHIDE);
        frame.getContext().setInitSide(DockContext.DOCK_SIDE_EAST);
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

    protected static DockableFrame createSampleServerFrame() {
        DockableFrame frame = new DockableFrame("Server Explorer", JideIconsFactory.getImageIcon(JideIconsFactory.DockableFrame.FRAME3));
        frame.getContext().setInitMode(DockContext.STATE_AUTOHIDE);
        frame.getContext().setInitSide(DockContext.DOCK_SIDE_WEST);
        frame.getContext().setInitIndex(0);
        frame.getContentPane().add(createScrollPane(new JTextArea()));
        frame.setPreferredSize(new Dimension(200, 200));
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

    protected static DockableFrame createSamplePropertyFrame() {
        DockableFrame frame = new DockableFrame("Property", JideIconsFactory.getImageIcon(JideIconsFactory.DockableFrame.FRAME5));
        frame.getContext().setInitMode(DockContext.STATE_FRAMEDOCKED);
        frame.getContext().setInitSide(DockContext.DOCK_SIDE_WEST);
        frame.getContext().setInitIndex(0);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(createScrollPane(new JTextArea()));
        frame.setPreferredSize(new Dimension(200, 200));
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
        frame.getContext().setInitIndex(1);
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
        frame.getContext().setInitIndex(1);
        frame.getContentPane().add(createScrollPane(new JTextArea()));
        frame.setPreferredSize(new Dimension(200, 200));
        return frame;
    }

    protected static DockableFrame createSampleWatchesFrame() {
        DockableFrame frame = new DockableFrame("Watch Window", JideIconsFactory.getImageIcon(JideIconsFactory.DockableFrame.FRAME12));
        frame.getContext().setInitMode(DockContext.STATE_FRAMEDOCKED);
        frame.getContext().setInitSide(DockContext.DOCK_SIDE_EAST);
        frame.getContentPane().setLayout(new BorderLayout());
        JLabel label = new JLabel("Text Field");
        label.setDisplayedMnemonic('T');
        JTextField textField = new JTextField();
        label.setLabelFor(textField);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(label, BorderLayout.BEFORE_LINE_BEGINS);
        panel.add(textField, BorderLayout.CENTER);
        frame.getContentPane().add(panel, BorderLayout.BEFORE_FIRST_LINE);
        frame.getContentPane().add(createScrollPane(new JTextArea()));
        JCheckBox checkBox = new JCheckBox("CheckBox");
        checkBox.setMnemonic('C');
        frame.getContentPane().add(checkBox, BorderLayout.AFTER_LAST_LINE);
        frame.setPreferredSize(new Dimension(200, 200));
        return frame;
    }

    protected static DockableFrame createSampleVariablesFrame() {
        DockableFrame frame = new DockableFrame("Variables", JideIconsFactory.getImageIcon(JideIconsFactory.DockableFrame.FRAME13));
        frame.getContext().setInitMode(DockContext.STATE_FRAMEDOCKED);
        frame.getContext().setInitSide(DockContext.DOCK_SIDE_EAST);
        frame.getContentPane().add(createScrollPane(new JTextArea()));
        frame.setPreferredSize(new Dimension(200, 200));
        return frame;
    }

    protected static DockableFrame createSampleBreakpointsFrame() {
        DockableFrame frame = new DockableFrame("Breakpoints", JideIconsFactory.getImageIcon(JideIconsFactory.DockableFrame.FRAME14));
        frame.setAvailableButtons(DockableFrame.BUTTON_AUTOHIDE | DockableFrame.BUTTON_FLOATING);
        frame.setTitle("Breakpoints (Close Button Invisible)");
        frame.getContext().setInitMode(DockContext.STATE_FRAMEDOCKED);
        frame.getContext().setInitSide(DockContext.DOCK_SIDE_SOUTH);
        frame.getContext().setInitIndex(2);
        frame.getContentPane().add(createScrollPane(new JTextArea()));
        frame.setPreferredSize(new Dimension(200, 200));
        return frame;
    }

    protected static DockableFrame createSampleStackTraceFrame() {
        DockableFrame frame = new DockableFrame("Stack Trace", JideIconsFactory.getImageIcon(JideIconsFactory.DockableFrame.FRAME15));
        frame.getContext().setInitMode(DockContext.STATE_AUTOHIDE);
        frame.getContext().setInitSide(DockContext.DOCK_SIDE_EAST);
        frame.getContext().setInitIndex(1);
        JList list = new JList(new String[]{"DefaultDockingManager.java:100", "DockingFrameworkDemo.java:200"});
        list.setToolTipText("This is a tooltip");
        frame.getContentPane().add(createScrollPane(list));
        frame.setPreferredSize(new Dimension(200, 200));
        return frame;
    }

    protected static DockableFrame createSampleThreadsFrame() {
        DockableFrame frame = new DockableFrame("Threads", JideIconsFactory.getImageIcon(JideIconsFactory.DockableFrame.FRAME16));
        frame.getContext().setInitMode(DockContext.STATE_FRAMEDOCKED);
        frame.getContext().setInitSide(DockContext.DOCK_SIDE_EAST);
        frame.getContentPane().add(createScrollPane(new JTextArea()));
        frame.setPreferredSize(new Dimension(200, 200));
        return frame;
    }

    protected static DockableFrame createSampleConsoleFrame() {
        DockableFrame frame = new DockableFrame("Console", JideIconsFactory.getImageIcon(JideIconsFactory.DockableFrame.FRAME17));
        frame.getContext().setInitMode(DockContext.STATE_AUTOHIDE);
        frame.getContext().setInitSide(DockContext.DOCK_SIDE_EAST);
        frame.getContext().setInitIndex(1);
        frame.getContentPane().add(createScrollPane(new JTextArea()));
        frame.setPreferredSize(new Dimension(200, 200));
        return frame;
    }

    protected static JMenuBar createMenuBar(int index) {
        JMenuBar menu = new JMenuBar();

        JMenu fileMenu = createFileMenu();
        JMenu viewMenu;
        if (index == 0) {
            viewMenu = createDockingPane1ViewMenu();
        }
        else {
            viewMenu = createDockingPane2ViewMenu();
        }
        JMenu windowMenu = createWindowsMenu();
        JMenu lnfMenu = CommandBarFactory.createLookAndFeelMenu(_frame);
        JMenu helpMenu = createHelpMenu();

        menu.add(fileMenu);
        menu.add(viewMenu);
        menu.add(windowMenu);
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
            public void actionPerformed(ActionEvent e) {
                Lm.showAboutMessageBox();
            }
        });
        menu.add(item);

        return menu;
    }

    private static JMenu createWindowsMenu() {
        JMenu menu = new JideMenu("Window");
        menu.setMnemonic('W');

        JMenuItem item = new JMenuItem("Load Default Layout");
        item.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                _frame.getDockingManager().loadLayoutDataFrom(getPrefix() + "default");
            }
        });
        menu.add(item);

        item = new JMenuItem("Load Design Layout");
        item.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
//use these two lines if you don't want the frame size and state changes
//                _frame.getDockingManager().setUseFrameBounds(false);
//                _frame.getDockingManager().setUseFrameState(false);
                _frame.getDockingManager().loadLayoutDataFrom(getPrefix() + "design");
            }
        });
        menu.add(item);

        item = new JMenuItem("Load Debug Layout");
        item.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                _frame.getDockingManager().loadLayoutDataFrom(getPrefix() + "debug");
            }
        });
        menu.add(item);

        menu.addSeparator();

        item = new JMenuItem("Save as Default Layout");
        item.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                _frame.getDockingManager().saveLayoutDataAs(getPrefix() + "default");
            }
        });
        menu.add(item);

        item = new JMenuItem("Save as Design Layout");
        item.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                _frame.getDockingManager().saveLayoutDataAs(getPrefix() + "design");
            }
        });
        menu.add(item);

        item = new JMenuItem("Save as Debug Layout");
        item.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                _frame.getDockingManager().saveLayoutDataAs(getPrefix() + "debug");
            }
        });
        menu.add(item);

        menu.addSeparator();

        item = new JMenuItem("Reset Layout");
        item.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                _frame.getDockingManager().resetToDefault();
            }
        });
        menu.add(item);

        menu.addSeparator();

        item = new JMenuItem("Toggle Auto Hide All");
        item.setMnemonic('T');
        item.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                String profileName = getPrefix() + "full";
                boolean autohideAll;
                if (_tabbedPane.getSelectedIndex() == 0) {
                    autohideAll = _firstAutohideAll;
                }
                else {
                    autohideAll = _secondAutohideAll;
                }

                if (!autohideAll) {
                    _frame.getDockingManager().saveLayoutDataAs(profileName);
                    _frame.getDockingManager().autohideAll();
                    autohideAll = true;
                }
                else {
                    _frame.getDockingManager().loadLayoutDataFrom(profileName);
                    autohideAll = false;
                }

                if (_tabbedPane.getSelectedIndex() == 0) {
                    _firstAutohideAll = autohideAll;
                }
                else {
                    _secondAutohideAll = autohideAll;
                }
            }
        });
        menu.add(item);

        return menu;
    }

    private static JMenu createDockingPane1ViewMenu() {
        JMenuItem item;
        JMenu menu = new JideMenu("View");
        menu.setMnemonic('V');

        item = new JMenuItem("Project View", JideIconsFactory.getImageIcon(JideIconsFactory.DockableFrame.FRAME1));
        item.setMnemonic('P');
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_MASK | InputEvent.ALT_MASK));
        item.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                _frame.getDockingManager().showFrame("Project View");
            }
        });
        menu.add(item);

        item = new JMenuItem("Class View", JideIconsFactory.getImageIcon(JideIconsFactory.DockableFrame.FRAME2));
        item.setMnemonic('A');
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
        item.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                _frame.getDockingManager().showFrame("Class View");
            }
        });
        menu.add(item);

        item = new JMenuItem("Server Explorer", JideIconsFactory.getImageIcon(JideIconsFactory.DockableFrame.FRAME3));
        item.setMnemonic('V');
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK | InputEvent.ALT_MASK));
        item.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                _frame.getDockingManager().showFrame("Server Explorer");
            }
        });
        menu.add(item);

        item = new JMenuItem("Resource View", JideIconsFactory.getImageIcon(JideIconsFactory.DockableFrame.FRAME4));
        item.setMnemonic('R');
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
        item.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                _frame.getDockingManager().showFrame("Resource View");
            }
        });
        menu.add(item);

        item = new JMenuItem("Properties Window", JideIconsFactory.getImageIcon(JideIconsFactory.DockableFrame.FRAME5));
        item.setMnemonic('W');
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0));
        item.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                _frame.getDockingManager().showFrame("Property");
            }
        });
        menu.add(item);

        return menu;
    }

    private static JMenu createDockingPane2ViewMenu() {
        JMenuItem item;
        JMenu menu = new JideMenu("View");
        menu.setMnemonic('V');

        item = new JMenuItem("Task List", JideIconsFactory.getImageIcon(JideIconsFactory.DockableFrame.FRAME6));
        item.setMnemonic('T');
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K, InputEvent.CTRL_MASK | InputEvent.ALT_MASK));
        item.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                _frame.getDockingManager().showFrame("Task List");
            }
        });
        menu.add(item);

        item = new JMenuItem("Command Window", JideIconsFactory.getImageIcon(JideIconsFactory.DockableFrame.FRAME7));
        item.setMnemonic('N');
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK | InputEvent.ALT_MASK));
        item.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                _frame.getDockingManager().showFrame("Command");
            }
        });
        menu.add(item);

        item = new JMenuItem("Output", JideIconsFactory.getImageIcon(JideIconsFactory.DockableFrame.FRAME8));
        item.setMnemonic('U');
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK | InputEvent.ALT_MASK));
        item.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                _frame.getDockingManager().showFrame("Output");
            }
        });
        menu.add(item);

        menu.addSeparator();

        item = new JMenuItem("Find Results 1", JideIconsFactory.getImageIcon(JideIconsFactory.DockableFrame.FRAME9));
        item.setMnemonic('1');
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F10, InputEvent.CTRL_MASK | InputEvent.ALT_MASK));
        item.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                _frame.getDockingManager().showFrame("Find Results 1");
            }
        });
        menu.add(item);

        item = new JMenuItem("Find Results 2", JideIconsFactory.getImageIcon(JideIconsFactory.DockableFrame.FRAME10));
        item.setMnemonic('3');
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F11, InputEvent.CTRL_MASK | InputEvent.ALT_MASK));
        item.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                _frame.getDockingManager().showFrame("Find Results 2");
            }
        });
        menu.add(item);

        item = new JMenuItem("Find Results 3", JideIconsFactory.getImageIcon(JideIconsFactory.DockableFrame.FRAME11));
        item.setMnemonic('3');
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F12, InputEvent.CTRL_MASK | InputEvent.ALT_MASK));
        item.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                _frame.getDockingManager().showFrame("Find Results 3");
            }
        });
        menu.add(item);

        menu.addSeparator();

        item = new JMenuItem("Watch Window", JideIconsFactory.getImageIcon(JideIconsFactory.DockableFrame.FRAME12));
        item.setMnemonic('W');
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, InputEvent.CTRL_MASK));
        item.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                _frame.getDockingManager().showFrame("Watch Window");
            }
        });
        menu.add(item);

        item = new JMenuItem("Variables", JideIconsFactory.getImageIcon(JideIconsFactory.DockableFrame.FRAME13));
        item.setMnemonic('V');
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, InputEvent.CTRL_MASK | InputEvent.ALT_MASK));
        item.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                _frame.getDockingManager().showFrame("Variables");
            }
        });
        menu.add(item);

        item = new JMenuItem("Breakpoints", JideIconsFactory.getImageIcon(JideIconsFactory.DockableFrame.FRAME14));
        item.setMnemonic('B');
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, InputEvent.CTRL_MASK | InputEvent.ALT_MASK));
        item.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                _frame.getDockingManager().showFrame("Breakpoints");
            }
        });
        menu.add(item);

        item = new JMenuItem("Stack Trace", JideIconsFactory.getImageIcon(JideIconsFactory.DockableFrame.FRAME15));
        item.setMnemonic('S');
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3, InputEvent.CTRL_MASK | InputEvent.ALT_MASK));
        item.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                _frame.getDockingManager().showFrame("Stack Trace");
            }
        });
        menu.add(item);

        item = new JMenuItem("Threads", JideIconsFactory.getImageIcon(JideIconsFactory.DockableFrame.FRAME16));
        item.setMnemonic('T');
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3, InputEvent.SHIFT_MASK | InputEvent.ALT_MASK));
        item.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                _frame.getDockingManager().showFrame("Threads");
            }
        });
        menu.add(item);

        item = new JMenuItem("Console", JideIconsFactory.getImageIcon(JideIconsFactory.DockableFrame.FRAME17));
        item.setMnemonic('C');
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3, InputEvent.SHIFT_MASK | InputEvent.ALT_MASK));
        item.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                _frame.getDockingManager().showFrame("Console");
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
            InputStream in = TwoDockingFrameworkDemo.class.getResourceAsStream(fileName);
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
            URL url = TwoDockingFrameworkDemo.class.getResource(fileName);
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
