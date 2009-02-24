package edu.cuny.brooklyn.tandem.controller.widgets;

import edu.cuny.brooklyn.tandem.model.DistanceList;
import edu.cuny.brooklyn.tandem.model.DrawType;
import edu.cuny.brooklyn.tandem.model.LimitedRange;

public class TriangleTrapezoidSelectorController 
{

	private final DistanceList distances_;
	private final Runnable runnable_;

	public TriangleTrapezoidSelectorController(DistanceList distances,
			Runnable runnable) {
		distances_ = distances;
		runnable_ = runnable;
	}

	public void switchDrawType(DrawType drawType) 
	{
		distances_.setDrawType(drawType);
		runnable_.run();
		
	}
	
	public DistanceList getDistances()
	{
		return distances_;
	}

}
