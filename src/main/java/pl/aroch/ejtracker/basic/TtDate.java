
package pl.aroch.ejtracker.basic;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Klasa do obsługi czasu oraz daty
 * @author Arkadiusz Roch
 */
public class TtDate extends Date {
    
    /**
     * Domyślny konstruktor, ustawia datę i czas na aktualną
     */
    public TtDate()
    {
        super();
    }
    
    /**
     * Konstruktor przyjmujący obiekt Date
     * @param date 
     */
    public TtDate(Date date)
    {
        super(date.getTime());
    }
    
    /**
     * Konstruktor przyjmujący czas w milisekundach
     * @param time Czas w milisekundach
     */
    public TtDate(long time)
    {
        super(time);
    }
    
    /**
     * Konstruktor przyjmujący czas w sekundach lub milisekundach
     * w zależności do flagi timestamp. Jeśli jest ustawiony na true
     * traktowany jest jako sekundy, jeśli false jako milisekundy
     * @param time Czas w sekundach / milisekundach
     * @param timestamp Flaga określająca czy podawany czas jest w sekundach czy milisekundach
     */
    public TtDate(long time, boolean timestamp)
    {
        super(time * ((timestamp) ? 1000 : 1));
    }
    
    /**
     * Pobranie daty w danym formacie
     * @param format Format daty
     * @see SimpleDateFormat
     * @see DateFormat
     * @return Data w żądanym formacie
     */
    public String format(String format)
    {
        DateFormat df = new SimpleDateFormat(format);
        df.setTimeZone(TimeZone.getTimeZone("Europe/Warsaw"));
        return df.format(this);
    }
    
    /**
     * Pobranie czasu w sekundach
     * @return Czas w sekundach
     */
    public long getTimestamp()
    {
        return (this.getTime() / 1000);
    }
    
    /**
     * Ustawienie czasu w sekundach
     * @param timestamp Czas w sekundach
     */
    public void setTimestamp(long timestamp)
    {
        this.setTime(timestamp * 1000);
    }
}
