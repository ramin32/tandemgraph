package edu.cuny.brooklyn.tandem.model;

public class DistanceInformation
{

	private final int period_;
	private final int errors_;
	private final int distanceId_;
	
	public DistanceInformation(int period, int errors, int distanceId)
	{

		period_ = period;
		errors_ = errors;
		distanceId_ = distanceId;
		
	}
	
	public int getPeriod()
	{
		return period_;
	}

	public int getErrors()
	{
		return errors_;
	}

	public int getDistanceId()
	{
		return distanceId_;
	}
}
