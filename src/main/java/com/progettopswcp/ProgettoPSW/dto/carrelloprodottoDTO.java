package com.progettopswcp.ProgettoPSW.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Data
@Getter
@Setter
public class carrelloprodottoDTO {
        private int idProdotto;
        private String nomeProdotto;
        private BigDecimal prezzoProdotto;
        private int quantita;

    }