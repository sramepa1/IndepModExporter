/*
 * @(#)DirectionCellEditor.java 4/6/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.combobox.AbstractComboBox;
import com.jidesoft.grid.AbstractComboBoxCellEditor;
import com.jidesoft.grid.EditorContext;

/**
 * A direction cell editor.
 */
public class DirectionCellEditor extends AbstractComboBoxCellEditor {

    public static final EditorContext CONTEXT = new EditorContext("Direction");

    /**
     * Creates a DirectionCellEditor.
     */
    public DirectionCellEditor() {
    }

    @Override
    public AbstractComboBox createAbstractComboBox() {
        return new DirectionComboBox();
    }
}