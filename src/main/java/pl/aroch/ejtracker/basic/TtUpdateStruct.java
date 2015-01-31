
package pl.aroch.ejtracker.basic;

import java.util.ArrayList;

/**
 * Struktura z informacjami o najnowszej wersji programu
 * @author Arkadiusz Roch
 */
public class TtUpdateStruct 
{
    /**
     * Numer aktualnej wersji jako String
     */
    public String currentVersion;
    
    /**
     * Numer najnowszej wersji jako String
     */
    public String newVersion;
    
    /**
     * Data wydania najnowszej wersji
     */
    public String releaseDate;
    
    /**
     * Informacje o wydaniu
     */
    public String releaseInfo;
    
    /**
     * Adres do pobrania programu
     */
    public String downloadAddress;
    
    /**
     * Lista aktywnych kanałów aktualizacji
     */
    public ArrayList<String> activeChanels;
    
    /**
     * Lista przestarzałych kanałów aktualizacji
     */
    public ArrayList<String> deprecatedChanels;
    
    /**
     * Lista usuniętych kanałów aktualizacji
     */
    public ArrayList<String> removedChanels;
}
