/*
 * @(#)ShortcutEditorDemo.java 7/10/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.dialog.ButtonPanel;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.shortcut.*;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

/**
 * Demoed Component: {@link com.jidesoft.shortcut.ShortcutEditor} <br> Required jar files: jide-common.jar,
 * jide-grids.jar <br> Required L&F: any L&F
 */
public class ShortcutEditorDemo extends AbstractDemo {
    public static final String PROFILE_NAME = "jidesoft-shortcutDemo";

    protected ShortcutSchemaManager _manager;
    private ShortcutEditor _shortcutEditor;
    private JPanel _container;
    public JTextArea _textArea;

    public ShortcutEditorDemo() {
    }

    public String getName() {
        return "ShortcutEditor Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_SHORTCUT;
    }

    public String getDemoFolder() {
        return "S1.ShortcutEditor";
    }

    @Override
    public String getDescription() {
        return "This is a demo of ShortcutEditor. \n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.shortcut.ShortcutEditor";
    }

    @Override
    public Component getOptionsPanel() {
        ButtonPanel buttonPanel = new ButtonPanel(SwingConstants.TOP);
        buttonPanel.add(new JButton(new AbstractAction("Open ...") {
            @SuppressWarnings({"CallToPrintStackTrace"})
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                Preferences prefs = Preferences.userRoot();
                String lastDirectory = prefs.node(PROFILE_NAME).get("lastDirectory", ".");
                chooser.setCurrentDirectory(new File(lastDirectory));
                int result = chooser.showDialog(((JComponent) e.getSource()).getTopLevelAncestor(), "Open");
                if (result == JFileChooser.APPROVE_OPTION) {
                    lastDirectory = chooser.getCurrentDirectory().getAbsolutePath();
                    prefs.node(PROFILE_NAME).put("lastDirectory", lastDirectory);
                    try {
                        ShortcutPersistenceUtils.load(_manager, chooser.getSelectedFile().getAbsolutePath());
                        _container.remove(_shortcutEditor);
                        _shortcutEditor = new ShortcutEditor(_manager);
                        _container.add(_shortcutEditor);

                        // not sure why it doesn't update unless the whole frame changes size.
                        JFrame frame = ((JFrame) _container.getTopLevelAncestor());
                        Rectangle bounds = frame.getBounds();
                        bounds.width++;
                        frame.setBounds(bounds);
                    }
                    catch (ParserConfigurationException e1) {
                        e1.printStackTrace();
                    }
                    catch (SAXException e1) {
                        e1.printStackTrace();
                    }
                    catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }));
        buttonPanel.add(new JButton(new AbstractAction("Save as ...") {
            @SuppressWarnings({"CallToPrintStackTrace"})
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                Preferences prefs = Preferences.userRoot();
                String lastDirectory = prefs.node("ShortcutEditorDemo").get("lastDirectory", ".");
                chooser.setCurrentDirectory(new File(lastDirectory));
                int result = chooser.showDialog(((JComponent) e.getSource()).getTopLevelAncestor(), "Save");
                if (result == JFileChooser.APPROVE_OPTION) {
                    lastDirectory = chooser.getCurrentDirectory().getAbsolutePath();
                    prefs.node("ShortcutEditorDemo").put("lastDirectory", lastDirectory);
                }
                else {
                    return;
                }
                try {
                    ShortcutPersistenceUtils.save(_manager, chooser.getSelectedFile().getAbsolutePath());
                }
                catch (ParserConfigurationException e1) {
                    e1.printStackTrace();
                }
                catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }));

        JPanel eventPanel = new JPanel(new BorderLayout(3, 3));
        JLabel eventsLabel = new JLabel("Events:");
        eventsLabel.setDisplayedMnemonic('E');
        eventPanel.add(eventsLabel, BorderLayout.BEFORE_FIRST_LINE);
        _textArea = new JTextArea();
        _textArea.setRows(10);
        _textArea.setColumns(30);
        eventsLabel.setLabelFor(_textArea);
        eventPanel.add(new JScrollPane(_textArea));

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.add(buttonPanel, BorderLayout.BEFORE_FIRST_LINE);
        panel.add(eventPanel);
        return panel;
    }

    @SuppressWarnings({"CallToPrintStackTrace"})
    public Component getDemoPanel() {
        ShortcutSchemaManager manager = new ShortcutSchemaManager();
        try {
            ShortcutPersistenceUtils.load(manager, this.getClass().getClassLoader().getResourceAsStream("DefaultKeymap.xml"));
        }
        catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        catch (SAXException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        manager.getShortcutSchema("$default").setEditable(false);

        _manager = new ShortcutSchemaManager(manager);
        _shortcutEditor = new ShortcutEditor(_manager, new String[]{"Global", "Code Editor", "Graph Editor"}, true);

        final ShortcutListener l = new ShortcutListener() {
            public void shortcutChanged(ShortcutEvent e) {
                output("" + e);
            }
        };
        ShortcutSchema[] schemas = _manager.getShortcutSchemas();
        for (ShortcutSchema schema : schemas) {
            schema.addShortcutListener(l);
        }
        _manager.addShortcutSchemaListener(new ShortcutSchemaListener() {
            public void shortcutSchemaChanged(ShortcutSchemaEvent e) {
                output("" + e);
                if (e.getType() == ShortcutSchemaEvent.SHORTCUT_SCHEMA_ADDED) {
                    _manager.getShortcutSchema(e.getSchemaName()).addShortcutListener(l);
                }
            }
        });

        _container = new JPanel(new BorderLayout());
        _container.add(_shortcutEditor);
        return _container;
    }

    private void output(String s) {
        int start = _textArea.getText().length();
        if (start == 0) {
            _textArea.setText(s);
        }
        else {
            _textArea.setText(_textArea.getText() + "\n" + s);
            start++;
        }
        _textArea.setSelectionStart(start);
        _textArea.setSelectionEnd(start);
    }

    static public void main(String[] s) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new ShortcutEditorDemo());
    }
}
