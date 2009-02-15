/**
 * @(#)GraphScrollBar.java 1.00 Copyright (c) 2008 by FJA Finansys, Inc., 512
 *                         Seventh Avenue, 15th Floor, New York, N.Y., 10018,
 *                         U.S.A. All rights reserved. This software is the
 *                         confidential and proprietary information of FJA
 *                         Finansys, Inc. You shall not disclose such
 *                         Confidential Information and shall use it only in
 *                         accordance with the terms of the license agreement
 *                         you entered into with FJA Finansys.
 * @version 1.00, Jul 28, 2008
 * @author Ramin Rakhamimov
 */

package edu.cuny.brooklyn.tandem.view;

import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import javax.swing.JScrollBar;

import edu.cuny.brooklyn.tandem.controller.widgets.GraphShifterController;

public class GraphScrollBar extends JScrollBar implements AdjustmentListener
{
    private final GraphShifterController graphShifterController_;
    
    private static final int             interval = 100 / 7;     // 6 Intervals
    // plus 1 for
    private int                          center_;
    
    // neutral
    
    public GraphScrollBar(GraphShifterController graphShifterController)
    {
        graphShifterController_ = graphShifterController;
        setOrientation(HORIZONTAL);
        centralize();
        
    }
    
    private final void centralize()
    {
        removeAdjustmentListener(this);
        setValue(50 - getBlockIncrement() / 2);
        center_ = getValue();
        addAdjustmentListener(this);
    }
    
    @Override public void adjustmentValueChanged(AdjustmentEvent ae)
    {
        // if (ae.getValueIsAdjusting() == true)
        // ; // Skip while being dragged
        if (ae.getValue() < center_)
            graphShifterController_.shiftRight();
        else if (ae.getValue() > center_)
            graphShifterController_.shiftLeft();
        
        centralize();
        
    }
}
