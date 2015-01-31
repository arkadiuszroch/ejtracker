
package pl.aroch.ejtracker.model.tasks;

import java.sql.ResultSet;
import java.sql.SQLException;
import pl.aroch.ejtracker.basic.TtDate;
import pl.aroch.ejtracker.model.Tasks;

/**
 * Klasa reprezentująca obiekt zadania
 * @author Arkadiusz Roch
 */
public class Task 
{
    /**
     * Identyfikator zadania
     */
    private Integer _id;
    
    /**
     * Nazwa zadania
     */
    private String _name;
    
    /**
     * Nazwa projektu do którego przypisane jest zadanie
     */
    private Integer _projectId;
    
    /**
     * Data rozpoczęcia zadania
     */
    private TtDate _startDate;
    
    /**
     * Data zakończenia zadania
     */
    private TtDate _endDate;
    
    /**
     * Dodatkowy opis zadania
     */
    private String _description;
    
    /**
     * Domyślny konstruktor
     */
    public Task()
    {
        this._init(0, "", 0, null, null, "");
    }
    
    /**
     * Konstroktor przyjmujący jako parametry identyfikator, nazwę, id projektu,
     * datę rozpoczęcia oraz zakończenia zadania
     * @param id Identyfikator zadania
     * @param name Nazwa zadania
     * @param projectId Identyfikator projektu
     * @param startDate Data rozpoczęcia zadania
     * @param endDate Data zakończenia zadania
     */
    public Task(Integer id, String name, Integer projectId, TtDate startDate, TtDate endDate)
    {
        this._init(id, name, projectId, startDate, endDate, "");
    }
    
    /**
     * Konstruktor przyjmujący jako parametry identyfikator, nazwę, id projektu,
     * datę rozpoczęcia oraz zakończenia zadania, a takżę dodatkowy opis
     * @param id Identyfikator zadania
     * @param name Nazwa zadania
     * @param projectId Identyfikator projektu
     * @param startDate Data rozpoczęcia zadania
     * @param endDate Data zakończenia zadania
     * @param description Dodatkowy opis zadania
     */
    public Task(Integer id, String name, Integer projectId, TtDate startDate, TtDate endDate, String description)
    {
        this._init(id, name, projectId, startDate, endDate, description);
    }
    
    /**
     * Konstruktor przyjmujący jako parametr obiekt ResultSet
     * @param rs Dane pobrane z bazy
     * @throws SQLException 
     */
    public Task(ResultSet rs) throws SQLException
    {
        if(rs.findColumn("id_task") > 0)
        {
            this._id = rs.getInt("id_task");
        }
        else
        {
            this._id = 0;
        }
        
        if(rs.findColumn("name") > 0)
        {
            this._name = rs.getString("name");
        }
        else
        {
            this._name = "";
        }
        
        if(rs.findColumn("id_project") > 0)
        {
            this._projectId = rs.getInt("id_project");
        }
        else
        {
            this._projectId = 0;
        }
        
        if(rs.findColumn("start_date") > 0)
        {
            this._startDate = new TtDate(rs.getInt("start_date"), true);
        }
        else
        {
            this._startDate = null;
        }
        
        if(rs.findColumn("end_date") > 0)
        {
            this._endDate = new TtDate(rs.getInt("end_date"), true);
        }
        else
        {
            this._endDate = null;
        }
        
        if(rs.findColumn("description") > 0)
        {
            this._description = rs.getString("description");
        }
        else
        {
            this._description = "";
        }
    }
    
    /**
     * Przyposanie identyfikatora zadaniu
     * @param id Identyfikator zadania
     */
    public void setId(Integer id)
    {
        this._id = id;
    }
    
    /**
     * Przypisanie nazwy zadania
     * @param name Nazwa zadania
     */
    public void setName(String name)
    {
        this._name = name;
    }
    
    /**
     * Przypisanie identyfikatora projektu
     * @param projectId Identyfikator projektu
     */
    public void setProjectId(Integer projectId)
    {
        this._projectId = projectId;
    }
    
    /**
     * Przypisanie daty rozpoczęcia zadania
     * @param date Data rozpoczęcia zadania
     */
    public void setStartDate(TtDate date)
    {
        this._startDate = date;
    }
    
    /**
     * Przypisanie daty zakończenia zadania
     * @param date Data zakończenia zadania
     */
    public void setEndDate(TtDate date)
    {
        this._endDate = date;
    }
    
    /**
     * Przypisanie dodatkowego opisu
     * @param description Dodatkowy opis
     */
    public void setDescription(String description)
    {
        this._description = description;
    }
    
    /**
     * Pobranie identyfikatora zadania
     * @return Identyfikator zadania
     */
    public Integer getId()
    {
        return this._id;
    }
    
    /**
     * Pobranie nazwy zadania
     * @return Nazwa zadania
     */
    public String getName()
    {
        return this._name;
    }
    
    /**
     * Pobranie idetyfikatora projektu, do którego przypisane jest zadanie
     * @return Identyfikator projektu
     */
    public Integer getProjectId()
    {
        return this._projectId;
    }
    
    /**
     * Pobranie ilości czasu jaki poświęconego na wykonanie zadania
     * @return Ilośc czasu
     */
    public Integer getTime()
    {
        if(this._startDate == null)
        {
            return 0;
        }
        else
        {
            TtDate endDate = new TtDate();
            
            if(this._endDate != null)
            {
                endDate.setTimestamp(this._endDate.getTimestamp());
            }
            
            return (int)(endDate.getTimestamp() - this._startDate.getTimestamp());
        }
    }
    
    /**
     * Pobranie daty rozpoczęcia zadania
     * @return Data rozpoczęcia zadania
     */
    public TtDate getStartDate()
    {
        return this._startDate;
    }
    
    /**
     * Pobranie day zakończenia zadania
     * @return Data zakończenia zadania
     */
    public TtDate getEndDate()
    {
        return this._endDate;
    }
    
    /**
     * Pobranie dodatkowego opisu
     * @return Dodatkowy opis
     */
    public String getDescription()
    {
        return this._description;
    }
    
    /**
     * Zapisanie zadania w bazie danych
     * @return Identyfikator zapisanego zadania
     */
    public int save()
    {
        Tasks tasks = new Tasks();
        TtDate startDate = (this._startDate != null) ? this._startDate : new TtDate(0);
        TtDate endDate = (this._endDate != null) ? this._endDate : new TtDate(0);
        
        if(this._id == 0)
        {
            this._id = tasks.addTask(this._name, this._projectId, startDate, endDate, this._description);
        }
        else
        {
            tasks.saveTask(this._id, this._name, this._projectId, startDate, endDate, this._description);
        }
        
        return this._id;
    }
    
    /**
     * Metoda inicjująca pola obiektu
     * @param id Identyfikator zadania
     * @param name Nazwa zadania
     * @param projectId Identyfikator projektu
     * @param startDate Data rozpoczęcia zadania
     * @param endDate Data zakończenia zadania
     * @param description Dodatkowy opis zadania
     */
    private void _init(Integer id, String name, Integer projectId, TtDate startDate, TtDate endDate, String description)
    {
        this._id = id;
        this._name = name;
        this._projectId = projectId;
        this._startDate = startDate;
        this._endDate = endDate;
        this._description = description;
    }
}
