package br.com.manipuladorcsv.manipuladores.operacoes;

import java.lang.reflect.Field;
import java.util.function.Function;

public interface ManipuladorFunction<E> {

    Function<E, Object> gerarFunction(Field field);
}
