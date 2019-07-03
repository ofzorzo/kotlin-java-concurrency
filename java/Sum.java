
package pdp;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

// Para funcionar assume que n é uma potencia de 2 e é divisível por p

public class Sum {
    
    static long RANGE = 10;
    static boolean PRINT = false;
    public static void main(String[] args){
        int n =  67108864; // 2^26
        int p = 2;
        
        long[] A = randArr(n);
        
        if (PRINT){
            System.out.println("Adding array");
            printArray(A, n);
        }        
        long beg = System.currentTimeMillis();
        
        int logN = (int) Math.round(Math.log(n) / Math.log(2));
        for (int nivel = 0; nivel < logN; nivel++){
            int threadsToRun = Math.min(p, n / (int) Math.pow(2, 1 + nivel));
            SumThread[] threads = new SumThread[threadsToRun];
            for (int i = 0; i <threadsToRun; i++){
                threads[i] = new SumThread(A, n, nivel, p, i);
            }
            for (int i = 0; i<threadsToRun; i++){
                threads[i].start();
            }
            for (int i = 0; i < threadsToRun; i++){
                try {
                    threads[i].join();
                } catch (InterruptedException ex) {
                    Logger.getLogger(Sum.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        long end = System.currentTimeMillis();
        
        if(PRINT){
            System.out.print("Sum = ");
            System.out.println(A[0]);
        }
        
        System.out.print("Done in ");
        System.out.print(end - beg);
        System.out.println("ms");
        
    }
    
    public static long[] randArr(int n){
        long[] A = new long[n];
        Random r = new Random();
        for (int i = 0; i < n; i++){
            A[i] = ((r.nextLong() %  RANGE) + RANGE) % RANGE + 1; // values from 1 to RANGE
        }
        return A;
    }
    
    public static void printArray(long[] Arr, int n){
        for (int i = 0; i < n; i++){
            System.out.print(Arr[i]);
            System.out.print(" ");
        }
        System.out.println("");
    }
}
