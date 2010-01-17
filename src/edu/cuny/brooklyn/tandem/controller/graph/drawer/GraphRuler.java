package edu.cuny.brooklyn.tandem.controller.graph.drawer;

import java.awt.Graphics;

import javax.swing.JPanel;

import org.apache.log4j.Logger;

import edu.cuny.brooklyn.tandem.model.Range;
import java.awt.geom.AffineTransform;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Font;
import java.awt.Color;

public class GraphRuler
{
  private static final Logger logger_ = Logger.getLogger(GraphRuler.class);
  private final JPanel containingPanel_;
  private final int MARGIN;
  private final int MARKINGS;
  public static final int DEFAULT_TICK_SIZE = 10;
  private int xAxisTickSize;
  private int yAxisTickSize;

  public GraphRuler(JPanel panel, int margin, int markings)
  {
    if (panel == null) {
      throw new IllegalArgumentException("Panel can't be null.");
    }
    this.containingPanel_ = panel;
    this.MARGIN = margin;
    this.MARKINGS = markings;

    this.xAxisTickSize = (this.yAxisTickSize = this.xAxisTickSize);
  }

  public GraphRuler(JPanel panel)
  {
    this.containingPanel_ = panel;
    this.MARGIN = 70;
    this.MARKINGS = 10;
  }

  public void drawLines(Graphics g)
  {
    g.drawLine(this.MARGIN, 0, this.MARGIN, this.containingPanel_.getHeight());
    int bottomMarginLocation = this.containingPanel_.getHeight() - this.MARGIN;
    g.drawLine(0, bottomMarginLocation, this.containingPanel_.getWidth(), bottomMarginLocation);
  }

  public void drawMarkings(Graphics g, Range horRange, int height, boolean isLogGraph)
  {
    int horizontalScale = horRange.getSize() / this.MARKINGS;
    int verticalScale = height;
    if (!(isLogGraph)) {
      verticalScale /= this.MARKINGS;
    }
    int bottomMarginPos = this.containingPanel_.getHeight() - this.MARGIN;

    int xScale = (this.containingPanel_.getWidth() - this.MARGIN) / this.MARKINGS;
    int yScale = (this.containingPanel_.getHeight() - this.MARGIN) / ((isLogGraph) ? verticalScale : this.MARKINGS);

    drawHorizontalLabels(g, horRange, xScale, horizontalScale, bottomMarginPos);

    if (isLogGraph)
      drawVerticalLogGraphLabels(g, verticalScale, yScale);
    else
      drawVerticalLabels(g, verticalScale, yScale);
  }

  private final void drawHorizontalLabels(Graphics g, Range horRange, int xScale, int horizontalScale, int bottomMarginPos)
  {
    int mark = 1; for (int horLabel = horRange.getMin(); mark < this.MARKINGS; ++mark)
    {
      String label = Integer.toString(mark * horizontalScale + horRange.getMin());
      label = commanize(label);

      int xPos = this.MARGIN + mark * xScale;
      g.drawLine(xPos, bottomMarginPos, xPos, bottomMarginPos + this.xAxisTickSize);

      Image labelImage = createSlantedMarkingImage(label, g.getFont(), g.getColor());
      g.drawImage(labelImage, 
                   xPos - 3, 
                   bottomMarginPos + this.xAxisTickSize + 1 ,//+  + 1,
                   null);
    }
  }

  private final static Image createSlantedMarkingImage(String label, Font font, Color color)
  {
    Image image = new BufferedImage(100,100,BufferedImage.TYPE_INT_RGB);
    Graphics2D g2d = (Graphics2D)image.getGraphics();
    g2d.setFont(font);
    g2d.setColor(color);
    AffineTransform transform = g2d.getTransform().getRotateInstance(.2);
    g2d.setTransform(transform);

    g2d.drawString(label,1,g2d.getFont().getSize());
    return image;
  }

  private final void drawVerticalLabels(Graphics g, int verticalScale, int yScale)
  {
    for (int mark = 1; mark < this.MARKINGS; ++mark)
    {
      String label = Integer.toString((this.MARKINGS - mark) * verticalScale);

      label = commanize(label);

      int yPos = mark * yScale;
      g.drawLine(this.MARGIN - this.yAxisTickSize, yPos, this.MARGIN, yPos);
      g.drawString(label, 0, yPos + 5);
    }
  }

  private final void drawVerticalLogGraphLabels(Graphics g, int verticalScale, int yScale)
  {
    for (int mark = 0; mark < verticalScale; ++mark)
    {
      String label = Long.toString(Math.round(Math.pow(10.0D, verticalScale - mark)));
      label = commanize(label);

      int yPos = mark * yScale;
      g.drawLine(this.MARGIN - this.yAxisTickSize, yPos, this.MARGIN, yPos);
      g.drawString(label, 0, yPos + 5);
    }
  }

  public static String commanize(String input)
  {
    StringBuilder output = new StringBuilder(input);
    for (int i = output.length() - 3; i > 0; i -= 3)
      output.insert(i, ",");
    return output.toString();
  }

  public void drawRuler(Graphics g, Range horRange, int height, boolean isLogGraph)
  {
    drawLines(g);
    drawMarkings(g, horRange, height, isLogGraph);
  }

  public void setXAxisTickSize(int n)
  {
    this.xAxisTickSize = n;
  }

  public void setYAxisTickSize(int n)
  {
    this.yAxisTickSize = n;
  }
}
