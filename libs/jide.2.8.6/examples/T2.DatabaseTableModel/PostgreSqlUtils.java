/*
 * @(#)PostgreSqlUtils.java 7/6/2008
 *
 * Copyright 2002 - 2008 JIDE Software Inc. All rights reserved.
 */

import java.util.logging.Logger;

public class PostgreSqlUtils extends SqlUtils {
    private static final Logger LOGGER = Logger.getLogger(PostgreSqlUtils.class.getName());

    @Override
    String getDriver() {
        return "org.postgresql.Driver";
    }

    @Override
    String getConnectionURL() {
        return "jdbc:postgresql:" + getDatabaseName();
    }

    @Override
    Logger getLogger() {
        return LOGGER;
    }

    @Override
    String getDatabaseName() {
        return "jidedb";
    }

    @Override
    String getUserName() {
        return "sa";
    }

    @Override
    String getPassword() {
        return "sa";
    }

    String getName() {
        return "PostgreSQL";
    }
}