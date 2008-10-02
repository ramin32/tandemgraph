package edu.cuny.brooklyn.tandem.controller.widgets;

import static javax.swing.JOptionPane.showMessageDialog;

import java.awt.Insets;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.ResultSet;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import edu.cuny.brooklyn.tandem.helper.InfoFiles;
import edu.cuny.brooklyn.tandem.helper.SwingUtil;
import edu.cuny.brooklyn.tandem.helper.SqlConnectionFactory;
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
        JComponent aboutTextArea = SwingUtil.createFileTextArea(InfoFiles.ABOUT);
        showMessageDialog(frame, aboutTextArea, "About", JOptionPane.INFORMATION_MESSAGE);
    }

//	public void openFile(final JFrame frame)
//	{
//
//		JFileChooser fc = new JFileChooser();
//		fc.setCurrentDirectory(new File("."));
//		int returnVal = fc.showOpenDialog(frame);
//
//		if (returnVal == JFileChooser.APPROVE_OPTION)
//		{
//			File fileName = fc.getSelectedFile();
//			try
//			{
//
//				distanceList_.setInputFile(fileName);
//
//				BusyDialog busyDialog = new BusyDialog(frame, InfoFiles.LOADING);
//				DistancesLoaderWorker worker = new DistancesLoaderWorker(distanceList_, runnable_, busyDialog);
//				worker.execute();
//			}
//			catch (FileNotFoundException e)
//			{
//				showMessageDialog(frame,
//						"Errors occured while trying to read from file: "
//						+ e.getMessage());
//			}
//
//
//		}
//	}

    public void clear()
    {
        distanceList_.clear();
        runnable_.run();

    }

    public void exit(final JFrame frame)
    {
        int choice = JOptionPane.showConfirmDialog(frame,
                "Are you sure you want to quit? ");
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

    public void openChromosome(JFrame frame, String chromosomeName)
    {

        distanceList_.setChromosomeName(chromosomeName);
        BusyDialog busyDialog = new BusyDialog(frame, InfoFiles.LOADING);
        DistancesLoaderWorker worker = new DistancesLoaderWorker(distanceList_, runnable_, busyDialog);
        worker.execute();


    }
}
