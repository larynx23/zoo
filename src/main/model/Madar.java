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

    @Override
    protected int getAdagMeret() {
        return Math.max(1, (int) Math.ceil(szarnyfesztav / 0.5));
    }

    @Override
    public String getReszletek() {
        return "Szárnyfesztáv: " + szarnyfesztav + " m";
    }

    public double getSzarnyfesztav() {
        return szarnyfesztav;
    }

    public void setSzarnyfesztav(double szarnyfesztav) {
        this.szarnyfesztav = szarnyfesztav;
    }
}
