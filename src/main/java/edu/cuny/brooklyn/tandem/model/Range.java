/*****************************************************************************
 * Range.java ADT to represent a range object with start,end,midpoint and size.
 * Author: Ramin Rakhamimov http://www.ramrak.net ramin32@gmail.com
 ****************************************************************************/
package edu.cuny.brooklyn.tandem.model;

import org.apache.log4j.Logger;

public class Range implements Comparable<Distance>
{
	private static final Logger logger_ = Logger.getLogger(Range.class);
    private final int min_;
    private final int max_;
    private final int midPoint_;
    private final int size_;
    
    public Range()
    {
        min_ = max_ = size_ = midPoint_ = 0;
        
    }
    
    public Range(int min, int max)
    {
        if (min > max)
        {
            throw new IllegalArgumentException("Start cannot exceed max: min = " + min + ", max = " + max);
        }
        if (min < 0)
        {
            throw new IllegalArgumentException("Points can't be negative: min = " + min + ", max =  " + max);
        }
        
        min_ = min;
        max_ = max;
        size_ = max - min;
        
        midPoint_ = (max - min) / 2 + min;
    }
    
    public boolean isWithin(Range r)
    {
        if (getMin() < r.getMin() || getMax() > r.getMax())
            return false;
        return true;
    }
    
    public boolean isWithin(int start, int end)
    {
        return isWithin(new Range(start, end));
    }
    
    public boolean intersects(Range r)
    {
        if (getMin() >= r.getMin() && getMin() < r.getMax())
            return true;
        if (getMax() > r.getMin() && getMax() <= r.getMax())
            return true;
        
        return false;
    }
    
    public boolean intersects(int start, int end)
    {
        return intersects(new Range(start, end));
    }
    
    public int getMin()
    {
        return min_;
    }
    
    public int getMax()
    {
        return max_;
    }
    
    public int getSize()
    {
        return size_;
    }
    
    public int getMidPoint()
    {
        return midPoint_;
    }
    
    public boolean equals(Object o)
    {
        if (this == o)
            return true;
        if (!(o instanceof Range))
            return false;
        
        Range range = (Range) o;
        
        if (max_ != range.max_)
            return false;
        if (size_ != range.size_)
            return false;
        if (min_ != range.min_)
            return false;
        
        return true;
    }
    
    public int hashCode()
    {
        int result;
        result = min_;
        result = 31 * result + max_;
        result = 31 * result + size_;
        return result;
    }
    
    public int compareTo(Distance d)
    {
        if (getMin() == d.getMin())
            return getMax() - d.getMax();
        return getMin() - d.getMin();
    }
    
    public String toString()
    {
        return "Start: " + min_ + ", End: " + getMax() + ", Length: " + size_;
    }
    
    public static void main(String[] args)
    {
        Range d = new Range(4, 5);
        try
        {
            d = new Range(5, 2);
        }
        catch (IllegalArgumentException e)
        {
            logger_.error(e,e);
        }
        try
        {
            d = new Range(-100, 2);
        }
        catch (IllegalArgumentException e)
        {
        	logger_.error(e,e);
        }
        
        try
        {
            d = new Range(90, -1000);
        }
        catch (IllegalArgumentException e)
        {
        	logger_.error(e,e);
        }
        System.out.println(d);
        
        Range a = new Range(20, 30);
        
        (new Range(30, 40)).isWithin(new Range(0, 5));
        
        Range first = new Range(30, 90);
        System.out.println(first.intersects(0, 30));
        
        System.out.println(first.intersects(30, 101));
        System.out.println(first.intersects(90, 101));
        
        System.out.println(first.intersects(89, 101));
    }
}
