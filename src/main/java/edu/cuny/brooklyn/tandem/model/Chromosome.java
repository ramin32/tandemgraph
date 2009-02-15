package edu.cuny.brooklyn.tandem.model;

/**
 * User: ramin Date: Oct 3, 2008 Time: 6:54:32 AM
 */
public class Chromosome implements Comparable<Chromosome>
{
    private final int    id_;
    private final String name_;
    private final String string_;
    
    public Chromosome(int id, String name)
    {
        id_ = id;
        name_ = name;
        string_ = "Name: " + name_ + ", ID: " + id_;
    }
    
    public int getId()
    {
        return id_;
    }
    
    public String getName()
    {
        return name_;
    }
    
    public String toString()
    {
        return string_;
    }
    
    public boolean equals(Object o)
    {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        
        Chromosome that = (Chromosome) o;
        
        if (id_ != that.id_)
            return false;
        if (!name_.equals(that.name_))
            return false;
        
        return true;
    }
    
    public int hashCode()
    {
        int result;
        result = id_;
        result = 31 * result + name_.hashCode();
        return result;
    }
    
    public int compareTo(Chromosome o)
    {
        return this.name_.compareTo(o.name_);
    }
}
