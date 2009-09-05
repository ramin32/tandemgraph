package edu.cuny.brooklyn.tandem.controller.graph.drawer;

import edu.cuny.brooklyn.tandem.model.Distance;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class TriangleShapeDrawer extends ShapeDrawer
{
  public static final int DEFAULT_THICKNESS = 3;
  public static final int TRIANLGE_POINTS = 3;
  private final BufferedImage grid_;

  public TriangleShapeDrawer(BufferedImage bufferedImage)
  {
    this.grid_ = bufferedImage;
  }

  public void drawShape(Graphics g, Distance distance, double startPoint, double xScale, double yScale, boolean fill)
  {
    double x1 = translateXPoint(distance.getMin(), startPoint, xScale);
    double x2 = translateXPoint(distance.getMidPoint(), startPoint, xScale);
    double x3 = translateXPoint(distance.getMax(), startPoint, xScale);

    double y1 = translateYPoint(0.0D, yScale, this.grid_.getHeight());
    double y2 = translateYPoint(distance.getSize(), yScale, this.grid_.getHeight());
    double y3 = y1;

    g.setColor(distance.getColor());

    drawTriangle(g, new int[] { (int)x1, (int)x2, (int)x3 }, 
      new int[] { (int)y1, (int)y2, (int)y3 }, 3, fill);
  }

  private void drawTriangle(Graphics g, int[] xPoints, int[] yPoints, int thickness, boolean fill)
  {
    for (int i = 0; i < thickness; ++i)
    {
      if (fill)
      {
        g.fillPolygon(shrinkXPoints(xPoints, i), shrinkYPoints(yPoints, i), 3);
      }
      else
      {
        g.drawPolygon(shrinkXPoints(xPoints, i), shrinkYPoints(yPoints, i), 3);
      }
    }
  }

  private int[] shrinkXPoints(int[] points, int amount) {
    return new int[] { points[0] + amount, points[1], points[2] - amount };
  }

  private int[] shrinkYPoints(int[] points, int amount)
  {
    return new int[] { points[0] + amount, points[1] - amount, points[2] + amount };
  }
}