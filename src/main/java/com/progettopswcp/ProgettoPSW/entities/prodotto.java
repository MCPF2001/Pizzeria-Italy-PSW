package com.progettopswcp.ProgettoPSW.entities;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "prodotto",schema="public")
public class prodotto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_prodotto", nullable = false,length = 10)
    private int id;

    @Basic
    @Column(name = "prezzo", nullable = true, precision = 10, scale = 2)
    private BigDecimal prezzo;


    @Basic
    @Column(name = "nome_prodotto", nullable = true , length = 50)
    private String nome;


    @JsonProperty("TipoProdotto")
    @Column(name = "tipo_prodotto",length = 100)
    private String TipoProdotto;



    @Column(name = "quantita")
    private Integer quantita;



    @Column(name = "disponibilita",nullable = false)
    private Boolean disponibilita;

}




