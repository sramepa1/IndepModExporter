/*
 * @(#)JideDemos.java 2/14/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.action.DockableBarDockableHolderPanel;
import com.jidesoft.docking.DefaultDockingManager;
import com.jidesoft.docking.DockContext;
import com.jidesoft.docking.DockableFrame;
import com.jidesoft.document.*;
import com.jidesoft.icons.JideIconsFactory;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.plaf.office2003.BasicOffice2003Theme;
import com.jidesoft.plaf.office2003.Office2003Painter;
import com.jidesoft.status.MemoryStatusBarItem;
import com.jidesoft.status.ProgressStatusBarItem;
import com.jidesoft.status.ResizeStatusBarItem;
import com.jidesoft.status.StatusBar;
import com.jidesoft.swing.JideBoxLayout;
import com.jidesoft.swing.JideTabbedPane;
import com.jidesoft.utils.Lm;
import com.jidesoft.utils.PortingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class JideDemos extends DockableBarDockableHolderPanel {

    private static JideDemos _holder;
    private static IDocumentPane _documentPane;

    private static StatusBar _statusBar;
    private static final String PROFILE_NAME = "jidesoft-demos";

    private static boolean _autohideAll = false;
    private static byte[] _fullScreenLayout;

    public static final String DEMOS_DOCKABLE_FRAME_KEY = "Demos";
    public static final String OPTIONS_DOCKABLE_FRAME_KEY = "Options";
    public static WindowAdapter _windowListener;
    private static JFrame _frame;

    public JideDemos(RootPaneContainer rootPaneContainer) {
        super(rootPaneContainer);

        // set the profile key
        getLayoutPersistence().setProfileKey(PROFILE_NAME);

        getDockingManager().setAllowedDockSides(DockContext.DOCK_SIDE_VERTICAL);

        // create tabbed-document interface and add it to workspace area
        _documentPane = createDocumentTabs();
        getDockingManager().getWorkspace().setLayout(new BorderLayout());
        getDockingManager().getWorkspace().add((Component) _documentPane, BorderLayout.CENTER);

        getDockableBarManager().setProfileKey(PROFILE_NAME);

        // add toolbar
        getDockableBarManager().addDockableBar(DemosCommandBarFactory.createMenuCommandBar(_frame));
        getDockableBarManager().addDockableBar(DemosCommandBarFactory.createLookAndFeelCommandBar(_frame));

        getDockingManager().getWorkspace().setAdjustOpacityOnFly(true);

        getDockingManager().setUndoLimit(10);
        getDockingManager().setAutohideShowingContentHidden(false);
        getDockingManager().beginLoadLayoutData();

        getDockingManager().setInitSplitPriority(DefaultDockingManager.SPLIT_SOUTH_NORTH_EAST_WEST);
        getDockingManager().setShowGripper(true);

        getDockingManager().addFrame(new DemosDockableFrame(DEMOS_DOCKABLE_FRAME_KEY, this));
        DockableFrame optionsFrame = new DockableFrame(OPTIONS_DOCKABLE_FRAME_KEY, DemoIconsFactory.getImageIcon(DemoIconsFactory.Frame.OPTIONS));
        optionsFrame.getContext().setInitMode(DockContext.STATE_HIDDEN);
        getDockingManager().addFrame(optionsFrame);

    }

    public static void main(String[] args) {
//        JFrame.setDefaultLookAndFeelDecorated(true);
        PortingUtils.prerequisiteChecking();

//        com.jidesoft.swing.JideSwingUtilities.traceFocus(true);
//        LoggerUtils.enableLogger("java.awt.focus", Level.ALL);

        // add an example custom theme
        BasicOffice2003Theme theme = new BasicOffice2003Theme("Custom");
        theme.setBaseColor(new Color(50, 190, 150), true, "default");
        ((Office2003Painter) Office2003Painter.getInstance()).addTheme(theme);

        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();

        _frame = new JFrame("Demo of JIDE Products - " + Lm.getProductVersion());
        _frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_G, Event.CTRL_MASK
                        | Event.SHIFT_MASK), "printMem");
        _frame.getRootPane().getActionMap().put("printMem", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                com.jidesoft.swing.JideSwingUtilities.runGCAndPrintFreeMemory();
            }
        });
        _frame.setIconImage(JideIconsFactory.getImageIcon(JideIconsFactory.JIDE32).getImage());

        // add a window listener so that timer can be stopped when exit
        _windowListener = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                clearUp();
                System.exit(0);
            }
        };
        _frame.addWindowListener(_windowListener);

        // add status bar
        _statusBar = createStatusBar();
        _frame.getContentPane().add(_statusBar, BorderLayout.AFTER_LAST_LINE);

        _holder = new JideDemos(_frame);

        _frame.getContentPane().add(_holder, BorderLayout.CENTER);
        _holder.getLayoutPersistence().loadLayoutData();
        _holder.getDockingManager().hideFrame(OPTIONS_DOCKABLE_FRAME_KEY);

        _holder.getDockingManager().activateFrame(DEMOS_DOCKABLE_FRAME_KEY);

        _frame.toFront();
    }

    private static void clearUp() {
        _frame.removeWindowListener(_windowListener);
        _windowListener = null;

        if (_holder.getLayoutPersistence() != null) {
            _holder.getLayoutPersistence().saveLayoutData();
            _holder.dispose();
            if (_holder.getParent() != null) {
                _holder.getParent().remove(_holder);
            }
            _holder = null;
        }

        _documentPane = null;

        if (_statusBar != null && _statusBar.getParent() != null) {
            _statusBar.getParent().remove(_statusBar);
        }
        _statusBar = null;

        _frame.dispose();
    }

    private static StatusBar createStatusBar() {
        // setup status bar
        StatusBar statusBar = new StatusBar();
        final ProgressStatusBarItem progress = new ProgressStatusBarItem();
        progress.setCancelCallback(new ProgressStatusBarItem.CancelCallback() {
            public void cancelPerformed() {
                progress.setStatus("Cancelled");
                progress.showStatus();
            }
        });
        statusBar.add(progress, JideBoxLayout.VARY);

        final MemoryStatusBarItem gc = new MemoryStatusBarItem();
        statusBar.add(gc, JideBoxLayout.FIX);

        final ResizeStatusBarItem resize = new ResizeStatusBarItem();
        statusBar.add(resize, JideBoxLayout.FIX);

        return statusBar;
    }

    private static DocumentPane createDocumentTabs() {
        final DocumentPane pane = new DocumentPane() {
            // add function to maximize (autohideAll) the document pane when mouse double clicks on the tabs of DocumentPane.
            @Override
            protected IDocumentGroup createDocumentGroup() {
                IDocumentGroup group = super.createDocumentGroup();
                if (group instanceof JideTabbedPane) {
                    ((JideTabbedPane) group).addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2) {
                                if (!_autohideAll) {
                                    _fullScreenLayout = _holder.getDockingManager().getLayoutRawData();
                                    _holder.getDockingManager().autohideAll();
                                    _autohideAll = true;
                                }
                                else {
                                    if (_fullScreenLayout != null) {
                                        _holder.getDockingManager().setLayoutRawData(_fullScreenLayout);
                                    }
                                    _autohideAll = false;
                                }
                            }
                        }
                    });
                }
                return group;
            }
        };
        pane.registerKeyboardAction(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                pane.closeDocument(pane.getActiveDocumentName());
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_F4, KeyEvent.CTRL_MASK), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        pane.registerKeyboardAction(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                pane.nextDocument();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, KeyEvent.ALT_MASK), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        pane.registerKeyboardAction(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                pane.prevDocument();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, KeyEvent.ALT_MASK | KeyEvent.SHIFT_MASK), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        pane.setTabPlacement(JTabbedPane.TOP);
        pane.setName("JideDemo$DocumentPane");
        return pane;
    }

    public void openDemo(final Demo demo) {
        if (!_documentPane.isDocumentOpened(demo.getName())) {
            final DemoDocumentComponent documentComponent = new DemoDocumentComponent(demo);
            documentComponent.addDocumentComponentListener(new DocumentComponentAdapter() {
                @Override
                public void documentComponentOpened(DocumentComponentEvent e) {
                }

                @Override
                public void documentComponentClosing(DocumentComponentEvent e) {
                    DockableFrame frame = getDockingManager().getFrame(OPTIONS_DOCKABLE_FRAME_KEY);
                    frame.getContentPane().removeAll();
                    getDockingManager().hideFrame(OPTIONS_DOCKABLE_FRAME_KEY);
                    demo.dispose();
                }

                @Override
                public void documentComponentClosed(DocumentComponentEvent e) {
                }

                @Override
                public void documentComponentActivated(final DocumentComponentEvent e) {
                    JComponent panes = AbstractDemo.createOptionsPanel(_frame, demo, documentComponent.getDemoPanel());
                    DockableFrame frame = getDockingManager().getFrame(OPTIONS_DOCKABLE_FRAME_KEY);
                    frame.getContentPane().removeAll();
                    if (panes != null) {
                        frame.getContentPane().setLayout(new BorderLayout());
                        frame.getContentPane().add(new JScrollPane(panes), BorderLayout.CENTER);
                        frame.setPreferredSize(new Dimension(300, 300));
                        frame.setTitle("Options - " + demo.getName());
                        if (frame.isHidden()) {
                            getDockingManager().showFrame(OPTIONS_DOCKABLE_FRAME_KEY, false);
                        }
                    }
                    else {
                        frame.setTitle("Options");
                        getDockingManager().hideFrame(OPTIONS_DOCKABLE_FRAME_KEY);
                    }
                    if (demo instanceof AnimatedDemo) {
                        ((AnimatedDemo) demo).startAnimation();
                    }
                }

                @Override
                public void documentComponentDeactivated(DocumentComponentEvent e) {
                    if (demo instanceof AnimatedDemo) {
                        ((AnimatedDemo) demo).stopAnimation();
                    }

                    DockableFrame frame = getDockingManager().getFrame(OPTIONS_DOCKABLE_FRAME_KEY);
                    frame.setTitle("Options");
                    frame.getContentPane().removeAll();
                }
            });
            _documentPane.openDocument(documentComponent);
        }
        _documentPane.setActiveDocument(demo.getName(), true);
    }

    public static StatusBar getStatusBar() {
        return _statusBar;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (LookAndFeelFactory.isCurrentLnfDecorated()) {
            ImageIcon imageIcon = JideIconsFactory.getImageIcon(JideIconsFactory.JIDELOGO_SMALL2);
            imageIcon.paintIcon(this, g, getWidth() - imageIcon.getIconWidth() - 2, 2);
        }
    }
}
