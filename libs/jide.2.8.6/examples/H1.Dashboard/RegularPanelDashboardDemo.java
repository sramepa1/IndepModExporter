/*
 * @(#)RegularPanelDashboardDemo.java 8/24/2007
 *
 * Copyright 2002 - 2007 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.dashboard.AbstractGadget;
import com.jidesoft.dashboard.Gadget;
import com.jidesoft.dashboard.GadgetComponent;
import com.jidesoft.icons.IconsFactory;
import com.jidesoft.plaf.LookAndFeelFactory;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.HashMap;

public class RegularPanelDashboardDemo extends CollapsiblePaneDashboardDemo {
    @Override
    public String getName() {
        return "Dashboard Demo (using JPanel)";
    }

    static class PanelGadget extends JPanel implements GadgetComponent {
        private Gadget _gadget;
        private Map<String, String> _settings;

        public PanelGadget(Gadget gadget) {
            _gadget = gadget;
            setLayout(new BorderLayout());
            setBorder(BorderFactory.createTitledBorder(_gadget.getName()));
        }

        public Gadget getGadget() {
            return _gadget;
        }

        public Map<String, String> getSettings() {
            if (_settings == null) {
                _settings = new HashMap<String, String>();
            }
            return _settings;
        }

        public void setSettings(Map<String, String> settings) {
            _settings = settings;
        }

    }

    @Override
    protected AbstractGadget createGadget(String key) {
        AbstractGadget dashboardElement = new AbstractGadget(key,
                IconsFactory.getImageIcon(RegularPanelDashboardDemo.class, "icons/" + key.toLowerCase() + "_32x32.png"),
                IconsFactory.getImageIcon(RegularPanelDashboardDemo.class, "icons/" + key.toLowerCase() + "_64x64.png")) {
            public GadgetComponent createGadgetComponent() {
                final PanelGadget gadget = new PanelGadget(this);
                if (getKey().startsWith("Calculator")) {
                    gadget.add(GadgetFactory.createCalculator());
                }
                else if (getKey().startsWith("Call")) {
                    gadget.add(GadgetFactory.createCalendar());
                }
                else if (getKey().startsWith("Notes")) {
                    gadget.add(GadgetFactory.createNotes());
                }
                else if (getKey().startsWith("Find")) {
                    gadget.add(GadgetFactory.createFind());
                }
                else if (getKey().startsWith("News")) {
                    gadget.add(GadgetFactory.createNews());
                }
                else if (getKey().startsWith("Chart")) {
                    gadget.add(GadgetFactory.createChart());
                }
                else if (getKey().startsWith("Clock")) {
                    gadget.add(GadgetFactory.createClock());
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
        showAsFrame(new RegularPanelDashboardDemo());
    }
}
