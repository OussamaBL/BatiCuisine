package repository;

import config.connexion;
import domain.entities.Component;
import domain.entities.Labor;
import domain.entities.Material;
import repository.interfaces.ComponentInterface;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ComponentRepository implements ComponentInterface<Component> {

    private Connection cnx;
    public ComponentRepository(){
        this.cnx= connexion.getInstance();
    }
    @Override
    public Component save(Component component) {
        try {
            String componentQuery = "INSERT INTO component (name, componenttype, vatrate) VALUES (?, ?, ?)";
            PreparedStatement stmt = cnx.prepareStatement(componentQuery,Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, component.getName());
            stmt.setString(2, component.getComponentType());
            stmt.setDouble(3, component.getVatRate());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) throw new SQLException("Inserting component failed, no rows affected.");

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            int componentId;
            if (generatedKeys.next()) {
                componentId = generatedKeys.getInt(1);
            }
            else throw new SQLException("Inserting component failed, no ID obtained.");

            if (component instanceof Labor) {
                Labor labor = (Labor) component;
                String laborQuery = "INSERT INTO labor (component_id, hourlyrate, workhours, workerproductivity) VALUES (?, ?, ?, ?)";
                PreparedStatement laborStmt = cnx.prepareStatement(laborQuery);
                laborStmt.setInt(1, componentId);
                laborStmt.setDouble(2, labor.gethourlyRate());
                laborStmt.setDouble(3, labor.getworkHours());
                laborStmt.setDouble(4, labor.getWorkerProductivity());
                laborStmt.executeUpdate();
            } else if (component instanceof Material) {
                Material material = (Material) component;
                String materialQuery = "INSERT INTO material (component_id, unitcost, quantity, transportcost, qualitycoefficient) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement materialStmt = cnx.prepareStatement(materialQuery);
                materialStmt.setInt(1, componentId);
                materialStmt.setDouble(2, material.getunitCost());
                materialStmt.setDouble(3, material.getQuantity());
                materialStmt.setDouble(4, material.getTransportCost());
                materialStmt.setDouble(5, material.getqualityCoefficient());
                materialStmt.executeUpdate();
            }
            return component;
        } catch (SQLException ex) {
            throw new RuntimeException("Error executing insert", ex);
        }

    }

    @Override
    public Optional<Component> findById(Component component) {
        try {
            String componentQuery = "SELECT * FROM component WHERE id = ?";
            PreparedStatement stmt = cnx.prepareStatement(componentQuery);
            stmt.setInt(1, component.getId());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String name = rs.getString("name");
                String componentType = rs.getString("componenttype"); // Assuming it's a string representing the type
                double vatRate = rs.getDouble("vatrate");

                Component cmp = null;

                if (componentType.equals("LABOR")) {
                    String laborQuery = "SELECT * FROM labor WHERE component_id = ?";
                    stmt = cnx.prepareStatement(laborQuery);
                    stmt.setInt(1, component.getId());
                    ResultSet laborRs = stmt.executeQuery();

                    if (laborRs.next()) {
                        double hourlyRate = laborRs.getDouble("hourlyrate");
                        int workHours = laborRs.getInt("workhours");
                        double workerProductivity = laborRs.getDouble("workerproductivity");

                        cmp = new Labor(name,componentType, vatRate, hourlyRate, workHours, workerProductivity);
                    }

                } else if (componentType.equals("MATERIAL")) {
                    String materialQuery = "SELECT * FROM material WHERE component_id = ?";
                    stmt = cnx.prepareStatement(materialQuery);
                    stmt.setInt(1, component.getId());
                    ResultSet materialRs = stmt.executeQuery();

                    if (materialRs.next()) {
                        double unitCost = materialRs.getDouble("unitcost");
                        int quantity = materialRs.getInt("quantity");
                        double transportCost = materialRs.getDouble("transportcost");
                        double qualityCoefficient = materialRs.getDouble("qualitycoefficient");

                        cmp = new Material(name,componentType, vatRate, unitCost, quantity, transportCost, qualityCoefficient);
                    }
                }

                if (cmp != null) {
                    return Optional.of(cmp);
                }
            }
            return Optional.empty();

        } catch (SQLException ex) {
            throw new RuntimeException("Error executing findById", ex);
        }
    }

    @Override
    public List<Component> findAll() {
        List<Component> components = new ArrayList<>();

        try {
            String componentQuery = "SELECT * FROM component";
            PreparedStatement componentStmt = cnx.prepareStatement(componentQuery);
            ResultSet rs = componentStmt.executeQuery();

            while (rs.next()) {
                int componentId = rs.getInt("id");
                String name = rs.getString("name");
                String componentType = rs.getString("componenttype");
                double vatRate = rs.getDouble("vatrate");

                Component component = null;

                if (componentType.equals("LABOR")) {
                    String laborQuery = "SELECT * FROM labor WHERE component_id = ?";
                    PreparedStatement laborStmt = cnx.prepareStatement(laborQuery);
                    laborStmt.setInt(1, componentId);
                    ResultSet laborRs = laborStmt.executeQuery();

                    if (laborRs.next()) {
                        double hourlyRate = laborRs.getDouble("hourlyrate");
                        int workHours = laborRs.getInt("workhours");
                        double workerProductivity = laborRs.getDouble("workerproductivity");

                        component = new Labor(name,componentType, vatRate, hourlyRate, workHours, workerProductivity);
                    }

                } else if (componentType.equals("MATERIAL")) {
                    String materialQuery = "SELECT * FROM material WHERE component_id = ?";
                    PreparedStatement materialStmt = cnx.prepareStatement(materialQuery);
                    materialStmt.setInt(1, componentId);
                    ResultSet materialRs = materialStmt.executeQuery();

                    if (materialRs.next()) {
                        double unitCost = materialRs.getDouble("unitcost");
                        int quantity = materialRs.getInt("quantity");
                        double transportCost = materialRs.getDouble("transportcost");
                        double qualityCoefficient = materialRs.getDouble("qualitycoefficient");

                        component = new Material(name,componentType, vatRate, unitCost, quantity, transportCost, qualityCoefficient);
                    }
                }

                if (component != null) {
                    components.add(component);
                }
            }
            return components;

        } catch (SQLException ex) {
            throw new RuntimeException("Error executing findAll", ex);
        }
    }

    @Override
    public Component update(Component component) {
        try {
            String updateComponentQuery = "UPDATE component SET name = ?, vatrate = ? WHERE id = ?";
            PreparedStatement stmt = cnx.prepareStatement(updateComponentQuery);
            stmt.setString(1, component.getName());
            stmt.setDouble(2, component.getVatRate());
            stmt.setInt(3, component.getId());

            stmt.executeUpdate();

            if (component instanceof Labor) {
                String updateLaborQuery = "UPDATE labor SET hourlyrate = ?, workhours = ?, workerproductivity = ? WHERE component_id = ?";
                PreparedStatement laborStmt = cnx.prepareStatement(updateLaborQuery);
                Labor labor = (Labor) component;
                laborStmt.setDouble(1, labor.gethourlyRate());
                laborStmt.setDouble(2, labor.getworkHours());
                laborStmt.setDouble(3, labor.getWorkerProductivity());
                laborStmt.setInt(4, component.getId());

                laborStmt.executeUpdate();
            } else if (component instanceof Material) {
                String updateMaterialQuery = "UPDATE material SET unitcost = ?, quantity = ?, transportcost = ?, qualitycoefficient = ? WHERE component_id = ?";
                PreparedStatement materialStmt = cnx.prepareStatement(updateMaterialQuery);
                Material material = (Material) component;
                materialStmt.setDouble(1, material.getunitCost());
                materialStmt.setDouble(2, material.getQuantity());
                materialStmt.setDouble(3, material.getTransportCost());
                materialStmt.setDouble(4, material.getqualityCoefficient());
                materialStmt.setInt(5, component.getId());

                materialStmt.executeUpdate();
            }

            return component;

        } catch (SQLException ex) {
            throw new RuntimeException("Error updating component", ex);
        }
    }

    @Override
    public boolean delete(Component component) {
        try {
            if (component instanceof Labor) {
                String deleteLaborQuery = "DELETE FROM labor WHERE component_id = ?";
                PreparedStatement laborStmt = cnx.prepareStatement(deleteLaborQuery);
                laborStmt.setInt(1, component.getId());
                laborStmt.executeUpdate();
            } else if (component instanceof Material) {
                String deleteMaterialQuery = "DELETE FROM material WHERE component_id = ?";
                PreparedStatement materialStmt = cnx.prepareStatement(deleteMaterialQuery);
                materialStmt.setInt(1, component.getId());
                materialStmt.executeUpdate();
            }

            String deleteComponentQuery = "DELETE FROM component WHERE id = ?";
            PreparedStatement stmt = cnx.prepareStatement(deleteComponentQuery);
            stmt.setInt(1, component.getId());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException ex) {
            throw new RuntimeException("Error deleting component", ex);
        }
    }
}
