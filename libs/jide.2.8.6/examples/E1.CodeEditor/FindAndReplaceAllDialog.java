/*
 * @(#)FindAndReplaceAllDialig.java 1/1/2007
 *
 * Copyright 2002 - 2007 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.dialog.ButtonPanel;
import com.jidesoft.search.FindAndReplace;
import com.jidesoft.search.FindAndReplaceDialog;
import com.jidesoft.search.FindResultTree;
import com.jidesoft.search.FindResults;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class FindAndReplaceAllDialog extends FindAndReplaceDialog {

    public FindAndReplaceAllDialog(Frame owner, String title, FindAndReplace findAndReplace) throws HeadlessException {
        super(owner, title, findAndReplace);
    }

    public FindAndReplaceAllDialog(Dialog owner, String title, FindAndReplace findAndReplace) throws HeadlessException {
        super(owner, title, findAndReplace);
    }

    @Override
    public ButtonPanel createButtonPanel() {
        final ButtonPanel buttonPanel = new ButtonPanel();
        buttonPanel.setGroupGap(10);

        JButton findAllButton = new JButton(new AbstractAction("Find") {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                _findAndReplacePanel.saveData();
                if (_findAndReplace.isReplace()) {
                    _findAndReplacePanel.getFindAndReplace().search();
                }
                else {
                    FindResults result = _findAndReplacePanel.getFindAndReplace().searchAll();
                    JDialog dialog = new JDialog((JFrame) null, "Find Result");
                    FindResultTree view = new FindResultTree();
                    view.addSearchResult(result);
                    dialog.add(new JScrollPane(view));
                    dialog.pack();
                    dialog.setVisible(true);
                }

            }
        });
        findAllButton.setMnemonic('A');
        buttonPanel.addButton(findAllButton, ButtonPanel.AFFIRMATIVE_BUTTON);

        JButton closeButton = new JButton(new AbstractAction("Close") {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        closeButton.setMnemonic('C');
        buttonPanel.addButton(closeButton, ButtonPanel.CANCEL_BUTTON);

        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setDefaultAction(findAllButton.getAction());
        setDefaultCancelAction(closeButton.getAction());
        getRootPane().setDefaultButton(findAllButton);
        return buttonPanel;
    }
}
