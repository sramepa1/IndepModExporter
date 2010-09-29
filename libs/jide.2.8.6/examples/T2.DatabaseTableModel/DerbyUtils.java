import java.util.logging.Logger;

public class DerbyUtils extends SqlUtils {
    private static final Logger LOGGER = Logger.getLogger(DerbyUtils.class.getName());

    String getName() {
        return "Derby";
    }

    @Override
    String getDriver() {
        return "org.apache.derby.jdbc.EmbeddedDriver";
    }

    @Override
    String getConnectionURL() {
        return "jdbc:derby:" + getDatabaseName() + ";create=true";
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
        return "";
    }

    @Override
    String getPassword() {
        return "";
    }

    @Override
    protected String getCreateTableStatement() {
        return "CREATE TABLE SALES (ID INT PRIMARY KEY, CATEGORYNAME VARCHAR(100), PRODUCTNAME VARCHAR(100), AMOUNT DECIMAL(8,3) NOT NULL, ORDERDATE DATE)";
    }
}