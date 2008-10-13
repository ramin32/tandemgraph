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

import edu.cuny.brooklyn.tandem.controller.widgets.MenuBarController;
import edu.cuny.brooklyn.tandem.model.Chromosome;
import edu.cuny.brooklyn.tandem.model.JdbcTandemDao;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuBarView extends JMenuBar implements ActionListener
{
    private final JMenu fileMenu_;
    private final JMenu aboutMenu_;
    private final JMenu dbOpen_;
    private final List<JMenuItem> chromosomeMenuItems_;
    private Map<String, Chromosome> chromosomes_;

    private final JMenuItem clear_;
    private final JMenuItem exit_;

    private final JMenuItem usage_;
    private final JMenuItem license_;
    private final JMenuItem about_;

    private final JFrame containingFrame_;
    private final MenuBarController menuBarController_;

    private JdbcTandemDao jdbcTandemDao_;

    public MenuBarView(JFrame containingFrame, MenuBarController menuBarController)
    {
        menuBarController_ = menuBarController;
        containingFrame_ = containingFrame;
        // Instantiate Menu's
        fileMenu_ = new JMenu("File");
        aboutMenu_ = new JMenu("About");

        // Instantiate fileMenu's items
        dbOpen_ = new JMenu("Open Chromosome");
        chromosomeMenuItems_ = new ArrayList<JMenuItem>();

        new Thread(new Runnable()
        {
            public void run()
            {
                jdbcTandemDao_ = JdbcTandemDao.getInstance();
                chromosomes_ = new HashMap<String, Chromosome>();

                for (Chromosome chromosome : jdbcTandemDao_.getAllChromosomes())
                {
                    chromosomes_.put(chromosome.getName(), chromosome);
                    JMenuItem menuItem = new JMenuItem(chromosome.getName());
                    chromosomeMenuItems_.add(menuItem);
                    menuItem.addActionListener(MenuBarView.this);
                    dbOpen_.add(menuItem);
                }
            }
        });


        clear_ = new JMenuItem("Clear");
        exit_ = new JMenuItem("Exit");

        // Instantiate aboutMenu's items
        usage_ = new JMenuItem("Usage");
        license_ = new JMenuItem("License");
        about_ = new JMenuItem("About");

        clear_.addActionListener(this);
        exit_.addActionListener(this);

        usage_.addActionListener(this);
        license_.addActionListener(this);
        about_.addActionListener(this);

        // Add fileMenu's items
        fileMenu_.add(dbOpen_);
        fileMenu_.add(clear_);
        fileMenu_.add(exit_);

        aboutMenu_.add(usage_);
        aboutMenu_.add(license_);
        aboutMenu_.add(about_);


        add(fileMenu_);
        add(aboutMenu_);

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
        else if (source == license_)
        {
            menuBarController_.showLicense(containingFrame_);
        }
        else // Otherwise a chromosome name has been selected!
        {
            for (JMenuItem menuItem : chromosomeMenuItems_)
            {
                if (source == menuItem)
                {
                    menuBarController_.openChromosome(containingFrame_, chromosomes_.get(menuItem.getText()));
                }
            }
        }
    }
}
