/*******************************************************
 * Listens for clicks on a graphPane and pops up info.
 *********************************************************/

package edu.cuny.brooklyn.tandem.controller.graph.listener;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.apache.log4j.Logger;

import edu.cuny.brooklyn.tandem.helper.SwingUtil;
import edu.cuny.brooklyn.tandem.model.Distance;
import edu.cuny.brooklyn.tandem.model.DistanceInformation;
import edu.cuny.brooklyn.tandem.model.DistanceList;
import edu.cuny.brooklyn.tandem.model.JdbcTandemDao;
import edu.cuny.brooklyn.tandem.view.GraphPanelView;

public class RepeatClickListener extends MouseAdapter
{
    private static final Logger logger_ = Logger.getLogger(RepeatClickListener.class);
    private static final int MOTION_RADIUS = 20;
    private GraphPanelView containingPanelView_;
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
        containingPanelView_ = panelView;
        containingPanelView_.addMouseListener(this);
        containingPanelView_.addMouseMotionListener(this);
        logger_.debug("I am installed!");
    }

    @Override 
    public void mouseMoved(MouseEvent e)
    {
        if (distances_ == null || containingPanelView_ == null)
            return;

        if(previousX == null || Math.abs(previousX - e.getX()) < MOTION_RADIUS)
        {
            previousX = e.getX();
            return;
        }


        int adjustedHeight = containingPanelView_.getHeight() - containingPanelView_.MARGIN;
        int adjustedWidth = containingPanelView_.getWidth() - containingPanelView_.MARGIN;

        // skip click if it isn't inside the graph.
        if (distances_.isEmpty() || e.getY() > adjustedHeight || e.getX() < containingPanelView_.MARGIN)
            return;

        // Skip click if local range is too large to discern between individual repeats
        if(distances_.withinMaxFunctioningArea(containingPanelView_))
        {   
            distances_.setSelectedIndex(null);
            mainPanel_.setToolTipText(null);  // Clear the tool tip since listener is not currently active
            return;
        }

        int rangeStart = distances_.getLimitedRange().getLocalMin();

        // Reverses the scale
        double xUnScale = (double) distances_.getLimitedRange().getLocal().getSize() / adjustedWidth;

        int actualPoint = (int) ((e.getX() - containingPanelView_.MARGIN) * xUnScale) + rangeStart;

        Distance correspondingDistance = null;
        Integer correspondingIndex = null;
        for (int i = distances_.getLocalStartIndex(); i <= distances_.getLocalEndIndex(); i++)
        {
            int start = distances_.get(i).getMin();
            int end = distances_.get(i).getMax();
            if (actualPoint >= start && actualPoint <= end)
            {
                correspondingDistance = distances_.get(i);
                correspondingIndex = i;
            }            
        }

        if (correspondingDistance == null)
            return;
        distances_.setSelectedIndex(correspondingIndex);
        containingPanelView_.repaint();

        DistanceInformation distanceInformation = correspondingDistance.getDistanceInformation();

        String tipText = "Period Size: " + distanceInformation.getPeriod() + ", Errors: " + distanceInformation.getErrors();
        mainPanel_.setToolTipText(tipText);

    }

    @Override 
    public void mouseClicked(MouseEvent e)
    {
    	repeatListenerDelegator_.showAlignmentTable();
    }

   
}
