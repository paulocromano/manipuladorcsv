package br.com.manipuladorcsv.manipuladores.operacoes;

import br.com.manipuladorcsv.manipuladores.enums.ModoOrdenacao;
import br.com.manipuladorcsv.manipuladores.model.OrdenacaoField;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class ManipuladorComparatorImpl implements ManipuladorComparator {

    @Override
    public Comparator<Object> getComparatorAntesDaOperacaoMap(List<OrdenacaoField> fieldsParaOrdenacaoField) {
        Comparator<Object> comparator = null;

        for (OrdenacaoField fieldParaOrdenacao : fieldsParaOrdenacaoField) {
            Comparator<Object> comparatorFieldAtual = gerarComparatorDeAcordoComTipoField(fieldParaOrdenacao);

            comparator = (Objects.isNull(comparator))
                ? comparatorFieldAtual
                : comparator.thenComparing(comparatorFieldAtual);

            if (fieldParaOrdenacao.getModoOrdenacao().equals(ModoOrdenacao.DESCENTEDENTE)) comparator = comparator.reversed();
        }

        if (Objects.isNull(comparator)) throw new IllegalStateException("O Comparator não pode estar nulo!");

        return comparator;
    }

    private Comparator<Object> gerarComparatorDeAcordoComTipoField(OrdenacaoField ordenacaoField) {
        Field field = ordenacaoField.getField();
        Function<Object, ? extends Comparable<Object>> functionComparator = object -> {
            try {
                return (Comparable<Object>) field.get(object);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        };

        if (field.getType().getSuperclass().equals(Number.class) || field.getType().equals(String.class)) {
            return Comparator.comparing(functionComparator);
        }

        throw new IllegalStateException("Comparator não implementado!");
    }

    @Override
    public Comparator<Object> getComparatorDepoisDaOperacaoMap(ModoOrdenacao modoOrdenacao) {
        Comparator<Object> comparator = Comparator.comparing(objeto -> (Comparable<Object>) objeto);
        if (modoOrdenacao.equals(ModoOrdenacao.DESCENTEDENTE)) comparator = comparator.reversed();

        return comparator;
    }
}
