
package pl.aroch.ejtracker.panels.projects;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import pl.aroch.ejtracker.Messages;
import pl.aroch.ejtracker.basic.TtObservable;
import pl.aroch.ejtracker.basic.TtPanel;
import pl.aroch.ejtracker.model.Projects;
import pl.aroch.ejtracker.model.projects.Project;

/**
 * Panel okna edycji projektu
 * @author Arkadiusz Roch
 */
public class ProjectEditPanel extends TtPanel implements DocumentListener 
{
    /**
     * Input z opisem projektu
     */
    private JTextArea _descField;
    
    /**
     * Flaga określająca czy były wprowadzone jakieś zmiany
     */
    private boolean _edited;
 
    /**
     * Id projektu
     */
    private Integer _id;
    
    /**
     * Input z nazwą projektu
     */
    private JTextField _nameField;
    
    /**
     * Klasa okna nadrzędna dla panelu
     */
    private JDialog _parent;
    
    /**
     * Obiekt projektu
     */
    private Project _project;
    
    /**
     * Input z krótką nazwą projektu
     */
    private JTextField _shortNameField;
    
    /**
     * Tytuł panelu
     */
    private String _title;
    
    /**
     * Konstruktor przyjmujący jako parametry obiekt nadrzędny oraz identyfikator projektu
     * @param parent Obiekt nadrzędny
     * @param id Identyfikator projektu
     */
    public ProjectEditPanel(JDialog parent, Integer id)
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
        if(this._id == 0)
        {
            this._title = "Nowy projekt";
            this._project = new Project();
        }
        else
        {
            Projects model = new Projects();
            this._project = model.getProject(this._id);
            
            this._title = "Edycja projektu";
        }
        
        GroupLayout layout = new GroupLayout(this);
        
        this.setLayout(layout);
        
        JLabel title = new JLabel(this._title);
        title.setFont(new Font("Arial", Font.PLAIN, 20));
        
        JLabel nameLabel = new JLabel("Nazwa");
        JLabel shortNameLabel = new JLabel("Krótka nazwa");
        JLabel descLabel = new JLabel("Opis");
        
        this._nameField = new JTextField(this._project.getName());
        this._nameField.getDocument().addDocumentListener(this);
        
        this._shortNameField = new JTextField(this._project.getShortName());
        
        this._descField = new JTextArea(this._project.getDescription());
        this._descField.setLineWrap(true);
        this._descField.setWrapStyleWord(true);
        this._descField.getDocument().addDocumentListener(this);
        
        JScrollPane descScroll = new JScrollPane();
        descScroll.setViewportView(this._descField);
        
        JButton save = new JButton("Zapisz");
        save.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                _save();
            }
        });
        
        JButton cancel = new JButton("Anuluj");
        cancel.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                _cancel();
            }
        });
        
        JSeparator topSeparator = new JSeparator();
        JSeparator bottomSeparator = new JSeparator();
        
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        
        layout.setHorizontalGroup
        (
            layout.createParallelGroup()
            .addComponent(title)
            .addComponent(topSeparator)
            .addComponent(nameLabel)
            .addComponent(this._nameField)
            .addComponent(shortNameLabel)
            .addComponent(this._shortNameField)
            .addComponent(descLabel)
            .addComponent(descScroll)
            .addComponent(bottomSeparator)
            .addGroup
            (
                layout.createSequentialGroup()
                .addGap(0, 90, Short.MAX_VALUE)
                .addComponent(cancel)
                .addComponent(save)
            )
        );
        
        layout.setVerticalGroup
        (
            layout.createParallelGroup()
            .addGroup
            (
                layout.createSequentialGroup()
                .addComponent(title)
                .addComponent(topSeparator, GroupLayout.PREFERRED_SIZE, 10, GroupLayout.PREFERRED_SIZE)
                .addComponent(nameLabel)
                .addComponent(this._nameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addComponent(shortNameLabel)
                .addComponent(this._shortNameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addComponent(descLabel)
                .addComponent(descScroll, GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                .addComponent(bottomSeparator, GroupLayout.PREFERRED_SIZE, 10, GroupLayout.PREFERRED_SIZE)
                .addGroup
                (
                    layout.createParallelGroup()
                    .addComponent(cancel)
                    .addComponent(save)
                )
            )
        );
    }
    
    /**
     * Metoda zamykająca okno oraz wyświetlająca komunikat o dokonanych zmianach
     */
    public void close()
    {
        if(this._edited == true)
        {
            if(Messages.YES_OPTION == Messages.questionYesNo("Wprowadzono zmiany", "Wprowadzono zmiany, na pewno chcesz zamknąć?"))
            {
                this._parent.dispose();
            }
        }
        else
        {
            this._parent.dispose();
        }
    }
    
    /**
     * Metoda zamykająca okno bez zapisywania zmian w projekcie
     */
    private void _cancel()
    {
        this.close();
    }
    
    /**
     * Metoda zapisująca zmiany w projekcie
     */
    private void _save()
    {
        StringBuilder errors = new StringBuilder();
        if(this._nameField.getText().length() == 0)
        {
            errors.append("Proszę podać nazwę projektu\n");
        }
        if(this._shortNameField.getText().length() == 0)
        {
            errors.append("Proszę podać skróconą nazwę projektu");
        }
        
        if(errors.length() > 0)
        {
            Messages.warning("Bład", errors.toString());
        }
        else
        {
            
            this._project.setName(this._nameField.getText());
            this._project.setShortName(this._shortNameField.getText());
            this._project.setDescription(this._descField.getText());
            
            if(this._project.save() > 0)
            {
                TtObservable o = TtObservable.getInstance(TtObservable.PROJECT_CHANGED);
                o.setChanged();
                o.notifyObservers(null);
            }
            this._edited = false;
            this.close();
        }
    }
    
    /**
     * Metoda ustawiająca flagę zmian w projekcie na true
     * @param e 
     */
    @Override
    public void insertUpdate(DocumentEvent e) 
    {
        this._edited = true;
    }

    /**
     * Metoda ustawiająca flagę zmian w projekcie na true
     * @param e 
     */
    @Override
    public void removeUpdate(DocumentEvent e) 
    {
        this._edited = true;
    }

    /**
     * Metoda wymagana dla DocumentListener
     * @param e 
     */
    @Override
    public void changedUpdate(DocumentEvent e) {}
}
