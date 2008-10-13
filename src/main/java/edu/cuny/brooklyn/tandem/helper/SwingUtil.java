/************************************************************************
 * SwingHelper.java
 *
 * Helper class for various miscellaneous Swing/AWT functionality
 *
 * Author:
 * Ramin Rakhamimov
 * Brooklyn College Research Project
 * Under the supervision of Professor Sokol
 ************************************************************************/
package edu.cuny.brooklyn.tandem.helper;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class SwingUtil
{

    private static final boolean DEFAULT_SCROLLABLE = true;
    private static final Insets DEFAULT_INSETS = new Insets(20, 20, 20, 20);

    /**
     * Returns a random color.
     */
    public static Color getRandColor()
    {
        return new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255));
    }

    public static Color getRandColor(int threashold)
    {
        if (threashold < 0 || threashold > 255)
            throw new IllegalArgumentException("Threashold is not between 0 and 255");
        int secondOperand = 255 - threashold;
        return new Color((int) (Math.random() * secondOperand) + threashold, (int) (Math.random() * secondOperand) + threashold, (int) (Math.random() * secondOperand) + threashold);
    }

    public static void showDialogsforUncaughtExceptionsInFrame(final JFrame frame)
    {
        // Handle all uncaught exceptions, displaying errors with Message
        // Dialogs...
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler()
        {
            public void uncaughtException(Thread t, Throwable ex)
            {
                if (ex.getMessage() == null) ex.printStackTrace();
                    // JOptionPane.showMessageDialog(frame,
                    // ex.getStackTrace(), "Warning!",
                    // JOptionPane.ERROR_MESSAGE);
                else
                {
                    JOptionPane.showMessageDialog(frame, ex.getMessage(), "Warning!", JOptionPane.ERROR_MESSAGE);

                }
            }

        });
    }

    public static JButton createJButtonfromImgUrl(URL imgUrl)
    {
        if (imgUrl == null) throw new NullPointerException("imgUrl is null");
        ImageIcon icon = new ImageIcon(imgUrl);
        return new JButton(icon);
    }


    public static JComponent createFileTextArea(String inputFile, Insets insets, boolean scrollable)
    {
        String usageString = IOUtil.getStringFromFile(inputFile);
        JTextArea textArea = new JTextArea(usageString);
        textArea.setSize(500,500);
        textArea.setEditable(false);
        textArea.setBackground(Color.LIGHT_GRAY);

        if (insets == null)
            textArea.setMargin(DEFAULT_INSETS);
        else
            textArea.setMargin(insets);

        if (scrollable)
            return new JScrollPane(textArea);
        return textArea;

    }

    public static JComponent createFileTextArea(String inputFile)
    {
        return createFileTextArea(inputFile, DEFAULT_INSETS, DEFAULT_SCROLLABLE);
    }


    public static void setBusyCursor(Component component, boolean isBusy)
    {
        if (isBusy) component.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        else component.setCursor(Cursor.getDefaultCursor());
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

    public static JFrame createAndShowTestFrame(final JPanel panel)
    {
        final JFrame frame = new JFrame("Test");

        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                panel.setPreferredSize(new Dimension(500, 500));
                frame.add(panel);
                frame.pack();
                frame.setVisible(true);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        });

        return frame;
    }
}
