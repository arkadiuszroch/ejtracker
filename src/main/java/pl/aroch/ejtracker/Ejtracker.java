
package pl.aroch.ejtracker;

/**
 * Główna klasa programu
 * @author Arkadiusz Roch
 */
public class Ejtracker 
{
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        
        final Application app = new Application();
        
        app.install();
        
        app.run();
        
        app.setVisible(true);
        
        Thread updates = new Thread()
        {
            @Override
            public void run()
            {
                app.checkUpdates();
            };
        };
        
        updates.start();
    }
}
