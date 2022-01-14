package com.example.tema6;


import  com.example.tema6.application.LehrerView;
import  com.example.tema6.application.StudentView;
import  com.example.tema6.controller.RegistrationSystemController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class UIController {
    final RegistrationSystemController registrationSystemController = new RegistrationSystemController();
    @FXML
    private TextField textField;

    @FXML
    protected void onStudentButtonClick(){
        if (textField.getText().trim().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Register Error");
            alert.setContentText("No Id");
            alert.showAndWait();
        }
        else if (registrationSystemController.controller_studentRepository().findOne(Long.valueOf(textField.getText())) == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Register Error");
            alert.setContentText("No Student with ID: " + textField.getText());
            alert.showAndWait();
            textField.clear();
        }
        else {
            try {
                StudentView studentView = new StudentView(registrationSystemController);
                studentView.setStudentID(Long.valueOf(textField.getText()));
                studentView.start(new Stage());
                textField.clear();
            } catch (Exception e) {
                final Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Exception Error");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }

    }

    @FXML
    protected void onLehrerButtonClick(){
        if (textField.getText().trim().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Register Error");
            alert.setContentText("No Id");
            alert.showAndWait();
        }
        else if (registrationSystemController.controller_lehrerRepository().findOne(Long.valueOf(textField.getText())) == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Register Error");
            alert.setContentText("There is no Lehrer with ID: " + textField.getText());
            alert.showAndWait();
            textField.clear();
        }
        else {
            try {
                LehrerView lehrerView = new LehrerView(registrationSystemController);
                lehrerView.setLehrerID(Long.valueOf(textField.getText()));
                lehrerView.start(new Stage());
                textField.clear();
            } catch (Exception e) {
                final Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Exception Error");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }
    }

    @FXML
    protected void onHomeButtonClick(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Home Page");
        alert.setContentText("Home Page");
        alert.showAndWait();
    }


    @FXML
    protected void onExitButtonClick() {
        Platform.exit();
    }
}
