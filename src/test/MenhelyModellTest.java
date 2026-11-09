import enums.Allapot;
import enums.EledelTipus;
import model.Allat;
import model.Emlos;
import model.Hullo;
import model.Madar;
import model.MenhelyModell;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


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

    @Test
    void hozzaadNoveliAListat() {
        modell.hozzaad(new Emlos("E", 1, 10, "rovid"));
        assertEquals(1, modell.getAllatok().size());
    }

    @Test
    void hozzaadNullKivetel() {
        assertThrows(IllegalArgumentException.class, () -> modell.hozzaad(null));
    }

    @Test
    void torolEltavolitjaAzAllatot() {
        Emlos e = new Emlos("E", 1, 10, "rovid");
        modell.hozzaad(e);
        assertTrue(modell.torol(e));
        assertTrue(modell.getAllatok().isEmpty());
    }

    @Test
    void torolHamisHaNincsBenne() {
        assertFalse(modell.torol(new Emlos("nincs", 1, 10, "rovid")));
    }

    @Test
    void szurTipusSzerint() {
        modell.hozzaad(new Emlos("E", 1, 10, "rovid"));
        modell.hozzaad(new Madar("M", 1, 1, 0.5));
        modell.hozzaad(new Hullo("H", 1, 1, false));

        List<Allat> madarak = modell.szur(a -> a instanceof Madar);
        assertEquals(1, madarak.size());
        assertEquals("M", madarak.get(0).getNev());
    }

    @Test
    void szurNullFeltetelMindentVisszaad() {
        modell.hozzaad(new Emlos("E", 1, 10, "rovid"));
        modell.hozzaad(new Madar("M", 1, 1, 0.5));
        assertEquals(2, modell.szur(null).size());
    }

    @Test
    void frissitAllapotokMindenAllatraHat() {
        Emlos friss = new Emlos("friss", 1, 10, "rovid");
        friss.setUtolsoEtetesIdopont(LocalDateTime.now());

        Emlos ehes = new Emlos("ehes", 1, 10, "rovid");
        ehes.setUtolsoEtetesIdopont(
                LocalDateTime.now().minus(Allat.BOLDOG_IDOTARTAM).minusMinutes(1));

        modell.hozzaad(friss);
        modell.hozzaad(ehes);
        modell.frissitAllapotok();

        assertEquals(Allapot.BOLDOG, friss.getAllapot());
        assertEquals(Allapot.EHES, ehes.getAllapot());
    }

    @Test
    void mentesBetoltesKorut(@TempDir Path tempDir) throws Exception {
        modell.hozzaad(new Emlos("Simba", 5, 180.0, "rovid"));
        modell.hozzaad(new Madar("Rio", 2, 0.9, 1.2));
        modell.getRaktar().feltoltKeszlet(EledelTipus.HUS, 42);

        String utvonal = tempDir.resolve("allatok.dat").toString();
        modell.mentes(utvonal);

        MenhelyModell ujModell = new MenhelyModell();
        ujModell.betoltes(utvonal);

        assertEquals(2, ujModell.getAllatok().size());
        assertEquals("Simba", ujModell.getAllatok().get(0).getNev());
        assertEquals(42, ujModell.getRaktar().getKeszlet(EledelTipus.HUS));
    }

    @Test
    void betoltesFrissitiAMeglevoReferenciat(@TempDir Path tempDir) throws Exception {
        modell.hozzaad(new Hullo("Kaa", 7, 12.0, true));
        String utvonal = tempDir.resolve("allatok.dat").toString();
        modell.mentes(utvonal);

        // ugyanabba a modellbe toltunk vissza; a kulso referencia ervenyben marad
        List<Allat> referencia = modell.getAllatok();
        modell.betoltes(utvonal);
        assertEquals(1, referencia.size());
        assertEquals("Kaa", referencia.get(0).getNev());
    }
}
