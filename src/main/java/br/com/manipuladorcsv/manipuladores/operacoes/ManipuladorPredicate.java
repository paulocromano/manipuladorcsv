package br.com.manipuladorcsv.manipuladores.operacoes;

import br.com.manipuladorcsv.manipuladores.enums.OperadorLogico;

import java.lang.reflect.Field;
import java.util.function.Predicate;

public interface ManipuladorPredicate<E> {

    Predicate<E> gerarPredicate(Field field, OperadorLogico operadorLogico, Object object);
}
