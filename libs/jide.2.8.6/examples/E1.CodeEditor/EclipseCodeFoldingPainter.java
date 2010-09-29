/*
 * @(#)EclipseCodeFoldingPainter.java 1/9/2007
 *
 * Copyright 2002 - 2007 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.editor.Span;
import com.jidesoft.editor.margin.CodeFoldingPainter;
import com.jidesoft.icons.IconsFactory;
import com.jidesoft.plaf.basic.ThemePainter;

import javax.swing.*;
import java.awt.*;

public class EclipseCodeFoldingPainter implements CodeFoldingPainter {
    private static final Icon FOLDING_COLLAPSED = IconsFactory.getImageIcon(EclipseCodeFoldingPainter.class, "icons/eclipse_folding_collapsed.gif");
    private static final Icon FOLDING_EXPANDED = IconsFactory.getImageIcon(EclipseCodeFoldingPainter.class, "icons/eclipse_folding_expanded.gif");
    private static final Color COLOR_BORDER = new Color(236, 233, 216);

    protected Icon getExpandedIcon() {
        return FOLDING_EXPANDED;
    }

    protected Icon getCollapsedIcon() {
        return FOLDING_COLLAPSED;
    }

    public int paintFoldingStart(Component c, Graphics g, Span span, Rectangle rect, int state) {
        Icon icon = getExpandedIcon();
        icon.paintIcon(c, g, rect.x + rect.width / 2 - icon.getIconWidth() / 2, rect.y + rect.height / 2 - icon.getIconHeight() / 2);
        return rect.y + rect.height / 2 + 4;
    }

    public int paintFoldingEnd(Component c, Graphics g, Span span, Rectangle rect, int state) {
        return rect.y + rect.height / 2;
    }

    public void paintCollapsedFolding(Component c, Graphics g, Span span, Rectangle rect, int state) {
        Icon icon = getCollapsedIcon();
        icon.paintIcon(c, g, rect.x + rect.width / 2 - icon.getIconWidth() / 2, rect.y + rect.height / 2 - icon.getIconHeight() / 2);
    }

    public void paintExpandedFolding(Component c, Graphics g, Span span, Rectangle rect, int state) {
        Icon icon = getExpandedIcon();
        icon.paintIcon(c, g, rect.x + rect.width / 2 - icon.getIconWidth() / 2, rect.y + rect.height / 2 - icon.getIconHeight() / 2);
    }

    public void paintFoldingLine(Component c, Graphics g, Span span, Rectangle rect, int state) {
        if (state == ThemePainter.STATE_ROLLOVER) {
            g.setColor(Color.LIGHT_GRAY);
            int centerX = rect.x + rect.width / 2 - 1;
            g.drawLine(centerX, rect.y, centerX, rect.y + rect.height);
            g.drawLine(centerX, rect.y + rect.height, centerX + rect.width / 4, rect.y + rect.height);
        }
    }

    public void paintBackground(Component c, Graphics g, Rectangle rect) {
        g.setColor(Color.WHITE);
        g.fillRect(rect.x, rect.y, rect.width, rect.height);
        g.setColor(COLOR_BORDER);
        g.drawLine(rect.x + rect.width - 1, rect.y, rect.x + rect.width - 1, rect.y + rect.height);
    }

    public int getPreferredWidth() {
        return 10;
    }
}
