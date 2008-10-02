/*******************************************************
 * Listens for clicks on a graphPane and pops up info.
 *********************************************************/

package edu.cuny.brooklyn.tandem.controller.graph;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;

import edu.cuny.brooklyn.tandem.model.Distance;
import edu.cuny.brooklyn.tandem.model.DistanceList;
import edu.cuny.brooklyn.tandem.view.GraphPanelView;

public class TriangleClickListener extends MouseAdapter
{
	private GraphPanelView containingPanelView_;
	private final DistanceList distances_;

	private Integer startIndex_;
	private Integer endIndex_;

	public TriangleClickListener(DistanceList distances)
	{
		distances_ = distances;
	}

	public void install(final GraphPanelView panelView)
	{
		containingPanelView_ = panelView;
		containingPanelView_.addMouseListener(this);
		Thread t = new Thread(new Runnable()
		{
			public void run()
			{
				while (true)
				{
					try
					{
						distances_.sort();
					}
					catch (Exception e)
					{
						startIndex_ = 0;
						endIndex_ = distances_.getSize() - 1;
						break;
					}
				}

			}
		});

		t.start();

	}

	public void mouseMoved(MouseEvent e)
	{
		if (distances_ == null || containingPanelView_ == null)
			return;
		int adjustedHeight = containingPanelView_.getHeight()
				- containingPanelView_.MARGIN;
		int adjustedWidth = containingPanelView_.getWidth()
				- containingPanelView_.MARGIN;

		// skip click if it isn't inside the graph.
		if (distances_.isEmpty() || e.getY() > adjustedHeight
				|| e.getX() < containingPanelView_.MARGIN)
			return;

		int rangeStart = distances_.getLimitedRange().getLocalMin();

		// Reverses the scale
		double xUnScale = (double) distances_.getLimitedRange().getLocal().getSize()
				/ adjustedWidth;

		int actualPoint = (int) ((e.getX() - containingPanelView_.MARGIN) * xUnScale)
				+ rangeStart;

		Distance correspondingDist = null;
		for (int i = 0; i < distances_.getSize(); i++)
		{
			int start = distances_.get(i).getMin();
			int end = distances_.get(i).getMax();
			if (actualPoint >= start && actualPoint <= end)
			{
				correspondingDist = distances_.get(i);
				break;
			}

		}

		if (correspondingDist == null)
			return;

		JOptionPane.showMessageDialog(containingPanelView_, correspondingDist
				+ "\n Alignment goes here...");

	}
}
