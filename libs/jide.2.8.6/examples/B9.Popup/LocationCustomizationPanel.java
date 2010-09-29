/*
 * @(#)LocationCustomizationPanel.java 2/24/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.pane.CollapsiblePane;
import com.jidesoft.swing.JideToggleButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 */
public class LocationCustomizationPanel extends CollapsiblePane {
    private int _displayLocation;

    public LocationCustomizationPanel() {
        super("Location");
        initComponents();
    }

    public void initComponents() {
        setStyle(PLAIN_STYLE);
        JPanel panel = new JPanel(new FlowLayout());
        panel.add(createDisplayLocationPanel());
        setContentPane(panel);
        setName("Location");
    }

    public int getDisplayLocation() {
        return _displayLocation;
    }

    public void setDisplayLocation(int displayLocation) {
        _displayLocation = displayLocation;
    }

    private JComponent createDisplayLocationPanel() {
        ButtonGroup group = new ButtonGroup();
        JideToggleButton center = new JideToggleButton(AnimationIconsFactory.getImageIcon(AnimationIconsFactory.Location.PIC));
        center.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    _displayLocation = SwingConstants.CENTER;
                }
            }
        });
        JideToggleButton northEast = new JideToggleButton(AnimationIconsFactory.getImageIcon(AnimationIconsFactory.Location.PIC));
        northEast.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    _displayLocation = SwingConstants.NORTH_EAST;
                }
            }
        });
        JideToggleButton northWest = new JideToggleButton(AnimationIconsFactory.getImageIcon(AnimationIconsFactory.Location.PIC));
        northWest.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    _displayLocation = SwingConstants.NORTH_WEST;
                }
            }
        });
        JideToggleButton southEast = new JideToggleButton(AnimationIconsFactory.getImageIcon(AnimationIconsFactory.Location.PIC));
        southEast.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    _displayLocation = SwingConstants.SOUTH_EAST;
                }
            }
        });
        JideToggleButton southWest = new JideToggleButton(AnimationIconsFactory.getImageIcon(AnimationIconsFactory.Location.PIC));
        southWest.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    _displayLocation = SwingConstants.SOUTH_WEST;
                }
            }
        });
        JideToggleButton north = new JideToggleButton(AnimationIconsFactory.getImageIcon(AnimationIconsFactory.Location.PIC));
        north.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    _displayLocation = SwingConstants.NORTH;
                }
            }
        });
        JideToggleButton south = new JideToggleButton(AnimationIconsFactory.getImageIcon(AnimationIconsFactory.Location.PIC));
        south.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    _displayLocation = SwingConstants.SOUTH;
                }
            }
        });
        JideToggleButton west = new JideToggleButton(AnimationIconsFactory.getImageIcon(AnimationIconsFactory.Location.PIC));
        west.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    _displayLocation = SwingConstants.WEST;
                }
            }
        });
        JideToggleButton east = new JideToggleButton(AnimationIconsFactory.getImageIcon(AnimationIconsFactory.Location.PIC));
        east.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    _displayLocation = SwingConstants.EAST;
                }
            }
        });
        group.add(center);
        group.add(north);
        group.add(east);
        group.add(south);
        group.add(west);
        group.add(northWest);
        group.add(northEast);
        group.add(southWest);
        group.add(southEast);
        center.setSelected(true);
        _displayLocation = SwingConstants.CENTER;
        JPanel panel = new JPanel(new GridLayout(3, 3, 2, 2));
        panel.add(northWest);
        panel.add(north);
        panel.add(northEast);
        panel.add(west);
        panel.add(center);
        panel.add(east);
        panel.add(southWest);
        panel.add(south);
        panel.add(southEast);
        panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.DARK_GRAY), BorderFactory.createEmptyBorder(2, 2, 2, 2)));
        return panel;
    }
}
