
package pl.aroch.ejtracker.basic;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Klasa zarzadzająca obserwatorami
 * @author Arkadiusz Roch
 */
public class TtObservable
{
    /**
     * Domyślne wydarzenia wywołujące obserwatorów
     * PROJECT_ADDED - dodano projekt
     * PROJECT_CHANGED - zmiany w projekcie
     * PROJECT_DELETED - usunięto projekt
     * TASK_ADDED - dodano nowe zadanie
     * TASK_CHANGED - zmiany w zadaniu
     * TAST_DELETED - usunięto zadanie
     */
    public static String PROJECT_ADDED = "projectAdded";
    public static String PROJECT_CHANGED = "projectChanged";
    public static String PROJECT_REMOVED = "projectRemoved";
    public static String TASK_ADDED = "taskAdded";
    public static String TASK_CHANGED = "taskChanged";
    public static String TASK_REMOVED = "taskRemoved";
    
    /**
     * Nazwa wydarzenia
     */
    private String _actionName;
    
    /**
     * Flaga określająca czy były jakieś zmiany
     */
    private boolean _changed;
    
    /**
     * Instancje klas zarządzających obserwatorami
     */
    private static HashMap<String, TtObservable> _instances = new HashMap<>();
    
    /**
     * Obserwatorzy
     */
    private ArrayList<TtObserver> _observers;
    
    /**
     * Pobranie instancji klasy dla danego wydarzenia
     * @param actionName Nazwa wydarzenia
     * @return Obiekt klasy
     */
    public static TtObservable getInstance(String actionName)
    {
        if(!TtObservable._instances.containsKey(actionName))
            TtObservable._instances.put(actionName, new TtObservable(actionName));
            
        return TtObservable._instances.get(actionName);
    }
    
    /**
     * Domyślny konstruktor 
     * @param actionName Nazwa wydarzenia
     */
    private TtObservable(String actionName)
    {
        this._observers = new ArrayList<>();
        this._changed = false;
        this._actionName = actionName;
    }
    
    /**
     * Dodanie obserwatora
     * @param o Instancja nowego obserwatora
     */
    public void addObserver(TtObserver o)
    {
        this._observers.add(o);
    }
    
    /**
     * Usunięcie obserwatora
     * @param o Instancja obserwatora do usunięcia
     */
    public void removeObserver(TtObserver o)
    {
        this._observers.remove(o);
    }
    
    /**
     * Usunięcie wszystkich obserwatorów
     */
    public void clearObservers()
    {
        this._observers.clear();
    }
    
    /**
     * Usunięcie zmiany
     */
    public void clearChanged()
    {
        this._changed = false;
    }
    
    /**
     * Ustawienie jako zmieniony
     */
    public void setChanged()
    {
        this._changed = true;
    }
    
    /**
     * Ustawienie czy były zmiany
     * @param changed Flaga określająca czy były zmiany
     */
    public void setChanged(boolean changed)
    {
        this._changed = changed;
    }
    
    /**
     * Powiadomienie obserwatorów o zmianach
     * @param args Dodatkowe argumenty dla obserwatorów
     */
    public void notifyObservers(Object args)
    {
        if(this._changed)
        {
            for(TtObserver o: this._observers)
            {
                o.update(args, this._actionName);
            }
            
            this._changed = false;
        }
    }
}