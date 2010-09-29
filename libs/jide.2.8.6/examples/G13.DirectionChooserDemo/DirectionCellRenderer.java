/*
 * @(#)DirectionCellRenderer.java 4/6/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * CellRenderer for direction.
 */
class DirectionCellRenderer extends DirectionComboBox implements TableCellRenderer {
    public DirectionCellRenderer() {
        super();
        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        setButtonVisible(false);
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        installColorFontAndBorder(table, isSelected, hasFocus, row, column);
        setSelectedItem(value, false);
        return this;
    }
}
