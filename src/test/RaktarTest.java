import enums.EledelTipus;
import model.Raktar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RaktarTest {

    private Raktar raktar;

    @BeforeEach
    void setUp() {
        raktar = new Raktar();
    }

    @Test
    void ujRaktarUresKeszlettel() {
        for (EledelTipus tipus : EledelTipus.values()) {
            assertEquals(0, raktar.getKeszlet(tipus));
        }
    }

    @Test
    void feltoltKeszletNoveliAzErteket() {
        raktar.feltoltKeszlet(EledelTipus.HUS, 10);
        assertEquals(10, raktar.getKeszlet(EledelTipus.HUS));

        raktar.feltoltKeszlet(EledelTipus.HUS, 5);
        assertEquals(15, raktar.getKeszlet(EledelTipus.HUS));
    }

    @Test
    void csokkentKeszletCsokkentiAzErteket() {
        raktar.feltoltKeszlet(EledelTipus.MAG, 20);
        raktar.csokkentKeszlet(EledelTipus.MAG, 8);
        assertEquals(12, raktar.getKeszlet(EledelTipus.MAG));
    }

    @Test
    void vanElegEledelHatarertekek() {
        raktar.feltoltKeszlet(EledelTipus.ROVAR, 5);
        assertTrue(raktar.vanElegEledel(EledelTipus.ROVAR, 5));
        assertTrue(raktar.vanElegEledel(EledelTipus.ROVAR, 3));
        assertFalse(raktar.vanElegEledel(EledelTipus.ROVAR, 6));
    }

    @Test
    void vanElegEledelNemPozitivMennyisegreHamis() {
        raktar.feltoltKeszlet(EledelTipus.HUS, 5);
        assertFalse(raktar.vanElegEledel(EledelTipus.HUS, 0));
        assertFalse(raktar.vanElegEledel(EledelTipus.HUS, -2));
    }

    @Test
    void csokkentTobbetMintAmennyiVanKivetel() {
        raktar.feltoltKeszlet(EledelTipus.HUS, 3);
        assertThrows(IllegalStateException.class,
                () -> raktar.csokkentKeszlet(EledelTipus.HUS, 4));
        // a keszlet nem valtozott a sikertelen muvelet utan
        assertEquals(3, raktar.getKeszlet(EledelTipus.HUS));
    }

    @Test
    void negativMennyisegKivetel() {
        assertThrows(IllegalArgumentException.class,
                () -> raktar.feltoltKeszlet(EledelTipus.MAG, -1));
        assertThrows(IllegalArgumentException.class,
                () -> raktar.csokkentKeszlet(EledelTipus.MAG, -1));
    }
}
