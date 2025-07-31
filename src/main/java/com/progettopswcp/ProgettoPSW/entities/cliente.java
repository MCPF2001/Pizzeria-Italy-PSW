package com.progettopswcp.ProgettoPSW.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@Entity
@Table(name = "cliente", schema = "public")
public class cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente", nullable = false)
    private Integer idCliente;

    @Basic
    @Column(name = "nome", nullable = true, length = 50)
    private String nome;

    @Column(name = "cognome")
    private String cognome;



    @Basic
    @Column(name = "telefono", nullable = true, length = 50)
    private String telefono;


    @Basic
    @Column(name= "email",nullable = false, length = 50)
    private String email;

    @Basic
    @Column(name = "indirizzo", nullable = true, length = 255)
    private String indirizzo;






}