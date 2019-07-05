
package philosophers;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Philosopher implements Runnable{
    
    private Object leftForkLock;
    private Object rightForkLock;
    private final int SLEEP_MULTIPLIER = 1000;
    public static final String outFileName = "eaters_report.txt";
    
    public Philosopher(Object leftLock, Object rightLock){
        this.leftForkLock = leftLock;
        this.rightForkLock = rightLock;
    }
    
    private void think() throws InterruptedException{
        System.out.println(Thread.currentThread().getName() + " thinking ... ");     
        Thread.sleep((long) (Math.random() * this.SLEEP_MULTIPLIER));
    }
    
    private void pickFork(String fork) throws InterruptedException{
        System.out.println(Thread.currentThread().getName() + " picked " + fork + " fork");
        Thread.sleep((long) (Math.random() * this.SLEEP_MULTIPLIER));
    }
    
    private void putForkDown(String fork) throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + " putted " + fork + " fork down");
        Thread.sleep((long) (Math.random() * this.SLEEP_MULTIPLIER));
    }
    
    private void eat() throws InterruptedException, IOException{
        System.out.println(Thread.currentThread().getName() + " eating ... ");
        BufferedWriter writer = new BufferedWriter(new FileWriter(this.outFileName, true));
        writer.write(Thread.currentThread().getName().replace("Philosopher ", ""));
        writer.newLine();
        writer.close();
        Thread.sleep((long) (SLEEP_MULTIPLIER * Math.random()));
    }
    
    @Override
    public void run() {
       while (true) {
           try {
               //thinking
               
               this.think();
               synchronized (this.leftForkLock){
                   this.pickFork("left");
                   synchronized(this.rightForkLock){
                       this.pickFork("right");
                       this.eat();
                       this.putForkDown("right");
                   }
                   this.putForkDown("left");
               }
           } catch (InterruptedException ex) {
              Thread.currentThread().interrupt();
              return;
           } catch (IOException ex) {
              Logger.getLogger(Philosopher.class.getName()).log(Level.SEVERE, null, ex);
           }
       }
    }

    

    
}
