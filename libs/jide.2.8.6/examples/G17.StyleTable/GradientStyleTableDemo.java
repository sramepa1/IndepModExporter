/*
 * @(#)CustomStyleTableDemo.java 6/21/2007
 *
 * Copyright 2002 - 2007 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.grid.*;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.JideSwingUtilities;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.Serializable;

/**
 * Demoed Component: {@link com.jidesoft.grid.CellStyleTable} <br> Required jar files: jide-common.jar, jide-grids.jar
 * <br> Required L&F: any L&F
 */
public class GradientStyleTableDemo extends AbstractDemo {
    public ContextSensitiveTable _table;
    protected AbstractTableModel _tableModel;
    protected int _pattern;

    public GradientStyleTableDemo() {
    }

    public String getName() {
        return "CellStyleTable Demo (Extending CellStyle)";
    }

    public String getProduct() {
        return PRODUCT_NAME_GRIDS;
    }

    @Override
    public String getDescription() {
        return "This is a demo of CellStyleTable and how to extend it to add your own style. In this demo, we create a GradientLabel which can paint gradient as background. " +
                "Then we create GradientCellStyle which supports setting gradient in a cell style. At last we extend the CellStyleTable to use GradientCellStyle. " +
                "Putting them together we get a table which can have gradient in any cell. \n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.grid.AbstractStyleTableModel\n" +
                "com.jidesoft.grid.CellStyleTable";
    }

    class StyleTableTableModel extends AbstractStyleTableModel {
        protected final Color BACKGROUND1 = new Color(253, 253, 244);
        protected final Color BACKGROUND2 = new Color(150, 230, 255);
        protected final Color BACKGROUND3 = new Color(210, 255, 150);

        public int getRowCount() {
            return 10000;
        }

        public int getColumnCount() {
            return 9;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            return "" + rowIndex + "," + columnIndex;
        }

        GradientCellStyle _cellStyle = new GradientCellStyle();

        @Override
        public CellStyle getCellStyleAt(int row, int column) {
            _cellStyle.setHorizontalAlignment(SwingConstants.CENTER);
            _cellStyle.setIcon(null);
            if (_pattern == 0) {
                if (row % 2 == 0) {
                    _cellStyle.setStartColor(BACKGROUND1);
                    _cellStyle.setEndColor(BACKGROUND2);
                }
                else {
                    _cellStyle.setStartColor(null);
                    _cellStyle.setEndColor(null);
                }
            }
            else if (_pattern == 1) {
                if (row % 3 == 0) {
                    _cellStyle.setStartColor(BACKGROUND1);
                    _cellStyle.setEndColor(BACKGROUND2);
                }
                else if (row % 3 == 1) {
                    _cellStyle.setStartColor(BACKGROUND2);
                    _cellStyle.setEndColor(BACKGROUND3);
                }
                else {
                    _cellStyle.setStartColor(null);
                    _cellStyle.setEndColor(null);
                }
            }
            else if (_pattern == 2) {
                if (column % 2 == 0) {
                    _cellStyle.setStartColor(BACKGROUND1);
                    _cellStyle.setEndColor(BACKGROUND2);
                    _cellStyle.setVertical(false);
                }
                else {
                    _cellStyle.setStartColor(null);
                    _cellStyle.setEndColor(null);
                    _cellStyle.setVertical(true);
                }
            }
            else {
                if (row % 2 == column % 2) {
                    _cellStyle.setStartColor(BACKGROUND1);
                    _cellStyle.setEndColor(BACKGROUND2);
                }
                else {
                    _cellStyle.setStartColor(null);
                    _cellStyle.setEndColor(null);
                }
            }
            return _cellStyle;
        }


        @Override
        public boolean isCellStyleOn() {
            return _pattern != 4;
        }
    }

