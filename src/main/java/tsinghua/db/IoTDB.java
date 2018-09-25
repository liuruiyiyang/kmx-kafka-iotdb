package tsinghua.db;

import cn.edu.tsinghua.iotdb.jdbc.TsfileJDBCConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tsinghua.conf.Config;
import tsinghua.conf.ConfigDescriptor;
import tsinghua.conf.Constants;

import java.sql.*;
import java.text.SimpleDateFormat;

public class IoTDB {
    private static final Logger logger = LoggerFactory.getLogger(IoTDB.class);
    private Connection connection;
    private static Config config;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private int count = 0;
    Statement statement;

    public IoTDB() throws ClassNotFoundException {
        Class.forName(TsfileJDBCConfig.JDBC_DRIVER_NAME);
        config = ConfigDescriptor.getInstance().getConfig();
        try {
            init();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void init() throws SQLException {
        connection = DriverManager.getConnection(String.format(Constants.URL, config.host, config.port), Constants.USER,
                Constants.PASSWD);
    }

    public void close() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    public void executeSQL(String sql){
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        long startTimeStamp = System.nanoTime();
        try {
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null)
                    statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        long endTimeStamp = System.nanoTime();
        float executeTime = (endTimeStamp - startTimeStamp) / 1000000.0f;
        logger.info("Executed one SQL: {}", sql);
        logger.info("Latency: {} ms", executeTime);
    }

    private void executeSQLBatch(String sql){
        int[] result;
        long errorNum = 0;
        long startTimeStamp = System.nanoTime();
        if (statement == null) {
            try {
                statement = connection.createStatement();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        try {
            statement.addBatch(sql);
            count ++;
            if ((count % config.CACHE_NUM) == 0) {
                try {
                    result = statement.executeBatch();
                } catch (BatchUpdateException e) {
                    long[] arr = e.getLargeUpdateCounts();
                    for (long i : arr) {
                        if (i == -3) {
                            errorNum++;
                        }
                    }
                }
                statement.clearBatch();
                statement.close();
                count = 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        long endTimeStamp = System.nanoTime();
        float executeTime = (endTimeStamp - startTimeStamp) / 1000000.0f;
        logger.info("Executed one batch, latency: {} ms", executeTime);
        if (errorNum > 0) {
            logger.info("Batch insert failed, the failed number is {}! ", errorNum);
        }
    }
}
