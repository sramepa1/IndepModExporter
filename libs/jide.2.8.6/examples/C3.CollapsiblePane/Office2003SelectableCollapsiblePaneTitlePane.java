/*
 * @(#)SelectableCollapsiblePaneTitlePane.java 9/13/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.pane.CollapsiblePane;
import com.jidesoft.plaf.office2003.Office2003CollapsiblePaneTitlePane;

import java.awt.event.MouseEvent;

public class Office2003SelectableCollapsiblePaneTitlePane extends Office2003CollapsiblePaneTitlePane {
    public Office2003SelectableCollapsiblePaneTitlePane(CollapsiblePane f) {
        super(f);
    }

    @Override
    protected BasicCollapseButton createCollapseButton() {
        return new Button();
    }

    protected class Button extends BasicCollapseButton {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getSource() != Office2003SelectableCollapsiblePaneTitlePane.this) {
                super.mouseClicked(e);
            }
            _pane.setEmphasized(true);
        }
    }
}
