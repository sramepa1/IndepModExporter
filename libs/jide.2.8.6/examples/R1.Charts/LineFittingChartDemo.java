/*
 * @(#)LineFittingChartDemo.java 8/22/2009
 *
 * 2002 - 2010 JIDE Software Inc. All rights reserved.
 * Copyright 2005 - 2010 Catalysoft Ltd. All rights reserved.
 */

import com.jidesoft.chart.Chart;
import com.jidesoft.chart.Legend;
import com.jidesoft.chart.PointShape;
import com.jidesoft.chart.annotation.AutoPositionedLabel;
import com.jidesoft.chart.axis.Axis;
import com.jidesoft.chart.fit.LineFitter;
import com.jidesoft.chart.fit.PolynomialFitter;
import com.jidesoft.chart.fit.QuadraticFitter;
import com.jidesoft.chart.model.*;
import com.jidesoft.chart.style.ChartStyle;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.range.NumericRange;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.util.logging.Logger;

@SuppressWarnings("serial")
public class LineFittingChartDemo extends AbstractDemo {
    private static final Logger logger = Logger.getLogger(LineFittingChartDemo.class.getName());
    private static final String description = "Drag the points to new locations or right-click and use the context menu to add and remove points. " +
            "As you make changes, you will see trends approximated with a constant, straight line, quadratic and cubic.";
    private static final String name = "Line & Curve Fitting";
    private final JMenuItem addPointMenuItem = new JMenuItem("Add point");
    private final JMenuItem removePointMenuItem = new JMenuItem("Remove point");
    private JPanel demoPanel;
    private Chart chart;
    private Axis xAxis;
    private Axis yAxis;
    private DefaultChartModel model;
    private JPopupMenu popup;
    private Point2D selectedPoint;
    private ChartModel constantModel;
    private ChartModel lineModel;
    private ChartModel quadraticModel;
    private ChartModel cubicModel;
    private Legend legend;

    public LineFittingChartDemo() {
        super();
    }

    public JPanel createDemo() {
        demoPanel = new JPanel();
        demoPanel.setPreferredSize(new Dimension(600, 500));
        demoPanel.setLayout(new BorderLayout());
        chart = new Chart();
        legend = new Legend(chart, 5);
        demoPanel.add(chart, BorderLayout.CENTER);
        demoPanel.add(legend, BorderLayout.SOUTH);
        model = new DefaultChartModel("Points");
        xAxis = new Axis(new NumericRange(0, 10));
        yAxis = new Axis(new NumericRange(0, 10));
        popup = new JPopupMenu();
        chart.setXAxis(xAxis);
        chart.setYAxis(yAxis);
        chart.setTitle(new AutoPositionedLabel("Least Squares Curve Fitting", Color.blue));
        xAxis.setLabel("x");
        yAxis.setLabel("y");
        ChartStyle style = new ChartStyle(Color.red, PointShape.UP_TRIANGLE);
        style.setPointSize(10);
        chart.addModel(model, style);
        popup.add(addPointMenuItem);
        popup.add(removePointMenuItem);

        FittingMouseListener mouseAdapter = new FittingMouseListener();
        chart.addMouseListener(mouseAdapter);
        chart.addMouseMotionListener(mouseAdapter);
        addPointMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                model.addPoint(selectedPoint.getX(), selectedPoint.getY());
                updateCurveFit();
            }
        });
        removePointMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Chartable nearest = chart.nearestPoint(selectedPoint, model).getFirst();
                model.removePoint(nearest);
                updateCurveFit();
            }
        });
        for (int i = 0; i < 5; i++) {
            model.addPoint(10 * Math.random(), 10 * Math.random());
        }
        updateCurveFit();
        return demoPanel;
    }

    private void updateCurveFit() {
        if (constantModel != null && chart.containsModel(constantModel)) {
            chart.removeModel(constantModel);
        }
        if (model.getPointCount() >= 1) {
            PolynomialFitter fitter = new PolynomialFitter(0);
            constantModel = fitter.performRegression("Constant", model, xAxis.getOutputRange(), 2);
            ChartStyle constantStyle = new ChartStyle(Color.darkGray, false, true);
            chart.addModel(constantModel, constantStyle);
        }

        if (lineModel != null && chart.containsModel(lineModel)) {
            chart.removeModel(lineModel);
        }
        if (model.getPointCount() >= 2) {
            lineModel = LineFitter.getInstance().performRegression("Line", model, xAxis.getOutputRange(), 2);
            ChartStyle style = new ChartStyle(Color.green.darker(), false, true);
            chart.addModel(lineModel, style);
        }

        if (quadraticModel != null && chart.containsModel(quadraticModel)) {
            chart.removeModel(quadraticModel);
        }
        if (model.getPointCount() >= 3) {
            quadraticModel = QuadraticFitter.getInstance().performRegression("Quadratic", model, xAxis.getOutputRange(), 50);
            ChartStyle quadraticStyle = new ChartStyle(Color.blue, false, true);
            chart.addModel(quadraticModel, quadraticStyle);
        }

        if (cubicModel != null && chart.containsModel(cubicModel)) {
            chart.removeModel(cubicModel);
        }

        if (model.getPointCount() >= 4) {
            try {
                PolynomialFitter fitter = new PolynomialFitter(3);
                cubicModel = fitter.performRegression("Cubic", model, xAxis.getOutputRange(), 50);
                ChartStyle cubicStyle = new ChartStyle(Color.magenta, false, true);
                chart.addModel(cubicModel, cubicStyle);
            }
            catch (Exception e) {
                logger.warning(e.getMessage());
            }
        }
    }

    @Override
    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getDemoFolder() {
        return "R1.Charts";
    }

    class FittingMouseListener implements MouseListener, MouseMotionListener {

        public void mousePressed(MouseEvent me) {
            selectedPoint = chart.calculateUserPoint(me.getPoint());
            checkPopup(me);
        }

        public void mouseReleased(MouseEvent me) {
            checkPopup(me);
        }

        public void mouseClicked(MouseEvent me) {
            //logger.info("Real point is "+p2d);
            checkPopup(me);
        }

        public void mouseDragged(MouseEvent me) {
            Point2D dragPoint = chart.calculateUserPoint(me.getPoint());
            if (dragPoint != null) {
                ChartPoint nearest = (ChartPoint) chart.nearestPoint(dragPoint, model).getFirst();
                nearest.setX(new RealPosition(dragPoint.getX()));
                nearest.setY(new RealPosition(dragPoint.getY()));
                updateCurveFit();
            }
        }

        /*
         * Part of the MouseMotionListener interface
         * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
         */
        public void mouseMoved(MouseEvent e) {

        }

        private void checkPopup(MouseEvent me) {
            if (me.isPopupTrigger()) {
                popup.show(demoPanel, me.getX(), me.getY());
            }
        }

        /* (non-Javadoc)
         * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
         */
        public void mouseEntered(MouseEvent e) {
        }

        /* (non-Javadoc)
         * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
         */
        public void mouseExited(MouseEvent e) {
        }

    }

    public Component getDemoPanel() {
        if (demoPanel == null) {
            demoPanel = createDemo();
        }
        return demoPanel;
    }

    public String getProduct() {
        return PRODUCT_NAME_CHARTS;
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new LineFittingChartDemo());
    }
}
