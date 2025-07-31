package com.progettopswcp.ProgettoPSW.repositories;


import com.progettopswcp.ProgettoPSW.entities.prodotto;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.*;
import java.util.*;


@Repository
public interface ProdottoRepository extends JpaRepository<prodotto, Integer> {

    List<prodotto> findByNome(String nome);
    List<prodotto> findById(int idProdotto);
    boolean existsById(int Id);
    @Query("SELECT p " +
            "FROM prodotto p " +
            "WHERE (p.nome LIKE ?1 OR ?1 IS NULL) AND " +
            "      (p.TipoProdotto LIKE ?2 OR ?2 IS NULL)")
    List<prodotto> advancedSearch(String nome, String tipoprodotto);






}
