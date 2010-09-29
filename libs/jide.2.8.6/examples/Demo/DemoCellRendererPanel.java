/*
 * @(#)DemoCellRendererPanel.java 3/6/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.swing.NullPanel;

import javax.swing.*;
import java.awt.*;

/**
 */
class DemoCellRendererPanel extends NullPanel {
    private JLabel _label;

    public DemoCellRendererPanel() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBorder(BorderFactory.createEmptyBorder());
        setOpaque(false);
    }

    public void setLabel(JLabel label) {
        _label = label;
        add(_label);
    }

    public JLabel getLabel() {
        return _label;
    }

    @Override
    public void setFont(Font font) {
        super.setFont(font);
        if (_label != null) {
            _label.setFont(font);
        }
    }
}
