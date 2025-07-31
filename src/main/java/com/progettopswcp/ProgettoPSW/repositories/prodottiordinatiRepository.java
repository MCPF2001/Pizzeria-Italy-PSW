package com.progettopswcp.ProgettoPSW.repositories;

import com.progettopswcp.ProgettoPSW.entities.prodottiordinati;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface prodottiordinatiRepository extends JpaRepository<prodottiordinati, Integer> {

    prodottiordinati findProdottiOrdinatiByIdProdotto(int idProdotto);

    List<prodottiordinati> findProdottiOrdinatiByIdOrdine(Integer idOrdine);


}