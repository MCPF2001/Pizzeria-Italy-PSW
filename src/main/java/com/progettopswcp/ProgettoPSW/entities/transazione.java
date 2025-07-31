package com.progettopswcp.ProgettoPSW.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalTime;

@Data
@Getter
@Setter
@Entity
@Table(name = "transazione")
public class transazione {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_transazione", nullable = false)
    private Integer id;

    @Column(name = "id_ordine", nullable = false)
    private int idOrdine;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ordine", insertable = false, updatable = false)
    private ordine ordine;

    @Column(name = "data", nullable = false)
    private Instant data;

    @Column(name = "ora", nullable = false)
    private LocalTime ora;

    @Column(name = "importo", nullable = false, precision = 50)
    private BigDecimal importo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "metodo_di_pagamento", nullable = false)
    private metododipagamento metodoDiPagamento;

    @Column(name = "esito", nullable = false)
    private boolean esito;

}