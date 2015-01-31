
package pl.aroch.ejtracker.panels;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle;
import pl.aroch.ejtracker.basic.TtPanel;
import pl.aroch.ejtracker.dialogs.ReleaseInfo;

/**
 * Panel okna z informacjami o nowym wydaniu programu
 * @author Arkadiusz Roch
 */
public class ReleaseInfoPanel extends TtPanel
{
    /**
     * Klasa okna nadrzędnego dla panelu
     */
    private ReleaseInfo _parent;
    
    /**
     * Tekst z informacjami
     */
    private String _text;
    
    /**
     * Kontruktor przyjmujący jako parametry tekst z informają oraz 
     * obiekt nadrzędny dla panelu
     * @param text Informacje o nowym wydaniu
     * @param parent Obiekt nadrzędny
     */
    public ReleaseInfoPanel(String text, ReleaseInfo parent)
    {
        this._text = text;
        this._parent = parent;
    }
    
    /**
     * Metoda inicjująca elementy panelu
     */
    @Override
    public void init() 
    {
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        
        JLabel title = new JLabel("Informacje o nowej wersji");
        title.setFont(new Font("Arial", Font.PLAIN, 18));
        
        JSeparator topSeparator = new JSeparator();
        JSeparator bottomSeparator = new JSeparator();
        
        JTextArea textField = new JTextArea(this._text);
        textField.setEditable(false);
        textField.setLineWrap(true);
        textField.setWrapStyleWord(true);
        
        JScrollPane scroll = new JScrollPane(textField);
        
        JButton closeButton = new JButton("Zamknij");
        closeButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                _close();
            }
        });
        
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        
        layout.setHorizontalGroup
        (
            layout.createParallelGroup()
            .addComponent(title)
            .addComponent(topSeparator)
            .addComponent(scroll)
            .addComponent(bottomSeparator)
            .addGroup
            (
                layout.createSequentialGroup()
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(closeButton)
            )
        );
        
        layout.setVerticalGroup
        (
            layout.createSequentialGroup()
            .addComponent(title)
            .addComponent(topSeparator, 2, 2, 2)
            .addComponent(scroll)
            .addComponent(bottomSeparator, 2, 2, 2)
            .addComponent(closeButton)
        );
    }
    
    /**
     * Metoda zamykająca okno
     */
    private void _close()
    {
        this._parent.dispose();
    }
}
