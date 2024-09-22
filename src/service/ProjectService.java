package service;

import domain.entities.Client;
import domain.entities.Project;
import repository.ProjectRepository;

import java.util.List;
import java.util.Optional;

public class ProjectService {
    private ProjectRepository prp;
    public ProjectService(){
        prp=new ProjectRepository();
    }
    public Project save(Project project) {
        return this.prp.save(project);
    }

    public Boolean delete(Project project) {
        return prp.delete(project);
    }

    public Project update(Project project) {
        return this.prp.update(project);
    }

    public List<Project> findAll() {
        return this.prp.findAll();
    }

    public Optional<Project> findById(Project project) {
        return this.prp.findById(project);
    }

    public void saveClientProject(Client client, Project project) {
        this.prp.saveClientProject(client,project);
    }

    public Project findProjectByName(String name) {
        return this.prp.findProjectByName(name);
    }
}
