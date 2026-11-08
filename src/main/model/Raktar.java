package model;

import enums.EledelTipus;

import java.io.Serializable;
import java.util.EnumMap;
import java.util.Map;

public class Raktar implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Map<EledelTipus, Integer> keszlet = new EnumMap<>(EledelTipus.class);

    public Raktar() {
        for (EledelTipus tipus : EledelTipus.values()) {
            keszlet.put(tipus, 0);
        }
    }

    public boolean vanElegEledel(EledelTipus tipus, int mennyiseg) {
        if (tipus == null || mennyiseg <= 0) {
            return false;
        }
        return getKeszlet(tipus) >= mennyiseg;
    }

    public void csokkentKeszlet(EledelTipus tipus, int mennyiseg) {
        if (tipus == null) {
            throw new IllegalArgumentException("A tipus nem lehet null");
        }
        if (mennyiseg < 0) {
            throw new IllegalArgumentException("A mennyiseg nem lehet negativ");
        }
        int aktualis = getKeszlet(tipus);
        if (aktualis < mennyiseg) {
            throw new IllegalStateException(
                    "Nincs eleg eledel (" + tipus + "): " + aktualis + " < " + mennyiseg);
        }
        keszlet.put(tipus, aktualis - mennyiseg);
    }

    public void feltoltKeszlet(EledelTipus tipus, int mennyiseg) {
        if (tipus == null) {
            throw new IllegalArgumentException("A tipus nem lehet null");
        }
        if (mennyiseg < 0) {
            throw new IllegalArgumentException("A mennyiseg nem lehet negativ");
        }
        keszlet.put(tipus, getKeszlet(tipus) + mennyiseg);
    }

    public int getKeszlet(EledelTipus tipus) {
        return keszlet.get(tipus);
    }
}
