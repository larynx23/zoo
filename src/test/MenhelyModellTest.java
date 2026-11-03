import model.MenhelyModell;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Skeleton tesztek a MenhelyModell-hez. A valodi asszertek
 * az uzleti logika implementalasa utan kerulnek be.
 */
class MenhelyModellTest {

    private MenhelyModell modell;

    @BeforeEach
    void setUp() {
        modell = new MenhelyModell();
    }

    @Test
    void ujModellNemNull() {
        assertNotNull(modell.getAllatok());
        assertNotNull(modell.getRaktar());
    }

    // TODO: hozzaad / torol teszt
    // TODO: szur teszt
    // TODO: etetes + raktar csokkenes teszt
    // TODO: mentes/betoltes kor-utteszt (temp fajllal)
}
