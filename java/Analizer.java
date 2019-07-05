/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package philosophers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author ACEW
 */
public class Analizer {
    
    public static void analizeReport() throws FileNotFoundException, IOException{
        BufferedReader br = new BufferedReader(new FileReader(Philosopher.outFileName));
        
        int[] timesEaten = new int[MainPhilosophers.PHILOSOPHERS];
        
        for (int i = 0; i <  timesEaten.length; i++){
            timesEaten[i] = 0;
        }
        
        String philosopherId;
        
        while ((philosopherId = br.readLine()) != null){
            timesEaten[Integer.parseInt(philosopherId) - 1] += 1;
        }
        
        System.out.println("Number of times each philosopher have eaten:");
        
        for (int i = 0; i <  timesEaten.length; i++){
            System.out.println(
                    "Philosopher " + Integer.toString(i + 1) + ": " + 
                            Integer.toString(timesEaten[i]));
        }
    }
    
}
