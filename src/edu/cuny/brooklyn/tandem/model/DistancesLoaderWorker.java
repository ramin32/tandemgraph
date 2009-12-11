package edu.cuny.brooklyn.tandem.model;

import javax.swing.JFrame;
import javax.swing.SwingWorker;

import edu.cuny.brooklyn.tandem.helper.InfoFiles;
import edu.cuny.brooklyn.tandem.helper.SwingUtil;
import edu.cuny.brooklyn.tandem.view.widgets.BusyDialog;

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
    
    @Override public Void doInBackground()
    {
        busyDialog_.setLoading(true);
        distanceList_.load();
        return null;
    }
    
    @Override public void done()
    {
        
        runnable_.run();
        busyDialog_.requestFocus();
        busyDialog_.setLoading(false);
    }
    
    public static void loadDistances(JFrame frame, DistanceList distanceList_, Runnable runnable_)
    {    	
            boolean undecorated = false;
            BusyDialog busyDialog = new BusyDialog(frame, SwingUtil.createFileTextArea(InfoFiles.LOADING), undecorated);
            DistancesLoaderWorker worker = new DistancesLoaderWorker(distanceList_, runnable_, busyDialog);
            worker.execute();        
    }
    
}
