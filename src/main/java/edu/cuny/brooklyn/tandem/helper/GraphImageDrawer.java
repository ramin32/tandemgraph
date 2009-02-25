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
import edu.cuny.brooklyn.tandem.model.DrawType;
import edu.cuny.brooklyn.tandem.model.Range;

public class GraphImageDrawer
{
    
    private BufferedImage grid_;
    private final DistanceList distanceList_;
    
    public GraphImageDrawer(DistanceList distanceList, int width, int height)
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
    	
    	ShapeDrawer shapeDrawer = null;
        
        if(distanceList_.getDrawType() == DrawType.TRAPEZOID)
        {
        	shapeDrawer = new TrapezoidShapeDrawer(grid_);
        	height = Math.log10(height) + DistanceList.PY_GRAPH_SUPPLEMENT;
        }
        else if(distanceList_.getDrawType() == DrawType.TRIANGLE)
        	shapeDrawer = new TriangleShapeDrawer(grid_);
        
        double xScale = (double) range.getSize() / grid_.getWidth();
        double yScale = (double) height / grid_.getHeight();
        
        Graphics graphics = grid_.getGraphics();       
        
        
        // distanceList_.setSelected(distanceList_.get(0));
        for (int i = 0; i < distanceList_.size(); i++)
        	shapeDrawer.drawShape(graphics, distanceList_.get(i), range.getMin(), xScale, yScale, false);
                
         Distance selectedDistance = distanceList_.getSelectedDistance();
         if (selectedDistance != null) 
        	 shapeDrawer.drawShape(graphics, selectedDistance, range.getMin(), xScale, yScale, true);
         
        return grid_;
    }
    
   
    
    
    
    
}
