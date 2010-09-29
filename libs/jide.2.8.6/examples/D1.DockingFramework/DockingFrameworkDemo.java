/*
 * @(#)DockingFrameworkDemo.java
 *
 * Copyright 2002 - 2003 JIDE Software. All rights reserved.
 */

import com.jidesoft.action.CommandBarFactory;
import com.jidesoft.docking.*;
import com.jidesoft.icons.JideIconsFactory;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.JideMenu;
import com.jidesoft.swing.JideScrollPane;
import com.jidesoft.swing.JideSwingUtilities;
import com.jidesoft.utils.Lm;

import javax.swing.*;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import java.awt.*;
import java.awt.event.*;
import java.util.Collection;

/**
 * This is a sample program for JIDE Docking Framework. It will create a JFrame with about 20 dockable frames to show
 * the features of JIDE Docking Framework. <br> Required jar files: jide-common.jar, jide-dock.jar <br> Required L&F:
 * Jide L&F extension required
 */
public class DockingFrameworkDemo extends DefaultDockableHolder {

    private static DockingFrameworkDemo _frame;

    private static final String PROFILE_NAME = "jidesoft";

    private static boolean _autohideAll = false;
    private static WindowAdapter _windowListener;
    public static JMenuItem _redoMenuItem;
    public static JMenuItem _undoMenuItem;

    public DockingFrameworkDemo(String title) throws HeadlessException {
        super(title);
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showDemo(true);
    }

    public static DefaultDockableHolder showDemo(final boolean exit) {
        _frame = new DockingFrameworkDemo("Demo of JIDE Docking Framework");
        _frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        _frame.setIconImage(JideIconsFactory.getImageIcon(JideIconsFactory.JIDE32).getImage());

        // add a window listener to do clear up when windows closing.
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

        // Set the profile key
        _frame.getDockingManager().setProfileKey(PROFILE_NAME);

// comment this if you don't want to use javax pref
//        _frame.getDockingManager().setUsePref(false);

        // Uses light-weight outline. There are several options here.
        _frame.getDockingManager().setOutlineMode(DockingManager.FULL_OUTLINE_MODE);

// uncomment following lines to adjust the sliding speed of autohide frame
//        _frame.getDockingManager().setInitDelay(100);
//        _frame.getDockingManager().setSteps(1);
//        _frame.getDockingManager().setStepDelay(0);

// uncomment following lines if you want to customize the tabbed pane used in Docking Framework
//        _frame.getDockingManager().setTabbedPaneCustomizer(new DockingManager.TabbedPaneCustomizer(){
//            public void customize(JideTabbedPane tabbedPane) {
//                tabbedPane.setShowCloseButton(true);
//                tabbedPane.setShowCloseButtonOnTab(true);
//            }
//        });

// uncomment following lines if you want to customize the popup menu of DockableFrame
//       _frame.getDockingManager().setPopupMenuCustomizer(new com.jidesoft.docking.PopupMenuCustomizer() {
//           public void customizePopupMenu(JPopupMenu menu, DockingManager dockingManager, DockableFrame dockableFrame, boolean onTab) {
//              // ... do customization here
//           }
//
//           public boolean isPopupMenuShown(DockingManager dockingManager, DockableFrame dockableFrame, boolean onTab) {
//               return false;
//           }
//       });

        // add menu bar
        _frame.setJMenuBar(createMenuBar());

        // Sets the number of steps you allow user to undo.
        _frame.getDockingManager().setUndoLimit(10);
        _frame.getDockingManager().addUndoableEditListener(new UndoableEditListener() {
            public void undoableEditHappened(UndoableEditEvent e) {
                refreshUndoRedoMenuItems();
            }
        });

        // Now let's start to addFrame()
        _frame.getDockingManager().beginLoadLayoutData();

        _frame.getDockingManager().setInitSplitPriority(DockingManager.SPLIT_EAST_WEST_SOUTH_NORTH);

        // add all dockable frames
        _frame.getDockingManager().addFrame(createSampleTaskListFrame());
        _frame.getDockingManager().addFrame(createSampleResourceViewFrame());
        _frame.getDockingManager().addFrame(createSampleClassViewFrame());
        _frame.getDockingManager().addFrame(createSampleProjectViewFrame());
        _frame.getDockingManager().addFrame(createSampleServerFrame());
        _frame.getDockingManager().addFrame(createSamplePropertyFrame());
        _frame.getDockingManager().addFrame(createSampleFindResult1Frame());
        _frame.getDockingManager().addFrame(createSampleFindResult2Frame());
        _frame.getDockingManager().addFrame(createSampleFindResult3Frame());
        _frame.getDockingManager().addFrame(createSampleOutputFrame());
        _frame.getDockingManager().addFrame(createSampleCommandFrame());

// just use default size. If you want to overwrite, you can call this method
//        _frame.getDockingManager().setInitBounds(new Rectangle(0, 0, 960, 800));

// disallow drop dockable frame to workspace area
        _frame.getDockingManager().getWorkspace().setAcceptDockableFrame(false);

        // load layout information from previous session. This indicates the end of beginLoadLayoutData() method above.
        _frame.getDockingManager().loadLayoutData();

// uncomment following line(s) if you want to limit certain feature.
// If you uncomment all four lines, then the dockable frame will not
// be moved anywhere.
//        _frame.getDockingManager().setRearrangable(false);
//        _frame.getDockingManager().setAutohidable(false);
//        _frame.getDockingManager().setFloatable(false);
//        _frame.getDockingManager().setHidable(false);

        _frame.toFront();
        return _frame;
    }

