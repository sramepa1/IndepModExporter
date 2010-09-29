/*
 * @(#)CustomizationPanel.java 2/23/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.animation.CustomAnimation;
import com.jidesoft.pane.CollapsiblePane;
import com.jidesoft.pane.CollapsiblePanes;
import com.jidesoft.swing.JideToggleButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 */
public class AnimationCustomizationPanel extends CollapsiblePanes {
    private CustomAnimation _animation;

    private int _type;

    public AnimationCustomizationPanel(int type) {
        _type = type;
        initComponents();
    }

    public void initComponents() {
        add(createCollapsiblePane("Options", createOptionsPanel()));
        add(createCollapsiblePane("Direction",
                _type == CustomAnimation.TYPE_ENTRANCE ? createShowDirectionPanel() : createHideDirectionPanel()));
        add(createCollapsiblePane("Functions", createFunctionsPanel()));
        addExpansion();
        setName("Custom");
    }

    private JComponent createCollapsiblePane(String title, JComponent content) {
        CollapsiblePane pane = new CollapsiblePane(title);
        pane.setStyle(CollapsiblePane.PLAIN_STYLE);
        content.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        JPanel panel = new JPanel(new FlowLayout());
        panel.add(content);
        pane.setContentPane(panel);
        return pane;
    }

    /**
     * Save data from UI components to animation object.
     */
    void update() {
        _animation.setType(_type);
    }

    /**
     * Load data from animation object to UI components.
     */
    void refresh() {

    }

    public void setAnimation(CustomAnimation animation) {
        _animation = animation;
        refresh();
    }

    public CustomAnimation getAnimation() {
        return (CustomAnimation) internalGetAnimation().clone();
    }

    private CustomAnimation internalGetAnimation() {
        if (_animation == null) {
            _animation = new CustomAnimation();
        }
        update();
        return _animation;
    }

