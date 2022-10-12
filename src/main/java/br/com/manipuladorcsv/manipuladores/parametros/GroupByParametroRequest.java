package br.com.manipuladorcsv.manipuladores.parametros;

import br.com.manipuladorcsv.manipuladores.interfaces.ManipuladorParametroRequest;
import br.com.manipuladorcsv.manipuladores.operacoes.ManipuladorFunction;
import br.com.manipuladorcsv.manipuladores.operacoes.ManipuladorFunctionImpl;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class GroupByParametroRequest<E> implements ParametroRequest<E, Collector<E, ?, Map<Object, Collection<E>>>> {

    private ManipuladorParametroRequest<E> manipuladorParametroRequest;
    private String conteudoParametro;
    private ManipuladorFunction<E> manipuladorFunction;

    public GroupByParametroRequest() {
        this.manipuladorFunction = new ManipuladorFunctionImpl<>();
    }

    @Override
    public void setManipuladorParametroRequest(ManipuladorParametroRequest<E> manipuladorParametroRequest) {
        this.manipuladorParametroRequest = manipuladorParametroRequest;
    }

    @Override
    public void setConteudoParametro(String conteudoParametro) {
        this.conteudoParametro = conteudoParametro;
    }

    @Override
    public Collector<E, ?, Map<Object, Collection<E>>> getOperacao() {
        Objects.requireNonNull(manipuladorParametroRequest);
        Objects.requireNonNull(conteudoParametro);
        Field field = validarField();
        Function<E, Object> function = this.manipuladorFunction.gerarFunction(field);

        return Collectors.groupingBy(function, Collectors.toCollection(LinkedList::new));
    }

    private Field validarField() {
        final Optional<Field> fieldOptional = this.manipuladorParametroRequest.getFields()
                .stream()
                .filter(field -> field.getName().equals(this.conteudoParametro))
                .findFirst();

        if (fieldOptional.isEmpty()) throw new RuntimeException("Nome do field não encontrado -> " + this.conteudoParametro);

        return fieldOptional.get();
    }
}
