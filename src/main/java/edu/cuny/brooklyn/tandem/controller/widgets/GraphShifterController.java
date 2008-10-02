package edu.cuny.brooklyn.tandem.controller.widgets;

import edu.cuny.brooklyn.tandem.model.LimitedRange;

public class GraphShifterController
{
	private static final double DIVEDEND = 4;
	private static final int LEAST_VISIBLE_PIXELS = 10;
	private final LimitedRange limitedRange_;
	private final Runnable runnable_;

	public GraphShifterController(LimitedRange limitedRange, Runnable runnable)
	{
		limitedRange_ = limitedRange;
		runnable_ = runnable;
	}

	public LimitedRange getLimitedRange()
	{
		return limitedRange_;
	}

	public void shiftLeft()
	{
		shift(true);
		runnable_.run();
	}

	public void shiftRight()
	{
		shift(false);

		runnable_.run();
	}

	public void shiftLeft(int times)
	{
		for (int i = 0; i < times; i++)
			shift(true);

		runnable_.run();
	}

	public void shiftRight(int times)
	{
		for (int i = 0; i < times; i++)
			shift(false);

		runnable_.run();
	}

	public void shift(boolean shiftingRight)
	{
		try
		{
			int range = limitedRange_.getLocal().getSize();
			int increment = (int) (range / DIVEDEND);
			int start = limitedRange_.getLocalMin();
			int end = limitedRange_.getLocalMax();
			if (shiftingRight)
			{
				start -= increment;
				end -= increment;

			}
			else
			{
				start += increment;
				end += increment;
			}

			if (start < limitedRange_.getGlobalMin())
			{
				start = limitedRange_.getGlobalMin();
				end = start + range;
			}
			if (end > limitedRange_.getGlobalMax())
			{
				end = limitedRange_.getGlobalMax();
				start = end - range;
			}
			limitedRange_.setLocal(start, end);
		}
		catch (Throwable exp)
		{
		}
	}

	public void zoomIn()
	{
		zoom(false);

		runnable_.run();
	}

	public void zoomOut()
	{
		zoom(true);

		runnable_.run();
	}

	public void zoom(boolean zoomingOut)
	{
		double start = limitedRange_.getLocalMin();
		double end = limitedRange_.getLocalMax();
		try
		{
			int range = limitedRange_.getLocal().getSize();
			double increment = range / DIVEDEND;
			if (zoomingOut)
			{
				start -= increment;
				end += increment;

			}
			else
			{
				start += increment;
				end -= increment;
				if (Math.abs(end - start) <= LEAST_VISIBLE_PIXELS)
					return;
			}

			if (start < limitedRange_.getGlobalMin())
				start = limitedRange_.getGlobalMin();

			if (end > limitedRange_.getGlobalMax())
				end = limitedRange_.getGlobalMax();
			limitedRange_.setLocal((int) start, (int) end);

		}
		catch (Throwable e)
		{
			if (Math.abs(start - end) < 10)
				limitedRange_.reset();
			throw new RuntimeException("No further zooming is allowed!");
		}
	}
}
