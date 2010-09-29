/*
 * @(#)CollapsiblePaneGroup.java 9/13/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.pane.CollapsiblePane;
import com.jidesoft.swing.WeakPropertyChangeListener;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CollapsiblePaneGroup implements Serializable, PropertyChangeListener {

    // the set of _panes participating in this group
    protected List<CollapsiblePane> _panes = new ArrayList<CollapsiblePane>();

    /**
     * The current selection.
     */
    CollapsiblePane _selection = null;

    private final PropertyChangeListener _listener;
    private static final long serialVersionUID = 7974231736378364811L;

    /**
     * Creates a new <code>CollapsiblePaneGroup</code>.
     */
    public CollapsiblePaneGroup() {
        final KeyboardFocusManager focusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        _listener = new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                Component focusOwner = focusManager.getFocusOwner();
                if (focusOwner != null) {
                    for (CollapsiblePane pane : _panes) {
                        if (SwingUtilities.isDescendingFrom(focusOwner, pane)) {
                            pane.setEmphasized(true);
                            break;
                        }
                    }
                }
            }
        };
        focusManager.addPropertyChangeListener("focusOwner",
                new WeakPropertyChangeListener(_listener, focusManager));
    }

    /**
     * Adds the CollapsiblePane to the group.
     *
     * @param pane the pane to be added
     */
    public void add(CollapsiblePane pane) {
        if (pane == null) {
            return;
        }
        _panes.add(pane);
        pane.addPropertyChangeListener(CollapsiblePane.EMPHASIZED_PROPERTY, this);

        if (pane.isEmphasized()) {
            if (_selection == null) {
                _selection = pane;
            }
            else {
                pane.setEmphasized(false);
            }
        }
    }

    /**
     * Removes the CollapsiblePane from the group.
     *
     * @param pane the pane to be removed
     */
    public void remove(CollapsiblePane pane) {
        if (pane == null) {
            return;
        }
        pane.removePropertyChangeListener(this);
        if (pane == _selection) {
            _selection = null;
        }
        _panes.remove(pane);
    }

    /**
     * Returns all the CollapsiblePanes that are participating in this group.
     *
     * @return the collapsible pane list in the group.
     */
    public List<CollapsiblePane> getCollapsiblePanes() {
        return _panes;
    }

    /**
     * Returns the selected CollapsiblePane.
     *
     * @return the selected pane
     */
    public CollapsiblePane getSelection() {
        return _selection;
    }

    /**
     * Sets the selected value for the <code>CollapsiblePaneGroup</code>. Only one pane in the group may be selected at
     * a time.
     *
     * @param pane     the <code>CollapsiblePane</code>
     * @param selected <code>true</code> if this pane is to be selected, otherwise <code>false</code>
     */
    public void setSelected(CollapsiblePane pane, boolean selected) {
        if (selected && pane != null && pane != _selection) {
            CollapsiblePane oldSelection = _selection;
            _selection = pane;
            if (oldSelection != null) {
                oldSelection.setEmphasized(false);
            }
            pane.setEmphasized(true);
        }
    }

    /**
     * Returns whether a <code>CollapsiblePane</code> is selected.
     *
     * @param pane the collapsible pane
     * @return <code>true</code> if the pane is selected, otherwise returns <code>false</code>
     */
    public boolean isSelected(CollapsiblePane pane) {
        return (pane == _selection);
    }

    /**
     * @return Returns the number of CollapsiblePanes in the group.
     */
    public int getPaneCount() {
        if (_panes == null) {
            return 0;
        }
        else {
            return _panes.size();
        }
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if (Boolean.TRUE.equals(evt.getNewValue())) {
            for (CollapsiblePane p : getCollapsiblePanes()) {
                if (p != evt.getSource()) {
                    p.setEmphasized(false);
                }
            }
        }
    }
}

