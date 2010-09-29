/*
 * @(#)ColorChooserDemo.java 2/12/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.combobox.ColorChooserPanel;
import com.jidesoft.combobox.ColorComboBox;
import com.jidesoft.converter.ColorConverter;
import com.jidesoft.converter.ObjectConverterManager;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.JideBoxLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Demoed Component: {@link ColorComboBox}, {@link ColorChooserPanel} <br> Required jar files: jide-common.jar,
 * jide-grids.jar <br> Required L&F: Jide L&F extension required
 */
public class ColorChooserDemo extends AbstractDemo {
    public ColorComboBox _colorComboBox;
    protected JLabel _messageLabel;
    private static final long serialVersionUID = 8852339234623388297L;

    public ColorChooserDemo() {
    }

    public String getName() {
        return "ColorChooser and ColorComboBox Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_GRIDS;
    }

    @Override
    public Component getOptionsPanel() {
        return createOptionsPanel();
    }

    @Override
    public String getDescription() {
        return "This is a demo of ColorChooserPanel and ColorComboBox. Please choose different options in the options pane and click on the drop down button of ColorComboBox to see the difference.\n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.combobox.ColorComboBox\n" +
                "com.jidesoft.combobox.ColorChooserPanel";
    }

    public Component getDemoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new JideBoxLayout(panel, BoxLayout.Y_AXIS));

        _colorComboBox = createColorComboBox();
        _colorComboBox.setConverterContext(ColorConverter.CONTEXT_RGB);
        _messageLabel = new JLabel();

        panel.add(_colorComboBox);
        panel.add(Box.createVerticalStrut(6));
        panel.add(_messageLabel);
        panel.add(Box.createGlue(), JideBoxLayout.VARY);
        return panel;
    }

    @Override
    public String getDemoFolder() {
        return "G6.ColorComboBoxes";
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new ColorChooserDemo());
    }

    private ColorComboBox createColorComboBox() {
        final ColorComboBox colorComboBox = new ColorComboBox();
        colorComboBox.setSelectedColor(Color.black);
        colorComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                Color selectedColor = colorComboBox.getSelectedColor();
                String colorString;
                if (colorComboBox.getConverter() != null) {
                    colorString = colorComboBox.getConverter().toString(selectedColor, colorComboBox.getConverterContext());
                }
                else {
                    colorString = ObjectConverterManager.toString(selectedColor, Color.class, colorComboBox.getConverterContext());
                }
                _messageLabel.setText("Selected color: " + colorString);
            }
        });
        return colorComboBox;
    }

    private JPanel createOptionsPanel() {
        String[] colorModes = new String[]{
                "15 Color Palette",
                "40 Color Palette",
                "216 Color Palette",
                "16 Gray Scale",
                "102 Gray Scale",
                "256 Gray Scale"
        };
        final JComboBox colorModeComboBox = new JComboBox(colorModes);
        colorModeComboBox.setSelectedIndex(_colorComboBox.getColorMode());
        colorModeComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    _colorComboBox.setColorMode(colorModeComboBox.getSelectedIndex());
                }
            }
        });

        String[] converterModes = new String[]{
                "RGB",
                "RGBA",
                "HEX",
                "HEX with alpha"
        };
        final JComboBox converterModeComboBox = new JComboBox(converterModes);
        converterModeComboBox.setSelectedIndex(0);
        converterModeComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    switch (converterModeComboBox.getSelectedIndex()) {
                        case 0:
                            _colorComboBox.setConverterContext(ColorConverter.CONTEXT_RGB);
                            if (_colorComboBox.getEditor() instanceof ColorComboBox.ColorEditorComponent) {
                                Color color = ((ColorComboBox.ColorEditorComponent) _colorComboBox.getEditor()).getColorLabel().getColor();
                                _colorComboBox.getEditor().setItem(new Color(color.getRGB()));
                            }
                            break;
                        case 1:
                            _colorComboBox.setConverterContext(ColorConverter.CONTEXT_RGBA);
                            break;
                        case 2:
                            _colorComboBox.setConverterContext(ColorConverter.CONTEXT_HEX);
                            if (_colorComboBox.getEditor() instanceof ColorComboBox.ColorEditorComponent) {
                                Color color = ((ColorComboBox.ColorEditorComponent) _colorComboBox.getEditor()).getColorLabel().getColor();
                                _colorComboBox.getEditor().setItem(new Color(color.getRGB()));
                            }
                            break;
                        case 3:
                            _colorComboBox.setConverterContext(ColorConverter.CONTEXT_HEX_WITH_ALPHA);
                            break;
                    }
                }
            }
        });

        final JCheckBox allowNone = new JCheckBox("Allow \"None\"");
        allowNone.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _colorComboBox.setAllowDefaultColor(allowNone.isSelected());
            }
        });
        allowNone.setSelected(_colorComboBox.isAllowDefaultColor());

        final JCheckBox allowMore = new JCheckBox("Allow \"More Color\"");
        allowMore.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _colorComboBox.setAllowMoreColors(allowMore.isSelected());
            }
        });
        allowMore.setSelected(_colorComboBox.isAllowMoreColors());

        final JCheckBox showColorValue = new JCheckBox("Show Color Value");
        showColorValue.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _colorComboBox.setColorValueVisible(showColorValue.isSelected());
            }
        });
        showColorValue.setSelected(_colorComboBox.isColorValueVisible());

        final JCheckBox showColorIcon = new JCheckBox("Show Color Icon");
        showColorIcon.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _colorComboBox.setColorIconVisible(showColorIcon.isSelected());
            }
        });
        showColorIcon.setSelected(_colorComboBox.isColorIconVisible());

        final JCheckBox editable = new JCheckBox("Editable");
        editable.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _colorComboBox.setEditable(editable.isSelected());
            }
        });
        editable.setSelected(_colorComboBox.isEditable());

        final JCheckBox showCross = new JCheckBox("Show Cross Background");
        showCross.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _colorComboBox.setCrossBackGroundStyle(showCross.isSelected());
            }
        });
        showCross.setSelected(_colorComboBox.isCrossBackGroundStyle());

        final JCheckBox dynamicColorButtons = new JCheckBox("Show Color Buttons using Alpha Value");
        dynamicColorButtons.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _colorComboBox.setUseAlphaColorButtons(dynamicColorButtons.isSelected());
            }
        });
        dynamicColorButtons.setSelected(_colorComboBox.isUseAlphaColorButtons());

        JPanel panel = new JPanel();
        panel.setLayout(new JideBoxLayout(panel, BoxLayout.Y_AXIS, 3));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("Set Color Mode"));
        panel.add(Box.createVerticalStrut(3), JideBoxLayout.FIX);
        panel.add(colorModeComboBox);
        panel.add(Box.createVerticalStrut(9), JideBoxLayout.FIX);
        panel.add(new JLabel("Set Converter Mode"));
        panel.add(Box.createVerticalStrut(3), JideBoxLayout.FIX);
        panel.add(converterModeComboBox);
        panel.add(Box.createVerticalStrut(9), JideBoxLayout.FIX);
        panel.add(new JLabel("Additional Buttons"));
        panel.add(Box.createVerticalStrut(3), JideBoxLayout.FIX);
        panel.add(allowNone);
        panel.add(allowMore);
        panel.add(showColorIcon);
        panel.add(showColorValue);
        panel.add(editable);
        panel.add(showCross);
        panel.add(dynamicColorButtons);
        panel.add(Box.createGlue(), JideBoxLayout.VARY);

        return panel;
    }
}
