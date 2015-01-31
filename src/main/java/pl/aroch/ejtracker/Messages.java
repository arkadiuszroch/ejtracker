
package pl.aroch.ejtracker;

import java.awt.TextArea;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;

/**
 * Klasa obsługująca okna z komunikatami
 * @author Arkadiusz Roch
 */
public class Messages 
{
    /**
     * Numer dla opcji - Anuluj
     */
    public static final int CANCEL_OPTION = JOptionPane.CANCEL_OPTION;
    
    /**
     * Numer dla opcji - Nie
     */
    public static final int NO_OPTION = JOptionPane.NO_OPTION;
    
    /**
     * Numer dla opcji - Tak
     */
    public static final int YES_OPTION = JOptionPane.YES_OPTION;
    
    /**
     * Wyświetlenie komunikatu o błędzie
     * @param title Tytuł
     * @param message Treść wiadomości
     */
    public static void error(String title, String message)
    {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Wyswietlenie komunikatu z obsługą wyjątku
     * @param title Tytuł
     * @param message Treść komunikatu
     * @param e Wyjątek
     */
    public static void exceptionHandler(String title, String message, Exception e)
    {
        
        StringBuilder stackTrace = new StringBuilder();
        
        int i = e.getStackTrace().length;
        
        for(StackTraceElement element: e.getStackTrace())
        {
            stackTrace.append(i--);
            stackTrace.append(". ");
            stackTrace.append(element.toString());
            stackTrace.append("\n");
        }
        
        TextArea text = new TextArea(e.getMessage() + "\n\n" + stackTrace.toString());
        
        text.setEditable(false);
        text.setVisible(true);
        
        JSeparator separator = new JSeparator();
        
        Object[] fields = {message, separator, text};
        
        JOptionPane.showMessageDialog(null, fields, title, JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Wyświetlenie komunikatu z ostrzeżeniem
     * @param title Tytuł
     * @param message Treść wiadomości
     */
    public static void warning(String title, String message)
    {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.WARNING_MESSAGE);
    }
    
    /**
     * Wyświetlenie komunikatu z opcją wyboru - tak lub nie
     * @param title Tytuł
     * @param message Treść wiadomości
     * @return Numer wybranje opcji
     */
    public static int questionYesNo(String title, String message)
    {
        return JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
    }
    
    /**
     * Wyswietlenie komunikatu z opcją wyboru - tak, nie lub anuluj
     * @param title Tytuł
     * @param message Treść wiadomości
     * @return Numer wybranej opcji
     */
    public static int questionYesNoCancel(String title, String message)
    {
        return JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
    }
}
