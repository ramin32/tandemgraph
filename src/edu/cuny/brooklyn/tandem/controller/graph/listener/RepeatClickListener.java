/*******************************************************
 * Listens for clicks on a graphPane and pops up info.
 *********************************************************/

package edu.cuny.brooklyn.tandem.controller.graph.listener;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import org.apache.log4j.Logger;

import edu.cuny.brooklyn.tandem.controller.graph.drawer.TriangleShapeDrawer;
import edu.cuny.brooklyn.tandem.helper.MathUtil;
import edu.cuny.brooklyn.tandem.model.Distance;
import edu.cuny.brooklyn.tandem.model.DistanceInformation;
import edu.cuny.brooklyn.tandem.model.DistanceList;
import edu.cuny.brooklyn.tandem.view.GraphPanelView;

public class RepeatClickListener extends MouseAdapter
{
    private static final Logger logger_ = Logger.getLogger(RepeatClickListener.class);
    private static final int MOTION_RADIUS = 3;
    private GraphPanelView graphPanelView_;
    private final DistanceList distances_;
    private Integer previousX;
    private final JPanel mainPanel_;
    private final RepeatListenerDelegator repeatListenerDelegator_;

    RepeatClickListener(DistanceList distances,  JPanel mainPanel,RepeatListenerDelegator delegator)
    {  
        distances_ = distances;
        mainPanel_ = mainPanel;

        repeatListenerDelegator_ = delegator;
    }

    void install(final GraphPanelView panelView)
    {
        graphPanelView_ = panelView;
        graphPanelView_.addMouseListener(this);
        graphPanelView_.addMouseMotionListener(this);
        logger_.debug("I am installed!");
    }

    @Override 
    public void mouseMoved(MouseEvent e)
    {
        if (distances_ == null || graphPanelView_ == null)
            return;

        
        // Verify motion exceeds MOTION_RADIUS
        if(previousX == null || Math.abs(previousX - e.getX()) < MOTION_RADIUS)
        {
            previousX = e.getX();
            return;
        }


        

        // skip click if it isn't inside the graph.
        if (distances_.isEmpty() || e.getY() > graphPanelView_.getAdjustedHeight() || e.getX() < graphPanelView_.MARGIN)
            return;

        // Skip click if local range is too large to discern between individual repeats
        if(distances_.withinMaxFunctioningArea(graphPanelView_))
        {   
            clearSelected();
            return;
        }

        int rangeStart = distances_.getLimitedRange().getLocalMin();

        // Reverses the scales
        double xUnScale = (double) distances_.getLimitedRange().getLocal().getSize() / graphPanelView_.getAdjustedWidth();
        double yUnScale = (double) distances_.getAdjustedMaxRepeatSize() / graphPanelView_.getAdjustedHeight();

        int actualXPoint = rangeStart + (int) Math.round((e.getX() - graphPanelView_.MARGIN) * xUnScale);
        int actualYPoint = (int) Math.round((graphPanelView_.getHeight() - e.getY() - graphPanelView_.MARGIN) * yUnScale);

        Distance correspondingDistance = null;
        Integer correspondingIndex = null;
        for (int i = distances_.getLocalStartIndex(); i <= distances_.getLocalEndIndex(); i++)
        {
            Distance distance = distances_.get(i);
            int start = distance.getMin();
            int end = distance.getMax();
            int midPoint = distance.getMidPoint();
            if (actualXPoint >= start && actualXPoint <= end)
            {
            	double yIntersect;
            	if(start < midPoint)
            		 yIntersect = MathUtil.computeYIntersect(start, 0, midPoint, distance.getAdjustedSize(), actualXPoint);
            	else
            		yIntersect = MathUtil.computeYIntersect(midPoint, distance.getAdjustedSize(), end, 0, actualXPoint);
            	
            	yIntersect = TriangleShapeDrawer.translateYPoint(yIntersect, yUnScale, graphPanelView_.getAdjustedHeight());
            	
            	if(actualYPoint < yIntersect)
            	{
            		correspondingDistance = distance;
            		correspondingIndex = i;
            	}
            }            
        }

        if (correspondingDistance == null)
        {
        	clearSelected();
        	return;
        }
        distances_.setSelectedIndex(correspondingIndex);
        //graphPanelView_.repaint();

        repeatListenerDelegator_.runUpdater();

    }
    
    private final void clearSelected()
    {
    	distances_.setSelectedIndex(null);
        mainPanel_.setToolTipText(null);  // Clear the tool tip since listener is not currently active
    }

    @Override 
    public void mouseClicked(MouseEvent e)
    {
    	repeatListenerDelegator_.showAlignmentTable();
    }

   
}
