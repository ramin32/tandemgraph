/************************************************************************
 * MenuBarView.java
 *
 * Menu bar with file and about menus, for selecting files,clearing the
 * the JFrame and usage help.
 *
 * Author:
 * Ramin Rakhamimov
 * Brooklyn College Research Project
 * Under the supervion of Professor Sokol
 ************************************************************************/
package edu.cuny.brooklyn.tandem.view.widgets;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import edu.cuny.brooklyn.tandem.controller.widgets.MenuBarController;
import edu.cuny.brooklyn.tandem.helper.SqlConnectionFactory;
import edu.cuny.brooklyn.tandem.model.JdbcDistanceDao;

public class MenuBarView extends JMenuBar implements ActionListener
{
    private final JMenu fileMenu_;
    private final JMenu aboutMenu_;
    private final JMenu dbOpen_;
    private final ArrayList<JMenuItem> chromosomes_;
    private final JMenuItem clear_;
    private final JMenuItem exit_;

    private final JMenuItem usage_;
    private final JMenuItem about_;

    private final JFrame containingFrame_;
    private final MenuBarController menuBarController_;

    public MenuBarView(JFrame containingFrame,
                       MenuBarController menuBarController)
    {
        menuBarController_ = menuBarController;
        containingFrame_ = containingFrame;
        // Instantiate Menu's
        fileMenu_ = new JMenu("File");
        aboutMenu_ = new JMenu("About");

        // Instantiate fileMenu's items
        dbOpen_ = new JMenu("Open Chromosome");
        chromosomes_ = new ArrayList<JMenuItem>();
        addChromosomesToMenu(dbOpen_);
        clear_ = new JMenuItem("Clear");
        exit_ = new JMenuItem("Exit");

        // Instantiate aboutMenu's items
        usage_ = new JMenuItem("Usage");
        about_ = new JMenuItem("About");

        clear_.addActionListener(this);
        exit_.addActionListener(this);

        usage_.addActionListener(this);
        about_.addActionListener(this);

        // Add fileMenu's items
        fileMenu_.add(dbOpen_);
        fileMenu_.add(clear_);
        fileMenu_.add(exit_);

        aboutMenu_.add(usage_);
        aboutMenu_.add(about_);


        add(fileMenu_);
        add(aboutMenu_);

    }

    public final void addChromosomesToMenu(javax.swing.JMenu menu)
    {
        for (Map<String, Object> map: JdbcDistanceDao.getAllChromosomeNames())
        {
            JMenuItem menuItem = new JMenuItem((String) map.get("name"));
            chromosomes_.add(menuItem);
            menuItem.addActionListener(this);
            menu.add(menuItem);
        }

    }

    public void actionPerformed(ActionEvent e)
    {
        Object source = e.getSource();

        if (source == clear_)
        {
            menuBarController_.clear();
        }
        else if (source == exit_)
        {
            menuBarController_.exit(containingFrame_);
        }
        else if (source == usage_)
        {
            menuBarController_.showUsage(containingFrame_);
        }
        else if (source == about_)
        {
            menuBarController_.showAbout(containingFrame_);
        }
        else // Otherwise a chromosome name has been selected!
        {
            for (JMenuItem menuItem : chromosomes_)
            {
                if (source == menuItem)
                {
                    menuBarController_.openChromosome(containingFrame_, menuItem.getText());
                }
            }
        }
    }
}
