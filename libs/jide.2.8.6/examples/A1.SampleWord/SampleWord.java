/*
 * @(#)SampleWord.java
 *
 * Copyright 2002 - 2004 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.action.DefaultDockableBarHolder;
import com.jidesoft.document.*;
import com.jidesoft.icons.JideIconsFactory;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.plaf.office2003.Office2003Painter;
import com.jidesoft.status.LabelStatusBarItem;
import com.jidesoft.status.StatusBar;
import com.jidesoft.swing.ContentContainer;
import com.jidesoft.swing.JideBoxLayout;
import com.jidesoft.swing.JideScrollPane;
import com.jidesoft.swing.JideTabbedPane;
import com.jidesoft.utils.Lm;
import com.jidesoft.utils.PortingUtils;
import com.jidesoft.utils.SystemInfo;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class SampleWord extends DefaultDockableBarHolder {

    private static SampleWord _frame;
    private static IDocumentPane _documentPane;

    private static StatusBar _statusBar;
    private static final String PROFILE_NAME = "jidesoft-worddemo";

    public SampleWord(String title) throws HeadlessException {
        super(title);
    }

    public SampleWord() throws HeadlessException {
        this("");
    }

    public static void main(String[] args) {
        PortingUtils.prerequisiteChecking();

        // setNative(true) will make the color used by action framework to be kept the same as native XP theme.
        Office2003Painter.setNative(SystemInfo.isWindowsXP());

        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showDemo(true);

    }

    public static void showDemo(final boolean exit) {
        if (_frame != null) {
            _frame.toFront();
            return;
        }
        _frame = new SampleWord("Demo of JIDE Action Framework - Microsoft Word");
        _frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        _frame.setIconImage(JideIconsFactory.getImageIcon(JideIconsFactory.JIDE32).getImage());

        // add a window listener so that timer can be stopped when exit
        _frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                clearUp();
                if (exit) {
                    System.exit(0);
                }
            }
        });

        _frame.getDockableBarManager().setProfileKey(PROFILE_NAME);

        // add status bar
        _statusBar = createStatusBar();
        _frame.getContentPane().add(_statusBar, BorderLayout.AFTER_LAST_LINE);

        // add command bar
        _frame.getDockableBarManager().addDockableBar(Office2003CommandBarFactory.createMenuCommandBar(_frame));
        _frame.getDockableBarManager().addDockableBar(Office2003CommandBarFactory.createStandardCommandBar());
        _frame.getDockableBarManager().addDockableBar(Office2003CommandBarFactory.createFormattingCommandBar());
        _frame.getDockableBarManager().addDockableBar(Office2003CommandBarFactory.createDrawingCommandBar());

        _documentPane = createDocumentTabs();
        _frame.getDockableBarManager().getMainContainer().setLayout(new BorderLayout());
        _frame.getDockableBarManager().getMainContainer().add((Component) _documentPane, BorderLayout.CENTER);

        _frame.getDockableBarManager().loadLayoutData();


        if (Lm.DEMO) {
            Lm.z();
        }

        _frame.toFront();
    }

    private static void clearUp() {
        if (_frame.getDockableBarManager() != null) {
            _frame.getDockableBarManager().saveLayoutData();
        }
        _documentPane = null;
        if (_statusBar != null && _statusBar.getParent() != null)
            _statusBar.getParent().remove(_statusBar);
        _statusBar = null;
        _frame.dispose();
        _frame = null;
    }

    private static StatusBar createStatusBar() {
        // setup status bar
        StatusBar statusBar = new StatusBar();

        final LabelStatusBarItem page = new LabelStatusBarItem("Page");
        page.setText("Page 1       Sec 1       1/1    ");
        page.setAlignment(JLabel.CENTER);
        statusBar.add(page, JideBoxLayout.FLEXIBLE);

        final LabelStatusBarItem line = new LabelStatusBarItem("Page");
        line.setText("At 1\"       Ln 1       Col 1    ");
        line.setAlignment(JLabel.CENTER);
        statusBar.add(line, JideBoxLayout.FLEXIBLE);

        final LabelStatusBarItem rec = new LabelStatusBarItem("Rec");
        rec.setText("REC");
        rec.getComponent().setForeground(Color.GRAY);
        rec.setAlignment(JLabel.CENTER);
        statusBar.add(rec, JideBoxLayout.FLEXIBLE);

        final LabelStatusBarItem trk = new LabelStatusBarItem("Trk");
        trk.setText("TRK");
        trk.getComponent().setForeground(Color.GRAY);
        trk.setAlignment(JLabel.CENTER);
        statusBar.add(trk, JideBoxLayout.FLEXIBLE);

        final LabelStatusBarItem ext = new LabelStatusBarItem("Ext");
        ext.setText("EXT");
        ext.getComponent().setForeground(Color.GRAY);
        ext.setAlignment(JLabel.CENTER);
        statusBar.add(ext, JideBoxLayout.FLEXIBLE);

        final LabelStatusBarItem ovr = new LabelStatusBarItem("Ovr");
        ovr.setText("OVR");
        ovr.getComponent().setForeground(Color.GRAY);
        ovr.setAlignment(JLabel.CENTER);
        statusBar.add(ovr, JideBoxLayout.FLEXIBLE);

        final LabelStatusBarItem language = new LabelStatusBarItem("Lang");
        language.setText("English (U.S.)");
        language.setAlignment(JLabel.CENTER);
        statusBar.add(language, JideBoxLayout.FLEXIBLE);

        final LabelStatusBarItem spelling = new LabelStatusBarItem("Spelling");
        spelling.setIcon(Office2003IconsFactory.getImageIcon(Office2003IconsFactory.Status.ERROR));
        spelling.setPreferredWidth(40);
        spelling.setAlignment(JLabel.CENTER);
        statusBar.add(spelling, JideBoxLayout.FLEXIBLE);

        statusBar.add(Box.createGlue(), JideBoxLayout.VARY);

        return statusBar;
    }

    private static DocumentPane createDocumentTabs() {
        DocumentPane panel = new DocumentPane();
        panel.setTabPlacement(JTabbedPane.TOP);
        JComponent editor = createTextArea("Readme.txt");
        final DocumentComponent document = new DocumentComponent(createScrollPane(editor),
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
        panel.openDocument(document);

        editor = createMultiViewDocument("SLA.htm");
        DocumentComponent document2 = new DocumentComponent(editor,
                "License", "License", JideIconsFactory.getImageIcon(JideIconsFactory.FileType.HTML));
        if (editor instanceof JideTabbedPane) {
            Component comp = ((JideTabbedPane) editor).getSelectedComponent();
            if (comp instanceof JScrollPane) {
                document2.setDefaultFocusComponent(((JScrollPane) comp).getViewport().getView());
            }
        }
        panel.openDocument(document2);

// an example to use DockableBarPopupMenuCustomizer to customize popup menu for DocumentPane
//        panel.setPopupMenuCustomizer(new DockableBarPopupMenuCustomizer(){
//            public void customizePopupMenu(JPopupMenu menu, final IDocumentPane pane, final String dragComponentName, final IDocumentGroup dropGroup, boolean onTab) {
//                menu.remove(0);
//                menu.insert(new AbstractAction("Say Hello") {
//                    public void actionPerformed(ActionEvent e) {
//                        JOptionPane.showMessageDialog(null, "Hello");
//                    }
//                }, 0);
//            }
//        });

        return panel;
    }

    private static JScrollPane createScrollPane(Component component) {
        JScrollPane pane = new JideScrollPane(component);
        pane.setFocusable(false);
        return pane;
    }

    private static JComponent createMultiViewDocument(String fileName) {
        JideTabbedPane pane = new JideTabbedPane(JideTabbedPane.BOTTOM);
        pane.setTabShape(JideTabbedPane.SHAPE_BOX);
        pane.addTab("Design", JideIconsFactory.getImageIcon(JideIconsFactory.View.DESIGN), createScrollPane(createHtmlArea(fileName)));
        pane.addTab("HTML", JideIconsFactory.getImageIcon(JideIconsFactory.View.HTML), createScrollPane(createTextArea(fileName)));
        return pane;
    }

    private static JComponent createTextArea(String fileName) {
        JTextArea area = new JTextArea();
        Document doc = new PlainDocument();
        try {
            InputStream in = SampleWord.class.getResourceAsStream(fileName);
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
            URL url = SampleWord.class.getResource(fileName);
            if (url != null) {
                area.setPage(url);
            }
            else {
                area.setPage("file://" + fileName);
            }
        }
        catch (IOException e) {
            // ignore
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
