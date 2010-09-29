/*
 * @(#)IndeterminateProgressBarDemo.java 6/5/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.JideBoxLayout;
import com.jidesoft.utils.SystemInfo;
import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;

import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.awt.*;

/**
 * <br> Required jar files: jide-common.jar <br> Required L&F: any L&F
 */
public class IndeterminateProgressBarDemo extends AbstractDemo {
    protected JProgressBar _progressBar1;

    public IndeterminateProgressBarDemo() {
    }

    public String getName() {
        return "Indeterminate ProgressBar Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_COMMON;
    }

    @Override
    public String getDescription() {
        return "The Swing's default indetermined progress bar looks ugly (if you ever tried to use it). This is a better version of it.\n" +
                "Please note, in JDK1.6, Sun introduced a much better indetermined progress bar which is exactly what it should be. So we will soon deprecate this implementation when JDK1.6 is out.\n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.plaf.vsnet.VsnetProgressBarUI";
    }

    public Component getDemoPanel() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(400, 200));
        panel.setLayout(new JideBoxLayout(panel, JideBoxLayout.Y_AXIS));
        _progressBar1 = new JProgressBar();
        _progressBar1.setString("Reading data ... (Indeterminate)");
        _progressBar1.setStringPainted(true);
        _progressBar1.setIndeterminate(true);
        panel.add(_progressBar1);

// for testing purpose        
//        panel.add(Box.createVerticalStrut(10));
//
//        JProgressBar progressBar2 = new JProgressBar();
//        progressBar2.setString("Reading data ... (Determinate)");
//        progressBar2.setStringPainted(true);
//        progressBar2.setIndeterminate(false);
//        progressBar2.setValue(50);
//        panel.add(progressBar2);

        panel.add(Box.createGlue(), JideBoxLayout.VARY);
        return panel;
    }

    @Override
    public String getDemoFolder() {
        return "B12.IndeterminateProgressBar";
    }

    static public void main(String[] s) {
        LookAndFeelFactory.addUIDefaultsCustomizer(new LookAndFeelFactory.UIDefaultsCustomizer() {
            public void customize(UIDefaults defaults) {
                if (!SystemInfo.isJdk6Above()) {
                    if (UIManager.getLookAndFeel() instanceof MetalLookAndFeel) {
                        defaults.put("ProgressBarUI", "com.jidesoft.plaf.vsnet.VsnetMetalProgressBarUI");
                    }
                    else if (UIManager.getLookAndFeel() instanceof WindowsLookAndFeel) {
                        defaults.put("ProgressBarUI", "com.jidesoft.plaf.vsnet.VsnetWindowsProgressBarUI");
                    }
                }
            }
        });
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new IndeterminateProgressBarDemo());
    }
}