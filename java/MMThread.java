/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pdp;
import java.util.*;

/**
 *
 * @author wwvargas
 */
public class MMThread extends Thread{

    private final long[][] M1;
    private final long[][] M2;
    
    private long[][] ANS;
    
    private final int id; // goes from 0 to the p-1 where p is the number of threads
    private final int n;
    private final int p;
    public MMThread(long[][] M1, long[][] M2, long [][] ANS, int n, int p, int id){
        this.M1 = M1;
        this.M2 = M2;
        this.ANS = ANS;
        this.id = id;
        this.n = n;
        this.p = p;
    }
    
    @Override
    public void run(){
        
        //System.out.print("Started thread ");
        //System.out.println(this.id);
        int lines = this.n / this.p;
        int begin = this.id * lines;
        
        
        for (int i = begin; i<begin+lines; i++ ){
            for (int j = 0; j < this.n; j++){
                long sum = 0;
                for (int k = 0; k < this.n; k++){
                    sum+= this.M1[i][k] * this.M2[k][j];
                }
                ANS[i][j] = sum;
            }
        }
    }

    
    
}
