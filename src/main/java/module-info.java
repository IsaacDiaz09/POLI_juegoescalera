module dev.poli.students.game.poli_juegoescalera {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.desktop;
    requires static lombok;
    requires com.fasterxml.jackson.annotation;
    requires org.slf4j;
    requires com.fasterxml.jackson.databind;


    opens dev.poli.students.game to javafx.fxml;
    exports dev.poli.students.game;
    exports dev.poli.students.game.controller;
    opens dev.poli.students.game.controller to javafx.fxml;
    exports dev.poli.students.game.model;
    opens dev.poli.students.game.model to com.fasterxml.jackson.databind, javafx.fxml;
}