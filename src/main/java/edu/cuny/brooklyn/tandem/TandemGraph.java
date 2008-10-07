/************************************************************************
 * TandemGraph.java
 *
 * Provides and intuitive  graphical triangular representation of
 * Tandem Repeats, where the triangle vertices represent the size of the
 * Distance.
 *
 * Implements the entire TandemRepeats Project.
 * Opens a file in TandemRepeat specific format, loads it up into a
 * DistanceList, Creates a JFrame with a graphic representation of all the
 * tandem Repeats. Also allows user to intereact with the JFrame, in
 * features such as zooming and showing actual repeat names.
 *
 * Author:
 * Ramin Rakhamimov
 * Brooklyn College Research Project
 * Under the supervision of Professor Sokol
 ************************************************************************/

package edu.cuny.brooklyn.tandem;

import edu.cuny.brooklyn.tandem.model.DistanceList;
import edu.cuny.brooklyn.tandem.view.TandemGraphView;

import javax.swing.*;

public class TandemGraph
{
    public static void main(final String[] args)
    {

        DistanceList distanceList = new DistanceList();
        TandemGraphView tandemGraphView = new TandemGraphView(distanceList);

        SwingUtilities.invokeLater(tandemGraphView);


    }
}
