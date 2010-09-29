/*
 * @(#)${NAME}.java
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.document.DocumentComponent;
import com.jidesoft.swing.JideScrollPane;
import com.jidesoft.swing.JideTabbedPane;
import com.jidesoft.swing.ResizablePanel;

import javax.swing.*;
import java.awt.*;

/**
 */
class DemoDocumentComponent extends DocumentComponent {
    private Demo _demo;
    public JideTabbedPane _pane;
    protected Component _demoPanel;

    public DemoDocumentComponent(Demo demo) {
        super(null, demo.getName(), demo.getName(), DemoCellRenderer.getProductIcon(demo.getProduct()));
        setComponent(createTabbedPane(demo));
        _demo = demo;
    }

    private JideTabbedPane createTabbedPane(Demo demo) {
        _pane = new JideTabbedPane();
        _demoPanel = demo.getDemoPanel();
        _pane.addTab("Demo", new JideScrollPane(new DemoPanel(_demoPanel)));
        _pane.setTabShape(JideTabbedPane.SHAPE_BOX);
        _pane.setHideOneTab(true);
        _pane.setFocusable(false);
        _pane.setTabPlacement(JideTabbedPane.TOP);
        return _pane;
    }

    public Demo getDemo() {
        return _demo;
    }


    public Component getDemoPanel() {
        return _demoPanel;
    }

    public void browseSourceCode() {
        if (_pane.getTabCount() == 1) {
            _pane.addTab("Source", AbstractDemo.createSourceCodePanel(_demo.getDemoSource()));
        }
        _pane.setSelectedIndex(1);
    }

    static class DemoPanel extends JPanel {
        public DemoPanel(Component demoPanel) {
            setBackground(Color.BLACK);
            setOpaque(true);
            setLayout(new FlowLayout(SwingConstants.LEADING));
            ResizablePanel resizablePanel = new ResizablePanel();
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            panel.add(demoPanel, BorderLayout.CENTER);
            resizablePanel.add(panel);
            add(resizablePanel);
        }
    }

}
