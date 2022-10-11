package br.com.manipuladorcsv.manipuladores;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum OperadorLogico {

    AND("@"),
    OR("#"),
    NEGACAO("!"),
    IGUALDADE("=="),
    MENOR("<"),
    MENOR_IGUAL("<="),
    MAIOR(">"),
    MAIOR_IGUAL(">=");


    private String valor;
}
