package edu.cuny.brooklyn.tandem.controller.graph.listener;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import org.apache.log4j.Logger;

import edu.cuny.brooklyn.tandem.model.DistanceList;

public class RepeatKeyListener extends KeyAdapter
{
    private static final Logger logger_ = Logger.getLogger(RepeatKeyListener.class);
    private final DistanceList distances_;
    private final RepeatListenerDelegator repeatListenerDelegator_;
	
    RepeatKeyListener(DistanceList distances, RepeatListenerDelegator delegator)
    {
        distances_ = distances;
        repeatListenerDelegator_ = delegator;
        logger_.debug("I am instantiated!");
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        Integer selectedIndex = distances_.getSelectedIndex();
        if(selectedIndex == null)
        	return;
        
        if(e.getKeyCode() == KeyEvent.VK_LEFT)
        {
        	distances_.setSelectedIndex(selectedIndex - 1);
        }
        else if(e.getKeyCode() == KeyEvent.VK_RIGHT)
        {
        	distances_.setSelectedIndex(selectedIndex + 1);
        }
        else if(e.getKeyCode() == KeyEvent.VK_ENTER)
        {
        	repeatListenerDelegator_.showAlignmentTable();
        	return;
        }
        
        repeatListenerDelegator_.repaintMainPanel();
    }
}
