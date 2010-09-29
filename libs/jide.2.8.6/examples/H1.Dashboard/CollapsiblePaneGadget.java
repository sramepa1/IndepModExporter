/*
 * @(#)Target.java 7/20/2007
 *
 * Copyright 2002 - 2007 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.dashboard.Dashboard;
import com.jidesoft.dashboard.Gadget;
import com.jidesoft.dashboard.GadgetComponent;
import com.jidesoft.dashboard.GadgetManager;
import com.jidesoft.icons.IconsFactory;
import com.jidesoft.pane.CollapsiblePane;
import com.jidesoft.pane.CollapsiblePaneTitleButton;
import com.jidesoft.pane.event.CollapsiblePaneEvent;
import com.jidesoft.pane.event.CollapsiblePaneListener;
import com.jidesoft.plaf.CollapsiblePaneUI;
import com.jidesoft.plaf.UIDefaultsLookup;
import com.jidesoft.plaf.basic.ThemePainter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

public class CollapsiblePaneGadget extends CollapsiblePane implements GadgetComponent {
    private Gadget _gadget;

    private RolloverTitlePane _titlePane;
    private Map<String, String> _settings;

    CollapsiblePaneTitleButton _maximizeButton;
    CollapsiblePaneTitleButton _restoreButton;

    public CollapsiblePaneGadget(Gadget gadget) {
        super(gadget.getName());
        _gadget = gadget;
        setTitleIcon(gadget.getIcon());
        setFocusable(true);
        setRequestFocusEnabled(true);
        setFocusPainted(false);
        setEmphasized(true);

        // we hide the default expand button and we will provide our own.
        setShowExpandButton(false);
        _titlePane = new RolloverTitlePane(this);
        final CollapsiblePaneTitleButton expandButton = new CollapsiblePaneTitleButton(this, ((ThemePainter) UIDefaultsLookup.get("Theme.painter")).getCollapsiblePaneUpMask());
        expandButton.addActionListener(new AbstractAction() {
            private static final long serialVersionUID = -1524082565433849904L;

            public void actionPerformed(ActionEvent e) {
                ((CollapsiblePaneUI) getUI()).getToggleAction().actionPerformed(null);
            }
        });
        addCollapsiblePaneListener(new CollapsiblePaneListener() {
            public void paneExpanding(CollapsiblePaneEvent event) {
            }

            public void paneExpanded(CollapsiblePaneEvent event) {
                updateExpandButton(expandButton);
            }

            public void paneCollapsing(CollapsiblePaneEvent event) {
            }

            public void paneCollapsed(CollapsiblePaneEvent event) {
                updateExpandButton(expandButton);
            }
        });
        _titlePane.addButton(expandButton);
        _maximizeButton = new CollapsiblePaneTitleButton(this, IconsFactory.getImageIcon(CollapsiblePaneDashboardDemo.class, "icons/gadget_maximize.png"));
        _titlePane.addButton(_maximizeButton);
        _restoreButton = new CollapsiblePaneTitleButton(this, IconsFactory.getImageIcon(CollapsiblePaneDashboardDemo.class, "icons/gadget_restore.png"));
        _restoreButton.setVisible(false);
        _titlePane.addButton(_restoreButton);
        CollapsiblePaneTitleButton closeButton = new CollapsiblePaneTitleButton(this, IconsFactory.getImageIcon(CollapsiblePaneDashboardDemo.class, "icons/gadget_close.png"));
        closeButton.addActionListener(new AbstractAction() {
            private static final long serialVersionUID = 7214349428761246735L;

            public void actionPerformed(ActionEvent e) {
                CollapsiblePaneGadget gadgetComponent = CollapsiblePaneGadget.this;
                hideGadget(gadgetComponent);
            }
        });
        _titlePane.addButton(closeButton);
        setTitleComponent(_titlePane);
    }

    private void updateExpandButton(CollapsiblePaneTitleButton expandButton) {
        if (isCollapsed()) {
            expandButton.setIcon(((ThemePainter) UIDefaultsLookup.get("Theme.painter")).getCollapsiblePaneDownMask());
        }
        else {
            expandButton.setIcon(((ThemePainter) UIDefaultsLookup.get("Theme.painter")).getCollapsiblePaneUpMask());
        }
    }

    public Gadget getGadget() {
        return _gadget;
    }

    public void setMessage(String message) {
        _titlePane.setMessage(message);
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

    public void addButton(AbstractButton button) {
        _titlePane.addButton(button);
    }

    public void addButton(AbstractButton button, int index) {
        _titlePane.addButton(button, index);
    }

    public void removeButton(AbstractButton button) {
        _titlePane.removeButton(button);
    }

    public void removeButton(int index) {
        _titlePane.removeButton(index);
    }

    public static void hideGadget(GadgetComponent gadgetComponent) {
        GadgetComponent parentGadgetComponent = (GadgetComponent) SwingUtilities.getAncestorOfClass(
                GadgetComponent.class, (Component) gadgetComponent);
        while (parentGadgetComponent != null) {
            gadgetComponent = parentGadgetComponent;
            parentGadgetComponent = (GadgetComponent) SwingUtilities.getAncestorOfClass(
                    GadgetComponent.class, (Component) gadgetComponent);
        }

        GadgetManager gadgetManager = gadgetComponent.getGadget().getGadgetManager();
        if (gadgetManager != null) {
            gadgetManager.hideGadget(gadgetComponent);
        }
        else {
            Container container = SwingUtilities.getAncestorOfClass(Dashboard.class, (Component) gadgetComponent);
            if (container == null) {
                return;
            }
            Dashboard dashboard = (Dashboard) container;
            dashboard.restoreGadget();
            Container parent = ((Component) gadgetComponent).getParent();
            if (parent != null) {
                parent.remove((Component) gadgetComponent);
                ((Component) gadgetComponent).setVisible(false);
                parent.doLayout();
            }
            Gadget gadget = gadgetComponent.getGadget();
            if (gadget != null) {
                gadget.disposeGadgetComponent(gadgetComponent);
            }
        }
    }

    void setMaximized(boolean maximized) {
        _maximizeButton.setVisible(!maximized);
        _restoreButton.setVisible(maximized);
    }
}
