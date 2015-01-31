
package pl.aroch.ejtracker.model.updateChanels;

/**
 * Statusy kanału aktualizacji
 * @author Arkadiusz Roch
 */
public enum ChanelStatus {
    /**
     * Kanał aktywny
     */
    CHANEL_ACTIVE("active"),
    
    /**
     * Kanał przestarzały, ale jeszcze działający
     */
    CHANEL_DEPRECATED("deprecated"),
    
    /**
     * Kanał nieaktywny
     */
    CHANEL_REMOVED("removed");
    
    /**
     * Wartość statusu
     */
    private String _value;
    
    /**
     * Konstruktor przyjmujący jako parametr status kanału
     * @param value Status kanału
     */
    private ChanelStatus(String value)
    {
        this._value = value;
    }
    
    /**
     * Porównanie statusów
     * @param compare Status do porównania
     * @return Wynik sprawdzania statusów
     */
    public boolean equalsName(String compare)
    {
        if(compare == null)
        {
            return false;
        }
        
        return this._value.equals(compare);
    }
    
    /**
     * Pobranie wartości statusu
     * @return Aktualny status
     */
    @Override
    public String toString()
    {
        return this._value;
    }
    
    /**
     * Pobranie statusu jako obiekt na podstawie podanej nazwy
     * @param status Nazwa statusu
     * @return Status kanału jako obiekt
     */
    public static ChanelStatus getObject(String status)
    {
        if(ChanelStatus.CHANEL_ACTIVE.equalsName(status))
        {
            return ChanelStatus.CHANEL_ACTIVE;
        }
        else if(ChanelStatus.CHANEL_DEPRECATED.equalsName(status))
        {
            return ChanelStatus.CHANEL_DEPRECATED;
        }
        else 
        {
            return ChanelStatus.CHANEL_REMOVED;
        }
    }
}
