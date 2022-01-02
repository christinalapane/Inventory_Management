package model;/**
 *
 * @author Christina LaPane
 */

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Product {

    //associated parts added into part array
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private int productID, stock, min, max;
    private double cost;
    private String name;

    public Product(int productID, String name, int stock, double cost, int min, int max ){
        this.productID = productID;
        this.name = name;
        this.stock = stock;
        this.cost = cost;
        this.min=min;
        this.max=max;
    }



    //gets and returns productID
    public int getProductID(){return productID;}
    public void setProductID(int productID){this.productID = productID;};

    //gets and returns name
    public String getName(){return name;}
    public void setName(String name){this.name = name;}

    //gets and returns inventory
    public int getStock(){return stock;}
    public void setStock(int stock){this.stock=stock;}

    //gets and returns price
    public double getCost(){return cost;}
    public void setCost(double cost){this.cost=cost;}

    //gets and returns min
    public int getMin(){return min;}
    public void setMin(int min){this.min = min;}

    //gets and returns max
    public int getMax(){return max;}
    public void setMax(int max){this.max = max;}

    //returns associated part
    public void addAssociatedParts(Part part){
       associatedParts.add(part);
    }

    //gets and returns all associated parts
    public ObservableList<Part> getAllAssociatedParts(){return associatedParts;}
    public void setAssociatedParts(Part part){associatedParts.add(part);}

    //deletes associated part
    public boolean deleteAssociatedPart(Part selectedAssociatedPart){
        for(Part p : associatedParts){
            if(p.getId() == selectedAssociatedPart.getId()){
                associatedParts.remove(p);
                return true;
            }
        }
        return false;
    }

}
