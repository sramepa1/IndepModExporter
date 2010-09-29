import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.text.ParseException;

import javax.swing.JPanel;
import com.jidesoft.chart.Chart;
import com.jidesoft.chart.Legend;
import com.jidesoft.chart.axis.NumericAxis;
import com.jidesoft.chart.axis.TimeAxis;
import com.jidesoft.chart.model.DefaultChartModel;
import com.jidesoft.chart.model.SummingChartModel;
import com.jidesoft.chart.style.ChartStyle;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.utils.TimeUtils;

@SuppressWarnings("serial")
public class StackedAreaChartDemo extends AbstractDemo {
    private Chart chart;
    private JPanel demoPanel;

    public Component getDemoPanel() {
        if (demoPanel == null) {
            demoPanel = new JPanel(new BorderLayout());
            chart = new Chart();
            demoPanel.add(chart, BorderLayout.CENTER);
            demoPanel.setPreferredSize(new Dimension(500, 500));
            try {
                long from = TimeUtils.createTime("01-Jan-1970 00:00:00").getTime();
                long to = TimeUtils.createTime("01-Jan-1980 00:00:00").getTime();
                TimeAxis xAxis = new TimeAxis(from, to);
                NumericAxis yAxis = new NumericAxis(0, 110);
                chart.setXAxis(xAxis);
                chart.setYAxis(yAxis);
                chart.setVerticalGridLinesVisible(false);
                DefaultChartModel boys = createModel("Boys", from, to);
                DefaultChartModel girls = createModel("Girls ind.", from, to);
                SummingChartModel total = new SummingChartModel("Girls", boys, girls);
                ChartStyle totalStyle = new ChartStyle(Color.magenta, false, true, false);
                totalStyle.setLineFill(new Color(255, 240, 255, 200));
                chart.addModel(total, totalStyle);
                //ChartStyle girlStyle = new ChartStyle(Color.pink.darker(), false, true, false);
                //girlStyle.setLineFill(new Color(255, 175, 175, 70));
                //chart.addModel(girls, girlStyle);
                ChartStyle boyStyle = new ChartStyle(Color.blue, false, true, false);
                boyStyle.setLineFill(new Color(0, 255, 255, 50));
                chart.addModel(boys, boyStyle);
                JPanel legendPanel = new JPanel();
                chart.setTitle("Population Variation");
                Legend legend = new Legend(chart, 3);
                legend.setBackground(Color.white);
                legendPanel.add(legend);
                demoPanel.add(legendPanel, BorderLayout.SOUTH);
            } catch (ParseException pe) {
                pe.printStackTrace();
            }
        }
        return demoPanel;
    }

    @Override
    public int getAttributes() {
        return ATTRIBUTE_NEW;
    }

    public String getName() {
        return "Stacked Area Chart";
    }

    @Override
    public String getDemoFolder() {
        return "R1.Charts";
    }

    public String getProduct() {
        return PRODUCT_NAME_CHARTS;
    }

    @Override
    public String getDescription() {
        return "The idea with this demo is to show how two independent traces can be summed to give a resulting trace. "+
        " The trace you can see here for boys is an independent trace and there is a similar trace generated for girls "+
        "that is not shown. Instead, we add together the two traces and the magenta line shown is the sum of "+
        "the boys and girls; i.e., the total population. ";
    }

    public DefaultChartModel createModel(String modelName, long from, long to) {
        DefaultChartModel model = new DefaultChartModel(modelName);
        long interval = (to - from) / 10;
        long time = from;
        for (int i = 0; i <= 10; i++) {
            int y = (int) (60 * Math.random());
            model.addPoint(time, y);
            time += interval;
        }
        return model;
    }

    public static void main(String[] args) throws Exception {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new StackedAreaChartDemo());
    }
}

