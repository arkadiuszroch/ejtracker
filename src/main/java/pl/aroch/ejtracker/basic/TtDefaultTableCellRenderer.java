
package pl.aroch.ejtracker.basic;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Klasa pomocnicza do wyswietlania podpowiedzi dla komórki tabeli
 * @author Arkadiusz Roch
 */
public class TtDefaultTableCellRenderer extends DefaultTableCellRenderer 
{
    /**
     * Metoda nadpisująca wyświetanie komórki
     * @param table Tabela
     * @param value Wartość komórki
     * @param isSelected Czy zaznaczona
     * @param hasFocus Czy posiada focus
     * @param row Wiersz
     * @param column Kolumna
     * @return Klasa renderująca komórkę tabeli
     */
    @Override
    public Component getTableCellRendererComponent(
                        JTable table, Object value,
                        boolean isSelected, boolean hasFocus,
                        int row, int column) 
    {
        JLabel c = (JLabel)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        TtCellValue cell = (TtCellValue)value;
        
        String text = cell.getToolTip();
        
        if(text.length() > 0)
        {
            c.setToolTipText(text);
        }
        
        return c;
    }
}
