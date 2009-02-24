package edu.cuny.brooklyn.tandem.helper;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import edu.cuny.brooklyn.tandem.model.Distance;

public class TrapezoidShapeDrawer extends ShapeDrawer
{

    private final static double LEFT_SIDE_SLOPE = 1;
    private final static double RIGHT_SIDE_SLOPE = -1;
    

	private final BufferedImage grid_;

	public TrapezoidShapeDrawer(BufferedImage bufferedImage)
	{
		grid_ = bufferedImage;
	}
    
	 public void drawShape(Graphics g, Distance distance,
	            double startPoint, double xScale, double yScale, boolean fill)
	    {
	        double log10y = Math.log10(distance.getSize());
	        // Compute the base x points
	        double x1 = translateXPoint(distance.getMin(), startPoint, xScale);
	        double x4 = translateXPoint(distance.getMax(), startPoint, xScale);
	        
	        // Compute the top x points using base points
	        double x2 = translateXPoint(leftTrapezoidPoint(log10y, distance.getMin()), startPoint, xScale);
	        double x3 = translateXPoint(rightTrapezoidPoint(log10y, distance.getMax()), startPoint, xScale);
	        
	        // Base points are 0, top points are log10y
	        double y1 = translateYPoint(0, yScale, grid_.getHeight());
	        double y2 = translateYPoint(log10y, yScale, grid_.getHeight());
	        double y3 = translateYPoint(log10y, yScale, grid_.getHeight());
	        double y4 = y1;
	        
	        g.setColor(distance.getColor());
	        if(fill)
	            g.fillPolygon(new int[] { (int) Math.round(x1), (int) Math.round(x2), (int) Math.round(x3), (int) Math.round(x4) }, new int[] { (int) Math.round(y1), (int) Math.round(y2), (int) Math.round(y3), (int) Math.round(y4) }, 4);
	        else
	            g.drawPolygon(new int[] { (int) Math.round(x1), (int) Math.round(x2), (int) Math.round(x3), (int) Math.round(x4) }, new int[] { (int) Math.round(y1), (int) Math.round(y2), (int) Math.round(y3), (int) Math.round(y4) }, 4);
	    }
	 
	// Using equation of the line of the side of the triangle
	    // plug in log10y to get x
	    public static double leftTrapezoidPoint(double log10y, double startPoint)
	    {
	        return (log10y + (LEFT_SIDE_SLOPE * startPoint)) / LEFT_SIDE_SLOPE;
	    }
	    
	    // Same as above.
	    public static double rightTrapezoidPoint(double log10y, double endPoint)
	    {
	        return (log10y + (RIGHT_SIDE_SLOPE * endPoint)) / RIGHT_SIDE_SLOPE;
	    }

}