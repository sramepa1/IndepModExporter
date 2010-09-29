/*
 * @(#)HeavyweightDockingFrameworkDemo.java 10/26/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.action.CommandBarFactory;
import com.jidesoft.docking.DefaultDockableHolder;
import com.jidesoft.docking.DefaultDockingManager;
import com.jidesoft.docking.DockContext;
import com.jidesoft.docking.DockableFrame;
import com.jidesoft.icons.JideIconsFactory;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.plaf.UIDefaultsLookup;
import com.jidesoft.swing.JideMenu;
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
public class HeavyweightDockingFrameworkDemo extends DefaultDockableHolder {

    private static HeavyweightDockingFrameworkDemo _frame;

    private static final String PROFILE_NAME = "jidesoft-hw";

    private static boolean _autohideAll = false;
    private static WindowAdapter _windowListener;
    public static JMenuItem _redoMenuItem;
    public static JMenuItem _undoMenuItem;
    public static Intro _intro;

    public HeavyweightDockingFrameworkDemo(String title) throws HeadlessException {
        super(title);
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showDemo(true);
    }

    public static void showDemo(final boolean exit) {
        if (_frame != null) {
            _frame.toFront();
            return;
        }
        _frame = new HeavyweightDockingFrameworkDemo("Demo of JIDE Docking Framework");
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

        _frame.getDockingManager().setInitSplitPriority(DefaultDockingManager.SPLIT_SOUTH_NORTH_EAST_WEST);

        // add all dockable frames
        _frame.getDockingManager().addFrame(createFrame1());
        _frame.getDockingManager().addFrame(createFrame2());
        _frame.getDockingManager().addFrame(createFrame3());
        _frame.getDockingManager().addFrame(createFrame4());
        _frame.getDockingManager().addFrame(createFrame5());

        _intro = new Intro();
        _intro.start();
        _frame.getDockingManager().getWorkspace().add(_intro);

        // enable HW component usage
        _frame.getDockingManager().setHeavyweightComponentEnabled(true);

        // load layout information from previous session. This indicates the end of beginLoadLayoutData() method above.
        _frame.getDockingManager().loadLayoutData();

        // disallow drop dockable frame to workspace area
        _frame.getDockingManager().getWorkspace().setAcceptDockableFrame(false);

        _frame.toFront();
    }

    private static void clearUp() {
        _intro.stop();
        _frame.removeWindowListener(_windowListener);
        _windowListener = null;

        if (_frame.getDockingManager() != null) {
            _frame.getDockingManager().saveLayoutData();
        }

        _frame.dispose();
        _frame = null;
    }

    protected static DockableFrame createFrame1() {
        DockableFrame frame = new DockableFrame("Frame 1", JideIconsFactory.getImageIcon(JideIconsFactory.DockableFrame.FRAME1));
        frame.getContext().setInitMode(DockContext.STATE_FRAMEDOCKED);
        frame.getContext().setInitSide(DockContext.DOCK_SIDE_EAST);
        frame.getContext().setInitIndex(1);
        frame.getContentPane().add(createPanel());
        frame.setPreferredSize(new Dimension(200, 200));
        return frame;
    }

    protected static DockableFrame createFrame2() {
        DockableFrame frame = new DockableFrame("Frame 2", JideIconsFactory.getImageIcon(JideIconsFactory.DockableFrame.FRAME2));
        frame.getContext().setInitMode(DockContext.STATE_FRAMEDOCKED);
        frame.getContext().setInitSide(DockContext.DOCK_SIDE_EAST);
        frame.getContext().setInitIndex(1);
        frame.getContentPane().add(createJPanel());
        frame.setPreferredSize(new Dimension(400, 200));
        return frame;
    }

    protected static DockableFrame createFrame3() {
        DockableFrame frame = new DockableFrame("Frame 3", JideIconsFactory.getImageIcon(JideIconsFactory.DockableFrame.FRAME3));
        frame.getContext().setInitMode(DockContext.STATE_AUTOHIDE);
        frame.getContext().setInitSide(DockContext.DOCK_SIDE_EAST);
        frame.getContentPane().add(createJPanel());
        frame.setPreferredSize(new Dimension(200, 200));
        return frame;
    }

    protected static DockableFrame createFrame4() {
        DockableFrame frame = new DockableFrame("Frame 4", JideIconsFactory.getImageIcon(JideIconsFactory.DockableFrame.FRAME4));
        frame.getContext().setInitMode(DockContext.STATE_AUTOHIDE);
        frame.getContext().setInitSide(DockContext.DOCK_SIDE_WEST);
        frame.getContext().setInitIndex(0);
        frame.getContentPane().add(createCanvas());
        frame.setPreferredSize(new Dimension(200, 200));
        return frame;
    }

    protected static DockableFrame createFrame5() {
        DockableFrame frame = new DockableFrame("Frame 5", JideIconsFactory.getImageIcon(JideIconsFactory.DockableFrame.FRAME5));
        frame.getContext().setInitMode(DockContext.STATE_FRAMEDOCKED);
        frame.getContext().setInitSide(DockContext.DOCK_SIDE_WEST);
        frame.getContext().setInitIndex(0);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(createCanvas());
        frame.setPreferredSize(new Dimension(200, 200));
        return frame;
    }

    private static Panel createPanel() {
        Panel panel = new Panel() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                g.setColor(Color.BLUE);
                Rectangle viewR = new Rectangle(0, 0, getWidth(), getHeight()), iconR = new Rectangle(), textR = new Rectangle();
                SwingUtilities.layoutCompoundLabel(g.getFontMetrics(), "This is an AWT Panel", null, SwingConstants.CENTER, SwingConstants.CENTER, SwingConstants.CENTER, SwingConstants.CENTER,
                        viewR, iconR, textR, 0);
                g.drawString("This is an AWT Panel", textR.x, textR.y);
                g.setColor(UIDefaultsLookup.getColor("controlShadow"));
                g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
            }
        };
        panel.setBackground(Color.WHITE);
        return panel;
    }

    private static JPanel createJPanel() {
        JPanel panel = new JPanel() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                g.setColor(Color.BLUE);
                Rectangle viewR = new Rectangle(0, 0, getWidth(), getHeight()), iconR = new Rectangle(), textR = new Rectangle();
                SwingUtilities.layoutCompoundLabel(g.getFontMetrics(), "This is an AWT Panel", null, SwingConstants.CENTER, SwingConstants.CENTER, SwingConstants.CENTER, SwingConstants.CENTER,
                        viewR, iconR, textR, 0);
                g.drawString("This is a Swing Panel", textR.x, textR.y);
                g.setColor(UIDefaultsLookup.getColor("controlShadow"));
                g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
            }
        };
        panel.setBackground(Color.YELLOW);
        return panel;
    }
    
    private static Canvas createCanvas() {
        Canvas canvas = new Canvas() {
            @Override
            public Dimension getMinimumSize() {
                return new Dimension(0, 0);
            }

            @Override
            public void paint(Graphics g) {
                super.paint(g);
                g.setColor(Color.MAGENTA);
                Rectangle viewR = new Rectangle(0, 0, getWidth(), getHeight()), iconR = new Rectangle(), textR = new Rectangle();
                SwingUtilities.layoutCompoundLabel(g.getFontMetrics(), "This is an AWT Canvas", null, SwingConstants.CENTER, SwingConstants.CENTER, SwingConstants.CENTER, SwingConstants.CENTER,
                        viewR, iconR, textR, 0);
                g.drawString("This is an AWT Canvas", textR.x, textR.y);
                g.setColor(UIDefaultsLookup.getColor("controlShadow"));
                g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
            }
        };
        canvas.setBackground(Color.WHITE);
        return canvas;
    }

    protected static JMenuBar createMenuBar() {
        JPopupMenu.setDefaultLightWeightPopupEnabled(false);
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

        JMenuItem item;

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

        item = new JMenuItem("Frame 1", JideIconsFactory.getImageIcon(JideIconsFactory.DockableFrame.FRAME1));
        item.setMnemonic('1');
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, InputEvent.CTRL_MASK));
        item.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                _frame.getDockingManager().showFrame("Frame 1");
            }
        });
        menu.add(item);

        item = new JMenuItem("Frame 2", JideIconsFactory.getImageIcon(JideIconsFactory.DockableFrame.FRAME2));
        item.setMnemonic('2');
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, InputEvent.CTRL_MASK));
        item.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                _frame.getDockingManager().showFrame("Frame 2");
            }
        });
        menu.add(item);

        item = new JMenuItem("Frame 3", JideIconsFactory.getImageIcon(JideIconsFactory.DockableFrame.FRAME3));
        item.setMnemonic('3');
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, InputEvent.CTRL_MASK));
        item.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                _frame.getDockingManager().showFrame("Frame 3");
            }
        });
        menu.add(item);

        item = new JMenuItem("Frame 4", JideIconsFactory.getImageIcon(JideIconsFactory.DockableFrame.FRAME4));
        item.setMnemonic('4');
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_4, InputEvent.CTRL_MASK));
        item.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                _frame.getDockingManager().showFrame("Frame 4");
            }
        });
        menu.add(item);

        item = new JMenuItem("Frame 5", JideIconsFactory.getImageIcon(JideIconsFactory.DockableFrame.FRAME5));
        item.setMnemonic('5');
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_5, InputEvent.CTRL_MASK));
        item.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                _frame.getDockingManager().showFrame("Frame 5");
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
                    _frame.getDockingManager().getFrame("Property").setShowTitleBar(((JCheckBoxMenuItem) e.getSource()).isSelected());
//                    _frame.getDockingManager().setShowTitleBar(((JCheckBoxMenuItem) e.getSource()).isSelected());
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

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(radioButtonMenuItem1);
        buttonGroup.add(radioButtonMenuItem2);

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
