package com.progettopswcp.ProgettoPSW.controllers;

import com.progettopswcp.ProgettoPSW.repositories.ProdottoRepository;
import com.progettopswcp.ProgettoPSW.support.ResponseMessage;
import com.progettopswcp.ProgettoPSW.resources.exceptions.BarCodeAlreadyExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.progettopswcp.ProgettoPSW.services.ProdottoService;
import com.progettopswcp.ProgettoPSW.entities.prodotto;
@CrossOrigin(
        origins = "http://localhost:4200",
        allowedHeaders = "*",
        methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE }
)
@RestController
@RequestMapping("/prodotto")
public class ProdottoController {
    @Autowired
    private ProdottoService prodottoService;
    private ProdottoRepository prodottoRepository;


    @PreAuthorize("hasRole('admin')")
    @PostMapping("/create")
    public ResponseEntity<?> aggiungiProdotto(@RequestBody prodotto prod) {
        try {
            prodottoService.aggiungiProdotto(prod);
            // Restituire un oggetto JSON
            Map<String, String> response = new HashMap<>();
            response.put("message", "Prodotto aggiunto con successo");
            return ResponseEntity.ok(response);
        } catch (BarCodeAlreadyExistException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Codice a barre già esistente");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
    }


    @PreAuthorize("hasAuthority('ROLE_default-roles-my-realm-test')")
    @GetMapping("/getall")
    public List<prodotto> getAll() {
        return prodottoService.mostraTuttiiprodotti();
    }


    @PreAuthorize("hasRole('admin')")
    @GetMapping("/paged")
    public ResponseEntity getAll(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize, @RequestParam(value = "sortBy", defaultValue = "id") String sortBy) {
        List<prodotto> ris = prodottoService.mostraTuttiiprodotti(pageNumber, pageSize, sortBy);
        if (ris.size() <= 0) {
            return new ResponseEntity<>(new ResponseMessage("Nessun Risultato!"), HttpStatus.OK);
        }
        return new ResponseEntity<>(ris, HttpStatus.OK);
    }


    @PreAuthorize("hasRole('admin')")
    @GetMapping("/search/by_name")
    public ResponseEntity getByName(@RequestParam(required = false) String nome) {
        List<prodotto> result = prodottoService.showProductByNome(nome);
        if (result.size() <= 0) {
            return new ResponseEntity<>(new ResponseMessage("Nessun Risultato!"), HttpStatus.OK);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }



}

