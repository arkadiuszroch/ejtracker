
package pl.aroch.ejtracker.basic;

/**
 * Klasa do obsługi wartości komórki w tabeli
 * Dodana, aby umożliwić dodanie tooltip
 * @author Arkadiusz Roch
 */
public class TtCellValue 
{
    /**
     * Treść podpowiedzi
     */
    private String _toolTip;
    
    /**
     * Wartość komórki
     */
    private String _value;
    
    /**
     * Domyślny konstruktor
     */
    public TtCellValue()
    {
        this._init("", "");
    }
    
    /**
     * Konstruktor przyjmujący wartość komórki
     * @param value Wartość komórki
     */
    public TtCellValue(String value)
    {
        this._init(value, "");
    }
    
    /**
     * Konstruktor przyjmujący wartość oraz podpowiedź komórki
     * @param value Wartość komórki
     * @param toolTip Treść podpowiedzi dla komórki
     */
    public TtCellValue(String value, String toolTip)
    {
        this._init(value, toolTip);
    }
    
    /**
     * Pobranie wartości komórki
     * @return Wartość komórki
     */
    public String getValue()
    {
        return this._value;
    }
    
    /**
     * Pobranie treści podpowiedzi
     * @return Treść podpowiedzi
     */
    public String getToolTip()
    {
        return this._toolTip;
    }
    
    /**
     * Nadanie wartości komórce
     * @param value Zapisywana wartość
     */
    public void setValue(String value)
    {
        this._value = value;
    }
    
    /**
     * Ustawienie podpowiedzi dla komórki
     * @param value Treść podpowiedzi
     */
    public void setToolTip(String value)
    {
        this._toolTip = value;
    }
    
    /**
     * Zwrócenie wartości komórki
     * @return Wartość komórki
     */
    @Override
    public String toString()
    {
        return this.getValue();
    }
    
    /**
     * Inicjalizacja wartości komórki oraz treści podpowiedzi
     * @param value Wartość komórki
     * @param toolTip Treść podpowiedzi
     */
    private void _init(String value, String toolTip)
    {
        this._value = value;
        this._toolTip = toolTip;
    }
}
