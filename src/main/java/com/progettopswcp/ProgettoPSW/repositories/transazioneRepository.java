package com.progettopswcp.ProgettoPSW.repositories;

import com.progettopswcp.ProgettoPSW.entities.transazione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface transazioneRepository extends JpaRepository<transazione, Integer> {

}
