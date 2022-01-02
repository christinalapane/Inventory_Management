package model;/**
 *
 * @author Christina LaPane
 */

//extension of part. if outsourced- company name is used//
public class Outsourced extends Part {

    private String companyName;
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);

        this.companyName = companyName;
    }

    public String getCompanyName() { return companyName;}
    public void setCompanyName(String companyName){this.companyName = companyName;}
}
