/*
 * @(#)DashboardDockableFrameDemo.java 8/21/2007
 *
 * Copyright 2002 - 2007 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.dashboard.AbstractGadget;
import com.jidesoft.dashboard.GadgetComponent;
import com.jidesoft.icons.IconsFactory;
import com.jidesoft.plaf.LookAndFeelFactory;

import java.awt.*;

public class DockableFrameDashboardDemo extends CollapsiblePaneDashboardDemo {
    @Override
    public String getName() {
        return "Dashboard Demo (using DockableFrame)";
    }

    @Override
    protected AbstractGadget createGadget(String key) {
        AbstractGadget dashboardElement = new AbstractGadget(key,
                IconsFactory.getImageIcon(DockableFrameDashboardDemo.class, "icons/" + key.toLowerCase() + "_32x32.png"),
                IconsFactory.getImageIcon(DockableFrameDashboardDemo.class, "icons/" + key.toLowerCase() + "_64x64.png")) {
            public GadgetComponent createGadgetComponent() {
                final DockableFrameGadget gadget = new DockableFrameGadget(this);
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
                    gadget.setPreferredSize(new Dimension(200, 100 + (int) (Math.random() * 100)));
                }
                return gadget;
            }

            public void disposeGadgetComponent(GadgetComponent component) {
                // do nothing in this case as we didn't allocate any resource in createGadgetComponent.
            }
        };
        dashboardElement.setDescription("Description is " + key);
        return dashboardElement;
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new DockableFrameDashboardDemo());
    }
}
