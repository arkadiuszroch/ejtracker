
package pl.aroch.ejtracker.dialogs.projects;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import pl.aroch.ejtracker.basic.TtDialog;
import pl.aroch.ejtracker.panels.projects.ProjectEditPanel;

/**
 * Okno umożliwiające dodawanie oraz edycję projektu
 * @author Arkadiusz Roch
 */
public class ProjectEdit extends JDialog implements TtDialog
{
    /**
     * Identyfikator projektu
     */
    private Integer _id;
    
    /**
     * Domyślny konstruktor
     */
    public ProjectEdit()
    {  
        super(new JFrame(), true);
        
        this._id = 0;
    }
    
    /**
     * Konstruktor przyjmujący jako parametr identyfikator projektu
     * @param id Identyfikator projektu
     */
    public ProjectEdit(Integer id)
    {
        super(new JFrame(), true);
        
        this._id = id;
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
        
        final ProjectEditPanel panel = new ProjectEditPanel(this, this._id);
        panel.init();
        
        this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        
        this.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                panel.close();
            }
        });
        
        this.add(panel);
        
        this.setTitle(((this._id == 0) ? "Dodaj projekt" : "Edytuj projekt") + " - Ejtracker v.0.1.2");
    }
}
