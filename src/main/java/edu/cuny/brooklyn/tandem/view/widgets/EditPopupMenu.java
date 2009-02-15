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

package edu.cuny.brooklyn.tandem.view.widgets;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class EditPopupMenu
{
    private final static String SELECT_ALL_STRING = "Select All";
    private final static String COPY_STRING = "Copy";
    private final static String PASTE_STRING = "Paste";
    
    private final JPopupMenu editPopupMenu_;
    private final JMenuItem selectAllMenuItem_;
    private final JMenuItem copyMenuItem_;
    private final JMenuItem pasteMenuItem_;
    private final MouseListener popupListener = new MouseAdapter()
    {};
    
    public EditPopupMenu()
    {
        editPopupMenu_ = new JPopupMenu();
        
        selectAllMenuItem_ = new JMenuItem(SELECT_ALL_STRING);
        copyMenuItem_ = new JMenuItem(COPY_STRING);
        pasteMenuItem_ = new JMenuItem(PASTE_STRING);
        
        editPopupMenu_.add(selectAllMenuItem_);
        editPopupMenu_.add(copyMenuItem_);
        editPopupMenu_.add(pasteMenuItem_);
    }
    
    public JPopupMenu getMenu()
    {
        return editPopupMenu_;
    }
    
    public static void main(String... args)
    {
        JFrame frame = new JFrame("Some Frame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setVisible(true);
        
        new EditPopupMenu().getMenu().show(frame, 25, 25);
        
    }
    
}