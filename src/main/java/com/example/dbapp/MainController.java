package com.example.dbapp;

import com.example.dbapp.dao.AutorDAO;
import com.example.dbapp.dao.PolaczenieDAO;
import com.example.dbapp.dao.UtworDAO;
import com.example.dbapp.model.Autor;
import com.example.dbapp.model.Polaczenie;
import com.example.dbapp.model.Utwor;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainController {

    // --- AUTORZY ---
    @FXML private TableView<Autor> autorTable;
    @FXML private TableColumn<Autor, Integer> colAutorId;
    @FXML private TableColumn<Autor, String> colAutorImie;
    @FXML private TableColumn<Autor, String> colAutorNazwisko;
    @FXML private TableColumn<Autor, String> colAutorPseudonim;
    @FXML private TableColumn<Autor, String> colAutorKraj;

    @FXML private TextField fieldImie;
    @FXML private TextField fieldNazwisko;
    @FXML private TextField searchAutorField;
    @FXML private TextField fieldPseudonim;
    @FXML private TextField fieldKraj;

    private AutorDAO autorDAO = new AutorDAO();
    private ObservableList<Autor> autorList = FXCollections.observableArrayList();

    // --- UTWORY ---
    @FXML private TableView<Utwor> utworTable;
    @FXML private TableColumn<Utwor, Integer> colUtworId;
    @FXML private TableColumn<Utwor, String> colUtworTytul;
    @FXML private TableColumn<Utwor, Integer> colUtworRok;
    @FXML private TableColumn<Utwor, Integer> colUtworDlugosc;
    @FXML private TableColumn<Utwor, String> colUtworGatunek;

    @FXML private TextField fieldTytul;
    @FXML private TextField fieldRok;
    @FXML private TextField fieldDlugosc;
    @FXML private TextField searchUtworField;
    @FXML private TextField fieldGatunek;

    private UtworDAO utworDAO = new UtworDAO();
    private ObservableList<Utwor> utworList = FXCollections.observableArrayList();

    // --- POŁĄCZENIA ---
    @FXML private ComboBox<Autor> comboAutor;
    @FXML private ComboBox<Utwor> comboUtwor;
    @FXML private TableView<Polaczenie> polaczenieTable;
    @FXML private TableColumn<Polaczenie, String> colPolaczenieAutor;
    @FXML private TextField searchPolaczenieField;
    @FXML private TableColumn<Polaczenie, String> colPolaczenieUtwor;

    private PolaczenieDAO polaczenieDAO = new PolaczenieDAO();
    private ObservableList<Polaczenie> polaczenieList = FXCollections.observableArrayList();


    @FXML
    public void initialize() {
        // Sprawdź połączenie przy starcie widoku
        String status = com.example.dbapp.util.DatabaseConnection.checkConnection();
        if (!"OK".equals(status)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd połączenia z bazą");
            alert.setHeaderText("Nie można połączyć się z Oracle");
            alert.setContentText(status);
            alert.showAndWait();
        }

        initAutorzy();
        initUtwory();
        initPolaczenia();
    }

    // ================= AUTORZY =================

    private void initAutorzy() {
        colAutorId.setCellValueFactory(new PropertyValueFactory<>("idAutor"));
        colAutorImie.setCellValueFactory(new PropertyValueFactory<>("imie"));
        colAutorNazwisko.setCellValueFactory(new PropertyValueFactory<>("nazwisko"));
        colAutorPseudonim.setCellValueFactory(new PropertyValueFactory<>("pseudonim"));
        colAutorKraj.setCellValueFactory(new PropertyValueFactory<>("kraj"));

        FilteredList<Autor> filteredData = new FilteredList<>(autorList, p -> true);

        searchAutorField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(autor -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (autor.getImie().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (autor.getNazwisko().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (autor.getPseudonim() != null && autor.getPseudonim().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (autor.getKraj().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });

        SortedList<Autor> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(autorTable.comparatorProperty());

        autorTable.setItems(sortedData);
        loadAutorzy();

        autorTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                fieldImie.setText(newVal.getImie());
                fieldNazwisko.setText(newVal.getNazwisko());
                fieldPseudonim.setText(newVal.getPseudonim());
                fieldKraj.setText(newVal.getKraj());
            }
        });
    }

    @FXML
    private void loadAutorzy() {
        try {
            autorList.setAll(autorDAO.findAll());
        } catch (Exception e) {
            showAlert("Błąd pobierania autorów: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddAutor() {
        try {
            Autor autor = new Autor(
                    fieldImie.getText(),
                    fieldNazwisko.getText(),
                    fieldPseudonim.getText(),
                    fieldKraj.getText()
            );
            autorDAO.save(autor);
            loadAutorzy();
            clearAutorFields();
        } catch (Exception e) {
            showAlert("Błąd dodawania autora: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUpdateAutor() {
        Autor selected = autorTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                selected.setImie(fieldImie.getText());
                selected.setNazwisko(fieldNazwisko.getText());
                selected.setPseudonim(fieldPseudonim.getText());
                selected.setKraj(fieldKraj.getText());
                
                autorDAO.update(selected);
                loadAutorzy();
                clearAutorFields();
            } catch (Exception e) {
                showAlert("Błąd aktualizacji autora: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            showAlert("Wybierz autora do edycji");
        }
    }

    @FXML
    private void handleDeleteAutor() {
        Autor selected = autorTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                autorDAO.delete(selected.getIdAutor());
                loadAutorzy();
                clearAutorFields();
            } catch (Exception e) {
                showAlert("Błąd usuwania autora: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            showAlert("Wybierz autora do usunięcia");
        }
    }

    @FXML
    private void handleShowAutorUtwory() {
        Autor selected = autorTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                List<String> utwory = autorDAO.getUtworyForAutor(selected.getIdAutor());
                showListPopup("Utwory autora: " + selected.getImie() + " " + selected.getNazwisko(), utwory);
            } catch (Exception e) {
                showAlert("Błąd pobierania utworów: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            showAlert("Wybierz autora, aby zobaczyć utwory");
        }
    }

    private void clearAutorFields() {
        fieldImie.clear();
        fieldNazwisko.clear();
        fieldPseudonim.clear();
        fieldKraj.clear();
    }

    // ================= UTWORY =================

    private void initUtwory() {
        colUtworId.setCellValueFactory(new PropertyValueFactory<>("idUtwor"));
        colUtworTytul.setCellValueFactory(new PropertyValueFactory<>("tytul"));
        colUtworRok.setCellValueFactory(new PropertyValueFactory<>("rokWydania"));
        colUtworDlugosc.setCellValueFactory(new PropertyValueFactory<>("dlugoscSekundy"));
        colUtworGatunek.setCellValueFactory(new PropertyValueFactory<>("gatunek"));

        FilteredList<Utwor> filteredData = new FilteredList<>(utworList, p -> true);

        searchUtworField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(utwor -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (utwor.getTytul().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(utwor.getRokWydania()).contains(lowerCaseFilter)) {
                    return true;
                } else if (utwor.getGatunek().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });

        SortedList<Utwor> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(utworTable.comparatorProperty());

        utworTable.setItems(sortedData);
        loadUtwory();

        utworTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                fieldTytul.setText(newVal.getTytul());
                fieldRok.setText(String.valueOf(newVal.getRokWydania()));
                fieldDlugosc.setText(String.valueOf(newVal.getDlugoscSekundy()));
                fieldGatunek.setText(newVal.getGatunek());
            }
        });
    }

    @FXML
    private void loadUtwory() {
        try {
            utworList.setAll(utworDAO.findAll());
        } catch (Exception e) {
            showAlert("Błąd pobierania utworów: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddUtwor() {
        try {
            Utwor utwor = new Utwor(
                    fieldTytul.getText(),
                    Integer.parseInt(fieldRok.getText()),
                    Integer.parseInt(fieldDlugosc.getText()),
                    fieldGatunek.getText()
            );
            utworDAO.save(utwor);
            loadUtwory();
            clearUtworFields();
        } catch (NumberFormatException e) {
            showAlert("Rok i długość muszą być liczbami!");
        } catch (Exception e) {
            showAlert("Błąd dodawania utworu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUpdateUtwor() {
        Utwor selected = utworTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                selected.setTytul(fieldTytul.getText());
                selected.setRokWydania(Integer.parseInt(fieldRok.getText()));
                selected.setDlugoscSekundy(Integer.parseInt(fieldDlugosc.getText()));
                selected.setGatunek(fieldGatunek.getText());

                utworDAO.update(selected);
                loadUtwory();
                clearUtworFields();
            } catch (NumberFormatException e) {
                showAlert("Rok i długość muszą być liczbami!");
            } catch (Exception e) {
                showAlert("Błąd aktualizacji utworu: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            showAlert("Wybierz utwór do edycji");
        }
    }

    @FXML
    private void handleDeleteUtwor() {
        Utwor selected = utworTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                utworDAO.delete(selected.getIdUtwor());
                loadUtwory();
                clearUtworFields();
            } catch (Exception e) {
                showAlert("Błąd usuwania utworu: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            showAlert("Wybierz utwór do usunięcia");
        }
    }

    @FXML
    private void handleShowUtworAutorzy() {
        Utwor selected = utworTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                List<String> autorzy = utworDAO.getAutorzyForUtwor(selected.getIdUtwor());
                showListPopup("Autorzy utworu: " + selected.getTytul(), autorzy);
            } catch (Exception e) {
                showAlert("Błąd pobierania autorów: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            showAlert("Wybierz utwór, aby zobaczyć autorów");
        }
    }

    private void clearUtworFields() {
        fieldTytul.clear();
        fieldRok.clear();
        fieldDlugosc.clear();
        fieldGatunek.clear();
    }

    // ================= POŁĄCZENIA =================

    private void initPolaczenia() {
        colPolaczenieAutor.setCellValueFactory(new PropertyValueFactory<>("autorPelneNazwisko"));
        colPolaczenieUtwor.setCellValueFactory(new PropertyValueFactory<>("utworTytul"));

        FilteredList<Polaczenie> filteredData = new FilteredList<>(polaczenieList, p -> true);

        searchPolaczenieField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(polaczenie -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (polaczenie.getAutorPelneNazwisko().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (polaczenie.getUtworTytul().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });

        SortedList<Polaczenie> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(polaczenieTable.comparatorProperty());

        polaczenieTable.setItems(sortedData);

        comboAutor.setItems(autorList);
        comboAutor.setConverter(new StringConverter<Autor>() {
            @Override
            public String toString(Autor autor) {
                return autor == null ? null : autor.getImie() + " " + autor.getNazwisko();
            }
            @Override
            public Autor fromString(String string) {
                return null; 
            }
        });

        comboUtwor.setItems(utworList);
        comboUtwor.setConverter(new StringConverter<Utwor>() {
            @Override
            public String toString(Utwor utwor) {
                return utwor == null ? null : utwor.getTytul();
            }
            @Override
            public Utwor fromString(String string) {
                return null;
            }
        });

        polaczenieTable.setItems(polaczenieList);
        loadPolaczenia();
    }

    @FXML
    private void loadPolaczenia() {
        try {
            polaczenieList.setAll(polaczenieDAO.findAll());
        } catch (Exception e) {
            showAlert("Błąd pobierania połączeń: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddPolaczenie() {
        Autor autor = comboAutor.getValue();
        Utwor utwor = comboUtwor.getValue();

        if (autor != null && utwor != null) {
            try {
                polaczenieDAO.save(autor.getIdAutor(), utwor.getIdUtwor());
                loadPolaczenia();
                showAlert("Połączono autora z utworem!");
            } catch (Exception e) {
                showAlert("Błąd łączenia: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            showAlert("Wybierz autora i utwór!");
        }
    }

    @FXML
    private void handleDeletePolaczenie() {
        Polaczenie selected = polaczenieTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                polaczenieDAO.delete(selected.getIdAutor(), selected.getIdUtwor());
                loadPolaczenia();
            } catch (Exception e) {
                showAlert("Błąd usuwania połączenia: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            showAlert("Wybierz połączenie do usunięcia");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Uwaga");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showListPopup(String title, List<String> items) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);

        ListView<String> listView = new ListView<>();
        listView.getItems().addAll(items);
        if (items.isEmpty()) {
            listView.getItems().add("(Brak powiązań)");
        }

        VBox layout = new VBox(10);
        layout.setPadding(new javafx.geometry.Insets(10));
        layout.getChildren().addAll(new Label(title), listView);

        Scene scene = new Scene(layout, 300, 250);
        stage.setScene(scene);
        stage.showAndWait();
    }

    @FXML
    private void handleCleanDuplicates() {
        try {
            autorDAO.removeDuplicates();
            utworDAO.removeDuplicates();
            
            // Odśwież widoki
            loadAutorzy();
            loadUtwory();
            loadPolaczenia();
            
            showAlert("Duplikaty zostały usunięte!");
        } catch (Exception e) {
            showAlert("Błąd podczas usuwania duplikatów: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleResetDatabase() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Potwierdzenie resetu");
        alert.setHeaderText("Czy na pewno chcesz zresetować bazę?");
        alert.setContentText("Ta operacja usunie WSZYSTKIE dane, ID wrócą do 1, i przywróci dane fabryczne.");

        if (alert.showAndWait().get() == ButtonType.OK) {
            try {
                com.example.dbapp.util.DatabaseInitializer.resetDatabase();
                
                // Odśwież widoki
                loadAutorzy();
                loadUtwory();
                loadPolaczenia();
                
                showAlert("Baza została zresetowana do stanu początkowego!");
            } catch (Exception e) {
                showAlert("Błąd resetowania bazy: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}