    private JComponent createOptionsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2, 6, 6));
        panel.add(new JLabel("Effect:"));
        panel.add(createEffectPanel());
        panel.add(new JLabel("Speed:"));
        panel.add(createSpeedPanel());
        panel.add(new JLabel("Smoothness:"));
        panel.add(createSmoothnessPanel());
        return panel;
    }

    private JComponent createEffectPanel() {
        final JComboBox comboBox = new JComboBox(new String[]{"Fly", "Zoom", "Fade"});
        comboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    switch (comboBox.getSelectedIndex()) {
                        case 0:
                            internalGetAnimation().setEffect(CustomAnimation.EFFECT_FLY);
                            break;
                        case 1:
                            internalGetAnimation().setEffect(CustomAnimation.EFFECT_ZOOM);
                            break;
                        case 2:
                            internalGetAnimation().setEffect(CustomAnimation.EFFECT_FADE);
                            break;
                    }
                }
            }
        });
        internalGetAnimation().setEffect(CustomAnimation.EFFECT_FLY);
        comboBox.setSelectedIndex(0);
        return comboBox;
    }

    private JComponent createSpeedPanel() {
        final JComboBox comboBox = new JComboBox(new String[]{"Very Slow", "Slow", "Medium", "Fast", "Very Fast"});
        comboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    switch (comboBox.getSelectedIndex()) {
                        case 0:
                            internalGetAnimation().setSpeed(CustomAnimation.SPEED_VERY_SLOW);
                            break;
                        case 1:
                            internalGetAnimation().setSpeed(CustomAnimation.SPEED_SLOW);
                            break;
                        case 2:
                            internalGetAnimation().setSpeed(CustomAnimation.SPEED_MEDIUM);
                            break;
                        case 3:
                            internalGetAnimation().setSpeed(CustomAnimation.SPEED_FAST);
                            break;
                        case 4:
                            internalGetAnimation().setSpeed(CustomAnimation.SPEED_VERY_FAST);
                            break;
                    }
                }
            }
        });
        internalGetAnimation().setSpeed(CustomAnimation.SPEED_MEDIUM);
        comboBox.setSelectedIndex(2);
        return comboBox;
    }

    private JComponent createSmoothnessPanel() {
        final JComboBox comboBox = new JComboBox(new String[]{"Very Smooth", "Smooth", "Medium", "Rough", "Very Rough"});
        comboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    switch (comboBox.getSelectedIndex()) {
                        case 0:
                            internalGetAnimation().setSmoothness(CustomAnimation.SMOOTHNESS_VERY_SMOOTH);
                            break;
                        case 1:
                            internalGetAnimation().setSmoothness(CustomAnimation.SMOOTHNESS_SMOOTH);
                            break;
                        case 2:
                            internalGetAnimation().setSmoothness(CustomAnimation.SMOOTHNESS_MEDIUM);
                            break;
                        case 3:
                            internalGetAnimation().setSmoothness(CustomAnimation.SMOOTHNESS_ROUGH);
                            break;
                        case 4:
                            internalGetAnimation().setSmoothness(CustomAnimation.SMOOTHNESS_VERY_ROUGH);
                            break;
                    }
                }
            }
        });
        internalGetAnimation().setSmoothness(CustomAnimation.SMOOTHNESS_MEDIUM);
        comboBox.setSelectedIndex(2);
        return comboBox;
    }

    private JComponent createFunctionsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2, 6, 6));
        panel.add(new JLabel("Function X:"));
        panel.add(createFunctionXPanel());
        panel.add(new JLabel("Function Y:"));
        panel.add(createFunctionYPanel());
        panel.add(new JLabel("Function Zoom:"));
        panel.add(createFunctionFadePanel());
        panel.add(new JLabel("Function Fade:"));
        panel.add(createFunctionZoomPanel());
        return panel;
    }

    private JComponent createFunctionXPanel() {
        final JComboBox comboBox = new JComboBox(new String[]{"Linear", "Pow2", "Pow3", "Pow1/2", "Bounce", "Vibrate"});
        comboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    switch (comboBox.getSelectedIndex()) {
                        case 0:
                            internalGetAnimation().setFunctionX(CustomAnimation.FUNC_LINEAR);
                            break;
                        case 1:
                            internalGetAnimation().setFunctionX(CustomAnimation.FUNC_POW2);
                            break;
                        case 2:
                            internalGetAnimation().setFunctionX(CustomAnimation.FUNC_POW3);
                            break;
                        case 3:
                            internalGetAnimation().setFunctionX(CustomAnimation.FUNC_POW_HALF);
                            break;
                        case 4:
                            internalGetAnimation().setFunctionX(CustomAnimation.FUNC_BOUNCE);
                            break;
                        case 5:
                            internalGetAnimation().setFunctionX(CustomAnimation.FUNC_VIBRATE);
                            break;
                    }
                }
            }
        });
        internalGetAnimation().setFunctionX(CustomAnimation.FUNC_LINEAR);
        comboBox.setSelectedIndex(0);
        return comboBox;
    }

    private JComponent createFunctionYPanel() {
        final JComboBox comboBox = new JComboBox(new String[]{"Linear", "Pow2", "Pow3", "Pow1/2", "Bounce", "Vibrate"});
        comboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    switch (comboBox.getSelectedIndex()) {
                        case 0:
                            internalGetAnimation().setFunctionY(CustomAnimation.FUNC_LINEAR);
                            break;
                        case 1:
                            internalGetAnimation().setFunctionY(CustomAnimation.FUNC_POW2);
                            break;
                        case 2:
                            internalGetAnimation().setFunctionY(CustomAnimation.FUNC_POW3);
                            break;
                        case 3:
                            internalGetAnimation().setFunctionY(CustomAnimation.FUNC_POW_HALF);
                            break;
                        case 4:
                            internalGetAnimation().setFunctionY(CustomAnimation.FUNC_BOUNCE);
                            break;
                        case 5:
                            internalGetAnimation().setFunctionY(CustomAnimation.FUNC_VIBRATE);
                            break;
                    }
                }
            }
        });
        internalGetAnimation().setFunctionY(CustomAnimation.FUNC_LINEAR);
        comboBox.setSelectedIndex(0);
        return comboBox;
    }

    private JComponent createFunctionFadePanel() {
        final JComboBox comboBox = new JComboBox(new String[]{"Linear", "Pow2", "Pow3", "Pow1/2"});
        comboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    switch (comboBox.getSelectedIndex()) {
                        case 0:
                            internalGetAnimation().setFunctionFade(CustomAnimation.FUNC_LINEAR);
                            break;
                        case 1:
                            internalGetAnimation().setFunctionFade(CustomAnimation.FUNC_POW2);
                            break;
                        case 2:
                            internalGetAnimation().setFunctionFade(CustomAnimation.FUNC_POW3);
                            break;
                        case 3:
                            internalGetAnimation().setFunctionFade(CustomAnimation.FUNC_POW_HALF);
                            break;
                    }
                }
            }
        });
        internalGetAnimation().setFunctionFade(CustomAnimation.FUNC_LINEAR);
        comboBox.setSelectedIndex(0);
        return comboBox;
    }

    private JComponent createFunctionZoomPanel() {
        final JComboBox comboBox = new JComboBox(new String[]{"Linear", "Pow2", "Pow3", "Pow1/2"});
        comboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    switch (comboBox.getSelectedIndex()) {
                        case 0:
                            internalGetAnimation().setFunctionZoom(CustomAnimation.FUNC_LINEAR);
                            break;
                        case 1:
                            internalGetAnimation().setFunctionZoom(CustomAnimation.FUNC_POW2);
                            break;
                        case 2:
                            internalGetAnimation().setFunctionZoom(CustomAnimation.FUNC_POW3);
                            break;
                        case 3:
                            internalGetAnimation().setFunctionZoom(CustomAnimation.FUNC_POW_HALF);
                            break;
                    }
                }
            }
        });
        internalGetAnimation().setFunctionZoom(CustomAnimation.FUNC_LINEAR);
        comboBox.setSelectedIndex(0);
        return comboBox;
    }

    private JComponent createShowDirectionPanel() {
        ButtonGroup group = new ButtonGroup();
        JideToggleButton top = new JideToggleButton(AnimationIconsFactory.getImageIcon(AnimationIconsFactory.Direction.S));
        top.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    internalGetAnimation().setDirection(CustomAnimation.TOP);
                }
            }
        });
        JideToggleButton topRight = new JideToggleButton(AnimationIconsFactory.getImageIcon(AnimationIconsFactory.Direction.SW));
        topRight.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    internalGetAnimation().setDirection(CustomAnimation.TOP_RIGHT);
                }
            }
        });
        JideToggleButton topLeft = new JideToggleButton(AnimationIconsFactory.getImageIcon(AnimationIconsFactory.Direction.SE));
        topLeft.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    internalGetAnimation().setDirection(CustomAnimation.TOP_LEFT);
                }
            }
        });
        JideToggleButton right = new JideToggleButton(AnimationIconsFactory.getImageIcon(AnimationIconsFactory.Direction.W));
        right.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    internalGetAnimation().setDirection(CustomAnimation.RIGHT);
                }
            }
        });
        JideToggleButton bottom = new JideToggleButton(AnimationIconsFactory.getImageIcon(AnimationIconsFactory.Direction.N));
        bottom.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    internalGetAnimation().setDirection(CustomAnimation.BOTTOM);
                }
            }
        });
        JideToggleButton bottomRight = new JideToggleButton(AnimationIconsFactory.getImageIcon(AnimationIconsFactory.Direction.NW));
        bottomRight.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    internalGetAnimation().setDirection(CustomAnimation.BOTTOM_RIGHT);
                }
            }
        });
        JideToggleButton bottomLeft = new JideToggleButton(AnimationIconsFactory.getImageIcon(AnimationIconsFactory.Direction.NE));
        bottomLeft.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    internalGetAnimation().setDirection(CustomAnimation.BOTTOM_LEFT);
                }
            }
        });
        JideToggleButton left = new JideToggleButton(AnimationIconsFactory.getImageIcon(AnimationIconsFactory.Direction.E));
        left.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    internalGetAnimation().setDirection(CustomAnimation.LEFT);
                }
            }
        });
        group.add(top);
        group.add(right);
        group.add(left);
        group.add(bottom);
        group.add(topLeft);
        group.add(topRight);
        group.add(bottomLeft);
        group.add(bottomRight);
        bottom.setSelected(true);
        internalGetAnimation().setDirection(CustomAnimation.BOTTOM);
        JPanel panel = new JPanel(new GridLayout(3, 3, 2, 2));
        panel.add(topLeft);
        panel.add(top);
        panel.add(topRight);
        panel.add(left);
        panel.add(new JLabel());
        panel.add(right);
        panel.add(bottomLeft);
        panel.add(bottom);
        panel.add(bottomRight);
        return panel;
    }


    private JComponent createHideDirectionPanel() {
        ButtonGroup group = new ButtonGroup();
        JideToggleButton top = new JideToggleButton(AnimationIconsFactory.getImageIcon(AnimationIconsFactory.Direction.N));
        top.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    internalGetAnimation().setDirection(CustomAnimation.TOP);
                }
            }
        });
        JideToggleButton topRight = new JideToggleButton(AnimationIconsFactory.getImageIcon(AnimationIconsFactory.Direction.NE));
        topRight.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    internalGetAnimation().setDirection(CustomAnimation.TOP_RIGHT);
                }
            }
        });
        JideToggleButton topLeft = new JideToggleButton(AnimationIconsFactory.getImageIcon(AnimationIconsFactory.Direction.NW));
        topLeft.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    internalGetAnimation().setDirection(CustomAnimation.TOP_LEFT);
                }
            }
        });
        JideToggleButton right = new JideToggleButton(AnimationIconsFactory.getImageIcon(AnimationIconsFactory.Direction.E));
        right.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    internalGetAnimation().setDirection(CustomAnimation.RIGHT);
                }
            }
        });
        JideToggleButton bottom = new JideToggleButton(AnimationIconsFactory.getImageIcon(AnimationIconsFactory.Direction.S));
        bottom.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    internalGetAnimation().setDirection(CustomAnimation.BOTTOM);
                }
            }
        });
        JideToggleButton bottomRight = new JideToggleButton(AnimationIconsFactory.getImageIcon(AnimationIconsFactory.Direction.SE));
        bottomRight.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    internalGetAnimation().setDirection(CustomAnimation.BOTTOM_RIGHT);
                }
            }
        });
        JideToggleButton bottomLeft = new JideToggleButton(AnimationIconsFactory.getImageIcon(AnimationIconsFactory.Direction.SW));
        bottomLeft.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    internalGetAnimation().setDirection(CustomAnimation.BOTTOM_LEFT);
                }
            }
        });
        JideToggleButton left = new JideToggleButton(AnimationIconsFactory.getImageIcon(AnimationIconsFactory.Direction.W));
        left.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    internalGetAnimation().setDirection(CustomAnimation.LEFT);
                }
            }
        });
        group.add(top);
        group.add(right);
        group.add(left);
        group.add(bottom);
        group.add(topLeft);
        group.add(topRight);
        group.add(bottomLeft);
        group.add(bottomRight);
        bottom.setSelected(true);
        internalGetAnimation().setDirection(CustomAnimation.BOTTOM);
        JPanel panel = new JPanel(new GridLayout(3, 3, 2, 2));
        panel.add(topLeft);
        panel.add(top);
        panel.add(topRight);
        panel.add(left);
        panel.add(new JLabel());
        panel.add(right);
        panel.add(bottomLeft);
        panel.add(bottom);
        panel.add(bottomRight);
        return panel;
    }
}