    private static void clearUp() {
        _frame.removeWindowListener(_windowListener);
        _windowListener = null;

        if (_frame.getDockingManager() != null) {
            _frame.getDockingManager().saveLayoutData();
        }

        _frame.dispose();
        _frame = null;
    }

    protected static DockableFrame createDockableFrame(String key, Icon icon) {
        DockableFrame frame = new DockableFrame(key, icon);
        frame.setPreferredSize(new Dimension(200, 200));
        return frame;
    }

    protected static DockableFrame createSampleProjectViewFrame() {
        DockableFrame frame = createDockableFrame("Project View", JideIconsFactory.getImageIcon(JideIconsFactory.DockableFrame.FRAME1));
        frame.getContext().setInitMode(DockContext.STATE_AUTOHIDE);
        frame.getContext().setInitSide(DockContext.DOCK_SIDE_EAST);
        frame.add(createScrollPane(new JTextArea()));
        return frame;
    }

    protected static DockableFrame createSampleClassViewFrame() {
        DockableFrame frame = createDockableFrame("Class View", JideIconsFactory.getImageIcon(JideIconsFactory.DockableFrame.FRAME2));
        frame.getContext().setInitMode(DockContext.STATE_FRAMEDOCKED);
        frame.getContext().setInitSide(DockContext.DOCK_SIDE_EAST);
        frame.getContext().setInitIndex(1);
        frame.add(createScrollPane(new JTextArea()));
        frame.setTitle("Class View - DockingFrameworkDemo");
        frame.setTabTitle("Class View");
        return frame;
    }

    protected static DockableFrame createSampleServerFrame() {
        DockableFrame frame = createDockableFrame("Server Explorer", JideIconsFactory.getImageIcon(JideIconsFactory.DockableFrame.FRAME3));
        frame.getContext().setInitMode(DockContext.STATE_AUTOHIDE);
        frame.getContext().setInitSide(DockContext.DOCK_SIDE_WEST);
        frame.getContext().setInitIndex(0);
        frame.add(createScrollPane(new JTextArea()));
        frame.setToolTipText("Server");
        return frame;
    }

    protected static DockableFrame createSampleResourceViewFrame() {
        DockableFrame frame = createDockableFrame("Resource View", JideIconsFactory.getImageIcon(JideIconsFactory.DockableFrame.FRAME4));
        frame.getContext().setInitMode(DockContext.STATE_FRAMEDOCKED);
        frame.getContext().setInitSide(DockContext.DOCK_SIDE_EAST);
        frame.getContext().setInitIndex(1);
        frame.add(createScrollPane(new JTextArea()));
        frame.setTitle("Resource View");
        return frame;
    }

    protected static DockableFrame createSamplePropertyFrame() {
        DockableFrame frame = createDockableFrame("Property", JideIconsFactory.getImageIcon(JideIconsFactory.DockableFrame.FRAME5));
        frame.getContext().setInitMode(DockContext.STATE_FRAMEDOCKED);
        frame.getContext().setInitSide(DockContext.DOCK_SIDE_WEST);
        frame.getContext().setInitIndex(0);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.add(createScrollPane(new JTextArea()));
        return frame;
    }

    static int i = 0;

