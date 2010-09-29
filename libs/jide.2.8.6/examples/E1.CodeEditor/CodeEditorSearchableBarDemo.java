/*
 * @(#)CodeEditorSearchableBarDemo.java 7/15/2008
 *
 * Copyright 2002 - 2008 JIDE Software Inc. All rights reserved.
 *
 */

import com.jidesoft.editor.CodeEditor;
import com.jidesoft.editor.CodeEditorSearchable;
import com.jidesoft.editor.tokenmarker.JavaTokenMarker;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.Searchable;
import com.jidesoft.swing.SearchableBar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Demoed Component: {@link com.jidesoft.swing.SearchableBar} <br> Required jar files: jide-common.jar,
 * jide-components.jar <br> Required L&F: Jide L&F extension required
 */
public class CodeEditorSearchableBarDemo extends AbstractDemo {
    public SearchableBar _codeEditorSearchableBar;

    public CodeEditorSearchableBarDemo() {
    }

    public String getName() {
        return "SearchableBar Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_COMMON;
    }

    @Override
    public String getDescription() {
        return "This is a demo to show SearchableBar working with CodeEditor to achieve the searching feature. \n" +
                "1. Press any a specified key stroke to start the search. In the demo, we use CTRL-SHIFT-F to start searching but it could be customized to any key stroke.\n" +
                "2. Press up/down arrow key to navigation to next or previous matching occurrence\n" +
                "3. Press Highlights button to select all matching occurrences\n" +
                "4. A lot of customization options using the API\n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.swing.SearchableBar";
    }

    @Override
    public Component getOptionsPanel() {
        JPanel switchPanel = new JPanel(new GridLayout(0, 1, 3, 3));

        final JRadioButton style1 = new JRadioButton("Full");
        final JRadioButton style2 = new JRadioButton("Compact");

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(style1);
        buttonGroup.add(style2);

        switchPanel.add(new JLabel("Styles:"));
        switchPanel.add(style1);
        switchPanel.add(style2);
        style1.setSelected(true);

        style1.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (style1.isSelected()) {
                    _codeEditorSearchableBar.setCompact(false);
                }
            }
        });
        style2.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (style2.isSelected()) {
                    _codeEditorSearchableBar.setCompact(true);
                }
            }
        });

        switchPanel.add(new JLabel("Options: "));

        final JCheckBox option1 = new JCheckBox("Show close button");
        final JCheckBox option2 = new JCheckBox("Show navigation buttons");
        final JCheckBox option3 = new JCheckBox("Show highlights button");
        final JCheckBox option4 = new JCheckBox("Show match case check box");
        final JCheckBox option5 = new JCheckBox("Show repeats check box");
        final JCheckBox option6 = new JCheckBox("Show status");

        switchPanel.add(option1);
        switchPanel.add(option2);
        switchPanel.add(option3);
        switchPanel.add(option4);
        switchPanel.add(option5);
        switchPanel.add(option6);

        option1.setSelected(true);
        option1.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                updateButtons(option1.isSelected(), SearchableBar.SHOW_CLOSE);
            }
        });

        option2.setSelected(true);
        option2.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                updateButtons(option2.isSelected(), SearchableBar.SHOW_NAVIGATION);
            }
        });

        option3.setSelected(true);
        option3.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                updateButtons(option3.isSelected(), SearchableBar.SHOW_HIGHLIGHTS);
            }
        });

        option4.setSelected(true);
        option4.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                updateButtons(option4.isSelected(), SearchableBar.SHOW_MATCHCASE);
            }
        });

        option5.setSelected(false);
        option5.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                updateButtons(option5.isSelected(), SearchableBar.SHOW_REPEATS);
            }
        });

        option6.setSelected(true);
        option6.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                updateButtons(option6.isSelected(), SearchableBar.SHOW_STATUS);
            }
        });

        return switchPanel;
    }

    private void updateButtons(boolean selected, int bit) {
        if (selected) {
            _codeEditorSearchableBar.setVisibleButtons(_codeEditorSearchableBar.getVisibleButtons() | bit);
        }
        else {
            _codeEditorSearchableBar.setVisibleButtons(_codeEditorSearchableBar.getVisibleButtons() & ~bit);
        }
    }

    public Component getDemoPanel() {
        return createCodeEditor();
    }

    private JPanel createCodeEditor() {
        CodeEditor editor = new CodeEditor();
        try {
            StringBuffer buf = readInputStream(CodeEditorDemo.class.getClassLoader().getResourceAsStream("CodeEditorDemo.java"));
            editor.setTokenMarker(new JavaTokenMarker());
            editor.setText(buf.toString());
            editor.setPreferredSize(new Dimension(600, 500));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        final JPanel panel = new JPanel(new BorderLayout());
        panel.add(editor, BorderLayout.CENTER);
        Searchable searchable = new CodeEditorSearchable(editor);
        searchable.setRepeats(true);
        _codeEditorSearchableBar = SearchableBar.install(searchable, KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK), new SearchableBar.Installer() {
            public void openSearchBar(SearchableBar searchableBar) {
                panel.add(searchableBar, BorderLayout.AFTER_LAST_LINE);
                panel.invalidate();
                panel.revalidate();
            }

            public void closeSearchBar(SearchableBar searchableBar) {
                panel.remove(searchableBar);
                panel.invalidate();
                panel.revalidate();
            }
        });
        panel.add(_codeEditorSearchableBar, BorderLayout.AFTER_LAST_LINE);
        return panel;
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

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        AbstractDemo.showAsFrame(new CodeEditorSearchableBarDemo());
    }
}