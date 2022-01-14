package com.example.tema6.application;


import  com.example.tema6.controller.RegistrationSystemController;
import  com.example.tema6.exception.RegisterException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class StudentView extends Application{
    private final RegistrationSystemController registrationSystemController;
    private Stage stage;
    private Long studentID;
    private Scene scene;
    final ToolBar toolBar = new ToolBar();
    final Button buttonHome = new Button("Sign Out");
    final Button buttonExit = new Button("Exit");

    final Button registerButton = new Button("Register");
    final ListView kursList = new ListView();
    final ListView studentListView = new ListView();
    final TextField kursID = new TextField();

    final Button creditButton = new Button("Credit");
    final TextArea textArea = new TextArea();

    final Button kursButton = new Button("Kurs");

    final Label kurse = new Label("Kurse");
    final Label credits = new Label("Credits");
    final Label kursStudent = new Label("Eigene Kurs");


    public StudentView() {
        this.registrationSystemController = new RegistrationSystemController();
    }

    public StudentView(RegistrationSystemController registrationSystemController){
        this.registrationSystemController = registrationSystemController;
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Student");
        setStage(stage);
        setToolBar();

        registerButton.setAlignment(Pos.TOP_LEFT);
        creditButton.setAlignment(Pos.BASELINE_CENTER);
        kursButton.setAlignment(Pos.CENTER_RIGHT);

        setRegisterButton();
        setCreditButton();
        setKursButton();

        HBox texts = new HBox();
        texts.getChildren().addAll(kursID);
        texts.setAlignment(Pos.CENTER);
        HBox buttons = new HBox();
        buttons.getChildren().addAll(registerButton, creditButton, kursButton);
        buttons.setSpacing(20);
        buttons.setAlignment(Pos.TOP_CENTER);

        VBox vBox = new VBox();
        vBox.getChildren().addAll(toolBar, kurse, kursList, texts, credits, textArea, kursStudent, studentListView, buttons);
        vBox.setSpacing(10);

        StackPane root = new StackPane();
        root.getChildren().addAll(vBox);
        scene = new Scene(root, 1000, 750);

        stage.setScene(scene);
        stage.show();
    }

    public void setStudentID(Long studentID) { this.studentID = studentID; }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void setToolBar(){
        toolBar.getItems().addAll(buttonHome, buttonExit);
        buttonHome.setOnAction(actionEvent -> getStage().close());
        buttonExit.setOnAction(actionEvent ->  Platform.exit());
    }

    private void setRegisterButton(){
        List<String> toStringKurs = new ArrayList<>();
        registrationSystemController.controller_getAllKurse()
                .forEach(vorlesung -> {
                    final boolean add = toStringKurs.add("Vorlesung ID: " + vorlesung.getKursID() +
                            "\t\tVorlesung Name: " +  vorlesung.getName()  + "\t\tVorlesung Credits: " + vorlesung.getCredits());
                });
        ObservableList<String> kurse = FXCollections.observableArrayList(toStringKurs);
        kursList.setPrefSize(400, 300);
        kursList.setItems(kurse);

        kursID.setPromptText("Kurs Id:\n");
        registerButton.setOnAction(actionEvent -> {
            if (kursID.getText().trim().isEmpty() || registrationSystemController.controllerKursRepository().findOne(Long.valueOf(kursID.getText())) == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Register Error");
                alert.setContentText("ENTER an ID");
                alert.showAndWait();
            } else {
                try {
                    registrationSystemController.controller_register(Long.parseLong(kursID.getText()), studentID);
                } catch (RegisterException e) {
                    final Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Exception Error");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                }
            }
            kursID.clear();
        });

    }

    private void setCreditButton(){
        textArea.setPrefRowCount(1);
        textArea.setPrefColumnCount(1);
        creditButton.setOnAction(actionEvent -> textArea.setText("Total Credits:\t" +
                registrationSystemController.controller_studentRepository().findOne(studentID).getTotalCredits()));

    }

    private void setKursButton(){
        kursButton.setOnAction(actionEvent -> {
            ObservableList<Long> studentList = FXCollections.observableArrayList(registrationSystemController
                    .controller_studentRepository().findOne(studentID).getEnrolledCourses());
            studentListView.setItems(studentList);
        });
    }
}
