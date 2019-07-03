/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pdp;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;




public class MM {
    final static int RANGE = 10;
    final static boolean PRINT = false;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int p = 8;
        int n = 1024;
        
        long[][] M1 = randMatrix(n);
        long[][] M2 = randMatrix(n);
        
        long[][] ANS = new long[n][n];
        
        if (PRINT){
            System.out.println("Multiplying the following matrixes: ");

            printMatrix(M1, n);

            System.out.println("With");
            printMatrix(M2, n);
        }        
        MMThread[] threads = new MMThread[p];
        
        long beg = System.currentTimeMillis();
        
        for(int i = 0; i < p; i++){
            threads[i] = new MMThread(M1, M2, ANS, n, p, i);
            threads[i].start();
        }
        
        for(int i = 0; i < p; i++){
            try {
                threads[i].join();
            } catch (InterruptedException ex) {
                Logger.getLogger(MM.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
        if (PRINT){
            System.out.println("Result:");
            printMatrix(ANS, n);
        }        
        long end = System.currentTimeMillis();
       
        System.out.print("Done in ");
        System.out.print(end - beg);
        System.out.println("ms");
        
    }
    
    public static long[][] randMatrix(int n){
        long[][] M= new long[n][n];
        Random r = new Random();
        for (int i = 0; i < n; i++){
            for (int j = 0; j < n; j++){
                M[i][j] = ((r.nextLong() % RANGE) + RANGE) % RANGE + 1; // valores entre 1 e RANGE
            }
        }
        return M;
    }
    
    public static void printMatrix(long[][] M, int n){
        for (int i = 0; i<n; i++){
            System.out.print('|');
            for(int j = 0; j < n; j++){
                
                System.out.print(M[i][j]);
                if (j < n-1){
                    System.out.print(' ');
                
                }
            }
            System.out.println('|');
        }
    }
    
}
