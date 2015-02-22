
package pl.aroch.ejtracker.dialogs.projects;

import javax.swing.JDialog;
import javax.swing.JFrame;
import pl.aroch.ejtracker.basic.TtDialog;
import pl.aroch.ejtracker.panels.projects.ProjectsListPanel;

/**
 * Okno z listą projektów
 * @author Arkadiusz Roch
 */
public class ProjectsList extends JDialog implements TtDialog 
{
    /**
     * Domyślny konstruktor
     */
    public ProjectsList()
    {
        super(new JFrame(), true);
    }

    /**
     * Metoda inicjująca okno
     */
    @Override
    public void init()
    {
        this.setSize(500, 400);
        
        this.setResizable(true);
        
        this.setLocationRelativeTo(null);
        
        ProjectsListPanel panel = new ProjectsListPanel();
        panel.init();
        
        this.add(panel);
        
        this.setTitle("Lista projektów - Ejtracker v.0.1.1");
    }
}
