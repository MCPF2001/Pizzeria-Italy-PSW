package com.progettopswcp.ProgettoPSW.entities;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Setter
@Getter
public class carrelloprodottoid implements Serializable {

    private Integer carrelloId;
    private Integer prodottoId;
}
