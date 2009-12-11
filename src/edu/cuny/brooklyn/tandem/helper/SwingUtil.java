/************************************************************************
 * SwingHelper.java Helper class for various miscellaneous Swing/AWT
 * functionality Author: Ramin Rakhamimov Brooklyn College Research Project
 * Under the supervision of Professor Sokol
 ************************************************************************/
package edu.cuny.brooklyn.tandem.helper;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

public class SwingUtil
{
    private static final Logger logger_ = Logger.getLogger(SwingUtil.class);
    public static final int DEFAULT_INSET_SIZE = 20;
    public static final Insets DEFAULT_INSETS = new Insets(DEFAULT_INSET_SIZE, DEFAULT_INSET_SIZE, DEFAULT_INSET_SIZE, DEFAULT_INSET_SIZE);
    public static final boolean DEFAULT_SCROLLABLE = true;
    public static final double HALF = 2.0;
	public static final Font courierFont_ = new Font("Courier", Font.PLAIN, 12);
    
    /**
     * Returns a random color.
     */
    public static Color getRandColor()
    {
        return new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255));
    }
    
    public static Font getCourierFont()
    {
    	return courierFont_ ;
    }
    
    public static Color getRandColor(int threashold)
    {
        if (threashold < 0 || threashold > 255)
            throw new IllegalArgumentException("Threashold is not between 0 and 255");
        int secondOperand = 255 - threashold;
        return new Color((int) (Math.random() * secondOperand) + threashold, (int) (Math.random() * secondOperand) + threashold, (int) (Math.random() * secondOperand) + threashold);
    }
    
    public static void showDialogsforUncaughtExceptionsInFrame(
            final JFrame frame)
    {
        // Handle all uncaught exceptions, displaying errors with Message
        // Dialogs...
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler()
        {
            public void uncaughtException(Thread t, Throwable ex)
            {
                logger_.error(ex.getMessage(), ex);
                
                if (ex.getMessage() == null)
                    ex.printStackTrace();
                else
                    JOptionPane.showMessageDialog(frame, ex.getMessage(), "Warning!", JOptionPane.ERROR_MESSAGE);
            }
            
        });
    }
    
    public static JButton createJButtonfromImgUrl(String imagePath)
    {
        Image imgUrl = Toolkit.getDefaultToolkit().getImage(SwingUtil.class.getClassLoader().getResource(imagePath));
        if (imgUrl == null)
            throw new NullPointerException("imgUrl is null");
        ImageIcon icon = new ImageIcon(imgUrl);
        return new JButton(icon);
    }
    
    public static JComponent createStringTextArea(String string, Insets insets, boolean scrollable)
    {
        JTextArea textArea = new JTextArea();
        textArea.setText(string);
        textArea.setEditable(false);
        textArea.setBackground(Color.LIGHT_GRAY);
        
        if (insets == null)
            textArea.setMargin(DEFAULT_INSETS);
        else
            textArea.setMargin(insets);
        
        if (scrollable)
        {
            int scrollPaneHeight = Math.min(500, getTextableHeight(textArea));
            JScrollPane scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setPreferredSize(new Dimension(500, scrollPaneHeight));
            scrollPane.scrollRectToVisible(new Rectangle(0,0));
            return scrollPane;
        }
        return textArea;
    }
    
    public static int getTextableHeight(JTextArea textArea)
    {
        int textableHeight = textArea.getLineCount() * textArea.getFontMetrics(textArea.getFont()).getHeight();
        Insets insets = textArea.getInsets();
        if (insets != null)
            textableHeight += insets.top + insets.bottom;
        
        return textableHeight;
    }
    
    public static JComponent createStringTextArea(String string)
    {
        return createStringTextArea(string, DEFAULT_INSETS, DEFAULT_SCROLLABLE);
    }
    
    public static JComponent createFileTextArea(String inputFile,
            Insets insets, boolean scrollable)
    {
        String usageString = IOUtil.getStringFromFile(inputFile);
        return createStringTextArea(usageString, insets, scrollable);
    }
    
    public static JComponent createFileTextArea(String string)
    {
        return createFileTextArea(string, DEFAULT_INSETS, DEFAULT_SCROLLABLE);
    }
    
    public static void setBusyCursor(Component component, boolean isBusy)
    {
        if (isBusy)
            component.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        else
            component.setCursor(Cursor.getDefaultCursor());
    }
    
    public static JSeparator getVerticalSeparator()
    {
        JSeparator separator = new JSeparator(JSeparator.VERTICAL);
        separator.setSize(5, 25);
        return separator;
    }
    
    public static JSeparator getHorizontalSeparator()
    {
        JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
        separator.setSize(10, 10);
        return separator;
    }
    
    public static JFrame createAndShowTestFrame(final JComponent jComponent)
    {
        final JFrame frame = new JFrame("Test");
        
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                frame.setLayout(new BorderLayout());
                frame.add(jComponent, BorderLayout.CENTER);
                frame.setSize(500, 500);
                frame.setVisible(true);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        });
        
        return frame;
    }
    
    public static void centralizeComponent(Component component, Component otherComponent)
    {
        
        Dimension othersDimension = null;
        Point othersLocation = null;
        if (otherComponent == null || !otherComponent.isVisible())
        {
            othersDimension = Toolkit.getDefaultToolkit().getScreenSize();
            othersLocation = new Point(0,0);
        }
        else
        {
            othersDimension = otherComponent.getSize();
            othersLocation = otherComponent.getLocation();
        }   
        Point centerPoint = new Point((int) Math.round(othersDimension.width / HALF  - component.getWidth() / HALF) + othersLocation.x,
                                      (int) Math.round(othersDimension.height / HALF  - component.getHeight() / HALF)  + othersLocation.y);
        component.setLocation(centerPoint);
    }
    
    public static Image getImage(String path)
    {
        return Toolkit.getDefaultToolkit().getImage(SwingUtil.class.getClassLoader().getResource(path));
    }
}
