/*********************************************************************************
 * LimitedRange.java ADT for a limitedRange with a local-range which is always
 * within a global-range Author: Ramin Rakhamimov ramin32@gmail.com
 * http://www.ramrak.net
 ********************************************************************************/

package edu.cuny.brooklyn.tandem.model;

import java.awt.Component;

import org.apache.log4j.Logger;

public class LimitedRange
{
	private static final Logger logger_ = Logger.getLogger(LimitedRange.class);
    private static final int MAX_FUNCTIONING_SIZE = 100;
    private Range local_;
    private Range global_;
    private Integer minLocal_;
    
    public LimitedRange()
    {}
    
    public LimitedRange(Range local, Range global) throws IllegalArgumentException
    {
        if (!local.isWithin(global))
            throw new IllegalArgumentException("Local range is not withing global range.");
        global_ = global;
        local_ = local;
    }
    
    public LimitedRange(int localStart, int localEnd, int globalStart, int globalEnd)
    {
        this(new Range(localStart, localEnd), new Range(globalStart, globalEnd));
        
    }
    
    public LimitedRange(Range range)
    {
        global_ = local_ = range;
    }
    
    public void setMinLocalRange(int min)
    {
        minLocal_ = min;
    }
    
    public LimitedRange(int start, int end)
    {
        this(new Range(start, end));
    }
    
    public Range getGlobal()
    {
        return global_;
    }
    
    public void setGlobal(Range r)
    {
        global_ = local_ = r;
    }
    
    public void setGlobal(int start, int end)
    {
        setGlobal(new Range(start, end));
    }
    
    public Range getLocal()
    {
        return local_;
    }
    
    public void setLocal(Range r) throws IllegalArgumentException
    {
        if (!r.isWithin(global_))
        {
            throw new IllegalArgumentException("Range is not within the global range.");
        }
        
        // TODO uncomment
        // // Return if localRange about to be set is less than minLocal_
        // if (minLocal_ != null)
        // if (r.getSize() <= minLocal_) throw new
        // IllegalArgumentException("Range is below minimal allowable range!");
        
        local_ = r;
    }
    
    public void setLocal(int start, int end)
    {
        setLocal(new Range(start, end));
    }
    
    public void setGlobalMin(int min)
    {
        setGlobal(min, global_.getMax());
    }
    
    public int getGlobalMin()
    {
        return global_.getMin();
    }
    
    public void setGlobalMax(int max)
    {
        setGlobal(global_.getMin(), max);
    }
    
    public int getGlobalMax()
    {
        return global_.getMax();
    }
    
    public void setLocalMin(int min)
    {
        setLocal(min, local_.getMax());
    }
    
    public int getLocalMin()
    {
        return local_.getMin();
    }
    
    public void setLocalMax(int max)
    {
        setLocal(local_.getMin(), max);
    }
    
    public int getLocalMax()
    {
        return local_.getMax();
    }
    
    public int getLocalRangePercentage()
    {
        double perc = (getLocalSize() * 100.0) / getGlobalSize();
        return (int) perc;
    }
    
    public void setLocalRangePercentage(double value)
    {
        if (!isInitialized())
        {
            return;
            
        }
        int increment = (int) ((value * getGlobalSize()) / 100.0);
        increment /= 2;
        int start = getGlobalMidPoint() - increment;
        int end = getGlobalMidPoint() + increment;
        try
        {
            setLocal(start, end);
        }
        catch (IllegalArgumentException e)
        {   

        }
    }
    
    public void reset()
    {
        local_ = global_;
    }
    
    public void clear()
    {
        local_ = global_ = null;
    }
    
    public boolean isSet()
    {
        return global_ != null;
    }
    
    public String toString()
    {
        return "local: " + local_.toString() + "\nglobal:" + global_.toString();
    }
    
    public boolean isInitialized()
    {
        return global_ != null;
        
    }
    
    public boolean globalIsLocal()
    {
        return local_ == global_;
    }
    
    public int getGlobalMidPoint()
    {
        return global_.getMidPoint();
    }
    
    public int getLocalMidPoint()
    {
        return local_.getMidPoint();
    }
    
    public int getGlobalSize()
    {
        return global_.getSize();
    }
    
    public int getLocalSize()
    {
        return local_.getSize();
    }
    
    public boolean withinMaxFunctioningArea(Component component)
    {
    	if(local_ == null)
    		return false;
    	return local_.getSize() > (component.getWidth() * MAX_FUNCTIONING_SIZE);
    }
    
    public static void main(String[] args)
    {
        LimitedRange lr = new LimitedRange(55, 53424);
        lr.setLocalRangePercentage(50);
        logger_.debug(lr + "\nLocal" + lr.getLocalRangePercentage());
        
    }
}
