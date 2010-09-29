/*
 * @(#)GanttChartDemo.java 7/27/2009
 *
 * Copyright 2002 - 2009 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.combobox.DateComboBox;
import com.jidesoft.gantt.*;
import com.jidesoft.grid.CellEditorManager;
import com.jidesoft.grid.CellRendererManager;
import com.jidesoft.plaf.GanttUIDefaultsCustomizer;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.range.Range;
import com.jidesoft.range.TimeRange;
import com.jidesoft.scale.*;
import com.jidesoft.swing.CornerScroller;
import com.jidesoft.swing.JideBoxLayout;
import com.jidesoft.swing.JideSwingUtilities;
import com.jidesoft.utils.SwingWorker;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.InternationalFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;

@SuppressWarnings("serial")
public class GanttChartDemo extends AbstractDemo {

    protected GanttChart<Date, DefaultGanttEntry<Date>> _ganttChart;
    protected final boolean _largeModel = true;
    protected final boolean _debug = false;

    public GanttChartDemo() {
    }

    public String getName() {
        return "Gantt Chart Demo";
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
        return "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.gantt.GanttChartPane";
    }


    @Override
    public Component getOptionsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new JideBoxLayout(panel, JideBoxLayout.Y_AXIS, 5));

        NumberFormat format = DecimalFormat.getInstance();
        format.setGroupingUsed(false);
        format.setMinimumFractionDigits(0);
        format.setMaximumFractionDigits(0);
        format.setMinimumIntegerDigits(1);
        format.setMaximumIntegerDigits(Integer.MAX_VALUE);
        InternationalFormatter formatter = new InternationalFormatter(format) {
            @Override
            protected DocumentFilter getDocumentFilter() {
                return new IntegerFilter(false);
            }
        };
        formatter.setAllowsInvalid(false);
        formatter.setMinimum(1);
        formatter.setMaximum(Integer.MAX_VALUE);
        final JFormattedTextField widthTextField = new JFormattedTextField(formatter);
        widthTextField.setValue(_ganttChart.getScaleArea().getPreferredPeriodSize().width);
        widthTextField.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Dimension size = _ganttChart.getScaleArea().getPreferredPeriodSize();
                size.width = (Integer) widthTextField.getValue();
                _ganttChart.getScaleArea().setPreferredPeriodSize(size);
            }
        });

        List<Period> periods = _ganttChart.getModel().getScaleModel().getPeriods();
        SpinnerNumberModel model = new SpinnerNumberModel(3, 1, periods.size(), 1);
        final JSpinner spinner = new JSpinner(model);
        spinner.setValue(_ganttChart.getScaleArea().getVisiblePeriods().size());
        spinner.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                ScaleArea<Date> scaleArea = _ganttChart.getScaleArea();
                List<Period> periods = scaleArea.getScaleModel().getPeriods();
                int value = (Integer) spinner.getValue();
                int fromIndex = Math.min(periods.indexOf(scaleArea.getSmallestVisiblePeriod()),
                        periods.size() - value);
                int toIndex = fromIndex + value;
                scaleArea.setVisiblePeriods(periods.subList(fromIndex, toIndex));
            }
        });

        JButton button = new JButton(new AbstractAction("Resize periods to fit") {
            public void actionPerformed(ActionEvent e) {
                _ganttChart.autoResizePeriods(false);
                spinner.setValue(_ganttChart.getScaleArea().getVisiblePeriods().size());
                widthTextField.setValue(_ganttChart.getScaleArea().getPreferredPeriodSize().width);
            }
        });
        panel.add(button);

        button = new JButton(new AbstractAction("Resize periods to fit visible rows") {
            public void actionPerformed(ActionEvent e) {
                _ganttChart.autoResizePeriods(true);
                spinner.setValue(_ganttChart.getScaleArea().getVisiblePeriods().size());
                widthTextField.setValue(_ganttChart.getScaleArea().getPreferredPeriodSize().width);
            }
        });
        panel.add(button);

        panel.add(JideSwingUtilities.createLabeledComponent(
                new JLabel("Number of period tiers: "), spinner, BorderLayout.LINE_START));

        panel.add(JideSwingUtilities.createLabeledComponent(
                new JLabel("Smallest period width: "), widthTextField, BorderLayout.LINE_START));

        TexturePaint striped = PaintedTexture.createDiagonalLines(10, 10,
                _ganttChart.getGridColor(), true, false);
        final DatePeriodMarker marker = new DatePeriodMarker(striped, _ganttChart.getGridColor(), null,
                DatePeriod.DAY_OF_WEEK, Calendar.SATURDAY, Calendar.SUNDAY);
        _ganttChart.addPeriodBackgroundPainter(marker);

        final JCheckBox markCheckBox = new JCheckBox();
        markCheckBox.setAction(new AbstractAction("Mark weekends") {
            private static final long serialVersionUID = -7237041042930729077L;

            public void actionPerformed(ActionEvent e) {
                GanttChart<Date, DefaultGanttEntry<Date>> chart = _ganttChart;
                if (markCheckBox.isSelected()) {
                    chart.addPeriodBackgroundPainter(marker);
                }
                else {
                    chart.removePeriodBackgroundPainter(marker);
                }
                chart.repaint();
            }
        });
        markCheckBox.setSelected(true);
        panel.add(markCheckBox);

        final IntervalMarker<Date> todayMarker = new IntervalMarker<Date>(Color.RED, null, null) {
            @Override
            public Date getStartInstant() {
                return new Date();
            }

            @Override
            public Date getEndInstant() {
                return new Date();
            }
        };
        final JCheckBox todayMarkCheckBox = new JCheckBox();
        todayMarkCheckBox.setAction(new AbstractAction("Mark today") {
            private static final long serialVersionUID = -7237041042930729077L;

            public void actionPerformed(ActionEvent e) {
                GanttChart<Date, DefaultGanttEntry<Date>> chart = _ganttChart;
                if (todayMarkCheckBox.isSelected()) {
                    chart.addPeriodBackgroundPainter(todayMarker);
                }
                else {
                    chart.removePeriodBackgroundPainter(todayMarker);
                }
                chart.repaint();
            }
        });
        todayMarkCheckBox.setSelected(false);
        panel.add(todayMarkCheckBox);

        final IntervalMarker<Date> customMarker = new IntervalMarker<Date>();
        customMarker.setBackgroundPaint(new GradientPaint(0f, 0f, new Color(255, 255, 0, 31),
                0f, 1000f, new Color(255, 255, 0, 223)));
        customMarker.setOutlinePaint(new Color(255, 255, 0));

        final DateComboBox startCombobox = new DateComboBox();
        startCombobox.setShowNoneButton(false);
        startCombobox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                customMarker.setStartInstant(startCombobox.getDate());
                _ganttChart.repaint();
            }
        });
        final DateComboBox endCombobox = new DateComboBox();
        endCombobox.setShowNoneButton(false);
        endCombobox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                customMarker.setEndInstant(endCombobox.getDate());
                _ganttChart.repaint();
            }
        });
        final JCheckBox customMarkCheckBox = new JCheckBox();
        customMarkCheckBox.setAction(new AbstractAction("Custom period") {
            private static final long serialVersionUID = -7237041042930729077L;

            public void actionPerformed(ActionEvent e) {
                GanttChart<Date, DefaultGanttEntry<Date>> chart = _ganttChart;
                startCombobox.setEnabled(customMarkCheckBox.isSelected());
                endCombobox.setEnabled(customMarkCheckBox.isSelected());
                if (customMarkCheckBox.isSelected()) {
                    chart.addPeriodBackgroundPainter(customMarker);
                }
                else {
                    chart.removePeriodBackgroundPainter(customMarker);
                }
                chart.repaint();
            }
        });
        customMarkCheckBox.setSelected(false);
        startCombobox.setDate(new Date());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.WEEK_OF_YEAR, 1);
        endCombobox.setDate(calendar.getTime());
        panel.add(customMarkCheckBox);
        panel.add(JideSwingUtilities.createLabeledComponent(
                new JLabel("Start: "), startCombobox, BorderLayout.LINE_START));
        panel.add(JideSwingUtilities.createLabeledComponent(
                new JLabel("End:   "), endCombobox, BorderLayout.LINE_START));


        final JCheckBox dragCheckBox = new JCheckBox();
        dragCheckBox.setAction(new AbstractAction("View mode") {
            private static final long serialVersionUID = -7237041042930729077L;

            {
                putValue(Action.SHORT_DESCRIPTION, "Centers the chart on mouse click and allows panning with mouse drag");
            }

            public void actionPerformed(ActionEvent e) {
                _ganttChart.setViewMode(dragCheckBox.isSelected() ? 1 : 0);
            }
        });
        dragCheckBox.setSelected(false);
        panel.add(dragCheckBox);

        final JCheckBox gridCheckBox = new JCheckBox();
        gridCheckBox.setAction(new AbstractAction("Show horizontal grid lines") {
            private static final long serialVersionUID = -7237041042930729077L;

            public void actionPerformed(ActionEvent e) {
                _ganttChart.setShowGrid(gridCheckBox.isSelected());
            }
        });
        gridCheckBox.setSelected(_ganttChart.isShowGrid());
        panel.add(gridCheckBox);

        panel.add(new JLabel("Select two entries to change their relation:"));
        Integer[] relationOptions = {
                -1,
                GanttEntryRelation.ENTRY_RELATION_FINISH_TO_FINISH,
                GanttEntryRelation.ENTRY_RELATION_FINISH_TO_START,
                GanttEntryRelation.ENTRY_RELATION_START_TO_FINISH,
                GanttEntryRelation.ENTRY_RELATION_START_TO_START
        };
        final JComboBox relationComboBox = new JComboBox(relationOptions);
        relationComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                if (value != null) {
                    switch ((Integer) value) {
                        case GanttEntryRelation.ENTRY_RELATION_FINISH_TO_FINISH:
                            value = "Finish-to-Finish";
                            break;
                        case GanttEntryRelation.ENTRY_RELATION_FINISH_TO_START:
                            value = "Finish-to-Start";
                            break;
                        case GanttEntryRelation.ENTRY_RELATION_START_TO_FINISH:
                            value = "Start-to-Finish";
                            break;
                        case GanttEntryRelation.ENTRY_RELATION_START_TO_START:
                            value = "Start-to-Start";
                            break;
                        default:
                            value = "None";
                    }
                }
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });
        relationComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED
                        && relationComboBox.getClientProperty("adjusting") == null) {
                    changeRelation(_ganttChart, (Integer) relationComboBox.getSelectedItem());
                }
            }
        });
        panel.add(JideSwingUtilities.createLabeledComponent(new JLabel("Relation: "), relationComboBox, BorderLayout.BEFORE_LINE_BEGINS));

        ListSelectionListener listener = new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                relationComboBox.putClientProperty("adjusting", true);
                if (_ganttChart.getSelectedRowCount() == 2) {
                    GanttEntryRelation<DefaultGanttEntry<Date>> relation = getSelectedRelation(_ganttChart);
                    if (relation != null) {
                        relationComboBox.setSelectedItem(relation.getRelationType());
                    }
                    else {
                        relationComboBox.setSelectedItem(null);
                    }
                    relationComboBox.setEnabled(true);
                }
                else {
                    relationComboBox.setSelectedItem(null);
                    relationComboBox.setEnabled(false);
                }
                relationComboBox.putClientProperty("adjusting", null);
            }
        };
        listener.valueChanged(null);
        _ganttChart.getSelectionModel().addListSelectionListener(listener);

        final String[] labelOptions = {
                "None",
                "Before the bar",
                "After the bar",
        };
        JComboBox labelComboBox = new JComboBox(labelOptions);
        labelComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if (labelOptions[0].equals(e.getItem())) {
                        _ganttChart.setDefaultLabelRenderer(null);
                        _ganttChart.repaint();
                    }
                    else if (labelOptions[1].equals(e.getItem())) {
                        _ganttChart.setDefaultLabelRenderer(new DefaultGanttLabelRenderer());
                        _ganttChart.setLabelPosition(SwingConstants.LEADING);
                        _ganttChart.repaint();
                    }
                    else if (labelOptions[2].equals(e.getItem())) {
                        _ganttChart.setDefaultLabelRenderer(new DefaultGanttLabelRenderer());
                        _ganttChart.setLabelPosition(SwingConstants.TRAILING);
                        _ganttChart.repaint();
                    }
                }

            }
        });
        panel.add(JideSwingUtilities.createLabeledComponent(new JLabel("Labeling: "), labelComboBox, BorderLayout.BEFORE_LINE_BEGINS));

        return panel;
    }

    protected GanttEntryRelation<DefaultGanttEntry<Date>> getSelectedRelation(GanttChart<Date, DefaultGanttEntry<Date>> chart) {
        int[] selectedRows = chart.getSelectedRows();
        if (selectedRows != null && selectedRows.length == 2) {
            int leadRow = chart.getLeadSelectionIndex();
            int anchorRow;
            if (selectedRows[0] == leadRow) {
                anchorRow = selectedRows[1];
            }
            else if (selectedRows[1] == leadRow) {
                anchorRow = selectedRows[0];
            }
            else {
                leadRow = selectedRows[0];
                anchorRow = selectedRows[1];
            }
            GanttModel<Date, DefaultGanttEntry<Date>> ganttModel = chart.getModel();
            GanttEntryRelationModel<DefaultGanttEntry<Date>> relationModel = ganttModel.getGanttEntryRelationModel();
            DefaultGanttEntry<Date> startEntry = ganttModel.getEntryAt(leadRow);
            DefaultGanttEntry<Date> endEntry = ganttModel.getEntryAt(anchorRow);
            return relationModel.getEntryRelation(startEntry, endEntry);
        }
        else {
            return null;
        }
    }

    protected void changeRelation(GanttChart<Date, DefaultGanttEntry<Date>> chart, Integer relation) {
        int[] selectedRows = chart.getSelectedRows();
        if (selectedRows != null && selectedRows.length == 2) {
            int leadRow = chart.getLeadSelectionIndex();
            int anchorRow;
            if (selectedRows[0] == leadRow) {
                anchorRow = selectedRows[1];
            }
            else if (selectedRows[1] == leadRow) {
                anchorRow = selectedRows[0];
            }
            else {
                leadRow = selectedRows[0];
                anchorRow = selectedRows[1];
            }
            GanttModel<Date, DefaultGanttEntry<Date>> ganttModel = chart.getModel();
            GanttEntryRelationModel<DefaultGanttEntry<Date>> relationModel = ganttModel.getGanttEntryRelationModel();
            DefaultGanttEntry<Date> startEntry = ganttModel.getEntryAt(leadRow);
            DefaultGanttEntry<Date> endEntry = ganttModel.getEntryAt(anchorRow);
            GanttEntryRelation<DefaultGanttEntry<Date>> existingRelation = relationModel.getEntryRelation(startEntry, endEntry);
            if (existingRelation != null) {
                relationModel.removeEntryRelation(existingRelation);
            }
            if (relation != null && relation != -1) {
                relationModel.addEntryRelation(new DefaultGanttEntryRelation<DefaultGanttEntry<Date>>(startEntry, endEntry, relation));
            }
        }
    }

    public static class IntegerFilter extends DocumentFilter {

        private final boolean allowNegative;

        public IntegerFilter(boolean allowNegative) {
            this.allowNegative = allowNegative;
        }

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            super.insertString(fb, offset, removeNonDigitsCharacters(string, allowNegative, false), attr);
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String string, AttributeSet attr) throws BadLocationException {
            super.replace(fb, offset, length, removeNonDigitsCharacters(string, allowNegative, false), attr);
        }

        private String removeNonDigitsCharacters(String string, boolean allowNegative,
                                                 boolean allowDecimal) {
            StringBuilder builder = new StringBuilder(string);
            for (int i = builder.length() - 1; i >= 0; i--) {
                int cp = builder.codePointAt(i);
                if (!Character.isDigit(cp) && (!allowNegative || cp != '-')
                        && (!allowDecimal || cp != ',' || cp != '.')) {
                    builder.deleteCharAt(i);
                    if (Character.isSupplementaryCodePoint(cp)) {
                        i--;
                        builder.deleteCharAt(i);
                    }
                }
            }
            return builder.toString();
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        _ganttChart = null;
    }

    public Component getDemoPanel() {
        CellRendererManager.initDefaultRenderer();
        CellEditorManager.initDefaultEditor();

        DefaultGanttModel<Date, DefaultGanttEntry<Date>> model = createGanttModel();

        _ganttChart = new DateGanttChart<DefaultGanttEntry<Date>>(model);
        _ganttChart.setShowGrid(false);

        _ganttChart.getScaleArea().addPopupMenuCustomizer(new VisiblePeriodsPopupMenuCustomizer<Date>());
        _ganttChart.getScaleArea().addPopupMenuCustomizer(new ResizePeriodsPopupMenuCustomizer<Date>(_ganttChart));

        GanttChartPopupMenuInstaller installer = new GanttChartPopupMenuInstaller(_ganttChart);
        installer.addGanttChartPopupMenuCustomizer(new RelationGanttChartPopupMenuCustomizer());

        if (_largeModel) {
            _ganttChart.getScaleArea().setVisiblePeriods(Arrays.asList(
                    DatePeriod.WEEK_OF_YEAR, DatePeriod.MONTH, DatePeriod.QUARTER, DatePeriod.YEAR));
        }

        JScrollPane chartScroll = new JScrollPane(_ganttChart);
        chartScroll.setCorner(JScrollPane.LOWER_RIGHT_CORNER, new CornerScroller(chartScroll));
        return chartScroll;
    }

    protected DefaultGanttModel<Date, DefaultGanttEntry<Date>> createGanttModel() {
        DefaultGanttModel<Date, DefaultGanttEntry<Date>> model = new DefaultGanttModel<Date, DefaultGanttEntry<Date>>();

        DateScaleModel scaleModel = new DateScaleModel();

        model.setScaleModel(scaleModel);

        if (_largeModel) {
            createLargeModel(model);
        }
        else {
            Calendar today = Calendar.getInstance();
            Calendar dayBefore2 = Calendar.getInstance();
            dayBefore2.add(Calendar.DAY_OF_WEEK, -2);
            Calendar dayAfter7 = Calendar.getInstance();
            dayAfter7.add(Calendar.DAY_OF_WEEK, 7);
            Calendar dayAfter5 = Calendar.getInstance();
            dayAfter5.add(Calendar.DAY_OF_WEEK, 5);
            Calendar dayAfter2 = Calendar.getInstance();
            dayAfter2.add(Calendar.DAY_OF_WEEK, 2);
            dayAfter2.add(Calendar.DAY_OF_WEEK, 2);

            model.setRange(new TimeRange(dayBefore2.getTime(), dayAfter7.getTime()));
            DefaultGanttEntry<Date> group = new DefaultGanttEntry<Date>("Task 1", Date.class, new TimeRange(today.getTime(), dayAfter5.getTime()), 0);
            group.setExpanded(true);

            DefaultGanttEntry<Date> subgroup = new DefaultGanttEntry<Date>("Sub group",
                    Date.class, new TimeRange(today.getTime(), dayAfter5.getTime()), 0);
            subgroup.setExpanded(true);

            for (int relation : new int[]{
                    GanttEntryRelation.ENTRY_RELATION_FINISH_TO_START,
                    GanttEntryRelation.ENTRY_RELATION_START_TO_FINISH,
                    GanttEntryRelation.ENTRY_RELATION_FINISH_TO_FINISH,
                    GanttEntryRelation.ENTRY_RELATION_START_TO_START}) {

// test subentries                
//                DefaultGanttEntry<Date> child1 = new DefaultGanttEntry<Date>("Task 1." + relation,
//                        Date.class, new TimeRange(today.getTime(), dayAfter7.getTime()), 0.4);
//                child1.addSubEntry(new DefaultGanttEntry<Date>("Task 1.1." + relation,
//                        Date.class, new TimeRange(today.getTime(), dayAfter2.getTime()), 0.4));
//                child1.addSubEntry(new DefaultGanttEntry<Date>("Task 1.2." + relation,
//                        Date.class, new TimeRange(dayAfter5.getTime(), dayAfter7.getTime()), 0.4));
                DefaultGanttEntry<Date> child1 = new DefaultGanttEntry<Date>("Task 1." + relation,
                        Date.class, new TimeRange(today.getTime(), dayAfter2.getTime()), 0.4);
                DefaultGanttEntry<Date> child2 = new DefaultGanttEntry<Date>("Task 2." + relation,
                        Date.class, new TimeRange(dayAfter2.getTime(), dayAfter5.getTime()), 0);

                group.addChild(child1);
                if (_debug) {
                    group.addChild(new DefaultGanttEntry<Date>("Task 2." + relation + "spacer",
                            new TimeRange(today.getTime(), today.getTime())));
                }
                group.addChild(child2);

                if (_debug) {
                    child1 = new DefaultGanttEntry<Date>("Task 1." + relation,
                            Date.class, new TimeRange(today.getTime(), dayAfter2.getTime()), 0.4);

                    subgroup.addChild(child1);

                    model.getGanttEntryRelationModel().addEntryRelation(new DefaultGanttEntryRelation<DefaultGanttEntry<Date>>(child1, child2, relation));
                }
                else {
                    model.getGanttEntryRelationModel().addEntryRelation(new DefaultGanttEntryRelation<DefaultGanttEntry<Date>>(child1, child2, relation));
                }
            }

            if (_debug) {
                group.addChild(subgroup);
            }
            model.addGanttEntry(group);
        }
        return model;
    }


    private void createLargeModel(final DefaultGanttModel<Date, DefaultGanttEntry<Date>> model) {
        final int min = 1, max = 1001;
        final JDialog dialog = new JDialog((Frame) null, "Creating large Gantt model...", true);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialog.setPreferredSize(new Dimension(200, 10));
        dialog.pack();
        dialog.setLocationRelativeTo(null);

        final ProgressMonitor monitor = new ProgressMonitor(dialog,
                "Creating large Gantt model...", "Press 'Cancel' to use partial model", min, max);
        monitor.setMillisToDecideToPopup(0);
        monitor.setMillisToPopup(0);

        new SwingWorker<Void, Integer>() {
            @Override
            protected Void doInBackground() throws Exception {
                model.setAutoUpdateRange(false);

                Random random = new Random();

                Calendar calendar = Calendar.getInstance();

                Range<Date> span = new TimeRange(
                        model.getScaleModel().getDefaultStart(),
                        model.getScaleModel().getDefaultEnd());
                model.setRange(span);

                for (int n = min; n < max; n++) {
                    DefaultGanttEntry<Date> group = new DefaultGanttEntry<Date>("TaskGroup " + n, Date.class, null, 0);
                    List<DefaultGanttEntry<Date>> linked = new ArrayList<DefaultGanttEntry<Date>>();
                    group.setExpanded(true);

                    calendar.setTime(model.getScaleModel().getDefaultStart());
                    Date start = calendar.getTime();
                    Date end = model.getScaleModel().getDefaultEnd();

                    calendar.add(Calendar.DAY_OF_WEEK, random.nextInt(14) + 1);
                    Date current = calendar.getTime();

                    int groupCount = 1;
                    DefaultGanttEntry<Date> currentGroup = group;

                    for (int i = 1; current.before(end); i++) {
                        if (random.nextInt(10) > 8) {
                            DefaultGanttEntry<Date> newGroup = new DefaultGanttEntry<Date>("SubGroup " + n + "." + groupCount, Date.class, null, 0);
                            newGroup.setExpanded(true);
                            currentGroup.addChild(newGroup);

                            currentGroup = newGroup;
                            groupCount++;

                            if (random.nextInt(10) > 3) {
                                linked.add(currentGroup);
                            }
                        }

                        DefaultGanttEntry<Date> entry = new DefaultGanttEntry<Date>(
                                "Task " + n + "." + i, Date.class, new TimeRange(start, current), 0);
                        currentGroup.addChild(entry);

                        if (random.nextInt(20) > 18) {
                            DefaultGanttEntry<Date> milestone = new DefaultGanttEntry<Date>("Milestone " + n + "." + i,
                                    Date.class, new TimeRange(current, current), 0);
                            currentGroup.addChild(milestone);

                            linked.add(milestone);
                        }

                        if (random.nextInt(10) > 3) {
                            linked.add(entry);
                        }

                        if (linked.size() > 1 && random.nextInt(10) > 6) {
                            DefaultGanttEntry<Date> from = linked.remove(random.nextInt(linked.size()));
                            DefaultGanttEntry<Date> to = linked.remove(random.nextInt(linked.size()));
                            int type;
                            switch (random.nextInt(4)) {
                                case 0:
                                    type = GanttEntryRelation.ENTRY_RELATION_FINISH_TO_FINISH;
                                    break;
                                case 1:
                                    type = GanttEntryRelation.ENTRY_RELATION_START_TO_START;
                                    break;
                                case 2:
                                    type = GanttEntryRelation.ENTRY_RELATION_START_TO_FINISH;
                                    break;
                                case 3:
                                default:
                                    type = GanttEntryRelation.ENTRY_RELATION_FINISH_TO_START;
                            }

                            model.getGanttEntryRelationModel().addEntryRelation(new DefaultGanttEntryRelation<DefaultGanttEntry<Date>>(from, to, type));
                        }

                        calendar.add(Calendar.DAY_OF_WEEK, random.nextInt(7) - 4);
                        start = calendar.getTime();
                        calendar.add(Calendar.DAY_OF_WEEK, random.nextInt(14) + 1);
                        current = calendar.getTime();

                        if (monitor.isCanceled()) {
                            currentGroup.addChild(new DefaultGanttEntry<Date>("Last task of " + n,
                                    Date.class, new TimeRange(end, current), 0));


                            updateRange(group);
                            model.addGanttEntry(group);
                            model.setAutoUpdateRange(true);

                            cancel(true);
                            return null;
                        }
                    }

                    currentGroup.addChild(new DefaultGanttEntry<Date>("Last task of " + n,
                            Date.class, new TimeRange(end, current), 0));

                    updateRange(group);
                    model.addGanttEntry(group);
                    model.setAutoUpdateRange(true);

                    publish(n);
                }

                return null;
            }

            private TimeRange createUnion(TimeRange span, TimeRange entrySpan) {
                if (span == null) {
                    return entrySpan;
                }
                Date min = entrySpan.lower().before(span.lower()) ? entrySpan.lower() : span.lower();
                Date max = entrySpan.upper().after(span.upper()) ? entrySpan.upper() : span.upper();
                return new TimeRange(min, max);
            }

            @SuppressWarnings("unchecked")
            private TimeRange updateRange(DefaultGanttEntry<Date> group) {
                Range<Date> span = group.getRange();
                for (int i = 0; i < group.getChildrenCount(); i++) {
                    DefaultGanttEntry<Date> childEntry = (DefaultGanttEntry<Date>) group.getChildAt(i);
                    Range<Date> childSpan;
                    if (childEntry.hasChildren()) {
                        childSpan = updateRange(childEntry);
                    }
                    else {
                        childSpan = childEntry.getRange();
                    }

                    span = createUnion((TimeRange) span, (TimeRange) childSpan);
                }
                group.setRange(span);
                return (TimeRange) span;
            }

            @Override
            protected void process(List<Integer> chunks) {
                if (!chunks.isEmpty()) {
                    monitor.setProgress(chunks.get(chunks.size() - 1));
                }
            }

            @Override
            protected void done() {
                dialog.dispose();
            }
        }.execute();

        dialog.setVisible(true);
    }

    public static void main(String[] s) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                LookAndFeelFactory.addUIDefaultsCustomizer(new GanttUIDefaultsCustomizer());
                LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
                showAsFrame(new GanttChartDemo());
            }
        });
    }
}