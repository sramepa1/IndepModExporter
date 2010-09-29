/*
 * @(#)BannerPanelDemo.java
 *
 * Copyright 2002 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.dialog.BannerPanel;
import com.jidesoft.icons.JideIconsFactory;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.JideBoxLayout;
import com.jidesoft.swing.JideSwingUtilities;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

/**
 * Demoed Component: {@link BannerPanel} <br> Required jar files: jide-common.jar, jide-dialogs.jar
 * <br> Required L&F: Jide L&F extension required
 */
public class BannerPanelDemo extends AbstractDemo {
    public BannerPanelDemo() {
    }

    public String getName() {
        return "BannerPanel Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_DIALOGS;
    }

    @Override
    public String getDescription() {
        return "BannerPanel is a panel that can be used to display a banner. It's an ideal component to be put on top of a dialog.\n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.dialog.BannerPanel";
    }

    public Component getDemoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new JideBoxLayout(panel, JideBoxLayout.Y_AXIS, 10));

        BannerPanel headerPanel0 = new BannerPanel("This is also a BannerPanel with no subtitle or icon");
        headerPanel0.setFont(new Font("Tahoma", Font.PLAIN, 11));
        headerPanel0.setBackground(new Color(0, 0, 128));
        headerPanel0.setForeground(Color.WHITE);
        headerPanel0.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.white, Color.lightGray, Color.lightGray, Color.gray));

        BannerPanel headerPanel1 = new BannerPanel("This is a BannerPanel", "BannerPanel is very useful to display a title, a description and an icon. It can be used in dialog to show some help information or display a product logo in a nice way.",
                JideIconsFactory.getImageIcon(JideIconsFactory.JIDE32));
        headerPanel1.setFont(new Font("Tahoma", Font.PLAIN, 11));
        headerPanel1.setBackground(Color.WHITE);
        headerPanel1.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

        BannerPanel headerPanel2 = new BannerPanel("This is a BannerPanel", "BannerPanel is very useful to display a title, a description and an icon. It can be used in dialog to show some help information or display a product logo in a nice way.",
                JideIconsFactory.getImageIcon(JideIconsFactory.JIDE32));
        headerPanel2.setFont(new Font("Tahoma", Font.PLAIN, 11));
        headerPanel2.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        headerPanel2.setTitleIconLocation(SwingConstants.LEADING);
// this method will use JideSwingUtilities fast gradient paint to do the painting.
        headerPanel2.setGradientPaint(Color.WHITE, new Color(0, 0, 128), false);
//  you can use Paint such as GradientPaint or TexturePaint
//        headerPanel2.setBackgroundPaint(new GradientPaint(0, 0, new Color(0, 0, 128), 500, 0, Color.WHITE));

        BannerPanel headerPanel3 = new BannerPanel("This is a BannerPanel", "The place for the title icon of BannerPanel actually can be any JComponent. Here is an example to use a JComboBox instead of an icon. The component can be placed at the left or right.",
                JideSwingUtilities.createCenterPanel(new JComboBox(new Object[]{"Any Component", "Just a Demo"})));

        headerPanel3.setFont(new Font("Tahoma", Font.PLAIN, 11));
        headerPanel3.setBackground(Color.WHITE);
        headerPanel3.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

        panel.add(headerPanel1, JideBoxLayout.FLEXIBLE);
        panel.add(Box.createVerticalStrut(12), JideBoxLayout.FIX);
        panel.add(headerPanel2, JideBoxLayout.FLEXIBLE);
        panel.add(Box.createVerticalStrut(12), JideBoxLayout.FIX);
        panel.add(headerPanel0, JideBoxLayout.FLEXIBLE);
        panel.add(Box.createVerticalStrut(12), JideBoxLayout.FIX);
        panel.add(headerPanel3, JideBoxLayout.FLEXIBLE);
        panel.add(Box.createGlue(), JideBoxLayout.VARY);

        panel.setPreferredSize(new Dimension(500, 400));

        return panel;
    }

    @Override
    public String getDemoFolder() {
        return "W5.BannerPanel";
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new BannerPanelDemo());

    }
}
