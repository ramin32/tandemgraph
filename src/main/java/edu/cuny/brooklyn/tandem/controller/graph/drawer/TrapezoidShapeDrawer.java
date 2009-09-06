package edu.cuny.brooklyn.tandem.controller.graph.drawer;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import edu.cuny.brooklyn.tandem.model.Distance;

public class TrapezoidShapeDrawer extends ShapeDrawer
{
  private static final double LEFT_SIDE_SLOPE = 1.0D;
  private static final double RIGHT_SIDE_SLOPE = -1.0D;
  private final BufferedImage grid_;

  public TrapezoidShapeDrawer(BufferedImage bufferedImage)
  {
    this.grid_ = bufferedImage;
  }

  public void drawShape(Graphics g, Distance distance, double startPoint, double xScale, double yScale, boolean fill)
  {
    double y3And4 = distance.getAdjustedSize();

    double x1 = translateXPoint(distance.getMin(), startPoint, xScale);
    double x4 = translateXPoint(distance.getMax(), startPoint, xScale);

    double x2 = translateXPoint(leftTrapezoidPoint(y3And4, distance.getMin()), startPoint, xScale);
    double x3 = translateXPoint(rightTrapezoidPoint(y3And4, distance.getMax()), startPoint, xScale);

    double y1 = translateYPoint(0.0D, yScale, this.grid_.getHeight());
    double y2 = translateYPoint(y3And4, yScale, this.grid_.getHeight());
    double y3 = translateYPoint(y3And4, yScale, this.grid_.getHeight());
    double y4 = y1;

    g.setColor(distance.getColor());
    if (fill)
      g.fillPolygon(new int[] { (int)Math.round(x1), (int)Math.round(x2), (int)Math.round(x3), (int)Math.round(x4) }, new int[] { (int)Math.round(y1), (int)Math.round(y2), (int)Math.round(y3), (int)Math.round(y4) }, 4);
    else
      g.drawPolygon(new int[] { (int)Math.round(x1), (int)Math.round(x2), (int)Math.round(x3), (int)Math.round(x4) }, new int[] { (int)Math.round(y1), (int)Math.round(y2), (int)Math.round(y3), (int)Math.round(y4) }, 4);
  }

  public static double leftTrapezoidPoint(double log10y, double startPoint)
  {
    return ((log10y + 1.0D * startPoint) / 1.0D);
  }

  public static double rightTrapezoidPoint(double log10y, double endPoint)
  {
    return ((log10y + -1.0D * endPoint) / -1.0D);
  }
}