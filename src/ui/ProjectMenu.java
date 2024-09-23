package ui;

import domain.entities.Client;
import domain.entities.Material;
import domain.entities.Project;
import domain.enums.ProjectStatus;
import service.ProjectService;

import java.util.List;
import java.util.Scanner;

public class ProjectMenu {

    private final ProjectService projectService;
    private final ClientMenu clientMenu;
    private final Scanner scanner;
    private Client selectedClient;
    private final MaterialMenu materialMenu;
    private final WorkForceMenu workForceMenu;

    public ProjectMenu(ProjectService projectService, ClientMenu clientMenu, MaterialMenu materialMenu, WorkForceMenu workForceMenu) {
        this.projectService = projectService;
        this.clientMenu = clientMenu;
        this.materialMenu = materialMenu;
        this.workForceMenu = workForceMenu;
        this.scanner = new Scanner(System.in);
    }

    public void addOrSearchClientMenu() {
        while (true) {
            System.out.println("\n" + drawTableHeader("🌟  Client Management Menu 🌟"));
            System.out.println(drawTableRow("👤 1. Search for an Existing Client"));
            System.out.println(drawTableRow("🆕 2. Add a New Client"));
            System.out.println(drawTableRow("🚪 3. Exit"));
            System.out.println(drawTableFooter());
            System.out.print("\n👉 Enter your choice (1-3): ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    searchClient();
                    break;
                case 2:
                    addNewClient();
                    break;
                case 3:
                    System.out.println("👋 Exiting...");
                    return;
                default:
                    System.out.println("⚠️ Invalid choice. Please try again.");
            }
        }
    }

    private void searchClient() {
        System.out.println("\n" + drawTableHeader("🔍 Search for an Existing Client 🔍"));
        System.out.print("👤 Enter the name of the client: ");
        String name = scanner.nextLine();
        Client optionalClient = clientMenu.searchByName(name);

        if (optionalClient != null) {
            selectedClient = optionalClient;
            System.out.println("🎉 Client found! 🎉\n");
            System.out.println(drawClientTable(selectedClient));
            addProject();
        } else {
            System.out.println("❌ Client not found.");
            selectedClient = null;
        }
    }

    private void addNewClient() {
        System.out.println("\n" + drawTableHeader("🆕 Add a New Client ✏️"));
        selectedClient = clientMenu.addNewClient();

        if (selectedClient != null) {
            System.out.println("🎉 Client added successfully!\n");
            System.out.println(drawClientTable(selectedClient));
            addProject();
        } else {
            System.out.println("❌ Failed to add new client.");
        }
    }

    private void addProject() {
        try {
            System.out.println("\n" + drawTableHeader("🔨 Add a New Project 🔨"));
            System.out.print("🏗️ Enter the name of the project: ");
            String name = scanner.nextLine();
            System.out.print("📊 Enter project Profit margin : ");
            double profitMargin = scanner.nextDouble();
            scanner.nextLine();

            Project project = new Project(0, name, profitMargin, 0,"INPROGRESS", selectedClient);
            Project savedProject = projectService.save(project);
            System.out.println(savedProject);
            materialMenu.addMaterial(savedProject);
            workForceMenu.addWorkForce(savedProject);

            System.out.println("✅ Project added successfully!\n");
            System.out.println(drawProjectTable(savedProject));
        } catch (Exception e) {
            System.out.println("⚠️ An error occurred while adding the project: " + e.getMessage());
        }
    }

    public void findAll() {
        System.out.println("\n📜 **All Projects** 📜");
        List<Project> listProjects= projectService.findAll();
        listProjects.forEach(project -> {
            System.out.println(drawProjectTable(project));
        });
    }

    // Method to draw a simple table header for better presentation
    private String drawTableHeader(String title) {
        return "+---------------------------------------------+\n" +
                "| " + String.format("%-43s", title) + "|\n" +
                "+---------------------------------------------+";
    }

    // Method to draw a row inside the table
    private String drawTableRow(String content) {
        return "| " + String.format("%-43s", content) + " |";
    }

    // Method to close the table after rows
    private String drawTableFooter() {
        return "+---------------------------------------------+";
    }

    // Method to display client information in a table format
    private String drawClientTable(Client client) {
        return drawTableHeader("👤 Client Information") + "\n" +
                drawTableRow("📛 Name: " + client.getName()) + "\n" +
                drawTableRow("🏠 Address: " + client.getaddress()) + "\n" +
                drawTableRow("📞 Phone: " + client.getphone()) + "\n" +
                drawTableRow("🛠️ Professional: " + (client.isProfessional() ? "Yes" : "No")) + "\n" +
                drawTableFooter();
    }

    // Method to display project information in a table format
    private String drawProjectTable(Project project) {
        return drawTableHeader("🏗️ Project Information") + "\n" +
                drawTableRow("🏗️ Name: " + project.getprojectname()) + "\n" +
                drawTableRow("📊 Status: " + project.getStatus()) + "\n" +
                drawTableRow("👤 Client: " + project.getClient().getName()) + "\n" +
                drawTableFooter();
    }
}
