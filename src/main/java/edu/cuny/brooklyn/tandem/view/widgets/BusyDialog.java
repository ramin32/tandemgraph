package edu.cuny.brooklyn.tandem.view.widgets;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import edu.cuny.brooklyn.tandem.helper.IOUtil;
import edu.cuny.brooklyn.tandem.helper.InfoFiles;
import edu.cuny.brooklyn.tandem.helper.SwingUtil;

public class BusyDialog extends JDialog implements ActionListener, FocusListener
{
	private final Component busyComponent_;
	private final JProgressBar progressBar_;
	private final JButton closeButton_;

	public BusyDialog(Component busyComponent, String messageFileName) {
		busyComponent_ = busyComponent;
		JComponent messageTextArea = SwingUtil.createFileTextArea(messageFileName);
		progressBar_ = new JProgressBar();
		progressBar_.setStringPainted(true);
		
		closeButton_ = new JButton("Close");
		closeButton_.setEnabled(false);
		closeButton_.addActionListener(this);
		
		JPanel southPanel = new JPanel();
		southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.LINE_AXIS));
		southPanel.add(progressBar_);
		southPanel.add(closeButton_);
		
		add(messageTextArea, BorderLayout.CENTER);
		add(southPanel, BorderLayout.SOUTH);
		pack();
		
	
	}

	public void setLoading(boolean isLoading)
	{
		setVisible(true);

		centralizeDialog();
		progressBar_.setIndeterminate(isLoading);
		
		String progressBarString = isLoading? "Loading..." : "Done!" ;
		progressBar_.setString(progressBarString);
		
		closeButton_.setEnabled(!isLoading);
		
		SwingUtil.setBusyCursor(this, isLoading);
		
		if(!isLoading)
			progressBar_.setValue(100);
		
		if(busyComponent_ != null)
		{
			SwingUtil.setBusyCursor(busyComponent_, isLoading);
		}
	}




	private void centralizeDialog()
	{
		if(busyComponent_ == null)
			return;
		Point framePoint = busyComponent_.getLocationOnScreen();
		Point busyDialogPoint = new Point(framePoint.x + busyComponent_.getWidth()
				/ 2 - getWidth() / 2, framePoint.y
				+ busyComponent_.getHeight() / 2);

		setLocation(busyDialogPoint);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		setVisible(false);
	}
	
	public static void main(String[] args)
	{
		
		BusyDialog busyDialog = new BusyDialog(null, InfoFiles.LOADING);
		busyDialog.setVisible(true);
		busyDialog.setLoading(true);
		
		try
		{
			Thread.sleep(2000);
			busyDialog.setLoading(false);

			Thread.sleep(5000);
			System.exit(0);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
	}

	@Override
	public void focusGained(FocusEvent e)
	{		
	}

	@Override
	public void focusLost(FocusEvent e)
	{
		requestFocus();		
	}

}
