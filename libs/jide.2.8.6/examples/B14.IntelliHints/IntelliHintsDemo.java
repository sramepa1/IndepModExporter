/*
 * @(#)IntelliHintsDemo.java 7/24/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.grid.SortableTable;
import com.jidesoft.grid.TextFieldCellEditor;
import com.jidesoft.hints.AbstractListIntelliHints;
import com.jidesoft.hints.FileIntelliHints;
import com.jidesoft.hints.ListDataIntelliHints;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.JideBoxLayout;
import com.jidesoft.swing.SelectAllUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.zip.GZIPInputStream;

/**
 * Demoed Component: {@link com.jidesoft.hints.IntelliHints}. <br> Required jar files: jide-common.jar, jide-grids.jar
 * <br> Required L&F: Jide L&F extension required
 */
public class IntelliHintsDemo extends AbstractDemo {
    private static final long serialVersionUID = 4729636896685844732L;
    private boolean _applyFileFilter;

    public IntelliHintsDemo() {
    }

    public String getName() {
        return "IntelliHints Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_COMMON;
    }

    @Override
    public String getDescription() {
        return "This is a demo of IntelliHints components. IntelliHints can display a hint popup in a text field or text area " +
                "so that user can pick a hint directly while typing.\n" +
                "\nYou can start to type in those text fields or text area to see how it works. " +
                "At any time, if you want to see whether there are hints available, you can press DOWN key " +
                "in text field or CTRL+SPACE in text area.\n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.hints.IntelliHints\n" +
                "com.jidesoft.hints.AbstractIntelliHints\n" +
                "com.jidesoft.hints.AbstractListIntelliHints\n" +
                "com.jidesoft.hints.FileIntelliHints\n" +
                "com.jidesoft.hints.ListDataIntelliHints";
    }

