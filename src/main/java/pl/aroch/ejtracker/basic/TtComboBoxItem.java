
package pl.aroch.ejtracker.basic;

/**
 * Element ComboBox-a
 * @author Arkadiusz Roch
 */
public class TtComboBoxItem 
{
    /**
     * Integer Id elementu
     */
    private Integer _id;
    
    /**
     * String Wartość elementu
     */
    private String _value;
    
    /**
     * Konstruktor nadający identyfikator oraz wartość elementu
     * @param id Identyfikator elementu
     * @param value Wartość elementu
     */
    public TtComboBoxItem(Integer id, String value)
    {
        this._id = id;
        this._value = value;
    }
    
    /**
     * Pobranie identyfikatora elementu
     * @return Identyfikator wpisu
     */
    public Integer getId()
    {
        return this._id;
    }
    
    /**
     * Pobranie wartości elementu
     * @return Wartość elementu
     */
    public String getValue()
    {
        return this._value;
    }
    
    /**
     * Zwrócenie wartości elementu
     * @return Wartość elementu
     */
    @Override
    public String toString()
    {
        return this._value;
    }
}
