package com.progettopswcp.ProgettoPSW.repositories;


import com.progettopswcp.ProgettoPSW.entities.metododipagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface metododipagamentoRepository extends JpaRepository<metododipagamento, Integer> {

    metododipagamento findById(int id);

}