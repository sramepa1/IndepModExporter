/*
 * @(#)RandomWalkMeter.java 8/22/2009
 *
 * 2002 - 2010 JIDE Software Inc. All rights reserved.
 * Copyright 2005 - 2010 Catalysoft Ltd. All rights reserved.
 */

import com.jidesoft.chart.Meter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class RandomWalkMeter extends JPanel {
    private Meter meter = new Meter();
    private Timer timer;

    public RandomWalkMeter(String title, Color faceColor, Color needleColor) {
        setLayout(new GridBagLayout());
        //GridBagConstraints c = new GridBagConstraints(1,1,1,1,1.0,0.5,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5,5,5,5), 0, 0);
        //add(Box.createGlue(), c);
        GridBagConstraints c2 = new GridBagConstraints(1, 2, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0);
        add(meter, c2);
        //meter.setBorder(new LineBorder(Color.red));
        GridBagConstraints c3 = new GridBagConstraints(1, 1, 1, 1, 1.0, 0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0);
        JLabel label = new JLabel(title, JLabel.CENTER);
        label.setVerticalAlignment(JLabel.BOTTOM);
        add(label, c3);
        //label.setBorder(new LineBorder(Color.blue));
        meter.setFaceColor(faceColor);
        meter.setNeedleColor(needleColor);
        // Start off mid-way
        meter.setValue(50.0);

        ActionListener listener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                double r = 50 * Math.random() - 25;
                double oldValue = meter.getValue();
                double newValue = oldValue + r;
                if (newValue < 0) {
                    newValue = 0;
                }
                else if (newValue > 100) {
                    newValue = 100;
                }
                meter.setValue(newValue);
                // Use null as the old value to make sure that an event is fired
                // (If the old and new values are the same no event is fired)
                firePropertyChange("value", null, newValue);
            }
        };
        timer = new Timer(1000, listener);
        timer.setRepeats(true);
        timer.start();
    }

    public double getValue() {
        return meter.getValue();
    }
}
