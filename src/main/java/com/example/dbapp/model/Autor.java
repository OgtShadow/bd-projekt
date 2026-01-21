package com.example.dbapp.model;

public class Autor {
    private int idAutor;
    private String imie;
    private String nazwisko;
    private String pseudonim;
    private String kraj;

    public Autor(int idAutor, String imie, String nazwisko, String pseudonim, String kraj) {
        this.idAutor = idAutor;
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.pseudonim = pseudonim;
        this.kraj = kraj;
    }

    // Konstruktor bez ID (ID nadawane przez bazę)
    public Autor(String imie, String nazwisko, String pseudonim, String kraj) {
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.pseudonim = pseudonim;
        this.kraj = kraj;
    }

    public int getIdAutor() { return idAutor; }
    public void setIdAutor(int idAutor) { this.idAutor = idAutor; }

    public String getImie() { return imie; }
    public void setImie(String imie) { this.imie = imie; }

    public String getNazwisko() { return nazwisko; }
    public void setNazwisko(String nazwisko) { this.nazwisko = nazwisko; }

    public String getPseudonim() { return pseudonim; }
    public void setPseudonim(String pseudonim) { this.pseudonim = pseudonim; }

    public String getKraj() { return kraj; }
    public void setKraj(String kraj) { this.kraj = kraj; }

    @Override
    public String toString() {
        return imie + " " + nazwisko + (pseudonim != null ? " (" + pseudonim + ")" : "");
    }
}