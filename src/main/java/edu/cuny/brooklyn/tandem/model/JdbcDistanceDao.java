package edu.cuny.brooklyn.tandem.model;

import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * User: ramin
 * Date: Oct 2, 2008
 * Time: 8:36:12 PM
 */
public class JdbcDistanceDao extends SimpleJdbcDaoSupport
{
    private static final JdbcDistanceDao distanceDao;

    private static final String SELECT_ALL_CHROMOSOMES =
            "SELECT name FROM chromosome";
    
    static
    {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("edu/cuny/brooklyn/tandem/model/spring.xml");
        distanceDao = (JdbcDistanceDao) applicationContext.getBean("distanceDao");
    }

    public static List<Map<String, Object>> getAllChromosomeNames()
    {
        return distanceDao.getSimpleJdbcTemplate().queryForList(SELECT_ALL_CHROMOSOMES);
    }
}
