package edu.cuny.brooklyn.tandem.controller.graph.listener;

import java.awt.Dimension;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.apache.log4j.Logger;

import edu.cuny.brooklyn.tandem.helper.SwingUtil;
import edu.cuny.brooklyn.tandem.model.Distance;
import edu.cuny.brooklyn.tandem.model.DistanceList;
import edu.cuny.brooklyn.tandem.model.JdbcTandemDao;
import edu.cuny.brooklyn.tandem.view.GraphPanelView;

public class RepeatListenerDelegator 
{
	
	private final static Logger logger_ = Logger.getLogger(RepeatListenerDelegator.class);
	private final RepeatClickListener repeatClickListener_;
	private final RepeatKeyListener repeatKeyListener_;
	
	private final DistanceList distances_;
	private final JFrame frame_;
	private final JPanel mainPanel_;
	


    private static final int TABLE = 0;
    private static final int MATCH = 1;
    private static final int TABLE_HEIGHT = 500;
    private static final int TABLE_WIDTH = 500;
    private static final int WIDTH_SUPPLEMENT = 5;

	public RepeatListenerDelegator(DistanceList distances, JFrame frame, JPanel mainPanel)
	{
		distances_ = distances;
		frame_ = frame;
		mainPanel_ = mainPanel;
		
		repeatClickListener_ = new RepeatClickListener(distances, mainPanel, this);
		repeatKeyListener_ = new RepeatKeyListener(distances, this);
	}
	
	public void installListeners(GraphPanelView graphPanelView)
	{
        frame_.addKeyListener(repeatKeyListener_);
        repeatClickListener_.install(graphPanelView);
        logger_.debug("Listeners installed!");
	}

	void showAlignmentTable()
	{
		Distance selectedDistance = distances_.getSelectedDistance();
		if(selectedDistance == null)
			return;

		Object[] alignmentTableAndMaxMatch = getAlignmentTableAndMaxMatch(selectedDistance);
		JTable alignmentTable = (JTable) alignmentTableAndMaxMatch[TABLE];
		String maxMatch = (String) alignmentTableAndMaxMatch[MATCH];

		alignmentTable.setFont(SwingUtil.getCourierFont());
		JScrollPane scrollPane = new JScrollPane(alignmentTable, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		JDialog dialog = new JDialog();
		dialog.add(scrollPane);
		dialog.pack();
		SwingUtil.centralizeComponent(dialog, frame_);
		dialog.setTitle(selectedDistance.toString());
		dialog.setIconImage(SwingUtil.getImage("images/dna-icon.gif"));

		alignmentTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		int width = alignmentTable.getGraphics().getFontMetrics().stringWidth(maxMatch)  + WIDTH_SUPPLEMENT;
		alignmentTable.getColumnModel().getColumn(1).setPreferredWidth(width);


		int tableWidth = Math.min(TABLE_WIDTH, alignmentTable.getWidth());
		int tableHeight = Math.min(TABLE_HEIGHT, alignmentTable.getHeight());
		alignmentTable.setPreferredScrollableViewportSize(new Dimension(tableWidth, tableHeight));
//		dialog.pack();
		dialog.setVisible(true);
	}

	Object[] getAlignmentTableAndMaxMatch(Distance selectedDistance)
	{        
		String[] headers = {"Start","Match","End"};
		String alignment = JdbcTandemDao.getInstance().getAlignmentByDistance(selectedDistance);
		String[] alignmentLines = alignment.split("\n");
		String[][] alignmentTokens = new String[alignmentLines.length][];
		String maxMatch = "";

		for(int i = 0; i < alignmentLines.length; i++)
		{
			alignmentTokens[i] = new String[3];
			String[] tokens =  alignmentLines[i].split("\\s+");
			if(tokens.length > 1 && tokens[1].length() > maxMatch.length())
				maxMatch = tokens[1];
			for(int j = 0; j < tokens.length; j++)
				alignmentTokens[i][j] = tokens[j];

		}
		return new Object[]{new JTable(alignmentTokens, headers), maxMatch};
	}
	
	void repaintMainPanel()
	{
		mainPanel_.repaint();
	}

}
