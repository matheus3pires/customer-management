package br.com.totvs.customermanagement.repository;

import br.com.totvs.customermanagement.model.Address;
import br.com.totvs.customermanagement.model.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
class AddressRepositoryTest {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AddressRepository addressRepository;

    private Client client;

    @BeforeEach
    void setUp() {
        this.client = new Client();
        this.client.setName("Matheus Pires");
        this.client.setCpf("98765432100");
        this.client = this.clientRepository.save(this.client);

        Address address1 = new Address();
        address1.setStreet("Main Street");
        address1.setComplement("QD11");
        address1.setCity("Goiânia");
        address1.setState("Goiás");
        address1.setZipCode("74860405");
        address1.setClient(this.client);

        Address address2 = new Address();
        address2.setStreet("Second Street");
        address2.setComplement("QD12");
        address2.setCity("Goiânia");
        address2.setState("Goiás");
        address2.setZipCode("74860406");
        address2.setClient(this.client);

        this.addressRepository.saveAll(List.of(address1, address2));
    }

    @Test
    void findByClient() {
        List<Address> addresses = this.addressRepository.findByClient(this.client);

        assertThat(addresses).hasSize(2);
        assertThat(addresses.get(0).getClient()).isEqualTo(this.client);
    }

    @Test
    void deleteAllByClient() {
        this.addressRepository.deleteAllByClient(this.client);

        List<Address> addresses = this.addressRepository.findByClient(this.client);
        assertThat(addresses).isEmpty();
    }
}