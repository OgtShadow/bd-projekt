-- Tabela AUTOR
CREATE TABLE autor (
    id_autor NUMBER PRIMARY KEY,
    imie VARCHAR2(50) NOT NULL,
    nazwisko VARCHAR2(50) NOT NULL,
    pseudonim VARCHAR2(50),
    kraj VARCHAR2(50)
);

CREATE SEQUENCE seq_autor START WITH 1 INCREMENT BY 1;

CREATE OR REPLACE TRIGGER trg_autor
BEFORE INSERT ON autor
FOR EACH ROW
BEGIN
  :NEW.id_autor := seq_autor.NEXTVAL;
END;
/

-- Tabela UTWOR
CREATE TABLE utwor (
    id_utwor NUMBER PRIMARY KEY,
    tytul VARCHAR2(100) NOT NULL,
    rok_wydania NUMBER(4),
    dlugosc_sekundy NUMBER,
    gatunek VARCHAR2(50)
);

CREATE SEQUENCE seq_utwor START WITH 1 INCREMENT BY 1;

CREATE OR REPLACE TRIGGER trg_utwor
BEFORE INSERT ON utwor
FOR EACH ROW
BEGIN
  :NEW.id_utwor := seq_utwor.NEXTVAL;
END;
/

-- Tabela łącząca (Wiele do Wielu)
CREATE TABLE autor_utwor (
    id_autor NUMBER,
    id_utwor NUMBER,
    CONSTRAINT pk_autor_utwor PRIMARY KEY (id_autor, id_utwor),
    CONSTRAINT fk_autor FOREIGN KEY (id_autor) REFERENCES autor(id_autor),
    CONSTRAINT fk_utwor FOREIGN KEY (id_utwor) REFERENCES utwor(id_utwor)
);

-- DANE TESTOWE
INSERT INTO autor (imie, nazwisko, pseudonim, kraj) VALUES ('Jan', 'Kowalski', 'JK', 'Polska');
INSERT INTO autor (imie, nazwisko, pseudonim, kraj) VALUES ('Anna', 'Nowak', NULL, 'Polska');
INSERT INTO autor (imie, nazwisko, pseudonim, kraj) VALUES ('Adam', 'Wiśniewski', 'AW', 'Polska');
INSERT INTO autor (imie, nazwisko, pseudonim, kraj) VALUES ('John', 'Smith', 'DJ Smith', 'USA');
INSERT INTO autor (imie, nazwisko, pseudonim, kraj) VALUES ('Maria', 'Garcia', 'MG', 'Hiszpania');

INSERT INTO utwor (tytul, rok_wydania, dlugosc_sekundy, gatunek) VALUES ('Letni Wiatr', 2020, 210, 'Pop');
INSERT INTO utwor (tytul, rok_wydania, dlugosc_sekundy, gatunek) VALUES ('Ciemna Noc', 2019, 185, 'Rock');
INSERT INTO utwor (tytul, rok_wydania, dlugosc_sekundy, gatunek) VALUES ('Droga do Domu', 2021, 240, 'Pop');
INSERT INTO utwor (tytul, rok_wydania, dlugosc_sekundy, gatunek) VALUES ('Electric Pulse', 2018, 300, 'Electronic');
INSERT INTO utwor (tytul, rok_wydania, dlugosc_sekundy, gatunek) VALUES ('Sunrise', 2022, 195, 'Electronic');
INSERT INTO utwor (tytul, rok_wydania, dlugosc_sekundy, gatunek) VALUES ('Desert Road', 2017, 260, 'Rock');
INSERT INTO utwor (tytul, rok_wydania, dlugosc_sekundy, gatunek) VALUES ('Nocne Miasto', 2023, 220, 'Hip-Hop');
INSERT INTO utwor (tytul, rok_wydania, dlugosc_sekundy, gatunek) VALUES ('Fuego', 2021, 205, 'Latin');
INSERT INTO utwor (tytul, rok_wydania, dlugosc_sekundy, gatunek) VALUES ('Echoes', 2016, 275, 'Ambient');
INSERT INTO utwor (tytul, rok_wydania, dlugosc_sekundy, gatunek) VALUES ('Zimny Poranek', 2020, 230, 'Indie');

-- POWIĄZANIA
INSERT INTO autor_utwor (id_autor, id_utwor) VALUES (1, 1);
INSERT INTO autor_utwor (id_autor, id_utwor) VALUES (1, 3);
INSERT INTO autor_utwor (id_autor, id_utwor) VALUES (3, 2);
INSERT INTO autor_utwor (id_autor, id_utwor) VALUES (3, 6);
INSERT INTO autor_utwor (id_autor, id_utwor) VALUES (4, 4);
INSERT INTO autor_utwor (id_autor, id_utwor) VALUES (4, 5);
INSERT INTO autor_utwor (id_autor, id_utwor) VALUES (2, 7);
INSERT INTO autor_utwor (id_autor, id_utwor) VALUES (2, 10);
INSERT INTO autor_utwor (id_autor, id_utwor) VALUES (5, 8);
INSERT INTO autor_utwor (id_autor, id_utwor) VALUES (5, 9);
