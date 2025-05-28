package br.com.totvs.customermanagement.repository;

import br.com.totvs.customermanagement.model.Client;
import br.com.totvs.customermanagement.model.Phone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PhoneRepository extends JpaRepository<Phone, UUID> {

    boolean existsPhoneByNumber(String number);

    List<Phone> findByClient(Client client);

    void deleteAllByClient(Client client);

    @Query("SELECT p.client FROM Phone p WHERE p.number = :number")
    Optional<Client> findClientByPhoneNumber(@Param("number") String number);

}
