package com.solidify.oauth2.client;

import org.junit.Test;

import java.util.List;

import static java.util.Collections.singletonList;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

public class ClientRestControllerTest {

    ClientRepository repository = mock(ClientRepository.class);

    ClientRestController controller = new ClientRestController(repository);

    @Test
    public void should_return_all_clients() {
        // given
        Client client = new Client();
        when(repository.findAll()).thenReturn(singletonList(client));

        // when
        List<ClientDto> clients = controller.getAllClients();

        // then
        assertFalse(clients.isEmpty());
        verify(repository).findAll();
    }
}