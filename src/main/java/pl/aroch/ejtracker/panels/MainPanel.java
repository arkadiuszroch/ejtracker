
package pl.aroch.ejtracker.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Set;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableColumnModel;
import pl.aroch.ejtracker.Messages;
import pl.aroch.ejtracker.basic.TtCellValue;
import pl.aroch.ejtracker.basic.TtComboBoxItem;
import pl.aroch.ejtracker.basic.TtComboBoxRender;
import pl.aroch.ejtracker.basic.TtDate;
import pl.aroch.ejtracker.basic.TtDefaultTableCellRenderer;
import pl.aroch.ejtracker.basic.TtDefaultTableModel;
import pl.aroch.ejtracker.basic.TtObservable;
import pl.aroch.ejtracker.basic.TtObserver;
import pl.aroch.ejtracker.basic.TtPanel;
import pl.aroch.ejtracker.basic.TtTable;
import pl.aroch.ejtracker.basic.TtTime;
import pl.aroch.ejtracker.dialogs.tasks.TaskEdit;
import pl.aroch.ejtracker.model.Projects;
import pl.aroch.ejtracker.model.Tasks;
import pl.aroch.ejtracker.model.projects.Project;
import pl.aroch.ejtracker.model.tasks.DayList;
import pl.aroch.ejtracker.model.tasks.Task;

/**
 * Panel głownego okna programu
 * @author Arkadiusz Roch
 */
public class MainPanel extends TtPanel implements TtObserver
{
    /**
     * Aktualnie wykonywane zadanie
     */
    private Task _currentTask;
 
    /**
     * Flaga określająca czy zegar jest uruchomiony
     */
    private boolean _isTimerRunning;
    
    /**
     * Lista projektów
     */
    private JComboBox _projects;
    
    /**
     * Panel z suwakiem, w którym będzie lista zadań
     */
    JScrollPane _scroll;
    
    /**
     * Przycisk rozpoczynający oraz zatrzymujący zegar
     */
    private JButton _startStop;
    
    /**
     * Czas rozpoczęcia zadania
     */
    private long _startTime;
    
    /**
     * Nazwa aktualnie wykonywanego zadania
     */
    private JTextField _task;
    
    /**
     * Panel z listą zadań wykonywanych w ciągu ostatnich trzech dni
     */
    private JPanel _tasksPanel;
    
    /**
     * Lista tabel z zadaniami, kluczem jest data 
     */
    private LinkedHashMap<String, TtTable> _tasksTables;
    
    /**
     * Czas aktualnie wykonywanego zadania
     */
    private JTextField _time;
    
    /**
     * Timer wykonujący funkcję aktualizującą zegar
     */
    private Timer _timer;
    
    /**
     * Panel z informacjami o aktualnym zadaniu
     */
    private JPanel _topPanel;
    
