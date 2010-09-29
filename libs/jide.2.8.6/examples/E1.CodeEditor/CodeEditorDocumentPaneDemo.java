/*
 * @(#)CodeEditorDocumentPaneDemo.java 5/4/2007
 *
 * Copyright 2002 - 2007 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.dialog.ButtonPanel;
import com.jidesoft.dialog.StandardDialog;
import com.jidesoft.document.DocumentComponent;
import com.jidesoft.editor.*;
import com.jidesoft.editor.action.DefaultInputHandler;
import com.jidesoft.editor.margin.AbstractLineMargin;
import com.jidesoft.editor.margin.BraceMatchingMarginPainter;
import com.jidesoft.editor.margin.CodeFoldingMargin;
import com.jidesoft.editor.margin.DefaultCodeFoldingPainter;
import com.jidesoft.editor.settings.FontPanel;
import com.jidesoft.editor.settings.StyleListPanel;
import com.jidesoft.icons.IconsFactory;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.plaf.UIDefaultsLookup;
import com.jidesoft.search.*;
import com.jidesoft.shortcut.ShortcutEditor;
import com.jidesoft.swing.JideSwingUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;

/**
 * Demoed Component: {@link com.jidesoft.editor.CodeEditor} <br> Required jar files: jide-common.jar, jide-editor.jar,
 * jide-components.jar, jide-shortcut.jar, jide-editor.jar <br> Required L&F: any L&F
 */
public class CodeEditorDocumentPaneDemo extends AbstractDemo {
    public CodeEditorDocumentPane _documentPane;
    public CodeEditor[] _editors;

    public CodeEditorDocumentPaneDemo() {
    }

    public String getName() {
        return "CodeEditor DocumentPane Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_CODE_EDITOR;
    }


    @Override
    public int getAttributes() {
        return ATTRIBUTE_BETA;
    }

    @Override
    public String getDescription() {
        return "This is an example to show how to use multiple CodeEditors along with DocumentPane to implement a tabbed document interface (TDI)" +
                "as you can find in most IDEs. We also add a status bar and margin areas to show you how easy it is to add those features. " +
                "We also allow you to change several attributes through the option panel.\n\n" +
                "Demoed classes:\n" +
                "CodeEditor";
    }

    @Override
    public Component getOptionsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1, 2, 2));
