package cz.cvut.promod.epc.workspace.cell.borders;

import javax.swing.border.AbstractBorder;
import java.awt.*;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 19:06:36, 7.12.2009
 *
 * The special border for Machine epc element.
 */
public class MachineBorder extends AbstractBorder {

    private final int VERTICAL_LINE_OFFSET = 11;

    private final Insets INSETS = new Insets(1,9,1,9);

    private Color lineColor;


    public MachineBorder(final Color color) {
        lineColor = color;
    }

    @Override
    public void paintBorder(final Component c,
                            final Graphics g,
                            final int x,
                            final int y,
                            final int width,
                            final int height) {

        g.setColor(lineColor);

        g.drawRect(x, y, width-1, height-1);

        final Graphics2D g2d = (Graphics2D) g;

        final int x2 = width - (VERTICAL_LINE_OFFSET);

        g2d.drawLine(x2, 0, x2, height);
        g2d.drawOval(x2+1, 0, VERTICAL_LINE_OFFSET-3, VERTICAL_LINE_OFFSET-4);
    }

    public Insets getBorderInsets(final Component component) {
        return INSETS;
    }

    public Color getLineColor() {
        return lineColor;
    }

    public boolean isBorderOpaque() {
        return false;
    }

}