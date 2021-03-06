/*********************************************************************
 * Distance.java ADT for containing starting and ending points of a Tandem
 * Distance Author: Ramin Rakhamimov Brooklyn College Research Project Under the
 * supervion of Professor Sokol
 ************************************************************************/
package edu.cuny.brooklyn.tandem.model;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import edu.cuny.brooklyn.tandem.helper.SwingUtil;

public class Distance extends Range
{
    private static final Logger logger_ = Logger.getLogger(Distance.class);
    private final Color color_;
    private final int id_;
    
    private final static Map<Integer, DistanceInformation> informationCache_;
    static
    {
    	informationCache_ = new HashMap<Integer, DistanceInformation>();
    }
    
    public Distance(int start, int end, int id)
    {
        super(start, end);
        color_ = SwingUtil.getRandColor(75);
        id_ = id;
    }
    
    public Color getColor()
    {
        return color_;
    }
    
    public int getId()
    {
        return id_;
    }
    
    public String toString()
    {
        return super.toString();
    }
    
    public double getAdjustedSize()
    {
    	if(DistanceList.getDrawType() == DrawType.TRAPEZOID)
    		return Math.log10(getSize());
    	return getSize();
    }
    public static void main(String[] args)
    {
        Distance r = new Distance(20, 30, 1);
        try
        {
            new Distance(100, 70, 2);
            // new Distance(100,-10);
        }
        catch (IllegalArgumentException e)
        {
            System.out.println(e.getMessage());
        }
        
        System.out.println(r);
    }

	public DistanceInformation getDistanceInformation()
	{
		DistanceInformation info = informationCache_.get(id_);
		if(info == null)
		{			
			info = JdbcTandemDao.getInstance().getDistanceInformationByDistance(this);
			informationCache_.put(id_, info);
		}
		return info;
	}    
}
