package com.progettopswcp.ProgettoPSW.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Data
@Getter
@Setter
public class prodottoordinatoDTO {
    private Integer idProdotto;
    private String nome;
    private BigDecimal prezzo;
    private Integer quantita;


}