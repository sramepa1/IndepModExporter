/*
 * @(#)SampleVsnet.java
 *
 * Copyright 2002 - 2004 JIDE Software. All rights reserved.
 */

import com.jidesoft.action.DefaultDockableBarDockableHolder;
import com.jidesoft.docking.DefaultDockingManager;
import com.jidesoft.docking.DockContext;
import com.jidesoft.docking.DockableFrame;
import com.jidesoft.docking.DockingManager;
import com.jidesoft.document.*;
import com.jidesoft.icons.JideIconsFactory;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.status.*;
import com.jidesoft.swing.ContentContainer;
import com.jidesoft.swing.JideBoxLayout;
import com.jidesoft.swing.JideTabbedPane;
import com.jidesoft.utils.Lm;
import com.jidesoft.utils.PortingUtils;

import javax.swing.*;
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

public class SampleVsnet extends DefaultDockableBarDockableHolder {

    private static SampleVsnet _frame;
    private static IDocumentPane _documentPane;

    private static WindowAdapter _windowListener;
    private static StatusBar _statusBar;
    private static Timer _timer;
    private static final String PROFILE_NAME = "jidesoft-vsnetdemo";

    private static boolean _autohideAll = false;
    private static byte[] _fullScreenLayout;

    public SampleVsnet(String title) throws HeadlessException {
        super(title);
    }

    public SampleVsnet() throws HeadlessException {
        this("");
    }

    public static void main(String[] args) {
        PortingUtils.prerequisiteChecking();
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showDemo(true);
    }

