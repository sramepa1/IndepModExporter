/*
 * @(#)DateChooserPanelDemo.java 3/20/2006
 *
 * Copyright 2002 - 2006 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.combobox.*;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.JideBoxLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Calendar;

/**
 * Demoed Component: {@link com.jidesoft.combobox.DateChooserPanel} <br> Required jar files: jide-common.jar,
 * jide-grids.jar <br> Required L&F: Jide L&F extension required
 */
public class DateChooserDemo extends AbstractDemo {

    private DateChooserPanel _dateChooserPanel;

    private static final String SINGLE = "Single Selection";
    private static final String SINGLE_INTERVAL = "Single Interval Selection";
    private static final String MULTIPLE_INTERVAL = "Multiple Interval Selection";
    private static final long serialVersionUID = -1380096649365575903L;

    public DateChooserDemo() {
    }

    public String getName() {
        return "DateChooserPanel Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_GRIDS;
    }

    public Component getDemoPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        _dateChooserPanel = createDateChooserPanel();
        panel.add(_dateChooserPanel);
        return panel;
    }

    @Override
    public Component getOptionsPanel() {
        return createOptionPanel();
    }

    @Override
    public String getDescription() {
        return "This is a demo of DateChooserPanel. Please choose different options in the options pane and try to click on DateChooserPanel to select dates.\n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.combobox.DateChooserPanel\n" +
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
        showAsFrame(new DateChooserDemo());
    }

    private DateChooserPanel createDateChooserPanel() {
        DefaultDateModel model = new DefaultDateModel();
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.YEAR, 2010);
        calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMaximum(Calendar.DAY_OF_YEAR));
        model.setMaxDate(calendar);

        calendar.set(Calendar.YEAR, 1980);
        calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMinimum(Calendar.DAY_OF_YEAR));
        model.setMinDate(calendar);

        DateChooserPanel dateChooserPanel = new DateChooserPanel(model);
