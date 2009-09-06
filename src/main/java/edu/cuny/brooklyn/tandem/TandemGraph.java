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
import edu.cuny.brooklyn.tandem.model.JdbcTandemDao;
import edu.cuny.brooklyn.tandem.view.TandemGraphView;
import edu.cuny.brooklyn.tandem.view.widgets.BusyDialog;

public class TandemGraph
{
	private final static Logger logger_ = Logger.getLogger(TandemGraph.class);

	public static void main(final String[] args)
	{
		logger_.debug("Intializing TandemGraph...");
		initializeTandemGraph();

		logger_.debug("Obtaining instance of TandemGraphView...");
		TandemGraphView tandemGraphView = new TandemGraphView(args);

		logger_.debug("Invoking view...");
		SwingUtilities.invokeLater(tandemGraphView);
	}


	private static void initializeTandemGraph()
	{
		JPanel loadingPanel = new JPanel();
		loadingPanel.add(getAboutButton(), BorderLayout.CENTER);

		boolean undecorated = true;
		BusyDialog busyDialog = new BusyDialog(null, loadingPanel, undecorated);
		busyDialog.setLoading(true);
		JdbcTandemDao.getInstance().initialize();
		busyDialog.dispose();
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
