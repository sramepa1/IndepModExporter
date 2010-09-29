import com.jidesoft.utils.SecurityUtils;

import java.io.File;
import java.sql.*;
import java.util.logging.Logger;

abstract public class SqlUtils {
    abstract String getDriver();

    abstract String getConnectionURL();

    abstract Logger getLogger();

    abstract String getDatabaseName();

    abstract String getUserName();

    abstract String getPassword();

    abstract String getName();

    /**
     * Gets the database connection. This connection is used by this class to update the database.
     *
     * @return the database connection.
     *
     * @throws java.sql.SQLException if any SQL exception happens
     */
    public Connection getConnection() throws SQLException {
        if (getUserName() != null) {
            return DriverManager.getConnection(getConnectionURL(), getUserName(), getPassword());
        }
        else {
            return DriverManager.getConnection(getConnectionURL());
        }
    }

    protected String getDefaultFolder() {
        return SecurityUtils.getProperty("user.home", "") + File.separator + ".jidedb" + File.separator;
    }

    /**
     * Initializes the database.
     *
     * @param alwaysCreate true to always create the database
     */
    @SuppressWarnings({"CallToPrintStackTrace"})
    public void initializeDB(boolean alwaysCreate) {
        Connection connection = null;
        try {
            registerDriver();
            connection = getConnection();
            if (connection != null) {
                if (alwaysCreate || isSampleTableAvailable(connection) == -1) {
                    installSampleTable(connection);

                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (connection != null) {
                try {
                    connection.close();
                }
                catch (SQLException e) {
                    getLogger().warning(e.getLocalizedMessage());
                }
            }
        }
    }

    public int isSampleTableAvailable(Connection connection) {
        try {
            Statement stat = connection.createStatement();
            ResultSet rs = stat.executeQuery("select count(*) from SALES");
            rs.next();
            int count = rs.getInt(1);
            rs.close();
            return count;
        }
        catch (SQLException e) {
            // ignore
        }
        return -1;
    }

    public void registerDriver() throws ClassNotFoundException {
        //register class
        Class.forName(getDriver());
    }

    /**
     * Installs the database.
     *
     * @param connection the connection.
     */
    @SuppressWarnings({"CallToPrintStackTrace"})
    public void installSampleTable(Connection connection) {
        try {
            Statement statement = connection.createStatement();
            try {
                statement.execute(getDeleteTableStatement());
            }
            catch (Throwable t) {
                // ignore
            }
            try {
                statement.execute(getCreateTableStatement());
            }
            catch (Throwable t) {
                t.printStackTrace();
            }
            statement.close();
        }
        catch (Exception e) {
            getLogger().warning(e.getLocalizedMessage());
        }
    }

    protected String getDeleteTableStatement() {
        return "DROP TABLE SALES";
    }

    protected String getCreateTableStatement() {
        return "CREATE TABLE SALES (ID INT PRIMARY KEY, CATEGORYNAME VARCHAR(100), PRODUCTNAME VARCHAR(100), AMOUNT FLOAT, ORDERDATE DATE)";
    }
}
