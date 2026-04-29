package model;

public class TaxPayer {
    protected String taxpayerID;
    protected String name;
    protected double income;
    protected String taxType; // Individual or Company

    public TaxPayer(String taxpayerID, String name, double income, String taxType) {
        this.taxpayerID = taxpayerID;
        this.name = name;
        this.income = income;
        this.taxType = taxType;
    }

    public String getTaxpayerID() { return taxpayerID; }
    public String getName() { return name; }
    public double getIncome() { return income; }
    public String getTaxType() { return taxType; }

    public void setIncome(double income) { this.income = income; }

    // override in subclasses
    public double calculateTax() { return 0.0; }
}
