package edu.cuny.brooklyn.tandem.model;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import java.util.List;

/**
 * User: ramin
 * Date: Oct 4, 2008
 * Time: 8:33:20 AM
 */
public class JdbcTemplateDaoTest extends TestCase
{
    private static final Logger logger_ = Logger.getLogger(JdbcTemplateDaoTest.class);
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

        assertEquals(chromosomes.size(),3);
        logger_.debug(chromosomes);

        start = System.currentTimeMillis();
        List<Distance> distances = jdbcTandemDao_.getAllDistancesByChromosome(chromosomes.get(0));
        logger_.debug("Obtaining all distances took: " + (System.currentTimeMillis() - start) + "ms");
        
        assertEquals(distances.size(), 224399);
    }

   
}
