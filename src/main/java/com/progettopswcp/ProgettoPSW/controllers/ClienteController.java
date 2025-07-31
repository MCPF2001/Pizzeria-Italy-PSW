package com.progettopswcp.ProgettoPSW.controllers;

import com.progettopswcp.ProgettoPSW.entities.cliente;
import com.progettopswcp.ProgettoPSW.repositories.ClienteRepository;
import com.progettopswcp.ProgettoPSW.resources.exceptions.MailUserAlreadyExistsException;
import com.progettopswcp.ProgettoPSW.resources.exceptions.UserNotFoundException;
import com.progettopswcp.ProgettoPSW.support.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.progettopswcp.ProgettoPSW.services.ClienteService;
import javax.validation.Valid;


@RestController
@CrossOrigin(origins = {"http://localhost:4200"})
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ClienteRepository clienteRepository;

    @PreAuthorize(value="hasRole('admin')")
    @GetMapping(value="/searchid/{id}")
    public @ResponseBody List<cliente> readById(@PathVariable(value = "id") int id) throws UserNotFoundException {
        return clienteService.getcliente(id);
    }

    @PreAuthorize(value="hasRole('admin')")
    @GetMapping(value="/searchname/{name}")
    public @ResponseBody List<cliente> readByName(@PathVariable(value = "name") String name) {
        return clienteService.getclienteBynome(name);
    }

    @PreAuthorize("hasRole('admin')")
    @GetMapping("/all")
    public @ResponseBody List<cliente> readAll(){
        return clienteService.getClienti();
    }


    @GetMapping("/me")
    public ResponseEntity<cliente> getCurrentUser(Authentication authentication) {
        String email = authentication.getName();
        cliente currentUser = clienteRepository.findByEmail(email);
        if (currentUser != null) {
            return ResponseEntity.ok(currentUser);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }



    @PutMapping("/update")
    public ResponseEntity<cliente> updateCurrentUser(@RequestBody cliente updatedUser, Authentication authentication) {
        String email = authentication.getName();
        cliente currentUser = clienteRepository.findByEmail(email);
        if (currentUser != null) {
            currentUser.setTelefono(updatedUser.getTelefono());
            clienteRepository.save(currentUser);
            return ResponseEntity.ok(currentUser);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    @PostMapping("/creacliente")
    public ResponseEntity create(@RequestBody @Valid cliente cliente) {
        try {
            cliente aggiungi = clienteService.registraCliente(cliente);
            return new ResponseEntity(aggiungi, HttpStatus.OK);
        } catch (MailUserAlreadyExistsException e) {
            return new ResponseEntity<>(new ResponseMessage("ERROR_MAIL_USER_ALREADY_EXISTS"), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/visualizzatutti")
    public List<cliente> getAll() {
        return clienteService.getAllUsers();
    }




}

