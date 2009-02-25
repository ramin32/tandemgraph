package edu.cuny.brooklyn.tandem.model;

public class DistanceInformation
{

	private final int period_;
	private final int errors_;
	
	public DistanceInformation(int period, int errors)
	{

		period_ = period;
		errors_ = errors;
		
	}
	
	public int getPeriod()
	{
		return period_;
	}

	public int getErrors()
	{
		return errors_;
	}
}
