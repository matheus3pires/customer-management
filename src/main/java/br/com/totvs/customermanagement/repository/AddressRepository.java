package br.com.totvs.customermanagement.repository;

import br.com.totvs.customermanagement.model.Address;
import br.com.totvs.customermanagement.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {

    List<Address> findByClient(Client client);

    void deleteAllByClient(Client client);
}
