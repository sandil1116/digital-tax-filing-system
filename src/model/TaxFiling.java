package model;

import java.util.Date;

public class TaxFiling implements TaxFilingInterface {
    private final String filingID;
    private final TaxPayer taxpayer;
    private double taxAmount;
    private String status;
    private final Date filingDate;

    public TaxFiling(String filingID, TaxPayer taxpayer) {
        this.filingID = filingID;
        this.taxpayer = taxpayer;
        this.status = "Submitted";
        this.filingDate = new Date();
    }

    public String getFilingID() { return filingID; }
    public TaxPayer getTaxpayer() { return taxpayer; }
    public double getTaxAmount() { return taxAmount; }
    public String getStatus() { return status; }
    public Date getFilingDate() { return filingDate; }

    public void setStatus(String status) { this.status = status; }

    @Override
    public void submitFiling() {
        this.taxAmount = taxpayer.calculateTax();
        this.status = "Submitted";
    }

    @Override
    public void generateReport() {
        System.out.println("---- Filing Report ----");
        System.out.println("Filing ID: " + filingID);
        System.out.println("Taxpayer: " + taxpayer.getTaxpayerID() + " - " + taxpayer.getName());
        System.out.println("Income: " + taxpayer.getIncome());
        System.out.println("Tax: " + taxAmount);
        System.out.println("Status: " + status);
        System.out.println("-----------------------");
    }
}
