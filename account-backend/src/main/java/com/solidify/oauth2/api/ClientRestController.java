package com.solidify.oauth2.api;

import com.solidify.oauth2.client.Client;
import com.solidify.oauth2.client.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;

/**
 * Created by tomasz on 16.10.16.
 */
@RestController
public class ClientRestController {

    private final ClientRepository clientRepository;

    @Autowired
    public ClientRestController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @RequestMapping("/api/clients")
    public List<ClientDto> getAllClients() {
        return stream(
                clientRepository.findAll().spliterator(),
                false)
                .map(this::toDto)
                .collect(toList());
    }

    protected ClientDto toDto(Client model) {
        ClientDto dto = new ClientDto();
        dto.setId(model.getClientId());
        return dto;
    }
}
