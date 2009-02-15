/****************************************************************************************
 * TriangleGraphImage.java Constructs a BufferedImage of Distances using a
 * DistanceList and horizontal and vertical Ranges. Distances are drawn as
 * triangles bases depicting starting and ending points, while while the peeks
 * depict the range of the Distance. Usage: TriangelGraphImage tgi = new
 * TriangleGraphImage(List<Distance>,imageWidth,imageHeight); Image image =
 * tgi.getGraphImage(startPoint,endPoint,maxHeight); //Now you can simply draw
 * the image: g.drawImage(image,xPos,yPos,null); Author: Ramin Rakhamimov
 * ramin32@gmail.com http://www.ramrak.net
 ****************************************************************************************/

package edu.cuny.brooklyn.tandem.helper;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import edu.cuny.brooklyn.tandem.model.Distance;
import edu.cuny.brooklyn.tandem.model.DistanceList;
import edu.cuny.brooklyn.tandem.model.Range;

public class TrapezoidGraphImage
{
    
    private BufferedImage grid_;
    private final DistanceList distanceList_;
    private final static double LEFT_SIDE_SLOPE = 2;
    private final static double RIGHT_SIDE_SLOPE = -2;
    
    public TrapezoidGraphImage(DistanceList distanceList, int width, int height)
    {
        distanceList_ = distanceList;
        if (width <= 0)
            width = 1;
        if (height <= 0)
            height = 1;
        grid_ = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_RGB);
        
    }
    
    public Image getGraphImage(Range range, double height)
    {
        
        double xScale = (double) range.getSize() / grid_.getWidth();
        double yScale = (double) height / grid_.getHeight();
        
        Graphics graphics = grid_.getGraphics();
        
        // distanceList_.setSelected(distanceList_.get(0));
        for (int i = 0; i < distanceList_.size(); i++)
            drawTrapezoid(graphics, distanceList_.get(i), range.getMin(), xScale, yScale, false);
        
         Distance selectedDistance = distanceList_.getSelectedDistance();
         if (selectedDistance != null) 
             drawTrapezoid(graphics, selectedDistance, range.getMin(), xScale, yScale, true);
        return grid_;
    }
    
    private void drawTrapezoid(Graphics g, Distance distance,
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
