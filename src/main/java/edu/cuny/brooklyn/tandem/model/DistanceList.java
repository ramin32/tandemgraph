/****************************************************************
 * DistanceList.java Delegates ArrayList<Distance>, providing specific
 * functionality for working with a list of Tandem Repeats. Author: Ramin
 * Rakhamimov Brooklyn College Research Project Under the supervion of Professor
 * Sokol
 ************************************************************************/
package edu.cuny.brooklyn.tandem.model;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class DistanceList extends AbstractList<Distance>
{
    private final List<Distance> distances_;
    private final Comparator<Distance> distanceComparator;
    {
        distanceComparator = new Comparator<Distance>()
        {
            public int compare(Distance o1, Distance o2)
            {
                if(o1.isWithin(o2))
                    return 0;
                return o1.compareTo(o2);
            }
        };
    }
    
    private final LimitedRange limitedRange_;
    private int maxSize_;
    
    private String inputId_;
    private Chromosome chromosome_;

    private Distance selectedDistance_;
    
    public DistanceList()
    {
        distances_ = new ArrayList<Distance>();
        limitedRange_ = new LimitedRange();
        
    }
    
    public void load()
    {
        if (chromosome_ == null)
            throw new IllegalStateException("Chromosome name not set!");
        
        distances_.clear();
        addAll(JdbcTandemDao.getInstance().getAllDistancesByChromosome(chromosome_));
    }
    
    /**
     * Adds a Distance object to the DistanceList and adjusts extremes as
     * necessary
     * 
     * @param distance
     */
    public boolean add(Distance distance)
    {
        try
        {
            // if list is empty assign current distance points to extremes
            if (isEmpty())
            {
                limitedRange_.setGlobal(distance);
                maxSize_ = distance.getSize();
                // Otherwise adjust them accordingly.
            }
            else
            {
                if (limitedRange_.getGlobalMin() > distance.getMin())
                    limitedRange_.setGlobalMin(distance.getMin());
                
                if (limitedRange_.getGlobalMax() < distance.getMax())
                    limitedRange_.setGlobalMax(distance.getMax());
                
                if (maxSize_ < distance.getSize())
                    maxSize_ = distance.getSize();
            }
            distances_.add(distance);
        }
        catch (NullPointerException ex)
        {
            System.out.println("Null " + ex.getMessage() + " " + distance + " " + limitedRange_ + " " + maxSize_);
            ex.printStackTrace();
        }
        
        return true;
    }
    
    public void add(int start, int end, int id)
    {
        add(new Distance(start, end, id));
    }
    
    public void clear()
    {
        distances_.clear();
        maxSize_ = 0;
        limitedRange_.clear();
    }
    
    public boolean isEmpty()
    {
        return distances_.isEmpty();
    }
    
    public Distance get(int index)
    {
        return distances_.get(index);
    }
    
    public int getMaxRepeatSize()
    {
        return maxSize_;
    }
    
    public int getSize()
    {
        return distances_.size();
    }
    
    public LimitedRange getLimitedRange()
    {
        return limitedRange_;
    }
    
    // /**
    // * Sorts the DistanceList by natural order--ascending starting points.
    // */
    // public void sort()
    // {
    // Collections.sort(distances_);
    // }
    //
    // public void sortBySize(final boolean ascending)
    // {
    // // Create a comparator to sort by size instead of starting location
    // Comparator<Distance> c = new Comparator<Distance>()
    // {
    // public int compare(Distance r1, Distance r2)
    // {
    // if (ascending) return r1.getSize() - r2.getSize();
    // else return r2.getSize() - r1.getSize();
    // }
    // };
    //
    // Collections.sort(distances_, c);
    // }
    public int find(Distance d)
    {
        int index = Collections.binarySearch(distances_, d, distanceComparator);
        
        if (index < 0)
            index = (index + 1) * (-1);
        
        return index;
    }
    
    public int getStartIndex()
    {
        int localStart = getLimitedRange().getLocalMin();
        Distance d = new Distance(localStart,localStart,0);
        return find(d);
    }
    
    public int getEndIndex()
    {
        int localEnd = getLimitedRange().getLocalMax();
        Distance d = new Distance(localEnd,localEnd,0);
        return find(d);
    }
    
    public Iterator<Distance> iterator()
    {
        return distances_.iterator();
    }
    
    public void print()
    {
        for (Distance r : this)
        {
            System.out.println(r);
        }
    }
    
    public List<Distance> toList()
    {
        return distances_;
    }
    
    @Override public int size()
    {
        return distances_.size();
    }
    
    public void setChromosome(Chromosome chromosome)
    {
        chromosome_ = chromosome;
    }
    
    public Chromosome getChromosome()
    {
        return chromosome_;
    }
    
    public int getIntLogMaxSize()
    {
        return (int) Math.round(Math.log10(getMaxRepeatSize()));
    }
    
    public double getDoubleLogMaxSize()
    {
        return Math.log10(getMaxRepeatSize());
    }

    public void setSelectedDistance(Distance selectedDistance)
    {
        selectedDistance_ = selectedDistance;
        
    }

    public Distance getSelectedDistance()
    {
        return selectedDistance_;
    }
}
