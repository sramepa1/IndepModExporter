/*
 * @(#)InitializeDatabase.java 9/8/2008
 *
 * Copyright 2002 - 2008 JIDE Software Inc. All rights reserved.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Vector;
import java.util.zip.GZIPInputStream;

public class InitializeDatabase {

    public static void populateDatabase(Connection connection) {
        try {
            populateSalesTable(connection, 100);
            connection.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void populateSalesTable(Connection connection, int multiply) throws SQLException {
        try {
            InputStream resource = InitializeDatabase.class.getClassLoader()
                    .getResourceAsStream("ProductReports.txt.gz");
            if (resource == null) {
                return;
            }
            InputStream in = new GZIPInputStream(resource);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    in, "UTF-8"));

            Vector<String> columnNames = new Vector<String>();

            String columnsLine = reader.readLine(); // skip first line
            String[] columnValues = columnsLine.split("\t");

            columnNames.addAll(Arrays.asList(columnValues));

            int id = 1;
            int count = 0;
            PreparedStatement pstmt = connection.prepareStatement("INSERT into Sales (ID, CATEGORYNAME, PRODUCTNAME, AMOUNT, ORDERDATE) values(?, ?, ?, ?, ?)");
            do {
                String line = reader.readLine();
                if (line == null || line.length() == 0) {
                    break;
                }
                String[] values = line.split("\t");
                pstmt.setString(2, values[0]);
                pstmt.setString(3, values[1]);

                String value = values[2];
                if (value.startsWith("$")) {
                    float f = Float.parseFloat(value.substring(1));
                    pstmt.setFloat(4, f);
                }

                SimpleDateFormat format = new SimpleDateFormat(
                        "MM/dd/yyyy hh:mm:ss");
                try {
                    Date date = format.parse(values[3]);
                    java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                    pstmt.setDate(5, sqlDate);
                }
                catch (ParseException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < multiply; i++) {
                    pstmt.setInt(1, id++);
                    pstmt.executeUpdate();
                }

                count += multiply;
                System.out.println(count);
            }
            while (true);
            pstmt.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings({"CallToPrintStackTrace"})
    public static void main(String[] args) {
        SqlUtils sqlUtils = new DerbyUtils();
        sqlUtils.initializeDB(true);
        long start = System.currentTimeMillis();
        try {
            populateDatabase(sqlUtils.getConnection());
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println("Takes " + (end - start));
    }
}