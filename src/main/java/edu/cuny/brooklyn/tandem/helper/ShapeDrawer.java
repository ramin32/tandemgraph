package edu.cuny.brooklyn.tandem.helper;

import java.awt.Graphics;

import edu.cuny.brooklyn.tandem.model.Distance;

public abstract class ShapeDrawer 
{
	public abstract void drawShape(Graphics g, Distance distance,
            double startPoint, double xScale, double yScale, boolean fill);
	
	public static double translateXPoint(double x, double startPoint,
            double xScale)
    {
        return (x - startPoint) / xScale;
    }
    
    public static double translateYPoint(double y, double yScale,
            double gridHeight)
    {
        return gridHeight - (y / yScale);
    }

}
