package model;

import enums.EledelTipus;

public class Madar extends Allat {

    private static final long serialVersionUID = 1L;

    private double szarnyfesztav;

    public Madar(String nev, int kor, double suly, double szarnyfesztav) {
        super(nev, kor, suly);
        this.szarnyfesztav = szarnyfesztav;
    }

    @Override
    public EledelTipus getSzuksegesEledel() {
        return EledelTipus.MAG;
    }

    public double getSzarnyfesztav() {
        return szarnyfesztav;
    }

    public void setSzarnyfesztav(double szarnyfesztav) {
        this.szarnyfesztav = szarnyfesztav;
    }
}
