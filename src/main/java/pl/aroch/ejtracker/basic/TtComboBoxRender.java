
package pl.aroch.ejtracker.basic;

import java.awt.Component;
import javax.swing.JList;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

/**
 * Klasa rozszerzająca bazową klasę wyświetlającą ComboBox-a
 * @see BasicComboBoxRenderer
 * @author Arkadiusz Roch
 */
public class TtComboBoxRender extends BasicComboBoxRenderer 
{
    /**
     * Metoda wyświetlająca element listy
     * @param list Lista
     * @param value Wartość
     * @param index Numer elementu
     * @param isSelected Czy element jest zaznaczony
     * @param hasFocus Czy element posiada aktywny focus
     * @return Aktualny obiekt
     */
    public Component getListCellRenderComponent(JList list, Object value, int index, boolean isSelected, boolean hasFocus)
    {
        super.getListCellRendererComponent(list, value, index, isSelected, hasFocus);
        
        if(value != null)
        {
            TtComboBoxItem item = (TtComboBoxItem)value;
            setText(item.getValue());
        }
        
        if(index == -1)
        {
            TtComboBoxItem item = (TtComboBoxItem)value;
            setText("" + item.getId());
        }
        
        return this;
    }
}
