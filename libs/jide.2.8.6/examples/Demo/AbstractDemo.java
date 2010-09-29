/*
 * @(#)AbstractDemo.java 8/24/2009
 *
 * Copyright 2002 - 2009 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.dialog.ButtonPanel;
import com.jidesoft.dialog.StandardDialog;
import com.jidesoft.document.DocumentComponent;
import com.jidesoft.document.DocumentPane;
import com.jidesoft.editor.CodeEditor;
import com.jidesoft.editor.SyntaxDocument;
import com.jidesoft.editor.tokenmarker.JavaTokenMarker;
import com.jidesoft.icons.JideIconsFactory;
import com.jidesoft.pane.CollapsiblePane;
import com.jidesoft.pane.CollapsiblePanes;
import com.jidesoft.plaf.UIDefaultsLookup;
import com.jidesoft.swing.*;
import com.jidesoft.utils.ProductNames;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;

/**
 * A template to create additional demo module.
 */
abstract public class AbstractDemo implements Demo, ProductNames {
    public AbstractDemo() {
    }

    public String getDescription() {
        return null;
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    @Override
    public String toString() {
        return getName();
    }

    public Component getOptionsPanel() {
        return null;
    }

    public boolean isCommonOptionsPaneVisible() {
        return true;
    }

    public void dispose() {
    }

    public static JFrame showAsFrame(final Demo demo) {
        final JFrame frame = new JFrame(demo.getName());
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (demo instanceof AnimatedDemo) {
                    ((AnimatedDemo) demo).stopAnimation();
                }
                demo.dispose();
            }
        });
        frame.setIconImage(JideIconsFactory.getImageIcon(JideIconsFactory.JIDE32).getImage());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Component demoPanel = demo.getDemoPanel();
        JComponent pane = createOptionsPanel(frame, demo, demoPanel);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(demoPanel, BorderLayout.CENTER);

        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        if (pane != null) {
            frame.getContentPane().add(new JScrollPane(pane), BorderLayout.BEFORE_LINE_BEGINS);
        }

