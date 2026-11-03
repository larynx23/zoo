package model;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class MenhelyModell implements Serializable {

    private static final long serialVersionUID = 1L;

    private final List<Allat> allatok = new ArrayList<>();
    private Raktar raktar = new Raktar();

    public void hozzaad(Allat allat) {
        // TODO: implementacio
        throw new UnsupportedOperationException("Nincs implementalva");
    }

    public void torol(Allat allat) {
        // TODO: implementacio
        throw new UnsupportedOperationException("Nincs implementalva");
    }

    public List<Allat> szur(Predicate<Allat> feltetel) {
        // TODO: implementacio
        throw new UnsupportedOperationException("Nincs implementalva");
    }

    public void frissitAllapotok() {
        // TODO: implementacio
        throw new UnsupportedOperationException("Nincs implementalva");
    }

    public void mentes(String fajlNev) throws IOException {
        // TODO: ObjectOutputStream alapu szerializacio
        throw new UnsupportedOperationException("Nincs implementalva");
    }

    public void betoltes(String fajlNev) throws IOException, ClassNotFoundException {
        // TODO: ObjectInputStream alapu deszerializacio
        throw new UnsupportedOperationException("Nincs implementalva");
    }

    public List<Allat> getAllatok() {
        return allatok;
    }

    public Raktar getRaktar() {
        return raktar;
    }

    public void setRaktar(Raktar raktar) {
        this.raktar = raktar;
    }
}
