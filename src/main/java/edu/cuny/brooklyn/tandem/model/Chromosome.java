package edu.cuny.brooklyn.tandem.model;

/**
 * User: ramin
 * Date: Oct 3, 2008
 * Time: 6:54:32 AM
 */
public class Chromosome
{
    private final int id_;
    private final String name_;

    public Chromosome(int id, String name)
    {
        id_ = id;
        name_ = name;
    }

    public int getId()
    {
        return id_;
    }

    public String getName()
    {
        return name_;
    }
}
