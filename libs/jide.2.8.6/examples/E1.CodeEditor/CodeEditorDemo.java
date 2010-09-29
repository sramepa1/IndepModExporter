/*
 * @(#)CodeEditorDemo.java 5/30/2006
 *
 * Copyright 2002 - 2006 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.editor.CodeEditor;
import com.jidesoft.editor.status.CodeEditorStatusBar;
import com.jidesoft.editor.tokenmarker.JavaTokenMarker;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.Searchable;
import com.jidesoft.swing.SearchableBar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Demoed Component: {@link com.jidesoft.editor.CodeEditor} <br> Required jar files: jide-common.jar, jide-editor.jar,
 * jide-components.jar, jide-shortcut.jar, jide-editor.jar <br> Required L&F: any L&F
 */
public class CodeEditorDemo extends AbstractDemo {
    public CodeEditor _editor;
    private static final long serialVersionUID = 146545943327662356L;

    public CodeEditorDemo() {
    }

    public String getName() {
        return "CodeEditor Demo";
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
        return "This is the simplest example of using CodeEditor. All it does is to show you how to open a CodeEditor" +
                "with Java syntax coloring. There is no other customization in this example. You can check CodeEditorDocumentPane " +
                "and other examples to see more advanced features.\n\n" +
                "Demoed classes:\n" +
                "CodeEditor";
    }

    public Component getDemoPanel() {
        _editor = new CodeEditor();
        try {
            StringBuffer buf = readInputStream(CodeEditorDemo.class.getClassLoader().getResourceAsStream("CodeEditorDemo.java"));
            _editor.setTokenMarker(new JavaTokenMarker());
            _editor.setText(buf.toString());
            _editor.setPreferredSize(new Dimension(600, 500));
            _editor.setHorizontalScrollBarPolicy(ScrollPane.SCROLLBARS_AS_NEEDED);
            _editor.setVerticalScrollBarPolicy(ScrollPane.SCROLLBARS_AS_NEEDED);

            final JPanel panel = new JPanel(new BorderLayout());
            panel.add(_editor);
            final JPanel barPanel = new JPanel(new BorderLayout());
            barPanel.add(new CodeEditorStatusBar(_editor), BorderLayout.CENTER);
            panel.add(barPanel, BorderLayout.AFTER_LAST_LINE);
            Searchable searchable = _editor.getSearchable();
            searchable.setRepeats(true);
            SearchableBar _textAreaSearchableBar = SearchableBar.install(searchable, KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK), new SearchableBar.Installer() {
                public void openSearchBar(SearchableBar searchableBar) {
                    barPanel.add(searchableBar, BorderLayout.AFTER_LAST_LINE);
                    barPanel.invalidate();
                    barPanel.revalidate();
                }

                public void closeSearchBar(SearchableBar searchableBar) {
                    barPanel.remove(searchableBar);
                    barPanel.invalidate();
                    barPanel.revalidate();
                }
            });
            _textAreaSearchableBar.getInstaller().openSearchBar(_textAreaSearchableBar);
            return panel;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static StringBuffer readInputStream(InputStream in) throws IOException {
        Reader reader = new InputStreamReader(in);
        char[] buf = new char[1024];
        StringBuffer buffer = new StringBuffer();
        int read;
        while ((read = reader.read(buf)) != -1) {
            buffer.append(buf, 0, read);
        }
        reader.close();
        return buffer;
    }

    @Override
    public String getDemoFolder() {
        return "E1.CodeEditor";
    }

    @Override
    public void dispose() {
        _editor = null;
    }

    static public void main(String[] s) {
        LookAndFeelFactory.installDefaultLookAndFeel();
        showAsFrame(new CodeEditorDemo());
    }
}
