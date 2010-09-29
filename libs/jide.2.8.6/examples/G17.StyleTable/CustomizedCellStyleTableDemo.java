/*
 * @(#)CustomizedCellStyleTableDemo.java 3/28/2008
 *
 * Copyright 2002 - 2008 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.grid.*;
import com.jidesoft.icons.JideIconsFactory;
import com.jidesoft.plaf.LookAndFeelFactory;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Demoed Component: {@link com.jidesoft.grid.CellStyleTable} <br> Required jar files: jide-common.jar, jide-grids.jar
 * <br> Required L&F: any L&F
 */
public class CustomizedCellStyleTableDemo extends AbstractDemo {
    public CellStyleTable _table;
    protected AbstractTableModel _tableModel;

    public CustomizedCellStyleTableDemo() {
    }

    public String getName() {
        return "CellStyleTable Demo (Customized CellStyle)";
    }

    public String getProduct() {
        return PRODUCT_NAME_GRIDS;
    }

    @Override
    public String getDescription() {
        return "This is a demo of CellStyleTable with CellStyleCustomizer. CellStyle defined many styles that you can " +
                "use right away. However CellStyleTable is designed with even further customization in mind. In this demo," +
                "we add a new style called iconTextGap. From the name, you can tell it determines the icon and text gap if " +
                "the renderer component is JLabel. When we define the CellStyle, we subclass it and create " +
                "CustomizedCellStyle with this new style. Then when we define the getCellStyleAt in the model, we said if " +
                "the cell value is greater than zero, we put an UP icon and set the iconTextGap to 10. " +
                "If the cell value is less than or equal zero, we put a DOWN icon and set the iconTextGap to 20. " +
                "You can change the radio buttons in the option panel to see the different when we use CellStyleCustomizer or not use it" +
                "\n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.grid.AbstractStyleTableModel\n" +
                "com.jidesoft.grid.CellStyleTable";
    }


    protected static final Color UP = new Color(0, 136, 0);
    protected static final Color DOWN = new Color(204, 0, 0);

    /**
     * A customized CellStyle which adds a new style called iconTextGap. We will later use {@link
     * com.jidesoft.grid.CellStyleCustomizer} to customize the renderer.
     */
    public static class CustomizedCellStyle extends CellStyle {
        private int _iconTextGap;

        public int getIconTextGap() {
            return _iconTextGap;
        }

        public void setIconTextGap(int iconTextGap) {
            _iconTextGap = iconTextGap;
        }
    }

    class StyleTableTableModel extends DefaultTableModel implements StyleModel {

        public StyleTableTableModel(Object[][] data, Object[] columnNames) {
            super(data, columnNames);
        }

        CustomizedCellStyle _cellStyle = new CustomizedCellStyle();

        public CellStyle getCellStyleAt(int row, int column) {
            Number value = (Number) getValueAt(row, column);
            if (value.doubleValue() > 0) {
                _cellStyle.setForeground(UP);
                _cellStyle.setIcon(JideIconsFactory.getImageIcon(JideIconsFactory.Arrow.UP));
                _cellStyle.setIconTextGap(10);
            }
            else {
                _cellStyle.setForeground(DOWN);
                _cellStyle.setIcon(JideIconsFactory.getImageIcon(JideIconsFactory.Arrow.DOWN));
                _cellStyle.setIconTextGap(20);
            }
            return _cellStyle;
        }


        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return Double.class;
        }

        public boolean isCellStyleOn() {
            return true;
        }
    }

    public Component getDemoPanel() {
        JPanel panel = new JPanel(new BorderLayout(6, 6));
        Object[][] data = new Object[20][6];
        String[] names = new String[6];
        for (int i = 0; i < names.length; i++) {
            names[i] = "" + i;
        }
        for (int i = 0; i < data.length; i++) {
            data[i] = new Object[6];
            for (int j = 0; j < data[i].length; j++) {
                data[i][j] = Math.round((Math.random() - 0.2) * 10000) / 100.0;
            }
        }
        _tableModel = new CustomizedCellStyleTableDemo.StyleTableTableModel(data, names);
        _table = new SortableTable(_tableModel);
        _table.setRowSelectionAllowed(true);
        _table.setColumnSelectionAllowed(true);
        _table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        _table.getColumnModel().getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        panel.add(new JScrollPane(_table), BorderLayout.CENTER);
        return panel;
    }

    @Override
    public Component getOptionsPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 1));
        JRadioButton[] buttons = new JRadioButton[2];
        buttons[0] = new JRadioButton(new AbstractAction("CellStyleTable without CellStyleCustomizer") {
            public void actionPerformed(ActionEvent e) {
                _table.clearCellStyleCustomizers();
                _table.repaint();
            }
        });
        buttons[1] = new JRadioButton(new AbstractAction("CellStyleTable with CellStyleCustomizer (Different text/icon gap)") {
            public void actionPerformed(ActionEvent e) {
                _table.addCellStyleCustomizer(new CellStyleCustomizer() {
                    private int _savedIconTextGap = -1;

                    public void customizeRendererComponent(int row, int column, Component rendererComponent, CellStyle cellStyle) {
                        if (rendererComponent instanceof JLabel && cellStyle instanceof CustomizedCellStyle) {
                            if (((CustomizedCellStyle) cellStyle).getIconTextGap() != -1) {
                                if (_savedIconTextGap == -1) {
                                    _savedIconTextGap = ((JLabel) rendererComponent).getIconTextGap();
                                }
                                ((JLabel) rendererComponent).setIconTextGap(((CustomizedCellStyle) cellStyle).getIconTextGap());
                            }
                        }
                    }

                    public void releaseRendererComponent(int row, int column, Component rendererComponent) {
                        if (rendererComponent instanceof JLabel) {
                            if (_savedIconTextGap != -1) {
                                ((JLabel) rendererComponent).setIconTextGap(_savedIconTextGap);
                            }
                        }
                    }

                    public void customizeEditorComponent(int row, int column, Component editorComponent, CellStyle cellStyle) {
                        // do nothing as we will not support editor component here. The editor component can't be a JLabel.
                        // JTextField doesn't have setIconTextGap method.
                    }
                });
                _table.repaint();
            }
        });
        buttons[0].setSelected(true);

        ButtonGroup buttonGroup = new ButtonGroup();
        for (JRadioButton button : buttons) {
            panel.add(button);
            buttonGroup.add(button);
        }
        return panel;
    }

    @Override
    public String getDemoFolder() {
        return "G17.StyleTable";
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new CustomizedCellStyleTableDemo());
    }
}
