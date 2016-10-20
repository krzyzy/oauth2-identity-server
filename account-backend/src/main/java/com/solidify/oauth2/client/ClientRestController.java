package com.solidify.oauth2.client;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

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
    public List<Client> getClient(){
        return new ArrayList<Client>(Lists.newArrayList(clientRepository.findAll()));
    }
}
