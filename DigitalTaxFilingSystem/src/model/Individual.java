package model;

public class Individual extends TaxPayer {

    public Individual(String taxpayerID, String name, String email, double income) {
        super(taxpayerID, name, email, income, "Individual");
    }

    @Override
    public double calculateTax() {
        // simple slabs (edit if you want)
        if (income <= 100000) return 0;
        if (income <= 500000) return income * 0.10;
        return income * 0.20;
    }
}
