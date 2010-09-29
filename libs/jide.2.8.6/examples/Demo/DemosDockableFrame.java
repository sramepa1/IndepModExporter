/*
 * @(#)${NAME}.java
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.action.CommandBar;
import com.jidesoft.docking.DockContext;
import com.jidesoft.docking.DockableFrame;
import com.jidesoft.status.ProgressStatusBarItem;
import com.jidesoft.swing.JideButton;
import com.jidesoft.swing.JideScrollPane;
import com.jidesoft.swing.JideToggleButton;
import com.jidesoft.swing.SearchableUtils;
import com.jidesoft.tree.QuickTreeFilterField;
import com.jidesoft.tree.TreeUtils;
import com.jidesoft.utils.ProductNames;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.Position;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.Vector;

/**
 */
class DemosDockableFrame extends DockableFrame {
    private Vector<ComparableTreeNode> _demos = new Vector<ComparableTreeNode>();
    private int _order; // 0 or 1
    private JideDemos _demoHolder;

    static final String[] DEMOS = new String[]{
            // Components
            "DocumentPaneDemo",
            "StatusBarDemo",
            "CollapsiblePaneDemo",
            "FloorTabbedPaneDemo",
            "OutlookTabbedPaneDemo",
            "AlertDemo",
            "BalloonTipDemo",

            // Common
            "JideButtonDemo",
            "JideSplitButtonDemo",
            "JideLabelDemo",
            "JideTabbedPaneDemo",
            "JideScrollPaneDemo",
            "SimpleScrollPaneDemo",
            "CheckBoxListDemo",
            "CheckBoxTreeDemo",
            "AutoResizingTextAreaDemo",
            "JideBorderLayoutDemo",
            "JideBoxLayoutDemo",
            "BorderDemo",
            "FastGradientPaintDemo",
            "SearchableDemo",
            "SearchableBarDemo",
            "AutoCompletionDemo",
            "IntelliHintsDemo",
            "PopupDemo",
            "JideSplitPaneDemo",
            "StyledLabelDemo",
            "StyledLabelBuilderDemo",
            "StyledLabelPerformanceDemo",
            "FolderChooserDemo",
            "RangeSliderDemo",
            "OverlayableDemo",
            "TitledSeparatorDemo",
            "MarqueePaneDemo",

            // Dock
            "DockDemo",
            "TwoDockDemo",
            "InitialLayoutDockDemo",
            "HeavyweightDockDemo",

            // Action
            "SampleVsnetDemo",
            "SampleWordDemo",
            "ThreeActionDemo",

            // Grids
            "PropertyPaneDemo",
            "SortableTableDemo",
            "SortableListDemo",
            "SortableComboBoxDemo",
            "SortableTreeDemo",
            "TableSortItemEditorDemo",
            "LargeSortableTableDemo",
            "FilterableTableDemo",
            "LuceneQuickFilterListDemo",
            "LuceneQuickFilterTableDemo",
            "CustomFilterDemo",
            "QuickFilterTableDemo",
            "QuickFilterListDemo",
            "QuickFilterComboBoxDemo",
            "QuickFilterTreeDemo",
            "ShrinkSearchableDemo",
            "EditableTableHeaderDemo",
            "AutoFilterTableHeaderDemo",
            "AutoFilterTreeTableDemo",
            "AutoFilterTableDemo",
            "TradingHierarchicalTableDemo",
            "ProgramHierarchicalTableDemo",
            "HierarchicalTableDemo",
            "FileSystemTreeTableDemo",
            "GroupTableDemo",
            "FontGroupListDemo",
            "IconGroupListDemo",
            "BeanTableDemo",
            "DefaultSpanTableDemo",
            "AbstractSpanTableDemo",
            "AutoCellMergeTableDemo",
            "AbstractStyleTableDemo",
            "GradientStyleTableDemo",
            "StripesTableDemo",
            "CustomizedCellStyleTableDemo",
            "CustomStyleDemo",
            "TableFlashableDemo",
            "AbstractNavigableTableDemo",
            "JideTableDemo",
            "ContextSensitiveTableDemo",
            "NonContiguousTableDemo",
            "AutoResizeModeDemo",
            "TableScrollPaneDemo",
            "TreeTableScrollPaneDemo",
            "TableSplitPaneDemo",
            "DualListDemo",
            "ResizableTableDemo",
            "TableUtilsDemo",
            "CachedTableModelDemo",
            "ComboBoxDemo",
            "CalculatorDemo",
            "DateComboBoxDemo",
            "DateChooserDemo",
            "MonthChooserDemo",
            "CalendarViewerDemo",
            "ColorChooserDemo",
            "DirectionChooserDemo",
            "DirectionPropertyPaneDemo",
            "ConverterDemo",
            "DateSpinnerDemo",
            "PointSpinnerDemo",
            "ImagePreviewListDemo",
            "IPTextFieldDemo",
            "CreditCardTextFieldDemo",

            // Pivot
            "PivotTableDemo",
            "CalculatedTableModelDemo",
            "AggregateTableDemo",

            // Data
            "PageNavigationBarDemo",
            "DatabaseTableModelDemo",
            "DatabasePageTableModelDemo",
            "HibernateTableModelDemo",
            "HibernatePageTableModelDemo",

            // Dialogs
            "WizardDemo",
            "OptionsDialogDemo",
            "TipsOfTheDayDemo",
            "ButtonPanelDemo",
            "BannerPanelDemo",
            "StandardDialogDemo",
            "JideOptionPaneDemo",

            // Shortcut
            "ShortcutEditorDemo",

            // CodeEditor
            "CodeEditorDemo",
            "CodeEditorDocumentPaneDemo",
            "PhpSyntaxParsingDemo",

            // FeedReader
            "FeedReaderDemo",

            // Dashboard
            "CollapsiblePaneDashboardDemo",
            "DockableFrameDashboardDemo",
            "CollapsiblePaneDashboardDocumentPaneDemo",
            "RegularPanelDashboardDemo",

            // Charts
            "ConfigurableChartDemo",
            "PieChartDemo",
            "DifferentViewsDemo",
            "RainfallChartDemo",
            "TemperaturesChartDemo",
            "SunshineChartDemo",
            "CategoricalChartDemo",
            "ConfigurableBarChartDemo",
            "CubicSplinesChartDemo",
            "DualYAxisBarChartDemo",
            "InteractiveChartDemo",
            "LogarithmDemo",
            "LineChartDemo",
            "GradientLineFillDemo",
            "WithImagesChartDemo",
            "DynamicChartDemo",
            "ScatterChartDemo",
            "MeterDemo",
            "LineFittingChartDemo",
            "ScalabilityDemo",
            "CyclicalChartDemo",
            "BubbleChartDemo",
            "CandleStickDemo",
            "MultiChartDemo",
            "MarkerDemo",

            // Gantt Charts
            "GanttChartDemo",
            "GanttChartPaneDemo",
            "NumberGanttChartDemo",
            "ProjectGanttChartDemo",
    };

