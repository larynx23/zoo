package model;

import enums.EledelTipus;

public class Emlos extends Allat {

    private static final long serialVersionUID = 1L;

    private String szorTipus;

    public Emlos(String nev, int kor, double suly, String szorTipus) {
        super(nev, kor, suly);
        this.szorTipus = szorTipus;
    }

    @Override
    public EledelTipus getSzuksegesEledel() {
        return EledelTipus.HUS;
    }

    public String getSzorTipus() {
        return szorTipus;
    }

    public void setSzorTipus(String szorTipus) {
        this.szorTipus = szorTipus;
    }
}
