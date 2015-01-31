
package pl.aroch.ejtracker.model.updateChanels;

import java.sql.ResultSet;
import java.sql.SQLException;
import pl.aroch.ejtracker.model.UpdateChanels;

/**
 * Klasa reprezentująca kanał aktualizacji programu
 * @author Arkadiusz Roch
 */
public class Chanel 
{
    /**
     * Nazwa kanału jest jednocześnie jego adresem
     */
    private String _chanel;
    
    /**
     * Status kanału
     */
    private ChanelStatus _status;
    
    /**
     * Konstruktor przyjmujący jako parametry nazwę kanału oraz jego status
     * @param chanel Adres kanału aktualizacji
     * @param status Status kanału aktualizacji
     */
    public Chanel(String chanel, ChanelStatus status)
    {
        this._chanel = chanel;
        this._status = status;
    }
    
    /**
     * Konstruktor przyjmujący jako parametr obiekt ResultSet 
     * @param row Dane pobrane z bazy
     * @throws SQLException 
     */
    public Chanel(ResultSet row) throws SQLException
    {
        if(row.findColumn("chanel") > 0)
        {
            this._chanel = row.getString("chanel");
        }
        else
        {
            this._chanel = "";
        }
        
        if(row.findColumn("status") > 0)
        {
            this._status = ChanelStatus.getObject(row.getString("status"));
        }
        else
        {
            this._status = ChanelStatus.CHANEL_REMOVED;
        }
    }
    
    /**
     * Nadanie nowej wartości kanałowi
     * @param chanel Nowy adres kanału
     */
    public void setChanel(String chanel)
    {
        this._chanel = chanel;
    }
    
    /**
     * Nadanie nowego statusu kanału
     * @param status Status kanału
     */
    public void setStatus(ChanelStatus status)
    {
        this._status = status;
    }
    
    /**
     * Pobranie adresu kanału
     * @return Adres kanału
     */
    public String getChanel()
    {
        return this._chanel;
    }
    
    /**
     * Pobranie statusu kanału
     * @return Status kanału
     */
    public ChanelStatus getStatus()
    {
        return this._status;
    }
    
    /**
     * Zapisanie kanału
     * @return 
     */
    public Integer save()
    {
        UpdateChanels chanels = new UpdateChanels();
        
        return chanels.saveChanel(this._chanel, this._status);
    }
}
