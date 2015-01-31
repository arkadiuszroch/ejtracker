
package pl.aroch.ejtracker.panels;

import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.LayoutStyle;
import pl.aroch.ejtracker.Messages;
import pl.aroch.ejtracker.basic.TtPanel;
import pl.aroch.ejtracker.basic.TtUpdateStruct;
import pl.aroch.ejtracker.dialogs.ReleaseInfo;
import pl.aroch.ejtracker.dialogs.UpdateInfo;


/**
 * Panel okna z informacjami o aktualizacji
 * @author Arkadiusz Roch
 */
public class UpdateInfoPanel extends TtPanel
{
    /**
     * Struktra z informacjami o najnowszej wersji programu
     */
    private TtUpdateStruct _params;
    
    /**
     * Klasa okna nadrzędna dla panelu
     */
    private UpdateInfo _parent;
    
    /**
     * Konstruktor przyjmujący jako parametry informacje o najnowszej wersji programu
     * oraz obiekt nadrzędny dla panelu
     * @param params Informacje o najnowszej wersji programu
     * @param parent Obiekt nadrzędny
     */
    public UpdateInfoPanel(TtUpdateStruct params, UpdateInfo parent)
    {
        this._params = params;
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
        
        JSeparator topSeparator = new JSeparator();
        JSeparator bottomSeparator = new JSeparator();
        
        JLabel title = new JLabel("Aktualizacje automatyczne");
        title.setFont(new Font("Arial", Font.PLAIN, 18));
        
        Font boldFont = new Font("Arial", Font.BOLD, 14);
        
        JLabel desc = new JLabel("Dostępna jest nowsza wersja programu");
        
        JLabel currentVersionLabel = new JLabel("Obecna wersja:");
        
        JLabel newVersionLabel = new JLabel();
        newVersionLabel.setFont(boldFont);
        newVersionLabel.setText("Najnowsza wersja:");
        
        JLabel releaseDateLabel = new JLabel("Data publikacji:");
        
        JLabel currentVersion = new JLabel(this._params.currentVersion);
        
        JLabel newVersion = new JLabel(this._params.newVersion);
        newVersion.setFont(boldFont);
        
        JLabel releaseDate = new JLabel(this._params.releaseDate);
        
        JButton downloadButton = new JButton("Pobierz najnowszą wersję");
        JLabel downloadLabel = new JLabel("Pobierz z adresu:");
        JLabel downloadAddress = new JLabel(this._params.downloadAddress);
        
        if(Desktop.isDesktopSupported())
        {
            downloadButton.addActionListener(new ActionListener() 
            {
                @Override
                public void actionPerformed(ActionEvent e) 
                {
                    _download();
                }
            });
            
            downloadLabel.setVisible(false);
            downloadAddress.setVisible(false);
        }
        else
        {
            downloadButton.setVisible(false);
        }
        
        JButton changeLogButton = new JButton("Co nowego...");
        if(this._params.releaseInfo.length() > 0)
        {
            changeLogButton.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e) 
                {
                    _openReleaseInfo();
                }
            });
        }
        else
        {
            changeLogButton.setVisible(false);
        }
        
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
            .addComponent(desc)
            .addGroup
            (
                layout.createSequentialGroup()
                .addGroup
                (
                    layout.createParallelGroup()
                    .addComponent(currentVersionLabel)
                    .addComponent(newVersionLabel)
                    .addComponent(releaseDateLabel)
                    .addComponent(downloadLabel)
                )
                .addGap(18, 18, 18)
                .addGroup
                (
                    layout.createParallelGroup()
                    .addComponent(currentVersion)
                    .addComponent(newVersion)
                    .addComponent(releaseDate)
                    .addComponent(downloadAddress)
                )
            )
            .addComponent(downloadButton, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
            .addGroup
            (
                layout.createParallelGroup()
            )
            .addComponent(bottomSeparator)
            .addGroup
            (
                layout.createSequentialGroup()
                .addComponent(changeLogButton)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(closeButton)
            )
        );
        
        layout.setVerticalGroup
        (
            layout.createSequentialGroup()
            .addComponent(title)
            .addComponent(topSeparator, 2, 2, 2)
            .addComponent(desc)
            .addGap(18,18,18)
            .addGroup
            (
                layout.createParallelGroup()
                .addComponent(currentVersionLabel)
                .addComponent(currentVersion)
            )
            .addGap(12,12,12)
            .addGroup
            (
                layout.createParallelGroup()
                .addComponent(newVersionLabel)
                .addComponent(newVersion)
            )
            .addGap(12,12,12)
            .addGroup
            (
                layout.createParallelGroup()
                .addComponent(releaseDateLabel)
                .addComponent(releaseDate)
            )
            .addGap(12,12,12)
            .addComponent(downloadButton)
            .addGroup
            (
                layout.createParallelGroup()
                .addComponent(downloadLabel)
                .addComponent(downloadAddress)
            )
            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(bottomSeparator, 2, 2, 2)
            .addGroup
            (
                layout.createParallelGroup()
                .addComponent(changeLogButton)
                .addComponent(closeButton)
            )
        );
    }
    
    /**
     * Metoda zamykająca okno
     */
    private void _close()
    {
        this._parent.dispose();
    }
    
    /**
     * Otwarcie w domyślnej przeglądarce adresu, 
     * pod którym znajduje się najnowsza wersja programu
     */
    private void _download()
    {
        if(Desktop.isDesktopSupported())
        {
            try
            {
                Desktop.getDesktop().browse(new URI(this._params.downloadAddress));
            }
            catch(IOException e)
            {
                Messages.exceptionHandler("Wyjątek", "Błąd podczas otwierania adresu", e);
            }
            catch(URISyntaxException e)
            {
                Messages.exceptionHandler("Wyjątek", "Błąd podczas otwierania adresu", e);
            }
        }
    }
    
    /**
     * Otwarcie okna, z informacjami o najnowszej wersji programu
     */
    private void _openReleaseInfo()
    {
        ReleaseInfo dialog = new ReleaseInfo(this._params.releaseInfo);
        dialog.init();
        dialog.setVisible(true);
    }
}
