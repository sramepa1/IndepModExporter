package cz.cvut.promod.epc.workspace.cell;

import com.jgraph.components.labels.MultiLineVertexRenderer;

import java.awt.*;

import cz.cvut.promod.epc.modelFactory.epcGraphItemModels.OrganizationUnitModel;
import cz.cvut.promod.epc.modelFactory.epcGraphItemModels.LogicFunctionModel;
import org.apache.log4j.Logger;

/**
 * ProMod, master thesis project
 * User: Petr Zverina, petr.zverina@gmail.com
 * Date: 21:52:01, 2.12.2009
 *
 * Special implementation of the MultiLineVertexRenderer for the EPCNotation plugin.
 *
 * It paints the special shapes of EPC items.
 */
public class EPCVertexRenderer extends MultiLineVertexRenderer{

    private final static Logger LOG = Logger.getLogger(EPCVertexRenderer.class);

    private static final Font DOUBLE_LOGICS_FONT = new Font("Dialog", Font.BOLD, 20);

    /**
     * In case of any change in constants in any future version of JGraph, this value could be re-considered,
     * if any errors occur.
     */
    public static final int SHAPE_EPC_EVENT = -1;
    public static final int SHAPE_EPC_GOAL = -2;
    public static final int SHAPE_EPC_ENVELOPE = -3;
    public static final int SHAPE_EPC_LOGIC_AND_OR = -4;
    public static final int SHAPE_EPC_LOGIC_AND_XOR = -5;
    public static final int SHAPE_EPC_LOGIC_OR_AND = -6;
    public static final int SHAPE_EPC_LOGIC_OR_XOR = -7;
    public static final int SHAPE_EPC_LOGIC_XOR_AND = -8;
    public static final int SHAPE_EPC_LOGIC_XOR_OR = -9;

