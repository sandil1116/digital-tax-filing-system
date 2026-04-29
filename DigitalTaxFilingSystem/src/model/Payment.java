package model;

import java.util.Date;

public class Payment {
    private final String paymentID;
    private final TaxFiling filing;
    private final double amount;
    private final String method;
    private boolean verified;
    private final Date paymentDate;

    public Payment(String paymentID, TaxFiling filing, String method) {
        this.paymentID = paymentID;
        this.filing = filing;
        this.amount = filing.getTaxAmount();
        this.method = method;
        this.verified = true; // simple project = auto verified
        this.paymentDate = new Date();
    }

    public String getPaymentID() { return paymentID; }
    public TaxFiling getFiling() { return filing; }
    public double getAmount() { return amount; }
    public String getMethod() { return method; }
    public boolean isVerified() { return verified; }
    public Date getPaymentDate() { return paymentDate; }

    public void processPayment() {
        // just a simulation
        verified = true;
    }
}
