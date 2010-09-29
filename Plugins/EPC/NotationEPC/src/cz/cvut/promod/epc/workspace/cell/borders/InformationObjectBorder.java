package cz.cvut.promod.epc.workspace.cell.borders;

import javax.swing.border.AbstractBorder;
import java.awt.*;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 19:06:36, 7.12.2009
 *
 * The special border for Information Object epc element.
 */
public class InformationObjectBorder extends AbstractBorder {

    private final int FIRST_VERTICAL_LINE_OFFSET = 7;
    private final int SECOND_VERTICAL_LINE_OFFSET = 10;

    private final Insets INSETS = new Insets(1,9,1,9);

    private Color lineColor;

    public InformationObjectBorder(final Color color) {
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
        g.drawRect(x + FIRST_VERTICAL_LINE_OFFSET, y, width - (2 * FIRST_VERTICAL_LINE_OFFSET), height-1);
        g.drawRect(x + SECOND_VERTICAL_LINE_OFFSET, y, width - (2 * SECOND_VERTICAL_LINE_OFFSET), height-1);
    }

    @Override
    public Insets getBorderInsets(final Component component) {
        return INSETS;
    }

    public Color getLineColor() {
        return lineColor;
    }

    @Override
    public boolean isBorderOpaque() {
        return false;
    }

}
