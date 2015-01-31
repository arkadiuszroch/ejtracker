
package pl.aroch.ejtracker.model;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import pl.aroch.ejtracker.Messages;

/**
 * Klasa obsługująca połaczenie z bazą danych
 * @author Arkadiusz Roch
 */
public class DbDriver 
{
    /**
     * Instancja połączenia
     */
    private static Connection _connection;
    
    /**
     * Nazwa sterownika bazy danych
     */
    private static String _dbDriver = "org.sqlite.JDBC";
    
    /**
     * Nazwa pliku bazy danych
     */
    private static String _dbFileName = "dbfile.db";
    
    /**
     * Adres bazy danych
     */
    private static String _dbLocation = "jdbc:sqlite:" + DbDriver._dbFileName;
    
    /**
     * Połączenie z bazą danych
     * @return Informacja o tym, czy udało się połączyć
     * @throws SQLException 
     */
    public static boolean connect() throws SQLException
    {
        if(DbDriver._connection == null)
        {
            try
            {
                Class.forName(DbDriver._dbDriver);
            }
            catch(ClassNotFoundException e)
            {
                Messages.exceptionHandler("Wyjątek", "Nie znaleziono sterownika bazy danych", e);
                return false;
            }
            
            DbDriver._connection = DriverManager.getConnection(DbDriver._dbLocation);
        }
        return true;
    }
    
    /**
     * Zakończenie połaczenia z bazą danych
     */
    public static void disconnect()
    {
        if(DbDriver._connection != null)
        {
            try
            {
                DbDriver._connection.close();
            }
            catch(SQLException e)
            {
                Messages.exceptionHandler("Wyjątek", "Nie udało się zamknąć połączenia z bazą danych", e);
            }
            finally
            {
                DbDriver._connection = null;
            }
        }
    }
    
    /**
     * Pobranie aktywnego połaczenia do bazy danych
     * @return Instancję połączenia
     * @throws SQLException 
     */
    public static Connection getConnection() throws SQLException
    {
        if(DbDriver._connection == null)
        {
            DbDriver.connect();
        }
        
        return DbDriver._connection;
    }
    
    /**
     * Sprawdzenie czy istnieje baza danych, jeśli nie jest tworzona nowa
     */
    public static void installDatabase()
    {
        File dbFile = new File(DbDriver._dbFileName);
        
        if(!dbFile.exists())
        {
            String installSQL = "CREATE TABLE `projects` (\n " +
                "`id_project` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\n " +
                "`name` TEXT NOT NULL UNIQUE,\n " +
                "`short_name` TEXT NOT NULL UNIQUE,\n " +
                "`description` TEXT\n " +
                ");" + 
                "CREATE TABLE `tasks` (\n" +
                "`id_task` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\n " +
                "`name` TEXT NOT NULL,\n " +
                "`id_project` INTEGER,\n " +
                "`start_date` TEXT,\n " +
                "`end_date` TEXT,\n " +
                "`description` TEXT\n " +
                ");" +
                "CREATE TABLE `update_chanels` (\n " +
                "`chanel` TEXT NOT NULL UNIQUE,\n " +
                "`status` TEXT NOT NULL\n " +
                ");" +
                "CREATE TABLE `settings` (\n " +
                "`name`	TEXT NOT NULL UNIQUE,\n " +
                "`value` TEXT,\n " +
                "PRIMARY KEY(name)\n " +
                ");" + 
                "INSERT INTO update_chanels VALUES ('http://ejtracker.aroch.pl/updates.php', 'active')";
            
            try
            {
                Statement statement = DbDriver.getConnection().createStatement();
                statement.executeUpdate(installSQL);
            }
            catch(SQLException e)
            {
                Messages.exceptionHandler("Błąd krytyczny", "Nie udało sie zainstalować bazy danych", e);
                System.exit(0);
            }
            finally
            {
                DbDriver.disconnect();
            }
        }
    }
}
