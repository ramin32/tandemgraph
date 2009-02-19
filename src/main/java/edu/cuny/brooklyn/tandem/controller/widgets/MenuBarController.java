package edu.cuny.brooklyn.tandem.controller.widgets;

import static javax.swing.JOptionPane.showMessageDialog;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import edu.cuny.brooklyn.tandem.helper.InfoFiles;
import edu.cuny.brooklyn.tandem.helper.SwingUtil;
import edu.cuny.brooklyn.tandem.model.Chromosome;
import edu.cuny.brooklyn.tandem.model.DistanceList;
import edu.cuny.brooklyn.tandem.model.DistancesLoaderWorker;
import edu.cuny.brooklyn.tandem.view.widgets.BusyDialog;

public class MenuBarController
{
    private final DistanceList distanceList_;
    private final Runnable runnable_;
    
    public MenuBarController(DistanceList distanceList, Runnable runnable)
    {
        distanceList_ = distanceList;
        runnable_ = runnable;
    }
    
    public void showUsage(JFrame frame)
    {
        JComponent usageTextArea = SwingUtil.createFileTextArea(InfoFiles.USAGE);
        showMessageDialog(frame, usageTextArea, "Usage", JOptionPane.INFORMATION_MESSAGE);
        
    }
    
    public void showAbout(JFrame frame)
    {
        JComponent aboutTextArea = SwingUtil.createFileTextArea(InfoFiles.ABOUT, SwingUtil.DEFAULT_INSETS, true);
        showMessageDialog(frame, aboutTextArea, "About", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void showLicense(JFrame frame)
    {
        JComponent aboutTextArea = SwingUtil.createFileTextArea(InfoFiles.LICENSE);
        showMessageDialog(frame, aboutTextArea, "License", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void clear()
    {
        distanceList_.clear();
        runnable_.run();
        
    }
    
    public void exit(final JFrame frame)
    {
        int choice = JOptionPane.showConfirmDialog(frame, "Are you sure you want to quit? ");
        if (choice == 0)
        {
            System.exit(0);
        }
        
    }
    
    public void openChromosome(JFrame frame, Chromosome chromosome)
    {
        
        distanceList_.setChromosome(chromosome);
        boolean undecorated = false;
        BusyDialog busyDialog = new BusyDialog(frame, SwingUtil.createFileTextArea(InfoFiles.LOADING), undecorated);
        DistancesLoaderWorker worker = new DistancesLoaderWorker(distanceList_, runnable_, busyDialog);
        worker.execute();
        
    }
}
