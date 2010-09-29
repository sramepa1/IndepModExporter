/*
 * @(#)WeatherData.java 8/22/2009
 *
 * 2002 - 2010 JIDE Software Inc. All rights reserved.
 * Copyright 2005 - 2010 Catalysoft Ltd. All rights reserved.
 */

import com.jidesoft.chart.model.DefaultChartModel;
import com.jidesoft.csv.CsvReader;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * A data store for the Cambridge Weather data, which is used in more than one example.
 */
public class WeatherData {
    private final int yearColumn = 0;
    private final int monthColumn = 1;
    private final int tempMaxColumn = 2;
    private final int rainfallColumn = 5;
    private final int sunshineColumn = 6;
    private static final DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
    //private static final DateFormat yearFormat = new SimpleDateFormat("yyyy");
    private static WeatherData instance;
    private DefaultChartModel[] maxTemps = new DefaultChartModel[12];
    private DefaultChartModel[] rainfallModels = new DefaultChartModel[12];
    private DefaultChartModel[] sunshineModels = new DefaultChartModel[12];

    private WeatherData() {
        try {
            load();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static WeatherData getInstance() {
        if (instance == null) {
            instance = new WeatherData();
        }
        return instance;
    }

    public DefaultChartModel getTempMaxModel(Month month) {
        return maxTemps[month.ordinal()];
    }

    public DefaultChartModel getRainfallModel(Month month) {
        return rainfallModels[month.ordinal()];
    }

    public DefaultChartModel getSunshineModel(Month month) {
        return sunshineModels[month.ordinal()];
    }

    protected void load() throws IOException, ParseException {
        CsvReader reader = null;
        reader = new CsvReader('\t');
        // Load the data file from the classpath
        InputStream dataStream = getClass().getResourceAsStream("/cambridgedata.txt");
        List<List<String>> csvValues = reader.parse(dataStream);

        final int headerLines = 2; // The number of header lines
        for (int line = 0; line < csvValues.size(); line++) {
            if (line >= headerLines) {
                List<String> lineValues = csvValues.get(line);
                String yearString = lineValues.get(yearColumn);
                Month month = Month.getMonth(new Integer(lineValues.get(monthColumn)));
                String maxTempString = lineValues.get(tempMaxColumn);
                Double maxTemp = Double.parseDouble(maxTempString);
                String rainfallString = lineValues.get(rainfallColumn);
                Double rainfall = null;
                try {
                    rainfall = Double.parseDouble(rainfallString);
                }
                catch (NumberFormatException nfe) {
                }
                String sunshineString = lineValues.get(sunshineColumn);
                Double sunshine = null;
                try {
                    sunshine = Double.parseDouble(sunshineString);
                }
                catch (NumberFormatException nfe) {
                }
                String dateString = String.format("15-%s-%s", month, yearString);
                long time = dateFormat.parse(dateString).getTime();
                long year = dateFormat.parse("15-Jun-" + yearString).getTime();
                // Used to check for month not being null in the following, but
                // it can't be null as it is an int
                if (yearString != null && maxTemp != null) {
                    addToModel(maxTemps, month, time, maxTemp);
                    addToModel(rainfallModels, month, time, rainfall);
                    addToModel(sunshineModels, month, year, sunshine);
                }
            }
        }
    }

    private void addToModel(DefaultChartModel[] array, Month month, long time, Double value) {
        if (value == null) {
            return;
        }
        DefaultChartModel m = array[month.ordinal()];
        if (m == null) {
            m = new DefaultChartModel(month);
            array[month.ordinal()] = m;
        }
        m.addPoint(time, value);
    }
}
