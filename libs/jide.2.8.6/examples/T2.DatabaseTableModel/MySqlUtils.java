/*
 * @(#)MySqlUtils.java 9/10/2008
 *
 * Copyright 2002 - 2008 JIDE Software Inc. All rights reserved.
 */

import java.util.logging.Logger;

public class MySqlUtils extends SqlUtils {
    private static final Logger LOGGER = Logger.getLogger(MySqlUtils.class.getName());

    @Override
    String getDriver() {
        return "com.mysql.jdbc.Driver";
    }

    @Override
    String getConnectionURL() {
        return "jdbc:mysql://" + getDatabaseName();
    }

    @Override
    Logger getLogger() {
        return LOGGER;
    }

    @Override
    String getDatabaseName() {
        return "localhost/jidedb";
    }

    @Override
    String getUserName() {
        return "sa";
    }

    @Override
    String getPassword() {
        return "";
    }

    String getName() {
        return "MySQL";
    }
}