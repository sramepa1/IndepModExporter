/*
 * @(#)DirectionComboBox.java 3/26/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.combobox.AbstractComboBox;
import com.jidesoft.combobox.PopupPanel;
import com.jidesoft.utils.SystemInfo;

import javax.swing.*;
import java.awt.*;

/**
 */
public class DirectionComboBox extends AbstractComboBox {
    /**
     * Creates a new <code>DirectionComboBox</code> using DirectionChooserPanel with 40 directions.
     */
    public DirectionComboBox() {
        super(DROPDOWN, DirectionConverter.CONTEXT);
        setType(int.class);
        initComponent();
        setEditable(false);
    }

    @Override
    public EditorComponent createEditorComponent() {
        return new DirectionEditorComponent(getType());
    }

    @Override
    public PopupPanel createPopupComponent() {
        PopupPanel panel = new DirectionChooserPanel();
        if (!SystemInfo.isMacOSX()) { // don't set background for Mac OS X
            panel.setBackground(new Color(249, 248, 247));
        }
        return panel;
    }

    /**
     * Gets selected direction.
     *
     * @return the selected direction
     */
    public int getSelectedDirection() {
        updateDirectionFromEditorComponent();
        if (getSelectedItem() instanceof Integer) {
            return (Integer) getSelectedItem();
        }
        else {
            return SwingConstants.CENTER;
        }
    }

    protected void updateDirectionFromEditorComponent() {
        Object editorValue = getEditor().getItem();
        if (editorValue instanceof Integer && !editorValue.equals(getSelectedItem())) {
            setSelectedDirection((Integer) editorValue);
        }
    }

    /**
     * Sets selected direction.
     *
     * @param direction
     */
    public void setSelectedDirection(int direction) {
        setSelectedItem(direction);
    }

    class DirectionEditorComponent extends DefaultTextFieldEditorComponent {
        private DirectionLabel _directionLabel;

        /**
         * Constructs a new <code>TextField</code>.  A default model is created, the initial string
         * is <code>null</code>, and the number of columns is set to 0.
         *
         * @param clazz
         */
        public DirectionEditorComponent(Class clazz) {
            super(clazz);
            _directionLabel = new DirectionLabel();
            _directionLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 4));
            _directionLabel.setOpaque(false);
            add(_directionLabel, BorderLayout.BEFORE_LINE_BEGINS);
        }

        @Override
        public void setItem(Object value) {
            super.setItem(value);
            if (value instanceof Integer) {
                _directionLabel.setDirection((Integer) value);
                _directionLabel.repaint();
            }
            else if (value == null) {
                _directionLabel.setDirection(SwingConstants.SOUTH);
                _directionLabel.repaint();
            }
        }

    }

    private class DirectionLabel extends JLabel {
        private int _direction;

        public DirectionLabel() {
            setOpaque(false);
        }

        public int getDirection() {
            return _direction;
        }

        public void setDirection(int direction) {
            _direction = direction;
            setIcon(DirectionChooserPanel.getDirectionIcon(direction));
        }
    }
}
