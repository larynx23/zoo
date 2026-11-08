package model;

import enums.Allapot;
import enums.EledelTipus;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;

public abstract class Allat implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final Duration BOLDOG_IDOTARTAM = Duration.ofHours(12);
    public static final Duration EHES_IDOTARTAM = Duration.ofHours(12);

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

    /*
        Az aktualis ido es az utolso etetes kozotti idokulonbseg alapjan
        frissiti az allapotot: BOLDOG -> EHES -> MEGHALT.
    */
    public void frissitAllapot() {
        if (allapot == Allapot.MEGHALT) {
            return;
        }

        Duration eltelt = Duration.between(utolsoEtetesIdopont, LocalDateTime.now());

        if (eltelt.compareTo(BOLDOG_IDOTARTAM) < 0) {
            allapot = Allapot.BOLDOG;
        } else if (eltelt.compareTo(BOLDOG_IDOTARTAM.plus(EHES_IDOTARTAM)) < 0) {
            allapot = Allapot.EHES;
        } else {
            allapot = Allapot.MEGHALT;
        }
    }


    public abstract EledelTipus getSzuksegesEledel();

    public abstract String getReszletek();

    public final boolean etetes(Raktar raktar) {
        if (raktar == null || allapot == Allapot.MEGHALT) {
            return false;
        }

        EledelTipus szukseges = getSzuksegesEledel();
        int adag = getAdagMeret();
        if (!raktar.vanElegEledel(szukseges, adag)) {
            return false;
        }

        raktar.csokkentKeszlet(szukseges, adag);
        utolsoEtetesIdopont = LocalDateTime.now();
        allapot = Allapot.BOLDOG;
        etetesHatasa();
        return true;
    }

    protected abstract int getAdagMeret();

    protected void etetesHatasa() {};

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