    private JTree _tree;
    private JButton _openButton;
    protected QuickTreeFilterField _field;

    public DemosDockableFrame(String key, JideDemos demos) {
        super(key, DemoIconsFactory.getImageIcon(DemoIconsFactory.Frame.DEMO));
        _demoHolder = demos;
        getContext().setInitSide(DockContext.DOCK_SIDE_WEST);
        setPreferredSize(new Dimension(200, 200));
        loadDemos();
        initComponents();
        setDefaultFocusComponent(_field.getTextField());
    }

    protected JideButton createButton(Action action) {
        JideButton button = new JideButton(action);
        button.setFocusable(false);
        button.setRequestFocusEnabled(false);
        return button;
    }

    protected JideButton createToggleButton(Action action) {
        JideButton button = new JideToggleButton(action);
        button.setFocusable(false);
        button.setRequestFocusEnabled(false);
        return button;
    }

    protected JComponent createToolBar() {
        CommandBar toolBar = new CommandBar();
        toolBar.setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
        toolBar.setFloatable(false);
        toolBar.setStretch(true);
        toolBar.setPaintBackground(false);
        toolBar.setChevronAlwaysVisible(false);

        _openButton = createButton(new AbstractAction("", DemoIconsFactory.getImageIcon(DemoIconsFactory.Tree.FOLDER_OPENED)) {
            private static final long serialVersionUID = -7414539194884773553L;

            public void actionPerformed(ActionEvent e) {
                openSelectedDemo();
            }
        });
        _openButton.setToolTipText("Open the selected demo");
        _openButton.setEnabled(false);
        toolBar.add(_openButton);

        toolBar.addSeparator();

        JButton categoryViewButton = createToggleButton(new AbstractAction("", DemoIconsFactory.getImageIcon(DemoIconsFactory.Tree.TREE)) {
            private static final long serialVersionUID = 6174457687626496602L;

            public void actionPerformed(ActionEvent e) {
                _order = 0;
                TreeModel model = reloadTreeModel(_order);
                _field.setTreeModel(model);
                _tree.setModel(_field.getDisplayTreeModel());
                expandAll(model);
            }
        });
        categoryViewButton.setToolTipText("Categorizing by products");
        toolBar.add(categoryViewButton);

        JButton alphabetViewButton = createToggleButton(new AbstractAction("", DemoIconsFactory.getImageIcon(DemoIconsFactory.Tree.SORT)) {
            private static final long serialVersionUID = 6073930856511015109L;

            public void actionPerformed(ActionEvent e) {
                _order = 1;
                TreeModel model = reloadTreeModel(_order);
                _field.setTreeModel(model);
                _tree.setModel(_field.getDisplayTreeModel());
            }
        });
        alphabetViewButton.setToolTipText("Sort demo by alphabetic order");
        toolBar.add(alphabetViewButton);

        if (_order == 0) {
            categoryViewButton.setSelected(true);
        }
        else {
            alphabetViewButton.setSelected(true);
        }

        ButtonGroup group = new ButtonGroup();
        group.add(categoryViewButton);
        group.add(alphabetViewButton);

        CommandBar.Expansion expansion = new CommandBar.Expansion();
        expansion.setLayout(new BorderLayout(0, 0));
        expansion.add(_field);

        toolBar.add(expansion);

        return toolBar;
    }

