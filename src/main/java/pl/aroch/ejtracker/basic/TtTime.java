
package pl.aroch.ejtracker.basic;

/**
 * Klasa obsługująca czas 
 * @author Arkadiusz Roch
 */
public class TtTime 
{
    /**
     * Czas w sekundach
     */
    private long _time;
    
    /**
     * Domyślny konstruktor
     */
    public TtTime()
    {
        this._time = 0;
    }
    
    /**
     * Konstruktor przyjmujący jako parametr ilość sekund (Integer)
     * @param time Ilość sekund jako Integer
     */
    public TtTime(Integer time)
    {
        this._time = time;
    }
    
    /**
     * Konstruktor przyjmujący jako parametr ilość sekund (long)
     * @param time Ilość sekund jako long
     */
    public TtTime(long time)
    {
        this._time = time;
    }
    
    /**
     * Zwiększenie czasu o podaną ilość sekund (Integer)
     * @param time Ilość sekund jako Integer
     */
    public void add(Integer time)
    {
        this._time += time;
    }
    
    /**
     * Zwiększenie czasu o podaną ilść sekund (long)
     * @param time Ilość sekund jako long
     */
    public void add(long time)
    {
        this._time += time;
    }
    
    /**
     * Zmniejszenie czasu o jedną sekundę
     */
    public void decrement()
    {
        if(this._time > 0)
        {
            this._time--;
        }
    }
    
    /**
     * Zwiększenie czasu o jedną sekundę
     */
    public void increment()
    {
        this._time++;
    }
    
    /**
     * Zmniejszenie czasu o podaną ilość sekund (Integer)
     * @param time Ilość sekund jako Integer
     */
    public void sub(Integer time)
    {
        if(time < this._time)
        {
            this._time -= time;
        }
        else
        {
            this._time = 0;
        }
    }
    
    /**
     * Zmniejszenie czasu o podaną ilość sekund (long)
     * @param time Ilość sekund jako long
     */
    public void sub(long time)
    {
        this.sub((int)time);
    }
    
    /**
     * Pobranie czasu w sekundach, identyczne działanie jak metoda "getTime"
     * @return Czas w sekundach
     */
    public long getAsSeconds()
    {
        return this._time;
    }
    
    /**
     * Pobranie czasu w minutach
     * @return Czas w minutach
     */
    public long getAsMinutes()
    {
        return this._time / 60;
    }
    
    /**
     * Pobranie czasu w godzinach
     * @return Czas w godzinach
     */
    public long getAsHours()
    {
        return this._time / 3600;
    }
    
    /**
     * Przelicza oraz pobiera ilość sekund
     * @return Ilość sekund
     */
    public Integer getSeconds()
    {
        return (int)this._time % 60;
    }
    
    /**
     * Przelicza oraz pobiera ilość minut
     * @return Ilość minut
     */
    public Integer getMinutes()
    {
       return (int)(this._time / 60) % 60;
    }
    
    /**
     * Przelicza oraz pobiera jako ilość godzin
     * @return Ilość godzin
     */
    public Integer getHours()
    {
        return (int)(this._time / 3600) % 24;
    }
    
    /**
     * Pobranie czasu, identyczne działanie jak metoda "getAsSeconds"
     * @return Ilość sekund
     */
    public long getTime()
    {
        return this._time;
    }
    
    /**
     * Ustawienie czasu na podaną ilość sekund (Integer)
     * @param time Ilość sekund jako Integer
     */
    public void setTime(Integer time)
    {
        this._time = time;
    }
    
    /**
     * Ustawienie czasu na podaną ilość sekund (long)
     * @param time Ilość sekund jako long
     */
    public void setTime(long time)
    {
        this._time = time;
    }
}
