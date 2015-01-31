
package pl.aroch.ejtracker.model.projects;

import java.sql.ResultSet;
import java.sql.SQLException;
import pl.aroch.ejtracker.basic.TtTime;
import pl.aroch.ejtracker.model.Projects;
import pl.aroch.ejtracker.model.Tasks;

/**
 * Klasa reprezentująca obiekt projektu
 * @author Arkadiusz Roch
 */
public class Project 
{
    /**
     * Identyfikator projektu
     */
    private Integer _id;
    
    /**
     * Nazwa projektu
     */
    private String _name;
    
    /**
     * Skrócona nazwa projektu
     */
    private String _shortName;
    
    /**
     * Opis projektu
     */
    private String _description;
    
    /**
     * Domyślny konstruktor
     */
    public Project()
    {
        this._init(0, "", "", "");
    }
    
    /**
     * Konstruktor przyjmujący jako parametry identyfikator, nazwę oraz krótką nazwę projektu
     * @param id Identyfikator
     * @param name Nazwa
     * @param shortName Krótka nazwa
     */
    public Project(Integer id, String name, String shortName)
    {
        this._init(id, name, shortName, "");
    }
    
    /**
     * Konstruktor przyjmujący jako parametry identyfikator, nazwę, krótką nazwę oraz opis projektu
     * @param id Identyfikator
     * @param name Nazwa
     * @param shortName Krótka nazwa
     * @param desc Opis
     */
    public Project(Integer id, String name, String shortName, String desc)
    {
        this._init(id, name, shortName, desc);
    }
    
    /**
     * Konstruktor przyjmujący jako parametr obiekt ResultSet
     * @param rs Dane pobrane z bazy
     * @throws SQLException 
     */
    public Project(ResultSet rs) throws SQLException
    {
        if(rs.findColumn("id_project") > 0)
        {
            this._id = rs.getInt("id_project");
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
        
        if(rs.findColumn("short_name") > 0)
        {
            this._shortName = rs.getString("short_name");
        }
        else
        {
            this._shortName = "";
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
     * Przypisanie identyfikatora projektowi
     * @param id Identyfikator
     */
    public void setId(Integer id)
    {
        this._id = id;
    }
    
    /**
     * Przypisanie nazwy projektowi
     * @param name Nazwa projektu
     */
    public void setName(String name)
    {
        this._name = name;
    }
    
    /**
     * Przypisanie skróconej nazwy projektowi
     * @param shortName Skrócona nazwa projektu
     */
    public void setShortName(String shortName)
    {
        this._shortName = shortName;
    }
    
    /**
     * Przypisanie opisu do projektu
     * @param description Opis projektu
     */
    public void setDescription(String description)
    {
        this._description = description;
    }
    
    /**
     * Pobranie identyfikatora projektu
     * @return Identyfikator projektu
     */
    public Integer getId()
    {
        return this._id;
    }
    
    /**
     * Pobranie nazwy projektu
     * @return Nazwa projektu
     */
    public String getName()
    {
        return this._name;
    }
    
    /**
     * Pobranie skróconej nazwy projektu
     * @return Skrócona nazwa projektu
     */
    public String getShortName()
    {
        return this._shortName;
    }
    
    /**
     * Pobranie opisu projektu
     * @return Opis projektu
     */
    public String getDescription()
    {
        return this._description;
    }
    
    /**
     * Zapisanie projektu w bazie danych
     * @return Identyfikator zapisanego projektu
     */
    public int save()
    {
        Projects model = new Projects();
        if(this._id == 0)
        {
            this._id = model.addProject(this._name, this._shortName, this._description);
        }
        else
        {
            model.saveProject(this._name, this._shortName, this._description, this._id);
        }
        
        return this._id;
    }
    
    /**
     * Pobranie ilości zadań przypisanych do projektu
     * @return Ilość zadań przypisanych do projektu
     */
    public Integer getTasksCount()
    {
        Projects model = new Projects();
        
        return model.getTasksCount(this._id);
    }
    
    /**
     * Pobranie ilości czasu spędzonego przy projekcie
     * @return Ilość czasu spędzonego przy projekcie
     */
    public TtTime getTime()
    {
        Tasks model = new Tasks();
        
        return new TtTime(model.getTimeForProject(this._id));
    }
    
    /**
     * Metoda inicjująca pola obiektu
     * @param id Identyfikator projektu 
     * @param name Nazwa projektu
     * @param shortName Skrócona nazwa projektu
     * @param description Opis projektu
     */
    private void _init(Integer id, String name, String shortName, String description)
    {
        this._id = id;
        this._name = name;
        this._shortName = shortName;
        this._description = description;
    }
}
