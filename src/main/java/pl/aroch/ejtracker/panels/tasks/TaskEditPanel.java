
package pl.aroch.ejtracker.panels.tasks;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import pl.aroch.ejtracker.Messages;
import pl.aroch.ejtracker.basic.TtComboBoxItem;
import pl.aroch.ejtracker.basic.TtDate;
import pl.aroch.ejtracker.basic.TtObservable;
import pl.aroch.ejtracker.basic.TtPanel;
import pl.aroch.ejtracker.basic.TtTimeSpinner;
import pl.aroch.ejtracker.model.Projects;
import pl.aroch.ejtracker.model.Tasks;
import pl.aroch.ejtracker.model.projects.Project;
import pl.aroch.ejtracker.model.tasks.Task;

/**
 * Panel okna edycji zadania
 * @author Arkadiusz Roch
 */
public class TaskEditPanel extends TtPanel implements ActionListener,ChangeListener,DocumentListener
{
    /**
     * Input z dodatkowym opisem zadania
     */
    private JTextArea _descriptionInput;
    
    /**
     * Input z czasem zakończenia zadania
     */
    private JSpinner _endTimeSpinner;
    
    /**
     * Identyfikator zadania
     */
    private Integer _id;
    
    /**
     * Flaga określająca czy zostały wprowadzone jakieś zmiany
     */
    private boolean _modified;
    
    /**
     * Input z nazwą zadania
     */
    private JTextField _nameInput;
    
    /**
     * Klasa okna nadrzędna dla panelu
     */
    private JDialog _parent;
    
    /**
     * Select z listą projektów
     */
    private JComboBox _projects;
    
    /**
     * Input z czasem rozpoczęcia zadania
     */
    private JSpinner _startTimeSpinner;
    
    /**
     * Obiekt zadania
     */
    private Task _task;
    
    /**
     * Konstruktor przyjmujący jako parametry identyfikator zadania oraz obiekt nadrzędny
     * @param id Identyfikator zadania
     * @param parent Obiekt nadrzędny
     */
    public TaskEditPanel(Integer id, JDialog parent)
    {
        this._id = id;
        this._parent = parent;
    }
    
