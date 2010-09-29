/*
 * @(#)CalculatorGadget.java 8/28/2007
 *
 * Copyright 2002 - 2007 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.combobox.DateChooserPanel;
import com.jidesoft.grid.QuickTableFilterField;
import com.jidesoft.icons.IconsFactory;
import com.jidesoft.rss.FeedItemTableModel;
import com.jidesoft.rss.FeedTable;
import com.jidesoft.swing.Calculator;
import com.jidesoft.swing.JideSwingUtilities;
import com.jidesoft.swing.SelectAllUtils;
import de.nava.informa.core.ParseException;
import de.nava.informa.impl.basic.ChannelBuilder;
import de.nava.informa.parsers.FeedParser;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

public class GadgetFactory {
    public static JComponent createCalculator() {
        JPanel panel = new JPanel(new BorderLayout(6, 6));
        final JTextField textField = new JTextField();
        SelectAllUtils.install(textField);
        textField.setColumns(20);
        textField.setHorizontalAlignment(JTextField.TRAILING);
        panel.add(JideSwingUtilities.createTopPanel(textField), BorderLayout.CENTER);
        Calculator calculator = new Calculator();
        calculator.registerKeyboardActions(textField, JComponent.WHEN_FOCUSED);
        calculator.addPropertyChangeListener(Calculator.PROPERTY_DISPLAY_TEXT, new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                textField.setText("" + evt.getNewValue());
            }
        });
        calculator.clear();
        panel.add(calculator, BorderLayout.AFTER_LINE_ENDS);
        JideSwingUtilities.setOpaqueRecursively(panel, false);
        return panel;

    }

    public static JComponent createCalendar() {
        JPanel panel = new JPanel(new BorderLayout(6, 6));
        DateChooserPanel dateChooserPanel = new DateChooserPanel();
        panel.add(dateChooserPanel);
        JideSwingUtilities.setOpaqueRecursively(panel, false);
        return panel;
    }

    public static JComponent createNotes() {
        JPanel panel = new JPanel(new BorderLayout(6, 6));
        JTextArea textArea = new JTextArea();
        textArea.setRows(6);
        panel.add(new JScrollPane(textArea));
        JideSwingUtilities.setOpaqueRecursively(panel, false);
        return panel;
    }

    public static JComponent createFind() {
        JPanel panel = new JPanel(new BorderLayout(6, 6));
        QuickTableFilterField field = new QuickTableFilterField();
        field.putClientProperty(JideSwingUtilities.SET_OPAQUE_RECURSIVELY_EXCLUDED, Boolean.TRUE);
        panel.add(field);
        JideSwingUtilities.setOpaqueRecursively(panel, false);
        return panel;
    }

    public static JComponent createNews() {
        FeedItemTableModel model = new FeedItemTableModel(10);
        try {
            model.openChannel(FeedParser.parse(new ChannelBuilder(), "http://www.curious-creature.org/feed/"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        JTable table = new FeedTable(model) {
            @Override
            public Dimension getPreferredScrollableViewportSize() {
                return new Dimension(200, 200);
            }
        };
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(table));
        return panel;
    }

    public static JComponent createChart() {
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                ImageIcon image = IconsFactory.getImageIcon(GadgetFactory.class, "icons/dasbhoard_chart.png");
                g2d.drawImage(image.getImage(), 0, 0, getWidth(), getHeight(), this);
                g2d.dispose();
            }
        };
        panel.setPreferredSize(new Dimension(342, 401));
        panel.setOpaque(false);
        return panel;
    }

    public static JComponent createClock() {
        JPanel panel = new JPanel(new GridBagLayout());
        ImageIcon image = IconsFactory.getImageIcon(GadgetFactory.class, "icons/dasbhoard_clock.png");
        panel.add(new JLabel(image));
        panel.setOpaque(false);
        return panel;
    }
}