    protected static DockableFrame createSampleTaskListFrame() {
        DockableFrame frame = createDockableFrame("Task List", JideIconsFactory.getImageIcon(JideIconsFactory.DockableFrame.FRAME6));
        frame.getContext().setInitMode(-DockContext.STATE_FLOATING);
        frame.getContext().setInitIndex(3);
        frame.getContext().setInitSide(DockContext.DOCK_SIDE_SOUTH);
        JList list = new JList(new String[]{"Task1", "Task2", "Task3"});
        list.setToolTipText("This is a tooltip");
        frame.add(createScrollPane(list));
//        frame.addAdditionalButtonActions(new AbstractAction("test") {
//            public void actionPerformed(ActionEvent e) {
//                System.out.println("Test additional buttons.");
//            }
//        }, JideIconsFactory.getImageIcon(JideIconsFactory.DockableFrame.FRAME6));
        return frame;
    }

    protected static DockableFrame createSampleCommandFrame() {
        DockableFrame frame = createDockableFrame("Command", JideIconsFactory.getImageIcon(JideIconsFactory.DockableFrame.FRAME7));
        frame.getContext().setInitMode(DockContext.STATE_FRAMEDOCKED);
        frame.getContext().setInitSide(DockContext.DOCK_SIDE_SOUTH);
        frame.getContext().setInitIndex(1);
        JTextArea textArea = new JTextArea();
        frame.add(createScrollPane(textArea));
        textArea.setText(">");
        return frame;
    }

    protected static DockableFrame createSampleOutputFrame() {
        DockableFrame frame = createDockableFrame("Output", JideIconsFactory.getImageIcon(JideIconsFactory.DockableFrame.FRAME8));
        frame.getContext().setInitMode(DockContext.STATE_FRAMEDOCKED);
        frame.getContext().setInitSide(DockContext.DOCK_SIDE_SOUTH);
        frame.getContext().setInitIndex(0);
        frame.setLayout(new BorderLayout(2, 2));
        frame.add(createScrollPane(new JTextArea()));
        frame.add(new JTextField(), BorderLayout.AFTER_LAST_LINE);
        return frame;
    }

    protected static DockableFrame createSampleFindResult1Frame() {
        DockableFrame frame = createDockableFrame("Find Results 1", JideIconsFactory.getImageIcon(JideIconsFactory.DockableFrame.FRAME9));
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
        return frame;
    }

    protected static DockableFrame createSampleFindResult2Frame() {
        DockableFrame frame = createDockableFrame("Find Results 2", JideIconsFactory.getImageIcon(JideIconsFactory.DockableFrame.FRAME10));
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
        return frame;
    }

    protected static DockableFrame createSampleFindResult3Frame() {
        DockableFrame frame = createDockableFrame("Find Results 3", JideIconsFactory.getImageIcon(JideIconsFactory.DockableFrame.FRAME11));
        frame.getContext().setInitMode(DockContext.STATE_FRAMEDOCKED);
        frame.getContext().setInitSide(DockContext.DOCK_SIDE_SOUTH);
        frame.getContext().setInitIndex(1);
        frame.add(createScrollPane(new JTextArea()));
        return frame;
    }

    protected static JMenuBar createMenuBar() {
        JMenuBar menu = new JMenuBar();

        JMenu fileMenu = createFileMenu();
        JMenu viewMenu = VsnetCommandBarFactory.createViewMenu(_frame);
        JMenu windowMenu = createWindowsMenu();
        JMenu optionMenu = createOptionMenu();
        JMenu lnfMenu = CommandBarFactory.createLookAndFeelMenu(_frame);
        JMenu helpMenu = createHelpMenu();

        menu.add(fileMenu);
        menu.add(viewMenu);
        menu.add(windowMenu);
        menu.add(optionMenu);
        menu.add(lnfMenu);
        menu.add(helpMenu);


        return menu;
    }

