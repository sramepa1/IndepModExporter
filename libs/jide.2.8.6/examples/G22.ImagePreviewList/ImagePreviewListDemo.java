/*
 * @(#)ImagePreviewListDemo.java 4/4/2006
 *
 * Copyright 2002 - 2006 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.combobox.ColorComboBox;
import com.jidesoft.icons.IconsFactory;
import com.jidesoft.icons.JideIconsFactory;
import com.jidesoft.list.DefaultPreviewImageIcon;
import com.jidesoft.list.ImagePreviewList;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.JideBoxLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Demoed Component: {@link ImagePreviewList} <br> Required jar files: jide-common.jar, jide-grids.jar <br> Required
 * L&F: any L&F
 */
public class ImagePreviewListDemo extends AbstractDemo {
    protected ImagePreviewList _imagePreviewlist;

    public ImagePreviewListDemo() {
    }

    public String getName() {
        return "ImagePreviewList Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_GRIDS;
    }

    @Override
    public String getDescription() {
        return "This is a demo of ImagePreviewList. It can be used to preview a list of images in thumbnail view.\n" +
                "Demoed classes:\n" +
                "com.jidesoft.list.ImagePreviewList";
    }

    public Component getDemoPanel() {
        DefaultListModel list = new DefaultListModel();
        list.addElement(new DefaultPreviewImageIcon(JideIconsFactory.getImageIcon(JideIconsFactory.JIDE32), "Logo", "This is a 32x32 logo for JIDE product"));
        list.addElement(new DefaultPreviewImageIcon(JideIconsFactory.getImageIcon(JideIconsFactory.JIDELOGO_SMALL), "Logo", "This is a larger logo for JIDE product"));
        ImageIcon logo = JideIconsFactory.getImageIcon(JideIconsFactory.JIDELOGO);
        logo.setDescription("Logo\nThis is an even larger logo for JIDE product");
        list.addElement(logo);
        try {
            ImageIcon icon = IconsFactory.getImageIcon(ImagePreviewList.class, "/images/Freestyle.jpg");
            list.addElement(new DefaultPreviewImageIcon(icon, "Freestyle", "This is a sample description"));
//            for(int i = 0; i < 720; i += 15) {
//                list.addElement(new DefaultPreviewImageIcon(IconsFactory.createRotatedImage(new JLabel(), icon, i), "Freestyle", "This is a sample description"));
//            }
            list.addElement(new DefaultPreviewImageIcon(IconsFactory.getImageIcon(ImagePreviewList.class, "/images/Fish.jpg"), "Fish", "This is a sample description"));
            list.addElement(new DefaultPreviewImageIcon(IconsFactory.getImageIcon(ImagePreviewList.class, "/images/Overlooking Rio.jpg"), "Overlooking Rio", "This is a sample description"));
            list.addElement(new DefaultPreviewImageIcon(IconsFactory.getImageIcon(ImagePreviewList.class, "/images/Sunset.jpg"), "Sunset", "This is a sample description"));
            list.addElement(new DefaultPreviewImageIcon(IconsFactory.getImageIcon(ImagePreviewList.class, "/images/Water lilies.jpg"), "Water lilies", "This is a sample description"));
            list.addElement(new DefaultPreviewImageIcon(IconsFactory.getImageIcon(ImagePreviewList.class, "/images/Winter.jpg"), "Winter", "This is a sample description"));
            list.addElement(new DefaultPreviewImageIcon(IconsFactory.getImageIcon(ImagePreviewList.class, "/images/Big Wave.jpg"), "Big Wave", "The dimension is not real size", new Dimension(2, 2)));
            ImageIcon imageIcon = IconsFactory.getImageIcon(ImagePreviewList.class, "/images/At the Arch.jpg");
            imageIcon.setDescription("At the Arch\nThis is passed in as ImageIcon");
            list.addElement(imageIcon);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        list.addElement(new DefaultPreviewImageIcon(null, "", "This is a demo of image whose preview is not available", null));
        _imagePreviewlist = new ImagePreviewList(list) {
            @Override
            public Dimension getPreferredScrollableViewportSize() {
                return new Dimension(750, 600);
            }
        };
        _imagePreviewlist.setShowDetails(ImagePreviewList.SHOW_TITLE | ImagePreviewList.SHOW_DESCRIPTION | ImagePreviewList.SHOW_SIZE);
        _imagePreviewlist.setCellDimension(new Dimension(250, 135));
        return new JScrollPane(_imagePreviewlist);
    }

    @Override
    public Component getOptionsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new JideBoxLayout(panel, JideBoxLayout.Y_AXIS, 2));
        panel.add(new JLabel("Grid Line Color: "));
        ColorComboBox gridColor = new ColorComboBox();
        gridColor.setSelectedColor(_imagePreviewlist.getGridForeground());
        gridColor.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if (e.getItem() instanceof Color) {
                        _imagePreviewlist.setGridForeground((Color) e.getItem());
                    }
                }
            }
        });
        panel.add(gridColor);
        panel.add(Box.createVerticalStrut(4));
        panel.add(new JLabel("Grid Background Color: "));
        ColorComboBox gridBackground = new ColorComboBox();
        gridBackground.setSelectedColor(_imagePreviewlist.getGridBackground());
        gridBackground.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if (e.getItem() instanceof Color) {
                        _imagePreviewlist.setGridBackground((Color) e.getItem());
                    }
                }
            }
        });
        panel.add(gridBackground);
        panel.add(Box.createVerticalStrut(4));
        panel.add(new JLabel("Selection Background Color: "));
        ColorComboBox highlightBackground = new ColorComboBox();
        Color highlight = _imagePreviewlist.getHighlightBackground();
        highlightBackground.setSelectedColor(new Color(highlight.getRGB() & 0xFFFFFF));
        highlightBackground.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if (e.getItem() instanceof Color) {
                        Color newColor = (Color) e.getItem();
                        _imagePreviewlist.setHighlightBackground(new Color(newColor.getRGB() & 0x64FFFFFF, true));
                    }
                }
            }
        });
        panel.add(highlightBackground);
        panel.add(Box.createVerticalStrut(4));
        panel.add(Box.createGlue());
        return panel;
    }

    @Override
    public String getDemoFolder() {
        return "G22.ImagePreviewList";
    }

    static public void main(String[] s) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new ImagePreviewListDemo());
    }
}