    public void openSelectedDemo() {
        TreePath path = _tree.getSelectionPath();
        if (path != null) {
            Object node = path.getLastPathComponent();
            openDemo(node);
        }
    }


    protected void initComponents() {
        TreeModel model = reloadTreeModel(_order);
        _field = new QuickTreeFilterField() {
            @Override
            public void applyFilter() {
                super.applyFilter();
                TreeUtils.expandAll(_tree);
            }
        };
        _field.setHintText("Filter");
        _field.setMatchesLeafNodeOnly(true);
        _field.setHideEmptyParentNode(true);
        _field.setWildcardEnabled(true);
        _field.setTreeModel(model);
        _tree = new JTree(_field.getDisplayTreeModel()) {
            @Override
            public TreePath getNextMatch(String prefix, int startingRow, Position.Bias bias) {
                return null;
            }
        };
        _tree.registerKeyboardAction(new AbstractAction() {
            private static final long serialVersionUID = -7647825587470936818L;

            public void actionPerformed(ActionEvent e) {
                openSelectedDemo();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_FOCUSED);
        SearchableUtils.installSearchable(_tree);
        _tree.setCellRenderer(new DemoCellRenderer());
        _tree.setRootVisible(false);
        TreeUtils.expandAll(_tree);

        _tree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                updateOpenButton();
            }
        });
        updateOpenButton();
        _tree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2) {
                    TreePath path = _tree.getSelectionPath();
                    if (path != null) {
                        Object node = path.getLastPathComponent();
                        openDemo(node);
                    }
                }
            }
        });

        JPanel panel = new JPanel(new BorderLayout(2, 2));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 3, 2, 3));
        _tree.setBorder(BorderFactory.createEmptyBorder(0, 3, 2, 2));
        panel.add(createToolBar(), BorderLayout.BEFORE_FIRST_LINE);
        JideScrollPane scrollPane = new JideScrollPane(_tree);
        scrollPane.setRequestFocusEnabled(false);
        panel.add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(panel);
    }

    private void updateOpenButton() {
        if (_openButton == null) {
            return;
        }

        if (_tree.getSelectionPath() != null) {
            _openButton.setEnabled(true);
        }
        else {
            _openButton.setEnabled(false);
        }
    }

    private void expandAll(TreeModel model) {
        if (_order == 0) {
            for (int i = 0; i < ((ComparableTreeNode) model.getRoot()).getChildCount(); i++) {
                _tree.expandPath(new TreePath(new Object[]{model.getRoot(),
                        ((ComparableTreeNode) model.getRoot()).getChildAt(i)}));
            }
        }
    }

    /**
     * Loads tree into TreeModel
     *
     * @param order the order
     * @return TreeModel
     */
    private TreeModel reloadTreeModel(int order) {
        ComparableTreeNode root = new ComparableTreeNode("JIDE Products");
        if (order == 0) {
            ComparableTreeNode nodeCommon = new ComparableTreeNode(ProductNames.PRODUCT_NAME_COMMON + " (Open Source)");
            ComparableTreeNode nodeDock = new ComparableTreeNode(ProductNames.PRODUCT_NAME_DOCK);
            ComparableTreeNode nodeAction = new ComparableTreeNode(ProductNames.PRODUCT_NAME_ACTION);
            ComparableTreeNode nodeComponents = new ComparableTreeNode(ProductNames.PRODUCT_NAME_COMPONENTS);
            ComparableTreeNode nodeGrids = new ComparableTreeNode(ProductNames.PRODUCT_NAME_GRIDS);
            ComparableTreeNode nodeDialogs = new ComparableTreeNode(ProductNames.PRODUCT_NAME_DIALOGS);
            ComparableTreeNode nodeShortcut = new ComparableTreeNode(ProductNames.PRODUCT_NAME_SHORTCUT);
            ComparableTreeNode nodePivot = new ComparableTreeNode(ProductNames.PRODUCT_NAME_PIVOT);
            ComparableTreeNode nodeCodeEditor = new ComparableTreeNode(ProductNames.PRODUCT_NAME_CODE_EDITOR);
            ComparableTreeNode nodeFeedReader = new ComparableTreeNode(ProductNames.PRODUCT_NAME_FEEDREADER);
            ComparableTreeNode nodeDashboard = new ComparableTreeNode(ProductNames.PRODUCT_NAME_DASHBOARD);
            ComparableTreeNode nodeData = new ComparableTreeNode(ProductNames.PRODUCT_NAME_DATAGRIDS);
            ComparableTreeNode nodeCharts = new ComparableTreeNode(ProductNames.PRODUCT_NAME_CHARTS);
            ComparableTreeNode nodeGantt = new ComparableTreeNode(ProductNames.PRODUCT_NAME_GANTT_CHART);
            root.add(nodeCommon);
            root.add(nodeDock);
            root.add(nodeAction);
            root.add(nodeComponents);
            root.add(nodeGrids);
            root.add(nodeDialogs);
            root.add(nodePivot);
            root.add(nodeData);
            root.add(nodeShortcut);
            root.add(nodeCodeEditor);
            root.add(nodeFeedReader);
            root.add(nodeDashboard);
            root.add(nodeCharts);
            root.add(nodeGantt);

            for (ComparableTreeNode node : _demos) {
                if (node.getUserObject() instanceof Demo) {
                    Demo demo = (Demo) node.getUserObject();
                    Object product = demo.getProduct();
                    if (ProductNames.PRODUCT_NAME_COMMON.equals(product)) {
                        nodeCommon.add(node); // add to components for now
                    }
                    else if (ProductNames.PRODUCT_NAME_DOCK.equals(product)) {
                        nodeDock.add(node);
                    }
                    else if (ProductNames.PRODUCT_NAME_ACTION.equals(product)) {
                        nodeAction.add(node);
                    }
                    else if (ProductNames.PRODUCT_NAME_COMPONENTS.equals(product)) {
                        nodeComponents.add(node);
                    }
                    else if (ProductNames.PRODUCT_NAME_GRIDS.equals(product)) {
                        nodeGrids.add(node);
                    }
                    else if (ProductNames.PRODUCT_NAME_DIALOGS.equals(product)) {
                        nodeDialogs.add(node);
                    }
                    else if (ProductNames.PRODUCT_NAME_SHORTCUT.equals(product)) {
                        nodeShortcut.add(node);
                    }
                    else if (ProductNames.PRODUCT_NAME_PIVOT.equals(product)) {
                        nodePivot.add(node);
                    }
                    else if (ProductNames.PRODUCT_NAME_CODE_EDITOR.equals(product)) {
                        nodeCodeEditor.add(node);
                    }
                    else if (ProductNames.PRODUCT_NAME_FEEDREADER.equals(product)) {
                        nodeFeedReader.add(node);
                    }
                    else if (ProductNames.PRODUCT_NAME_DASHBOARD.equals(product)) {
                        nodeDashboard.add(node);
                    }
                    else if (ProductNames.PRODUCT_NAME_DATAGRIDS.equals(product)) {
                        nodeData.add(node);
                    }
                    else if (ProductNames.PRODUCT_NAME_CHARTS.equals(product)) {
                        nodeCharts.add(node);
                    }
                    else if (ProductNames.PRODUCT_NAME_GANTT_CHART.equals(product)) {
                        nodeGantt.add(node);
                    }
                }
            }
        }
        else {
            Collections.sort(_demos);
            for (Object demoe : _demos) {
                ComparableTreeNode node = (ComparableTreeNode) demoe;
                root.add(node);
            }
        }

        return new DefaultTreeModel(root);
    }

    /**
     * Loads all demos into a list of tree nodes.
     */
    private void loadDemos() {
        int loaded = 0;
        _demos.clear();
        for (String demoClasName : DEMOS) {
            try {
                Class<?> demoClass = getClass().getClassLoader().loadClass(demoClasName);
                Demo demo = (Demo) demoClass.newInstance();
                ComparableTreeNode demoNode = new ComparableTreeNode(demo);
                _demos.add(demoNode);
                loaded++;
            }
            catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            catch (InstantiationException e) {
                e.printStackTrace();
            }
            catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            ((ProgressStatusBarItem) JideDemos.getStatusBar().getItemByName("Status")).setStatus("Total " + DEMOS.length + " demos; "
                    + ((loaded == DEMOS.length) ? "all" : "" + loaded) + " of them are loaded successfully.");
        }
    }

    private void openDemo(Object node) {
        Object object = ((ComparableTreeNode) node).getUserObject();
        if (object instanceof Demo) {
            _demoHolder.openDemo(((Demo) object));
        }
    }

    class ComparableTreeNode extends DefaultMutableTreeNode implements Comparable {
        private static final long serialVersionUID = -7121339323800524242L;

        public ComparableTreeNode() {
        }

        public ComparableTreeNode(Object userObject) {
            super(userObject);
        }

        public ComparableTreeNode(Object userObject, boolean allowsChildren) {
            super(userObject, allowsChildren);
        }

        public int compareTo(Object o) {
            return getUserObject().toString().compareTo(((ComparableTreeNode) o).getUserObject().toString());
        }
    }
}
