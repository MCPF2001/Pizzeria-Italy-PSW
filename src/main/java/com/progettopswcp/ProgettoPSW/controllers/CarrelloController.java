package com.progettopswcp.ProgettoPSW.controllers;

import com.progettopswcp.ProgettoPSW.dto.carrelloprodottoDTO;
import com.progettopswcp.ProgettoPSW.jwt.CustomJwt;
import com.progettopswcp.ProgettoPSW.repositories.CarrelloRepository;
import com.progettopswcp.ProgettoPSW.resources.exceptions.*;
import com.progettopswcp.ProgettoPSW.services.CarrelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/carrello")
public class CarrelloController {


    @Autowired
    private CarrelloService carrelloService;
    @Autowired
    private CarrelloRepository carrelloRepository;

    @CrossOrigin(
            origins = "http://localhost:4200",
            allowedHeaders = "*",
            methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE }
    )
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/aggiungi")
    public ResponseEntity<Map<String, String>> aggiungiAlCarrello(
            @RequestParam @NotNull @Positive int idProdotto,
            @RequestParam @NotNull @Positive int quantita) {

        var jwt = (CustomJwt) SecurityContextHolder.getContext().getAuthentication();
        String email = jwt.getName();

        try {
            carrelloService.aggiungiAlCarrello(email, idProdotto, quantita);

            return creaRisposta("Prodotto aggiunto al carrello con successo.", HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return creaRisposta("Utente non trovato.", HttpStatus.NOT_FOUND);
        } catch (ProductNotFoundException e) {
            return creaRisposta("Prodotto non trovato.", HttpStatus.NOT_FOUND);
        } catch (InvalidQuantityException e) {
            return creaRisposta(e.getMessage(), HttpStatus.BAD_REQUEST);
        } //catch (Exception e) {
           // e.printStackTrace(); // Log dell'errore per il debugging
           // return creaRisposta("Errore interno del server.", HttpStatus.INTERNAL_SERVER_ERROR);

    }


    /**
     * Crea una risposta standardizzata con il messaggio e il codice di stato.
     */
    private ResponseEntity<Map<String, String>> creaRisposta(String message, HttpStatus status) {
        Map<String, String> response = new HashMap<>();
        response.put("message", message);
        return ResponseEntity.status(status).body(response);
    }



    @PreAuthorize("hasRole('default-roles')")
    @DeleteMapping("/svuota")
    public ResponseEntity<Map<String, String>> svuotaTuttoCarrello() {
        var jwt = (CustomJwt) SecurityContextHolder.getContext().getAuthentication();
        String email = jwt.getName();

        try {
            carrelloService.svuotaCarrello(email);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Carrello svuotato con successo.");
            return ResponseEntity.ok(response);
        } catch (UserNotFoundException e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Utente non trovato.");
            return ResponseEntity.status(404).body(response);
        }
    }



    @DeleteMapping("/rimuovi")
    public ResponseEntity<Map<String, String>> rimuoviDalCarrello(
            @RequestParam @NotNull @Positive int idProdotto) {
        var jwt = (CustomJwt) SecurityContextHolder.getContext().getAuthentication();
        String email = jwt.getName();

        try {
            carrelloService.rimuoviDalCarrello(email, idProdotto);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Prodotto rimosso dal carrello con successo.");
            return ResponseEntity.ok(response);
        } catch (UserNotFoundException e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Utente non trovato.");
            return ResponseEntity.status(404).body(response);
        } catch (InvalidOperationException e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.status(400).body(response);
        }
    }

    // Diminuisce la quantità di un prodotto nel carrello
    @PutMapping("/minus")
    public ResponseEntity<Map<String, String>> decrementaprodottocarrello(
            @RequestParam @NotNull @Positive int idProdotto) {
        var jwt = (CustomJwt) SecurityContextHolder.getContext().getAuthentication();
        String email = jwt.getName();

        try {
            carrelloService.decrementaquantitaprodottocarrello(email, idProdotto);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Quantità ridotta con successo.");
            return ResponseEntity.ok(response);
        } catch (UserNotFoundException e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Utente non trovato.");
            return ResponseEntity.status(404).body(response);
        } catch (InvalidOperationException e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.status(400).body(response);
        }
    }




    @GetMapping("/items")
    public ResponseEntity<List<carrelloprodottoDTO>> getCartItems() {
        var jwt = (CustomJwt) SecurityContextHolder.getContext().getAuthentication();
        String email = jwt.getName();

        try {
            List<carrelloprodottoDTO> cartItems = carrelloService.getCartItemsByEmail(email);
            return ResponseEntity.ok(cartItems);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(404).body(Collections.emptyList());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Collections.emptyList());
        }
    }
    // Effettua un ordine
    @PostMapping("/ordina")
    public ResponseEntity<Map<String, String>> ordina(
            @RequestParam @NotNull @Min(1) int metodoPagamento,
            @RequestParam @NotNull String indirizzoSpedizione) {
        var jwt = (CustomJwt) SecurityContextHolder.getContext().getAuthentication();
        String email = jwt.getName();

        try {
            carrelloService.ordina(email, metodoPagamento, indirizzoSpedizione);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Ordine effettuato con successo.");
            return ResponseEntity.ok(response);
        } catch (UserNotFoundException e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Utente non trovato.");
            return ResponseEntity.status(404).body(response);
        } catch (InvalidOperationException e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.status(400).body(response);
        }
    }
    // Aumento la quantità di un prodotto nel carrello (plus adding)
    @PutMapping("/plus")
    public ResponseEntity<Map<String, String>> incrementaquantitaprodottocarrello(
            @RequestParam @NotNull @Positive int idProdotto) {
        var jwt = (CustomJwt) SecurityContextHolder.getContext().getAuthentication();
        String email = jwt.getName();
        try {
            carrelloService.incrementaquantitaprodottocarrello(email, idProdotto);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Quantità aumentata con successo.");
            return ResponseEntity.ok(response);
        } catch (UserNotFoundException e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Utente non trovato.");
            return ResponseEntity.status(404).body(response);
        } catch (ProductNotFoundException e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Prodotto non trovato.");
            return ResponseEntity.status(404).body(response);
        } catch (InvalidQuantityException e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.status(400).body(response);
        }
    }
}