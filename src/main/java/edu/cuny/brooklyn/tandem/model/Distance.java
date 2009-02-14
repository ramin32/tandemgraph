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

import edu.cuny.brooklyn.tandem.helper.SwingUtil;

import java.awt.*;

public class Distance extends Range
{

    private final Color color_;
    private final int id_;

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

    public static void main(String[] args)
    {
        Distance r = new Distance(20, 30,1);
        try
        {
            new Distance(100, 70,2);
            // new Distance(100,-10);
        }
        catch (IllegalArgumentException e)
        {
            System.out.println(e.getMessage());
        }

        System.out.println(r);
    }



}