    /**
     * Metoda inicjująca panel;
     */
    @Override
    public void init()
    {
        this._currentTask = null;
        
        this._isTimerRunning = false;
        
        this._startTime = 0;
        
        this._projects = new JComboBox();
        this._projects.setEditable(false);
        this._projects.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                _projectChanged();
            }
        });
        
        this._fillProjects();
        
        this._task = new JTextField();
        this._task.getDocument().addDocumentListener(new DocumentListener() 
        {
            @Override
            public void insertUpdate(DocumentEvent e) 
            {
                _taskChanged();
            }

            @Override
            public void removeUpdate(DocumentEvent e) 
            {
                _taskChanged();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {}
        });
        
        Font timeFont = new Font("Arial", Font.PLAIN, 20);
        
        this._time = new JTextField("00:00");
        this._time.setFont(timeFont);
        this._time.setEditable(false);
        this._time.setHorizontalAlignment(JTextField.RIGHT);
        
        this._startStop = new JButton("Start");
        this._startStop.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) 
            {
                _startStopButtonClick();
            }
        });
        
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        
        this._initPanel();
        
        this._tasksPanel = new JPanel();
        this._tasksPanel.setLayout(new BoxLayout(this._tasksPanel, BoxLayout.PAGE_AXIS));
        this._scroll = new JScrollPane(this._tasksPanel);
        this.add(this._scroll);
        
        this._tasksTables = new LinkedHashMap<>();
        
        this._fillTasksList();
        
        this._loadPendingTask();
        
        TtObservable.getInstance(TtObservable.TASK_CHANGED).addObserver(this);
        TtObservable.getInstance(TtObservable.TASK_ADDED).addObserver(this);
        TtObservable.getInstance(TtObservable.TASK_REMOVED).addObserver(this);
        TtObservable.getInstance(TtObservable.PROJECT_CHANGED).addObserver(this);
        TtObservable.getInstance(TtObservable.PROJECT_ADDED).addObserver(this);
        TtObservable.getInstance(TtObservable.PROJECT_REMOVED).addObserver(this);
    }
    
    /**
     * Metoda wykonywana podczas zamykania okna
     * @return Flaga określajaca czy można zamknąć okno
     */
    public boolean close()
    {
        if(this._isTimerRunning)
        {
            int confirm = Messages.questionYesNo("Ostrzeżenie", "Zegar cały czas działa, na pewno chcesz zamknąć program?\nPo ponownym uruchomieniu czas będzie dalej liczony");
            
            if(confirm == Messages.YES_OPTION)
            {
                return true;
            }
            else 
            {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Metoda aktualizująca widok okna po dokonaniu zmian w liście projektów lub zadań
     * @param args Dodatkowe argumenty
     * @param actionName Nazwa akcji
     */
    @Override
    public void update(Object args,  String actionName)
    {
        if(actionName.equals(TtObservable.TASK_CHANGED) || actionName.equals(TtObservable.TASK_ADDED) || actionName.equals(TtObservable.TASK_REMOVED))
        {
            this._fillTasksList();
        }
        else if(actionName.equals(TtObservable.PROJECT_CHANGED))
        {
            this._fillProjects();
            this._fillTasksList();
        }
        else if(actionName.equals(TtObservable.PROJECT_ADDED) || actionName.equals(TtObservable.PROJECT_REMOVED))
        {
            this._fillProjects();
        }
    }
    
    private void _clearSelections(MouseEvent e)
    {
        TtTable currentTable = (TtTable)e.getSource();
        Integer tableId = currentTable.getTableId();
        
        Set<String> tableKeys = this._tasksTables.keySet();
        
        for(String key: tableKeys)
        {
            TtTable table = this._tasksTables.get(key);
            
            if(tableId != table.getTableId())
            {
                table.clearSelection();
            }
        }
    }
    
    /**
     * Metoda otwierająca okno edycji zadania
     * @param e Obiekt zdarzenia MouseEvent
     */
    private void _editTask(MouseEvent e)
    {
        if(e.getClickCount() == 2)
        {
            TtTable table = (TtTable)e.getSource();        
            
            Integer row = table.rowAtPoint(e.getPoint());
            TtDefaultTableModel model = table.getTtTableModel();
            
            TaskEdit editDialog = new TaskEdit(model.getRowId(row));
            editDialog.init();
            editDialog.setVisible(true);
        }
    }
    
    /**
     * Metoda uzupełniająca listę projektów
     */
    private void _fillProjects()
    {
        Projects projectsModel = new Projects();
        
        ArrayList<Project> projectsList = projectsModel.getProjectsList();
        Integer currentId = 0;
        
        if(this._projects.getSelectedIndex() > 0)
        {
            TtComboBoxItem item = (TtComboBoxItem)this._projects.getSelectedItem();
            currentId = item.getId();
        }
        
        this._projects.removeAllItems();
        
        this._projects.addItem(new TtComboBoxItem(0, "Brak"));
        
        this._projects.setRenderer(new TtComboBoxRender());
        
        int size = projectsList.size();
        for(int i = 0; i < size; i++)
        {
            Project project = projectsList.get(i);
            this._projects.addItem(new TtComboBoxItem(project.getId(), project.getShortName()));
            if(currentId == project.getId())
            {
                this._projects.setSelectedIndex(i);
            }
        }
    }
    
    /**
     * Metoda uzupełniająca listę ostatnio wykonanych zadań
     */
    private void _fillTasksList()
    {
        this._tasksPanel.removeAll();
        
        Tasks tasksModel = new Tasks();
        Projects projectsModel = new Projects();
        
        ArrayList<DayList> tasksList = tasksModel.getTasksPerDay(true);
        int tasksListSize = tasksList.size();
        
        if(tasksListSize > 0)
        {
            for(int current = 0; current < tasksListSize; current++)
            {
                DayList list = tasksList.get(current);

                TtDefaultTableModel model = null;

                if(!this._tasksTables.containsKey(list.getDay()))
                {
                    TtTable table = new TtTable();

                    table.setTableId(current);
                    table.addMouseListener(new MouseAdapter()
                    {
                        public void mousePressed(MouseEvent e)
                        {
                            _clearSelections(e);
                            _editTask(e);
                        }
                    });

                    model = new TtDefaultTableModel();
                    model.addColumn("Projekt");
                    model.addColumn("Zadanie");
                    model.addColumn("Czas");

                    this._tasksTables.put(list.getDay(), table);
                }
                else 
                {
                    model = this._tasksTables.get(list.getDay()).getTtTableModel();
                    int rowsCount = model.getRowCount() - 1;
                    for(int i = rowsCount; i >= 0; i--)
                    {
                        model.removeRow(i);
                    }
                }

                ArrayList<Task> dayTasksList = list.getTasks();
                int size = dayTasksList.size();

                for(int i = 0; i < size; i++)
                {
                    Task item = dayTasksList.get(i);

                    TtCellValue row[] = new TtCellValue[3];
                    TtTime time = new TtTime(item.getTime());
                    StringBuilder timeText = new StringBuilder();

                    row[0] = new TtCellValue();
                    row[1] = new TtCellValue();
                    row[2] = new TtCellValue();

                    if(item.getProjectId() > 0)
                    {
                        Project project = projectsModel.getProject(item.getProjectId());
                        row[0].setValue(project.getShortName());
                        row[0].setToolTip(project.getName());
                    }
                    else
                    {
                        row[0].setValue("Brak");
                    }
                    
                    if(item.getName().length() > 0)
                    {
                        row[1].setValue(item.getName());
                        row[1].setToolTip(item.getName());
                    }
                    else
                    {
                        row[1].setValue("Pusto");
                    }
                    
                    time.setTime(item.getTime());
                    if(time.getHours() > 0)
                    {
                        timeText.append(String.format("%02d:", time.getHours()));
                    }
                    
                    timeText.append(String.format("%02d:%02d", time.getMinutes(), time.getSeconds()));

                    row[2].setValue(timeText.toString());
                    row[2].setToolTip(item.getStartDate().format("hh:mm:ss") + " - " + item.getEndDate().format("hh:mm:ss"));

                    model.setRowId(i, item.getId());

                    model.addRow(row);
                }

                this._tasksTables.get(list.getDay()).setTtModel(model);

                TtDefaultTableCellRenderer rightCellRenderer = new TtDefaultTableCellRenderer();
                TtDefaultTableCellRenderer cellRenderer = new TtDefaultTableCellRenderer();

                rightCellRenderer.setHorizontalAlignment(SwingConstants.RIGHT);

                TableColumnModel columnModel = this._tasksTables.get(list.getDay()).getColumnModel();
                columnModel.setColumnSelectionAllowed(false);

                columnModel.getColumn(0).setMaxWidth(75);
                columnModel.getColumn(0).setPreferredWidth(75);
                columnModel.getColumn(0).setCellRenderer(cellRenderer);

                columnModel.getColumn(1).setCellRenderer(cellRenderer);

                columnModel.getColumn(2).setMaxWidth(55);
                columnModel.getColumn(2).setPreferredWidth(55);

                columnModel.getColumn(2).setCellRenderer(rightCellRenderer);

                JPanel panel = new JPanel();
                panel.setLayout(new BorderLayout());
                panel.setBackground(new Color(200, 200, 200));
                panel.setMaximumSize(new Dimension(Short.MAX_VALUE, 20));
                panel.setMinimumSize(new Dimension(Short.MIN_VALUE, 20));
                panel.setPreferredSize(new Dimension(Short.MIN_VALUE, 20));

                panel.add(new JLabel(list.getDay()), BorderLayout.LINE_START);
                panel.add(new JLabel(String.format("%02d:%02d:%02d", list.getTime().getHours(), list.getTime().getMinutes(), list.getTime().getSeconds())), BorderLayout.LINE_END);

                this._tasksPanel.add(panel);

                this._tasksPanel.add(this._tasksTables.get(list.getDay()));
            }
        }
        else
        {
            JPanel panel = new JPanel();
            
            panel.setLayout(new BorderLayout());
            
            JLabel desc = new JLabel("Brak zadań w ciągu ostatnich 3 dni");
            desc.setVerticalAlignment(JLabel.CENTER);
            desc.setHorizontalAlignment(JLabel.CENTER);
            
            panel.add(desc, BorderLayout.CENTER);
            
            this._tasksPanel.add(panel);
        }
        // Wymuszenie przeładowania tabeli, repaint ani nic innego nie chce zadziałać
        this._tasksPanel.setVisible(false);
        this._tasksPanel.setVisible(true);
    }
    
    /**
     * Metoda inicjująca panel z zegarem
     */
    private void _initPanel()
    {
        this._topPanel = new JPanel();
        
        GroupLayout topLayout = new GroupLayout(this._topPanel);
        
        this._topPanel.setLayout(topLayout);
        
        JLabel tasksListLabel = new JLabel("Lista zadań");
        
        JSeparator tasksListSeparator = new JSeparator();
        
        topLayout.setAutoCreateGaps(true);
        topLayout.setAutoCreateContainerGaps(true);
        
        topLayout.setHorizontalGroup
        (
            topLayout.createParallelGroup()
                .addGroup
                (
                    topLayout.createSequentialGroup()
                    .addGroup
                    (
                        topLayout.createParallelGroup()
                        .addComponent(this._projects, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(this._task, GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                    )
                    .addComponent(this._time, 85, 85, 85)
                )
                .addGroup
                (
                    topLayout.createSequentialGroup()
                    .addComponent(this._startStop, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                )
                .addGroup
                (
                    topLayout.createParallelGroup()
                    .addComponent(tasksListSeparator)
                    .addComponent(tasksListLabel)
                )
        );
        
        topLayout.setVerticalGroup
        (
            topLayout.createParallelGroup()
            .addGroup
            (
                topLayout.createSequentialGroup()
                .addGroup
                (
                    topLayout.createParallelGroup()
                    .addComponent(this._time, 56, 56, 56)
                    .addGroup
                    (
                        topLayout.createSequentialGroup()
                        .addComponent(this._projects, 25, 25, 25)
                        .addComponent(this._task, 25, 25, 25)
                    )
                )
                .addGroup
                (
                    topLayout.createParallelGroup()
                    .addComponent(this._startStop, 35, 35, 35)
                )
                .addGroup
                (
                    topLayout.createSequentialGroup()
                        .addComponent(tasksListSeparator, 2, 2, 2)
                        .addComponent(tasksListLabel, 15, 15, 15)
                )
            )
        );
        
        this.add(this._topPanel);
    }
    
    /**
     * Metoda ładująca zadanie, które nie zostało zakończone przed ostatnim zamknięciem programu.
     * Uruchamiana jest tylko raz, podczas uruchamiania programu.
     */
    private void _loadPendingTask()
    {
        Tasks tasks = new Tasks();
        this._currentTask = tasks.getPendingTask();

        if(this._currentTask != null)
        {
            this._startStopButtonClick();
        }
    }
    
    /**
     * Metoda aktualizująca identyfikator zadania, do którego przypisany jest projekt
     */
    private void _projectChanged()
    {
        if(this._currentTask != null)
        {
            this._currentTask.setProjectId(((TtComboBoxItem)(this._projects.getSelectedItem())).getId());
            this._currentTask.save();
        }
    }
    
    /**
     * Metoda uruchamiająca oraz zatrzymująza zegar. Po zatrzymaniu zegara zadanie
     * jest zapisywane w bazie danych, a panel zegara jest resetowany do domyślnych ustawień
     */
    private void _startStopButtonClick()
    {
        if(this._isTimerRunning)
        {
            this._currentTask.setEndDate(new TtDate());
            this._currentTask.save();
            
            this._currentTask = null;
            
            this._isTimerRunning = false;
            this._startStop.setText("Start");
            this._startTime = 0;
            this._timer.stop();
            this._time.setText("00:00");
            this._projects.setSelectedIndex(0);
            this._task.setText("");
            
            TtObservable o = TtObservable.getInstance(TtObservable.TASK_ADDED);
            o.setChanged();
            o.notifyObservers(null);
        }
        else
        {
            this._isTimerRunning = true;
            
            this._startStop.setText("Stop");
            
            if(this._currentTask != null)
            {
                this._startTime = this._currentTask.getStartDate().getTimestamp();
                this._task.setText(this._currentTask.getName());
                
                if(this._currentTask.getProjectId() == 0)
                {
                    this._projects.setSelectedIndex(0);
                }
                else
                {
                    int size = this._projects.getItemCount();
                    for(int i = 0; i < size; i++)
                    {
                        if(((TtComboBoxItem)this._projects.getItemAt(i)).getId() == this._currentTask.getProjectId())
                        {
                            this._projects.setSelectedIndex(i);
                            break;
                        }
                    }
                }
            }
            else
            {
                this._startTime = new TtDate().getTimestamp();
            }
            
            ActionListener taskPerformer = new ActionListener() 
            {
                @Override
                public void actionPerformed(ActionEvent e) 
                {
                    _timerAction();
                }
            };
            
            this._timer = new Timer(500, taskPerformer);
            this._timer.start();
            
            if(this._currentTask == null)
            {
                this._currentTask = new Task(0, this._task.getText(), ((TtComboBoxItem)this._projects.getSelectedItem()).getId(), new TtDate(this._startTime, true), null);
                this._currentTask.save();
            }
        }
    }
    
    /**
     * Metoda zapisująca na bierząco zmiany w zadaniu
     */
    private void _taskChanged()
    {
        if(this._currentTask != null)
        {
            this._currentTask.setName(this._task.getText());
            this._currentTask.save();
        }
    }
    
    /**
     * Metoda odświeżająca zegar
     */
    private void _timerAction()
    {
        TtTime time = new TtTime(0);
        
        Integer diff = (int)((new TtDate()).getTimestamp() - this._startTime);
        time.setTime(diff);

        Integer seconds = time.getSeconds();
        Integer minutes = time.getMinutes();
        Integer hours = time.getHours();

        StringBuilder timeText = new StringBuilder();

        timeText.append(String.format("%02d", minutes));
        timeText.append(":");
        timeText.append(String.format("%02d", seconds));

        if(hours > 0)
        {
            timeText.insert(0, String.format("%02d", hours) + ":");
        }

        this._time.setText(timeText.toString());
    }
}
