package edu.cuny.brooklyn.tandem.helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * User: ramin
 * Date: Sep 27, 2008
 * Time: 7:39:27 PM
 */
public class SqlConnectionFactory
{

    private static final String HOST = "jdbc:mysql://tandem.sci.brooklyn.cuny.edu/tandem";
    private static final String USERNAME = "client";
    private static final String PASSWORD = "client";

    private static Connection connection_;
    private static Statement statement_;

    static
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            connection_ =
                    DriverManager.getConnection(HOST, USERNAME, PASSWORD);
            statement_ = connection_.createStatement();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    private SqlConnectionFactory()
    {
    }


    public static final Connection getConnection()
    {
        return connection_;
    }

    public static Statement getStatement()
    {
        return statement_;
    }

    public static Statement getPreparedStatement(String query)
    {
        Statement statement = null;
        try
        {
            statement = connection_.prepareStatement(query);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return statement;
    }


}
