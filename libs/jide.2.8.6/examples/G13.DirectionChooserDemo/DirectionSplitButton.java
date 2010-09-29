/*
 * @(#)DirectionSplitButton.java 3/28/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.plaf.UIDefaultsLookup;
import com.jidesoft.swing.JideSplitButton;

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 */
public class DirectionSplitButton extends JideSplitButton {
    private int _selectedDirection;
    public DirectionChooserPanel _directionChooserPanel;
    public ItemListener _itemListener;

    /**
     * Creates a split button using the specified icon and a rectangle which will be painted as the
     * last selected color. If rect is empty, no color will be painted. The x and y of rect is
     * relative to the icon.
     */
    public DirectionSplitButton() {
        _directionChooserPanel = createDirectionChooserPanel();
        add(_directionChooserPanel);
        _itemListener = new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    hideMenu();
                    setSelectedDirection((Integer) e.getItem());
                    ActionEvent ae = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "");
                    fireActionPerformed(ae);
                }
            }
        };
        _directionChooserPanel.addItemListener(_itemListener);
    }

    /**
     * By default, a DirectionChooserPanel of PALETTE_COLOR_40 will be used. If you want a different
     * palette, you can override this method to provide your own <code>DirectionChooserPanel</code>.
     *
     * @return
     */
    protected DirectionChooserPanel createDirectionChooserPanel() {
        DirectionChooserPanel DirectionChooserPanel = new DirectionChooserPanel() {
            @Override
            public void updateUI() {
                super.updateUI();
                setBackground(UIDefaultsLookup.getColor("MenuItem.background"));
            }
        };
        DirectionChooserPanel.setBackground(UIDefaultsLookup.getColor("MenuItem.background"));
        return DirectionChooserPanel;
    }

    /**
     * @return the selected direction.
     */
    public int getSelectedDirection() {
        return _selectedDirection;
    }

    /**
     * @param direction
     */
    public void setSelectedDirection(int direction) {
        _selectedDirection = direction;
        setIcon(DirectionChooserPanel.getDirectionIcon(direction));
        _directionChooserPanel.removeItemListener(_itemListener);
        _directionChooserPanel.setSelectedDirection(direction);
        _directionChooserPanel.addItemListener(_itemListener);
    }
}
