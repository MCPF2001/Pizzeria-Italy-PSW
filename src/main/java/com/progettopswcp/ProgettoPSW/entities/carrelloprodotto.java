package com.progettopswcp.ProgettoPSW.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
@Entity
@Table(name = "carrello_prodotto")
@IdClass(carrelloprodottoid.class)
public class carrelloprodotto {

    @Id
    @Column(name = "carrello_id")
    private Integer carrelloId;

    @Id
    @Column(name = "prodotto_id")
    private Integer prodottoId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JsonIgnore
    @JoinColumn(name = "carrello_id", insertable = false, updatable = false)
    private carrello carrello;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "prodotto_id", insertable = false, updatable = false)
    @JsonIgnore
    private prodotto prodotto;

    @Column(name = "quantita")
    private Integer quantita;

    public String getId() {
        return carrelloId + "-" + prodottoId;
    }
}