package com.progettopswcp.ProgettoPSW.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "carrello", schema = "public")
public class carrello {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_carrello", nullable = false)
    private int idCarrello;


    @JoinColumn(name = "id_cliente", nullable = false)
    private int idCliente;



    @Basic
    @Column(name = "data", nullable = true,length = 50)
    private String Data;



    @Column(name ="totalecarrello", precision = 10, scale = 2)
    private BigDecimal totaleCarrello = BigDecimal.ZERO;

    @OneToMany(mappedBy = "carrello", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<carrelloprodotto> carrelloProdottos = new LinkedHashSet<>();







}
