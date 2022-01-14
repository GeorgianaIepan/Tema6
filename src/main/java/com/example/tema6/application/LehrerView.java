package com.example.tema6.application;


import com.example.tema6.controller.RegistrationSystemController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class LehrerView extends Application {
    private final RegistrationSystemController registrationSystemController;
    private Stage stage;
    private Long lehrerID;
    final ToolBar toolBar = new ToolBar();
    final Button buttonHome = new Button("Sign Out");
    final Button buttonExit = new Button("Exit");
    final Button enrolledStudentsButton = new Button("Enrolled Students");
    final TextArea kursID = new TextArea();
    final ListView kursList = new ListView();
    final ListView enrolledStudents = new ListView();

    final Label kurs = new Label("Kurse");
    final Label students = new Label("Enrolled Students");


    public LehrerView() {
        this.registrationSystemController = new RegistrationSystemController();
    }

    public LehrerView(RegistrationSystemController registrationSystemController) {
        this.registrationSystemController = registrationSystemController;
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Lehrer");
        setStage(stage);
        setToolBar();

        kursID.setPromptText("Enter Kurs Id:\n");
        kursDisplay();
        enrolledStudentsDisplay();

        HBox texts = new HBox();
        texts.getChildren().addAll(kursID);
        texts.setPrefSize(1, 1);
        texts.setAlignment(Pos.CENTER);
        HBox buttons = new HBox();
        buttons.getChildren().addAll(enrolledStudentsButton);
        buttons.setSpacing(20);

        VBox vBox = new VBox(toolBar, kurs, kursList, texts, students, buttons, enrolledStudents);
        vBox.setSpacing(10);

        StackPane root = new StackPane();
        root.getChildren().addAll(vBox);
        Scene scene = new Scene(root, 1000, 750);

        stage.setScene(scene);
        stage.show();
    }

    public void setLehrerID(Long lehrerID) {
        this.lehrerID = lehrerID;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void setToolBar() {
        toolBar.getItems().addAll(buttonHome, buttonExit);
        buttonHome.setOnAction(actionEvent -> getStage().close());
        buttonExit.setOnAction(actionEvent -> Platform.exit());

    }

    private void kursDisplay() {
        List<String> toStringKurs = new ArrayList<>();

        registrationSystemController.controller_getAllKurse()
                .forEach(kurs ->
                {
                    if (kurs.getLehrer() == this.lehrerID)
                        toStringKurs.add("Kurs ID: " + kurs.getKursID() + "\t\tKurs Name: " +
                                kurs.getName() + "\t\tKurs Credits: " + kurs.getCredits());
                });

        ObservableList<String> kurs = FXCollections.observableArrayList(toStringKurs);
        kursList.setPrefSize(400, 300);
        kursList.setItems(kurs);
    }


    private void enrolledStudentsDisplay() {
        enrolledStudentsButton.setOnAction(event -> {
            if (kursID.getText().trim().isEmpty() ||
                    registrationSystemController.controllerKursRepository().findOne(Long.valueOf(kursID.getText())) == null ||
                    registrationSystemController.controllerKursRepository().findOne(Long.valueOf(kursID.getText())).getLehrer() != lehrerID) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Register Error");
                alert.setContentText("Please ENTER a valid ID");
                alert.showAndWait();
            } else {
                ObservableList<Long> studentList = FXCollections.observableArrayList(registrationSystemController
                        .controllerKursRepository().findOne(Long.valueOf(kursID.getText())).getStudentsEnrolled());
                kursID.clear();
                if (studentList.isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Enrolled Infrormation");
                    alert.setContentText("There are no students enrolled.");
                    alert.showAndWait();

                } else
                    enrolledStudents.setItems(studentList);
            }
        });
    }
}
