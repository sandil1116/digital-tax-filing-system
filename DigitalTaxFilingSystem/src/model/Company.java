package model;

public class Company extends TaxPayer {

    public Company(String taxpayerID, String name, String email, double income) {
        super(taxpayerID, name, email, income, "Company");
    }

    @Override
    public double calculateTax() {
        // companies pay slightly higher in this simple example
        if (income <= 200000) return income * 0.10;
        if (income <= 800000) return income * 0.20;
        return income * 0.30;
    }
}
