package edu.cuny.brooklyn.tandem.view.widgets;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JToolBar;

import edu.cuny.brooklyn.tandem.controller.graph.GraphRangeSelector;
import edu.cuny.brooklyn.tandem.controller.widgets.TextRangeSelectorController;
import edu.cuny.brooklyn.tandem.model.Range;

public class NavigatorToolbar extends JToolBar implements ActionListener
{
    
    private final TextRangeSelectorView textRangeSelectorView_;
    private final GraphRangeSelector    graphRangeSelector_;
    
    public NavigatorToolbar(TextRangeSelectorController textRangeSelectorController, GraphRangeSelector graphRangeSelector, GraphShifterView shifterView)
    {
        graphRangeSelector_ = graphRangeSelector;
        
        setLayout(new BorderLayout());
        
        textRangeSelectorView_ = new TextRangeSelectorView(textRangeSelectorController.getLimitedRange(), textRangeSelectorController);
        textRangeSelectorView_.getClearButton().addActionListener(this);
        textRangeSelectorView_.getZoomButton().addActionListener(this);
        
        add(shifterView.getLeftButton(), BorderLayout.WEST);
        add(textRangeSelectorView_, BorderLayout.CENTER);
        add(shifterView.getRightButton(), BorderLayout.EAST);
        
        graphRangeSelector.setTextRangeSelectorView(textRangeSelectorView_);
        
        addComponentListener(new ComponentAdapter()
        {
            @Override public void componentShown(ComponentEvent e)
            {
                super.componentShown(e);
                textRangeSelectorView_.setFields();
            }
        });
        
    }
    
    public TextRangeSelectorView getTextRangeSelectorView()
    {
        return textRangeSelectorView_;
    }
    
    public void setFields()
    {
        textRangeSelectorView_.setFields();
    }
    
    public void setFields(Range r)
    {
        textRangeSelectorView_.setFields(r);
    }
    
    public void actionPerformed(ActionEvent e)
    {
        graphRangeSelector_.clear();
    }
}
