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
import java.util.Optional;
import java.util.ResourceBundle;

public class MainScreen implements Initializable {

    //parts table
    @FXML private TableColumn<Part, Double> partPrice;
    @FXML private TableColumn<Part, String> partName;
    @FXML private TableColumn<Part, Integer> partID;
    @FXML private TableView<Part> partsTable;

    //buttons
    @FXML private Button exitProgram;
    @FXML private Button modifyProduct;
    @FXML private Button deleteProduct;
    @FXML private Button addProduct;
    @FXML private Button deletePart;
    @FXML private Button modifyPart;
    @FXML private Button addPart;

    //product table
    @FXML private TableColumn<Product, Double> productCost;
    @FXML private TableColumn<Product, Integer> productStock;
    @FXML private TableColumn<Product, String> productName;
    @FXML private TableColumn<Product, Integer> productID;
    @FXML private TableView<Product> productsTable;

    //search bars
    @FXML private TextField searchProduct;
    @FXML private TextField searchPart;
    @FXML private TableColumn<Part, Integer> parStock;


    //points to the selected part or product to modify. used in modify controllers
    private static Part partToModify;
    private static Product productToModify;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //initializing parts table.
        partsTable.setItems(Inventory.getAllParts());
        partID.setCellValueFactory(new PropertyValueFactory<>("id"));
        partName.setCellValueFactory(new PropertyValueFactory<>("name"));
        parStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        //initializing products table
        productsTable.setItems(Inventory.getAllProducts());
        productID.setCellValueFactory(new PropertyValueFactory<>("productID"));
        productName.setCellValueFactory(new PropertyValueFactory<>("name"));
        productStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productCost.setCellValueFactory(new PropertyValueFactory<>("cost"));

    }

    //To exit program
    @FXML public void onExitProgram(ActionEvent actionEvent) {
        System.exit(0);
    }

    //all methods associated with Part

    //method to point to selected part
    public static Part getPartToModify() {
        return partToModify;
    }

    //pushing add button brings up AddPart screen
    @FXML public void onAddPart(ActionEvent actionEvent) throws IOException {
        Stage addPartStage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Parent scene = FXMLLoader.load(getClass().getResource("/view/AddPart.fxml"));
        addPartStage.setTitle("Add Part");
        addPartStage.setScene(new Scene(scene));
        addPartStage.show();
    }

    //Pressing delete checks if part is highlighted, then removes
    @FXML void onDeletePart(ActionEvent actionEvent) {
        partToModify = partsTable.getSelectionModel().getSelectedItem();

        if (partToModify == null) {
            errorAlert("Error", "Must pick a part to delete");
        } else {
            confirmAlert("Confirm", "Press OK to delete Part");
            Part selectedPart;
            selectedPart = partsTable.getSelectionModel().getSelectedItem();
            Inventory.deletePart(selectedPart);
        }
    }

    //Pressing modify checks to make sure you highlight a part and then brings it to ModifyPart screen
    @FXML public void onModifyPart(ActionEvent actionEvent) throws IOException {
        partToModify = partsTable.getSelectionModel().getSelectedItem();
        if (partToModify == null) {
            errorAlert("Error", "Must pick a part to modify");
        } else {
            Stage modifyPartStage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/view/ModifyPart.fxml"));
            Scene scene = new Scene(root);
            modifyPartStage.setScene(scene);
            modifyPartStage.show();
        }

    }

    //When you press enter it will search part
    @FXML public void onEnterPart(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            onSearchPart();
        }
    }

    /* Searching parts. Only searched part will show in table after Enter is hit.
    Once search field is cleared, all parts show in table again
     */
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


    //all methods associated with product

    //points to selected product
    public static Product getProductToModify() {
        return productToModify;
    }

    //pushing add button on Product brings addProduct screen
    @FXML public void onAddProduct(ActionEvent actionEvent) throws IOException {
        Stage addProductStage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Parent scene = FXMLLoader.load(getClass().getResource("/view/AddProduct.fxml"));
        addProductStage.setTitle("Add Product");
        addProductStage.setScene(new Scene(scene));
        addProductStage.show();
    }

    //hitting delete button - checking for highlighted product, before
    @FXML void onDeleteProduct(ActionEvent actionEvent) {
        Product selectedProduct = productsTable.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            errorAlert("Error", "Choose a product to delete");
        } else {
            if(confirmAlert("Confirm", "Press OK to delete Product")){
                ObservableList<Part> associatedParts = selectedProduct.getAllAssociatedParts();
                if(associatedParts.size() >=1){
                    errorAlert("Error", "You must delete associated part first");
                }else{
                    Inventory.deleteProduct(selectedProduct);
                }
        }
        }
    }

    //hitting modify button- checking for highlighted product first then modifyproduct screen
    @FXML public void onModifyProduct(ActionEvent actionEvent) throws IOException {
        productToModify = productsTable.getSelectionModel().getSelectedItem();
        if (productToModify == null) {
            errorAlert("Error", "Must pick a product to modify");
        } else {
            Stage modifyProductStage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/view/ModifyProduct.fxml"));
            Scene scene = new Scene(root);
            modifyProductStage.setScene(scene);
            modifyProductStage.show();
        }
    }

    //pressing enter after searching for product
    @FXML public void onEnterProduct(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            onSearchProduct();
        }
    }

    /*Searching for product. Shows only searched product in table after hitting enter.
    After text field is cleared, all products will show again.
     */
    @FXML public void onSearchProduct() {
        String q = searchProduct.getText();
        ObservableList<Product> allProducts = Inventory.getAllProducts();
        ObservableList<Product> foundProduct = FXCollections.observableArrayList();

        for (Product p : allProducts) {
            if (String.valueOf(p.getProductID()).contains(q) || p.getName().contains(q)) {
                foundProduct.add(p);
            }
        }

        productsTable.setItems(foundProduct);
        if (foundProduct.size() == 0) {
            errorAlert("Search Error", "Must contain valid product ID or Name ");
        }

        if (searchProduct.getText().isEmpty()) {
            productsTable.setItems(Inventory.getAllProducts());
        }
    }


    //warning alert - to use throughout program
    static boolean warningAlert(String title, String context) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setContentText(context);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            return true;
        } else {
            return false;
        }
    }

    //confirm alert - to use throughout program
    static boolean confirmAlert(String title, String context) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setContentText(context);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            return true;
        } else {
            return false;

        }
    }

    //error alert- to use thorughout program
    static boolean errorAlert(String title, String context) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(context);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            return true;
        } else {
            return false;

        }
    }
}



