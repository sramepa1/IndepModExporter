/*
 * @(#)InitialLayoutDockDemo.java 6/5/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.docking.DockableFrame;
import com.jidesoft.docking.DockableHolderPanel;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.JideSwingUtilities;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

/**
 * <br> Required jar files: jide-common.jar, jide-dock.jar <br> Required L&F: any L&F
 */
public class InitialLayoutDockDemo extends AbstractDemo {
    private DockableHolderPanel _panel;

    public InitialLayoutDockDemo() {
    }

    public String getName() {
        return "Docking Framework (Initial Layout) Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_DOCK;
    }

    @Override
    public String getDescription() {
        return "A quicker way to design the layout of Docking Framework is to use Visual Designer. After you finish the design in Visual Designer, you can save it as .ilayout (inital layout) file and ship them along with your application.\n" +
                "\nThis is an example to load the .ilayout files. There are three .ilayout files created by Visual Designer in this demo. They represent three different modes (Edit source code, Design GUI form and Debug) in a typical IDE.\n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.docking.DefaultDockingManager";
    }

    @Override
    public Component getOptionsPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 3, 3));
        panel.add(new JButton(new AbstractAction("Load Edit Layout") {
            public void actionPerformed(ActionEvent e) {
                loadInitialLayout("edit.ilayout");
            }
        }));
        panel.add(new JButton(new AbstractAction("Load Design Layout") {
            public void actionPerformed(ActionEvent e) {
                loadInitialLayout("design.ilayout");
            }
        }));
        panel.add(new JButton(new AbstractAction("Load Debug Layout") {
            public void actionPerformed(ActionEvent e) {
                loadInitialLayout("debug.ilayout");
            }
        }));
        return panel;
    }

    public Component getDemoPanel() {
        _panel = new DockableHolderPanel();
        _panel.getDockingManager().setDockableFrameFactory(new com.jidesoft.docking.DockableFrameFactory() {
            public DockableFrame create(String key) {
                DockableFrame frame = new DockableFrame(key);
                frame.getContentPane().add(new JScrollPane(new JTextArea()));
                return frame;
            }
        });

        _panel.setPreferredSize(new Dimension(800, 500));
        return _panel;
    }

    private void loadInitialLayout(String resourceName) {
        _panel.getDockingManager().switchRootPaneContainer((RootPaneContainer) _panel.getTopLevelAncestor());
        try {
            _panel.getDockingManager().loadInitialLayout(getClass().getClassLoader().getResourceAsStream(resourceName));
        }
        catch (ParserConfigurationException e) {
            JideSwingUtilities.printException(e);
        }
        catch (SAXException e) {
            JideSwingUtilities.printException(e);
        }
        catch (IOException e) {
            JideSwingUtilities.printException(e);
        }

        _panel.getDockingManager().resetToDefault();
    }

    @Override
    public String getDemoFolder() {
        return "D3.InitialLayoutDockingFramework";
    }

    static public void main(String[] s) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new InitialLayoutDockDemo());
    }
}