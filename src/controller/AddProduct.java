package controller;/**
 *
 * @author Christina LaPane
 */

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static controller.MainScreen.confirmAlert;
import static controller.MainScreen.errorAlert;

public class AddProduct implements Initializable {

    //parts table
    @FXML private TableColumn<Part, Double> partPrice;
    @FXML private TableColumn<Part, Integer> partStock;
    @FXML private TableColumn<Part, String> partName;
    @FXML private TableColumn<Part, Integer> partID;
    @FXML private TableView<Part> partsTable;

    //associated parts
    @FXML private TableColumn<Part, Double> associatedPrice;
    @FXML private TableColumn<Part, Integer> associatedStock;
    @FXML private TableColumn<Part, String> associatedName;
    @FXML private TableColumn<Part, Integer> associatedID;
    @FXML private TableView<Part> associatedTable;

    //buttons
    @FXML private Button addPartToAssociated;
    @FXML private Button removeAssociated;
    @FXML private Button cancelProduct;
    @FXML private Button saveProduct;

    //textfields
    @FXML private TextField productID;
    @FXML private TextField productName;
    @FXML private TextField productMax;
    @FXML private TextField productMin;
    @FXML private TextField productPrice;
    @FXML private TextField productInv;

    //searchbar
    @FXML private TextField searchPart;

    //add associatedParts into parts array
    ObservableList<Part> associatedPart = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //initialize parts table
        partID.setCellValueFactory(new PropertyValueFactory<>("id"));
        partName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        partsTable.setItems(Inventory.getAllParts());

        //initialize associated parts table
        associatedID.setCellValueFactory(new PropertyValueFactory<>("id"));
        associatedName.setCellValueFactory(new PropertyValueFactory<>("name"));
        associatedStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        associatedPrice.setCellValueFactory(new PropertyValueFactory<>("price"));


    }

    //cancel button returns to main screen
    @FXML public void onCancelProduct(ActionEvent actionEvent) throws IOException {
        if(MainScreen.confirmAlert("Confirm", "All progress will be lost \n Press OK to cancel")){
            returnToMain(actionEvent);
        }
    }

    //after hitting enter in search bar
    @FXML public void onEnter(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            onSearchPart();
        }
    }

    //searching through parts with name or ID. shows only valid part after hitting enter. Shows all parts after clear//
    @FXML public void onSearchPart() {
        String q = searchPart.getText();
        ObservableList<Part> allParts = Inventory.getAllParts();
        ObservableList<Part> foundPart = FXCollections.observableArrayList();

        for (Part p : allParts) {
            if (String.valueOf(p.getId()).contains(q) || p.getName().contains(q)) {
                foundPart.add(p);
            }
        }
        partsTable.setItems(foundPart);
        if (foundPart.size() == 0) {
            errorAlert("Search Error", "Must enter a valid ID or Part Name");
        }
        if (searchPart.getText().isEmpty()) {
            partsTable.setItems(Inventory.getAllParts());
        }
    }

    //tests if input is valid and then adds- goes to main screen//
    @FXML public void onSaveProduct(ActionEvent actionEvent) throws IOException {

        try {
            int id = 0;
            String name = productName.getText();
            Double price = Double.parseDouble(productPrice.getText());
            int inventory = Integer.parseInt(productInv.getText());
            int min = Integer.parseInt(productMin.getText());
            int max = Integer.parseInt(productMax.getText());

            if (name.isEmpty()) {
                nameError();
            } else if (min > max) {
                maxMinError();
            } else if (inventory < min || inventory > max) {
                inventoryError();
            } else {
                Product newProduct = new Product(id, name, inventory, price, min, max);
                for (Part part : associatedPart) {
                    newProduct.addAssociatedParts(part);
                }
                newProduct.setProductID(Inventory.generateProductID());
                Inventory.addProduct(newProduct);
                returnToMain(actionEvent);
            }

    }catch (Exception e){
            emptyValueError();
        }
    }

    //removing part from associated table
    @FXML public void onRemoveAssociated(ActionEvent actionEvent) {
        Part selectedPart = associatedTable.getSelectionModel().getSelectedItem();

        if(selectedPart == null){
            errorAlert("Error", "Must pick an associated part to delete");
        }else {
            confirmAlert("Confirm", "Press OK to delete associated part");
            associatedPart.remove(selectedPart);
            associatedTable.setItems(associatedPart);
        }
    }

    //adding part to associated table
    @FXML public void onAddPartToAssociated(ActionEvent actionEvent) throws IOException {
        Part selectedPart = partsTable.getSelectionModel().getSelectedItem();
        if(selectedPart == null){
            errorAlert("Error", "Must select part to add");
        }else {
            associatedPart.add(selectedPart);
            associatedTable.setItems(associatedPart);
        }
    }

    //called if name is empty
    private void nameError(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error Adding Product");
        alert.setContentText("Must enter valid Product Name");
        alert.showAndWait();
    }

    //called if inventory < min or  > max
    private void inventoryError(){
        Alert alert =  new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error Adding Product");
        alert.setContentText("Inventory must be between minimum and maximum");
        alert.showAndWait();

    }

    //called if max < min or min > max
    private void maxMinError(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error Adding Product");
        alert.setContentText("Max cannot be lower than minimum \n Minimum cannot be higher than maximum");
        alert.showAndWait();
    }

    //called if any empty field
    private void emptyValueError(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error Adding Product");
        alert.setContentText("Must enter information for all fields");
        alert.showAndWait();
    }
    //return to main screen
    private void returnToMain(ActionEvent actionEvent) throws IOException{
        Stage primaryStage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
        primaryStage.setScene(new Scene((Parent) scene));
        primaryStage.show();
    }
}
