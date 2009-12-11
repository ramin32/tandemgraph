/***********************************************************************
 * TextualRangeSelectorView.java Extends JToolBar, adds 2 text fields depicting
 * starting and ending points of a range and zoom button. Modifies the
 * LimitedRange object of the containing panel and repaints it upon an action.
 * Author: Ramin Rakhamimov ramin32@gmail.com http://www.ramrak.net
 *********************************************************************/

package edu.cuny.brooklyn.tandem.view.widgets;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.cuny.brooklyn.tandem.controller.widgets.TextRangeSelectorController;
import edu.cuny.brooklyn.tandem.model.LimitedRange;
import edu.cuny.brooklyn.tandem.model.Range;

public class TextRangeSelectorView extends JPanel implements ActionListener
{
    public static final int COLUMNS = 10;
    private final JTextField startField_;
    private final JTextField endField_;
    private final JButton zoomButton_;
    private final LimitedRange limitedRange_;
    
    private final TextRangeSelectorController rangeSelectorController_;
    private final JButton clearButton_;
    
    public TextRangeSelectorView(LimitedRange range, TextRangeSelectorController textualRangeSelectorController)
    {
        limitedRange_ = range;
        rangeSelectorController_ = textualRangeSelectorController;
        
        JLabel startLabel = new JLabel("Start ");
        JLabel endLabel = new JLabel(" End ");
        startField_ = new JTextField(COLUMNS);
        endField_ = new JTextField(COLUMNS);
        clearButton_ = new JButton("Clear");
        zoomButton_ = new JButton("Zoom");
        
        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.LINE_AXIS));
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.LINE_AXIS));
        leftPanel.add(startLabel);
        leftPanel.add(startField_);
        
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.LINE_AXIS));
        rightPanel.add(endLabel);
        rightPanel.add(endField_);
        
        fieldsPanel.add(leftPanel);
        fieldsPanel.add(rightPanel);
        
        add(fieldsPanel);
        add(clearButton_);
        add(zoomButton_);
        
        startField_.addActionListener(this);
        endField_.addActionListener(this);
        zoomButton_.addActionListener(this);
        clearButton_.addActionListener(this);
        
    }
    
    public void setFields()
    {
        setFields(limitedRange_.getLocal());
    }
    
    public void setFields(Range r)
    {
        if (r == null)
            return;
        startField_.setText("" + r.getMin());
        endField_.setText("" + r.getMax());
    }
    
    public void clearFields()
    {
        startField_.setText("");
        endField_.setText("");
    }
    
    public int getStartValue()
    {
        return Integer.parseInt(startField_.getText().trim());
    }
    
    public int getEndValue()
    {
        return Integer.parseInt(endField_.getText().trim());
    }
    
    public void setStartValue(String value)
    {
        startField_.setText(value);
    }
    
    public void setEndValue(String value)
    {
        endField_.setText(value);
    }
    
    public void actionPerformed(ActionEvent event)
    {
        if (event.getSource() == clearButton_)
        {
            setFields(limitedRange_.getLocal());
            return;
        }
        
        // Otherwise zoom in
        String start = startField_.getText();
        String end = endField_.getText();
        rangeSelectorController_.zoom(start, end);
    }
    
    public JButton getClearButton()
    {
        return clearButton_;
    }
    
    public JButton getZoomButton()
    {
        return zoomButton_;
    }
}
