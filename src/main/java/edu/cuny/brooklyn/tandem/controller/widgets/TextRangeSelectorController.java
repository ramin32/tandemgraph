package edu.cuny.brooklyn.tandem.controller.widgets;

import edu.cuny.brooklyn.tandem.model.LimitedRange;

public class TextRangeSelectorController
{
    private final LimitedRange limitedRange_;
    private final Runnable     runnable_;
    
    public TextRangeSelectorController(LimitedRange limitedRange, Runnable runnable)
    {
        limitedRange_ = limitedRange;
        runnable_ = runnable;
    }
    
    public void zoom(String startField, String endField)
    {
        if (!limitedRange_.isInitialized())
            return;
        try
        {
            int start = Integer.parseInt(startField);
            int end = Integer.parseInt(endField);
            limitedRange_.setLocal(start, end);
        }
        catch (NumberFormatException e)
        {
            throw new RuntimeException("Numeric data only please.");
        }
        catch (IllegalArgumentException e)
        {
            throw new RuntimeException(e.getMessage() + "\nPlease re-enter.\n" + "Range of graph is: " + limitedRange_.getGlobal());
        }
        
        runnable_.run();
    }
    
    public LimitedRange getLimitedRange()
    {
        return limitedRange_;
    }
}
