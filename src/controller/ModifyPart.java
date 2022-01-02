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
import model.Part;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ModifyPart implements Initializable {
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

    //selected part to use throughout
    public Part selectedPart;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //selectedPart points to mainscreen highlighted part
        selectedPart = MainScreen.getPartToModify();

        //if highlighted item is in house
        if(selectedPart instanceof InHouse){
            inHouseButton.setSelected(true);
            machineIDLabel.setText("Machine ID");
            machineID.setText(String.valueOf((((InHouse) selectedPart).getMachineID())));
        }
        //if highlighted item is outsourced
        if(selectedPart instanceof Outsourced){
            outsourcedButton.setSelected(true);
            machineIDLabel.setText("Company Name");
            machineID.setText(String.valueOf(((Outsourced)selectedPart).getCompanyName()));
        }
        //bring in all information from selectedItem. I first had an issue with this screen because I forgot to add this//
        partID.setText(String.valueOf(selectedPart.getId()));
        partName.setText(selectedPart.getName());
        partInv.setText(String.valueOf(selectedPart.getStock()));
        partCost.setText(String.valueOf(selectedPart.getPrice()));
        partMin.setText(String.valueOf(selectedPart.getMin()));
        partMax.setText(String.valueOf(selectedPart.getMax()));

    }

    //outsource radio button is chosen
    @FXML void onOutsource(ActionEvent actionEvent) {
        machineIDLabel.setText("Company Name");
    }
    //inhouse radio button is chosen
    @FXML void onInHouse(ActionEvent actionEvent) {
        machineIDLabel.setText("Machine ID");
    }
    //cancel button - returns to main
    @FXML void onCancelPart(ActionEvent actionEvent) throws IOException {
        if(MainScreen.confirmAlert("Confirm", "All new information will be lost \n Press OK to cancel")){
            returnToMain(actionEvent);
        }
    }
    //save. doesn't generate new ID because just changing information.
    @FXML void onSavePart(ActionEvent actionEvent) throws IOException{
        try {
            int id = selectedPart.getId();
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
                        Inventory.addPart(in);
                        addPartComplete = true;
                    }catch (Exception e){
                        machineIdError();

                    }

                }
                if(outsourcedButton.isSelected()){
                    companyName = machineID.getText();
                    Outsourced out = new Outsourced(id, name, price, inventory, min, max, companyName);
                    Inventory.addPart(out);
                    addPartComplete = true;
                }
                if(addPartComplete){
                    Inventory.deletePart(selectedPart);
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

    @FXML void onMachineID(ActionEvent actionEvent) {
    }


}
