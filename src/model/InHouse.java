package model;/**
 *
 * @author Christina LaPane
 */

//in house is an extension of part. if inhouse- machine ID //
public class InHouse extends Part{

    int machineID;
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineID) {
        super(id, name, price, stock, min, max);

        this.machineID = machineID;
    }

    public int getMachineID(){ return machineID;}
    public void setMachineID(int machineID){this.machineID = machineID;}
}
