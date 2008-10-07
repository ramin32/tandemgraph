/*******************************************************
 * RunnableSet.java
 *
 * A collection of runnable objects which all run at once
 * upon calling the run method.
 ******************************************************/
package edu.cuny.brooklyn.tandem.helper;

import java.util.HashSet;
import java.util.Set;

public class RunnableSet implements Runnable
{
    private final Set<Runnable> runnableSet_;

    public RunnableSet()
    {
        runnableSet_ = new HashSet<Runnable>();
    }

    public void addRunnable(Runnable runnable)
    {
        runnableSet_.add(runnable);

    }

    public void run()
    {
        for (Runnable runnable : runnableSet_)
        {
            runnable.run();
        }

    }

    public static void main(String[] args)
    {
        RunnableSet set = new RunnableSet();

        for (int i = 0; i < 10; i++)
        {

            set.addRunnable(new Runnable()
            {
                public void run()
                {
                    System.out.println("Printing " + Math.random() * 1000);
                }
            });
        }

        set.run();
    }
}
