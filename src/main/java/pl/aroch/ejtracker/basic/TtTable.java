
package pl.aroch.ejtracker.basic;

import javax.swing.JTable;

/**
 * Klasa rozszerzająca komponent JTable
 * @author Arkadiusz Roch
 */
public class TtTable extends JTable
{
    /**
     * Identyfikator tabeli
     */
    private Integer _tableId;
    
    /**
     * Dodatkowy model tabeli
     */
    private TtDefaultTableModel _ttModel;
    
    /**
     * Pobranie identyfiktora tabeli
     * @return Identyfikator tabeli
     */
    public Integer getTableId()
    {
        return this._tableId;
    }
    
    /**
     * Pobranie dodatkowego modelu tabeli
     * @return Dodatkowy model tabeli
     */
    public TtDefaultTableModel getTtTableModel()
    {
        return this._ttModel;
    }
    
    /**
     * Ustawienie identyfikatora tabeli
     * @param id Identyfikator tabeli
     */
    public void setTableId(Integer id)
    {
        this._tableId = id;
    }
    
    /**
     * Nadpisanie metody ustawiającej model tabeli
     * @param model Model tabeli
     */
    public void setModel(TtDefaultTableModel model)
    {
        super.setModel(model);
        this._ttModel = model;
    }
    
    /**
     * Ustawienie modelu tabeli 
     * @param model Model tabeli
     */
    public void setTtModel(TtDefaultTableModel model)
    {
        super.setModel(model);
        this._ttModel = model;
    }
}
