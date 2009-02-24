package edu.cuny.brooklyn.tandem.helper;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import edu.cuny.brooklyn.tandem.model.Distance;

public class TriangleShapeDrawer extends ShapeDrawer
{
	public final int DEFAULT_THICKNESS = 5;
	private final BufferedImage grid_;


	public TriangleShapeDrawer(BufferedImage bufferedImage)
	{
		grid_ = bufferedImage;
	}

	public void drawShape(Graphics g, Distance distance,
			double startPoint, double xScale, double yScale, boolean fill)
	{

		double x1 = translateXPoint(distance.getMin(), startPoint, xScale);
		double x2 = translateXPoint(distance.getMidPoint(), startPoint, xScale);
		double x3 = translateXPoint(distance.getMax(), startPoint, xScale);

		double y1 = translateYPoint(0, yScale, grid_.getHeight());
		double y2 = translateYPoint(distance.getSize(), yScale, grid_.getHeight());
		double y3 = y1;

		g.setColor(distance.getColor());

		drawTriangle(g,new int[] {(int) x1,(int) x2,(int) x3}, 
				new int[] {(int) y1,(int) y2,(int) y3}, DEFAULT_THICKNESS, fill);
	}

	private void drawTriangle(Graphics g, int[] xPoints, int[] yPoints, int thickness, boolean fill)
	{
		for(int i = 0; i < thickness; i++)
		{
			if(fill)
			{
				g.fillPolygon(xPoints, yPoints, 3);
				g.fillPolygon(shrinkXPoints(xPoints, i), shrinkYPoints(yPoints, i), 3);
			}
			else
			{
				g.drawPolygon(xPoints, yPoints, 3);
				g.drawPolygon(shrinkXPoints(xPoints, i), shrinkYPoints(yPoints, i), 3);
			}
		}
	}
	private int[] shrinkXPoints(int[] points, int amount)
	{
		return new int[]{points[0] + 1, points[1], points[2] - 1};
	}

	private int[] shrinkYPoints(int[] points, int amount)
	{
		return new int[]{points[0] - 1, points[1] + 1, points[2] - 1};
	}


}
