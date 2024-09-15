package service;

import domain.entities.Component;
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
    public Component update(Component component){
        return crp.update(component);
    }
    public boolean delete(Component component){
        return crp.delete(component);
    }
}
