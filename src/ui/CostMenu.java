package ui;

import domain.entities.Labor;
import domain.entities.Material;
import domain.entities.Project;
import domain.entities.Quote;
import domain.enums.ProjectStatus;
import exceptions.QuotesNotFoundException;
import service.ComponentService;
import service.ProjectService;
import service.QuoteService;
import utils.CheckInput;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class CostMenu {

    private final ProjectService projectService;
    private final ComponentService componentService;
    private final QuoteService devisService;
    private final DevisMenu devisMenu;

    private static Scanner scanner;
    private final double discount = 0.7;


    public CostMenu(ProjectService projectService , ComponentService componentService, QuoteService devisService,DevisMenu devisMenu){
        this.projectService=projectService;
        this.componentService=componentService;
        this.devisService=devisService;
        this.devisMenu=devisMenu;
        scanner = new Scanner(System.in);
    }

    private static boolean getYesNoInput(String prompt) {
        System.out.print(prompt);
        String input = scanner.next().trim().toLowerCase();
        scanner.nextLine();
        return input.equals("y") || input.equals("yes");
    }

    public void save() {
        System.out.println("--- Total Cost Calculation ---");

        int projectId = CheckInput.readInt("Enter project ID: ");

        Project project = projectService.findById(new Project(projectId)).orElseThrow(() ->
                new RuntimeException("Project not found"));

        List<Material> materials = componentService.findAllMaterialsByProject(projectId);
        List<Labor> Labors = componentService.findAllLaborsByProject(projectId);

        double totalMaterialBeforeVat = 0;
        double totalMaterialAfterVat = 0;

        for (Material material : materials) {
            double materialCostBeforeVat = componentService.calculateMaterialBeforeVatRate(material);
            double materialCostAfterVat = componentService.calculateMaterialAfterVatRate(material);

            totalMaterialBeforeVat += materialCostBeforeVat;
            totalMaterialAfterVat += materialCostAfterVat;
        }

        double totalLaborsBeforeVat = 0;
        double totalLaborsAfterVat = 0;

        for (Labor labor : Labors) {
            double laborCostBeforeVat = componentService.calculateLaborBeforeVatRate(labor);
            double laborCostAfterVat = componentService.calculateLaborAfterVatRate(labor);

            totalLaborsBeforeVat += laborCostBeforeVat;
            totalLaborsAfterVat += laborCostAfterVat;
        }

        double totalCostBeforeMargin = totalMaterialBeforeVat + totalLaborsBeforeVat;
        double totalCostAfterVat = totalMaterialAfterVat + totalLaborsAfterVat;

        double totalCost = totalCostAfterVat;
        double marginRate = 0.0;
        if (getYesNoInput("Do you want to apply a profit margin to the project? (y/n): ")) {
            marginRate = CheckInput.readDouble("Enter profit margin percentage: ");
            scanner.nextLine();
            project.setprofitMargin(marginRate);
            totalCost=totalCost+(totalCost*marginRate/100);
        }

        projectService.updateMarginAndTotalCost_Project(projectId, marginRate, totalCost);


        System.out.println("\n--- Calculation Result ---");
        System.out.println("Project Name: " + project.getprojectname());
        System.out.println("Client: " + project.getClient().getName());
        System.out.println("Address: " + project.getClient().getaddress());
        System.out.println("--- Cost Details ---");
        System.out.println("Materials Cost Before VAT: " + String.format("%.2f", totalMaterialBeforeVat) + " €");
        System.out.println("Materials Cost After VAT: " + String.format("%.2f", totalMaterialAfterVat) + " €");
        System.out.println("Labors Cost Before VAT: " + String.format("%.2f", totalLaborsBeforeVat) + " €");
        System.out.println("Labors Cost After VAT: " + String.format("%.2f", totalLaborsAfterVat) + " €");
        System.out.println("Total Cost Before Margin: " + String.format("%.2f", totalCostBeforeMargin) + " €");


        if (project.getClient().isProfessional()) {
            System.out.println("\n--- Professional Client Discount Applied ---");
            totalCost *= discount;
            System.out.println("Discounted Total Cost: " + String.format("%.2f", totalCost) + " €");
        }

        LocalDate issueDate = CheckInput.readDate("\nEnter issue date (yyyy-MM-dd): ");

        LocalDate validatedDate = CheckInput.readDate("\n Enter validated date (yyyy-MM-dd): ");

        while(validatedDate.isBefore(issueDate)){
            validatedDate = CheckInput.readDate("\nEnter the validated date (yyyy-MM-dd): After = "+ issueDate);
        }
        Quote devis = new Quote(0, totalCost, issueDate, validatedDate, false, project);
        devisService.save(devis);

        System.out.print("Do you want to accept the devis? (Yes/No): ");
        String choice = scanner.nextLine().trim().toLowerCase();

        switch (choice) {
            case "yes":
            case "y":
                devisService.updateDevisStatus(devis.getId());
                projectService.updateStatus(projectId, ProjectStatus.COMPLETED.name());
                System.out.println("Devis accepted. Project marked as FINISHED.");
                break;
            case "no":
            case "n":
                projectService.updateStatus(projectId, ProjectStatus.CANCELLED.name());
                System.out.println("Devis rejected. Project marked as CANCELLED.");
                break;
            default:
                System.out.println("Invalid choice. Please enter 'Yes' or 'No'.");
        }
        try {
            devisMenu.findDevisByProject(projectId);
        } catch (QuotesNotFoundException devisNotFoundException) {
            System.out.println(devisNotFoundException.getMessage());
        }
    }



}
