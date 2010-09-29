import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.JideSwingUtilities;
import com.jidesoft.swing.MeterProgressBar;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;


public class MeterProgressBarDemo extends AbstractDemo {

    private Timer _timer;
    private MeterProgressBar _progressBar;
    private JSpinner _delay, _increasement;
    private JCheckBox _start;
    private JComboBox _horizontal, _style;

    public String getName() {
        return "MeterProgressBar";
    }

    public String getProduct() {
        return PRODUCT_NAME_COMPONENTS;
    }

    public Component getDemoPanel() {
        _progressBar = new MeterProgressBar();
        _progressBar.setIndeterminate(false);
        _progressBar.setValue(50);
        return JideSwingUtilities.createCenterPanel(_progressBar);
    }

    @Override
    public Component getOptionsPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 1));

        _style = new JComboBox(new String[]{"Plain Style", "Gradient Style"});
        _style.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _progressBar.setStyle(_style.getSelectedIndex());
            }
        });
        _style.setSelectedIndex(1);
        panel.add(_style);

        _horizontal = new JComboBox(new String[]{"Horizontal", "Vertical"});
        _horizontal.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _progressBar.setOrientation(_horizontal.getSelectedIndex());
            }
        });
        panel.add(_horizontal);

        _start = new JCheckBox("Start");
        _start.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (_start.isSelected()) {
                    startTimer();
                }
                else {
                    _timer.stop();
                    _timer = null;
                }
            }
        });
        panel.add(_start);

        _delay = new JSpinner(new SpinnerNumberModel(5, 1, 100, 5));
        _delay.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                if (_timer != null) {
                    _timer.setDelay((Integer) _delay.getValue() * 10);
                }
            }
        });
        JPanel spinnerPanel = new JPanel();
        spinnerPanel.add(new JLabel("Delay "));
        spinnerPanel.add(_delay);
        panel.add(spinnerPanel);

        _increasement = new JSpinner(new SpinnerNumberModel(5.0, 1.0, 20.0, .1)) {
            public void setValue(Object value) {
                System.out.println(super.getValue() + "=>" + value);
                super.setValue(value);
            }
        };
        _increasement.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                System.out.println("changed");
            }
        });
        JPanel increasementPanel = new JPanel();
        increasementPanel.add(new JLabel("Increasement "));
        increasementPanel.add(_increasement);
        panel.add(increasementPanel);

        panel.setBorder(BorderFactory.createTitledBorder("Layout Orientation"));

        return panel;
    }

    private void startTimer() {
        _timer = new Timer(0, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int value = _progressBar.getValue() + (Integer) _increasement.getValue();
                _progressBar.setValue(value % 100);
            }
        });
        _timer.setDelay((Integer) _delay.getValue() * 10);
        _timer.start();
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new MeterProgressBarDemo());
    }

}
