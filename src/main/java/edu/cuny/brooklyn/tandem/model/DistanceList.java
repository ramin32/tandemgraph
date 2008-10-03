/****************************************************************
 * DistanceList.java
 *
 * Delegates ArrayList<Distance>, providing specific functionality
 * for working with a list of Tandem Repeats.
 *
 * Author:
 * Ramin Rakhamimov
 * Brooklyn College Research Project
 * Under the supervion of Professor Sokol
 ************************************************************************/
package edu.cuny.brooklyn.tandem.model;

import edu.cuny.brooklyn.tandem.helper.SqlConnectionFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DistanceList extends AbstractList<Distance>
{
    private final List<Distance> distances_;

    private final LimitedRange limitedRange_;
    private int maxSize_;

    private String inputId_;
    //	private Scanner inputScanner_;
    private Distance selected_;
    private String chromosomeName_;

    public DistanceList()
    {
        distances_ = new ArrayList<Distance>();
        limitedRange_ = new LimitedRange();

    }

    public Distance getSelected()
    {
        return selected_;
    }

    public void setSelected(Distance selected)
    {
        selected_ = selected;
    }



    public void load()
    {
        if (chromosomeName_ == null)
            throw new IllegalStateException("Chromosome name not set!");

        distances_.clear();
        try
        {
            String sqlQuery = "SELECT start, end FROM edit_distance where chromosome_id = " +
                                "(SELECT chromosome_id from chromosome WHERE name = '" + chromosomeName_ + "')";
            Statement statement = SqlConnectionFactory.getPreparedStatement(sqlQuery);
            statement.execute(sqlQuery);
            ResultSet resultSet = statement.getResultSet();

            while (resultSet.next())
            {
                add(resultSet.getInt("start"), resultSet.getInt("end"));
            }
        }
        catch (SQLException e)
        {
        }
    }

    /**
     * Adds a Distance object to the DistanceList and adjusts extremes as
     * necessary
     *
     * @param distance
     */
    public boolean add(Distance distance)
    {
        try
        {
            // if list is empty assign current distance points to extremes
            if (isEmpty())
            {
                limitedRange_.setGlobal(distance);
                maxSize_ = distance.getSize();
                // Otherwise adjust them accordingly.
            }
            else
            {
                if (limitedRange_.getGlobalMin() > distance.getMin())
                    limitedRange_.setGlobalMin(distance.getMin());

                if (limitedRange_.getGlobalMax() < distance.getMax())
                    limitedRange_.setGlobalMax(distance.getMax());

                if (maxSize_ < distance.getSize())
                    maxSize_ = distance.getSize();
            }
            distances_.add(distance);
        }
        catch (NullPointerException ex)
        {
            System.out.println("Null " + ex.getMessage() + " " + distance + " " + limitedRange_ + " " + maxSize_);
            ex.printStackTrace();
        }

        return true;
    }

    public void add(int start, int end)
    {
        add(new Distance(start, end));
    }

    public void clear()
    {
        distances_.clear();
        maxSize_ = 0;
        limitedRange_.clear();
    }

    public boolean isEmpty()
    {
        return distances_.isEmpty();
    }

    public Distance get(int index)
    {
        return distances_.get(index);
    }

    public int getMaxRepeatSize()
    {
        return maxSize_;
    }

    public int getSize()
    {
        return distances_.size();
    }

    public LimitedRange getLimitedRange()
    {
        return limitedRange_;
    }

    /**
     * Sorts the DistanceList by natural order--ascending starting points.
     */
    public void sort()
    {
        Collections.sort(distances_);
    }

    public void sortBySize(final boolean ascending)
    {
        // Create a comparator to sort by size instead of starting location
        Comparator<Distance> c = new Comparator<Distance>()
        {
            public int compare(Distance r1, Distance r2)
            {
                if (ascending)
                    return r1.getSize() - r2.getSize();
                else
                    return r2.getSize() - r1.getSize();
            }
        };

        Collections.sort(distances_, c);
    }

    public int find(Distance d)
    {
        int index = Collections.binarySearch(distances_, d);

        if (index < 0)
            index = (index + 1) * (-1);

        return index;
    }

    public int findStartPointAt(int n)
    {
        return find(new Distance(n, n));
    }

    public Iterator<Distance> iterator()
    {
        return distances_.iterator();
    }

    public void print()
    {
        for (Distance r : this)
        {
            System.out.println(r);
        }
    }

    public List<Distance> toList()
    {
        return distances_;
    }

    public static void main(String[] args)
    {

        DistanceList dl = new DistanceList();
        System.out.println(dl.isEmpty());
        dl.add(20, 70);
        dl.add(25, 26);
        dl.add(40, 45);
        dl.add(20, 80);
        dl.add(60, 90);
        dl.print();
        System.out.println(dl.isEmpty());

    }

    public String getInputId()
    {
        return inputId_;
    }

    @Override
    public int size()
    {
        return distances_.size();
    }


    public void setChromosomeName(String chromosomeName)
    {

        chromosomeName_ = chromosomeName;
    }
}
