/*
 * @(#)DateChooserDemo.java 2/12/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.combobox.DateChooserPanel;
import com.jidesoft.combobox.DateComboBox;
import com.jidesoft.combobox.DateFilter;
import com.jidesoft.combobox.DefaultDateModel;
import com.jidesoft.converter.ObjectConverterManager;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.JideBoxLayout;
import com.jidesoft.swing.JideSwingUtilities;
import com.jidesoft.swing.SearchableUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Locale;

/**
 * Demoed Component: {@link DateComboBox}, {@link DateChooserPanel} <br> Required jar files: jide-common.jar,
 * jide-grids.jar <br> Required L&F: Jide L&F extension required
 */
public class DateComboBoxDemo extends AbstractDemo {

    private DateComboBox _dateComboBox;
    private SimpleDateFormat _dateFormat;
    public JLabel _valueLabel;

    public DateComboBoxDemo() {
    }

    public String getName() {
        return "DateComboBox Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_GRIDS;
    }

    public Component getDemoPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 0, 2, 2));
        _dateComboBox = createDateComboBox();

        _valueLabel = new JLabel();
        _dateComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    _valueLabel.setText("Date selected: " + ObjectConverterManager.toString(e.getItem()));
                }
            }
        });

        panel.add(_dateComboBox);
        panel.add(_valueLabel);
        return JideSwingUtilities.createTopPanel(panel);
    }

    @Override
    public Component getOptionsPanel() {
        return createOptionPanel();
    }

    @Override
    public String getDescription() {
        return "This is a demo of DateChooserPanel and DateComboBox. Please choose different options in the options pane and click on the drop down button of DateComboBox to see the difference.\n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.combobox.DateComboBox\n" +
                "com.jidesoft.combobox.DateChooserPanel\n" +
                "com.jidesoft.combobox.DateFilter\n" +
                "com.jidesoft.combobox.DefaultDateModel";
    }

    @Override
    public String getDemoFolder() {
        return "G5.DateComboBoxes";
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new DateComboBoxDemo());
    }

    private DateComboBox createDateComboBox() {
        DefaultDateModel model = new DefaultDateModel();

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.YEAR, 2010);
        calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMaximum(Calendar.DAY_OF_YEAR));
        model.setMaxDate(calendar);

        calendar.set(Calendar.YEAR, 1980);
        calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMinimum(Calendar.DAY_OF_YEAR));
        model.setMinDate(calendar);

        DateComboBox dateComboBox = new DateComboBox(model);

        Calendar prototypeValue = Calendar.getInstance();
        prototypeValue.set(Calendar.YEAR, 2000);
        prototypeValue.set(Calendar.MONDAY, 8);
        prototypeValue.set(Calendar.DAY_OF_MONTH, 30);
        dateComboBox.setPrototypeDisplayValue(prototypeValue);
        Calendar currentDate = Calendar.getInstance();
        dateComboBox.setCalendar(currentDate);

        dateComboBox.setLocale(Locale.getDefault());
        return dateComboBox;
    }

    protected Component createOptionPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new JideBoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] formatters = new String[]{
                "(Default)",
                "MMM dd, yyyy",
                "MM/dd/yy",
                "yyyy.MM.dd",
                "EEE M/dd/yyyy",
                "EEE, MMM d, ''yy",
                "yyyyy.MMMMM.dd GGG",
                "EEE, d MMM yyyy",
                "yyMMdd"
        };

        JComboBox comboBox = new JComboBox(formatters);
        comboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED && e.getItem() instanceof String) {
                    if ((e.getItem()).equals("(Default)")) {
                        _dateFormat = null;
                        _dateComboBox.setFormat(null);
                    }
                    else {
                        SimpleDateFormat dateFormat = new SimpleDateFormat((String) e.getItem());
                        _dateFormat = dateFormat;
                        _dateComboBox.setFormat(_dateFormat);
                    }
                }
            }
        });

        final DateComboBox minDateComboBox = new DateComboBox();
        minDateComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    _dateComboBox.getDateModel().setMinDate(minDateComboBox.getCalendar());
                }
            }
        });
        minDateComboBox.setCalendar(_dateComboBox.getDateModel().getMinDate());

        final DateComboBox maxDateComboBox = new DateComboBox();
        maxDateComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    _dateComboBox.getDateModel().setMaxDate(maxDateComboBox.getCalendar());
                }
            }
        });
        maxDateComboBox.setCalendar(_dateComboBox.getDateModel().getMaxDate());

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
                    if (_dateComboBox.getDateModel() instanceof DefaultDateModel) {
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
                        ((DefaultDateModel) _dateComboBox.getDateModel()).clearDateFilters();
                        if (dateFilter != null) {
                            ((DefaultDateModel) _dateComboBox.getDateModel()).addDateFilter(dateFilter);
                        }
                    }
                }
            }
        });

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
                    _dateComboBox.setLocale(l);
                    SwingUtilities.updateComponentTreeUI(_dateComboBox);
                }
            }
        });
        SearchableUtils.installSearchable(locale);

        final JComboBox focusLostComboBox = new JComboBox(new String[]{"Commit", "Commit or Revert", "Revert", "Persist", "Commit or Reset", "Reset"});
        focusLostComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    _dateComboBox.setFocusLostBehavior(focusLostComboBox.getSelectedIndex());
                }
            }
        });
        focusLostComboBox.setSelectedIndex(_dateComboBox.getFocusLostBehavior());

        panel.add(new JLabel("Set DateFormat"));
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
        panel.add(new JLabel("Focus Lost Behavior"));
        panel.add(Box.createVerticalStrut(6), JideBoxLayout.FIX);
        panel.add(focusLostComboBox);
        panel.add(Box.createVerticalStrut(12), JideBoxLayout.FIX);
        final JCheckBox todayCheckBox = (JCheckBox) panel.add(new JCheckBox("Show Today Button"));
        todayCheckBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _dateComboBox.setShowTodayButton(todayCheckBox.isSelected());
            }
        });
        todayCheckBox.setSelected(_dateComboBox.isShowTodayButton());
        panel.add(Box.createVerticalStrut(6), JideBoxLayout.FIX);
        final JCheckBox noneCheckBox = (JCheckBox) panel.add(new JCheckBox("Show None Button"));
        noneCheckBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _dateComboBox.setShowNoneButton(noneCheckBox.isSelected());
            }
        });
        noneCheckBox.setSelected(_dateComboBox.isShowNoneButton());
        panel.add(Box.createVerticalStrut(6), JideBoxLayout.FIX);

        final JCheckBox weekNumbersCheckBox = (JCheckBox) panel.add(new JCheckBox("Show Week Numbers"));
        weekNumbersCheckBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _dateComboBox.setShowWeekNumbers(weekNumbersCheckBox.isSelected());
            }
        });
        weekNumbersCheckBox.setSelected(_dateComboBox.isShowWeekNumbers());
        panel.add(Box.createVerticalStrut(6), JideBoxLayout.FIX);

        final JCheckBox showTimeCheckBox = (JCheckBox) panel.add(new JCheckBox("Display Time"));
        panel.add(Box.createVerticalStrut(6), JideBoxLayout.FIX);
        final JCheckBox okCheckBox = (JCheckBox) panel.add(new JCheckBox("Show OK Button"));
        showTimeCheckBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _dateComboBox.setTimeDisplayed(showTimeCheckBox.isSelected());
                okCheckBox.setEnabled(!showTimeCheckBox.isSelected());
                if (showTimeCheckBox.isSelected()) {
                    _dateComboBox.setFormat(SimpleDateFormat.getDateTimeInstance());
                    okCheckBox.setSelected(true);
                }
                else {
                    _dateComboBox.setFormat(_dateFormat);
                    okCheckBox.setSelected(false);
                }
            }
        });
        showTimeCheckBox.setSelected(_dateComboBox.isTimeDisplayed());

        okCheckBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _dateComboBox.setShowOKButton(okCheckBox.isSelected());
            }
        });
        okCheckBox.setSelected(_dateComboBox.isShowOKButton());
        okCheckBox.setEnabled(!showTimeCheckBox.isSelected());
        panel.add(Box.createVerticalStrut(6), JideBoxLayout.FIX);

        final JCheckBox editableCheckBox = (JCheckBox) panel.add(new JCheckBox("Editable"));
        editableCheckBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _dateComboBox.setEditable(editableCheckBox.isSelected());
            }
        });
        editableCheckBox.setSelected(_dateComboBox.isEditable());
        panel.add(Box.createVerticalStrut(6), JideBoxLayout.FIX);

        final JCheckBox invalidCheckBox = (JCheckBox) panel.add(new JCheckBox("Alllow Invalid Value"));
        invalidCheckBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _dateComboBox.setInvalidValueAllowed(invalidCheckBox.isSelected());
            }
        });
        invalidCheckBox.setSelected(_dateComboBox.isInvalidValueAllowed());
        panel.add(Box.createVerticalStrut(6), JideBoxLayout.FIX);

        panel.add(new JLabel("Set Locale"));
        panel.add(Box.createVerticalStrut(6), JideBoxLayout.FIX);
        panel.add(locale);
        panel.add(Box.createGlue(), JideBoxLayout.VARY);

        return panel;
    }
}
