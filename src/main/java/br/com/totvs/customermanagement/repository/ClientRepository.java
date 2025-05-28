package br.com.totvs.customermanagement.repository;

import br.com.totvs.customermanagement.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID> {
    Optional<Client> findByCpf(String cpf);

    boolean existsClientByCpf(String cpf);

    boolean existsClientByName(String name);

    @Query("SELECT c.name FROM Client c WHERE c.cpf = :cpf")
    Optional<String> findNameByCpf(@Param("cpf") String cpf);
}
