/*******************************************************
 * Listens for clicks on a graphPane and pops up info.
 *********************************************************/

package edu.cuny.brooklyn.tandem.controller.graph;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import edu.cuny.brooklyn.tandem.helper.SwingUtil;
import edu.cuny.brooklyn.tandem.model.Distance;
import edu.cuny.brooklyn.tandem.model.DistanceList;
import edu.cuny.brooklyn.tandem.model.JdbcTandemDao;
import edu.cuny.brooklyn.tandem.view.GraphPanelView;

public class RepeatClickListener extends MouseAdapter
{
    private static final Logger logger_ = Logger.getLogger(RepeatClickListener.class);
    private static final int MAX_FUNCTIONING_SIZE = 20;
    private GraphPanelView containingPanelView_;
    private final DistanceList distances_;
    
    
    public RepeatClickListener(DistanceList distances)
    {
        distances_ = distances;
    }
    
    public void install(final GraphPanelView panelView)
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
        int adjustedHeight = containingPanelView_.getHeight() - containingPanelView_.MARGIN;
        int adjustedWidth = containingPanelView_.getWidth() - containingPanelView_.MARGIN;
        
        // skip click if it isn't inside the graph.
        if (distances_.isEmpty() || e.getY() > adjustedHeight || e.getX() < containingPanelView_.MARGIN)
            return;
        
        // Skip click if local range is too large to discern between individual repeats
        if(distances_.getLimitedRange().getLocalSize() > (containingPanelView_.getWidth() * MAX_FUNCTIONING_SIZE))
            return;
        
        int rangeStart = distances_.getLimitedRange().getLocalMin();
        
        // Reverses the scale
        double xUnScale = (double) distances_.getLimitedRange().getLocal().getSize() / adjustedWidth;
        
        int actualPoint = (int) ((e.getX() - containingPanelView_.MARGIN) * xUnScale) + rangeStart;
        
        Distance correspondingDist = null;
        for (int i = distances_.getStartIndex(); i <= distances_.getEndIndex(); i++)
        {
            int start = distances_.get(i).getMin();
            int end = distances_.get(i).getMax();
            if (actualPoint >= start && actualPoint <= end)
            {
                correspondingDist = distances_.get(i);
            }            
        }
        
        if (correspondingDist == null)
            return;
        distances_.setSelectedDistance(correspondingDist);
        containingPanelView_.repaint();
        
    }
    
    @Override 
    public void mouseClicked(MouseEvent e)
    {
        Distance selectedDistance = distances_.getSelectedDistance();
        if(selectedDistance == null)
            return;
        
        String alignment = JdbcTandemDao.getInstance().getAlignmentByDistance(selectedDistance);
        JComponent currentAlignmentArea = SwingUtil.createStringTextArea("Repeat:\n" + selectedDistance + "\nAlignment:\n" + alignment);
        
        JOptionPane.showMessageDialog(null, currentAlignmentArea);
    }
}
