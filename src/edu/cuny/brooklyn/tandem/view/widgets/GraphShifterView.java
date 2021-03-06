/**************************************************************
 * NavigatorView.java ScrollBar used for dragging the graph left or right.
 * Executes thread upon action. Author: Ramin Rakhamimov ramin32@gmail.com
 * http://www.ramrak.net
 *************************************************************/

package edu.cuny.brooklyn.tandem.view.widgets;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import edu.cuny.brooklyn.tandem.controller.widgets.GraphShifterController;
import edu.cuny.brooklyn.tandem.helper.SwingUtil;

public class GraphShifterView extends JPanel implements ActionListener
{
    private final JButton shiftLeft_;
    private final JButton shiftRight_;
    
    private final GraphShifterController shifterController_;
    
    public GraphShifterView(GraphShifterController graphShifterController)
    {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        shifterController_ = graphShifterController;
        
        shiftRight_ = SwingUtil.createJButtonfromImgUrl("images/Forward24.gif");
        shiftLeft_ = SwingUtil.createJButtonfromImgUrl("images/Back24.gif");
        
        shiftLeft_.setToolTipText("Shift Left");
        shiftRight_.setToolTipText("Shift Right");
        
        add(shiftLeft_);
        
        add(new JSeparator(SwingConstants.VERTICAL));
        add(shiftRight_);
        
        shiftLeft_.addActionListener(this);
        shiftRight_.addActionListener(this);
        
    }
    
    public JButton getLeftButton()
    {
        return shiftLeft_;
    }
    
    public JButton getRightButton()
    {
        return shiftRight_;
    }
    
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == shiftLeft_)
        {
            shifterController_.shiftLeft();
        }
        else if (e.getSource() == shiftRight_)
        {
            shifterController_.shiftRight();
        }
    }
}
