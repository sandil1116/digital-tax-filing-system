package main;

import dao.AdminDAO;
import dao.PaymentDAO;
import dao.TaxFilingDAO;
import dao.TaxPayerDAO;
import model.*;

import java.util.ArrayList;
import java.util.Scanner;

public class MainSystem {

    static ArrayList<TaxFiling> filings = new ArrayList<>();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n---- Digital Tax Filing System ----");
            System.out.println("1. Register");
            System.out.println("2. Login & File Tax");
            System.out.println("3. Pay");
            System.out.println("4. Admin Approve");
            System.out.println("5. Exit");

            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) {
                System.out.print("1 Individual / 2 Company: ");
                int t = sc.nextInt(); sc.nextLine();

                System.out.print("ID: ");
                String id = sc.nextLine();

                System.out.print("Name: ");
                String name = sc.nextLine();

                System.out.print("Income: ");
                double income = sc.nextDouble(); sc.nextLine();

                System.out.print("Password: ");
                String pw = sc.nextLine();

                TaxPayer tp = (t == 2) ? new Company(id, name, income) : new Individual(id, name, income);
                boolean ok = TaxPayerDAO.insertTaxPayer(tp, pw);
                System.out.println(ok ? "Registered!" : "ID already exists / failed.");

            } else if (choice == 2) {
                System.out.print("ID: ");
                String id = sc.nextLine();
                System.out.print("Password: ");
                String pw = sc.nextLine();

                TaxPayer user = TaxPayerDAO.login(id, pw);
                if (user == null) {
                    System.out.println("Login failed.");
                    continue;
                }

                System.out.print("Income for this filing: ");
                double income = sc.nextDouble(); sc.nextLine();
                user.setIncome(income);

                String filingId = "F" + System.currentTimeMillis();
                TaxFiling f = new TaxFiling(filingId, user);
                f.submitFiling();
                TaxFilingDAO.insertTaxFiling(f);
                filings.add(f);

                System.out.println("Filed: " + filingId + " Tax=" + f.getTaxAmount());

            } else if (choice == 3) {
                System.out.print("Filing ID: ");
                String fid = sc.nextLine();

                TaxFiling target = null;
                for (TaxFiling f : filings) {
                    if (f.getFilingID().equalsIgnoreCase(fid)) { target = f; break; }
                }
                if (target == null) {
                    System.out.println("Not found in this session.");
                    continue;
                }

                System.out.print("Method (Card/Bank/Cash): ");
                String method = sc.nextLine();

                Payment p = new Payment("P" + System.currentTimeMillis(), target, method);
                p.processPayment();
                PaymentDAO.insertPayment(p);
                System.out.println("Paid.");

            } else if (choice == 4) {
                System.out.print("Admin user: ");
                String au = sc.nextLine();
                System.out.print("Admin pass: ");
                String ap = sc.nextLine();

                Admin admin = AdminDAO.login(au, ap);
                if (admin == null) { System.out.println("Admin login failed."); continue; }

                System.out.print("Filing ID to approve: ");
                String fid = sc.nextLine();

                boolean ok = TaxFilingDAO.updateFilingStatus(fid, "Approved");
                System.out.println(ok ? "Approved." : "Filing not found.");

            } else if (choice == 5) {
                return;
            }
        }
    }
}
