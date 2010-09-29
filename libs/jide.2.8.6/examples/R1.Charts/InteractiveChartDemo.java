/*
 * @(#)InteractiveChartDemo.java 8/22/2009
 *
 * 2002 - 2010 JIDE Software Inc. All rights reserved.
 * Copyright 2005 - 2010 Catalysoft Ltd. All rights reserved.
 */

import com.jidesoft.chart.Chart;
import com.jidesoft.chart.PointShape;
import com.jidesoft.chart.annotation.AutoPositionedLabel;
import com.jidesoft.chart.axis.Axis;
import com.jidesoft.chart.event.PointSelection;
import com.jidesoft.chart.model.ChartPoint;
import com.jidesoft.chart.model.Highlight;
import com.jidesoft.chart.model.TableToChartAdapter;
import com.jidesoft.chart.model.TransposingChartModel;
import com.jidesoft.chart.style.ChartStyle;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.range.NumericRange;
import com.jidesoft.tooltip.BalloonTip;
import com.jidesoft.tooltip.shapes.RoundedRectangularBalloonShape;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.logging.Logger;

/**
 * A chart that demonstrates interactity with a table.
 */
@SuppressWarnings("serial")
public class InteractiveChartDemo extends AbstractDemo {
    private static final Logger logger = Logger.getLogger(InteractiveChartDemo.class.getName());
    private JPanel demoPanel;
    private JScrollPane scrollPane;
    private Chart chart;
    private JTable table;
    private JPanel controlPanel;
    private JToggleButton button;
    private int[] selectedRows = new int[]{};
    private BalloonTip balloonTip;

    public InteractiveChartDemo() {
        super();
    }

    private JPanel createDemo() {
        demoPanel = new JPanel();
        demoPanel.setPreferredSize(new Dimension(600, 500));
        chart = new Chart();
        table = new JTable();
        controlPanel = new JPanel();
        button = new JToggleButton("Transpose");
        NumericRange yRange = new NumericRange(0, 2);
        NumericRange xRange = new NumericRange(0, 2);
        Axis xAxis = new Axis(xRange);
        Axis yAxis = new Axis(yRange);
        xAxis.setLabel(new AutoPositionedLabel("This is the X Label"));
        yAxis.setLabel(new AutoPositionedLabel("This is the Y Label"));

        controlPanel.add(button);

        scrollPane = new JScrollPane(table);
        TableModel tableModel = createTableModel();
        final TableToChartAdapter sineModel = new TableToChartAdapter("Sine", tableModel);
        final TransposingChartModel transposingModel = new TransposingChartModel(sineModel);
        //logger.info("Table Model has "+tableModel.getRowCount()+" rows");
        table.setModel(tableModel);

        ChartStyle chartStyle = new ChartStyle(Color.green.darker(), PointShape.CIRCLE);
        chart.addModel(transposingModel, chartStyle);
        chart.setXAxis(xAxis);
        chart.setYAxis(yAxis);
        chart.setTitle(new AutoPositionedLabel("Interactive Chart", Color.blue));

        demoPanel.setLayout(new BorderLayout());

        JTableHeader header = table.getTableHeader();
        TableColumnModel columnModel = header.getColumnModel();
        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            TableColumn column = columnModel.getColumn(i);
            column.setMaxWidth(60);
            column.setMinWidth(30);
        }
        scrollPane.setMaximumSize(new Dimension(120, 600));
        scrollPane.setPreferredSize(new Dimension(120, 600));

        demoPanel.add(scrollPane, BorderLayout.WEST);
        demoPanel.add(chart, BorderLayout.CENTER);
        demoPanel.add(controlPanel, BorderLayout.SOUTH);

        final ListSelectionModel selectionModel = table.getSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        final Highlight h = new Highlight("red");
        ChartStyle highlightStyle = new ChartStyle(Color.red, PointShape.DISC, 8);
        chart.setHighlightStyle(h, highlightStyle);

        selectionModel.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                // Remove highlights for previously selected rows 
                for (int i = 0; i < selectedRows.length; i++) {
                    sineModel.removeHighlight(selectedRows[i]);
                }
                // Now deal with new selections
                selectedRows = table.getSelectedRows();
                chart.clearHighlights();

