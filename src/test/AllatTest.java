import enums.Allapot;
import enums.EledelTipus;
import model.Allat;
import model.Emlos;
import model.Hullo;
import model.Madar;
import model.Raktar;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


class AllatTest {

    @Test
    void getSzuksegesEledelTipusonkent() {
        assertEquals(EledelTipus.HUS, new Emlos("E", 1, 10, "rovid").getSzuksegesEledel());
        assertEquals(EledelTipus.MAG, new Madar("M", 1, 1, 0.5).getSzuksegesEledel());
        assertEquals(EledelTipus.ROVAR, new Hullo("H", 1, 1, false).getSzuksegesEledel());
    }

    @Test
    void getReszletekTartalmazzaAzEgyediMezot() {
        assertTrue(new Emlos("E", 1, 10, "bundas").getReszletek().contains("bundas"));
        assertTrue(new Madar("M", 1, 1, 1.5).getReszletek().contains("1.5"));
        assertTrue(new Hullo("H", 1, 1, true).getReszletek().toLowerCase().contains("igen"));
        assertTrue(new Hullo("H", 1, 1, false).getReszletek().toLowerCase().contains("nem"));
    }

    @Test
    void frissitAllapotBoldogFrissEtetesUtan() {
        Emlos e = new Emlos("E", 1, 10, "rovid");
        e.setUtolsoEtetesIdopont(LocalDateTime.now());
        e.frissitAllapot();
        assertEquals(Allapot.BOLDOG, e.getAllapot());
    }

    @Test
    void frissitAllapotEhesABoldogIdotartamUtan() {
        Emlos e = new Emlos("E", 1, 10, "rovid");
        // a boldog idoszak mar lejart, de a halal meg nem
        e.setUtolsoEtetesIdopont(LocalDateTime.now().minus(Allat.BOLDOG_IDOTARTAM).minusMinutes(1));
        e.frissitAllapot();
        assertEquals(Allapot.EHES, e.getAllapot());
    }

    @Test
    void frissitAllapotMeghaltAzEhesIdotartamUtan() {
        Emlos e = new Emlos("E", 1, 10, "rovid");
        e.setUtolsoEtetesIdopont(LocalDateTime.now()
                .minus(Allat.BOLDOG_IDOTARTAM)
                .minus(Allat.EHES_IDOTARTAM)
                .minusMinutes(1));
        e.frissitAllapot();
        assertEquals(Allapot.MEGHALT, e.getAllapot());
    }

    @Test
    void meghaltAllatNemElesztodikUjra() {
        Emlos e = new Emlos("E", 1, 10, "rovid");
        e.setAllapot(Allapot.MEGHALT);
        e.setUtolsoEtetesIdopont(LocalDateTime.now());
        e.frissitAllapot();
        assertEquals(Allapot.MEGHALT, e.getAllapot());
    }

    @Test
    void etetesSikeresEsCsokkentiARaktarat() {
        Raktar raktar = new Raktar();
        raktar.feltoltKeszlet(EledelTipus.HUS, 10);

        Emlos e = new Emlos("E", 1, 10, "rovid"); // adag = ceil(10/10) = 1
        e.setAllapot(Allapot.EHES);

        assertTrue(e.etetes(raktar));
        assertEquals(Allapot.BOLDOG, e.getAllapot());
        assertEquals(9, raktar.getKeszlet(EledelTipus.HUS));
    }

    @Test
    void etetesSikertelenHaNincsElegEledel() {
        Raktar raktar = new Raktar(); // ures
        Madar m = new Madar("M", 1, 1, 0.5);
        assertFalse(m.etetes(raktar));
    }

    @Test
    void etetesSikertelenNullRaktarral() {
        Emlos e = new Emlos("E", 1, 10, "rovid");
        assertFalse(e.etetes(null));
    }

    @Test
    void etetesSikertelenHalottAllatra() {
        Raktar raktar = new Raktar();
        raktar.feltoltKeszlet(EledelTipus.HUS, 10);
        Emlos e = new Emlos("E", 1, 10, "rovid");
        e.setAllapot(Allapot.MEGHALT);
        assertFalse(e.etetes(raktar));
        // a keszlet nem valtozott
        assertEquals(10, raktar.getKeszlet(EledelTipus.HUS));
    }

    @Test
    void adagmeretTipusonkentElter() {
        Raktar raktar = new Raktar();
        raktar.feltoltKeszlet(EledelTipus.HUS, 100);
        raktar.feltoltKeszlet(EledelTipus.MAG, 100);
        raktar.feltoltKeszlet(EledelTipus.ROVAR, 100);

        // Emlos: ceil(25/10) = 3 adag hus
        new Emlos("nagy", 1, 25, "rovid").etetes(raktar);
        assertEquals(97, raktar.getKeszlet(EledelTipus.HUS));

        // Madar: ceil(1.2/0.5) = 3 adag mag
        new Madar("nagyszarnyu", 1, 1, 1.2).etetes(raktar);
        assertEquals(97, raktar.getKeszlet(EledelTipus.MAG));

        // Hullo: mergezo -> 1 adag, nem mergezo -> 2 adag
        new Hullo("mergezo", 1, 1, true).etetes(raktar);
        new Hullo("szelid", 1, 1, false).etetes(raktar);
        assertEquals(97, raktar.getKeszlet(EledelTipus.ROVAR));
    }

    @Test
    void emlosSulyaNoEtetesUtan() {
        Raktar raktar = new Raktar();
        raktar.feltoltKeszlet(EledelTipus.HUS, 10);
        Emlos e = new Emlos("E", 1, 10.0, "rovid");
        double elotte = e.getSuly();
        e.etetes(raktar);
        assertTrue(e.getSuly() > elotte);
    }
}