//        com.jidesoft.swing.JideSwingUtilities.traceFocus();

        frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_G, Event.CTRL_MASK
                        | Event.SHIFT_MASK | Event.ALT_MASK), "printMem");
        frame.getRootPane().getActionMap().put("printMem", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                com.jidesoft.swing.JideSwingUtilities.runGCAndPrintFreeMemory();
            }
        });

        frame.pack();

        if (demo instanceof AnimatedDemo) {
            ((AnimatedDemo) demo).startAnimation();
        }

        JideSwingUtilities.globalCenterWindow(frame);
        frame.setVisible(true);
        frame.toFront();
        return frame;
    }

    protected static JComponent createOptionsPanel(JFrame parentFrame, Demo demo, Component demoPanel) {
        CollapsiblePanes panes = new CollapsiblePanes();

        if (demoPanel != null) {
            demoPanel.setName("Demo.DemoPanel");
            Component optionsPanel = demo.getOptionsPanel();
            if (optionsPanel != null) {
                optionsPanel.setName("Demo.OptionsPanel");
            }

            if (optionsPanel != null) {
                CollapsiblePane pane = createCollapsiblePane("Options");
                pane.setEmphasized(true);
                if (optionsPanel instanceof JComponent) {
                    JComponent optionPanel = (JComponent) optionsPanel;
                    optionPanel.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
                    JideSwingUtilities.setOpaqueRecursively(optionPanel, false);
                    pane.setContentPane(optionPanel);
                }
                panes.add(pane);
            }

            if (demo.isCommonOptionsPaneVisible()) {
                CollapsiblePane pane = createCollapsiblePane("Common Options");
                JComponent commonOptionsPanel = createCommonOptions(demoPanel);
                commonOptionsPanel.setName("Demo.CommonOptionsPanel");
                commonOptionsPanel.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
                pane.setContentPane(commonOptionsPanel);
                JideSwingUtilities.setOpaqueRecursively(pane, false);
                panes.add(pane);
            }

            String description = demo.getDescription();
            if (description != null && description.trim().length() > 0) {
                CollapsiblePane pane = createCollapsiblePane("Description");
                MultilineLabel label = new MultilineLabel(description);
                label.setColumns(30);
                label.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
                pane.setContentPane(label);
                panes.add(pane);
            }

            String[] source = demo.getDemoSource();
            if (source != null && source.length > 0) {
                CollapsiblePane pane = createCollapsiblePane("Source Code");
                JPanel panel = new JPanel(new BorderLayout(4, 4));
                StringBuffer sourceFiles = new StringBuffer();
                sourceFiles.append("Under examples/").append(demo.getDemoFolder());
                for (String s : source) {
                    sourceFiles.append("\n  - ");
                    sourceFiles.append(s);
                }
                MultilineLabel label = new MultilineLabel(sourceFiles.toString());
                label.setColumns(30);
                panel.add(label);
                panel.add(JideSwingUtilities.createLeftPanel(AbstractDemo.createBrowseSourceCodeButton(parentFrame, demo)), BorderLayout.AFTER_LAST_LINE);
                pane.setContentPane(panel);
                panel.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
                JideSwingUtilities.setOpaqueRecursively(panel, false);
                panes.add(pane);
            }

            if (panes.getComponentCount() >= 1) {
                panes.addExpansion();
            }
            else {
                return null;
            }
        }
        return panes;
    }

    private static CollapsiblePane createCollapsiblePane(String title) {
        return new CollapsiblePane(title);
    }

    private static JComponent createCommonOptions(final Component component) {
        JCheckBox toggleLTR = new JCheckBox("Toggle Left-to-Right/Right-to-Left");
        toggleLTR.setSelected(component.getComponentOrientation().isLeftToRight());
        toggleLTR.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                JideSwingUtilities.toggleRTLnLTR(component);
                JideSwingUtilities.invalidateRecursively(component);
            }
        });
        toggleLTR.setMnemonic('T');

        Locale[] locales = Locale.getAvailableLocales();
        Arrays.sort(locales, new Comparator() {
            public int compare(Object o1, Object o2) {
                if (o1 instanceof Locale && o2 instanceof Locale) {
                    Locale l1 = (Locale) o1;
                    Locale l2 = (Locale) o2;
                    return l1.toString().compareTo(l2.toString());
                }
                return 0;
            }
        });
        JComboBox locale = new JComboBox(locales);
        locale.setSelectedItem(Locale.getDefault());
        locale.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED && e.getItem() instanceof Locale) {
                    Locale l = (Locale) e.getItem();
                    JideSwingUtilities.setLocaleRecursively(component, l);
                    SwingUtilities.updateComponentTreeUI(component);
                }
            }
        });
        SearchableUtils.installSearchable(locale);

        JPanel panel = new JPanel();
        panel.setLayout(new JideBoxLayout(panel, JideBoxLayout.Y_AXIS, 3));
        panel.add(toggleLTR);
        JPanel localePanel = new JPanel(new BorderLayout(3, 3));
        JLabel label = new JLabel("Change Locale: ");
        label.setDisplayedMnemonic('C');
        label.setLabelFor(locale);
        localePanel.add(label, BorderLayout.BEFORE_LINE_BEGINS);
        localePanel.add(locale);
        panel.add(localePanel);
        return panel;
    }


    public String[] getDemoSource() {
        return new String[]{getClass().getName() + ".java"};
    }

    public String getDemoFolder() {
        return "";
    }

    public int getAttributes() {
        return ATTRIBUTE_NONE;
    }

    public static JButton createBrowseSourceCodeButton(final JFrame frame, final Demo demo) {
        return new JButton(new AbstractAction("Browse Source Code") {
            public void actionPerformed(ActionEvent e) {
                StandardDialog dialog = new StandardDialog(frame, "Browse Source Code", false) {
                    @Override
                    public JComponent createBannerPanel() {
                        return null;
                    }

                    @Override
                    public JComponent createContentPanel() {
                        JPanel panel = new JPanel(new BorderLayout());
                        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                        panel.add(createSourceCodePanel(demo.getDemoSource()));
                        panel.setPreferredSize(new Dimension(600, 500));
                        return panel;
                    }

                    @Override
                    public ButtonPanel createButtonPanel() {
                        JButton closeButton = new JButton();
                        closeButton.setName(CLOSE);
                        closeButton.setAction(new AbstractAction(UIDefaultsLookup.getString("OptionPane.okButtonText")) {
                            public void actionPerformed(ActionEvent e) {
                                setDialogResult(RESULT_AFFIRMED);
                                setVisible(false);
                                dispose();
                            }
                        });

                        setDefaultCancelAction(closeButton.getAction());
                        return null;
                    }
                };
                dialog.pack();
                dialog.setLocationRelativeTo(frame);
                dialog.setVisible(true);
            }
        });
    }

    public static JComponent createSourceCodePanel(String[] sourceCode) {
        DocumentPane pane = new DocumentPane();
        for (String s : sourceCode) {
            pane.openDocument(new DocumentComponent(createTextComponent(s), s));
        }
        pane.setTabbedPaneCustomizer(new DocumentPane.TabbedPaneCustomizer() {
            public void customize(JideTabbedPane tabbedPane) {
                tabbedPane.setTabPlacement(JideTabbedPane.BOTTOM);
            }
        });
        return pane;
    }

//    private static JPanel createSearchableTextArea(final JTextComponent textComponent) {
//        final JPanel panel = new JPanel(new BorderLayout());
//        panel.add(new JScrollPane(textComponent), BorderLayout.CENTER);
//        Searchable searchable = SearchableUtils.installSearchable(textComponent);
//        searchable.setRepeats(true);
//        SearchableBar searchableBar = SearchableBar.install(searchable, KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_DOWN_MASK), new SearchableBar.Installer() {
//            public void openSearchBar(SearchableBar searchableBar) {
//                panel.add(searchableBar, BorderLayout.AFTER_LAST_LINE);
//                panel.invalidate();
//                panel.revalidate();
//            }
//
//            public void closeSearchBar(SearchableBar searchableBar) {
//                panel.remove(searchableBar);
//                panel.invalidate();
//                panel.revalidate();
//            }
//        });
//        return panel;
//    }

    public static JComponent createTextComponent(String fileName) {
        CodeEditor area = new CodeEditor();
//        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        SyntaxDocument doc = new SyntaxDocument();
        try {
            // try to start reading
            InputStream in = AbstractDemo.class.getResourceAsStream(fileName);
            if (in != null) {
                byte[] buff = new byte[4096];
                int nch;
                while ((nch = in.read(buff, 0, buff.length)) != -1) {
                    doc.insertString(doc.getLength(), new String(buff, 0, nch), null);
                }
                area.setDocument(doc);
            }
            else {
                area.setText("Please make sure the source code files are in the same location as classes.");
            }
        }
        catch (Exception e) {
            // ignore
        }
        area.setTokenMarker(new JavaTokenMarker());
        return area;
    }
}