    @Override
    protected void paintBackground(Graphics g) {
        final Dimension d = getSize();
        final int b = borderWidth;
        final int width = d.width - b;
        final int height = d.height - b;

        if(shape == SHAPE_EPC_LOGIC_AND_OR
                || shape == SHAPE_EPC_LOGIC_AND_XOR
                || shape == SHAPE_EPC_LOGIC_OR_AND
                || shape == SHAPE_EPC_LOGIC_OR_XOR
                || shape == SHAPE_EPC_LOGIC_XOR_AND
                || shape == SHAPE_EPC_LOGIC_XOR_OR){

            final int originalShape = shape;

            shape = SHAPE_CIRCLE;
            super.paintBackground(g);

            final Color originColor = g.getColor();
            g.setColor(getForeground());

            g.drawLine(0, height / 2, width, height / 2);

            final String upString;
            final String downString;
            switch (originalShape){
                case SHAPE_EPC_LOGIC_AND_OR:
                    upString = LogicFunctionModel.AND_UNICODE;
                    downString = LogicFunctionModel.OR_UNICODE;
                    break;
                case SHAPE_EPC_LOGIC_AND_XOR:
                    upString = LogicFunctionModel.AND_UNICODE;
                    downString = LogicFunctionModel.XOR_UNICODE;
                    break;
                case SHAPE_EPC_LOGIC_OR_AND:
                    upString = LogicFunctionModel.OR_UNICODE;
                    downString = LogicFunctionModel.AND_UNICODE;
                    break;
                case SHAPE_EPC_LOGIC_OR_XOR:
                    upString = LogicFunctionModel.OR_UNICODE;
                    downString = LogicFunctionModel.XOR_UNICODE;
                    break;
                case SHAPE_EPC_LOGIC_XOR_AND:
                    upString = LogicFunctionModel.XOR_UNICODE;
                    downString = LogicFunctionModel.AND_UNICODE;
                    break;
                case SHAPE_EPC_LOGIC_XOR_OR:
                    upString = LogicFunctionModel.XOR_UNICODE;
                    downString = LogicFunctionModel.OR_UNICODE;
                    break;
                default:
                    LOG.error("Wrong 'double' logic function shape identifier");
                    upString = downString = "-";
            }

            g.setFont(DOUBLE_LOGICS_FONT);
            
            g.drawString(upString, 9, 15);
            g.drawString(downString, 9, 32);

            g.setColor(originColor);

        } else if(shape == SHAPE_EPC_ENVELOPE){
            final int OVERLAP = 7;

            final Color originColor = g.getColor();
            g.setColor(getForeground());

            g.drawLine(0, height, width / 2, height / 2 - OVERLAP);
            g.drawLine(width, height, width / 2, height / 2 - OVERLAP);

            g.setColor(getBackground());
            final int[] xpoints = { 0, width, width / 2, };
            final int[] ypoints = { 0, 0, height / 2 + OVERLAP };
            g.fillPolygon(xpoints, ypoints, 3);

            g.setColor(getForeground());
            g.drawPolygon(xpoints, ypoints, 3);

            g.drawRect(0, 0, width, height);

            g.setColor(originColor);
        }

        else if(shape == SHAPE_EPC_GOAL){
            /*  Points of the polygon
                    4
                0       1
                2       3
             */
            final int point0X = 0;
            final int point0Y = Math.min(10, height);

            final int point1X = width;
            final int point1Y = Math.min(10, height);

            final int point2X =  0;
            final int point2Y =  height;

            final int point3X =  width;
            final int point3Y =  height;

            final int point4X = width / 2;
            final int point4Y = 0; 

            final int[] xpoints = { point0X, point4X, point1X, point3X, point2X};
            final int[] ypoints = { point0Y, point4Y, point1Y, point3Y, point2Y};

            diamond = new Polygon(xpoints, ypoints, 5);

            final Color originColor = g.getColor();
            g.setColor(getForeground());
            g.drawPolygon(diamond);
            g.setColor(originColor);

        } else if(shape == SHAPE_EPC_EVENT){
            /* Points of the polygon, variables point_X(Y)
                0----1
            5 <       > 2
                4----3
             */

            final int point0X = Math.min(((int)(0.2 * width)), 25);
            final int point0Y = 0;

            final int point1X = Math.max(((int)(0.8 * width)), width - 25);
            final int point1Y = 0;

            final int point2X = width;
            final int point2Y = height / 2;

            final int point3X = Math.max(((int)(0.8 * width)), width - 25);
            final int point3Y = height;

            final int point4X = Math.min(((int)(0.2 * width)), 25);
            final int point4Y = height;

            final int point5X = 0;
            final int point5Y = height / 2;

            final int[] xpoints = { point0X, point1X, point2X, point3X, point4X, point5X };
            final int[] ypoints = { point0Y, point1Y, point2Y, point3Y, point4Y, point5Y };

            diamond = new Polygon(xpoints, ypoints, 6);

            g.setColor(super.getBackground());
            g.fillPolygon(diamond);

        } else if(shape == SHAPE_CIRCLE){
            super.paintBackground(g);

            if(isOrganizationUnit()){
                // add the extra line if the vertex is Organization Unit

                final Color originColor = g.getColor();
                g.setColor(getForeground());

                /**
                 * Using (x/a)^2 + (y/b)^2 = 1
                 */

                final int x = 20;
                final int distance = getDistancefromHorizontalCenter(x);
                final int y1 = (getHeight() - (2*distance)) / 2;
                final int y2 = y1 + (2*distance);

                g.drawLine(x, y1, x, y2);

                g.setColor(originColor);
            }

        } else {
            super.paintBackground(g);
        }
    }


    /**
     *  Calculates distance from horizontal axis.
     *
     * @param xFromLeft is a distance from left side of eclipse
     * @return the distance of  eclipse borders at the given horizontal position
     */
    private int getDistancefromHorizontalCenter(final int xFromLeft) {
        final double x = (getWidth() / 2.0) - xFromLeft;
        final double a = getWidth() / 2.0;
        final double b = getHeight() / 2.0;
        final double b_square = b*b;

        return new Double(Math.sqrt( b_square - ((b_square * x * x) / (a*a)))).intValue();
    }

    @Override
    protected void paintShapeBorder(Graphics g) {
        if(shape == SHAPE_EPC_EVENT){
            g.drawPolygon(diamond);
            
        } else {
            super.paintShapeBorder(g);
        }
    }

    /**
     * @return true if the cell is an instance of Organization Unit, false otherwise
     */
    private boolean isOrganizationUnit() {
        return userObject instanceof OrganizationUnitModel;
    }
}
