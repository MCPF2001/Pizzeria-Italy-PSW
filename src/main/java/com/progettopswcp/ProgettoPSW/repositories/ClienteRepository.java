package com.progettopswcp.ProgettoPSW.repositories;


import com.progettopswcp.ProgettoPSW.entities.cliente;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;



@Repository
public interface ClienteRepository extends JpaRepository<cliente, Integer> {

    List<cliente> findByNome(String nome);
    cliente findByEmail(String keycloakUserId);
    List<cliente> findByIdCliente(int idCliente);

    boolean existsByEmail(String email);

    boolean existsByIdCliente(int idCliente);
    }
