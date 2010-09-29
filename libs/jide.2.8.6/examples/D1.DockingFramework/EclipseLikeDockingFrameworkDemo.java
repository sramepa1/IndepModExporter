/*
 * @(#)EclipseLikeDockingFrameworkDemo.java 11/4/2008
 *
 * Copyright 2002 - 2008 JIDE Software Inc. All rights reserved.
 *
 */

import com.jidesoft.action.CommandBarFactory;
import com.jidesoft.docking.*;
import com.jidesoft.icons.JideIconsFactory;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.JideMenu;
import com.jidesoft.swing.JideScrollPane;
import com.jidesoft.swing.JideSwingUtilities;
import com.jidesoft.swing.JideTabbedPane;
import com.jidesoft.utils.Lm;

import javax.swing.*;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.plaf.UIResource;
import java.awt.*;
import java.awt.event.*;
import java.util.Collection;

/**
 * This is a sample program for JIDE Docking Framework. It will create a JFrame with about 20 dockable frames to show
 * the features of JIDE Docking Framework. <br> Required jar files: jide-common.jar, jide-dock.jar <br> Required L&F:
 * Jide L&F extension required
 */
public class EclipseLikeDockingFrameworkDemo extends DefaultDockableHolder {

    private static EclipseLikeDockingFrameworkDemo _frame;

    private static final String PROFILE_NAME = "jidesoft-eclipse";

    private static boolean _autohideAll = false;
    private static WindowAdapter _windowListener;
    public static JMenuItem _redoMenuItem;
    public static JMenuItem _undoMenuItem;

    public EclipseLikeDockingFrameworkDemo(String title) throws HeadlessException {
        super(title);
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showDemo(true);
    }

