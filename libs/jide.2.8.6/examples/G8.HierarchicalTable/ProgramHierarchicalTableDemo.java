/*
 * @(#)AddRemoveProgramTableDemo.java 7/24/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.grid.*;
import com.jidesoft.icons.IconsFactory;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.plaf.UIDefaultsLookup;
import com.jidesoft.swing.NullButton;
import com.jidesoft.swing.NullJideButton;
import com.jidesoft.swing.NullLabel;
import com.jidesoft.swing.NullPanel;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.*;

/**
 * Demoed Component: {@link com.jidesoft.grid.HierarchicalTable} <br> Required jar files: jide-common.jar,
 * jide-grids.jar <br> Required L&F: Jide L&F extension required
 */
public class ProgramHierarchicalTableDemo extends AbstractDemo {
    private TableModel _programTableModel;

    public ProgramHierarchicalTableDemo() {
    }

    public String getName() {
        return "HierarchicalTable (Add/Remove Programs) Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_GRIDS;
    }

    @Override
    public String getDescription() {
        return "This is a demo of HierarchicalTable that mimics the Add/Remove Program in Control Panel on Windows.\n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.grid.HierarchicalTable\n" +
                "com.jidesoft.grid.HierarchicalTableModel";
    }

    public Component getDemoPanel() {
        HierarchicalTable table = createTable();
        JScrollPane pane = new JScrollPane(table);
        pane.getViewport().setBackground(Color.WHITE);
        return pane;
    }

