package com.progettopswcp.ProgettoPSW.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ordineDTO {
    private Integer idOrdine;
    private LocalDateTime data;
    private LocalTime ora;
    private String stato;
    private BigDecimal totaleOrdine;
    private List<prodottoordinatoDTO> prodotti;
}
