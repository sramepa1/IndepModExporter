package cz.cvut.promod.epc.workspace.cell.borders;

import javax.swing.border.AbstractBorder;
import java.awt.*;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 19:06:36, 7.12.2009
 *
 * The special border for Computer HW epc element.
 */
public class ComputerHWBorder extends AbstractBorder {

    private final int FIRST_VERTICAL_LINE_OFFSET = 7;
    private final int DIAMETER = 7;

    private final Insets INSETS = new Insets(1,9,1,9);

    private Color lineColor;


    public ComputerHWBorder(final Color color) {
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

        final int x1 = x + FIRST_VERTICAL_LINE_OFFSET;

        g2d.drawLine(x1, 0, x1, height);

        g2d.fillOval(x1 + 2, 1, DIAMETER, DIAMETER);
        g2d.fillOval(x1 + 2, height - 2 - DIAMETER, DIAMETER, DIAMETER);
        g2d.fillOval(width - 2 - DIAMETER, 1, DIAMETER, DIAMETER);
        g2d.fillOval(width - 2 - DIAMETER, height - 2 - DIAMETER, DIAMETER, DIAMETER);
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