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
    private GraphPanelView      containingPanelView_;
    private final DistanceList  distances_;
    
    public RepeatClickListener(DistanceList distances)
    {
        distances_ = distances;
    }
    
    public void install(final GraphPanelView panelView)
    {
        containingPanelView_ = panelView;
        containingPanelView_.addMouseListener(this);
    }
    
    public void mouseClicked(MouseEvent e)
    {
        if (distances_ == null || containingPanelView_ == null)
            return;
        int adjustedHeight = containingPanelView_.getHeight() - containingPanelView_.MARGIN;
        int adjustedWidth = containingPanelView_.getWidth() - containingPanelView_.MARGIN;
        
        // skip click if it isn't inside the graph.
        if (distances_.isEmpty() || e.getY() > adjustedHeight || e.getX() < containingPanelView_.MARGIN)
            return;
        
        int rangeStart = distances_.getLimitedRange().getLocalMin();
        
        // Reverses the scale
        double xUnScale = (double) distances_.getLimitedRange().getLocal().getSize() / adjustedWidth;
        
        int actualPoint = (int) ((e.getX() - containingPanelView_.MARGIN) * xUnScale) + rangeStart;
        
        Distance correspondingDist = null;
        for (int i = 0; i < distances_.getSize(); i++)
        {
            int start = distances_.get(i).getMin();
            int end = distances_.get(i).getMax();
            if (actualPoint >= start && actualPoint <= end)
            {
                correspondingDist = distances_.get(i);
                break;
            }
            
        }
        
        if (correspondingDist == null)
            return;
        
        String alignment = JdbcTandemDao.getInstance().getAlignmentByDistance(correspondingDist);
        JComponent currentAlignmentArea = SwingUtil.createStringTextArea("Repeat:\n" + correspondingDist + "\nAlignment:\n" + alignment);
        
        JOptionPane.showMessageDialog(null, currentAlignmentArea);
    }
}
