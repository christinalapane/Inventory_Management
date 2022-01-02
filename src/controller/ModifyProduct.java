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

public class ModifyProduct implements Initializable {

    //textfields
    @FXML private TextField productID;
    @FXML private TextField productName;
    @FXML private TextField productMax;
    @FXML private TextField productMin;
    @FXML private TextField productPrice;
    @FXML private TextField productInv;

    //searchbar
    @FXML private TextField searchPart;

    //parts table
    @FXML private TableColumn<Part, Double> partPrice;
    @FXML private TableColumn<Part, Integer> partStock;
    @FXML private TableColumn<Part, String> partName;
    @FXML private TableColumn<Part, Integer> partID;
    @FXML private TableView<Part> partsTable;

    //associated table
    @FXML private TableColumn<Part, Double> associatedPrice;
    @FXML private TableColumn<Part, Integer> associatedStock;
    @FXML private TableColumn<Part, String> associatedName;
    @FXML private TableColumn<Part, Integer> associatedID;
    @FXML private TableView<Part> associatedTable;

    //buttons
    @FXML private Button saveProduct;
    @FXML private Button cancelProduct;
    @FXML private Button removeAssociated;
    @FXML private Button addPartToAssociated;

    //selected product
    private Product selectedProduct;
    //associated parts included in parts array
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //selected Product pointing to highlighted items
        selectedProduct = MainScreen.getProductToModify();
        associatedParts = selectedProduct.getAllAssociatedParts();

        //partsTable columns
        partID.setCellValueFactory(new PropertyValueFactory<>("id"));
        partName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        partsTable.setItems(model.Inventory.getAllParts());

        //associatedTables column
        associatedID.setCellValueFactory(new PropertyValueFactory<>("id"));
        associatedName.setCellValueFactory(new PropertyValueFactory<>("name"));
        associatedStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        associatedPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        associatedTable.setItems(associatedParts);

        //bringing over product information from highlighted product
        //I first had an issue with this screen because I forgot to add this in//
        productID.setText(String.valueOf(selectedProduct.getProductID()));
        productName.setText(selectedProduct.getName());
        productInv.setText(String.valueOf(selectedProduct.getStock()));
        productPrice.setText(String.valueOf(selectedProduct.getCost()));
        productMin.setText(String.valueOf(selectedProduct.getMin()));
        productMax.setText(String.valueOf(selectedProduct.getMax()));
    }

    //cancel button returns to main screen
    @FXML void onCancelProduct(ActionEvent actionEvent) throws IOException {
        if (confirmAlert("Confirm", "All progress will be lost \n Press OK to cancel")) {
            returnToMain(actionEvent);
        }
    }

    //search after Enter is hit
    @FXML void onEnter(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            onSearchPart();
        }
    }

    //search parts. Enter shows only valid parts. all parts shown in table after cleared.
    @FXML void onSearchPart() {
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

    //checks valid input. saves then returns to main screen.
    @FXML void onSaveProduct(ActionEvent actionEvent) throws IOException {
        try {
            int id = selectedProduct.getProductID();
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
                for (Part part : associatedParts) {
                    newProduct.addAssociatedParts(part);
                }
                Inventory.addProduct(newProduct);
                Inventory.deleteProduct(selectedProduct);
                returnToMain(actionEvent);
            }
        } catch (Exception e) {
            emptyValueError();
        }
    }

    //adds part to associated table
    @FXML void onAddPartToAssociated(ActionEvent actionEvent) {
        Part selectedPart = partsTable.getSelectionModel().getSelectedItem();
        if(selectedPart == null){
           errorAlert("Error", "Must pick a part to associate");
        }else
            confirmAlert("Confirm", "Press OK to add part to associated product");
            associatedParts.add(selectedPart);
            associatedTable.setItems(associatedParts);
    }

    //removes part from associated table
    @FXML void onRemoveAssociated(ActionEvent actionEvent) {
        Part selectedPart = associatedTable.getSelectionModel().getSelectedItem();
        if(selectedPart != null){
            MainScreen.confirmAlert("Confirm removal", "Hit OK if you want to remove the associateion");
            associatedParts.remove(selectedPart);
            associatedTable.setItems(associatedParts);
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
