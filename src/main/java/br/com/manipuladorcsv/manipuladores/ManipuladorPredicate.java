package br.com.manipuladorcsv.manipuladores;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public interface ManipuladorPredicate<E> {

    Predicate<E> gerarPredicate(Field field, OperadorLogico operadorLogico, Object object);
}
