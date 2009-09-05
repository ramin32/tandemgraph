package edu.cuny.brooklyn.tandem.controller.graph.drawer;

import edu.cuny.brooklyn.tandem.model.Distance;
import java.awt.Graphics;

public abstract class ShapeDrawer
{
  public abstract void drawShape(Graphics paramGraphics, Distance paramDistance, double paramDouble1, double paramDouble2, double paramDouble3, boolean paramBoolean);

  public static double translateXPoint(double x, double startPoint, double xScale)
  {
    return ((x - startPoint) / xScale);
  }

  public static double translateYPoint(double y, double yScale, double gridHeight)
  {
    return (gridHeight - (y / yScale));
  }
}