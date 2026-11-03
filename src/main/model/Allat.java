package model;

import enums.Allapot;
import enums.EledelTipus;

import java.io.Serializable;
import java.time.LocalDateTime;

public abstract class Allat implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nev;
    private int kor;
    private double suly;
    private LocalDateTime utolsoEtetesIdopont;
    private Allapot allapot;

    protected Allat(String nev, int kor, double suly) {
        this.nev = nev;
        this.kor = kor;
        this.suly = suly;
        this.utolsoEtetesIdopont = LocalDateTime.now();
        this.allapot = Allapot.BOLDOG;
    }

    /**
     * Az aktualis ido es az utolso etetes kozotti idokulonbseg alapjan
     * frissiti az allapotot: BOLDOG -> EHES -> MEGHALT.
     */
    public void frissitAllapot() {
        // TODO: idoalapu allapotfrissites implementacioja
        throw new UnsupportedOperationException("Nincs implementalva");
    }

    /**
     * Visszaadja az allat altal igenyelt eledeltipust.
     */
    public abstract EledelTipus getSzuksegesEledel();

    /**
     * Megeteti az allatot a raktarbol, ha van eleg eledel.
     *
     * @return true, ha sikeres volt az etetes
     */
    public boolean etetes(Raktar raktar) {
        // TODO: etetes implementacioja (raktar ellenorzes, csokkentes,
        //       allapot es idobelyeg frissitese)
        throw new UnsupportedOperationException("Nincs implementalva");
    }

    public String getNev() {
        return nev;
    }

    public void setNev(String nev) {
        this.nev = nev;
    }

    public int getKor() {
        return kor;
    }

    public void setKor(int kor) {
        this.kor = kor;
    }

    public double getSuly() {
        return suly;
    }

    public void setSuly(double suly) {
        this.suly = suly;
    }

    public LocalDateTime getUtolsoEtetesIdopont() {
        return utolsoEtetesIdopont;
    }

    public void setUtolsoEtetesIdopont(LocalDateTime utolsoEtetesIdopont) {
        this.utolsoEtetesIdopont = utolsoEtetesIdopont;
    }

    public Allapot getAllapot() {
        return allapot;
    }

    public void setAllapot(Allapot allapot) {
        this.allapot = allapot;
    }
}
