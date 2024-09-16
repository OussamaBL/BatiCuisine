package repository;

import config.connexion;
import domain.entities.Project;
import repository.interfaces.ProjectInterface;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
        try (PreparedStatement preparedStatement = cnx.prepareStatement(sql)) {
            preparedStatement.setString(1, project.getprojectname());
            preparedStatement.setDouble(2, project.getprofitMargin());
            preparedStatement.setDouble(3, project.gettotalCost());
            preparedStatement.setString(4, project.getStatus().name());
            preparedStatement.setInt(5, project.getClient().getId());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating project failed, no rows affected.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return project;
    }

    @Override
    public Optional<Project> findById(Project project) {
        return Optional.empty();
    }

    @Override
    public List<Project> findAll() {
        return List.of();
    }

    @Override
    public Project update(Project entity) {
        return null;
    }

    @Override
    public boolean delete(Project entity) {
        return false;
    }
}
