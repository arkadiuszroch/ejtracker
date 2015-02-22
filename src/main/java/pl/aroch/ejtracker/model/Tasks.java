
package pl.aroch.ejtracker.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import pl.aroch.ejtracker.Messages;
import pl.aroch.ejtracker.Basic;
import pl.aroch.ejtracker.basic.TtDate;
import pl.aroch.ejtracker.model.tasks.DayList;
import pl.aroch.ejtracker.model.tasks.Task;

/**
 * Klasa pobierająca oraz zpisująca dane zadań w bazie danych
 * @author Arkadiusz Roch
 */
public class Tasks 
{
    /**
     * Pobranie listy zadań
     * @return Lista zadań
     */
    public ArrayList<Task> getTasks()
    {
        return this.getTasks(null);
    }
    
    /**
     * Pobranie listy zadań przypisanych do projektu o podanym identyfikatorze
     * @param projectId Identyfikator projektu
     * @return Lista zadań
     */
    public ArrayList<Task> getTasks(Integer projectId)
    {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM tasks WHERE end_date > 0");
        
        if(projectId != null && projectId > -1)
        {
            sql.append( " AND id_project = ?");
        }
        
        sql.append(" ORDER BY start_date DESC");
        
        ArrayList<Task> result = null;
        
        try
        {
            PreparedStatement statement = DbDriver.getConnection().prepareStatement(sql.toString());
            if(projectId != null && projectId > -1)
            {
                statement.setInt(1, projectId);
            }
            
            statement.execute();
            
            result = this._prepareResults(statement.getResultSet());
        }
        catch(SQLException e)
        {
            Messages.exceptionHandler("Wyjątek", "Błąd pobierania listy zadań", e);
        }
        finally
        {
            DbDriver.disconnect();
        }
        
        return result;
    }
    
    /**
     * Pobranie zadań z ostatnich trzech dni
     * @return Lista zadań
     */
    public ArrayList<Task> getLastTasks()
    {
        return this.getLastTasks(null);
    }
    
    /**
     * Pobranie zadań z ostatnich trzech dni przypisanych do projektu o podanym identyfikatorze
     * @param projectId Identyfikator projektu
     * @return Lista zadań
     */
    public ArrayList<Task> getLastTasks(Integer projectId)
    {
        Calendar cal = Calendar.getInstance();
        
        cal.add(Calendar.DAY_OF_YEAR, -2);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        
        TtDate date = new TtDate(cal.getTime().getTime());
        
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM tasks WHERE end_date > 0 AND start_date > ?");
        if(projectId != null && projectId > -1)
        {
            sql.append(" AND id_project = ?");
        }
        
        sql.append(" ORDER BY start_date DESC");
        
        ArrayList<Task> result = null;
        
        try
        {
            PreparedStatement statement = DbDriver.getConnection().prepareStatement(sql.toString());
            statement.setLong(1, date.getTimestamp());
            if(projectId != null && projectId > -1)
            {
                statement.setInt(2, projectId);
            }
            
            statement.execute();
            
            result = this._prepareResults(statement.getResultSet());
        }
        catch(SQLException e)
        {
            Messages.exceptionHandler("Wyjątek", "Błąd pobierania listy zadań", e);
        }
        finally
        {
            DbDriver.disconnect();
        }
        
        return result;
    }
    
    /**
     * Pobranie listy zadań z podziałem na dni
     * @param latest Flaga określająca czy mają być pobrane wszystkie zadania czy tylko te z ostatnich trzech dni
     * @return Lista zadań z podziałem na dni
     */
    public ArrayList<DayList> getTasksPerDay(boolean latest)
    {
        return this.getTasksPerDay(latest, null);
    }
    
