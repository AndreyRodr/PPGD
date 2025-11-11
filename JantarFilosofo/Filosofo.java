
import java.util.Random;

public class Filosofo implements Runnable {

    private final Random random = new Random();
    private final String nome;

    private Filosofo proximo;

    public Filosofo(String nome) {
        this.nome = nome;
    }

    public void setProximoFilosofo(Filosofo next) {
        this.proximo = next;
    }

    @Override
    public void run() {

        for (int i = 0; i < 10; i++) {
            this.jantar(i + 1);

            synchronized(this.proximo) {
                this.proximo.notify();
            }

            this.esperar();
        }
    }


    public void jantar(int turn) {

        final long tempoComendo = this.random.nextLong(1000, 5000);
        final long tempoInicial = System.currentTimeMillis();

        System.out.println(
            "Filosofo " + this.nome + " está jantando pela " + 
            String.valueOf(turn) + "a vez e por " + 
            String.valueOf(tempoComendo / 1000.) + " segundos");

        while (System.currentTimeMillis() - tempoInicial < tempoComendo);

        System.out.println(
            "Filosofo " + this.nome + 
            " terminou de jantar!");
    }

    public void esperar() {
        final long tempoEspera = this.random.nextLong(5000, 10000);

        System.out.println(
                " *** Filosofo " + this.nome + 
                " está esperando por " + String.valueOf(tempoEspera / 1000.));

        try {

            synchronized(this) {
                this.wait(25000);
            }

        } catch(InterruptedException e) {

        }
    }
}
