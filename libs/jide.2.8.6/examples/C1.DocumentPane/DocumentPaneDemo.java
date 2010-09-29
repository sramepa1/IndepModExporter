/*
 * @(#)DocumentPaneDemo.java 4/3/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.dialog.ButtonPanel;
import com.jidesoft.document.DocumentComponent;
import com.jidesoft.document.DocumentPane;
import com.jidesoft.icons.JideIconsFactory;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.JideSplitPane;
import com.jidesoft.swing.JideTabbedPane;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Demoed Component: {@link DocumentPane} <br> Required jar files: jide-common.jar, jide-components.jar <br> Required
 * L&F: Jide L&F extension required
 */
public class DocumentPaneDemo extends AbstractDemo {
    public DocumentPane _documentPane;
    private static final long serialVersionUID = 5092562223449688843L;

    public DocumentPaneDemo() {
    }

    public String getName() {
        return "DocumentPane Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_COMPONENTS;
    }

    @Override
    public int getAttributes() {
        return ATTRIBUTE_UPDATED;
    }

    @Override
    public String getDescription() {
        return "DocumentPane is tabbed document interface (or TDI in short) implementation. It supports viewing multiple documents " +
                "using tabbed pane, viewing two ore more documents at the same time, view several different views of the same documents, " +
                "document open/close/activate/deactivate events, drag-n-drop organizing documents and many other advanced features. " +
                "It's an ideal component to replace traditional MDI implementation using JDesktopPane.\n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.document.DocumentPane\n" +
                "com.jidesoft.document.DocumentComponent";
    }

