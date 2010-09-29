/*
 * @(#)NetBeansCodeFoldingPainter.java 1/9/2007
 *
 * Copyright 2002 - 2007 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.editor.Span;
import com.jidesoft.plaf.UIDefaultsLookup;

import javax.swing.*;
import java.awt.*;

public class NetBeansCodeFoldingPainter extends EclipseCodeFoldingPainter {
    private static final Color COLOR_FOLDING_LINE = new Color(102, 102, 102);

    @Override
    protected Icon getExpandedIcon() {
        return UIDefaultsLookup.getIcon("Tree.expandedIcon");
    }

    @Override
    protected Icon getCollapsedIcon() {
        return UIDefaultsLookup.getIcon("Tree.collapsedIcon");
    }

    @Override
    public void paintFoldingLine(Component c, Graphics g, Span span, Rectangle rect, int state) {
        g.setColor(COLOR_FOLDING_LINE);
        int centerX = rect.x + rect.width / 2;
        g.drawLine(centerX, rect.y, centerX, rect.y + rect.height);
        g.drawLine(centerX, rect.y + rect.height, centerX + rect.width / 3, rect.y + rect.height);
    }

    @Override
    public void paintBackground(Component c, Graphics g, Rectangle rect) {
        g.setColor(Color.WHITE);
        g.fillRect(rect.x, rect.y, rect.width, rect.height);
    }

    @Override
    public int getPreferredWidth() {
        return getExpandedIcon().getIconWidth() + 4;
    }
}
