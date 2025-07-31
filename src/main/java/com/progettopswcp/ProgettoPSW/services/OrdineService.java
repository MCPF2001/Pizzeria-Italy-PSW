package com.progettopswcp.ProgettoPSW.services;

import com.progettopswcp.ProgettoPSW.dto.ordineDTO;
import com.progettopswcp.ProgettoPSW.dto.prodottoordinatoDTO;
import com.progettopswcp.ProgettoPSW.entities.*;
import com.progettopswcp.ProgettoPSW.repositories.OrdineRepository;
import com.progettopswcp.ProgettoPSW.repositories.ClienteRepository;
import com.progettopswcp.ProgettoPSW.repositories.ProdottoRepository;
import com.progettopswcp.ProgettoPSW.repositories.prodottiordinatiRepository;
import com.progettopswcp.ProgettoPSW.resources.exceptions.InvalidOperationException;
import com.progettopswcp.ProgettoPSW.resources.exceptions.ProductNotFoundException;
import com.progettopswcp.ProgettoPSW.resources.exceptions.UserNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@Service
public class OrdineService {
    private static final Random RANDOM = new Random();

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private OrdineRepository ordineRepository;

    @Autowired
    private ClienteRepository clienteRepository;


    @Autowired
    private ProdottoRepository prodottoRepository;

    @Autowired
    private prodottiordinatiRepository prodottiordinatiRepository;

    @Transactional
    public void annullaOrdine(cliente c, ordine o, String motivo) {
        // Lock sull'utente
        cliente cl = clienteRepository.findById(c.getIdCliente())
                .orElseThrow();
        entityManager.lock(cl, LockModeType.PESSIMISTIC_WRITE);

        // Lock sull'ordine
        ordine ordine = ordineRepository.findById(o.getIdOrdine())
                .orElseThrow(InvalidOperationException::new);
        entityManager.lock(ordine, LockModeType.PESSIMISTIC_WRITE);

        if (ordine.getIdCliente()!=(cl.getIdCliente())) {
            throw new InvalidOperationException("L'ordine non appartiene all'utente.");
        }

        // Lock sulla spedizione
        spedizione sped = ordine.getSpedizione();
        if (sped != null) {
            entityManager.lock(sped, LockModeType.PESSIMISTIC_WRITE);
            sped.setStato("Ordine annullato!");
            // Se necessario, salvo la spedizione
            // spedizioneRepository.save(sped);
        }

        ordine.setStato("Annullato. Motivo: " + motivo);
        ordineRepository.save(ordine);

        // Processo il rimborso
        processaRimborso(ordine);
    }

    @Transactional(readOnly = true)
    public List<ordineDTO> getOrdiniByEmail(String email) throws UserNotFoundException, ProductNotFoundException {
        // Recupero il cliente tramite la sua email
        cliente cliente = clienteRepository.findByEmail(email);
        if (cliente == null) {
            throw new UserNotFoundException("Cliente non trovato con email: " + email);
        }

        // Log per verificare il cliente trovato
        System.out.println("Cliente trovato: " + cliente.getEmail());

        // Recupero gli ordini del cliente
        List<ordine> ordini = ordineRepository.findByIdCliente(cliente.getIdCliente());
        if (ordini.isEmpty()) {
            System.out.println("Nessun ordine trovato per il cliente con email: " + email);
        }

        // Creazione della lista di DTO per gli ordini
        List<ordineDTO> ordiniDTO = new ArrayList<>();
        for (ordine ordine : ordini) {
            ordineDTO dto = new ordineDTO();
            dto.setIdOrdine(ordine.getIdOrdine());
            dto.setData(ordine.getData());
            dto.setOra(ordine.getOra());
            dto.setStato(ordine.getStato());


            // Log per ogni ordine trovato
            System.out.println("Ordine trovato: ID = " + ordine.getIdOrdine() + ", Stato = " + ordine.getStato());

            // Recupero dei prodotti ordinati per ciascun ordine
            List<prodottiordinati> prodottiOrdinati = prodottiordinatiRepository.findProdottiOrdinatiByIdOrdine(ordine.getIdOrdine());
            List<prodottoordinatoDTO> prodottiDTO = new ArrayList<>();

            for (prodottiordinati po : prodottiOrdinati) {
                // Recupero il prodotto utilizzando l'ID del prodotto
                prodotto p = prodottoRepository.findById(po.getIdProdotto())
                        .orElseThrow(() -> new ProductNotFoundException("Prodotto non trovato con ID: " + po.getIdProdotto()));

                prodottoordinatoDTO prodottoDTO = new prodottoordinatoDTO();
                prodottoDTO.setIdProdotto(p.getId());
                prodottoDTO.setNome(p.getNome());
                prodottoDTO.setPrezzo(p.getPrezzo());
                prodottoDTO.setQuantita(po.getQuantita());

                // Aggiungi il prodotto al DTO dell'ordine
                prodottiDTO.add(prodottoDTO);
            }

            // Aggiungi la lista di prodotti all'ordineDTO
            dto.setProdotti(prodottiDTO);

            // Aggiungi l'ordineDTO alla lista finale
            ordiniDTO.add(dto);
        }

        return ordiniDTO;
    }

    private void processaRimborso(ordine ordine) {
        BigDecimal importoRimborso = calcolaImportoRimborso(ordine);
        simulaRimborso(importoRimborso);
    }

    private BigDecimal calcolaImportoRimborso(ordine ordine) {
        // Lock sulla transazione
        transazione transazione = ordine.getTransaziones();
        if (transazione != null) {
            entityManager.lock(transazione, LockModeType.PESSIMISTIC_WRITE);
            return transazione.getImporto();
        } else {
            throw new InvalidOperationException("Transazione non trovata per l'ordine.");
        }
    }

    private void simulaRimborso(BigDecimal importo) {
        // Logica di esempio per simulare un rimborso
        System.out.println("Rimborso in corso. Importo: " + importo);

        // Simulazione di una probabilità di successo del 90%
        boolean rimborsoSuccesso = RANDOM.nextInt(100) < 90;

        if (rimborsoSuccesso) {
            System.out.println("Rimborso completato con successo!");
        } else {
            System.out.println("Rimborso fallito.");
            throw new InvalidOperationException("Il rimborso è fallito.");
        }
    }
}

