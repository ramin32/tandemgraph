package edu.cuny.brooklyn.tandem.controller.widgets;

import edu.cuny.brooklyn.tandem.model.LimitedRange;
import edu.cuny.brooklyn.tandem.view.widgets.TextRangeSelectorView;

public class ZoomSliderController
{
    private final LimitedRange          limitedRange_;
    private final Runnable              runnable_;
    private final TextRangeSelectorView textRangeSelectorView_;
    
    public ZoomSliderController(LimitedRange limitedRange, Runnable runnable, TextRangeSelectorView textRangeSelectorView)
    {
        limitedRange_ = limitedRange;
        runnable_ = runnable;
        textRangeSelectorView_ = textRangeSelectorView;
    }
    
    public LimitedRange getLimitedRange()
    {
        return limitedRange_;
    }
    
    public void setZoomPercentage(int percentage)
    {
        if (!limitedRange_.isInitialized())
        {
            return;
        }
        limitedRange_.setLocalRangePercentage(percentage);
        textRangeSelectorView_.setFields();
        runnable_.run();
        
    }
}