//        panel.add(new JButton(new AbstractAction("Save") {
//            public void actionPerformed(ActionEvent e) {
//            }
//        }));
//        panel.add(new JButton(new AbstractAction("Load") {
//            public void actionPerformed(ActionEvent e) {
//            }
//        }));

        final JCheckBox specialChars = new JCheckBox("Show Special Characters");
        specialChars.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                DefaultSettings.getDefaults().setSpecialCharactersVisible(specialChars.isSelected());
            }
        });
        specialChars.setSelected(DefaultSettings.getDefaults().isSpecialCharactersVisible());

        final JCheckBox lineNumber = new JCheckBox("Show Line Number");
        lineNumber.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                for (int i = 0; i < _documentPane.getDocumentCount(); i++) {
                    String name = _documentPane.getDocumentNameAt(i);
                    DocumentComponent doc = _documentPane.getDocument(name);
                    if (doc instanceof CodeEditorDocumentComponent) {
                        ((CodeEditorDocumentComponent) doc).getCodeEditor().setLineNumberVisible(lineNumber.isSelected());
                    }
                }
            }
        });
        lineNumber.setSelected(true);

        final JCheckBox enabled = new JCheckBox("Enabled");
        enabled.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                for (int i = 0; i < _documentPane.getDocumentCount(); i++) {
                    String name = _documentPane.getDocumentNameAt(i);
                    DocumentComponent doc = _documentPane.getDocument(name);
                    if (doc instanceof CodeEditorDocumentComponent) {
                        ((CodeEditorDocumentComponent) doc).getCodeEditor().setEnabled(enabled.isSelected());
                    }
                }
            }
        });
        enabled.setSelected(true);

        final JCheckBox virtualSpace = new JCheckBox("Allow Virtual Space");
        virtualSpace.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                DefaultSettings.getDefaults().setVirtualSpaceAllowed(virtualSpace.isSelected());
            }
        });
        virtualSpace.setSelected(DefaultSettings.getDefaults().isVirtualSpaceAllowed());

        final JCheckBox columnGuide = new JCheckBox("Show Column Guide");
        columnGuide.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                for (int i = 0; i < _documentPane.getDocumentCount(); i++) {
                    String name = _documentPane.getDocumentNameAt(i);
                    DocumentComponent doc = _documentPane.getDocument(name);
                    if (doc instanceof CodeEditorDocumentComponent) {
                        if (columnGuide.isSelected()) {
                            ColumnGuide guide = new ColumnGuide(80, ColumnGuide.UNIT_IN_COLUMNS);
                            ((CodeEditorDocumentComponent) doc).getCodeEditor().addColumnGuide(guide);
                        }
                        else {
                            ((CodeEditorDocumentComponent) doc).getCodeEditor().removeAllColumnGuides();
                        }
                    }
                }
            }
        });
        columnGuide.setSelected(false);

        final JCheckBox editable = new JCheckBox("Editable");
        editable.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                for (int i = 0; i < _documentPane.getDocumentCount(); i++) {
                    String name = _documentPane.getDocumentNameAt(i);
                    DocumentComponent doc = _documentPane.getDocument(name);
                    if (doc instanceof CodeEditorDocumentComponent) {
                        ((CodeEditorDocumentComponent) doc).getCodeEditor().setEditable(editable.isSelected());
                    }
                }
            }
        });
        editable.setSelected(true);

        final JButton style = new JButton("Change Style");
        style.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Window window = JideSwingUtilities.getFrame(style);
                StandardDialog dialog = new StandardDialog((Frame) window, "Change Style") {
                    private StyleListPanel _stylesPanel;
                    private FontPanel _fontPanel;

                    @Override
                    public JComponent createContentPanel() {
                        _stylesPanel = new StyleListPanel();
                        _stylesPanel.setNames(new String[]{"Normal", "Comment1", "Comment2", "Keywords1", "Keywords2", "Keywords3", "Literal1", "Literal2", "Label", "Operator", "Invalid", "Error", "Warning", "Folded Text"});
                        _stylesPanel.setStyles(DefaultSettings.getDefaults().getStyles());
                        _stylesPanel.loadData();

                        _fontPanel = new FontPanel();
                        Font font = DefaultSettings.getDefaults().getFont();
                        _fontPanel.setFontName(font.getName());
                        _fontPanel.setFontSize(font.getSize());
                        _fontPanel.loadData();

                        JPanel panel = new JPanel(new BorderLayout(6, 6));
                        panel.add(_fontPanel, BorderLayout.BEFORE_FIRST_LINE);
                        panel.add(_stylesPanel, BorderLayout.CENTER);
                        return panel;
                    }

                    @Override
                    public ButtonPanel createButtonPanel() {
                        ButtonPanel buttonPanel = new ButtonPanel();
                        JButton okButton = new JButton();
                        JButton cancelButton = new JButton();
                        okButton.setName(OK);
                        cancelButton.setName(CANCEL);
                        buttonPanel.addButton(okButton, ButtonPanel.AFFIRMATIVE_BUTTON);
                        buttonPanel.addButton(cancelButton, ButtonPanel.CANCEL_BUTTON);

                        okButton.setAction(new AbstractAction(UIDefaultsLookup.getString("OptionPane.okButtonText")) {
                            public void actionPerformed(ActionEvent e) {
                                setDialogResult(RESULT_AFFIRMED);
                                _stylesPanel.saveData();
                                DefaultSettings.getDefaults().setStyles(_stylesPanel.getStyles());

                                _fontPanel.saveData();
                                DefaultSettings.getDefaults().setFont(new Font(_fontPanel.getFontName(), Font.PLAIN, _fontPanel.getFontSize()));

                                setVisible(false);
                                dispose();
                            }
                        });
                        cancelButton.setAction(new AbstractAction(UIDefaultsLookup.getString("OptionPane.cancelButtonText")) {
                            public void actionPerformed(ActionEvent e) {
                                setDialogResult(RESULT_CANCELLED);
                                setVisible(false);
                                dispose();
                            }
                        });

                        setDefaultCancelAction(cancelButton.getAction());
                        setDefaultAction(okButton.getAction());
                        getRootPane().setDefaultButton(okButton);
                        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                        return buttonPanel;
                    }


                    @Override
                    public JComponent createBannerPanel() {
                        return null;
                    }
                };
                dialog.setLocationRelativeTo(_documentPane);
                dialog.pack();
                dialog.setVisible(true);
            }
        });

        final JButton shortcut = new JButton("Change Shortcut Keys");
        shortcut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DefaultInputHandler inputHandler = (DefaultInputHandler) DefaultSettings.getDefaults().getInputHandler();
                final ShortcutEditor shortcutEditor = new ShortcutEditor(inputHandler.getShortcutSchemaManager(), true);
                Window window = JideSwingUtilities.getFrame(style);
                StandardDialog dialog = new StandardDialog((Frame) window, "Change Shortcut Keys") {
                    @Override
                    public JComponent createContentPanel() {
                        return shortcutEditor;
                    }

                    @Override
                    public ButtonPanel createButtonPanel() {
                        ButtonPanel buttonPanel = new ButtonPanel();
                        JButton okButton = new JButton();
                        JButton cancelButton = new JButton();
                        okButton.setName(OK);
                        cancelButton.setName(CANCEL);
                        buttonPanel.addButton(okButton, ButtonPanel.AFFIRMATIVE_BUTTON);
                        buttonPanel.addButton(cancelButton, ButtonPanel.CANCEL_BUTTON);

                        okButton.setAction(new AbstractAction(UIDefaultsLookup.getString("OptionPane.okButtonText")) {
                            public void actionPerformed(ActionEvent e) {
                                setDialogResult(RESULT_AFFIRMED);
                                setVisible(false);
                                dispose();
                            }
                        });
                        cancelButton.setAction(new AbstractAction(UIDefaultsLookup.getString("OptionPane.cancelButtonText")) {
                            public void actionPerformed(ActionEvent e) {
                                setDialogResult(RESULT_CANCELLED);
                                setVisible(false);
                                dispose();
                            }
                        });

                        setDefaultCancelAction(cancelButton.getAction());
                        setDefaultAction(okButton.getAction());
                        getRootPane().setDefaultButton(okButton);
                        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                        return buttonPanel;
                    }


                    @Override
                    public JComponent createBannerPanel() {
                        return null;
                    }
                };
                dialog.setLocationRelativeTo(_documentPane);
                dialog.pack();
                dialog.setVisible(true);
            }
        });

        final JButton findAll = new JButton("Find All");
        findAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Window window = JideSwingUtilities.getWindowForComponent(findAll);
                FindAndReplaceAllDialog dialog;
                FindAndReplace findAndReplace = new FindAndReplace();
                findAndReplace.addTarget(new CodeEditorDocumentPaneFindAndReplaceTarget(_documentPane));
                findAndReplace.addFindAndReplaceListener(new FindAndReplaceListener() {
                    public void statusChanged(FindAndReplaceEvent e) {
                        // System.out.println(e);
                    }
                });
                if (window instanceof Frame) {
                    dialog = new FindAndReplaceAllDialog((Frame) window, "Find All", findAndReplace);
                }
                else {
                    dialog = new FindAndReplaceAllDialog((Dialog) window, "Find All", findAndReplace);
                }
                dialog.pack();
                dialog.setLocationRelativeTo(_documentPane);
                dialog.setVisible(true);
            }
        });

        final JButton replaceAll = new JButton("Replace All");
        replaceAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Window window = JideSwingUtilities.getWindowForComponent(replaceAll);
                FindAndReplaceAllDialog dialog;
                FindAndReplace findAndReplace = new FindAndReplace();
                findAndReplace.setReplace(true);
                findAndReplace.addTarget(new CodeEditorDocumentPaneFindAndReplaceTarget(_documentPane));
                if (window instanceof Frame) {
                    dialog = new FindAndReplaceAllDialog((Frame) window, "Replace All", findAndReplace);
                }
                else {
                    dialog = new FindAndReplaceAllDialog((Dialog) window, "Replace All", findAndReplace);
                }
                dialog.pack();
                dialog.setLocationRelativeTo(_documentPane);
                dialog.setVisible(true);
            }
        });

        panel.add(specialChars);
        panel.add(lineNumber);
        panel.add(virtualSpace);
        panel.add(columnGuide);
        panel.add(editable);
        panel.add(enabled);
        panel.add(style);
        panel.add(shortcut);
        panel.add(findAll);
        panel.add(replaceAll);
        return panel;
    }

    public Component getDemoPanel() {
        _documentPane = new CodeEditorDocumentPane();

        CodeEditorDocumentComponent javaDocument = createDocumentComponent("CodeEditorDemo.java", "Java");
        final CodeEditor editorForJava = javaDocument.getCodeEditor();
        editorForJava.getFindAndReplace().addFindAndReplaceListener(new FindAndReplaceListener() {
            public void statusChanged(FindAndReplaceEvent e) {
                if (e.getStatus() == FindAndReplaceEvent.SEARCH_FINISHED && e.getFindResults() != null) {
                    FindResults results = e.getFindResults();
                    JDialog dialog = new JDialog((Frame) editorForJava.getTopLevelAncestor(), "Find Results");
                    FindResultTree view = new FindResultTree();
                    view.addSearchResult(results);
                    dialog.add(new JScrollPane(view));
                    dialog.pack();
                    dialog.setLocationRelativeTo(editorForJava);
                    dialog.setVisible(true);
                }
            }
        });
        configureCodeEditorForJava(editorForJava);
        _documentPane.openDocument(javaDocument);

        CodeEditorDocumentComponent verilogDocument = createDocumentComponent("sample.v", "Verilog");
        configureCodeEditorForVerilog(verilogDocument.getCodeEditor());
        _documentPane.openDocument(verilogDocument);

        CodeEditorDocumentComponent vhdlDocument = createDocumentComponent("sample.vhdl", "VHDL");
        configureCodeEditorForVhdl(vhdlDocument.getCodeEditor());
        _documentPane.openDocument(vhdlDocument);

        CodeEditorDocumentComponent sqlDocument = createDocumentComponent("sample.plsql", "PLSQL");
        _documentPane.openDocument(sqlDocument);

        CodeEditorDocumentComponent phpDocument = createDocumentComponent("sample.php", "PHP");
        _documentPane.openDocument(phpDocument);

        CodeEditorDocumentComponent perlDocument = createDocumentComponent("sample.pl", "Perl");
        _documentPane.openDocument(perlDocument);

        CodeEditorDocumentComponent pythonDocument = createDocumentComponent("sample.py", "Python");
        _documentPane.openDocument(pythonDocument);

        CodeEditorDocumentComponent htmlDocument = createDocumentComponent("sample.html", "HTML");
        _documentPane.openDocument(htmlDocument);

        CodeEditorDocumentComponent xmlDocument = createDocumentComponent("sample.xml", "XML");
        _documentPane.openDocument(xmlDocument);

        _documentPane.setPreferredSize(new Dimension(700, 500));

        return _documentPane;
    }

    private DocumentComponent createDocumentComponent(JComponent component, String name) {
        DocumentComponent documentComponent = new DocumentComponent(component, name);
// uncomment this for a shared status bar
//        documentComponent.addDocumentComponentListener(new DocumentComponentAdapter() {
//            public void documentComponentActivated(DocumentComponentEvent e) {
//                JComponent component = e.getDocumentComponent().getComponent();
//                if (component instanceof JPanel && ((JPanel) component).getComponent(0) instanceof CodeEditor) {
//                    _sharedStatusBar.setCodeEditor((CodeEditor) ((JPanel) component).getComponent(0));
//                }
//            }
//        });
        return documentComponent;
    }

    private CodeEditorDocumentComponent createDocumentComponent(String resourceName, String languageName) {
        CodeEditorDocumentComponent documentComponent = new CodeEditorDocumentComponent(resourceName);
        try {
            documentComponent.open(CodeEditorDocumentPaneDemo.class.getClassLoader(), resourceName);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        documentComponent.setLanguageName(languageName);
        return documentComponent;
    }

    private CodeEditorDocumentComponent createDocumentComponent(String title, String languageName, SyntaxDocument document) {
        CodeEditorDocumentComponent documentComponent = new CodeEditorDocumentComponent(title);
        documentComponent.setLanguageName(languageName);
        documentComponent.open(document);
        return documentComponent;
    }

    public void configureCodeEditorForVerilog(CodeEditor editor) {
        editor.getMarginArea().addMarginComponent(new CodeFoldingMargin());
    }

    public final Icon BOOKMARK0 = IconsFactory.getImageIcon(CodeEditorDocumentPaneDemo.class, "icons/bookmark0.png");
    public final Icon BOOKMARK1 = IconsFactory.getImageIcon(CodeEditorDocumentPaneDemo.class, "icons/bookmark1.png");

    public void configureCodeEditorForVhdl(CodeEditor editor) {
        editor.getMarginArea().addMarginComponent(new AbstractLineMargin(editor) {
            @Override
            public void paintLineMargin(Graphics g, Rectangle rect, int line) {
                g.setColor(Color.WHITE);
                g.drawLine(rect.x, rect.y, rect.x, rect.y + rect.height - 1);
                if (line == 13) {
                    BOOKMARK0.paintIcon(this, g, 1 + rect.x + rect.width / 2 - BOOKMARK0.getIconWidth() / 2, rect.y + (rect.height - BOOKMARK0.getIconHeight()) / 2);
                }
                else if (line == 23) {
                    BOOKMARK1.paintIcon(this, g, 1 + rect.x + rect.width / 2 - BOOKMARK1.getIconWidth() / 2, rect.y + (rect.height - BOOKMARK1.getIconHeight()) / 2);
                }
            }

            @Override
            public String getToolTipText(int line) {
                if (line == 13) {
                    return "Bookmark 1";
                }
                else if (line == 23) {
                    return "Bookmark 2";
                }
                else {
                    return null;
                }
            }

            public int getPreferredWidth() {
                return BOOKMARK0.getIconWidth() + 5;
            }
        });
        editor.getMarginArea().addMarginComponent(new CodeFoldingMargin());
    }

    public final Icon DB_VALID = IconsFactory.getImageIcon(CodeEditorDocumentPaneDemo.class, "icons/db_verified_breakpoint.png");
    public final Icon DB_INVALID = IconsFactory.getImageIcon(CodeEditorDocumentPaneDemo.class, "icons/db_invalid_breakpoint.png");

    public void configureCodeEditorForJava(CodeEditor editor) {
//        editor.getMarginArea().addMarginComponent(new LineOffsetMargin(editor), 0);
        AbstractLineMargin breakpointMargin = new AbstractLineMargin(editor) {
            @Override
            public void paintLineMargin(Graphics g, Rectangle rect, int line) {
                g.setColor(Color.WHITE);
                g.drawLine(rect.x, rect.y, rect.x, rect.y + rect.height - 1);
                if (line == 70) {
                    DB_VALID.paintIcon(this, g, 1 + rect.x + rect.width / 2 - DB_VALID.getIconWidth() / 2, rect.y + (rect.height - DB_VALID.getIconHeight()) / 2);
                }
                else if (line == 49) {
                    DB_INVALID.paintIcon(this, g, 1 + rect.x + rect.width / 2 - DB_INVALID.getIconWidth() / 2, rect.y + (rect.height - DB_INVALID.getIconHeight()) / 2);
                }
            }

            @Override
            public String getToolTipText(int line) {
                if (line == 70) {
                    return "Line 52 of Sample.java";
                }
                else if (line == 50) {
                    return "Line 49 of Sample.java";
                }
                else {
                    return null;
                }
            }

            public int getPreferredWidth() {
                return DB_INVALID.getIconWidth() + 5;
            }
        };
//        breakpointMargin.setOpaque(true);
//        breakpointMargin.setBackground(new Color(236, 233, 216));
        editor.getMarginArea().addMarginComponent(0, breakpointMargin);


        CodeFoldingMargin margin = new CodeFoldingMargin();
        margin.setBackground(null);
        margin.setCodeFoldingPainter(new DefaultCodeFoldingPainter());
        margin.addMarginPainter(new BraceMatchingMarginPainter());
        editor.getMarginArea().addMarginComponent(margin);
//        editor.getMarginArea().setBackground(Color.WHITE);
//        editor.getLineNumberMargin().setForeground(Color.GRAY);
        editor.getMarginArea().setBackground(new Color(233, 232, 226));
        editor.getLineNumberMargin().setForeground(Color.BLACK);

//        editor.getFoldingModel().setAdjusting(true);
//        editor.getFoldingModel().addFoldingSpan(120, 385, "...");
//        editor.getFoldingModel().addFoldingSpan(1052, 1540, "{...}");
//        editor.getFoldingModel().setAdjusting(false);

//        _editorForJava.getCaretModel().addCaretListener(new CaretListener(){
//            public void caretUpdated(CaretEvent e) {
//                System.out.println(e);
//            }
//        });
//        _editorForJava.getSelectionModel().addSelectionListener(new TextSelectionListener(){
//            public void selectionChanged(TextSelectionEvent e) {
//                System.out.println(e);
//            }
//        });
    }

    @Override
    public String getDemoFolder() {
        return "E1.CodeEditor";
    }

    static public void main(String[] s) {
        LookAndFeelFactory.installDefaultLookAndFeel();
        showAsFrame(new CodeEditorDocumentPaneDemo());
    }
}
