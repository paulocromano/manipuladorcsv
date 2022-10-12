package br.com.manipuladorcsv.manipuladores.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum TipoParametro {

    FILTER("filter"),
    MAP("map"),
    GROUPY_BY("groupBy"),
    ORDER_BY("orderBy");


    private String valor;
}
