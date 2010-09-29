/*
 * @(#)DirectoryChooserDemo.java 10/24/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.FolderChooser;
import com.jidesoft.swing.JideSwingUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Demoed Component: {@link com.jidesoft.swing.FolderChooser} <br> Required jar files: jide-common.jar, jide-grids.jar
 * <br> Required L&F: Jide L&F extension required
 */
public class FolderChooserDemo extends AbstractDemo {

    private FolderChooser _folderChooser;
    private List<String> _recentList = new ArrayList<String>();
    private File _currentFolder = null;

    public JTextField _textField;

    public FolderChooserDemo() {
        _folderChooser = new FolderChooser();
    }

    public String getName() {
        return "FolderChooser Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_COMMON;
    }

    public Component getDemoPanel() {
        JPanel panel = new JPanel(new BorderLayout(6, 6));
        _textField = new JTextField(40);
        panel.add(_textField, BorderLayout.CENTER);
        panel.add(createBrowseButton(), BorderLayout.AFTER_LINE_ENDS);
        return JideSwingUtilities.createTopPanel(panel);
    }

    public Component getOptionsPanel() {
        JPanel switchPanel = new JPanel(new GridLayout(7, 1, 3, 3));

        final JCheckBox showNavigationText = new JCheckBox("Show navigation text field");
        showNavigationText.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _folderChooser.setNavigationFieldVisible(showNavigationText.isSelected());
            }
        });
        showNavigationText.setSelected(_folderChooser.isNavigationFieldVisible());

        final JCheckBox showRecentList = new JCheckBox("Show recent list on the tool bar");
        showRecentList.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _folderChooser.setRecentListVisible(showRecentList.isSelected());
            }
        });
        showRecentList.setSelected((_folderChooser.isRecentListVisible()));

        final JCheckBox showDeleteButton = new JCheckBox("Show delete button on the tool bar");
        showDeleteButton.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (showDeleteButton.isSelected()) {
                    _folderChooser.setAvailableButtons(_folderChooser.getAvailableButtons() | FolderChooser.BUTTON_DELETE);
                }
                else {
                    _folderChooser.setAvailableButtons(_folderChooser.getAvailableButtons() & ~FolderChooser.BUTTON_DELETE);
                }
            }
        });
        showDeleteButton.setSelected((_folderChooser.getAvailableButtons() & ~FolderChooser.BUTTON_DELETE) != 0);

        final JCheckBox showNewButton = new JCheckBox("Show new button on the tool bar");
        showNewButton.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (showNewButton.isSelected()) {
                    _folderChooser.setAvailableButtons(_folderChooser.getAvailableButtons() | FolderChooser.BUTTON_NEW);
                }
                else {
                    _folderChooser.setAvailableButtons(_folderChooser.getAvailableButtons() & ~FolderChooser.BUTTON_NEW);
                }
            }
        });
        showNewButton.setSelected((_folderChooser.getAvailableButtons() & ~FolderChooser.BUTTON_NEW) != 0);

        final JCheckBox showRefreshButton = new JCheckBox("Show refresh button on the tool bar");
        showRefreshButton.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (showRefreshButton.isSelected()) {
                    _folderChooser.setAvailableButtons(_folderChooser.getAvailableButtons() | FolderChooser.BUTTON_REFRESH);
                }
                else {
                    _folderChooser.setAvailableButtons(_folderChooser.getAvailableButtons() & ~FolderChooser.BUTTON_REFRESH);
                }
            }
        });
        showRefreshButton.setSelected((_folderChooser.getAvailableButtons() & ~FolderChooser.BUTTON_REFRESH) != 0);

        final JCheckBox showDesktopButton = new JCheckBox("Show back to desktop button on the tool bar");
        showDesktopButton.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (showDesktopButton.isSelected()) {
                    _folderChooser.setAvailableButtons(_folderChooser.getAvailableButtons() | FolderChooser.BUTTON_DESKTOP);
                }
                else {
                    _folderChooser.setAvailableButtons(_folderChooser.getAvailableButtons() & ~FolderChooser.BUTTON_DESKTOP);
                }
            }
        });
        showDesktopButton.setSelected((_folderChooser.getAvailableButtons() & ~FolderChooser.BUTTON_DESKTOP) != 0);

        final JCheckBox showMyDocumentsButton = new JCheckBox("Show back to my documents button on the tool bar");
        showMyDocumentsButton.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (showMyDocumentsButton.isSelected()) {
                    _folderChooser.setAvailableButtons(_folderChooser.getAvailableButtons() | FolderChooser.BUTTON_MY_DOCUMENTS);
                }
                else {
                    _folderChooser.setAvailableButtons(_folderChooser.getAvailableButtons() & ~FolderChooser.BUTTON_MY_DOCUMENTS);
                }
            }
        });
        showMyDocumentsButton.setSelected((_folderChooser.getAvailableButtons() & ~FolderChooser.BUTTON_MY_DOCUMENTS) != 0);

        switchPanel.add(showNavigationText);
        switchPanel.add(showRecentList);
        switchPanel.add(showDeleteButton);
        switchPanel.add(showNewButton);
        switchPanel.add(showRefreshButton);
        switchPanel.add(showDesktopButton);
        switchPanel.add(showMyDocumentsButton);
        return switchPanel;
    }

    private AbstractButton createBrowseButton() {
        final JButton button = new JButton("Browse");
        button.setMnemonic('B');
        button.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (_textField.getText().length() > 0) {
                    _currentFolder = _folderChooser.getFileSystemView().createFileObject(_textField.getText());
                }
                _folderChooser.setCurrentDirectory(_currentFolder);
                _folderChooser.setRecentList(_recentList);
                _folderChooser.setFileHidingEnabled(true);
                int result = _folderChooser.showOpenDialog(button.getTopLevelAncestor());
                if (result == FolderChooser.APPROVE_OPTION) {
                    _currentFolder = _folderChooser.getSelectedFile();
                    if (_recentList.contains(_currentFolder.toString())) {
                        _recentList.remove(_currentFolder.toString());
                    }
                    _recentList.add(0, _currentFolder.toString());
                    File selectedFile = _folderChooser.getSelectedFile();
                    if (selectedFile != null) {
                        _textField.setText(selectedFile.toString());
                    }
                    else {
                        _textField.setText("");
                    }
                }
            }
        });
        button.setRequestFocusEnabled(false);
        button.setFocusable(false);
        return button;
    }

    @Override
    public String getDescription() {
        return "This is a demo of FolderChooser. \n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.swing.FolderChooser";
    }

    @Override
    public String getDemoFolder() {
        return "G5.DateComboBoxes";
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new FolderChooserDemo());
    }
}
