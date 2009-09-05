/************************************************************************
 * TandemGraph.java Provides and intuitive graphical triangular representation
 * of Tandem Repeats, where the triangle vertices represent the size of the
 * Distance. Implements the entire TandemRepeats Project. Opens a file in
 * TandemRepeat specific format, loads it up into a DistanceList, Creates a
 * JFrame with a graphic representation of all the tandem Repeats. Also allows
 * user to intereact with the JFrame, in features such as zooming and showing
 * actual repeat names. Author: Ramin Rakhamimov Brooklyn College Research
 * Project Under the supervision of Professor Sokol
 ************************************************************************/

package edu.cuny.brooklyn.tandem;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

import edu.cuny.brooklyn.tandem.helper.SwingUtil;
import edu.cuny.brooklyn.tandem.model.Chromosome;
import edu.cuny.brooklyn.tandem.model.DistanceList;
import edu.cuny.brooklyn.tandem.model.JdbcTandemDao;
import edu.cuny.brooklyn.tandem.view.TandemGraphView;
import edu.cuny.brooklyn.tandem.view.widgets.BusyDialog;

public class TandemGraph
{
	private final static Logger logger_ = Logger.getLogger(TandemGraph.class);

	public static void main(final String[] args)
	{
		logger_.debug("Intializing TandemGraph...");
		DistanceList distanceList = initializeTandemGraph(args);

		logger_.debug("Obtaining instance of TandemGraphView...");
		TandemGraphView tandemGraphView = new TandemGraphView(distanceList);

		logger_.debug("Invoking view...");
		SwingUtilities.invokeLater(tandemGraphView);
	}

	private static DistanceList loadDistanceList(String[] args)
	{

		JdbcTandemDao tandemDao = JdbcTandemDao.getInstance();
		tandemDao.initialize();

		DistanceList distanceList = new DistanceList();
		
		if(args.length == 2)
		{
			String usersChromosome = args[1];
			
			for(Chromosome chromosome: tandemDao.getAllChromosomes())
			{
				if(usersChromosome == chromosome.getName())
				{
					distanceList.setChromosome(chromosome);
					distanceList.load();
					return distanceList;
				}					
			}
			throw new RuntimeException(usersChromosome + " not found in the database!");
		}
		
		return distanceList;
	
	}


	private static DistanceList initializeTandemGraph(String[] args)
	{
				JPanel loadingPanel = new JPanel();
				loadingPanel.add(getAboutButton(), BorderLayout.CENTER);

				boolean undecorated = true;
				BusyDialog busyDialog = new BusyDialog(null, loadingPanel, undecorated);
				busyDialog.setLoading(true);
				DistanceList distanceList = loadDistanceList(args);
				busyDialog.dispose();
				return distanceList;
	}

	public static JButton getAboutButton()
	{
		JButton dnaButton = SwingUtil.createJButtonfromImgUrl("images/start-screen.gif");  
		dnaButton.addActionListener(new ActionListener()
		{

			@Override 
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					java.awt.Desktop.getDesktop().browse(new URI("http://tandem.sci.brooklyn.cuny.edu/")); 
				}
				catch(Exception ex){}
			}

		});
		return dnaButton;
	}
}
