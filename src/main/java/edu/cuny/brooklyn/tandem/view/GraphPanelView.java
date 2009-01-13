/***********************************************************************
 * TandemGraphPanl.java
 *
 * Provides a JPanel with a drawable area. Uses GraphRuler, LimitedRange
 * DistanceList TriangleGraphImage and GraphRuler to construct the image,
 * on each repaint(); call.
 *
 * Author:
 * Ramin Rakhamimov
 * ramin32@gmail.com
 * http://www.ramrak.net
 ***********************************************************************/
package edu.cuny.brooklyn.tandem.view;

import edu.cuny.brooklyn.tandem.controller.graph.GraphRangeSelector;
import edu.cuny.brooklyn.tandem.controller.widgets.GraphShifterController;
import edu.cuny.brooklyn.tandem.helper.GraphRuler;
import edu.cuny.brooklyn.tandem.helper.TrapezoidGraphImage;
import edu.cuny.brooklyn.tandem.model.DistanceList;
import edu.cuny.brooklyn.tandem.model.Range;

import javax.swing.*;
import java.awt.*;

public class GraphPanelView extends JPanel
{
    public final int MARKINGS = 10;
    public final int MARGIN = 70;
    public static final int SELECTOR_HEIGHT = 20;

    private final DistanceList distances_;
    private TrapezoidGraphImage graphImage_;
    private GraphRuler graphRuler_;
    private GraphRangeSelector graphicalRangeSelector_;
    private final Runnable runnable_;
    private static final int defaultValue = 5;

//	public final static String SEQUENCE_STRING = "SJKDHFDSLKAJSLKDHFJKLASDHFJKLHHSDJKFGSAGFSDAGFSADJKSAHFJSHAASDLJGHASJKLHFJKASDFHLJKASHDFJKHSADFJKHASJKDHFJLKA";

    public GraphPanelView(DistanceList rl, Runnable runnable)
    {
        runnable_ = runnable;
        distances_ = rl;
        graphRuler_ = new GraphRuler(this, MARGIN, MARKINGS);
        graphRuler_.setXAxisTickSize(SELECTOR_HEIGHT);

        graphicalRangeSelector_ = new GraphRangeSelector(this, runnable, distances_.getLimitedRange());
        setLayout(new BorderLayout());

        GraphShifterController controller = new GraphShifterController(rl.getLimitedRange(), runnable);
    }

    public GraphRangeSelector getGraphicalRangeSelector()
    {
        return graphicalRangeSelector_;
    }

    public void paintComponent(Graphics g)
    {
        g.setColor(Color.black);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.gray);

//		if (distances_.isLoading())
//		{
//			graphRuler_.drawLines(g);
//			g.setFont(new Font("Sans Serif", Font.BOLD, 24));
//			g.drawString("Loading...", getWidth() / 2, getHeight() / 2);
//
//		}
//		else 
        if (distances_.isEmpty())
        {
            graphRuler_.drawLines(g);
            g.setFont(new Font("Sans Serif", Font.BOLD, 24));
            g.drawString("Please choose an input source.", getWidth() / 3, getHeight() / 2);
        }
        else
        {
            // Get the actual graph image and draw it.
            graphImage_ = new TrapezoidGraphImage(distances_, getWidth() - MARGIN, getHeight() - MARGIN);
            Image img = graphImage_.getGraphImage(distances_.getLimitedRange().getLocal(), distances_.getMaxRepeatSize());
            g.drawImage(img, MARGIN + 1, 0, null);

            // draw the ruler
            g.setColor(Color.gray);
            graphRuler_.drawRuler(g, distances_.getLimitedRange().getLocal(), new Range(0, distances_.getMaxRepeatSize()));

            // Get the range selector and draw it
            int x = MARGIN;
            int y = getHeight() - MARGIN + 1;
            int fontSize = 14;
            g.setFont(new Font("Sans Serif", Font.BOLD, fontSize));
//			g.drawString(SEQUENCE_STRING, MARGIN, y + fontSize);

            Point point = new Point(x, y);
            Dimension dimension = new Dimension(getWidth() - MARGIN - 1, SELECTOR_HEIGHT);

            graphicalRangeSelector_.paintComponent(g, point, dimension);


        }
    }

}
