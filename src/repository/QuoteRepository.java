package repository;

import config.connexion;
import domain.entities.Client;
import domain.entities.Project;
import domain.entities.Quote;
import repository.interfaces.QuoteInterface;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QuoteRepository implements QuoteInterface<Quote> {
    private Connection cnx;
    public QuoteRepository(){
        cnx= connexion.getInstance();
    }
    @Override
    public Quote save(Quote devis) {
        String query = "INSERT INTO quotes (estimated_amount, issuedate,date_validity, project_id) VALUES (?,?, ?, ?) RETURNING id";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(query)) {
            preparedStatement.setDouble(1, devis.getEstimatedAmount());
            preparedStatement.setDate(2, Date.valueOf(devis.getIssueDate()));
            preparedStatement.setDate(3, Date.valueOf(devis.getDateValidity()));
            preparedStatement.setInt(4, devis.getProject().getId());

            try (ResultSet generatedKeys = preparedStatement.executeQuery()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    devis.setId(id);
                    System.out.println("Quote was successfully saved with ID " + id);
                } else {
                    throw new SQLException("Creating quote failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return devis;
    }

    @Override
    public Optional<Quote> findById(Quote devis) {
        String query = "SELECT q.id, q.estimatedAmount, q.issueDate, q.dateValidity, q.isAccepted, q.project_id, " +
                "p.projectName, p.profitMargin, p.totalCost, p.status, " +
                "c.id AS client_id, c.name, c.address, c.phone, c.isProfessional " +
                "FROM quotes q " +
                "JOIN projects p ON q.project_id = p.id " +
                "JOIN clients c ON p.client_id = c.id " +
                "WHERE q.id = ?";

        try (PreparedStatement preparedStatement = cnx.prepareStatement(query)) {
            preparedStatement.setInt(1, devis.getId());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Client client = new Client(
                            resultSet.getInt("client_id"),
                            resultSet.getString("name"),
                            resultSet.getString("address"),
                            resultSet.getString("phone"),
                            resultSet.getBoolean("isProfessional")
                    );

                    Project project = new Project(
                            resultSet.getInt("project_id"),
                            resultSet.getString("projectName"),
                            resultSet.getDouble("profitMargin"),
                            resultSet.getDouble("totalCost"),
                            resultSet.getString("status"),
                            client
                    );

                    Quote foundDevis = new Quote(
                            resultSet.getInt("id"),
                            resultSet.getDouble("estimatedAmount"),
                            resultSet.getDate("issueDate").toLocalDate(),
                            resultSet.getDate("dateValidity").toLocalDate(),
                            resultSet.getBoolean("isAccepted"),
                            project
                    );

                    return Optional.of(foundDevis);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Quote> findAll() {
        String query = "SELECT q.id, q.estimatedAmount, q.issueDate, q.dateValidity, q.isAccepted, q.project_id, " +
                "p.projectName, p.profitMargin, p.totalCost, p.status, " +
                "c.id AS client_id, c.name, c.address, c.phone, c.isProfessional " +
                "FROM quotes q " +
                "JOIN projects p ON q.project_id = p.id " +
                "JOIN clients c ON p.client_id = c.id";
        List<Quote> devisList = new ArrayList<>();
        try (PreparedStatement preparedStatement = cnx.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Client client = new Client(
                        resultSet.getInt("client_id"),
                        resultSet.getString("name"),
                        resultSet.getString("address"),
                        resultSet.getString("phone"),
                        resultSet.getBoolean("isProfessional")
                );

                Project project = new Project(
                        resultSet.getInt("project_id"),
                        resultSet.getString("projectName"),
                        resultSet.getDouble("profitMargin"),
                        resultSet.getDouble("totalCost"),
                        resultSet.getString("status"),
                        client
                );

                Quote quote = new Quote(
                        resultSet.getInt("id"),
                        resultSet.getDouble("estimatedAmount"),
                        resultSet.getDate("issueDate").toLocalDate(),
                        resultSet.getDate("dateValidity").toLocalDate(),
                        resultSet.getBoolean("isAccepted"),
                        project
                );

                devisList.add(quote);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return devisList;
    }

    @Override
    public Quote update(Quote devis) {
        String query = "UPDATE quotes SET estimatedAmount = ?, issueDate = ?, dateValidity = ? ,isAccepted = ?, project_id = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(query)) {
            preparedStatement.setDouble(1, devis.getEstimatedAmount());
            preparedStatement.setDate(2, Date.valueOf(devis.getIssueDate()));
            preparedStatement.setDate(3, Date.valueOf(devis.getDateValidity()));
            preparedStatement.setBoolean(4, devis.isAccepted());
            preparedStatement.setInt(5, devis.getProject().getId());
            preparedStatement.setInt(6, devis.getId());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 1) {
                System.out.println("Quote updated successfully");
            } else {
                throw new SQLException("Update failed, no rows affected.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return devis;
    }

    @Override
    public boolean delete(Quote devis) {
        String query = "DELETE FROM quotes WHERE id = ?";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(query)) {
            preparedStatement.setInt(1, devis.getId());
            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows == 1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public Optional<Quote> findDevisByProject(int projectId) {
        String sql = "SELECT q.id,\n" +
                "       q.estimated_amount,\n" +
                "       q.issuedate,\n" +
                "       q.date_validity,\n" +
                "       q.is_accepted,\n" +
                "       q.project_id AS project_id,\n" +
                "       p.id AS prId,\n" +
                "       p.projectname,\n" +
                "       p.client_id AS client_id,\n" +
                "       c.name AS clientName\n" +
                "FROM quotes q\n" +
                "JOIN projects p ON p.id = q.project_id\n" +
                "JOIN clients c ON c.id = p.client_id\n" +
                "WHERE q.project_id = ?\n";

        try (PreparedStatement preparedStatement = cnx.prepareStatement(sql)) {
            preparedStatement.setLong(1, projectId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Quote devis = new Quote();
                Project project = new Project();
                Client client = new Client();

                project.setprojectname(resultSet.getString("projectname"));
                client.setName(resultSet.getString("clientName"));
                project.setClient(client);

                devis.setId(resultSet.getInt("id"));
                devis.setEstimatedAmount(resultSet.getDouble("estimated_amount"));
                devis.setIssueDate(resultSet.getDate("issuedate").toLocalDate());

                Date validatedDate = resultSet.getDate("date_validity");
                devis.setDateValidity(validatedDate != null ? validatedDate.toLocalDate() : null);

                devis.setAccepted(resultSet.getBoolean("is_accepted"));
                devis.setProject(project);

                return Optional.of(devis);
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }

        return Optional.empty();
    }
    public boolean updateDevisStatus(int id) {
        String sql = "UPDATE quotes SET is_accepted = true WHERE id = ?";
        try(PreparedStatement preparedStatement = cnx.prepareStatement(sql)){
            preparedStatement.setInt(1, id);
            int result = preparedStatement.executeUpdate();
            if(result == 1) {
                return true;
            }
        }catch (SQLException sqlException){
            System.out.println(sqlException.getMessage());
        }
        return false;
    }
}
