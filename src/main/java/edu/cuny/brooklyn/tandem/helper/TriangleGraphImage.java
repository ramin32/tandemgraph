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

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ConcurrentModificationException;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import edu.cuny.brooklyn.tandem.model.Distance;
import edu.cuny.brooklyn.tandem.model.DistanceList;
import edu.cuny.brooklyn.tandem.model.Range;

public class TriangleGraphImage
{

	private BufferedImage grid_;
	private final DistanceList distanceList_;

	public TriangleGraphImage(DistanceList distanceList, int width, int height)
	{
		distanceList_ = distanceList;
		if (width <= 0)
			width = 1;
		if (height <= 0)
			height = 1;
		grid_ = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

	}

	public Image getGraphImage(Range range, int height)
	{

		double xScale = (double) range.getSize() / grid_.getWidth();
		double yScale = (double) height / grid_.getHeight();

		Graphics graphics = grid_.getGraphics();

		distanceList_.setSelected(distanceList_.get(0));
		for(int i = 0; i < distanceList_.size(); i++)
				drawTriangle(graphics,distanceList_.get(i), range.getMin(), xScale, yScale);
	
		Distance selectedDistance = distanceList_.getSelected();
		if(selectedDistance != null)
			fillTriangle(graphics,selectedDistance, range.getMin(), xScale, yScale);
		return grid_;
	}

	private void drawTriangle(Graphics g, Distance distance, int startPoint, double xScale, double yScale)
	{

		int x1 = translateXPoint(distance.getMin(), startPoint, xScale);
		int x2 = translateXPoint(distance.getMidPoint(), startPoint, xScale);
		int x3 = translateXPoint(distance.getMax(), startPoint, xScale);

		int y1 = translateYPoint(0, yScale);
		int y2 = translateYPoint(distance.getSize(), yScale);
		int y3 = y1;

		g.setColor(distance.getColor());
		g.drawPolygon(new int[] { x1, x2, x3 }, new int[] { y1, y2, y3 }, 3);
	}
	
	private void fillTriangle(Graphics g, Distance distance, int startPoint, double xScale, double yScale)
	{

		int x1 = translateXPoint(distance.getMin(), startPoint, xScale);
		int x2 = translateXPoint(distance.getMidPoint(), startPoint, xScale);
		int x3 = translateXPoint(distance.getMax(), startPoint, xScale);

		int y1 = translateYPoint(0, yScale);
		int y2 = translateYPoint(distance.getSize(), yScale);
		int y3 = y1;

		g.setColor(distance.getColor());
		g.fillPolygon(new int[] { x1, x2, x3 }, new int[] { y1, y2, y3 }, 3);
	}

	private int translateXPoint(int x, int startPoint, double xScale)
	{
		return (int) ((x - startPoint) / xScale);
	}

	private int translateYPoint(int y, double yScale)
	{
		return grid_.getHeight() - (int) (y / yScale);
	}

	public static void main(String[] args)
	{
		JFrame frame = new JFrame("Triangle Graph Image");
		final DistanceList repeats = new DistanceList();
		repeats.add(100, 1000);
		repeats.add(300, 700);
		repeats.add(500, 1590);

		frame.setSize(500, 500);
		frame.setVisible(true);

		final TriangleGraphImage image = new TriangleGraphImage(repeats, 300, 300);
		frame.add(new JPanel()
		{
			public void paintComponent(Graphics g)
			{
				g.drawImage(image.getGraphImage(repeats.getLimitedRange().getGlobal(), repeats.getMaxRepeatSize()), 70, 0, null);
			}
		});
	}

}
