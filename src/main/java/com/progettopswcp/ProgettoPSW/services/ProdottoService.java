package com.progettopswcp.ProgettoPSW.services;

import com.progettopswcp.ProgettoPSW.repositories.ProdottoRepository;
import com.progettopswcp.ProgettoPSW.resources.exceptions.BarCodeAlreadyExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import com.progettopswcp.ProgettoPSW.entities.prodotto;


@Service
public class ProdottoService {
    @Autowired
    private ProdottoRepository prodottoRepository;


    @Transactional
    public void aggiungiProdotto(prodotto prod) throws BarCodeAlreadyExistException {
        if (prod.getId() != 0 && prodottoRepository.existsById(prod.getId())) {
            throw new BarCodeAlreadyExistException();
        }


        if (prod.getDisponibilita() == null) {
            prod.setDisponibilita(false);
        }

        prodottoRepository.save(prod);
    }

    @Transactional(readOnly = true)
    public List<prodotto> mostraTuttiiprodotti() {

        return prodottoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<prodotto> mostraTuttiiprodotti(int pageNumber, int pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        Page<prodotto> ris = prodottoRepository.findAll(paging);
        if ( ris.hasContent() ) {
            return ris.getContent();
        }
        else {
            return new ArrayList<>();
        }
    }

    @Transactional(readOnly = true)
    public List<prodotto> showProductByNome(String nome) {
        return prodottoRepository.findByNome(nome);
    }

    @Transactional(readOnly = true)
    public List<prodotto> showProductById(int id) {

        return prodottoRepository.findById(id);
    }


}

