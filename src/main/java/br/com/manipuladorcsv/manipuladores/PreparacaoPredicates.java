package br.com.manipuladorcsv.manipuladores;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PreparacaoPredicates {

    public static <E> Map<String, Field> buscarFieldsDaClasse(Class<E> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .peek(field -> field.setAccessible(true))
                .collect(Collectors.toMap(Field::getName, Function.identity()));
    }
}
