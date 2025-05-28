package br.com.totvs.customermanagement.repository;

import br.com.totvs.customermanagement.model.Client;
import br.com.totvs.customermanagement.model.Phone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
class PhoneRepositoryTest {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PhoneRepository phoneRepository;

    private Client client;

    @BeforeEach
    void setup() {
        this.client = new Client();
        this.client.setName("Matheus Pires");
        this.client.setCpf("12345678901");
        this.client = this.clientRepository.save(this.client);

        Phone phone = new Phone();
        phone.setNumber("11988887777");
        phone.setClient(this.client);
        this.phoneRepository.save(phone);
    }

    @Test
    void existsPhoneByNumber() {
        assertTrue(this.phoneRepository.existsPhoneByNumber("11988887777"));
        assertFalse(this.phoneRepository.existsPhoneByNumber("00000000000"));
    }

    @Test
    void findByClient() {
        List<Phone> phones = this.phoneRepository.findByClient(this.client);
        assertEquals(1, phones.size());
        assertEquals("11988887777", phones.get(0).getNumber());
    }

    @Test
    void findClientByPhoneNumber() {
        Optional<Client> result = this.phoneRepository.findClientByPhoneNumber("11988887777");
        assertTrue(result.isPresent());
        assertEquals(this.client.getCpf(), result.get().getCpf());

        Optional<Client> notFound = this.phoneRepository.findClientByPhoneNumber("00000000000");
        assertTrue(notFound.isEmpty());
    }

    @Test
    void deleteAllByClient() {
        this.phoneRepository.deleteAllByClient(this.client);
        List<Phone> phones = this.phoneRepository.findByClient(this.client);
        assertTrue(phones.isEmpty());
    }

}