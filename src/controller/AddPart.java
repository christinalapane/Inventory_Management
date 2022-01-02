package controller;/**
 *
 * @author Christina LaPane
 */

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;



public class AddPart implements Initializable {
    @FXML private Button cancelPart;
    @FXML private Button savePart;
    @FXML private Label machineIDLabel;
    @FXML private TextField machineID;
    @FXML private TextField partMax;
    @FXML private TextField partMin;
    @FXML private TextField partCost;
    @FXML private TextField partInv;
    @FXML private TextField partName;
    @FXML private TextField partID;
    @FXML private RadioButton outsourcedButton;
    @FXML private ToggleGroup onToggle;
    @FXML private RadioButton inHouseButton;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //always starts out as Inhouse
        inHouseButton.setSelected(true);

    }

    //if outsourced radio button is picked - changes label to company name
    @FXML void onOutsource() {
        machineIDLabel.setText("Company Name");
    }

    //if inhouse radio button is picked- changes label to machine id
    @FXML void onInHouse() {
        machineIDLabel.setText("Machine ID");
    }

    //cancels out any data put in and goes to main screen
    @FXML public void onCancelPart(ActionEvent actionEvent) throws IOException {
        if (MainScreen.confirmAlert("Confirm", "All progress will be lost \n Press OK to cancel")) {
            returnToMain(actionEvent);
        }
    }

    //checks for valid input and then saves, goes to main screen after.//
    @FXML public void onSavePart(ActionEvent actionEvent) throws IOException {
        try {
            int id = 0;
            String name = partName.getText();
            Double price = Double.parseDouble(partCost.getText());
            int inventory = Integer.parseInt(partInv.getText());
            int min = Integer.parseInt(partMin.getText());
            int max = Integer.parseInt(partMax.getText());
            int machineId;
            String companyName;
            boolean addPartComplete = false;

            if (name.isEmpty()) {
                nameError();
            } else if (min > max) {
                maxMinError();
            } else if (inventory < min || inventory > max) {
                inventoryError();
            } else {
                if (inHouseButton.isSelected()) {
                    try{

                        machineId = Integer.parseInt(machineID.getText());
                        InHouse in = new InHouse(id, name, price, inventory, min, max, machineId);
                        in.setId(Inventory.generatePartID());
                        Inventory.addPart(in);
                        addPartComplete = true;
                    }catch (Exception e){
                        machineIdError();

                    }

                }
                if(outsourcedButton.isSelected()){
                    companyName = machineID.getText();
                    Outsourced out = new Outsourced(id, name, price, inventory, min, max, companyName);
                    out.setId(Inventory.generatePartID());
                    Inventory.addPart(out);
                    addPartComplete = true;
                }
                if(addPartComplete){
                    returnToMain(actionEvent);
                }
            }
        }catch(Exception e){
            emptyValueError();
        }
    }


    //error for name
    private void nameError(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error Adding Product");
        alert.setContentText("Must enter valid Product Name");
        alert.showAndWait();
    }

    //error for inventory
    private void inventoryError(){
        Alert alert =  new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error Adding Product");
        alert.setContentText("Inventory must be between minimum and maximum");
        alert.showAndWait();

    }

    //error for max/min
    private void maxMinError(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error Adding Product");
        alert.setContentText("Max cannot be lower than minimum \n Minimum cannot be higher than maximum");
        alert.showAndWait();
    }

    //error for any empty fields
    private void emptyValueError(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error Adding Product");
        alert.setContentText("Must enter information for all fields");
        alert.showAndWait();
    }

    //make sure text for machineID is correct based on inhouse or outsourced//
    private void machineIdError(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error Adding Product");
        alert.setContentText("Must enter number for Machine ID field \n Or change to outsourced");
        alert.showAndWait();

    }

   //return to main screen
    private void returnToMain(ActionEvent actionEvent) throws IOException{
        Stage primaryStage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
        primaryStage.setScene(new Scene((Parent) scene));
        primaryStage.show();
    }


    @FXML  void onMachineID(ActionEvent actionEvent) {
    }
}
