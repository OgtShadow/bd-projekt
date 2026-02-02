package com.example.dbapp.model;

public class Utwor {
    private int idUtwor;
    private String tytul;
    private int rokWydania;
    private int dlugoscSekundy;
    private String gatunek;

    public Utwor(int idUtwor, String tytul, int rokWydania, int dlugoscSekundy, String gatunek) {
        this.idUtwor = idUtwor;
        this.tytul = tytul;
        this.rokWydania = rokWydania;
        this.dlugoscSekundy = dlugoscSekundy;
        this.gatunek = gatunek;
    }

    public Utwor(String tytul, int rokWydania, int dlugoscSekundy, String gatunek) {
        this.tytul = tytul;
        this.rokWydania = rokWydania;
        this.dlugoscSekundy = dlugoscSekundy;
        this.gatunek = gatunek;
    }

    public int getIdUtwor() { return idUtwor; }
    public void setIdUtwor(int idUtwor) { this.idUtwor = idUtwor; }

    public String getTytul() { return tytul; }
    public void setTytul(String tytul) { this.tytul = tytul; }

    public int getRokWydania() { return rokWydania; }
    public void setRokWydania(int rokWydania) { this.rokWydania = rokWydania; }

    public int getDlugoscSekundy() { return dlugoscSekundy; }
    public void setDlugoscSekundy(int dlugoscSekundy) { this.dlugoscSekundy = dlugoscSekundy; }

    public String getGatunek() { return gatunek; }
    public void setGatunek(String gatunek) { this.gatunek = gatunek; }

     @Override
    public String toString() {
        return tytul + " (" + rokWydania + ")";
    }
}