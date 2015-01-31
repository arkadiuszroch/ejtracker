
package pl.aroch.ejtracker.basic;

import javax.swing.JPanel;

/**
 * Klasa rozszerzająca funkcjonalność klasy JPanel
 * @author Arkadiusz Roch
 */
public abstract class TtPanel extends JPanel 
{
    /**
     * Metoda w której będą inicjowane i dodawane wszystkie elementy do panelu
     */
    public abstract void init();
}
