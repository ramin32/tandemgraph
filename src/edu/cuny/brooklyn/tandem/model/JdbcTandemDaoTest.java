package edu.cuny.brooklyn.tandem.model;

import java.util.List;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

/**
 * User: ramin Date: Oct 4, 2008 Time: 8:33:20 AM
 */
public class JdbcTandemDaoTest extends TestCase
{
    private static final Logger logger_ = Logger.getLogger(JdbcTandemDaoTest.class);
    private JdbcTandemDao jdbcTandemDao_;
    
    public void setUp()
    {
        long start = System.currentTimeMillis();
        jdbcTandemDao_ = JdbcTandemDao.getInstance();
        logger_.debug("Obtaining jdbcTandemDao instance took: " + (System.currentTimeMillis() - start) + "ms");
    }
    
    public void testGetAllChromosomes()
    {
        long start = System.currentTimeMillis();
        List<Chromosome> chromosomes = jdbcTandemDao_.getAllChromosomes();
        logger_.debug("Obtaining all chromosomes took: " + (System.currentTimeMillis() - start) + "ms");
        
        assertFalse(chromosomes.size() == 0);
        logger_.debug(chromosomes);
    }
    
    public void testGetInputLine()
    {
        Chromosome chromosome = new Chromosome(1, "CH1");
        String expected = "TAACCCTAACCCTAACCCTAACCCTAACCCTAACCCTAACCCTAACCCTAACCCTAACCCTAACCCTAACCCTAACCCTAACCCTAACCCTAACCCTAACCCTAACCCAACCCTAACCCTAACCCTAACCCTAACCCTAACCCTAACCCCTAACCCTAACCCTAACCCTAACCCTAACCTAACCCTAACCCTAACCCTAACCCTAACCCTAACCCTAACCCTAACCCTAACCCCTAACCCTAAC";
        String actual = jdbcTandemDao_.getInputString(chromosome, 0, 61 * 4);
        assertEquals("testGetInputLine", expected, actual);
    }
    
}
