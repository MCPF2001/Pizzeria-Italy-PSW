package com.progettopswcp.ProgettoPSW.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import java.time.Instant;

@Data
@Getter
@Setter
@Entity
@Table(name = "spedizione", uniqueConstraints = {
        @UniqueConstraint(name = "id_ordine", columnNames = {"id_ordine"})
})
public class spedizione {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_spedizione", nullable = false)
    private Integer id;

    @Column(name = "id_ordine", nullable = false)
    private int idOrdine;

    @Column(name = "indirizzo_spedizione", nullable = false, length = 50)
    private String indirizzoSpedizione;

    @Column(name = "data_prevista", nullable = false)
    private Instant dataPrevista;

    @Column(name = "stato", nullable = false, length = 50)
    private String stato;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ordine", insertable = false, updatable = false)
    private ordine ordine;
}