    public static DefaultDockableBarDockableHolder showDemo(final boolean exit) {
        _frame = new SampleVsnet("Demo of JIDE Action Framework and JIDE Docking Framework - Microsoft Visual Studio .NET");
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

        // set the profile key
        _frame.getLayoutPersistence().setProfileKey(PROFILE_NAME);

// uncomment the next line if you don't want to use javax pref
//        _frame.getLayoutPersistence().setUsePref(false);

// uncomment the next three lines if you want to adjust the sliding speed of autohide frame
//        _frame.getDockingManager().setInitDelay(100);
//        _frame.getDockingManager().setSteps(1);
//        _frame.getDockingManager().setStepDelay(0);

        // create tabbed-document interface and add it to workspace area
        _documentPane = createDocumentTabs();
        _frame.getDockingManager().getWorkspace().setLayout(new BorderLayout());
        _frame.getDockingManager().getWorkspace().add((Component) _documentPane, BorderLayout.CENTER);

//        _frame.getDockingManager().setAutohidable(false);

// uncomment the following lines if you want to customize the popup menu of DockableFrame
//       _frame.getDockingManager().setPopupMenuCustomizer(new com.jidesoft.docking.DockableBarPopupMenuCustomizer() {
//           public void customizePopupMenu(JPopupMenu menu, DockingManager dockingManager, DockableFrame dockableFrame, boolean onTab) {
//           }
//
//           public boolean isPopupMenuShown(DockingManager dockingManager, DockableFrame dockableFrame, boolean onTab) {
//               return false;
//           }
//       });

// uncomment the following lines if you want to customize the appearance of tabbed pane
//        _frame.getDockingManager().setTabbedPaneCustomizer(new DefaultDockingManager.TabbedPaneCustomizer() {
//            public void customize(JideTabbedPane tabbedPane) {
//                tabbedPane.setTabResizeMode(JideTabbedPane.RESIZE_MODE_DEFAULT);
//                tabbedPane.setTabPlacement(SwingConstants.TOP);
//                // hide the title bar if there are more than one tabs in the tabbed pane.
//                tabbedPane.addChangeListener(new ChangeListener() {
//                    public void stateChanged(ChangeEvent e) {
//                        JideTabbedPane tabbedPane = (JideTabbedPane) e.getSource();
//                        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
//                            ((DockableFrame) tabbedPane.getComponentAt(i)).setShowTitleBar(tabbedPane.getTabCount() <= 1);
//                        }
//                    }
//                });
//            }
//        });

        // add menu bar
//        _frame.setJMenuBar(createMenuBar());

        _frame.getDockableBarManager().setProfileKey(PROFILE_NAME);
        // add toolbar
        _frame.getDockableBarManager().addDockableBar(VsnetCommandBarFactory.createMenuCommandBar(_frame));
        _frame.getDockableBarManager().addDockableBar(VsnetCommandBarFactory.createStandardCommandBar());
        _frame.getDockableBarManager().addDockableBar(VsnetCommandBarFactory.createLayoutCommandBar());
        _frame.getDockableBarManager().addDockableBar(VsnetCommandBarFactory.createBuildCommandBar());
        _frame.getDockableBarManager().addDockableBar(VsnetCommandBarFactory.createFormattingCommandBar());
        _frame.getDockableBarManager().addDockableBar(VsnetCommandBarFactory.createLookAndFeelCommandBar(_frame));
        _frame.getDockableBarManager().addDockableBar(VsnetCommandBarFactory.createDockingFrameworkCommandBar(_frame.getDockingManager(), (DocumentPane) _documentPane));

        // add status bar
        _statusBar = createStatusBar();
        _frame.getContentPane().add(_statusBar, BorderLayout.AFTER_LAST_LINE);

        _frame.getDockingManager().getWorkspace().setAdjustOpacityOnFly(true);
        _frame.getDockingManager().setUndoLimit(10);
        _frame.getDockingManager().beginLoadLayoutData();

//        _frame.getDockingManager().setFloatable(false);

        _frame.getDockingManager().setInitSplitPriority(DefaultDockingManager.SPLIT_SOUTH_NORTH_EAST_WEST);

        // add all dockable frames
        _frame.getDockingManager().addFrame(DemoDockableFrameFactory.createSampleTaskListFrame());
        _frame.getDockingManager().addFrame(DemoDockableFrameFactory.createSampleResourceViewFrame());
        _frame.getDockingManager().addFrame(DemoDockableFrameFactory.createSampleClassViewFrame());
        _frame.getDockingManager().addFrame(DemoDockableFrameFactory.createSampleProjectViewFrame());
        _frame.getDockingManager().addFrame(DemoDockableFrameFactory.createSampleServerFrame());
        _frame.getDockingManager().addFrame(DemoDockableFrameFactory.createSamplePropertyFrame());
        _frame.getDockingManager().addFrame(DemoDockableFrameFactory.createSampleFindResult1Frame());
        _frame.getDockingManager().addFrame(DemoDockableFrameFactory.createSampleFindResult2Frame());
        _frame.getDockingManager().addFrame(DemoDockableFrameFactory.createSampleOutputFrame());
        _frame.getDockingManager().addFrame(DemoDockableFrameFactory.createSampleCommandFrame());

// just use default size. If you want to overwrite, you can call this method
//      _frame.getDockingManager().setInitBounds(new Rectangle(0, 0, 960, 800));

        _frame.getDockingManager().setShowGripper(true);
        _frame.getDockingManager().setOutlineMode(DockingManager.TRANSPARENT_OUTLINE_MODE);

        _frame.getDockingManager().setPopupMenuCustomizer(new com.jidesoft.docking.PopupMenuCustomizer() {
            public void customizePopupMenu(JPopupMenu menu, final DockingManager dockingManager, final DockableFrame dockableFrame, boolean onTab) {
                menu.addSeparator();
                menu.add(new AbstractAction("Move to Document Area") {
                    public void actionPerformed(ActionEvent e) {
                        dockingManager.removeFrame(dockableFrame.getKey(), true);

                        DocumentComponent documentComponent = new DocumentComponent((JComponent) dockableFrame.getContentPane(),
                                dockableFrame.getKey(),
                                dockableFrame.getTitle(),
                                dockableFrame.getFrameIcon());
                        _documentPane.openDocument(documentComponent);
                        _documentPane.setActiveDocument(documentComponent.getName());
                    }
                });
            }
        });

        // load layout information from previous session
        _frame.getLayoutPersistence().loadLayoutData();

// uncomment the following line(s) if you want to limit certain feature.
// If you uncomment all four lines, then the dockable frames can not be moved at all.
//        _frame.getDockingManager().setRearrangable(false);
//        _frame.getDockingManager().setAutohidable(false);
//        _frame.getDockingManager().setFloatable(false);
//        _frame.getDockingManager().setHidable(false);

        if (Lm.DEMO) {
            Lm.z();
        }

        _frame.toFront();
        return _frame;
    }

    private static void clearUp() {
        _frame.removeWindowListener(_windowListener);
        _windowListener = null;
        if (_frame.getLayoutPersistence() != null) {
            _frame.getLayoutPersistence().saveLayoutData();
        }

        if (_documentPane != null) {
            _documentPane.dispose();
            _documentPane = null;
        }
        if (_statusBar != null && _statusBar.getParent() != null)
            _statusBar.getParent().remove(_statusBar);
        _statusBar = null;
        _frame.dispose();
        _frame = null;
    }

