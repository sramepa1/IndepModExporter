/*
 * @(#)GanttChartDemo.java 7/27/2009
 *
 * Copyright 2002 - 2009 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.gantt.DateGanttChartPane;
import com.jidesoft.gantt.DefaultGanttEntry;
import com.jidesoft.gantt.DefaultGanttModel;
import com.jidesoft.grid.*;
import com.jidesoft.plaf.GanttUIDefaultsCustomizer;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.range.TimeRange;
import com.jidesoft.scale.*;
import com.jidesoft.swing.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.zip.GZIPInputStream;

@SuppressWarnings("serial")
public class ProjectGanttChartDemo extends AbstractDemo {

    private DateGanttChartPane<DefaultGanttEntry<Date>> _ganttChartPane;

    private int _filterAppliedRowCount;
    private StyledLabel _filterTimesLabel;

    public ProjectGanttChartDemo() {
    }

    public String getName() {
        return "Gantt Chart Demo (Project)";
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

        JButton button = new JButton(new AbstractAction("Resize periods to fit") {
            public void actionPerformed(ActionEvent e) {
                _ganttChartPane.getGanttChart().autoResizePeriods(false);
            }
        });
        panel.add(button);

        button = new JButton(new AbstractAction("Resize periods to fit visible rows") {
            public void actionPerformed(ActionEvent e) {
                _ganttChartPane.getGanttChart().autoResizePeriods(true);
            }
        });
        panel.add(button);

        return panel;
    }

    public Component getDemoPanel() {
        TaskTreeTableModel tableModel = new TaskTreeTableModel();
        final JPanel quickSearchPanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 5, 2));
        QuickTableFilterField filterField = new QuickTableFilterField(tableModel);
        filterField.setObjectConverterManagerEnabled(true);
        quickSearchPanel.add(new JLabel("Filter:"));
        quickSearchPanel.add(filterField);

        JPanel tablePanel = new JPanel(new BorderLayout(2, 2));
        final JLabel label = new JLabel("Click on \"Auto Filter\" in option panel to enable AutoFilter feature");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));


        final SortableTreeTableModel<DefaultGanttEntry<Date>> sortableTreeTableModel =
                new SortableTreeTableModel<DefaultGanttEntry<Date>>(
                        new FilterableTreeTableModel<DefaultGanttEntry<Date>>(filterField.getDisplayTableModel()));
        sortableTreeTableModel.addIndexChangeListener(new IndexChangeListener() {
            public void indexChanged(IndexChangeEvent event) {
                if (event.getSource() instanceof FilterableTableModel && event.getType() == IndexChangeEvent.INDEX_CHANGED_EVENT && _filterTimesLabel != null) {
                    FilterableTableModel model = (FilterableTableModel) event.getSource();
                    _filterAppliedRowCount = model.retrieveFilterApplyRecords();
                    if (_filterAppliedRowCount != 0) {
                        StyledLabelBuilder.setStyledText(_filterTimesLabel, "Filter is applied on {" + _filterAppliedRowCount + ":f:blue} rows. {" + model.getRowCount() + ":f:blue} rows are left.");
                    }
                    else {
                        StyledLabelBuilder.setStyledText(_filterTimesLabel, "Filter in this level is cleared. {" + model.getRowCount() + ":f:blue} rows are shown up from the table model it wraps.");
                    }
                }
            }
        });

        DefaultGanttModel<Date, DefaultGanttEntry<Date>> model = new DefaultGanttModel<Date, DefaultGanttEntry<Date>>();
        model.setScaleModel(new DateScaleModel());
        model.setTreeTableModel(sortableTreeTableModel);
        createTreeTableModel(model);

        _ganttChartPane = new DateGanttChartPane<DefaultGanttEntry<Date>>(model) {
            @Override
            protected JComponent createTableHeaderComponent() {
                JComponent component = super.createTableHeaderComponent();
                component.add(quickSearchPanel);
                return component;
            }
        };
        _ganttChartPane.getGanttChart().getScaleArea().setVisiblePeriods(Arrays.asList(
                DatePeriod.MONTH, DatePeriod.QUARTER, DatePeriod.YEAR));

        TreeTable treeTable = _ganttChartPane.getTreeTable();
        _ganttChartPane.getGanttChart().getScaleArea().setTableCellRenderer(
                // note: cannot use a table header with table
                // a null table is not handled correctly in 6u3-6u13 Windows L&F
                // see http://bugs.sun.com/view_bug.do?bug_id=6777378
                new JideTable().getTableHeader().getDefaultRenderer());

        _ganttChartPane.getGanttChart().getScaleArea().addPopupMenuCustomizer(new VisiblePeriodsPopupMenuCustomizer<Date>());
        _ganttChartPane.getGanttChart().getScaleArea().addPopupMenuCustomizer(new ResizePeriodsPopupMenuCustomizer<Date>(_ganttChartPane.getGanttChart()));

        AutoFilterTableHeader header = new AutoFilterTableHeader(treeTable);
        header.setAutoFilterEnabled(true);
        treeTable.setTableHeader(header);

        treeTable.setDragEnabled(true);
        treeTable.setTransferHandler(new TreeTableTransferHandler());

        ScaleArea<Date> scaleArea = _ganttChartPane.getGanttChart().getScaleArea();
        DatePeriodConverter periodRenderer = new DatePeriodConverter("Month",
                new SimpleDateFormat("MMM"), new SimpleDateFormat("MMMM"));

        scaleArea.setPeriodConverter(DatePeriod.MONTH, periodRenderer);
        scaleArea.setPreferredPeriodSize(new Dimension(50, 21));
        scaleArea.setVisiblePeriodCount(10);

        // do not clear selection when filtering
        treeTable.setClearSelectionOnTableDataChanges(false);
//        treeTable.getColumnModel().getColumn(0).setPreferredWidth(200);
//        treeTable.getColumnModel().getColumn(3).setPreferredWidth(60);
//        treeTable.getColumnModel().getColumn(4).setPreferredWidth(60);

        treeTable.expandAll();
        TableUtils.autoResizeAllColumns(treeTable);
        filterField.setTable(treeTable);

        tablePanel.add(_ganttChartPane);

        JPanel infoPanel = new JPanel(new BorderLayout(3, 3));
        _filterTimesLabel = new StyledLabel("Not filtered yet.");
        infoPanel.add(_filterTimesLabel);
        infoPanel.setBorder(BorderFactory.createCompoundBorder(new JideTitledBorder(new PartialEtchedBorder(PartialEtchedBorder.LOWERED, PartialSide.NORTH), "Filtered Row Count", JideTitledBorder.LEADING, JideTitledBorder.ABOVE_TOP),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)));

        JPanel panel = new JPanel(new BorderLayout(3, 3));
        panel.add(tablePanel);
        panel.add(infoPanel, BorderLayout.AFTER_LAST_LINE);
        return panel;
    }

    private void createTreeTableModel(DefaultGanttModel<Date, DefaultGanttEntry<Date>> model) {
        BufferedReader reader = null;
        try {
            InputStream resource = this.getClass().getClassLoader().getResourceAsStream("Project.txt.gz");
            if (resource == null) {
                Calendar today = Calendar.getInstance();
                Calendar dayBefore2 = Calendar.getInstance();
                dayBefore2.add(Calendar.DAY_OF_WEEK, -2);
                Calendar dayAfter7 = Calendar.getInstance();
                dayAfter7.add(Calendar.DAY_OF_WEEK, 7);
                Calendar dayAfter5 = Calendar.getInstance();
                dayAfter5.add(Calendar.DAY_OF_WEEK, 5);
                Calendar dayAfter2 = Calendar.getInstance();
                dayAfter2.add(Calendar.DAY_OF_WEEK, 2);

                model.setRange(new TimeRange(dayBefore2.getTime(), dayAfter7.getTime()));
                DefaultGanttEntry<Date> group = new DefaultGanttEntry<Date>("Task 1", Date.class, new TimeRange(today.getTime(), dayAfter5.getTime()), 0);
                group.setExpanded(true);
                group.addChild(new DefaultGanttEntry<Date>("Task 2", Date.class, new TimeRange(today.getTime(), dayAfter2.getTime()), 0.4));
                group.addChild(new DefaultGanttEntry<Date>("Task 3", Date.class, new TimeRange(dayAfter2.getTime(), dayAfter5.getTime()), 0));
                model.addGanttEntry(group);
                return;
            }
            InputStream in = new GZIPInputStream(resource);
            reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));

            Task[] tasks = new Task[3];
            DateFormat format = new SimpleDateFormat("dd/MM/yyyy kk:mm");
            Date first = null;
            Date last = null;

            do {
                String line = reader.readLine();
                if (line == null || line.length() == 0) {
                    break;
                }
                String[] values = line.split(",");
                String name = "";
                double completed = 0.0;
                double duration = 0.0;
                double work = 0.0;
                Date startTime = new Date();
                Date endTime = new Date();
                if (values.length >= 1) {
                    name = values[0];
                }
                if (values.length >= 2) {
                    try {
                        completed = Double.parseDouble(values[1]);
                    }
                    catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
                if (values.length >= 3) {
                    try {
                        duration = Double.parseDouble(values[2]);
                    }
                    catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
                if (values.length >= 4) {
                    try {
                        work = Double.parseDouble(values[3]);
                    }
                    catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
                if (values.length >= 5) {
                    try {
                        startTime = format.parse(values[4]);
                    }
                    catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                if (values.length >= 6) {
                    try {
                        endTime = format.parse(values[5]);
                    }
                    catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                Date start = startTime.before(endTime) ? startTime : endTime;
                if (first == null || start.before(first)) {
                    first = start;
                }
                Date end = startTime.after(endTime) ? startTime : endTime;
                if (last == null || end.after(last)) {
                    last = end;
                }

                if (name.startsWith("++")) {
                    Task project = new Task(name.substring(2), completed, duration, work, start, end);
                    tasks[1].addChild(project);
                    tasks[2] = project;
                }
                else if (name.startsWith("+")) {
                    Task task = new Task(name.substring(1), completed, duration, work, start, end);
                    tasks[0].addChild(task);
                    tasks[1] = task;
                }
                else {
                    Task task = new Task(name, completed, duration, work, start, end);
                    model.addGanttEntry(task);
                    tasks[0] = task;
                }
            }
            while (true);

            if (first == null) {
                first = model.getScaleModel().getDefaultStart();
            }
            if (last == null) {
                last = model.getScaleModel().getDefaultEnd();
            }
            model.setRange(new TimeRange(first, last));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private static class TaskTreeTableModel extends TreeTableModel<DefaultGanttEntry<Date>> implements StyleModel {
        private static final long serialVersionUID = 3589523753024111735L;

        public int getColumnCount() {
            return 6;
        }

        @Override
        public Class<?> getCellClassAt(int rowIndex, int columnIndex) {
            return getColumnClass(columnIndex);
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return String.class;
                case 1:
                    return Double.class;
                case 2:
                    return Double.class;
                case 3:
                    return Double.class;
                case 4:
                    return Date.class;
                case 5:
                    return Date.class;
            }
            return Object.class;
        }

        @Override
        public String getColumnName(int column) {
            switch (column) {
                case 0:
                    return "Task Name";
                case 1:
                    return "% Completed";
                case 2:
                    return "Duration";
                case 3:
                    return "Work";
                case 4:
                    return "Start";
                case 5:
                    return "Finish";
            }
            return null;
        }

        static CellStyle BOLD = new CellStyle();

        static {
            BOLD.setFontStyle(Font.BOLD);
        }

        public CellStyle getCellStyleAt(int rowIndex, int columnIndex) {
            Row row = getRowAt(rowIndex);
            if (row.getParent() == getRoot() || (row instanceof ExpandableRow && ((ExpandableRow) row).hasChildren())) {
                return BOLD;
            }
            return null;
        }

        public boolean isCellStyleOn() {
            return true;
        }
    }

    private static class Task extends DefaultGanttEntry<Date> {
        private double _duration;
        private double _work;

        public Task(String name, double completed, double duration, double work, Date startTime, Date endTime) {
            super(name, Date.class, new TimeRange(startTime, endTime), completed);
            _duration = duration;
            _work = work;
        }

        @Override
        protected int getActualColumnIndex(int column) {
            switch (column) {
                case COLUMN_NAME:
                    return 0;
                case COLUMN_COMPLETION:
                    return 1;
                case COLUMN_RANGE_START:
                    return 4;
                case COLUMN_RANGE_END:
                    return 5;
            }
            return super.getActualColumnIndex(column);
        }

        @Override
        public Object getValueAt(int columnIndex) {
            switch (columnIndex) {
                case 2:
                    return _duration;
                case 3:
                    return _work;
                default:
                    return super.getValueAt(columnIndex);
            }
        }

        @Override
        public void setValueAt(Object value, int columnIndex) {
            switch (columnIndex) {
                case 2:
                    _duration = (Double) value;
                    break;
                case 3:
                    _work = (Double) value;
                    break;
                default:
                    super.setValueAt(value, columnIndex);
                    break;
            }
        }
    }

    public static void main(String[] s) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                LookAndFeelFactory.addUIDefaultsCustomizer(new GanttUIDefaultsCustomizer());
                LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
                showAsFrame(new ProjectGanttChartDemo());
            }
        });
    }

}