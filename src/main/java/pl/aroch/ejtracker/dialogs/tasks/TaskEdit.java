
package pl.aroch.ejtracker.dialogs.tasks;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import pl.aroch.ejtracker.basic.TtDialog;
import pl.aroch.ejtracker.panels.tasks.TaskEditPanel;

/**
 * Okno edycji zadania
 * @author Arkadiusz Roch
 */
public class TaskEdit extends JDialog implements TtDialog 
{
    /**
     * Identyfikator zadania
     */
    private Integer _id;
    
    /**
     * Konstruktor przyjmujący jako parametr identyfikator zadania
     * @param id Identyfikator zadania
     */
    public TaskEdit(Integer id)
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
        this.setSize(300, 400);
        
        this.setResizable(true);
        
        this.setLocationRelativeTo(null);
        
        this.setTitle("Edycja zadania - Ejtracker v.0.1.1");
        
        final TaskEditPanel panel = new TaskEditPanel(this._id, this);
        panel.init();
        
        this.add(panel);
        
        this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        
        this.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                panel.close();
            }
        });
    }
}
