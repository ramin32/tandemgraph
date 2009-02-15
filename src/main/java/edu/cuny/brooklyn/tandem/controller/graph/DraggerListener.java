package edu.cuny.brooklyn.tandem.controller.graph;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

import edu.cuny.brooklyn.tandem.helper.SwingUtil;

public class DraggerListener extends MouseInputAdapter
{
    
    private Integer      x1;
    private Integer      x2;
    
    private int          xMin;
    private int          xMax;
    private int          yMin;
    private int          yMax;
    
    private boolean      mouseIsPressed = false;
    private final JPanel panel_;
    
    public DraggerListener(JPanel panel)
    {
        panel_ = panel;
    }
    
    @Override public void mouseDragged(MouseEvent e)
    {
        if (mouseIsPressed)
        {
            x2 = e.getX();
            printPoints();
            panel_.repaint();
        }
        
    }
    
    @Override public void mousePressed(MouseEvent e)
    {
        
        x1 = x2 = null;
        x1 = e.getX();
        mouseIsPressed = true;
        printPoints();
    }
    
    @Override public void mouseReleased(MouseEvent e)
    {
        x2 = e.getX();
        mouseIsPressed = false;
        printPoints();
        panel_.repaint();
        
    }
    
    public int getXStart()
    {
        return Math.min(x1, x2);
    }
    
    public int getXEnd()
    {
        return Math.max(x1, x2);
    }
    
    public void printPoints()
    {
        System.out.println("X1 = " + x1 + ", X2 = " + x2);
    }
    
    public static void main(String[] args)
    {
        SwingUtil.createAndShowTestFrame(new TestPanel());
    }
    
    static class TestPanel extends JPanel
    {
        private DraggerListener listener_;
        
        public TestPanel()
        {
            listener_ = new DraggerListener(this);
            addMouseListener(listener_);
            addMouseMotionListener(listener_);
        }
        
        @Override protected void paintComponent(Graphics g)
        {
            // TODO Auto-generated method stub
            super.paintComponent(g);
            
            try
            {
                int start = listener_.getXStart();
                int end = listener_.getXEnd();
                g.fillRect(start, 0, end - start, getHeight());
            }
            catch (Exception e)
            {}
        }
        
    }
    
}
