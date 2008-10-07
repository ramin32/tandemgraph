package edu.cuny.brooklyn.tandem.model;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * User: ramin
 * Date: Oct 2, 2008
 * Time: 8:36:12 PM
 */
public class JdbcTandemDao extends SimpleJdbcDaoSupport
{

    private static final String SELECT_ALL_CHROMOSOMES = "SELECT chromosome_id, name FROM chromosome";
    private static final String SELECT_ALL_DISTANCES_BY_CHROMOSOME_ID = "SELECT start, end FROM edit_distance WHERE chromosome_id = ?";

    private static JdbcTandemDao jdbcTandemDaoInstance_;

    public static JdbcTandemDao getInstance()
    {
        if (jdbcTandemDaoInstance_ == null)
            jdbcTandemDaoInstance_ = (JdbcTandemDao) new ClassPathXmlApplicationContext("conf/spring.xml").
                    getBean("jdbcTandemDao");
        return jdbcTandemDaoInstance_;
    }

    public List<Chromosome> getAllChromosomes()
    {
        return getSimpleJdbcTemplate().query(SELECT_ALL_CHROMOSOMES, new ParameterizedRowMapper<Chromosome>()
        {
            public Chromosome mapRow(ResultSet resultSet, int i) throws SQLException
            {
                return new Chromosome(resultSet.getInt("chromosome_id"), resultSet.getString("name"));
            }
        });
    }

    public List<Distance> getAllDistancesByChromosome(Chromosome chromosome)
    {
        return getSimpleJdbcTemplate().query(SELECT_ALL_DISTANCES_BY_CHROMOSOME_ID, new ParameterizedRowMapper<Distance>()
        {
            public Distance mapRow(ResultSet resultSet, int i) throws SQLException
            {
                return new Distance(resultSet.getInt("start"), resultSet.getInt("end"));
            }
        }, chromosome.getId());
    }
}
