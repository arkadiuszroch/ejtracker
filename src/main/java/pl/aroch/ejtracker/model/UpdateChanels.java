
package pl.aroch.ejtracker.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import pl.aroch.ejtracker.Messages;
import pl.aroch.ejtracker.model.updateChanels.Chanel;
import pl.aroch.ejtracker.model.updateChanels.ChanelStatus;

/**
 * Klasa zarządzająca kanałami aktualizacji programu
 * @author Arkadiusz Roch
 */
public class UpdateChanels 
{
    /**
     * Pobranie listy wszystkich kanałó aktualizacji
     * @return Lista kanałów aktualizacji
     */
    public ArrayList<Chanel> getChanels()
    {
        return this.getChanels(null);
    }
    
    /**
     * Pobranie kanałów aktualizacji o określonym statusie, jeśli null pobiera wszystkie
     * @param status Status projektu, może być null
     * @return Lista kanałów aktualizacji
     */
    public ArrayList<Chanel> getChanels(ChanelStatus status)
    {
        ArrayList<Chanel> result = new ArrayList<>();
        
        try
        {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT * FROM update_chanels");
            
            if(status != null)
            {
                sql.append(" WHERE status = ?");
            }
            
            PreparedStatement statement = DbDriver.getConnection().prepareStatement(sql.toString());
            if(status != null)
            {
                statement.setString(1, status.toString());
            }
            
            ResultSet rows = statement.executeQuery();
            
            while(rows.next())
            {
                result.add(new Chanel(rows));
            }
        }
        catch(SQLException e)
        {
            Messages.exceptionHandler("Wyjątek", "Błąd pobierania kanałów aktualizacji programu", e);
        }
        finally
        {
            DbDriver.disconnect();
        }
        
        return result;
    }
    
    /**
     * Dodanie / aktualizacja kanału o podanej nazwie
     * @param chanel Nazwa kanału
     * @param status Ststua kanału
     * @return Ilość zmienionych wierszy
     */
    public Integer saveChanel(String chanel, ChanelStatus status)
    {
        Integer result = 0;
        try
        {
            String sql = "INSERT OR REPLACE INTO update_chanels VALUES (?, ?)";
            PreparedStatement statement = DbDriver.getConnection().prepareStatement(sql);
            statement.setString(1, chanel);
            statement.setString(2, status.toString());
            statement.execute();
            
            result = statement.getUpdateCount();
        }
        catch(SQLException e)
        {
            Messages.exceptionHandler("Wyjątek", "Bład zapisu zmian w liście kanałów aktualizacji", e);
        }
        finally
        {
            DbDriver.disconnect();
        }
        
        return result;
    }
}
