module com.example.dbapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.naming;
    requires io.github.cdimascio.dotenv.java;

    opens com.example.dbapp to javafx.fxml;
    opens com.example.dbapp.model to javafx.base; // Ważne dla TableView
    exports com.example.dbapp;
}