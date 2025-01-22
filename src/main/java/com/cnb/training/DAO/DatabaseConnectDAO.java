package com.cnb.training.DAO;


import com.cnb.training.util.exception.DataAccessException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;

/**
 *
 * @author EisenbraunM
 */


public class DatabaseConnectDAO {




    protected Connection getConnTraining() throws DataAccessException {
        DataSource ds = null;
        Connection conn = null;
        try {
            InitialContext initialContext = new InitialContext();
            Context envContext = (Context) initialContext.lookup("java:comp/env");

            ds = (DataSource) envContext.lookup("jdbc/Training");

            conn = ds.getConnection();
            conn.setAutoCommit(false);


        } catch (NamingException ne) {

            throw new DataAccessException(ne.toString());
        } catch (SQLException e) {
            throw new DataAccessException("SQL exception  getting  jdbc/Training connection: " + e.toString());
        }

        if (conn == null) {
            throw new DataAccessException("No  Datasource found for jdbc/Training");
        }
        return conn;
    }




    /**
     * Return a connection to the database.
     *
     * @returns a connection to the datasource.
     * @throws Exception specifying a database connection error.
     *
     * protected Connection getConnection() throws ApplicationException {
     * DatabaseConnection tConnection = null; try { tConnection =
     * DatabaseConnectionFactory.getDatbaseConnection();
     * tConnection.getConnection().setAutoCommit(false); LogUtil.log("Opened a
     * connection for " + this.getClass().getName()); } catch (Exception se) {
     * se.printStackTrace(); LogUtil.log("Exception in getConnection() of
     * baseDAO" + se.getMessage(), 4); throw new ApplicationException("Exception
     * in getConnection() of baseDAO" + se.getMessage()); } return
     * tConnection.getConnection(); }
     */
    /**
     * Returns a PreparedStatement of the given connection, set with the given
     * SQL query and the given parameter values.
     *
     * @param connection The Connection to create the PreparedStatement from.
     * @param sql The SQL query to construct the PreparedStatement with.
     * @param returnGeneratedKeys Set whether to return generated keys or not.
     * @param values The parameter values to be set in the created
     * PreparedStatement.
     * @throws SQLException If something fails during creating the
     * PreparedStatement.
     */
    public static PreparedStatement prepareStatement(Connection connection, String sql, boolean returnGeneratedKeys, Object... values)
            throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(sql,
                returnGeneratedKeys ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS);
        setValues(preparedStatement, values);
        return preparedStatement;
    }

    /**
     * Set the given parameter values in the given PreparedStatement.
     *
     * @param connection The PreparedStatement to set the given parameter values
     * in.
     * @param values The parameter values to be set in the created
     * PreparedStatement.
     * @throws SQLException If something fails during setting the
     * PreparedStatement values.
     */
    public static void setValues(PreparedStatement preparedStatement, Object... values)
            throws SQLException {
        for (int i = 0; i < values.length; i++) {
            preparedStatement.setObject(i + 1, values[i]);
        }
    }

    /**
     * Quietly close the Connection, Statement and ResultSet. Any errors will be
     * printed to the stderr.
     *
     * @param connection The Connection to be closed quietly.
     * @param statement The Statement to be closed quietly.
     * @param resultSet The ResultSet to be closed quietly.
     */
    public static void close(Connection connection, Statement statement, ResultSet resultSet) {
        close(resultSet);
        close(statement);
        close(connection);
    }

    /**
     * Quietly close the Connection. Any errors will be printed to the stderr.
     *
     * @param connection The Connection to be closed quietly.
     */
    public static void close(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Closing Connection failed: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Quietly close the Statement. Any errors will be printed to the stderr.
     *
     * @param statement The Statement to be closed quietly.
     */
    public static void close(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                System.err.println("Closing Statement failed: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Quietly close the ResultSet. Any errors will be printed to the stderr.
     *
     * @param resultSet The ResultSet to be closed quietly.
     */
    public static void close(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                System.err.println("Closing ResultSet failed: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