    @Override
    public Component getOptionsPanel() {
        ButtonPanel buttonPanel = new ButtonPanel(SwingConstants.TOP);
        buttonPanel.addButton(new JButton(new AbstractAction("Reset Documents Layout") {
            private static final long serialVersionUID = -9159930345704254300L;

            public void actionPerformed(ActionEvent e) {
                java.util.List<DocumentComponent> list = new ArrayList<DocumentComponent>();
                list.add(new DocumentComponent(createMultiViewDocument("SLA.htm"),
                        "License", "License", JideIconsFactory.getImageIcon(JideIconsFactory.FileType.HTML)));
                list.add(new DocumentComponent(new JScrollPane(DocumentPaneDemo.createTextArea("Readme.txt")),
                        "Readme", "Readme",
                        JideIconsFactory.getImageIcon(JideIconsFactory.FileType.TEXT)));
                list.add(new DocumentComponent(new JScrollPane(DocumentPaneDemo.createTextArea("DocumentPaneDemo.java")),
                        "DocumentPaneDemo.java", "C:\\Projects\\JideSoft\\Source Code\\DocumentPaneDemo.java",
                        JideIconsFactory.getImageIcon(JideIconsFactory.FileType.JAVA)));
                _documentPane.setOpenedDocuments(list);
                _documentPane.getLayoutPersistence().resetToDefault();
            }
        }));
        buttonPanel.addButton(new JButton(new AbstractAction("Tile Horizontally") {
            private static final long serialVersionUID = 2745558452168850356L;

            public void actionPerformed(ActionEvent e) {
                String[] names = _documentPane.getDocumentNames();
                for (int i = 0; i < names.length; i++) {
                    String name = names[i];
                    _documentPane.newDocumentGroup(name, i, JideSplitPane.HORIZONTAL_SPLIT);
                }
            }
        }));
        buttonPanel.addButton(new JButton(new AbstractAction("Tile Vertically") {
            private static final long serialVersionUID = -3546476316455005201L;

            public void actionPerformed(ActionEvent e) {
                String[] names = _documentPane.getDocumentNames();
                for (int i = 0; i < names.length; i++) {
                    String name = names[i];
                    _documentPane.newDocumentGroup(name, i, JideSplitPane.VERTICAL_SPLIT);
                }
            }
        }));
        buttonPanel.addButton(new JButton(new AbstractAction("Next Document") {
            private static final long serialVersionUID = 947394973544927871L;

            public void actionPerformed(ActionEvent e) {
                _documentPane.nextDocument();
            }
        }));
        buttonPanel.addButton(new JButton(new AbstractAction("Previous Document") {
            private static final long serialVersionUID = -3161733881128267806L;

            public void actionPerformed(ActionEvent e) {
                _documentPane.prevDocument();
            }
        }));
        buttonPanel.addButton(new JButton(new AbstractAction("Activate Document") {
            private static final long serialVersionUID = 8188297658775593002L;

            public void actionPerformed(ActionEvent e) {
                JPopupMenu menu = new JPopupMenu("Activate");
                for (int i = 0; i < _documentPane.getDocumentCount(); i++) {
                    final DocumentComponent document = _documentPane.getDocumentAt(i);
                    menu.add(new AbstractAction(document.getName()) {
                        private static final long serialVersionUID = 1023207435962800692L;

                        public void actionPerformed(ActionEvent e) {
                            _documentPane.setActiveDocument(document.getName());
                        }
                    });
                }
                JButton button = (JButton) e.getSource();
                menu.show(button, 0, button.getHeight());
            }
        }));

        buttonPanel.addButton(new JButton(new AbstractAction("Open Document in Floating Mode") {
            private static final long serialVersionUID = 2418804071076075435L;

            public void actionPerformed(ActionEvent e) {
                DocumentComponent component = new DocumentComponent(createMultiViewDocument("SLA.htm"),
                        "License 2", "License 2", JideIconsFactory.getImageIcon(JideIconsFactory.FileType.HTML));
                _documentPane.openDocument(component, true);
            }
        }));

        buttonPanel.addButton(new JToggleButton(new AbstractAction("Dock or Close when closing floating container") {
            private static final long serialVersionUID = 6301160308607075378L;

            public void actionPerformed(ActionEvent e) {
                _documentPane.setFloatingContainerCloseAction(_documentPane.getFloatingContainerCloseAction() == DocumentPane.CLOSE_ACTION_TO_CLOSE ? DocumentPane.CLOSE_ACTION_TO_DOCK : DocumentPane.CLOSE_ACTION_TO_CLOSE);
            }
        }));

        buttonPanel.addButton(new JButton(new AbstractAction("Save Layout") {
            private static final long serialVersionUID = -7559230589199255892L;

            public void actionPerformed(ActionEvent e) {
                _documentPane.getLayoutPersistence().saveLayoutData();
            }
        }));

        buttonPanel.addButton(new JButton(new AbstractAction("Load Layout") {
            private static final long serialVersionUID = -4001269983592295108L;

            public void actionPerformed(ActionEvent e) {
                java.util.List<DocumentComponent> list = new ArrayList<DocumentComponent>();

                JComponent editor = createMultiViewDocument("SLA.htm");
                DocumentComponent htmDocument = new DocumentComponent(editor,
                        "License", "License", JideIconsFactory.getImageIcon(JideIconsFactory.FileType.HTML));
                if (editor instanceof JideTabbedPane) {
                    Component comp = ((JideTabbedPane) editor).getSelectedComponent();
                    if (comp instanceof JScrollPane) {
                        htmDocument.setDefaultFocusComponent(((JScrollPane) comp).getViewport().getView());
                    }
                }
                list.add(htmDocument);

                editor = createTextArea("Readme.txt");
                final DocumentComponent txtDocument = new DocumentComponent(DemoDockableFrameFactory.createScrollPane(editor),
                        "Readme.txt", "Readme.txt",
                        JideIconsFactory.getImageIcon(JideIconsFactory.FileType.TEXT));
                txtDocument.setDefaultFocusComponent(editor);
                list.add(txtDocument);

                editor = createTextArea("SampleVsnet.java");
                DocumentComponent javaDocument = new DocumentComponent(DemoDockableFrameFactory.createScrollPane(editor),
                        "SampleVsnet.java", "C:\\Program Files\\JIDE Software\\Source Code\\SampleVsnet.java",
                        JideIconsFactory.getImageIcon(JideIconsFactory.FileType.JAVA));
                javaDocument.setDefaultFocusComponent(editor);
                list.add(javaDocument);

                _documentPane.setOpenedDocuments(list);
                _documentPane.getLayoutPersistence().loadLayoutData();
            }
        }));

        JCheckBox allowReorder = new JCheckBox("Allow Reorder");
        allowReorder.setSelected(_documentPane.isReorderAllowed());
        buttonPanel.addButton(allowReorder);
        allowReorder.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _documentPane.setReorderAllowed(e.getStateChange() == ItemEvent.SELECTED);
            }
        });

        JCheckBox allowRearrange = new JCheckBox("Allow Rearrange");
        allowRearrange.setSelected(_documentPane.isRearrangeAllowed());
        buttonPanel.addButton(allowRearrange);
        allowRearrange.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _documentPane.setRearrangeAllowed(e.getStateChange() == ItemEvent.SELECTED);
            }
        });
        JCheckBox allowFloating = new JCheckBox("Allow Floating");
        allowFloating.setSelected(_documentPane.isFloatingAllowed());
        buttonPanel.addButton(allowFloating);
        allowFloating.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _documentPane.setFloatingAllowed(e.getStateChange() == ItemEvent.SELECTED);
            }
        });
        JCheckBox allowDraggingPast = new JCheckBox("Allow Passing Unmovable Components");
        allowDraggingPast.setSelected(_documentPane.isDragPassUnmovableAllowed());
        buttonPanel.addButton(allowDraggingPast);
        allowDraggingPast.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _documentPane.getDocument("License").setTitle(e.getStateChange() == ItemEvent.SELECTED ? "License" : "License(Freeze)");
                _documentPane.getDocument("License").setAllowMoving(e.getStateChange() == ItemEvent.SELECTED);
                _documentPane.setDragPassUnmovableAllowed(e.getStateChange() == ItemEvent.SELECTED);
            }
        });
        return buttonPanel;
    }

    public Component getDemoPanel() {
        _documentPane = createDocumentTabs();
//        _documentPane.setMaximumGroupCount(3);
        _documentPane.setGroupsAllowed(true);
        _documentPane.setTabbedPaneCustomizer(new DocumentPane.TabbedPaneCustomizer() {
            public void customize(final JideTabbedPane tabbedPane) {
                tabbedPane.setShowCloseButton(true);
                tabbedPane.setUseDefaultShowCloseButtonOnTab(false);
                tabbedPane.setShowCloseButtonOnTab(true);
            }
        });
        _documentPane.setPreferredSize(new Dimension(500, 400));
        return _documentPane;
    }

    @Override
    public String getDemoFolder() {
        return "C1.DocumentPane";
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new DocumentPaneDemo());
    }

    private DocumentPane createDocumentTabs() {
        DocumentPane pane = new DocumentPane();
        java.util.List<DocumentComponent> list = new ArrayList<DocumentComponent>();

        JComponent editor = createMultiViewDocument("SLA.htm");
        DocumentComponent htmDocument = new DocumentComponent(editor,
                "License", "License", JideIconsFactory.getImageIcon(JideIconsFactory.FileType.HTML));
        if (editor instanceof JideTabbedPane) {
            Component comp = ((JideTabbedPane) editor).getSelectedComponent();
            if (comp instanceof JScrollPane) {
                htmDocument.setDefaultFocusComponent(((JScrollPane) comp).getViewport().getView());
            }
        }
        list.add(htmDocument);

        editor = createTextArea("Readme.txt");
        final DocumentComponent txtDocument = new DocumentComponent(DemoDockableFrameFactory.createScrollPane(editor),
                "Readme.txt", "Readme.txt",
                JideIconsFactory.getImageIcon(JideIconsFactory.FileType.TEXT));
        txtDocument.setDefaultFocusComponent(editor);
        list.add(txtDocument);

        editor = createTextArea("SampleVsnet.java");
        DocumentComponent javaDocument = new DocumentComponent(DemoDockableFrameFactory.createScrollPane(editor),
                "SampleVsnet.java", "C:\\Program Files\\JIDE Software\\Source Code\\SampleVsnet.java",
                JideIconsFactory.getImageIcon(JideIconsFactory.FileType.JAVA));
        javaDocument.setDefaultFocusComponent(editor);
        list.add(javaDocument);

        pane.setOpenedDocuments(list);
        pane.getLayoutPersistence().setProfileKey("documents");
        pane.getLayoutPersistence().loadLayoutData();
        return pane;
    }

    private static JComponent createMultiViewDocument(String fileName) {
        JideTabbedPane pane = new JideTabbedPane(JideTabbedPane.BOTTOM);
        pane.setTabShape(JideTabbedPane.SHAPE_BOX);
        pane.addTab("Design", JideIconsFactory.getImageIcon(JideIconsFactory.View.DESIGN), new JScrollPane(createHtmlArea(fileName)));
        pane.addTab("HTML", JideIconsFactory.getImageIcon(JideIconsFactory.View.HTML), new JScrollPane(createTextArea(fileName)));
        return pane;
    }

    public static JTextArea createTextArea(String fileName) {
        JTextArea area = new JTextArea();
        Document doc = new PlainDocument();
        try {
            // try to start reading
            InputStream in = DocumentPaneDemo.class.getResourceAsStream(fileName);
            if (in != null) {
                byte[] buff = new byte[4096];
                int nch;
                while ((nch = in.read(buff, 0, buff.length)) != -1) {
                    doc.insertString(doc.getLength(), new String(buff, 0, nch), null);
                }
                area.setDocument(doc);
            }
            else {
                area.setText("Copy Readme.txt and DocumentPaneDemo.java into the class output directory");
            }
        }
        catch (FileNotFoundException e) {
            // null
        }
        catch (IOException e) {
            // null
        }
        catch (BadLocationException e) {
            // null
        }
        return area;
    }

    public static Component createHtmlArea(String fileName) {
        JEditorPane area = new JEditorPane();
        try {
            area.setPage(DocumentPaneDemo.class.getResource(fileName));
        }
        catch (IOException e) {
            area.setText("Copy SLA.html into the class output directory");
        }
        return area;
    }
}
