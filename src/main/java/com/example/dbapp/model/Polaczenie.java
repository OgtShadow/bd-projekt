package com.example.dbapp.model;

public class Polaczenie {
    private int idAutor;
    private int idUtwor;
    private String autorNazwisko;
    private String autorImie;
    private String utworTytul;

    public Polaczenie(int idAutor, int idUtwor, String autorImie, String autorNazwisko, String utworTytul) {
        this.idAutor = idAutor;
        this.idUtwor = idUtwor;
        this.autorImie = autorImie;
        this.autorNazwisko = autorNazwisko;
        this.utworTytul = utworTytul;
    }

    public int getIdAutor() { return idAutor; }
    public int getIdUtwor() { return idUtwor; }
    public String getAutorImie() { return autorImie; }
    public String getAutorNazwisko() { return autorNazwisko; }
    public String getUtworTytul() { return utworTytul; }

    public String getAutorPelneNazwisko() {
        return autorImie + " " + autorNazwisko;
    }
}
