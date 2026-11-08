package model;

import enums.EledelTipus;

public class Hullo extends Allat {

    private static final long serialVersionUID = 1L;

    private boolean mergezo;

    public Hullo(String nev, int kor, double suly, boolean mergezo) {
        super(nev, kor, suly);
        this.mergezo = mergezo;
    }

    @Override
    public EledelTipus getSzuksegesEledel() {
        return EledelTipus.ROVAR;
    }

    @Override
    protected int getAdagMeret() {
        return mergezo ? 1 : 2;
    }

    public boolean isMergezo() {
        return mergezo;
    }

    public void setMergezo(boolean mergezo) {
        this.mergezo = mergezo;
    }
}
