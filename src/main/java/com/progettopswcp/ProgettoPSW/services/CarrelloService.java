package com.progettopswcp.ProgettoPSW.services;

import com.progettopswcp.ProgettoPSW.dto.carrelloprodottoDTO;
import com.progettopswcp.ProgettoPSW.entities.*;
import com.progettopswcp.ProgettoPSW.repositories.*;
import com.progettopswcp.ProgettoPSW.resources.exceptions.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;


@Service
public class CarrelloService {
    @Autowired
    private CarrelloRepository carrelloRepository;


    @Autowired
    private ClienteRepository  clienteRepository;

    @Autowired
    private ProdottoRepository prodottoRepository;


    @Autowired
    private OrdineRepository ordineRepository;

    @Autowired
    private transazioneRepository transazioneRepository;

    @Autowired
    private spedizioneRepository spedizioneRepository;

    @Autowired
    private prodottiordinatiRepository prodottiordinatiRepository;


    @Autowired
    private carrelloprodottoRepository carrelloprodottoRepository;

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private com.progettopswcp.ProgettoPSW.repositories.metododipagamentoRepository metododipagamentoRepository;

    private static final Random RANDOM = new Random();

    @Transactional
    public void aggiungiAlCarrello(String idUtente, int idProdotto, int quantita)
            throws UserNotFoundException, ProductNotFoundException, InvalidQuantityException {

        // Recupero l'utente dal database.
        cliente cliente = clienteRepository.findByEmail(idUtente);
        if (cliente == null) {
            throw new UserNotFoundException("Cliente non trovato!");
        }

        // Recupero o creo il carrello dell'utente
        carrello carrello = carrelloRepository.findByIdCliente(cliente.getIdCliente());
        if (carrello == null) {
            carrello = new carrello();
            carrello.setIdCliente(cliente.getIdCliente());
            carrello = carrelloRepository.save(carrello);
        } else {
            // Lock del carrello per evitare accessi concorrenti
            entityManager.lock(carrello, LockModeType.PESSIMISTIC_WRITE);
        }

        prodotto prod = prodottoRepository.findAll()  // Recuperi la lista completa dei prodotti
                .stream()  // Crei uno stream dalla lista
                .filter(p -> p.getId() == idProdotto)  // Filtro per idProdotto
                .findFirst()  // Trova il primo prodotto che soddisfa il filtro
                .orElseThrow(() -> new ProductNotFoundException("Prodotto con ID " + idProdotto + " non trovato"));

        // Verifico la disponibilità del prodotto
        int disponibilitaProd = prod.getQuantita();

        if (quantita > disponibilitaProd) {
            throw new InvalidQuantityException("Impossibile aggiungere al carrello: " +
                    "il prodotto non è disponibile per la quantità desiderata");
        }

        // Controllo se il prodotto è già presente nel carrello

        carrelloprodotto cp = carrelloprodottoRepository.findByCarrelloAndProdotto(carrello,prod);
        if (cp != null) {
            // Lock dell'elemento del carrello
            entityManager.lock(cp, LockModeType.PESSIMISTIC_WRITE);

            // Aggiorno la quantità
            int nuovaQuantita = cp.getQuantita() + quantita;
            if (nuovaQuantita <= prod.getQuantita()) {
                cp.setQuantita(nuovaQuantita);
                carrelloprodottoRepository.save(cp);
            } else {
                throw new InvalidQuantityException("Quantità totale superiore alla disponibilità del prodotto");
            }
        } else {
            // Aggiungo il prodotto al carrello
            carrelloprodotto aggiunta = new carrelloprodotto();
            aggiunta.setCarrello(carrello);
            aggiunta.setProdotto(prod);
            aggiunta.setProdottoId(prod.getId());
            aggiunta.setQuantita(quantita);
            aggiunta.setCarrelloId(carrello.getIdCarrello());

            carrelloprodottoRepository.save(aggiunta);
        }
    }

