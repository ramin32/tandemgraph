/***************************************************************
 * ZoomSliderView.java
 *
 * Scrollbar for zooming in and out of the graph.
 *
 * Author:
 * Ramin Rakhamimov
 * ramin32@gmail.com
 * http://www.ramrak.net
 **************************************************************/
package edu.cuny.brooklyn.tandem.view.widgets;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.cuny.brooklyn.tandem.controller.widgets.GraphShifterController;
import edu.cuny.brooklyn.tandem.controller.widgets.ZoomSliderController;
import edu.cuny.brooklyn.tandem.helper.SwingUtil;

public class ZoomSliderView extends JPanel implements ChangeListener, ActionListener
{
	private final JSlider slider_;
	private final ZoomSliderController zoomSliderController_;

	private final JButton zoomInButton_;
	private final JButton zoomOutButton_;
	private final GraphShifterController graphShifterController_;

	public ZoomSliderView(ZoomSliderController zoomSliderController, GraphShifterController navigatorController)

	{
		super(new BorderLayout());

		zoomSliderController_ = zoomSliderController;
		graphShifterController_ = navigatorController;
		slider_ = new JSlider(JSlider.VERTICAL, 0, 100, 100);
		slider_.setInverted(true);

		// Turn on labels at major tick marks.
		slider_.setMajorTickSpacing(5);
		slider_.setMinorTickSpacing(1);
		slider_.setPaintTicks(true);
		// slider.setPaintLabels(true);
		slider_.setSnapToTicks(true);
		slider_.addChangeListener(this);

		// Create zoom icons
		zoomInButton_ = SwingUtil.createJButtonfromImgUrl(getClass().getClassLoader().getResource("ZoomIn24.gif"));
		zoomOutButton_ = SwingUtil.createJButtonfromImgUrl(getClass().getClassLoader().getResource("ZoomOut24.gif"));

		zoomInButton_.addActionListener(this);
		zoomOutButton_.addActionListener(this);

		// add everything
		add(zoomInButton_, BorderLayout.PAGE_START);
		add(slider_, BorderLayout.CENTER);
		add(zoomOutButton_, BorderLayout.PAGE_END);

	}

	public JSlider getSlider()
	{
		return slider_;
	}

	public void setSliderValue(int n)
	{
		slider_.setValue(n);
	}

	public void stateChanged(ChangeEvent event)
	{
		zoomSliderController_.setZoomPercentage(slider_.getValue());
		
	}

	public void actionPerformed(ActionEvent event)
	{
		if (event.getSource() == zoomInButton_)
			graphShifterController_.zoomIn();
		else if (event.getSource() == zoomOutButton_)
			graphShifterController_.zoomOut();

	}

	public void removeListener()
	{
		slider_.removeChangeListener(this);
	}

	public void installListener()
	{
		slider_.addChangeListener(this);
	}

}