    @Override
    public String getDemoFolder() {
        return "G8.HierarchicalTable";
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new ProgramHierarchicalTableDemo());
    }

    // create property table
    private HierarchicalTable createTable() {
        _programTableModel = new ProgramTableModel();
        final HierarchicalTable table = new HierarchicalTable() {
            @Override
            public TableModel getStyleModel() {
                return _programTableModel; // designate it as the style model
            }
        };
        table.setModel(_programTableModel);
        table.setPreferredScrollableViewportSize(new Dimension(600, 400));
        table.setHierarchicalColumn(-1);
        table.setSingleExpansion(true);
        table.setName("Program Table");
        table.setShowGrid(false);
        table.setRowHeight(24);
        table.getTableHeader().setPreferredSize(new Dimension(0, 0));
        table.getColumnModel().getColumn(0).setPreferredWidth(500);
        table.getColumnModel().getColumn(1).setPreferredWidth(30);
        table.getColumnModel().getColumn(1).setMaxWidth(30);
        table.getColumnModel().getColumn(2).setPreferredWidth(60);
        table.getColumnModel().getColumn(2).setMaxWidth(60);
        table.getColumnModel().getColumn(0).setCellRenderer(new ProgramCellRenderer());
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setComponentFactory(new HierarchicalTableComponentFactory() {
            public Component createChildComponent(HierarchicalTable table, Object value, int row) {
                if (value instanceof Program) {
                    return new ProgramPanel((Program) value);
                }
                return null;
            }

            public void destroyChildComponent(HierarchicalTable table, Component component, int row) {
            }
        });
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    table.expandRow(row);
                }
            }
        });
        return table;
    }

    static class ProgramPanel extends JPanel {
        Program program;

        public ProgramPanel(Program program) {
            this.program = program;
            setLayout(new BorderLayout());
            setBorder(BorderFactory.createEmptyBorder(2, 2, 3, 2));
            add(createTextPanel());
            add(createControlPanel(), BorderLayout.AFTER_LINE_ENDS);
            setBackground(UIDefaultsLookup.getColor("Table.selectionBackground"));
            setForeground(UIDefaultsLookup.getColor("Table.selectionForeground"));
        }

        JComponent createTextPanel() {
            NullPanel panel = new NullPanel(new GridLayout(4, 1, 5, 0));
            panel.add(new NullLabel(program.name, IconsFactory.getImageIcon(ProgramHierarchicalTableDemo.class, program.icon), JLabel.LEADING));
            final NullJideButton supportButton = new NullJideButton("Click here for support information");
            supportButton.setHorizontalAlignment(SwingConstants.LEADING);
            supportButton.setButtonStyle(NullJideButton.HYPERLINK_STYLE);
            supportButton.addActionListener(new ClickAction(program, "Support", supportButton));
            panel.add(supportButton);
            panel.add(new NullPanel());
            final NullJideButton changeButton = new NullJideButton("To change or remove it from your computer, click Change or Remove.");
            changeButton.setButtonStyle(NullJideButton.HYPERLINK_STYLE);
            changeButton.setHorizontalAlignment(SwingConstants.LEADING);
            changeButton.addActionListener(new ClickAction(program, "Help", changeButton));
            panel.add(changeButton);
            return panel;
        }

        JComponent createControlPanel() {
            NullPanel panel = new NullPanel(new GridLayout(4, 2, 5, 0));
            panel.add(new NullLabel("Size", NullLabel.TRAILING));
            NullJideButton sizeButton = new NullJideButton(program.size);
            sizeButton.setButtonStyle(NullJideButton.HYPERLINK_STYLE);
            sizeButton.setHorizontalAlignment(SwingConstants.TRAILING);
            sizeButton.addActionListener(new ClickAction(program, "Size", sizeButton));
            panel.add(sizeButton);
            panel.add(new NullLabel("Used", NullLabel.TRAILING));
            NullJideButton usedButton = new NullJideButton(program.used);
            usedButton.setHorizontalAlignment(SwingConstants.TRAILING);
            usedButton.setButtonStyle(NullJideButton.HYPERLINK_STYLE);
            usedButton.addActionListener(new ClickAction(program, "Used", usedButton));
            panel.add(usedButton);
            panel.add(new NullLabel("Last Used On", NullLabel.TRAILING));
            final NullJideButton lastButton = new NullJideButton(program.lastUsed);
            lastButton.setButtonStyle(NullJideButton.HYPERLINK_STYLE);
            lastButton.setHorizontalAlignment(SwingConstants.TRAILING);
            lastButton.addActionListener(new ClickAction(program, "Last Used On", lastButton));
            panel.add(lastButton);
            NullButton changeButton = new NullButton("Change");
            changeButton.addActionListener(new ClickAction(program, "Change", changeButton));
            panel.add(changeButton);
            NullButton removeButton = new NullButton("Remove");
            removeButton.addActionListener(new ClickAction(program, "Remove", removeButton));
            panel.add(removeButton);
            return panel;
        }
    }

    static class ClickAction implements ActionListener {
        Program program;
        String buttonName;
        AbstractButton button;

        public ClickAction(Program program, String buttonName, AbstractButton button) {
            this.program = program;
            this.buttonName = buttonName;
            this.button = button;
        }

        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(button, "\"" + buttonName + "\" in Program \"" + program.name + "\" is clicked.");
        }
    }

    static class ProgramTableModel extends AbstractTableModel implements HierarchicalTableModel, StyleModel {

        public ProgramTableModel() {
        }

        public int getRowCount() {
            return PROGRAMS.length;
        }

        public int getColumnCount() {
            return 3;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            Program program = PROGRAMS[rowIndex];
            switch (columnIndex) {
                case 0:
                    return program;
                case 1:
                    return "Size";
                case 2:
                    return program.size;
            }
            return "";
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }

        public boolean hasChild(int row) {
            return true;
        }

        public boolean isExpandable(int row) {
            return true;
        }

        public boolean isHierarchical(int row) {
            return false;
        }

        public Object getChildValueAt(int row) {
            return PROGRAMS[row];
        }

        public boolean isCellStyleOn() {
            return true;
        }

        static CellStyle _cellStyle = new CellStyle();

        static {
            _cellStyle.setHorizontalAlignment(SwingConstants.TRAILING);
        }

        public CellStyle getCellStyleAt(int rowIndex, int columnIndex) {
            if (columnIndex == 2) {
                return _cellStyle;
            }
            return null;
        }
    }

    static class FitScrollPane extends JScrollPane implements ComponentListener {
        public FitScrollPane() {
            initScrollPane();
        }

        public FitScrollPane(Component view) {
            super(view);
            initScrollPane();
        }

        public FitScrollPane(Component view, int vsbPolicy, int hsbPolicy) {
            super(view, vsbPolicy, hsbPolicy);
            initScrollPane();
        }

        public FitScrollPane(int vsbPolicy, int hsbPolicy) {
            super(vsbPolicy, hsbPolicy);
            initScrollPane();
        }

        private void initScrollPane() {
            setBorder(BorderFactory.createLineBorder(Color.GRAY));
            setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
            getViewport().getView().addComponentListener(this);
            removeMouseWheelListeners();
        }

        // remove MouseWheelListener as there is no need for it in FitScrollPane.
        private void removeMouseWheelListeners() {
            MouseWheelListener[] listeners = getMouseWheelListeners();
            for (MouseWheelListener listener : listeners) {
                removeMouseWheelListener(listener);
            }
        }

        @Override
        public void updateUI() {
            super.updateUI();
            removeMouseWheelListeners();
        }

        public void componentResized(ComponentEvent e) {
            setSize(getSize().width, getPreferredSize().height);
        }

        public void componentMoved(ComponentEvent e) {
        }

        public void componentShown(ComponentEvent e) {
        }

        public void componentHidden(ComponentEvent e) {
        }

        @Override
        public Dimension getPreferredSize() {
            getViewport().setPreferredSize(getViewport().getView().getPreferredSize());
            return super.getPreferredSize();
        }
    }

    static class Program {
        String name;
        String size;
        String used;
        String lastUsed;
        String icon;

        public Program(String name, String size, String used, String lastUsed, String icon) {
            this.name = name;
            this.size = size;
            this.used = used;
            this.lastUsed = lastUsed;
            this.icon = icon;
        }
    }

    static final Program[] PROGRAMS = new Program[]{
            new Program("Adobe Reader 7.0", "61.48MB", "Frequently", "7/8/2005", "icons/program_acrobat.png"),
            new Program("Demo for JIDE Products", "2.00MB", "Frequently", "7/18/2005", "icons/program_java.png"),
            new Program("iTunes", "13.28MB", "Frequently", "7/18/2005", "icons/program_itune.png"),
            new Program("J2SE Development Kit 6.0", "191.00MB", "Rarely", "7/18/2005", "icons/program_java.png"),
            new Program("Java Web Start", "507.00MB", "Rarely", "7/18/2005", "icons/program_java.png"),
            new Program("Microsoft Office 2003", "619.00MB", "Rarely", "7/18/2005", "icons/program_office2003.png"),
            new Program("Mozilla Firefox", "15.67MB", "Rarely", "7/18/2005", "icons/program_firefox.png"),
    };

    class ProgramCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof Program) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, ((Program) value).name, isSelected, hasFocus, row, column);
                label.setIcon(IconsFactory.getImageIcon(ProgramHierarchicalTableDemo.class, ((Program) value).icon));
                return label;
            }
            else {
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        }
    }
}