    @Transactional
    public void incrementaquantitaprodottocarrello(String email, int idProdotto)
            throws UserNotFoundException, ProductNotFoundException, InvalidQuantityException {

        cliente cliente = clienteRepository.findByEmail(email);
        if (cliente == null) {
            throw new UserNotFoundException("Cliente non trovato!");
        }

        int idUtente = cliente.getIdCliente();

        // Recupero e locko il carrello
        carrello carrello = carrelloRepository.findByIdCliente(idUtente);
        if (carrello == null) {
            throw new InvalidOperationException("Il carrello dell'utente non è stato trovato.");
        }
        entityManager.lock(carrello, LockModeType.PESSIMISTIC_WRITE);

        // Recupero e locko l'elemento del carrello
        carrelloprodotto cp = carrelloprodottoRepository.findByCarrelloAndProdottoId(carrello, idProdotto);
        if (cp == null) {
            throw new InvalidOperationException("Il prodotto non è presente nel carrello.");
        }
        entityManager.lock(cp, LockModeType.PESSIMISTIC_WRITE);


        prodotto prod = prodottoRepository.findAll()  // Recuperi la lista completa dei prodotti
                .stream()  // Crei uno stream dalla lista
                .filter(p -> p.getId() == idProdotto)  // Filtro per idProdotto
                .findFirst()  // Trova il primo prodotto che soddisfa il filtro
                .orElseThrow(() -> new ProductNotFoundException("Prodotto con ID " + idProdotto + " non trovato"));

        int disponibilitaProd = prod.getQuantita();

        if (cp.getQuantita() + 1 > disponibilitaProd) {
            throw new InvalidQuantityException("Impossibile aggiungere al carrello: " +
                    "il prodotto non è disponibile per la quantità desiderata");
        }
        cp.setQuantita(cp.getQuantita() + 1);
        carrelloprodottoRepository.save(cp);
    }

    @Transactional
    public void rimuoviDalCarrello(String email, int prodottoID)
            throws UserNotFoundException, InvalidOperationException {

        cliente cliente = clienteRepository.findByEmail(email);
        if (cliente == null) {
            throw new UserNotFoundException("Cliente non trovato!");
        }

        // Recupero e locko il carrello
        carrello carrello = carrelloRepository.findByIdCliente(cliente.getIdCliente());
        if (carrello == null) {
            throw new InvalidOperationException("Il carrello dell'utente non è stato trovato.");
        }
        entityManager.lock(carrello, LockModeType.PESSIMISTIC_WRITE);

        // Recupero e locko l'elemento del carrello
        carrelloprodotto cp = carrelloprodottoRepository.findByCarrelloAndProdottoId(carrello, prodottoID);
        if (cp == null) {
            throw new InvalidOperationException("Il prodotto non è presente nel carrello.");
        }
        entityManager.lock(cp, LockModeType.PESSIMISTIC_WRITE);

        // Rimuovo il prodotto dal carrello
        carrelloprodottoRepository.delete(cp);
    }

    @Transactional
    public void decrementaquantitaprodottocarrello(String email, int idProdotto)
            throws UserNotFoundException, InvalidOperationException {

        cliente cliente = clienteRepository.findByEmail(email);
        if (cliente == null) {
            throw new UserNotFoundException("Cliente non trovato!");
        }

        int idUtente = cliente.getIdCliente();

        // Recupero e locko il carrello
        carrello carrello = carrelloRepository.findByIdCliente(idUtente);
        if (carrello == null) {
            throw new InvalidOperationException("Il carrello dell'utente non è stato trovato.");
        }
        entityManager.lock(carrello, LockModeType.PESSIMISTIC_WRITE);

        // Recupero e locko l'elemento del carrello
        carrelloprodotto cp = carrelloprodottoRepository.findByCarrelloAndProdottoId(carrello, idProdotto);
        if (cp == null) {
            throw new InvalidOperationException("Il prodotto non è presente nel carrello.");
        }
        entityManager.lock(cp, LockModeType.PESSIMISTIC_WRITE);

        if (!cp.getCarrello().equals(carrello)) {
            throw new InvalidOperationException("Operazione non valida: il carrello non corrisponde");
        }

        // Decremento o rimuovo il prodotto dal carrello
        if (cp.getQuantita() > 1) {
            cp.setQuantita(cp.getQuantita() - 1);
            carrelloprodottoRepository.save(cp);
        } else {
            carrelloprodottoRepository.delete(cp);
        }
    }

