package edu.cuny.brooklyn.tandem.view.widgets;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import com.sun.org.apache.xerces.internal.util.DraconianErrorHandler;

import edu.cuny.brooklyn.tandem.controller.widgets.TriangleTrapezoidSelectorController;
import edu.cuny.brooklyn.tandem.model.DistanceList;
import edu.cuny.brooklyn.tandem.model.DrawType;

public class TriangleTrapezoidSelector extends JPanel implements ActionListener
{
	private final JRadioButton triangleButton_;
	private final JRadioButton trapezoidButton_;
	private final ButtonGroup buttonGroup_;
	private final TriangleTrapezoidSelectorController controller_;
	
	public TriangleTrapezoidSelector(TriangleTrapezoidSelectorController triangleTrapezoidSelectorController_)
	{
		controller_ = triangleTrapezoidSelectorController_;
		triangleButton_ = new JRadioButton("Triangle");
		trapezoidButton_ = new JRadioButton("Trapezoid");
		buttonGroup_ = new ButtonGroup();
		
		triangleButton_.addActionListener(this);
		trapezoidButton_.addActionListener(this);
		
		if(controller_.getDistances().getDrawType() == DrawType.TRIANGLE)
			triangleButton_.setSelected(true);
		else
			trapezoidButton_.setSelected(true);
		
		
		buttonGroup_.add(triangleButton_);
		buttonGroup_.add(trapezoidButton_);

		add(triangleButton_);
		add(trapezoidButton_);
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		
		if(e.getSource() == triangleButton_)
			controller_.switchDrawType(DrawType.TRIANGLE);
		else if(e.getSource() == trapezoidButton_)
			controller_.switchDrawType(DrawType.TRAPEZOID);
		
		
	}

}
