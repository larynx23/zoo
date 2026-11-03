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
        // TODO: implementacio
        throw new UnsupportedOperationException("Nincs implementalva");
    }

    public void csokkentKeszlet(EledelTipus tipus, int mennyiseg) {
        // TODO: implementacio
        throw new UnsupportedOperationException("Nincs implementalva");
    }

    public void feltoltKeszlet(EledelTipus tipus, int mennyiseg) {
        // TODO: implementacio
        throw new UnsupportedOperationException("Nincs implementalva");
    }

    public int getKeszlet(EledelTipus tipus) {
        return keszlet.getOrDefault(tipus, 0);
    }
}
