/*
 * @(#)WithImagesChartDemo.java 8/22/2009
 *
 * 2002 - 2010 JIDE Software Inc. All rights reserved.
 * Copyright 2005 - 2010 Catalysoft Ltd. All rights reserved.
 */

import com.jidesoft.chart.Chart;
import com.jidesoft.chart.PointShape;
import com.jidesoft.chart.annotation.AutoPositionedLabel;
import com.jidesoft.chart.annotation.ChartImage;
import com.jidesoft.chart.annotation.ChartLabel;
import com.jidesoft.chart.axis.Axis;
import com.jidesoft.chart.model.ChartCategory;
import com.jidesoft.chart.model.ChartModel;
import com.jidesoft.chart.model.DefaultChartModel;
import com.jidesoft.chart.model.RealPosition;
import com.jidesoft.chart.style.ChartStyle;
import com.jidesoft.chart.style.LabelStyle;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.range.CategoryRange;
import com.jidesoft.range.NumericRange;
import com.jidesoft.range.Range;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

@SuppressWarnings("serial")
public class WithImagesChartDemo extends AbstractDemo {
    private JPanel demoPanel;
    private Chart chart;

    private enum Building {
        TAIPEI(1670, "TaiPei.png"),
        PETRONAS_TOWERS(1483, "Petronas.png"),
        SEARS_TOWER(1450, "Sears.png"),
        JIN_MAO_BUILDING(1380, "JinMao.png"),
        EMPIRE_STATE_BUILDING(1250, "EmpireState.png");

        private int height; // in feet
        private Image image;

        private Building(int height, String imageName) {
            this.height = height;
            image = createImage(imageName);
        }

        public int getHeight() {
            return height;
        }

        public Image getImage() {
            return image;
        }

        private Image createImage(String path) {
            ClassLoader loader = getClass().getClassLoader();
            if (loader != null) {
                URL url = loader.getResource(path);
                if (url == null) {
                    url = loader.getResource("/" + path);
                }
                Image image = Toolkit.getDefaultToolkit().createImage(url);
                return image;
            }
            return null;
        }
    }

    private final ChartCategory<Building> taipai = new ChartCategory<Building>("Taipei", Building.TAIPEI);
    private final ChartCategory<Building> petronas = new ChartCategory<Building>("Petronas Towers", Building.PETRONAS_TOWERS);
    private final ChartCategory<Building> sears = new ChartCategory<Building>("Sears Tower", Building.SEARS_TOWER);
    private final ChartCategory<Building> jin_mao = new ChartCategory<Building>("Jin Mao Building", Building.JIN_MAO_BUILDING);
    private final ChartCategory<Building> empireState = new ChartCategory<Building>("Empire State Building", Building.EMPIRE_STATE_BUILDING);
    private Range<Building> xRange = new CategoryRange<Building>().add(taipai).add(petronas).add(sears).add(jin_mao).add(empireState);
    private final NumericRange yRange = new NumericRange(0, 2000);

    private JPanel createDemo() {
        chart = new Chart();
        demoPanel = new JPanel();
        demoPanel.setPreferredSize(new Dimension(500, 500));

        final Axis xAxis = new Axis(xRange);
        final Axis yAxis = new Axis(yRange);
        yAxis.setLabel(new AutoPositionedLabel("Height (ft)", Color.white));
        chart.setChartBackground(new GradientPaint(0f, 0f, Color.blue, 0f, 800f, Color.green));
        chart.setPanelBackground(Color.black);
        chart.setLabelColor(Color.white);
        chart.setTickColor(Color.white);
        chart.setLabelColor(Color.white);
        ChartModel sineModel = createModel();

        chart.addModel(sineModel, new ChartStyle(Color.green, PointShape.CIRCLE, Color.magenta));
        chart.setXAxis(xAxis);
        xAxis.setTicksVisible(false);
        chart.setYAxis(yAxis);
        chart.setTitle(new AutoPositionedLabel("World's Tallest Buildings", Color.yellow));
        chart.setHorizontalGridLinesVisible(false);
        chart.setVerticalGridLinesVisible(false);
        demoPanel.setLayout(new BorderLayout());

        demoPanel.add(chart);
        return demoPanel;
    }


    private ChartModel createModel() {
        DefaultChartModel model = new DefaultChartModel();
        // The heights given here are where we want to see the labels
        model.addAnnotation(createLabel(taipai, Building.TAIPEI.getHeight()));
        model.addAnnotation(createLabel(petronas, Building.PETRONAS_TOWERS.getHeight()));
        model.addAnnotation(createLabel(sears, Building.SEARS_TOWER.getHeight()));
        model.addAnnotation(createLabel(jin_mao, Building.JIN_MAO_BUILDING.getHeight()));
        model.addAnnotation(createLabel(empireState, Building.EMPIRE_STATE_BUILDING.getHeight()));
        // Note that the positions of the images are given in user coordinates, not pixels
        model.addAnnotation(new ChartImage(0.6, 0, 1.3, Building.TAIPEI.getHeight(), Building.TAIPEI.getImage()));
        model.addAnnotation(new ChartImage(1.3, 0, 2.7, Building.PETRONAS_TOWERS.getHeight(), Building.PETRONAS_TOWERS.getImage()));
        model.addAnnotation(new ChartImage(2.7, 0, 3.5, Building.SEARS_TOWER.getHeight(), Building.SEARS_TOWER.getImage()));
        model.addAnnotation(new ChartImage(3.5, 0, 4.5, Building.JIN_MAO_BUILDING.getHeight(), Building.JIN_MAO_BUILDING.getImage()));
        model.addAnnotation(new ChartImage(4.5, 0, 5.5, Building.EMPIRE_STATE_BUILDING.getHeight(), Building.EMPIRE_STATE_BUILDING.getImage()));
        return model;
    }

    public ChartLabel createLabel(ChartCategory<?> cat, double height) {
        ChartLabel label = new ChartLabel(new RealPosition(cat.position()), new RealPosition(height), cat.getName());
        LabelStyle labelStyle = new LabelStyle();
        labelStyle.setColor(Color.magenta);
        labelStyle.setRotation(-Math.PI / 8);
        label.setLabelStyle(labelStyle);
        return label;
    }

    @Override
    public String getDescription() {
        return "This example shows that you can add images to a chart. " +
                "The images have been scaled according to the heights of the buildings.";
    }

    public String getName() {
        return "With Images Chart";
    }

    @Override
    public String getDemoFolder() {
        return "R1.Charts";
    }

    public Component getDemoPanel() {
        if (demoPanel == null) {
            demoPanel = createDemo();
        }
        return demoPanel;
    }

    public String getProduct() {
        return PRODUCT_NAME_CHARTS;
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new WithImagesChartDemo());
    }

}
