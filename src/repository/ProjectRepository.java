package repository;

import config.connexion;
import domain.entities.Client;
import domain.entities.Project;
import exceptions.ProjectsNotFoundException;
import repository.interfaces.ProjectInterface;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProjectRepository implements ProjectInterface<Project> {
    private Connection cnx;
    public ProjectRepository(){
        cnx= connexion.getInstance();
    }
    @Override
    public Project save(Project project) {
        String sql = "INSERT INTO projects (projectName, profitMargin, totalCost, status, client_id) VALUES (?, ?, ?, ?::projectStatus, ?);";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, project.getprojectname());
            preparedStatement.setDouble(2, project.getprofitMargin());
            preparedStatement.setDouble(3, project.gettotalCost());
            preparedStatement.setString(4, project.getStatus().name());
            preparedStatement.setInt(5, project.getClient().getId());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating project failed, no rows affected.");
            }
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    project.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating project failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return project;
    }

    @Override
    public void saveClientProject(Client client, Project project) {
        ClientRepository clientRepository = new ClientRepository();
        Client savedClient = clientRepository.save(client);
        project.setClient(savedClient);
        save(project);
    }

    @Override
    public Optional<Project> findById(Project project) {
        String sql = "SELECT p.*,c.* FROM projects p join clients c on p.client_id=c.id WHERE p.id = ?";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(sql)) {
            preparedStatement.setInt(1, project.getId());
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                Client client = new Client();
                client.setId(rs.getInt("client_id"));
                client.setaddress(rs.getString("address"));
                client.setName(rs.getString("name"));
                Project proj = new Project(
                        rs.getInt("id"),
                        rs.getString("projectName"),
                        rs.getDouble("profitMargin"),
                        rs.getDouble("totalCost"),
                        rs.getString("status"),
                        client
                );
                return Optional.of(proj);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Project> findAll() {
        String sql = "SELECT p.*,c.* FROM projects p inner join clients c on p.client_id=c.id";
            List<Project> projects = new ArrayList<>();

        try (PreparedStatement preparedStatement = cnx.prepareStatement(sql);
             ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                Client client = new Client();
                client.setId(rs.getInt("client_id"));
                client.setName(rs.getString("name"));
                Project project = new Project(
                        rs.getInt("id"),
                        rs.getString("projectName"),
                        rs.getDouble("profitMargin"),
                        rs.getDouble("totalCost"),
                        rs.getString("status"),
                        client
                );
                projects.add(project);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return projects;
    }

    @Override
    public Project update(Project project) {
        String sql = "UPDATE projects SET projectName = ?, profitMargin = ?, totalCost = ?, status = ?::projectStatus, client_id = ? WHERE id = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(sql)) {

            stmt.setString(1, project.getprojectname());
            stmt.setDouble(2, project.getprofitMargin());
            stmt.setDouble(3, project.gettotalCost());
            stmt.setString(4, project.getStatus().name());
            stmt.setInt(5, project.getClient().getId());
            stmt.setInt(6, project.getId());

            int result = stmt.executeUpdate();
            if (result == 1) {
                System.out.println("Project updated successfully");
            } else {
                throw new ProjectsNotFoundException("Update failed, project not found");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return project;
    }

    @Override
    public boolean delete(Project project) {
        String sql = "DELETE FROM projects WHERE id = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(sql)) {

            stmt.setInt(1, project.getId());

            int result = stmt.executeUpdate();
            if (result == 1) return true;
             else
                throw new ProjectsNotFoundException("Delete failed, project not found");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public Project findProjectByName(String name) {
        String sql = "SELECT id , projectName FROM projects WHERE projectName = ?";
        Project project = new Project();
        try (PreparedStatement preparedStatement = cnx.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                project.setId(id);
                project.setprojectname(resultSet.getString("projectName"));
            } else {
                throw new ProjectsNotFoundException("Project not found");
            }

        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }

        return project;
    }

    public void updateMarginAndTotalCost_Project(int id, double marginProfit, double totalCost) {
        String sql = "UPDATE projects SET profitMargin =? , totalCost = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(sql)) {
            preparedStatement.setDouble(1, marginProfit);
            preparedStatement.setDouble(2, totalCost);
            preparedStatement.setInt(3, id);
            int result = preparedStatement.executeUpdate();
            if (result == 1) {
                System.out.println("Project updated successfully");
            } else {
                System.out.println("Update failed, project not found");
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
    }
    public boolean updateStatus(int id, String status) {
        String sql = "UPDATE projects SET status = ?::projectStatus  WHERE id = ?";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(sql)) {
            preparedStatement.setString(1, status);
            preparedStatement.setInt(2, id);
            int result = preparedStatement.executeUpdate();
            if (result == 1) {
                return true;
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return false;
    }

}