//        panel.setFont(new Font("Tahoma", Font.BOLD, 30));
        Calendar currentDate = Calendar.getInstance();
        dateChooserPanel.setSelectedCalendar(currentDate);
        return dateChooserPanel;
    }

    protected Component createOptionPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new JideBoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] selectionMode = new String[]{
                SINGLE,
                SINGLE_INTERVAL,
                MULTIPLE_INTERVAL
        };

        JComboBox comboBox = new JComboBox(selectionMode);
        comboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED && e.getItem() instanceof String) {
                    if ((e.getItem()).equals(SINGLE)) {
                        _dateChooserPanel.getSelectionModel().setSelectionMode(DateSelectionModel.SINGLE_SELECTION);
                    }
                    else if ((e.getItem()).equals(SINGLE_INTERVAL)) {
                        _dateChooserPanel.getSelectionModel().setSelectionMode(DateSelectionModel.SINGLE_INTERVAL_SELECTION);
                    }
                    else {
                        _dateChooserPanel.getSelectionModel().setSelectionMode(DateSelectionModel.MULTIPLE_INTERVAL_SELECTION);
                    }
                }
            }
        });

        final DateComboBox minDateComboBox = new DateComboBox();
        minDateComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    _dateChooserPanel.getDateModel().setMinDate(minDateComboBox.getCalendar());
                }
            }
        });
        minDateComboBox.setCalendar(_dateChooserPanel.getDateModel().getMinDate());

        final DateComboBox maxDateComboBox = new DateComboBox();
        maxDateComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    _dateChooserPanel.getDateModel().setMaxDate(maxDateComboBox.getCalendar());
                }
            }
        });
        maxDateComboBox.setCalendar(_dateChooserPanel.getDateModel().getMaxDate());

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
                    if (_dateChooserPanel.getDateModel() instanceof DefaultDateModel) {
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
                        ((DefaultDateModel) _dateChooserPanel.getDateModel()).clearDateFilters();
                        if (dateFilter != null) {
                            ((DefaultDateModel) _dateChooserPanel.getDateModel()).addDateFilter(dateFilter);
                        }
                    }
                }
            }
        });

        panel.add(new JLabel("Set Selection Mode"));
        panel.add(Box.createVerticalStrut(6), JideBoxLayout.FIX);
        panel.add(comboBox);
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
        final JCheckBox todayCheckBox = (JCheckBox) panel.add(new JCheckBox("Show Today Button"));
        todayCheckBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _dateChooserPanel.setShowTodayButton(todayCheckBox.isSelected());
            }
        });
        todayCheckBox.setSelected(_dateChooserPanel.isShowTodayButton());
        panel.add(Box.createVerticalStrut(6), JideBoxLayout.FIX);

        final JCheckBox noneCheckBox = (JCheckBox) panel.add(new JCheckBox("Show None Button"));
        noneCheckBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _dateChooserPanel.setShowNoneButton(noneCheckBox.isSelected());
            }
        });
        noneCheckBox.setSelected(_dateChooserPanel.isShowNoneButton());
        panel.add(Box.createVerticalStrut(6), JideBoxLayout.FIX);

        final JCheckBox prevCheckBox = (JCheckBox) panel.add(new JCheckBox("Show Previous Month Button"));
        prevCheckBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _dateChooserPanel.setShowPreviousButton(prevCheckBox.isSelected());
            }
        });
        prevCheckBox.setSelected(_dateChooserPanel.isShowPreviousButton());
        panel.add(Box.createVerticalStrut(6), JideBoxLayout.FIX);

        final JCheckBox prevYearCheckBox = (JCheckBox) panel.add(new JCheckBox("Show Previous Year Button"));
        prevYearCheckBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _dateChooserPanel.setShowPreviousYearButton(prevYearCheckBox.isSelected());
            }
        });
        prevYearCheckBox.setSelected(_dateChooserPanel.isShowPreviousYearButton());
        panel.add(Box.createVerticalStrut(6), JideBoxLayout.FIX);

        final JCheckBox nextCheckBox = (JCheckBox) panel.add(new JCheckBox("Show Next Month Button"));
        nextCheckBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _dateChooserPanel.setShowNextButton(nextCheckBox.isSelected());
            }
        });
        nextCheckBox.setSelected(_dateChooserPanel.isShowNextButton());
        panel.add(Box.createVerticalStrut(6), JideBoxLayout.FIX);

        final JCheckBox nextYearCheckBox = (JCheckBox) panel.add(new JCheckBox("Show Next Year Button"));
        nextYearCheckBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _dateChooserPanel.setShowNextYearButton(nextYearCheckBox.isSelected());
            }
        });
        nextYearCheckBox.setSelected(_dateChooserPanel.isShowNextYearButton());
        panel.add(Box.createVerticalStrut(6), JideBoxLayout.FIX);

        final JCheckBox showYearButtonsCheckBox = (JCheckBox) panel.add(new JCheckBox("Show Year Buttons"));
        showYearButtonsCheckBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _dateChooserPanel.setShowYearButtons(showYearButtonsCheckBox.isSelected());
                prevYearCheckBox.setEnabled(showYearButtonsCheckBox.isSelected());
                nextYearCheckBox.setEnabled(showYearButtonsCheckBox.isSelected());
            }
        });
        showYearButtonsCheckBox.setSelected(_dateChooserPanel.isShowYearButtons());
        panel.add(Box.createVerticalStrut(6), JideBoxLayout.FIX);

        final JCheckBox showYearSpinnerCheckBox = (JCheckBox) panel.add(new JCheckBox("Show Year Spinner"));
        showYearSpinnerCheckBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _dateChooserPanel.setShowYearSpinner(showYearSpinnerCheckBox.isSelected());
            }
        });
        showYearSpinnerCheckBox.setSelected(_dateChooserPanel.isShowYearSpinner());
        panel.add(Box.createVerticalStrut(6), JideBoxLayout.FIX);

        final JCheckBox prevDaysCheckBox = (JCheckBox) panel.add(new JCheckBox("Show Previous Month Days"));
        prevDaysCheckBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _dateChooserPanel.setShowPreviousMonthDays(prevDaysCheckBox.isSelected());
            }
        });
        prevDaysCheckBox.setSelected(_dateChooserPanel.isShowPreviousMonthDays());
        panel.add(Box.createVerticalStrut(6), JideBoxLayout.FIX);

        final JCheckBox nextDaysCheckBox = (JCheckBox) panel.add(new JCheckBox("Show Next Month Days"));
        nextDaysCheckBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _dateChooserPanel.setShowNextMonthDays(nextDaysCheckBox.isSelected());
            }
        });
        nextDaysCheckBox.setSelected(_dateChooserPanel.isShowNextMonthDays());
        panel.add(Box.createVerticalStrut(6), JideBoxLayout.FIX);

        final JCheckBox weekNumbersCheckBox = (JCheckBox) panel.add(new JCheckBox("Show Week Numbers"));
        weekNumbersCheckBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _dateChooserPanel.setShowWeekNumbers(weekNumbersCheckBox.isSelected());
            }
        });
        weekNumbersCheckBox.setSelected(_dateChooserPanel.isShowWeekNumbers());
        panel.add(Box.createVerticalStrut(6), JideBoxLayout.FIX);

        final JCheckBox viewOnlyCheckBox = (JCheckBox) panel.add(new JCheckBox("View Only Mode"));
        viewOnlyCheckBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _dateChooserPanel.setViewOnly(viewOnlyCheckBox.isSelected());
            }
        });
        viewOnlyCheckBox.setSelected(_dateChooserPanel.isViewOnly());
        panel.add(Box.createVerticalStrut(6), JideBoxLayout.FIX);

        final JCheckBox enabledCheckBox = (JCheckBox) panel.add(new JCheckBox("Enabled"));
        enabledCheckBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _dateChooserPanel.setEnabled(enabledCheckBox.isSelected());
            }
        });
        enabledCheckBox.setSelected(_dateChooserPanel.isEnabled());
        panel.add(Box.createVerticalStrut(6), JideBoxLayout.FIX);

        final JCheckBox toggleCheckBox = (JCheckBox) panel.add(new JCheckBox("Toggle Mode (Multi-Interval Selection)"));
        toggleCheckBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _dateChooserPanel.setToggleMode(toggleCheckBox.isSelected());
            }
        });
        toggleCheckBox.setSelected(_dateChooserPanel.isToggleMode());
        panel.add(Box.createVerticalStrut(6), JideBoxLayout.FIX);

        panel.add(Box.createGlue(), JideBoxLayout.VARY);

        return panel;
    }
}
