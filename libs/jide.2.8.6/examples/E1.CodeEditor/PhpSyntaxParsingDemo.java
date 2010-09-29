/*
 * @(#)SyntaxParsingDemo.java 5/5/2007
 *
 * Copyright 2002 - 2007 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.editor.CodeEditor;
import com.jidesoft.editor.CodeInspector;
import com.jidesoft.editor.marker.Marker;
import com.jidesoft.editor.marker.MarkerArea;
import com.jidesoft.editor.marker.MarkerModel;
import com.jidesoft.editor.status.CodeEditorStatusBar;
import com.jidesoft.editor.tokenmarker.PHPTokenMarker;
import com.jidesoft.plaf.LookAndFeelFactory;
import gatchan.phpparser.parser.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Demoed Component: {@link com.jidesoft.editor.CodeEditor} <br> Required jar files:
 * jide-common.jar, jide-editor.jar, jide-components.jar, jide-shortcut.jar, jide-editor.jar <br>
 * Required L&F: any L&F
 */
public class PhpSyntaxParsingDemo extends AbstractDemo {
    public CodeEditor _editor;

    public PhpSyntaxParsingDemo() {
    }

    public String getName() {
        return "PHP Syntax Parser Demo";
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
        return "This is an example to use a grammar parser to detect warnings and errors in the source code and use MarkerArea provided by JIDE Code Editor " +
                "to display those warnings and errors.\n" +
                "Demoed classes:\n" +
                "CodeEditor";
    }

    public Component getDemoPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        try {
            _editor = new CodeEditor();
            StringBuffer buf = readInputStream(PhpSyntaxParsingDemo.class.getClassLoader().getResourceAsStream("sample.php"));
            _editor.setTokenMarker(new PHPTokenMarker());
            _editor.setText(buf.toString());
            _editor.setPreferredSize(new Dimension(600, 500));
            panel.add(_editor, BorderLayout.CENTER);
            panel.add(new CodeEditorStatusBar(_editor), BorderLayout.AFTER_LAST_LINE);
            panel.add(new MarkerArea(_editor), BorderLayout.AFTER_LINE_ENDS);

            _editor.addCodeInspector(new PhpCodeInspector());
            _editor.setAutoInspecting(true);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
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

    static public void main(String[] s) {
        LookAndFeelFactory.installDefaultLookAndFeel();
        showAsFrame(new PhpSyntaxParsingDemo());
    }

    private static class PhpCodeInspector implements CodeInspector {
        public void inspect(final CodeEditor codeEditor, final DocumentEvent evt, final MarkerModel markerModel) {
            PHPParser parser = new PHPParser();
            parser.setPhp5Enabled(true);
            parser.addParserListener(new PHPParserListener() {
                public void parseError(PHPParseErrorEvent e) {
                    markerModel.addMarker(
                            codeEditor.getLineStartOffset(e.getBeginLine() - 1) + e.getBeginColumn() - 1,
                            codeEditor.getLineStartOffset(e.getEndLine() - 1) + e.getEndColumn() - 1,
                            Marker.TYPE_ERROR, e.getMessage());
                    System.out.println("Error: " + e.getMessage() + " " + e.getBeginLine() + "," + e.getBeginColumn() + "->" + e.getEndColumn() + "," + codeEditor.getLineStartOffset(e.getBeginLine() - 1));
                }

                public void parseMessage(PHPParseMessageEvent e) {
                    markerModel.addMarker(
                            codeEditor.getLineStartOffset(e.getBeginLine() - 1) + e.getBeginColumn() - 1,
                            codeEditor.getLineStartOffset(e.getEndLine() - 1) + e.getEndColumn() - 1,
                            Marker.TYPE_WARNING, e.getMessage());
                    System.out.println("Warning: " + e.getMessage() + " " + e.getBeginLine() + "," + e.getBeginColumn() + "->" + e.getEndColumn() + "," + codeEditor.getLineStartOffset(e.getBeginLine() - 1));
                }
            });

            long start = System.currentTimeMillis();
            try {
                markerModel.setAdjusting(true);
                markerModel.clearMarkers();
                parser.parse(codeEditor.getText());
            }
            catch (ParseException e) {
                e.printStackTrace();
            }
            finally {
                Runnable runnable = new Runnable() {
                    public void run() {
                        markerModel.setAdjusting(false);
                    }
                };
                SwingUtilities.invokeLater(runnable);
            }
            System.out.println("Parsing took " + (System.currentTimeMillis() - start) + " ms");
        }
    }
}
