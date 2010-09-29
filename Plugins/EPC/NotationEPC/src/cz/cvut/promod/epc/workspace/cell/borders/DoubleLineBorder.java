package cz.cvut.promod.epc.workspace.cell.borders;

import javax.swing.border.AbstractBorder;
import java.awt.*;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 19:13:11, 7.12.2009
 *
 * The special border for Material Input/Output epc element.
 */
public class DoubleLineBorder extends AbstractBorder {

    private Color lineColor;

    public DoubleLineBorder(final Color color) {
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
          g.drawRect(x+2, y+2, width-5, height-5);
    }

    public Color getLineColor() {
        return lineColor;
    }

    @Override
    public Insets getBorderInsets(final Component component) {
        return new Insets(3,3,3,3);
    }

    @Override
    public boolean isBorderOpaque() {
        return false;
    }
}
