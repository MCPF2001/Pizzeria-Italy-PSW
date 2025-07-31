package com.progettopswcp.ProgettoPSW.controllers;


import com.progettopswcp.ProgettoPSW.entities.cliente;
import com.progettopswcp.ProgettoPSW.entities.prodotto;
import com.progettopswcp.ProgettoPSW.jwt.CustomJwt;
import com.progettopswcp.ProgettoPSW.repositories.ClienteRepository;
import com.progettopswcp.ProgettoPSW.repositories.ProdottoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(
        origins = "http://localhost:4200",
        allowedHeaders = "*",
        methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE }
)

@RequestMapping("")
public class HomeController {
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private ProdottoRepository prodottoRepository;


    @GetMapping("/home")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, String>> home() {
        var jwt = (CustomJwt) SecurityContextHolder.getContext().getAuthentication();
        cliente loggato = clienteRepository.findByEmail(jwt.getName());
        String email = jwt.getName();
        if (loggato == null) {
            cliente save = new cliente();
            save.setEmail(email);
            save.setNome(jwt.getFirstname());
            save.setCognome(jwt.getLastname());
            clienteRepository.save(save);
        }
        Map<String, String> response = new HashMap<>();
        response.put("message", "Utente loggato correttamente e presente nel database");
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('admin')")
    @GetMapping("/prodotti")
    public ResponseEntity<List<prodotto>> getAllProdotti() {
        List<prodotto> prodotti = prodottoRepository.findAll();
        return ResponseEntity.ok(prodotti);
    }

    @PostMapping("/aggiungi")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<Map<String, String>> aggiungiProdotto(@RequestBody prodotto prodotto) {
        try {
            prodottoRepository.save(prodotto);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Prodotto aggiunto con successo.");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Errore nell'aggiunta del prodotto.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}