    protected List<String> readUrls() throws IOException {
        InputStream resource = this.getClass().getClassLoader().getResourceAsStream("url.txt.gz");
        if (resource == null) {
            return null;
        }

        InputStream in = new GZIPInputStream(resource);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));

        List<String> list = new ArrayList<String>();
        while (true) {
            String line = reader.readLine(); // skip first line
            if (line == null || line.length() == 0) {
                break;
            }
            list.add(line);
        }
        return list;
    }

    @Override
    public Component getOptionsPanel() {
        JPanel panel = new JPanel(new GridLayout());
        final JCheckBox applyFileFilter = new JCheckBox("Show \"Program\" Folders/Files Only");
        applyFileFilter.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _applyFileFilter = applyFileFilter.isSelected();
            }
        });
        panel.add(applyFileFilter);
        return panel;
    }

    public Component getDemoPanel() {
        final String[] fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        // create file text field
        List<String> urls = null;
        try {
            urls = readUrls();
        }
        catch (IOException e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }

        JTextField urlTextField = new JTextField("http://");
        urlTextField.setName("URL IntelliHint");
        SelectAllUtils.install(urlTextField);
        ListDataIntelliHints intelliHints = new ListDataIntelliHints<String>(urlTextField, urls);
        intelliHints.setCaseSensitive(false);

        JTextField pathTextField = new JTextField();
        SelectAllUtils.install(pathTextField);
        FileIntelliHints fileIntelliHints = new FileIntelliHints(pathTextField);
        fileIntelliHints.setFilter(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return !_applyFileFilter || dir.getAbsolutePath().contains("Program") || name.contains("Program");
            }
        });
        fileIntelliHints.setFolderOnly(false);
        fileIntelliHints.setFollowCaret(true);
        fileIntelliHints.setShowFullPath(false);

        // create file text field
        JTextField fileTextField = new JTextField();
        fileTextField.setName("File IntelliHint");
        SelectAllUtils.install(fileTextField);
        new FileIntelliHints(fileTextField);

        // create file text field
        JTextArea fileTextArea = new JTextArea();
        new FileIntelliHints(fileTextArea);
        fileTextArea.setRows(4);

        // create file text field
        JTextField fontTextField = new JTextField();
        fontTextField.setName("Font IntelliHint");
        SelectAllUtils.install(fontTextField);
        ListDataIntelliHints fontIntelliHints = new ListDataIntelliHints<String>(fontTextField, fontNames);
        fontIntelliHints.setCaseSensitive(false);

        JTextField textField = new JTextField();
        SelectAllUtils.install(textField);
        //noinspection UnusedDeclaration
        new AbstractListIntelliHints(textField) {
            protected JLabel _messageLabel;

            @Override
            public JComponent createHintsComponent() {
                JPanel panel = (JPanel) super.createHintsComponent();
                _messageLabel = new JLabel();
                panel.add(_messageLabel, BorderLayout.BEFORE_FIRST_LINE);
                return panel;
            }

            // update list model depending on the data in textfield
            public boolean updateHints(Object value) {
                if (value == null) {
                    return false;
                }
                String s = value.toString();
                s = s.trim();
                if (s.length() == 0) {
                    return false;
                }
                try {
                    long l = Long.parseLong(s);
                    boolean prime = isProbablePrime(l);
                    _messageLabel.setText("");
                    if (prime) {
                        return false;
                    }
                    else {
                        Vector<Long> list = new Vector<Long>();
                        long nextPrime = l;
                        for (int i = 0; i < 10; i++) {
                            nextPrime = nextPrime(nextPrime);
                            list.add(nextPrime);
                        }
                        setListData(list);
                        _messageLabel.setText("Next 10 prime numbers:");
                        _messageLabel.setForeground(Color.DARK_GRAY);
                        return true;
                    }
                }
                catch (NumberFormatException e) {
                    setListData(new Object[0]);
                    _messageLabel.setText("Invalid long number");
                    setListData(new Object[0]);
                    _messageLabel.setForeground(Color.RED);
                    return true;
                }
            }
        };

        DefaultTableModel model = new DefaultTableModel(0, 1) {
            private static final long serialVersionUID = -2794741068912785630L;

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return String.class;
            }

            @Override
            public String getColumnName(int column) {
                return "Font";
            }
        };
        model.addRow(new Object[]{"Arial"});
        model.addRow(new Object[]{"Tahoma"});
        SortableTable table = new SortableTable(model);
        table.getColumnModel().getColumn(0).setCellEditor(new TextFieldCellEditor(String.class) {
            private static final long serialVersionUID = 2023654568542192380L;

            @Override
            protected JTextField createTextField() {
                JTextField cellEditorTextField = new JTextField();
                ListDataIntelliHints fontIntellihints = new ListDataIntelliHints<String>(cellEditorTextField, fontNames);
                fontIntellihints.setCaseSensitive(false);
                return cellEditorTextField;
            }
        });
        table.setPreferredScrollableViewportSize(new Dimension(100, 100));

        JPanel panel = new JPanel();
        panel.setLayout(new JideBoxLayout(panel, JideBoxLayout.Y_AXIS, 3));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("ListDataIntelliHints TextField for URLs: "));
        panel.add(urlTextField);
        panel.add(Box.createVerticalStrut(6), JideBoxLayout.FIX);

        // create path text field
        panel.add(new JLabel("FileIntelliHints TextField for path (folders only, show partial path):"));
        panel.add(pathTextField);
        panel.add(Box.createVerticalStrut(6), JideBoxLayout.FIX);

        panel.add(new JLabel("FileIntelliHints TextField for file (files and folders, show full path):"));
        panel.add(fileTextField);
        panel.add(Box.createVerticalStrut(6), JideBoxLayout.FIX);

        panel.add(new JLabel("FileIntelliHints TextArea for files (each line is a new file):"));
        panel.add(new JScrollPane(fileTextArea));
        panel.add(Box.createVerticalStrut(6), JideBoxLayout.FIX);

        panel.add(new JLabel("IntelliHints TextField to choose font name:"));
        panel.add(fontTextField);
        panel.add(Box.createVerticalStrut(6), JideBoxLayout.FIX);

        panel.add(new JLabel("A custom IntelliHints for prime numbers: "));
        panel.add(textField);
        panel.add(Box.createVerticalStrut(6), JideBoxLayout.FIX);

        panel.add(new JLabel("Using IntelliHint in JTable's cell editor"));
        panel.add(new JScrollPane(table), JideBoxLayout.FLEXIBLE);

        panel.add(Box.createGlue(), JideBoxLayout.VARY);

        return panel;
    }

    @Override
    public String getDemoFolder() {
        return "B14.IntelliHints";
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new IntelliHintsDemo());
    }

    public static boolean isProbablePrime(long number) {
        return new BigInteger("" + number).isProbablePrime(500);
    }

    public static long nextPrime(long lastPrime) {
        long testPrime;
        testPrime = lastPrime + 1;
        while (!isProbablePrime(testPrime)) testPrime += (testPrime % 2 == 0) ? 1 : 2;

        return testPrime;
    }

}
