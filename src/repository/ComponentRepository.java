package repository;

import config.connexion;
import domain.entities.Component;
import domain.entities.Labor;
import domain.entities.Material;
import domain.entities.Project;
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
    public Component save(Component entity) {
        try{
            String query="";
            if (entity instanceof Labor) {
                query = "insert into labors (name, vat_rate,component_type,project_id, hourlyRate, workHours, workerProductivity) VALUES (?, ?,?, ?, ?, ?,?)";
            } else if (entity instanceof Material) {
                query = "insert into materials (name, vat_rate,component_type,project_id, unitcost, quantity, transportcost, qualitycoefficient) values (?, ?, ?, ?, ?, ?,?,?)";
            }

            PreparedStatement pst=cnx.prepareStatement(query);
            pst.setString(1, entity.getName());
            pst.setDouble(2, entity.getVatRate());
            pst.setString(3,entity.getComponentType());
            pst.setInt(4,entity.getProject().getId());
            if (entity instanceof Labor) {
                pst.setDouble(5,((Labor) entity).getHourlyRate());
                pst.setDouble(6, ((Labor) entity).getworkHours());
                pst.setDouble(7, ((Labor) entity).getWorkerProductivity());
            } else if (entity instanceof Material) {
                pst.setDouble(5,((Material) entity).getUnitCost());
                pst.setDouble(6, ((Material) entity).getQuantity());
                pst.setDouble(7, ((Material) entity).getTransportCost());
                pst.setDouble(8, ((Material) entity).getqualityCoefficient());

            }
            pst.executeUpdate();
            return entity;
        } catch (SQLException ex) {
           throw new RuntimeException("Error inserted : "+ex.getMessage());
        }

    }

    @Override
    public Optional<Component> findById(Component entity) {
        String query = "SELECT c.id, c.name, c.vatRate,c.componentType,c.project_id " +
                "       l.hourlyrate, l.workHours, l.workerProductivity, " +
                "       m.unitcost, m.quantity, m.transportcost, m.qualitycoefficient " +
                "FROM composants c " +
                "LEFT JOIN labors l ON c.id = l.id " +
                "LEFT JOIN materials m ON c.id = m.id " +
                "WHERE c.id = ?";

        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setInt(1, entity.getId());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String name = rs.getString(2);
                double vatRate = rs.getDouble(3);
                int id = rs.getInt(1);
                String componentType = rs.getString(4);

                if (componentType.equals("LABOR")) {
                    double hourlyRate = rs.getDouble(6);
                    double workHours = rs.getDouble(7);
                    double workerProductivity = rs.getDouble(8);
                    Labor labor = new Labor(name, "LABOR", vatRate, new Project(rs.getInt(5)), hourlyRate, workHours, workerProductivity);
                    labor.setId(id);
                    return Optional.of(labor);
                }

                if (componentType.equals("MATERIAL")) {
                    double unitCost = rs.getDouble(6);
                    double quantity = rs.getDouble(7);
                    double transportCost = rs.getDouble(8);
                    double qualityCoefficient = rs.getDouble(9);
                    Material material = new Material(name, "MATERIAL", vatRate,new Project(rs.getInt(5)), unitCost, quantity, transportCost, qualityCoefficient);
                    material.setId(id);
                    return Optional.of(material);
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error executing findById", ex);
        }
        return Optional.empty();
    }
    public List<Component> findAllType(String type){
        String query = "SELECT c.id, c.name, c.vatRate, c.componentType,c.project_id " +
                "       l.hourlyrate, l.workHours, l.workerProductivity, " +
                "       m.unitcost, m.quantity, m.transportcost, m.qualitycoefficient " +
                "FROM composants c " +
                "LEFT JOIN labors l ON c.id = l.id " +
                "LEFT JOIN materials m ON c.id = m.id " +
                "where c.componentType=? ";

        List<Component> components = new ArrayList<>();

        try (PreparedStatement stmt = cnx.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String name = rs.getString("name");
                double vatRate = rs.getDouble("vatRate");
                int id = rs.getInt("id");
                String componentType = rs.getString("componentType");

                if ("LABOR".equals(type)) {
                    double hourlyRate = rs.getDouble("hourlyrate");
                    double workHours = rs.getDouble("workHours");
                    double workerProductivity = rs.getDouble("workerProductivity");

                    Labor labor = new Labor(name, "LABOR", vatRate,new Project(rs.getInt("project_id")), hourlyRate, workHours, workerProductivity);
                    labor.setId(id);
                    components.add(labor);
                } else if ("MATERIAL".equals(type)) {
                    double unitCost = rs.getDouble("unitcost");
                    double quantity = rs.getDouble("quantity");
                    double transportCost = rs.getDouble("transportcost");
                    double qualityCoefficient = rs.getDouble("qualitycoefficient");

                    Material material = new Material(name, "MATERIAL", vatRate,new Project(rs.getInt("project_id")), unitCost, quantity, transportCost, qualityCoefficient);
                    material.setId(id);
                    components.add(material);
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error executing findAll", ex);
        }
        return components;
    }

    @Override
    public List<Component> findAll() {
        String query = "SELECT c.id, c.name, c.vatRate, c.componentType,c.project_id " +
                "       l.hourlyrate, l.workHours, l.workerProductivity, " +
                "       m.unitcost, m.quantity, m.transportcost, m.qualitycoefficient " +
                "FROM composants c " +
                "LEFT JOIN labors l ON c.id = l.id " +
                "LEFT JOIN materials m ON c.id = m.id";

        List<Component> components = new ArrayList<>();

        try (PreparedStatement stmt = cnx.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String name = rs.getString("name");
                double vatRate = rs.getDouble("vatRate");
                int id = rs.getInt("id");
                String componentType = rs.getString("componentType");

                if ("LABOR".equals(componentType)) {
                    double hourlyRate = rs.getDouble("hourlyrate");
                    double workHours = rs.getDouble("workHours");
                    double workerProductivity = rs.getDouble("workerProductivity");

                    Labor labor = new Labor(name, "LABOR", vatRate,new Project(rs.getInt("project_id")), hourlyRate, workHours, workerProductivity);
                    labor.setId(id);
                    components.add(labor);
                } else if ("MATERIAL".equals(componentType)) {
                    double unitCost = rs.getDouble("unitcost");
                    double quantity = rs.getDouble("quantity");
                    double transportCost = rs.getDouble("transportcost");
                    double qualityCoefficient = rs.getDouble("qualitycoefficient");

                    Material material = new Material(name, "MATERIAL", vatRate,new Project(rs.getInt("project_id")), unitCost, quantity, transportCost, qualityCoefficient);
                    material.setId(id);
                    components.add(material);
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error executing findAll", ex);
        }
        return components;
    }

    @Override
    public Component update(Component entity) {
        String updateComponentQuery = "UPDATE components SET name = ?, vatRate = ? WHERE id = ?";
        String updateLaborQuery = "UPDATE labors SET hourlyrate = ?, workhourscount = ?, productivityrate = ? WHERE id = ?";
        String updateMaterialQuery = "UPDATE materials SET unitcost = ?, quantity = ?, transportcost = ?, qualitycoefficient = ? WHERE id = ?";

        try {
            try (PreparedStatement stmt = cnx.prepareStatement(updateComponentQuery)) {
                stmt.setString(1, entity.getName());
                stmt.setDouble(2, entity.getVatRate());
                stmt.setInt(3, entity.getId());
                stmt.executeUpdate();
            }

            if (entity instanceof Labor) {
                Labor labor = (Labor) entity;
                try (PreparedStatement stmt = cnx.prepareStatement(updateLaborQuery)) {
                    stmt.setDouble(1, labor.getHourlyRate());
                    stmt.setDouble(2, labor.getWorkHours());
                    stmt.setDouble(3, labor.getWorkerProductivity());
                    stmt.setInt(4, labor.getId());
                    stmt.executeUpdate();
                }
            } else if (entity instanceof Material) {
                Material material = (Material) entity;
                try (PreparedStatement stmt = cnx.prepareStatement(updateMaterialQuery)) {
                    stmt.setDouble(1, material.getUnitCost());
                    stmt.setDouble(2, material.getQuantity());
                    stmt.setDouble(3, material.getTransportCost());
                    stmt.setDouble(4, material.getQualityCoefficient());
                    stmt.setInt(5, material.getId());
                    stmt.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error executing update", ex);
        }
        return entity;
    }

    @Override
    public boolean delete(Component entity) {
        try {
            if (entity instanceof Labor) {
                String deleteLaborQuery = "DELETE FROM labors WHERE id = ?";
                try (PreparedStatement stmt = cnx.prepareStatement(deleteLaborQuery)) {
                    stmt.setInt(1, entity.getId());
                    stmt.executeUpdate();
                }
            } else if (entity instanceof Material) {
                String deleteMaterialQuery = "DELETE FROM materials WHERE id = ?";
                try (PreparedStatement stmt = cnx.prepareStatement(deleteMaterialQuery)) {
                    stmt.setInt(1, entity.getId());
                    stmt.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error executing delete", ex);
        }
        return true;
    }
}