    @Transactional
    public void svuotaCarrello(String email) throws UserNotFoundException {
        cliente cliente = clienteRepository.findByEmail(email);
        if (cliente == null) {
            throw new UserNotFoundException("Cliente non trovato!");
        }

        // Recupero e locko il carrello
        carrello carrello = carrelloRepository.findByIdCliente(cliente.getIdCliente());
        if (carrello == null) {
            throw new InvalidOperationException("Il carrello dell'utente non è stato trovato.");
        }
        entityManager.lock(carrello, LockModeType.PESSIMISTIC_WRITE);

        // Recupero e locko tutti gli elementi del carrello
        Set<carrelloprodotto> cartProducts = carrelloprodottoRepository.findByCarrelloId(carrello.getIdCarrello());
        for (carrelloprodotto cp : cartProducts) {
            entityManager.lock(cp, LockModeType.PESSIMISTIC_WRITE);
        }

        // Svuoto il carrello
        carrelloprodottoRepository.deleteAllByCarrello(carrello);
    }

    @Transactional
    public void ordina(String email, int metodoDiPagamento, String indirizzoSpedizione)
            throws UserNotFoundException, InvalidOperationException {

        cliente cliente = clienteRepository.findByEmail(email);
        if (cliente == null) {
            throw new UserNotFoundException("Cliente non trovato!");
        }
        int idCliente = cliente.getIdCliente();

        // Recupero e locko il carrello
        carrello carrello = carrelloRepository.findByIdCliente(cliente.getIdCliente());
        if (carrello == null) {
            throw new InvalidOperationException("Il carrello dell'utente non è stato trovato.");
        }
        entityManager.lock(carrello, LockModeType.PESSIMISTIC_WRITE);

        // Recupero e locko gli elementi del carrello
        Set<carrelloprodotto> prodottiUser = carrelloprodottoRepository.findByCarrelloId(carrello.getIdCarrello());
        if (prodottiUser.isEmpty()) {
            throw new InvalidOperationException("Il carrello è vuoto. Aggiungi prodotti prima di procedere all'ordine.");
        }

        // Ordino gli elementi per evitare deadlock
        List<carrelloprodotto> prodottiUserList = new ArrayList<>(prodottiUser);
        prodottiUserList.sort(Comparator.comparingInt(carrelloprodotto::getProdottoId));

        // Verifico la disponibilità e locko i prodotti
        for (carrelloprodotto cp : prodottiUserList) {
            // Lock dell'elemento del carrello
            entityManager.lock(cp, LockModeType.PESSIMISTIC_WRITE);

            prodotto prodotto = prodottoRepository.findById(cp.getProdottoId())
                    .orElseThrow(() -> new InvalidOperationException("Prodotto non trovato"));
            // Lock del prodotto
            entityManager.lock(prodotto, LockModeType.PESSIMISTIC_WRITE);

            // Verifico la disponibilità
            if (prodotto.getQuantita() < cp.getQuantita()) {
                throw new InvalidOperationException("La quantità del prodotto '" + prodotto.getNome() + "' non è sufficiente per completare l'ordine.");
            }

            // Decremento la disponibilità del prodotto
            prodotto.setQuantita(prodotto.getQuantita() - cp.getQuantita());
            prodottoRepository.save(prodotto);
        }

        // Creazione dell'ordine e della transazione
        ordine ordine = new ordine();
        transazione transazione = new transazione();

        metododipagamento met = metododipagamentoRepository.findById(metodoDiPagamento);
        if (met == null) {
            throw new InvalidOperationException("Metodo di pagamento non trovato");
        }

        ordine.setId_carrello(carrello.getIdCarrello());
        ordine.setIdCliente(cliente.getIdCliente());
        ordine.setOra(LocalTime.now());
        ordine.setData(LocalDateTime.now());
        ordine.setStato("Processamento in corso...");
        ordineRepository.save(ordine);

        transazione.setMetodoDiPagamento(met);
        transazione.setIdOrdine(ordine.getIdOrdine());
        transazione.setOra(LocalTime.now());
        transazione.setData(Instant.now());
        transazione.setImporto(calcolaImporto(prodottiUser));

        spedizione spedizione = new spedizione();
        spedizione.setIdOrdine(ordine.getIdOrdine());
        spedizione.setIndirizzoSpedizione(indirizzoSpedizione);
        spedizione.setDataPrevista(Instant.now().plus(7, ChronoUnit.DAYS));
        spedizione.setStato("In corso...");

        boolean esitoPagamento = processaPagamento(met, transazione.getImporto());

        if (esitoPagamento) {
            transazione.setEsito(true);
            ordine.setStato("Pagamento completato");
            svuotaCarrello(email);

            // Salvo i prodotti ordinati
            for (carrelloprodotto cp : prodottiUserList) {
                prodottiordinati po = new prodottiordinati();
                po.setIdProdotto(cp.getProdottoId());
                po.setIdUtente(cliente.getIdCliente());
                po.setIdOrdine(ordine.getIdOrdine());
                po.setQuantita(cp.getQuantita());
                prodottiordinatiRepository.save(po);
            }

        } else {
            transazione.setEsito(false);
            ordine.setStato("Pagamento fallito");

            // Ripristino la quantità dei prodotti
            for (carrelloprodotto cp : prodottiUserList) {
                prodotto prodotto = prodottoRepository.findById(cp.getProdottoId())
                        .orElseThrow(() -> new InvalidOperationException("Prodotto non trovato"));
                // Lock del prodotto
                entityManager.lock(prodotto, LockModeType.PESSIMISTIC_WRITE);

                prodotto.setQuantita(prodotto.getQuantita() + cp.getQuantita());
                prodottoRepository.save(prodotto);
            }

            throw new InvalidOperationException("Il pagamento è fallito. Riprovare.");
        }

        // Salvo le transazioni e la spedizione
        transazioneRepository.save(transazione);
        spedizioneRepository.save(spedizione);
        ordineRepository.save(ordine);
    }

