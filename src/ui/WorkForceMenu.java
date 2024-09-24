package ui;

import domain.entities.Component;
import domain.entities.Labor;
import domain.entities.Project;
import org.postgresql.largeobject.LargeObject;
import service.ComponentService;
import utils.CheckInput;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class WorkForceMenu {

    private final ComponentService componentService;
    private final Scanner scanner;

    public WorkForceMenu(ComponentService componentService) {
        this.componentService = componentService;
        this.scanner = new Scanner(System.in);
    }

    public Labor addWorkForce(Project project) {
        String continueChoice;
        Labor workForce = null;

        do {
            System.out.println("\n" + drawTableHeader("🔨 Add Workforce 🔨"));

            System.out.print("👷 Enter the name of the workforce: ");
            String name = scanner.nextLine();

            double vatRate = CheckInput.readDouble("📊 Enter the VAT rate of the workforce: ");
            scanner.nextLine();

            double hourlyRate = CheckInput.readDouble("💰 Enter the hourly rate for this labor (€/h): ");
            scanner.nextLine();

            double hoursWorked = CheckInput.readDouble("⏱️ Enter the number of hours worked: ");
            scanner.nextLine();

            double productivityFactor = CheckInput.readDouble("🔧 Enter the productivity factor (1.0 = standard, > 1.0 = high productivity): ");
            scanner.nextLine();

            // Create and save the component
            Component labor=new Labor(name,"LABOR",vatRate,project,hourlyRate,hoursWorked,productivityFactor);

            Component savedComponent = componentService.create(labor);

            System.out.println("\n✅ Workforce added successfully!\n");
            System.out.println(drawWorkforceTable((Labor) savedComponent));

            System.out.print("👉 Would you like to add another workforce? (y/n): ");
            continueChoice = scanner.nextLine().trim().toLowerCase();

        } while (continueChoice.equals("y"));

        return workForce;
    }


    // Helper method to draw a simple table header for better presentation
    private String drawTableHeader(String title) {
        return "+---------------------------------------------+\n" +
                "| " + String.format("%-43s", title) + "|\n" +
                "+---------------------------------------------+";
    }

    // Helper method to draw a row inside the table
    private String drawTableRow(String content) {
        return "| " + String.format("%-43s", content) + " |";
    }

    // Helper method to close the table after rows
    private String drawTableFooter() {
        return "+---------------------------------------------+";
    }

    // Method to display workforce information in a table format
    private String drawWorkforceTable(Labor labor) {
        return drawTableHeader("👷 Workforce Information") + "\n" +
                drawTableRow("👷 Name: " + labor.getName()) + "\n" +
                drawTableRow("📊 VAT Rate: " + labor.getVatRate() + "%") + "\n" +
                drawTableRow("💰 Hourly Rate: €" + labor.getHourlyRate()) + "\n" +
                drawTableRow("⏱️ Hours Worked: " + labor.getWorkHours()) + "\n" +
                drawTableRow("🔧 Productivity Factor: " + labor.getWorkerProductivity()) + "\n" +
                drawTableFooter();
    }

    public void update(Labor labor) {
        this.componentService.update(labor);
        System.out.println("🔄 Workforce updated successfully.");
    }

    public void delete(Labor labor) {
        this.componentService.delete(labor);
        System.out.println("🗑️ Workforce deleted successfully.");
    }

    public void findById(Labor labor) {
        Component foundWorkForce = this.componentService.findById(labor);
        if(foundWorkForce!=null) System.out.println(drawWorkforceTable((Labor) foundWorkForce));
        else System.out.println("❌ Workforce not found.");
    }

    public void findAll() {
        List<Component> workForces = this.componentService.findAllType("LABOR");
        if (!workForces.isEmpty()) {
            System.out.println("\n📋 **All Workforce Entries** 📋");
            for (Component workForce : workForces) {
                System.out.println(drawWorkforceTable((Labor) workForce));
            }
        } else {
            System.out.println("No workforce entries available.");
        }
    }
}
