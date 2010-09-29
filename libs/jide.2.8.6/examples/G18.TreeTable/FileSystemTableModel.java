/*
 * @(#)FileSystemTableModel.java 6/29/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.grid.CellStyle;
import com.jidesoft.grid.StyleModel;
import com.jidesoft.grid.TreeTableModel;

import java.awt.*;
import java.util.Date;
import java.util.List;

public class FileSystemTableModel extends TreeTableModel implements StyleModel {
    static final protected String[] COLUMN_NAMES = {"Name", "Size", "Type", "Modified"};

    public FileSystemTableModel(List rows) {
        super(rows);
    }

    @Override
    public String getColumnName(int column) {
        return COLUMN_NAMES[column];
    }

    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    static final Color BACKGROUND = new Color(247, 247, 247);
    static final CellStyle CELL_STYLE = new CellStyle();

    static {
        CELL_STYLE.setBackground(BACKGROUND);
    }

    public CellStyle getCellStyleAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            return CELL_STYLE;
        }
        else {
            return null;
        }
    }

    public boolean isCellStyleOn() {
        return true;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return FileRow.class;
            case 1:
                return Long.class;
            case 2:
                return String.class;
            case 3:
                return Date.class;
        }
        return super.getColumnClass(columnIndex);
    }
}
