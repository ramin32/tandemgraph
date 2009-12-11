package edu.cuny.brooklyn.tandem.controller.graph.drawer;

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
    this.distanceList_ = distanceList;
    if (width <= 0)
      width = 1;
    if (height <= 0)
      height = 1;
    this.grid_ = new BufferedImage(width, height, 1);
  }

  public Image getGraphImage(Range range, double height)
  {
    ShapeDrawer shapeDrawer = null;

    if (DistanceList.getDrawType() == DrawType.TRAPEZOID)
    {
      shapeDrawer = new TrapezoidShapeDrawer(this.grid_);
    }
    else if (DistanceList.getDrawType() == DrawType.TRIANGLE) {
      shapeDrawer = new TriangleShapeDrawer(this.grid_);
    }
    double xScale = range.getSize() / this.grid_.getWidth();
    double yScale = height / this.grid_.getHeight();

    Graphics graphics = this.grid_.getGraphics();

    for (int i = 0; i < this.distanceList_.size(); ++i) {
      shapeDrawer.drawShape(graphics, this.distanceList_.get(i), range.getMin(), xScale, yScale, false);
    }
    Distance selectedDistance = this.distanceList_.getSelectedDistance();
    if (selectedDistance != null) {
      shapeDrawer.drawShape(graphics, selectedDistance, range.getMin(), xScale, yScale, true);
    }
    return this.grid_;
  }
}