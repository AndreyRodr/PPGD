import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de teste para verificar a configuração inicial de recursos
 * do problema do Jantar dos Filósofos.
 * * Pressupõe que as classes Filosofo, Garfo e Jantar existem.
 * Assume Jantar.iniciar() configura a mesa (5 filósofos, 5 garfos).
 */
public class JantarConfiguracaoTest {

    private Jantar jantar;
    private final int NUM_ENTIDADES = 5; // Número padrão de filósofos/garfos

    /**
     * Configura o ambiente de teste: inicializa o Jantar e configura os artefatos.
     */
    @BeforeEach
    void setUp() {
        // Assume que o construtor de Jantar recebe o número de entidades, ou usa 5 como padrão.
        jantar = new Jantar(NUM_ENTIDADES); 
        jantar.iniciar(); // Executa o método que configura Garfos e Filósofos
    }

    // --- Testes de Cardinalidade (Contagem) ---

    @Test
    void deveTerONumeroCorretoDeFilosofos() {
        List<Filosofo> filosofos = jantar.getFilosofos();
        assertNotNull(filosofos, "A lista de Filósofos não deve ser nula.");
        assertEquals(NUM_ENTIDADES, filosofos.size(), 
            "Deve haver " + NUM_ENTIDADES + " Filósofos na mesa.");
    }

    @Test
    void deveTerONumeroCorretoDeGarfos() {
        List<Garfo> garfos = jantar.getGarfos();
        assertNotNull(garfos, "A lista de Garfos não deve ser nula.");
        assertEquals(NUM_ENTIDADES, garfos.size(), 
            "Deve haver " + NUM_ENTIDADES + " Garfos na mesa.");
    }
    
    // --- Testes de Estrutura (Associação e Compartilhamento) ---

    /**
     * Verifica se cada Filósofo tem exatamente dois Garfos, e se estes Garfos
     * são os esperados (i.e., Garfo da Esquerda e Garfo da Direita).
     */
    @Test
    void cadaFilosofoDeveTerDoisGarfosCorretamenteAssociados() {
        List<Filosofo> filosofos = jantar.getFilosofos();
        List<Garfo> garfos = jantar.getGarfos();

        for (int i = 0; i < NUM_ENTIDADES; i++) {
            Filosofo filosofo = filosofos.get(i);
            
            // Garfo da Esquerda é o Garfo[i]
            Garfo garfoEsquerdaEsperado = garfos.get(i);
            // Garfo da Direita é o Garfo[(i + 1) % NUM_ENTIDADES] (circular)
            Garfo garfoDireitaEsperado = garfos.get((i + 1) % NUM_ENTIDADES);

            assertNotNull(filosofo.getGarfoEsquerda(), 
                "Filósofo " + i + " deve ter um Garfo Esquerdo.");
            assertNotNull(filosofo.getGarfoDireita(), 
                "Filósofo " + i + " deve ter um Garfo Direito.");

            assertSame(garfoEsquerdaEsperado, filosofo.getGarfoEsquerda(),
                "Filósofo " + i + " não está associado corretamente ao Garfo Esquerdo " + i + ".");
            assertSame(garfoDireitaEsperado, filosofo.getGarfoDireita(),
                "Filósofo " + i + " não está associado corretamente ao Garfo Direito " + ((i + 1) % NUM_ENTIDADES) + ".");

            // Opcional: Verificação de que os garfos são distintos
            assertNotSame(filosofo.getGarfoEsquerda(), filosofo.getGarfoDireita(),
                "Filósofo " + i + " não deve ter o mesmo Garfo para ambas as mãos.");
        }
    }

    /**
     * Verifica a propriedade de compartilhamento: cada Garfo deve ser referenciado
     * exatamente duas vezes pelos Filósofos (uma vez como Garfo da Esquerda,
     * uma vez como Garfo da Direita para o filósofo adjacente).
     */
    @Test
    void cadaGarfoDeveSerCompartilhadoPorDoisFilosofos() {
        List<Filosofo> filosofos = jantar.getFilosofos();
        List<Garfo> garfos = jantar.getGarfos();

        for (Garfo garfo : garfos) {
            int contagemReferencias = 0;
            
            for (Filosofo filosofo : filosofos) {
                if (filosofo.getGarfoEsquerda() == garfo || filosofo.getGarfoDireita() == garfo) {
                    contagemReferencias++;
                }
            }
            
            assertEquals(2, contagemReferencias, 
                "Garfo " + garfos.indexOf(garfo) + " deve ser referenciado por exatamente 2 Filósofos (compartilhamento).");
        }
    }
}

// Nota: Para que este código compile, você deve garantir que as classes 
// Filosofo e Jantar tenham os seguintes métodos:
// class Jantar {
//     public Jantar(int numEntidades) { ... }
//     public void iniciar() { ... }
//     public List<Filosofo> getFilosofos() { ... }
//     public List<Garfo> getGarfos() { ... }
// }
// class Filosofo {
//     public Garfo getGarfoEsquerda() { ... }
//     public Garfo getGarfoDireita() { ... }
// }
// class Garfo {
//     // Apenas a referência de objeto é usada aqui, sem métodos adicionais necessários.
// }
