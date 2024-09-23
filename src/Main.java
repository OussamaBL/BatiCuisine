import config.connexion;
import domain.entities.Component;
import domain.entities.Labor;
import repository.ClientRepository;
import repository.ComponentRepository;
import repository.ProjectRepository;
import repository.QuoteRepository;
import service.ClientService;
import service.ComponentService;
import service.ProjectService;
import service.QuoteService;
import ui.*;

import java.sql.Connection;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {

        String BLUE = "\u001B[34m";
        String RESET = "\u001B[0m";

        System.out.println(BLUE + "                                                                                           ");
        System.out.println("  ____        _  _____  ___    ____ _   _ ___ ____ ___ _   _ _____ ");
        System.out.println(" | __ )  __ _| ||_   _|_ _|  / ___| | | |_ _/ ___|_ _| \\ | | ____|");
        System.out.println(" |  _ \\ / _` | __|| |  | |  | |   | | | || |\\___ \\| ||  \\| |  _|  ");
        System.out.println(" | |_) | (_| | |_ | |  | |  | |___| |_| || | ___) | || |\\  | |___ ");
        System.out.println(" |____/ \\__,_|\\__||_| |___|  \\____|\\___/|___|____/___|_| \\_|_____|");
        System.out.println("                                                                   " + RESET);



        ClientRepository clientRepository = new ClientRepository();
        ComponentRepository componentRepository = new ComponentRepository();
        ProjectRepository projectRepository = new ProjectRepository();


        ProjectService projectService = new ProjectService();
        ClientService clientService = new ClientService(clientRepository);

        ClientMenu clientMenu = new ClientMenu(clientService);

        ComponentService componentService = new ComponentService();
        MaterialMenu materialMenu = new MaterialMenu(componentService);
        WorkForceMenu workForceMenu = new WorkForceMenu(componentService);
        ProjectMenu projectMenu = new ProjectMenu(projectService,clientMenu,materialMenu, workForceMenu);

        QuoteRepository devisRepository = new QuoteRepository();
        QuoteService devisService = new QuoteService(devisRepository);
        DevisMenu devisMenu = new DevisMenu(devisService,projectService);

       // CostMenu costMenu = new CostMenu(projectRepository , componentRepository, materialService , workForceService , devisService, devisMenu);
        CostMenu costMenu = new CostMenu(projectService , componentService, devisService, devisMenu);

        PrincipalMenu principalMenu = new PrincipalMenu(projectMenu,devisMenu,costMenu);
        principalMenu.Menu();


    }
}