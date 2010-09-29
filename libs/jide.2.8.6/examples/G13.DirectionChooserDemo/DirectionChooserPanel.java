/*
 * @(#)DirectionChooserPanel.java 3/26/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.combobox.PopupPanel;
import com.jidesoft.converter.ObjectConverterManager;
import com.jidesoft.swing.JideToggleButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 */
public class DirectionChooserPanel extends PopupPanel implements ItemListener {
    /**
     * JPanel which holds all direction buttons.
     */
    protected JComponent _directionPanel;


    /**
     * Creates a new <code>ColorChooserPanel</code>.
     */
    public DirectionChooserPanel() {
        initComponent();
        addItemListener(this);
    }

    /**
     * Gets the selected direction.
     *
     * @return the selected direction.
     */
    public int getSelectedDirection() {
        if (getSelectedObject() != null && getSelectedObject() instanceof Integer) {
            return (Integer) getSelectedObject();
        }
        else {
            return SwingConstants.SOUTH;
        }
    }

    /**
     * Sets the selected direction.
     *
     * @param direction
     */
    public void setSelectedDirection(int direction) {
        JButton button = getButton(direction);
        if (button != null) {
            button.getModel().setRollover(false);
        }
        setSelectedObject(direction);
    }

    private JButton getButton(int direction) {
        if (_directionPanel == null) {
            return null;
        }
        for (int i = 0; i < _directionPanel.getComponentCount(); i++) {
            Component component = _directionPanel.getComponent(i);
            if (component instanceof DirectionButton) {
                if (((DirectionButton) component).getDirection() == direction) {
                    return ((DirectionButton) component);
                }
            }
        }
        return null;
    }

    private JComponent createDisplayLocationPanel() {
        ButtonGroup group = new ButtonGroup();
        SelectAction selectAction = new SelectAction();

        DirectionButton bottom = new DirectionButton(SwingConstants.SOUTH);
        bottom.addActionListener(selectAction);

        DirectionButton right = new DirectionButton(SwingConstants.EAST);
        right.addActionListener(selectAction);

        DirectionButton left = new DirectionButton(SwingConstants.WEST);
        left.addActionListener(selectAction);

        DirectionButton top = new DirectionButton(SwingConstants.NORTH);
        top.addActionListener(selectAction);

        DirectionButton bottomLeft = new DirectionButton(SwingConstants.SOUTH_WEST);
        bottomLeft.addActionListener(selectAction);

        DirectionButton bottomRight = new DirectionButton(SwingConstants.SOUTH_EAST);
        bottomRight.addActionListener(selectAction);

        DirectionButton topLeft = new DirectionButton(SwingConstants.NORTH_WEST);
        topLeft.addActionListener(selectAction);

        DirectionButton topRight = new DirectionButton(SwingConstants.NORTH_EAST);
        topRight.addActionListener(selectAction);

        group.add(bottom);
        group.add(left);
        group.add(right);
        group.add(top);
        group.add(bottomLeft);
        group.add(bottomRight);
        group.add(topLeft);
        group.add(topRight);

        JPanel panel = new JPanel(new GridLayout(3, 3));
        panel.add(topLeft);
        panel.add(top);
        panel.add(topRight);
        panel.add(left);
        panel.add(new JPanel());
        panel.add(right);
        panel.add(bottomLeft);
        panel.add(bottom);
        panel.add(bottomRight);
        return panel;
    }

    private class SelectAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof DirectionButton) {
                DirectionButton button = (DirectionButton) e.getSource();
                setSelectedDirection(button.getDirection());
                button.repaint();
            }
        }
    }


    protected void initComponent() {
        setLayout(new BorderLayout());

        _directionPanel = createDisplayLocationPanel();
        add(_directionPanel, BorderLayout.CENTER);

        setRequestFocusEnabled(true);
        setFocusable(true);
    }

    private class DirectionButton extends JideToggleButton {
        private int _direction;

        /**
         * Creates a button with no set text or icon.
         *
         * @param direction
         */
        public DirectionButton(int direction) {
            super(getDirectionIcon(direction));
            setToolTipText(ObjectConverterManager.toString(direction, Integer.class, DirectionConverter.CONTEXT));
            _direction = direction;
            setOpaque(false);
        }

        public int getDirection() {
            return _direction;
        }

        public void setDirection(int direction) {
            _direction = direction;
        }
    }

    static Icon getDirectionIcon(int direction) {
        String s = null;
        switch (direction) {
            case SwingConstants.NORTH:
                s = AnimationIconsFactory.Direction.S;
                break;
            case SwingConstants.NORTH_EAST:
                s = AnimationIconsFactory.Direction.SW;
                break;
            case SwingConstants.EAST:
                s = AnimationIconsFactory.Direction.W;
                break;
            case SwingConstants.SOUTH_EAST:
                s = AnimationIconsFactory.Direction.NW;
                break;
            case SwingConstants.SOUTH:
                s = AnimationIconsFactory.Direction.N;
                break;
            case SwingConstants.SOUTH_WEST:
                s = AnimationIconsFactory.Direction.NE;
                break;
            case SwingConstants.WEST:
                s = AnimationIconsFactory.Direction.E;
                break;
            case SwingConstants.NORTH_WEST:
                s = AnimationIconsFactory.Direction.SE;
                break;
        }
        return AnimationIconsFactory.getImageIcon(s);
    }

    public void itemStateChanged(ItemEvent e) {
        if (e.getItem() instanceof Integer) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                JButton button = getButton((Integer) e.getItem());
                if (button != null) {
                    button.setSelected(true);
                }
            }
            else if (e.getStateChange() == ItemEvent.DESELECTED) {
                JButton button = getButton((Integer) e.getItem());
                if (button != null) {
                    button.setSelected(false);
                }
            }
        }
    }
}
