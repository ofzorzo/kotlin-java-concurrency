
package pdp;


public class SumThread extends Thread{
    
    private long[] Arr;
    private final int n;
    private final int id;
    private final int nivel;
    private final int p;
    
    SumThread(long Arr[], int n, int nivel, int p, int id){
        this.Arr = Arr;
        this.n = n;
        this.id = id;
        this.nivel = nivel;
        this.p = p;
    }
    @Override
    public void run(){
        int sums = n / (int) Math.pow(2, 1 + nivel);
            
        int sumsToRun = Math.max(sums / p, 1);
        for (int i = 0; i < sumsToRun; i++){
            int index = this.id*sumsToRun + i;
            Arr[index] = Arr[2*index] + Arr[2*index] + 1; 
        }
    }
    
    
    
}
