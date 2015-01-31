
package pl.aroch.ejtracker.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import pl.aroch.ejtracker.Basic;
import pl.aroch.ejtracker.Messages;
import pl.aroch.ejtracker.model.projects.Project;

/**
 * Klasa pobierająca oraz zapisująca dane projektów w bazie danych
 * @author Arkadiusz Roch
 */
public class Projects 
{
    /**
     * Pobranie projektu o podanym identyfikatorze
     * @param id Identyfikator projektu
     * @return Pobrany projekt lub pusty obiekt
     */
    public Project getProject(Integer id)
    {
        Project project = new Project();
        try
        {
            String sql = "SELECT * FROM projects WHERE id_project = ?";
            PreparedStatement statement = DbDriver.getConnection().prepareStatement(sql);

            statement.setInt(1, id);

            statement.execute();
            
            ResultSet rows = statement.getResultSet();

            if(rows.next())
            {
                project = new Project(rows);
            }
            else
            {
                project = new Project();
            }
        }
        catch(SQLException e)
        {
            Messages.exceptionHandler("Wyjątek", "Błąd pobierania\ninformacji o projekcie", e);
        }
        finally
        {
            DbDriver.disconnect();
        }
        
        return project;
    }
    
    /**
     * Pobranie listy projektów
     * @return Lista projektów
     */
    public ArrayList<Project> getProjectsList()
    {
        String sql = "SELECT * FROM projects ORDER BY name";
        ArrayList<Project> results = new ArrayList<>();
        
        try
        {
            Statement statement = DbDriver.getConnection().createStatement();
            statement.execute(sql);
            
            ResultSet row = statement.getResultSet();
            
            while(row.next())
            {
                results.add(new Project(row));
            }
        }
        catch(SQLException e)
        {
            Messages.exceptionHandler("Wyjątek", "Bład pobierania\nlisty projektów", e);
        }
        finally
        {
            DbDriver.disconnect();
        }
        
        return results;
    }
    
    /**
     * Pobranie ilości zadań przypisanych do projektu o podanym identyfikatorze
     * @param projectId Identyfikator projektu
     * @return Ilość zadań przypisanych do projektu
     */
    public Integer getTasksCount(Integer projectId)
    {
        String sql = "SELECT COUNT(id_task) tasks_count FROM tasks WHERE id_project = ?";
        
        Integer result = 0;
        
        try
        {
            PreparedStatement statement = DbDriver.getConnection().prepareStatement(sql);
            statement.setInt(1, projectId);
            
            statement.execute();
            
            ResultSet rows = statement.getResultSet();
            
            if(rows.next())
            {
                result = rows.getInt("tasks_count");
            }
        }
        catch(SQLException e)
        {
            Messages.exceptionHandler("Wyjątek", "Nie udało się pobrać ilość zadań dla projektu", e);
        }
        finally
        {
            DbDriver.disconnect();
        }
        
        return result;
    }
    
    /**
     * Dodanie nowego projektu do bazy danych
     * @param name Nazwa projektu
     * @param shortName Skrócona nazwa projektu
     * @param desc Opis projektu
     * @return Identyfikator dodanego projektu
     */
    public Integer addProject(String name, String shortName, String desc)
    {
        Integer result = 0;
        try
        {
            String sql = "INSERT INTO projects (name, short_name, description) VALUES (?, ?, ?)";
            PreparedStatement statement = DbDriver.getConnection().prepareStatement(sql);
            
            statement.setString(1, name);
            statement.setString(2, shortName);
            statement.setString(3, desc);
            
            statement.execute();
            
            ResultSet keys = statement.getGeneratedKeys();
            
            if(keys.next())
            {
                result = keys.getInt(1);
            }
            else
            {
                result = 0;
            }
        }
        catch(SQLException e)
        {
            Messages.exceptionHandler("Wyjątek", "Nie udało się dodać projektu", e);
        }
        finally
        {
            DbDriver.disconnect();
        }
        
        return result;
    }
    
    /**
     * Zapisanie już istniejącego projektu
     * @param name Nazwa projektu
     * @param shortName Krótka nazwa projektu
     * @param desc Opis projektu
     * @param id Identyfikator projektu
     * @return Ilość zmienionych wierszy
     */
    public Integer saveProject(String name, String shortName, String desc, Integer id)
    {
        Integer result = 0;
        try
        {
            String sql = "UPDATE projects SET name = ?, short_name = ?, description = ? WHERE id_project = ?";
            PreparedStatement statement = DbDriver.getConnection().prepareStatement(sql);
            
            statement.setString(1, name);
            statement.setString(2, shortName);
            statement.setString(3, desc);
            statement.setInt(4, id);
            
            result = statement.executeUpdate();
        }
        catch(SQLException e)
        {
            Messages.exceptionHandler("Wyjątek", "Nie udało się zapisać projektu", e);
        }
        finally
        {
            DbDriver.disconnect();
        }
        
        return result;
    }
    
    /**
     * Usunięcie projektu i podanym identyfikatorze
     * @param id Identyfikator projektu
     * @return Ilość usuniętych wierwszy
     */
    public Integer deleteProject(Integer id)
    {
        Integer[] ids = new Integer[1];
        ids[0] = id;
        
        return this.deleteProjects(ids);
    }
    
    /**
     * Usunięcie projektów o podanych identyfikatorach
     * @param ids Identyfikatory projektów
     * @return Ilość usuniętych wierwszy
     */
    public Integer deleteProjects(ArrayList<Integer> ids)
    {
        return this.deleteProjects(ids.toArray(new Integer[ids.size()]));
    }
    
    /**
     * Usunięcie projektów o podanych identyfikatorach
     * @param ids Identyfikatory projektów do usunięcia
     * @return Ilość usuniętych wierszy
     */
    public Integer deleteProjects(Integer[] ids)
    {
        Integer result = 0;
        try
        {
            StringBuilder sql = new StringBuilder();
            String[] qMarks = new String[ids.length];
            Arrays.fill(qMarks, "?");
            
            sql.append("DELETE FROM projects WHERE id_project IN (");
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
            Messages.exceptionHandler("Wyjątek", "Nie udało się usunąć\nwybranych projektów", e);
        }
        finally
        {
            DbDriver.disconnect();
        }
        
        return result;
    }
}
