package com.progettopswcp.ProgettoPSW.services;

import com.progettopswcp.ProgettoPSW.entities.cliente;
import com.progettopswcp.ProgettoPSW.repositories.ClienteRepository;
import com.progettopswcp.ProgettoPSW.resources.exceptions.MailUserAlreadyExistsException;
import com.progettopswcp.ProgettoPSW.resources.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.*;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class ClienteService {
    @Autowired
    private ClienteRepository clienteRepository;


    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public cliente registraCliente(cliente c) throws MailUserAlreadyExistsException {
        if (clienteRepository.existsByEmail(c.getEmail()) ) {
            throw new MailUserAlreadyExistsException();
        }
        return clienteRepository.save(c);
   }

    @Transactional(readOnly = true)
    public List<cliente> getcliente(int id) throws UserNotFoundException {
        if(!clienteRepository.existsByIdCliente(id))
            throw new UserNotFoundException("L´utente non esiste!");
        return clienteRepository.findByIdCliente(id);
    }



    @Transactional(readOnly = true)
     public List<cliente> getAllUsers() {
       return clienteRepository.findAll();
    }
    @Transactional(readOnly = true)
    public List<cliente> getclienteBynome(String nome){
        return clienteRepository.findByNome(nome);
    }

    @Transactional(readOnly = true)
    public List<cliente> getClienti(){
        return clienteRepository.findAll();
    }



}


