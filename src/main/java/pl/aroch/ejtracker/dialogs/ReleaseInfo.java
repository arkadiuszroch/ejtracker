
package pl.aroch.ejtracker.dialogs;

import javax.swing.JDialog;
import javax.swing.JFrame;
import pl.aroch.ejtracker.basic.TtDialog;
import pl.aroch.ejtracker.panels.ReleaseInfoPanel;

/**
 * Okno wyświetlające dodatkowe informacje o nowej wersji
 * @author Arkadiusz Roch
 */
public class ReleaseInfo extends JDialog implements TtDialog
{
    /**
     * Treść informacji
     */
    private String _text;
    
    /**
     * Konstruktor przyjmujący jako parametr treść informacji do wyświetlenia
     * @param text Treść informacji
     */
    public ReleaseInfo(String text)
    {
        super(new JFrame(), true);
        this._text = text;
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
        
        this.setTitle("Co nowego - Ejtracker v.0.1.0");
        
        ReleaseInfoPanel panel = new ReleaseInfoPanel(this._text, this);
        
        panel.init();
        
        this.add(panel);
    }
}