    private static BigDecimal calcolaImporto(Set<carrelloprodotto> prodottiUser) {
        BigDecimal totale = BigDecimal.ZERO;

        if (prodottiUser == null || prodottiUser.isEmpty()) {
            return totale;
        }

        for (carrelloprodotto cp : prodottiUser) {
            prodotto prodotto = cp.getProdotto();
            BigDecimal prezzo = prodotto.getPrezzo();
            int quantita = cp.getQuantita();

            if (prezzo == null || prezzo.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Prezzo del prodotto " + prodotto.getNome() + " non valido.");
            }
            if (quantita <= 0) {
                throw new IllegalArgumentException("Quantità del prodotto " + prodotto.getNome() + " non valida.");
            }

            BigDecimal costoProdotto = prezzo.multiply(BigDecimal.valueOf(quantita));
            totale = totale.add(costoProdotto);
        }

        return totale;
    }

    private boolean processaPagamento(metododipagamento metodoDiPagamento, BigDecimal amount) {

        if (metodoDiPagamento == null) {
            System.out.println("Errore: nessun metodo di pagamento selezionato.");
            return false;
        }

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("Errore: importo non valido. Valore importo: " + amount);
            return false;
        }

        System.out.println("Pagamento in corso con il metodo selezionato: " + metodoDiPagamento.getSelezione());
        System.out.println("Importo da pagare: " + amount);

        boolean pagamentoRiuscito = RANDOM.nextInt(100) < 80;

        if (pagamentoRiuscito) {
            System.out.println("Pagamento effettuato con successo!");
            return true;
        } else {
            System.out.println("Pagamento fallito.");
            return false;
        }
    }

    @Transactional(readOnly = true)
    public List<carrelloprodottoDTO> getCartItemsByEmail(String email) throws UserNotFoundException {

        cliente cliente = clienteRepository.findByEmail(email);
        if (cliente == null) {
            throw new UserNotFoundException("Cliente non trovato!");
        }

        carrello carrello = carrelloRepository.findByIdCliente(cliente.getIdCliente());
        if (carrello == null) {
            throw new InvalidOperationException("Il carrello dell'utente non è stato trovato.");
        }

        Set<carrelloprodotto> prodottiUser = carrelloprodottoRepository.findByCarrelloId(carrello.getIdCarrello());

        List<carrelloprodottoDTO> cartItems = new ArrayList<>();

        for (carrelloprodotto cp : prodottiUser) {
            prodotto prodotto = cp.getProdotto();

            carrelloprodottoDTO dto = new carrelloprodottoDTO();
            dto.setIdProdotto(prodotto.getId());
            dto.setNomeProdotto(prodotto.getNome());
            dto.setPrezzoProdotto(prodotto.getPrezzo());
            dto.setQuantita(cp.getQuantita());

            cartItems.add(dto);
        }

        return cartItems;
    }
}
