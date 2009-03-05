/****************************************************************
 * DistanceList.java Delegates ArrayList<Distance>, providing specific
 * functionality for working with a list of Tandem Repeats. Author: Ramin
 * Rakhamimov Brooklyn College Research Project Under the supervion of Professor
 * Sokol
 ************************************************************************/
package edu.cuny.brooklyn.tandem.model;

import java.awt.Component;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.swing.JPanel;

import org.apache.log4j.Logger;

public class DistanceList extends AbstractList<Distance>
{
	private final static Logger logger_ = Logger.getLogger(DistanceList.class);
	private static final int TRAPEZOID_SUPPLEMENT = 1;

	private static final int TRIANGLE_SUPPLEMENT = 1000;
	
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

    private Integer selectedIndex_;
    
    private Integer cachedLocalStartIndex_;
    private Integer cachedLocalStart_;

    private Integer cachedLocalEndIndex_;
    private Integer cachedLocalEnd_;
	private DrawType drawType_ = DrawType.TRIANGLE;   // To prevent null pointer exception
    
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
    
    public int getAdjustedMaxRepeatSize()
    {
    	if(drawType_ == DrawType.TRIANGLE)
    	{
    		int amountToAdd = TRIANGLE_SUPPLEMENT - (maxSize_ % TRIANGLE_SUPPLEMENT);
    		return maxSize_ + amountToAdd;
    	}
    	else if(drawType_ == DrawType.TRAPEZOID)
    	{
    		return (int) Math.round(Math.log10(maxSize_) + TRAPEZOID_SUPPLEMENT);
    	}
    	
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
    
    public int getLocalStartIndex()
    {
        int localStart = getLimitedRange().getLocalMin();
        if(cachedLocalStartIndex_ != null && cachedLocalStart_ != null && cachedLocalStart_ == localStart)
            return cachedLocalStartIndex_;
        cachedLocalStart_ = localStart;
        Distance d = new Distance(localStart,localStart,0);
        cachedLocalStartIndex_ = find(d);
        return cachedLocalStartIndex_;
    }
    
    public int getLocalEndIndex()
    {
        int localEnd = getLimitedRange().getLocalMax();
        if(cachedLocalEndIndex_ != null && cachedLocalEnd_ != null && cachedLocalEnd_ == localEnd)
            return cachedLocalEndIndex_;
        cachedLocalEnd_ = localEnd;
        Distance d = new Distance(localEnd,localEnd,0);
        cachedLocalEndIndex_ = find(d);
        return cachedLocalEndIndex_;
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
        return (int) Math.round(Math.log10(getAdjustedMaxRepeatSize()));
    }
    
    public double getDoubleLogMaxSize()
    {
        return Math.log10(getAdjustedMaxRepeatSize());
    }

    public void setSelectedIndex(Integer index)
    {
    	if(index != null && (index < 0 || index >= distances_.size()))
    	{
    		logger_.debug("Not setting index!");
    		
    		return;
    	}
        selectedIndex_ = index;
        
    }
    
    public Integer getSelectedIndex()
    {
        return selectedIndex_;
    }

	public void setDrawType(DrawType drawType) 
	{
		drawType_ = drawType;
	}
	
	public DrawType getDrawType()
	{
		return drawType_;
	}

	public Distance getSelectedDistance()
	{
		if(selectedIndex_ == null)
			return null;
		return distances_.get(selectedIndex_);
	}

	public boolean withinMaxFunctioningArea(Component component) {
		return getLimitedRange().withinMaxFunctioningArea(component);
	}
}
