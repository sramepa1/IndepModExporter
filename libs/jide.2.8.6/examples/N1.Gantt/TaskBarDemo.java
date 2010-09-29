/*
 * @(#)TaskBarDemo.java 8/15/2009
 *
 * Copyright 2002 - 2009 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.gantt.TaskBar;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.plaf.GanttUIDefaultsCustomizer;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

@SuppressWarnings("serial")
public class TaskBarDemo extends AbstractDemo {

    private TaskBar[] _taskBars;

    public TaskBarDemo() {
    }

    public String getName() {
        return "Task Bar Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_GRIDS;
    }

    @Override
    public int getAttributes() {
        return ATTRIBUTE_BETA;
    }

    @Override
    public String getDescription() {
        return "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.gantt.GanttChartPane";
    }

    @Override
    public Component getOptionsPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 1, 2, 2));
        final JCheckBox rollover = new JCheckBox("Rollover");
        rollover.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                for (TaskBar taskBar : _taskBars) {
                    taskBar.setRollover(rollover.isSelected());
                }
            }
        });
        final JCheckBox selected = new JCheckBox("Selected");
        selected.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                for (TaskBar taskBar : _taskBars) {
                    taskBar.setSelected(selected.isSelected());
                }
            }
        });
        panel.add(rollover);
        panel.add(selected);
        return panel;
    }

    public Component getDemoPanel() {
        _taskBars = new TaskBar[5];
        _taskBars[0] = new TaskBar();
        _taskBars[1] = new TaskBar();
        _taskBars[1].setCompletion(.5);
        _taskBars[2] = new TaskBar();
        _taskBars[2].setCompletion(1);
        _taskBars[3] = new TaskBar();
        _taskBars[3].setGroup(true);
        _taskBars[4] = new TaskBar();
        _taskBars[4].setMilestone(true);

        Box panel = new Box(BoxLayout.Y_AXIS);

        for (TaskBar taskBar : _taskBars) {
            panel.add(taskBar);
        }

        panel.setPreferredSize(new Dimension(300, 150));
        return panel;
    }


    public static void main(String[] s) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                LookAndFeelFactory.addUIDefaultsCustomizer(new GanttUIDefaultsCustomizer());
                LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
                showAsFrame(new TaskBarDemo());
            }
        });
    }
}