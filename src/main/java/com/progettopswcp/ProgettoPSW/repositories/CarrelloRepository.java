package com.progettopswcp.ProgettoPSW.repositories;

import com.progettopswcp.ProgettoPSW.entities.carrello;
import com.progettopswcp.ProgettoPSW.entities.prodotto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import com.progettopswcp.ProgettoPSW.entities.cliente;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface CarrelloRepository extends JpaRepository<carrello, Integer> {
    carrello findByIdCliente(int idCliente);
    @Query("SELECT c FROM carrello c LEFT JOIN FETCH c.carrelloProdottos WHERE c.idCliente = :idCliente")
    carrello findByIdUtenteWithProducts(@Param("idCliente") int idCliente);


}