package edu.cuny.brooklyn.tandem.model;

import edu.cuny.brooklyn.tandem.view.widgets.BusyDialog;

import javax.swing.*;

public class DistancesLoaderWorker extends SwingWorker<Void, Void>
{

    private final DistanceList distanceList_;
    private final Runnable runnable_;
    private final BusyDialog busyDialog_;

    public DistancesLoaderWorker(DistanceList distanceList, Runnable runnable, BusyDialog busyDialog)
    {
        distanceList_ = distanceList;
        runnable_ = runnable;
        busyDialog_ = busyDialog;
    }

    @Override
    public Void doInBackground()
    {
        busyDialog_.setLoading(true);
        distanceList_.load();
        return null;
    }

    @Override
    public void done()
    {

        runnable_.run();
        busyDialog_.requestFocus();
        busyDialog_.setLoading(false);
    }

}
