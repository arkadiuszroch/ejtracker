
package pl.aroch.ejtracker.panels.tasks;

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
import javax.swing.SwingConstants;
import javax.swing.table.TableColumnModel;
import pl.aroch.ejtracker.Messages;
import pl.aroch.ejtracker.basic.TtCellValue;
import pl.aroch.ejtracker.basic.TtComboBoxItem;
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
 * Panel okna historii zadań
 * @author Arkadiusz Roch
 */
public class TasksHistoryPanel extends TtPanel implements TtObserver 
{
    /**
     * Aktualnie wybrany projekt
     */
    private Integer _currentProject;
    
    /**
     * Select z listą projektów
     */
    private JComboBox _projects;
 
    /**
     * Panel z tabelami, w których znajdują się zadania
     */
    private JPanel _tasksPanel;
    
    /**
     * Lista tabel, kluczem jest data, wartością tabela z listą zadań
     */
    private LinkedHashMap<String, TtTable> _tasksTables;
    
    /**
     * Metoda inicjująca elementy panelu
     */
    @Override
    public void init()
    {
        this._currentProject = -1;
        
        JLabel title = new JLabel("Historia zadań");
        title.setFont(new Font("Arial", Font.PLAIN, 20));
        
        JSeparator topSeparator = new JSeparator();
        JSeparator bottomSeparator = new JSeparator();
        
        this._tasksPanel = new JPanel();
        this._tasksPanel.setLayout(new BoxLayout(this._tasksPanel, BoxLayout.PAGE_AXIS));
        
        this._tasksTables = new LinkedHashMap<>();
        
        this._fillTasksList();
        
        JScrollPane scroll = new JScrollPane(this._tasksPanel);
        
        Projects projectsModel = new Projects();
        
        this._projects = new JComboBox();
        
        this._projects.addItem(new TtComboBoxItem(-1, "Wszystkie projekty"));
        this._projects.addItem(new TtComboBoxItem(0, "Brak"));
        for(Project project: projectsModel.getProjectsList())
        {
            this._projects.addItem(new TtComboBoxItem(project.getId(), project.getShortName()));
        }
        
        this._projects.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                _changeProject();
            }
        });
        
        JLabel filterLabel = new JLabel("Filtruj:");
        
        JButton editButton = new JButton("Edytuj");
        editButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                _editTaskFromButton();
            }
        });
        
        JButton deleteButton = new JButton("Usuń");
        deleteButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                _remove();
            }
        });
        
        GroupLayout layout = new GroupLayout(this);
        
        this.setLayout(layout);
        
        layout.setAutoCreateContainerGaps(true);
        layout.setAutoCreateGaps(true);
        
        layout.setHorizontalGroup
        (
            layout.createParallelGroup()
            .addGroup
            (
                layout.createSequentialGroup()
                .addComponent(title)
                .addContainerGap(0, Short.MAX_VALUE)
                .addComponent(editButton)
                .addComponent(deleteButton)
            )
            .addComponent(topSeparator)
            .addGroup
            (
                layout.createSequentialGroup()
                .addComponent(filterLabel)
                .addComponent(this._projects)
            )
            .addComponent(scroll)
            .addComponent(bottomSeparator)
        );
        
        layout.setVerticalGroup
        (
            layout.createSequentialGroup()
            .addGroup
            (
                layout.createParallelGroup()
                .addComponent(title)
                .addComponent(editButton)
                .addComponent(deleteButton)
            )
            .addComponent(topSeparator, 1, 1, 1)
            .addGroup
            (
                layout.createParallelGroup()
                .addComponent(filterLabel, 27, 27, 27)
                .addComponent(this._projects, 27, 27, 27)
            )
            .addComponent(scroll)
            .addComponent(bottomSeparator, 1, 1, 1)
        );
        
        TtObservable.getInstance(TtObservable.TASK_CHANGED).addObserver(this);
        TtObservable.getInstance(TtObservable.TASK_REMOVED).addObserver(this);
    }
    
    /**
     * Metoda wywoływana przez menadżera obserwatorów w przypadku gdy zostało dodane
     * lub zmodyfikowane jakiś zadanie.
     * @param args Dodatkowe argumenty - nie wykorzystywane
     * @param actionName Nazwa instancji obserwatora - nie wykorzystywane
     */
    @Override
    public void update(Object args, String actionName)
    {
        this._fillTasksList();
    }
    
    /**
     * Metoda aktualizująca listę po zmianie projektu
     */
    private void _changeProject()
    {
        TtComboBoxItem item = (TtComboBoxItem)this._projects.getSelectedItem();
        
        this._currentProject = item.getId();
        this._fillTasksList();
    }
    
    /**
     * Wyczyszenie zaznaczenia dla wszystkich tabel z wyjątkiem ostatnio klikniętej
     * @param e Obiekt zdarzenia MouseEvent
     */
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
     * Edycja wybranego zadania
     * @param id Identyfikator zadania
     */
    private void _edit(Integer id)
    {
        TaskEdit editDialog = new TaskEdit(id);
        editDialog.init();
        editDialog.setVisible(true);
    }
    
    /**
     * Wywołanie okna edycji poprzez kliknięcie w przycisk "Edytuj"
     */
    private void _editTaskFromButton()
    {
        TtTable table = this._getSelectedTable();
        if(table != null)
        {
            if(table.getSelectedRowCount() == 1)
            {
                Integer row = table.getSelectedRow();
                TtDefaultTableModel model = table.getTtTableModel();

                this._edit(model.getRowId(row));
            }
            else
            {
                Messages.warning("Błąd", "Można wybrać tylko jedno zadanie do edycji");
            }
        }
        else
        {
            Messages.warning("Błąd", "Nie wybrano żadnego zadania do edycji");
        }
    }
    
    /**
     * Wywołanie okna edycji poprzez podwójne kliknięcie myszką w wybrane zadanie
     * @param e Obiekt zdarzenia MouseEvent
     */
    private void _editTaskFromClick(MouseEvent e)
    {
        if(e.getClickCount() == 2)
        {
            TtTable table = (TtTable)e.getSource();
            
            Integer row = table.rowAtPoint(e.getPoint());
            TtDefaultTableModel model = table.getTtTableModel();
            
            this._edit(model.getRowId(row));
        }
    }
    
    /**
     * Metoda wypełniająca listę projektów
     */
    private void _fillTasksList()
    {
        this._tasksPanel.removeAll();
        
        Tasks tasksModel = new Tasks();
        Projects projectsModel = new Projects();
        
        ArrayList<DayList> tasksList = tasksModel.getTasksPerDay(false, this._currentProject);
        int tasksListSize = tasksList.size();
        
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
                    @Override
                    public void mousePressed(MouseEvent e)
                    {
                        _clearSelections(e);
                        _editTaskFromClick(e);
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
            panel.add(new JLabel(String.format("%02d:%02d:%02d", list.getTime().getAsHours(), list.getTime().getMinutes(), list.getTime().getSeconds())), BorderLayout.LINE_END);

            this._tasksPanel.add(panel);
            
            this._tasksPanel.add(this._tasksTables.get(list.getDay()));
        }
        // Wymuszenie przeładowania tabeli, repaint ani nic innego nie chce zadziałać
        this._tasksPanel.setVisible(false);
        this._tasksPanel.setVisible(true);
    }
    
    /**
     * Pobranie tabeli z aktualnie zaznaczonym wierszem
     * @return Tabela z aktualnie zaznaczonym wierszem
     */
    private TtTable _getSelectedTable()
    {
        for(String key: this._tasksTables.keySet())
        {
            TtTable table = this._tasksTables.get(key);
            if(table.getSelectedRowCount() > 0)
            {
                return table;
            }
        }
        return null;
    }
    
    /**
     * Usunięcie wybraqnych zadań
     */
    private void _remove()
    {
        TtTable table = this._getSelectedTable();
        
        if(table != null)
        {
            if(Messages.YES_OPTION == Messages.questionYesNo("Potwierdzenie", "Czy na pewno chcesz usunąć wybrane zadania?"))
            {
                TtDefaultTableModel tableModel = table.getTtTableModel();
                Tasks tasksModel = new Tasks();
                
                int[] rows = table.getSelectedRows();
                
                int size = rows.length;
                
                Integer[] ids = new Integer[size];
                
                for(int i = 0; i < size; i++)
                {
                    ids[i] = tableModel.getRowId(rows[i]);
                }
                
                tasksModel.deleteTasks(ids);
                
                TtObservable observable = TtObservable.getInstance(TtObservable.TASK_REMOVED);
                observable.setChanged();
                observable.notifyObservers(null);
            }
        }
        else
        {
            Messages.warning("Błąd", "Nie wybrano żadnego zadania do usunięcia");
        }
    }
}
