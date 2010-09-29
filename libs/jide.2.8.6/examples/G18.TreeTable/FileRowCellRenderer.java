/*
 * @(#)FilecellRenderer.java 6/29/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class FileRowCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (value instanceof FileRow) {
            FileRow fileRow = (FileRow) value;
            JLabel label = (JLabel) super.getTableCellRendererComponent(table, fileRow.getName(), isSelected, hasFocus, row, column);
            try {
                label.setIcon(fileRow.getIcon());
            }
            catch (Exception e) {
                System.out.println(fileRow.getFile().getAbsolutePath());
            }
            label.setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 3));
            return label;
        }
        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }
}
