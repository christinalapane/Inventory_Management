package model;/**
 *
 * @author Christina LaPane
 */

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Inventory {

    //these are used to generate a new part or product id
    private static int partID =0;
    private static int productID = 0;


    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    //addPart
    public static void addPart(Part newPart){
        allParts.add(newPart);
    }

    //addProduct
    public static void addProduct(Product newProduct){
        allProducts.add(newProduct);
    }

    //modifyPart
    public static void modifyPart(int index, Part selectedPart){
        allParts.set(index, selectedPart);
    }

    //modifyProduct
    public static void modifyProduct(int index, Product selectedProduct){
        allProducts.set(index, selectedProduct);
    }

    //deletePart
    public static boolean deletePart(Part selectedPart){
        return allParts.remove(selectedPart);
    }

    //deleteProduct
    public static boolean deleteProduct(Product selectedProduct){
        return allProducts.remove(selectedProduct);
    }

   //generate new Part ID
    public static int generatePartID(){
        return ++partID;
    }

    //generate new Product ID
    public static int generateProductID(){
        return ++productID;
    }

    //searchPart by ID
    public static Part lookUpPartID(int partID){
        for(int i = 0; i < allParts.size(); i++){
            Part p = allParts.get(i);
            if(p.getId() == partID){
                return p;
            }
        }
        return null;
    }

    //searchPart by name
    public static ObservableList<Part> lookUpPartName(String searchName){
        ObservableList<Part> namedPart = FXCollections.observableArrayList();

        for(Part p : getAllParts()){
            if(p.getName().contains(searchName)){
                namedPart.add(p);
            }
        }
        return namedPart;
    }

    //Search by product ID
    public static Product lookUpProductID(int productID){

        for(int i = 0; i < allProducts.size(); i++){
            Product p = allProducts.get(i);

            if(p.getProductID() == productID){
                return p;
            }
        }
        return null;
    }

    //searchProduct by name
    public static ObservableList<Product> lookUpProductName(String searchProductName){
        ObservableList<Product> namedProduct = FXCollections.observableArrayList();

        for(Product p : getAllProducts()){
            if(p.getName().contains(searchProductName)){
                namedProduct.add(p);
            }
        }
        return namedProduct;
    }

    //get all parts
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    //getAllProducts
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }
}
