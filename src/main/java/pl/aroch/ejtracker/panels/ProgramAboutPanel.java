
package pl.aroch.ejtracker.panels;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import pl.aroch.ejtracker.Application;

/**
 * Okno wyświetlające informacje o programie
 * @author Arkadiusz Roch
 */
public class ProgramAboutPanel extends JPanel 
{
    /**
     * Klasa okna nadrzędnego dla panelu
     */
    private JDialog _parent;
    
    /**
     * Konstruktor przyjmujący jako parametr obiekt nadrzędny dla panelu
     * @param parent Obiekt nadrzędny
     */
    public ProgramAboutPanel(JDialog parent)
    {
        this._parent = parent;
    }
    
    /**
     * Metoda inicjująca elementy okna
     */
    public void init()
    {
        Font bigFont = new Font("Arial", Font.PLAIN, 18);
        
        JLabel title = new JLabel("O EjTracker");
        title.setFont(bigFont);
        
        JLabel aboutDescription = new JLabel("<html><body>"
                + "EjTracker jest niewielkiem, darmowym programem do śledzenia czasu pracy. "
                + "Nie wymagana jest żadna instalacja, program doskonale nadaje się dla osób, "
                + "które chcą wiedzieć ile czasu i kiedy spędziły nad danym zadaniem lub projektem. "
                + "Na chwile obecną oprócz zarządzania zadaniami istnieje możliwość "
                + "grupowania ich według projektów."
                + "</body></html>");
        aboutDescription.setMaximumSize(new Dimension(470, 1000));
        
        JLabel versionLabel = new JLabel("Wersja programu: ");
        JLabel version = new JLabel(Application.getVersionAsString());
        
        JLabel projectSiteLabel = new JLabel("Strona projektu:");
        JLabel projectSite = new JLabel("www.ejtracker.aroch.pl");
        
        JLabel contactEmailLabel = new JLabel("Kontakt:");
        JLabel contactEmail = new JLabel("ejtracker@aroch.pl");
        
        JLabel donationTitle = new JLabel("Dotacja");
        donationTitle.setFont(bigFont);
        
        JLabel donationDescription = new JLabel("<html><body>"
                + "EjTracker jest jeszcze zbyt małym programem, aby można było prosić o wsparcie na jego rozwój. "
                + "Nie mniej jednak jeśli masz trochę wolnych środków na koncie będzie miło jeśli wspomożesz "
                + "jakąś organizację pożytku publicznego. Jeśli nie wiesz jaką fundację wspomóc "
                + "może znajdziesz coś tutaj: "
                + "http://www.fundacje.org, http://www.fundacje.pl, http://www.firmy.net/fundacje.html"
                + "</body></html>");
        donationDescription.setMaximumSize(new Dimension(470, 1000));
        
        JSeparator titleSeparator = new JSeparator();
        JSeparator versionSeparator = new JSeparator();
        JSeparator donationSeparator = new JSeparator();
        
        JButton closeButton = new JButton("Zamknij");
        closeButton.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                _close();
            }
        });
        
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        
        layout.setAutoCreateContainerGaps(true);
        layout.setAutoCreateGaps(true);
        
        layout.setHorizontalGroup
        (
            layout.createParallelGroup()
            .addComponent(title)
            .addComponent(aboutDescription)
            .addComponent(titleSeparator)
            .addGroup
            (
                layout.createSequentialGroup()
                .addGroup
                (
                    layout.createParallelGroup()
                    .addComponent(versionLabel)
                    .addComponent(projectSiteLabel)
                    .addComponent(contactEmailLabel)
                )
                .addGroup
                (
                    layout.createParallelGroup()
                    .addComponent(version)
                    .addComponent(projectSite)
                    .addComponent(contactEmail)
                )
            )
            .addComponent(versionSeparator)
            .addComponent(donationTitle)
            .addComponent(donationDescription)
            .addComponent(donationSeparator)
            .addGroup
            (
                layout.createSequentialGroup()
                .addContainerGap(0, Short.MAX_VALUE)
                .addComponent(closeButton)
            )
        );
        
        layout.setVerticalGroup
        (
            layout.createSequentialGroup()
            .addComponent(title, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
            .addComponent(aboutDescription, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
            .addComponent(titleSeparator, 2, 2, 2)
            .addGroup
            (
                layout.createParallelGroup()
                .addGroup
                (
                    layout.createSequentialGroup()
                    .addComponent(versionLabel)
                    .addComponent(projectSiteLabel)
                    .addComponent(contactEmailLabel)
                )
                .addGroup
                (
                    layout.createSequentialGroup()
                    .addComponent(version)
                    .addComponent(projectSite)
                    .addComponent(contactEmail)
                )
            )
            .addComponent(versionSeparator, 2, 2, 2)
            .addComponent(donationTitle, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
            .addComponent(donationDescription, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
            .addContainerGap(0, Short.MAX_VALUE)
            .addComponent(donationSeparator, 2, 2, 2)
            .addComponent(closeButton)
        );
    }
    
    /**
     * Zamknięcie okna
     */
    private void _close()
    {
        this._parent.dispose();
    }
}
