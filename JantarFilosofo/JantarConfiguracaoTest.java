import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.util.List;

// Importe suas classes de produção aqui
// import suasClasses.Filosofo;
// import suasClasses.Garfo;
// import suasClasses.Jantar;

/**
 * Classe de teste para validar a configuração inicial da classe Jantar.
 * Foca em garantir que os Garfos e Filósofos foram instanciados e associados
 * corretamente após a execução do método iniciar().
 */
public class JantarConfiguracaoTest {

    private Jantar jantar;
    private static final int NUM_PARTICIPANTES = 5; // Caso clássico

    @BeforeEach
    void setup() {
        // Instancia o ambiente antes de cada teste
        jantar = new Jantar();
        jantar.iniciar(NUM_PARTICIPANTES);
    }

    /**
     * Auxiliar para acessar campos privados (Lists de Filósofos/Garfos) via Reflection.
     * @param object Instância da classe a ser inspecionada.
     * @param fieldName Nome do campo privado.
     * @return O valor do campo.
     * @throws Exception Em caso de falha de acesso (campo não existe ou não acessível).
     */
    @SuppressWarnings("unchecked")
    private <T> List<T> getPrivateList(Object object, String fieldName) throws Exception {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return (List<T>) field.get(object);
    }

    // --- TESTES DE ESTRUTURA E QUANTIDADE ---

    @Test
    void deveTerNumeroCorretoDeFilosofosAposIniciar() throws Exception {
        List<Filosofo> filosofos = getPrivateList(jantar, "filosofos"); // Assumindo campo 'filosofos' em Jantar
        assertNotNull(filosofos, "A lista de Filósofos não deve ser nula.");
        assertEquals(NUM_PARTICIPANTES, filosofos.size(),
            "O Jantar deve criar exatamente " + NUM_PARTICIPANTES + " Filósofos.");
    }

    @Test
    void deveTerNumeroCorretoDeGarfosAposIniciar() throws Exception {
        List<Garfo> garfos = getPrivateList(jantar, "garfos"); // Assumindo campo 'garfos' em Jantar
        assertNotNull(garfos, "A lista de Garfos não deve ser nula.");
        assertEquals(NUM_PARTICIPANTES, garfos.size(),
            "O Jantar deve criar exatamente " + NUM_PARTICIPANTES + " Garfos (um por filósofo).");
    }

    // --- TESTES DE ASSOCIAÇÃO (Pontas) ---

    @Test
    void cadaFilosofoDeveTerDoisGarfosAssociados() throws Exception {
        List<Filosofo> filosofos = getPrivateList(jantar, "filosofos"); // Assumindo campo 'filosofos' em Jantar

        for (int i = 0; i < NUM_PARTICIPANTES; i++) {
            Filosofo f = filosofos.get(i);

            // Assumindo que a classe Filosofo tem métodos getGarfoEsquerdo() e getGarfoDireito()
            Garfo garfoEsquerdo = f.getGarfoEsquerdo();
            Garfo garfoDireito = f.getGarfoDireito();

            assertNotNull(garfoEsquerdo, "Filósofo " + i + " deve ter Garfo Esquerdo.");
            assertNotNull(garfoDireito, "Filósofo " + i + " deve ter Garfo Direito.");
            assertNotEquals(garfoEsquerdo, garfoDireito, "Os dois garfos de um filósofo não devem ser o mesmo objeto.");
        }
    }

    @Test
    void deveGarantirQueGarfosSaoCompartilhadosCorretamente() throws Exception {
        List<Filosofo> filosofos = getPrivateList(jantar, "filosofos"); // Assumindo campo 'filosofos' em Jantar
        List<Garfo> garfos = getPrivateList(jantar, "garfos"); // Assumindo campo 'garfos' em Jantar

        // A lógica de associação correta é:
        // Filósofo 'i' usa Garfo 'i' (Esquerda) e Garfo '(i + 1) % N' (Direita)

        for (int i = 0; i < NUM_PARTICIPANTES; i++) {
            Filosofo f = filosofos.get(i);
            Garfo garfoEsquerdoEsperado = garfos.get(i);
            Garfo garfoDireitoEsperado = garfos.get((i + 1) % NUM_PARTICIPANTES); // Módulo N para fechar o círculo

            // 1. Verifica se o Filósofo 'i' pegou o Garfo 'i' (Esquerdo)
            assertEquals(garfoEsquerdoEsperado, f.getGarfoEsquerdo(),
                "Filósofo " + i + " deve ter o Garfo " + i + " como esquerdo.");

            // 2. Verifica se o Filósofo 'i' pegou o Garfo '(i+1) % N' (Direito)
            assertEquals(garfoDireitoEsperado, f.getGarfoDireito(),
                "Filósofo " + i + " deve ter o Garfo " + ((i + 1) % NUM_PARTICIPANTES) + " como direito.");
        }
    }
}
