/*
 * @(#)GanttChartPaneDemo.java 10/15/2009
 *
 * Copyright 2002 - 2009 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.gantt.*;
import com.jidesoft.grid.CellEditorManager;
import com.jidesoft.grid.CellRendererManager;
import com.jidesoft.grid.TableUtils;
import com.jidesoft.grid.TreeTable;
import com.jidesoft.icons.JideIconsFactory;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.scale.DatePeriod;
import com.jidesoft.scale.ResizePeriodsPopupMenuCustomizer;
import com.jidesoft.scale.VisiblePeriodsPopupMenuCustomizer;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Date;

@SuppressWarnings("serial")
public class GanttChartPaneDemo extends GanttChartDemo {

    protected GanttChartPane<Date, DefaultGanttEntry<Date>> _ganttChartPane;

    public GanttChartPaneDemo() {
    }

    @Override
    public String getName() {
        return "Gantt Chart Pane Demo";
    }

    @Override
    public String getDescription() {
        return "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.gantt.GanttChartPane";
    }


    @Override
    public Component getDemoPanel() {
        CellRendererManager.initDefaultRenderer();
        CellEditorManager.initDefaultEditor();

        DefaultGanttModel<Date, DefaultGanttEntry<Date>> model = createGanttModel();

        _ganttChartPane = new DateGanttChartPane<DefaultGanttEntry<Date>>(model) {
            @Override
            protected JComponent createTableHeaderComponent() {
                JComponent component = super.createTableHeaderComponent();
                component.add(new LogoPanel());
                return component;
            }
        };

        TreeTable treeTable = _ganttChartPane.getTreeTable();
        treeTable.setDragEnabled(true);
        treeTable.setTransferHandler(new TreeTableTransferHandler());

        _ganttChart = _ganttChartPane.getGanttChart();
        _ganttChart.getScaleArea().addPopupMenuCustomizer(new VisiblePeriodsPopupMenuCustomizer<Date>());
        _ganttChartPane.getGanttChart().getScaleArea().addPopupMenuCustomizer(new ResizePeriodsPopupMenuCustomizer<Date>(_ganttChartPane.getGanttChart()));

        GanttChartPopupMenuInstaller installer = new GanttChartPopupMenuInstaller(_ganttChartPane.getGanttChart());
        installer.addGanttChartPopupMenuCustomizer(new RelationGanttChartPopupMenuCustomizer());

        if (_largeModel) {
            _ganttChartPane.getGanttChart().getScaleArea().setVisiblePeriods(Arrays.asList(
                    DatePeriod.DAY_OF_WEEK, DatePeriod.FIRST_DAY_OF_WEEK, DatePeriod.MONTH, DatePeriod.YEAR));
        }

        TableUtils.autoResizeAllColumns(_ganttChartPane.getTreeTable());

        return _ganttChartPane;
    }

    @Override
    public void dispose() {
        super.dispose();
        _ganttChartPane = null;
    }

    public static void main(String[] s) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
                showAsFrame(new GanttChartPaneDemo());
            }
        });
    }

    private class LogoPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            ImageIcon imageIcon = JideIconsFactory.getImageIcon(JideIconsFactory.JIDELOGO_SMALL);
            Rectangle visible = getVisibleRect();
            Graphics2D g2D = (Graphics2D) g.create();
            if (visible.getHeight() < imageIcon.getIconHeight()) {
                double scale = visible.getHeight() / imageIcon.getIconHeight();
                g2D.scale(scale, scale);
            }
            if (getComponentOrientation().isLeftToRight()) {
                imageIcon.paintIcon(this, g2D, visible.x, 2);
            }
            else {
                imageIcon.paintIcon(this, g2D, visible.x + visible.width - imageIcon.getIconWidth() - 2, 2);
            }
            g2D.dispose();
        }
    }
}