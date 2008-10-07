package edu.cuny.brooklyn.tandem.controller.widgets;

import edu.cuny.brooklyn.tandem.helper.InfoFiles;
import edu.cuny.brooklyn.tandem.helper.SwingUtil;
import edu.cuny.brooklyn.tandem.model.Chromosome;
import edu.cuny.brooklyn.tandem.model.DistanceList;
import edu.cuny.brooklyn.tandem.model.DistancesLoaderWorker;
import edu.cuny.brooklyn.tandem.view.widgets.BusyDialog;

import javax.swing.*;
import static javax.swing.JOptionPane.showMessageDialog;

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
        JComponent aboutTextArea = SwingUtil.createFileTextArea(InfoFiles.ABOUT);
        showMessageDialog(frame, aboutTextArea, "About", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showLicense(JFrame frame)
    {
        JComponent aboutTextArea = SwingUtil.createFileTextArea(InfoFiles.LICENSE);
        showMessageDialog(frame, aboutTextArea, "About", JOptionPane.INFORMATION_MESSAGE);
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

//	public void openNetworkStream(final JFrame frame)
//	{
//		final String url = JOptionPane.showInputDialog("Enter an url: ");
//		if (url == null || url == "")
//			return;
//
//		try
//		{
//			distanceList_.setInputURL(url);
//			BusyDialog busyDialog = new BusyDialog(frame, InfoFiles.LOADING);
//			DistancesLoaderWorker worker = new DistancesLoaderWorker(distanceList_, runnable_, busyDialog);
//			worker.execute();
//		}
//		catch (IOException e)
//		{
//			showMessageDialog(frame,
//					"Errors occured while trying to open url.");
//		}
//	}

    public void openChromosome(JFrame frame, Chromosome chromosome)
    {

        distanceList_.setChromosomeName(chromosome);
        BusyDialog busyDialog = new BusyDialog(frame, InfoFiles.LOADING);
        DistancesLoaderWorker worker = new DistancesLoaderWorker(distanceList_, runnable_, busyDialog);
        worker.execute();


    }
}
