/***********************************************************************
 * TandemGraphPanl.java Provides a JPanel with a drawable area. Uses GraphRuler,
 * LimitedRange DistanceList TriangleGraphImage and GraphRuler to construct the
 * image, on each repaint(); call. Author: Ramin Rakhamimov ramin32@gmail.com
 * http://www.ramrak.net
 ***********************************************************************/
package edu.cuny.brooklyn.tandem.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JPanel;

import edu.cuny.brooklyn.tandem.controller.graph.GraphRangeSelector;
import edu.cuny.brooklyn.tandem.controller.graph.drawer.GraphImageDrawer;
import edu.cuny.brooklyn.tandem.controller.graph.drawer.GraphRuler;
import edu.cuny.brooklyn.tandem.model.DistanceList;
import edu.cuny.brooklyn.tandem.model.DrawType;
import edu.cuny.brooklyn.tandem.model.JdbcTandemDao;
import edu.cuny.brooklyn.tandem.model.Range;

public class GraphPanelView extends JPanel
{
    public final int MARKINGS = 10;
    public final int MARGIN = 70;
    public static final int SELECTOR_HEIGHT = 20;
    private static final Font PANEL_FONT = new Font("Sans Serif", Font.BOLD, 24);
    private static final int SEQUENCE_FONT_SIZE = 10;
    private static final Font SEQUENCE_FONT = new Font("Sans Serif", Font.BOLD, SEQUENCE_FONT_SIZE);
    private static final int defaultValue = 5;
    
    private final DistanceList distances_;
    private GraphImageDrawer graphImage_;
    private GraphRuler graphRuler_;
    private GraphRangeSelector graphicalRangeSelector_;
    private final Runnable runnable_;
    
    
    public GraphPanelView(DistanceList rl, Runnable runnable, JFrame frame)
    {
        runnable_ = runnable;
        distances_ = rl;
        graphRuler_ = new GraphRuler(this, MARGIN, MARKINGS);
        graphRuler_.setXAxisTickSize(SELECTOR_HEIGHT);
        
        graphicalRangeSelector_ = new GraphRangeSelector(this, runnable, distances_.getLimitedRange());
        setLayout(new BorderLayout());
        
    }
    
    public GraphRangeSelector getGraphicalRangeSelector()
    {
        return graphicalRangeSelector_;
    }
    
    public void paintComponent(Graphics g)
    {
        g.setColor(Color.black);
        g.fillRect(0, 0, getWidth(), getHeight());
        
        g.setColor(Color.gray);
        
        if (distances_.isEmpty())
        {
            graphRuler_.drawLines(g);
            g.setFont(PANEL_FONT);
            g.drawString("Please choose an input source.", getWidth() / 3, getHeight() / 2);
        }
        else
        {
            // Get the actual graph image and draw it.
            graphImage_ = new GraphImageDrawer(distances_, getWidth() - MARGIN, getHeight() - MARGIN);
            Image img = graphImage_.getGraphImage(distances_.getLimitedRange().getLocal(), distances_.getAdjustedMaxRepeatSize());
            g.drawImage(img, MARGIN + 1, 0, null);
            
            // draw the ruler
            g.setColor(Color.gray);
            boolean isLogGraph = distances_.getDrawType() == DrawType.TRAPEZOID;
            graphRuler_.drawRuler(g, distances_.getLimitedRange().getLocal(), distances_.getAdjustedMaxRepeatSize(), isLogGraph);
            
            // Get the range selector and draw it
            int x = MARGIN;
            int y = getHeight() - MARGIN + 1;
           
            drawSequence(g, x, y);
            
            Point point = new Point(x, y);
            Dimension dimension = new Dimension(getWidth() - MARGIN - 1, SELECTOR_HEIGHT);
            
            graphicalRangeSelector_.paintComponent(g, point, dimension);
            
        }
    }
    
    private void drawSequence(Graphics g, int x, int y)
    {
        Range localRange = distances_.getLimitedRange().getLocal();
        long sequenceStringWidth = ((long) localRange.getSize());
        if(sequenceStringWidth > getWidth())
            return;
        
        g.setFont(SEQUENCE_FONT);  
        String sequenceString = JdbcTandemDao.getInstance().getInputString(distances_.getChromosome(), localRange.getMin(), localRange.getSize());
        
        g.drawString(sequenceString, x, y + SEQUENCE_FONT_SIZE);
        
    }
    
    public int getAdjustedHeight()
    {
    	return getHeight() - MARGIN;
    }
    public int getAdjustedWidth()
    {
    	return getWidth() - MARGIN;
    }
}


