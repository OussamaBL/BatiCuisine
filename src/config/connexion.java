package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class connexion {
    private String pilote;
    private String url;
    private String login;
    private String password;
    private static Connection myConnexion;

    public connexion() {
        this.pilote = "org.postgresql.Driver";
        this.url = "jdbc:postgresql://localhost:5432/BatiCuisine";
        this.login = "BatiCuisine";
        this.password = "";
    }

    public connexion(String pilote, String url, String login, String password) {
        this.pilote = pilote;
        this.url = url;
        this.login = login;
        this.password = password;
    }

    public static Connection getInstance() {
        if (myConnexion == null) {
            try {
                connexion cnx=new connexion();
                Class.forName(cnx.getPilote());
                System.out.println("Chargement de Pilote OK ....");
                myConnexion = DriverManager.getConnection(cnx.getURL(), cnx.getLogin(), cnx.getPassword());
                System.out.println("Connexion établie : Go ...");
            } catch (ClassNotFoundException ex) {
                System.err.println("Problème de chargement du Pilote !!!!");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
        return myConnexion;
    }

    public String getPilote() {
        return pilote;
    }

    public void setPilote(String pilote) {
        pilote = pilote;
    }

    public String getURL() {
        return url;
    }

    public void setURL(String url) {
        this.url = url;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        password = password;
    }

    public Connection getMyConnexion() {
        return myConnexion;
    }

    public void setMyConnexion(Connection myConnexion) {
        myConnexion = myConnexion;
    }

}
