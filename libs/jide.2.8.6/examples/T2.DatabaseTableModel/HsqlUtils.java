import java.util.logging.Logger;

public class HsqlUtils extends SqlUtils {
    private static final Logger LOGGER = Logger.getLogger(HsqlUtils.class.getName());

    @Override
    String getDriver() {
        return "org.hsqldb.jdbcDriver";
    }

    @Override
    String getConnectionURL() {
        return "jdbc:hsqldb:" + getDatabaseName() + "/db/";
    }

    @Override
    Logger getLogger() {
        return LOGGER;
    }

    @Override
    String getDatabaseName() {
        return "file:" + getDefaultFolder();
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
        return "HSQL";
    }

    @Override
    protected String getCreateTableStatement() {
        return "CREATE TABLE SALES (ID INT PRIMARY KEY, CATEGORYNAME VARCHAR(100), PRODUCTNAME VARCHAR(100), AMOUNT DECIMAL(8,3) NOT NULL, ORDERDATE DATE)";
    }
}
