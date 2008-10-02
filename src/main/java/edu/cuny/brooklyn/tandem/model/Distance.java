/*********************************************************************
 * Distance.java
 *
 * ADT for containing starting and ending points of a Tandem Distance
 *
 * Author:
 * Ramin Rakhamimov
 * Brooklyn College Research Project
 * Under the supervion of Professor Sokol
 ************************************************************************/
package edu.cuny.brooklyn.tandem.model;

import java.awt.Color;

import edu.cuny.brooklyn.tandem.helper.SwingUtil;

public class Distance extends Range
{

	private final Color color_;
//	private boolean filledIn_;

	public Distance(int start, int end)
	{
		super(start, end);
		color_ = SwingUtil.getRandColor(75);
	}

	public Color getColor()
	{
		return color_;
	}

	public String toString()
	{
		return super.toString() + " " + color_.toString();
	}

	public static void main(String[] args)
	{
		Distance r = new Distance(20, 30);
		try
		{
			new Distance(100, 70);
			// new Distance(100,-10);
		}
		catch (IllegalArgumentException e)
		{
			System.out.println(e.getMessage());
		}

		System.out.println(r);
	}
	
//	public void setFilledIn(boolean filledIn)
//	{
//		filledIn_ = filledIn;
//	}
//	
//	public boolean isFilledIn()
//	{
//		return filledIn_;
//	}

}
