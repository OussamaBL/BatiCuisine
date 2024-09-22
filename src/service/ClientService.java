package service;

import domain.entities.Client;
import repository.ClientRepository;

import java.util.List;
import java.util.Optional;

public class ClientService {
    private ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client save(Client client) {
        return clientRepository.save(client);
    }

    public Optional<Client> findById(Client client) {
        return clientRepository.findById(client);
    }

    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    public void update(Client client) {
        clientRepository.update(client);
    }

    public void delete(Client client) {
        clientRepository.delete(client);
    }

    public Optional<Client> findByName(String name) {
        if(name == null || name.isEmpty())
            throw new RuntimeException("name is null");
        else return this.clientRepository.findByName(name);

    }

}
