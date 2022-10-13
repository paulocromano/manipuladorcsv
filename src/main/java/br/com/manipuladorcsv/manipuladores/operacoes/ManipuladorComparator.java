package br.com.manipuladorcsv.manipuladores.operacoes;

import br.com.manipuladorcsv.manipuladores.enums.ModoOrdenacao;
import br.com.manipuladorcsv.manipuladores.model.OrdenacaoField;

import java.util.Comparator;
import java.util.List;

public interface ManipuladorComparator {

    Comparator<Object> getComparatorAntesDaOperacaoMap(List<OrdenacaoField> fieldsParaOrdenacaoField);

    Comparator<Object> getComparatorDepoisDaOperacaoMap(ModoOrdenacao modoOrdenacao);
}
