/****************************************************************************************
 *
 * TriangleGraphImage.java
 *
 * Constructs a BufferedImage of Distances using a DistanceList and horizontal and vertical
 * Ranges.
 * Distances are drawn as triangles bases depicting starting and ending points, while
 * while the peeks depict the range of the Distance.
 * Usage:
 * TriangelGraphImage tgi = new TriangleGraphImage(List<Distance>,imageWidth,imageHeight);
 * Image image = tgi.getGraphImage(startPoint,endPoint,maxHeight);
 *
 *  //Now you can simply draw the image:
 *  g.drawImage(image,xPos,yPos,null);
 *
 *
 * Author:
 * Ramin Rakhamimov
 * ramin32@gmail.com
 * http://www.ramrak.net
 ****************************************************************************************/

package edu.cuny.brooklyn.tandem.helper;

import edu.cuny.brooklyn.tandem.model.Distance;
import edu.cuny.brooklyn.tandem.model.DistanceList;
import edu.cuny.brooklyn.tandem.model.Range;

import java.awt.*;
import java.awt.image.BufferedImage;

public class TrapezoidGraphImage
{

    private BufferedImage grid_;
    private final DistanceList distanceList_;
    private final static int LEFT_SIDE_SLOPE = 2;
    private final static int RIGHT_SIDE_SLOPE = -2;
    
    public TrapezoidGraphImage(DistanceList distanceList, int width, int height)
    {
        distanceList_ = distanceList;
        if (width <= 0) width = 1;
        if (height <= 0) height = 1;
        grid_ = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

    }

    public Image getGraphImage(Range range, int height)
    {

        double xScale = (double) range.getSize() / grid_.getWidth();
        double yScale = (double) height / grid_.getHeight();

        Graphics graphics = grid_.getGraphics();

//        distanceList_.setSelected(distanceList_.get(0));
        for (int i = 0; i < distanceList_.size(); i++)
            drawTrapezoid(graphics, distanceList_.get(i), range.getMin(), xScale, yScale);

//        Distance selectedDistance = distanceList_.getSelected();
//        if (selectedDistance != null) fillTriangle(graphics, selectedDistance, range.getMin(), xScale, yScale);
        return grid_;
    }

    private void drawTrapezoid(Graphics g, Distance distance, int startPoint, double xScale, double yScale)
    {

    	double log10y = Math.log10(distance.getSize());
    	// Compute the base x points
        int x1 = translateXPoint(distance.getMin(), startPoint, xScale);
        int x4 = translateXPoint(distance.getMax(), startPoint, xScale);
        
        // Compute the top x points using base points
        int x2 = translateXPoint(leftTrapezoidPoint(log10y, distance.getMin()), startPoint, xScale);
        int x3 = translateXPoint(rightTrapezoidPoint(log10y, distance.getMax()), startPoint, xScale);

        // Base points are 0, top points are log10y
        int y1 = translateYPoint(0, yScale, grid_.getHeight());
        int y2 = translateYPoint(log10y, yScale, grid_.getHeight());
        int y3 = translateYPoint(log10y, yScale, grid_.getHeight());
        int y4 = y1;

        g.setColor(distance.getColor());
        g.drawPolygon(new int[]{x1, x2, x3, x4}, new int[]{y1, y2, y3, y4}, 4);
    }

    public static int translateXPoint(int x, int startPoint, double xScale)
    {
        return (int) ((x - startPoint) / xScale);
    }

    public static int translateYPoint(double y, double yScale, int gridHeight)
    {
        return gridHeight - (int) (y / yScale);
    }
    
    // Using equation of the line of the side of the triangle 
    // plug in log10y to get x
    public static int leftTrapezoidPoint(double log10y,double startPoint)
    {
    	return (int) ( (log10y+(LEFT_SIDE_SLOPE*startPoint))/LEFT_SIDE_SLOPE );	
    }
    
    
    // Same as above.
    public static int rightTrapezoidPoint(double log10y,double endPoint)
    {
    	return (int) ((log10y+(RIGHT_SIDE_SLOPE*endPoint))/RIGHT_SIDE_SLOPE );	
    }
}
