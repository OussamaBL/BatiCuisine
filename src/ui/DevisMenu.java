package ui;

import domain.entities.Project;
import domain.entities.Quote;
import exceptions.ProjectsNotFoundException;
import exceptions.QuotesNotFoundException;
import service.ProjectService;
import service.QuoteService;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class DevisMenu {
    private final Scanner scanner;
    private QuoteService quoteService;
    private ProjectService projectService;

    public DevisMenu(QuoteService quoteService, ProjectService projectService) {
        this.quoteService = quoteService;
        this.projectService = projectService;
        this.scanner = new Scanner(System.in);
    }

    public void displayMenu() {
        while (true) {
            System.out.println("\n Devis Management Menu");
            System.out.println("1. Save Devis");
            System.out.println("2. Delete Devis");
            System.out.println("3. Find All Devis");
            System.out.println("4. Find Devis by ID");
            System.out.println("5. Update Devis");
            System.out.println("6. Accept  Devis");
            System.out.println("7. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    saveDevis();
                    break;
                case 2:
                    delete();
                    break;
                case 3:
                    findAll();
                    break;
                case 4:
                    findById();
                    break;
                case 5:
                    update();
                    break;
                case 6:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }


    public void save(Quote quote){
        quoteService.save(quote);
    }


    private void saveDevis() {
        System.out.print("Enter Project Name: ");
        String projectName = scanner.nextLine();
        Project project = null;
        try {
            project = projectService.findProjectByName(projectName);
        } catch (ProjectsNotFoundException projectNotFoundException) {
            System.out.println(projectNotFoundException.getMessage());
            return;
        }

        System.out.print("Enter estimated amount: ");
        double estimatedAmount = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Enter issue date (yyyy-MM-dd): ");
        String issueDate = scanner.nextLine();
        LocalDate issueDateParse = LocalDate.parse(issueDate, DateTimeFormatter.ofPattern("yyyy/MM/dd"));



        System.out.print("Enter validated date (yyyy-MM-dd): ");
        String validatedDate = scanner.nextLine();
        LocalDate validatedDateParse = LocalDate.parse(validatedDate, DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        Quote quote = new Quote(0, estimatedAmount, issueDateParse, validatedDateParse, false, project);
        quoteService.save(quote);
    }

    private void delete() {
        System.out.print("Enter Devis ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        quoteService.delete(new Quote(id));
    }

    public void findAll() {
        List<Quote> devisList = quoteService.findAll();
        System.out.printf("+--------------+-------------------+--------------+--------------+-------------+--------------------+--------------------+%n");
        System.out.printf("| %-12s | %-17s | %-12s | %-12s | %-11s | %-18s | %-18s |%n",
                "Devis ID", "Estimated Amount", "Issue Date", "Validated Date", "Is Accepted", "Project Name", "Client Name");
        System.out.printf("+--------------+-------------------+--------------+--------------+-------------+--------------------+--------------------+%n");

        devisList.forEach(devis -> {
            System.out.printf("| %-12d | %-17.2f | %-12s | %-12s | %-11b | %-18s | %-18s |%n",
                    devis.getId(),
                    devis.getEstimatedAmount(),
                    devis.getIssueDate(),
                    devis.getDateValidity(),
                    devis.isAccepted(),
                    devis.getProject().getprojectname(),
                    devis.getProject().getClient().getName()
            );
        });

        System.out.printf("+--------------+-------------------+--------------+--------------+-------------+--------------------+--------------------+%n");
    }


    private void findById() {
        System.out.print("Enter Devis ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        Optional<Quote> devis = quoteService.findById(new Quote(id));

        devis.ifPresent(devis1 -> {
            System.out.printf("+--------------+-------------------+--------------+--------------+-------------+--------------------+--------------------+%n");
            System.out.printf("| %-12s | %-17s | %-12s | %-12s | %-11s | %-18s | %-18s |%n",
                    "Devis ID", "Estimated Amount", "Issue Date", "Validated Date", "Is Accepted", "Project Name", "Client Name");
            System.out.printf("+--------------+-------------------+--------------+--------------+-------------+--------------------+--------------------+%n");

            System.out.printf("| %-12d | %-17.2f | %-12s | %-12s | %-11b | %-18s | %-18s |%n",
                    devis1.getId(),
                    devis1.getEstimatedAmount(),
                    devis1.getIssueDate(),
                    devis1.getDateValidity(),
                    devis1.isAccepted(),
                    devis1.getProject().getprojectname(),
                    devis1.getProject().getClient().getName()
            );
            System.out.printf("+--------------+-------------------+--------------+--------------+-------------+--------------------+--------------------+%n");
        });
    }


    public void update() {
        System.out.print("Enter Devis ID: ");
        int id = scanner.nextInt();
        System.out.print("Enter estimatedAmount");
        double estimatedAmount = scanner.nextDouble();
        System.out.print("Enter issueDate (yyyy-MM-dd) : ");
        String issueDate = scanner.nextLine();
        LocalDate issueDateParse = LocalDate.parse(issueDate, DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        System.out.print("Enter validated date (yyyy-MM-dd): ");
        String validatedDate = scanner.nextLine();
        LocalDate validatedDateParse = LocalDate.parse(validatedDate, DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        System.out.print("Enter Project Name: ");
        String projectName = scanner.nextLine();
        Project project = null;
        try {
            project = projectService.findProjectByName(projectName);
        } catch (ProjectsNotFoundException projectNotFoundException) {
            System.out.println(projectNotFoundException.getMessage());
            return;
        }
        Quote devis = new Quote(id, estimatedAmount, issueDateParse, validatedDateParse, false, project);
        quoteService.update(devis);
    }

    public void findDevisByProject(int projectId) {
        Optional<Quote> devisOptional = this.quoteService.findQuoteByProject(projectId);

        devisOptional.ifPresent(devis1 -> {
            System.out.printf("+--------------+-------------------+--------------+--------------+-------------+--------------------+--------------------+%n");
            System.out.printf("| %-12s | %-17s | %-12s | %-12s | %-11s | %-18s | %-18s |%n",
                    "Devis ID", "Estimated Amount", "Issue Date", "Validated Date", "Is Accepted", "Project Name", "Client Name");
            System.out.printf("+--------------+-------------------+--------------+--------------+-------------+--------------------+--------------------+%n");

            System.out.printf("| %-12d | %-17.2f | %-12s | %-12s | %-11b | %-18s | %-18s |%n",
                    devis1.getId(),
                    devis1.getEstimatedAmount(),
                    devis1.getIssueDate(),
                    devis1.getDateValidity() != null ? devis1.getDateValidity() : "N/A",
                    devis1.isAccepted(),
                    devis1.getProject().getprojectname() != null ? devis1.getProject().getprojectname() : "N/A",
                    devis1.getProject().getClient().getName() != null ? devis1.getProject().getClient().getName() : "N/A"
            );

            System.out.printf("+--------------+-------------------+--------------+--------------+-------------+--------------------+--------------------+%n");
        });

        devisOptional.orElseThrow(() -> new QuotesNotFoundException("Devis not found!"));
    }
}