    /**
     * Pobranie listy zadań z podziałem na dni przypisanych do projektu o podanym identyfikatorze 
     * @param latest Flaga określająca czy mają być pobrane wszystkie zadania czy tylko te z ostatnich trzech dni
     * @param projectId Identyfikator projektu
     * @return Lista zadań z podziałem na dni
     */
    public ArrayList<DayList> getTasksPerDay(boolean latest, Integer projectId)
    {
        ArrayList<Task> list;
        
        ArrayList<DayList> results = new ArrayList<>();
        
        if(latest)
        {
            list = this.getLastTasks(projectId);
        }
        else 
        {
            list = this.getTasks(projectId);
        }
        
        
        if(list.isEmpty())
        {
            return results;
        }
        
        TtDate currentDate = null;
        DayList currentList = new DayList();
        
        Calendar cal = Calendar.getInstance();
        
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        
        TtDate todayDate = new TtDate(cal.getTime());
        
        cal.add(Calendar.DAY_OF_YEAR, -1);
        
        TtDate yesterdayDate = new TtDate(cal.getTime());
        
        cal.add(Calendar.DAY_OF_YEAR, -1);
        
        TtDate beforeYesterdayDate = new TtDate(cal.getTime());
        
        StringBuilder label = new StringBuilder();
        
        boolean changeLabel = true;
        
        for(Task item: list)
        {
            if(currentDate == null)
            {
                currentDate = item.getStartDate();
            }
            
            if(currentDate.format("dd-MM-yyyy").compareToIgnoreCase(item.getStartDate().format("dd-MM-yyyy")) != 0)
            {
                currentDate = item.getStartDate();
                results.add(currentList);
                
                currentList = new DayList();
                changeLabel = true;
            }
            
            if(changeLabel)
            {
                label.delete(0, label.length());

                if(currentDate.after(todayDate))
                {
                    label.append("Dzisiaj");
                }
                else if(currentDate.before(todayDate) && currentDate.after(yesterdayDate))
                {
                    label.append("Wczoraj");
                }
                else if(currentDate.before(yesterdayDate) && currentDate.after(beforeYesterdayDate))
                {
                    label.append("Przedwczoraj");
                }
                else
                {
                    label.append(currentDate.format("dd-MM-yyyy"));
                }
                
                changeLabel = false;
            }
            
            currentList.setDay(label.toString());
            
            currentList.addTask(item);
        }
        
        results.add(currentList);
        
        return results;
    }
    
    /**
     * Pobranie zadania, które nie zostało zakończone przed zamknięciem programu
     * @return Zadanie, które nie zostało zakończone przed zamknięciem programu
     */
    public Task getPendingTask()
    {
        String sql = "SELECT * FROM tasks WHERE end_date = 0 LIMIT 1";
        
        Task result = null;
        try
        {
            Statement statement = DbDriver.getConnection().createStatement();
            statement.execute(sql);
            
            ResultSet row = statement.getResultSet();
            
            if(row.next())
            {
                result = new Task(row);
            }
        }
        catch(SQLException e)
        {
            Messages.exceptionHandler("Wyjątek", "Nie udało się pobrać aktualnego zadania", e);
        }
        finally
        {
            DbDriver.disconnect();
        }
        
        return result;
    }
    
    /**
     * Pobranie zadania na podstawie podanego identyfikatora
     * @param id Identyfikator zadania
     * @return Pobrane zadanie lub null
     */
    public Task getTaskById(Integer id)
    {
        String sql = "SELECT * FROM tasks WHERE id_task = ?";
        Task result = null;
        
        try
        {
            PreparedStatement statement = DbDriver.getConnection().prepareStatement(sql);
            statement.setInt(1, id);
            
            statement.execute();
            
            ResultSet row = statement.getResultSet();
            
            if(row.next())
            {
                result = new Task(row);
            }
        }
        catch(SQLException e)
        {
            Messages.exceptionHandler("Wyjątek", "Nie udało się pobrać zadania", e);
        }
        
        return result;
    }
    
    /**
     * Dodanie zadania
     * @param name Nazwa projektu
     * @param projectId Identyfikator projektu
     * @param startDate Czas rozpoczęcia zadania
     * @param endDate Czas zakończenia zadania
     * @param description Dodatkowy opis
     * @return Identyfikator dodanego zadania
     */
    public Integer addTask(String name, Integer projectId, TtDate startDate, TtDate endDate, String description)
    {
        String sql = "INSERT INTO tasks (name, id_project, start_date, end_date, description) VALUES (?, ?, ?, ?, ?)";
        
        Integer result = 0;
        try
        {
            PreparedStatement statement = DbDriver.getConnection().prepareStatement(sql);
            
            statement.setString(1, name);
            statement.setInt(2, projectId);
            statement.setLong(3, startDate.getTimestamp());
            statement.setLong(4, endDate.getTimestamp());
            statement.setString(5, description);
            
            statement.execute();
            
            ResultSet ids = statement.getGeneratedKeys();
            
            if(ids.next())
            {
                result = ids.getInt(1);
            }
        }
        catch(SQLException e)
        {
            Messages.exceptionHandler("Wyjątek", "Bład podczas dodawania zadania", e);
        }
        finally
        {
            DbDriver.disconnect();
        }
        
        return result;
    }
    
