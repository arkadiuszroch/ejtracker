
package pl.aroch.ejtracker.basic;

/**
 * Interfejs obserwatora
 * @author Arkadiusz Roch
 */
public interface TtObserver 
{
    /**
     * Metoda wywoływana przez menadżera obserwatorów
     * @param args Dodatkowe argumenty
     * @param actionName Nazwa wydarzenia
     */
    public void update(Object args, String actionName);
}
