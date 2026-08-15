package com.pedromolon.CasaAndrade.service;

import com.pedromolon.CasaAndrade.dto.request.ClientRequest;
import com.pedromolon.CasaAndrade.dto.response.ClientResponse;
import com.pedromolon.CasaAndrade.exception.ResourceNotFoundException;
import com.pedromolon.CasaAndrade.mapper.ClientMapper;
import com.pedromolon.CasaAndrade.model.Client;
import com.pedromolon.CasaAndrade.repository.ClientRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    public ClientService(ClientRepository clientRepository, ClientMapper clientMapper) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
    }

    @Transactional
    public ClientResponse saveClient(ClientRequest request) {
        if (clientRepository.existsByDocument(request.document())) {
            throw new IllegalStateException("Client already exists");
        }

        Client client = clientMapper.toEntity(request);
        return clientMapper.toResponse(clientRepository.save(client));
    }

    @Transactional(readOnly = true)
    public Page<ClientResponse> getAllClients(Pageable pageable) {
        return clientRepository.findByActiveTrue(pageable)
                .map(clientMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public ClientResponse getClientById(Long id) {
        return clientRepository.findById(id)
                .map(clientMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with this id"));
    }

    @Transactional
    public ClientResponse updateClient(Long id, ClientRequest request) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with this id"));

        client.setName(request.name());
        client.setDocument(request.document());
        client.setEmail(request.email());
        client.setPhone(request.phone());

        return clientMapper.toResponse(clientRepository.save(client));
    }

    @Transactional
    public void disableClient(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id"));

        client.setActive(false);

        clientRepository.save(client);
    }

}