    public static DefaultDockableHolder showDemo(final boolean exit) {
        if (_frame != null) {
            _frame.toFront();
            return _frame;
        }
        _frame = new EclipseLikeDockingFrameworkDemo("Demo of JIDE Docking Framework");
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
        _frame.getDockingManager().setOutlineMode(DockingManager.TRANSPARENT_OUTLINE_MODE);

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

// uncomment following lines if you want to customize the appearance of tabbed pane
        _frame.getDockingManager().setTabbedPaneCustomizer(new DefaultDockingManager.TabbedPaneCustomizer() {
            public void customize(JideTabbedPane tabbedPane) {
                tabbedPane.setTabPlacement(SwingConstants.TOP);
                tabbedPane.setHideOneTab(false);
            }
        });
        _frame.getDockingManager().setShowTitleBar(false);
        _frame.getDockingManager().setEasyTabDock(true);

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

    static class UIResourcePanel extends JPanel implements UIResource {
        UIResourcePanel() {
            setOpaque(false);
        }
    }

    protected static DockableFrame createDockableFrame(String key, Icon icon) {
        DockableFrame frame = new DockableFrame(key, icon);
        frame.setPreferredSize(new Dimension(200, 200));
//        frame.addDockableFrameListener(new DockableFrameAdapter(){
//            @Override
//            public void dockableFrameTabShown(DockableFrameEvent e) {
//                if(e.getDockableFrame().getParent() instanceof FrameContainer) {
//                    UIResourcePanel toolbar = new UIResourcePanel();
//                    JButton closeButton = new JideButton(e.getDockableFrame().getCloseAction());
//                    closeButton.setPreferredSize(new Dimension(16, 16));
//                    toolbar.add(closeButton);
//                    JButton autohideButton = new JideButton(e.getDockableFrame().getAutohideAction());
//                    autohideButton.setPreferredSize(new Dimension(16, 16));
//                    toolbar.add(autohideButton);
//                    JButton floatingButton = new JideButton(e.getDockableFrame().getFloatingAction());
//                    floatingButton.setPreferredSize(new Dimension(16, 16));
//                    toolbar.add(floatingButton);
//                    ((FrameContainer) e.getDockableFrame().getParent()).setTabTrailingComponent(toolbar);
//                }
//            }
//        });
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
        frame.getContext().setInitMode(DockContext.STATE_FRAMEDOCKED);
        frame.getContext().setInitSide(DockContext.DOCK_SIDE_SOUTH);
        JList list = new JList(new String[]{"Task1", "Task2", "Task3"});
        list.setToolTipText("This is a tooltip");
        frame.add(createScrollPane(list));
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
        JMenu viewMenu = createViewMenu();
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
            public void actionPerformed(ActionEvent e) {
                _frame.getDockingManager().undo();
                refreshUndoRedoMenuItems();
            }
        });
        _redoMenuItem.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                _frame.getDockingManager().redo();
                refreshUndoRedoMenuItems();
            }
        });

        menu.addSeparator();

        JMenuItem item;

        item = new JMenuItem("Load Default Layout");
        item.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                _frame.getDockingManager().loadLayoutData();
            }
        });
        menu.add(item);

        item = new JMenuItem("Load Design Layout");
        item.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                _frame.getDockingManager().loadLayoutDataFrom("design");
            }
        });
        menu.add(item);

        item = new JMenuItem("Load Debug Layout");
        item.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                _frame.getDockingManager().loadLayoutDataFrom("debug");
            }
        });
        menu.add(item);

        menu.addSeparator();

        item = new JMenuItem("Save as Default Layout");
        item.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                _frame.getDockingManager().saveLayoutData();
            }
        });
        menu.add(item);

        item = new JMenuItem("Save as Design Layout");
        item.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                _frame.getDockingManager().saveLayoutDataAs("design");
            }
        });
        menu.add(item);

        item = new JMenuItem("Save as Debug Layout");
        item.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                _frame.getDockingManager().saveLayoutDataAs("debug");
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
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_MASK));
        menu.add(item);

        menu.addSeparator();

        item = new JMenuItem("Toggle Auto Hide All");
        item.setMnemonic('T');
        item.addActionListener(new AbstractAction() {
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
            public void actionPerformed(ActionEvent e) {
                _frame.getDockingManager().setShowWorkspace(true);
            }
        });
        menu.add(item);

        item = new JMenuItem("Hide Workspace Area");
        item.setMnemonic('H');
        item.addActionListener(new AbstractAction() {
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

    private static JMenu createViewMenu() {
        JMenuItem item;
        JMenu menu = new JideMenu("View");
        menu.setMnemonic('V');

        item = new JMenuItem("Select Next View");
        item.setMnemonic('N');
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0));
        item.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                String frameKey = _frame.getDockingManager().getNextFrame(_frame.getDockingManager().getActiveFrameKey());
                if (frameKey != null) {
                    _frame.getDockingManager().showFrame(frameKey);
                }
            }
        });
        menu.add(item);

        item = new JMenuItem("Select Previous View");
        item.setMnemonic('P');
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F6, InputEvent.SHIFT_MASK));
        item.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                String frameKey = _frame.getDockingManager().getPreviousFrame(_frame.getDockingManager().getActiveFrameKey());
                if (frameKey != null) {
                    _frame.getDockingManager().showFrame(frameKey);
                }
            }
        });
        menu.add(item);

        menu.addSeparator();

        item = new JMenuItem("Project View", JideIconsFactory.getImageIcon(JideIconsFactory.DockableFrame.FRAME1));
        item.setMnemonic('P');
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK | InputEvent.ALT_MASK));
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
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
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

        menu.addSeparator();

        item = new JMenuItem("Task List", JideIconsFactory.getImageIcon(JideIconsFactory.DockableFrame.FRAME6));
        item.setMnemonic('T');
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_MASK | InputEvent.ALT_MASK));
        item.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                _frame.getDockingManager().showFrame("Task List");
            }
        });
        menu.add(item);

        item = new JMenuItem("Command Window", JideIconsFactory.getImageIcon(JideIconsFactory.DockableFrame.FRAME7));
        item.setMnemonic('N');
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_MASK | InputEvent.ALT_MASK));
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
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, InputEvent.CTRL_MASK | InputEvent.ALT_MASK));
        item.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                _frame.getDockingManager().showFrame("Find Results 1");
            }
        });
        menu.add(item);

        item = new JMenuItem("Find Results 2", JideIconsFactory.getImageIcon(JideIconsFactory.DockableFrame.FRAME10));
        item.setMnemonic('3');
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, InputEvent.CTRL_MASK | InputEvent.ALT_MASK));
        item.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                _frame.getDockingManager().showFrame("Find Results 2");
            }
        });
        menu.add(item);

        item = new JMenuItem("Find Results 3", JideIconsFactory.getImageIcon(JideIconsFactory.DockableFrame.FRAME11));
        item.setMnemonic('3');
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, InputEvent.CTRL_MASK | InputEvent.ALT_MASK));
        item.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                _frame.getDockingManager().showFrame("Find Results 3");
            }
        });
        menu.add(item);

        return menu;
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

        JRadioButtonMenuItem radioButtonMenuItem1 = new JRadioButtonMenuItem("Draw Full Outline When Dragging");
        radioButtonMenuItem1.setMnemonic('D');
        radioButtonMenuItem1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() instanceof JRadioButtonMenuItem) {
                    _frame.getDockingManager().setOutlineMode(DefaultDockingManager.FULL_OUTLINE_MODE);
                    Lm.showPopupMessageBox("<HTML>" +
                            "<B><FONT FACE='Tahoma' SIZE='4' COLOR='#0000FF'>Outline Paint Mode</FONT></B><FONT FACE='Tahoma'>" +
                            "<FONT SIZE='4'>" +
                            "<FONT COLOR='#0000FF' SIZE='3'><BR><BR><B>An option of how to paint the outline during dragging.</B></FONT>" +
                            "<BR><BR><FONT SIZE='3'>Since our demo is purely based on Swing, and there is no way to have transparent native " +
                            "<BR>window using Swing. So we have to develop workarounds to paint the outline of a dragging frame. " +
                            "<BR>As a result, we get two ways to draw the outline. Since neither is perfect, we just leave it as " +
                            "<BR>an option to user to choose. You can try each of the option and see which one you like most." +
                            "<BR><B><BR>Option 1: PARTIAL_OUTLINE_MODE</B><BR>Pros: Fast, very smooth, works the best if user " +
                            "of your application always keeps it as full screen" +
                            "<BR>Cons: Partial outline or no outline at all if outside main frame although it's there wherever " +
                            "your mouse is." +
                            "<BR><BR><B>Option 2: FULL_OUTLINE_MODE</B>" +
                            "<BR>Pros: It always draw the full outline" +
                            "<BR>Cons: Sometimes it's flickering. Slower comparing with partial outline mode." +
                            "<BR><BR>Default: PARTIAL_OUTLINE_MODE</FONT>" +
                            "<BR></HTML>");
                }
            }
        });
        radioButtonMenuItem1.setSelected(_frame.getDockingManager().getOutlineMode() == DefaultDockingManager.FULL_OUTLINE_MODE);
        menu.add(radioButtonMenuItem1);

        JRadioButtonMenuItem radioButtonMenuItem2 = new JRadioButtonMenuItem("Draw Partial Outline When Dragging");
        radioButtonMenuItem2.setMnemonic('P');
        radioButtonMenuItem2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() instanceof JRadioButtonMenuItem) {
                    _frame.getDockingManager().setOutlineMode(DefaultDockingManager.PARTIAL_OUTLINE_MODE);
                    Lm.showPopupMessageBox("<HTML>" +
                            "<B><FONT FACE='Tahoma' SIZE='4' COLOR='#0000FF'>Outline Paint Mode</FONT></B><FONT FACE='Tahoma'>" +
                            "<FONT SIZE='4'><FONT COLOR='#0000FF'><BR></FONT><BR></FONT><B>An option of how to paint the outline during dragging. " +
                            "<BR><BR></B>Since our demo is purely based on Swing, and there is no way to have transparent native " +
                            "<BR>window using Swing. So we have to develop workarounds to paint the outline of a dragging frame. " +
                            "<BR>As a result, we get two ways to draw the outline. Since neither is perfect, we just leave it as " +
                            "<BR>an option to user to choose. You can try each of the option and see which one you like most." +
                            "<BR><B><BR>Option 1: PARTIAL_OUTLINE_MODE</B>" +
                            "<BR>Pros: Fast, very smooth" +
                            "<BR>Cons: Partial outline or no outline at all if outside main frame although it&#39;s there wherever your mouse is." +
                            "<BR><BR><B>Option 2: FULL_OUTLINE_MODE</B>" +
                            "<BR>Pros: It always draw the full outline<BR>Cons: Sometimes it&#39;s flickering. Slower comparing with partial outline mode.</FONT>" +
                            "<BR><BR><FONT FACE='Tahoma'>Default: PARTIAL_OUTLINE_MODE</FONT>" +
                            "<BR></HTML>");
                }
            }
        });
        radioButtonMenuItem2.setSelected(_frame.getDockingManager().getOutlineMode() == DefaultDockingManager.PARTIAL_OUTLINE_MODE);
        menu.add(radioButtonMenuItem2);

        JRadioButtonMenuItem radioButtonMenuItem3 = new JRadioButtonMenuItem("Draw Transparent Pane When Dragging");
        radioButtonMenuItem3.setMnemonic('P');
        radioButtonMenuItem3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() instanceof JRadioButtonMenuItem) {
                    _frame.getDockingManager().setOutlineMode(DefaultDockingManager.TRANSPARENT_OUTLINE_MODE);
                    Lm.showPopupMessageBox("<HTML>" +
                            "<B><FONT FACE='Tahoma' SIZE='4' COLOR='#0000FF'>Outline Paint Mode</FONT></B><FONT FACE='Tahoma'>" +
                            "<FONT SIZE='4'><FONT COLOR='#0000FF'><BR></FONT><BR></FONT><B>An option of how to paint the outline during dragging. " +
                            "<BR><BR></B>Instead of drawing an outline as all other options, this option will draw a transparent pane" +
                            "<BR>which looks better than the outline only." +
                            "<BR></HTML>");
                }
            }
        });
        radioButtonMenuItem3.setSelected(_frame.getDockingManager().getOutlineMode() == DefaultDockingManager.TRANSPARENT_OUTLINE_MODE);
        menu.add(radioButtonMenuItem3);

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(radioButtonMenuItem1);
        buttonGroup.add(radioButtonMenuItem2);
        buttonGroup.add(radioButtonMenuItem3);

        menu.addSeparator();
        menu.add(new AbstractAction("Toggle Left-to-Right") {
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
            public void actionPerformed(ActionEvent e) {
                clearUp();
            }
        });
        menu.add(item);
        return menu;
    }
}