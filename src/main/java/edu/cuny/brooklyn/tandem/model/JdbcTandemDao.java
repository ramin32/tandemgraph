package edu.cuny.brooklyn.tandem.model;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class JdbcTandemDao extends SimpleJdbcDaoSupport
{
    private static final Logger logger_ = Logger.getLogger(JdbcTandemDao.class);
    private static Integer inputSingleLineSize_ = null;

    private static final String SELECT_ALL_CHROMOSOMES =
            "SELECT chromosome_id, name " +
            "FROM chromosome_filter";

    private static final String SELECT_ALL_DISTANCES_BY_CHROMOSOME_ID =
            "SELECT start, end, edit_distance_id " +
            "FROM edit_distance_filter " +
            "WHERE chromosome_id = ? " +
            "ORDER BY start, end";

    private static final String SELECT_LENGHT_OF_INPUT_LINE =
            "SELECT length(line) " +
            "FROM input " +
            "LIMIT 1";

    private static final String SELECT_FIRST_ID_OF_INPUT_LINE_WITH_CHROMOSOME =
            "SELECT input_id " +
            "FROM input " +
            "WHERE chromosome_id = ? " +
            "LIMIT 1";

    private static final String SELECT_INPUT_LINE_BY_INPUT_ID =
            "SELECT line " +                                                        
            "FROM input " +
            "WHERE input_id = ?";

     private static final String SELECT_ALIGNMENT_BY_EDIT_DISTANCE_ID =
             "SELECT group_concat(txt SEPARATOR '') " +
             "FROM alignment_filter " +
             "WHERE edit_distance_id = ? " +
             "GROUP BY edit_distance_id " +
             "LIMIT 1";

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
                return new Distance(resultSet.getInt("start"), resultSet.getInt("end"), resultSet.getInt("edit_distance_id"));
            }
        }, chromosome.getId());
    }

    public String getInputString(Chromosome chromosome, int start, int length)
    {
        if(inputSingleLineSize_ == null)
            inputSingleLineSize_ = getSimpleJdbcTemplate().queryForInt(SELECT_LENGHT_OF_INPUT_LINE);

        int lineId = start / inputSingleLineSize_;
        int linePos = start % inputSingleLineSize_;
        if(linePos > 0)
            lineId++;

        lineId += getSimpleJdbcTemplate().queryForInt(SELECT_FIRST_ID_OF_INPUT_LINE_WITH_CHROMOSOME, chromosome.getId());

        StringBuilder result = new StringBuilder(
                getSimpleJdbcTemplate().
                        queryForObject(SELECT_INPUT_LINE_BY_INPUT_ID, String.class, lineId).
                        substring(linePos).trim());
        while(result.length() < length)
        {
           lineId++;
           result.append(getSimpleJdbcTemplate().queryForObject(SELECT_INPUT_LINE_BY_INPUT_ID, String.class, lineId).trim());
        }

        return result.substring(0, length); 
    }

    public String getAlignmentByDistance(Distance distance)
    {
      return getSimpleJdbcTemplate().queryForObject(SELECT_ALIGNMENT_BY_EDIT_DISTANCE_ID, String.class, distance.getId()); 
    }
}
