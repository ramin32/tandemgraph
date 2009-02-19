package edu.cuny.brooklyn.tandem.view.widgets;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import edu.cuny.brooklyn.tandem.helper.InfoFiles;
import edu.cuny.brooklyn.tandem.helper.SwingUtil;

public class BusyDialog extends JDialog implements ActionListener, FocusListener
{
    private final Component busyComponent_;
    private final JProgressBar progressBar_;
    private JButton closeButton_;
    
    public BusyDialog(Component busyComponent, JComponent message, boolean unDecorated)
    {
        setIconImage(SwingUtil.getImage("images/dna-icon.gif"));
        setUndecorated(unDecorated);
        busyComponent_ = busyComponent;
        progressBar_ = new JProgressBar();
        progressBar_.setStringPainted(true);
        
        if(!unDecorated)
        {
            closeButton_ = new JButton("Close");
            closeButton_.setEnabled(false);
            closeButton_.addActionListener(this);
        }
        
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.LINE_AXIS));
        southPanel.add(progressBar_);
        if(!unDecorated)
            southPanel.add(closeButton_);
        
        add(message, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
        pack();
        setTitle("Loading...");
        
    }
    
    public void setLoading(boolean isLoading)
    {

        SwingUtil.centralizeComponent(this, busyComponent_);
        setVisible(true);
        
        progressBar_.setIndeterminate(isLoading);
        
        String progressBarString = isLoading ? "Loading..." : "Done!";
        progressBar_.setString(progressBarString);
        
        if(closeButton_ != null)
            closeButton_.setEnabled(!isLoading);
        
        SwingUtil.setBusyCursor(this, isLoading);
        
        if (!isLoading)
            progressBar_.setValue(100);
        
        if (busyComponent_ != null)
        {
            SwingUtil.setBusyCursor(busyComponent_, isLoading);
        }
    }
    
   
    
    @Override 
    public void actionPerformed(ActionEvent e)
    {
        setVisible(false);
    }
    

    
    @Override 
    public void focusGained(FocusEvent e){}
    
    @Override 
    public void focusLost(FocusEvent e)
    {
        requestFocus();
    }
    
    
    public static void main(String[] args)
    {
        
        BusyDialog busyDialog = new BusyDialog(null, SwingUtil.createFileTextArea(InfoFiles.LOADING),true);
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
}
