import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String args[]) {
        
        final int NUM_FILOSOFOS = 5;
        List<Thread> threads = new ArrayList<>();
        
        Object[] hashis = new Object[NUM_FILOSOFOS];
        for (int i = 0; i < NUM_FILOSOFOS; i++) {
            hashis[i] = new Object();
        }

        Filosofo[] filosofos = new Filosofo[NUM_FILOSOFOS];
        for (int i = 0; i < NUM_FILOSOFOS; i++) {
            
            Object hashiEsquerdo = hashis[i];
            Object hashiDireito = hashis[(i + 1) % NUM_FILOSOFOS]; 

            filosofos[i] = new Filosofo("F" + (i + 1), i, hashiEsquerdo, hashiDireito);
            
            threads.add(new Thread(filosofos[i]));
        }

        System.out.println("O jantar dos filósofos vai começar!");

        for (Thread t : threads)
            t.start();

        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("O jantar terminou!");
    }
}