    /**
     * Metoda inicjująca elementy panelu
     */
    @Override
    public void init()
    {
        Tasks tasksModel = new Tasks();
        this._task = tasksModel.getTaskById(this._id);
        
        JLabel nameLabel = new JLabel("Nazwa zadania:");
        JLabel projectLabel = new JLabel("Projekt:");
        JLabel startTimeLabel = new JLabel("Czas rozpoczęcia:");
        JLabel endTimeLabel = new JLabel("Czas zakończenia:");
        JLabel descriptionLabel = new JLabel("Dodatkowy opis:");
        
        this._nameInput = new JTextField(this._task.getName());
        this._nameInput.getDocument().addDocumentListener(this);
        
        this._projects = new JComboBox();
        this._projects.addActionListener(this);
        
        Projects projectsModel = new Projects();
        ArrayList<Project> projectsList = projectsModel.getProjectsList();
        
        int size = projectsList.size();
        
        this._projects.addItem(new TtComboBoxItem(0, "Brak"));
        
        for(int i = 0; i < size; i++)
        {
            Project item = projectsList.get(i);
            this._projects.addItem(new TtComboBoxItem(item.getId(), item.getShortName() + " - " + item.getName()));
            if(this._task.getProjectId() == item.getId())
            {
                this._projects.setSelectedIndex(i+1);
            }
        }
        
        this._startTimeSpinner = TtTimeSpinner.getTimeSpinner(this._task.getStartDate(), "HH:mm:ss dd-MM-yyyy");
        this._startTimeSpinner.addChangeListener(this);
        
        this._endTimeSpinner = TtTimeSpinner.getTimeSpinner(this._task.getEndDate(), "HH:mm:ss dd-MM-yyyy");
        this._endTimeSpinner.addChangeListener(this);
        
        this._descriptionInput = new JTextArea(this._task.getDescription());
        this._descriptionInput.setLineWrap(true);
        this._descriptionInput.setWrapStyleWord(true);
        this._descriptionInput.getDocument().addDocumentListener(this);
        
        JScrollPane scroll = new JScrollPane(this._descriptionInput);
        JSeparator separator = new JSeparator();
        
        JButton saveButton = new JButton("Zapisz");
        saveButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                _save();
            }
        });
        
        JButton cancelButton = new JButton("Anuluj");
        cancelButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                close();
            }
        });
        
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        
        layout.setHorizontalGroup
        (
            layout.createParallelGroup()
            .addComponent(nameLabel)
            .addComponent(this._nameInput)
            .addComponent(projectLabel)
            .addComponent(this._projects)
            .addGroup
            (
                layout.createSequentialGroup()
                .addGroup
                (
                    layout.createParallelGroup()
                    .addComponent(startTimeLabel)
                    .addComponent(endTimeLabel)
                )
                .addGroup
                (
                    layout.createParallelGroup()
                    .addComponent(this._startTimeSpinner)
                    .addComponent(this._endTimeSpinner)
                )
            )
            .addComponent(descriptionLabel)
            .addComponent(scroll)
            .addComponent(separator)
            .addGroup
            (
                layout.createSequentialGroup()
                .addGap(0, 90, Short.MAX_VALUE)
                .addComponent(cancelButton)
                .addComponent(saveButton)
            )
        );
        
        layout.setVerticalGroup
        (
            layout.createSequentialGroup()
            .addComponent(nameLabel, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
            .addComponent(this._nameInput, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
            .addComponent(projectLabel, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
            .addComponent(this._projects, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
            .addGroup
            (
                layout.createParallelGroup()
                .addGroup
                (
                    layout.createSequentialGroup()
                    .addComponent(startTimeLabel, 27, 27, 27)
                    .addComponent(endTimeLabel, 27, 27, 27)
                )
                .addGroup
                (
                    layout.createSequentialGroup()
                    .addComponent(this._startTimeSpinner, 27, 27, 27)
                    .addComponent(this._endTimeSpinner, 27, 27, 27)
                )
            )
            .addComponent(descriptionLabel, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
            .addComponent(scroll, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
            .addComponent(separator, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
            .addGroup
            (
                layout.createParallelGroup()
                .addComponent(cancelButton)
                .addComponent(saveButton)
            )
        );
        
        this._modified = false;
    }
    
    /**
     * Metoda zamykająca okno oraz wyświetlająca ostrzeżenie o dokonanych zmianach
     */
    public void close()
    {
        if(this._modified)
        {
            if(Messages.NO_OPTION == Messages.questionYesNo("Wprowadzono zmiany", "Wprowadzono zmiany w zadaniu, na pewno chcesz zamknąć okno bez zapisu?"))
            {
                return;
            }
        }
        
        this._parent.dispose();
    }
    
    /**
     * Metoda zapisująca zadanie i zamykająca okno
     */
    private void _save()
    {
        TtDate startDate = new TtDate((Date)this._startTimeSpinner.getModel().getValue());
        TtDate endDate = new TtDate((Date)this._endTimeSpinner.getModel().getValue());
        
        if(startDate.getTimestamp() > endDate.getTimestamp())
        {
            Messages.error("Błąd", "Data zakończenia zadania nie może być mniejsza niż data jego rozpoczęcia");
            return;
        }
        
        this._task.setName(this._nameInput.getText());
        
        TtComboBoxItem selectedItem = (TtComboBoxItem)this._projects.getSelectedItem();
        this._task.setProjectId(selectedItem.getId());
        
        this._task.setStartDate(startDate);
        this._task.setEndDate(endDate);
        
        this._task.setDescription(this._descriptionInput.getText());
        
        if(this._task.save() > 0)
        {
            TtObservable o = TtObservable.getInstance(TtObservable.TASK_CHANGED);
            o.setChanged();
            o.notifyObservers(null);
        
            this._modified = false;
            this.close();
        }
        else
        {
            Messages.error("Błąd", "Nie udało się zapisać zmian");
        }
    }
    
    /**
     * Metoda ustawiająca flagę zmian w zadaniu na true po zmianie projektu
     * @param e 
     */
    @Override
    public void actionPerformed(ActionEvent e) 
    {
        this._modified = true;
    }
    
    /**
     * Metoda ustawiająca flagę zmian w zadaniu na true po zmianie daty rozpoczęcia lub zakończenia
     * @param e 
     */
    @Override
    public void stateChanged(ChangeEvent e) 
    {
        this._modified = true;
    }
    
    /**
     * Metoda ustawiająca flagę zmian w zadaniu na true po zmianie nazwy lub opisu dodatkowego
     * @param e 
     */
    @Override
    public void insertUpdate(DocumentEvent e) 
    {
        this._modified = true;
    }
    
    /**
     * Metoda ustawiająca flagę zmian w zadaniu na true po zmianie nazwy lub opisu dodatkowego
     * @param e 
     */
    @Override
    public void removeUpdate(DocumentEvent e) 
    {
        this._modified = true;
    }
    
    /**
     * Metoda wymagana dla DocumentListener
     * @param e 
     */
    @Override
    public void changedUpdate(DocumentEvent e) {}
}
