/*
 * @(#)CalendarViewDemo.java 3/22/2006
 *
 * Copyright 2002 - 2006 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.combobox.*;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.JideBoxLayout;
import com.jidesoft.swing.SearchableUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Locale;

/**
 * Demoed Component: {@link com.jidesoft.combobox.DateChooserPanel} <br> Required jar files: jide-common.jar,
 * jide-grids.jar <br> Required L&F: Jide L&F extension required
 */
public class CalendarViewerDemo extends AbstractDemo {

    private CalendarViewer _calendarViewer;

    private static final String SINGLE = "Single Selection";
    private static final String SINGLE_INTERVAL = "Single Interval Selection";
    private static final String MULTIPLE_INTERVAL = "Multiple Interval Selection";


    public CalendarViewerDemo() {
    }

    public String getName() {
        return "CalendarViewer Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_GRIDS;
    }

    public Component getDemoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        _calendarViewer = createCalendarViewer();
        panel.add(_calendarViewer, BorderLayout.CENTER);
        return panel;
    }

    @Override
    public Component getOptionsPanel() {
        return createOptionPanel();
    }

    @Override
    public String getDescription() {
        return "This is a demo of CalendarViewer. CalendarViewer can display multiple DateChooserPanels in one panel " +
                "and allow you to choose multiple dates across all month viewers. " +
                "Please choose different options in the options pane and try to click on DateChooserPanel to select dates.\n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.combobox.CalendarView\n" +
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
        showAsFrame(new CalendarViewerDemo());
    }

    private CalendarViewer createCalendarViewer() {
        DefaultDateModel model = new DefaultDateModel();
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.YEAR, 2011);
        calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMaximum(Calendar.DAY_OF_YEAR));
        model.setMaxDate(calendar);

        calendar.set(Calendar.YEAR, 1980);
        calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMinimum(Calendar.DAY_OF_YEAR));
        model.setMinDate(calendar);

        CalendarViewer panel = new CalendarViewer(model);
        Calendar currentDate = Calendar.getInstance();
        panel.getSelectionModel().setSelectionMode(DateSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        panel.getSelectionModel().setSelectedDate(currentDate.getTime());
//        panel.getSelectionModel().addDateSelectionListener(new DateSelectionListener() {
//            public void valueChanged(DateSelectionEvent e) {
//                Date[] dates = ((DateSelectionModel) e.getSource()).getSelectedDates();
//                if (dates != null) {
//                    for (int i = 0; i < dates.length; i++) {
//                        Date date = dates[i];
//                        System.out.println(date);
//                    }
//                }
//                else {
//                    System.out.println("empty");
//                }
//                System.out.println("---");
//            }
//        });

        return panel;
    }

    protected Component createOptionPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new JideBoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] selectionMode = new String[]{
                CalendarViewerDemo.SINGLE,
                CalendarViewerDemo.SINGLE_INTERVAL,
                CalendarViewerDemo.MULTIPLE_INTERVAL
        };

        JComboBox comboBox = new JComboBox(selectionMode);
        comboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED && e.getItem() instanceof String) {
                    if ((e.getItem()).equals(CalendarViewerDemo.SINGLE)) {
                        _calendarViewer.getSelectionModel().setSelectionMode(DateSelectionModel.SINGLE_SELECTION);
                    }
                    else if ((e.getItem()).equals(CalendarViewerDemo.SINGLE_INTERVAL)) {
                        _calendarViewer.getSelectionModel().setSelectionMode(DateSelectionModel.SINGLE_INTERVAL_SELECTION);
                    }
                    else {
                        _calendarViewer.getSelectionModel().setSelectionMode(DateSelectionModel.MULTIPLE_INTERVAL_SELECTION);
                    }
                }
            }
        });
        comboBox.setSelectedIndex(_calendarViewer.getSelectionModel().getSelectionMode());


        final DateComboBox minDateComboBox = new DateComboBox();
        minDateComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    _calendarViewer.getDateModel().setMinDate(minDateComboBox.getCalendar());
                }
            }
        });
        minDateComboBox.setCalendar(_calendarViewer.getDateModel().getMinDate());

        final DateComboBox maxDateComboBox = new DateComboBox();
        maxDateComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    _calendarViewer.getDateModel().setMaxDate(maxDateComboBox.getCalendar());
                }
            }
        });
        maxDateComboBox.setCalendar(_calendarViewer.getDateModel().getMaxDate());

        final JComboBox dateValidatorComboBox = new JComboBox(new String[]{
                "<None>",
                "This year",
                "Previous year",
                "Next year",
                "Weekday only",
                "Weekend only"
        });
        dateValidatorComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if (_calendarViewer.getDateModel() instanceof DefaultDateModel) {
                        DateFilter dateFilter = null;
                        switch (dateValidatorComboBox.getSelectedIndex()) {
                            case 0:
                                dateFilter = null;
                                break;
                            case 1:
                                dateFilter = DefaultDateModel.THIS_YEAR_ONLY;
                                break;
                            case 2:
                                dateFilter = DefaultDateModel.PREVIOUS_YEAR_ONLY;
                                break;
                            case 3:
                                dateFilter = DefaultDateModel.NEXT_YEAR_ONLY;
                                break;
                            case 4:
                                dateFilter = DefaultDateModel.WEEKDAY_ONLY;
                                break;
                            case 5:
                                dateFilter = DefaultDateModel.WEEKEND_ONLY;
                                break;

                        }
                        ((DefaultDateModel) _calendarViewer.getDateModel()).clearDateFilters();
                        if (dateFilter != null) {
                            ((DefaultDateModel) _calendarViewer.getDateModel()).addDateFilter(dateFilter);
                        }
                    }
                }
            }
        });

        final JSpinner rowSpinner = new JSpinner();
        rowSpinner.setModel(new SpinnerNumberModel(_calendarViewer.getCalendarDimension().height, 1, 6, 1));
        rowSpinner.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                Integer row = (Integer) rowSpinner.getValue();
                _calendarViewer.setCalendarDimension(new Dimension(_calendarViewer.getCalendarDimension().width, row));
            }
        });

        final JSpinner columnSpinner = new JSpinner();
        columnSpinner.setModel(new SpinnerNumberModel(_calendarViewer.getCalendarDimension().width, 1, 6, 1));
        columnSpinner.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                Integer column = (Integer) columnSpinner.getValue();
                _calendarViewer.setCalendarDimension(new Dimension(column, _calendarViewer.getCalendarDimension().height));
            }
        });

        _calendarViewer.addPropertyChangeListener(CalendarViewer.PROPERTY_CALENDAR_DIMENSION, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                Dimension dim = _calendarViewer.getCalendarDimension();
                columnSpinner.setValue(dim.width);
                rowSpinner.setValue(dim.height);
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

        panel.add(new JLabel("Number of rows"));
        panel.add(Box.createVerticalStrut(6), JideBoxLayout.FIX);
        panel.add(rowSpinner);
        panel.add(Box.createVerticalStrut(12), JideBoxLayout.FIX);
        panel.add(new JLabel("Number of columns"));
        panel.add(Box.createVerticalStrut(6), JideBoxLayout.FIX);
        panel.add(columnSpinner);
        panel.add(Box.createVerticalStrut(12), JideBoxLayout.FIX);

        final JCheckBox viewOnly = new JCheckBox("View Only Mode");
        viewOnly.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _calendarViewer.setViewOnly(viewOnly.isSelected());
            }
        });
        viewOnly.setSelected(_calendarViewer.isViewOnly());
        panel.add(viewOnly);
        panel.add(Box.createVerticalStrut(6), JideBoxLayout.FIX);

        final JCheckBox enabled = new JCheckBox("Enabled");
        enabled.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _calendarViewer.setEnabled(enabled.isSelected());
            }
        });
        enabled.setSelected(_calendarViewer.isEnabled());
        panel.add(enabled);
        panel.add(Box.createVerticalStrut(6), JideBoxLayout.FIX);

        final JCheckBox autoResize = new JCheckBox("Automatically Change Dimension");
        autoResize.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _calendarViewer.setAutoChangeDimension(autoResize.isSelected());
            }
        });
        autoResize.setSelected(_calendarViewer.isAutoChangeDimension());
        panel.add(autoResize);
        panel.add(Box.createVerticalStrut(12), JideBoxLayout.FIX);

        Locale[] locales = Locale.getAvailableLocales();
        Arrays.sort(locales, new Comparator() {
            public int compare(Object o1, Object o2) {
                if (o1 instanceof Locale && o2 instanceof Locale) {
                    Locale l1 = (Locale) o1;
                    Locale l2 = (Locale) o2;
                    return l1.toString().compareTo(l2.toString());
                }
                return 0;
            }
        });
        JComboBox locale = new JComboBox(locales);
        locale.setSelectedItem(Locale.getDefault());
        locale.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED && e.getItem() instanceof Locale) {
                    Locale l = (Locale) e.getItem();
                    _calendarViewer.setLocale(l);
                }
            }
        });
        SearchableUtils.installSearchable(locale);

        panel.add(new JLabel("Set Locale"));
        panel.add(Box.createVerticalStrut(6), JideBoxLayout.FIX);
        panel.add(locale);
        panel.add(Box.createGlue(), JideBoxLayout.VARY);

        return panel;
    }
}
