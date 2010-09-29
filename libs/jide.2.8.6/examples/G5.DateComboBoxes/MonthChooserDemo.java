/*
 * @(#)MonthChooserDemo.java 11/16/2006
 *
 * Copyright 2002 - 2006 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.combobox.DateComboBox;
import com.jidesoft.combobox.DateFilter;
import com.jidesoft.combobox.DefaultDateModel;
import com.jidesoft.combobox.MonthChooserPanel;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.JideBoxLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Calendar;

/**
 * Demoed Component: {@link com.jidesoft.combobox.MonthChooserPanel} <br> Required jar files: jide-common.jar,
 * jide-grids.jar <br> Required L&F: Jide L&F extension required
 */
public class MonthChooserDemo extends AbstractDemo {

    private MonthChooserPanel _monthChooserPanel;

    public MonthChooserDemo() {
    }

    public String getName() {
        return "MonthChooserPanel Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_GRIDS;
    }

    public Component getDemoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        _monthChooserPanel = createMonthChooserPanel();
        panel.add(_monthChooserPanel, BorderLayout.BEFORE_FIRST_LINE);
        return panel;
    }

    @Override
    public Component getOptionsPanel() {
        return createOptionPanel();
    }

    @Override
    public String getDescription() {
        return "This is a demo of MonthChooserPanel. Please choose different options in the options pane and try to click on MonthChooserPanel to select dates.\n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.combobox.MonthChooserPanel\n" +
                "com.jidesoft.combobox.DateFilter\n" +
                "com.jidesoft.combobox.DateSelectionModel\n" +
                "com.jidesoft.combobox.DefaultDateModel";
    }

    @Override
    public String getDemoFolder() {
        return "G5.DateComboBoxes";
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new MonthChooserDemo());
    }

    private MonthChooserPanel createMonthChooserPanel() {
        DefaultDateModel model = new DefaultDateModel();
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.YEAR, 2010);
        calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMaximum(Calendar.DAY_OF_YEAR));
        model.setMaxDate(calendar);

        calendar.set(Calendar.YEAR, 1980);
        calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMinimum(Calendar.DAY_OF_YEAR));
        model.setMinDate(calendar);

        MonthChooserPanel panel = new MonthChooserPanel(model);
        Calendar currentDate = Calendar.getInstance();
        panel.setSelectedCalendar(currentDate);

        return panel;
    }

    protected Component createOptionPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new JideBoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        final DateComboBox minDateComboBox = new DateComboBox();
        minDateComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    _monthChooserPanel.getDateModel().setMinDate(minDateComboBox.getCalendar());
                }
            }
        });
        minDateComboBox.setCalendar(_monthChooserPanel.getDateModel().getMinDate());

        final DateComboBox maxDateComboBox = new DateComboBox();
        maxDateComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    _monthChooserPanel.getDateModel().setMaxDate(maxDateComboBox.getCalendar());
                }
            }
        });
        maxDateComboBox.setCalendar(_monthChooserPanel.getDateModel().getMaxDate());

        final JComboBox dateValidatorComboBox = new JComboBox(new String[]{
                "<None>",
                "This week",
                "This month",
                "Later this month",
                "Weekday only",
                "Weekend only"
        });
        dateValidatorComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if (_monthChooserPanel.getDateModel() instanceof DefaultDateModel) {
                        DateFilter dateFilter = null;
                        switch (dateValidatorComboBox.getSelectedIndex()) {
                            case 0:
                                dateFilter = null;
                                break;
                            case 1:
                                dateFilter = DefaultDateModel.THIS_WEEK;
                                break;
                            case 2:
                                dateFilter = DefaultDateModel.THIS_MONTH_ONLY;
                                break;
                            case 3:
                                dateFilter = DefaultDateModel.LATER_THIS_MONTH;
                                break;
                            case 4:
                                dateFilter = DefaultDateModel.WEEKDAY_ONLY;
                                break;
                            case 5:
                                dateFilter = DefaultDateModel.WEEKEND_ONLY;
                                break;

                        }
                        ((DefaultDateModel) _monthChooserPanel.getDateModel()).clearDateFilters();
                        if (dateFilter != null) {
                            ((DefaultDateModel) _monthChooserPanel.getDateModel()).addDateFilter(dateFilter);
                        }
                    }
                }
            }
        });

        panel.add(Box.createVerticalStrut(12), JideBoxLayout.FIX);
        panel.add(new JLabel("Set MinDate"));
        panel.add(Box.createVerticalStrut(6), JideBoxLayout.FIX);
        panel.add(minDateComboBox);
        panel.add(Box.createVerticalStrut(12), JideBoxLayout.FIX);
        panel.add(new JLabel("Set MaxDate"));
        panel.add(Box.createVerticalStrut(6), JideBoxLayout.FIX);
        panel.add(maxDateComboBox);
        panel.add(Box.createVerticalStrut(12), JideBoxLayout.FIX);
        panel.add(new JLabel("Example Date Filters"));
        panel.add(Box.createVerticalStrut(6), JideBoxLayout.FIX);
        panel.add(dateValidatorComboBox);
        panel.add(Box.createVerticalStrut(12), JideBoxLayout.FIX);
        panel.add(Box.createVerticalStrut(6), JideBoxLayout.FIX);

        final JCheckBox noneCheckBox = (JCheckBox) panel.add(new JCheckBox("Show None Button"));
        noneCheckBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _monthChooserPanel.setShowNoneButton(noneCheckBox.isSelected());
            }
        });
        noneCheckBox.setSelected(_monthChooserPanel.isShowNoneButton());
        panel.add(Box.createVerticalStrut(6), JideBoxLayout.FIX);

        final JCheckBox viewOnlyCheckBox = (JCheckBox) panel.add(new JCheckBox("View Only Mode"));
        viewOnlyCheckBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _monthChooserPanel.setViewOnly(viewOnlyCheckBox.isSelected());
            }
        });
        viewOnlyCheckBox.setSelected(_monthChooserPanel.isViewOnly());
        panel.add(Box.createVerticalStrut(6), JideBoxLayout.FIX);

        panel.add(Box.createGlue(), JideBoxLayout.VARY);

        return panel;
    }
}
