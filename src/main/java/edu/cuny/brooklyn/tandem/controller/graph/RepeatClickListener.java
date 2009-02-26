/*******************************************************
 * Listens for clicks on a graphPane and pops up info.
 *********************************************************/

package edu.cuny.brooklyn.tandem.controller.graph;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.JToolTip;

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
    private static final int MAX_FUNCTIONING_SIZE = 100;
    private static final int MOTION_RADIUS = 20;
    private GraphPanelView containingPanelView_;
    private final DistanceList distances_;
    private Integer previousX;
    private final JFrame frame_;
    private final JPanel mainPanel_;
    
    private static final int TABLE = 0;
    private static final int MATCH = 1;
	private static final int TABLE_HEIGHT = 500;
	private static final int TABLE_WIDTH = 500;
	private static final int WIDTH_SUPPLEMENT = 5;
    
    public RepeatClickListener(DistanceList distances, JFrame frame, JPanel mainPanel)
    {
        distances_ = distances;
        frame_ = frame;
        mainPanel_ = mainPanel;
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
        if(distances_.getLimitedRange().getLocalSize() > (containingPanelView_.getWidth() * MAX_FUNCTIONING_SIZE))
        {   
        	distances_.setSelectedDistance(null);
        	mainPanel_.setToolTipText(null);  // Clear the tool tip since listener is not currently active
        	return;
        }
        
        int rangeStart = distances_.getLimitedRange().getLocalMin();
        
        // Reverses the scale
        double xUnScale = (double) distances_.getLimitedRange().getLocal().getSize() / adjustedWidth;
        
        int actualPoint = (int) ((e.getX() - containingPanelView_.MARGIN) * xUnScale) + rangeStart;
        
        Distance correspondingDist = null;
        for (int i = distances_.getLocalStartIndex(); i <= distances_.getLocalEndIndex(); i++)
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
        
        DistanceInformation distanceInformation = correspondingDist.getDistanceInformation();

        String tipText = "Period Size: " + distanceInformation.getPeriod() + ", Errors: " + distanceInformation.getErrors();
        mainPanel_.setToolTipText(tipText);
        
    }
    
    @Override 
    public void mouseClicked(MouseEvent e)
    {
        Distance selectedDistance = distances_.getSelectedDistance();
        if(selectedDistance == null)
        	return;
      
        Object[] alignmentTableAndMaxMatch = getAlignmentTableAndMaxMatch(selectedDistance);
        JTable alignmentTable = (JTable) alignmentTableAndMaxMatch[TABLE];
        String maxMatch = (String) alignmentTableAndMaxMatch[MATCH];
        
        alignmentTable.setFont(SwingUtil.getCourierFont());
        JScrollPane scrollPane = new JScrollPane(alignmentTable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        JDialog dialog = new JDialog();
        dialog.add(scrollPane);
        dialog.pack();
        SwingUtil.centralizeComponent(dialog, frame_);
        dialog.setTitle(selectedDistance.toString());
        dialog.setIconImage(SwingUtil.getImage("images/dna-icon.gif"));
        
        alignmentTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        int width = alignmentTable.getGraphics().getFontMetrics().stringWidth(maxMatch)  + WIDTH_SUPPLEMENT;
        alignmentTable.getColumnModel().getColumn(1).setPreferredWidth(width);
        
        int tableWidth = Math.min(TABLE_WIDTH, alignmentTable.getWidth());
        int tableHeight = Math.min(TABLE_HEIGHT, alignmentTable.getHeight());
        alignmentTable.setPreferredScrollableViewportSize(new Dimension(tableWidth, tableHeight));
        dialog.pack();
        dialog.setVisible(true);
        
    }
    
    private final Object[] getAlignmentTableAndMaxMatch(Distance selectedDistance)
    {        
        String[] headers = {"Start","Match","End"};
        String alignment = JdbcTandemDao.getInstance().getAlignmentByDistance(selectedDistance);
        String[] alignmentLines = alignment.split("\n");
        String[][] alignmentTokens = new String[alignmentLines.length][];
        String maxMatch = "";
        
        for(int i = 0; i < alignmentLines.length; i++)
        {
            alignmentTokens[i] = new String[3];
            String[] tokens =  alignmentLines[i].split("\\s+");
            if(tokens.length > 1 && tokens[1].length() > maxMatch.length())
            	maxMatch = tokens[1];
            for(int j = 0; j < tokens.length; j++)
            	alignmentTokens[i][j] = tokens[j];
            
        }
        return new Object[]{new JTable(alignmentTokens, headers), maxMatch};
    }
}