                for (int i = 0; i < selectedRows.length; i++) {
                    ChartPoint p = (ChartPoint) sineModel.getPoint(selectedRows[i]);
                    sineModel.addHighlight(selectedRows[i], h);
                    logger.fine("Setting highlight on " + p);
                }
                if (balloonTip != null && balloonTip.isVisible()) {
                    balloonTip.hide();
                }
                if (selectedRows.length == 1) {
                    ChartPoint p = (ChartPoint) transposingModel.getPoint(selectedRows[0]);
                    double x = p.getX().position();
                    double y = p.getY().position();
                    String text = String.format("x = %.3f, y = %.3f %s", x, y, transposingModel.isTransposing() ? "[Transposed]" : "");
                    Point2D userPoint = new Point2D.Double(x, y);
                    Point pixelPoint = chart.calculatePixelPoint(userPoint);
                    balloonTip = createBalloonTip(text);
                    balloonTip.show(chart, pixelPoint.x, pixelPoint.y);
                }
                chart.repaint();
            }
        });

        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                //System.out.println("Selected: " + button.isSelected());
                transposingModel.setTransposing(button.isSelected());
            }
        });

        chart.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                Point p = me.getPoint();
                PointSelection hit = chart.nearestPoint(p, transposingModel);
                table.getSelectionModel().setSelectionInterval(hit.getIndex(), hit.getIndex());
                Rectangle rect = table.getCellRect(hit.getIndex(), 0, true);
                table.scrollRectToVisible(rect);
            }
        });
        return demoPanel;
    }
    
    private BalloonTip createBalloonTip(String text) {
        JLabel label = new JLabel(text);
        BalloonTip balloonTip = new BalloonTip(label);
        RoundedRectangularBalloonShape shape = (RoundedRectangularBalloonShape) balloonTip.getBalloonShape();
        shape.setArrowLeftRatio(0.2);
        shape.setArrowRightRatio(0.6);
        shape.setVertexPosition(0.1);
        shape.setBalloonSizeRatio(0.6);
        shape.setCornerSize(6);
        return balloonTip;
    }

    private TableModel createTableModel() {
        DefaultTableModel model = new NumericTableModel(new Double[0][0], new String[]{"x", "y"});
        double yScale = 0.8;
        double xMax = 2.0;
        int numIntervals = 100;
        for (int i = 0; i <= numIntervals; i++) {
            double x = (xMax * i) / numIntervals;
            double y = 1 + yScale * Math.sin(x * 2 * Math.PI);
            if (y < 0) {
                y = 0;
            }
            Double[] row = new Double[]{new Double(x), new Double(y)};
            model.addRow(row);
        }
        return model;
    }


    @Override
    public String getDescription() {
        return "The idea here is to demonstrate that a chart is not just a static display, it is something you can interact with. " +
                "If you select a value from the table on the left, it is highlighted as a red point in the chart. " +
                "You can also select multiple rows of the table by holding down the CTRL key, and all the corresponding points are highlighted. " +
                "After you have made a selection, try transposing the dataset by clicking on the button. Notice that the same selection remains.\n\n"+
                "If there is just one point selected, the chart now also displays a BalloonTip. Note that BalloonTip is not part of JIDE Charts, but" +
                "is available in the JIDE Components package.";
    }

    public String getName() {
        return "Interactive Chart";
    }

    @Override
    public String getDemoFolder() {
        return "R1.Charts";
    }

    class NumericTableModel extends DefaultTableModel {

        public NumericTableModel(Object[][] data, Object[] columnNames) {
            super(data, columnNames);
        }

        @Override
        public Class<?> getColumnClass(int col) {
            return Double.class;
        }
    }

    public Component getDemoPanel() {
        if (demoPanel == null) {
            demoPanel = createDemo();
        }
        return demoPanel;
    }

    @Override
    public int getAttributes() {
        return ATTRIBUTE_NONE;
    }

    public String getProduct() {
        return PRODUCT_NAME_CHARTS;
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new InteractiveChartDemo());
    }

}
