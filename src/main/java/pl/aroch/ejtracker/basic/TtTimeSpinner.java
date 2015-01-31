
package pl.aroch.ejtracker.basic;

import java.util.Date;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

/**
 * Klasa pomocnicza dla komponentu JSpinner
 * @author Arkadiusz Roch
 */
public class TtTimeSpinner 
{
    /**
     * Pobranie komponentu JSpinner z aktualna datą oraz domyślnym formatem wyświetlania
     * @return Komponent JSpinner
     */
    public static JSpinner getTimeSpinner()
    {
        return TtTimeSpinner._init(new Date(), "HH:mm:ss");
    }
    
    /**
     * Pobranie komponentu JSpinner z podaną datą oraz domyślnym formatem wyświetlania
     * @param date Data
     * @return Komponent JSpinner
     */
    public static JSpinner getTimeSpinner(Date date)
    {
        return TtTimeSpinner._init(date, "HH:mm:ss");
    }
    
    /**
     * Pobranie komponentu JSpinner z aktualną datą oraz podanym formatem wyświetlania
     * @param format Format wyświetlanej daty
     * @return Komponent JSpinner
     */
    public static JSpinner getTimeSpinner(String format)
    {
        return TtTimeSpinner._init(new Date(), format);
    }
    
    /**
     * Pobranie komponentu JSpinner z podana datą oraz formatem wyświetlania 
     * @param date Data
     * @param format Format wyświetlania daty
     * @return Komponent JSpinner
     */
    public static JSpinner getTimeSpinner(Date date, String format)
    {
        return TtTimeSpinner._init(date, format);
    }
    
    /**
     * Utworzenie oraz zwrócenie komponentu JSpinner zgodnie z podanymi parametrami
     * @param date Data
     * @param format Format wyświetlania daty
     * @return Komponent JSpinner
     */
    private static JSpinner _init(Date date, String format)
    {
        JSpinner timeSpinner = new JSpinner( new SpinnerDateModel() );
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, format);
        
        timeSpinner.setEditor(timeEditor);
        timeSpinner.setValue(date);
        
        return timeSpinner;
    }
}
