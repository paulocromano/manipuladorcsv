package br.com.manipuladorcsv.manipuladores;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum TipoParametro {

    FILTER("filter"),
    MAP("map"),
    GROUPY_BY("groupBy");


    private String valor;
}
