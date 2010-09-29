/*
 * @(#)FileRow.java 6/29/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.grid.AbstractExpandableRow;
import com.jidesoft.grid.Row;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class FileRow extends AbstractExpandableRow implements Comparable<FileRow> {
    static FileSystemView _fileSystemView;
    private File _file;
    private List<?> _children;

    private static final Long SIZE_NO_AVAILABLE = -1L;

    static HashMap<FileRow, Icon> _icons = new HashMap();

    private transient boolean _isFile;
    private transient long _date;
    private transient String _name;
    private transient String _description;
    private transient long _length;
    private transient Icon _icon;

    public FileRow(File file) {
        _file = file;
        _isFile = _file.isFile();
        _date = _file.lastModified();
        _name = getName(_file);
        _description = getTypeDescription(_file);
        _length = _isFile ? _file.length() : -1;
        _icon = retrieveIcon();
    }

    public boolean isFile() {
        return _isFile;
    }

    static FileSystemView getFileSystemView() {
        if (_fileSystemView == null) {
            _fileSystemView = FileSystemView.getFileSystemView();
        }
        return _fileSystemView;
    }

    public Object getValueAt(int columnIndex) {
        try {
            switch (columnIndex) {
                case 0:
                    return this;
                case 1:
                    if (_isFile) {
                        return _length;
                    }
                    else {
                        return SIZE_NO_AVAILABLE;
                    }
                case 2:
                    return "  " + getTypeDescription();
                case 3:
                    return new Date(_date);
            }
        }
        catch (SecurityException se) {
            // ignore
        }
        return null;
    }

    @Override
    public Class<?> getCellClassAt(int columnIndex) {
        return null;
    }

    public void setChildren(List<?> children) {
        _children = children;
        if (_children != null) {
            for (Object row : _children) {
                if (row instanceof Row) {
                    ((Row) row).setParent(this);
                }
            }
        }
    }

    @Override
    public boolean hasChildren() {
        return !_isFile;
    }

    public List<?> getChildren() {
        if (_children != null) {
            return _children;
        }
        try {
            if (!_isFile) {
                File[] files = getFileSystemView().getFiles(_file, true);
                List<FileRow> children = new ArrayList();
                List<FileRow> fileChildren = new ArrayList();
                for (File file : files) {
                    FileRow fileRow = createFileRow(file);
                    if (fileRow.isFile()) {
                        fileChildren.add(fileRow);
                    }
                    else {
                        children.add(fileRow);
                    }
                }
                children.addAll(fileChildren);
                setChildren(children);
            }
        }
        catch (SecurityException se) {
            // ignore
        }
        return _children;
    }

    protected FileRow createFileRow(File file) {
        return new FileRow(file);
    }

    public File getFile() {
        return _file;
    }

    public String getName() {
        return _name;
    }

    public Icon getIcon() {
        return _icon;
    }

    private Icon retrieveIcon() {
        Icon icon = _icons.get(this);
        if (icon == null) {
            icon = getIcon(getFile());
            _icons.put(this, icon);
            return icon;
        }
        else {
            return icon;
        }
    }

    public String getTypeDescription() {
        return _description;
    }

    public static Icon getIcon(File file) {
        return getFileSystemView().getSystemIcon(file);
    }

    public static String getTypeDescription(File file) {
        return getFileSystemView().getSystemTypeDescription(file);
    }

    public static String getName(File file) {
        return getFileSystemView().getSystemDisplayName(file);
    }

    public int compareTo(FileRow o) {
        FileRow fileRow = o;
        return getName().compareToIgnoreCase(fileRow.getName());
    }
}
