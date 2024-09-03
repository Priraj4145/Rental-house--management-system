import java.util.ArrayList;
import java.util.Scanner;

class Property {
    private String address;
    private double monthlyRent;
    private double electricityBill;
    private double maintenanceCharges;
    private Tenant tenant;
    private boolean isOccupied;

    public Property(String address, double monthlyRent) {
        this.address = address;
        this.monthlyRent = monthlyRent;
        this.electricityBill = 0.0;
        this.maintenanceCharges = 0.0;
        this.isOccupied = false;
    }

    public void assignTenant(Tenant tenant) {
        this.tenant = tenant;
        this.isOccupied = true;
    }

    public void collectRent() {
        if (tenant != null) {
            double totalAmountDue = monthlyRent + electricityBill + maintenanceCharges;
            tenant.payRent(totalAmountDue);
            clearDues();
        }
    }

    public void recordElectricityUsage(double usage, double ratePerUnit) {
        this.electricityBill = usage * ratePerUnit;
    }

    public void setMaintenanceCharges(double charges) {
        this.maintenanceCharges = charges;
    }

    public void endLease() {
        if (tenant != null) {
            tenant.clearOutstandingDues();
            tenant = null;
            isOccupied = false;
        }
    }

    private void clearDues() {
        this.electricityBill = 0.0;
        this.maintenanceCharges = 0.0;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public double getMonthlyRent() {
        return monthlyRent;
    }

    public String getAddress() {
        return address;
    }
}

class Tenant {
    private String name;
    private boolean paymentStatus;

    public Tenant(String name) {
        this.name = name;
        this.paymentStatus = false;
    }

    public void payRent(double amount) {
        System.out.println(name + " has paid a total amount of: Rs " + amount);
        paymentStatus = true;
    }

    public void clearOutstandingDues() {
        paymentStatus = false;
        System.out.println(name + " has cleared all outstanding dues.");
    }

    public boolean getPaymentStatus() {
        return paymentStatus;
    }

    public String getName() {
        return name;
    }
}

class Owner {
    private String name;
    private ArrayList<Property> properties;

    public Owner(String name) {
        this.name = name;
        this.properties = new ArrayList<>();
    }

    public void addProperty(Property property) {
        properties.add(property);
    }

    public void generateFinancialReport() {
        double totalIncome = 0.0;
        double totalExpenses = 0.0;

        for (Property property : properties) {
            if (property.isOccupied()) {
                totalIncome += property.getMonthlyRent();
                // Assuming maintenance charges are considered expenses
                totalExpenses += property.getMonthlyRent() - property.getMonthlyRent();
            }
        }

        double netIncome = totalIncome - totalExpenses;
        System.out.println("Financial Report for " + name);
        System.out.println("Total Income: Rs " + totalIncome);
        System.out.println("Total Expenses: Rs " + totalExpenses);
        System.out.println("Net Income: Rs " + netIncome);
    }

    public void displayMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nRental House Management System");
            System.out.println("1. Add Property");
            System.out.println("2. Assign Tenant");
            System.out.println("3. Collect Rent");
            System.out.println("4. Record Electricity Usage");
            System.out.println("5. Set Maintenance Charges");
            System.out.println("6. End Lease");
            System.out.println("7. Generate Financial Report");
            System.out.println("8. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter property address: ");
                    String address = scanner.nextLine();
                    System.out.print("Enter monthly rent: ");
                    double rent = scanner.nextDouble();
                    addProperty(new Property(address, rent));
                    System.out.println("Property added.");
                    break;
                case 2:
                    System.out.print("Enter property address to assign tenant: ");
                    address = scanner.nextLine();
                    Property property = findPropertyByAddress(address);
                    if (property != null && !property.isOccupied()) {
                        System.out.print("Enter tenant name: ");
                        String tenantName = scanner.nextLine();
                        property.assignTenant(new Tenant(tenantName));
                        System.out.println("Tenant assigned.");
                    } else {
                        System.out.println("Property not found or already occupied.");
                    }
                    break;
                case 3:
                    System.out.print("Enter property address to collect rent: ");
                    address = scanner.nextLine();
                    property = findPropertyByAddress(address);
                    if (property != null && property.isOccupied()) {
                        property.collectRent();
                    } else {
                        System.out.println("Property not found or no tenant assigned.");
                    }
                    break;
                case 4:
                    System.out.print("Enter property address to record electricity usage: ");
                    address = scanner.nextLine();
                    property = findPropertyByAddress(address);
                    if (property != null && property.isOccupied()) {
                        System.out.print("Enter electricity usage (in units): ");
                        double usage = scanner.nextDouble();
                        System.out.print("Enter rate per unit: ");
                        double rate = scanner.nextDouble();
                        property.recordElectricityUsage(usage, rate);
                        System.out.println("Electricity usage recorded.");
                    } else {
                        System.out.println("Property not found or no tenant assigned.");
                    }
                    break;
                case 5:
                    System.out.print("Enter property address to set maintenance charges: ");
                    address = scanner.nextLine();
                    property = findPropertyByAddress(address);
                    if (property != null) {
                        System.out.print("Enter maintenance charges: ");
                        double charges = scanner.nextDouble();
                        property.setMaintenanceCharges(charges);
                        System.out.println("Maintenance charges set.");
                    } else {
                        System.out.println("Property not found.");
                    }
                    break;
                case 6:
                    System.out.print("Enter property address to end lease: ");
                    address = scanner.nextLine();
                    property = findPropertyByAddress(address);
                    if (property != null && property.isOccupied()) {
                        property.endLease();
                        System.out.println("Lease ended, property is now vacant.");
                    } else {
                        System.out.println("Property not found or no tenant assigned.");
                    }
                    break;
                case 7:
                    generateFinancialReport();
                    break;
                case 8:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private Property findPropertyByAddress(String address) {
        for (Property property : properties) {
            if (property.getAddress().equalsIgnoreCase(address)) {
                return property;
            }
        }
        return null;
    }
}

public class Oops_concepts {
    public static void main(String[] args) {
        Owner owner = new Owner("John Doe");

        // Start the menu-driven interaction
        owner.displayMenu();
    }
}
