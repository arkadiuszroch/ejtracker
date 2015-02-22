
package pl.aroch.ejtracker.dialogs;

import javax.swing.JDialog;
import javax.swing.JFrame;
import pl.aroch.ejtracker.basic.TtDialog;
import pl.aroch.ejtracker.panels.ProgramAboutPanel;

/**
 * Okno wyświetlające informacje o programie
 * @author Arkadiusz Roch
 */
public class ProgramAbout extends JDialog implements TtDialog
{
    /**
     * Domyślny konstruktor
     */
    public ProgramAbout()
    {
        super(new JFrame(), true);        
    }
    
    /**
     * Metoda inicjująca okno
     */
    @Override
    public void init()
    {
        this.setSize(500, 410);
        
        this.setResizable(false);
        
        this.setLocationRelativeTo(null);
        
        this.setTitle("O programie - Ejtracker v.0.1.1");
        
        ProgramAboutPanel panel = new ProgramAboutPanel(this);
        
        panel.init();
        
        this.add(panel);
    }
}
