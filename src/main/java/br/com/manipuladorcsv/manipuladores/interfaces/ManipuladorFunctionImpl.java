package br.com.manipuladorcsv.manipuladores.interfaces;

import java.lang.reflect.Field;
import java.util.function.Function;

public class ManipuladorFunctionImpl<E> implements ManipuladorFunction<E> {

    @Override
    public Function<E, Object> gerarFunction(Field field) {
        return objectFunction -> {
            try {
                return field.get(objectFunction);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        };
    }
}
