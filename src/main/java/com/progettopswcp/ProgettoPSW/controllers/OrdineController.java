package com.progettopswcp.ProgettoPSW.controllers;

import com.progettopswcp.ProgettoPSW.dto.ordineDTO;
import com.progettopswcp.ProgettoPSW.entities.cliente;
import com.progettopswcp.ProgettoPSW.entities.ordine;
import com.progettopswcp.ProgettoPSW.jwt.CustomJwt;
import com.progettopswcp.ProgettoPSW.repositories.ClienteRepository;
import com.progettopswcp.ProgettoPSW.repositories.OrdineRepository;
import com.progettopswcp.ProgettoPSW.resources.exceptions.UserNotFoundException;
import com.progettopswcp.ProgettoPSW.services.OrdineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
@CrossOrigin(
        origins = "http://localhost:4200",
        allowedHeaders = "*",
        methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE }
)

@RestController
@RequestMapping("/ordine")
public class OrdineController {

    @Autowired
    private OrdineService ordineService;

    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private OrdineRepository ordineRepository;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{idOrdine}/annulla")
    public ResponseEntity<String> annullaOrdine(
            @PathVariable Integer idOrdine,
            @RequestParam String motivo) {

        var jwt = (CustomJwt) SecurityContextHolder.getContext().getAuthentication();
        String email = jwt.getName();
        cliente cliente = clienteRepository.findByEmail(email);
        ordine ordine = ordineRepository.findById(idOrdine).orElse(null);


        if (cliente == null || ordine == null) {
            return new ResponseEntity<>("Utente o Ordine non trovati.", HttpStatus.NOT_FOUND);
        }

        try {
            ordineService.annullaOrdine(cliente, ordine, motivo);
            return new ResponseEntity<>("Ordine annullato con successo.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Errore durante l'annullamento dell'ordine: " + e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        }
    }




    @PreAuthorize("isAuthenticated()")
    @GetMapping("/miei-ordini")
    public ResponseEntity<List<ordineDTO>> getMieiOrdini() {
        var jwt = (CustomJwt) SecurityContextHolder.getContext().getAuthentication();
        String email = jwt.getName();


        try {
            List<ordineDTO> ordini = ordineService.getOrdiniByEmail(email);
            return ResponseEntity.ok(ordini);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }
}
