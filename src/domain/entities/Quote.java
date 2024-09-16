package domain.entities;

import java.time.LocalDate;

public class Quote {
    private int id;
    private double estimatedAmount;
    private LocalDate issueDate;
    private LocalDate dateValidity;

    public LocalDate getDateValidity() {
        return dateValidity;
    }

    public void setDateValidity(LocalDate dateValidity) {
        this.dateValidity = dateValidity;
    }

    private boolean isAccepted;
    private Project project;

    public Quote(int id, double estimatedAmount, LocalDate issueDate,LocalDate dateValidity, boolean isAccepted, Project project) {
        this.id = id;
        this.estimatedAmount = estimatedAmount;
        this.issueDate = issueDate;
        this.dateValidity = dateValidity;
        this.isAccepted = isAccepted;
        this.project = project;
    }

    public Quote() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getEstimatedAmount() {
        return estimatedAmount;
    }

    public void setEstimatedAmount(double estimatedAmount) {
        this.estimatedAmount = estimatedAmount;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @Override
    public String toString() {
        return "Quote{" +
                "id=" + id +
                ", estimatedAmount=" + estimatedAmount +
                ", issueDate=" + issueDate +
                ", dateValidity=" + dateValidity +
                ", isAccepted=" + isAccepted +
                ", project=" + project +
                '}';
    }
}