    public Component getDemoPanel() {
        JPanel panel = new JPanel(new BorderLayout(6, 6));
        _tableModel = new StyleTableTableModel();
        _table = new SortableTable(_tableModel) {
            private Color _saveStartColor;
            private Color _saveEndColor;

            @Override
            protected void customizeRendererComponent(int row, int column, Component component, CellStyle cellStyle) {
                super.customizeRendererComponent(row, column, component, cellStyle);

                Component rendererComponent = component;
                boolean isRendererWrapper = false;
                if (rendererComponent instanceof RendererWrapper) {
                    rendererComponent = ((RendererWrapper) rendererComponent).getActualRenderer();
                    if (rendererComponent != null) {
                        isRendererWrapper = true;
                    }
                    else {
                        rendererComponent = component;
                    }
                }

                if (rendererComponent instanceof GradientLabel && cellStyle instanceof GradientCellStyle) {
                    if (((GradientCellStyle) cellStyle).getStartColor() != null) {
                        if (_saveStartColor == null) {
                            _saveStartColor = ((GradientLabel) rendererComponent).getStartColor();
                        }
                        ((GradientLabel) rendererComponent).setStartColor(((GradientCellStyle) cellStyle).getStartColor());
                    }
                    if (((GradientCellStyle) cellStyle).getEndColor() != null) {
                        if (_saveEndColor == null) {
                            _saveEndColor = ((GradientLabel) rendererComponent).getEndColor();
                        }
                        ((GradientLabel) rendererComponent).setEndColor(((GradientCellStyle) cellStyle).getEndColor());
                    }
                    ((GradientLabel) rendererComponent).setVertical(((GradientCellStyle) cellStyle).isVertical());
                }
            }

            @Override
            public void releaseRendererComponent(TableCellRenderer renderer, int row, int column, Component component) {
                super.releaseRendererComponent(renderer, row, column, component);

                Component rendererComponent = component;
                boolean isRendererWrapper = false;
                if (rendererComponent instanceof RendererWrapper) {
                    rendererComponent = ((RendererWrapper) rendererComponent).getActualRenderer();
                    if (rendererComponent != null) {
                        isRendererWrapper = true;
                    }
                    else {
                        rendererComponent = component;
                    }
                }

                if (rendererComponent instanceof GradientLabel) {
                    ((GradientLabel) rendererComponent).setStartColor(_saveStartColor);
                    _saveStartColor = null;
                    ((GradientLabel) rendererComponent).setEndColor(_saveEndColor);
                    _saveEndColor = null;
                    ((GradientLabel) rendererComponent).setVertical(true);
                }
            }
        };

        _table.setDefaultCellRenderer(new GradientLabelCellRenderer());
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
        JRadioButton[] buttons = new JRadioButton[5];
        buttons[0] = new JRadioButton(new AbstractAction("Row Stripe (two colors)") {
            public void actionPerformed(ActionEvent e) {
                _pattern = 0;
                _table.repaint();
            }
        });
        buttons[1] = new JRadioButton(new AbstractAction("Row Stripe (three colors)") {
            public void actionPerformed(ActionEvent e) {
                _pattern = 1;
                _table.repaint();
            }
        });
        buttons[2] = new JRadioButton(new AbstractAction("Column Stripe") {
            public void actionPerformed(ActionEvent e) {
                _pattern = 2;
                _table.repaint();
            }
        });
        buttons[3] = new JRadioButton(new AbstractAction("Grid") {
            public void actionPerformed(ActionEvent e) {
                _pattern = 3;
                _table.repaint();
            }
        });
        buttons[4] = new JRadioButton(new AbstractAction("None") {
            public void actionPerformed(ActionEvent e) {
                _pattern = 4;
                _table.repaint();
            }
        });

        ButtonGroup buttonGroup = new ButtonGroup();
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setSelected(_pattern == i);
            panel.add(buttons[i]);
            buttonGroup.add(buttons[i]);
        }
        return panel;
    }

    @Override
    public String getDemoFolder() {
        return "G17.StyleTable";
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new GradientStyleTableDemo());
    }

    public static class GradientCellStyle extends CellStyle {
        private Color _startColor;
        private Color _endColor;
        private boolean _isVertical = true;

        public Color getStartColor() {
            return _startColor;
        }

        public void setStartColor(Color startColor) {
            _startColor = startColor;
        }

        public Color getEndColor() {
            return _endColor;
        }

        public void setEndColor(Color endColor) {
            _endColor = endColor;
        }

        public boolean isVertical() {
            return _isVertical;
        }

        public void setVertical(boolean vertical) {
            _isVertical = vertical;
        }
    }

    // copied from DefaultTableCellRenderer
    public static class GradientLabelCellRenderer extends GradientLabel implements TableCellRenderer, Serializable {

        protected static Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);
        private static final Border SAFE_NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);

        // We need a place to store the color the JLabel should be returned
        // to after its foreground and background colors have been set
        // to the selection background color.
        // These ivars will be made protected when their names are finalized.
        private Color unselectedForeground;
        private Color unselectedBackground;

        /**
         * Creates a default table cell renderer.
         */
        public GradientLabelCellRenderer() {
            super();
            setOpaque(true);
            setBorder(getNoFocusBorder());
        }

        private static Border getNoFocusBorder() {
            if (System.getSecurityManager() != null) {
                return SAFE_NO_FOCUS_BORDER;
            }
            else {
                return noFocusBorder;
            }
        }

        /**
         * Overrides <code>JComponent.setForeground</code> to assign the unselected-foreground color to the specified
         * color.
         *
         * @param c set the foreground color to this value
         */
        @Override
        public void setForeground(Color c) {
            super.setForeground(c);
            unselectedForeground = c;
        }

        /**
         * Overrides <code>JComponent.setBackground</code> to assign the unselected-background color to the specified
         * color.
         *
         * @param c set the background color to this value
         */
        @Override
        public void setBackground(Color c) {
            super.setBackground(c);
            unselectedBackground = c;
        }

        /**
         * Notification from the <code>UIManager</code> that the look and feel [L&F] has changed. Replaces the current
         * UI object with the latest version from the <code>UIManager</code>.
         *
         * @see JComponent#updateUI
         */
        @Override
        public void updateUI() {
            super.updateUI();
            setForeground(null);
            setBackground(null);
        }

        // implements javax.swing.table.TableCellRenderer
        /**
         * Returns the default table cell renderer.
         *
         * @param table      the <code>JTable</code>
         * @param value      the value to assign to the cell at <code>[row, column]</code>
         * @param isSelected true if cell is selected
         * @param hasFocus   true if cell has focus
         * @param row        the row of the cell to render
         * @param column     the column of the cell to render
         * @return the default table cell renderer
         */
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {

            if (isSelected) {
                super.setForeground(table.getSelectionForeground());
                super.setBackground(table.getSelectionBackground());
            }
            else {
                super.setForeground((unselectedForeground != null) ? unselectedForeground
                        : table.getForeground());
                super.setBackground((unselectedBackground != null) ? unselectedBackground
                        : table.getBackground());
            }

            setFont(table.getFont());

            if (hasFocus) {
                Border border = null;
                if (isSelected) {
                    border = UIManager.getBorder("Table.focusSelectedCellHighlightBorder");
                }
                if (border == null) {
                    border = UIManager.getBorder("Table.focusCellHighlightBorder");
                }
                setBorder(border);

                if (!isSelected && table.isCellEditable(row, column)) {
                    Color col;
                    col = UIManager.getColor("Table.focusCellForeground");
                    if (col != null) {
                        super.setForeground(col);
                    }
                    col = UIManager.getColor("Table.focusCellBackground");
                    if (col != null) {
                        super.setBackground(col);
                    }
                }
            }
            else {
                setBorder(getNoFocusBorder());
            }

            setValue(value);

            return this;
        }

        /*
        * The following methods are overridden as a performance measure to
        * to prune code-paths are often called in the case of renders
        * but which we know are unnecessary.  Great care should be taken
        * when writing your own renderer to weigh the benefits and
        * drawbacks of overriding methods like these.
        */

        /**
         * Overridden for performance reasons. See the <a href="#override">Implementation Note</a> for more
         * information.
         */
        @Override
        public boolean isOpaque() {
            Color back = getBackground();
            Component p = getParent();
            if (p != null) {
                p = p.getParent();
            }
            // p should now be the JTable.
            boolean colorMatch = (back != null) && (p != null) &&
                    back.equals(p.getBackground()) &&
                    p.isOpaque();
            return !colorMatch && super.isOpaque();
        }

        /**
         * Overridden for performance reasons. See the <a href="#override">Implementation Note</a> for more
         * information.
         *
         * @since 1.5
         */
        @Override
        public void invalidate() {
        }

        /**
         * Overridden for performance reasons. See the <a href="#override">Implementation Note</a> for more
         * information.
         */
        @Override
        public void validate() {
        }

        /**
         * Overridden for performance reasons. See the <a href="#override">Implementation Note</a> for more
         * information.
         */
        @Override
        public void revalidate() {
        }

        /**
         * Overridden for performance reasons. See the <a href="#override">Implementation Note</a> for more
         * information.
         */
        @Override
        public void repaint(long tm, int x, int y, int width, int height) {
        }

        /**
         * Overridden for performance reasons. See the <a href="#override">Implementation Note</a> for more
         * information.
         */
        @Override
        public void repaint(Rectangle r) {
        }

        /**
         * Overridden for performance reasons. See the <a href="#override">Implementation Note</a> for more
         * information.
         *
         * @since 1.5
         */
        @Override
        public void repaint() {
        }

        /**
         * Overridden for performance reasons. See the <a href="#override">Implementation Note</a> for more
         * information.
         */
        @Override
        protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
            // Strings get interned...
            if (propertyName == "text") {
                super.firePropertyChange(propertyName, oldValue, newValue);
            }
        }

        /**
         * Overridden for performance reasons. See the <a href="#override">Implementation Note</a> for more
         * information.
         */
        @Override
        public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
        }


        /**
         * Sets the <code>String</code> object for the cell being rendered to <code>value</code>.
         *
         * @param value the string value for this cell; if value is <code>null</code> it sets the text value to an empty
         *              string
         * @see JLabel#setText
         */
        protected void setValue(Object value) {
            setText((value == null) ? "" : value.toString());
        }


        /**
         * A subclass of <code>DefaultTableCellRenderer</code> that implements <code>UIResource</code>.
         * <code>DefaultTableCellRenderer</code> doesn't implement <code>UIResource</code> directly so that applications
         * can safely override the <code>cellRenderer</code> property with <code>DefaultTableCellRenderer</code>
         * subclasses.
         * <p/>
         * <strong>Warning:</strong> Serialized objects of this class will not be compatible with future Swing releases.
         * The current serialization support is appropriate for short term storage or RMI between applications running
         * the same version of Swing.  As of 1.4, support for long term storage of all JavaBeans<sup><font
         * size="-2">TM</font></sup> has been added to the <code>java.beans</code> package. Please see {@link
         * java.beans.XMLEncoder}.
         */
        public static class UIResource extends GradientLabelCellRenderer implements javax.swing.plaf.UIResource {
        }

    }

    public static class GradientLabel extends JLabel {
        private Color _startColor;
        private Color _endColor;
        private boolean _isVertical = true;

        public GradientLabel() {
        }

        public GradientLabel(Icon image) {
            super(image);
        }

        public GradientLabel(Icon image, int horizontalAlignment) {
            super(image, horizontalAlignment);
        }

        public GradientLabel(String text) {
            super(text);
        }

        public GradientLabel(String text, int horizontalAlignment) {
            super(text, horizontalAlignment);
        }

        public GradientLabel(String text, Icon icon, int horizontalAlignment) {
            super(text, icon, horizontalAlignment);
        }

        public Color getStartColor() {
            return _startColor;
        }

        public void setStartColor(Color startColor) {
            _startColor = startColor;
        }

        public Color getEndColor() {
            return _endColor;
        }

        public void setEndColor(Color endColor) {
            _endColor = endColor;
        }

        public boolean isVertical() {
            return _isVertical;
        }

        public void setVertical(boolean vertical) {
            _isVertical = vertical;
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (getStartColor() != null && getEndColor() != null) {
                JideSwingUtilities.fillGradient((Graphics2D) g, new Rectangle(0, 0, getWidth(), getHeight()), getStartColor(), getEndColor(), isVertical());
            }
            super.paintComponent(g);
        }
    }
}
