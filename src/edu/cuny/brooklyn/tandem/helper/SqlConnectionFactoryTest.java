package edu.cuny.brooklyn.tandem.helper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import junit.framework.TestCase;

/**
 * Created by IntelliJ IDEA. User: ramin Date: Sep 28, 2008 Time: 12:02:54 AM To
 * change this template use File | Settings | File Templates.
 */
public class SqlConnectionFactoryTest extends TestCase
{
    public void testGetConnection()
    {
        long start = System.currentTimeMillis();
        Connection connection = SqlConnectionFactory.getConnection();
        System.out.println("Time to connect: " + (System.currentTimeMillis() - start) + "ms");
        
        start = System.currentTimeMillis();
        connection = SqlConnectionFactory.getConnection();
        System.out.println("Time to connect 2nd time: " + (System.currentTimeMillis() - start) + "ms");
        
        try
        {
            start = System.currentTimeMillis();
            Statement statement = connection.createStatement();
            System.out.println("Time to create statement: " + (System.currentTimeMillis() - start) + "ms");
            
            start = System.currentTimeMillis();
            statement.execute("SELECT start, end FROM edit_distance WHERE chromosome_id = 2 LIMIT 10");
            System.out.println("Time to execute query: " + (System.currentTimeMillis() - start) + "ms");
            
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next())
            {
                System.out.println("" + resultSet.getInt("start") + " " + resultSet.getInt("end"));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (connection != null)
            {
                try
                {
                    connection.close();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
