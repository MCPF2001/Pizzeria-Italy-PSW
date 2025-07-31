package com.progettopswcp.ProgettoPSW.repositories;

import com.progettopswcp.ProgettoPSW.entities.carrello;
import com.progettopswcp.ProgettoPSW.entities.carrelloprodotto;
import com.progettopswcp.ProgettoPSW.entities.prodotto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Set;

@Repository
public interface carrelloprodottoRepository extends JpaRepository<carrelloprodotto, Integer> {

    carrelloprodotto findByCarrelloAndProdotto(carrello carrello, prodotto prod);
    carrelloprodotto findByCarrelloAndProdottoId(carrello carrello, int prod);

    @Query("SELECT cp FROM carrelloprodotto cp WHERE cp.carrello.idCarrello = :idCarrello")
    Set<carrelloprodotto> findByCarrelloId(@Param("idCarrello") int idCarrello);

    void deleteAllByCarrello(carrello carrello);
}
