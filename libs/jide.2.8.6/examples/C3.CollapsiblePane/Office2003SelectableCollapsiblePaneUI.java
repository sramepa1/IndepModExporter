/*
 * @(#)SelectableCollapsiblePaneUI.java 9/13/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.pane.CollapsiblePane;
import com.jidesoft.plaf.office2003.Office2003CollapsiblePaneUI;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;

public class Office2003SelectableCollapsiblePaneUI extends Office2003CollapsiblePaneUI {

    public Office2003SelectableCollapsiblePaneUI() {
    }

    public Office2003SelectableCollapsiblePaneUI(CollapsiblePane f) {
        super(f);
    }

    public static ComponentUI createUI(JComponent b) {
        return new Office2003SelectableCollapsiblePaneUI((CollapsiblePane) b);
    }

    @Override
    protected JComponent createTitlePane(CollapsiblePane w) {
        return new Office2003SelectableCollapsiblePaneTitlePane(w);
    }
}
