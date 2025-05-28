package br.com.totvs.customermanagement.repository;

import br.com.totvs.customermanagement.model.Client;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
class ClientRepositoryTest {

    @Autowired
    private ClientRepository clientRepository;

    @Test
    void findByCpf() {
        Client client = new Client();
        client.setCpf("12345678909");
        client.setName("Matheus Pires");
        this.clientRepository.save(client);

        Optional<Client> found = this.clientRepository.findByCpf("12345678909");
        assertTrue(found.isPresent());
        assertEquals("Matheus Pires", found.get().getName());
    }

    @Test
    void existsClientByCpf() {
        Client client = new Client();
        client.setCpf("12345678909");
        client.setName("Matheus Pires");
        this.clientRepository.save(client);

        assertTrue(this.clientRepository.existsClientByCpf("12345678909"));
        assertFalse(this.clientRepository.existsClientByCpf("00000000000"));
    }

    @Test
    void existsClientByName() {
        Client client = new Client();
        client.setCpf("12345678909");
        client.setName("Matheus Pires");
        this.clientRepository.save(client);

        assertTrue(this.clientRepository.existsClientByName("Matheus Pires"));
        assertFalse(this.clientRepository.existsClientByName("Jane Smith"));
    }

    @Test
    void findNameByCpf() {
        Client client = new Client();
        client.setCpf("12345678909");
        client.setName("Matheus Pires");
        this.clientRepository.save(client);

        Optional<String> name = this.clientRepository.findNameByCpf("12345678909");
        assertTrue(name.isPresent());
        assertEquals("Matheus Pires", name.get());
    }
}