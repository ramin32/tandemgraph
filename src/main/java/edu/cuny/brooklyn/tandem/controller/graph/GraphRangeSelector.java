/****************************************************
 * ClickBar.java
 *
 * Adds a clicker bar to select ranges to the graph.
 *
 * Author:
 * Ramin Rakhamimov
 * ramin32@gmail.com
 * http://www.ramrak.net
 ***************************************************/
package edu.cuny.brooklyn.tandem.controller.graph;

import edu.cuny.brooklyn.tandem.model.LimitedRange;
import edu.cuny.brooklyn.tandem.view.widgets.TextRangeSelectorView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class GraphRangeSelector extends MouseAdapter implements MouseMotionListener
{
    private Runnable updater_;
    private LimitedRange limitedRange_;
    private int barWidth_;
    private int barHeight_;
    private int barX1_;
    private int barX2_;
    private int barY1_;
    private int barY2_;


    // Points where user clicks inside the RangeSelector
    private Integer startPoint_;
    private Integer scaledStartPoint_;
    private Integer endPoint_;
    private Integer scaledEndPoint_;

    private boolean mouseIsPressed_;
    private JPanel panel_;

    private TextRangeSelectorView textRangeSelectorView_;

    public GraphRangeSelector(final JPanel panel, Runnable runnable, LimitedRange limitedRange)
    {
        panel_ = panel;
        updater_ = runnable;
        limitedRange_ = limitedRange;

        panel.addMouseListener(this);
        panel.addMouseMotionListener(this);
    }

    public void setTextRangeSelectorView(TextRangeSelectorView textRangeSelectorView)
    {
        textRangeSelectorView_ = textRangeSelectorView;
    }

    public void paintComponent(Graphics g, Point point, Dimension dimension)
    {
        update(point, dimension);
        g.setColor(Color.red);
        // Draw the range selector rectangle
        g.drawRect(barX1_, barY1_, barWidth_, barHeight_);
        g.setFont(new Font("Monospace", Font.BOLD, (int) (barHeight_ * (3.0 / 2))));
        g.setColor(Color.WHITE);

        g.setColor(Color.green);

        if (startPoint_ != null)
        {
            if (endPoint_ == null) g.drawLine(startPoint_, barY1_ + 1, startPoint_, barY2_ - 1);
            else
            {
                int min = getMinPoint();
                int max = getMaxPoint();
                int width = max - min;
                g.fillRect(min, barY1_ + 1, width, barHeight_ - 1);
            }
        }
    }

    private int scalePoint(int point)
    {
        point -= barX1_;

        double scale = ((double) limitedRange_.getLocal().getSize()) / barWidth_;
        point = (int) (point * scale + limitedRange_.getLocalMin());

        return point;
    }

    // g.drawString("Select an inner range to view in the rectangle above.",
    // barX1_ + barWidth_ / 4, barY2_ + barHeight_ * 2);
    public void clear()
    {
        startPoint_ = endPoint_ = null;
        // textRangeSelectorView_.setFields(limitedRange_.getLocal());
        panel_.repaint();

    }

    private void update(Point point, Dimension dimension)
    {
        barX1_ = (int) point.getX();
        barY1_ = (int) point.getY();
        barWidth_ = (int) dimension.getWidth();
        barHeight_ = (int) dimension.getHeight();
        barX2_ = barX1_ + barWidth_;
        barY2_ = barY1_ + barHeight_;
    }

    public void mousePressed(MouseEvent e)
    {
        if (e.getY() < barY1_ || e.getY() > barY2_ || e.getX() < barX1_ || e.getX() > barX2_) return;

        mouseIsPressed_ = true;

        startPoint_ = endPoint_ = null;

        startPoint_ = endPoint_ = e.getX();
        scaledStartPoint_ = scalePoint(e.getX());
        textRangeSelectorView_.setStartValue("" + scaledStartPoint_);
        textRangeSelectorView_.setEndValue("");
        panel_.repaint();

    }

    @Override
    public void mouseDragged(MouseEvent e)
    {
        if (!mouseIsPressed_) return;
        if (e.getX() <= barX1_) endPoint_ = barX1_;
        else if (e.getX() >= barX2_) endPoint_ = barX2_;

        else endPoint_ = e.getX();

        scaledEndPoint_ = scalePoint(endPoint_);


        textRangeSelectorView_.setStartValue("" + scalePoint(getMinPoint()));
        textRangeSelectorView_.setEndValue("" + scalePoint(getMaxPoint()));

        panel_.repaint();
    }


    @Override
    public void mouseReleased(MouseEvent e)
    {
        if (!mouseIsPressed_) return;
        mouseIsPressed_ = false;
    }


    public int getMinPoint()
    {
        return Math.min(startPoint_, endPoint_);
    }

    public int getMaxPoint()
    {
        return Math.max(startPoint_, endPoint_);
    }


}
