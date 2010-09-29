/*
 * @(#)GanttChartDemo.java 7/27/2009
 *
 * Copyright 2002 - 2009 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.gantt.*;
import com.jidesoft.grid.TableUtils;
import com.jidesoft.grid.TreeTableModel;
import com.jidesoft.plaf.GanttUIDefaultsCustomizer;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.range.IntegerRange;
import com.jidesoft.scale.DefaultPeriodConverter;
import com.jidesoft.scale.NumberPeriod;
import com.jidesoft.scale.NumberScaleModel;
import com.jidesoft.scale.ScaleArea;
import com.jidesoft.swing.JideBoxLayout;
import com.jidesoft.swing.JideSwingUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;

@SuppressWarnings("serial")
public class NumberGanttChartDemo extends AbstractDemo {

    private GanttChartPane<Integer, GanttEntry<Integer>> _ganttChartPane;
    private Integer _markedStep = 0;

    public NumberGanttChartDemo() {
    }

    public String getName() {
        return "Gantt Chart Demo (Number)";
    }

    public String getProduct() {
        return PRODUCT_NAME_GANTT_CHART;
    }

    @Override
    public int getAttributes() {
        return ATTRIBUTE_BETA;
    }

    @Override
    public String getDescription() {
        return "\n" + "Demoed classes:\n" + "com.jidesoft.gantt.GanttChartPane";
    }

    @Override
    public Component getOptionsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new JideBoxLayout(panel, JideBoxLayout.Y_AXIS, 5));

        return panel;
    }

    public Component getDemoPanel() {
        DefaultGanttModel<Integer, GanttEntry<Integer>> model = new DefaultGanttModel<Integer, GanttEntry<Integer>>(
                new TreeTableModel<GanttEntry<Integer>>() {
                    public int getColumnCount() {
                        return 1;
                    }

                    @Override
                    public String getColumnName(int column) {
                        switch (column) {
                            case 0:
                                return "Task";
                            default:
                                return super.getColumnName(column);
                        }
                    }
                });

        model.setScaleModel(new NumberScaleModel());
        model.setRange(new IntegerRange(1, 11));

        DefaultGanttEntry<Integer> gameGroup = new DefaultGanttEntry<Integer>(
                "The Gantt Game", Integer.class, new IntegerRange(1, 11), 0);
        gameGroup.setExpanded(true);

        DefaultGanttEntry<Integer> setupGroup = new DefaultGanttEntry<Integer>(
                "Game setup", Integer.class, new IntegerRange(1, 4), 0);
        setupGroup.setExpanded(true);

        setupGroup.addChild(new DefaultGanttEntry<Integer>("Shuffle cards",
                Integer.class, new IntegerRange(1, 2), 0));
        setupGroup.addChild(new DefaultGanttEntry<Integer>("Place starting pieces",
                Integer.class, new IntegerRange(2, 3), 0));
        setupGroup.addChild(new DefaultGanttEntry<Integer>("Determine player turn order",
                Integer.class, new IntegerRange(3, 4), 0));

        gameGroup.addChild(setupGroup);

        DefaultGanttEntry<Integer> turnGroup = new DefaultGanttEntry<Integer>(
                "Player turn", Integer.class, new IntegerRange(4, 11), 0);
        turnGroup.setExpanded(true);

        turnGroup.addChild(new DefaultGanttEntry<Integer>("Draw card",
                Integer.class, new IntegerRange(4, 5), 0));
        turnGroup.addChild(new DefaultGanttEntry<Integer>("Play cards",
                Integer.class, new IntegerRange(5, 6), 0));
        turnGroup.addChild(new DefaultGanttEntry<Integer>("Roll dice",
                Integer.class, new IntegerRange(6, 7), 0));
        turnGroup.addChild(new DefaultGanttEntry<Integer>("Move piece",
                Integer.class, new IntegerRange(7, 8), 0));
        turnGroup.addChild(new DefaultGanttEntry<Integer>("Remove captured pieces",
                Integer.class, new IntegerRange(8, 9), 0));
        turnGroup.addChild(new DefaultGanttEntry<Integer>("Trade cards",
                Integer.class, new IntegerRange(9, 10), 0));
        turnGroup.addChild(new DefaultGanttEntry<Integer>("Check for player victory",
                Integer.class, new IntegerRange(10, 11), 0));

        gameGroup.addChild(turnGroup);

        model.addGanttEntry(gameGroup);

        _ganttChartPane = new GanttChartPane<Integer, GanttEntry<Integer>>(model);

        ScaleArea<Integer> scaleArea = _ganttChartPane.getGanttChart().getScaleArea();
        scaleArea.setVisiblePeriods(Collections.singletonList(NumberPeriod.ONE));

        DefaultPeriodConverter<Integer> renderer = new DefaultPeriodConverter<Integer>("Step") {
            @Override
            public String getText(Integer startInstant, Integer endInstant) {
                return String.valueOf(startInstant);
            }

            @Override
            public String getDescription(Integer startInstant, Integer endInstant) {
                return "Step " + getText(startInstant, endInstant);
            }
        };
        scaleArea.setPeriodConverter(NumberPeriod.ONE, renderer);

        PeriodBackgroundPainter<Integer> painter = new AbstractPeriodMarker<Integer>(
                NumberPeriod.ONE, UIManager.getColor("Table.selectionBackground"), null, null) {

            @Override
            public boolean isNextPeriodMarked(Integer startInstant, Integer endInstant) {
                return false;
            }

            @Override
            public boolean isPeriodMarked(Integer startInstant, Integer endInstant) {
                return JideSwingUtilities.equals(_markedStep, startInstant);
            }

            @Override
            public boolean isPreviousPeriodMarked(Integer startInstant, Integer endInstant) {
                return false;
            }
        };

        _ganttChartPane.getGanttChart().addPeriodBackgroundPainter(painter);

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                _ganttChartPane.getGanttChart().autoResizePeriods(false);
            }
        });

        new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                _markedStep = (_markedStep % 10) + 1;
                _ganttChartPane.getGanttChart().repaint();
            }
        }).start();

        TableUtils.autoResizeAllColumns(_ganttChartPane.getTreeTable());

        return _ganttChartPane;
    }

    public static void main(String[] s) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                LookAndFeelFactory.addUIDefaultsCustomizer(new GanttUIDefaultsCustomizer());
                LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
                showAsFrame(new NumberGanttChartDemo());
            }
        });
    }
}