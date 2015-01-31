
package pl.aroch.ejtracker.basic;

import java.util.HashMap;
import javax.swing.table.DefaultTableModel;

/**
 * Klasa nadpisująca domyślny model tabel
 * @author Arkadiusz Roch
 */
public class TtDefaultTableModel extends DefaultTableModel 
{
    /**
     * Identyfikatory wierszy (numer wiersza, identyfikator)
     */
    private HashMap<Integer, Integer> _rowIds;
    
    /**
     * Domyślny konstruktor
     */
    public TtDefaultTableModel()
    {
        super();
        this._rowIds = new HashMap<>();
    }
    
    /**
     * Zwraca informację o tym, czy komórkę można edytować, 
     * domyślnie w klasie nadrzędnej zwracany jest true,
     * natomiast w obecnie nie jest to konieczne dlatego metoda zwraca zawsze false.
     * Nie było konieczności implementacji pełnej funkcjonalności tej metody
     * @param rowIndex Numer wiersza
     * @param cellIndex Numer komórki
     * @return Flaga określająca czy komórkę można edytować
     */
    @Override
    public boolean isCellEditable(int rowIndex, int cellIndex)
    {
        return false;
    }
    
    /**
     * Pobranie identyfikatora wiersza
     * @param rowIndex Numer wiersza
     * @return Przypisany identyfikator wiersza
     */
    public int getRowId(Integer rowIndex)
    {
        return this._rowIds.get(rowIndex);
    }

    /**
     * Nadanie wierszowi identyfikatora
     * @param rowIndex Numer wiersza
     * @param id Nowy identyfikator
     */
    public void setRowId(Integer rowIndex, Integer id)
    {
        this._rowIds.put(rowIndex, id);
    }
}
