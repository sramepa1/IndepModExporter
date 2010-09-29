/*
 * @(#)CachedTableModelDemo.java 7/14/2006
 *
 * Copyright 2002 - 2006 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.grid.CachedTableModel;
import com.jidesoft.grid.SortableTable;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Demoed Component: {@link com.jidesoft.grid.SortableTable} <br> Required jar files:
 * jide-common.jar, jide-grids.jar <br> Required L&F: any L&F
 */
public class CachedTableModelDemo extends AbstractDemo {
    private long _delay;
    private TableModel _tableModel;
    private CachedTableModel _cachedTableModel;
    private JLabel _status2;
    private JLabel _status1;

    public CachedTableModelDemo() {
    }

    public String getName() {
        return "CachedTableModel Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_GRIDS;
    }

    @Override
    public String getDescription() {
        return "If you were using DefaultTableModel, the value of each cell is already in the table model so it is fast to retrieve. " +
                "However, if you are using AbstractTableModel and you calculate the value on fly in getValueAt(...) method, you will face performance problem even each getValueAt only takes merely 10 ms. " +
                "\n\n" +
                "To solve this performace, we introduced CachedTableModel. CachedTableModel will call getValueAt on the actual table model and save it to the cache. " +
                "If getValueAt is called again, it will get the value from the cache first. If not found, then ask the actual table model for the value. " +
                "CachedTableModel is also transparent when actual table model value changes. " +
                "\n\n" +
                "The demo uses a 1000 x 100 AbstractTableModel. It artificially adds a loop in getValueAt(...) to cause a delay. You can use slider to adjust the delay. " +
                "As the delay goes up, you will see the performace of CachedTableModel gets more and more faster than the one without caching." +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.grid.CachedTableModel";
    }

    @Override
    public Component getOptionsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new JideBoxLayout(panel, JideBoxLayout.Y_AXIS, 6));
        final JButton benchmark = new JButton();
        benchmark.setAction(new AbstractAction("Benchmark") {
            public void actionPerformed(ActionEvent e) {
                Thread runnable = new Thread() {
                    @Override
                    public void run() {
                        Thread runnable = new Thread() {
                            @Override
                            public void run() {
                                Runnable runnable = new Runnable() {
                                    public void run() {
                                        benchmark.setEnabled(false);
                                        _status2.setText(" ");
                                        _status1.setText("Benchmarking ...");
                                    }
                                };
                                SwingUtilities.invokeLater(runnable);
                                final long delay1 = timingIt(_tableModel);
                                runnable = new Runnable() {
                                    public void run() {
                                        _status1.setText("Without Caching: " + delay1 + " ms");
                                    }
                                };
                                SwingUtilities.invokeLater(runnable);

                                runnable = new Runnable() {
                                    public void run() {
                                        _status2.setText("Caching ...");
                                    }
                                };
                                SwingUtilities.invokeLater(runnable);

                                _cachedTableModel.cacheIt();

                                runnable = new Runnable() {
                                    public void run() {
                                        _status2.setText("Benchmarking ...");
                                    }
                                };

                                final long delay2 = timingIt(_cachedTableModel);
                                runnable = new Runnable() {
                                    public void run() {
                                        _status2.setText("With Caching: " + delay2 + " ms");
                                        benchmark.setEnabled(true);
                                    }
                                };
                                SwingUtilities.invokeLater(runnable);
                            }
                        };
                        runnable.start();
                    }
                };
                runnable.start();
            }
        });

        final MultilineLabel delay = new MultilineLabel();
        panel.add(delay);
        final JSlider slider = new JSlider(0, 100000);
        slider.setPreferredSize(new Dimension(150, 20));
        slider.setMinorTickSpacing(1);
        slider.setValue((int) _delay);
        panel.add(JideSwingUtilities.createCenterPanel(slider));
        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                _delay = slider.getValue();
                long start = System.nanoTime();
                for (int i = 0; i < _delay; i++) ;
                long end = System.nanoTime();
                delay.setText("Assuming there is " + (end - start) + " nanoseconds delay in each getValueAt");
            }
        });
        long start = System.nanoTime();
        for (int i = 0; i < _delay; i++) ;
        long end = System.nanoTime();
        delay.setText("Assuming there is " + (end - start) + " nanoseconds delay in each getValueAt");

        _status1 = new JLabel(" ");
        _status2 = new JLabel(" ");
        _status1.setForeground(Color.BLUE);
        _status2.setForeground(Color.BLUE);

        panel.add(Box.createVerticalStrut(6));
        panel.add(StyledLabelBuilder.createStyledLabel("{Time spend to read the whole table\\::b}"));
        panel.add(_status1);
        panel.add(_status2);

        panel.add(JideSwingUtilities.createCenterPanel(benchmark));

        return panel;
    }

    public Component getDemoPanel() {
        _tableModel = createSampleTableModel();
        JPanel panel1 = createTable("Without Caching", _tableModel);
        _cachedTableModel = new CachedTableModel(_tableModel);
        JPanel panel2 = createTable("With Caching", _cachedTableModel);
        JideSplitPane pane = new JideSplitPane();
        pane.add(panel1);
        pane.add(panel2);
        return pane;
    }

    private long timingIt(TableModel model) {
        long start = System.currentTimeMillis();
        for (int row = 0; row < model.getRowCount(); row++) {
            for (int col = 0; col < model.getColumnCount(); col++) {
                model.getValueAt(row, col);
            }
        }
        long end = System.currentTimeMillis();
        return end - start;
    }

    private JPanel createTable(String title, TableModel tableModel) {
        JPanel panel = new JPanel(new BorderLayout(2, 2));
        panel.setBorder(BorderFactory.createCompoundBorder(new JideTitledBorder(new PartialEtchedBorder(PartialEtchedBorder.LOWERED, PartialSide.NORTH), title, JideTitledBorder.LEADING, JideTitledBorder.ABOVE_TOP),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)));
        SortableTable sortableTable = new SortableTable(tableModel);
        sortableTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        panel.add(new JScrollPane(sortableTable));
        return panel;
    }

    private TableModel createSampleTableModel() {
        return new AbstractTableModel() {
            public int getRowCount() {
                return 1000;
            }

            public int getColumnCount() {
                return 100;
            }

            public Object getValueAt(int rowIndex, int columnIndex) {
                for (int i = 0; i < _delay; i++) ;
                return rowIndex * columnIndex;
            }
        };
    }

    @Override
    public String getDemoFolder() {
        return "G25.CachedTableModel";
    }

    static public void main(String[] s) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new CachedTableModelDemo());
    }
}
