package edu.cuny.brooklyn.tandem.helper;

public class MathUtil
{
  public static double computeSlope(double x1, double y1, double x2, double y2)
  {
    return ((y2 - y1) / (x2 - x1));
  }

  public static double computeYIntersect(double x1, double y1, double x2, double y2, double pluggableX)
  {
    double slope = computeSlope(x1, y1, x2, y2);
    double y = slope * (pluggableX - x1) - y1;
    return y;
  }
}