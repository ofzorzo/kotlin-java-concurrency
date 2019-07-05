
package philosophers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainPhilosophers {

    
    public static final int PHILOSOPHERS = 5;
    
    
    public static void cleanFile() throws IOException{
        BufferedWriter writer = new BufferedWriter(new FileWriter(Philosopher.outFileName)); 
        writer.write("");
        writer.close();
        
    }
    
    public static void main(String[] args) {
        Philosopher[] philosophers = new Philosopher[PHILOSOPHERS];
        Object[] forks = new Object[PHILOSOPHERS]; // the number of forsks is the same of philosophers
        try {
            cleanFile();
        } catch (IOException ex) {
            Logger.getLogger(MainPhilosophers.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for (int i = 0; i < forks.length; i++){
            forks[i] = new Object(); // initialize the objects that will be used as locks
        }
        for (int i = 0; i < PHILOSOPHERS; i++) {
            Object leftForkLock = forks[i];
            Object rightForkLock = forks[(i + 1) % forks.length];
            
            /*OBS: 
            A solução encontrada para resolver o problema do deadlock 
            foi fazer com que um dos filósofos procure primeiro o garfo da 
            direita enquanto os outros procurem primeiro o da esquerda, isso
            evita qualquer deadlock pois esse filósofo que pega o garfo da 
            direita primeiro não vai bloquear o garfo do filósofo a seua
            esquerda se o garfo da direita não estiver disponível, logo o 
            filósofo a sua esquerda pode comer e liberar seus garfos.
            */
            if (i == 0){
                philosophers[i] = new Philosopher(leftForkLock, rightForkLock);
            }
            else{
                philosophers[i] = new Philosopher(rightForkLock, leftForkLock);
            }             
            Thread t = new Thread(philosophers[i], "Philosopher " + (i + 1));
            t.start();
        }
        
    }
    
}
