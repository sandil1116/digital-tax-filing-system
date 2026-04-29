package model;

public class Admin {
    private final String adminId;
    private final String username;

    public Admin(String adminId, String username) {
        this.adminId = adminId;
        this.username = username;
    }

    public String getAdminId() { return adminId; }
    public String getUsername() { return username; }

    public void approveFiling(TaxFiling filing) {
        filing.setStatus("Approved");
    }
}
