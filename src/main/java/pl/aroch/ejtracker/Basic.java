/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.aroch.ejtracker;

/**
 * Klasa z funkcjami pomocniczymi
 * @author Arkadiusz Roch
 */
public class Basic 
{
    /**
     * Metoda łącząca string 
     * @param list Tablica elementów
     * @param glue Ciąg znaków wstawiany między elementy
     * @return Wynik łączenia elementów
     */
    public static String Join(String[] list, String glue)
    {
        int size = list.length;
        StringBuilder result = new StringBuilder();
        for(int i = 0; i < size; i++)
        {
            if((size - 1) == i)
            {
                result.append(list[i]);
            }
            else
            {
                result.append(list[i]);
                result.append(glue);
            }
        }
        
        return result.toString();
    }
    
    /**
     * Metoda łącząca string 
     * @param list Tablica elementów
     * @param glue Ciąg znaków wstawiany między elementy
     * @return Wynik łączenia elementów
     */
    public static String Join(int[] list, String glue)
    {
        int size = list.length;
        StringBuilder result = new StringBuilder();
        for(int i = 0; i < size; i++)
        {
            if((size - 1) == i)
            {
                result.append(list[i]);
            }
            else
            {
                result.append(list[i]);
                result.append(glue);
            }
        }
        
        return result.toString();
    }
}
