package edu.cuny.brooklyn.tandem.view.widgets;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JPanel;
import javax.swing.JToolBar;

import edu.cuny.brooklyn.tandem.controller.graph.GraphRangeSelector;
import edu.cuny.brooklyn.tandem.controller.widgets.TextRangeSelectorController;
import edu.cuny.brooklyn.tandem.controller.widgets.TriangleTrapezoidSelectorController;
import edu.cuny.brooklyn.tandem.model.Range;

public class NavigatorToolbar extends JToolBar implements ActionListener
{
    
    private final TextRangeSelectorView textRangeSelectorView_;
    private final GraphRangeSelector graphRangeSelector_;
    private final TriangleTrapezoidSelector trapezoidTriangleSelector_;
    private final GraphShifterView shifterView_;
    
    public NavigatorToolbar(TextRangeSelectorController textRangeSelectorController, 
    						GraphRangeSelector graphRangeSelector, 
    						GraphShifterView shifterView,
    						TriangleTrapezoidSelectorController triangleTrapezoidSelectorController_)
    {
        graphRangeSelector_ = graphRangeSelector;
        shifterView_ = shifterView;
        
        setLayout(new BorderLayout());
        
        textRangeSelectorView_ = new TextRangeSelectorView(textRangeSelectorController.getLimitedRange(), textRangeSelectorController);
        textRangeSelectorView_.getClearButton().addActionListener(this);
        textRangeSelectorView_.getZoomButton().addActionListener(this);
        trapezoidTriangleSelector_ = new TriangleTrapezoidSelector(triangleTrapezoidSelectorController_);
        
        add(shifterView.getLeftButton(), BorderLayout.WEST);
        
        JPanel centerPanel = new JPanel();
        centerPanel.add(textRangeSelectorView_);
        centerPanel.add(trapezoidTriangleSelector_);
        add(centerPanel, BorderLayout.CENTER);
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
