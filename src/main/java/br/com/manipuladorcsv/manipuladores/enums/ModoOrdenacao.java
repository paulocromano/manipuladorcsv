package br.com.manipuladorcsv.manipuladores.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ModoOrdenacao {

    ASCENDENTE("asc"),
    DESCENTEDENTE("desc");


    private String valor;
}
