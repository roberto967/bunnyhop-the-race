module Main {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;

    opens cliente;
    opens dados;
    opens cliente.corrida;
}