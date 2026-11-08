package model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MenhelyModell implements Serializable {

    private static final long serialVersionUID = 1L;

    private final List<Allat> allatok = new ArrayList<>();
    private Raktar raktar = new Raktar();

    public void hozzaad(Allat allat) {
        if (allat == null) {
            throw new IllegalArgumentException("Az allat nem lehet null");
        }
        allatok.add(allat);
    }

    public boolean torol(Allat allat) {
        return allatok.remove(allat);
    }

    public List<Allat> szur(Predicate<Allat> feltetel) {
        if (feltetel == null) {
            return new ArrayList<>(allatok);
        }
        return allatok.stream()
                .filter(feltetel)
                .collect(Collectors.toList());
    }

    public void frissitAllapotok() {
        for (Allat allat : allatok) {
            allat.frissitAllapot();
        }
    }

    public void mentes(String fajlNev) throws IOException {
        Objects.requireNonNull(fajlNev, "A fajlnev nem lehet null");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fajlNev))) {
            oos.writeObject(new MentettAllapot(new ArrayList<>(allatok), raktar));
        }
    }

    public void betoltes(String fajlNev) throws IOException, ClassNotFoundException {
        Objects.requireNonNull(fajlNev, "A fajlnev nem lehet null");
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fajlNev))) {
            MentettAllapot allapot = (MentettAllapot) ois.readObject();
            allatok.clear();
            allatok.addAll(allapot.allatok());
            setRaktar(allapot.raktar());
        }
    }

    private record MentettAllapot(List<Allat> allatok, Raktar raktar) implements Serializable {
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
