
package pl.aroch.ejtracker.dialogs.tasks;

import javax.swing.JDialog;
import javax.swing.JFrame;
import pl.aroch.ejtracker.basic.TtDialog;
import pl.aroch.ejtracker.panels.tasks.TasksHistoryPanel;

/**
 * Okno wyświetlające historię zadań
 * @author Arkadiusz Roch
 */
public class TasksHistory extends JDialog implements TtDialog 
{
    /**
     * Domyślny konstruktor
     */
    public TasksHistory()
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
        
        this.setTitle("Historia zadań - Ejtracker v.0.1.2");
        
        TasksHistoryPanel panel = new TasksHistoryPanel();
        panel.init();
        
        this.add(panel);
    }
}
