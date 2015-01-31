
package pl.aroch.ejtracker.model.tasks;

import java.util.ArrayList;
import pl.aroch.ejtracker.basic.TtTime;

/**
 * Klasa grupująca zadania względem daty. Każda instancja klasy to osobny dzień z listą zadań
 * oraz sumaryczną ilością czasu jaki im poświęcono.
 * @author Arkadiusz Roch
 */
public class DayList 
{
    /**
     * Data wykonywania zadań
     */
    private String _day;
    
    /**
     * Sumaryczny czas wykonanych zadań
     */
    private TtTime _timeSummary;
    
    /**
     * Lista zadsań
     */
    private ArrayList<Task> _tasks;
    
    /**
     * Domyślny konstruktor
     */
    public DayList()
    {
        this._tasks = new ArrayList<>();
        this._timeSummary = new TtTime(0);
    }
    
    /**
     * Dodanie zadania do listy
     * @param item Obiekt zadania
     */
    public void addTask(Task item)
    {
        this._timeSummary.add(item.getTime());
        this._tasks.add(item);
    }
    
    /**
     * Ustawienie daty 
     * @param day Data
     */
    public void setDay(String day)
    {
        this._day = day;
    }
    
    /**
     * Pobranie daty
     * @return Data
     */
    public String getDay()
    {
        return this._day;
    }
    
    /**
     * Pobranie czasu wykonanych zadań
     * @return Czas wykonanych zadań
     */
    public TtTime getTime()
    {
        return this._timeSummary;
    }
    
    /**
     * Pobranie listy zadań
     * @return Lista zadań
     */
    public ArrayList<Task> getTasks()
    {
        return this._tasks;
    }
}
