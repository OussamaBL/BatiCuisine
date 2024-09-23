package service;

import domain.entities.Component;
import domain.entities.Labor;
import domain.entities.Material;
import repository.ComponentRepository;

import java.util.List;
import java.util.Optional;

public class ComponentService {
    private ComponentRepository crp;
    public ComponentService(){
        crp=new ComponentRepository();
    }
    public Component create(Component component){
        return crp.save(component);
    }
    public Component findById(Component component){
        Optional<Component> opComponent=crp.findById(component);
        if(opComponent.isEmpty()) return null;
        else return opComponent.get();
    }
    public List<Component> readAll(){
        return crp.findAll();
    }
    public List<Component> findAllType(String type){
        return crp.findAllType(type);
    }

    public Component update(Component component){
        return crp.update(component);
    }
    public boolean delete(Component component){
        return crp.delete(component);
    }

    public List<Material> findAllMaterialsByProject(int id){
        return crp.findAllMaterialsByProject(id);
    }
    public List<Labor> findAllLaborsByProject(int id){
        return crp.findAllLaborsByProject(id);
    }

    public double calculateMaterialAfterVatRate(Material material) {
        double costBeforeVat = (material.getUnitCost() * material.getQuantity() * material.getQualityCoefficient())+material.getTransportCost();
        return costBeforeVat+(costBeforeVat*material.getVatRate()/100);
    }

    public double calculateMaterialBeforeVatRate(Material material) {
        return (material.getUnitCost() * material.getQuantity() * material.getQualityCoefficient()) + material.getTransportCost();
    }
    public double calculateLaborAfterVatRate(Labor labor) {
        double costBeforeVat = calculateLaborBeforeVatRate(labor);
        return costBeforeVat+(costBeforeVat*labor.getVatRate()/100);
    }

    public double calculateLaborBeforeVatRate(Labor labor) {
        return labor.getWorkHours() * labor.getHourlyRate() * labor.getWorkerProductivity();
    }

}
