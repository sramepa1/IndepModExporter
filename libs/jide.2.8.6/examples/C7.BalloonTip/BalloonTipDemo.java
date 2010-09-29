import com.jidesoft.combobox.ColorComboBox;
import com.jidesoft.icons.JideIconsFactory;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.popup.JidePopup;
import com.jidesoft.swing.JideBoxLayout;
import com.jidesoft.tooltip.BalloonShape;
import com.jidesoft.tooltip.BalloonTip;
import com.jidesoft.tooltip.ShadowSettings;
import com.jidesoft.tooltip.ShadowStyle;
import com.jidesoft.tooltip.shadows.*;
import com.jidesoft.tooltip.shapes.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class BalloonTipDemo extends AbstractDemo {
    private BalloonTip _balloontip;
    private BalloonShape _shape;
    private int _position;
    private ShadowStyle _shadowStyle = new LeftBottomBackward();
    protected JCheckBox _opaqueCheckBox;
    protected ColorComboBox _colorComboBox;
    protected JButton _ownerButton;
    protected JSlider _leftRadio;
    protected JSlider _rightRadio;
    protected JSlider _vertexRadio;
    protected JSlider _balloonRadio;
    protected JSlider _cornerSize;
    protected JSlider _xRatio;
    protected JSlider _yRatio;
    protected JSlider _xOffset;
    protected JSlider _yOffset;
    protected JSlider _scale;
    protected JPanel _dropShadow;
    protected JPanel _perspectiveShadow;
    protected JPanel _optionsPanel;

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new BalloonTipDemo());

    }

    public String getName() {
        return "Balloon Tip Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_COMPONENTS;
    }

    public Component getDemoPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setPreferredSize(new Dimension(400, 600));
        _ownerButton = new JButton("Click to show the Balloon Tooltip");
        _ownerButton.setToolTipText("Click to show the Balloon Tooltip");
        _ownerButton.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                toggleToolTip();
            }
        });
        panel.add(_ownerButton);
        return panel;
    }

    private void toggleToolTip() {
        if (_balloontip != null && _balloontip.isVisible()) {
            hideToolTip();
        }
        else {
            showToolTip();
        }
    }

    private void showToolTip() {
        _balloontip = new BalloonTip(createToolTipContent()) {
            @Override
            protected void customizePopup(JidePopup popup) {
                super.customizePopup(popup);
                popup.addExcludedComponent(_optionsPanel);
            }
        };
        ShadowSettings settings = _balloontip.getShadowSettings();

        if (_opaqueCheckBox != null && _opaqueCheckBox.isSelected()) {
            settings.setColor(_colorComboBox.getSelectedColor());
            settings.setOpacity(1f);
        }
        else {
            settings.setColor(_colorComboBox == null ? Color.BLACK : _colorComboBox.getSelectedColor());
            settings.setOpacity(0.25f);
        }

        if (_shape != null && _shape instanceof RoundedRectangularBalloonShape) {
            ((RoundedRectangularBalloonShape) _shape).setArrowLeftRatio(_leftRadio.getValue() / 100.0);
            ((RoundedRectangularBalloonShape) _shape).setArrowRightRatio(_rightRadio.getValue() / 100.0);
            ((RoundedRectangularBalloonShape) _shape).setVertexPosition(_vertexRadio.getValue() / 100.0);
            ((RoundedRectangularBalloonShape) _shape).setBalloonSizeRatio(_balloonRadio.getValue() / 100.0);
            ((RoundedRectangularBalloonShape) _shape).setCornerSize(_cornerSize.getValue());
            ((RoundedRectangularBalloonShape) _shape).setPosition(_position);
        }
        if (_shape != null && _shape instanceof RoundedRectangularShape) {
            ((RoundedRectangularShape) _shape).setCornerSize(_cornerSize.getValue());
        }
        _balloontip.setBalloonShape(_shape);
        _balloontip.setPreferredSize(new Dimension(600, 600));

        if (_shadowStyle instanceof PerspectiveShadow) {
            ((PerspectiveShadow) _shadowStyle).setXRatio(_xRatio.getValue() / 100.0);
            ((PerspectiveShadow) _shadowStyle).setYRatio(_yRatio.getValue() / 100.0);
        }
        else if (_shadowStyle instanceof DropShadow) {
            ((DropShadow) _shadowStyle).setXOffset(_xOffset.getValue());
            ((DropShadow) _shadowStyle).setYOffset(_yOffset.getValue());
            ((DropShadow) _shadowStyle).setScale(_scale.getValue() / 10.0);
        }

        _balloontip.setShadowStyle(_shadowStyle);
        _balloontip.show(_ownerButton, 0, 0);
    }

    private void hideToolTip() {
        if (_balloontip != null) {
            _balloontip.hide();
            _balloontip = null;
        }
    }

    @Override
    public Component getOptionsPanel() {
        _optionsPanel = new JPanel();
        _optionsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        _optionsPanel.setLayout(new JideBoxLayout(_optionsPanel, JideBoxLayout.Y_AXIS, 6));

        _perspectiveShadow = getPerspectiveShadowStylePanel();
        _dropShadow = getDropShadowStylePanel();

        _optionsPanel.add(getBalloonShapePanel());
        _optionsPanel.add(getBalloonSizePanel());
        _optionsPanel.add(getBalloonPositionPanel());
        _optionsPanel.add(getShadowStylePanel());
        _optionsPanel.add(getShadowCompositePanel());
        _optionsPanel.add(_perspectiveShadow);
        _optionsPanel.add(_dropShadow);
        _optionsPanel.add(Box.createGlue(), JideBoxLayout.VARY);
        return _optionsPanel;
    }

    JPanel getShadowStylePanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Shadow Style"));
        panel.setLayout(new GridLayout(0, 1));

        JRadioButton button = (JRadioButton) panel.add(createShadowStyleRadioButton("LeftBottomBackward", new LeftBottomBackward()));
        button.setSelected(true);
        panel.add(createShadowStyleRadioButton("LeftBottomForward", new LeftBottomForward()));
        panel.add(createShadowStyleRadioButton("RightBottomBackward", new RightBottomBackward()));
        panel.add(createShadowStyleRadioButton("RightBottomForward", new RightBottomForward()));

        panel.add(createShadowStyleRadioButton("LeftBottomDrop", new LeftBottomDrop()));
        panel.add(createShadowStyleRadioButton("LeftTopDrop", new LeftTopDrop()));
        panel.add(createShadowStyleRadioButton("RightBottomDrop", new RightBottomDrop()));
        panel.add(createShadowStyleRadioButton("RightTopDrop", new RightTopDrop()));

        panel.add(createShadowStyleRadioButton("No Shadow", null));

        return panel;
    }

    JPanel getPerspectiveShadowStylePanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Perspective Shadow Style"));
        panel.setLayout(new GridLayout(0, 1));

        _xRatio = new JSlider(0, 100, 67);
        _yRatio = new JSlider(0, 100, 40);

        panel.add(createSliderPanel("X Ratio", _xRatio, 100.0));
        panel.add(createSliderPanel("Y Ratio", _yRatio, 100.0));

        return panel;
    }

    JPanel getDropShadowStylePanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Drop Shadow Style"));
        panel.setLayout(new GridLayout(0, 1));

        _xOffset = new JSlider(0, 100, 40);
        _yOffset = new JSlider(0, 100, 40);
        _scale = new JSlider(10, 100, 10);

        panel.add(createSliderPanel("X Offset", _xOffset, 1));
        panel.add(createSliderPanel("Y Offset", _yOffset, 1));
        panel.add(createSliderPanel("Scale", _scale, 10));

        return panel;
    }

    JPanel getBalloonShapePanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Balloon Shape"));
        panel.setLayout(new GridLayout(0, 1));

        RoundedRectangularBalloonShape roundedRectangular = new RoundedRectangularBalloonShape();
        JRadioButton button = (JRadioButton) panel.add(createShapeRadioButton("Rounded Rectangular Callout (Default)", roundedRectangular));
        button.setSelected(true);
        panel.add(createShapeRadioButton("Rectangular Callout", new RectangularBalloonShape()));
        panel.add(createShapeRadioButton("Rounded Rectangular", new RoundedRectangularShape()));
        panel.add(createShapeRadioButton("Rectangular", new RectangularShape()));
        panel.add(createShapeRadioButton("Oval", new OvalShape()));

        return panel;
    }

    JPanel getBalloonPositionPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Balloon Position"));
        panel.setLayout(new GridLayout(2, 2));

        JRadioButton button = (JRadioButton) panel.add(createPositionRadioButton("Top (Default)", SwingConstants.TOP));
        button.setSelected(true);
        panel.add(createPositionRadioButton("Left", SwingConstants.LEFT));
        panel.add(createPositionRadioButton("Bottom", SwingConstants.BOTTOM));
        panel.add(createPositionRadioButton("Right", SwingConstants.RIGHT));

        return panel;
    }

    JPanel getBalloonSizePanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Balloon Shape Dimension"));
        panel.setLayout(new GridLayout(0, 1));

        _leftRadio = new JSlider(0, 100, 50);
        _rightRadio = new JSlider(0, 100, 25);
        _vertexRadio = new JSlider(0, 100, 25);
        _balloonRadio = new JSlider(0, 100, 75);
        _cornerSize = new JSlider(0, 100, 18);

        panel.add(createSliderPanel("Arrow Left/Top Radio", _leftRadio, 100.0));
        panel.add(createSliderPanel("Arrow Right/Bottom Radio", _rightRadio, 100.0));
        panel.add(createSliderPanel("Vertex Position", _vertexRadio, 100.0));
        panel.add(createSliderPanel("Balloon Size Radio", _balloonRadio, 100.0));
        panel.add(createSliderPanel("Corner Size", _cornerSize, 1));

        return panel;
    }

    JPanel getShadowCompositePanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Shadow Composite"));
        panel.setLayout(new GridLayout(0, 1));

        _opaqueCheckBox = new JCheckBox("Opaque Shadow");
        _opaqueCheckBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (_balloontip != null) {
                    hideToolTip();
                    showToolTip();
                }
            }
        });
        panel.add(_opaqueCheckBox);
        _colorComboBox = new ColorComboBox();
        _colorComboBox.setSelectedColor(Color.BLACK);
        _colorComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (_balloontip != null) {
                    hideToolTip();
                    showToolTip();
                }
            }
        });
        panel.add(_colorComboBox);

        return panel;
    }

    private ButtonGroup _shapeGroup = new ButtonGroup();

    private JRadioButton createShapeRadioButton(final String text, final BalloonShape shape) {
        JRadioButton button = new JRadioButton(text);
        _shapeGroup.add(button);
        button.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    _shape = shape;
                    if (_balloontip != null) {
                        hideToolTip();
                        showToolTip();
                    }
                }
            }
        });
        return button;
    }

    private ButtonGroup _positionGroup = new ButtonGroup();

    private JRadioButton createPositionRadioButton(final String text, final int position) {
        JRadioButton button = new JRadioButton(text);
        _positionGroup.add(button);
        button.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    _position = position;
                    if (_leftRadio != null) {
                        hideToolTip();
                        showToolTip();
                    }
                }
            }
        });
        return button;
    }

    private ButtonGroup _shadowGroup = new ButtonGroup();

    private JRadioButton createShadowStyleRadioButton(final String text, final ShadowStyle shadowStyle) {
        JRadioButton button = new JRadioButton(text);
        _shadowGroup.add(button);
        button.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    _shadowStyle = shadowStyle;
                    if (_shadowStyle instanceof PerspectiveShadow) {
                        _perspectiveShadow.setVisible(true);
                        _dropShadow.setVisible(false);
                    }
                    else if (_shadowStyle instanceof DropShadow) {
                        _perspectiveShadow.setVisible(false);
                        _dropShadow.setVisible(true);
                    }
                    else {
                        _perspectiveShadow.setVisible(false);
                        _dropShadow.setVisible(false);
                    }
                    if (_balloontip != null) {
                        hideToolTip();
                        showToolTip();
                    }
                }
            }
        });
        return button;
    }

    private JPanel createToolTipContent() {
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.add(new JLabel(JideIconsFactory.getImageIcon(JideIconsFactory.JIDELOGO_SMALL2)));
        JPanel fieldPanel = new JPanel(new BorderLayout(6, 6));
        fieldPanel.setOpaque(false);
        fieldPanel.add(new JLabel("Name:"), BorderLayout.BEFORE_LINE_BEGINS);
        fieldPanel.add(new JTextField(20));
        panel.add(fieldPanel, BorderLayout.AFTER_LAST_LINE);
        panel.setOpaque(false);
        return panel;
    }

    private JPanel createSliderPanel(final String name, final JSlider slider, final double ratio) {
        final JLabel label = new JLabel(name);
        double f = slider.getValue() / ratio;
        label.setText(name + ": (" + f + ")");
        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int v = slider.getValue();
                double f = v / ratio;
                label.setText(name + ": (" + f + ")");
                hideToolTip();
                showToolTip();
            }
        });

        JPanel panel = new JPanel(new BorderLayout(3, 3));
        panel.add(slider);
        panel.add(label, BorderLayout.BEFORE_FIRST_LINE);
        return panel;
    }
}
