
package pl.aroch.ejtracker.dialogs;

import javax.swing.JDialog;
import javax.swing.JFrame;
import pl.aroch.ejtracker.basic.TtDialog;
import pl.aroch.ejtracker.basic.TtUpdateStruct;
import pl.aroch.ejtracker.panels.UpdateInfoPanel;

/**
 * Okno wyświetlające informacje o nowej wersji progrmau
 * @author Arkadiusz Roch
 */
public class UpdateInfo extends JDialog implements TtDialog 
{
    /**
     * Struktura z informacjami o nowej wersji
     */
    private TtUpdateStruct _params;
    
    /**
     * Konstruktor przyjmujący jako parametr strukturę z informacjami o nowej wersji
     * @param params Struktura z informacjami o nowej wersji
     */
    public UpdateInfo(TtUpdateStruct params)
    {
        super(new JFrame(), true);
        
        this._params = params;
    }
    
    /**
     * Metda inicjująca okno
     */
    @Override
    public void init()
    {
        this.setResizable(false);
        
        this.setTitle("Aktualizacja - Ejtracker v.0.1.2");
        
        UpdateInfoPanel panel = new UpdateInfoPanel(this._params, this);
        
        panel.init();
        
        this.add(panel);
        
        this.pack();
        
        this.setLocationRelativeTo(null);
    }
}
