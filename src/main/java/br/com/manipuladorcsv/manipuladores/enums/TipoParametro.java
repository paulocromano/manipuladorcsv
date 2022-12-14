package br.com.manipuladorcsv.manipuladores.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum TipoParametro {

    FILTER("filter", false),
    MAP("map", false),
    ORDER_BY("orderBy", false),
    GROUPY_BY("groupBy", true);


    private String valor;
    private Boolean operacaoTerminal;
}
