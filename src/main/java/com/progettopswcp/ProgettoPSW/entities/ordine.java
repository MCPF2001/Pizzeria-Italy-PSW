package com.progettopswcp.ProgettoPSW.entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;


@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "ordine", schema = "public")
public class ordine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ordine", nullable = false)
    private int idOrdine;


    @Column(name = "id_cliente", nullable = false)
    private int idCliente;

    @Basic
    @Column(name = "data", nullable = true,length = 50)
    private LocalDateTime data = LocalDateTime.now();



    @Basic
    @Column(name = "totale_ordine", nullable = true)
    private BigDecimal TotaleOrdine;

    @Basic
    @Column(name = "id_carrello", nullable = true)
    private float id_carrello;


    @Basic
    @Column(name = "ora", nullable = true,length = 50)
    private LocalTime ora;


    @OneToOne(mappedBy = "ordine")
    private spedizione spedizione;

    @Column(name = "stato", nullable = false, length = Integer.MAX_VALUE)
    private String stato;

    @OneToOne(mappedBy = "ordine")
    private transazione transaziones;


}



