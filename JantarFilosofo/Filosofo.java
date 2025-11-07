import java.util.Random;

public class Filosofo implements Runnable {

    private final Random random = new Random();
    private final String nome;
    private final int id;
    private final Object hashiEsquerdo;
    private final Object hashiDireito;

    public Filosofo(String nome, int id, Object hashiEsquerdo, Object hashiDireito) {
        this.nome = nome;
        this.id = id;
        this.hashiEsquerdo = hashiEsquerdo;
        this.hashiDireito = hashiDireito;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 10; i++) {
                this.pensar();
                
                this.pegarHashisEComer(i + 1);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Filosofo " + this.nome + " foi interrompido.");
        }
    }

    private void pegarHashisEComer(int turn) throws InterruptedException {
        System.out.println("Filosofo " + this.nome + " está tentando pegar os hashis.");

        // SOLUÇÃO DE DEADLOCK:
        // O último filósofo (id == 4) pega o hashi da DIREITA primeiro.
        // Todos os outros (id 0-3) pegam o da ESQUERDA primeiro.
        
        if (this.id == 4) {
            synchronized (hashiDireito) {
                System.out.println("Filosofo " + this.nome + " pegou o hashi DIREITO (id=" + this.id + ")");
                Thread.sleep(100);
                
                synchronized (hashiEsquerdo) {
                    System.out.println("Filosofo " + this.nome + " pegou o hashi ESQUERDO (id=" + this.id + ")");
                    this.jantar(turn); 
                }
                System.out.println("Filosofo " + this.nome + " largou o hashi ESQUERDO.");
            }
            System.out.println("Filosofo " + this.nome + " largou o hashi DIREITO.");
        } else {
            synchronized (hashiEsquerdo) {
                System.out.println("Filosofo " + this.nome + " pegou o hashi ESQUERDO (id=" + this.id + ")");
                Thread.sleep(100); 
                
                synchronized (hashiDireito) {
                    System.out.println("Filosofo " + this.nome + " pegou o hashi DIREITO (id=" + this.id + ")");
                    this.jantar(turn); 
                }
                System.out.println("Filosofo " + this.nome + " largou o hashi DIREITO.");
            }
            System.out.println("Filosofo " + this.nome + " largou o hashi ESQUERDO.");
        }
    }

    public void jantar(int turn) throws InterruptedException {
        final long tempoComendo = this.random.nextLong(1000, 3000); 

        System.out.println(
                "--- Filosofo " + this.nome + " está jantando pela " +
                        String.valueOf(turn) + "a vez por " +
                        String.valueOf(tempoComendo / 1000.) + " segundos ---");

        Thread.sleep(tempoComendo);

        System.out.println(
                "--- Filosofo " + this.nome +
                        " terminou de jantar! ---");
    }

    public void pensar() throws InterruptedException {
        final long tempoPensando = this.random.nextLong(1000, 5000); 

        System.out.println(
                " *** Filosofo " + this.nome +
                        " está pensando por " + String.valueOf(tempoPensando / 1000.) + "s ***");
        
        Thread.sleep(tempoPensando);
    }
}
