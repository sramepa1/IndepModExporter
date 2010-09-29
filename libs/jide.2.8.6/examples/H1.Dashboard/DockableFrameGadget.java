/*
 * @(#)DockableFrameDE.java 7/20/2007
 *
 * Copyright 2002 - 2007 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.dashboard.Gadget;
import com.jidesoft.dashboard.GadgetComponent;
import com.jidesoft.docking.DockableFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Map;
import java.util.HashMap;

public class DockableFrameGadget extends DockableFrame implements GadgetComponent {
    private Gadget _gadget;
    private Map<String, String> _settings;

    public DockableFrameGadget(Gadget gadget) {
        super(gadget.getName(), gadget.getIcon());
        _gadget = gadget;
        setOpaque(true);
        setCloseAction(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                DockableFrameGadget gadgetComponent = DockableFrameGadget.this;
                gadgetComponent.getGadget().getGadgetManager().hideGadget(gadgetComponent);
            }
        });
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
