
package pl.aroch.ejtracker.panels.projects;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.table.TableColumnModel;
import pl.aroch.ejtracker.Messages;
import pl.aroch.ejtracker.basic.TtCellValue;
import pl.aroch.ejtracker.basic.TtDefaultTableCellRenderer;
import pl.aroch.ejtracker.basic.TtDefaultTableModel;
import pl.aroch.ejtracker.basic.TtObservable;
import pl.aroch.ejtracker.basic.TtObserver;
import pl.aroch.ejtracker.basic.TtPanel;
import pl.aroch.ejtracker.basic.TtTable;
import pl.aroch.ejtracker.basic.TtTime;
import pl.aroch.ejtracker.dialogs.projects.ProjectEdit;
import pl.aroch.ejtracker.model.Projects;
import pl.aroch.ejtracker.model.projects.Project;

/**
 * Panel okna listy projektów
 * @author Arkadiusz Roch
 */
public class ProjectsListPanel extends TtPanel implements TtObserver 
{
    /**
     * Tabela z listą projektów
     */
    private TtTable _table;
    
    /**
     * Model tabeli
     */
    private TtDefaultTableModel _tableModel;
    
    /**
     * Metoda inicjująca elementu panelu
     */
    @Override
    public void init()
    {
        this._table = new TtTable(); 

        this._table.setAutoResizeMode(this._table.AUTO_RESIZE_LAST_COLUMN);
        this._fillTable();
        
        JScrollPane scroll = new JScrollPane();
        scroll.setViewportView(this._table);
        JLabel title = new JLabel("Lista projektów");
        
        title.setFont(new Font("Arial", Font.PLAIN, 20));
        
        JLabel desc = new JLabel("Tutaj znajduje się lista projektów");
        
        JButton add = new JButton("Nowy");
        add.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                _add();                
            }
        });
        JButton edit = new JButton("Edytuj");
        edit.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                _editProjectFromButton();
            }
            
        });
        JButton remove = new JButton("Usuń");
        remove.addActionListener(new ActionListener()
        {   
            @Override
            public void actionPerformed(ActionEvent e)
            {
                _remove();
            }
        });
        
        JSeparator topSeparator = new JSeparator();
        topSeparator.setForeground(new Color(180, 180, 180));
        
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        
        layout.setHorizontalGroup
        (   
            layout.createParallelGroup()
            .addComponent(scroll)
            .addComponent(desc)
            .addComponent(topSeparator)
            .addGroup
            (
                layout.createSequentialGroup()
                .addComponent(title)
                .addContainerGap(0, Short.MAX_VALUE)
                .addComponent(add)
                .addComponent(edit)
                .addComponent(remove)
            )
        );
        
        layout.setVerticalGroup
        (
            layout.createSequentialGroup()
            .addGroup
            (
                layout.createParallelGroup()
                .addComponent(title)
                .addComponent(add)
                .addComponent(edit)
                .addComponent(remove)
            )
            .addComponent(topSeparator)
            .addComponent(desc)
            .addComponent(scroll)
        );
        
        TtObservable.getInstance(TtObservable.PROJECT_CHANGED).addObserver(this);
        TtObservable.getInstance(TtObservable.PROJECT_ADDED).addObserver(this);
        TtObservable.getInstance(TtObservable.PROJECT_REMOVED).addObserver(this);
   }
    
    /**
     * Uzupełnienie tabeli z projektami
     */
    private void _fillTable()
    {
        Projects model = new Projects();
        
        Integer i = 1;
        this._tableModel = new TtDefaultTableModel();
        
        this._tableModel.addColumn("Lp.");
        this._tableModel.addColumn("Krótka nazwa");
        this._tableModel.addColumn("Opis");
        this._tableModel.addColumn("Czas");
        
        for(Project project : model.getProjectsList())
        {
            TtTime time = project.getTime();
            StringBuilder timeText = new StringBuilder();
            
            if(time.getHours() > 0)
            {
                timeText.append(String.format("%02d:", time.getHours()));
            }

            timeText.append(String.format("%02d:%02d", time.getMinutes(), time.getSeconds()));
            
            TtCellValue[] row = new TtCellValue[4];
            row[0] = new TtCellValue();
            row[1] = new TtCellValue();
            row[2] = new TtCellValue();
            row[3] = new TtCellValue();
            
            row[0].setValue(i.toString());
            row[1].setValue(project.getShortName());
            row[1].setToolTip(project.getName());
            row[2].setValue(project.getDescription());
            row[2].setToolTip(project.getDescription());
            row[3].setValue(timeText.toString());
            
            this._tableModel.setRowId(i - 1, project.getId());
            this._tableModel.addRow(row);
            
            i++;
        }
        
        this._table.setModel(this._tableModel);
        this._table.addMouseListener(new MouseAdapter() 
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
                _editProjectFromClick(e);
            }
        });
        
        TtDefaultTableCellRenderer rightCellRenderer = new TtDefaultTableCellRenderer();
        TtDefaultTableCellRenderer cellRenderer = new TtDefaultTableCellRenderer();

        rightCellRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        
        TableColumnModel columnModel = this._table.getColumnModel();
        columnModel.setColumnSelectionAllowed(false);
        
        columnModel.getColumn(0).setMaxWidth(30);
        columnModel.getColumn(0).setPreferredWidth(30);
        columnModel.getColumn(0).setCellRenderer(cellRenderer);
        
        columnModel.getColumn(1).setMaxWidth(100);
        columnModel.getColumn(1).setPreferredWidth(100);
        columnModel.getColumn(1).setCellRenderer(cellRenderer);
        
        columnModel.getColumn(2).setCellRenderer(cellRenderer);
        
        columnModel.getColumn(3).setMaxWidth(70);
        columnModel.getColumn(3).setPreferredWidth(70);
        columnModel.getColumn(3).setCellRenderer(rightCellRenderer);
    }
    
    /**
     * Metoda wywoływana przez menadżera obserwatorów w przypadku gdy został dodany
     * lub zmodyfikowany jakiś projekt.
     * @param args
     * @param actionName 
     */
    @Override
    public void update(Object args, String actionName)
    {
        this._fillTable();
    }
    
    /**
     * Otwarcie okna dodawania nowego projektu
     */
    private void _add()
    {
        ProjectEdit editDialog = new ProjectEdit();
        editDialog.init();
        editDialog.setVisible(true);
    }
    
    /**
     * Otwarcie okna edycji projektu
     * @param id Identyfikator projektu
     */
    private void _edit(Integer id)
    {
        ProjectEdit editDialog = new ProjectEdit(id);
        editDialog.init();
        editDialog.setVisible(true);
    }
    
    /**
     * Otwarcie okna edycji projektu
     */
    public void _editProjectFromButton()
    {
        int[] rows = this._table.getSelectedRows();
        
        if(rows.length == 0)
        {
            Messages.warning("Błąd", "Nie wybrano nic do edycji");
        }
        else if(rows.length > 1)
        {
            Messages.warning("Błąd", "Można wybrać tylko jeden projekt do edycji");
        }
        else
        {
            this._edit(this._tableModel.getRowId(rows[0]));
        }
    }
    
    /**
     * Otwarcie okna edycji po podwójnym kliknięciu w projekt
     * @param e Obiekt zdarzenia MouseEvent
     */
    private void _editProjectFromClick(MouseEvent e)
    {
        if(e.getClickCount() == 2)
        {
            Integer row = this._table.rowAtPoint(e.getPoint());
            TtDefaultTableModel model = this._table.getTtTableModel();

            this._edit(model.getRowId(row));
        }
    }
    
    /**
     * Usunięcie wybranych projektów
     */
    private void _remove()
    {
        int[] rows = this._table.getSelectedRows();
        
        if(rows.length < 1)
        {
            Messages.warning("Błąd", "Nie wybrano nic do usunięcia");
        }
        else
        {
            Projects model = new Projects();
            
            ArrayList<Integer> ids = new ArrayList<>();
            int size = rows.length;
            
            for(int i = 0; i < size; i++)
            {
                Integer id = this._tableModel.getRowId(rows[i]);
                if(model.getTasksCount(id) == 0)
                {
                    ids.add(id);
                }
            }
            
            if(ids.size() > 0)
            {
                model.deleteProjects(ids);
            }
        }
        
        TtObservable observable = TtObservable.getInstance(TtObservable.PROJECT_REMOVED);
        observable.setChanged();
        observable.notifyObservers(null);
    }
}