    private static StatusBar createStatusBar() {
        // setup status bar
        StatusBar statusBar = new StatusBar();
        final ProgressStatusBarItem progress = new ProgressStatusBarItem();
        progress.setCancelCallback(new ProgressStatusBarItem.CancelCallback() {
            public void cancelPerformed() {
                _timer.stop();
                _timer = null;
                progress.setStatus("Cancelled");
                progress.showStatus();
            }
        });
        statusBar.add(progress, JideBoxLayout.VARY);
        ButtonStatusBarItem button = new ButtonStatusBarItem("READ-ONLY");
        button.setIcon(JideIconsFactory.getImageIcon(JideIconsFactory.SAVE));
        button.setPreferredWidth(20);
        statusBar.add(button, JideBoxLayout.FLEXIBLE);

        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (_timer != null && _timer.isRunning())
                    return;
                _timer = new Timer(100, new ActionListener() {
                    int i = 0;

                    public void actionPerformed(ActionEvent e) {
                        if (i == 0)
                            progress.setProgressStatus("Initializing ......");
                        if (i == 10)
                            progress.setProgressStatus("Running ......");
                        if (i == 90)
                            progress.setProgressStatus("Completing ......");
                        progress.setProgress(i++);
                        if (i > 100)
                            _timer.stop();
                    }
                });
                _timer.start();
            }
        });

        final LabelStatusBarItem label = new LabelStatusBarItem("Line");
        label.setText("100:42");
        label.setPreferredWidth(60);
        label.setAlignment(JLabel.CENTER);
        statusBar.add(label, JideBoxLayout.FLEXIBLE);

        final OvrInsStatusBarItem ovr = new OvrInsStatusBarItem();
        ovr.setPreferredWidth(100);
        ovr.setAlignment(JLabel.CENTER);
        statusBar.add(ovr, JideBoxLayout.FLEXIBLE);

        final MemoryStatusBarItem gc = new MemoryStatusBarItem();
        gc.setPreferredWidth(100);
        statusBar.add(gc, JideBoxLayout.FLEXIBLE);

        return statusBar;
    }

    private static DocumentPane createDocumentTabs() {
        DocumentPane documentPane = new DocumentPane() {
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
                                    _fullScreenLayout = _frame.getDockingManager().getLayoutRawData();
                                    _frame.getDockingManager().autohideAll();
                                    _autohideAll = true;
                                }
                                else {
                                    if (_fullScreenLayout != null) {
                                        _frame.getDockingManager().setLayoutRawData(_fullScreenLayout);
                                    }
                                    _autohideAll = false;
                                }
                                Component lastFocusedComponent = _documentPane.getActiveDocument().getLastFocusedComponent();
                                if (lastFocusedComponent != null) {
                                    lastFocusedComponent.requestFocusInWindow();
                                }
                            }
                        }
                    });
                }
                return group;
            }
        };
        documentPane.setTabPlacement(javax.swing.JTabbedPane.TOP);
        JComponent editor = createTextArea("Readme.txt");
        final DocumentComponent document = new DocumentComponent(DemoDockableFrameFactory.createScrollPane(editor),
                "Readme.txt", "Readme.txt",
                JideIconsFactory.getImageIcon(JideIconsFactory.FileType.TEXT));
        document.setDefaultFocusComponent(editor);
        document.addDocumentComponentListener(new DocumentComponentAdapter() {
            @Override
            public void documentComponentClosing(DocumentComponentEvent e) {
                int ret = JOptionPane.showConfirmDialog(_frame, "<HTML><B>Do you want to save and close Readme.txt?" +
                        "<BR>&nbsp;&nbsp;&nbsp;Yes: close and save<BR>&nbsp;&nbsp;&nbsp;No: close but not save<BR>&nbsp;&nbsp;&nbsp;Cancel: cancel the closing process</B><BR><BR>" +
                        "This is just an example of how to add your own code to the document closing process. Nothing is saved.</HTML>", "Confirm", JOptionPane.YES_NO_CANCEL_OPTION);
                if (ret == JOptionPane.YES_OPTION) {
                    // save it
                    document.setAllowClosing(true);
                }
                else if (ret == JOptionPane.NO_OPTION) {
                    // don't save it
                    document.setAllowClosing(true);
                }
                else if (ret == JOptionPane.CANCEL_OPTION) {
                    // don't save it
                    document.setAllowClosing(false);
                }
            }
        });
        documentPane.openDocument(document);

        editor = createMultiViewDocument("SLA.htm");
        DocumentComponent document2 = new DocumentComponent(editor,
                "License", "License", JideIconsFactory.getImageIcon(JideIconsFactory.FileType.HTML));
        if (editor instanceof JideTabbedPane) {
            Component comp = ((JideTabbedPane) editor).getSelectedComponent();
            if (comp instanceof JScrollPane) {
                document2.setDefaultFocusComponent(((JScrollPane) comp).getViewport().getView());
            }
        }
        documentPane.openDocument(document2);

        editor = createTextArea("SampleVsnet.java");
        DocumentComponent document3 = new DocumentComponent(DemoDockableFrameFactory.createScrollPane(editor),
                "SampleVsnet.java", "C:\\Program Files\\JIDE Software\\Source Code\\SampleVsnet.java",
                JideIconsFactory.getImageIcon(JideIconsFactory.FileType.JAVA));
        document3.setDefaultFocusComponent(editor);
        documentPane.openDocument(document3);

        documentPane.setPopupMenuCustomizer(new PopupMenuCustomizer() {
            public void customizePopupMenu(JPopupMenu menu, final IDocumentPane pane, final String dragComponentName, final IDocumentGroup dropGroup, boolean onTab) {
                if (!pane.isDocumentFloating(dragComponentName)) {
                    menu.addSeparator();
                    menu.add(new AbstractAction("Dock to the Side") {
                        public void actionPerformed(ActionEvent e) {
                            DocumentComponent documentComponent = pane.getDocument(dragComponentName);
                            if (documentComponent != null) {
                                pane.closeDocument(dragComponentName);

                                // check if the document is really closed. There are cases a document is not closable or veto closing happens which can keep the document open after closeDocument call.
                                if (!pane.isDocumentOpened(dragComponentName)) {
                                    final DockableFrame frame = new DockableFrame(documentComponent.getName(), documentComponent.getIcon());
                                    frame.setTabTitle(documentComponent.getTitle());
                                    frame.getContentPane().add(documentComponent.getComponent());
                                    frame.setInitIndex(0);
                                    frame.setInitSide(DockContext.DOCK_SIDE_EAST);
                                    frame.setInitMode(DockContext.STATE_FRAMEDOCKED);
                                    _frame.getDockingManager().addFrame(frame);
                                    _frame.getDockingManager().activateFrame(frame.getKey());
                                }
                            }
                        }
                    });
                }
            }
        });

        return documentPane;
    }

    private static JComponent createMultiViewDocument(String fileName) {
        JideTabbedPane pane = new JideTabbedPane(JideTabbedPane.BOTTOM);
        pane.setTabShape(JideTabbedPane.SHAPE_BOX);
        pane.addTab("Design", JideIconsFactory.getImageIcon(JideIconsFactory.View.DESIGN), DemoDockableFrameFactory.createScrollPane(createHtmlArea(fileName)));
        pane.addTab("HTML", JideIconsFactory.getImageIcon(JideIconsFactory.View.HTML), DemoDockableFrameFactory.createScrollPane(createTextArea(fileName)));
        return pane;
    }

    private static JComponent createTextArea(String fileName) {
        JTextArea area = new JTextArea();
        Document doc = new PlainDocument();
        try {
            InputStream in = SampleVsnet.class.getResourceAsStream(fileName);
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
            System.out.println(e.getLocalizedMessage());
        }
        catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
        catch (BadLocationException e) {
            System.out.println(e.getLocalizedMessage());
        }

        return area;
    }

    private static Component createHtmlArea(String fileName) {
        JEditorPane area = new JEditorPane();
        try {
            URL url = SampleVsnet.class.getResource(fileName);
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
        return area;
    }

    @Override
    protected ContentContainer createContentContainer() {
        return new LogoContentContainer();
    }

    private class LogoContentContainer extends ContentContainer {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            ImageIcon imageIcon = JideIconsFactory.getImageIcon(JideIconsFactory.JIDELOGO_SMALL);
            imageIcon.paintIcon(this, g, getWidth() - imageIcon.getIconWidth() - 2, 2);
        }
    }
}
