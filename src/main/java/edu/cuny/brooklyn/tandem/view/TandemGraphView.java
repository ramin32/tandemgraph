package edu.cuny.brooklyn.tandem.view;

import java.awt.BorderLayout;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

import edu.cuny.brooklyn.tandem.controller.graph.listener.RepeatListenerDelegator;
import edu.cuny.brooklyn.tandem.controller.widgets.GraphShifterController;
import edu.cuny.brooklyn.tandem.controller.widgets.MenuBarController;
import edu.cuny.brooklyn.tandem.controller.widgets.TextRangeSelectorController;
import edu.cuny.brooklyn.tandem.controller.widgets.TriangleTrapezoidSelectorController;
import edu.cuny.brooklyn.tandem.controller.widgets.ZoomSliderController;
import edu.cuny.brooklyn.tandem.helper.SwingUtil;
import edu.cuny.brooklyn.tandem.model.DistanceList;
import edu.cuny.brooklyn.tandem.model.DistancesLoaderWorker;
import edu.cuny.brooklyn.tandem.view.widgets.GraphShifterView;
import edu.cuny.brooklyn.tandem.view.widgets.MenuBarView;
import edu.cuny.brooklyn.tandem.view.widgets.NavigatorToolbar;
import edu.cuny.brooklyn.tandem.view.widgets.ZoomSliderView;

public class TandemGraphView implements Runnable
{

	private static final int FRAME_WIDTH;
	private static final int FRAME_HEIGHT;
	private final String FRAME_TITLE = "Tandem Repeats Grapher V2.0";

	// Set frame size to 1/2 height and 3/4 width of client monitor.
	static
	{
		FRAME_WIDTH = (int) Math.round(Toolkit.getDefaultToolkit().getScreenSize().width* (3/4.0));
		FRAME_HEIGHT = (int) Math.round(Toolkit.getDefaultToolkit().getScreenSize().height/2.0);
	}


	private final JFrame frame_;
	private final MenuBarView menuBarView_;
	private final GraphPanelView graphPanelView_;
	private final ZoomSliderView zoomSliderView_;

	private final DistanceList distances_;

	private final NavigatorToolbar navigatorToolbar_;
	private final GraphShifterController graphShifterController_;
	private final TextRangeSelectorController textRangeSelectorController_;
	private final ZoomSliderController zoomSliderController_;
	private final TriangleTrapezoidSelectorController triangleTrapezoidSelectorController_;
	private final RepeatListenerDelegator listenerDelegator_;

	private Runnable userDefinedChromosomeLoaderRunnable_;
	private final Runnable viewUpdaterRunnable_;



	public TandemGraphView(final String[] args)
	{
		viewUpdaterRunnable_ = new ViewUpdaterRunnable();
		distances_ = new DistanceList();
		frame_ = new JFrame(FRAME_TITLE);
		frame_.setIconImage(SwingUtil.getImage("images/dna-icon.gif"));
		graphPanelView_ = new GraphPanelView(distances_, viewUpdaterRunnable_, frame_);

		menuBarView_ = new MenuBarView(frame_, new MenuBarController(distances_, viewUpdaterRunnable_));

		// Controllers
		textRangeSelectorController_ = new TextRangeSelectorController(distances_.getLimitedRange(), viewUpdaterRunnable_);
		graphShifterController_ = new GraphShifterController(distances_.getLimitedRange(), viewUpdaterRunnable_);

		GraphShifterView graphShifterView_ = new GraphShifterView(graphShifterController_);

		triangleTrapezoidSelectorController_ = new TriangleTrapezoidSelectorController(distances_, viewUpdaterRunnable_);
		navigatorToolbar_ = new NavigatorToolbar(textRangeSelectorController_, 
				graphPanelView_.getGraphicalRangeSelector(), 
				graphShifterView_, 
				triangleTrapezoidSelectorController_);

		zoomSliderController_ = new ZoomSliderController(distances_.getLimitedRange(), viewUpdaterRunnable_, navigatorToolbar_.getTextRangeSelectorView());
		zoomSliderView_ = new ZoomSliderView(zoomSliderController_, graphShifterController_);
		listenerDelegator_ = new RepeatListenerDelegator(distances_, frame_, graphPanelView_);


		userDefinedChromosomeLoaderRunnable_ = createUserDefinedChromosomeLoaderRunnable(args);

	}

	private Runnable createUserDefinedChromosomeLoaderRunnable(final String[] args) {
		if(args.length == 1)
		{
			return new Runnable()
			{
				public void run()
				{
					distances_.setChromosome(args[0]);
					DistancesLoaderWorker.loadDistances(frame_, distances_, viewUpdaterRunnable_);
				}
			};
		}
		return null;
	}

	public void run()
	{
		frame_.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SwingUtil.showDialogsforUncaughtExceptionsInFrame(frame_);

		frame_.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		frame_.setJMenuBar(menuBarView_);

		JPanel southPanel = new JPanel(new BorderLayout());
		frame_.add(southPanel, BorderLayout.SOUTH);

		frame_.add(graphPanelView_, BorderLayout.CENTER);

		frame_.add(zoomSliderView_, BorderLayout.EAST);
		frame_.add(navigatorToolbar_, BorderLayout.SOUTH);

		SwingUtil.centralizeComponent(frame_, null);
		frame_.setVisible(true);
		viewUpdaterRunnable_.run();

		navigatorToolbar_.getTextRangeSelectorView().setFields(distances_.getLimitedRange().getLocal());

		// set minimal size of local range by font size
		int fontWidth = graphPanelView_.getGraphics().getFontMetrics().charWidth('A');
		int minLocalRange = graphPanelView_.getWidth() * fontWidth;
		distances_.getLimitedRange().setMinLocalRange(minLocalRange);

		listenerDelegator_.installListeners(graphPanelView_);
	}

	private class ViewUpdaterRunnable implements Runnable
	{
		public void run()
		{

			if (distances_.isEmpty())
			{
				if (zoomSliderView_ != null)
					zoomSliderView_.setVisible(false);
				if (navigatorToolbar_ != null)
					navigatorToolbar_.setVisible(false);
				if (frame_ != null)
					frame_.setTitle(FRAME_TITLE);
			}
			else
			{
				if (distances_.getChromosome() != null)
					frame_.setTitle(FRAME_TITLE + " - " + distances_.getChromosome().getName());

				if (zoomSliderView_ != null)
				{
					zoomSliderView_.setVisible(true);
					int percentage = distances_.getLimitedRange().getLocalRangePercentage();
					zoomSliderView_.removeListener();
					zoomSliderView_.setSliderValue(percentage);
					zoomSliderView_.installListener();
				}

				if (navigatorToolbar_ != null)
					navigatorToolbar_.setVisible(true);
			}

			if (frame_ != null)
			{
				frame_.repaint();
				frame_.setVisible(true); // Refreshes all widgets
				frame_.requestFocus();
			}

			if(userDefinedChromosomeLoaderRunnable_ != null)
			{
				userDefinedChromosomeLoaderRunnable_.run();
				userDefinedChromosomeLoaderRunnable_ = null;
			}
		}
	}



	public JFrame getFrame()
	{
		return frame_;
	}
}
