package com.progettopswcp.ProgettoPSW.repositories;

import com.progettopswcp.ProgettoPSW.entities.cliente;
import com.progettopswcp.ProgettoPSW.entities.ordine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.*;

public interface OrdineRepository extends JpaRepository<ordine, Integer> {

    List<ordine> findByIdOrdine(int IdOrdine);
    List<ordine> findByIdCliente(int idCliente );
    List<ordine> findByData(LocalDateTime data);

}