    /**
     * Zapisanie zmian w już istniejącym zadaniu
     * @param id Identyfikator zadania
     * @param name Nazwa zadania
     * @param projectId Identyfikator projektu
     * @param startDate Czas rozpoczęcia zadania
     * @param endDate Czas zakończenia zadania
     * @param description Dodatkowy opis
     * @return Ilość zmienionych wierszy
     */
    public Integer saveTask(Integer id, String name, Integer projectId, TtDate startDate, TtDate endDate, String description)
    {
        Integer result = 0;
        try
        {
            String sql = "UPDATE tasks SET name = ?, id_project = ?, start_date = ?, end_date = ?, description = ? WHERE id_task = ?";
            PreparedStatement statement = DbDriver.getConnection().prepareStatement(sql);
            
            statement.setString(1, name);
            statement.setInt(2, projectId);
            statement.setLong(3, startDate.getTimestamp());
            statement.setLong(4, endDate.getTimestamp());
            statement.setString(5, description);
            statement.setInt(6, id);
            
            result = statement.executeUpdate();
        }
        catch(SQLException e)
        {
            Messages.exceptionHandler("Wyjątek", "Nie udało się zapisać zadania", e);
        }
        finally
        {
            DbDriver.disconnect();
        }
        
        return result;
    }
    
    /**
     * Pobranie czasu wykonanych zadań dla projektu o podanym identyfikatorze
     * @param projectId Identyfikator projektu
     * @return Czas w sekundach
     */
    public Integer getTimeForProject(Integer projectId)
    {
        String sql = "SELECT SUM(end_date - start_date) as time FROM tasks WHERE id_project = ? AND end_date > 0";
        Integer result = 0;
        try
        {
            PreparedStatement statement = DbDriver.getConnection().prepareStatement(sql);
            statement.setInt(1, projectId);
            
            statement.execute();
            
            ResultSet rows = statement.getResultSet();
            
            if(rows.next())
            {
                result = rows.getInt("time");
            }
        }
        catch(SQLException e)
        {
            Messages.exceptionHandler("Wyjątek", "Nie udało się pobrać czasu dla projektu'", e);
        }
        finally
        {
            DbDriver.disconnect();
        }
        
        return result;
    }
    
    /**
     * Usunięcie zadania o podanym identyfikatorze
     * @param id Identyfikator zadania do usunięcia
     * @return Ilość usuniętych wierszy
     */
    public Integer deleteTask(Integer id)
    {
        Integer[] ids = new Integer[1];
        ids[0] = id;
        
        return this.deleteTasks(ids);
    }
    
    /**
     * Usunięcie zadań o podanych identyfikatorach
     * @param ids Identyfikatory zadań do usunięcia
     * @return Ilośc usuniętych wierszy
     */
    public Integer deleteTasks(Integer[] ids)
    {
        Integer result = 0;
        try
        {
            StringBuilder sql = new StringBuilder();
            String[] qMarks = new String[ids.length];
            Arrays.fill(qMarks, "?");
            
            sql.append("DELETE FROM tasks WHERE id_task IN (");
            sql.append(Basic.Join(qMarks, ","));
            sql.append(")");
            
            PreparedStatement statement = DbDriver.getConnection().prepareStatement(sql.toString());
            int size = ids.length;
            
            for(int i = 0; i < size; i++)
            {
                statement.setInt(i+1, ids[i]);
            }
            //statement.setArray(1, DbDriver.getConnection().createArrayOf("Integer", ids));
            
            result = statement.executeUpdate();
        }
        catch(SQLException e)
        {
            Messages.exceptionHandler("Wyjątek", "Nie udało się pobrać czasu dla projektu'", e);
        }
        finally
        {
            DbDriver.disconnect();
        }
        
        return result;
    }
    
    /**
     * Metoda przygotowująca listę zadań na podstawie pobranego wyniku
     * @param rows Wiersze pobrane z bazy danych
     * @return Lista zadań
     * @throws SQLException 
     */
    private ArrayList<Task> _prepareResults(ResultSet rows) throws SQLException
    {
        ArrayList<Task> results = new ArrayList<>();
        
        while(rows.next())
        {
            results.add(new Task(rows));
        }
        
        return results;
    }
}
