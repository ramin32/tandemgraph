package edu.cuny.brooklyn.tandem.view;

import edu.cuny.brooklyn.tandem.controller.widgets.GraphShifterController;
import edu.cuny.brooklyn.tandem.controller.widgets.MenuBarController;
import edu.cuny.brooklyn.tandem.controller.widgets.TextRangeSelectorController;
import edu.cuny.brooklyn.tandem.controller.widgets.ZoomSliderController;
import edu.cuny.brooklyn.tandem.helper.SwingUtil;
import edu.cuny.brooklyn.tandem.model.DistanceList;
import edu.cuny.brooklyn.tandem.view.widgets.GraphShifterView;
import edu.cuny.brooklyn.tandem.view.widgets.MenuBarView;
import edu.cuny.brooklyn.tandem.view.widgets.NavigatorToolbar;
import edu.cuny.brooklyn.tandem.view.widgets.ZoomSliderView;

import javax.swing.*;
import java.awt.*;

public class TandemGraphView implements Runnable
{
    private final JFrame frame_;
    private final MenuBarView menuBarView_;
    private final GraphPanelView graphPanelView_;
    private final ZoomSliderView zoomSliderView_;

    private final DistanceList distanceList_;

    private final NavigatorToolbar navigatorToolbar_;
    private final GraphShifterController graphShifterController_;
    private final TextRangeSelectorController textRangeSelectorController_;
    private final ZoomSliderController zoomSliderController_;

    private final String FRAME_TITLE = "Tandem Repeats Grapher";

    public TandemGraphView(DistanceList distanceList)
    {
        Runnable runnable = getViewUpdaterRunnable();
        distanceList_ = distanceList;
        frame_ = new JFrame(FRAME_TITLE);
        graphPanelView_ = new GraphPanelView(distanceList, runnable);

        menuBarView_ = new MenuBarView(frame_, new MenuBarController(distanceList, runnable));

        // Controllers
        textRangeSelectorController_ = new TextRangeSelectorController(distanceList.getLimitedRange(), runnable);
        graphShifterController_ = new GraphShifterController(distanceList.getLimitedRange(), runnable);

        GraphShifterView graphShifterView_ = new GraphShifterView(graphShifterController_);


        navigatorToolbar_ = new NavigatorToolbar(textRangeSelectorController_, graphPanelView_.getGraphicalRangeSelector(), graphShifterView_);

        zoomSliderController_ = new ZoomSliderController(distanceList.getLimitedRange(), runnable, navigatorToolbar_.getTextRangeSelectorView());

        zoomSliderView_ = new ZoomSliderView(zoomSliderController_, graphShifterController_);
    }

    public void run()
    {
        frame_.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        SwingUtil.showDialogsforUncaughtExceptionsInFrame(frame_);
        frame_.setSize(1024, 768);
        frame_.setJMenuBar(menuBarView_);

        JPanel southPanel = new JPanel(new BorderLayout());
        frame_.add(southPanel, BorderLayout.SOUTH);

        frame_.add(graphPanelView_, BorderLayout.CENTER);

        frame_.add(zoomSliderView_, BorderLayout.EAST);
        frame_.add(navigatorToolbar_, BorderLayout.SOUTH);

        frame_.setVisible(true);
        getViewUpdaterRunnable().run();

        navigatorToolbar_.getTextRangeSelectorView().setFields(distanceList_.getLimitedRange().getLocal());

        // set minimal size of local range by font size
        int fontWidth = graphPanelView_.getGraphics().getFontMetrics().charWidth('A');
        int minLocalRange = graphPanelView_.getWidth() * fontWidth;
        distanceList_.getLimitedRange().setMinLocalRange(minLocalRange);
    }

    public Runnable getViewUpdaterRunnable()
    {
        return new Runnable()
        {
            public void run()
            {

                if (distanceList_.isEmpty())
                {
                    if (zoomSliderView_ != null) zoomSliderView_.setVisible(false);
                    if (navigatorToolbar_ != null) navigatorToolbar_.setVisible(false);
                    if (frame_ != null) frame_.setTitle(FRAME_TITLE);
                }
                else
                {
                    if (distanceList_.getChromosome() != null)
                        frame_.setTitle(FRAME_TITLE + " - " + distanceList_.getChromosome().getName());


                    if (zoomSliderView_ != null)
                    {
                        zoomSliderView_.setVisible(true);
                        int percentage = distanceList_.getLimitedRange().getLocalRangePercentage();
                        zoomSliderView_.removeListener();
                        zoomSliderView_.setSliderValue(percentage);
                        zoomSliderView_.installListener();
                    }

                    if (navigatorToolbar_ != null) navigatorToolbar_.setVisible(true);
                }

                if (frame_ != null)
                {
                    frame_.repaint();
                    frame_.setVisible(true); // Needed to really get rid of widgets
                }
            }
        };
    }

    public JFrame getFrame()
    {
        return frame_;
    }
}