    private static JScrollPane createScrollPane(Component component) {
        JScrollPane pane = new JideScrollPane(component);
        pane.setVerticalScrollBarPolicy(JideScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        return pane;
    }

    private static JMenu createHelpMenu() {
        JMenu menu = new JideMenu("Help");
        menu.setMnemonic('H');

        JMenuItem item = new JMenuItem("About JIDE Products");
        item.setMnemonic('A');
        item.addActionListener(new AbstractAction() {
            private static final long serialVersionUID = -616369516668939084L;

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

        _undoMenuItem = new JMenuItem("Undo");
        menu.add(_undoMenuItem);
        _redoMenuItem = new JMenuItem("Redo");
        menu.add(_redoMenuItem);
        _undoMenuItem.setEnabled(false);
        _redoMenuItem.setEnabled(false);

        _undoMenuItem.addActionListener(new AbstractAction() {
            private static final long serialVersionUID = 6684720736606887098L;

            public void actionPerformed(ActionEvent e) {
                _frame.getDockingManager().undo();
                refreshUndoRedoMenuItems();
            }
        });
        _redoMenuItem.addActionListener(new AbstractAction() {
            private static final long serialVersionUID = 4162637946958846137L;

            public void actionPerformed(ActionEvent e) {
                _frame.getDockingManager().redo();
                refreshUndoRedoMenuItems();
            }
        });

        menu.addSeparator();

        JMenuItem item;

        item = new JMenuItem("Load Default Layout");
        item.addActionListener(new AbstractAction() {
            private static final long serialVersionUID = 1614065919178040707L;

            public void actionPerformed(ActionEvent e) {
                _frame.getDockingManager().loadLayoutData();
            }
        });
        menu.add(item);

        item = new JMenuItem("Load Design Layout");
        item.addActionListener(new AbstractAction() {
            private static final long serialVersionUID = -2174027206413266401L;

            public void actionPerformed(ActionEvent e) {
                _frame.getDockingManager().loadLayoutDataFrom("design");
            }
        });
        menu.add(item);

        item = new JMenuItem("Load Debug Layout");
        item.addActionListener(new AbstractAction() {
            private static final long serialVersionUID = -6468555992089075850L;

            public void actionPerformed(ActionEvent e) {
                _frame.getDockingManager().loadLayoutDataFrom("debug");
            }
        });
        menu.add(item);

        menu.addSeparator();

        item = new JMenuItem("Save as Default Layout");
        item.addActionListener(new AbstractAction() {
            private static final long serialVersionUID = -7238785723342314597L;

            public void actionPerformed(ActionEvent e) {
                _frame.getDockingManager().saveLayoutData();
            }
        });
        menu.add(item);

        item = new JMenuItem("Save as Design Layout");
        item.addActionListener(new AbstractAction() {
            private static final long serialVersionUID = 3432776231956146151L;

            public void actionPerformed(ActionEvent e) {
                _frame.getDockingManager().saveLayoutDataAs("design");
            }
        });
        menu.add(item);

        item = new JMenuItem("Save as Debug Layout");
        item.addActionListener(new AbstractAction() {
            private static final long serialVersionUID = -6839562276754180723L;

            public void actionPerformed(ActionEvent e) {
                _frame.getDockingManager().saveLayoutDataAs("debug");
            }
        });
        menu.add(item);

        menu.addSeparator();

        item = new JMenuItem("Reset Layout");
        item.addActionListener(new AbstractAction() {
            private static final long serialVersionUID = 4112865401284696085L;

            public void actionPerformed(ActionEvent e) {
                _frame.getDockingManager().resetToDefault();
            }
        });
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_MASK));
        menu.add(item);

        menu.addSeparator();

        item = new JMenuItem("Toggle Auto Hide All");
        item.setMnemonic('T');
        item.addActionListener(new AbstractAction() {
            private static final long serialVersionUID = -2104666580416375633L;

            public void actionPerformed(ActionEvent e) {
                if (!_autohideAll) {
                    _frame.getDockingManager().saveLayoutDataAs("fullscreen");
                    _frame.getDockingManager().autohideAll();
                    _autohideAll = true;
                }
                else {
                    _frame.getDockingManager().loadLayoutDataFrom("fullscreen");
                    _autohideAll = false;

                }
            }
        });
        menu.add(item);

        menu.addSeparator();

        item = new JMenuItem("Show Workspace Area");
        item.setMnemonic('S');
        item.addActionListener(new AbstractAction() {
            private static final long serialVersionUID = -9053943964394445510L;

            public void actionPerformed(ActionEvent e) {
                _frame.getDockingManager().setShowWorkspace(true);
            }
        });
        menu.add(item);

        item = new JMenuItem("Hide Workspace Area");
        item.setMnemonic('H');
        item.addActionListener(new AbstractAction() {
            private static final long serialVersionUID = -3689655634777393816L;

            public void actionPerformed(ActionEvent e) {
                _frame.getDockingManager().setShowWorkspace(false);
            }
        });
        menu.add(item);

        return menu;
    }

    private static void refreshUndoRedoMenuItems() {
        _undoMenuItem.setEnabled(_frame.getDockingManager().getUndoManager().canUndo());
        _undoMenuItem.setText(_frame.getDockingManager().getUndoManager().getUndoPresentationName());
        _redoMenuItem.setEnabled(_frame.getDockingManager().getUndoManager().canRedo());
        _redoMenuItem.setText(_frame.getDockingManager().getUndoManager().getRedoPresentationName());
    }

    private static JMenu createOptionMenu() {
        JMenu menu = new JideMenu("Options");
        menu.setMnemonic('P');

        JCheckBoxMenuItem checkBoxMenuItem = new JCheckBoxMenuItem("Frames Floatable");
        checkBoxMenuItem.setMnemonic('F');
        checkBoxMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() instanceof JCheckBoxMenuItem)
                    _frame.getDockingManager().setFloatable(((JCheckBoxMenuItem) e.getSource()).isSelected());
            }
        });
        checkBoxMenuItem.setSelected(_frame.getDockingManager().isFloatable());
        menu.add(checkBoxMenuItem);

        checkBoxMenuItem = new JCheckBoxMenuItem("Frames Autohidable");
        checkBoxMenuItem.setMnemonic('A');
        checkBoxMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() instanceof JCheckBoxMenuItem)
                    _frame.getDockingManager().setAutohidable(((JCheckBoxMenuItem) e.getSource()).isSelected());
            }
        });
        checkBoxMenuItem.setSelected(_frame.getDockingManager().isAutohidable());

        menu.add(checkBoxMenuItem);

        checkBoxMenuItem = new JCheckBoxMenuItem("Frames Hidable");
        checkBoxMenuItem.setMnemonic('H');
        checkBoxMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() instanceof JCheckBoxMenuItem)
                    _frame.getDockingManager().setHidable(((JCheckBoxMenuItem) e.getSource()).isSelected());
            }
        });
        checkBoxMenuItem.setSelected(_frame.getDockingManager().isHidable());
        menu.add(checkBoxMenuItem);

        checkBoxMenuItem = new JCheckBoxMenuItem("Frames Rearrangable");
        checkBoxMenuItem.setMnemonic('R');
        checkBoxMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() instanceof JCheckBoxMenuItem)
                    _frame.getDockingManager().setRearrangable(((JCheckBoxMenuItem) e.getSource()).isSelected());
            }
        });
        checkBoxMenuItem.setSelected(_frame.getDockingManager().isHidable());
        menu.add(checkBoxMenuItem);

        checkBoxMenuItem = new JCheckBoxMenuItem("Frames Resizable");
        checkBoxMenuItem.setMnemonic('S');
        checkBoxMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() instanceof JCheckBoxMenuItem)
                    _frame.getDockingManager().setResizable(((JCheckBoxMenuItem) e.getSource()).isSelected());
            }
        });
        checkBoxMenuItem.setSelected(_frame.getDockingManager().isResizable());
        menu.add(checkBoxMenuItem);

        menu.addSeparator();

        JMenu buttonsMenu = new JideMenu("Available Titlebar Buttons");

        checkBoxMenuItem = new JCheckBoxMenuItem("Close Button");
        checkBoxMenuItem.setMnemonic('C');
        checkBoxMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean selected = ((JCheckBoxMenuItem) e.getSource()).isSelected();
                toggleButton(selected, DockableFrame.BUTTON_CLOSE);
            }
        });
        checkBoxMenuItem.setSelected(true);
        buttonsMenu.add(checkBoxMenuItem);

        checkBoxMenuItem = new JCheckBoxMenuItem("Autohide Button");
        checkBoxMenuItem.setMnemonic('A');
        checkBoxMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean selected = ((JCheckBoxMenuItem) e.getSource()).isSelected();
                toggleButton(selected, DockableFrame.BUTTON_AUTOHIDE);
            }
        });
        checkBoxMenuItem.setSelected(true);
        buttonsMenu.add(checkBoxMenuItem);

        checkBoxMenuItem = new JCheckBoxMenuItem("Float Button");
        checkBoxMenuItem.setMnemonic('F');
        checkBoxMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean selected = ((JCheckBoxMenuItem) e.getSource()).isSelected();
                toggleButton(selected, DockableFrame.BUTTON_FLOATING);
            }
        });
        checkBoxMenuItem.setSelected(true);
        buttonsMenu.add(checkBoxMenuItem);

        checkBoxMenuItem = new JCheckBoxMenuItem("Maximize Button");
        checkBoxMenuItem.setMnemonic('M');
        checkBoxMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean selected = ((JCheckBoxMenuItem) e.getSource()).isSelected();
                toggleButton(selected, DockableFrame.BUTTON_MAXIMIZE);
            }
        });
        checkBoxMenuItem.setSelected(false);
        buttonsMenu.add(checkBoxMenuItem);

        menu.add(buttonsMenu);

        menu.addSeparator();

        checkBoxMenuItem = new JCheckBoxMenuItem("Continuous Layout");
        checkBoxMenuItem.setMnemonic('C');
        checkBoxMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() instanceof JCheckBoxMenuItem) {
                    _frame.getDockingManager().setContinuousLayout(((JCheckBoxMenuItem) e.getSource()).isSelected());
                    if (_frame.getDockingManager().isContinuousLayout()) {
                        Lm.showPopupMessageBox("<HTML>" +
                                "<B><FONT FACE='Tahoma' SIZE='4' COLOR='#0000FF'>Continuous Layout</FONT></B><FONT FACE='Tahoma'>" +
                                "<FONT FACE='Tahoma' SIZE='3'><BR><BR><B>An option to continuously layout affected components during resizing." +
                                "<BR></B><BR>This is the same option as in JSplitPane. If the option is true, when you resize" +
                                "<BR>the JSplitPane's divider, it will continuously redisplay and laid out during user" +
                                "<BR>intervention." +
                                "<BR><BR>Default: off</FONT>" +
                                "<BR></HTML>");
                    }
                }
            }
        });
        checkBoxMenuItem.setSelected(_frame.getDockingManager().isContinuousLayout());
        menu.add(checkBoxMenuItem);

        menu.addSeparator();

        checkBoxMenuItem = new JCheckBoxMenuItem("Easy Tab Docking");
        checkBoxMenuItem.setMnemonic('E');
        checkBoxMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() instanceof JCheckBoxMenuItem) {
                    _frame.getDockingManager().setEasyTabDock(((JCheckBoxMenuItem) e.getSource()).isSelected());
                    if (_frame.getDockingManager().isEasyTabDock()) {
                        Lm.showPopupMessageBox("<HTML>" +
                                "<B><FONT COLOR='BLUE' FACE='Tahoma' SIZE='4'>Easy Tab Docking </FONT></B>" +
                                "<BR><BR><FONT FACE='Tahoma' SIZE='3'><B>An option to make the tab-docking of a dockable frame easier</B>" +
                                "<BR><BR>It used to be dragging a dockable frame and pointing to the title" +
                                "<BR>bar of another dockable frame to tab-dock with it. However if " +
                                "<BR>this option on, pointing to the middle portion of any dockable " +
                                "<BR>frame will tab-dock with that frame." +
                                "<BR><BR>Default: off</FONT>" +
                                "<BR></HTML>");
                    }
                }
            }
        });
        checkBoxMenuItem.setSelected(_frame.getDockingManager().isEasyTabDock());
        menu.add(checkBoxMenuItem);

        checkBoxMenuItem = new JCheckBoxMenuItem("Title Bar Dragging All Tabs");
        checkBoxMenuItem.setMnemonic('T');
        checkBoxMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() instanceof JCheckBoxMenuItem) {
                    _frame.getDockingManager().setDragAllTabs(((JCheckBoxMenuItem) e.getSource()).isSelected());
                    if (_frame.getDockingManager().isDragAllTabs()) {
                        Lm.showPopupMessageBox("<HTML>" +
                                "<B><FONT COLOR='BLUE' FACE='Tahoma' SIZE='4'>Title Bar Dragging All Tabs</FONT></B>" +
                                "<BR><BR><FONT FACE='Tahoma' SIZE='3'><B>An option for title bar dragging</B>" +
                                "<BR><BR>If user drags the title bar, by default, all tabs in the tabbed pane will be dragged." +
                                "<BR>However if you set this options to be false, only the active tab will be dragged" +
                                "<BR>if user drags the title bar." +
                                "<BR><BR>Default: on</FONT>" +
                                "<BR></HTML>");
                    }
                }
            }
        });
        checkBoxMenuItem.setSelected(_frame.getDockingManager().isDragAllTabs());
        menu.add(checkBoxMenuItem);

        checkBoxMenuItem = new JCheckBoxMenuItem("Allow Nested Floating Windows");
        checkBoxMenuItem.setMnemonic('A');
        checkBoxMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() instanceof JCheckBoxMenuItem) {
                    _frame.getDockingManager().setNestedFloatingAllowed(((JCheckBoxMenuItem) e.getSource()).isSelected());
                    if (_frame.getDockingManager().isNestedFloatingAllowed()) {
                        Lm.showPopupMessageBox("<HTML>" +
                                "<FONT FACE='Tahoma' SIZE='4'><B><FONT COLOR='#0000FF'>Nested Floating Windows<BR></FONT></B><BR></FONT>" +
                                "<FONT FACE='Tahoma' SIZE='3'><B>An option to allow nested windows when in floating mode</B>" +
                                "<BR><BR>JIDE Docking Framework can allow you to have as many nested windows in one floating " +
                                "<BR>container as you want. However, not all your users want to have that complexity. So we " +
                                "<BR>leave it as an option and you can choose to turn it on or off. " +
                                "<BR><BR>Default: off</FONT> <BR>" +
                                "</HTML>");
                    }
                }
            }
        });
        checkBoxMenuItem.setSelected(_frame.getDockingManager().isNestedFloatingAllowed());
        menu.add(checkBoxMenuItem);

        checkBoxMenuItem = new JCheckBoxMenuItem("Show Gripper");
        checkBoxMenuItem.setMnemonic('S');
        checkBoxMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() instanceof JCheckBoxMenuItem) {
                    _frame.getDockingManager().setShowGripper(((JCheckBoxMenuItem) e.getSource()).isSelected());
                    if (_frame.getDockingManager().isShowGripper()) {
                        Lm.showPopupMessageBox("<HTML>" +
                                "<FONT FACE='Tahoma' SIZE='4'><FONT COLOR='#0000FF'><B>Show Gripper</B><BR></FONT><BR></FONT>" +
                                "<FONT FACE='Tahoma' SIZE='3'><B>An option to give user a visual hint that the dockable frame can be dragged<BR></B>" +
                                "<BR>Normal tabs in JTabbedPane can not be dragged. However in our demo, " +
                                "<BR>most of them can be dragged. To make it obvious to user, we added an " +
                                "<BR>option so that a gripper is painted on the tab or the title bar of those " +
                                "<BR>dockable frames which can be dragged." +
                                "<BR><BR>Default: off</FONT><BR>" +
                                "</HTML>");
                    }
                }
            }
        });
        checkBoxMenuItem.setSelected(_frame.getDockingManager().isShowGripper());
        menu.add(checkBoxMenuItem);

        checkBoxMenuItem = new JCheckBoxMenuItem("Show TitleBar");
        checkBoxMenuItem.setMnemonic('T');
        checkBoxMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() instanceof JCheckBoxMenuItem) {
                    _frame.getDockingManager().setShowTitleBar(((JCheckBoxMenuItem) e.getSource()).isSelected());
                    if (_frame.getDockingManager().isShowTitleBar()) {
                        Lm.showPopupMessageBox("<HTML>" +
                                "<FONT FACE='Tahoma' SIZE='4'><FONT COLOR='#0000FF'><B>Show TitleBar</B><BR></FONT><BR></FONT>" +
                                "<FONT FACE='Tahoma' SIZE='3'><B>An option to show/hide dockable frame's title bar<BR></B>" +
                                "<BR><BR>Default: on</FONT><BR>" +
                                "</HTML>");
                    }
                }
            }
        });
        checkBoxMenuItem.setSelected(_frame.getDockingManager().isShowTitleBar());
        menu.add(checkBoxMenuItem);

        checkBoxMenuItem = new JCheckBoxMenuItem("SideBar Rollover");
        checkBoxMenuItem.setMnemonic('A');
        checkBoxMenuItem.addActionListener(new AbstractAction() {
            private static final long serialVersionUID = 7316173526008682922L;

            public void actionPerformed(ActionEvent e) {
                if (e.getSource() instanceof JCheckBoxMenuItem) {
                    _frame.getDockingManager().setSidebarRollover(((JCheckBoxMenuItem) e.getSource()).isSelected());
                    if (_frame.getDockingManager().isSidebarRollover()) {
                        Lm.showPopupMessageBox("<HTML>" +
                                "<FONT FACE='Tahoma' SIZE='4'><FONT COLOR='#0000FF'><B>SideBar Rollover</B><BR></FONT><BR></FONT>" +
                                "<FONT FACE='Tahoma' SIZE='3'><B>An option to control the sensibility of tabs on sidebar<BR></B>" +
                                "<BR>Each tab on four sidebars is corresponding to a dockable frame. Usually when " +
                                "<BR>user moves mouse over the tab, the dockable frame will show up. However in Eclipse" +
                                "<BR>you must click on it to show the dockable frame. This option will allow you to " +
                                "<BR>control the sensibility of it." +
                                "<BR><BR>Default: on</FONT><BR>" +
                                "</HTML>");
                    }
                }
            }
        });
        checkBoxMenuItem.setSelected(_frame.getDockingManager().isSidebarRollover());
        menu.add(checkBoxMenuItem);

        menu.addSeparator();

        JRadioButtonMenuItem radioButtonMenuItem1 = new JRadioButtonMenuItem("Draw Full Outline When Dragging (Heavy-weighted)");
        radioButtonMenuItem1.setMnemonic('F');
        radioButtonMenuItem1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() instanceof JRadioButtonMenuItem) {
                    _frame.getDockingManager().setOutlineMode(DefaultDockingManager.FULL_OUTLINE_MODE);
                }
            }
        });
        radioButtonMenuItem1.setSelected(_frame.getDockingManager().getOutlineMode() == DefaultDockingManager.FULL_OUTLINE_MODE);
        menu.add(radioButtonMenuItem1);

        JRadioButtonMenuItem radioButtonMenuItem2 = new JRadioButtonMenuItem("Draw Partial Outline When Dragging (Light-weighted)");
        radioButtonMenuItem2.setMnemonic('P');
        radioButtonMenuItem2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() instanceof JRadioButtonMenuItem) {
                    _frame.getDockingManager().setOutlineMode(DefaultDockingManager.PARTIAL_OUTLINE_MODE);
                }
            }
        });
        radioButtonMenuItem2.setSelected(_frame.getDockingManager().getOutlineMode() == DefaultDockingManager.PARTIAL_OUTLINE_MODE);
        menu.add(radioButtonMenuItem2);

        JRadioButtonMenuItem radioButtonMenuItem3 = new JRadioButtonMenuItem("Draw Transparent Pane When Dragging (Light-weighted)");
        radioButtonMenuItem3.setMnemonic('T');
        radioButtonMenuItem3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() instanceof JRadioButtonMenuItem) {
                    _frame.getDockingManager().setOutlineMode(DefaultDockingManager.TRANSPARENT_OUTLINE_MODE);
                }
            }
        });
        radioButtonMenuItem3.setSelected(_frame.getDockingManager().getOutlineMode() == DefaultDockingManager.TRANSPARENT_OUTLINE_MODE);
        menu.add(radioButtonMenuItem3);

        JRadioButtonMenuItem radioButtonMenuItem4 = new JRadioButtonMenuItem("Draw Transparent Pane When Dragging (Heavy-weighted, JDK6u10 and above only)");
        radioButtonMenuItem4.setMnemonic('H');
        radioButtonMenuItem4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() instanceof JRadioButtonMenuItem) {
                    _frame.getDockingManager().setOutlineMode(DefaultDockingManager.HW_TRANSPARENT_OUTLINE_MODE);
                }
            }
        });
        radioButtonMenuItem4.setSelected(_frame.getDockingManager().getOutlineMode() == DefaultDockingManager.HW_TRANSPARENT_OUTLINE_MODE);
        menu.add(radioButtonMenuItem4);

        JRadioButtonMenuItem radioButtonMenuItem5 = new JRadioButtonMenuItem("Draw Full Outline When Dragging (Heavy-weighted, JDK6u10 and above only)");
        radioButtonMenuItem5.setMnemonic('O');
        radioButtonMenuItem5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() instanceof JRadioButtonMenuItem) {
                    _frame.getDockingManager().setOutlineMode(DefaultDockingManager.HW_OUTLINE_MODE);
                }
            }
        });
        radioButtonMenuItem5.setSelected(_frame.getDockingManager().getOutlineMode() == DefaultDockingManager.HW_OUTLINE_MODE);
        menu.add(radioButtonMenuItem5);

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(radioButtonMenuItem1);
        buttonGroup.add(radioButtonMenuItem2);
        buttonGroup.add(radioButtonMenuItem3);
        buttonGroup.add(radioButtonMenuItem4);
        buttonGroup.add(radioButtonMenuItem5);

        menu.addSeparator();
        menu.add(new AbstractAction("Toggle Left-to-Right") {
            private static final long serialVersionUID = -581950962868061955L;

            public void actionPerformed(ActionEvent e) {
                JideSwingUtilities.toggleRTLnLTR(_frame);
            }
        });

        return menu;
    }

    private static void toggleButton(boolean selected, int button) {
        Collection<String> names = _frame.getDockingManager().getAllFrames();
        for (String name : names) {
            DockableFrame frame = _frame.getDockingManager().getFrame(name);
            if (selected) {
                frame.setAvailableButtons(frame.getAvailableButtons() | button);
            }
            else {
                frame.setAvailableButtons(frame.getAvailableButtons() & ~button);
            }
        }
    }

    private static JMenu createFileMenu() {
        JMenuItem item;

        JMenu menu = new JideMenu("File");
        menu.setMnemonic('F');

        item = new JMenuItem("Exit");
        item.addActionListener(new AbstractAction() {
            private static final long serialVersionUID = -5359744447961331767L;

            public void actionPerformed(ActionEvent e) {
                clearUp();
            }
        });
        menu.add(item);
        return menu;
    }
}
