package br.com.manipuladorcsv.manipuladores.model;

import br.com.manipuladorcsv.manipuladores.enums.ModoOrdenacao;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.Field;

@Getter
@AllArgsConstructor
public class OrdenacaoField {

    private Field field;
    private ModoOrdenacao modoOrdenacao;
}
