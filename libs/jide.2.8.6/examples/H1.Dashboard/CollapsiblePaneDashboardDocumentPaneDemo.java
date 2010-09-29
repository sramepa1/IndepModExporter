/*
 * @(#)CollapsiblePaneDashboardDocumentPaneDemo.java 4/16/2009
 *
 * Copyright 2002 - 2009 JIDE Software Inc. All rights reserved.
 *
 */

import com.jidesoft.converter.ObjectConverterManager;
import com.jidesoft.dashboard.*;
import com.jidesoft.dialog.ButtonPanel;
import com.jidesoft.icons.IconsFactory;
import com.jidesoft.pane.CollapsiblePaneTitleButton;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.JideToggleButton;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;

public class CollapsiblePaneDashboardDocumentPaneDemo extends AbstractDemo {
    public static String _lastDirectory = ".";
    protected DashboardDocumentPane _documentPane;
    private static final long serialVersionUID = -1657620893604498434L;

    public CollapsiblePaneDashboardDocumentPaneDemo() {
    }

    public String getName() {
        return "Dashboard DocumentPane Demo (using CollapsiblePane)";
    }

    public String getProduct() {
        return PRODUCT_NAME_DASHBOARD;
    }

    @Override
    public String getDemoFolder() {
        return "H1.Dashboard";
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

        _documentPane = new DashboardDocumentPane(manager) {
            private int paletteHeight = 50;
            private boolean fixPalleteVisible;

            public void setFixPalleteVisible(boolean fixPalleteVisible) {
                this.fixPalleteVisible = fixPalleteVisible;
            }

            @Override
            protected Container createToolBarComponent() {
                Container toolBarComponent = super.createToolBarComponent();
                toolBarComponent.remove(1);
                final JideToggleButton button = new JideToggleButton();
                button.setAction(new AbstractAction("Show palette") {
                    private static final long serialVersionUID = -3748761026959071775L;

                    public void actionPerformed(ActionEvent e) {
                        setFixPalleteVisible(button.isSelected());
                        if (button.isSelected() != _documentPane.isPaletteVisible(_documentPane)) {
                            _documentPane.togglePalette(_documentPane);
                        }
                    }
                });
                toolBarComponent.add(button);
                return toolBarComponent;
            }

            @Override
            public Dashboard createDashboard(String key) {
                final Dashboard dashboard = super.createDashboard(key);
                dashboard.addMouseMotionListener(new MouseMotionListener() {
                    public void mouseMoved(MouseEvent e) {
                        Rectangle rect = dashboard.getVisibleRect();
                        int offSetToBottom = (rect.y + rect.height) - e.getY();
                        if (isPaletteVisible(dashboard)) {
                            if (!fixPalleteVisible && offSetToBottom > 25) {
                                hidePalette();
                            }
                        }
                        else if (offSetToBottom < paletteHeight) {
                            showPalette(dashboard);
                        }
                    }

                    public void mouseDragged(MouseEvent e) {
                    }
                });
                return dashboard;
            }

            @Override
            protected GadgetPalette createGadgetPalette() {
                GadgetPalette palette = new GadgetPalette(getGadgetManager(), this) {
                    @Override
                    protected JPanel createDescriptionPanel(Component statusComponent) {
                        JPanel panel = super.createDescriptionPanel(statusComponent);
                        panel.remove(3);
                        return panel;
                    }
                };
                paletteHeight = palette.getPreferredSize().height;
                return palette;
            }
        };
        _documentPane.setPreferredSize(new Dimension(1110, 700));
        _documentPane.getLayoutPersistence().setProfileKey("CollapsiblePaneDashboardDocumentPaneDemo");

        Dashboard dashBoard = _documentPane.createDashboard("Home Page");
        dashBoard.setColumnCount(3);
        dashBoard.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        _documentPane.getGadgetManager().addDashboard(dashBoard);

        return _documentPane;

    }

