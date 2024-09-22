package repository;


import config.connexion;
import domain.entities.Client;
import repository.interfaces.ClientInterface;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientRepository implements ClientInterface<Client> {

    private Connection cnx;
    public ClientRepository(){
        this.cnx= connexion.getInstance();
    }
    @Override
    public Client save(Client client) {
        String query = "INSERT INTO clients (name, address, phone, isProfessional) VALUES (?, ?, ?, ?) RETURNING id";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(query)) {
            preparedStatement.setString(1, client.getName());
            preparedStatement.setString(2, client.getaddress());
            preparedStatement.setString(3, client.getphone());
            preparedStatement.setBoolean(4, client.isProfessional());

            try (ResultSet generatedKeys = preparedStatement.executeQuery()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    client.setId(id);
                    System.out.println("Client with name " + client.getName() + " was successfully added");
                } else {
                    throw new SQLException("Creating client failed");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return client;
    }

    @Override
    public Optional<Client> findById(Client client) {

        try {
            String query = "SELECT * FROM clients WHERE id = ?";
            var preparedStatement = cnx.prepareStatement(query);
            preparedStatement.setInt(1, client.getId());
            var rs = preparedStatement.executeQuery();
            if (rs.next()) {
                Client c=new Client(rs.getInt("id"),rs.getString("name"),rs.getString("address"),rs.getString("phone"),rs.getBoolean("isprofessional"));
                return Optional.of(c);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Client> findAll() {
        String query = "SELECT * FROM clients";
        List<Client> clients = new ArrayList<>();
        try (PreparedStatement preparedStatement = cnx.prepareStatement(query)) {
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Client client = new Client(rs.getInt("id"),rs.getString("name"),rs.getString("address"),rs.getString("phone"),rs.getBoolean("isProfessional"));
                clients.add(client);
            }
            return clients;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return clients;
    }

    @Override
    public Client update(Client client) {
        String sql = "UPDATE clients SET name = ?, address = ?, phone = ?, isProfessional = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(sql)) {
            preparedStatement.setString(1, client.getName());
            preparedStatement.setString(2, client.getaddress());
            preparedStatement.setString(3, client.getphone());
            preparedStatement.setBoolean(4, client.isProfessional());
            preparedStatement.setInt(5, client.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return client;
    }

    @Override
    public boolean delete(Client client) {
        String query = "DELETE FROM clients WHERE id = ?";

        try (PreparedStatement preparedStatement = cnx.prepareStatement(query)) {
            preparedStatement.setInt(1, client.getId());
            int result = preparedStatement.executeUpdate();
            if (result == 1) return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public Optional<Client> findByName(String name) {
        String sql = "SELECT * FROM clients WHERE name = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(sql)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToClient(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private Client mapResultSetToClient(ResultSet rs) throws SQLException {
        Client client = new Client();
        client.setId(rs.getInt("id"));
        client.setName(rs.getString("name"));
        client.setaddress(rs.getString("address"));
        client.setphone(rs.getString("phone"));
        client.setProfessional(rs.getBoolean("isProfessional"));
        return client;
    }
}
