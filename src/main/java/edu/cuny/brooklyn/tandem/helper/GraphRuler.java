/***************************************************************
 * GraphRuler.java Draws a ruler in a JPanel with a horizontal and vertical
 * range. Author: Ramin Rakhamimov ramin32@gmail.com http://www.ramrak.net
 **************************************************************/
package edu.cuny.brooklyn.tandem.helper;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

import edu.cuny.brooklyn.tandem.model.Range;

public class GraphRuler
{
    private final JPanel    containingPanel_;
    private final int       MARGIN, MARKINGS;
    
    public static final int DEFAULT_TICK_SIZE = 10;
    
    private int             xAxisTickSize;
    private int             yAxisTickSize;
    
    public GraphRuler(JPanel panel, int margin, int markings)
    {
        if (panel == null)
            throw new IllegalArgumentException("Panel can't be null.");
        
        containingPanel_ = panel;
        MARGIN = margin;
        MARKINGS = markings;
        
        xAxisTickSize = yAxisTickSize = xAxisTickSize;
    }
    
    public GraphRuler(JPanel panel)
    {
        containingPanel_ = panel;
        MARGIN = 70;
        MARKINGS = 10;
    }
    
    public void drawLines(Graphics g)
    {
        g.drawLine(MARGIN, 0, MARGIN, containingPanel_.getHeight());
        int bottomMarginLocation = containingPanel_.getHeight() - MARGIN;
        g.drawLine(0, bottomMarginLocation, containingPanel_.getWidth(), bottomMarginLocation);
        
    }
    
    public void drawMarkings(Graphics g, Range horRange, Range verRange)
    {
        int horizontalScale = horRange.getSize() / MARKINGS;
        int verticalScale = verRange.getSize() / MARKINGS;
        
        int bottomMarginPos = containingPanel_.getHeight() - MARGIN;
        
        int xScale = (containingPanel_.getWidth() - MARGIN) / MARKINGS;
        int yScale = (containingPanel_.getHeight() - MARGIN) / MARKINGS;
        
        // Draw horizontal markings and labels5
        for (int mark = 1, horLabel = horRange.getMin(); mark < MARKINGS; mark++)
        {
            String label = mark * horizontalScale + horRange.getMin() + "";
            label = commanize(label);
            
            int xPos = MARGIN + mark * xScale;
            g.drawLine(xPos, bottomMarginPos, xPos, bottomMarginPos + xAxisTickSize);
            
            g.drawString(label, xPos - 3, bottomMarginPos + xAxisTickSize + g.getFont().getSize() + 1);
        }
        
        // Draw vertical markings and labels
        for (int mark = 1; mark < MARKINGS; mark++)
        {
            String label = ((MARKINGS - mark) * verticalScale + verRange.getMin()) + "";
            label = commanize(label);
            
            int yPos = mark * yScale;
            g.drawLine(MARGIN - yAxisTickSize, yPos, MARGIN, yPos);
            g.drawString(label, 0, yPos + 5);
        }
        
    }
    
    public static String commanize(String input)
    {
        StringBuilder output = new StringBuilder(input);
        for (int i = output.length() - 3; i > 0; i -= 3)
            output.insert(i, ",");
        return output.toString();
        
    }
    
    public void drawRuler(Graphics g, Range horRange, Range verRange)
    {
        drawLines(g);
        drawMarkings(g, horRange, verRange);
    }
    
    public void drawRuler(Graphics g, Range horRange, int maxHeight)
    {
        drawRuler(g, horRange, new Range(0, maxHeight));
    }
    
    public void setXAxisTickSize(int n)
    {
        xAxisTickSize = n;
    }
    
    public void setYAxisTickSize(int n)
    {
        yAxisTickSize = n;
    }
    
    public static void main(String[] args)
    {
        JFrame frame = new JFrame("GraphRuler Window");
        JPanel panel = new JPanel()
        {
            public void paintComponent(Graphics g)
            {
                GraphRuler ruler = new GraphRuler(this, 50, 10);
                g.setColor(Color.black);
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(Color.cyan);
                ruler.drawRuler(g, new Range(111, 999), new Range(20, 30));
            }
            
        };
        panel.setPreferredSize(new Dimension(500, 500));
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
        
    }
    
}