    @Override
    public Component getOptionsPanel() {
        ButtonPanel buttonPanel = new ButtonPanel(SwingConstants.TOP);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        buttonPanel.addButton(new JButton(new AbstractAction("Save") {
            private static final long serialVersionUID = 5707246699151027514L;

            public void actionPerformed(ActionEvent e) {
                try {
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
                        DashboardPersistenceUtils.save(_documentPane, chooser.getSelectedFile().getAbsolutePath());
                        _documentPane.getLayoutPersistence().saveLayoutData();
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
        buttonPanel.addButton(new JButton(new AbstractAction("Load") {
            private static final long serialVersionUID = 3301425277993627921L;

            public void actionPerformed(ActionEvent e) {
                try {
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
                        DashboardPersistenceUtils.load(_documentPane, chooser.getSelectedFile().getAbsolutePath());
                        try {
                            _documentPane.getGadgetManager().setDisposeGadgetsWhenHidingDashboard(false);
                            _documentPane.getLayoutPersistence().loadLayoutData();
                        }
                        finally {
                            _documentPane.getGadgetManager().setDisposeGadgetsWhenHidingDashboard(true);
                        }
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

        buttonPanel.addButton(new JButton(new AbstractAction("Toggle Column Count") {
            private static final long serialVersionUID = 2658124831273629921L;

            public void actionPerformed(ActionEvent e) {
                Dashboard dashboard = _documentPane.getActiveDashboard();
                if (dashboard != null) {
                    if (dashboard.getColumnCount() == 3)
                        dashboard.setColumnCount(2);
                    else if (dashboard.getColumnCount() == 2)
                        dashboard.setColumnCount(3);
                }
            }
        }));

        buttonPanel.addButton(new JButton(new AbstractAction("Activate Home Page") {
            private static final long serialVersionUID = -7745793609820084555L;

            public void actionPerformed(ActionEvent e) {
                _documentPane.getGadgetManager().setActiveDashboardKey("Home Page");
            }
        }));

        buttonPanel.addButton(new JButton(new AbstractAction("Remove \"Clock\" Gadget") {
            private static final long serialVersionUID = -7960637621677202734L;

            public void actionPerformed(ActionEvent e) {
                GadgetManager gadgetManager = _documentPane.getGadgetManager();
                if (gadgetManager.getGadget("Clock") == null) {
                    gadgetManager.addGadget(createGadget("Clock"), 2);
                    ((JButton) e.getSource()).setText("Remove \"Clock\" Gadget");
                }
                else {
                    gadgetManager.removeGadget("Clock");
                    ((JButton) e.getSource()).setText("Add \"Clock\" Gadget");
                }
            }
        }));

        return buttonPanel;
    }

    public static void main(String[] args) {
//        LoggerUtils.enableLogger(DocumentComponentEvent.class.getName(), Level.FINE);
//        LoggerUtils.enableLogger(GadgetEvent.class.getName(), Level.FINE);
//        LoggerUtils.enableLogger(DashboardEvent.class.getName(), Level.FINE);
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new CollapsiblePaneDashboardDocumentPaneDemo());
    }

    protected AbstractGadget createGadget(String key) {
        AbstractGadget dashboardElement = new AbstractGadget(key,
                IconsFactory.getImageIcon(CollapsiblePaneDashboardDocumentPaneDemo.class, "icons/" + key.toLowerCase() + "_32x32.png"),
                IconsFactory.getImageIcon(CollapsiblePaneDashboardDocumentPaneDemo.class, "icons/" + key.toLowerCase() + "_64x64.png")) {
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
                CollapsiblePaneTitleButton toolButton = new CollapsiblePaneTitleButton(gadget, IconsFactory.getImageIcon(CollapsiblePaneDashboardDocumentPaneDemo.class, "icons/gadget_tool.png"));
                toolButton.addActionListener(new AbstractAction() {
                    private static final long serialVersionUID = -5668190432153103058L;

                    public void actionPerformed(ActionEvent e) {
                        JOptionPane.showMessageDialog(gadget, "Settings dialog goes here!");
                    }
                });
                gadget.addButton(toolButton, 1);
                gadget.setMessage("Last update on " + ObjectConverterManager.toString(Calendar.getInstance()));
                return new ResizableGadgetComponent(gadget);
            }

            public void disposeGadgetComponent(GadgetComponent component) {
                // do nothing in this case as we didn't allocate any resource in createGadgetComponent.
            }
        };
        dashboardElement.setDescription("Description is " + key);
        return dashboardElement;
    }
}