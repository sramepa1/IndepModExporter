/*
 * @(#)CollapsiblePaneSingleDashboardDemo.java 10/25/2007
 *
 * Copyright 2002 - 2007 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.converter.ObjectConverterManager;
import com.jidesoft.dashboard.*;
import com.jidesoft.dialog.ButtonPanel;
import com.jidesoft.icons.IconsFactory;
import com.jidesoft.pane.CollapsiblePaneTitleButton;
import com.jidesoft.pane.CollapsiblePane;
import com.jidesoft.plaf.LookAndFeelFactory;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;

public class CollapsiblePaneSingleDashboardDemo extends AbstractDemo {
    public static String _lastDirectory = ".";
    protected SingleDashboardHolder _dashboardHolder;
    private static final long serialVersionUID = 502408740940055925L;

    public CollapsiblePaneSingleDashboardDemo() {
    }

    public String getName() {
        return "Dashboard Demo (using CollapsiblePane)";
    }

    public String getProduct() {
        return PRODUCT_NAME_DASHBOARD;
    }

    @Override
    public String getDescription() {
        return "This is a demo of Dashboard using CollapsiblePane as the gadget component. \n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.dasbhoard.GadgetManager\n" +
                "com.jidesoft.dasbhoard.Gadget\n" +
                "com.jidesoft.dasbhoard.GadgetComponent\n" +
                "com.jidesoft.dasbhoard.Dashboard\n" +
                "com.jidesoft.dasbhoard.DashboardTabbedPand";
    }

    public Component getDemoPanel() {
        GadgetManager manager = new GadgetManager();
        manager.addGadget(createGadget("Calculator"));
        manager.addGadget(createGadget("Call"));
        manager.addGadget(createGadget("Clock"));
        manager.addGadget(createGadget("Find"));
        manager.addGadget(createGadget("Image"));
        manager.addGadget(createGadget("Network"));
        manager.addGadget(createGadget("News"));
        manager.addGadget(createGadget("Notes"));
        manager.addGadget(createGadget("Chart"));

        _dashboardHolder = new SingleDashboardHolder(manager) {
            private final CollapsiblePaneGroup group = new CollapsiblePaneGroup();
            @Override
            protected JPanel createSubpanel() {
                JPanel panel = super.createSubpanel();

                    panel.addContainerListener(new ContainerListener() {
                        public void componentRemoved(ContainerEvent e) {
                            Component child = e.getChild();
                            if(child instanceof CollapsiblePane) {
                                group.remove((CollapsiblePane) child);
                            }
                        }

                        public void componentAdded(ContainerEvent e) {
                            Component child = e.getChild();
                            if(child instanceof CollapsiblePane) {
                                group.add((CollapsiblePane) child);
                            }
                        }
                    });
                return panel;
            }
        };
        _dashboardHolder.setPreferredSize(new Dimension(1110, 700));
        return new JScrollPane(_dashboardHolder);

    }

    @Override
    public Component getOptionsPanel() {
        ButtonPanel buttonPanel = new ButtonPanel(SwingConstants.TOP);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonPanel.addButton(new JButton(new AbstractAction("Load") {
            private static final long serialVersionUID = -6994663016037232412L;

            public void actionPerformed(ActionEvent e) {
                try {
//                    DashboardPersistenceUtils.load(tabbedPane, "c:\\dashboard.xml");
                    JFileChooser chooser = new JFileChooser() {
                        @Override
                        protected JDialog createDialog(Component parent) throws HeadlessException {
                            JDialog dialog = super.createDialog(parent);
                            dialog.setTitle("Load an \".xml\" file");
                            return dialog;
                        }
                    };
                    chooser.setCurrentDirectory(new File(_lastDirectory));
                    int result = chooser.showDialog(((JButton) e.getSource()).getTopLevelAncestor(), "Open");
                    if (result == JFileChooser.APPROVE_OPTION) {
                        _lastDirectory = chooser.getCurrentDirectory().getAbsolutePath();
                        DashboardPersistenceUtils.load(_dashboardHolder, chooser.getSelectedFile().getAbsolutePath());
                    }
                }
                catch (SAXException e1) {
                    e1.printStackTrace();
                }
                catch (ParserConfigurationException e1) {
                    e1.printStackTrace();
                }
                catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }));
        buttonPanel.addButton(new JButton(new AbstractAction("Save") {
            private static final long serialVersionUID = 5229961960661898375L;

            public void actionPerformed(ActionEvent e) {
                try {
//                    DashboardPersistenceUtils.save(tabbedPane, "C:\\dashboard.xml");
                    JFileChooser chooser = new JFileChooser() {
                        @Override
                        protected JDialog createDialog(Component parent) throws HeadlessException {
                            JDialog dialog = super.createDialog(parent);
                            dialog.setTitle("Save the layout as an \".xml\" file");
                            return dialog;
                        }
                    };
                    chooser.setCurrentDirectory(new File(_lastDirectory));
                    int result = chooser.showDialog(((JButton) e.getSource()).getTopLevelAncestor(), "Save");
                    if (result == JFileChooser.APPROVE_OPTION) {
                        _lastDirectory = chooser.getCurrentDirectory().getAbsolutePath();
                        DashboardPersistenceUtils.save(_dashboardHolder, chooser.getSelectedFile().getAbsolutePath());
                    }
                }
                catch (ParserConfigurationException e1) {
                    e1.printStackTrace();
                }
                catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }));
        buttonPanel.addButton(new JButton(new AbstractAction("Show/Hide Palette") {
            private static final long serialVersionUID = 3434956019223922179L;

            public void actionPerformed(ActionEvent e) {
                if (!_dashboardHolder.isPaletteVisible()) {
                    _dashboardHolder.showPalette();
                }
                else {
                    _dashboardHolder.hidePalette();
                }
            }
        }));

        return buttonPanel;
    }

    public static void main(String[] args) {
//        LoggerUtils.enableLogger(GadgetEvent.class.getName(), Level.FINE);
//        LoggerUtils.enableLogger(DashboardEvent.class.getName(), Level.FINE);
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new CollapsiblePaneSingleDashboardDemo());
    }

    protected AbstractGadget createGadget(String key) {
        AbstractGadget dashboardElement = new AbstractGadget(key,
                IconsFactory.getImageIcon(CollapsiblePaneSingleDashboardDemo.class, "icons/" + key.toLowerCase() + "_32x32.png"),
                IconsFactory.getImageIcon(CollapsiblePaneSingleDashboardDemo.class, "icons/" + key.toLowerCase() + "_64x64.png")) {
            public GadgetComponent createGadgetComponent() {
                final CollapsiblePaneGadget gadget = new CollapsiblePaneGadget(this);
                gadget.getContentPane().setLayout(new BorderLayout());
                gadget.getContentPane().setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
                if (getKey().startsWith("Calculator")) {
                    gadget.getContentPane().add(GadgetFactory.createCalculator());
                }
                else if (getKey().startsWith("Call")) {
                    gadget.getContentPane().add(GadgetFactory.createCalendar());
                }
                else if (getKey().startsWith("Notes")) {
                    gadget.getContentPane().add(GadgetFactory.createNotes());
                }
                else if (getKey().startsWith("Find")) {
                    gadget.getContentPane().add(GadgetFactory.createFind());
                }
                else if (getKey().startsWith("News")) {
                    gadget.getContentPane().add(GadgetFactory.createNews());
                }
                else if (getKey().startsWith("Chart")) {
                    gadget.getContentPane().add(GadgetFactory.createChart());
                }
                else if (getKey().startsWith("Clock")) {
                    gadget.getContentPane().add(GadgetFactory.createClock());
                }
                else {
                    gadget.getContentPane().setPreferredSize(new Dimension(200, 100 + (int) (Math.random() * 100)));
                }
                CollapsiblePaneTitleButton toolButton = new CollapsiblePaneTitleButton(gadget, IconsFactory.getImageIcon(CollapsiblePaneSingleDashboardDemo.class, "icons/gadget_tool.png"));
                toolButton.addActionListener(new AbstractAction() {
                    private static final long serialVersionUID = -332487105417025587L;

                    public void actionPerformed(ActionEvent e) {
                        JOptionPane.showMessageDialog(gadget, "Settings dialog goes here!");
                    }
                });
                gadget.addButton(toolButton, 1);
                gadget.setMessage("Last update on " + ObjectConverterManager.toString(Calendar.getInstance()));
                return gadget;
            }

            public void disposeGadgetComponent(GadgetComponent component) {
                // do nothing in this case as we didn't allocate any resource in createGadgetComponent.
            }
        };
        dashboardElement.setDescription("Description is " + key);
        return dashboardElement;
    }
}
