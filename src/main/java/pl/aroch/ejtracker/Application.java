
package pl.aroch.ejtracker;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.UIManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import pl.aroch.ejtracker.basic.TtUpdateStruct;
import pl.aroch.ejtracker.dialogs.ProgramAbout;
import pl.aroch.ejtracker.dialogs.UpdateInfo;
import pl.aroch.ejtracker.dialogs.projects.ProjectEdit;
import pl.aroch.ejtracker.dialogs.projects.ProjectsList;
import pl.aroch.ejtracker.dialogs.tasks.TasksHistory;
import pl.aroch.ejtracker.model.DbDriver;
import pl.aroch.ejtracker.model.UpdateChanels;
import pl.aroch.ejtracker.model.updateChanels.Chanel;
import pl.aroch.ejtracker.model.updateChanels.ChanelStatus;
import pl.aroch.ejtracker.panels.MainPanel;

/**
 * Klasa wyświetlająca główne okno
 * @author Arkadiusz Roch
 */
public class Application extends JFrame 
{
    /**
     * Wersja programu
     */
    private static Integer[] _currentVersion = {0, 1, 0};
    
    /**
     * Panel z elementami głównego okna
     */
    private MainPanel _panel;
    
    /**
     * Metoda uruchamiająca program
     */
    public void run()
    {
        this._initMenu();
        
        this._setUILanguage();
        
        this._panel = new MainPanel();
        this._panel.init();
        
        this.add(this._panel);
        
        this.setSize(300, 400);
        this.setMinimumSize(new Dimension(300, 400));
        
        this.setLocationRelativeTo(null);
        
        this.setResizable(true);
        
        this.setTitle("Ejtracker v.0.1.0");
        
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        this.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                _closeProgram();
            }
        });
    }
    
    /**
     * Metoda sprawdzająca aktualizacje
     */
    public void checkUpdates()
    {
        UpdateChanels chanelsModel = new UpdateChanels();
        
        JSONObject json = null;
        
        json = this._getUpdateInfo(chanelsModel.getChanels(ChanelStatus.CHANEL_ACTIVE));
        
        if(json == null)
        {
            json = this._getUpdateInfo(chanelsModel.getChanels(ChanelStatus.CHANEL_DEPRECATED));
            
            if(json == null)
            {
                // dla pewności, gdyby przypadkiem oznaczono wszystkie kanały jako usunięte
                json = this._getUpdateInfo(chanelsModel.getChanels(ChanelStatus.CHANEL_REMOVED));
            }
        }

        if(json != null)
        {
            this._refreshUpdateChanels(json.getJSONArray("update_chanels"));

            String stringVersion = Application.getVersionAsString();
            if(json.getString("version") != stringVersion)
            {
                Integer[] version = Application.getVersion();

                JSONObject versionParts = json.getJSONObject("version_parts");

                if(versionParts.getInt("major") > version[0] ||
                   versionParts.getInt("minor") > version[1] ||
                   versionParts.getInt("build") > version[2])
                {
                    TtUpdateStruct params = new TtUpdateStruct();

                    params.currentVersion = stringVersion;
                    params.newVersion = json.getString("version");
                    params.releaseDate = json.getString("release_date");
                    params.releaseInfo = json.getString("release_notes");
                    params.downloadAddress = json.getString("download_address");

                    UpdateInfo updateDialog = new UpdateInfo(params);

                    updateDialog.init();
                    updateDialog.setVisible(true);
                }
            }
        }
    }
    
    /**
     * Instalacja bazy danych
     */
    public void install()
    {
        DbDriver.installDatabase();
    }
    
    /**
     * Pobranie wersji jako tablica
     * @return Integer[] Wersja programu jako tablica
     */
    public static Integer[] getVersion()
    {
        return Application._currentVersion;
    }
    
    /**
     * Pobranie wersji jako string
     * @return String Wersja programu
     */
    public static String getVersionAsString()
    {
        StringBuilder version = new StringBuilder();
        
        version.append(Application._currentVersion[0]);
        version.append(".");
        version.append(Application._currentVersion[1]);
        version.append(".");
        version.append(Application._currentVersion[2]);
        
        return version.toString();
    }
    
    /**
     * Ustawienie zmiennych językowych
     */
    private void _setUILanguage()
    {
        UIManager.put("OptionPane.yesButtonText", "Tak");
        UIManager.put("OptionPane.noButtonText", "Nie");
        UIManager.put("OptionPane.cancelButtonText", "Anuluj");
    }
    
    /**
     * Dodanie menu do głównego okna
     */
    private void _initMenu()
    {
        JMenuBar menuBar = new JMenuBar();
        
        // Menu "Program"
        JMenu menu = new JMenu("Program");
        
        JMenuItem menuItem = new JMenuItem("O programie");
        menuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                _openProgramAbout();
            }
        });
        menu.add(menuItem);
        
        menuItem = new JMenuItem("Zamknij");
        menuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                _closeProgram();
            }
        });
        menu.add(menuItem);
        
        menuBar.add(menu);
        
        // Menu "Projekty"
        menu = new JMenu("Projekty");
        
        menuItem = new JMenuItem("Lista");
        
        menuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                _openProjectsListDialog();
            }
        });
        menu.add(menuItem);
        
        menuItem = new JMenuItem("Dodaj");
        menuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                _openProjectsAddDialog();
            }
        });
        menu.add(menuItem);
        
        menuBar.add(menu);
        
        /**
         * Menu "Zadania"
         */
        menu = new JMenu("Zadania");
        menuItem = new JMenuItem("Historia");
        menuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                _openTasksHistory();
            }
        });
        
        menu.add(menuItem);
        
        menuBar.add(menu);
        
        this.setJMenuBar(menuBar);
    }
    
    /**
     * Zamkniecie programu
     */
    private void _closeProgram()
    {
        if(this._panel.close())
        {
            System.exit(0);
        }
    }
    
    /**
     * Otwarcie okna z listą projektów
     */
    private void _openProjectsListDialog()
    {
        ProjectsList projectsDialog = new ProjectsList();
        projectsDialog.init();
        projectsDialog.setVisible(true);
    }
    
    /**
     * Otwarcie okna dodawania projektów
     */
    private void _openProjectsAddDialog()
    {
        ProjectEdit projectDialog = new ProjectEdit();
        projectDialog.init();
        projectDialog.setVisible(true);
    }
    
    /**
     * Otwarcie okna z informacjami o programie
     */
    private void _openProgramAbout()
    {
        ProgramAbout aboutDialog = new ProgramAbout();
        aboutDialog.init();
        aboutDialog.setVisible(true);
    }
    
    /**
     * Otwarcie okna z historią zadań
     */
    private void _openTasksHistory()
    {
        TasksHistory tasksHistory = new TasksHistory();
        tasksHistory.init();
        tasksHistory.setVisible(true);
    }
    
    /**
     * Pobranie informacji o aktualizacja z podanej listy kanałów
     * @param chanels Lista kanałów
     * @return Obiekt JSONObject z infrmacjami o najnowszej wersji programu
     */
    private JSONObject _getUpdateInfo(ArrayList<Chanel> chanels)
    {
        JSONObject result = null;
        
        for(Chanel chanel: chanels)
        {
            StringBuilder input = new StringBuilder();
            boolean errors = false;

            try
            {
                URL updateAddress = new URL(chanel.getChanel());
                BufferedReader reader = new BufferedReader(new InputStreamReader(updateAddress.openStream()));

                String line;
                while((line = reader.readLine()) != null)
                {
                    input.append(line);
                    input.append("\n");
                }
            }
            catch(MalformedURLException e)
            {
                System.out.println(e.getMessage());
                errors = true;
            }
            catch(IOException e)
            {
                System.out.println(e.getMessage());
                errors = true;
            }
            catch(Exception e)
            {
                System.out.println(e.getMessage());
                errors = true;
            }

            if(!errors && input.length() > 0)
            {
                try
                {
                    result = new JSONObject(input.toString());
                    break;
                }
                catch(JSONException e)
                {
                    System.out.println(e.getMessage());
                }
                catch(Exception e)
                {
                    System.out.println(e.getMessage());
                }
            }
        }
        
        return result;
    }
    
    /**
     * Zapisujemy nową listę kanałów aktualizacji
     * @param chanels Lista kanałów aktualizacji
     */
    private void _refreshUpdateChanels(JSONArray chanels)
    {
        UpdateChanels chanelsModel = new UpdateChanels();
        int size = chanels.length();
        for(int i = 0; i < size; i++)
        {
            JSONObject currentChanel = chanels.getJSONObject(i);
            
            //nie usuwamy kanałów, aby przypadkiem nie odciąć programu od aktualizacji
            chanelsModel.saveChanel(currentChanel.getString("chanel"), ChanelStatus.getObject(currentChanel.getString("status")));
        }
    }
}
