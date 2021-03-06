package edu.cuny.brooklyn.tandem.model;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;

public class JdbcTandemDao extends SimpleJdbcDaoSupport
{
    private static final Logger logger_ = Logger.getLogger(JdbcTandemDao.class);
    private static Integer inputSingleLineSize_ = null;

    private static final String SELECT_ALL_CHROMOSOMES = "SELECT chromosome_id, name " + 
        "FROM chromosome_filter";

    private static final String SELECT_ALL_DISTANCES_BY_CHROMOSOME_ID = "SELECT " +
        "start, end, edit_distance_id, period, errs " +
        "FROM edit_distance_filter " + 
        "WHERE chromosome_id = ? " + 
        "ORDER BY start, end";

    private static final String SELECT_LENGTH_OF_INPUT_LINE = "SELECT length(line) " + 
        "FROM input " + 
        "LIMIT 1";

    private static final String SELECT_FIRST_ID_OF_INPUT_LINE_WITH_CHROMOSOME = "SELECT input_id " + 
        "FROM input " + 
        "WHERE chromosome_id = ? " + 
        "LIMIT 1";

    private static final String SELECT_INPUT_LINE_BY_INPUT_ID = "SELECT line " + 
        "FROM input " + 
        "WHERE input_id = ?";

    private static final String SELECT_GROUPED_ALIGNMENT_BY_EDIT_DISTANCE_ID = 
        "SELECT group_concat(txt SEPARATOR '') " + 
        "FROM alignment_filter " + 
        "WHERE edit_distance_id = ? " + 
        "GROUP BY edit_distance_id " + 
        "LIMIT 1";

    private static final String SELECT_DISTANCE_INFORAMTION_BY_EDIT_DISTANCE_ID = 
        "SELECT period, errs " +
        "FROM edit_distance_filter " +
        "WHERE edit_distance_id = ?";

    private static final String INPUT_URL = "http://tandem.sci.brooklyn.cuny.edu/GetInput.do?chromosome=%s&offset=%s&length=%s";


    private static JdbcTandemDao jdbcTandemDaoInstance_;
    private List<Chromosome> chromosomes_;

    public static JdbcTandemDao getInstance()
    {
        if (jdbcTandemDaoInstance_ == null)
            jdbcTandemDaoInstance_ = (JdbcTandemDao) new ClassPathXmlApplicationContext("conf/spring.xml").getBean("jdbcTandemDao");
        return jdbcTandemDaoInstance_;
    }    

    public List<Chromosome> getAllChromosomes()
    {
        if(chromosomes_ == null)
        {
            chromosomes_ =  getSimpleJdbcTemplate().query(SELECT_ALL_CHROMOSOMES, new ParameterizedRowMapper<Chromosome>()
            {
        public Chromosome mapRow(ResultSet resultSet, int i) throws SQLException
            {
                return new Chromosome(resultSet.getInt("chromosome_id"), resultSet.getString("name"));
            }
            });
        }
        return chromosomes_;
    }

    public List<Distance> getAllDistancesByChromosome(Chromosome chromosome)
    {
        return getSimpleJdbcTemplate().query(SELECT_ALL_DISTANCES_BY_CHROMOSOME_ID, 
                new ParameterizedRowMapper<Distance>()
                {
                    public Distance mapRow(ResultSet resultSet, int i) throws SQLException
                    {
                        return new Distance(resultSet.getInt("start"),
                                            resultSet.getInt("end"), 
                                            resultSet.getInt("edit_distance_id"));

                    }
                }, 
                chromosome.getId());
    }

    public DistanceInformation getDistanceInformationByDistance(final Distance distance)
    {
        return getSimpleJdbcTemplate().queryForObject(SELECT_DISTANCE_INFORAMTION_BY_EDIT_DISTANCE_ID, new ParameterizedRowMapper<DistanceInformation>()
                {
                    public DistanceInformation mapRow(ResultSet resultSet, int i) throws SQLException
        {
            return new DistanceInformation(resultSet.getInt("period"), resultSet.getInt("errs"), distance.getId());

        }
        }, distance.getId());
    }

    public String getInputString(Chromosome chromosome, int offset, int length)
    {
        String result = "";
        try
        {
            String formattedUrlString = String.format(INPUT_URL, chromosome.getId(), offset, length);
            URL getInputUrl = new URL(formattedUrlString);

            Scanner scanner = new Scanner(getInputUrl.openStream()).useDelimiter("\\Z+|\\s+");
            result = scanner.next();
        }
        catch (IOException e)
        {
            logger_.error(e.getMessage(), e);
        }

        return result;

    }

    public String getAlignmentByDistance(Distance distance)
    {
        return getSimpleJdbcTemplate().queryForObject(SELECT_GROUPED_ALIGNMENT_BY_EDIT_DISTANCE_ID, String.class, distance.getId());
    }

    public void initialize() {
        getAllChromosomes();		
    }
}
